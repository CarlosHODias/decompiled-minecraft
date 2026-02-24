/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.MerchantMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ public class MerchantScreen extends AbstractContainerScreen<MerchantMenu> {
/*  22 */   private static final Identifier OUT_OF_STOCK_SPRITE = Identifier.withDefaultNamespace("container/villager/out_of_stock");
/*  23 */   private static final Identifier EXPERIENCE_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/villager/experience_bar_background");
/*  24 */   private static final Identifier EXPERIENCE_BAR_CURRENT_SPRITE = Identifier.withDefaultNamespace("container/villager/experience_bar_current");
/*  25 */   private static final Identifier EXPERIENCE_BAR_RESULT_SPRITE = Identifier.withDefaultNamespace("container/villager/experience_bar_result");
/*  26 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/villager/scroller");
/*  27 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/villager/scroller_disabled");
/*  28 */   private static final Identifier TRADE_ARROW_OUT_OF_STOCK_SPRITE = Identifier.withDefaultNamespace("container/villager/trade_arrow_out_of_stock");
/*  29 */   private static final Identifier TRADE_ARROW_SPRITE = Identifier.withDefaultNamespace("container/villager/trade_arrow");
/*  30 */   private static final Identifier DISCOUNT_STRIKETHRUOGH_SPRITE = Identifier.withDefaultNamespace("container/villager/discount_strikethrough");
/*  31 */   private static final Identifier VILLAGER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/villager.png");
/*     */   
/*     */   private static final int TEXTURE_WIDTH = 512;
/*     */   
/*     */   private static final int TEXTURE_HEIGHT = 256;
/*     */   
/*     */   private static final int MERCHANT_MENU_PART_X = 99;
/*     */   
/*     */   private static final int PROGRESS_BAR_X = 136;
/*     */   
/*     */   private static final int PROGRESS_BAR_Y = 16;
/*     */   private static final int SELL_ITEM_1_X = 5;
/*     */   private static final int SELL_ITEM_2_X = 35;
/*     */   private static final int BUY_ITEM_X = 68;
/*     */   private static final int LABEL_Y = 6;
/*     */   private static final int NUMBER_OF_OFFER_BUTTONS = 7;
/*     */   private static final int TRADE_BUTTON_X = 5;
/*     */   private static final int TRADE_BUTTON_HEIGHT = 20;
/*     */   private static final int TRADE_BUTTON_WIDTH = 88;
/*     */   private static final int SCROLLER_HEIGHT = 27;
/*     */   private static final int SCROLLER_WIDTH = 6;
/*     */   private static final int SCROLL_BAR_HEIGHT = 139;
/*     */   private static final int SCROLL_BAR_TOP_POS_Y = 18;
/*     */   private static final int SCROLL_BAR_START_X = 94;
/*  55 */   private static final Component TRADES_LABEL = (Component)Component.translatable("merchant.trades");
/*  56 */   private static final Component DEPRECATED_TOOLTIP = (Component)Component.translatable("merchant.deprecated");
/*     */   
/*     */   private int shopItem;
/*  59 */   private final TradeOfferButton[] tradeOfferButtons = new TradeOfferButton[7];
/*     */   private int scrollOff;
/*     */   private boolean isDragging;
/*     */   
/*     */   public MerchantScreen(MerchantMenu menu, Inventory inventory, Component title) {
/*  64 */     super(menu, inventory, title);
/*  65 */     this.imageWidth = 276;
/*  66 */     this.inventoryLabelX = 107;
/*     */   }
/*     */   
/*     */   private void postButtonClick() {
/*  70 */     this.menu.setSelectionHint(this.shopItem);
/*  71 */     this.menu.tryMoveItems(this.shopItem);
/*  72 */     this.minecraft.getConnection().send((net.minecraft.network.protocol.Packet)new ServerboundSelectTradePacket(this.shopItem));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  77 */     super.init();
/*     */     
/*  79 */     int xo = (this.width - this.imageWidth) / 2;
/*  80 */     int yo = (this.height - this.imageHeight) / 2;
/*     */     
/*  82 */     int buttonY = yo + 16 + 2;
/*  83 */     for (int i = 0; i < 7; i++) {
/*  84 */       this.tradeOfferButtons[i] = (TradeOfferButton)addRenderableWidget((net.minecraft.client.gui.components.events.GuiEventListener)new TradeOfferButton(xo + 5, buttonY, i, button -> {
/*     */               if (button instanceof TradeOfferButton) {
/*     */                 this.shopItem = ((TradeOfferButton)button).getIndex() + this.scrollOff;
/*     */                 
/*     */                 postButtonClick();
/*     */               } 
/*     */             }));
/*  91 */       buttonY += 20;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
/*  97 */     int traderLevel = this.menu.getTraderLevel();
/*  98 */     if (traderLevel > 0 && traderLevel <= 5 && this.menu.showProgressBar()) {
/*  99 */       MutableComponent mutableComponent = Component.translatable("merchant.title", new Object[] { this.title, Component.translatable("merchant.level." + traderLevel) });
/* 100 */       int totalWidth = this.font.width((FormattedText)mutableComponent);
/* 101 */       int startX = 49 + this.imageWidth / 2 - totalWidth / 2;
/* 102 */       graphics.drawString(this.font, (Component)mutableComponent, startX, 6, -12566464, false);
/*     */     } else {
/* 104 */       graphics.drawString(this.font, this.title, 49 + this.imageWidth / 2 - this.font.width((FormattedText)this.title) / 2, 6, -12566464, false);
/*     */     } 
/* 106 */     graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
/*     */     
/* 108 */     int textWidth = this.font.width((FormattedText)TRADES_LABEL);
/* 109 */     graphics.drawString(this.font, TRADES_LABEL, 5 - textWidth / 2 + 48, 6, -12566464, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 114 */     int xo = (this.width - this.imageWidth) / 2;
/* 115 */     int yo = (this.height - this.imageHeight) / 2;
/*     */     
/* 117 */     graphics.blit(RenderPipelines.GUI_TEXTURED, VILLAGER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
/*     */     
/* 119 */     MerchantOffers offers = this.menu.getOffers();
/* 120 */     if (!offers.isEmpty()) {
/* 121 */       int itemIndex = this.shopItem;
/* 122 */       if (itemIndex < 0 || itemIndex >= offers.size()) {
/*     */         return;
/*     */       }
/*     */       
/* 126 */       MerchantOffer offer = (MerchantOffer)offers.get(itemIndex);
/* 127 */       if (offer.isOutOfStock()) {
/* 128 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, OUT_OF_STOCK_SPRITE, this.leftPos + 83 + 99, this.topPos + 35, 28, 21);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderProgressBar(GuiGraphics graphics, int xo, int yo, MerchantOffer offer) {
/* 134 */     int traderLevel = this.menu.getTraderLevel();
/* 135 */     int traderXp = this.menu.getTraderXp();
/*     */     
/* 137 */     if (traderLevel >= 5) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 142 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_BACKGROUND_SPRITE, xo + 136, yo + 16, 102, 5);
/*     */     
/* 144 */     int minXp = VillagerData.getMinXpPerLevel(traderLevel);
/* 145 */     if (traderXp < minXp || !VillagerData.canLevelUp(traderLevel)) {
/*     */       return;
/*     */     }
/*     */     
/* 149 */     int progressLength = 102;
/* 150 */     float multiplier = 102.0F / (VillagerData.getMaxXpPerLevel(traderLevel) - minXp);
/* 151 */     int w = Math.min(Mth.floor(multiplier * (traderXp - minXp)), 102);
/*     */ 
/*     */     
/* 154 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_CURRENT_SPRITE, 102, 5, 0, 0, xo + 136, yo + 16, w, 5);
/* 155 */     int futureXp = this.menu.getFutureTraderXp();
/* 156 */     if (futureXp > 0) {
/* 157 */       int futureXpWidth = Math.min(Mth.floor(futureXp * multiplier), 102 - w);
/* 158 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_RESULT_SPRITE, 102, 5, w, 0, xo + 136 + w, yo + 16, futureXpWidth, 5);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderScroller(GuiGraphics graphics, int xo, int yo, int mouseX, int mouseY, MerchantOffers offers) {
/* 163 */     int steps = offers.size() + 1 - 7;
/* 164 */     if (steps > 1) {
/* 165 */       int leftOver = 139 - 27 + (steps - 1) * 139 / steps;
/* 166 */       int stepHeight = 1 + leftOver / steps + 139 / steps;
/* 167 */       int maxScrollerOff = 113;
/* 168 */       int scrollerYOff = Math.min(113, this.scrollOff * stepHeight);
/* 169 */       if (this.scrollOff == steps - 1)
/*     */       {
/* 171 */         scrollerYOff = 113;
/*     */       }
/* 173 */       int scrollerX = xo + 94;
/* 174 */       int scrollerY = yo + 18 + scrollerYOff;
/* 175 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_SPRITE, scrollerX, scrollerY, 6, 27);
/* 176 */       if (mouseX >= scrollerX && mouseX < xo + 94 + 6 && mouseY >= scrollerY && mouseY <= scrollerY + 27) {
/* 177 */         graphics.requestCursor(this.isDragging ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
/*     */       }
/*     */     } else {
/*     */       
/* 181 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_DISABLED_SPRITE, xo + 94, yo + 18, 6, 27);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 187 */     super.renderContents(graphics, mouseX, mouseY, a);
/*     */     
/* 189 */     MerchantOffers offers = this.menu.getOffers();
/* 190 */     if (!offers.isEmpty()) {
/* 191 */       int xo = (this.width - this.imageWidth) / 2;
/* 192 */       int yo = (this.height - this.imageHeight) / 2;
/*     */       
/* 194 */       int offerY = yo + 16 + 1;
/* 195 */       int sellItem1X = xo + 5 + 5;
/*     */       
/* 197 */       renderScroller(graphics, xo, yo, mouseX, mouseY, offers);
/*     */       
/* 199 */       int currentOfferIndex = 0;
/* 200 */       for (MerchantOffer offer : (Iterable<MerchantOffer>)offers) {
/* 201 */         if (canScroll(offers.size()) && (currentOfferIndex < this.scrollOff || currentOfferIndex >= 7 + this.scrollOff)) {
/* 202 */           currentOfferIndex++;
/*     */           
/*     */           continue;
/*     */         } 
/* 206 */         ItemStack baseCostA = offer.getBaseCostA();
/* 207 */         ItemStack costA = offer.getCostA();
/* 208 */         ItemStack costB = offer.getCostB();
/* 209 */         ItemStack result = offer.getResult();
/*     */         
/* 211 */         int decorHeight = offerY + 2;
/*     */         
/* 213 */         renderAndDecorateCostA(graphics, costA, baseCostA, sellItem1X, decorHeight);
/*     */         
/* 215 */         if (!costB.isEmpty()) {
/* 216 */           graphics.renderFakeItem(costB, xo + 5 + 35, decorHeight);
/* 217 */           graphics.renderItemDecorations(this.font, costB, xo + 5 + 35, decorHeight);
/*     */         } 
/*     */         
/* 220 */         renderButtonArrows(graphics, offer, xo, decorHeight);
/*     */         
/* 222 */         graphics.renderFakeItem(result, xo + 5 + 68, decorHeight);
/* 223 */         graphics.renderItemDecorations(this.font, result, xo + 5 + 68, decorHeight);
/*     */         
/* 225 */         offerY += 20;
/* 226 */         currentOfferIndex++;
/*     */       } 
/*     */       
/* 229 */       int itemIndex = this.shopItem;
/* 230 */       MerchantOffer selectedOffer = (MerchantOffer)offers.get(itemIndex);
/*     */       
/* 232 */       if (this.menu.showProgressBar()) {
/* 233 */         renderProgressBar(graphics, xo, yo, selectedOffer);
/*     */       }
/*     */       
/* 236 */       if (selectedOffer.isOutOfStock() && isHovering(186, 35, 22, 21, mouseX, mouseY) && this.menu.canRestock()) {
/* 237 */         graphics.setTooltipForNextFrame(this.font, DEPRECATED_TOOLTIP, mouseX, mouseY);
/*     */       }
/*     */       
/* 240 */       for (TradeOfferButton button : this.tradeOfferButtons) {
/* 241 */         if (button.isHoveredOrFocused()) {
/* 242 */           button.renderToolTip(graphics, mouseX, mouseY);
/*     */         }
/* 244 */         button.visible = (button.index < this.menu.getOffers().size());
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     renderTooltip(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   private void renderButtonArrows(GuiGraphics graphics, MerchantOffer offer, int xo, int decorHeight) {
/* 252 */     if (offer.isOutOfStock()) {
/*     */       
/* 254 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRADE_ARROW_OUT_OF_STOCK_SPRITE, xo + 5 + 35 + 20, decorHeight + 3, 10, 9);
/*     */     } else {
/*     */       
/* 257 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRADE_ARROW_SPRITE, xo + 5 + 35 + 20, decorHeight + 3, 10, 9);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderAndDecorateCostA(GuiGraphics graphics, ItemStack costA, ItemStack baseCostA, int sellItem1X, int decorHeight) {
/* 262 */     graphics.renderFakeItem(costA, sellItem1X, decorHeight);
/* 263 */     if (baseCostA.getCount() == costA.getCount()) {
/* 264 */       graphics.renderItemDecorations(this.font, costA, sellItem1X, decorHeight);
/*     */     } else {
/* 266 */       graphics.renderItemDecorations(this.font, baseCostA, sellItem1X, decorHeight, (baseCostA.getCount() == 1) ? "1" : null);
/* 267 */       graphics.renderItemDecorations(this.font, costA, sellItem1X + 14, decorHeight, (costA.getCount() == 1) ? "1" : null);
/*     */       
/* 269 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISCOUNT_STRIKETHRUOGH_SPRITE, sellItem1X + 7, decorHeight + 12, 9, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canScroll(int numberOfOffers) {
/* 274 */     return (numberOfOffers > 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 279 */     if (super.mouseScrolled(x, y, scrollX, scrollY)) {
/* 280 */       return true;
/*     */     }
/*     */     
/* 283 */     int numberOfOffers = this.menu.getOffers().size();
/* 284 */     if (canScroll(numberOfOffers)) {
/* 285 */       int maxScrollOff = numberOfOffers - 7;
/* 286 */       this.scrollOff = Mth.clamp((int)(this.scrollOff - scrollY), 0, maxScrollOff);
/*     */     } 
/* 288 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 293 */     int numberOfOffers = this.menu.getOffers().size();
/*     */     
/* 295 */     if (this.isDragging) {
/* 296 */       int fullScrollTopPos = this.topPos + 18;
/* 297 */       int fullScrollBottomPos = fullScrollTopPos + 139;
/* 298 */       int maxScrollOff = numberOfOffers - 7;
/*     */       
/* 300 */       float scrolling = ((float)event.y() - fullScrollTopPos - 13.5F) / ((fullScrollBottomPos - fullScrollTopPos) - 27.0F);
/* 301 */       scrolling = scrolling * maxScrollOff + 0.5F;
/*     */       
/* 303 */       this.scrollOff = Mth.clamp((int)scrolling, 0, maxScrollOff);
/*     */       
/* 305 */       return true;
/*     */     } 
/* 307 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 312 */     int xo = (this.width - this.imageWidth) / 2;
/* 313 */     int yo = (this.height - this.imageHeight) / 2;
/*     */     
/* 315 */     if (canScroll(this.menu.getOffers().size()) && 
/* 316 */       event.x() > (xo + 94) && event.x() < (xo + 94 + 6) && 
/* 317 */       event.y() > (yo + 18) && event.y() <= (yo + 18 + 139 + 1))
/*     */     {
/* 319 */       this.isDragging = true;
/*     */     }
/* 321 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 326 */     this.isDragging = false;
/* 327 */     return super.mouseReleased(event);
/*     */   }
/*     */   
/*     */   private class TradeOfferButton extends Button.Plain {
/*     */     final int index;
/*     */     
/*     */     public TradeOfferButton(int x, int y, int index, Button.OnPress onPress) {
/* 334 */       super(x, y, 88, 20, net.minecraft.network.chat.CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
/* 335 */       this.index = index;
/* 336 */       this.visible = false;
/*     */     }
/*     */     
/*     */     public int getIndex() {
/* 340 */       return this.index;
/*     */     }
/*     */     
/*     */     public void renderToolTip(GuiGraphics graphics, int xm, int ym) {
/* 344 */       if (this.isHovered && MerchantScreen.this.menu.getOffers().size() > this.index + MerchantScreen.this.scrollOff)
/* 345 */         if (xm < getX() + 20) {
/* 346 */           ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostA();
/* 347 */           graphics.setTooltipForNextFrame(MerchantScreen.this.font, item, xm, ym);
/* 348 */         } else if (xm < getX() + 50 && xm > getX() + 30) {
/* 349 */           ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostB();
/* 350 */           if (!item.isEmpty()) {
/* 351 */             graphics.setTooltipForNextFrame(MerchantScreen.this.font, item, xm, ym);
/*     */           }
/* 353 */         } else if (xm > getX() + 65) {
/* 354 */           ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getResult();
/* 355 */           graphics.setTooltipForNextFrame(MerchantScreen.this.font, item, xm, ym);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/MerchantScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */