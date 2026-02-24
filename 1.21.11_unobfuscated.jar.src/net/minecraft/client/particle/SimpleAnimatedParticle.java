/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ 
/*    */ public abstract class SimpleAnimatedParticle
/*    */   extends SingleQuadParticle
/*    */ {
/*    */   protected final SpriteSet sprites;
/*    */   private float fadeR;
/*    */   private float fadeG;
/*    */   private float fadeB;
/*    */   private boolean hasFade;
/*    */   
/*    */   protected SimpleAnimatedParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float gravity) {
/* 15 */     super(level, x, y, z, sprites.first());
/* 16 */     this.friction = 0.91F;
/* 17 */     this.gravity = gravity;
/* 18 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public void setColor(int rgb) {
/* 22 */     float r = ((rgb & 0xFF0000) >> 16) / 255.0F;
/* 23 */     float g = ((rgb & 0xFF00) >> 8) / 255.0F;
/* 24 */     float b = ((rgb & 0xFF) >> 0) / 255.0F;
/* 25 */     float scale = 1.0F;
/* 26 */     setColor(r * 1.0F, g * 1.0F, b * 1.0F);
/*    */   }
/*    */   
/*    */   public void setFadeColor(int rgb) {
/* 30 */     this.fadeR = ((rgb & 0xFF0000) >> 16) / 255.0F;
/* 31 */     this.fadeG = ((rgb & 0xFF00) >> 8) / 255.0F;
/* 32 */     this.fadeB = ((rgb & 0xFF) >> 0) / 255.0F;
/* 33 */     this.hasFade = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 38 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 43 */     super.tick();
/* 44 */     setSpriteFromAge(this.sprites);
/* 45 */     if (this.age > this.lifetime / 2) {
/* 46 */       setAlpha(1.0F - (this.age - (this.lifetime / 2)) / this.lifetime);
/*    */       
/* 48 */       if (this.hasFade) {
/* 49 */         this.rCol += (this.fadeR - this.rCol) * 0.2F;
/* 50 */         this.gCol += (this.fadeG - this.gCol) * 0.2F;
/* 51 */         this.bCol += (this.fadeB - this.bCol) * 0.2F;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 58 */     return 15728880;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SimpleAnimatedParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */