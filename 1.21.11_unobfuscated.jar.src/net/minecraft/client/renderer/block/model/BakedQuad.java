/*    */ package net.minecraft.client.renderer.block.model;
/*    */ public final class BakedQuad extends Record { private final org.joml.Vector3fc position0; private final org.joml.Vector3fc position1; private final org.joml.Vector3fc position2; private final org.joml.Vector3fc position3;
/*    */   private final long packedUV0;
/*    */   private final long packedUV1;
/*    */   private final long packedUV2;
/*    */   
/*  7 */   public BakedQuad(org.joml.Vector3fc position0, org.joml.Vector3fc position1, org.joml.Vector3fc position2, org.joml.Vector3fc position3, long packedUV0, long packedUV1, long packedUV2, long packedUV3, int tintIndex, net.minecraft.core.Direction direction, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite, boolean shade, int lightEmission) { this.position0 = position0; this.position1 = position1; this.position2 = position2; this.position3 = position3; this.packedUV0 = packedUV0; this.packedUV1 = packedUV1; this.packedUV2 = packedUV2; this.packedUV3 = packedUV3; this.tintIndex = tintIndex; this.direction = direction; this.sprite = sprite; this.shade = shade; this.lightEmission = lightEmission; } private final long packedUV3; private final int tintIndex; private final net.minecraft.core.Direction direction; private final net.minecraft.client.renderer.texture.TextureAtlasSprite sprite; private final boolean shade; private final int lightEmission; public static final int VERTEX_COUNT = 4; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BakedQuad;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BakedQuad; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BakedQuad;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BakedQuad; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BakedQuad;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BakedQuad;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public org.joml.Vector3fc position0() { return this.position0; } public org.joml.Vector3fc position1() { return this.position1; } public org.joml.Vector3fc position2() { return this.position2; } public org.joml.Vector3fc position3() { return this.position3; } public long packedUV0() { return this.packedUV0; } public long packedUV1() { return this.packedUV1; } public long packedUV2() { return this.packedUV2; } public long packedUV3() { return this.packedUV3; } public int tintIndex() { return this.tintIndex; } public net.minecraft.core.Direction direction() { return this.direction; } public net.minecraft.client.renderer.texture.TextureAtlasSprite sprite() { return this.sprite; } public boolean shade() { return this.shade; } public int lightEmission() { return this.lightEmission; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTinted() {
/* 27 */     return (this.tintIndex != -1);
/*    */   }
/*    */   
/*    */   public org.joml.Vector3fc position(int vertex) {
/*    */     // Byte code:
/*    */     //   0: iload_1
/*    */     //   1: tableswitch default -> 60, 0 -> 32, 1 -> 39, 2 -> 46, 3 -> 53
/*    */     //   32: aload_0
/*    */     //   33: getfield position0 : Lorg/joml/Vector3fc;
/*    */     //   36: goto -> 69
/*    */     //   39: aload_0
/*    */     //   40: getfield position1 : Lorg/joml/Vector3fc;
/*    */     //   43: goto -> 69
/*    */     //   46: aload_0
/*    */     //   47: getfield position2 : Lorg/joml/Vector3fc;
/*    */     //   50: goto -> 69
/*    */     //   53: aload_0
/*    */     //   54: getfield position3 : Lorg/joml/Vector3fc;
/*    */     //   57: goto -> 69
/*    */     //   60: new java/lang/IndexOutOfBoundsException
/*    */     //   63: dup
/*    */     //   64: iload_1
/*    */     //   65: invokespecial <init> : (I)V
/*    */     //   68: athrow
/*    */     //   69: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     //   #32	-> 32
/*    */     //   #33	-> 39
/*    */     //   #34	-> 46
/*    */     //   #35	-> 53
/*    */     //   #36	-> 60
/*    */     //   #31	-> 69
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	70	0	this	Lnet/minecraft/client/renderer/block/model/BakedQuad;
/*    */     //   0	70	1	vertex	I
/*    */   }
/*    */   
/*    */   public long packedUV(int vertex) {
/*    */     // Byte code:
/*    */     //   0: iload_1
/*    */     //   1: tableswitch default -> 60, 0 -> 32, 1 -> 39, 2 -> 46, 3 -> 53
/*    */     //   32: aload_0
/*    */     //   33: getfield packedUV0 : J
/*    */     //   36: goto -> 69
/*    */     //   39: aload_0
/*    */     //   40: getfield packedUV1 : J
/*    */     //   43: goto -> 69
/*    */     //   46: aload_0
/*    */     //   47: getfield packedUV2 : J
/*    */     //   50: goto -> 69
/*    */     //   53: aload_0
/*    */     //   54: getfield packedUV3 : J
/*    */     //   57: goto -> 69
/*    */     //   60: new java/lang/IndexOutOfBoundsException
/*    */     //   63: dup
/*    */     //   64: iload_1
/*    */     //   65: invokespecial <init> : (I)V
/*    */     //   68: athrow
/*    */     //   69: lreturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     //   #42	-> 32
/*    */     //   #43	-> 39
/*    */     //   #44	-> 46
/*    */     //   #45	-> 53
/*    */     //   #46	-> 60
/*    */     //   #41	-> 69
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	70	0	this	Lnet/minecraft/client/renderer/block/model/BakedQuad;
/*    */     //   0	70	1	vertex	I
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BakedQuad.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */