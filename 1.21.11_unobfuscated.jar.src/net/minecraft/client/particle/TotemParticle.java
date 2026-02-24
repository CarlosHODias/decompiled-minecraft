/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class TotemParticle extends SimpleAnimatedParticle {
/*    */   private TotemParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/*  9 */     super(level, x, y, z, sprites, 1.25F);
/*    */     
/* 11 */     this.friction = 0.6F;
/*    */     
/* 13 */     this.xd = xa;
/* 14 */     this.yd = ya;
/* 15 */     this.zd = za;
/*    */     
/* 17 */     this.quadSize *= 0.75F;
/*    */     
/* 19 */     this.lifetime = 60 + this.random.nextInt(12);
/* 20 */     setSpriteFromAge(sprites);
/*    */     
/* 22 */     if (this.random.nextInt(4) == 0) {
/* 23 */       setColor(0.6F + this.random.nextFloat() * 0.2F, 0.6F + this.random.nextFloat() * 0.3F, this.random.nextFloat() * 0.2F);
/*    */     } else {
/* 25 */       setColor(0.1F + this.random.nextFloat() * 0.2F, 0.4F + this.random.nextFloat() * 0.3F, this.random.nextFloat() * 0.2F);
/*    */     } 
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
/* 38 */       return new TotemParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/TotemParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */