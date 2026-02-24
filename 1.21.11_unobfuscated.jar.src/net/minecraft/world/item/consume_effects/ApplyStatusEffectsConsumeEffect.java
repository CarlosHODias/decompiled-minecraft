/*    */ package net.minecraft.world.item.consume_effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class ApplyStatusEffectsConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final List<MobEffectInstance> effects;
/*    */   private final float probability;
/*    */   public static final com.mojang.serialization.MapCodec<ApplyStatusEffectsConsumeEffect> CODEC;
/*    */   
/* 16 */   public ApplyStatusEffectsConsumeEffect(List<MobEffectInstance> effects, float probability) { this.effects = effects; this.probability = probability; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect; } public List<MobEffectInstance> effects() { return this.effects; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public float probability() { return this.probability; } static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(ApplyStatusEffectsConsumeEffect::effects), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("probability", 1.0F).forGetter(ApplyStatusEffectsConsumeEffect::probability)).apply((com.mojang.datafixers.kinds.Applicative)i, ApplyStatusEffectsConsumeEffect::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ApplyStatusEffectsConsumeEffect> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 23 */       MobEffectInstance.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), ApplyStatusEffectsConsumeEffect::effects, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ApplyStatusEffectsConsumeEffect::probability, ApplyStatusEffectsConsumeEffect::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ApplyStatusEffectsConsumeEffect(MobEffectInstance effect, float probability) {
/* 29 */     this(List.of(effect), probability);
/*    */   }
/*    */   
/*    */   public ApplyStatusEffectsConsumeEffect(List<MobEffectInstance> effects) {
/* 33 */     this(effects, 1.0F);
/*    */   }
/*    */   
/*    */   public ApplyStatusEffectsConsumeEffect(MobEffectInstance effect) {
/* 37 */     this(effect, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public ConsumeEffect.Type<ApplyStatusEffectsConsumeEffect> getType() {
/* 42 */     return ConsumeEffect.Type.APPLY_EFFECTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean apply(net.minecraft.world.level.Level level, net.minecraft.world.item.ItemStack stack, LivingEntity user) {
/* 47 */     if (user.getRandom().nextFloat() >= this.probability) {
/* 48 */       return false;
/*    */     }
/*    */     
/*    */     boolean anyApplied = false;
/* 52 */     for (MobEffectInstance effect : this.effects) {
/* 53 */       if (user.addEffect(new MobEffectInstance(effect))) {
/* 54 */         anyApplied = true;
/*    */       }
/*    */     } 
/*    */     
/* 58 */     return anyApplied;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */