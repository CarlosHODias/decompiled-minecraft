/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class NoteParticle extends SingleQuadParticle {
/*    */   private NoteParticle(ClientLevel level, double x, double y, double z, double color, TextureAtlasSprite sprite) {
/* 11 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 12 */     this.friction = 0.66F;
/* 13 */     this.speedUpWhenYMotionIsBlocked = true;
/* 14 */     this.xd *= 0.009999999776482582D;
/* 15 */     this.yd *= 0.009999999776482582D;
/* 16 */     this.zd *= 0.009999999776482582D;
/* 17 */     this.yd += 0.2D;
/*    */     
/* 19 */     this.rCol = Math.max(0.0F, Mth.sin((((float)color + 0.0F) * 6.2831855F)) * 0.65F + 0.35F);
/* 20 */     this.gCol = Math.max(0.0F, Mth.sin((((float)color + 0.33333334F) * 6.2831855F)) * 0.65F + 0.35F);
/* 21 */     this.bCol = Math.max(0.0F, Mth.sin((((float)color + 0.6666667F) * 6.2831855F)) * 0.65F + 0.35F);
/*    */     
/* 23 */     this.quadSize *= 1.5F;
/* 24 */     this.lifetime = 6;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 29 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 34 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 41 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 46 */       NoteParticle particle = new NoteParticle(level, x, y, z, xAux, this.sprite.get(random));
/* 47 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/NoteParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */