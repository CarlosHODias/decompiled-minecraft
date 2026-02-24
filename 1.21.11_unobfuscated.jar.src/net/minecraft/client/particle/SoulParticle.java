/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SoulParticle extends RisingParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   private SoulParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
/* 12 */     super(level, x, y, z, xd, yd, zd, sprites.first());
/*    */     
/* 14 */     this.sprites = sprites;
/* 15 */     scale(1.5F);
/*    */     
/* 17 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   protected boolean isGlowing;
/*    */   
/*    */   public int getLightColor(float a) {
/* 22 */     if (this.isGlowing) {
/* 23 */       return 240;
/*    */     }
/*    */     
/* 26 */     return super.getLightColor(a);
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 31 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     super.tick();
/* 37 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 44 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */       SoulParticle particle = new SoulParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 50 */       particle.setAlpha(1.0F);
/* 51 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class EmissiveProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public EmissiveProvider(SpriteSet sprite) {
/* 59 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 64 */       SoulParticle particle = new SoulParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 65 */       particle.setAlpha(1.0F);
/* 66 */       particle.isGlowing = true;
/* 67 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SoulParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */