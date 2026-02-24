/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ public class ScissorState {
/*    */   private boolean enabled;
/*    */   private int x;
/*    */   private int y;
/*    */   private int width;
/*    */   private int height;
/*    */   
/*    */   public void enable(int x, int y, int width, int height) {
/* 11 */     this.enabled = true;
/* 12 */     this.x = x;
/* 13 */     this.y = y;
/* 14 */     this.width = width;
/* 15 */     this.height = height;
/*    */   }
/*    */   
/*    */   public void disable() {
/* 19 */     this.enabled = false;
/*    */   }
/*    */   
/*    */   public boolean enabled() {
/* 23 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public int x() {
/* 27 */     return this.x;
/*    */   }
/*    */   
/*    */   public int y() {
/* 31 */     return this.y;
/*    */   }
/*    */   
/*    */   public int width() {
/* 35 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 39 */     return this.height;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/ScissorState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */