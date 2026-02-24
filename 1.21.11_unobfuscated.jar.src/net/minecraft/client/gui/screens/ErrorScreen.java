/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ErrorScreen
/*    */   extends Screen {
/*    */   private final Component message;
/*    */   
/*    */   public ErrorScreen(Component title, Component message) {
/* 13 */     super(title);
/* 14 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 19 */     super.init();
/*    */     
/* 21 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.minecraft.setScreen(null)).bounds(this.width / 2 - 100, 140, 200, 20).build());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 26 */     super.render(graphics, mouseX, mouseY, a);
/*    */     
/* 28 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 90, -1);
/* 29 */     graphics.drawCenteredString(this.font, this.message, this.width / 2, 110, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 34 */     graphics.fillGradient(0, 0, this.width, this.height, -12574688, -11530224);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ErrorScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */