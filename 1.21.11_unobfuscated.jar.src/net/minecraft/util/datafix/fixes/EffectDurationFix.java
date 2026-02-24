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
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EffectDurationFix
/*    */   extends DataFix
/*    */ {
/* 21 */   private static final Set<String> POTION_ITEMS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EffectDurationFix(Schema outputSchema) {
/* 29 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 34 */     Schema inputSchema = getInputSchema();
/* 35 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 36 */     OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 37 */     OpticFinder<?> tagFinder = itemStackType.findField("tag");
/* 38 */     return TypeRewriteRule.seq(
/* 39 */         fixTypeEverywhereTyped("EffectDurationEntity", inputSchema.getType(References.ENTITY), input -> input.update(DSL.remainderFinder(), this::updateEntity)), new TypeRewriteRule[] {
/*    */ 
/*    */           
/* 42 */           fixTypeEverywhereTyped("EffectDurationPlayer", inputSchema.getType(References.PLAYER), input -> input.update(DSL.remainderFinder(), this::updateEntity)), 
/*    */ 
/*    */           
/* 45 */           fixTypeEverywhereTyped("EffectDurationItem", itemStackType, input -> {
/*    */               if (idFinder.getOptional(idFinder).filter(()).isPresent()) {
/*    */                 Optional<? extends Typed<?>> tag = idFinder.getOptionalTyped(idFinder);
/*    */                 if (tag.isPresent()) {
/*    */                   Dynamic<?> tagRest = (Dynamic)((Typed)tag.get()).get(DSL.remainderFinder());
/*    */                   Typed<?> newTag = ((Typed)tag.get()).set(DSL.remainderFinder(), tagRest.update("CustomPotionEffects", this::fix));
/*    */                   return idFinder.set(idFinder, newTag);
/*    */                 } 
/*    */               } 
/*    */               return idFinder;
/*    */             })
/*    */         });
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixEffect(Dynamic<?> effect) {
/* 60 */     return effect.update("FactorCalculationData", factorData -> {
/*    */           int timestamp = factorData.get("effect_changed_timestamp").asInt(-1);
/*    */           factorData = factorData.remove("effect_changed_timestamp");
/*    */           int duration = effect.get("Duration").asInt(-1), ticksActive = timestamp - duration;
/*    */           return factorData.set("ticks_active", factorData.createInt(ticksActive));
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> input) {
/* 70 */     return input.createList(input.asStream().map(this::fixEffect));
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateEntity(Dynamic<?> data) {
/* 75 */     data = data.update("Effects", this::fix);
/* 76 */     data = data.update("ActiveEffects", this::fix);
/* 77 */     data = data.update("CustomPotionEffects", this::fix);
/* 78 */     return data;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EffectDurationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */