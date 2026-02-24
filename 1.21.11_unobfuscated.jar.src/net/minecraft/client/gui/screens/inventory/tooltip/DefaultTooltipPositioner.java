/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import org.joml.Vector2i;
/*    */ import org.joml.Vector2ic;
/*    */ 
/*    */ public class DefaultTooltipPositioner implements ClientTooltipPositioner {
/*  7 */   public static final ClientTooltipPositioner INSTANCE = new DefaultTooltipPositioner();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vector2ic positionTooltip(int screenWidth, int screenHeight, int x, int y, int tooltipWidth, int tooltipHeight) {
/* 17 */     Vector2i result = new Vector2i(x, y).add(12, -12);
/* 18 */     positionTooltip(screenWidth, screenHeight, result, tooltipWidth, tooltipHeight);
/* 19 */     return (Vector2ic)result;
/*    */   }
/*    */ 
/*    */   
/*    */   private void positionTooltip(int screenWidth, int screenHeight, Vector2i result, int tooltipWidth, int tooltipHeight) {
/* 24 */     if (result.x + tooltipWidth > screenWidth) {
/* 25 */       result.x = Math.max(result.x - 24 - tooltipWidth, 4);
/*    */     }
/*    */ 
/*    */     
/* 29 */     int paddedHeight = tooltipHeight + 3;
/* 30 */     if (result.y + paddedHeight > screenHeight)
/* 31 */       result.y = screenHeight - paddedHeight; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/tooltip/DefaultTooltipPositioner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */