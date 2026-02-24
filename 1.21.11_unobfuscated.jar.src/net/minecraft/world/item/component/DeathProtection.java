/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*    */ 
/*    */ public final class DeathProtection extends Record {
/*    */   private final List<ConsumeEffect> deathEffects;
/*    */   public static final com.mojang.serialization.Codec<DeathProtection> CODEC;
/*    */   
/* 19 */   public DeathProtection(List<ConsumeEffect> deathEffects) { this.deathEffects = deathEffects; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/DeathProtection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/item/component/DeathProtection; } public List<ConsumeEffect> deathEffects() { return this.deathEffects; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/DeathProtection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DeathProtection; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/DeathProtection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/DeathProtection;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ConsumeEffect.CODEC.listOf().optionalFieldOf("death_effects", List.of()).forGetter(DeathProtection::deathEffects)).apply((Applicative)i, DeathProtection::new));
/*    */   }
/*    */ 
/*    */   
/* 26 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, DeathProtection> STREAM_CODEC = StreamCodec.composite(
/* 27 */       ConsumeEffect.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), DeathProtection::deathEffects, DeathProtection::new);
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final DeathProtection TOTEM_OF_UNDYING = new DeathProtection((List)List.of(new net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect(), new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(
/*    */           
/* 33 */           List.of(new MobEffectInstance(MobEffects.REGENERATION, 900, 1), new MobEffectInstance(MobEffects.ABSORPTION, 100, 1), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void applyEffects(ItemStack itemStack, LivingEntity entity) {
/* 41 */     for (ConsumeEffect effect : this.deathEffects)
/* 42 */       effect.apply(entity.level(), itemStack, entity); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/DeathProtection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */