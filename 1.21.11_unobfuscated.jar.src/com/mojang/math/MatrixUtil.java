/*     */ package com.mojang.math;
/*     */ import org.apache.commons.lang3.tuple.Triple;
/*     */ import org.joml.Math;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class MatrixUtil {
/*  13 */   private static final float G = 3.0F + 2.0F * Math.sqrt(2.0F);
/*     */   
/*  15 */   private static final GivensParameters PI_4 = GivensParameters.fromPositiveAngle(0.7853982F);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix4f mulComponentWise(Matrix4f m, float factor) {
/*  21 */     return m.set(
/*  22 */         m.m00() * factor, m.m01() * factor, m.m02() * factor, m.m03() * factor, 
/*  23 */         m.m10() * factor, m.m11() * factor, m.m12() * factor, m.m13() * factor, 
/*  24 */         m.m20() * factor, m.m21() * factor, m.m22() * factor, m.m23() * factor, 
/*  25 */         m.m30() * factor, m.m31() * factor, m.m32() * factor, m.m33() * factor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static GivensParameters approxGivensQuat(float a11, float a12, float a22) {
/*  31 */     float ch = 2.0F * (a11 - a22);
/*  32 */     float sh = a12;
/*     */ 
/*     */     
/*  35 */     if (G * sh * sh < ch * ch) {
/*  36 */       return GivensParameters.fromUnnormalized(sh, ch);
/*     */     }
/*  38 */     return PI_4;
/*     */   }
/*     */ 
/*     */   
/*     */   private static GivensParameters qrGivensQuat(float a1, float a2) {
/*  43 */     float p = (float)Math.hypot(a1, a2);
/*  44 */     float sh = (p > 1.0E-6F) ? a2 : 0.0F;
/*  45 */     float ch = Math.abs(a1) + Math.max(p, 1.0E-6F);
/*  46 */     if (a1 < 0.0F) {
/*  47 */       float f = sh;
/*  48 */       sh = ch;
/*  49 */       ch = f;
/*     */     } 
/*  51 */     return GivensParameters.fromUnnormalized(sh, ch);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void similarityTransform(Matrix3f a, Matrix3f q) {
/*  56 */     a.mul((Matrix3fc)q);
/*     */     
/*  58 */     q.transpose();
/*     */     
/*  60 */     q.mul((Matrix3fc)a);
/*     */     
/*  62 */     a.set((Matrix3fc)q);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void stepJacobi(Matrix3f m, Matrix3f tmpMat, Quaternionf tmpQ, Quaternionf output) {
/*  67 */     if (m.m01 * m.m01 + m.m10 * m.m10 > 1.0E-6F) {
/*  68 */       GivensParameters p = approxGivensQuat(m.m00, 0.5F * (m.m01 + m.m10), m.m11);
/*     */       
/*  70 */       Quaternionf qt = p.aroundZ(tmpQ);
/*  71 */       output.mul((Quaternionfc)qt);
/*     */       
/*  73 */       p.aroundZ(tmpMat);
/*  74 */       similarityTransform(m, tmpMat);
/*     */     } 
/*     */     
/*  77 */     if (m.m02 * m.m02 + m.m20 * m.m20 > 1.0E-6F) {
/*     */       
/*  79 */       GivensParameters p = approxGivensQuat(m.m00, 0.5F * (m.m02 + m.m20), m.m22).inverse();
/*     */       
/*  81 */       Quaternionf qt = p.aroundY(tmpQ);
/*  82 */       output.mul((Quaternionfc)qt);
/*     */       
/*  84 */       p.aroundY(tmpMat);
/*  85 */       similarityTransform(m, tmpMat);
/*     */     } 
/*     */     
/*  88 */     if (m.m12 * m.m12 + m.m21 * m.m21 > 1.0E-6F) {
/*  89 */       GivensParameters p = approxGivensQuat(m.m11, 0.5F * (m.m12 + m.m21), m.m22);
/*     */       
/*  91 */       Quaternionf qt = p.aroundX(tmpQ);
/*  92 */       output.mul((Quaternionfc)qt);
/*     */       
/*  94 */       p.aroundX(tmpMat);
/*  95 */       similarityTransform(m, tmpMat);
/*     */     } 
/*     */   }
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
/*     */   public static Quaternionf eigenvalueJacobi(Matrix3f inOut, int steps) {
/* 110 */     Quaternionf v = new Quaternionf();
/*     */     
/* 112 */     Matrix3f scratchMat = new Matrix3f();
/* 113 */     Quaternionf scratchQ = new Quaternionf();
/*     */     
/* 115 */     for (int i = 0; i < steps; i++) {
/* 116 */       stepJacobi(inOut, scratchMat, scratchQ, v);
/*     */     }
/*     */     
/* 119 */     v.normalize();
/* 120 */     return v;
/*     */   }
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
/*     */   public static Triple<Quaternionf, Vector3f, Quaternionf> svdDecompose(Matrix3f matrix) {
/* 133 */     Matrix3f b = new Matrix3f((Matrix3fc)matrix);
/* 134 */     b.transpose();
/* 135 */     b.mul((Matrix3fc)matrix);
/*     */     
/* 137 */     Quaternionf v = eigenvalueJacobi(b, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     float columnScaleSquare0 = b.m00;
/* 144 */     float columnScaleSquare1 = b.m11;
/*     */ 
/*     */ 
/*     */     
/* 148 */     boolean zeroColumn0 = (columnScaleSquare0 < 1.0E-6D);
/* 149 */     boolean zeroColumn1 = (columnScaleSquare1 < 1.0E-6D);
/*     */     
/* 151 */     Matrix3f scratch = b;
/*     */ 
/*     */ 
/*     */     
/* 155 */     Matrix3f u012s = matrix.rotate((Quaternionfc)v);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Quaternionf u = new Quaternionf();
/*     */     
/* 187 */     Quaternionf tmpQ = new Quaternionf();
/*     */ 
/*     */     
/* 190 */     if (zeroColumn0) {
/* 191 */       p = qrGivensQuat(u012s.m11, -u012s.m10);
/*     */     } else {
/* 193 */       p = qrGivensQuat(u012s.m00, u012s.m01);
/*     */     } 
/*     */     
/* 196 */     Quaternionf qt0 = p.aroundZ(tmpQ);
/* 197 */     Matrix3f u12s = p.aroundZ(scratch);
/*     */ 
/*     */ 
/*     */     
/* 201 */     u.mul((Quaternionfc)qt0);
/* 202 */     u12s.transpose().mul((Matrix3fc)u012s);
/*     */     
/* 204 */     scratch = u012s;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     if (zeroColumn0) {
/* 210 */       p = qrGivensQuat(u12s.m22, -u12s.m20);
/*     */     } else {
/* 212 */       p = qrGivensQuat(u12s.m00, u12s.m02);
/*     */     } 
/*     */     
/* 215 */     GivensParameters p = p.inverse();
/*     */     
/* 217 */     Quaternionf qt1 = p.aroundY(tmpQ);
/*     */     
/* 219 */     Matrix3f u2s = p.aroundY(scratch);
/*     */ 
/*     */     
/* 222 */     u.mul((Quaternionfc)qt1);
/* 223 */     u2s.transpose().mul((Matrix3fc)u12s);
/*     */     
/* 225 */     scratch = u12s;
/*     */ 
/*     */     
/* 228 */     if (zeroColumn1) {
/* 229 */       p = qrGivensQuat(u2s.m22, -u2s.m21);
/*     */     } else {
/* 231 */       p = qrGivensQuat(u2s.m11, u2s.m12);
/*     */     } 
/*     */     
/* 234 */     Quaternionf qt2 = p.aroundX(tmpQ);
/*     */     
/* 236 */     Matrix3f s = p.aroundX(scratch);
/*     */ 
/*     */     
/* 239 */     u.mul((Quaternionfc)qt2);
/* 240 */     s.transpose().mul((Matrix3fc)u2s);
/*     */ 
/*     */     
/* 243 */     Vector3f scale = new Vector3f(s.m00, s.m11, s.m22);
/*     */ 
/*     */     
/* 246 */     return Triple.of(u, scale, v.conjugate());
/*     */   }
/*     */   
/*     */   private static boolean checkPropertyRaw(Matrix4fc matrix, int property) {
/* 250 */     return ((matrix.properties() & property) != 0);
/*     */   }
/*     */   
/*     */   public static boolean checkProperty(Matrix4fc matrix, int property) {
/* 254 */     if (checkPropertyRaw(matrix, property)) {
/* 255 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 259 */     if (matrix instanceof Matrix4f) { Matrix4f mutableMatrix = (Matrix4f)matrix;
/* 260 */       mutableMatrix.determineProperties();
/* 261 */       return checkPropertyRaw(matrix, property); }
/*     */ 
/*     */     
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isIdentity(Matrix4fc matrix) {
/* 268 */     return checkProperty(matrix, 4);
/*     */   }
/*     */   
/*     */   public static boolean isPureTranslation(Matrix4fc matrix) {
/* 272 */     return checkProperty(matrix, 8);
/*     */   }
/*     */   
/*     */   public static boolean isOrthonormal(Matrix4fc matrix) {
/* 276 */     return checkProperty(matrix, 16);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/MatrixUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */