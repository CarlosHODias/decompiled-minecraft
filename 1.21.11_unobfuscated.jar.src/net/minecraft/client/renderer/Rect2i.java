/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ public class Rect2i {
/*    */   private int xPos;
/*    */   private int yPos;
/*    */   private int width;
/*    */   private int height;
/*    */   
/*    */   public Rect2i(int x, int y, int width, int height) {
/* 10 */     this.xPos = x;
/* 11 */     this.yPos = y;
/* 12 */     this.width = width;
/* 13 */     this.height = height;
/*    */   }
/*    */   
/*    */   public Rect2i intersect(Rect2i other) {
/* 17 */     int x0 = this.xPos;
/* 18 */     int y0 = this.yPos;
/* 19 */     int x1 = this.xPos + this.width;
/* 20 */     int y1 = this.yPos + this.height;
/*    */     
/* 22 */     int x2 = other.getX();
/* 23 */     int y2 = other.getY();
/* 24 */     int x3 = x2 + other.getWidth();
/* 25 */     int y3 = y2 + other.getHeight();
/*    */     
/* 27 */     this.xPos = Math.max(x0, x2);
/* 28 */     this.yPos = Math.max(y0, y2);
/* 29 */     this.width = Math.max(0, Math.min(x1, x3) - this.xPos);
/* 30 */     this.height = Math.max(0, Math.min(y1, y3) - this.yPos);
/*    */     
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public int getX() {
/* 36 */     return this.xPos;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 40 */     return this.yPos;
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 44 */     this.xPos = x;
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 48 */     this.yPos = y;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 52 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 56 */     return this.height;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 60 */     this.width = width;
/*    */   }
/*    */   
/*    */   public void setHeight(int height) {
/* 64 */     this.height = height;
/*    */   }
/*    */   
/*    */   public void setPosition(int x, int y) {
/* 68 */     this.xPos = x;
/* 69 */     this.yPos = y;
/*    */   }
/*    */   
/*    */   public boolean contains(int x, int y) {
/* 73 */     return (x >= this.xPos && x <= this.xPos + this.width && y >= this.yPos && y <= this.yPos + this.height);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/Rect2i.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */