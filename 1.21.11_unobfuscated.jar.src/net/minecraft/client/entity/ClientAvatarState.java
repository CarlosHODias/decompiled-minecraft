/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ClientAvatarState {
/*   7 */   private Vec3 deltaMovementOnPreviousTick = Vec3.ZERO;
/*     */   
/*     */   private float walkDist;
/*     */   
/*     */   private float walkDistO;
/*     */   
/*     */   private double xCloak;
/*     */   private double yCloak;
/*     */   private double zCloak;
/*     */   private double xCloakO;
/*     */   private double yCloakO;
/*     */   private double zCloakO;
/*     */   private float bob;
/*     */   private float bobO;
/*     */   
/*     */   public void tick(Vec3 position, Vec3 deltaMovement) {
/*  23 */     this.walkDistO = this.walkDist;
/*  24 */     this.deltaMovementOnPreviousTick = deltaMovement;
/*  25 */     moveCloak(position);
/*     */   }
/*     */   
/*     */   public void addWalkDistance(float added) {
/*  29 */     this.walkDist += added;
/*     */   }
/*     */   
/*     */   public Vec3 deltaMovementOnPreviousTick() {
/*  33 */     return this.deltaMovementOnPreviousTick;
/*     */   }
/*     */   
/*     */   private void moveCloak(Vec3 pos) {
/*  37 */     this.xCloakO = this.xCloak;
/*  38 */     this.yCloakO = this.yCloak;
/*  39 */     this.zCloakO = this.zCloak;
/*     */     
/*  41 */     double x = pos.x() - this.xCloak;
/*  42 */     double y = pos.y() - this.yCloak;
/*  43 */     double z = pos.z() - this.zCloak;
/*     */ 
/*     */     
/*  46 */     double teleportThreshold = 10.0D;
/*  47 */     if (x > 10.0D || x < -10.0D) {
/*  48 */       this.xCloak = pos.x();
/*  49 */       this.xCloakO = this.xCloak;
/*     */     } else {
/*  51 */       this.xCloak += x * 0.25D;
/*     */     } 
/*     */     
/*  54 */     if (y > 10.0D || y < -10.0D) {
/*  55 */       this.yCloak = pos.y();
/*  56 */       this.yCloakO = this.yCloak;
/*     */     } else {
/*  58 */       this.yCloak += y * 0.25D;
/*     */     } 
/*     */     
/*  61 */     if (z > 10.0D || z < -10.0D) {
/*  62 */       this.zCloak = pos.z();
/*  63 */       this.zCloakO = this.zCloak;
/*     */     } else {
/*  65 */       this.zCloak += z * 0.25D;
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getInterpolatedCloakX(float partialTicks) {
/*  70 */     return Mth.lerp(partialTicks, this.xCloakO, this.xCloak);
/*     */   }
/*     */   
/*     */   public double getInterpolatedCloakY(float partialTicks) {
/*  74 */     return Mth.lerp(partialTicks, this.yCloakO, this.yCloak);
/*     */   }
/*     */   
/*     */   public double getInterpolatedCloakZ(float partialTicks) {
/*  78 */     return Mth.lerp(partialTicks, this.zCloakO, this.zCloak);
/*     */   }
/*     */   
/*     */   public void updateBob(float tBob) {
/*  82 */     this.bobO = this.bob;
/*  83 */     this.bob += (tBob - this.bob) * 0.4F;
/*     */   }
/*     */   
/*     */   public void resetBob() {
/*  87 */     this.bobO = this.bob;
/*  88 */     this.bob = 0.0F;
/*     */   }
/*     */   
/*     */   public float getInterpolatedBob(float partialTicks) {
/*  92 */     return Mth.lerp(partialTicks, this.bobO, this.bob);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBackwardsInterpolatedWalkDistance(float partialTicks) {
/*  98 */     float wda = this.walkDist - this.walkDistO;
/*  99 */     return -(this.walkDist + wda * partialTicks);
/*     */   }
/*     */   
/*     */   public float getInterpolatedWalkDistance(float partialTicks) {
/* 103 */     return Mth.lerp(partialTicks, this.walkDistO, this.walkDist);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/entity/ClientAvatarState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */