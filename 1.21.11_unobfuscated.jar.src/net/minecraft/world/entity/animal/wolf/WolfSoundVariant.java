/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class WolfSoundVariant extends Record {
/*    */   private final net.minecraft.core.Holder<SoundEvent> ambientSound;
/*    */   private final net.minecraft.core.Holder<SoundEvent> deathSound;
/*    */   private final net.minecraft.core.Holder<SoundEvent> growlSound;
/*    */   private final net.minecraft.core.Holder<SoundEvent> hurtSound;
/*    */   private final net.minecraft.core.Holder<SoundEvent> pantSound;
/*    */   private final net.minecraft.core.Holder<SoundEvent> whineSound;
/*    */   
/* 13 */   public WolfSoundVariant(net.minecraft.core.Holder<SoundEvent> ambientSound, net.minecraft.core.Holder<SoundEvent> deathSound, net.minecraft.core.Holder<SoundEvent> growlSound, net.minecraft.core.Holder<SoundEvent> hurtSound, net.minecraft.core.Holder<SoundEvent> pantSound, net.minecraft.core.Holder<SoundEvent> whineSound) { this.ambientSound = ambientSound; this.deathSound = deathSound; this.growlSound = growlSound; this.hurtSound = hurtSound; this.pantSound = pantSound; this.whineSound = whineSound; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant; } public net.minecraft.core.Holder<SoundEvent> ambientSound() { return this.ambientSound; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.Holder<SoundEvent> deathSound() { return this.deathSound; } public net.minecraft.core.Holder<SoundEvent> growlSound() { return this.growlSound; } public net.minecraft.core.Holder<SoundEvent> hurtSound() { return this.hurtSound; } public net.minecraft.core.Holder<SoundEvent> pantSound() { return this.pantSound; } public net.minecraft.core.Holder<SoundEvent> whineSound() { return this.whineSound; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final com.mojang.serialization.Codec<WolfSoundVariant> DIRECT_CODEC = getWolfSoundVariantCodec();
/* 22 */   public static final com.mojang.serialization.Codec<WolfSoundVariant> NETWORK_CODEC = getWolfSoundVariantCodec();
/* 23 */   public static final com.mojang.serialization.Codec<net.minecraft.core.Holder<WolfSoundVariant>> CODEC = (com.mojang.serialization.Codec<net.minecraft.core.Holder<WolfSoundVariant>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.WOLF_SOUND_VARIANT);
/* 24 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, net.minecraft.core.Holder<WolfSoundVariant>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.WOLF_SOUND_VARIANT);
/*    */   
/*    */   private static com.mojang.serialization.Codec<WolfSoundVariant> getWolfSoundVariantCodec() {
/* 27 */     return com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(WolfSoundVariant::ambientSound), (com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(WolfSoundVariant::deathSound), (com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("growl_sound").forGetter(WolfSoundVariant::growlSound), (com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(WolfSoundVariant::hurtSound), (com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("pant_sound").forGetter(WolfSoundVariant::pantSound), (com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("whine_sound").forGetter(WolfSoundVariant::whineSound)).apply((com.mojang.datafixers.kinds.Applicative)i, WolfSoundVariant::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/wolf/WolfSoundVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */