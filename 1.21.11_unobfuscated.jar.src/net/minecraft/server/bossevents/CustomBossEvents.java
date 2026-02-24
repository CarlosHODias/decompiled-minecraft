/*    */ package net.minecraft.server.bossevents;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class CustomBossEvents
/*    */ {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 22 */   private static final Codec<Map<Identifier, CustomBossEvent.Packed>> EVENTS_CODEC = (Codec<Map<Identifier, CustomBossEvent.Packed>>)Codec.unboundedMap(Identifier.CODEC, CustomBossEvent.Packed.CODEC);
/*    */   
/* 24 */   private final Map<Identifier, CustomBossEvent> events = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomBossEvent get(Identifier id) {
/* 30 */     return this.events.get(id);
/*    */   }
/*    */   
/*    */   public CustomBossEvent create(Identifier id, Component name) {
/* 34 */     CustomBossEvent result = new CustomBossEvent(id, name);
/* 35 */     this.events.put(id, result);
/* 36 */     return result;
/*    */   }
/*    */   
/*    */   public void remove(CustomBossEvent event) {
/* 40 */     this.events.remove(event.getTextId());
/*    */   }
/*    */   
/*    */   public Collection<Identifier> getIds() {
/* 44 */     return this.events.keySet();
/*    */   }
/*    */   
/*    */   public Collection<CustomBossEvent> getEvents() {
/* 48 */     return this.events.values();
/*    */   }
/*    */   
/*    */   public CompoundTag save(HolderLookup.Provider registries) {
/* 52 */     Map<Identifier, CustomBossEvent.Packed> packedEvents = Util.mapValues(this.events, CustomBossEvent::pack);
/* 53 */     return (CompoundTag)EVENTS_CODEC.encodeStart((DynamicOps)registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE), packedEvents).getOrThrow();
/*    */   }
/*    */   
/*    */   public void load(CompoundTag tag, HolderLookup.Provider registries) {
/* 57 */     Map<Identifier, CustomBossEvent.Packed> events = EVENTS_CODEC.parse((DynamicOps)registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE), tag)
/* 58 */       .resultOrPartial(error -> LOGGER.error("Failed to parse boss bar events: {}", error))
/* 59 */       .orElse(Map.of());
/* 60 */     events.forEach((id, packed) -> this.events.put(id, CustomBossEvent.load(id, packed)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onPlayerConnect(ServerPlayer player) {
/* 66 */     for (CustomBossEvent event : this.events.values()) {
/* 67 */       event.onPlayerConnect(player);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onPlayerDisconnect(ServerPlayer player) {
/* 72 */     for (CustomBossEvent event : this.events.values())
/* 73 */       event.onPlayerDisconnect(player); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/bossevents/CustomBossEvents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */