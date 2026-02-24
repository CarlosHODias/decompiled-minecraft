/*     */ package com.mojang.math;
/*     */ public final class GivensParameters extends Record {
/*     */   private final float sinHalf;
/*     */   private final float cosHalf;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/math/GivensParameters;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/math/GivensParameters;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/math/GivensParameters;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/math/GivensParameters;
/*     */   }
/*     */   
/*  14 */   public GivensParameters(float sinHalf, float cosHalf) { this.sinHalf = sinHalf; this.cosHalf = cosHalf; } public float sinHalf() { return this.sinHalf; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/math/GivensParameters;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/math/GivensParameters;
/*  14 */     //   0	8	1	o	Ljava/lang/Object; } public float cosHalf() { return this.cosHalf; }
/*     */    public static GivensParameters fromUnnormalized(float sinHalf, float cosHalf) {
/*  16 */     float w = org.joml.Math.invsqrt(sinHalf * sinHalf + cosHalf * cosHalf);
/*  17 */     return new GivensParameters(w * sinHalf, w * cosHalf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GivensParameters fromPositiveAngle(float angle) {
/*  26 */     float sin = org.joml.Math.sin(angle / 2.0F);
/*  27 */     float cos = org.joml.Math.cosFromSin(sin, angle / 2.0F);
/*  28 */     return new GivensParameters(sin, cos);
/*     */   }
/*     */   
/*     */   public GivensParameters inverse() {
/*  32 */     return new GivensParameters(-this.sinHalf, this.cosHalf);
/*     */   }
/*     */   
/*     */   public org.joml.Quaternionf aroundX(org.joml.Quaternionf input) {
/*  36 */     return input.set(this.sinHalf, 0.0F, 0.0F, this.cosHalf);
/*     */   }
/*     */   
/*     */   public org.joml.Quaternionf aroundY(org.joml.Quaternionf input) {
/*  40 */     return input.set(0.0F, this.sinHalf, 0.0F, this.cosHalf);
/*     */   }
/*     */   
/*     */   public org.joml.Quaternionf aroundZ(org.joml.Quaternionf input) {
/*  44 */     return input.set(0.0F, 0.0F, this.sinHalf, this.cosHalf);
/*     */   }
/*     */ 
/*     */   
/*     */   public float cos() {
/*  49 */     return this.cosHalf * this.cosHalf - this.sinHalf * this.sinHalf;
/*     */   }
/*     */ 
/*     */   
/*     */   public float sin() {
/*  54 */     return 2.0F * this.sinHalf * this.cosHalf;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.joml.Matrix3f aroundX(org.joml.Matrix3f input) {
/*  59 */     input.m01 = 0.0F;
/*  60 */     input.m02 = 0.0F;
/*  61 */     input.m10 = 0.0F;
/*  62 */     input.m20 = 0.0F;
/*     */     
/*  64 */     float c = cos();
/*  65 */     float s = sin();
/*     */     
/*  67 */     input.m11 = c;
/*  68 */     input.m22 = c;
/*     */     
/*  70 */     input.m12 = s;
/*  71 */     input.m21 = -s;
/*     */     
/*  73 */     input.m00 = 1.0F;
/*  74 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.joml.Matrix3f aroundY(org.joml.Matrix3f input) {
/*  79 */     input.m01 = 0.0F;
/*  80 */     input.m10 = 0.0F;
/*  81 */     input.m12 = 0.0F;
/*  82 */     input.m21 = 0.0F;
/*     */     
/*  84 */     float c = cos();
/*  85 */     float s = sin();
/*     */     
/*  87 */     input.m00 = c;
/*  88 */     input.m22 = c;
/*     */     
/*  90 */     input.m02 = -s;
/*  91 */     input.m20 = s;
/*     */     
/*  93 */     input.m11 = 1.0F;
/*  94 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.joml.Matrix3f aroundZ(org.joml.Matrix3f input) {
/*  99 */     input.m02 = 0.0F;
/* 100 */     input.m12 = 0.0F;
/* 101 */     input.m20 = 0.0F;
/* 102 */     input.m21 = 0.0F;
/*     */     
/* 104 */     float c = cos();
/* 105 */     float s = sin();
/*     */     
/* 107 */     input.m00 = c;
/* 108 */     input.m11 = c;
/*     */     
/* 110 */     input.m01 = s;
/* 111 */     input.m10 = -s;
/*     */     
/* 113 */     input.m22 = 1.0F;
/* 114 */     return input;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/GivensParameters.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */