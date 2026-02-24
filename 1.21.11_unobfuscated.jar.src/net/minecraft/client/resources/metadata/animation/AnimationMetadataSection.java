/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ public final class AnimationMetadataSection extends Record {
/*    */   private final java.util.Optional<java.util.List<AnimationFrame>> frames;
/*    */   private final java.util.Optional<Integer> frameWidth;
/*    */   private final java.util.Optional<Integer> frameHeight;
/*    */   private final int defaultFrameTime;
/*    */   private final boolean interpolatedFrames;
/*    */   public static final com.mojang.serialization.Codec<AnimationMetadataSection> CODEC;
/*    */   
/* 11 */   public AnimationMetadataSection(java.util.Optional<java.util.List<AnimationFrame>> frames, java.util.Optional<Integer> frameWidth, java.util.Optional<Integer> frameHeight, int defaultFrameTime, boolean interpolatedFrames) { this.frames = frames; this.frameWidth = frameWidth; this.frameHeight = frameHeight; this.defaultFrameTime = defaultFrameTime; this.interpolatedFrames = interpolatedFrames; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection; } public java.util.Optional<java.util.List<AnimationFrame>> frames() { return this.frames; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<Integer> frameWidth() { return this.frameWidth; } public java.util.Optional<Integer> frameHeight() { return this.frameHeight; } public int defaultFrameTime() { return this.defaultFrameTime; } public boolean interpolatedFrames() { return this.interpolatedFrames; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 18 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)AnimationFrame.CODEC.listOf().optionalFieldOf("frames").forGetter(AnimationMetadataSection::frames), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("width").forGetter(AnimationMetadataSection::frameWidth), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("height").forGetter(AnimationMetadataSection::frameHeight), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("frametime", 1).forGetter(AnimationMetadataSection::defaultFrameTime), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("interpolate", false).forGetter(AnimationMetadataSection::interpolatedFrames)).apply((com.mojang.datafixers.kinds.Applicative)i, AnimationMetadataSection::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<AnimationMetadataSection> TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("animation", CODEC);
/*    */   
/*    */   public FrameSize calculateFrameSize(int spriteWidth, int spriteHeight) {
/* 29 */     if (this.frameWidth.isPresent()) {
/* 30 */       if (this.frameHeight.isPresent()) {
/* 31 */         return new FrameSize((Integer)this.frameWidth.get(), (Integer)this.frameHeight.get());
/*    */       }
/*    */ 
/*    */       
/* 35 */       return new FrameSize((Integer)this.frameWidth.get(), spriteHeight);
/*    */     } 
/*    */     
/* 38 */     if (this.frameHeight.isPresent())
/*    */     {
/* 40 */       return new FrameSize(spriteWidth, (Integer)this.frameHeight.get());
/*    */     }
/*    */ 
/*    */     
/* 44 */     int minDimension = Math.min(spriteWidth, spriteHeight);
/* 45 */     return new FrameSize(minDimension, minDimension);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/metadata/animation/AnimationMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */