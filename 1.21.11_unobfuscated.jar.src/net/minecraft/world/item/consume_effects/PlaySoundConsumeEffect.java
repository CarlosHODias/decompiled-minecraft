/*    */ package net.minecraft.world.item.consume_effects;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class PlaySoundConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final Holder<SoundEvent> sound;
/*    */   public static final com.mojang.serialization.MapCodec<PlaySoundConsumeEffect> CODEC;
/*    */   
/* 13 */   public PlaySoundConsumeEffect(Holder<SoundEvent> sound) { this.sound = sound; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect; } public Holder<SoundEvent> sound() { return this.sound; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundConsumeEffect::sound)).apply((com.mojang.datafixers.kinds.Applicative)i, PlaySoundConsumeEffect::new)); }
/*    */ 
/*    */   
/* 17 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PlaySoundConsumeEffect> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(SoundEvent.STREAM_CODEC, PlaySoundConsumeEffect::sound, PlaySoundConsumeEffect::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConsumeEffect.Type<PlaySoundConsumeEffect> getType() {
/* 24 */     return ConsumeEffect.Type.PLAY_SOUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean apply(net.minecraft.world.level.Level level, net.minecraft.world.item.ItemStack stack, LivingEntity user) {
/* 29 */     level.playSound(null, user.blockPosition(), (SoundEvent)this.sound.value(), user.getSoundSource(), 1.0F, 1.0F);
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/consume_effects/PlaySoundConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */