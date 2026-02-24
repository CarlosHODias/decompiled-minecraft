/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.player.PlayerEarsModel;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class Deadmau5EarsLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
/*    */   public Deadmau5EarsLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
/* 19 */     super(renderer);
/* 20 */     this.model = (HumanoidModel<AvatarRenderState>)new PlayerEarsModel(modelSet.bakeLayer(ModelLayers.PLAYER_EARS));
/*    */   }
/*    */   private final HumanoidModel<AvatarRenderState> model;
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
/* 25 */     if (!state.showExtraEars || state.isInvisible) {
/*    */       return;
/*    */     }
/*    */     
/* 29 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 30 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, RenderTypes.entitySolid(state.skin.body().texturePath()), lightCoords, overlayCoords, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/Deadmau5EarsLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */