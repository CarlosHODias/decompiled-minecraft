/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import org.joml.Vector2i;
/*    */ import org.joml.Vector2ic;
/*    */ 
/*    */ public class BelowOrAboveWidgetTooltipPositioner implements ClientTooltipPositioner {
/*    */   private final ScreenRectangle screenRectangle;
/*    */   
/*    */   public BelowOrAboveWidgetTooltipPositioner(ScreenRectangle screenRectangle) {
/* 11 */     this.screenRectangle = screenRectangle;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Vector2ic positionTooltip(int screenWidth, int screenHeight, int x, int y, int tooltipWidth, int tooltipHeight) {
/* 17 */     Vector2i result = new Vector2i();
/* 18 */     result.x = this.screenRectangle.left() + 3;
/* 19 */     result.y = this.screenRectangle.bottom() + 3 + 1;
/*    */ 
/*    */     
/* 22 */     if (result.y + tooltipHeight + 3 > screenHeight) {
/* 23 */       result.y = this.screenRectangle.top() - tooltipHeight - 3 - 1;
/*    */     }
/*    */ 
/*    */     
/* 27 */     if (result.x + tooltipWidth > screenWidth) {
/* 28 */       result.x = Math.max(this.screenRectangle.right() - tooltipWidth - 3, 4);
/*    */     }
/* 30 */     return (Vector2ic)result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/tooltip/BelowOrAboveWidgetTooltipPositioner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */