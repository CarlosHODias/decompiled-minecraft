/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class WhiteAshParticle
/*    */   extends BaseAshSmokeParticle {
/*    */   protected WhiteAshParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
/* 12 */     super(level, x, y, z, 0.1F, -0.1F, 0.1F, xa, ya, za, scale, sprites, 0.0F, 20, 0.0125F, false);
/* 13 */     this.rCol = ARGB.red(12235202) / 255.0F;
/* 14 */     this.gCol = ARGB.green(12235202) / 255.0F;
/* 15 */     this.bCol = ARGB.blue(12235202) / 255.0F;
/*    */   }
/*    */   private static final int COLOR_RGB24 = 12235202;
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> { private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 22 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 27 */       double xa = random.nextFloat() * -1.9D * random.nextFloat() * 0.1D;
/* 28 */       double ya = random.nextFloat() * -0.5D * random.nextFloat() * 0.1D * 5.0D;
/* 29 */       double za = random.nextFloat() * -1.9D * random.nextFloat() * 0.1D;
/* 30 */       return new WhiteAshParticle(level, x, y, z, xa, ya, za, 1.0F, this.sprites);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/WhiteAshParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */