/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class CampfireSmokeParticle extends SingleQuadParticle {
/*    */   private CampfireSmokeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, boolean isSignalFire, TextureAtlasSprite sprite) {
/* 10 */     super(level, x, y, z, sprite);
/* 11 */     scale(3.0F);
/* 12 */     setSize(0.25F, 0.25F);
/* 13 */     if (isSignalFire) {
/* 14 */       this.lifetime = this.random.nextInt(50) + 280;
/*    */     } else {
/* 16 */       this.lifetime = this.random.nextInt(50) + 80;
/*    */     } 
/* 18 */     this.gravity = 3.0E-6F;
/* 19 */     this.xd = xa;
/* 20 */     this.yd = ya + (this.random.nextFloat() / 500.0F);
/* 21 */     this.zd = za;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 26 */     this.xo = this.x;
/* 27 */     this.yo = this.y;
/* 28 */     this.zo = this.z;
/*    */     
/* 30 */     if (this.age++ >= this.lifetime || this.alpha <= 0.0F) {
/* 31 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     this.xd += (this.random.nextFloat() / 5000.0F * (this.random.nextBoolean() ? true : -1));
/* 36 */     this.zd += (this.random.nextFloat() / 5000.0F * (this.random.nextBoolean() ? true : -1));
/* 37 */     this.yd -= this.gravity;
/*    */     
/* 39 */     move(this.xd, this.yd, this.zd);
/*    */     
/* 41 */     if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
/* 42 */       this.alpha -= 0.015F;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 48 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */   
/*    */   public static class CosyProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public CosyProvider(SpriteSet sprites) {
/* 55 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 60 */       CampfireSmokeParticle particle = new CampfireSmokeParticle(level, x, y, z, xAux, yAux, zAux, false, this.sprites.get(random));
/* 61 */       particle.setAlpha(0.9F);
/* 62 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SignalProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public SignalProvider(SpriteSet sprites) {
/* 70 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 75 */       CampfireSmokeParticle particle = new CampfireSmokeParticle(level, x, y, z, xAux, yAux, zAux, true, this.sprites.get(random));
/* 76 */       particle.setAlpha(0.95F);
/* 77 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/CampfireSmokeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */