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
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackCustomNameToOverrideComponentFix extends DataFix {
/*    */   public ItemStackCustomNameToOverrideComponentFix(Schema outputSchema) {
/* 23 */     super(outputSchema, false);
/*    */   }
/*    */   
/* 26 */   private static final Set<String> MAP_NAMES = Set.of(new String[] { "filled_map.buried_treasure", "filled_map.explorer_jungle", "filled_map.explorer_swamp", "filled_map.mansion", "filled_map.monument", "filled_map.trial_chambers", "filled_map.village_desert", "filled_map.village_plains", "filled_map.village_savanna", "filled_map.village_snowy", "filled_map.village_taiga" });
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
/*    */   public final TypeRewriteRule makeRule() {
/* 42 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 44 */     OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 46 */     OpticFinder<?> componentsFinder = itemStackType.findField("components");
/*    */     
/* 48 */     return fixTypeEverywhereTyped("ItemStack custom_name to item_name component fix", itemStackType, input -> {
/*    */           Optional<Pair<String, String>> id = input.getOptional(idFinder);
/*    */           Optional<String> maybeId = id.map(Pair::getSecond);
/*    */           return maybeId.filter(()).isPresent() ? input.updateTyped(componentsFinder, ItemStackCustomNameToOverrideComponentFix::fixBanner) : (maybeId.filter(()).isPresent() ? input.updateTyped(componentsFinder, ItemStackCustomNameToOverrideComponentFix::fixMap) : input);
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
/*    */   
/*    */   private static <T> Typed<T> fixMap(Typed<T> value) {
/* 65 */     Objects.requireNonNull(MAP_NAMES); return fixCustomName(value, MAP_NAMES::contains);
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixBanner(Typed<T> value) {
/* 69 */     return fixCustomName(value, e -> e.equals("block.minecraft.ominous_banner"));
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixCustomName(Typed<T> typed, Predicate<String> expectedTranslationKey) {
/* 73 */     return Util.writeAndReadTypedOrThrow(typed, typed.getType(), value -> {
/*    */           OptionalDynamic<?> customNameTag = value.get("minecraft:custom_name");
/*    */           Optional<String> hasCorrectTranslationKey = customNameTag.asString().result().flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(expectedTranslationKey);
/*    */           return hasCorrectTranslationKey.isPresent() ? value.renameField("minecraft:custom_name", "minecraft:item_name") : value;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ItemStackCustomNameToOverrideComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */