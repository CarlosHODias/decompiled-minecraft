/*   */ package net.minecraft.world.attribute;
/*   */ 
/*   */ public final class AmbientAdditionsSettings extends Record {
/*   */   private final net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> soundEvent;
/*   */   private final double tickChance;
/*   */   public static final com.mojang.serialization.Codec<AmbientAdditionsSettings> CODEC;
/*   */   
/* 8 */   public AmbientAdditionsSettings(net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> soundEvent, double tickChance) { this.soundEvent = soundEvent; this.tickChance = tickChance; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientAdditionsSettings;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 8 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientAdditionsSettings; } public net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> soundEvent() { return this.soundEvent; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientAdditionsSettings;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientAdditionsSettings; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientAdditionsSettings;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientAdditionsSettings;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } public double tickChance() { return this.tickChance; } static {
/* 9 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.sounds.SoundEvent.CODEC.fieldOf("sound").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.DOUBLE.fieldOf("tick_chance").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, AmbientAdditionsSettings::new));
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/AmbientAdditionsSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */