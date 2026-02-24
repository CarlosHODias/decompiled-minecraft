/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ChunkLevel;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.Ticket;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TicketStorage
/*     */   extends SavedData
/*     */ {
/*     */   private static final int INITIAL_TICKET_LIST_CAPACITY = 4;
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private static final Codec<Pair<ChunkPos, Ticket>> TICKET_ENTRY = Codec.mapPair(
/*  44 */       ChunkPos.CODEC.fieldOf("chunk_pos"), Ticket.CODEC)
/*     */     
/*  46 */     .codec(); public static final Codec<TicketStorage> CODEC;
/*     */   static {
/*  48 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TICKET_ENTRY.listOf().optionalFieldOf("tickets", List.of()).forGetter(TicketStorage::packTickets)).apply((Applicative)i, TicketStorage::fromPacked));
/*     */   }
/*     */ 
/*     */   
/*  52 */   public static final SavedDataType<TicketStorage> TYPE = new SavedDataType("chunks", TicketStorage::new, CODEC, DataFixTypes.SAVED_DATA_FORCED_CHUNKS);
/*     */   
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> tickets;
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets;
/*  56 */   private LongSet chunksWithForcedTickets = (LongSet)new LongOpenHashSet();
/*     */   private ChunkUpdated loadingChunkUpdatedListener;
/*     */   private ChunkUpdated simulationChunkUpdatedListener;
/*     */   
/*     */   private TicketStorage(Long2ObjectOpenHashMap<List<Ticket>> tickets, Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets) {
/*  61 */     this.tickets = tickets;
/*  62 */     this.deactivatedTickets = deactivatedTickets;
/*  63 */     updateForcedChunks();
/*     */   }
/*     */   
/*     */   public TicketStorage() {
/*  67 */     this(new Long2ObjectOpenHashMap(4), new Long2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   private static TicketStorage fromPacked(List<Pair<ChunkPos, Ticket>> tickets) {
/*  71 */     Long2ObjectOpenHashMap<List<Ticket>> ticketsToLoad = new Long2ObjectOpenHashMap();
/*  72 */     for (Pair<ChunkPos, Ticket> ticket : tickets) {
/*  73 */       ChunkPos pos = (ChunkPos)ticket.getFirst();
/*  74 */       List<Ticket> ticketsInChunk = (List<Ticket>)ticketsToLoad.computeIfAbsent(pos.toLong(), k -> new ObjectArrayList(4));
/*  75 */       ticketsInChunk.add((Ticket)ticket.getSecond());
/*     */     } 
/*     */     
/*  78 */     return new TicketStorage(new Long2ObjectOpenHashMap(4), ticketsToLoad);
/*     */   }
/*     */   
/*     */   private List<Pair<ChunkPos, Ticket>> packTickets() {
/*  82 */     List<Pair<ChunkPos, Ticket>> tickets = new ArrayList<>();
/*  83 */     forEachTicket((pos, ticket) -> {
/*     */           if (ticket.getType().persist()) {
/*     */             tickets.add(new Pair(pos, ticket));
/*     */           }
/*     */         });
/*  88 */     return tickets;
/*     */   }
/*     */   
/*     */   private void forEachTicket(BiConsumer<ChunkPos, Ticket> output) {
/*  92 */     forEachTicket(output, this.tickets);
/*  93 */     forEachTicket(output, this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   private static void forEachTicket(BiConsumer<ChunkPos, Ticket> output, Long2ObjectOpenHashMap<List<Ticket>> tickets) {
/*  97 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)tickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/*  98 */       ChunkPos chunkPos = new ChunkPos(entry.getLongKey());
/*  99 */       for (Ticket ticket : (Iterable<Ticket>)entry.getValue()) {
/* 100 */         output.accept(chunkPos, ticket);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public void activateAllDeactivatedTickets() {
/* 106 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)this.deactivatedTickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 107 */       for (Ticket ticket : (Iterable<Ticket>)entry.getValue()) {
/* 108 */         addTicket(entry.getLongKey(), ticket);
/*     */       } }
/*     */     
/* 111 */     this.deactivatedTickets.clear();
/*     */   }
/*     */   
/*     */   public void setLoadingChunkUpdatedListener(ChunkUpdated loadingChunkUpdatedListener) {
/* 115 */     this.loadingChunkUpdatedListener = loadingChunkUpdatedListener;
/*     */   }
/*     */   
/*     */   public void setSimulationChunkUpdatedListener(ChunkUpdated simulationChunkUpdatedListener) {
/* 119 */     this.simulationChunkUpdatedListener = simulationChunkUpdatedListener;
/*     */   }
/*     */   
/*     */   public boolean hasTickets() {
/* 123 */     return !this.tickets.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean shouldKeepDimensionActive() {
/* 127 */     for (ObjectIterator<List<Ticket>> objectIterator = this.tickets.values().iterator(); objectIterator.hasNext(); ) { List<Ticket> group = objectIterator.next();
/* 128 */       for (Ticket ticket : group) {
/* 129 */         if (ticket.getType().shouldKeepDimensionActive()) {
/* 130 */           return true;
/*     */         }
/*     */       }  }
/*     */     
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public List<Ticket> getTickets(long key) {
/* 138 */     return (List<Ticket>)this.tickets.getOrDefault(key, List.of());
/*     */   }
/*     */   
/*     */   private List<Ticket> getOrCreateTickets(long key) {
/* 142 */     return (List<Ticket>)this.tickets.computeIfAbsent(key, k -> new ObjectArrayList(4));
/*     */   }
/*     */   
/*     */   public void addTicketWithRadius(TicketType type, ChunkPos chunkPos, int radius) {
/* 146 */     Ticket ticket = new Ticket(type, ChunkLevel.byStatus(FullChunkStatus.FULL) - radius);
/* 147 */     addTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */   
/*     */   public void addTicket(Ticket ticket, ChunkPos chunkPos) {
/* 151 */     addTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */   
/*     */   public boolean addTicket(long key, Ticket ticket) {
/* 155 */     List<Ticket> tickets = getOrCreateTickets(key);
/* 156 */     for (Ticket t : tickets) {
/* 157 */       if (isTicketSameTypeAndLevel(ticket, t)) {
/* 158 */         t.resetTicksLeft();
/* 159 */         setDirty();
/* 160 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     int oldSimulationTicketLevel = getTicketLevelAt(tickets, true);
/* 165 */     int oldLoadingTicketLevel = getTicketLevelAt(tickets, false);
/* 166 */     tickets.add(ticket);
/*     */     
/* 168 */     if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 169 */       LOGGER.debug("ATI {} {}", new ChunkPos(key), ticket);
/*     */     }
/* 171 */     if (ticket.getType().doesSimulate() && 
/* 172 */       ticket.getTicketLevel() < oldSimulationTicketLevel && this.simulationChunkUpdatedListener != null) {
/* 173 */       this.simulationChunkUpdatedListener.update(key, ticket.getTicketLevel(), true);
/*     */     }
/*     */     
/* 176 */     if (ticket.getType().doesLoad() && 
/* 177 */       ticket.getTicketLevel() < oldLoadingTicketLevel && this.loadingChunkUpdatedListener != null) {
/* 178 */       this.loadingChunkUpdatedListener.update(key, ticket.getTicketLevel(), true);
/*     */     }
/*     */     
/* 181 */     if (ticket.getType().equals(TicketType.FORCED)) {
/* 182 */       this.chunksWithForcedTickets.add(key);
/*     */     }
/* 184 */     setDirty();
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isTicketSameTypeAndLevel(Ticket ticket, Ticket t) {
/* 189 */     return (t.getType() == ticket.getType() && t.getTicketLevel() == ticket.getTicketLevel());
/*     */   }
/*     */   
/*     */   public int getTicketLevelAt(long key, boolean simulation) {
/* 193 */     return getTicketLevelAt(getTickets(key), simulation);
/*     */   }
/*     */   
/*     */   private static int getTicketLevelAt(List<Ticket> tickets, boolean simulation) {
/* 197 */     Ticket lowestTicket = getLowestTicket(tickets, simulation);
/* 198 */     return (lowestTicket == null) ? (ChunkLevel.MAX_LEVEL + 1) : lowestTicket.getTicketLevel();
/*     */   }
/*     */   
/*     */   private static Ticket getLowestTicket(List<Ticket> tickets, boolean simulation) {
/* 202 */     if (tickets == null) {
/* 203 */       return null;
/*     */     }
/* 205 */     Ticket t = null;
/* 206 */     for (Ticket ticket : tickets) {
/* 207 */       if (t == null || ticket.getTicketLevel() < t.getTicketLevel()) {
/* 208 */         if (simulation && ticket.getType().doesSimulate()) {
/* 209 */           t = ticket; continue;
/* 210 */         }  if (!simulation && ticket.getType().doesLoad()) {
/* 211 */           t = ticket;
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     return t;
/*     */   }
/*     */   
/*     */   public void removeTicketWithRadius(TicketType type, ChunkPos chunkPos, int radius) {
/* 219 */     Ticket ticket = new Ticket(type, ChunkLevel.byStatus(FullChunkStatus.FULL) - radius);
/* 220 */     removeTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */   
/*     */   public void removeTicket(Ticket ticket, ChunkPos chunkPos) {
/* 224 */     removeTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */   
/*     */   public boolean removeTicket(long key, Ticket ticket) {
/* 228 */     List<Ticket> tickets = (List<Ticket>)this.tickets.get(key);
/* 229 */     if (tickets == null) {
/* 230 */       return false;
/*     */     }
/*     */     
/*     */     boolean found = false;
/* 234 */     for (Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext(); ) {
/* 235 */       Ticket t = iterator.next();
/* 236 */       if (isTicketSameTypeAndLevel(ticket, t)) {
/* 237 */         iterator.remove();
/* 238 */         if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 239 */           LOGGER.debug("RTI {} {}", new ChunkPos(key), t);
/*     */         }
/* 241 */         found = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 246 */     if (!found) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     if (tickets.isEmpty()) {
/* 251 */       this.tickets.remove(key);
/*     */     }
/*     */     
/* 254 */     if (ticket.getType().doesSimulate() && this.simulationChunkUpdatedListener != null) {
/* 255 */       this.simulationChunkUpdatedListener.update(key, getTicketLevelAt(tickets, true), false);
/*     */     }
/* 257 */     if (ticket.getType().doesLoad() && this.loadingChunkUpdatedListener != null) {
/* 258 */       this.loadingChunkUpdatedListener.update(key, getTicketLevelAt(tickets, false), false);
/*     */     }
/* 260 */     if (ticket.getType().equals(TicketType.FORCED)) {
/* 261 */       updateForcedChunks();
/*     */     }
/* 263 */     setDirty();
/* 264 */     return true;
/*     */   }
/*     */   
/*     */   private void updateForcedChunks() {
/* 268 */     this.chunksWithForcedTickets = getAllChunksWithTicketThat(t -> t.getType().equals(TicketType.FORCED));
/*     */   }
/*     */   
/*     */   public String getTicketDebugString(long key, boolean simulation) {
/* 272 */     List<Ticket> tickets = getTickets(key);
/* 273 */     Ticket lowestTicket = getLowestTicket(tickets, simulation);
/* 274 */     return (lowestTicket == null) ? "no_ticket" : lowestTicket.toString();
/*     */   }
/*     */   
/*     */   public void purgeStaleTickets(ChunkMap chunkMap) {
/* 278 */     removeTicketIf((ticket, chunkPos) -> { if (canTicketExpire(chunkMap, chunkMap, chunkPos)) { chunkMap.decreaseTicksLeft(); return chunkMap.isTimedOut(); }  return false; }, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     setDirty();
/*     */   }
/*     */   
/*     */   private boolean canTicketExpire(ChunkMap chunkMap, Ticket ticket, long chunkPos) {
/* 289 */     if (!ticket.getType().hasTimeout()) {
/* 290 */       return false;
/*     */     }
/* 292 */     if (ticket.getType().canExpireIfUnloaded()) {
/* 293 */       return true;
/*     */     }
/* 295 */     ChunkHolder updatingChunk = chunkMap.getUpdatingChunkIfPresent(chunkPos);
/*     */ 
/*     */ 
/*     */     
/* 299 */     return (updatingChunk == null || updatingChunk.isReadyForSaving());
/*     */   }
/*     */ 
/*     */   
/*     */   public void deactivateTicketsOnClosing() {
/* 304 */     removeTicketIf((ticket, chunkPos) -> (ticket.getType() != TicketType.UNKNOWN), this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   public void removeTicketIf(TicketPredicate predicate, Long2ObjectOpenHashMap<List<Ticket>> removedTickets) {
/* 308 */     ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> ticketsPerChunkIterator = this.tickets.long2ObjectEntrySet().fastIterator();
/*     */     boolean removedForced = false;
/* 310 */     while (ticketsPerChunkIterator.hasNext()) {
/* 311 */       Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry<List<Ticket>>)ticketsPerChunkIterator.next();
/* 312 */       Iterator<Ticket> chunkTicketsIterator = ((List<Ticket>)entry.getValue()).iterator();
/* 313 */       long chunkPos = entry.getLongKey();
/*     */       boolean removedSimulation = false;
/*     */       boolean removedLoading = false;
/* 316 */       while (chunkTicketsIterator.hasNext()) {
/* 317 */         Ticket ticket = chunkTicketsIterator.next();
/* 318 */         if (predicate.test(ticket, chunkPos)) {
/* 319 */           if (removedTickets != null) {
/* 320 */             List<Ticket> tickets = (List<Ticket>)removedTickets.computeIfAbsent(chunkPos, k -> new ObjectArrayList(((List)entry.getValue()).size()));
/* 321 */             tickets.add(ticket);
/*     */           } 
/* 323 */           chunkTicketsIterator.remove();
/* 324 */           if (ticket.getType().doesLoad()) {
/* 325 */             removedLoading = true;
/*     */           }
/* 327 */           if (ticket.getType().doesSimulate()) {
/* 328 */             removedSimulation = true;
/*     */           }
/* 330 */           if (ticket.getType().equals(TicketType.FORCED)) {
/* 331 */             removedForced = true;
/*     */           }
/*     */         } 
/*     */       } 
/* 335 */       if (!removedLoading && !removedSimulation) {
/*     */         continue;
/*     */       }
/* 338 */       if (removedLoading && this.loadingChunkUpdatedListener != null) {
/* 339 */         this.loadingChunkUpdatedListener.update(chunkPos, getTicketLevelAt((List<Ticket>)entry.getValue(), false), false);
/*     */       }
/* 341 */       if (removedSimulation && this.simulationChunkUpdatedListener != null) {
/* 342 */         this.simulationChunkUpdatedListener.update(chunkPos, getTicketLevelAt((List<Ticket>)entry.getValue(), true), false);
/*     */       }
/* 344 */       setDirty();
/* 345 */       if (((List)entry.getValue()).isEmpty()) {
/* 346 */         ticketsPerChunkIterator.remove();
/*     */       }
/*     */     } 
/* 349 */     if (removedForced) {
/* 350 */       updateForcedChunks();
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceTicketLevelOfType(int newLevel, TicketType ticketType) {
/* 355 */     List<Pair<Ticket, Long>> affectedTickets = new ArrayList<>();
/* 356 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = this.tickets.long2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 357 */       for (Ticket ticket : (Iterable<Ticket>)entry.getValue()) {
/* 358 */         if (ticket.getType() == ticketType) {
/* 359 */           affectedTickets.add(Pair.of(ticket, entry.getLongKey()));
/*     */         }
/*     */       }  }
/*     */     
/* 363 */     for (Pair<Ticket, Long> pair : affectedTickets) {
/* 364 */       Long key = (Long)pair.getSecond();
/* 365 */       Ticket ticket = (Ticket)pair.getFirst();
/* 366 */       removeTicket(key, ticket);
/* 367 */       TicketType type = ticket.getType();
/* 368 */       addTicket(key, new Ticket(type, newLevel));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean updateChunkForced(ChunkPos chunkPos, boolean forced) {
/* 373 */     Ticket ticket = new Ticket(TicketType.FORCED, ChunkMap.FORCED_TICKET_LEVEL);
/* 374 */     if (forced) {
/* 375 */       return addTicket(chunkPos.toLong(), ticket);
/*     */     }
/* 377 */     return removeTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LongSet getForceLoadedChunks() {
/* 383 */     return this.chunksWithForcedTickets;
/*     */   }
/*     */   
/*     */   private LongSet getAllChunksWithTicketThat(Predicate<Ticket> ticketCheck) {
/* 387 */     LongOpenHashSet chunks = new LongOpenHashSet();
/* 388 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)this.tickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 389 */       for (Ticket ticket : (Iterable<Ticket>)entry.getValue()) {
/* 390 */         if (ticketCheck.test(ticket)) {
/* 391 */           chunks.add(entry.getLongKey());
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/* 396 */     return (LongSet)chunks;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkUpdated {
/*     */     void update(long param1Long, int param1Int, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   public static interface TicketPredicate {
/*     */     boolean test(Ticket param1Ticket, long param1Long);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/TicketStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */