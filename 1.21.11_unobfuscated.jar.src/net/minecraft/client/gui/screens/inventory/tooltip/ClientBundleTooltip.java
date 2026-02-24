/*     */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public class ClientBundleTooltip
/*     */   implements ClientTooltipComponent
/*     */ {
/*  20 */   private static final Identifier PROGRESSBAR_BORDER_SPRITE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_border");
/*  21 */   private static final Identifier PROGRESSBAR_FILL_SPRITE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
/*  22 */   private static final Identifier PROGRESSBAR_FULL_SPRITE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_full");
/*  23 */   private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_back");
/*  24 */   private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_front");
/*  25 */   private static final Identifier SLOT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
/*     */   
/*     */   private static final int SLOT_MARGIN = 4;
/*     */   private static final int SLOT_SIZE = 24;
/*     */   private static final int GRID_WIDTH = 96;
/*     */   private static final int PROGRESSBAR_HEIGHT = 13;
/*     */   private static final int PROGRESSBAR_WIDTH = 96;
/*     */   private static final int PROGRESSBAR_BORDER = 1;
/*     */   private static final int PROGRESSBAR_FILL_MAX = 94;
/*     */   private static final int PROGRESSBAR_MARGIN_Y = 4;
/*  35 */   private static final Component BUNDLE_FULL_TEXT = (Component)Component.translatable("item.minecraft.bundle.full");
/*  36 */   private static final Component BUNDLE_EMPTY_TEXT = (Component)Component.translatable("item.minecraft.bundle.empty");
/*  37 */   private static final Component BUNDLE_EMPTY_DESCRIPTION = (Component)Component.translatable("item.minecraft.bundle.empty.description");
/*     */   
/*     */   private final BundleContents contents;
/*     */   
/*     */   public ClientBundleTooltip(BundleContents contents) {
/*  42 */     this.contents = contents;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Font font) {
/*  47 */     return this.contents.isEmpty() ? getEmptyBundleBackgroundHeight(font) : backgroundHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth(Font font) {
/*  52 */     return 96;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showTooltipWithItemInHand() {
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   private static int getEmptyBundleBackgroundHeight(Font font) {
/*  61 */     return getEmptyBundleDescriptionTextHeight(font) + 13 + 8;
/*     */   }
/*     */   
/*     */   private int backgroundHeight() {
/*  65 */     return itemGridHeight() + 13 + 8;
/*     */   }
/*     */   
/*     */   private int itemGridHeight() {
/*  69 */     return gridSizeY() * 24;
/*     */   }
/*     */   
/*     */   private int getContentXOffset(int tooltipWidth) {
/*  73 */     return (tooltipWidth - 96) / 2;
/*     */   }
/*     */   
/*     */   private int gridSizeY() {
/*  77 */     return Mth.positiveCeilDiv(slotCount(), 4);
/*     */   }
/*     */   
/*     */   private int slotCount() {
/*  81 */     return Math.min(12, this.contents.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderImage(Font font, int x, int y, int w, int h, GuiGraphics graphics) {
/*  86 */     if (this.contents.isEmpty()) {
/*  87 */       renderEmptyBundleTooltip(font, x, y, w, h, graphics);
/*     */     } else {
/*  89 */       renderBundleWithItemsTooltip(font, x, y, w, h, graphics);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderEmptyBundleTooltip(Font font, int x, int y, int w, int h, GuiGraphics graphics) {
/*  94 */     drawEmptyBundleDescriptionText(x + getContentXOffset(w), y, font, graphics);
/*  95 */     drawProgressbar(x + getContentXOffset(w), y + getEmptyBundleDescriptionTextHeight(font) + 4, font, graphics);
/*     */   }
/*     */   
/*     */   private void renderBundleWithItemsTooltip(Font font, int x, int y, int w, int h, GuiGraphics graphics) {
/*  99 */     boolean isOverflowing = (this.contents.size() > 12);
/* 100 */     List<ItemStack> shownItems = getShownItems(this.contents.getNumberOfItemsToShow());
/* 101 */     int xStartPos = x + getContentXOffset(w) + 96;
/* 102 */     int yStartPos = y + gridSizeY() * 24;
/*     */     
/* 104 */     int slotNumber = 1;
/* 105 */     for (int rowNumber = 1; rowNumber <= gridSizeY(); rowNumber++) {
/* 106 */       for (int columnNumber = 1; columnNumber <= 4; columnNumber++) {
/* 107 */         int drawX = xStartPos - columnNumber * 24;
/* 108 */         int drawY = yStartPos - rowNumber * 24;
/*     */         
/* 110 */         if (shouldRenderSurplusText(isOverflowing, columnNumber, rowNumber)) {
/* 111 */           renderCount(drawX, drawY, getAmountOfHiddenItems(shownItems), font, graphics);
/* 112 */         } else if (shouldRenderItemSlot(shownItems, slotNumber)) {
/* 113 */           renderSlot(slotNumber, drawX, drawY, shownItems, slotNumber, font, graphics);
/* 114 */           slotNumber++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 118 */     drawSelectedItemTooltip(font, graphics, x, y, w);
/* 119 */     drawProgressbar(x + getContentXOffset(w), y + itemGridHeight() + 4, font, graphics);
/*     */   }
/*     */   
/*     */   private List<ItemStack> getShownItems(int amountOfItemsToShow) {
/* 123 */     int lastToDisplay = Math.min(this.contents.size(), amountOfItemsToShow);
/* 124 */     return this.contents.itemCopyStream().toList().subList(0, lastToDisplay);
/*     */   }
/*     */   
/*     */   private static boolean shouldRenderSurplusText(boolean isOverflowing, int column, int row) {
/* 128 */     return (isOverflowing && column * row == 1);
/*     */   }
/*     */   
/*     */   private static boolean shouldRenderItemSlot(List<ItemStack> shownItems, int slotNumber) {
/* 132 */     return (shownItems.size() >= slotNumber);
/*     */   }
/*     */   
/*     */   private int getAmountOfHiddenItems(List<ItemStack> shownItems) {
/* 136 */     return this.contents.itemCopyStream()
/* 137 */       .skip(shownItems.size())
/* 138 */       .mapToInt(ItemStack::getCount)
/* 139 */       .sum();
/*     */   }
/*     */   
/*     */   private void renderSlot(int slotNumber, int drawX, int drawY, List<ItemStack> shownItems, int slotIndex, Font font, GuiGraphics graphics) {
/* 143 */     int itemVisualOrderIndex = shownItems.size() - slotNumber;
/* 144 */     boolean hasHighlight = (itemVisualOrderIndex == this.contents.getSelectedItem());
/*     */     
/* 146 */     ItemStack item = shownItems.get(itemVisualOrderIndex);
/* 147 */     if (hasHighlight) {
/* 148 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, drawX, drawY, 24, 24);
/*     */     } else {
/* 150 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND_SPRITE, drawX, drawY, 24, 24);
/*     */     } 
/* 152 */     graphics.renderItem(item, drawX + 4, drawY + 4, slotIndex);
/* 153 */     graphics.renderItemDecorations(font, item, drawX + 4, drawY + 4);
/*     */     
/* 155 */     if (hasHighlight) {
/* 156 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, drawX, drawY, 24, 24);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void renderCount(int drawX, int drawY, int hiddenItemCount, Font font, GuiGraphics graphics) {
/* 161 */     graphics.drawCenteredString(font, "+" + hiddenItemCount, drawX + 12, drawY + 10, -1);
/*     */   }
/*     */   
/*     */   private void drawSelectedItemTooltip(Font font, GuiGraphics graphics, int x, int y, int w) {
/* 165 */     if (this.contents.hasSelectedItem()) {
/* 166 */       ItemStack itemStack = this.contents.getItemUnsafe(this.contents.getSelectedItem());
/* 167 */       Component selectedItemName = itemStack.getStyledHoverName();
/* 168 */       int textWidth = font.width(selectedItemName.getVisualOrderText());
/* 169 */       int centerTooltip = x + w / 2 - 12;
/* 170 */       ClientTooltipComponent selectedItemNameTooltip = ClientTooltipComponent.create(selectedItemName.getVisualOrderText());
/* 171 */       graphics.renderTooltip(font, List.of(selectedItemNameTooltip), centerTooltip - textWidth / 2, y - 15, DefaultTooltipPositioner.INSTANCE, (Identifier)itemStack.get(DataComponents.TOOLTIP_STYLE));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawProgressbar(int x, int y, Font font, GuiGraphics graphics) {
/* 176 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getProgressBarTexture(), x + 1, y, getProgressBarFill(), 13);
/* 177 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESSBAR_BORDER_SPRITE, x, y, 96, 13);
/* 178 */     Component progressBarFillText = getProgressBarFillText();
/* 179 */     if (progressBarFillText != null) {
/* 180 */       graphics.drawCenteredString(font, progressBarFillText, x + 48, y + 3, -1);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void drawEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics graphics) {
/* 185 */     graphics.drawWordWrap(font, (FormattedText)BUNDLE_EMPTY_DESCRIPTION, x, y, 96, -5592406);
/*     */   }
/*     */   
/*     */   private static int getEmptyBundleDescriptionTextHeight(Font font) {
/* 189 */     Objects.requireNonNull(font); return font.split((FormattedText)BUNDLE_EMPTY_DESCRIPTION, 96).size() * 9;
/*     */   }
/*     */   
/*     */   private int getProgressBarFill() {
/* 193 */     return Mth.clamp(Mth.mulAndTruncate(this.contents.weight(), 94), 0, 94);
/*     */   }
/*     */   
/*     */   private Identifier getProgressBarTexture() {
/* 197 */     return (this.contents.weight().compareTo(Fraction.ONE) >= 0) ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
/*     */   }
/*     */   
/*     */   private Component getProgressBarFillText() {
/* 201 */     if (this.contents.isEmpty())
/* 202 */       return BUNDLE_EMPTY_TEXT; 
/* 203 */     if (this.contents.weight().compareTo(Fraction.ONE) >= 0) {
/* 204 */       return BUNDLE_FULL_TEXT;
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/tooltip/ClientBundleTooltip.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */