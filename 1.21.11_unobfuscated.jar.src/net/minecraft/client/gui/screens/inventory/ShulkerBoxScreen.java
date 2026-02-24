/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.ShulkerBoxMenu;
/*    */ 
/*    */ public class ShulkerBoxScreen extends AbstractContainerScreen<ShulkerBoxMenu> {
/* 11 */   private static final Identifier CONTAINER_TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/shulker_box.png");
/*    */   
/*    */   public ShulkerBoxScreen(ShulkerBoxMenu menu, Inventory inventory, Component title) {
/* 14 */     super(menu, inventory, title);
/*    */     
/* 16 */     this.imageHeight++;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 21 */     super.render(graphics, mouseX, mouseY, a);
/* 22 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 27 */     int xo = (this.width - this.imageWidth) / 2;
/* 28 */     int yo = (this.height - this.imageHeight) / 2;
/* 29 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/ShulkerBoxScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */