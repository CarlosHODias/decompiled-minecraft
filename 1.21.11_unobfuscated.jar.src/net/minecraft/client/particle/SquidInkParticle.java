/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SquidInkParticle extends SimpleAnimatedParticle {
/*    */   private SquidInkParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, int color, SpriteSet sprites) {
/* 12 */     super(level, x, y, z, sprites, 0.0F);
/* 13 */     this.friction = 0.92F;
/* 14 */     this.quadSize = 0.5F;
/*    */     
/* 16 */     setAlpha(1.0F);
/* 17 */     setColor(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color));
/*    */     
/* 19 */     this.lifetime = (int)(this.quadSize * 12.0F / (this.random.nextFloat() * 0.8F + 0.2F));
/* 20 */     setSpriteFromAge(sprites);
/*    */     
/* 22 */     this.hasPhysics = false;
/*    */     
/* 24 */     this.xd = xa;
/* 25 */     this.yd = ya;
/* 26 */     this.zd = za;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 31 */     super.tick();
/* 32 */     if (!this.removed) {
/* 33 */       setSpriteFromAge(this.sprites);
/* 34 */       if (this.age > this.lifetime / 2) {
/* 35 */         setAlpha(1.0F - (this.age - (this.lifetime / 2)) / this.lifetime);
/*    */       }
/* 37 */       if (this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir())
/* 38 */         this.yd -= 0.007400000002235174D; 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Provider
/*    */     implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 47 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 52 */       return new SquidInkParticle(level, x, y, z, xAux, yAux, zAux, -16777216, this.sprites);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class GlowInkProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public GlowInkProvider(SpriteSet sprites) {
/* 60 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 65 */       return new SquidInkParticle(level, x, y, z, xAux, yAux, zAux, ARGB.colorFromFloat(1.0F, 0.2F, 0.8F, 0.6F), this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SquidInkParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */