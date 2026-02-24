/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.InterpolationHandler;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class MinecartBehavior
/*    */ {
/*    */   protected final AbstractMinecart minecart;
/*    */   
/*    */   protected MinecartBehavior(AbstractMinecart minecart) {
/* 16 */     this.minecart = minecart;
/*    */   }
/*    */   
/*    */   public InterpolationHandler getInterpolation() {
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   public void lerpMotion(Vec3 movement) {
/* 24 */     setDeltaMovement(movement);
/*    */   }
/*    */   
/*    */   public abstract void tick();
/*    */   
/*    */   public Level level() {
/* 30 */     return this.minecart.level();
/*    */   }
/*    */   
/*    */   public abstract void moveAlongTrack(ServerLevel paramServerLevel);
/*    */   
/*    */   public abstract double stepAlongTrack(BlockPos paramBlockPos, RailShape paramRailShape, double paramDouble);
/*    */   
/*    */   public abstract boolean pushAndPickupEntities();
/*    */   
/*    */   public Vec3 getDeltaMovement() {
/* 40 */     return this.minecart.getDeltaMovement();
/*    */   }
/*    */   
/*    */   public void setDeltaMovement(Vec3 deltaMovement) {
/* 44 */     this.minecart.setDeltaMovement(deltaMovement);
/*    */   }
/*    */   
/*    */   public void setDeltaMovement(double x, double y, double z) {
/* 48 */     this.minecart.setDeltaMovement(x, y, z);
/*    */   }
/*    */   
/*    */   public Vec3 position() {
/* 52 */     return this.minecart.position();
/*    */   }
/*    */   
/*    */   public double getX() {
/* 56 */     return this.minecart.getX();
/*    */   }
/*    */   
/*    */   public double getY() {
/* 60 */     return this.minecart.getY();
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 64 */     return this.minecart.getZ();
/*    */   }
/*    */   
/*    */   public void setPos(Vec3 pos) {
/* 68 */     this.minecart.setPos(pos);
/*    */   }
/*    */   
/*    */   public void setPos(double x, double y, double z) {
/* 72 */     this.minecart.setPos(x, y, z);
/*    */   }
/*    */   
/*    */   public float getXRot() {
/* 76 */     return this.minecart.getXRot();
/*    */   }
/*    */   
/*    */   public void setXRot(float rot) {
/* 80 */     this.minecart.setXRot(rot);
/*    */   }
/*    */   
/*    */   public float getYRot() {
/* 84 */     return this.minecart.getYRot();
/*    */   }
/*    */   
/*    */   public void setYRot(float rot) {
/* 88 */     this.minecart.setYRot(rot);
/*    */   }
/*    */   
/*    */   public Direction getMotionDirection() {
/* 92 */     return this.minecart.getDirection();
/*    */   }
/*    */   
/*    */   public Vec3 getKnownMovement(Vec3 knownMovement) {
/* 96 */     return knownMovement;
/*    */   }
/*    */   
/*    */   public abstract double getMaxSpeed(ServerLevel paramServerLevel);
/*    */   
/*    */   public abstract double getSlowdownFactor();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/minecart/MinecartBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */