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
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackMapIdFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemStackMapIdFix(Schema outputSchema, boolean changesType) {
/* 20 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 27 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 28 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/*    */     
/* 30 */     return fixTypeEverywhereTyped("ItemInstanceMapIdFix", itemStackType, input -> {
/*    */           Optional<Pair<String, String>> id = input.getOptional(idF);
/*    */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), "minecraft:filled_map")) {
/*    */             Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/*    */             Typed<?> tag = input.getOrCreateTyped(tagF);
/*    */             Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder());
/*    */             tagRest = tagRest.set("map", tagRest.createInt(rest.get("Damage").asInt(0)));
/*    */             return input.set(tagF, tag.set(DSL.remainderFinder(), tagRest));
/*    */           } 
/*    */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ItemStackMapIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */