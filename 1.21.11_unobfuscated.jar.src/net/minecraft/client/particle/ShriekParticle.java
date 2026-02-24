/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ShriekParticleOption;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public class ShriekParticle
/*    */   extends SingleQuadParticle {
/*    */   private static final float MAGICAL_X_ROT = 1.0472F;
/*    */   private int delay;
/*    */   
/*    */   private ShriekParticle(ClientLevel level, double x, double y, double z, int delay, TextureAtlasSprite sprite) {
/* 19 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/*    */     
/* 21 */     this.quadSize = 0.85F;
/*    */     
/* 23 */     this.delay = delay;
/* 24 */     this.lifetime = 30;
/* 25 */     this.gravity = 0.0F;
/*    */     
/* 27 */     this.xd = 0.0D;
/* 28 */     this.yd = 0.1D;
/* 29 */     this.zd = 0.0D;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 34 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 0.75F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 39 */     if (this.delay > 0) {
/*    */       return;
/*    */     }
/*    */     
/* 43 */     this.alpha = 1.0F - Mth.clamp((this.age + partialTickTime) / this.lifetime, 0.0F, 1.0F);
/*    */     
/* 45 */     Quaternionf rotation = new Quaternionf();
/* 46 */     rotation.rotationX(-1.0472F);
/* 47 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*    */     
/* 49 */     rotation.rotationYXZ(-3.1415927F, 1.0472F, 0.0F);
/* 50 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 55 */     return 240;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 60 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 65 */     if (this.delay > 0) {
/* 66 */       this.delay--;
/*    */       
/*    */       return;
/*    */     } 
/* 70 */     super.tick();
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<ShriekParticleOption> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 77 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(ShriekParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 82 */       ShriekParticle particle = new ShriekParticle(level, x, y, z, options.getDelay(), this.sprite.get(random));
/* 83 */       particle.setAlpha(1.0F);
/* 84 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ShriekParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */