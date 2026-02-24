/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ 
/*    */ public class ClientTextTooltip
/*    */   implements ClientTooltipComponent
/*    */ {
/*    */   private final FormattedCharSequence text;
/*    */   
/*    */   public ClientTextTooltip(FormattedCharSequence text) {
/* 14 */     this.text = text;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth(Font font) {
/* 19 */     return font.width(this.text);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight(Font font) {
/* 24 */     return 10;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderText(GuiGraphics guiGraphics, Font font, int x, int y) {
/* 29 */     guiGraphics.drawString(font, this.text, x, y, -1, true);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/tooltip/ClientTextTooltip.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */