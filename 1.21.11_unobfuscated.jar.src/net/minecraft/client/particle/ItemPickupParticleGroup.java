/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.ParticleGroupRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ItemPickupParticleGroup
/*    */   extends ParticleGroup<ItemPickupParticle> {
/*    */   public ItemPickupParticleGroup(ParticleEngine engine) {
/* 19 */     super(engine);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
/* 24 */     return new State(this.particles.stream().map(particle -> ParticleInstance.fromParticle(particle, camera, partialTickTime)).toList());
/*    */   }
/*    */   private static final class State extends Record implements ParticleGroupRenderState { private final List<ItemPickupParticleGroup.ParticleInstance> instances;
/* 27 */     private State(List<ItemPickupParticleGroup.ParticleInstance> instances) { this.instances = instances; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 27 */       //   0	7	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$State; } public List<ItemPickupParticleGroup.ParticleInstance> instances() { return this.instances; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$State; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;
/*    */       //   0	8	1	o	Ljava/lang/Object; } public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 30 */       PoseStack poseStack = new PoseStack();
/* 31 */       EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
/* 32 */       for (ItemPickupParticleGroup.ParticleInstance instance : this.instances)
/* 33 */         entityRenderDispatcher.submit(instance.itemRenderState, camera, instance.xOffset, instance.yOffset, instance.zOffset, poseStack, submitNodeCollector); 
/*    */     } }
/*    */   private static final class ParticleInstance extends Record { private final EntityRenderState itemRenderState; private final double xOffset; private final double yOffset;
/*    */     private final double zOffset;
/*    */     
/* 38 */     private ParticleInstance(EntityRenderState itemRenderState, double xOffset, double yOffset, double zOffset) { this.itemRenderState = itemRenderState; this.xOffset = xOffset; this.yOffset = yOffset; this.zOffset = zOffset; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$ParticleInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$ParticleInstance; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$ParticleInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$ParticleInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$ParticleInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$ParticleInstance;
/* 38 */       //   0	8	1	o	Ljava/lang/Object; } public EntityRenderState itemRenderState() { return this.itemRenderState; } public double xOffset() { return this.xOffset; } public double yOffset() { return this.yOffset; } public double zOffset() { return this.zOffset; }
/*    */      public static ParticleInstance fromParticle(ItemPickupParticle particle, Camera camera, float partialTickTime) {
/* 40 */       float time = (particle.life + partialTickTime) / 3.0F;
/* 41 */       time *= time;
/*    */       
/* 43 */       double xt = Mth.lerp(partialTickTime, particle.targetXOld, particle.targetX);
/* 44 */       double yt = Mth.lerp(partialTickTime, particle.targetYOld, particle.targetY);
/* 45 */       double zt = Mth.lerp(partialTickTime, particle.targetZOld, particle.targetZ);
/*    */       
/* 47 */       double xx = Mth.lerp(time, particle.itemRenderState.x, xt);
/* 48 */       double yy = Mth.lerp(time, particle.itemRenderState.y, yt);
/* 49 */       double zz = Mth.lerp(time, particle.itemRenderState.z, zt);
/*    */       
/* 51 */       Vec3 pos = camera.position();
/* 52 */       return new ParticleInstance(particle.itemRenderState, xx - pos.x(), yy - pos.y(), zz - pos.z());
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ItemPickupParticleGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */