/*     */ package com.mojang.math;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum SymmetricGroup3
/*     */ {
/*  32 */   P123(0, 1, 2),
/*  33 */   P213(1, 0, 2),
/*  34 */   P132(0, 2, 1),
/*  35 */   P312(2, 0, 1),
/*  36 */   P231(1, 2, 0),
/*  37 */   P321(2, 1, 0);
/*     */   
/*     */   private final int p0;
/*     */   private final int p1;
/*     */   private final int p2;
/*     */   private final Matrix3fc transformation;
/*     */   private static final SymmetricGroup3[][] CAYLEY_TABLE;
/*     */   private static final SymmetricGroup3[] INVERSE_TABLE;
/*     */   
/*     */   SymmetricGroup3(int p0, int p1, int p2) {
/*  47 */     this.p0 = p0;
/*  48 */     this.p1 = p1;
/*  49 */     this.p2 = p2;
/*  50 */     this
/*     */ 
/*     */ 
/*     */       
/*  54 */       .transformation = (Matrix3fc)new Matrix3f().zero().set(permute(0), 0, 1.0F).set(permute(1), 1, 1.0F).set(permute(2), 2, 1.0F);
/*     */   }
/*     */   static {
/*  57 */     CAYLEY_TABLE = (SymmetricGroup3[][])Util.make(() -> {
/*     */           SymmetricGroup3 values[] = values(), table[][] = new SymmetricGroup3[values.length][values.length];
/*     */ 
/*     */           
/*     */           for (SymmetricGroup3 first : values) {
/*     */             for (SymmetricGroup3 second : values) {
/*     */               int p0 = first.permute(second.p0), p1 = first.permute(second.p1), p2 = first.permute(second.p2);
/*     */ 
/*     */               
/*     */               SymmetricGroup3 result = Arrays.<SymmetricGroup3>stream(values).filter(()).findFirst().get();
/*     */               
/*     */               table[first.ordinal()][second.ordinal()] = result;
/*     */             } 
/*     */           } 
/*     */           
/*     */           return table;
/*     */         });
/*     */     
/*  75 */     INVERSE_TABLE = (SymmetricGroup3[])Util.make(() -> {
/*     */           SymmetricGroup3[] values = values();
/*     */           return Arrays.<SymmetricGroup3>stream(values).map(()).toArray(());
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public SymmetricGroup3 compose(SymmetricGroup3 that) {
/*  83 */     return CAYLEY_TABLE[ordinal()][that.ordinal()];
/*     */   }
/*     */   
/*     */   public SymmetricGroup3 inverse() {
/*  87 */     return INVERSE_TABLE[ordinal()];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int permute(int i) {
/*     */     // Byte code:
/*     */     //   0: iload_1
/*     */     //   1: tableswitch default -> 49, 0 -> 28, 1 -> 35, 2 -> 42
/*     */     //   28: aload_0
/*     */     //   29: getfield p0 : I
/*     */     //   32: goto -> 63
/*     */     //   35: aload_0
/*     */     //   36: getfield p1 : I
/*     */     //   39: goto -> 63
/*     */     //   42: aload_0
/*     */     //   43: getfield p2 : I
/*     */     //   46: goto -> 63
/*     */     //   49: new java/lang/IllegalArgumentException
/*     */     //   52: dup
/*     */     //   53: iload_1
/*     */     //   54: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*     */     //   59: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   62: athrow
/*     */     //   63: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #91	-> 0
/*     */     //   #92	-> 28
/*     */     //   #93	-> 35
/*     */     //   #94	-> 42
/*     */     //   #95	-> 49
/*     */     //   #91	-> 63
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	64	0	this	Lcom/mojang/math/SymmetricGroup3;
/*     */     //   0	64	1	i	I
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Direction.Axis permuteAxis(Direction.Axis axis) {
/* 100 */     return Direction.Axis.VALUES[permute(axis.ordinal())];
/*     */   }
/*     */   
/*     */   public Vector3f permuteVector(Vector3f v) {
/* 104 */     float v0 = v.get(this.p0);
/* 105 */     float v1 = v.get(this.p1);
/* 106 */     float v2 = v.get(this.p2);
/* 107 */     return v.set(v0, v1, v2);
/*     */   }
/*     */   
/*     */   public Vector3i permuteVector(Vector3i v) {
/* 111 */     int v0 = v.get(this.p0);
/* 112 */     int v1 = v.get(this.p1);
/* 113 */     int v2 = v.get(this.p2);
/* 114 */     return v.set(v0, v1, v2);
/*     */   }
/*     */   
/*     */   public Matrix3fc transformation() {
/* 118 */     return this.transformation;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/SymmetricGroup3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */