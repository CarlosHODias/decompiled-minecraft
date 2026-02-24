/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class HugeExplosionSeedParticle extends NoRenderParticle {
/*    */   private HugeExplosionSeedParticle(ClientLevel level, double x, double y, double z) {
/* 10 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D);
/* 11 */     this.lifetime = 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 16 */     for (int i = 0; i < 6; i++) {
/* 17 */       double xx = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
/* 18 */       double yy = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
/* 19 */       double zz = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
/* 20 */       this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION, xx, yy, zz, (this.age / this.lifetime), 0.0D, 0.0D);
/*    */     } 
/* 22 */     this.age++;
/* 23 */     if (this.age == this.lifetime)
/* 24 */       remove(); 
/*    */   }
/*    */   
/*    */   public static class Provider
/*    */     implements ParticleProvider<SimpleParticleType>
/*    */   {
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 31 */       return new HugeExplosionSeedParticle(level, x, y, z);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/HugeExplosionSeedParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */