/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemDisplayWidget
/*    */   extends AbstractWidget {
/*    */   private final Minecraft minecraft;
/*    */   private final int offsetX;
/*    */   private final int offsetY;
/*    */   private final ItemStack itemStack;
/*    */   private final boolean decorations;
/*    */   private final boolean tooltip;
/*    */   
/*    */   public ItemDisplayWidget(Minecraft minecraft, int offsetX, int offsetY, int width, int height, Component message, ItemStack itemStack, boolean decorations, boolean tooltip) {
/* 20 */     super(0, 0, width, height, message);
/* 21 */     this.minecraft = minecraft;
/* 22 */     this.offsetX = offsetX;
/* 23 */     this.offsetY = offsetY;
/* 24 */     this.itemStack = itemStack;
/* 25 */     this.decorations = decorations;
/* 26 */     this.tooltip = tooltip;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 31 */     graphics.renderItem(this.itemStack, getX() + this.offsetX, getY() + this.offsetY, 0);
/* 32 */     if (this.decorations) {
/* 33 */       graphics.renderItemDecorations(this.minecraft.font, this.itemStack, getX() + this.offsetX, getY() + this.offsetY, null);
/*    */     }
/* 35 */     if (isFocused()) {
/* 36 */       graphics.renderOutline(getX(), getY(), getWidth(), getHeight(), -1);
/*    */     }
/* 38 */     if (this.tooltip && isHoveredOrFocused()) {
/* 39 */       renderTooltip(graphics, mouseX, mouseY);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void renderTooltip(GuiGraphics graphics, int x, int y) {
/* 44 */     graphics.setTooltipForNextFrame(this.minecraft.font, this.itemStack, x, y);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 49 */     output.add(NarratedElementType.TITLE, (Component)Component.translatable("narration.item", new Object[] { this.itemStack.getHoverName() }));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ItemDisplayWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */