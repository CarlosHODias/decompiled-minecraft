/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.squid.SquidModel;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SquidRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.animal.squid.Squid;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SquidRenderer<T extends Squid> extends AgeableMobRenderer<T, SquidRenderState, SquidModel> {
/* 12 */   private static final Identifier SQUID_LOCATION = Identifier.withDefaultNamespace("textures/entity/squid/squid.png");
/*    */   
/*    */   public SquidRenderer(EntityRendererProvider.Context context, SquidModel model, SquidModel babyModel) {
/* 15 */     super(context, model, babyModel, 0.7F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SquidRenderState state) {
/* 20 */     return SQUID_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SquidRenderState createRenderState() {
/* 25 */     return new SquidRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, SquidRenderState state, float partialTicks) {
/* 30 */     super.extractRenderState(entity, state, partialTicks);
/* 31 */     state.tentacleAngle = Mth.lerp(partialTicks, ((Squid)entity).oldTentacleAngle, ((Squid)entity).tentacleAngle);
/* 32 */     state.xBodyRot = Mth.lerp(partialTicks, ((Squid)entity).xBodyRotO, ((Squid)entity).xBodyRot);
/* 33 */     state.zBodyRot = Mth.lerp(partialTicks, ((Squid)entity).zBodyRotO, ((Squid)entity).zBodyRot);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(SquidRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 38 */     poseStack.translate(0.0F, state.isBaby ? 0.25F : 0.5F, 0.0F);
/* 39 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(180.0F - bodyRot));
/* 40 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.XP.rotationDegrees(state.xBodyRot));
/* 41 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(state.zBodyRot));
/* 42 */     poseStack.translate(0.0F, state.isBaby ? -0.6F : -1.2F, 0.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SquidRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */