/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class MobEffectsPredicate extends Record {
/*    */   private final Map<Holder<MobEffect>, MobEffectInstancePredicate> effectMap;
/*    */   
/* 16 */   public MobEffectsPredicate(Map<Holder<MobEffect>, MobEffectInstancePredicate> effectMap) { this.effectMap = effectMap; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MobEffectsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MobEffectsPredicate; } public Map<Holder<MobEffect>, MobEffectInstancePredicate> effectMap() { return this.effectMap; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MobEffectsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MobEffectsPredicate; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MobEffectsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MobEffectsPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 19 */   } public static final Codec<MobEffectsPredicate> CODEC = Codec.unboundedMap(MobEffect.CODEC, MobEffectInstancePredicate.CODEC).xmap(MobEffectsPredicate::new, MobEffectsPredicate::effectMap);
/*    */   
/*    */   public boolean matches(net.minecraft.world.entity.Entity entity) {
/* 22 */     if (entity instanceof LivingEntity) { LivingEntity living = (LivingEntity)entity; if (matches(living.getActiveEffectsMap())); }  return false;
/*    */   }
/*    */   
/*    */   public boolean matches(LivingEntity entity) {
/* 26 */     return matches(entity.getActiveEffectsMap());
/*    */   }
/*    */   
/*    */   public boolean matches(Map<Holder<MobEffect>, MobEffectInstance> effects) {
/* 30 */     for (Map.Entry<Holder<MobEffect>, MobEffectInstancePredicate> entry : this.effectMap.entrySet()) {
/* 31 */       MobEffectInstance instance = effects.get(entry.getKey());
/* 32 */       if (!((MobEffectInstancePredicate)entry.getValue()).matches(instance)) {
/* 33 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 41 */     private final com.google.common.collect.ImmutableMap.Builder<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = com.google.common.collect.ImmutableMap.builder();
/*    */     
/*    */     public static Builder effects() {
/* 44 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder and(Holder<MobEffect> effect) {
/* 48 */       this.effectMap.put(effect, new MobEffectsPredicate.MobEffectInstancePredicate());
/* 49 */       return this;
/*    */     }
/*    */     
/*    */     public Builder and(Holder<MobEffect> effect, MobEffectsPredicate.MobEffectInstancePredicate predicate) {
/* 53 */       this.effectMap.put(effect, predicate);
/* 54 */       return this;
/*    */     }
/*    */     
/*    */     public Optional<MobEffectsPredicate> build() {
/* 58 */       return Optional.of(new MobEffectsPredicate((Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate>)this.effectMap.build()));
/*    */     } }
/*    */   public static final class MobEffectInstancePredicate extends Record { private final MinMaxBounds.Ints amplifier; private final MinMaxBounds.Ints duration; private final Optional<Boolean> ambient; private final Optional<Boolean> visible; public static final Codec<MobEffectInstancePredicate> CODEC;
/*    */     
/* 62 */     public MobEffectInstancePredicate(MinMaxBounds.Ints amplifier, MinMaxBounds.Ints duration, Optional<Boolean> ambient, Optional<Boolean> visible) { this.amplifier = amplifier; this.duration = duration; this.ambient = ambient; this.visible = visible; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MobEffectsPredicate$MobEffectInstancePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MobEffectsPredicate$MobEffectInstancePredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MobEffectsPredicate$MobEffectInstancePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MobEffectsPredicate$MobEffectInstancePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MobEffectsPredicate$MobEffectInstancePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/MobEffectsPredicate$MobEffectInstancePredicate;
/* 62 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints amplifier() { return this.amplifier; } public MinMaxBounds.Ints duration() { return this.duration; } public Optional<Boolean> ambient() { return this.ambient; } public Optional<Boolean> visible() { return this.visible; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 68 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("amplifier", MinMaxBounds.Ints.ANY).forGetter(MobEffectInstancePredicate::amplifier), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("duration", MinMaxBounds.Ints.ANY).forGetter(MobEffectInstancePredicate::duration), (App)Codec.BOOL.optionalFieldOf("ambient").forGetter(MobEffectInstancePredicate::ambient), (App)Codec.BOOL.optionalFieldOf("visible").forGetter(MobEffectInstancePredicate::visible)).apply((com.mojang.datafixers.kinds.Applicative)i, MobEffectInstancePredicate::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public MobEffectInstancePredicate() {
/* 76 */       this(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, Optional.empty(), Optional.empty());
/*    */     }
/*    */     
/*    */     public boolean matches(MobEffectInstance instance) {
/* 80 */       if (instance == null) {
/* 81 */         return false;
/*    */       }
/* 83 */       if (!this.amplifier.matches(instance.getAmplifier())) {
/* 84 */         return false;
/*    */       }
/* 86 */       if (!this.duration.matches(instance.getDuration())) {
/* 87 */         return false;
/*    */       }
/* 89 */       if (this.ambient.isPresent() && (Boolean)this.ambient.get() != instance.isAmbient()) {
/* 90 */         return false;
/*    */       }
/* 92 */       if (this.visible.isPresent() && (Boolean)this.visible.get() != instance.isVisible()) {
/* 93 */         return false;
/*    */       }
/* 95 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/MobEffectsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */