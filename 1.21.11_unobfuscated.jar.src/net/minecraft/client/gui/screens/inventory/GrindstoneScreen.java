/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.GrindstoneMenu;
/*    */ 
/*    */ public class GrindstoneScreen extends AbstractContainerScreen<GrindstoneMenu> {
/* 11 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/grindstone/error");
/* 12 */   private static final Identifier GRINDSTONE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/grindstone.png");
/*    */   
/*    */   public GrindstoneScreen(GrindstoneMenu menu, Inventory inventory, Component title) {
/* 15 */     super(menu, inventory, title);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 20 */     super.render(graphics, mouseX, mouseY, a);
/* 21 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 26 */     int xo = (this.width - this.imageWidth) / 2;
/* 27 */     int yo = (this.height - this.imageHeight) / 2;
/* 28 */     graphics.blit(RenderPipelines.GUI_TEXTURED, GRINDSTONE_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */     
/* 30 */     if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem())
/* 31 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 92, yo + 31, 28, 21); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/GrindstoneScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */