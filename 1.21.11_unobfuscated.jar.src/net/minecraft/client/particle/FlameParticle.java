/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FlameParticle extends RisingParticle {
/*    */   private FlameParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/* 11 */     super(level, x, y, z, xd, yd, zd, sprite);
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 16 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void move(double xa, double ya, double za) {
/* 21 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/* 22 */     setLocationFromBoundingbox();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 27 */     float s = (this.age + a) / this.lifetime;
/* 28 */     return this.quadSize * (1.0F - s * s * 0.5F);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 33 */     float l = (this.age + a) / this.lifetime;
/* 34 */     l = Mth.clamp(l, 0.0F, 1.0F);
/* 35 */     int br = super.getLightColor(a);
/*    */     
/* 37 */     int br1 = br & 0xFF;
/* 38 */     int br2 = br >> 16 & 0xFF;
/* 39 */     br1 += (int)(l * 15.0F * 16.0F);
/* 40 */     if (br1 > 240) {
/* 41 */       br1 = 240;
/*    */     }
/* 43 */     return br1 | br2 << 16;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 50 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 55 */       FlameParticle particle = new FlameParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 56 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SmallFlameProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public SmallFlameProvider(SpriteSet sprite) {
/* 64 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 69 */       FlameParticle particle = new FlameParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 70 */       particle.scale(0.5F);
/* 71 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FlameParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */