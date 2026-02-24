/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class BaseAshSmokeParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   protected BaseAshSmokeParticle(ClientLevel level, double x, double y, double z, float dirX, float dirY, float dirZ, double xa, double ya, double za, float scale, SpriteSet sprites, float colorRandom, int maxLifetime, float gravity, boolean hasPhysics) {
/* 10 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
/* 11 */     this.friction = 0.96F;
/* 12 */     this.gravity = gravity;
/* 13 */     this.speedUpWhenYMotionIsBlocked = true;
/*    */     
/* 15 */     this.sprites = sprites;
/* 16 */     this.xd *= dirX;
/* 17 */     this.yd *= dirY;
/* 18 */     this.zd *= dirZ;
/* 19 */     this.xd += xa;
/* 20 */     this.yd += ya;
/* 21 */     this.zd += za;
/*    */     
/* 23 */     float col = this.random.nextFloat() * colorRandom;
/* 24 */     this.rCol = col;
/* 25 */     this.gCol = col;
/* 26 */     this.bCol = col;
/* 27 */     this.quadSize *= 0.75F * scale;
/*    */     
/* 29 */     this.lifetime = (int)(maxLifetime / (this.random.nextFloat() * 0.8D + 0.2D) * scale);
/* 30 */     this.lifetime = Math.max(this.lifetime, 1);
/* 31 */     setSpriteFromAge(sprites);
/* 32 */     this.hasPhysics = hasPhysics;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 37 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 42 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 47 */     super.tick();
/* 48 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/BaseAshSmokeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */