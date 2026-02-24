/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class FoodToConsumableFix
/*    */   extends DataFix {
/*    */   public FoodToConsumableFix(Schema outputSchema) {
/* 13 */     super(outputSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     return writeFixAndRead("Food to consumable fix", getInputSchema().getType(References.DATA_COMPONENTS), getOutputSchema().getType(References.DATA_COMPONENTS), components -> {
/*    */           Optional<? extends Dynamic<?>> foodComponent = components.get("minecraft:food").result();
/*    */           if (foodComponent.isPresent()) {
/*    */             float eatSeconds = ((Dynamic)foodComponent.get()).get("eat_seconds").asFloat(1.6F);
/*    */             Stream<? extends Dynamic<?>> effects = ((Dynamic)foodComponent.get()).get("effects").asStream(), onConsumeEffects = effects.map(());
/*    */             components = Dynamic.copyField(foodComponent.get(), "using_converts_to", components, "minecraft:use_remainder");
/*    */             components = components.set("minecraft:food", ((Dynamic)foodComponent.get()).remove("eat_seconds").remove("effects").remove("using_converts_to"));
/*    */             return components.set("minecraft:consumable", components.emptyMap().set("consume_seconds", components.createFloat(eatSeconds)).set("on_consume_effects", components.createList(onConsumeEffects)));
/*    */           } 
/*    */           return components;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/FoodToConsumableFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */