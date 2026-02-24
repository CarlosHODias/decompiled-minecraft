/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemBannerColorFix
/*    */   extends DataFix {
/*    */   public ItemBannerColorFix(Schema outputSchema, boolean changesType) {
/* 22 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 29 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 30 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 31 */     OpticFinder<?> blockEntityF = tagF.type().findField("BlockEntityTag");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("ItemBannerColorFix", itemStackType, input -> {
/*    */           Optional<Pair<String, String>> id = input.getOptional(idF);
/*    */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), "minecraft:banner")) {
/*    */             Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/*    */             Optional<? extends Typed<?>> tagOpt = input.getOptionalTyped(tagF);
/*    */             if (tagOpt.isPresent()) {
/*    */               Typed<?> tag = tagOpt.get();
/*    */               Optional<? extends Typed<?>> blockEntityOpt = tag.getOptionalTyped(blockEntityF);
/*    */               if (blockEntityOpt.isPresent()) {
/*    */                 Typed<?> blockEntity = blockEntityOpt.get();
/*    */                 Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder()), blockEntityRest = (Dynamic)blockEntity.getOrCreate(DSL.remainderFinder());
/*    */                 if (blockEntityRest.get("Base").asNumber().result().isPresent()) {
/*    */                   rest = rest.set("Damage", rest.createShort((short)(blockEntityRest.get("Base").asInt(0) & 0xF)));
/*    */                   Optional<? extends Dynamic<?>> displayOptional = tagRest.get("display").result();
/*    */                   if (displayOptional.isPresent()) {
/*    */                     Dynamic<?> display = displayOptional.get(), pickMarker = display.createMap((Map)ImmutableMap.of(display.createString("Lore"), display.createList(Stream.of(display.createString("(+NBT")))));
/*    */                     if (Objects.equals(display, pickMarker))
/*    */                       return input.set(DSL.remainderFinder(), rest); 
/*    */                   } 
/*    */                   blockEntityRest.remove("Base");
/*    */                   return input.set(DSL.remainderFinder(), rest).set(tagF, tag.set(blockEntityF, blockEntity.set(DSL.remainderFinder(), blockEntityRest)));
/*    */                 } 
/*    */               } 
/*    */             } 
/*    */             return input.set(DSL.remainderFinder(), rest);
/*    */           } 
/*    */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ItemBannerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */