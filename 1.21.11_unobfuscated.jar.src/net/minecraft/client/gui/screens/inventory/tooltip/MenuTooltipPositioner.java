/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Vector2i;
/*    */ import org.joml.Vector2ic;
/*    */ 
/*    */ public class MenuTooltipPositioner implements ClientTooltipPositioner {
/*    */   private static final int MARGIN = 5;
/*    */   private static final int MOUSE_OFFSET_X = 12;
/*    */   public static final int MAX_OVERLAP_WITH_WIDGET = 3;
/*    */   public static final int MAX_DISTANCE_TO_WIDGET = 5;
/*    */   private final ScreenRectangle screenRectangle;
/*    */   
/*    */   public MenuTooltipPositioner(ScreenRectangle screenRectangle) {
/* 16 */     this.screenRectangle = screenRectangle;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vector2ic positionTooltip(int screenWidth, int screenHeight, int x, int y, int tooltipWidth, int tooltipHeight) {
/* 21 */     Vector2i result = new Vector2i(x + 12, y);
/*    */     
/* 23 */     if (result.x + tooltipWidth > screenWidth - 5) {
/* 24 */       result.x = Math.max(x - 12 - tooltipWidth, 9);
/*    */     }
/*    */     
/* 27 */     result.y += 3;
/*    */     
/* 29 */     int paddedHeight = tooltipHeight + 3 + 3;
/*    */     
/* 31 */     int lowestPossibleY = this.screenRectangle.bottom() + 3 + getOffset(0, 0, this.screenRectangle.height());
/* 32 */     int maxY = screenHeight - 5;
/* 33 */     if (lowestPossibleY + paddedHeight <= maxY) {
/*    */       
/* 35 */       result.y += getOffset(result.y, this.screenRectangle.top(), this.screenRectangle.height());
/*    */     } else {
/*    */       
/* 38 */       result.y -= paddedHeight + getOffset(result.y, this.screenRectangle.bottom(), this.screenRectangle.height());
/*    */     } 
/* 40 */     return (Vector2ic)result;
/*    */   }
/*    */   
/*    */   private static int getOffset(int mouseY, int widgetY, int widgetHeight) {
/* 44 */     int distance = Math.min(Math.abs(mouseY - widgetY), widgetHeight);
/* 45 */     return Math.round(Mth.lerp(distance / widgetHeight, (widgetHeight - 3), 5.0F));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/tooltip/MenuTooltipPositioner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */