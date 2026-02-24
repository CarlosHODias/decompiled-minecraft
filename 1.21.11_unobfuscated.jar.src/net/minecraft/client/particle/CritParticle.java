/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class CritParticle extends SingleQuadParticle {
/*    */   private CritParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/* 11 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 12 */     this.friction = 0.7F;
/* 13 */     this.gravity = 0.5F;
/* 14 */     this.xd *= 0.10000000149011612D;
/* 15 */     this.yd *= 0.10000000149011612D;
/* 16 */     this.zd *= 0.10000000149011612D;
/* 17 */     this.xd += xa * 0.4D;
/* 18 */     this.yd += ya * 0.4D;
/* 19 */     this.zd += za * 0.4D;
/*    */     
/* 21 */     float col = this.random.nextFloat() * 0.3F + 0.6F;
/* 22 */     this.rCol = col;
/* 23 */     this.gCol = col;
/* 24 */     this.bCol = col;
/* 25 */     this.quadSize *= 0.75F;
/*    */     
/* 27 */     this.lifetime = Math.max((int)(6.0D / (this.random.nextFloat() * 0.8D + 0.6D)), 1);
/*    */     
/* 29 */     this.hasPhysics = false;
/* 30 */     tick();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 35 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 40 */     super.tick();
/* 41 */     this.gCol *= 0.96F;
/* 42 */     this.bCol *= 0.9F;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 47 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 54 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 59 */       CritParticle particle = new CritParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 60 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class MagicProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public MagicProvider(SpriteSet sprite) {
/* 68 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 73 */       CritParticle particle = new CritParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 74 */       particle.rCol *= 0.3F;
/* 75 */       particle.gCol *= 0.8F;
/* 76 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class DamageIndicatorProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public DamageIndicatorProvider(SpriteSet sprite) {
/* 84 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 89 */       CritParticle particle = new CritParticle(level, x, y, z, xAux, yAux + 1.0D, zAux, this.sprite.get(random));
/* 90 */       particle.setLifetime(20);
/* 91 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/CritParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */