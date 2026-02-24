/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BedItemColorFix
/*    */   extends DataFix {
/*    */   public BedItemColorFix(Schema outputSchema, boolean changesType) {
/* 18 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 25 */     return fixTypeEverywhereTyped("BedItemColorFix", getInputSchema().getType(References.ITEM_STACK), input -> {
/*    */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/*    */           if (idOpt.isPresent() && Objects.equals(((Pair)idOpt.get()).getSecond(), "minecraft:bed")) {
/*    */             Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/*    */             if (tag.get("Damage").asInt(0) == 0)
/*    */               return input.set(DSL.remainderFinder(), tag.set("Damage", tag.createShort((short)14))); 
/*    */           } 
/*    */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BedItemColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */