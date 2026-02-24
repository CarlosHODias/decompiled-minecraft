/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.render.state.pip.GuiSignRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ 
/*    */ public class GuiSignRenderer extends PictureInPictureRenderer<GuiSignRenderState> {
/*    */   private final MaterialSet materials;
/*    */   
/*    */   public GuiSignRenderer(MultiBufferSource.BufferSource bufferSource, MaterialSet materials) {
/* 21 */     super(bufferSource);
/* 22 */     this.materials = materials;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<GuiSignRenderState> getRenderStateClass() {
/* 27 */     return GuiSignRenderState.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderToTexture(GuiSignRenderState renderState, PoseStack poseStack) {
/* 32 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
/* 33 */     poseStack.translate(0.0F, -0.75F, 0.0F);
/*    */     
/* 35 */     Material material = Sheets.getSignMaterial(renderState.woodType());
/* 36 */     Model.Simple model = renderState.signModel();
/* 37 */     Objects.requireNonNull(model); VertexConsumer buffer = material.buffer(this.materials, (MultiBufferSource)this.bufferSource, model::renderType);
/* 38 */     model.renderToBuffer(poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 43 */     return "sign";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/GuiSignRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */