/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*    */ import net.minecraft.core.particles.DustColorTransitionOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class DustColorTransitionParticle extends DustParticleBase<DustColorTransitionOptions> {
/*    */   private final Vector3f fromColor;
/*    */   
/*    */   protected DustColorTransitionParticle(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, DustColorTransitionOptions options, SpriteSet sprites) {
/* 15 */     super(level, x, y, z, xAux, yAux, zAux, options, sprites);
/*    */     
/* 17 */     float baseFactor = this.random.nextFloat() * 0.4F + 0.6F;
/*    */     
/* 19 */     this.fromColor = randomizeColor(options.getFromColor(), baseFactor);
/* 20 */     this.toColor = randomizeColor(options.getToColor(), baseFactor);
/*    */   }
/*    */   private final Vector3f toColor;
/*    */   private Vector3f randomizeColor(Vector3f color, float baseFactor) {
/* 24 */     return new Vector3f(randomizeColor(color.x(), baseFactor), randomizeColor(color.y(), baseFactor), randomizeColor(color.z(), baseFactor));
/*    */   }
/*    */   
/*    */   private void lerpColors(float partialTickTime) {
/* 28 */     float a = (this.age + partialTickTime) / (this.lifetime + 1.0F);
/* 29 */     Vector3f lerpedColor = new Vector3f((Vector3fc)this.fromColor).lerp((Vector3fc)this.toColor, a);
/* 30 */     this.rCol = lerpedColor.x();
/* 31 */     this.gCol = lerpedColor.y();
/* 32 */     this.bCol = lerpedColor.z();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 37 */     lerpColors(partialTickTime);
/* 38 */     super.extract(particleTypeRenderState, camera, partialTickTime);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<DustColorTransitionOptions> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 45 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(DustColorTransitionOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 50 */       return new DustColorTransitionParticle(level, x, y, z, xAux, yAux, zAux, options, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/DustColorTransitionParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */