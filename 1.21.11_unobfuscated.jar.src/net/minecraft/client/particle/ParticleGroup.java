/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.google.common.collect.EvictingQueue;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.Queue;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.state.ParticleGroupRenderState;
/*    */ import net.minecraft.core.particles.ParticleLimit;
/*    */ 
/*    */ public abstract class ParticleGroup<P extends Particle> {
/*    */   private static final int MAX_PARTICLES = 16384;
/*    */   protected final ParticleEngine engine;
/* 18 */   protected final Queue<P> particles = (Queue<P>)EvictingQueue.create(16384);
/*    */   
/*    */   public ParticleGroup(ParticleEngine engine) {
/* 21 */     this.engine = engine;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 25 */     return this.particles.isEmpty();
/*    */   }
/*    */   
/*    */   public void tickParticles() {
/* 29 */     if (!this.particles.isEmpty()) {
/* 30 */       for (Iterator<P> iterator = this.particles.iterator(); iterator.hasNext(); ) {
/* 31 */         Particle particle = (Particle)iterator.next();
/* 32 */         tickParticle(particle);
/*    */         
/* 34 */         if (!particle.isAlive()) {
/* 35 */           particle.getParticleLimit().ifPresent(options -> this.engine.updateCount(options, -1));
/* 36 */           iterator.remove();
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void tickParticle(Particle particle) {
/*    */     try {
/* 44 */       particle.tick();
/* 45 */     } catch (Throwable t) {
/* 46 */       CrashReport report = CrashReport.forThrowable(t, "Ticking Particle");
/* 47 */       CrashReportCategory category = report.addCategory("Particle being ticked");
/* 48 */       Objects.requireNonNull(particle); category.setDetail("Particle", particle::toString);
/* 49 */       Objects.requireNonNull(particle.getGroup()); category.setDetail("Particle Type", particle.getGroup()::toString);
/*    */       
/* 51 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Particle particle) {
/* 56 */     this.particles.add((P)particle);
/*    */   }
/*    */   
/*    */   public int size() {
/* 60 */     return this.particles.size();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Queue<P> getAll() {
/* 66 */     return this.particles;
/*    */   }
/*    */   
/*    */   public abstract ParticleGroupRenderState extractRenderState(Frustum paramFrustum, Camera paramCamera, float paramFloat);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ParticleGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */