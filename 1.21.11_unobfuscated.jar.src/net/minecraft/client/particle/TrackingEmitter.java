/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TrackingEmitter extends NoRenderParticle {
/*    */   private final Entity entity;
/*    */   private int life;
/*    */   private final int lifeTime;
/*    */   private final ParticleOptions particleType;
/*    */   
/*    */   public TrackingEmitter(ClientLevel level, Entity entity, ParticleOptions particleType) {
/* 15 */     this(level, entity, particleType, 3);
/*    */   }
/*    */   
/*    */   public TrackingEmitter(ClientLevel level, Entity entity, ParticleOptions particleType, int lifeTime) {
/* 19 */     this(level, entity, particleType, lifeTime, entity.getDeltaMovement());
/*    */   }
/*    */   
/*    */   private TrackingEmitter(ClientLevel level, Entity entity, ParticleOptions particleType, int lifeTime, Vec3 movement) {
/* 23 */     super(level, entity.getX(), entity.getY(0.5D), entity.getZ(), movement.x, movement.y, movement.z);
/* 24 */     this.entity = entity;
/* 25 */     this.lifeTime = lifeTime;
/* 26 */     this.particleType = particleType;
/* 27 */     tick();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 32 */     for (int i = 0; i < 16; i++) {
/* 33 */       double xa = (this.random.nextFloat() * 2.0F - 1.0F);
/* 34 */       double ya = (this.random.nextFloat() * 2.0F - 1.0F);
/* 35 */       double za = (this.random.nextFloat() * 2.0F - 1.0F);
/* 36 */       if (xa * xa + ya * ya + za * za <= 1.0D) {
/*    */ 
/*    */         
/* 39 */         double x = this.entity.getX(xa / 4.0D);
/* 40 */         double y = this.entity.getY(0.5D + ya / 4.0D);
/* 41 */         double z = this.entity.getZ(za / 4.0D);
/* 42 */         this.level.addParticle(this.particleType, x, y, z, xa, ya + 0.2D, za);
/*    */       } 
/* 44 */     }  this.life++;
/* 45 */     if (this.life >= this.lifeTime)
/* 46 */       remove(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/TrackingEmitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */