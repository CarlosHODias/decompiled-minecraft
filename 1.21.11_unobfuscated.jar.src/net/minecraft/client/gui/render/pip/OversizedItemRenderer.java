/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.render.state.GuiItemRenderState;
/*    */ import net.minecraft.client.gui.render.state.GuiRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.OversizedItemRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*    */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ 
/*    */ 
/*    */ public class OversizedItemRenderer
/*    */   extends PictureInPictureRenderer<OversizedItemRenderState>
/*    */ {
/*    */   private boolean usedOnThisFrame;
/*    */   private Object modelOnTextureIdentity;
/*    */   
/*    */   public OversizedItemRenderer(MultiBufferSource.BufferSource bufferSource) {
/* 27 */     super(bufferSource);
/*    */   }
/*    */   
/*    */   public boolean usedOnThisFrame() {
/* 31 */     return this.usedOnThisFrame;
/*    */   }
/*    */   
/*    */   public void resetUsedOnThisFrame() {
/* 35 */     this.usedOnThisFrame = false;
/*    */   }
/*    */   
/*    */   public void invalidateTexture() {
/* 39 */     this.modelOnTextureIdentity = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<OversizedItemRenderState> getRenderStateClass() {
/* 44 */     return OversizedItemRenderState.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderToTexture(OversizedItemRenderState renderState, PoseStack poseStack) {
/* 49 */     poseStack.scale(1.0F, -1.0F, -1.0F);
/* 50 */     GuiItemRenderState guiItemRenderState = renderState.guiItemRenderState();
/* 51 */     ScreenRectangle itemBounds = guiItemRenderState.oversizedItemBounds();
/* 52 */     Objects.requireNonNull(itemBounds);
/* 53 */     float itemBoundsCenterX = (itemBounds.left() + itemBounds.right()) / 2.0F;
/* 54 */     float itemBoundsCenterY = (itemBounds.top() + itemBounds.bottom()) / 2.0F;
/* 55 */     float slotCenterX = guiItemRenderState.x() + 8.0F;
/* 56 */     float slotCenterY = guiItemRenderState.y() + 8.0F;
/* 57 */     poseStack.translate((slotCenterX - itemBoundsCenterX) / 16.0F, (itemBoundsCenterY - slotCenterY) / 16.0F, 0.0F);
/* 58 */     TrackingItemStackRenderState itemStackRenderState = guiItemRenderState.itemStackRenderState();
/* 59 */     boolean flat = !itemStackRenderState.usesBlockLight();
/* 60 */     if (flat) {
/* 61 */       (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
/*    */     } else {
/* 63 */       (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
/*    */     } 
/* 65 */     FeatureRenderDispatcher featureRenderDispatcher = (Minecraft.getInstance()).gameRenderer.getFeatureRenderDispatcher();
/* 66 */     SubmitNodeStorage submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();
/* 67 */     itemStackRenderState.submit(poseStack, (SubmitNodeCollector)submitNodeStorage, 15728880, OverlayTexture.NO_OVERLAY, 0);
/* 68 */     featureRenderDispatcher.renderAllFeatures();
/* 69 */     this.modelOnTextureIdentity = itemStackRenderState.getModelIdentity();
/*    */   }
/*    */ 
/*    */   
/*    */   public void blitTexture(OversizedItemRenderState renderState, GuiRenderState guiRenderState) {
/* 74 */     super.blitTexture(renderState, guiRenderState);
/* 75 */     this.usedOnThisFrame = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean textureIsReadyToBlit(OversizedItemRenderState renderState) {
/* 80 */     TrackingItemStackRenderState itemStackRenderState = renderState.guiItemRenderState().itemStackRenderState();
/* 81 */     return (!itemStackRenderState.isAnimated() && itemStackRenderState.getModelIdentity().equals(this.modelOnTextureIdentity));
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 86 */     return height / 2.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 91 */     return "oversized_item";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/OversizedItemRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */