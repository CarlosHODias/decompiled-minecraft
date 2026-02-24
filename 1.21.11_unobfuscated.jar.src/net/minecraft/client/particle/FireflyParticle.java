/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FireflyParticle extends SingleQuadParticle {
/*    */   private static final float PARTICLE_FADE_OUT_LIGHT_TIME = 0.3F;
/*    */   private static final float PARTICLE_FADE_IN_LIGHT_TIME = 0.1F;
/*    */   private static final float PARTICLE_FADE_OUT_ALPHA_TIME = 0.5F;
/*    */   private static final float PARTICLE_FADE_IN_ALPHA_TIME = 0.3F;
/*    */   private static final int PARTICLE_MIN_LIFETIME = 200;
/*    */   private static final int PARTICLE_MAX_LIFETIME = 300;
/*    */   
/*    */   private FireflyParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/* 20 */     super(level, x, y, z, xa, ya, za, sprite);
/* 21 */     this.speedUpWhenYMotionIsBlocked = true;
/* 22 */     this.friction = 0.96F;
/* 23 */     this.quadSize *= 0.75F;
/* 24 */     this.yd *= 0.800000011920929D;
/* 25 */     this.xd *= 0.800000011920929D;
/* 26 */     this.zd *= 0.800000011920929D;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 31 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 36 */     return (int)(255.0F * getFadeAmount(getLifetimeProgress(this.age + a), 0.1F, 0.3F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 41 */     super.tick();
/*    */     
/* 43 */     if (!this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
/* 44 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     setAlpha(getFadeAmount(getLifetimeProgress(this.age), 0.3F, 0.5F));
/*    */     
/* 50 */     if (this.random.nextFloat() > 0.95F || this.age == 1) {
/* 51 */       setParticleSpeed((-0.05F + 0.1F * 
/* 52 */           this.random.nextFloat()), (-0.05F + 0.1F * 
/* 53 */           this.random.nextFloat()), (-0.05F + 0.1F * 
/* 54 */           this.random.nextFloat()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private float getLifetimeProgress(float currentAge) {
/* 60 */     return Mth.clamp(currentAge / this.lifetime, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   private static float getFadeAmount(float lifetimeProgress, float fadeInTime, float fadeOutTime) {
/* 64 */     if (lifetimeProgress >= 1.0F - fadeInTime)
/* 65 */       return (1.0F - lifetimeProgress) / fadeInTime; 
/* 66 */     if (lifetimeProgress <= fadeOutTime) {
/* 67 */       return lifetimeProgress / fadeOutTime;
/*    */     }
/* 69 */     return 1.0F;
/*    */   }
/*    */   
/*    */   public static class FireflyProvider
/*    */     implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public FireflyProvider(SpriteSet sprite) {
/* 77 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 82 */       FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.5D - random.nextDouble(), random.nextBoolean() ? yAux : -yAux, 0.5D - random.nextDouble(), this.sprite.get(random));
/* 83 */       particle.setLifetime(random.nextIntBetweenInclusive(200, 300));
/* 84 */       particle.scale(1.5F);
/* 85 */       particle.setAlpha(0.0F);
/* 86 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FireflyParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */