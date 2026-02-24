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
/*    */ public class ItemShulkerBoxColorFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemShulkerBoxColorFix(Schema outputSchema, boolean changesType) {
/* 20 */     super(outputSchema, changesType);
/*    */   }
/*    */   
/* 23 */   public static final String[] NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box" };
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
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 44 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 46 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 47 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 48 */     OpticFinder<?> blockEntityF = tagF.type().findField("BlockEntityTag");
/*    */     
/* 50 */     return fixTypeEverywhereTyped("ItemShulkerBoxColorFix", itemStackType, input -> {
/*    */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/*    */           if (idOpt.isPresent() && Objects.equals(((Pair)idOpt.get()).getSecond(), "minecraft:shulker_box")) {
/*    */             Optional<? extends Typed<?>> tagOpt = input.getOptionalTyped(tagF);
/*    */             if (tagOpt.isPresent()) {
/*    */               Typed<?> tag = tagOpt.get();
/*    */               Optional<? extends Typed<?>> blockEntityOpt = tag.getOptionalTyped(blockEntityF);
/*    */               if (blockEntityOpt.isPresent()) {
/*    */                 Typed<?> blockEntity = blockEntityOpt.get();
/*    */                 Dynamic<?> blockEntityRest = (Dynamic)blockEntity.get(DSL.remainderFinder());
/*    */                 int color = blockEntityRest.get("Color").asInt(0);
/*    */                 blockEntityRest.remove("Color");
/*    */                 return input.set(tagF, tag.set(blockEntityF, blockEntity.set(DSL.remainderFinder(), blockEntityRest))).set(idF, Pair.of(References.ITEM_NAME.typeName(), NAMES_BY_COLOR[color % 16]));
/*    */               } 
/*    */             } 
/*    */           } 
/*    */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ItemShulkerBoxColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */