/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public final class ConditionalEffect<T> extends Record {
/*    */   private final T effect;
/*    */   private final java.util.Optional<LootItemCondition> requirements;
/*    */   
/* 14 */   public ConditionalEffect(T effect, java.util.Optional<LootItemCondition> requirements) { this.effect = effect; this.requirements = requirements; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>; } public T effect() { return this.effect; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>; } public java.util.Optional<LootItemCondition> requirements() { return this.requirements; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Codec<LootItemCondition> conditionCodec(ContextKeySet paramsSet) {
/* 19 */     return LootItemCondition.DIRECT_CODEC.validate(condition -> {
/*    */           ProblemReporter.Collector problemCollector = new ProblemReporter.Collector();
/*    */           ValidationContext validationContext = new ValidationContext((ProblemReporter)problemCollector, paramsSet);
/*    */           condition.validate(validationContext);
/*    */           return !problemCollector.isEmpty() ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(condition);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec, ContextKeySet paramsSet) {
/* 31 */     return RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)effectCodec.fieldOf("effect").forGetter(ConditionalEffect::effect), (com.mojang.datafixers.kinds.App)conditionCodec(paramsSet).optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements)).apply((com.mojang.datafixers.kinds.Applicative)i, ConditionalEffect::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.world.level.storage.loot.LootContext context) {
/* 38 */     if (this.requirements.isEmpty()) {
/* 39 */       return true;
/*    */     }
/* 41 */     return ((LootItemCondition)this.requirements.get()).test(context);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/ConditionalEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */