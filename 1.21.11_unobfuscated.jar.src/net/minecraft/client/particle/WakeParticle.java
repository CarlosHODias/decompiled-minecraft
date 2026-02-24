/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class WakeParticle
/*    */   extends SingleQuadParticle {
/*    */   private WakeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
/* 12 */     this.sprites = sprites;
/* 13 */     this.xd *= 0.30000001192092896D;
/* 14 */     this.yd = (this.random.nextFloat() * 0.2F + 0.1F);
/* 15 */     this.zd *= 0.30000001192092896D;
/*    */     
/* 17 */     setSize(0.01F, 0.01F);
/*    */     
/* 19 */     this.lifetime = (int)(8.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/* 20 */     setSpriteFromAge(sprites);
/* 21 */     this.gravity = 0.0F;
/* 22 */     this.xd = xa;
/* 23 */     this.yd = ya;
/* 24 */     this.zd = za;
/*    */   }
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 29 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 34 */     this.xo = this.x;
/* 35 */     this.yo = this.y;
/* 36 */     this.zo = this.z;
/*    */     
/* 38 */     int life = 60 - this.lifetime;
/* 39 */     if (this.lifetime-- <= 0) {
/* 40 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     this.yd -= this.gravity;
/* 45 */     move(this.xd, this.yd, this.zd);
/* 46 */     this.xd *= 0.9800000190734863D;
/* 47 */     this.yd *= 0.9800000190734863D;
/* 48 */     this.zd *= 0.9800000190734863D;
/*    */     
/* 50 */     float size = life * 0.001F;
/* 51 */     setSize(size, size);
/*    */     
/* 53 */     setSprite(this.sprites.get(life % 4, 4));
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 60 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 65 */       return new WakeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/WakeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */