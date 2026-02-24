/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.components.DebugScreenOverlay;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundDebugSubscriptionRequestPacket;
/*     */ import net.minecraft.util.debug.DebugSubscription;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ClientDebugSubscriber {
/*     */   private final ClientPacketListener connection;
/*     */   private final DebugScreenOverlay debugScreenOverlay;
/*  29 */   private Set<DebugSubscription<?>> remoteSubscriptions = Set.of();
/*     */   
/*  31 */   private final Map<DebugSubscription<?>, ValueMaps<?>> valuesBySubscription = new HashMap<>();
/*     */   
/*     */   public ClientDebugSubscriber(ClientPacketListener connection, DebugScreenOverlay debugScreenOverlay) {
/*  34 */     this.debugScreenOverlay = debugScreenOverlay;
/*  35 */     this.connection = connection;
/*     */   }
/*     */   
/*     */   private static void addFlag(Set<DebugSubscription<?>> output, DebugSubscription<?> subscription, boolean flag) {
/*  39 */     if (flag) {
/*  40 */       output.add(subscription);
/*     */     }
/*     */   }
/*     */   
/*     */   private Set<DebugSubscription<?>> requestedSubscriptions() {
/*  45 */     ReferenceOpenHashSet referenceOpenHashSet = new ReferenceOpenHashSet();
/*  46 */     addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, RemoteDebugSampleType.TICK_TIME.subscription(), this.debugScreenOverlay.showFpsCharts());
/*  47 */     if (SharedConstants.DEBUG_ENABLED) {
/*  48 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.BEES, SharedConstants.DEBUG_BEES);
/*  49 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.BEE_HIVES, SharedConstants.DEBUG_BEES);
/*  50 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.BRAINS, SharedConstants.DEBUG_BRAIN);
/*  51 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.BREEZES, SharedConstants.DEBUG_BREEZE_MOB);
/*  52 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS, SharedConstants.DEBUG_ENTITY_BLOCK_INTERSECTION);
/*  53 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.ENTITY_PATHS, SharedConstants.DEBUG_PATHFINDING);
/*  54 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.GAME_EVENTS, SharedConstants.DEBUG_GAME_EVENT_LISTENERS);
/*  55 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.GAME_EVENT_LISTENERS, SharedConstants.DEBUG_GAME_EVENT_LISTENERS);
/*  56 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.GOAL_SELECTORS, (SharedConstants.DEBUG_GOAL_SELECTOR || SharedConstants.DEBUG_BEES));
/*  57 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.NEIGHBOR_UPDATES, SharedConstants.DEBUG_NEIGHBORSUPDATE);
/*  58 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.POIS, SharedConstants.DEBUG_POI);
/*  59 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.RAIDS, SharedConstants.DEBUG_RAIDS);
/*  60 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, SharedConstants.DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER);
/*  61 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.STRUCTURES, SharedConstants.DEBUG_STRUCTURES);
/*  62 */       addFlag((Set<DebugSubscription<?>>)referenceOpenHashSet, DebugSubscriptions.VILLAGE_SECTIONS, SharedConstants.DEBUG_VILLAGE_SECTIONS);
/*     */     } 
/*  64 */     return (Set<DebugSubscription<?>>)referenceOpenHashSet;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  68 */     this.remoteSubscriptions = Set.of();
/*  69 */     dropLevel();
/*     */   }
/*     */   
/*     */   public void tick(long gameTime) {
/*  73 */     Set<DebugSubscription<?>> newSubscriptions = requestedSubscriptions();
/*  74 */     if (!newSubscriptions.equals(this.remoteSubscriptions)) {
/*  75 */       this.remoteSubscriptions = newSubscriptions;
/*  76 */       onSubscriptionsChanged(newSubscriptions);
/*     */     } 
/*     */     
/*  79 */     this.valuesBySubscription.forEach((subscription, valueMaps) -> {
/*     */           if (subscription.expireAfterTicks() != 0) {
/*     */             valueMaps.purgeExpired(gameTime);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void onSubscriptionsChanged(Set<DebugSubscription<?>> newSubscriptions) {
/*  89 */     this.valuesBySubscription.keySet().retainAll(newSubscriptions);
/*     */     
/*  91 */     initializeSubscriptions(newSubscriptions);
/*     */     
/*  93 */     this.connection.send((Packet<?>)new ServerboundDebugSubscriptionRequestPacket(newSubscriptions));
/*     */   }
/*     */   
/*     */   private void initializeSubscriptions(Set<DebugSubscription<?>> newSubscriptions) {
/*  97 */     for (DebugSubscription<?> subscription : newSubscriptions) {
/*  98 */       this.valuesBySubscription.computeIfAbsent(subscription, s -> new ValueMaps());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private <V> ValueMaps<V> getValueMaps(DebugSubscription<V> subscription) {
/* 104 */     return (ValueMaps<V>)this.valuesBySubscription.get(subscription);
/*     */   }
/*     */   
/*     */   private <K, V> ValueMap<K, V> getValueMap(DebugSubscription<V> subscription, ValueMapType<K, V> mapType) {
/* 108 */     ValueMaps<V> maps = getValueMaps(subscription);
/* 109 */     return (maps != null) ? mapType.get(maps) : null;
/*     */   }
/*     */   
/*     */   private <K, V> V getValue(DebugSubscription<V> subscription, K key, ValueMapType<K, V> type) {
/* 113 */     ValueMap<K, V> values = getValueMap(subscription, type);
/* 114 */     return (values != null) ? values.getValue(key) : null;
/*     */   }
/*     */   
/*     */   public DebugValueAccess createDebugValueAccess(final Level level) {
/* 118 */     return new DebugValueAccess()
/*     */       {
/*     */         public <T> void forEachChunk(DebugSubscription<T> subscription, BiConsumer<ChunkPos, T> consumer) {
/* 121 */           ClientDebugSubscriber.this.forEachValue(subscription, ClientDebugSubscriber.chunks(), consumer);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> T getChunkValue(DebugSubscription<T> subscription, ChunkPos chunkPos) {
/* 126 */           return ClientDebugSubscriber.this.getValue(subscription, chunkPos, ClientDebugSubscriber.chunks());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> void forEachBlock(DebugSubscription<T> subscription, BiConsumer<BlockPos, T> consumer) {
/* 131 */           ClientDebugSubscriber.this.forEachValue(subscription, ClientDebugSubscriber.blocks(), consumer);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> T getBlockValue(DebugSubscription<T> subscription, BlockPos blockPos) {
/* 136 */           return ClientDebugSubscriber.this.getValue(subscription, blockPos, ClientDebugSubscriber.blocks());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> void forEachEntity(DebugSubscription<T> subscription, BiConsumer<Entity, T> consumer) {
/* 141 */           ClientDebugSubscriber.this.forEachValue(subscription, ClientDebugSubscriber.entities(), (entityId, value) -> {
/*     */                 Entity entity = level.getEntity(entityId);
/*     */                 if (entity != null) {
/*     */                   consumer.accept(entity, value);
/*     */                 }
/*     */               });
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> T getEntityValue(DebugSubscription<T> subscription, Entity entity) {
/* 151 */           return ClientDebugSubscriber.this.getValue(subscription, entity.getUUID(), ClientDebugSubscriber.entities());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> void forEachEvent(DebugSubscription<T> subscription, DebugValueAccess.EventVisitor<T> visitor) {
/* 156 */           ClientDebugSubscriber.ValueMaps<T> values = ClientDebugSubscriber.this.getValueMaps(subscription);
/* 157 */           if (values == null) {
/*     */             return;
/*     */           }
/* 160 */           long gameTime = level.getGameTime();
/* 161 */           for (ClientDebugSubscriber.ValueWrapper<T> event : values.events) {
/* 162 */             int remainingTicks = (int)(event.expiresAfterTime() - gameTime);
/* 163 */             int totalLifetime = subscription.expireAfterTicks();
/* 164 */             visitor.accept(event.value(), remainingTicks, totalLifetime);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public <T> void updateChunk(long gameTime, ChunkPos chunkPos, DebugSubscription.Update<T> update) {
/* 171 */     updateMap(gameTime, chunkPos, update, chunks());
/*     */   }
/*     */   
/*     */   public <T> void updateBlock(long gameTime, BlockPos blockPos, DebugSubscription.Update<T> update) {
/* 175 */     updateMap(gameTime, blockPos, update, blocks());
/*     */   }
/*     */   
/*     */   public <T> void updateEntity(long gameTime, Entity entity, DebugSubscription.Update<T> update) {
/* 179 */     updateMap(gameTime, entity.getUUID(), update, entities());
/*     */   }
/*     */   
/*     */   public <T> void pushEvent(long gameTime, DebugSubscription.Event<T> event) {
/* 183 */     ValueMaps<T> values = getValueMaps(event.subscription());
/* 184 */     if (values != null) {
/* 185 */       values.events.add(new ValueWrapper<>((T)event.value(), gameTime + event.subscription().expireAfterTicks()));
/*     */     }
/*     */   }
/*     */   
/*     */   private <K, V> void updateMap(long gameTime, K key, DebugSubscription.Update<V> update, ValueMapType<K, V> type) {
/* 190 */     ValueMap<K, V> values = getValueMap(update.subscription(), type);
/* 191 */     if (values != null) {
/* 192 */       values.apply(gameTime, key, update);
/*     */     }
/*     */   }
/*     */   
/*     */   private <K, V> void forEachValue(DebugSubscription<V> subscription, ValueMapType<K, V> type, BiConsumer<K, V> consumer) {
/* 197 */     ValueMap<K, V> values = getValueMap(subscription, type);
/* 198 */     if (values != null) {
/* 199 */       values.forEach(consumer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void dropLevel() {
/* 204 */     this.valuesBySubscription.clear();
/* 205 */     initializeSubscriptions(this.remoteSubscriptions);
/*     */   }
/*     */   
/*     */   public void dropChunk(ChunkPos chunkPos) {
/* 209 */     if (this.valuesBySubscription.isEmpty()) {
/*     */       return;
/*     */     }
/* 212 */     for (ValueMaps<?> values : this.valuesBySubscription.values()) {
/* 213 */       values.dropChunkAndBlocks(chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void dropEntity(Entity entity) {
/* 218 */     if (this.valuesBySubscription.isEmpty()) {
/*     */       return;
/*     */     }
/* 221 */     for (ValueMaps<?> values : this.valuesBySubscription.values())
/* 222 */       values.entityValues.removeKey(entity.getUUID()); 
/*     */   }
/*     */   
/*     */   private static class ValueMap<K, V>
/*     */   {
/* 227 */     private final Map<K, ClientDebugSubscriber.ValueWrapper<V>> values = new HashMap<>();
/*     */     
/*     */     public void removeValues(Predicate<ClientDebugSubscriber.ValueWrapper<V>> predicate) {
/* 230 */       this.values.values().removeIf(predicate);
/*     */     }
/*     */     
/*     */     public void removeKey(K key) {
/* 234 */       this.values.remove(key);
/*     */     }
/*     */     
/*     */     public void removeKeys(Predicate<K> predicate) {
/* 238 */       this.values.keySet().removeIf(predicate);
/*     */     }
/*     */     
/*     */     public V getValue(K key) {
/* 242 */       ClientDebugSubscriber.ValueWrapper<V> result = this.values.get(key);
/* 243 */       return (result != null) ? result.value() : null;
/*     */     }
/*     */     
/*     */     public void apply(long gameTime, K key, DebugSubscription.Update<V> update) {
/* 247 */       if (update.value().isPresent()) {
/* 248 */         this.values.put(key, new ClientDebugSubscriber.ValueWrapper<>(update.value().get(), gameTime + update.subscription().expireAfterTicks()));
/*     */       } else {
/* 250 */         this.values.remove(key);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<K, V> output) {
/* 255 */       this.values.forEach((k, v) -> output.accept(k, v.value()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueMaps<V> {
/* 260 */     private final ClientDebugSubscriber.ValueMap<ChunkPos, V> chunkValues = new ClientDebugSubscriber.ValueMap<>();
/* 261 */     private final ClientDebugSubscriber.ValueMap<BlockPos, V> blockValues = new ClientDebugSubscriber.ValueMap<>();
/* 262 */     private final ClientDebugSubscriber.ValueMap<UUID, V> entityValues = new ClientDebugSubscriber.ValueMap<>();
/* 263 */     private final List<ClientDebugSubscriber.ValueWrapper<V>> events = new ArrayList<>();
/*     */     
/*     */     public void purgeExpired(long gameTime) {
/*     */       Predicate<ClientDebugSubscriber.ValueWrapper<V>> expiredPredicate = v -> v.hasExpired(gameTime);
/* 267 */       this.chunkValues.removeValues(expiredPredicate);
/* 268 */       this.blockValues.removeValues(expiredPredicate);
/* 269 */       this.entityValues.removeValues(expiredPredicate);
/* 270 */       this.events.removeIf(expiredPredicate);
/*     */     }
/*     */     
/*     */     public void dropChunkAndBlocks(ChunkPos chunkPos) {
/* 274 */       this.chunkValues.removeKey(chunkPos);
/*     */       
/* 276 */       Objects.requireNonNull(chunkPos); this.blockValues.removeKeys(chunkPos::contains);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> ValueMapType<UUID, T> entities() {
/* 286 */     return v -> v.entityValues;
/*     */   }
/*     */   
/*     */   private static <T> ValueMapType<BlockPos, T> blocks() {
/* 290 */     return v -> v.blockValues;
/*     */   }
/*     */   
/*     */   private static <T> ValueMapType<ChunkPos, T> chunks() {
/* 294 */     return v -> v.chunkValues;
/*     */   }
/*     */   private static final class ValueWrapper<T> extends Record { private final T value; private final long expiresAfterTime; private static final long NO_EXPIRY = -1L;
/* 297 */     private ValueWrapper(T value, long expiresAfterTime) { this.value = value; this.expiresAfterTime = expiresAfterTime; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 297 */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper<TT;>; } public T value() { return this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 297 */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper<TT;>; } public long expiresAfterTime() { return this.expiresAfterTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasExpired(long gameTime) {
/* 304 */       if (this.expiresAfterTime == -1L) {
/* 305 */         return false;
/*     */       }
/* 307 */       return (gameTime >= this.expiresAfterTime);
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface ValueMapType<K, V> {
/*     */     ClientDebugSubscriber.ValueMap<K, V> get(ClientDebugSubscriber.ValueMaps<V> param1ValueMaps);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientDebugSubscriber.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */