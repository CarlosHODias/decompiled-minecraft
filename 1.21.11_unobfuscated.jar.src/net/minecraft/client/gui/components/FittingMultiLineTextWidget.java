/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class FittingMultiLineTextWidget extends AbstractTextAreaWidget {
/*    */   private final Font font;
/*    */   
/*    */   public FittingMultiLineTextWidget(int x, int y, int width, int height, Component message, Font font) {
/* 14 */     super(x, y, width, height, message);
/* 15 */     this.font = font;
/* 16 */     this.multilineWidget = new MultiLineTextWidget(message, font).setMaxWidth(getWidth() - totalInnerPadding());
/*    */   }
/*    */   private final MultiLineTextWidget multilineWidget;
/*    */   
/*    */   public void setWidth(int width) {
/* 21 */     super.setWidth(width);
/* 22 */     this.multilineWidget.setMaxWidth(getWidth() - totalInnerPadding());
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getInnerHeight() {
/* 27 */     return this.multilineWidget.getHeight();
/*    */   }
/*    */   
/*    */   public void minimizeHeight() {
/* 31 */     if (!showingScrollBar()) {
/* 32 */       setHeight(getInnerHeight() + totalInnerPadding());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected double scrollRate() {
/* 38 */     Objects.requireNonNull(this.font); return 9.0D;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBackground(GuiGraphics graphics) {
/* 43 */     super.renderBackground(graphics);
/*    */   }
/*    */   
/*    */   public boolean showingScrollBar() {
/* 47 */     return scrollbarVisible();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 52 */     graphics.pose().pushMatrix();
/* 53 */     graphics.pose().translate(getInnerLeft(), getInnerTop());
/* 54 */     this.multilineWidget.render(graphics, mouseX, mouseY, a);
/* 55 */     graphics.pose().popMatrix();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 60 */     output.add(NarratedElementType.TITLE, getMessage());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMessage(Component message) {
/* 65 */     super.setMessage(message);
/* 66 */     this.multilineWidget.setMessage(message);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/FittingMultiLineTextWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */