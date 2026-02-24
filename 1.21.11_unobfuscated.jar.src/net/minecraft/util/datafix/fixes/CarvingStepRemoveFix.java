/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class CarvingStepRemoveFix
/*    */   extends DataFix {
/*    */   public CarvingStepRemoveFix(Schema outputSchema) {
/* 14 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     return fixTypeEverywhereTyped("CarvingStepRemoveFix", getInputSchema().getType(References.CHUNK), CarvingStepRemoveFix::fixChunk);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixChunk(Typed<?> input) {
/* 23 */     return input.update(DSL.remainderFinder(), chunkIn -> {
/*    */           Dynamic<?> chunk = chunkIn;
/*    */           Optional<? extends Dynamic<?>> carvingMasks = chunk.get("CarvingMasks").result();
/*    */           if (carvingMasks.isPresent()) {
/*    */             Optional<? extends Dynamic<?>> mask = ((Dynamic)carvingMasks.get()).get("AIR").result();
/*    */             if (mask.isPresent())
/*    */               chunk = chunk.set("carving_mask", mask.get()); 
/*    */           } 
/*    */           return chunk.remove("CarvingMasks");
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/CarvingStepRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */