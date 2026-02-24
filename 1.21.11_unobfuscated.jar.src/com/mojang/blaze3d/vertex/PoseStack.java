/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class PoseStack
/*     */ {
/*  18 */   private final List<Pose> poses = new ArrayList<>(16);
/*     */   private int lastIndex;
/*     */   
/*     */   public PoseStack() {
/*  22 */     this.poses.add(new Pose());
/*     */   }
/*     */   
/*     */   public void translate(double xo, double yo, double zo) {
/*  26 */     translate((float)xo, (float)yo, (float)zo);
/*     */   }
/*     */   
/*     */   public void translate(float xo, float yo, float zo) {
/*  30 */     last().translate(xo, yo, zo);
/*     */   }
/*     */   
/*     */   public void translate(Vec3 offset) {
/*  34 */     translate(offset.x, offset.y, offset.z);
/*     */   }
/*     */   
/*     */   public void scale(float xScale, float yScale, float zScale) {
/*  38 */     last().scale(xScale, yScale, zScale);
/*     */   }
/*     */   
/*     */   public void mulPose(Quaternionfc by) {
/*  42 */     last().rotate(by);
/*     */   }
/*     */   
/*     */   public void rotateAround(Quaternionfc rotation, float pivotX, float pivotY, float pivotZ) {
/*  46 */     last().rotateAround(rotation, pivotX, pivotY, pivotZ);
/*     */   }
/*     */   
/*     */   public void pushPose() {
/*  50 */     Pose lastPose = last();
/*  51 */     this.lastIndex++;
/*  52 */     if (this.lastIndex >= this.poses.size()) {
/*  53 */       this.poses.add(lastPose.copy());
/*     */     } else {
/*  55 */       ((Pose)this.poses.get(this.lastIndex)).set(lastPose);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void popPose() {
/*  60 */     if (this.lastIndex == 0) {
/*  61 */       throw new NoSuchElementException();
/*     */     }
/*  63 */     this.lastIndex--;
/*     */   }
/*     */   
/*     */   public Pose last() {
/*  67 */     return this.poses.get(this.lastIndex);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  71 */     return (this.lastIndex == 0);
/*     */   }
/*     */   
/*     */   public void setIdentity() {
/*  75 */     last().setIdentity();
/*     */   }
/*     */   
/*     */   public void mulPose(Matrix4fc matrix) {
/*  79 */     last().mulPose(matrix);
/*     */   }
/*     */   
/*     */   public static final class Pose
/*     */   {
/*  84 */     private final Matrix4f pose = new Matrix4f();
/*  85 */     private final Matrix3f normal = new Matrix3f();
/*     */ 
/*     */     
/*     */     private boolean trustedNormals = true;
/*     */ 
/*     */     
/*     */     private void computeNormalMatrix() {
/*  92 */       this.normal.set((Matrix4fc)this.pose).invert().transpose();
/*  93 */       this.trustedNormals = false;
/*     */     }
/*     */     
/*     */     public void set(Pose pose) {
/*  97 */       this.pose.set((Matrix4fc)pose.pose);
/*  98 */       this.normal.set((Matrix3fc)pose.normal);
/*  99 */       this.trustedNormals = pose.trustedNormals;
/*     */     }
/*     */     
/*     */     public Matrix4f pose() {
/* 103 */       return this.pose;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Matrix3f normal() {
/* 111 */       return this.normal;
/*     */     }
/*     */     
/*     */     public Vector3f transformNormal(Vector3fc normal, Vector3f destination) {
/* 115 */       return transformNormal(normal.x(), normal.y(), normal.z(), destination);
/*     */     }
/*     */     
/*     */     public Vector3f transformNormal(float x, float y, float z, Vector3f destination) {
/* 119 */       Vector3f result = this.normal.transform(x, y, z, destination);
/* 120 */       return this.trustedNormals ? result : result.normalize();
/*     */     }
/*     */     
/*     */     public Matrix4f translate(float xo, float yo, float zo) {
/* 124 */       return this.pose.translate(xo, yo, zo);
/*     */     }
/*     */     
/*     */     public void scale(float xScale, float yScale, float zScale) {
/* 128 */       this.pose.scale(xScale, yScale, zScale);
/*     */ 
/*     */       
/* 131 */       if (Math.abs(xScale) == Math.abs(yScale) && Math.abs(yScale) == Math.abs(zScale)) {
/* 132 */         if (xScale < 0.0F || yScale < 0.0F || zScale < 0.0F) {
/* 133 */           this.normal.scale(Math.signum(xScale), Math.signum(yScale), Math.signum(zScale));
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/* 138 */       this.normal.scale(1.0F / xScale, 1.0F / yScale, 1.0F / zScale);
/* 139 */       this.trustedNormals = false;
/*     */     }
/*     */     
/*     */     public void rotate(Quaternionfc by) {
/* 143 */       this.pose.rotate(by);
/* 144 */       this.normal.rotate(by);
/*     */     }
/*     */     
/*     */     public void rotateAround(Quaternionfc rotation, float pivotX, float pivotY, float pivotZ) {
/* 148 */       this.pose.rotateAround(rotation, pivotX, pivotY, pivotZ);
/* 149 */       this.normal.rotate(rotation);
/*     */     }
/*     */     
/*     */     public void setIdentity() {
/* 153 */       this.pose.identity();
/* 154 */       this.normal.identity();
/* 155 */       this.trustedNormals = true;
/*     */     }
/*     */     
/*     */     public void mulPose(Matrix4fc matrix) {
/* 159 */       this.pose.mul(matrix);
/* 160 */       if (!MatrixUtil.isPureTranslation(matrix)) {
/* 161 */         if (MatrixUtil.isOrthonormal(matrix)) {
/*     */           
/* 163 */           this.normal.mul((Matrix3fc)new Matrix3f(matrix));
/*     */         } else {
/* 165 */           computeNormalMatrix();
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public Pose copy() {
/* 171 */       Pose pose = new Pose();
/* 172 */       pose.set(this);
/* 173 */       return pose;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/PoseStack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */