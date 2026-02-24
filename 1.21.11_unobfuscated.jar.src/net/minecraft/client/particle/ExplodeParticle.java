/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ExplodeParticle
/*    */   extends SingleQuadParticle {
/*    */   protected ExplodeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, sprites.first());
/* 12 */     this.gravity = -0.1F;
/* 13 */     this.friction = 0.9F;
/* 14 */     this.sprites = sprites;
/* 15 */     this.xd = xa + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
/* 16 */     this.yd = ya + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
/* 17 */     this.zd = za + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
/*    */     
/* 19 */     float col = this.random.nextFloat() * 0.3F + 0.7F;
/* 20 */     this.rCol = col;
/* 21 */     this.gCol = col;
/* 22 */     this.bCol = col;
/* 23 */     this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 6.0F + 1.0F);
/*    */     
/* 25 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D)) + 2;
/* 26 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 31 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     super.tick();
/* 37 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 44 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */       return new ExplodeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ExplodeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */