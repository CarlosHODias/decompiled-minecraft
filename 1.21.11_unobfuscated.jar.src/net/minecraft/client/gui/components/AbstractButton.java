/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.input.InputWithModifiers;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractButton
/*    */   extends AbstractWidget.WithInactiveMessage
/*    */ {
/*    */   protected static final int TEXT_MARGIN = 2;
/* 20 */   private static final WidgetSprites SPRITES = new WidgetSprites(
/* 21 */       Identifier.withDefaultNamespace("widget/button"), 
/* 22 */       Identifier.withDefaultNamespace("widget/button_disabled"), 
/* 23 */       Identifier.withDefaultNamespace("widget/button_highlighted"));
/*    */   
/*    */   private Supplier<Boolean> overrideRenderHighlightedSprite;
/*    */ 
/*    */   
/*    */   public AbstractButton(int x, int y, int width, int height, Component message) {
/* 29 */     super(x, y, width, height, message);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract void onPress(InputWithModifiers paramInputWithModifiers);
/*    */   
/*    */   protected final void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 36 */     renderContents(graphics, mouseX, mouseY, a);
/*    */     
/* 38 */     handleCursor(graphics);
/*    */   }
/*    */   
/*    */   protected abstract void renderContents(GuiGraphics paramGuiGraphics, int paramInt1, int paramInt2, float paramFloat);
/*    */   
/*    */   protected void renderDefaultLabel(ActiveTextCollector output) {
/* 44 */     renderScrollingStringOverContents(output, getMessage(), 2);
/*    */   }
/*    */   
/*    */   protected final void renderDefaultSprite(GuiGraphics graphics) {
/* 48 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.active, (this.overrideRenderHighlightedSprite != null) ? (Boolean)this.overrideRenderHighlightedSprite.get() : isHoveredOrFocused()), getX(), getY(), getWidth(), getHeight(), ARGB.white(this.alpha));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/* 53 */     onPress((InputWithModifiers)event);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 58 */     if (!isActive()) {
/* 59 */       return false;
/*    */     }
/* 61 */     if (event.isSelection()) {
/* 62 */       playDownSound(Minecraft.getInstance().getSoundManager());
/* 63 */       onPress((InputWithModifiers)event);
/* 64 */       return true;
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public void setOverrideRenderHighlightedSprite(Supplier<Boolean> overrideRenderHighlightedSprite) {
/* 70 */     this.overrideRenderHighlightedSprite = overrideRenderHighlightedSprite;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */