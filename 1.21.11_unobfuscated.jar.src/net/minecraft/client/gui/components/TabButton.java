/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.tabs.Tab;
/*    */ import net.minecraft.client.gui.components.tabs.TabManager;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class TabButton extends AbstractWidget.WithInactiveMessage {
/* 19 */   private static final WidgetSprites SPRITES = new WidgetSprites(
/* 20 */       Identifier.withDefaultNamespace("widget/tab_selected"), 
/* 21 */       Identifier.withDefaultNamespace("widget/tab"), 
/* 22 */       Identifier.withDefaultNamespace("widget/tab_selected_highlighted"), 
/* 23 */       Identifier.withDefaultNamespace("widget/tab_highlighted"));
/*    */   
/*    */   private static final int SELECTED_OFFSET = 3;
/*    */   
/*    */   private static final int TEXT_MARGIN = 1;
/*    */   
/*    */   private static final int UNDERLINE_HEIGHT = 1;
/*    */   
/*    */   private static final int UNDERLINE_MARGIN_X = 4;
/*    */   private static final int UNDERLINE_MARGIN_BOTTOM = 2;
/*    */   private final TabManager tabManager;
/*    */   private final Tab tab;
/*    */   
/*    */   public TabButton(TabManager tabManager, Tab tab, int width, int height) {
/* 37 */     super(0, 0, width, height, tab.getTabTitle());
/* 38 */     this.tabManager = tabManager;
/* 39 */     this.tab = tab;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 44 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(isSelected(), isHoveredOrFocused()), getX(), getY(), this.width, this.height);
/*    */     
/* 46 */     Font font = (Minecraft.getInstance()).font;
/* 47 */     int underlineColor = this.active ? -1 : -6250336;
/*    */     
/* 49 */     if (isSelected()) {
/* 50 */       renderMenuBackground(graphics, getX() + 2, getY() + 2, getRight() - 2, getBottom());
/* 51 */       renderFocusUnderline(graphics, font, underlineColor);
/*    */     } 
/* 53 */     renderLabel(graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
/*    */     
/* 55 */     handleCursor(graphics);
/*    */   }
/*    */   
/*    */   protected void renderMenuBackground(GuiGraphics graphics, int x0, int y0, int x1, int y1) {
/* 59 */     Screen.renderMenuBackgroundTexture(graphics, Screen.MENU_BACKGROUND, x0, y0, 0.0F, 0.0F, x1 - x0, y1 - y0);
/*    */   }
/*    */   
/*    */   private void renderLabel(ActiveTextCollector output) {
/* 63 */     int left = getX() + 1;
/* 64 */     int top = getY() + (isSelected() ? 0 : 3);
/* 65 */     int right = getX() + getWidth() - 1;
/* 66 */     int bottom = getY() + getHeight();
/* 67 */     output.acceptScrollingWithDefaultCenter(getMessage(), left, right, top, bottom);
/*    */   }
/*    */   
/*    */   private void renderFocusUnderline(GuiGraphics graphics, Font font, int color) {
/* 71 */     int width = Math.min(font.width((FormattedText)getMessage()), getWidth() - 4);
/* 72 */     int left = getX() + (getWidth() - width) / 2;
/* 73 */     int top = getY() + getHeight() - 2;
/* 74 */     graphics.fill(left, top, left + width, top + 1, color);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 79 */     output.add(NarratedElementType.TITLE, (Component)Component.translatable("gui.narrate.tab", new Object[] { this.tab.getTabTitle() }));
/* 80 */     output.add(NarratedElementType.HINT, this.tab.getTabExtraNarration());
/*    */   }
/*    */ 
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */ 
/*    */   
/*    */   public Tab tab() {
/* 88 */     return this.tab;
/*    */   }
/*    */   
/*    */   public boolean isSelected() {
/* 92 */     return (this.tabManager.getCurrentTab() == this.tab);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/TabButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */