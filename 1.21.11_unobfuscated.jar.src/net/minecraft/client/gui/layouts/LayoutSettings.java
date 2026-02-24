/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface LayoutSettings
/*     */ {
/*     */   LayoutSettings padding(int paramInt);
/*     */   
/*     */   LayoutSettings padding(int paramInt1, int paramInt2);
/*     */   
/*     */   LayoutSettings padding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   LayoutSettings paddingLeft(int paramInt);
/*     */   
/*     */   LayoutSettings paddingTop(int paramInt);
/*     */   
/*     */   LayoutSettings paddingRight(int paramInt);
/*     */   
/*     */   LayoutSettings paddingBottom(int paramInt);
/*     */   
/*     */   LayoutSettings paddingHorizontal(int paramInt);
/*     */   
/*     */   LayoutSettings paddingVertical(int paramInt);
/*     */   
/*     */   LayoutSettings align(float paramFloat1, float paramFloat2);
/*     */   
/*     */   LayoutSettings alignHorizontally(float paramFloat);
/*     */   
/*     */   LayoutSettings alignVertically(float paramFloat);
/*     */   
/*     */   default LayoutSettings alignHorizontallyLeft() {
/*  39 */     return alignHorizontally(0.0F);
/*     */   }
/*     */   
/*     */   default LayoutSettings alignHorizontallyCenter() {
/*  43 */     return alignHorizontally(0.5F);
/*     */   }
/*     */   
/*     */   default LayoutSettings alignHorizontallyRight() {
/*  47 */     return alignHorizontally(1.0F);
/*     */   }
/*     */   
/*     */   default LayoutSettings alignVerticallyTop() {
/*  51 */     return alignVertically(0.0F);
/*     */   }
/*     */   
/*     */   default LayoutSettings alignVerticallyMiddle() {
/*  55 */     return alignVertically(0.5F);
/*     */   }
/*     */   
/*     */   default LayoutSettings alignVerticallyBottom() {
/*  59 */     return alignVertically(1.0F);
/*     */   }
/*     */   
/*     */   LayoutSettings copy();
/*     */   
/*     */   LayoutSettingsImpl getExposed();
/*     */   
/*     */   static LayoutSettings defaults() {
/*  67 */     return new LayoutSettingsImpl();
/*     */   }
/*     */   
/*     */   public static class LayoutSettingsImpl
/*     */     implements LayoutSettings {
/*     */     public int paddingLeft;
/*     */     public int paddingTop;
/*     */     public int paddingRight;
/*     */     public int paddingBottom;
/*     */     public float xAlignment;
/*     */     public float yAlignment;
/*     */     
/*     */     public LayoutSettingsImpl() {}
/*     */     
/*     */     public LayoutSettingsImpl(LayoutSettingsImpl copy) {
/*  82 */       this.paddingLeft = copy.paddingLeft;
/*  83 */       this.paddingTop = copy.paddingTop;
/*  84 */       this.paddingRight = copy.paddingRight;
/*  85 */       this.paddingBottom = copy.paddingBottom;
/*  86 */       this.xAlignment = copy.xAlignment;
/*  87 */       this.yAlignment = copy.yAlignment;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl padding(int padding) {
/*  92 */       return padding(padding, padding);
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl padding(int horizontal, int vertical) {
/*  97 */       return paddingHorizontal(horizontal).paddingVertical(vertical);
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl padding(int left, int top, int right, int bottom) {
/* 102 */       return paddingLeft(left)
/* 103 */         .paddingRight(right)
/* 104 */         .paddingTop(top)
/* 105 */         .paddingBottom(bottom);
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl paddingLeft(int padding) {
/* 110 */       this.paddingLeft = padding;
/* 111 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl paddingTop(int padding) {
/* 116 */       this.paddingTop = padding;
/* 117 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl paddingRight(int padding) {
/* 122 */       this.paddingRight = padding;
/* 123 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl paddingBottom(int padding) {
/* 128 */       this.paddingBottom = padding;
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl paddingHorizontal(int padding) {
/* 134 */       return paddingLeft(padding).paddingRight(padding);
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl paddingVertical(int padding) {
/* 139 */       return paddingTop(padding).paddingBottom(padding);
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl align(float xAlignment, float yAlignment) {
/* 144 */       this.xAlignment = xAlignment;
/* 145 */       this.yAlignment = yAlignment;
/* 146 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl alignHorizontally(float xAlignment) {
/* 151 */       this.xAlignment = xAlignment;
/* 152 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl alignVertically(float yAlignment) {
/* 157 */       this.yAlignment = yAlignment;
/* 158 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl copy() {
/* 163 */       return new LayoutSettingsImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public LayoutSettingsImpl getExposed() {
/* 168 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/LayoutSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */