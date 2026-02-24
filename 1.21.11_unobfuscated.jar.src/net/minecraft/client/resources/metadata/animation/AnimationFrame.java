/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ 
/*    */ public final class AnimationFrame extends Record {
/*    */   private final int index;
/*    */   private final java.util.Optional<Integer> time;
/*    */   public static final com.mojang.serialization.Codec<AnimationFrame> FULL_CODEC;
/*    */   public static final com.mojang.serialization.Codec<AnimationFrame> CODEC;
/*    */   
/* 10 */   public AnimationFrame(int index, java.util.Optional<Integer> time) { this.index = index; this.time = time; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationFrame; } public int index() { return this.index; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationFrame; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<Integer> time() { return this.time; }
/*    */ 
/*    */   
/*    */   static {
/* 14 */     FULL_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(AnimationFrame::index), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("time").forGetter(AnimationFrame::time)).apply((com.mojang.datafixers.kinds.Applicative)i, AnimationFrame::new));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 22 */     CODEC = com.mojang.serialization.Codec.either(net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT, FULL_CODEC).xmap(either -> (AnimationFrame)either.map(AnimationFrame::new, ()), frame -> frame.time.isPresent() ? com.mojang.datafixers.util.Either.right(frame) : com.mojang.datafixers.util.Either.left(frame.index));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AnimationFrame(int index) {
/* 28 */     this(index, java.util.Optional.empty());
/*    */   }
/*    */   
/*    */   public int timeOr(int defaultFrameTime) {
/* 32 */     return (Integer)this.time.orElse(defaultFrameTime);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/metadata/animation/AnimationFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */