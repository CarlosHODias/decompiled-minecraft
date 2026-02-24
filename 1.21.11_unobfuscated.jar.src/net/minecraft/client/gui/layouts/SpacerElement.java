/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ 
/*    */ public class SpacerElement
/*    */   implements LayoutElement {
/*    */   private int x;
/*    */   private int y;
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/*    */   public SpacerElement(int width, int height) {
/* 14 */     this(0, 0, width, height);
/*    */   }
/*    */   
/*    */   public SpacerElement(int x, int y, int width, int height) {
/* 18 */     this.x = x;
/* 19 */     this.y = y;
/* 20 */     this.width = width;
/* 21 */     this.height = height;
/*    */   }
/*    */   
/*    */   public static SpacerElement width(int width) {
/* 25 */     return new SpacerElement(width, 0);
/*    */   }
/*    */   
/*    */   public static SpacerElement height(int height) {
/* 29 */     return new SpacerElement(0, height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setX(int x) {
/* 34 */     this.x = x;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setY(int y) {
/* 39 */     this.y = y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getX() {
/* 44 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getY() {
/* 49 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 54 */     return this.width;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 59 */     return this.height;
/*    */   }
/*    */   
/*    */   public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/SpacerElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */