/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryExpiryDataFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   public MemoryExpiryDataFix(Schema schema, String entityType) {
/* 30 */     super(schema, false, "Memory expiry data fix (" + entityType + ")", References.ENTITY, entityType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 35 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 39 */     return input.update("Brain", this::updateBrain);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateBrain(Dynamic<?> input) {
/* 43 */     return input.update("memories", this::updateMemories);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateMemories(Dynamic<?> memories) {
/* 47 */     return memories.updateMapValues(this::updateMemoryEntry);
/*    */   }
/*    */   
/*    */   private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(Pair<Dynamic<?>, Dynamic<?>> memoryEntry) {
/* 51 */     return memoryEntry.mapSecond(this::wrapMemoryValue);
/*    */   }
/*    */   
/*    */   private Dynamic<?> wrapMemoryValue(Dynamic<?> dynamic) {
/* 55 */     return dynamic.createMap((Map)ImmutableMap.of(
/* 56 */           dynamic.createString("value"), dynamic));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/MemoryExpiryDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */