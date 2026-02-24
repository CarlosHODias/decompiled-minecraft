/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.ParticleGroupRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ElderGuardianParticleGroup
/*    */   extends ParticleGroup<ElderGuardianParticle>
/*    */ {
/*    */   public ElderGuardianParticleGroup(ParticleEngine engine) {
/* 23 */     super(engine);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
/* 28 */     return new State(this.particles.stream().map(particle -> ElderGuardianParticleRenderState.fromParticle(particle, camera, partialTickTime)).toList());
/*    */   }
/*    */   private static final class State extends Record implements ParticleGroupRenderState { private final List<ElderGuardianParticleGroup.ElderGuardianParticleRenderState> states;
/* 31 */     private State(List<ElderGuardianParticleGroup.ElderGuardianParticleRenderState> states) { this.states = states; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 31 */       //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State; } public List<ElderGuardianParticleGroup.ElderGuardianParticleRenderState> states() { return this.states; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;
/*    */       //   0	8	1	o	Ljava/lang/Object; } public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 34 */       for (ElderGuardianParticleGroup.ElderGuardianParticleRenderState state : this.states)
/* 35 */         submitNodeCollector.submitModel(state.model, Unit.INSTANCE, state.poseStack, state.renderType, 15728880, OverlayTexture.NO_OVERLAY, state.color, null, 0, null); 
/*    */     } }
/*    */   private static final class ElderGuardianParticleRenderState extends Record { private final Model<Unit> model; private final PoseStack poseStack; private final RenderType renderType;
/*    */     private final int color;
/*    */     
/* 40 */     private ElderGuardianParticleRenderState(Model<Unit> model, PoseStack poseStack, RenderType renderType, int color) { this.model = model; this.poseStack = poseStack; this.renderType = renderType; this.color = color; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;
/* 40 */       //   0	8	1	o	Ljava/lang/Object; } public Model<Unit> model() { return this.model; } public PoseStack poseStack() { return this.poseStack; } public RenderType renderType() { return this.renderType; } public int color() { return this.color; }
/*    */      public static ElderGuardianParticleRenderState fromParticle(ElderGuardianParticle particle, Camera camera, float partialTickTime) {
/* 42 */       float ageScale = (particle.age + partialTickTime) / particle.lifetime;
/* 43 */       float alpha = 0.05F + 0.5F * Mth.sin((ageScale * 3.1415927F));
/* 44 */       int color = ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F);
/* 45 */       PoseStack poseStack = new PoseStack();
/*    */       
/* 47 */       poseStack.pushPose();
/* 48 */       poseStack.mulPose((Quaternionfc)camera.rotation());
/* 49 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(60.0F - 150.0F * ageScale));
/*    */       
/* 51 */       float scale = 0.42553192F;
/* 52 */       poseStack.scale(0.42553192F, -0.42553192F, -0.42553192F);
/* 53 */       poseStack.translate(0.0F, -0.56F, 3.5F);
/*    */       
/* 55 */       return new ElderGuardianParticleRenderState((Model<Unit>)particle.model, poseStack, particle.renderType, color);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ElderGuardianParticleGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */