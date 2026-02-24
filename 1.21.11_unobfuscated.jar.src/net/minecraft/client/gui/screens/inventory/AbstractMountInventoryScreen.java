/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*    */ 
/*    */ public abstract class AbstractMountInventoryScreen<T extends AbstractMountInventoryMenu>
/*    */   extends AbstractContainerScreen<T> {
/*    */   protected final int inventoryColumns;
/*    */   protected float xMouse;
/*    */   protected float yMouse;
/*    */   protected LivingEntity mount;
/*    */   
/*    */   public AbstractMountInventoryScreen(T menu, Inventory inventory, Component title, int inventoryColumns, LivingEntity mount) {
/* 19 */     super(menu, inventory, title);
/* 20 */     this.inventoryColumns = inventoryColumns;
/* 21 */     this.mount = mount;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 26 */     int xo = (this.width - this.imageWidth) / 2;
/* 27 */     int yo = (this.height - this.imageHeight) / 2;
/*    */     
/* 29 */     graphics.blit(RenderPipelines.GUI_TEXTURED, getBackgroundTextureLocation(), xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */     
/* 31 */     if (this.inventoryColumns > 0 && getChestSlotsSpriteLocation() != null) {
/* 32 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getChestSlotsSpriteLocation(), 90, 54, 0, 0, xo + 79, yo + 17, this.inventoryColumns * 18, 54);
/*    */     }
/*    */     
/* 35 */     if (shouldRenderSaddleSlot()) {
/* 36 */       drawSlot(graphics, xo + 7, yo + 35 - 18);
/*    */     }
/* 38 */     if (shouldRenderArmorSlot()) {
/* 39 */       drawSlot(graphics, xo + 7, yo + 35);
/*    */     }
/*    */     
/* 42 */     InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, xo + 26, yo + 18, xo + 78, yo + 70, 17, 0.25F, this.xMouse, this.yMouse, this.mount);
/*    */   }
/*    */   
/*    */   protected void drawSlot(GuiGraphics graphics, int x, int y) {
/* 46 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getSlotSpriteLocation(), x, y, 18, 18);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 51 */     this.xMouse = mouseX;
/* 52 */     this.yMouse = mouseY;
/* 53 */     super.render(graphics, mouseX, mouseY, a);
/* 54 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */   
/*    */   protected abstract Identifier getBackgroundTextureLocation();
/*    */   
/*    */   protected abstract Identifier getSlotSpriteLocation();
/*    */   
/*    */   protected abstract Identifier getChestSlotsSpriteLocation();
/*    */   
/*    */   protected abstract boolean shouldRenderSaddleSlot();
/*    */   
/*    */   protected abstract boolean shouldRenderArmorSlot();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/AbstractMountInventoryScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */