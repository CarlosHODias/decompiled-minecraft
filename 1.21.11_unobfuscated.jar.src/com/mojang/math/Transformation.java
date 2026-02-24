/*     */ package com.mojang.math;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.tuple.Triple;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Transformation
/*     */ {
/*     */   private final Matrix4fc matrix;
/*     */   public static final Codec<Transformation> CODEC;
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.VECTOR3F.fieldOf("translation").forGetter(()), (App)ExtraCodecs.QUATERNIONF.fieldOf("left_rotation").forGetter(()), (App)ExtraCodecs.VECTOR3F.fieldOf("scale").forGetter(()), (App)ExtraCodecs.QUATERNIONF.fieldOf("right_rotation").forGetter(())).apply((Applicative)i, Transformation::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final Codec<Transformation> EXTENDED_CODEC = Codec.withAlternative(CODEC, 
/*     */       
/*  45 */       ExtraCodecs.MATRIX4F.xmap(Transformation::new, Transformation::getMatrix));
/*     */   
/*     */   private boolean decomposed;
/*     */   private Vector3fc translation;
/*     */   private Quaternionfc leftRotation;
/*     */   private Vector3fc scale;
/*     */   private Quaternionfc rightRotation;
/*     */   private static final Transformation IDENTITY;
/*     */   
/*     */   public Transformation(Matrix4fc matrix) {
/*  55 */     if (matrix == null) {
/*  56 */       this.matrix = (Matrix4fc)new Matrix4f();
/*     */     } else {
/*  58 */       this.matrix = matrix;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Transformation(Vector3fc translation, Quaternionfc leftRotation, Vector3fc scale, Quaternionfc rightRotation) {
/*  63 */     this.matrix = (Matrix4fc)compose(translation, leftRotation, scale, rightRotation);
/*  64 */     this.translation = (translation != null) ? translation : (Vector3fc)new Vector3f();
/*  65 */     this.leftRotation = (leftRotation != null) ? leftRotation : (Quaternionfc)new Quaternionf();
/*  66 */     this.scale = (scale != null) ? scale : (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*  67 */     this.rightRotation = (rightRotation != null) ? rightRotation : (Quaternionfc)new Quaternionf();
/*  68 */     this.decomposed = true;
/*     */   }
/*     */   static {
/*  71 */     IDENTITY = (Transformation)Util.make(() -> {
/*     */           Transformation identity = new Transformation((Matrix4fc)new Matrix4f());
/*     */           identity.translation = (Vector3fc)new Vector3f();
/*     */           identity.leftRotation = (Quaternionfc)new Quaternionf();
/*     */           identity.scale = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*     */           identity.rightRotation = (Quaternionfc)new Quaternionf();
/*     */           identity.decomposed = true;
/*     */           return identity;
/*     */         });
/*     */   }
/*     */   public static Transformation identity() {
/*  82 */     return IDENTITY;
/*     */   }
/*     */   
/*     */   public Transformation compose(Transformation that) {
/*  86 */     Matrix4f matrix = getMatrixCopy();
/*  87 */     matrix.mul(that.getMatrix());
/*  88 */     return new Transformation((Matrix4fc)matrix);
/*     */   }
/*     */   
/*     */   public Transformation inverse() {
/*  92 */     if (this == IDENTITY) {
/*  93 */       return this;
/*     */     }
/*  95 */     Matrix4f matrix = getMatrixCopy().invertAffine();
/*  96 */     if (matrix.isFinite()) {
/*  97 */       return new Transformation((Matrix4fc)matrix);
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   private void ensureDecomposed() {
/* 103 */     if (!this.decomposed) {
/* 104 */       float scaleFactor = 1.0F / this.matrix.m33();
/* 105 */       Triple<Quaternionf, Vector3f, Quaternionf> triple = MatrixUtil.svdDecompose(new Matrix3f(this.matrix).scale(scaleFactor));
/* 106 */       this.translation = (Vector3fc)this.matrix.getTranslation(new Vector3f()).mul(scaleFactor);
/* 107 */       this.leftRotation = (Quaternionfc)new Quaternionf((Quaternionfc)triple.getLeft());
/* 108 */       this.scale = (Vector3fc)new Vector3f((Vector3fc)triple.getMiddle());
/* 109 */       this.rightRotation = (Quaternionfc)new Quaternionf((Quaternionfc)triple.getRight());
/* 110 */       this.decomposed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Matrix4f compose(Vector3fc translation, Quaternionfc leftRotation, Vector3fc scale, Quaternionfc rightRotation) {
/* 115 */     Matrix4f result = new Matrix4f();
/* 116 */     if (translation != null) {
/* 117 */       result.translation(translation);
/*     */     }
/* 119 */     if (leftRotation != null) {
/* 120 */       result.rotate(leftRotation);
/*     */     }
/* 122 */     if (scale != null) {
/* 123 */       result.scale(scale);
/*     */     }
/* 125 */     if (rightRotation != null) {
/* 126 */       result.rotate(rightRotation);
/*     */     }
/* 128 */     return result;
/*     */   }
/*     */   
/*     */   public Matrix4fc getMatrix() {
/* 132 */     return this.matrix;
/*     */   }
/*     */   
/*     */   public Matrix4f getMatrixCopy() {
/* 136 */     return new Matrix4f(this.matrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3fc getTranslation() {
/* 141 */     ensureDecomposed();
/* 142 */     return this.translation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternionfc getLeftRotation() {
/* 147 */     ensureDecomposed();
/* 148 */     return this.leftRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3fc getScale() {
/* 153 */     ensureDecomposed();
/* 154 */     return this.scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternionfc getRightRotation() {
/* 159 */     ensureDecomposed();
/* 160 */     return this.rightRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 165 */     if (this == o) {
/* 166 */       return true;
/*     */     }
/* 168 */     if (o == null || getClass() != o.getClass()) {
/* 169 */       return false;
/*     */     }
/* 171 */     Transformation that = (Transformation)o;
/* 172 */     return Objects.equals(this.matrix, that.matrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 177 */     return Objects.hash(new Object[] { this.matrix });
/*     */   }
/*     */   
/*     */   public Transformation slerp(Transformation that, float progress) {
/* 181 */     return new Transformation((Vector3fc)
/* 182 */         getTranslation().lerp(that.getTranslation(), progress, new Vector3f()), (Quaternionfc)
/* 183 */         getLeftRotation().slerp(that.getLeftRotation(), progress, new Quaternionf()), (Vector3fc)
/* 184 */         getScale().lerp(that.getScale(), progress, new Vector3f()), (Quaternionfc)
/* 185 */         getRightRotation().slerp(that.getRightRotation(), progress, new Quaternionf()));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/Transformation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */