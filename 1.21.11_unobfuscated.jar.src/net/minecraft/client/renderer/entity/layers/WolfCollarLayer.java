/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.wolf.WolfModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class WolfCollarLayer extends RenderLayer<WolfRenderState, WolfModel> {
/* 14 */   private static final Identifier WOLF_COLLAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/wolf/wolf_collar.png");
/*    */   
/*    */   public WolfCollarLayer(RenderLayerParent<WolfRenderState, WolfModel> renderer) {
/* 17 */     super(renderer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, WolfRenderState state, float yRot, float xRot) {
/* 23 */     DyeColor collarColor = state.collarColor;
/* 24 */     if (collarColor == null || state.isInvisible) {
/*    */       return;
/*    */     }
/*    */     
/* 28 */     int color = collarColor.getTextureDiffuseColor();
/* 29 */     submitNodeCollector.order(1).submitModel((net.minecraft.client.model.Model)getParentModel(), state, poseStack, RenderTypes.entityCutoutNoCull(WOLF_COLLAR_LOCATION), lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/WolfCollarLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */