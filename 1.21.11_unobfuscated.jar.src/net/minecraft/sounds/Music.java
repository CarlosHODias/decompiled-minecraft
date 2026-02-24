/*    */ package net.minecraft.sounds;
/*    */ public final class Music extends Record { private final net.minecraft.core.Holder<SoundEvent> sound;
/*    */   private final int minDelay;
/*    */   private final int maxDelay;
/*    */   private final boolean replaceCurrentMusic;
/*    */   public static final com.mojang.serialization.Codec<Music> CODEC;
/*    */   
/*  8 */   public Music(net.minecraft.core.Holder<SoundEvent> sound, int minDelay, int maxDelay, boolean replaceCurrentMusic) { this.sound = sound; this.minDelay = minDelay; this.maxDelay = maxDelay; this.replaceCurrentMusic = replaceCurrentMusic; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/sounds/Music;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/sounds/Music; } public net.minecraft.core.Holder<SoundEvent> sound() { return this.sound; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/sounds/Music;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/Music; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/sounds/Music;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/sounds/Music;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public int minDelay() { return this.minDelay; } public int maxDelay() { return this.maxDelay; } public boolean replaceCurrentMusic() { return this.replaceCurrentMusic; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 14 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)SoundEvent.CODEC.fieldOf("sound").forGetter(Music::sound), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_delay").forGetter(Music::minDelay), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_delay").forGetter(Music::maxDelay), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("replace_current_music", false).forGetter(Music::replaceCurrentMusic)).apply((com.mojang.datafixers.kinds.Applicative)i, Music::new));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/sounds/Music.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */