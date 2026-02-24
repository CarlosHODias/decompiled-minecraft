/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class TrialSpawnerDetectionParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   private static final int BASE_LIFETIME = 8;
/*    */   
/*    */   protected TrialSpawnerDetectionParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
/* 14 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
/* 15 */     this.sprites = sprites;
/* 16 */     this.friction = 0.96F;
/* 17 */     this.gravity = -0.1F;
/* 18 */     this.speedUpWhenYMotionIsBlocked = true;
/*    */     
/* 20 */     this.xd *= 0.0D;
/* 21 */     this.yd *= 0.9D;
/* 22 */     this.zd *= 0.0D;
/* 23 */     this.xd += xa;
/* 24 */     this.yd += ya;
/* 25 */     this.zd += za;
/*    */     
/* 27 */     this.quadSize *= 0.75F * scale;
/*    */     
/* 29 */     this.lifetime = (int)(8.0F / Mth.randomBetween(this.random, 0.5F, 1.0F) * scale);
/* 30 */     this.lifetime = Math.max(this.lifetime, 1);
/* 31 */     setSpriteFromAge(sprites);
/* 32 */     this.hasPhysics = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 37 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 42 */     return 240;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
/* 47 */     return SingleQuadParticle.FacingCameraMode.LOOKAT_Y;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 52 */     super.tick();
/* 53 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 58 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 65 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 70 */       return new TrialSpawnerDetectionParticle(level, x, y, z, xAux, yAux, zAux, 1.5F, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/TrialSpawnerDetectionParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */