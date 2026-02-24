/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SonicBoomParticle extends HugeExplosionParticle {
/*    */   protected SonicBoomParticle(ClientLevel level, double x, double y, double z, double size, SpriteSet sprites) {
/*  9 */     super(level, x, y, z, size, sprites);
/*    */     
/* 11 */     this.lifetime = 16;
/* 12 */     this.quadSize = 1.5F;
/*    */     
/* 14 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 21 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 26 */       return new SonicBoomParticle(level, x, y, z, xAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SonicBoomParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */