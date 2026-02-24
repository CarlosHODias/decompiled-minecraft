/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TrackingDebugSynchronizer<T>
/*     */ {
/*     */   protected final DebugSubscription<T> subscription;
/*  36 */   private final Set<UUID> subscribedPlayers = (Set<UUID>)new ObjectOpenHashSet();
/*     */   
/*     */   public TrackingDebugSynchronizer(DebugSubscription<T> subscription) {
/*  39 */     this.subscription = subscription;
/*     */   }
/*     */   
/*     */   public final void tick(ServerLevel level) {
/*  43 */     for (ServerPlayer player : (Iterable<ServerPlayer>)level.players()) {
/*  44 */       boolean wasSubscribed = this.subscribedPlayers.contains(player.getUUID());
/*  45 */       boolean isSubscribed = player.debugSubscriptions().contains(this.subscription);
/*  46 */       if (isSubscribed == wasSubscribed) {
/*     */         continue;
/*     */       }
/*  49 */       if (isSubscribed) {
/*  50 */         addSubscriber(player); continue;
/*     */       } 
/*  52 */       this.subscribedPlayers.remove(player.getUUID());
/*     */     } 
/*     */     
/*  55 */     this.subscribedPlayers.removeIf(id -> (level.getPlayerByUUID(id) == null));
/*     */ 
/*     */     
/*  58 */     if (!this.subscribedPlayers.isEmpty()) {
/*  59 */       pollAndSendUpdates(level);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addSubscriber(ServerPlayer player) {
/*  64 */     this.subscribedPlayers.add(player.getUUID());
/*     */     
/*  66 */     player.getChunkTrackingView().forEach(chunkPos -> {
/*     */           if (!player.connection.chunkSender.isPending(player.toLong())) {
/*     */             startTrackingChunk(player, player);
/*     */           }
/*     */         });
/*     */     
/*  72 */     (player.level().getChunkSource()).chunkMap.forEachEntityTrackedBy(player, entity -> startTrackingEntity(player, player));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos trackedChunk, Packet<? super ClientGamePacketListener> packet) {
/*  78 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*  79 */     for (UUID playerId : this.subscribedPlayers) {
/*  80 */       Player player = level.getPlayerByUUID(playerId); if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player; if (chunkMap.isChunkTracked(serverPlayer, trackedChunk.x, trackedChunk.z))
/*  81 */           serverPlayer.connection.send(packet);  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void sendToPlayersTrackingEntity(ServerLevel level, Entity trackedEntity, Packet<? super ClientGamePacketListener> packet) {
/*  87 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*  88 */     chunkMap.sendToTrackingPlayersFiltered(trackedEntity, packet, player -> this.subscribedPlayers.contains(player.getUUID()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void startTrackingChunk(ServerPlayer player, ChunkPos chunkPos) {
/*  96 */     if (this.subscribedPlayers.contains(player.getUUID())) {
/*  97 */       sendInitialChunk(player, chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void startTrackingEntity(ServerPlayer player, Entity entity) {
/* 102 */     if (this.subscribedPlayers.contains(player.getUUID())) {
/* 103 */       sendInitialEntity(player, entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clear() {}
/*     */ 
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel level) {}
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {}
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer player, Entity entity) {}
/*     */   
/*     */   public static class SourceSynchronizer<T>
/*     */     extends TrackingDebugSynchronizer<T>
/*     */   {
/* 120 */     private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap<>();
/* 121 */     private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap<>();
/* 122 */     private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap<>();
/*     */     
/*     */     public SourceSynchronizer(DebugSubscription<T> subscription) {
/* 125 */       super(subscription);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void clear() {
/* 130 */       this.chunkSources.clear();
/* 131 */       this.blockEntitySources.clear();
/* 132 */       this.entitySources.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void pollAndSendUpdates(ServerLevel level) {
/* 137 */       for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 138 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 139 */         if (update != null) {
/* 140 */           ChunkPos chunkPos = entry.getKey();
/* 141 */           sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 146 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 147 */         if (update != null) {
/* 148 */           BlockPos blockPos = entry.getKey();
/* 149 */           ChunkPos chunkPos = new ChunkPos(blockPos);
/* 150 */           sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */         } 
/*     */       } 
/*     */       
/* 154 */       for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 155 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 156 */         if (update != null) {
/* 157 */           Entity entity = Objects.<Entity>requireNonNull(level.getEntity(entry.getKey()));
/* 158 */           sendToPlayersTrackingEntity(level, entity, (Packet<? super ClientGamePacketListener>)new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void registerChunk(ChunkPos chunkPos, DebugValueSource.ValueGetter<T> getter) {
/* 164 */       this.chunkSources.put(chunkPos, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */     }
/*     */     
/*     */     public void registerBlockEntity(BlockPos blockPos, DebugValueSource.ValueGetter<T> getter) {
/* 168 */       this.blockEntitySources.put(blockPos, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */     }
/*     */     
/*     */     public void registerEntity(UUID entityId, DebugValueSource.ValueGetter<T> getter) {
/* 172 */       this.entitySources.put(entityId, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */     }
/*     */     
/*     */     public void dropChunk(ChunkPos chunkPos) {
/* 176 */       this.chunkSources.remove(chunkPos);
/*     */ 
/*     */       
/* 179 */       Objects.requireNonNull(chunkPos); this.blockEntitySources.keySet().removeIf(chunkPos::contains);
/*     */     }
/*     */     
/*     */     public void dropBlockEntity(ServerLevel level, BlockPos blockPos) {
/* 183 */       TrackingDebugSynchronizer.ValueSource<T> source = this.blockEntitySources.remove(blockPos);
/* 184 */       if (source != null) {
/* 185 */         ChunkPos chunkPos = new ChunkPos(blockPos);
/* 186 */         sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void dropEntity(Entity entity) {
/* 191 */       this.entitySources.remove(entity.getUUID());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 196 */       TrackingDebugSynchronizer.ValueSource<T> chunkSource = this.chunkSources.get(chunkPos);
/* 197 */       if (chunkSource != null && chunkSource.lastSyncedValue != null) {
/* 198 */         player.connection.send((Packet)new ClientboundDebugChunkValuePacket(chunkPos, this.subscription.packUpdate(chunkSource.lastSyncedValue)));
/*     */       }
/*     */       
/* 201 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 202 */         T lastValue = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 203 */         if (lastValue == null) {
/*     */           continue;
/*     */         }
/* 206 */         BlockPos blockPos = entry.getKey();
/* 207 */         if (chunkPos.contains(blockPos)) {
/* 208 */           player.connection.send((Packet)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(lastValue)));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialEntity(ServerPlayer player, Entity entity) {
/* 215 */       TrackingDebugSynchronizer.ValueSource<T> source = this.entitySources.get(entity.getUUID());
/* 216 */       if (source != null && source.lastSyncedValue != null)
/* 217 */         player.connection.send((Packet)new ClientboundDebugEntityValuePacket(entity.getId(), this.subscription.packUpdate(source.lastSyncedValue))); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueSource<T>
/*     */   {
/*     */     private final DebugValueSource.ValueGetter<T> getter;
/*     */     private T lastSyncedValue;
/*     */     
/*     */     private ValueSource(DebugValueSource.ValueGetter<T> getter) {
/* 227 */       this.getter = getter;
/*     */     }
/*     */     
/*     */     public DebugSubscription.Update<T> pollUpdate(DebugSubscription<T> subscription) {
/* 231 */       T newValue = this.getter.get();
/* 232 */       if (!Objects.equals(newValue, this.lastSyncedValue)) {
/* 233 */         this.lastSyncedValue = newValue;
/* 234 */         return subscription.packUpdate(newValue);
/*     */       } 
/* 236 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PoiSynchronizer extends TrackingDebugSynchronizer<DebugPoiInfo> {
/*     */     public PoiSynchronizer() {
/* 242 */       super(DebugSubscriptions.POIS);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 247 */       ServerLevel level = player.level();
/* 248 */       PoiManager poiManager = level.getPoiManager();
/* 249 */       poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> player.connection.send((Packet)new ClientboundDebugBlockValuePacket(player.getPos(), this.subscription.packUpdate(new DebugPoiInfo(player)))));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPoiAdded(ServerLevel level, PoiRecord record) {
/* 257 */       sendToPlayersTrackingChunk(level, new ChunkPos(record.getPos()), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record))));
/*     */     }
/*     */     
/*     */     public void onPoiRemoved(ServerLevel level, BlockPos poiPos) {
/* 261 */       sendToPlayersTrackingChunk(level, new ChunkPos(poiPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(poiPos, this.subscription.emptyUpdate()));
/*     */     }
/*     */     
/*     */     public void onPoiTicketCountChanged(ServerLevel level, BlockPos poiPos) {
/* 265 */       sendToPlayersTrackingChunk(level, new ChunkPos(poiPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(poiPos, this.subscription.packUpdate(level.getPoiManager().getDebugPoiInfo(poiPos))));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VillageSectionSynchronizer extends TrackingDebugSynchronizer<Unit> {
/*     */     public VillageSectionSynchronizer() {
/* 271 */       super(DebugSubscriptions.VILLAGE_SECTIONS);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 276 */       ServerLevel level = player.level();
/* 277 */       PoiManager poiManager = level.getPoiManager();
/* 278 */       poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> {
/*     */             SectionPos centerSection = SectionPos.of(player.getPos());
/*     */             forEachVillageSectionUpdate(level, centerSection, ());
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPoiAdded(ServerLevel level, PoiRecord record) {
/* 290 */       sendVillageSectionsPacket(level, record.getPos());
/*     */     }
/*     */     
/*     */     public void onPoiRemoved(ServerLevel level, BlockPos poiPos) {
/* 294 */       sendVillageSectionsPacket(level, poiPos);
/*     */     }
/*     */     
/*     */     private void sendVillageSectionsPacket(ServerLevel level, BlockPos poiPos) {
/* 298 */       forEachVillageSectionUpdate(level, SectionPos.of(poiPos), (sectionPos, isVillage) -> {
/*     */             BlockPos sectionBlockPos = level.center();
/*     */             if (isVillage) {
/*     */               sendToPlayersTrackingChunk(level, new ChunkPos(sectionBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */             } else {
/*     */               sendToPlayersTrackingChunk(level, new ChunkPos(sectionBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.emptyUpdate()));
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/*     */     private static void forEachVillageSectionUpdate(ServerLevel level, SectionPos centerSection, BiConsumer<SectionPos, Boolean> consumer) {
/* 309 */       for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 310 */         for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 311 */           for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 312 */             SectionPos sectionPos = centerSection.offset(offsetX, offsetY, offsetZ);
/* 313 */             if (level.isVillage(sectionPos.center())) {
/* 314 */               consumer.accept(sectionPos, true);
/*     */             } else {
/* 316 */               consumer.accept(sectionPos, false);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/TrackingDebugSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */