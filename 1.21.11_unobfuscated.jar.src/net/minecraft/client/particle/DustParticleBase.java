/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ScalableParticleOptionsBase;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DustParticleBase<T extends ScalableParticleOptionsBase> extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   protected DustParticleBase(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, T options, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, xAux, yAux, zAux, sprites.first());
/* 12 */     this.friction = 0.96F;
/* 13 */     this.speedUpWhenYMotionIsBlocked = true;
/* 14 */     this.sprites = sprites;
/* 15 */     this.xd *= 0.10000000149011612D;
/* 16 */     this.yd *= 0.10000000149011612D;
/* 17 */     this.zd *= 0.10000000149011612D;
/*    */     
/* 19 */     this.quadSize *= 0.75F * options.getScale();
/*    */     
/* 21 */     int baseLifetime = (int)(8.0D / (this.random.nextDouble() * 0.8D + 0.2D));
/* 22 */     this.lifetime = (int)Math.max(baseLifetime * options.getScale(), 1.0F);
/* 23 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   
/*    */   protected float randomizeColor(float color, float baseFactor) {
/* 27 */     return (this.random.nextFloat() * 0.2F + 0.8F) * color * baseFactor;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 32 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 37 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 42 */     super.tick();
/* 43 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/DustParticleBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */