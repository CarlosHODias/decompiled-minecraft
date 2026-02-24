/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public interface TextRenderable {
/*    */   void render(Matrix4f paramMatrix4f, VertexConsumer paramVertexConsumer, int paramInt, boolean paramBoolean);
/*    */   
/*    */   RenderType renderType(Font.DisplayMode paramDisplayMode);
/*    */   
/*    */   GpuTextureView textureView();
/*    */   
/*    */   RenderPipeline guiPipeline();
/*    */   
/*    */   float left();
/*    */   
/*    */   float top();
/*    */   
/*    */   float right();
/*    */   
/*    */   float bottom();
/*    */   
/*    */   public static interface Styled
/*    */     extends TextRenderable, ActiveArea {
/*    */     default float activeLeft() {
/* 30 */       return left();
/*    */     }
/*    */ 
/*    */     
/*    */     default float activeTop() {
/* 35 */       return top();
/*    */     }
/*    */ 
/*    */     
/*    */     default float activeRight() {
/* 40 */       return right();
/*    */     }
/*    */ 
/*    */     
/*    */     default float activeBottom() {
/* 45 */       return bottom();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/TextRenderable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */