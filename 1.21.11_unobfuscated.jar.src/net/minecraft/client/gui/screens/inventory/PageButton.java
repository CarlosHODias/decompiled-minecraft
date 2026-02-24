/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public class PageButton extends Button {
/* 13 */   private static final Identifier PAGE_FORWARD_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/page_forward_highlighted");
/* 14 */   private static final Identifier PAGE_FORWARD_SPRITE = Identifier.withDefaultNamespace("widget/page_forward");
/* 15 */   private static final Identifier PAGE_BACKWARD_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/page_backward_highlighted");
/* 16 */   private static final Identifier PAGE_BACKWARD_SPRITE = Identifier.withDefaultNamespace("widget/page_backward");
/* 17 */   private static final Component PAGE_BUTTON_NEXT = (Component)Component.translatable("book.page_button.next");
/* 18 */   private static final Component PAGE_BUTTON_PREVIOUS = (Component)Component.translatable("book.page_button.previous");
/*    */   private final boolean isForward;
/*    */   private final boolean playTurnSound;
/*    */   
/*    */   public PageButton(int x, int y, boolean isForward, Button.OnPress onPress, boolean playTurnSound) {
/* 23 */     super(x, y, 23, 13, isForward ? PAGE_BUTTON_NEXT : PAGE_BUTTON_PREVIOUS, onPress, DEFAULT_NARRATION);
/* 24 */     this.isForward = isForward;
/* 25 */     this.playTurnSound = playTurnSound;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*    */     Identifier sprite;
/* 31 */     if (this.isForward) {
/* 32 */       sprite = isHoveredOrFocused() ? PAGE_FORWARD_HIGHLIGHTED_SPRITE : PAGE_FORWARD_SPRITE;
/*    */     } else {
/* 34 */       sprite = isHoveredOrFocused() ? PAGE_BACKWARD_HIGHLIGHTED_SPRITE : PAGE_BACKWARD_SPRITE;
/*    */     } 
/* 36 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), 23, 13);
/*    */   }
/*    */ 
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {
/* 41 */     if (this.playTurnSound) {
/* 42 */       soundManager.play((SoundInstance)SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldTakeFocusAfterInteraction() {
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/PageButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */