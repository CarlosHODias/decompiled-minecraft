/*    */ package net.minecraft.client.gui.render.pip;
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.render.state.pip.GuiBookModelRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class GuiBookModelRenderer extends PictureInPictureRenderer<GuiBookModelRenderState> {
/*    */   public GuiBookModelRenderer(MultiBufferSource.BufferSource bufferSource) {
/* 18 */     super(bufferSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<GuiBookModelRenderState> getRenderStateClass() {
/* 23 */     return GuiBookModelRenderState.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderToTexture(GuiBookModelRenderState bookModelState, PoseStack poseStack) {
/* 28 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
/*    */     
/* 30 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 31 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(25.0F));
/* 32 */     float open = bookModelState.open();
/* 33 */     poseStack.translate((1.0F - open) * 0.2F, (1.0F - open) * 0.1F, (1.0F - open) * 0.25F);
/* 34 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-(1.0F - open) * 90.0F - 90.0F));
/* 35 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(180.0F));
/*    */     
/* 37 */     float flip = bookModelState.flip();
/* 38 */     float pageFlip1 = Mth.clamp(Mth.frac(flip + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
/* 39 */     float pageFlip2 = Mth.clamp(Mth.frac(flip + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
/*    */     
/* 41 */     BookModel bookModel = bookModelState.bookModel();
/* 42 */     bookModel.setupAnim(new BookModel.State(0.0F, pageFlip1, pageFlip2, open));
/*    */     
/* 44 */     Identifier texture = bookModelState.texture();
/*    */     
/* 46 */     VertexConsumer buffer = this.bufferSource.getBuffer(bookModel.renderType(texture));
/* 47 */     bookModel.renderToBuffer(poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 52 */     return (17 * guiScale);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 57 */     return "book model";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/GuiBookModelRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */