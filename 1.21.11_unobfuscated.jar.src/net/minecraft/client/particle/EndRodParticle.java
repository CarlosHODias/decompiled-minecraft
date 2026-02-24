/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class EndRodParticle extends SimpleAnimatedParticle {
/*    */   private EndRodParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/*  9 */     super(level, x, y, z, sprites, 0.0125F);
/*    */     
/* 11 */     this.xd = xa;
/* 12 */     this.yd = ya;
/* 13 */     this.zd = za;
/*    */     
/* 15 */     this.quadSize *= 0.75F;
/*    */     
/* 17 */     this.lifetime = 60 + this.random.nextInt(12);
/*    */     
/* 19 */     setFadeColor(15916745);
/* 20 */     setSpriteFromAge(sprites);
/*    */   }
/*    */ 
/*    */   
/*    */   public void move(double xa, double ya, double za) {
/* 25 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/* 26 */     setLocationFromBoundingbox();
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 33 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 38 */       return new EndRodParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/EndRodParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */