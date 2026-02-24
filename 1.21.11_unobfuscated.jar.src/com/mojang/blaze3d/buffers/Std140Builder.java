/*     */ package com.mojang.blaze3d.buffers;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector2fc;
/*     */ import org.joml.Vector2ic;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector3ic;
/*     */ import org.joml.Vector4fc;
/*     */ import org.joml.Vector4ic;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Std140Builder
/*     */ {
/*     */   private final ByteBuffer buffer;
/*     */   private final int start;
/*     */   
/*     */   private Std140Builder(ByteBuffer buffer) {
/*  25 */     this.buffer = buffer;
/*  26 */     this.start = buffer.position();
/*     */   }
/*     */   
/*     */   public static Std140Builder intoBuffer(ByteBuffer buffer) {
/*  30 */     return new Std140Builder(buffer);
/*     */   }
/*     */   
/*     */   public static Std140Builder onStack(MemoryStack stack, int size) {
/*  34 */     return new Std140Builder(stack.malloc(size));
/*     */   }
/*     */   
/*     */   public ByteBuffer get() {
/*  38 */     return this.buffer.flip();
/*     */   }
/*     */   
/*     */   public Std140Builder align(int alignment) {
/*  42 */     int position = this.buffer.position();
/*  43 */     this.buffer.position(this.start + Mth.roundToward(position - this.start, alignment));
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putFloat(float value) {
/*  48 */     align(4);
/*  49 */     this.buffer.putFloat(value);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putInt(int value) {
/*  54 */     align(4);
/*  55 */     this.buffer.putInt(value);
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec2(float x, float y) {
/*  60 */     align(8);
/*  61 */     this.buffer.putFloat(x);
/*  62 */     this.buffer.putFloat(y);
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec2(Vector2fc vec) {
/*  67 */     align(8);
/*  68 */     vec.get(this.buffer);
/*  69 */     this.buffer.position(this.buffer.position() + 8);
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec2(int x, int y) {
/*  74 */     align(8);
/*  75 */     this.buffer.putInt(x);
/*  76 */     this.buffer.putInt(y);
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec2(Vector2ic vec) {
/*  81 */     align(8);
/*  82 */     vec.get(this.buffer);
/*  83 */     this.buffer.position(this.buffer.position() + 8);
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec3(float x, float y, float z) {
/*  88 */     align(16);
/*  89 */     this.buffer.putFloat(x);
/*  90 */     this.buffer.putFloat(y);
/*  91 */     this.buffer.putFloat(z);
/*  92 */     this.buffer.position(this.buffer.position() + 4);
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec3(Vector3fc vec) {
/*  97 */     align(16);
/*  98 */     vec.get(this.buffer);
/*  99 */     this.buffer.position(this.buffer.position() + 16);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec3(int x, int y, int z) {
/* 104 */     align(16);
/* 105 */     this.buffer.putInt(x);
/* 106 */     this.buffer.putInt(y);
/* 107 */     this.buffer.putInt(z);
/* 108 */     this.buffer.position(this.buffer.position() + 4);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec3(Vector3ic vec) {
/* 113 */     align(16);
/* 114 */     vec.get(this.buffer);
/* 115 */     this.buffer.position(this.buffer.position() + 16);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec4(float x, float y, float z, float w) {
/* 120 */     align(16);
/* 121 */     this.buffer.putFloat(x);
/* 122 */     this.buffer.putFloat(y);
/* 123 */     this.buffer.putFloat(z);
/* 124 */     this.buffer.putFloat(w);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec4(Vector4fc vec) {
/* 129 */     align(16);
/* 130 */     vec.get(this.buffer);
/* 131 */     this.buffer.position(this.buffer.position() + 16);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec4(int x, int y, int z, int w) {
/* 136 */     align(16);
/* 137 */     this.buffer.putInt(x);
/* 138 */     this.buffer.putInt(y);
/* 139 */     this.buffer.putInt(z);
/* 140 */     this.buffer.putInt(w);
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec4(Vector4ic vec) {
/* 145 */     align(16);
/* 146 */     vec.get(this.buffer);
/* 147 */     this.buffer.position(this.buffer.position() + 16);
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putMat4f(Matrix4fc vec) {
/* 152 */     align(16);
/* 153 */     vec.get(this.buffer);
/* 154 */     this.buffer.position(this.buffer.position() + 64);
/* 155 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/buffers/Std140Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */