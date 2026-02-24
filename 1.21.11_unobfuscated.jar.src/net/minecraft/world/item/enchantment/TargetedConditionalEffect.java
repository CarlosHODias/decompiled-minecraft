/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class TargetedConditionalEffect<T> extends Record {
/*    */   private final EnchantmentTarget enchanted;
/*    */   private final EnchantmentTarget affected;
/*    */   private final T effect;
/*    */   private final java.util.Optional<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> requirements;
/*    */   
/* 12 */   public TargetedConditionalEffect(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect, java.util.Optional<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> requirements) { this.enchanted = enchanted; this.affected = affected; this.effect = effect; this.requirements = requirements; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>; } public EnchantmentTarget enchanted() { return this.enchanted; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>; } public EnchantmentTarget affected() { return this.affected; } public T effect() { return this.effect; } public java.util.Optional<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> requirements() { return this.requirements; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <S> com.mojang.serialization.Codec<TargetedConditionalEffect<S>> codec(com.mojang.serialization.Codec<S> effectCodec, net.minecraft.util.context.ContextKeySet paramsSet) {
/* 19 */     return RecordCodecBuilder.create(i -> i.group((App)EnchantmentTarget.CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted), (App)EnchantmentTarget.CODEC.fieldOf("affected").forGetter(TargetedConditionalEffect::affected), (App)effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect), (App)ConditionalEffect.conditionCodec(paramsSet).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)).apply((com.mojang.datafixers.kinds.Applicative)i, TargetedConditionalEffect::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <S> com.mojang.serialization.Codec<TargetedConditionalEffect<S>> equipmentDropsCodec(com.mojang.serialization.Codec<S> effectCodec, net.minecraft.util.context.ContextKeySet paramsSet) {
/* 28 */     return RecordCodecBuilder.create(i -> i.group((App)EnchantmentTarget.CODEC.validate(()).fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted), (App)effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect), (App)ConditionalEffect.conditionCodec(paramsSet).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.world.level.storage.loot.LootContext context) {
/* 36 */     if (this.requirements.isEmpty()) {
/* 37 */       return true;
/*    */     }
/* 39 */     return ((net.minecraft.world.level.storage.loot.predicates.LootItemCondition)this.requirements.get()).test(context);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/TargetedConditionalEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */