/*    */ package net.minecraft.client.gui.navigation;
/*    */ 
/*    */ public enum ScreenAxis {
/*  4 */   HORIZONTAL,
/*  5 */   VERTICAL;
/*    */ 
/*    */   
/*    */   public ScreenAxis orthogonal() {
/*  9 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*    */       
/* 11 */       HORIZONTAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenDirection getPositive() {
/* 16 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*    */       
/* 18 */       ScreenDirection.DOWN;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenDirection getNegative() {
/* 23 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*    */       
/* 25 */       ScreenDirection.UP;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenDirection getDirection(boolean positive) {
/* 30 */     return positive ? getPositive() : getNegative();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/navigation/ScreenAxis.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */