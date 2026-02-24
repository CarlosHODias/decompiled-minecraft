/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class AbstractScrollArea extends AbstractWidget {
/*     */   public static final int SCROLLBAR_WIDTH = 6;
/*     */   private double scrollAmount;
/*  14 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("widget/scroller");
/*  15 */   private static final Identifier SCROLLER_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("widget/scroller_background");
/*     */   private boolean scrolling;
/*     */   
/*     */   public AbstractScrollArea(int x, int y, int width, int height, Component message) {
/*  19 */     super(x, y, width, height, message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
/*  24 */     if (!this.visible) {
/*  25 */       return false;
/*     */     }
/*  27 */     setScrollAmount(scrollAmount() - scrollY * scrollRate());
/*  28 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/*  33 */     if (this.scrolling) {
/*  34 */       if (event.y() < getY()) {
/*  35 */         setScrollAmount(0.0D);
/*  36 */       } else if (event.y() > getBottom()) {
/*  37 */         setScrollAmount(maxScrollAmount());
/*     */       } else {
/*  39 */         double max = Math.max(1, maxScrollAmount());
/*  40 */         int barHeight = scrollerHeight();
/*  41 */         double yDragScale = Math.max(1.0D, max / (this.height - barHeight));
/*  42 */         setScrollAmount(scrollAmount() + dy * yDragScale);
/*     */       } 
/*  44 */       return true;
/*     */     } 
/*     */     
/*  47 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRelease(MouseButtonEvent event) {
/*  52 */     this.scrolling = false;
/*     */   }
/*     */   
/*     */   public double scrollAmount() {
/*  56 */     return this.scrollAmount;
/*     */   }
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/*  60 */     this.scrollAmount = Mth.clamp(scrollAmount, 0.0D, maxScrollAmount());
/*     */   }
/*     */   
/*     */   public boolean updateScrolling(MouseButtonEvent event) {
/*  64 */     this.scrolling = (scrollbarVisible() && isValidClickButton(event.buttonInfo()) && isOverScrollbar(event.x(), event.y()));
/*  65 */     return this.scrolling;
/*     */   }
/*     */   
/*     */   protected boolean isOverScrollbar(double x, double y) {
/*  69 */     return (x >= scrollBarX() && x <= (scrollBarX() + 6) && y >= getY() && y < getBottom());
/*     */   }
/*     */   
/*     */   public void refreshScrollAmount() {
/*  73 */     setScrollAmount(this.scrollAmount);
/*     */   }
/*     */   
/*     */   public int maxScrollAmount() {
/*  77 */     return Math.max(0, contentHeight() - this.height);
/*     */   }
/*     */   
/*     */   protected boolean scrollbarVisible() {
/*  81 */     return (maxScrollAmount() > 0);
/*     */   }
/*     */   
/*     */   protected int scrollerHeight() {
/*  85 */     return Mth.clamp((int)((this.height * this.height) / contentHeight()), 32, this.height - 8);
/*     */   }
/*     */   
/*     */   protected int scrollBarX() {
/*  89 */     return getRight() - 6;
/*     */   }
/*     */   
/*     */   protected int scrollBarY() {
/*  93 */     return Math.max(getY(), (int)this.scrollAmount * (this.height - scrollerHeight()) / maxScrollAmount() + getY());
/*     */   }
/*     */   
/*     */   protected void renderScrollbar(GuiGraphics graphics, int mouseX, int mouseY) {
/*  97 */     if (scrollbarVisible()) {
/*  98 */       int scrollbarX = scrollBarX();
/*  99 */       int scrollerHeight = scrollerHeight();
/* 100 */       int scrollerY = scrollBarY();
/*     */       
/* 102 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_BACKGROUND_SPRITE, scrollbarX, getY(), 6, getHeight());
/* 103 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_SPRITE, scrollbarX, scrollerY, 6, scrollerHeight);
/*     */       
/* 105 */       if (isOverScrollbar(mouseX, mouseY))
/* 106 */         graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract int contentHeight();
/*     */   
/*     */   protected abstract double scrollRate();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractScrollArea.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */