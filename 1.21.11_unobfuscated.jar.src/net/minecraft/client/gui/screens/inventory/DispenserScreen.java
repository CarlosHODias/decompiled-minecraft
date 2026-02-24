/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.DispenserMenu;
/*    */ 
/*    */ public class DispenserScreen extends AbstractContainerScreen<DispenserMenu> {
/* 11 */   private static final Identifier CONTAINER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
/*    */   
/*    */   public DispenserScreen(DispenserMenu menu, Inventory inventory, Component title) {
/* 14 */     super(menu, inventory, title);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 19 */     super.init();
/* 20 */     this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 25 */     super.render(graphics, mouseX, mouseY, a);
/* 26 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 31 */     int xo = (this.width - this.imageWidth) / 2;
/* 32 */     int yo = (this.height - this.imageHeight) / 2;
/* 33 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/DispenserScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */