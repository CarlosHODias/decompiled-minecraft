/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerListener;
/*    */ import net.minecraft.world.inventory.ItemCombinerMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public abstract class ItemCombinerScreen<T extends ItemCombinerMenu> extends AbstractContainerScreen<T> implements ContainerListener {
/*    */   private final Identifier menuResource;
/*    */   
/*    */   public ItemCombinerScreen(T menu, Inventory inventory, Component title, Identifier menuResource) {
/* 17 */     super(menu, inventory, title);
/* 18 */     this.menuResource = menuResource;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subInit() {}
/*    */ 
/*    */   
/*    */   protected void init() {
/* 26 */     super.init();
/* 27 */     subInit();
/* 28 */     ((ItemCombinerMenu)this.menu).addSlotListener(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed() {
/* 33 */     super.removed();
/*    */     
/* 35 */     ((ItemCombinerMenu)this.menu).removeSlotListener(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 40 */     super.render(graphics, mouseX, mouseY, a);
/* 41 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 46 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.menuResource, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 47 */     renderErrorIcon(graphics, this.leftPos, this.topPos);
/*    */   }
/*    */   
/*    */   protected abstract void renderErrorIcon(GuiGraphics paramGuiGraphics, int paramInt1, int paramInt2);
/*    */   
/*    */   public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*    */   
/*    */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/ItemCombinerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */