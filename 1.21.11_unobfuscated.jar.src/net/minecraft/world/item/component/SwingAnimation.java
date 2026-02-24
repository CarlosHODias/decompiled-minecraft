/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ 
/*    */ public final class SwingAnimation extends Record {
/*    */   private final SwingAnimationType type;
/*    */   private final int duration;
/*    */   
/* 11 */   public SwingAnimation(SwingAnimationType type, int duration) { this.type = type; this.duration = duration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SwingAnimation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/component/SwingAnimation; } public SwingAnimationType type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SwingAnimation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SwingAnimation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SwingAnimation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SwingAnimation;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public int duration() { return this.duration; }
/* 12 */    public static final SwingAnimation DEFAULT = new SwingAnimation(SwingAnimationType.WHACK, 6); public static final com.mojang.serialization.Codec<SwingAnimation> CODEC; static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SwingAnimationType.CODEC.optionalFieldOf("type", DEFAULT.type).forGetter(SwingAnimation::type), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("duration", DEFAULT.duration).forGetter(SwingAnimation::duration)).apply((com.mojang.datafixers.kinds.Applicative)i, SwingAnimation::new));
/*    */   }
/*    */ 
/*    */   
/* 17 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, SwingAnimation> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(SwingAnimationType.STREAM_CODEC, SwingAnimation::type, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, SwingAnimation::duration, SwingAnimation::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/SwingAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */