/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class OverreachingTickFix
/*    */   extends DataFix {
/*    */   public OverreachingTickFix(Schema outputSchema) {
/* 17 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 23 */     OpticFinder<?> blockTicksFinder = chunkType.findField("block_ticks");
/*    */     
/* 25 */     return fixTypeEverywhereTyped("Handle ticks saved in the wrong chunk", chunkType, chunk -> {
/*    */           Optional<? extends Typed<?>> blockTicksOpt = chunk.getOptionalTyped(blockTicksFinder);
/*    */           Optional<? extends Dynamic<?>> blockTicks = blockTicksOpt.isPresent() ? ((Typed)blockTicksOpt.get()).write().result() : Optional.<Dynamic<?>>empty();
/*    */           return chunk.update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> extractOverreachingTicks(Dynamic<?> remainder, int chunkX, int chunkZ, Optional<? extends Dynamic<?>> ticks, String nameInUpgradeData) {
/* 41 */     if (ticks.isPresent()) {
/* 42 */       List<? extends Dynamic<?>> overreachingTicks = ((Dynamic)ticks.get()).asStream().filter(tick -> {
/*    */             int x = tick.get("x").asInt(0), z = tick.get("z").asInt(0), distX = Math.abs(chunkX - (x >> 4)), distZ = Math.abs(chunkZ - (z >> 4));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 50 */             return ((distX != 0 || distZ != 0) && distX <= 1 && distZ <= 1);
/* 51 */           }).toList();
/* 52 */       if (!overreachingTicks.isEmpty()) {
/* 53 */         remainder = remainder.set("UpgradeData", remainder.get("UpgradeData").orElseEmptyMap().set(nameInUpgradeData, remainder.createList(overreachingTicks.stream())));
/*    */       }
/*    */     } 
/* 56 */     return remainder;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OverreachingTickFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */