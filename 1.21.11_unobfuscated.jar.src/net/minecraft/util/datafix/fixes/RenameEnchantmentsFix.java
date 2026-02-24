/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RenameEnchantmentsFix extends com.mojang.datafixers.DataFix {
/*    */   final String name;
/*    */   
/*    */   public RenameEnchantmentsFix(Schema outputSchema, String name, Map<String, String> renames) {
/* 20 */     super(outputSchema, false);
/* 21 */     this.name = name;
/* 22 */     this.renames = renames;
/*    */   }
/*    */   final Map<String, String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> item = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder<?> tagFinder = item.findField("tag");
/* 29 */     return fixTypeEverywhereTyped(this.name, item, input -> tagFinder.updateTyped(tagFinder, ()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> tag) {
/* 33 */     tag = fixEnchantmentList(tag, "Enchantments");
/* 34 */     tag = fixEnchantmentList(tag, "StoredEnchantments");
/* 35 */     return tag;
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixEnchantmentList(Dynamic<?> itemStack, String field) {
/* 39 */     return itemStack.update(field, tag -> {
/*    */           Objects.requireNonNull(tag);
/*    */           return tag.asStreamOpt().map(()).map(tag::createList).mapOrElse(Function.identity(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/RenameEnchantmentsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */