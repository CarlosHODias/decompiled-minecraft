/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
/*    */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
/*    */ import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WidgetTooltipHolder
/*    */ {
/*    */   private Tooltip tooltip;
/* 18 */   private Duration delay = Duration.ZERO;
/*    */   private long displayStartTime;
/*    */   private boolean wasDisplayed;
/*    */   
/*    */   public void setDelay(Duration delay) {
/* 23 */     this.delay = delay;
/*    */   }
/*    */   
/*    */   public void set(Tooltip tooltip) {
/* 27 */     this.tooltip = tooltip;
/*    */   }
/*    */   
/*    */   public Tooltip get() {
/* 31 */     return this.tooltip;
/*    */   }
/*    */   
/*    */   public void refreshTooltipForNextRenderPass(GuiGraphics graphics, int mouseX, int mouseY, boolean isHovered, boolean isFocused, ScreenRectangle screenRectangle) {
/* 35 */     if (this.tooltip == null) {
/* 36 */       this.wasDisplayed = false;
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     Minecraft minecraft = Minecraft.getInstance();
/* 41 */     boolean shouldDisplay = (isHovered || (isFocused && minecraft.getLastInputType().isKeyboard()));
/* 42 */     if (shouldDisplay != this.wasDisplayed) {
/* 43 */       if (shouldDisplay) {
/* 44 */         this.displayStartTime = Util.getMillis();
/*    */       }
/* 46 */       this.wasDisplayed = shouldDisplay;
/*    */     } 
/*    */     
/* 49 */     if (shouldDisplay && Util.getMillis() - this.displayStartTime > this.delay.toMillis()) {
/* 50 */       graphics.setTooltipForNextFrame(minecraft.font, this.tooltip.toCharSequence(minecraft), createTooltipPositioner(screenRectangle, isHovered, isFocused), mouseX, mouseY, isFocused);
/*    */     }
/*    */   }
/*    */   
/*    */   private ClientTooltipPositioner createTooltipPositioner(ScreenRectangle screenRectangle, boolean isHovered, boolean isFocused) {
/* 55 */     if (!isHovered && isFocused && Minecraft.getInstance().getLastInputType().isKeyboard()) {
/* 56 */       return (ClientTooltipPositioner)new BelowOrAboveWidgetTooltipPositioner(screenRectangle);
/*    */     }
/* 58 */     return (ClientTooltipPositioner)new MenuTooltipPositioner(screenRectangle);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateNarration(NarrationElementOutput output) {
/* 63 */     if (this.tooltip != null)
/* 64 */       this.tooltip.updateNarration(output); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/WidgetTooltipHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */