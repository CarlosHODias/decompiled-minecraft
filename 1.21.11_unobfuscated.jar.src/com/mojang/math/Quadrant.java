/*    */ package com.mojang.math;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public enum Quadrant
/*    */ {
/*  9 */   R0(0, OctahedralGroup.IDENTITY, OctahedralGroup.IDENTITY, OctahedralGroup.IDENTITY),
/* 10 */   R90(1, OctahedralGroup.BLOCK_ROT_X_90, OctahedralGroup.BLOCK_ROT_Y_90, OctahedralGroup.BLOCK_ROT_Z_90),
/* 11 */   R180(2, OctahedralGroup.BLOCK_ROT_X_180, OctahedralGroup.BLOCK_ROT_Y_180, OctahedralGroup.BLOCK_ROT_Z_180),
/* 12 */   R270(3, OctahedralGroup.BLOCK_ROT_X_270, OctahedralGroup.BLOCK_ROT_Y_270, OctahedralGroup.BLOCK_ROT_Z_270);
/*    */   public static final Codec<Quadrant> CODEC; public final int shift; public final OctahedralGroup rotationX; public final OctahedralGroup rotationY; public final OctahedralGroup rotationZ;
/*    */   
/* 15 */   static { CODEC = Codec.INT.comapFlatMap(degrees -> { switch (Mth.positiveModulo(degrees, 360)) { case 0:
/*    */             
/*    */             case 90:
/*    */             
/*    */             case 180:
/*    */             
/*    */             case 270:
/*    */             
/*    */             default:
/*    */               break; }
/*    */            return DataResult.error(());
/*    */         }, quadrant -> { switch (quadrant.ordinal()) { default:
/*    */               throw new MatchException(null, null);
/*    */             case 0:
/*    */             
/*    */             case 1:
/*    */             
/*    */             case 2:
/*    */             
/*    */             case 3:
/*    */               break; }
/*    */            return 270;
/* 37 */         }); } Quadrant(int shift, OctahedralGroup rotationX, OctahedralGroup rotationY, OctahedralGroup rotationZ) { this.shift = shift;
/* 38 */     this.rotationX = rotationX;
/* 39 */     this.rotationY = rotationY;
/* 40 */     this.rotationZ = rotationZ; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static Quadrant parseJson(int degrees) {
/*    */     // Byte code:
/*    */     //   0: iload_0
/*    */     //   1: sipush #360
/*    */     //   4: invokestatic positiveModulo : (II)I
/*    */     //   7: lookupswitch default -> 72, 0 -> 48, 90 -> 54, 180 -> 60, 270 -> 66
/*    */     //   48: getstatic com/mojang/math/Quadrant.R0 : Lcom/mojang/math/Quadrant;
/*    */     //   51: goto -> 86
/*    */     //   54: getstatic com/mojang/math/Quadrant.R90 : Lcom/mojang/math/Quadrant;
/*    */     //   57: goto -> 86
/*    */     //   60: getstatic com/mojang/math/Quadrant.R180 : Lcom/mojang/math/Quadrant;
/*    */     //   63: goto -> 86
/*    */     //   66: getstatic com/mojang/math/Quadrant.R270 : Lcom/mojang/math/Quadrant;
/*    */     //   69: goto -> 86
/*    */     //   72: new com/google/gson/JsonParseException
/*    */     //   75: dup
/*    */     //   76: iload_0
/*    */     //   77: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*    */     //   82: invokespecial <init> : (Ljava/lang/String;)V
/*    */     //   85: athrow
/*    */     //   86: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     //   #46	-> 48
/*    */     //   #47	-> 54
/*    */     //   #48	-> 60
/*    */     //   #49	-> 66
/*    */     //   #51	-> 72
/*    */     //   #45	-> 86
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	87	0	degrees	I
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static OctahedralGroup fromXYAngles(Quadrant xRotation, Quadrant yRotation) {
/* 56 */     return yRotation.rotationY.compose(xRotation.rotationX);
/*    */   }
/*    */   
/*    */   public static OctahedralGroup fromXYZAngles(Quadrant xRotation, Quadrant yRotation, Quadrant zRotation) {
/* 60 */     return zRotation.rotationZ.compose(yRotation.rotationY.compose(xRotation.rotationX));
/*    */   }
/*    */   
/*    */   public int rotateVertexIndex(int index) {
/* 64 */     return (index + this.shift) % 4;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/Quadrant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */