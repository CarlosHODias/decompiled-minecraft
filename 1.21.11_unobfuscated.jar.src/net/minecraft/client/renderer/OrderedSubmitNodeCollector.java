/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface OrderedSubmitNodeCollector
/*    */ {
/*    */   void submitShadow(PoseStack paramPoseStack, float paramFloat, List<EntityRenderState.ShadowPiece> paramList);
/*    */   
/*    */   void submitNameTag(PoseStack paramPoseStack, Vec3 paramVec3, int paramInt1, Component paramComponent, boolean paramBoolean, int paramInt2, double paramDouble, CameraRenderState paramCameraRenderState);
/*    */   
/*    */   void submitText(PoseStack paramPoseStack, float paramFloat1, float paramFloat2, FormattedCharSequence paramFormattedCharSequence, boolean paramBoolean, Font.DisplayMode paramDisplayMode, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   void submitFlame(PoseStack paramPoseStack, EntityRenderState paramEntityRenderState, Quaternionf paramQuaternionf);
/*    */   
/*    */   void submitLeash(PoseStack paramPoseStack, EntityRenderState.LeashState paramLeashState);
/*    */   
/*    */   <S> void submitModel(Model<? super S> paramModel, S paramS, PoseStack paramPoseStack, RenderType paramRenderType, int paramInt1, int paramInt2, int paramInt3, TextureAtlasSprite paramTextureAtlasSprite, int paramInt4, ModelFeatureRenderer.CrumblingOverlay paramCrumblingOverlay);
/*    */   
/*    */   default <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 41 */     submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, -1, null, outlineColor, crumblingOverlay);
/*    */   }
/*    */   
/*    */   default void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite) {
/* 45 */     submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, false, false, -1, null, 0);
/*    */   }
/*    */   
/*    */   default void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, int tintedColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 49 */     submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, false, false, tintedColor, crumblingOverlay, 0);
/*    */   }
/*    */   
/*    */   default void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, boolean sheeted, boolean hasFoil) {
/* 53 */     submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, sheeted, hasFoil, -1, null, 0);
/*    */   }
/*    */   
/*    */   void submitModelPart(ModelPart paramModelPart, PoseStack paramPoseStack, RenderType paramRenderType, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, ModelFeatureRenderer.CrumblingOverlay paramCrumblingOverlay, int paramInt4);
/*    */   
/*    */   void submitBlock(PoseStack paramPoseStack, BlockState paramBlockState, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   void submitMovingBlock(PoseStack paramPoseStack, MovingBlockRenderState paramMovingBlockRenderState);
/*    */   
/*    */   void submitBlockModel(PoseStack paramPoseStack, RenderType paramRenderType, BlockStateModel paramBlockStateModel, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   void submitItem(PoseStack paramPoseStack, ItemDisplayContext paramItemDisplayContext, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint, List<BakedQuad> paramList, RenderType paramRenderType, ItemStackRenderState.FoilType paramFoilType);
/*    */   
/*    */   void submitCustomGeometry(PoseStack paramPoseStack, RenderType paramRenderType, SubmitNodeCollector.CustomGeometryRenderer paramCustomGeometryRenderer);
/*    */   
/*    */   void submitParticleGroup(SubmitNodeCollector.ParticleGroupRenderer paramParticleGroupRenderer);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/OrderedSubmitNodeCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */