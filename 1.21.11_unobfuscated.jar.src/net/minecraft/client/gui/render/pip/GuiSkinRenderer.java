/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.render.state.pip.GuiSkinRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Matrix4fStack;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class GuiSkinRenderer
/*    */   extends PictureInPictureRenderer<GuiSkinRenderState> {
/*    */   public GuiSkinRenderer(MultiBufferSource.BufferSource bufferSource) {
/* 19 */     super(bufferSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<GuiSkinRenderState> getRenderStateClass() {
/* 24 */     return GuiSkinRenderState.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderToTexture(GuiSkinRenderState skinState, PoseStack modelStack) {
/* 29 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.PLAYER_SKIN);
/*    */     
/* 31 */     int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
/* 32 */     Matrix4fStack viewStack = RenderSystem.getModelViewStack();
/* 33 */     viewStack.pushMatrix();
/* 34 */     float scale = skinState.scale() * guiScale;
/* 35 */     viewStack.rotateAround((Quaternionfc)Axis.XP.rotationDegrees(skinState.rotationX()), 0.0F, scale * -skinState.pivotY(), 0.0F);
/*    */     
/* 37 */     modelStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-skinState.rotationY()));
/*    */     
/* 39 */     modelStack.translate(0.0F, -1.6010001F, 0.0F);
/*    */     
/* 41 */     RenderType skinRenderType = skinState.playerModel().renderType(skinState.texture());
/* 42 */     skinState.playerModel().renderToBuffer(modelStack, this.bufferSource.getBuffer(skinRenderType), 15728880, OverlayTexture.NO_OVERLAY);
/* 43 */     this.bufferSource.endBatch();
/*    */     
/* 45 */     viewStack.popMatrix();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 50 */     return "player skin";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/GuiSkinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */