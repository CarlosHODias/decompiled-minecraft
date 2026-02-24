/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class GustParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   protected GustParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
/* 12 */     super(level, x, y, z, sprites.first());
/* 13 */     this.sprites = sprites;
/* 14 */     setSpriteFromAge(sprites);
/* 15 */     this.lifetime = 12 + this.random.nextInt(4);
/* 16 */     this.quadSize = 1.0F;
/* 17 */     setSize(1.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 22 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 27 */     return 15728880;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 32 */     if (this.age++ >= this.lifetime) {
/* 33 */       remove();
/*    */       return;
/*    */     } 
/* 36 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 43 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 48 */       return new GustParticle(level, x, y, z, this.sprites);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SmallProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public SmallProvider(SpriteSet sprites) {
/* 56 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 61 */       Particle particle = new GustParticle(level, x, y, z, this.sprites);
/* 62 */       particle.scale(0.15F);
/* 63 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/GustParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */