/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ItemStackEnchantmentNamesFix extends com.mojang.datafixers.DataFix {
/*    */   static {
/* 17 */     MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
/*    */           map.put(0, "minecraft:protection");
/*    */           map.put(1, "minecraft:fire_protection");
/*    */           map.put(2, "minecraft:feather_falling");
/*    */           map.put(3, "minecraft:blast_protection");
/*    */           map.put(4, "minecraft:projectile_protection");
/*    */           map.put(5, "minecraft:respiration");
/*    */           map.put(6, "minecraft:aqua_affinity");
/*    */           map.put(7, "minecraft:thorns");
/*    */           map.put(8, "minecraft:depth_strider");
/*    */           map.put(9, "minecraft:frost_walker");
/*    */           map.put(10, "minecraft:binding_curse");
/*    */           map.put(16, "minecraft:sharpness");
/*    */           map.put(17, "minecraft:smite");
/*    */           map.put(18, "minecraft:bane_of_arthropods");
/*    */           map.put(19, "minecraft:knockback");
/*    */           map.put(20, "minecraft:fire_aspect");
/*    */           map.put(21, "minecraft:looting");
/*    */           map.put(22, "minecraft:sweeping");
/*    */           map.put(32, "minecraft:efficiency");
/*    */           map.put(33, "minecraft:silk_touch");
/*    */           map.put(34, "minecraft:unbreaking");
/*    */           map.put(35, "minecraft:fortune");
/*    */           map.put(48, "minecraft:power");
/*    */           map.put(49, "minecraft:punch");
/*    */           map.put(50, "minecraft:flame");
/*    */           map.put(51, "minecraft:infinity");
/*    */           map.put(61, "minecraft:luck_of_the_sea");
/*    */           map.put(62, "minecraft:lure");
/*    */           map.put(65, "minecraft:loyalty");
/*    */           map.put(66, "minecraft:impaling");
/*    */           map.put(67, "minecraft:riptide");
/*    */           map.put(68, "minecraft:channeling");
/*    */           map.put(70, "minecraft:mending");
/*    */           map.put(71, "minecraft:vanishing_curse");
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Int2ObjectMap<String> MAP;
/*    */ 
/*    */   
/*    */   public ItemStackEnchantmentNamesFix(Schema outputSchema, boolean changesType) {
/* 61 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected com.mojang.datafixers.TypeRewriteRule makeRule() {
/* 66 */     Type<?> item = getInputSchema().getType(References.ITEM_STACK);
/* 67 */     OpticFinder<?> tagFinder = item.findField("tag");
/* 68 */     return fixTypeEverywhereTyped("ItemStackEnchantmentFix", item, input -> tagFinder.updateTyped(tagFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> tag) {
/* 74 */     Objects.requireNonNull(tag); Optional<? extends Dynamic<?>> newEnch = tag.get("ench").asStreamOpt().map(s -> s.map(())).map(tag::createList).result();
/*    */     
/* 76 */     if (newEnch.isPresent()) {
/* 77 */       tag = tag.remove("ench").set("Enchantments", newEnch.get());
/*    */     }
/*    */     
/* 80 */     return tag.update("StoredEnchantments", list -> {
/*    */           Objects.requireNonNull(list);
/*    */           return DataFixUtils.orElse(list.asStreamOpt().map(()).map(list::createList).result(), list);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ItemStackEnchantmentNamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */