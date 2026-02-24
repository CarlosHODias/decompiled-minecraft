/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.Slot;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CyclingSlotBackground
/*    */ {
/*    */   private static final int ICON_CHANGE_TICK_RATE = 30;
/*    */   private static final int ICON_SIZE = 16;
/*    */   private static final int ICON_TRANSITION_TICK_DURATION = 4;
/*    */   private final int slotIndex;
/* 19 */   private List<Identifier> icons = List.of();
/*    */   private int tick;
/*    */   private int iconIndex;
/*    */   
/*    */   public CyclingSlotBackground(int slotIndex) {
/* 24 */     this.slotIndex = slotIndex;
/*    */   }
/*    */   
/*    */   public void tick(List<Identifier> newIcons) {
/* 28 */     if (!this.icons.equals(newIcons)) {
/* 29 */       this.icons = newIcons;
/* 30 */       this.iconIndex = 0;
/*    */     } 
/*    */     
/* 33 */     if (!this.icons.isEmpty() && ++this.tick % 30 == 0) {
/* 34 */       this.iconIndex = (this.iconIndex + 1) % this.icons.size();
/*    */     }
/*    */   }
/*    */   
/*    */   public void render(AbstractContainerMenu menu, GuiGraphics graphics, float a, int left, int top) {
/* 39 */     Slot slot = menu.getSlot(this.slotIndex);
/* 40 */     if (this.icons.isEmpty() || slot.hasItem()) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     boolean shouldTransition = (this.icons.size() > 1 && this.tick >= 30);
/* 45 */     float alphaProgress = shouldTransition ? getIconTransitionTransparency(a) : 1.0F;
/* 46 */     if (alphaProgress < 1.0F) {
/* 47 */       int previousIconIndex = Math.floorMod(this.iconIndex - 1, this.icons.size());
/* 48 */       renderIcon(slot, this.icons.get(previousIconIndex), 1.0F - alphaProgress, graphics, left, top);
/*    */     } 
/* 50 */     renderIcon(slot, this.icons.get(this.iconIndex), alphaProgress, graphics, left, top);
/*    */   }
/*    */   
/*    */   private void renderIcon(Slot slot, Identifier iconIdentifier, float alphaProgress, GuiGraphics graphics, int left, int top) {
/* 54 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, iconIdentifier, left + slot.x, top + slot.y, 16, 16, ARGB.white(alphaProgress));
/*    */   }
/*    */   
/*    */   private float getIconTransitionTransparency(float a) {
/* 58 */     float elapsedTransitionTime = (this.tick % 30) + a;
/* 59 */     return Math.min(elapsedTransitionTime, 4.0F) / 4.0F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/CyclingSlotBackground.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */