/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.BundleMouseActions;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.ItemSlotMouseAction;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.MouseButtonInfo.MouseButton;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ClickType;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.joml.Vector2i;
/*     */ 
/*     */ public abstract class AbstractContainerScreen<T extends AbstractContainerMenu>
/*     */   extends Screen
/*     */   implements MenuAccess<T>
/*     */ {
/*  34 */   public static final Identifier INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/inventory.png");
/*     */   
/*  36 */   private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/slot_highlight_back");
/*  37 */   private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/slot_highlight_front");
/*     */   
/*     */   protected static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   
/*     */   protected static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*     */   
/*     */   private static final float SNAPBACK_SPEED = 100.0F;
/*     */   private static final int QUICKDROP_DELAY = 500;
/*  45 */   protected int imageWidth = 176;
/*  46 */   protected int imageHeight = 166;
/*     */   
/*     */   protected int titleLabelX;
/*     */   
/*     */   protected int titleLabelY;
/*     */   
/*     */   protected int inventoryLabelX;
/*     */   
/*     */   protected int inventoryLabelY;
/*     */   private final List<ItemSlotMouseAction> itemSlotMouseActions;
/*     */   protected final T menu;
/*     */   protected final Component playerInventoryTitle;
/*     */   protected Slot hoveredSlot;
/*     */   private Slot clickedSlot;
/*     */   private Slot quickdropSlot;
/*     */   private Slot lastClickSlot;
/*     */   private SnapbackData snapbackData;
/*     */   protected int leftPos;
/*     */   protected int topPos;
/*     */   private boolean isSplittingStack;
/*  66 */   private ItemStack draggingItem = ItemStack.EMPTY;
/*     */   
/*     */   private long quickdropTime;
/*  69 */   protected final Set<Slot> quickCraftSlots = Sets.newHashSet();
/*     */   protected boolean isQuickCrafting;
/*     */   private int quickCraftingType;
/*     */   @net.minecraft.client.input.MouseButtonInfo.MouseButton
/*     */   private int quickCraftingButton;
/*     */   private boolean skipNextRelease;
/*     */   private int quickCraftingRemainder;
/*     */   private boolean doubleclick;
/*  77 */   private ItemStack lastQuickMoved = ItemStack.EMPTY;
/*     */   
/*     */   public AbstractContainerScreen(T menu, Inventory inventory, Component title) {
/*  80 */     super(title);
/*  81 */     this.menu = menu;
/*  82 */     this.playerInventoryTitle = inventory.getDisplayName();
/*  83 */     this.skipNextRelease = true;
/*  84 */     this.titleLabelX = 8;
/*  85 */     this.titleLabelY = 6;
/*  86 */     this.inventoryLabelX = 8;
/*  87 */     this.inventoryLabelY = this.imageHeight - 94;
/*  88 */     this.itemSlotMouseActions = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  93 */     this.leftPos = (this.width - this.imageWidth) / 2;
/*  94 */     this.topPos = (this.height - this.imageHeight) / 2;
/*  95 */     this.itemSlotMouseActions.clear();
/*  96 */     addItemSlotMouseAction((ItemSlotMouseAction)new BundleMouseActions(this.minecraft));
/*     */   }
/*     */   
/*     */   protected void addItemSlotMouseAction(ItemSlotMouseAction itemSlotMouseAction) {
/* 100 */     this.itemSlotMouseActions.add(itemSlotMouseAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 105 */     renderContents(graphics, mouseX, mouseY, a);
/* 106 */     renderCarriedItem(graphics, mouseX, mouseY);
/* 107 */     renderSnapbackItem(graphics);
/*     */   }
/*     */   
/*     */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 111 */     int xo = this.leftPos;
/* 112 */     int yo = this.topPos;
/*     */     
/* 114 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 116 */     graphics.pose().pushMatrix();
/* 117 */     graphics.pose().translate(xo, yo);
/*     */     
/* 119 */     renderLabels(graphics, mouseX, mouseY);
/*     */     
/* 121 */     Slot previouslyHoveredSlot = this.hoveredSlot;
/* 122 */     this.hoveredSlot = getHoveredSlot(mouseX, mouseY);
/*     */     
/* 124 */     renderSlotHighlightBack(graphics);
/* 125 */     renderSlots(graphics, mouseX, mouseY);
/* 126 */     renderSlotHighlightFront(graphics);
/*     */     
/* 128 */     if (previouslyHoveredSlot != null && previouslyHoveredSlot != this.hoveredSlot) {
/* 129 */       onStopHovering(previouslyHoveredSlot);
/*     */     }
/*     */     
/* 132 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   public void renderCarriedItem(GuiGraphics graphics, int mouseX, int mouseY) {
/* 136 */     ItemStack carried = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
/* 137 */     if (!carried.isEmpty()) {
/* 138 */       int xOffset = 8;
/* 139 */       int yOffset = this.draggingItem.isEmpty() ? 8 : 16;
/* 140 */       String itemCount = null;
/*     */       
/* 142 */       if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
/* 143 */         carried = carried.copyWithCount(Mth.ceil(carried.getCount() / 2.0F));
/* 144 */       } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
/* 145 */         carried = carried.copyWithCount(this.quickCraftingRemainder);
/*     */         
/* 147 */         if (carried.isEmpty()) {
/* 148 */           itemCount = String.valueOf(ChatFormatting.YELLOW) + "0";
/*     */         }
/*     */       } 
/* 151 */       graphics.nextStratum();
/* 152 */       renderFloatingItem(graphics, carried, mouseX - 8, mouseY - yOffset, itemCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderSnapbackItem(GuiGraphics graphics) {
/* 157 */     if (this.snapbackData != null) {
/* 158 */       float snapbackProgress = Mth.clamp((float)(Util.getMillis() - this.snapbackData.time) / 100.0F, 0.0F, 1.0F);
/*     */       
/* 160 */       int xd = this.snapbackData.end.x - this.snapbackData.start.x;
/* 161 */       int yd = this.snapbackData.end.y - this.snapbackData.start.y;
/* 162 */       int x = this.snapbackData.start.x + (int)(xd * snapbackProgress);
/* 163 */       int y = this.snapbackData.start.y + (int)(yd * snapbackProgress);
/*     */       
/* 165 */       graphics.nextStratum();
/* 166 */       renderFloatingItem(graphics, this.snapbackData.item, x, y, null);
/*     */       
/* 168 */       if (snapbackProgress >= 1.0F) {
/* 169 */         this.snapbackData = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void renderSlots(GuiGraphics graphics, int mouseX, int mouseY) {
/* 175 */     for (Slot slot : (Iterable<Slot>)((AbstractContainerMenu)this.menu).slots) {
/* 176 */       if (slot.isActive()) {
/* 177 */         renderSlot(graphics, slot, mouseX, mouseY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 184 */     super.renderBackground(graphics, mouseX, mouseY, a);
/* 185 */     renderBg(graphics, a, mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 190 */     if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
/* 191 */       for (ItemSlotMouseAction itemMouseAction : this.itemSlotMouseActions) {
/* 192 */         if (itemMouseAction.matches(this.hoveredSlot) && 
/* 193 */           itemMouseAction.onMouseScrolled(scrollX, scrollY, this.hoveredSlot.index, this.hoveredSlot.getItem())) {
/* 194 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 199 */     return false;
/*     */   }
/*     */   
/*     */   private void renderSlotHighlightBack(GuiGraphics graphics) {
/* 203 */     if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable()) {
/* 204 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 24, 24);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderSlotHighlightFront(GuiGraphics graphics) {
/* 209 */     if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable()) {
/* 210 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 24, 24);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
/* 215 */     if (this.hoveredSlot == null || !this.hoveredSlot.hasItem()) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     ItemStack item = this.hoveredSlot.getItem();
/* 220 */     if (this.menu.getCarried().isEmpty() || showTooltipWithItemInHand(item)) {
/* 221 */       graphics.setTooltipForNextFrame(this.font, getTooltipFromContainerItem(item), item.getTooltipImage(), mouseX, mouseY, (Identifier)item.get(DataComponents.TOOLTIP_STYLE));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean showTooltipWithItemInHand(ItemStack item) {
/* 226 */     return (Boolean)item.getTooltipImage()
/* 227 */       .map(ClientTooltipComponent::create)
/* 228 */       .map(ClientTooltipComponent::showTooltipWithItemInHand)
/* 229 */       .orElse(false);
/*     */   }
/*     */   
/*     */   protected List<Component> getTooltipFromContainerItem(ItemStack itemStack) {
/* 233 */     return getTooltipFromItem(this.minecraft, itemStack);
/*     */   }
/*     */   
/*     */   private void renderFloatingItem(GuiGraphics graphics, ItemStack carried, int x, int y, String itemCount) {
/* 237 */     graphics.renderItem(carried, x, y);
/* 238 */     graphics.renderItemDecorations(this.font, carried, x, y - (this.draggingItem.isEmpty() ? 0 : 8), itemCount);
/*     */   }
/*     */   
/*     */   protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
/* 242 */     graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
/* 243 */     graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
/*     */   }
/*     */   
/*     */   protected abstract void renderBg(GuiGraphics paramGuiGraphics, float paramFloat, int paramInt1, int paramInt2);
/*     */   
/*     */   protected void renderSlot(GuiGraphics graphics, Slot slot, int mouseX, int mouseY) {
/* 249 */     int x = slot.x;
/* 250 */     int y = slot.y;
/* 251 */     ItemStack itemStack = slot.getItem();
/*     */     boolean ghostStack = false;
/* 253 */     boolean done = (slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack);
/* 254 */     ItemStack carried = this.menu.getCarried();
/* 255 */     String itemCount = null;
/*     */     
/* 257 */     if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemStack.isEmpty()) {
/* 258 */       itemStack = itemStack.copyWithCount(itemStack.getCount() / 2);
/* 259 */     } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !carried.isEmpty()) {
/* 260 */       if (this.quickCraftSlots.size() == 1) {
/*     */         return;
/*     */       }
/* 263 */       if (AbstractContainerMenu.canItemQuickReplace(slot, carried, true) && this.menu.canDragTo(slot)) {
/* 264 */         ghostStack = true;
/*     */         
/* 266 */         int maxSize = Math.min(carried.getMaxStackSize(), slot.getMaxStackSize(carried));
/* 267 */         int carry = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
/* 268 */         int newCount = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, carried) + carry;
/* 269 */         if (newCount > maxSize) {
/* 270 */           newCount = maxSize;
/* 271 */           itemCount = ChatFormatting.YELLOW.toString() + ChatFormatting.YELLOW.toString();
/*     */         } 
/*     */         
/* 274 */         itemStack = carried.copyWithCount(newCount);
/*     */       } else {
/* 276 */         this.quickCraftSlots.remove(slot);
/* 277 */         recalculateQuickCraftRemaining();
/*     */       } 
/*     */     } 
/*     */     
/* 281 */     if (itemStack.isEmpty() && slot.isActive()) {
/* 282 */       Identifier icon = slot.getNoItemIcon();
/* 283 */       if (icon != null) {
/* 284 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, x, y, 16, 16);
/* 285 */         done = true;
/*     */       } 
/*     */     } 
/*     */     
/* 289 */     if (!done) {
/* 290 */       if (ghostStack) {
/* 291 */         graphics.fill(x, y, x + 16, y + 16, -2130706433);
/*     */       }
/* 293 */       int seed = slot.x + slot.y * this.imageWidth;
/* 294 */       if (slot.isFake()) {
/* 295 */         graphics.renderFakeItem(itemStack, x, y, seed);
/*     */       } else {
/* 297 */         graphics.renderItem(itemStack, x, y, seed);
/*     */       } 
/* 299 */       graphics.renderItemDecorations(this.font, itemStack, x, y, itemCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recalculateQuickCraftRemaining() {
/* 304 */     ItemStack carried = this.menu.getCarried();
/* 305 */     if (carried.isEmpty() || !this.isQuickCrafting) {
/*     */       return;
/*     */     }
/*     */     
/* 309 */     if (this.quickCraftingType == 2) {
/* 310 */       this.quickCraftingRemainder = carried.getMaxStackSize();
/*     */       
/*     */       return;
/*     */     } 
/* 314 */     this.quickCraftingRemainder = carried.getCount();
/*     */     
/* 316 */     for (Slot slot : this.quickCraftSlots) {
/* 317 */       ItemStack slotItemStack = slot.getItem();
/* 318 */       int carry = slotItemStack.isEmpty() ? 0 : slotItemStack.getCount();
/* 319 */       int maxSize = Math.min(carried.getMaxStackSize(), slot.getMaxStackSize(carried));
/* 320 */       int newCount = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, carried) + carry, maxSize);
/* 321 */       this.quickCraftingRemainder -= newCount - carry;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Slot getHoveredSlot(double x, double y) {
/* 326 */     for (Slot slot : (Iterable<Slot>)((AbstractContainerMenu)this.menu).slots) {
/* 327 */       if (slot.isActive() && isHovering(slot, x, y)) {
/* 328 */         return slot;
/*     */       }
/*     */     } 
/* 331 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 336 */     if (super.mouseClicked(event, doubleClick)) {
/* 337 */       return true;
/*     */     }
/* 339 */     boolean cloning = (this.minecraft.options.keyPickItem.matchesMouse(event) && this.minecraft.player.hasInfiniteMaterials());
/* 340 */     Slot slot = getHoveredSlot(event.x(), event.y());
/* 341 */     this.doubleclick = (this.lastClickSlot == slot && doubleClick);
/* 342 */     this.skipNextRelease = false;
/*     */     
/* 344 */     if (event.button() == 0 || event.button() == 1 || cloning) {
/* 345 */       int xo = this.leftPos;
/* 346 */       int yo = this.topPos;
/* 347 */       boolean clickedOutside = hasClickedOutside(event.x(), event.y(), xo, yo);
/*     */       
/* 349 */       int slotId = -1;
/* 350 */       if (slot != null) {
/* 351 */         slotId = slot.index;
/*     */       }
/*     */       
/* 354 */       if (clickedOutside) {
/* 355 */         slotId = -999;
/*     */       }
/*     */       
/* 358 */       if ((Boolean)this.minecraft.options.touchscreen().get() && clickedOutside && this.menu.getCarried().isEmpty()) {
/* 359 */         onClose();
/* 360 */         return true;
/*     */       } 
/*     */       
/* 363 */       if (slotId != -1) {
/* 364 */         if ((Boolean)this.minecraft.options.touchscreen().get()) {
/* 365 */           if (slot != null && slot.hasItem()) {
/* 366 */             this.clickedSlot = slot;
/* 367 */             this.draggingItem = ItemStack.EMPTY;
/* 368 */             this.isSplittingStack = (event.button() == 1);
/*     */           } else {
/* 370 */             this.clickedSlot = null;
/*     */           } 
/* 372 */         } else if (!this.isQuickCrafting) {
/* 373 */           if (this.menu.getCarried().isEmpty()) {
/* 374 */             if (cloning) {
/* 375 */               slotClicked(slot, slotId, event.button(), ClickType.CLONE);
/*     */             } else {
/* 377 */               boolean quickKey = (slotId != -999 && event.hasShiftDown());
/* 378 */               ClickType clickType = ClickType.PICKUP;
/* 379 */               if (quickKey) {
/* 380 */                 this.lastQuickMoved = (slot != null && slot.hasItem()) ? slot.getItem().copy() : ItemStack.EMPTY;
/* 381 */                 clickType = ClickType.QUICK_MOVE;
/* 382 */               } else if (slotId == -999) {
/* 383 */                 clickType = ClickType.THROW;
/*     */               } 
/* 385 */               slotClicked(slot, slotId, event.button(), clickType);
/*     */             } 
/* 387 */             this.skipNextRelease = true;
/*     */           } else {
/* 389 */             this.isQuickCrafting = true;
/* 390 */             this.quickCraftingButton = event.button();
/* 391 */             this.quickCraftSlots.clear();
/*     */             
/* 393 */             if (event.button() == 0) {
/* 394 */               this.quickCraftingType = 0;
/* 395 */             } else if (event.button() == 1) {
/* 396 */               this.quickCraftingType = 1;
/* 397 */             } else if (cloning) {
/* 398 */               this.quickCraftingType = 2;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } else {
/* 404 */       checkHotbarMouseClicked(event);
/*     */     } 
/*     */     
/* 407 */     this.lastClickSlot = slot;
/*     */     
/* 409 */     return true;
/*     */   }
/*     */   
/*     */   private void checkHotbarMouseClicked(MouseButtonEvent event) {
/* 413 */     if (this.hoveredSlot != null && this.menu.getCarried().isEmpty()) {
/* 414 */       if (this.minecraft.options.keySwapOffhand.matchesMouse(event)) {
/* 415 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
/*     */         return;
/*     */       } 
/* 418 */       for (int i = 0; i < 9; i++) {
/* 419 */         if (this.minecraft.options.keyHotbarSlots[i].matchesMouse(event)) {
/* 420 */           slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ClickType.SWAP);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/* 427 */     return (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 432 */     Slot slot = getHoveredSlot(event.x(), event.y());
/* 433 */     ItemStack carried = this.menu.getCarried();
/*     */     
/* 435 */     if (this.clickedSlot != null && (Boolean)this.minecraft.options.touchscreen().get()) {
/* 436 */       if (event.button() == 0 || event.button() == 1) {
/* 437 */         if (this.draggingItem.isEmpty()) {
/* 438 */           if (slot != this.clickedSlot && !this.clickedSlot.getItem().isEmpty()) {
/* 439 */             this.draggingItem = this.clickedSlot.getItem().copy();
/*     */           }
/* 441 */         } else if (this.draggingItem.getCount() > 1 && slot != null && AbstractContainerMenu.canItemQuickReplace(slot, this.draggingItem, false)) {
/* 442 */           long time = Util.getMillis();
/*     */           
/* 444 */           if (this.quickdropSlot == slot) {
/* 445 */             if (time - this.quickdropTime > 500L) {
/* 446 */               slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
/* 447 */               slotClicked(slot, slot.index, 1, ClickType.PICKUP);
/* 448 */               slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
/* 449 */               this.quickdropTime = time + 750L;
/* 450 */               this.draggingItem.shrink(1);
/*     */             } 
/*     */           } else {
/* 453 */             this.quickdropSlot = slot;
/* 454 */             this.quickdropTime = time;
/*     */           } 
/*     */         } 
/*     */       }
/* 458 */       return true;
/* 459 */     }  if (this.isQuickCrafting && slot != null && !carried.isEmpty() && (carried.getCount() > this.quickCraftSlots.size() || this.quickCraftingType == 2) && AbstractContainerMenu.canItemQuickReplace(slot, carried, true) && slot.mayPlace(carried) && this.menu.canDragTo(slot)) {
/* 460 */       this.quickCraftSlots.add(slot);
/* 461 */       recalculateQuickCraftRemaining();
/* 462 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 466 */     if (slot == null && this.menu.getCarried().isEmpty()) {
/* 467 */       return super.mouseDragged(event, dx, dy);
/*     */     }
/*     */     
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 475 */     Slot slot = getHoveredSlot(event.x(), event.y());
/* 476 */     int xo = this.leftPos;
/* 477 */     int yo = this.topPos;
/* 478 */     boolean clickedOutside = hasClickedOutside(event.x(), event.y(), xo, yo);
/*     */     
/* 480 */     int slotId = -1;
/* 481 */     if (slot != null) {
/* 482 */       slotId = slot.index;
/*     */     }
/*     */     
/* 485 */     if (clickedOutside) {
/* 486 */       slotId = -999;
/*     */     }
/*     */     
/* 489 */     if (this.doubleclick && slot != null && event.button() == 0 && this.menu.canTakeItemForPickAll(ItemStack.EMPTY, slot)) {
/* 490 */       if (event.hasShiftDown()) {
/* 491 */         if (!this.lastQuickMoved.isEmpty()) {
/* 492 */           for (Slot target : (Iterable<Slot>)((AbstractContainerMenu)this.menu).slots) {
/* 493 */             if (target != null && target.mayPickup((Player)this.minecraft.player) && target.hasItem() && target.container == slot.container && AbstractContainerMenu.canItemQuickReplace(target, this.lastQuickMoved, true)) {
/* 494 */               slotClicked(target, target.index, event.button(), ClickType.QUICK_MOVE);
/*     */             }
/*     */           } 
/*     */         }
/*     */       } else {
/* 499 */         slotClicked(slot, slotId, event.button(), ClickType.PICKUP_ALL);
/*     */       } 
/* 501 */       this.doubleclick = false;
/*     */     } else {
/* 503 */       if (this.isQuickCrafting && this.quickCraftingButton != event.button()) {
/* 504 */         this.isQuickCrafting = false;
/* 505 */         this.quickCraftSlots.clear();
/* 506 */         this.skipNextRelease = true;
/* 507 */         return true;
/* 508 */       }  if (this.skipNextRelease) {
/* 509 */         this.skipNextRelease = false;
/* 510 */         return true;
/*     */       } 
/*     */       
/* 513 */       if (this.clickedSlot != null && (Boolean)this.minecraft.options.touchscreen().get()) {
/* 514 */         if (event.button() == 0 || event.button() == 1) {
/* 515 */           if (this.draggingItem.isEmpty() && slot != this.clickedSlot) {
/* 516 */             this.draggingItem = this.clickedSlot.getItem();
/*     */           }
/*     */           
/* 519 */           boolean canReplace = AbstractContainerMenu.canItemQuickReplace(slot, this.draggingItem, false);
/*     */           
/* 521 */           if (slotId != -1 && !this.draggingItem.isEmpty() && canReplace) {
/* 522 */             slotClicked(this.clickedSlot, this.clickedSlot.index, event.button(), ClickType.PICKUP);
/* 523 */             slotClicked(slot, slotId, 0, ClickType.PICKUP);
/*     */             
/* 525 */             if (this.menu.getCarried().isEmpty()) {
/* 526 */               this.snapbackData = null;
/*     */             } else {
/* 528 */               slotClicked(this.clickedSlot, this.clickedSlot.index, event.button(), ClickType.PICKUP);
/* 529 */               this.snapbackData = new SnapbackData(this.draggingItem, new Vector2i((int)event.x(), (int)event.y()), new Vector2i(this.clickedSlot.x + xo, this.clickedSlot.y + yo), Util.getMillis());
/*     */             } 
/* 531 */           } else if (!this.draggingItem.isEmpty()) {
/* 532 */             this.snapbackData = new SnapbackData(this.draggingItem, new Vector2i((int)event.x(), (int)event.y()), new Vector2i(this.clickedSlot.x + xo, this.clickedSlot.y + yo), Util.getMillis());
/*     */           } 
/*     */           
/* 535 */           clearDraggingState();
/*     */         } 
/* 537 */       } else if (this.isQuickCrafting && !this.quickCraftSlots.isEmpty()) {
/* 538 */         slotClicked(null, -999, AbstractContainerMenu.getQuickcraftMask(0, this.quickCraftingType), ClickType.QUICK_CRAFT);
/*     */         
/* 540 */         for (Slot quickSlot : this.quickCraftSlots) {
/* 541 */           slotClicked(quickSlot, quickSlot.index, AbstractContainerMenu.getQuickcraftMask(1, this.quickCraftingType), ClickType.QUICK_CRAFT);
/*     */         }
/*     */         
/* 544 */         slotClicked(null, -999, AbstractContainerMenu.getQuickcraftMask(2, this.quickCraftingType), ClickType.QUICK_CRAFT);
/* 545 */       } else if (!this.menu.getCarried().isEmpty()) {
/* 546 */         if (this.minecraft.options.keyPickItem.matchesMouse(event)) {
/* 547 */           slotClicked(slot, slotId, event.button(), ClickType.CLONE);
/*     */         } else {
/* 549 */           boolean quickKey = (slotId != -999 && event.hasShiftDown());
/* 550 */           if (quickKey) {
/* 551 */             this.lastQuickMoved = (slot != null && slot.hasItem()) ? slot.getItem().copy() : ItemStack.EMPTY;
/*     */           }
/* 553 */           slotClicked(slot, slotId, event.button(), quickKey ? ClickType.QUICK_MOVE : ClickType.PICKUP);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 558 */     this.isQuickCrafting = false;
/*     */     
/* 560 */     return true;
/*     */   }
/*     */   
/*     */   public void clearDraggingState() {
/* 564 */     this.draggingItem = ItemStack.EMPTY;
/* 565 */     this.clickedSlot = null;
/*     */   }
/*     */   
/*     */   private boolean isHovering(Slot slot, double xm, double ym) {
/* 569 */     return isHovering(slot.x, slot.y, 16, 16, xm, ym);
/*     */   }
/*     */   
/*     */   protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
/* 573 */     int xo = this.leftPos;
/* 574 */     int yo = this.topPos;
/* 575 */     xm -= xo;
/* 576 */     ym -= yo;
/*     */     
/* 578 */     return (xm >= (left - 1) && xm < (left + w + 1) && ym >= (top - 1) && ym < (top + h + 1));
/*     */   }
/*     */   
/*     */   private void onStopHovering(Slot slot) {
/* 582 */     if (slot.hasItem()) {
/* 583 */       for (ItemSlotMouseAction itemMouseAction : this.itemSlotMouseActions) {
/* 584 */         if (itemMouseAction.matches(slot)) {
/* 585 */           itemMouseAction.onStopHovering(slot);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ClickType clickType) {
/* 592 */     if (slot != null) {
/* 593 */       slotId = slot.index;
/*     */     }
/* 595 */     onMouseClickAction(slot, clickType);
/* 596 */     this.minecraft.gameMode.handleInventoryMouseClick(((AbstractContainerMenu)this.menu).containerId, slotId, buttonNum, clickType, (Player)this.minecraft.player);
/*     */   }
/*     */   
/*     */   void onMouseClickAction(Slot slot, ClickType clickType) {
/* 600 */     if (slot != null && slot.hasItem()) {
/* 601 */       for (ItemSlotMouseAction itemMouseAction : this.itemSlotMouseActions) {
/* 602 */         if (itemMouseAction.matches(slot)) {
/* 603 */           itemMouseAction.onSlotClicked(slot, clickType);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void handleSlotStateChanged(int slotId, int containerId, boolean newState) {
/* 610 */     this.minecraft.gameMode.handleSlotStateChanged(slotId, containerId, newState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 615 */     if (super.keyPressed(event)) {
/* 616 */       return true;
/*     */     }
/* 618 */     if (this.minecraft.options.keyInventory.matches(event)) {
/* 619 */       onClose();
/* 620 */       return true;
/*     */     } 
/* 622 */     checkHotbarKeyPressed(event);
/*     */     
/* 624 */     if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
/* 625 */       if (this.minecraft.options.keyPickItem.matches(event)) {
/* 626 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ClickType.CLONE);
/* 627 */       } else if (this.minecraft.options.keyDrop.matches(event)) {
/* 628 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, event.hasControlDown() ? 1 : 0, ClickType.THROW);
/*     */       } 
/*     */     }
/* 631 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean checkHotbarKeyPressed(KeyEvent event) {
/* 635 */     if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
/* 636 */       if (this.minecraft.options.keySwapOffhand.matches(event)) {
/* 637 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
/* 638 */         return true;
/*     */       } 
/* 640 */       for (int i = 0; i < 9; i++) {
/* 641 */         if (this.minecraft.options.keyHotbarSlots[i].matches(event)) {
/* 642 */           slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ClickType.SWAP);
/* 643 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 648 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 653 */     if (this.minecraft.player == null) {
/*     */       return;
/*     */     }
/* 656 */     this.menu.removed((Player)this.minecraft.player);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 661 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 666 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void tick() {
/* 671 */     super.tick();
/*     */ 
/*     */     
/* 674 */     if (!this.minecraft.player.isAlive() || this.minecraft.player.isRemoved()) {
/* 675 */       this.minecraft.player.closeContainer();
/*     */     } else {
/* 677 */       containerTick();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void containerTick() {}
/*     */ 
/*     */   
/*     */   public T getMenu() {
/* 686 */     return this.menu;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 691 */     this.minecraft.player.closeContainer();
/* 692 */     if (this.hoveredSlot != null) {
/* 693 */       onStopHovering(this.hoveredSlot);
/*     */     }
/* 695 */     super.onClose();
/*     */   }
/*     */   private static final class SnapbackData extends Record { private final ItemStack item; private final Vector2i start; private final Vector2i end; private final long time;
/*     */     
/* 699 */     private SnapbackData(ItemStack item, Vector2i start, Vector2i end, long time) { this.item = item; this.start = start; this.end = end; this.time = time; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen$SnapbackData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #699	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 699 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen$SnapbackData; } public ItemStack item() { return this.item; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen$SnapbackData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #699	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen$SnapbackData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen$SnapbackData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #699	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen$SnapbackData;
/* 699 */       //   0	8	1	o	Ljava/lang/Object; } public Vector2i start() { return this.start; } public Vector2i end() { return this.end; } public long time() { return this.time; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/AbstractContainerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */