/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public abstract class AbstractTextAreaWidget extends AbstractScrollArea {
/*  12 */   private static final WidgetSprites BACKGROUND_SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("widget/text_field"), Identifier.withDefaultNamespace("widget/text_field_highlighted"));
/*     */   private static final int INNER_PADDING = 4;
/*     */   public static final int DEFAULT_TOTAL_PADDING = 8;
/*     */   private boolean showBackground = true;
/*     */   private boolean showDecorations = true;
/*     */   
/*     */   public AbstractTextAreaWidget(int x, int y, int width, int height, Component narration) {
/*  19 */     super(x, y, width, height, narration);
/*     */   }
/*     */   
/*     */   public AbstractTextAreaWidget(int x, int y, int width, int height, Component narration, boolean showBackground, boolean showDecorations) {
/*  23 */     this(x, y, width, height, narration);
/*  24 */     this.showBackground = showBackground;
/*  25 */     this.showDecorations = showDecorations;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  30 */     boolean scrolling = updateScrolling(event);
/*  31 */     return (super.mouseClicked(event, doubleClick) || scrolling);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  36 */     boolean isUp = event.isUp();
/*  37 */     boolean isDown = event.isDown();
/*  38 */     if (isUp || isDown) {
/*  39 */       double previousScrollAmount = scrollAmount();
/*  40 */       setScrollAmount(scrollAmount() + (isUp ? -1 : true) * scrollRate());
/*  41 */       if (previousScrollAmount != scrollAmount()) {
/*  42 */         return true;
/*     */       }
/*     */     } 
/*  45 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  50 */     if (!this.visible) {
/*     */       return;
/*     */     }
/*     */     
/*  54 */     if (this.showBackground) {
/*  55 */       renderBackground(graphics);
/*     */     }
/*     */     
/*  58 */     graphics.enableScissor(getX() + 1, getY() + 1, getX() + this.width - 1, getY() + this.height - 1);
/*     */     
/*  60 */     graphics.pose().pushMatrix();
/*  61 */     graphics.pose().translate(0.0F, (float)-scrollAmount());
/*  62 */     renderContents(graphics, mouseX, mouseY, a);
/*  63 */     graphics.pose().popMatrix();
/*     */     
/*  65 */     graphics.disableScissor();
/*     */     
/*  67 */     renderScrollbar(graphics, mouseX, mouseY);
/*  68 */     if (this.showDecorations) {
/*  69 */       renderDecorations(graphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderDecorations(GuiGraphics graphics) {}
/*     */   
/*     */   protected int innerPadding() {
/*  77 */     return 4;
/*     */   }
/*     */   
/*     */   protected int totalInnerPadding() {
/*  81 */     return innerPadding() * 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/*  87 */     return (this.active && this.visible && mouseX >= getX() && mouseY >= getY() && mouseX < (getRight() + 6) && mouseY < getBottom());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int scrollBarX() {
/*  92 */     return getRight();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int contentHeight() {
/*  97 */     return getInnerHeight() + totalInnerPadding();
/*     */   }
/*     */   
/*     */   protected void renderBackground(GuiGraphics graphics) {
/* 101 */     renderBorder(graphics, getX(), getY(), getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   protected void renderBorder(GuiGraphics graphics, int x, int y, int width, int height) {
/* 105 */     Identifier sprite = BACKGROUND_SPRITES.get(isActive(), isFocused());
/* 106 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, height);
/*     */   }
/*     */   
/*     */   protected boolean withinContentAreaTopBottom(int top, int bottom) {
/* 110 */     return (bottom - scrollAmount() >= getY() && top - scrollAmount() <= (getY() + this.height));
/*     */   }
/*     */   
/*     */   protected abstract int getInnerHeight();
/*     */   
/*     */   protected abstract void renderContents(GuiGraphics paramGuiGraphics, int paramInt1, int paramInt2, float paramFloat);
/*     */   
/*     */   protected int getInnerLeft() {
/* 118 */     return getX() + innerPadding();
/*     */   }
/*     */   
/*     */   protected int getInnerTop() {
/* 122 */     return getY() + innerPadding();
/*     */   }
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractTextAreaWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */