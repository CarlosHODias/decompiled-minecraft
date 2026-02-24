/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.render.state.pip.GuiProfilerChartRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.profiling.ResultField;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class GuiProfilerChartRenderer
/*    */   extends PictureInPictureRenderer<GuiProfilerChartRenderState> {
/*    */   public GuiProfilerChartRenderer(MultiBufferSource.BufferSource bufferSource) {
/* 18 */     super(bufferSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<GuiProfilerChartRenderState> getRenderStateClass() {
/* 23 */     return GuiProfilerChartRenderState.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderToTexture(GuiProfilerChartRenderState chartState, PoseStack poseStack) {
/* 28 */     double totalPercentage = 0.0D;
/* 29 */     poseStack.translate(0.0F, -5.0F, 0.0F);
/* 30 */     Matrix4f pose = poseStack.last().pose();
/* 31 */     for (ResultField result : (Iterable<ResultField>)chartState.chartData()) {
/* 32 */       int steps = Mth.floor(result.percentage / 4.0D) + 1;
/*    */       
/* 34 */       VertexConsumer buffer = this.bufferSource.getBuffer(RenderTypes.debugTriangleFan());
/* 35 */       int color = ARGB.opaque(result.getColor());
/* 36 */       int shadeColor = ARGB.multiply(color, -8355712);
/* 37 */       buffer.addVertex((Matrix4fc)pose, 0.0F, 0.0F, 0.0F).setColor(color);
/* 38 */       for (int j = steps; j >= 0; j--) {
/* 39 */         float dir = (float)((totalPercentage + result.percentage * j / steps) * 6.2831854820251465D / 100.0D);
/* 40 */         float xx = Mth.sin(dir) * 105.0F;
/* 41 */         float yy = Mth.cos(dir) * 105.0F * 0.5F;
/* 42 */         buffer.addVertex((Matrix4fc)pose, xx, yy, 0.0F).setColor(color);
/*    */       } 
/*    */       
/* 45 */       buffer = this.bufferSource.getBuffer(RenderTypes.debugQuads());
/* 46 */       for (int i = steps; i > 0; i--) {
/* 47 */         float dir0 = (float)((totalPercentage + result.percentage * i / steps) * 6.2831854820251465D / 100.0D);
/* 48 */         float x0 = Mth.sin(dir0) * 105.0F;
/* 49 */         float y0 = Mth.cos(dir0) * 105.0F * 0.5F;
/* 50 */         float dir1 = (float)((totalPercentage + result.percentage * (i - 1) / steps) * 6.2831854820251465D / 100.0D);
/* 51 */         float x1 = Mth.sin(dir1) * 105.0F;
/* 52 */         float y1 = Mth.cos(dir1) * 105.0F * 0.5F;
/* 53 */         if ((y0 + y1) / 2.0F >= 0.0F) {
/*    */ 
/*    */           
/* 56 */           buffer.addVertex((Matrix4fc)pose, x0, y0, 0.0F).setColor(shadeColor);
/* 57 */           buffer.addVertex((Matrix4fc)pose, x0, y0 + 10.0F, 0.0F).setColor(shadeColor);
/* 58 */           buffer.addVertex((Matrix4fc)pose, x1, y1 + 10.0F, 0.0F).setColor(shadeColor);
/* 59 */           buffer.addVertex((Matrix4fc)pose, x1, y1, 0.0F).setColor(shadeColor);
/*    */         } 
/*    */       } 
/* 62 */       totalPercentage += result.percentage;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 68 */     return height / 2.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 73 */     return "profiler chart";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/GuiProfilerChartRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */