/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.ClickType;
/*     */ import net.minecraft.world.inventory.CrafterMenu;
/*     */ import net.minecraft.world.inventory.CrafterSlot;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {
/*  18 */   private static final Identifier DISABLED_SLOT_LOCATION_SPRITE = Identifier.withDefaultNamespace("container/crafter/disabled_slot");
/*  19 */   private static final Identifier POWERED_REDSTONE_LOCATION_SPRITE = Identifier.withDefaultNamespace("container/crafter/powered_redstone");
/*  20 */   private static final Identifier UNPOWERED_REDSTONE_LOCATION_SPRITE = Identifier.withDefaultNamespace("container/crafter/unpowered_redstone");
/*  21 */   private static final Identifier CONTAINER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/crafter.png");
/*  22 */   private static final Component DISABLED_SLOT_TOOLTIP = (Component)Component.translatable("gui.togglable_slot");
/*     */   
/*     */   private final Player player;
/*     */   
/*     */   public CrafterScreen(CrafterMenu menu, Inventory inventory, Component title) {
/*  27 */     super(menu, inventory, title);
/*  28 */     this.player = inventory.player;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  33 */     super.init();
/*  34 */     this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ClickType clickType) {
/*  39 */     if (slot instanceof CrafterSlot && !slot.hasItem() && !this.player.isSpectator()) {
/*  40 */       ItemStack playerInventoryItem; switch (clickType) {
/*     */         case PICKUP:
/*  42 */           if (this.menu.isSlotDisabled(slotId)) {
/*  43 */             enableSlot(slotId); break;
/*  44 */           }  if (this.menu.getCarried().isEmpty()) {
/*  45 */             disableSlot(slotId);
/*     */           }
/*     */           break;
/*     */         case SWAP:
/*  49 */           playerInventoryItem = this.player.getInventory().getItem(buttonNum);
/*  50 */           if (this.menu.isSlotDisabled(slotId) && !playerInventoryItem.isEmpty()) {
/*  51 */             enableSlot(slotId);
/*     */           }
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  57 */     super.slotClicked(slot, slotId, buttonNum, clickType);
/*     */   }
/*     */   
/*     */   private void enableSlot(int slotId) {
/*  61 */     updateSlotState(slotId, true);
/*     */   }
/*     */   
/*     */   private void disableSlot(int slotId) {
/*  65 */     updateSlotState(slotId, false);
/*     */   }
/*     */   
/*     */   private void updateSlotState(int slotId, boolean enabled) {
/*  69 */     this.menu.setSlotState(slotId, enabled);
/*  70 */     handleSlotStateChanged(slotId, this.menu.containerId, enabled);
/*  71 */     float pitch = enabled ? 1.0F : 0.75F;
/*  72 */     this.player.playSound((net.minecraft.sounds.SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, pitch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderSlot(GuiGraphics graphics, Slot slot, int mouseX, int mouseY) {
/*  77 */     if (slot instanceof CrafterSlot) { CrafterSlot crafterSlot = (CrafterSlot)slot;
/*  78 */       if (this.menu.isSlotDisabled(slot.index)) {
/*  79 */         renderDisabledSlot(graphics, crafterSlot);
/*     */       } else {
/*  81 */         super.renderSlot(graphics, slot, mouseX, mouseY);
/*     */       } 
/*  83 */       int x0 = this.leftPos + crafterSlot.x - 2;
/*  84 */       int y0 = this.topPos + crafterSlot.y - 2;
/*  85 */       if (mouseX > x0 && mouseY > y0 && mouseX < x0 + 19 && mouseY < y0 + 19) {
/*  86 */         graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */       } }
/*     */     else
/*  89 */     { super.renderSlot(graphics, slot, mouseX, mouseY); }
/*     */   
/*     */   }
/*     */   
/*     */   private void renderDisabledSlot(GuiGraphics graphics, CrafterSlot cs) {
/*  94 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISABLED_SLOT_LOCATION_SPRITE, cs.x - 1, cs.y - 1, 18, 18);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  99 */     super.render(graphics, mouseX, mouseY, a);
/* 100 */     renderRedstone(graphics);
/* 101 */     renderTooltip(graphics, mouseX, mouseY);
/*     */     
/* 103 */     if (this.hoveredSlot instanceof CrafterSlot && !this.menu.isSlotDisabled(this.hoveredSlot.index) && 
/* 104 */       this.menu.getCarried().isEmpty() && !this.hoveredSlot.hasItem() && !this.player.isSpectator())
/* 105 */       graphics.setTooltipForNextFrame(this.font, DISABLED_SLOT_TOOLTIP, mouseX, mouseY); 
/*     */   }
/*     */   
/*     */   private void renderRedstone(GuiGraphics graphics) {
/*     */     Identifier redstoneArrowTexture;
/* 110 */     int xo = this.width / 2 + 9;
/* 111 */     int yo = this.height / 2 - 48;
/*     */     
/* 113 */     if (this.menu.isPowered()) {
/* 114 */       redstoneArrowTexture = POWERED_REDSTONE_LOCATION_SPRITE;
/*     */     } else {
/* 116 */       redstoneArrowTexture = UNPOWERED_REDSTONE_LOCATION_SPRITE;
/*     */     } 
/* 118 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, redstoneArrowTexture, xo, yo, 16, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 123 */     int xo = (this.width - this.imageWidth) / 2;
/* 124 */     int yo = (this.height - this.imageHeight) / 2;
/* 125 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/CrafterScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */