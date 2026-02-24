/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class AbstractLayout implements Layout {
/*    */   private int x;
/*    */   private int y;
/*    */   protected int width;
/*    */   protected int height;
/*    */   
/*    */   public AbstractLayout(int x, int y, int width, int height) {
/* 12 */     this.x = x;
/* 13 */     this.y = y;
/* 14 */     this.width = width;
/* 15 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setX(int x) {
/* 20 */     visitChildren(child -> {
/*    */           int newChildX = x.getX() + x - getX();
/*    */           x.setX(newChildX);
/*    */         });
/* 24 */     this.x = x;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setY(int y) {
/* 29 */     visitChildren(child -> {
/*    */           int newChildY = y.getY() + y - getY();
/*    */           y.setY(newChildY);
/*    */         });
/* 33 */     this.y = y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getX() {
/* 38 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getY() {
/* 43 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 48 */     return this.width;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 53 */     return this.height;
/*    */   }
/*    */   
/*    */   protected static abstract class AbstractChildWrapper {
/*    */     public final LayoutElement child;
/*    */     public final LayoutSettings.LayoutSettingsImpl layoutSettings;
/*    */     
/*    */     protected AbstractChildWrapper(LayoutElement child, LayoutSettings layoutSettings) {
/* 61 */       this.child = child;
/* 62 */       this.layoutSettings = layoutSettings.getExposed();
/*    */     }
/*    */     
/*    */     public int getHeight() {
/* 66 */       return this.child.getHeight() + this.layoutSettings.paddingTop + this.layoutSettings.paddingBottom;
/*    */     }
/*    */     
/*    */     public int getWidth() {
/* 70 */       return this.child.getWidth() + this.layoutSettings.paddingLeft + this.layoutSettings.paddingRight;
/*    */     }
/*    */     
/*    */     public void setX(int x, int availableSpace) {
/* 74 */       float leastOffset = this.layoutSettings.paddingLeft;
/* 75 */       float mostOffset = (availableSpace - this.child.getWidth() - this.layoutSettings.paddingRight);
/* 76 */       int offset = (int)Mth.lerp(this.layoutSettings.xAlignment, leastOffset, mostOffset);
/* 77 */       this.child.setX(offset + x);
/*    */     }
/*    */     
/*    */     public void setY(int y, int availableSpace) {
/* 81 */       float leastOffset = this.layoutSettings.paddingTop;
/* 82 */       float mostOffset = (availableSpace - this.child.getHeight() - this.layoutSettings.paddingBottom);
/* 83 */       int offset = Math.round(Mth.lerp(this.layoutSettings.yAlignment, leastOffset, mostOffset));
/* 84 */       this.child.setY(offset + y);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/AbstractLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */