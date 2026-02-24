/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LockIconButton extends Button {
/*    */   private boolean locked;
/*    */   
/*    */   public LockIconButton(int x, int y, Button.OnPress onPress) {
/* 14 */     super(x, y, 20, 20, (Component)Component.translatable("narrator.button.difficulty_lock"), onPress, DEFAULT_NARRATION);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MutableComponent createNarrationMessage() {
/* 19 */     return CommonComponents.joinForNarration(new Component[] { (Component)super.createNarrationMessage(), isLocked() ? (Component)Component.translatable("narrator.button.difficulty_lock.locked") : (Component)Component.translatable("narrator.button.difficulty_lock.unlocked") });
/*    */   }
/*    */   
/*    */   public boolean isLocked() {
/* 23 */     return this.locked;
/*    */   }
/*    */   
/*    */   public void setLocked(boolean locked) {
/* 27 */     this.locked = locked;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*    */     Icon icon;
/* 33 */     if (!this.active) {
/* 34 */       icon = this.locked ? Icon.LOCKED_DISABLED : Icon.UNLOCKED_DISABLED;
/* 35 */     } else if (isHoveredOrFocused()) {
/* 36 */       icon = this.locked ? Icon.LOCKED_HOVER : Icon.UNLOCKED_HOVER;
/*    */     } else {
/* 38 */       icon = this.locked ? Icon.LOCKED : Icon.UNLOCKED;
/*    */     } 
/*    */     
/* 41 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon.sprite, getX(), getY(), this.width, this.height);
/*    */   }
/*    */   
/*    */   private enum Icon {
/* 45 */     LOCKED(Identifier.withDefaultNamespace("widget/locked_button")),
/* 46 */     LOCKED_HOVER(Identifier.withDefaultNamespace("widget/locked_button_highlighted")),
/* 47 */     LOCKED_DISABLED(Identifier.withDefaultNamespace("widget/locked_button_disabled")),
/* 48 */     UNLOCKED(Identifier.withDefaultNamespace("widget/unlocked_button")),
/* 49 */     UNLOCKED_HOVER(Identifier.withDefaultNamespace("widget/unlocked_button_highlighted")),
/* 50 */     UNLOCKED_DISABLED(Identifier.withDefaultNamespace("widget/unlocked_button_disabled"));
/*    */     
/*    */     private final Identifier sprite;
/*    */ 
/*    */     
/*    */     Icon(Identifier sprite) {
/* 56 */       this.sprite = sprite;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/LockIconButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */