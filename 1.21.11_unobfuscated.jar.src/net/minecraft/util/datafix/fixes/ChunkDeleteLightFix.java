/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkDeleteLightFix extends com.mojang.datafixers.DataFix {
/*    */   public ChunkDeleteLightFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder<?> sectionsFinder = chunkType.findField("sections");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteLightFix for " + getOutputSchema().getVersionKey(), chunkType, chunk -> {
/*    */           chunk = chunk.update(DSL.remainderFinder(), ());
/*    */           return chunk.updateTyped(sectionsFinder, ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ChunkDeleteLightFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */