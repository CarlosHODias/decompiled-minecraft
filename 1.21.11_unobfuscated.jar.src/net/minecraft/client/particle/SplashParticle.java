/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SplashParticle extends WaterDropParticle {
/*    */   private SplashParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/* 10 */     super(level, x, y, z, sprite);
/* 11 */     this.gravity = 0.04F;
/* 12 */     if (ya == 0.0D && (xa != 0.0D || za != 0.0D)) {
/* 13 */       this.xd = xa;
/* 14 */       this.yd = 0.1D;
/* 15 */       this.zd = za;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 23 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 28 */       return new SplashParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SplashParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */