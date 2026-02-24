/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemWaterPotionFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemWaterPotionFix(Schema outputSchema, boolean changesType) {
/* 19 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 26 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 27 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/*    */     
/* 29 */     return fixTypeEverywhereTyped("ItemWaterPotionFix", itemStackType, input -> {
/*    */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/*    */           if (idOpt.isPresent()) {
/*    */             String id = (String)((Pair)idOpt.get()).getSecond();
/*    */             if ("minecraft:potion".equals(id) || "minecraft:splash_potion".equals(id) || "minecraft:lingering_potion".equals(id) || "minecraft:tipped_arrow".equals(id)) {
/*    */               Typed<?> tag = input.getOrCreateTyped(tagF);
/*    */               Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder());
/*    */               if (tagRest.get("Potion").asString().result().isEmpty())
/*    */                 tagRest = tagRest.set("Potion", tagRest.createString("minecraft:water")); 
/*    */               return input.set(tagF, tag.set(DSL.remainderFinder(), tagRest));
/*    */             } 
/*    */           } 
/*    */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ItemWaterPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */