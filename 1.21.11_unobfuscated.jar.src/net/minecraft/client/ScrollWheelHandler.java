/*    */ package net.minecraft.client;
/*    */ 
/*    */ import org.joml.Vector2i;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScrollWheelHandler
/*    */ {
/*    */   private double accumulatedScrollX;
/*    */   private double accumulatedScrollY;
/*    */   
/*    */   public Vector2i onMouseScroll(double scaledXScrollOffset, double scaledYScrollOffset) {
/* 13 */     if (this.accumulatedScrollX != 0.0D && Math.signum(scaledXScrollOffset) != Math.signum(this.accumulatedScrollX)) {
/* 14 */       this.accumulatedScrollX = 0.0D;
/*    */     }
/* 16 */     if (this.accumulatedScrollY != 0.0D && Math.signum(scaledYScrollOffset) != Math.signum(this.accumulatedScrollY)) {
/* 17 */       this.accumulatedScrollY = 0.0D;
/*    */     }
/* 19 */     this.accumulatedScrollX += scaledXScrollOffset;
/* 20 */     this.accumulatedScrollY += scaledYScrollOffset;
/*    */     
/* 22 */     int wheelX = (int)this.accumulatedScrollX;
/* 23 */     int wheelY = (int)this.accumulatedScrollY;
/* 24 */     if (wheelX == 0 && wheelY == 0) {
/* 25 */       return new Vector2i(0, 0);
/*    */     }
/*    */     
/* 28 */     this.accumulatedScrollX -= wheelX;
/* 29 */     this.accumulatedScrollY -= wheelY;
/*    */     
/* 31 */     return new Vector2i(wheelX, wheelY);
/*    */   }
/*    */   
/*    */   public static int getNextScrollWheelSelection(double wheel, int currentSelected, int limit) {
/* 35 */     int step = (int)Math.signum(wheel);
/* 36 */     currentSelected -= step;
/* 37 */     currentSelected = Math.max(-1, currentSelected);
/*    */     
/* 39 */     while (currentSelected < 0) {
/* 40 */       currentSelected += limit;
/*    */     }
/* 42 */     while (currentSelected >= limit) {
/* 43 */       currentSelected -= limit;
/*    */     }
/* 45 */     return currentSelected;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/ScrollWheelHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */