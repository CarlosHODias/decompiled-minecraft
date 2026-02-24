/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class HeartParticle extends SingleQuadParticle {
/*    */   private HeartParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 11 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 12 */     this.speedUpWhenYMotionIsBlocked = true;
/* 13 */     this.friction = 0.86F;
/* 14 */     this.xd *= 0.009999999776482582D;
/* 15 */     this.yd *= 0.009999999776482582D;
/* 16 */     this.zd *= 0.009999999776482582D;
/* 17 */     this.yd += 0.1D;
/*    */     
/* 19 */     this.quadSize *= 1.5F;
/* 20 */     this.lifetime = 16;
/* 21 */     this.hasPhysics = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 26 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 31 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 38 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 43 */       HeartParticle particle = new HeartParticle(level, x, y, z, this.sprite.get(random));
/* 44 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class AngryVillagerProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public AngryVillagerProvider(SpriteSet sprite) {
/* 52 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 57 */       HeartParticle particle = new HeartParticle(level, x, y + 0.5D, z, this.sprite.get(random));
/* 58 */       particle.setColor(1.0F, 1.0F, 1.0F);
/* 59 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/HeartParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */