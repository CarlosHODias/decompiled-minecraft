/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class AttackSweepParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   private AttackSweepParticle(ClientLevel level, double x, double y, double z, double size, SpriteSet sprites) {
/* 12 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
/* 13 */     this.sprites = sprites;
/* 14 */     this.lifetime = 4;
/* 15 */     float col = this.random.nextFloat() * 0.6F + 0.4F;
/* 16 */     this.rCol = col;
/* 17 */     this.gCol = col;
/* 18 */     this.bCol = col;
/* 19 */     this.quadSize = 1.0F - (float)size * 0.5F;
/* 20 */     setSpriteFromAge(sprites);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 25 */     return 15728880;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 30 */     this.xo = this.x;
/* 31 */     this.yo = this.y;
/* 32 */     this.zo = this.z;
/*    */     
/* 34 */     if (this.age++ >= this.lifetime) {
/* 35 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 44 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 51 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 56 */       return new AttackSweepParticle(level, x, y, z, xAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/AttackSweepParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */