/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class GustSeedParticle extends NoRenderParticle {
/*    */   private final double scale;
/*    */   private final int tickDelayInBetween;
/*    */   
/*    */   private GustSeedParticle(ClientLevel level, double x, double y, double z, double scale, int lifetime, int tickDelayInBetween) {
/* 14 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D);
/* 15 */     this.scale = scale;
/* 16 */     this.lifetime = lifetime;
/* 17 */     this.tickDelayInBetween = tickDelayInBetween;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 22 */     if (this.age % (this.tickDelayInBetween + 1) == 0) {
/* 23 */       for (int i = 0; i < 3; i++) {
/* 24 */         double x = this.x + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
/* 25 */         double y = this.y + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
/* 26 */         double z = this.z + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
/* 27 */         this.level.addParticle((ParticleOptions)ParticleTypes.GUST, x, y, z, (this.age / this.lifetime), 0.0D, 0.0D);
/*    */       } 
/*    */     }
/* 30 */     if (this.age++ == this.lifetime)
/* 31 */       remove(); 
/*    */   }
/*    */   
/*    */   public static class Provider
/*    */     implements ParticleProvider<SimpleParticleType>
/*    */   {
/*    */     private final double scale;
/*    */     private final int lifetime;
/*    */     private final int tickDelayInBetween;
/*    */     
/*    */     public Provider(double scale, int lifetime, int tickDelayInBetween) {
/* 42 */       this.scale = scale;
/* 43 */       this.lifetime = lifetime;
/* 44 */       this.tickDelayInBetween = tickDelayInBetween;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */       return new GustSeedParticle(level, x, y, z, this.scale, this.lifetime, this.tickDelayInBetween);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/GustSeedParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */