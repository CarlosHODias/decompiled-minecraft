/*     */ package net.minecraft.world.entity.ai.control;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LookControl
/*     */   implements Control {
/*     */   protected final Mob mob;
/*     */   protected float yMaxRotSpeed;
/*     */   protected float xMaxRotAngle;
/*     */   protected int lookAtCooldown;
/*     */   protected double wantedX;
/*     */   protected double wantedY;
/*     */   protected double wantedZ;
/*     */   
/*     */   public LookControl(Mob mob) {
/*  20 */     this.mob = mob;
/*     */   }
/*     */   
/*     */   public void setLookAt(Vec3 vec) {
/*  24 */     setLookAt(vec.x, vec.y, vec.z);
/*     */   }
/*     */   
/*     */   public void setLookAt(Entity target) {
/*  28 */     setLookAt(target.getX(), target.getEyeY(), target.getZ());
/*     */   }
/*     */   
/*     */   public void setLookAt(Entity target, float yMaxRotSpeed, float xMaxRotAngle) {
/*  32 */     setLookAt(target.getX(), target.getEyeY(), target.getZ(), yMaxRotSpeed, xMaxRotAngle);
/*     */   }
/*     */   
/*     */   public void setLookAt(double x, double y, double z) {
/*  36 */     setLookAt(x, y, z, this.mob.getHeadRotSpeed(), this.mob.getMaxHeadXRot());
/*     */   }
/*     */   
/*     */   public void setLookAt(double x, double y, double z, float yMaxRotSpeed, float xMaxRotAngle) {
/*  40 */     this.wantedX = x;
/*  41 */     this.wantedY = y;
/*  42 */     this.wantedZ = z;
/*  43 */     this.yMaxRotSpeed = yMaxRotSpeed;
/*  44 */     this.xMaxRotAngle = xMaxRotAngle;
/*  45 */     this.lookAtCooldown = 2;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  49 */     if (resetXRotOnTick()) {
/*  50 */       this.mob.setXRot(0.0F);
/*     */     }
/*     */     
/*  53 */     if (this.lookAtCooldown > 0) {
/*  54 */       this.lookAtCooldown--;
/*  55 */       getYRotD().ifPresent(yRotD -> this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, yRotD, this.yMaxRotSpeed));
/*  56 */       getXRotD().ifPresent(xRotD -> this.mob.setXRot(rotateTowards(this.mob.getXRot(), xRotD, this.xMaxRotAngle)));
/*     */     } else {
/*  58 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
/*     */     } 
/*     */     
/*  61 */     clampHeadRotationToBody();
/*     */   }
/*     */   
/*     */   protected void clampHeadRotationToBody() {
/*  65 */     if (!this.mob.getNavigation().isDone())
/*     */     {
/*  67 */       this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean resetXRotOnTick() {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isLookingAtTarget() {
/*  77 */     return (this.lookAtCooldown > 0);
/*     */   }
/*     */   
/*     */   public double getWantedX() {
/*  81 */     return this.wantedX;
/*     */   }
/*     */   
/*     */   public double getWantedY() {
/*  85 */     return this.wantedY;
/*     */   }
/*     */   
/*     */   public double getWantedZ() {
/*  89 */     return this.wantedZ;
/*     */   }
/*     */   
/*     */   protected Optional<Float> getXRotD() {
/*  93 */     double xd = this.wantedX - this.mob.getX();
/*  94 */     double yd = this.wantedY - this.mob.getEyeY();
/*  95 */     double zd = this.wantedZ - this.mob.getZ();
/*  96 */     double sd = Math.sqrt(xd * xd + zd * zd);
/*  97 */     return (Math.abs(yd) > 9.999999747378752E-6D || Math.abs(sd) > 9.999999747378752E-6D) ? Optional.<Float>of((float)-(Mth.atan2(yd, sd) * 57.2957763671875D)) : Optional.<Float>empty();
/*     */   }
/*     */   
/*     */   protected Optional<Float> getYRotD() {
/* 101 */     double xd = this.wantedX - this.mob.getX();
/* 102 */     double zd = this.wantedZ - this.mob.getZ();
/* 103 */     return (Math.abs(zd) > 9.999999747378752E-6D || Math.abs(xd) > 9.999999747378752E-6D) ? Optional.<Float>of((float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F) : Optional.<Float>empty();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/control/LookControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */