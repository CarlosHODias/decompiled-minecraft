/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class BubblePopParticle
/*    */   extends SingleQuadParticle {
/*    */   private BubblePopParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, sprites.first());
/* 12 */     this.sprites = sprites;
/* 13 */     this.lifetime = 4;
/* 14 */     this.gravity = 0.008F;
/* 15 */     this.xd = xa;
/* 16 */     this.yd = ya;
/* 17 */     this.zd = za;
/* 18 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public void tick() {
/* 23 */     this.xo = this.x;
/* 24 */     this.yo = this.y;
/* 25 */     this.zo = this.z;
/*    */     
/* 27 */     if (this.age++ >= this.lifetime) {
/* 28 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     this.yd -= this.gravity;
/* 33 */     move(this.xd, this.yd, this.zd);
/*    */     
/* 35 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 40 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 47 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 52 */       return new BubblePopParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/BubblePopParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */