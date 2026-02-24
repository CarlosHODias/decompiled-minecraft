/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ public abstract class HitResult {
/*    */   protected final Vec3 location;
/*    */   
/*    */   public enum Type {
/*  7 */     MISS, BLOCK, ENTITY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected HitResult(Vec3 location) {
/* 13 */     this.location = location;
/*    */   }
/*    */   
/*    */   public double distanceTo(net.minecraft.world.entity.Entity entity) {
/* 17 */     double xd = this.location.x - entity.getX();
/* 18 */     double yd = this.location.y - entity.getY();
/* 19 */     double zd = this.location.z - entity.getZ();
/* 20 */     return xd * xd + yd * yd + zd * zd;
/*    */   }
/*    */   
/*    */   public abstract Type getType();
/*    */   
/*    */   public Vec3 getLocation() {
/* 26 */     return this.location;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/HitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */