/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ImageButton extends Button {
/*    */   protected final WidgetSprites sprites;
/*    */   
/*    */   public ImageButton(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress) {
/* 13 */     this(x, y, width, height, sprites, onPress, CommonComponents.EMPTY);
/*    */   }
/*    */   
/*    */   public ImageButton(int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
/* 17 */     this(0, 0, width, height, sprites, onPress, message);
/*    */   }
/*    */   
/*    */   public ImageButton(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
/* 21 */     super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
/* 22 */     this.sprites = sprites;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 27 */     Identifier sprite = this.sprites.get(isActive(), isHoveredOrFocused());
/* 28 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), this.width, this.height);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ImageButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */