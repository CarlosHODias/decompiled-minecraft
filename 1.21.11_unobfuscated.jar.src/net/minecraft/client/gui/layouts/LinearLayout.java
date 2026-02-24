/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinearLayout
/*     */   implements Layout
/*     */ {
/*     */   private final GridLayout wrapped;
/*     */   private final Orientation orientation;
/*  13 */   private int nextChildIndex = 0;
/*     */   
/*     */   private LinearLayout(Orientation orientation) {
/*  16 */     this(0, 0, orientation);
/*     */   }
/*     */   
/*     */   public LinearLayout(int x, int y, Orientation orientation) {
/*  20 */     this.wrapped = new GridLayout(x, y);
/*  21 */     this.orientation = orientation;
/*     */   }
/*     */   
/*     */   public LinearLayout spacing(int spacing) {
/*  25 */     this.orientation.setSpacing(this.wrapped, spacing);
/*  26 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettings newCellSettings() {
/*  30 */     return this.wrapped.newCellSettings();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultCellSetting() {
/*  34 */     return this.wrapped.defaultCellSetting();
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, LayoutSettings cellSettings) {
/*  38 */     return this.orientation.addChild(this.wrapped, child, this.nextChildIndex++, cellSettings);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child) {
/*  42 */     return addChild(child, newCellSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/*  46 */     return this.orientation.addChild(this.wrapped, child, this.nextChildIndex++, (LayoutSettings)Util.make(newCellSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  51 */     this.wrapped.visitChildren(layoutElementVisitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/*  56 */     this.wrapped.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  61 */     return this.wrapped.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  66 */     return this.wrapped.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(int x) {
/*  71 */     this.wrapped.setX(x);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(int y) {
/*  76 */     this.wrapped.setY(y);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/*  81 */     return this.wrapped.getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/*  86 */     return this.wrapped.getY();
/*     */   }
/*     */   
/*     */   public static LinearLayout vertical() {
/*  90 */     return new LinearLayout(Orientation.VERTICAL);
/*     */   }
/*     */   
/*     */   public static LinearLayout horizontal() {
/*  94 */     return new LinearLayout(Orientation.HORIZONTAL);
/*     */   }
/*     */   
/*     */   public enum Orientation {
/*  98 */     HORIZONTAL, VERTICAL;
/*     */     
/*     */     private void setSpacing(GridLayout gridLayout, int spacing) {
/* 101 */       switch (ordinal()) { case 0:
/* 102 */           gridLayout.columnSpacing(spacing); break;
/* 103 */         case 1: gridLayout.rowSpacing(spacing);
/*     */           break; }
/*     */     
/*     */     }
/*     */     public <T extends LayoutElement> T addChild(GridLayout gridLayout, T child, int index, LayoutSettings cellSettings) {
/* 108 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 110 */         (T)gridLayout.<LayoutElement>addChild((LayoutElement)child, index, 0, cellSettings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/LinearLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */