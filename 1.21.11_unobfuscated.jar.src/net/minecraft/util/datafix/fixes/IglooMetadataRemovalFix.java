/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class IglooMetadataRemovalFix extends com.mojang.datafixers.DataFix {
/*    */   public IglooMetadataRemovalFix(Schema outputSchema, boolean changesType) {
/* 12 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> type = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 18 */     return fixTypeEverywhereTyped("IglooMetadataRemovalFix", type, typed -> typed.update(DSL.remainderFinder(), IglooMetadataRemovalFix::fixTag));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixTag(Dynamic<T> input) {
/* 22 */     boolean isIglooOnly = (Boolean)input.get("Children").asStreamOpt().map(s -> s.allMatch(IglooMetadataRemovalFix::isIglooPiece)).result().orElse(false);
/*    */     
/* 24 */     if (isIglooOnly) {
/* 25 */       return input.set("id", input.createString("Igloo")).remove("Children");
/*    */     }
/* 27 */     return input.update("Children", IglooMetadataRemovalFix::removeIglooPieces);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> removeIglooPieces(Dynamic<T> children) {
/* 32 */     java.util.Objects.requireNonNull(children); return children.asStreamOpt().map(s -> s.filter(())).map(children::createList).result().orElse(children);
/*    */   }
/*    */   
/*    */   private static boolean isIglooPiece(Dynamic<?> tag) {
/* 36 */     return tag.get("id").asString("").equals("Iglu");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/IglooMetadataRemovalFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */