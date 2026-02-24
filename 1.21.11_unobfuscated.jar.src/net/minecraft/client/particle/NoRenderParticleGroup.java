/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.ParticleGroupRenderState;
/*    */ 
/*    */ public class NoRenderParticleGroup extends ParticleGroup<NoRenderParticle> {
/*    */   public NoRenderParticleGroup(ParticleEngine engine) {
/* 11 */     super(engine);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
/* 16 */     return EMPTY_RENDER_STATE;
/*    */   }
/*    */   
/*    */   private static final ParticleGroupRenderState EMPTY_RENDER_STATE = (ignored, camera) -> {
/*    */     
/*    */     };
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/NoRenderParticleGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */