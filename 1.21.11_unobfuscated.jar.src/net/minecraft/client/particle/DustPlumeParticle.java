/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class DustPlumeParticle
/*    */   extends BaseAshSmokeParticle {
/*    */   protected DustPlumeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
/* 12 */     super(level, x, y, z, 0.7F, 0.6F, 0.7F, xa, ya + 0.15000000596046448D, za, scale, sprites, 0.5F, 7, 0.5F, false);
/* 13 */     float colorShift = this.random.nextFloat() * 0.2F;
/* 14 */     this.rCol = ARGB.red(12235202) / 255.0F - colorShift;
/* 15 */     this.gCol = ARGB.green(12235202) / 255.0F - colorShift;
/* 16 */     this.bCol = ARGB.blue(12235202) / 255.0F - colorShift;
/*    */   }
/*    */   private static final int COLOR_RGB24 = 12235202;
/*    */   
/*    */   public void tick() {
/* 21 */     this.gravity = 0.88F * this.gravity;
/* 22 */     this.friction = 0.92F * this.friction;
/* 23 */     super.tick();
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 30 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 35 */       return new DustPlumeParticle(level, x, y, z, xAux, yAux, zAux, 1.0F, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/DustPlumeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */