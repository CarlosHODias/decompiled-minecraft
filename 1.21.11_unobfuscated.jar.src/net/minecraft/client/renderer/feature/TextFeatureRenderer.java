/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ 
/*    */ public class TextFeatureRenderer
/*    */ {
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource) {
/* 12 */     Font font = (Minecraft.getInstance()).font;
/* 13 */     for (SubmitNodeStorage.TextSubmit textSubmit : (Iterable<SubmitNodeStorage.TextSubmit>)nodeCollection.getTextSubmits()) {
/* 14 */       if (textSubmit.outlineColor() == 0) {
/* 15 */         font.drawInBatch(textSubmit.string(), textSubmit.x(), textSubmit.y(), textSubmit.color(), textSubmit.dropShadow(), textSubmit.pose(), (MultiBufferSource)bufferSource, textSubmit.displayMode(), textSubmit.backgroundColor(), textSubmit.lightCoords()); continue;
/*    */       } 
/* 17 */       font.drawInBatch8xOutline(textSubmit.string(), textSubmit.x(), textSubmit.y(), textSubmit.color(), textSubmit.outlineColor(), textSubmit.pose(), (MultiBufferSource)bufferSource, textSubmit.lightCoords());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/TextFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */