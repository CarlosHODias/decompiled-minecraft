/*     */ package net.minecraft.client.renderer.culling;
/*     */ 
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.FrustumIntersection;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class Frustum {
/*  11 */   private final FrustumIntersection intersection = new FrustumIntersection(); public static final int OFFSET_STEP = 4;
/*  12 */   private final Matrix4f matrix = new Matrix4f();
/*     */   private Vector4f viewVector;
/*     */   private double camX;
/*     */   private double camY;
/*     */   private double camZ;
/*     */   
/*     */   public Frustum(Matrix4f modelView, Matrix4f projection) {
/*  19 */     calculateFrustum(modelView, projection);
/*     */   }
/*     */   
/*     */   public Frustum(Frustum frustum) {
/*  23 */     this.intersection.set((Matrix4fc)frustum.matrix);
/*  24 */     this.matrix.set((Matrix4fc)frustum.matrix);
/*  25 */     this.camX = frustum.camX;
/*  26 */     this.camY = frustum.camY;
/*  27 */     this.camZ = frustum.camZ;
/*  28 */     this.viewVector = frustum.viewVector;
/*     */   }
/*     */   
/*     */   public Frustum offset(float offset) {
/*  32 */     this.camX += (this.viewVector.x * offset);
/*  33 */     this.camY += (this.viewVector.y * offset);
/*  34 */     this.camZ += (this.viewVector.z * offset);
/*  35 */     return this;
/*     */   }
/*     */   
/*     */   public Frustum offsetToFullyIncludeCameraCube(int cubeSize) {
/*  39 */     double camX1 = Math.floor(this.camX / cubeSize) * cubeSize;
/*  40 */     double camY1 = Math.floor(this.camY / cubeSize) * cubeSize;
/*  41 */     double camZ1 = Math.floor(this.camZ / cubeSize) * cubeSize;
/*  42 */     double camX2 = Math.ceil(this.camX / cubeSize) * cubeSize;
/*  43 */     double camY2 = Math.ceil(this.camY / cubeSize) * cubeSize;
/*  44 */     double camZ2 = Math.ceil(this.camZ / cubeSize) * cubeSize;
/*  45 */     while (this.intersection.intersectAab((float)(camX1 - this.camX), (float)(camY1 - this.camY), (float)(camZ1 - this.camZ), (float)(camX2 - this.camX), (float)(camY2 - this.camY), (float)(camZ2 - this.camZ)) != -2) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  53 */       this.camX -= (this.viewVector.x() * 4.0F);
/*  54 */       this.camY -= (this.viewVector.y() * 4.0F);
/*  55 */       this.camZ -= (this.viewVector.z() * 4.0F);
/*     */     } 
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   public void prepare(double camX, double camY, double camZ) {
/*  61 */     this.camX = camX;
/*  62 */     this.camY = camY;
/*  63 */     this.camZ = camZ;
/*     */   }
/*     */   
/*     */   private void calculateFrustum(Matrix4f modelView, Matrix4f projection) {
/*  67 */     projection.mul((Matrix4fc)modelView, this.matrix);
/*     */     
/*  69 */     this.intersection.set((Matrix4fc)this.matrix);
/*  70 */     this.viewVector = this.matrix.transformTranspose(new Vector4f(0.0F, 0.0F, 1.0F, 0.0F));
/*     */   }
/*     */   
/*     */   public boolean isVisible(AABB bb) {
/*  74 */     int intersectionResult = cubeInFrustum(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
/*  75 */     return (intersectionResult == -2 || intersectionResult == -1);
/*     */   }
/*     */   
/*     */   public int cubeInFrustum(BoundingBox bb) {
/*  79 */     return cubeInFrustum(bb.minX(), bb.minY(), bb.minZ(), (bb.maxX() + 1), (bb.maxY() + 1), (bb.maxZ() + 1));
/*     */   }
/*     */   
/*     */   private int cubeInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  83 */     float x1 = (float)(minX - this.camX);
/*  84 */     float y1 = (float)(minY - this.camY);
/*  85 */     float z1 = (float)(minZ - this.camZ);
/*  86 */     float x2 = (float)(maxX - this.camX);
/*  87 */     float y2 = (float)(maxY - this.camY);
/*  88 */     float z2 = (float)(maxZ - this.camZ);
/*  89 */     return this.intersection.intersectAab(x1, y1, z1, x2, y2, z2);
/*     */   }
/*     */   
/*     */   public boolean pointInFrustum(double x, double y, double z) {
/*  93 */     return this.intersection.testPoint((float)(x - this.camX), (float)(y - this.camY), (float)(z - this.camZ));
/*     */   }
/*     */   
/*     */   public Vector4f[] getFrustumPoints() {
/*  97 */     Vector4f[] frustumPoints = new Vector4f[8];
/*     */ 
/*     */     
/* 100 */     frustumPoints[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
/* 101 */     frustumPoints[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
/* 102 */     frustumPoints[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
/* 103 */     frustumPoints[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
/*     */ 
/*     */     
/* 106 */     frustumPoints[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
/* 107 */     frustumPoints[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
/* 108 */     frustumPoints[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 109 */     frustumPoints[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 111 */     Matrix4f clipToWorldMatrix = this.matrix.invert(new Matrix4f());
/* 112 */     for (int i = 0; i < 8; i++) {
/* 113 */       clipToWorldMatrix.transform(frustumPoints[i]);
/* 114 */       frustumPoints[i].div(frustumPoints[i].w());
/*     */     } 
/*     */     
/* 117 */     return frustumPoints;
/*     */   }
/*     */   
/*     */   public double getCamX() {
/* 121 */     return this.camX;
/*     */   }
/*     */   
/*     */   public double getCamY() {
/* 125 */     return this.camY;
/*     */   }
/*     */   
/*     */   public double getCamZ() {
/* 129 */     return this.camZ;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/culling/Frustum.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */