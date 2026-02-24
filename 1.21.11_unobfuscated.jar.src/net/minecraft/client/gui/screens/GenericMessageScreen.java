/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ 
/*    */ public class GenericMessageScreen extends Screen {
/*    */   private FocusableTextWidget textWidget;
/*    */   
/*    */   public GenericMessageScreen(Component title) {
/* 13 */     super(title);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 18 */     this.textWidget = addRenderableWidget(FocusableTextWidget.builder(this.title, this.font, 12).textWidth(this.font.width((FormattedText)this.title)).build());
/* 19 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 24 */     if (this.textWidget != null) {
/* 25 */       Objects.requireNonNull(this.font); this.textWidget.setPosition(this.width / 2 - this.textWidget.getWidth() / 2, this.height / 2 - 9 / 2);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldNarrateNavigation() {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 42 */     renderPanorama(graphics, a);
/* 43 */     renderBlurredBackground(graphics);
/* 44 */     renderMenuBackground(graphics);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/GenericMessageScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */