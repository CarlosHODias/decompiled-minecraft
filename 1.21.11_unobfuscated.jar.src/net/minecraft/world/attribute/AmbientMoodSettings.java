/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ public final class AmbientMoodSettings extends Record {
/*    */   private final net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> soundEvent;
/*    */   private final int tickDelay;
/*    */   private final int blockSearchExtent;
/*    */   private final double soundPositionOffset;
/*    */   public static final com.mojang.serialization.Codec<AmbientMoodSettings> CODEC;
/*    */   
/* 10 */   public AmbientMoodSettings(net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> soundEvent, int tickDelay, int blockSearchExtent, double soundPositionOffset) { this.soundEvent = soundEvent; this.tickDelay = tickDelay; this.blockSearchExtent = blockSearchExtent; this.soundPositionOffset = soundPositionOffset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientMoodSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings; } public net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> soundEvent() { return this.soundEvent; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientMoodSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientMoodSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int tickDelay() { return this.tickDelay; } public int blockSearchExtent() { return this.blockSearchExtent; } public double soundPositionOffset() { return this.soundPositionOffset; } static {
/* 11 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.sounds.SoundEvent.CODEC.fieldOf("sound").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.INT.fieldOf("tick_delay").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.INT.fieldOf("block_search_extent").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.DOUBLE.fieldOf("offset").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, AmbientMoodSettings::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS = new AmbientMoodSettings((net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent>)net.minecraft.sounds.SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/AmbientMoodSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */