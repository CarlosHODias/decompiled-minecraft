/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Properties
/*    */   extends Record
/*    */ {
/*    */   private final boolean handAnimationOnSwap;
/*    */   private final boolean oversizedInGui;
/*    */   private final float swapAnimationScale;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Properties(boolean handAnimationOnSwap, boolean oversizedInGui, float swapAnimationScale) {
/* 33 */     this.handAnimationOnSwap = handAnimationOnSwap; this.oversizedInGui = oversizedInGui; this.swapAnimationScale = swapAnimationScale; } public boolean handAnimationOnSwap() { return this.handAnimationOnSwap; } public boolean oversizedInGui() { return this.oversizedInGui; } public float swapAnimationScale() { return this.swapAnimationScale; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static final Properties DEFAULT = new Properties(true, false, 1.0F); public static final MapCodec<Properties> MAP_CODEC;
/*    */   static {
/* 40 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(Properties::handAnimationOnSwap), (App)Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(Properties::oversizedInGui), (App)Codec.FLOAT.optionalFieldOf("swap_animation_scale", 1.0F).forGetter(Properties::swapAnimationScale)).apply((Applicative)i, Properties::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ClientItem$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */