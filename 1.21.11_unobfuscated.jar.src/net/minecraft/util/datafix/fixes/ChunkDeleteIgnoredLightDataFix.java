/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkDeleteIgnoredLightDataFix extends com.mojang.datafixers.DataFix {
/*    */   public ChunkDeleteIgnoredLightDataFix(Schema outputSchema) {
/* 12 */     super(outputSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder<?> sectionsFinder = chunkType.findField("sections");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteIgnoredLightDataFix", chunkType, chunk -> {
/*    */           boolean isLightOn = ((Dynamic)chunk.get(DSL.remainderFinder())).get("isLightOn").asBoolean(false);
/*    */           return !isLightOn ? chunk.updateTyped(sectionsFinder, ()) : chunk;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ChunkDeleteIgnoredLightDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */