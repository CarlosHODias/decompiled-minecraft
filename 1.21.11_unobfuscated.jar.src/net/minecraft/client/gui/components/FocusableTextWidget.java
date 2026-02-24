/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class FocusableTextWidget extends MultiLineTextWidget {
/*     */   public static final int DEFAULT_PADDING = 4;
/*     */   private final int padding;
/*     */   private final int maxWidth;
/*     */   private final boolean alwaysShowBorder;
/*     */   private final BackgroundFill backgroundFill;
/*     */   
/*     */   private FocusableTextWidget(Component message, Font font, int padding, int maxWidth, BackgroundFill backgroundFill, boolean alwaysShowBorder) {
/*  21 */     super(message, font);
/*  22 */     this.active = true;
/*  23 */     this.padding = padding;
/*  24 */     this.maxWidth = maxWidth;
/*  25 */     this.alwaysShowBorder = alwaysShowBorder;
/*  26 */     this.backgroundFill = backgroundFill;
/*  27 */     updateWidth();
/*  28 */     updateHeight();
/*  29 */     setCentered(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {
/*  34 */     output.add(NarratedElementType.TITLE, getMessage());
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  39 */     int borderColor = (this.alwaysShowBorder && !isFocused()) ? ARGB.color(this.alpha, -6250336) : ARGB.white(this.alpha);
/*  40 */     switch (this.backgroundFill.ordinal()) { case 0:
/*  41 */         graphics.fill(getX() + 1, getY(), getRight(), getBottom(), ARGB.black(this.alpha)); break;
/*     */       case 1:
/*  43 */         if (isFocused()) {
/*  44 */           graphics.fill(getX() + 1, getY(), getRight(), getBottom(), ARGB.black(this.alpha));
/*     */         }
/*     */         break; }
/*     */ 
/*     */     
/*  49 */     if (isFocused() || this.alwaysShowBorder) {
/*  50 */       graphics.renderOutline(getX(), getY(), getWidth(), getHeight(), borderColor);
/*     */     }
/*  52 */     super.renderWidget(graphics, mouseX, mouseY, a);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getTextX() {
/*  57 */     return getX() + this.padding;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getTextY() {
/*  62 */     return super.getTextY() + this.padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiLineTextWidget setMaxWidth(int maxWidth) {
/*  67 */     return super.setMaxWidth(maxWidth - this.padding * 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  72 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  77 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getPadding() {
/*  81 */     return this.padding;
/*     */   }
/*     */   
/*     */   public void updateWidth() {
/*  85 */     if (this.maxWidth != -1) {
/*  86 */       setWidth(this.maxWidth);
/*  87 */       setMaxWidth(this.maxWidth);
/*     */     } else {
/*  89 */       setWidth(getFont().width((FormattedText)getMessage()) + this.padding * 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateHeight() {
/*  94 */     Objects.requireNonNull(getFont()); int textHeight = 9 * getFont().split((FormattedText)getMessage(), super.getWidth()).size();
/*  95 */     setHeight(textHeight + this.padding * 2);
/*     */   }
/*     */   
/*     */   public void setMessage(Component message) {
/*     */     int width;
/* 100 */     this.message = message;
/*     */     
/* 102 */     if (this.maxWidth != -1) {
/* 103 */       width = this.maxWidth;
/*     */     } else {
/* 105 */       width = getFont().width((FormattedText)message) + this.padding * 2;
/*     */     } 
/* 107 */     setWidth(width);
/* 108 */     updateHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {}
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final Component message;
/*     */     private final Font font;
/*     */     private final int padding;
/* 119 */     private int maxWidth = -1;
/*     */     private boolean alwaysShowBorder = true;
/* 121 */     private FocusableTextWidget.BackgroundFill backgroundFill = FocusableTextWidget.BackgroundFill.ALWAYS;
/*     */     
/*     */     private Builder(Component message, Font font) {
/* 124 */       this(message, font, 4);
/*     */     }
/*     */     
/*     */     private Builder(Component message, Font font, int padding) {
/* 128 */       this.message = message;
/* 129 */       this.font = font;
/* 130 */       this.padding = padding;
/*     */     }
/*     */     
/*     */     public Builder maxWidth(int maxWidth) {
/* 134 */       this.maxWidth = maxWidth;
/* 135 */       return this;
/*     */     }
/*     */     
/*     */     public Builder textWidth(int textWidth) {
/* 139 */       this.maxWidth = textWidth + this.padding * 2;
/* 140 */       return this;
/*     */     }
/*     */     
/*     */     public Builder alwaysShowBorder(boolean alwaysShowBorder) {
/* 144 */       this.alwaysShowBorder = alwaysShowBorder;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder backgroundFill(FocusableTextWidget.BackgroundFill backgroundFill) {
/* 149 */       this.backgroundFill = backgroundFill;
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public FocusableTextWidget build() {
/* 154 */       return new FocusableTextWidget(this.message, this.font, this.padding, this.maxWidth, this.backgroundFill, this.alwaysShowBorder);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Font font) {
/* 159 */     return new Builder(message, font);
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Font font, int padding) {
/* 163 */     return new Builder(message, font, padding);
/*     */   }
/*     */   
/*     */   public enum BackgroundFill {
/* 167 */     ALWAYS,
/* 168 */     ON_FOCUS,
/* 169 */     NEVER;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/FocusableTextWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */