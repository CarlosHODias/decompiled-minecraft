/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ 
/*    */ public class ContainerScreen extends AbstractContainerScreen<ChestMenu> {
/* 11 */   private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
/*    */   
/*    */   private final int containerRows;
/*    */   
/*    */   public ContainerScreen(ChestMenu menu, Inventory inventory, Component title) {
/* 16 */     super(menu, inventory, title);
/*    */     
/* 18 */     int defaultHeight = 222;
/* 19 */     int noRowHeight = 114;
/* 20 */     this.containerRows = menu.getRowCount();
/*    */     
/* 22 */     this.imageHeight = 114 + this.containerRows * 18;
/* 23 */     this.inventoryLabelY = this.imageHeight - 94;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 28 */     super.render(graphics, mouseX, mouseY, a);
/* 29 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 34 */     int xo = (this.width - this.imageWidth) / 2;
/* 35 */     int yo = (this.height - this.imageHeight) / 2;
/* 36 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo, 0.0F, 0.0F, this.imageWidth, this.containerRows * 18 + 17, 256, 256);
/* 37 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo + this.containerRows * 18 + 17, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/ContainerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */