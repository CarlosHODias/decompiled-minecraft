/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ProjectileDeflection {
/*    */   public static final ProjectileDeflection NONE = (projectile, entity, random) -> {
/*    */     
/*    */     };
/*    */   public static final ProjectileDeflection REVERSE;
/*    */   
/*    */   static {
/* 15 */     REVERSE = ((projectile, entity, random) -> {
/*    */         float rotation = 170.0F + random.nextFloat() * 20.0F;
/*    */         
/*    */         projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-0.5D));
/*    */         projectile.setYRot(projectile.getYRot() + rotation);
/*    */         projectile.yRotO += rotation;
/*    */         projectile.needsSync = true;
/*    */       });
/* 23 */     AIM_DEFLECT = ((projectile, entity, random) -> {
/*    */         if (entity != null) {
/*    */           Vec3 lookAngle = entity.getLookAngle();
/*    */           
/*    */           projectile.setDeltaMovement(lookAngle);
/*    */           projectile.needsSync = true;
/*    */         } 
/*    */       });
/* 31 */     MOMENTUM_DEFLECT = ((projectile, entity, random) -> {
/*    */         if (entity != null) {
/*    */           Vec3 movement = entity.getDeltaMovement().normalize();
/*    */           projectile.setDeltaMovement(movement);
/*    */           projectile.needsSync = true;
/*    */         } 
/*    */       });
/*    */   }
/*    */   
/*    */   public static final ProjectileDeflection AIM_DEFLECT;
/*    */   public static final ProjectileDeflection MOMENTUM_DEFLECT;
/*    */   
/*    */   void deflect(Projectile paramProjectile, Entity paramEntity, RandomSource paramRandomSource);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/ProjectileDeflection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */