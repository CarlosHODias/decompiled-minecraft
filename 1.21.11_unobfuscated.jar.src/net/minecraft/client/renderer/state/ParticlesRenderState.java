/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ 
/*    */ public class ParticlesRenderState {
/*  9 */   public final List<ParticleGroupRenderState> particles = new ArrayList<>();
/*    */   
/*    */   public void reset() {
/* 12 */     this.particles.forEach(ParticleGroupRenderState::clear);
/* 13 */     this.particles.clear();
/*    */   }
/*    */   
/*    */   public void add(ParticleGroupRenderState state) {
/* 17 */     this.particles.add(state);
/*    */   }
/*    */   
/*    */   public void submit(SubmitNodeStorage submitNodeStorage, CameraRenderState camera) {
/* 21 */     for (ParticleGroupRenderState particle : this.particles)
/* 22 */       particle.submit((SubmitNodeCollector)submitNodeStorage, camera); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/ParticlesRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */