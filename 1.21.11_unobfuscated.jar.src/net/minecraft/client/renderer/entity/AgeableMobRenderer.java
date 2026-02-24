/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ @Deprecated
/*    */ public abstract class AgeableMobRenderer<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends MobRenderer<T, S, M> {
/*    */   private final M adultModel;
/*    */   private final M babyModel;
/*    */   
/*    */   public AgeableMobRenderer(EntityRendererProvider.Context context, M adultModel, M babyModel, float shadow) {
/* 17 */     super(context, adultModel, shadow);
/* 18 */     this.adultModel = adultModel;
/* 19 */     this.babyModel = babyModel;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 24 */     this.model = ((LivingEntityRenderState)state).isBaby ? this.babyModel : this.adultModel;
/* 25 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AgeableMobRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */