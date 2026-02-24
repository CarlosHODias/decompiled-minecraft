/*      */ package net.minecraft.client.gui.screens.inventory;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.blaze3d.platform.InputConstants;
/*      */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.client.HotbarManager;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.GuiGraphics;
/*      */ import net.minecraft.client.gui.components.EditBox;
/*      */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*      */ import net.minecraft.client.input.CharacterEvent;
/*      */ import net.minecraft.client.input.KeyEvent;
/*      */ import net.minecraft.client.input.MouseButtonEvent;
/*      */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*      */ import net.minecraft.client.multiplayer.SessionSearchTrees;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.player.inventory.Hotbar;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.searchtree.SearchTree;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ClickType;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.CreativeModeTab;
/*      */ import net.minecraft.world.item.CreativeModeTabs;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.TooltipFlag;
/*      */ import net.minecraft.world.level.Level;
/*      */ 
/*      */ public class CreativeModeInventoryScreen extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
/*   60 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/creative_inventory/scroller");
/*   61 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/creative_inventory/scroller_disabled");
/*      */   
/*   63 */   private static final Identifier[] UNSELECTED_TOP_TABS = new Identifier[] {
/*   64 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_1"), 
/*   65 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_2"), 
/*   66 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_3"), 
/*   67 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_4"), 
/*   68 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_5"), 
/*   69 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_6"), 
/*   70 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_7")
/*      */     };
/*   72 */   private static final Identifier[] SELECTED_TOP_TABS = new Identifier[] {
/*   73 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_1"), 
/*   74 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_2"), 
/*   75 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_3"), 
/*   76 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_4"), 
/*   77 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_5"), 
/*   78 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_6"), 
/*   79 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_7")
/*      */     };
/*   81 */   private static final Identifier[] UNSELECTED_BOTTOM_TABS = new Identifier[] {
/*   82 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_1"), 
/*   83 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_2"), 
/*   84 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_3"), 
/*   85 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_4"), 
/*   86 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_5"), 
/*   87 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_6"), 
/*   88 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_7")
/*      */     };
/*   90 */   private static final Identifier[] SELECTED_BOTTOM_TABS = new Identifier[] {
/*   91 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_1"), 
/*   92 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_2"), 
/*   93 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_3"), 
/*   94 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_4"), 
/*   95 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_5"), 
/*   96 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_6"), 
/*   97 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_7")
/*      */     };
/*      */   
/*      */   private static final int NUM_ROWS = 5;
/*      */   private static final int NUM_COLS = 9;
/*      */   private static final int TAB_WIDTH = 26;
/*      */   private static final int TAB_HEIGHT = 32;
/*      */   private static final int SCROLLER_WIDTH = 12;
/*      */   private static final int SCROLLER_HEIGHT = 15;
/*  106 */   private static final SimpleContainer CONTAINER = new SimpleContainer(45);
/*  107 */   private static final Component TRASH_SLOT_TOOLTIP = (Component)Component.translatable("inventory.binSlot");
/*      */   
/*  109 */   private static CreativeModeTab selectedTab = CreativeModeTabs.getDefaultTab();
/*      */   
/*      */   private float scrollOffs;
/*      */   private boolean scrolling;
/*      */   private EditBox searchBox;
/*      */   private List<Slot> originalSlots;
/*      */   private Slot destroyItemSlot;
/*      */   private CreativeInventoryListener listener;
/*      */   private boolean ignoreTextInput;
/*      */   private boolean hasClickedOutside;
/*  119 */   private final Set<TagKey<Item>> visibleTags = new HashSet<>();
/*      */   private final boolean displayOperatorCreativeTab;
/*      */   private final EffectsInInventory effects;
/*      */   
/*      */   public static class ItemPickerMenu extends AbstractContainerMenu {
/*  124 */     public final NonNullList<ItemStack> items = NonNullList.create();
/*      */     
/*      */     private final AbstractContainerMenu inventoryMenu;
/*      */     
/*      */     public ItemPickerMenu(Player player) {
/*  129 */       super(null, 0);
/*  130 */       this.inventoryMenu = (AbstractContainerMenu)player.inventoryMenu;
/*      */       
/*  132 */       Inventory inventory = player.getInventory();
/*  133 */       for (int y = 0; y < 5; y++) {
/*  134 */         for (int x = 0; x < 9; x++) {
/*  135 */           addSlot(new CreativeModeInventoryScreen.CustomCreativeSlot((Container)CreativeModeInventoryScreen.CONTAINER, y * 9 + x, 9 + x * 18, 18 + y * 18));
/*      */         }
/*      */       } 
/*      */       
/*  139 */       addInventoryHotbarSlots((Container)inventory, 9, 112);
/*      */       
/*  141 */       scrollTo(0.0F);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean stillValid(Player player) {
/*  146 */       return true;
/*      */     }
/*      */     
/*      */     protected int calculateRowCount() {
/*  150 */       return Mth.positiveCeilDiv(this.items.size(), 9) - 5;
/*      */     }
/*      */     
/*      */     protected int getRowIndexForScroll(float scrollOffs) {
/*  154 */       return Math.max((int)((scrollOffs * calculateRowCount()) + 0.5D), 0);
/*      */     }
/*      */     
/*      */     protected float getScrollForRowIndex(int rowIndex) {
/*  158 */       return Mth.clamp(rowIndex / calculateRowCount(), 0.0F, 1.0F);
/*      */     }
/*      */     
/*      */     protected float subtractInputFromScroll(float scrollOffs, double input) {
/*  162 */       return Mth.clamp(scrollOffs - (float)(input / calculateRowCount()), 0.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public void scrollTo(float scrollOffs) {
/*  166 */       int rowToScrollTo = getRowIndexForScroll(scrollOffs);
/*  167 */       for (int y = 0; y < 5; y++) {
/*  168 */         for (int x = 0; x < 9; x++) {
/*  169 */           int slot = x + (y + rowToScrollTo) * 9;
/*  170 */           if (slot >= 0 && slot < this.items.size()) {
/*  171 */             CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, (ItemStack)this.items.get(slot));
/*      */           } else {
/*  173 */             CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, ItemStack.EMPTY);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean canScroll() {
/*  180 */       return (this.items.size() > 45);
/*      */     }
/*      */ 
/*      */     
/*      */     public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  185 */       if (slotIndex >= this.slots.size() - 9 && slotIndex < this.slots.size()) {
/*  186 */         Slot slot = (Slot)this.slots.get(slotIndex);
/*      */         
/*  188 */         if (slot != null && slot.hasItem()) {
/*  189 */           slot.setByPlayer(ItemStack.EMPTY);
/*      */         }
/*      */       } 
/*      */       
/*  193 */       return ItemStack.EMPTY;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/*  198 */       return (target.container != CreativeModeInventoryScreen.CONTAINER);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDragTo(Slot slot) {
/*  203 */       return (slot.container != CreativeModeInventoryScreen.CONTAINER);
/*      */     }
/*      */ 
/*      */     
/*      */     public ItemStack getCarried() {
/*  208 */       return this.inventoryMenu.getCarried();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setCarried(ItemStack carried) {
/*  213 */       this.inventoryMenu.setCarried(carried);
/*      */     }
/*      */   }
/*      */   
/*      */   public CreativeModeInventoryScreen(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean displayOperatorCreativeTab) {
/*  218 */     super(new ItemPickerMenu((Player)player), player.getInventory(), CommonComponents.EMPTY);
/*  219 */     player.containerMenu = this.menu;
/*  220 */     this.imageHeight = 136;
/*  221 */     this.imageWidth = 195;
/*  222 */     this.displayOperatorCreativeTab = displayOperatorCreativeTab;
/*  223 */     tryRebuildTabContents(player.connection.searchTrees(), enabledFeatures, hasPermissions((Player)player), (HolderLookup.Provider)player.level().registryAccess());
/*  224 */     this.effects = new EffectsInInventory(this);
/*      */   }
/*      */   
/*      */   private boolean hasPermissions(Player player) {
/*  228 */     return (player.canUseGameMasterBlocks() && this.displayOperatorCreativeTab);
/*      */   }
/*      */   
/*      */   private void tryRefreshInvalidatedTabs(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/*  232 */     ClientPacketListener connection = this.minecraft.getConnection();
/*  233 */     if (tryRebuildTabContents((connection != null) ? connection.searchTrees() : null, enabledFeatures, hasPermissions, holders)) {
/*  234 */       for (CreativeModeTab tab : (Iterable<CreativeModeTab>)CreativeModeTabs.allTabs()) {
/*  235 */         Collection<ItemStack> displayList = tab.getDisplayItems();
/*  236 */         if (tab == selectedTab) {
/*  237 */           if (tab.getType() == CreativeModeTab.Type.CATEGORY && displayList.isEmpty()) {
/*  238 */             selectTab(CreativeModeTabs.getDefaultTab()); continue;
/*      */           } 
/*  240 */           refreshCurrentTabContents(displayList);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean tryRebuildTabContents(SessionSearchTrees searchTrees, FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/*  248 */     if (!CreativeModeTabs.tryRebuildTabContents(enabledFeatures, hasPermissions, holders)) {
/*  249 */       return false;
/*      */     }
/*      */     
/*  252 */     if (searchTrees != null) {
/*  253 */       List<ItemStack> creativeSearchItems = List.copyOf(CreativeModeTabs.searchTab().getDisplayItems());
/*  254 */       searchTrees.updateCreativeTooltips(holders, creativeSearchItems);
/*  255 */       searchTrees.updateCreativeTags(creativeSearchItems);
/*      */     } 
/*      */     
/*  258 */     return true;
/*      */   }
/*      */   
/*      */   private void refreshCurrentTabContents(Collection<ItemStack> displayList) {
/*  262 */     int oldRowIndex = this.menu.getRowIndexForScroll(this.scrollOffs);
/*  263 */     this.menu.items.clear();
/*  264 */     if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
/*  265 */       refreshSearchResults();
/*      */     } else {
/*  267 */       this.menu.items.addAll(displayList);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  272 */     this.scrollOffs = this.menu.getScrollForRowIndex(oldRowIndex);
/*  273 */     this.menu.scrollTo(this.scrollOffs);
/*      */   }
/*      */ 
/*      */   
/*      */   public void containerTick() {
/*  278 */     super.containerTick();
/*      */     
/*  280 */     LocalPlayer player = this.minecraft.player;
/*  281 */     if (player != null) {
/*  282 */       tryRefreshInvalidatedTabs(player.connection.enabledFeatures(), hasPermissions((Player)player), (HolderLookup.Provider)player.level().registryAccess());
/*  283 */       if (!player.hasInfiniteMaterials()) {
/*  284 */         this.minecraft.setScreen(new InventoryScreen((Player)player));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ClickType clickType) {
/*  291 */     if (isCreativeSlot(slot)) {
/*  292 */       this.searchBox.moveCursorToEnd(false);
/*  293 */       this.searchBox.setHighlightPos(0);
/*      */     } 
/*      */     
/*  296 */     boolean quickKey = (clickType == ClickType.QUICK_MOVE);
/*  297 */     clickType = (slotId == -999 && clickType == ClickType.PICKUP) ? ClickType.THROW : clickType;
/*  298 */     if (clickType == ClickType.THROW && !this.minecraft.player.canDropItems()) {
/*      */       return;
/*      */     }
/*  301 */     onMouseClickAction(slot, clickType);
/*      */     
/*  303 */     if (slot != null || selectedTab.getType() == CreativeModeTab.Type.INVENTORY || clickType == ClickType.QUICK_CRAFT) {
/*  304 */       if (slot != null && !slot.mayPickup((Player)this.minecraft.player)) {
/*      */         return;
/*      */       }
/*  307 */       if (slot == this.destroyItemSlot && quickKey) {
/*  308 */         for (int i = 0; i < this.minecraft.player.inventoryMenu.getItems().size(); i++) {
/*  309 */           this.minecraft.player.inventoryMenu.getSlot(i).set(ItemStack.EMPTY);
/*  310 */           this.minecraft.gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, i);
/*      */         } 
/*  312 */       } else if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*      */         
/*  314 */         if (slot == this.destroyItemSlot) {
/*  315 */           this.menu.setCarried(ItemStack.EMPTY);
/*  316 */         } else if (clickType == ClickType.THROW && slot != null && slot.hasItem()) {
/*  317 */           ItemStack toDrop = slot.remove((buttonNum == 0) ? 1 : slot.getItem().getMaxStackSize());
/*  318 */           ItemStack afterDrop = slot.getItem();
/*  319 */           this.minecraft.player.drop(toDrop, true);
/*  320 */           this.minecraft.gameMode.handleCreativeModeItemDrop(toDrop);
/*      */           
/*  322 */           this.minecraft.gameMode.handleCreativeModeItemAdd(afterDrop, ((SlotWrapper)slot).target.index);
/*  323 */         } else if (clickType == ClickType.THROW && slotId == -999 && !this.menu.getCarried().isEmpty()) {
/*  324 */           this.minecraft.player.drop(this.menu.getCarried(), true);
/*  325 */           this.minecraft.gameMode.handleCreativeModeItemDrop(this.menu.getCarried());
/*  326 */           this.menu.setCarried(ItemStack.EMPTY);
/*      */         } else {
/*  328 */           this.minecraft.player.inventoryMenu.clicked((slot == null) ? slotId : ((SlotWrapper)slot).target.index, buttonNum, clickType, (Player)this.minecraft.player);
/*  329 */           this.minecraft.player.inventoryMenu.broadcastChanges();
/*      */         }
/*      */       
/*  332 */       } else if (clickType != ClickType.QUICK_CRAFT && slot.container == CONTAINER) {
/*      */         
/*  334 */         ItemStack carried = this.menu.getCarried();
/*  335 */         ItemStack clicked = slot.getItem();
/*      */         
/*  337 */         if (clickType == ClickType.SWAP) {
/*  338 */           if (!clicked.isEmpty()) {
/*  339 */             this.minecraft.player.getInventory().setItem(buttonNum, clicked.copyWithCount(clicked.getMaxStackSize()));
/*  340 */             this.minecraft.player.inventoryMenu.broadcastChanges();
/*      */           } 
/*      */           return;
/*      */         } 
/*  344 */         if (clickType == ClickType.CLONE) {
/*  345 */           if (this.menu.getCarried().isEmpty() && slot.hasItem()) {
/*  346 */             ItemStack item = slot.getItem();
/*  347 */             this.menu.setCarried(item.copyWithCount(item.getMaxStackSize()));
/*      */           } 
/*      */           return;
/*      */         } 
/*  351 */         if (clickType == ClickType.THROW) {
/*  352 */           if (!clicked.isEmpty()) {
/*  353 */             ItemStack toDrop = clicked.copyWithCount((buttonNum == 0) ? 1 : clicked.getMaxStackSize());
/*  354 */             this.minecraft.player.drop(toDrop, true);
/*  355 */             this.minecraft.gameMode.handleCreativeModeItemDrop(toDrop);
/*      */           } 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  361 */         if (!carried.isEmpty() && !clicked.isEmpty() && ItemStack.isSameItemSameComponents(carried, clicked)) {
/*      */           
/*  363 */           if (buttonNum == 0) {
/*  364 */             if (quickKey) {
/*  365 */               carried.setCount(carried.getMaxStackSize());
/*      */             }
/*  367 */             else if (carried.getCount() < carried.getMaxStackSize()) {
/*  368 */               carried.grow(1);
/*      */             } 
/*      */           } else {
/*      */             
/*  372 */             carried.shrink(1);
/*      */           } 
/*  374 */         } else if (clicked.isEmpty() || !carried.isEmpty()) {
/*      */           
/*  376 */           if (buttonNum == 0) {
/*  377 */             this.menu.setCarried(ItemStack.EMPTY);
/*  378 */           } else if (!this.menu.getCarried().isEmpty()) {
/*  379 */             this.menu.getCarried().shrink(1);
/*      */           } 
/*      */         } else {
/*      */           
/*  383 */           int count = quickKey ? clicked.getMaxStackSize() : clicked.getCount();
/*  384 */           this.menu.setCarried(clicked.copyWithCount(count));
/*      */         } 
/*  386 */       } else if (this.menu != null) {
/*  387 */         ItemStack oldItemStack = (slot == null) ? ItemStack.EMPTY : this.menu.getSlot(slot.index).getItem();
/*  388 */         this.menu.clicked((slot == null) ? slotId : slot.index, buttonNum, clickType, (Player)this.minecraft.player);
/*      */         
/*  390 */         if (AbstractContainerMenu.getQuickcraftHeader(buttonNum) == 2) {
/*  391 */           for (int i = 0; i < 9; i++) {
/*  392 */             this.minecraft.gameMode.handleCreativeModeItemAdd(this.menu.getSlot(45 + i).getItem(), 36 + i);
/*      */           }
/*  394 */         } else if (slot != null && Inventory.isHotbarSlot(slot.getContainerSlot()) && selectedTab.getType() != CreativeModeTab.Type.INVENTORY) {
/*  395 */           if (clickType == ClickType.THROW && !oldItemStack.isEmpty() && !this.menu.getCarried().isEmpty()) {
/*      */ 
/*      */ 
/*      */             
/*  399 */             int numToDrop = (buttonNum == 0) ? 1 : oldItemStack.getCount();
/*  400 */             ItemStack toDrop = oldItemStack.copyWithCount(numToDrop);
/*  401 */             oldItemStack.shrink(numToDrop);
/*  402 */             this.minecraft.player.drop(toDrop, true);
/*  403 */             this.minecraft.gameMode.handleCreativeModeItemDrop(toDrop);
/*      */           } 
/*      */           
/*  406 */           this.minecraft.player.inventoryMenu.broadcastChanges();
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  411 */     } else if (!this.menu.getCarried().isEmpty() && this.hasClickedOutside) {
/*  412 */       if (!this.minecraft.player.canDropItems()) {
/*      */         return;
/*      */       }
/*  415 */       if (buttonNum == 0) {
/*  416 */         this.minecraft.player.drop(this.menu.getCarried(), true);
/*  417 */         this.minecraft.gameMode.handleCreativeModeItemDrop(this.menu.getCarried());
/*  418 */         this.menu.setCarried(ItemStack.EMPTY);
/*      */       } 
/*  420 */       if (buttonNum == 1) {
/*  421 */         ItemStack removedItem = this.menu.getCarried().split(1);
/*  422 */         this.minecraft.player.drop(removedItem, true);
/*  423 */         this.minecraft.gameMode.handleCreativeModeItemDrop(removedItem);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isCreativeSlot(Slot slot) {
/*  430 */     return (slot != null && slot.container == CONTAINER);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void init() {
/*  435 */     if (this.minecraft.player.hasInfiniteMaterials()) {
/*  436 */       super.init();
/*      */       
/*  438 */       Objects.requireNonNull(this.font); this.searchBox = new EditBox(this.font, this.leftPos + 82, this.topPos + 6, 80, 9, (Component)Component.translatable("itemGroup.search"));
/*  439 */       this.searchBox.setMaxLength(50);
/*  440 */       this.searchBox.setBordered(false);
/*  441 */       this.searchBox.setVisible(false);
/*  442 */       this.searchBox.setTextColor(-1);
/*  443 */       this.searchBox.setInvertHighlightedTextColor(false);
/*  444 */       addWidget((GuiEventListener)this.searchBox);
/*      */       
/*  446 */       CreativeModeTab tab = selectedTab;
/*  447 */       selectedTab = CreativeModeTabs.getDefaultTab();
/*  448 */       selectTab(tab);
/*      */       
/*  450 */       this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
/*  451 */       this.listener = new CreativeInventoryListener(this.minecraft);
/*  452 */       this.minecraft.player.inventoryMenu.addSlotListener(this.listener);
/*  453 */       if (!selectedTab.shouldDisplay()) {
/*  454 */         selectTab(CreativeModeTabs.getDefaultTab());
/*      */       }
/*      */     } else {
/*  457 */       this.minecraft.setScreen(new InventoryScreen((Player)this.minecraft.player));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void resize(int width, int height) {
/*  463 */     int oldRowIndex = this.menu.getRowIndexForScroll(this.scrollOffs);
/*      */     
/*  465 */     String oldEdit = this.searchBox.getValue();
/*  466 */     init(width, height);
/*  467 */     this.searchBox.setValue(oldEdit);
/*      */     
/*  469 */     if (!this.searchBox.getValue().isEmpty()) {
/*  470 */       refreshSearchResults();
/*      */     }
/*      */     
/*  473 */     this.scrollOffs = this.menu.getScrollForRowIndex(oldRowIndex);
/*  474 */     this.menu.scrollTo(this.scrollOffs);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removed() {
/*  479 */     super.removed();
/*      */     
/*  481 */     if (this.minecraft.player != null && this.minecraft.player.getInventory() != null) {
/*  482 */       this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean charTyped(CharacterEvent event) {
/*  488 */     if (this.ignoreTextInput) {
/*  489 */       return false;
/*      */     }
/*  491 */     if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
/*  492 */       return false;
/*      */     }
/*  494 */     String oldContents = this.searchBox.getValue();
/*  495 */     if (this.searchBox.charTyped(event)) {
/*  496 */       if (!Objects.equals(oldContents, this.searchBox.getValue())) {
/*  497 */         refreshSearchResults();
/*      */       }
/*  499 */       return true;
/*      */     } 
/*  501 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean keyPressed(KeyEvent event) {
/*  506 */     this.ignoreTextInput = false;
/*  507 */     if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
/*  508 */       if (this.minecraft.options.keyChat.matches(event)) {
/*  509 */         this.ignoreTextInput = true;
/*  510 */         selectTab(CreativeModeTabs.searchTab());
/*  511 */         return true;
/*      */       } 
/*  513 */       return super.keyPressed(event);
/*      */     } 
/*      */     
/*  516 */     boolean doQuickSwap = (!isCreativeSlot(this.hoveredSlot) || this.hoveredSlot.hasItem());
/*  517 */     boolean pressingNumber = InputConstants.getKey(event).getNumericKeyValue().isPresent();
/*  518 */     if (doQuickSwap && pressingNumber && checkHotbarKeyPressed(event)) {
/*      */ 
/*      */       
/*  521 */       this.ignoreTextInput = true;
/*  522 */       return true;
/*      */     } 
/*      */     
/*  525 */     String oldContents = this.searchBox.getValue();
/*  526 */     if (this.searchBox.keyPressed(event)) {
/*  527 */       if (!Objects.equals(oldContents, this.searchBox.getValue())) {
/*  528 */         refreshSearchResults();
/*      */       }
/*      */       
/*  531 */       return true;
/*      */     } 
/*  533 */     if (this.searchBox.isFocused() && this.searchBox.isVisible() && !event.isEscape())
/*      */     {
/*  535 */       return true;
/*      */     }
/*  537 */     return super.keyPressed(event);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean keyReleased(KeyEvent event) {
/*  542 */     this.ignoreTextInput = false;
/*  543 */     return super.keyReleased(event);
/*      */   }
/*      */   
/*      */   private void refreshSearchResults() {
/*  547 */     this.menu.items.clear();
/*  548 */     this.visibleTags.clear();
/*      */     
/*  550 */     String searchTerm = this.searchBox.getValue();
/*  551 */     if (searchTerm.isEmpty()) {
/*  552 */       this.menu.items.addAll(selectedTab.getDisplayItems());
/*      */     } else {
/*  554 */       ClientPacketListener connection = this.minecraft.getConnection();
/*  555 */       if (connection != null) {
/*      */         SearchTree<ItemStack> tree;
/*  557 */         SessionSearchTrees searchTrees = connection.searchTrees();
/*  558 */         if (searchTerm.startsWith("#")) {
/*  559 */           searchTerm = searchTerm.substring(1);
/*  560 */           tree = searchTrees.creativeTagSearch();
/*  561 */           updateVisibleTags(searchTerm);
/*      */         } else {
/*  563 */           tree = searchTrees.creativeNameSearch();
/*      */         } 
/*  565 */         this.menu.items.addAll(tree.search(searchTerm.toLowerCase(Locale.ROOT)));
/*      */       } 
/*      */     } 
/*      */     
/*  569 */     this.scrollOffs = 0.0F;
/*  570 */     this.menu.scrollTo(0.0F);
/*      */   }
/*      */   private void updateVisibleTags(String searchTerm) {
/*      */     Predicate<Identifier> matcher;
/*  574 */     int colonIndex = searchTerm.indexOf(':');
/*      */ 
/*      */     
/*  577 */     if (colonIndex == -1) {
/*  578 */       matcher = (id -> id.getPath().contains(searchTerm));
/*      */     } else {
/*  580 */       String nsMatcher = searchTerm.substring(0, colonIndex).trim();
/*  581 */       String pathMatcher = searchTerm.substring(colonIndex + 1).trim();
/*  582 */       matcher = (id -> (id.getNamespace().contains(nsMatcher) && id.getPath().contains(pathMatcher)));
/*      */     } 
/*      */     
/*  585 */     Objects.requireNonNull(this.visibleTags); BuiltInRegistries.ITEM.getTags().map(HolderSet.Named::key).filter(tag -> matcher.test(tag.location())).forEach(this.visibleTags::add);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
/*  590 */     if (selectedTab.showTitle()) {
/*  591 */       graphics.drawString(this.font, selectedTab.getDisplayName(), 8, 6, -12566464, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  597 */     if (event.button() == 0) {
/*  598 */       double xm = event.x() - this.leftPos;
/*  599 */       double ym = event.y() - this.topPos;
/*      */       
/*  601 */       for (CreativeModeTab tab : (Iterable<CreativeModeTab>)CreativeModeTabs.tabs()) {
/*  602 */         if (checkTabClicked(tab, xm, ym)) {
/*  603 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  607 */       if (selectedTab.getType() != CreativeModeTab.Type.INVENTORY && insideScrollbar(event.x(), event.y())) {
/*  608 */         this.scrolling = canScroll();
/*  609 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  613 */     return super.mouseClicked(event, doubleClick);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mouseReleased(MouseButtonEvent event) {
/*  618 */     if (event.button() == 0) {
/*  619 */       double xm = event.x() - this.leftPos;
/*  620 */       double ym = event.y() - this.topPos;
/*      */       
/*  622 */       this.scrolling = false;
/*      */       
/*  624 */       for (CreativeModeTab tab : (Iterable<CreativeModeTab>)CreativeModeTabs.tabs()) {
/*  625 */         if (checkTabClicked(tab, xm, ym)) {
/*  626 */           selectTab(tab);
/*  627 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  632 */     return super.mouseReleased(event);
/*      */   }
/*      */   
/*      */   private boolean canScroll() {
/*  636 */     return (selectedTab.canScroll() && this.menu.canScroll());
/*      */   }
/*      */   
/*      */   private void selectTab(CreativeModeTab tab) {
/*  640 */     CreativeModeTab oldTab = selectedTab;
/*  641 */     selectedTab = tab;
/*      */     
/*  643 */     this.quickCraftSlots.clear();
/*  644 */     this.menu.items.clear();
/*  645 */     clearDraggingState();
/*  646 */     if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR) {
/*  647 */       HotbarManager manager = this.minecraft.getHotbarManager();
/*  648 */       for (int hotbarIndex = 0; hotbarIndex < 9; hotbarIndex++) {
/*  649 */         Hotbar hotbar = manager.get(hotbarIndex);
/*  650 */         if (hotbar.isEmpty()) {
/*  651 */           for (int i = 0; i < 9; i++) {
/*  652 */             if (i == hotbarIndex) {
/*  653 */               ItemStack placeholder = new ItemStack((net.minecraft.world.level.ItemLike)Items.PAPER);
/*  654 */               placeholder.set(DataComponents.CREATIVE_SLOT_LOCK, Unit.INSTANCE);
/*      */               
/*  656 */               Component translatedKeyMessage = this.minecraft.options.keyHotbarSlots[hotbarIndex].getTranslatedKeyMessage();
/*  657 */               Component activatorKeyMessage = this.minecraft.options.keySaveHotbarActivator.getTranslatedKeyMessage();
/*  658 */               placeholder.set(DataComponents.ITEM_NAME, Component.translatable("inventory.hotbarInfo", new Object[] { activatorKeyMessage, translatedKeyMessage }));
/*  659 */               this.menu.items.add(placeholder);
/*      */             } else {
/*  661 */               this.menu.items.add(ItemStack.EMPTY);
/*      */             } 
/*      */           } 
/*      */         } else {
/*  665 */           this.menu.items.addAll(hotbar.load((HolderLookup.Provider)this.minecraft.level.registryAccess()));
/*      */         } 
/*      */       } 
/*  668 */     } else if (selectedTab.getType() == CreativeModeTab.Type.CATEGORY) {
/*  669 */       this.menu.items.addAll(selectedTab.getDisplayItems());
/*      */     } 
/*      */     
/*  672 */     if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*  673 */       InventoryMenu inventoryMenu = this.minecraft.player.inventoryMenu;
/*      */       
/*  675 */       if (this.originalSlots == null) {
/*  676 */         this.originalSlots = (List<Slot>)ImmutableList.copyOf((Collection)this.menu.slots);
/*      */       }
/*  678 */       this.menu.slots.clear();
/*  679 */       for (int i = 0; i < ((AbstractContainerMenu)inventoryMenu).slots.size(); i++) {
/*      */         int x, y;
/*      */         
/*  682 */         if (i >= 5 && i < 9) {
/*  683 */           int pos = i - 5;
/*  684 */           int col = pos / 2;
/*  685 */           int row = pos % 2;
/*      */           
/*  687 */           x = 54 + col * 54;
/*  688 */           y = 6 + row * 27;
/*  689 */         } else if (i >= 0 && i < 5) {
/*  690 */           x = -2000;
/*  691 */           y = -2000;
/*  692 */         } else if (i == 45) {
/*  693 */           x = 35;
/*  694 */           y = 20;
/*      */         } else {
/*  696 */           int pos = i - 9;
/*  697 */           int col = pos % 9;
/*  698 */           int row = pos / 9;
/*      */           
/*  700 */           x = 9 + col * 18;
/*      */           
/*  702 */           if (i >= 36) {
/*  703 */             y = 112;
/*      */           } else {
/*  705 */             y = 54 + row * 18;
/*      */           } 
/*      */         } 
/*      */         
/*  709 */         Slot slot = new SlotWrapper((Slot)((AbstractContainerMenu)inventoryMenu).slots.get(i), i, x, y);
/*  710 */         this.menu.slots.add(slot);
/*      */       } 
/*      */       
/*  713 */       this.destroyItemSlot = new Slot((Container)CONTAINER, 0, 173, 112);
/*  714 */       this.menu.slots.add(this.destroyItemSlot);
/*  715 */     } else if (oldTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*  716 */       this.menu.slots.clear();
/*  717 */       this.menu.slots.addAll(this.originalSlots);
/*  718 */       this.originalSlots = null;
/*      */     } 
/*      */     
/*  721 */     if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
/*  722 */       this.searchBox.setVisible(true);
/*  723 */       this.searchBox.setCanLoseFocus(false);
/*  724 */       this.searchBox.setFocused(true);
/*  725 */       if (oldTab != tab) {
/*  726 */         this.searchBox.setValue("");
/*      */       }
/*  728 */       refreshSearchResults();
/*      */     } else {
/*  730 */       this.searchBox.setVisible(false);
/*  731 */       this.searchBox.setCanLoseFocus(true);
/*  732 */       this.searchBox.setFocused(false);
/*  733 */       this.searchBox.setValue("");
/*      */     } 
/*      */     
/*  736 */     this.scrollOffs = 0.0F;
/*  737 */     this.menu.scrollTo(0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/*  742 */     if (super.mouseScrolled(x, y, scrollX, scrollY)) {
/*  743 */       return true;
/*      */     }
/*      */     
/*  746 */     if (!canScroll()) {
/*  747 */       return false;
/*      */     }
/*  749 */     this.scrollOffs = this.menu.subtractInputFromScroll(this.scrollOffs, scrollY);
/*  750 */     this.menu.scrollTo(this.scrollOffs);
/*  751 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/*  756 */     boolean clickedOutside = (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/*  757 */     this.hasClickedOutside = (clickedOutside && !checkTabClicked(selectedTab, mx, my));
/*  758 */     return this.hasClickedOutside;
/*      */   }
/*      */   
/*      */   protected boolean insideScrollbar(double xm, double ym) {
/*  762 */     int xo = this.leftPos;
/*  763 */     int yo = this.topPos;
/*      */     
/*  765 */     int xscr = xo + 175;
/*  766 */     int yscr = yo + 18;
/*  767 */     int xscr2 = xscr + 14;
/*  768 */     int yscr2 = yscr + 112;
/*  769 */     return (xm >= xscr && ym >= yscr && xm < xscr2 && ym < yscr2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/*  775 */     if (this.scrolling) {
/*  776 */       int yscr = this.topPos + 18;
/*  777 */       int yscr2 = yscr + 112;
/*      */       
/*  779 */       this.scrollOffs = ((float)event.y() - yscr - 7.5F) / ((yscr2 - yscr) - 15.0F);
/*  780 */       this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
/*  781 */       this.menu.scrollTo(this.scrollOffs);
/*      */       
/*  783 */       return true;
/*      */     } 
/*  785 */     return super.mouseDragged(event, dx, dy);
/*      */   }
/*      */ 
/*      */   
/*      */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  790 */     this.effects.render(graphics, mouseX, mouseY);
/*  791 */     super.render(graphics, mouseX, mouseY, a);
/*  792 */     for (CreativeModeTab tab : (Iterable<CreativeModeTab>)CreativeModeTabs.tabs()) {
/*  793 */       if (checkTabHovering(graphics, tab, mouseX, mouseY)) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/*  798 */     if (this.destroyItemSlot != null && selectedTab.getType() == CreativeModeTab.Type.INVENTORY && isHovering(this.destroyItemSlot.x, this.destroyItemSlot.y, 16, 16, mouseX, mouseY)) {
/*  799 */       graphics.setTooltipForNextFrame(this.font, TRASH_SLOT_TOOLTIP, mouseX, mouseY);
/*      */     }
/*      */     
/*  802 */     renderTooltip(graphics, mouseX, mouseY);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean showsActiveEffects() {
/*  807 */     return this.effects.canSeeEffects();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Component> getTooltipFromContainerItem(ItemStack itemStack) {
/*  812 */     boolean isCreativeSlot = (this.hoveredSlot != null && this.hoveredSlot instanceof CustomCreativeSlot);
/*  813 */     boolean isSingleCategoryTab = (selectedTab.getType() == CreativeModeTab.Type.CATEGORY);
/*  814 */     boolean isSearchTab = (selectedTab.getType() == CreativeModeTab.Type.SEARCH);
/*      */     
/*  816 */     TooltipFlag.Default originalTooltipStyle = this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
/*  817 */     TooltipFlag.Default default_1 = isCreativeSlot ? originalTooltipStyle.asCreative() : originalTooltipStyle;
/*  818 */     List<Component> originalLines = itemStack.getTooltipLines(Item.TooltipContext.of((Level)this.minecraft.level), (Player)this.minecraft.player, (TooltipFlag)default_1);
/*  819 */     if (originalLines.isEmpty()) {
/*  820 */       return originalLines;
/*      */     }
/*      */     
/*  823 */     if (!isSingleCategoryTab || !isCreativeSlot) {
/*  824 */       List<Component> linesToDisplay = Lists.newArrayList(originalLines);
/*      */       
/*  826 */       if (isSearchTab && isCreativeSlot) {
/*  827 */         this.visibleTags.forEach(tag -> {
/*      */               if (itemStack.is(tag)) {
/*      */                 linesToDisplay.add(1, Component.literal("#" + String.valueOf(tag.location())).withStyle(ChatFormatting.DARK_PURPLE));
/*      */               }
/*      */             });
/*      */       }
/*      */       
/*  834 */       int i = 1;
/*  835 */       for (CreativeModeTab tab : (Iterable<CreativeModeTab>)CreativeModeTabs.tabs()) {
/*  836 */         if (tab.getType() != CreativeModeTab.Type.SEARCH && tab.contains(itemStack)) {
/*  837 */           linesToDisplay.add(i++, tab.getDisplayName().copy().withStyle(ChatFormatting.BLUE));
/*      */         }
/*      */       } 
/*      */       
/*  841 */       return linesToDisplay;
/*      */     } 
/*      */     
/*  844 */     return originalLines;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/*  849 */     for (CreativeModeTab tab : (Iterable<CreativeModeTab>)CreativeModeTabs.tabs()) {
/*  850 */       if (tab != selectedTab) {
/*  851 */         renderTabButton(graphics, xm, ym, tab);
/*      */       }
/*      */     } 
/*      */     
/*  855 */     graphics.blit(RenderPipelines.GUI_TEXTURED, selectedTab.getBackgroundTexture(), this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*      */     
/*  857 */     if (insideScrollbar(xm, ym) && canScroll()) {
/*  858 */       graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
/*      */     }
/*      */     
/*  861 */     this.searchBox.render(graphics, xm, ym, a);
/*      */     
/*  863 */     int xscr = this.leftPos + 175;
/*  864 */     int yscr = this.topPos + 18;
/*  865 */     int yscr2 = yscr + 112;
/*      */     
/*  867 */     if (selectedTab.canScroll()) {
/*  868 */       Identifier sprite = canScroll() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
/*  869 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xscr, yscr + (int)((yscr2 - yscr - 17) * this.scrollOffs), 12, 15);
/*      */     } 
/*      */     
/*  872 */     renderTabButton(graphics, xm, ym, selectedTab);
/*      */     
/*  874 */     if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*  875 */       InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, this.leftPos + 73, this.topPos + 6, this.leftPos + 105, this.topPos + 49, 20, 0.0625F, xm, ym, (LivingEntity)this.minecraft.player);
/*      */     }
/*      */   }
/*      */   
/*      */   private int getTabX(CreativeModeTab tab) {
/*  880 */     int pos = tab.column();
/*  881 */     int spacing = 27;
/*  882 */     int x = 27 * pos;
/*      */     
/*  884 */     if (tab.isAlignedRight()) {
/*  885 */       x = this.imageWidth - 27 * (7 - pos) + 1;
/*      */     }
/*  887 */     return x;
/*      */   }
/*      */   
/*      */   private int getTabY(CreativeModeTab tab) {
/*  891 */     int y = 0;
/*  892 */     if (tab.row() == CreativeModeTab.Row.TOP) {
/*  893 */       y -= 32;
/*      */     } else {
/*  895 */       y += this.imageHeight;
/*      */     } 
/*  897 */     return y;
/*      */   }
/*      */   
/*      */   protected boolean checkTabClicked(CreativeModeTab tab, double xm, double ym) {
/*  901 */     int x = getTabX(tab);
/*  902 */     int y = getTabY(tab);
/*  903 */     return (xm >= x && xm <= (x + 26) && ym >= y && ym <= (y + 32));
/*      */   }
/*      */   
/*      */   protected boolean checkTabHovering(GuiGraphics graphics, CreativeModeTab tab, int xm, int ym) {
/*  907 */     int x = getTabX(tab);
/*  908 */     int y = getTabY(tab);
/*  909 */     if (isHovering(x + 3, y + 3, 21, 27, xm, ym)) {
/*  910 */       graphics.setTooltipForNextFrame(this.font, tab.getDisplayName(), xm, ym);
/*  911 */       return true;
/*      */     } 
/*      */     
/*  914 */     return false;
/*      */   }
/*      */   protected void renderTabButton(GuiGraphics graphics, int mouseX, int mouseY, CreativeModeTab tab) {
/*      */     Identifier[] sprites;
/*  918 */     boolean selected = (tab == selectedTab);
/*  919 */     boolean isTop = (tab.row() == CreativeModeTab.Row.TOP);
/*  920 */     int pos = tab.column();
/*  921 */     int x = this.leftPos + getTabX(tab);
/*  922 */     int y = this.topPos - (isTop ? 28 : -(this.imageHeight - 4));
/*      */ 
/*      */     
/*  925 */     if (isTop) {
/*  926 */       sprites = selected ? SELECTED_TOP_TABS : UNSELECTED_TOP_TABS;
/*      */     } else {
/*  928 */       sprites = selected ? SELECTED_BOTTOM_TABS : UNSELECTED_BOTTOM_TABS;
/*      */     } 
/*      */     
/*  931 */     if (!selected && mouseX > x && mouseY > y && mouseX < x + 26 && mouseY < y + 32) {
/*  932 */       graphics.requestCursor(CursorTypes.POINTING_HAND);
/*      */     }
/*      */     
/*  935 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprites[Mth.clamp(pos, 0, sprites.length)], x, y, 26, 32);
/*      */     
/*  937 */     int iconX = x + 13 - 8;
/*  938 */     int iconY = y + 16 - 8 + (isTop ? 1 : -1);
/*  939 */     graphics.renderItem(tab.getIconItem(), iconX, iconY);
/*      */   }
/*      */   
/*      */   public boolean isInventoryOpen() {
/*  943 */     return (selectedTab.getType() == CreativeModeTab.Type.INVENTORY);
/*      */   }
/*      */   
/*      */   private static class SlotWrapper
/*      */     extends Slot {
/*      */     private final Slot target;
/*      */     
/*      */     public SlotWrapper(Slot target, int index, int x, int y) {
/*  951 */       super(target.container, index, x, y);
/*  952 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     public void onTake(Player player, ItemStack carried) {
/*  957 */       this.target.onTake(player, carried);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mayPlace(ItemStack itemStack) {
/*  962 */       return this.target.mayPlace(itemStack);
/*      */     }
/*      */ 
/*      */     
/*      */     public ItemStack getItem() {
/*  967 */       return this.target.getItem();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasItem() {
/*  972 */       return this.target.hasItem();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/*  977 */       this.target.setByPlayer(itemStack, previous);
/*      */     }
/*      */ 
/*      */     
/*      */     public void set(ItemStack itemStack) {
/*  982 */       this.target.set(itemStack);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setChanged() {
/*  987 */       this.target.setChanged();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getMaxStackSize() {
/*  992 */       return this.target.getMaxStackSize();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getMaxStackSize(ItemStack itemStack) {
/*  997 */       return this.target.getMaxStackSize(itemStack);
/*      */     }
/*      */ 
/*      */     
/*      */     public Identifier getNoItemIcon() {
/* 1002 */       return this.target.getNoItemIcon();
/*      */     }
/*      */ 
/*      */     
/*      */     public ItemStack remove(int amount) {
/* 1007 */       return this.target.remove(amount);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1012 */       return this.target.isActive();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mayPickup(Player player) {
/* 1017 */       return this.target.mayPickup(player);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class CustomCreativeSlot extends Slot {
/*      */     public CustomCreativeSlot(Container container, int slot, int x, int y) {
/* 1023 */       super(container, slot, x, y);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mayPickup(Player player) {
/* 1028 */       ItemStack item = getItem();
/* 1029 */       if (super.mayPickup(player) && !item.isEmpty()) {
/* 1030 */         return (item.isItemEnabled(player.level().enabledFeatures()) && 
/* 1031 */           !item.has(DataComponents.CREATIVE_SLOT_LOCK));
/*      */       }
/* 1033 */       return item.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void handleHotbarLoadOrSave(Minecraft minecraft, int index, boolean isLoadPressed, boolean isSavePressed) {
/* 1038 */     LocalPlayer player = minecraft.player;
/* 1039 */     RegistryAccess registries = player.level().registryAccess();
/* 1040 */     HotbarManager manager = minecraft.getHotbarManager();
/* 1041 */     Hotbar hotbar = manager.get(index);
/*      */     
/* 1043 */     if (isLoadPressed) {
/* 1044 */       List<ItemStack> originalItems = hotbar.load((HolderLookup.Provider)registries);
/* 1045 */       for (int i = 0; i < Inventory.getSelectionSize(); i++) {
/* 1046 */         ItemStack itemStack = originalItems.get(i);
/* 1047 */         player.getInventory().setItem(i, itemStack);
/* 1048 */         minecraft.gameMode.handleCreativeModeItemAdd(itemStack, 36 + i);
/*      */       } 
/* 1050 */       player.inventoryMenu.broadcastChanges();
/* 1051 */     } else if (isSavePressed) {
/* 1052 */       hotbar.storeFrom(player.getInventory(), registries);
/* 1053 */       Component translatedKeyMessage = minecraft.options.keyHotbarSlots[index].getTranslatedKeyMessage();
/* 1054 */       Component activatorKeyMessage = minecraft.options.keyLoadHotbarActivator.getTranslatedKeyMessage();
/* 1055 */       MutableComponent mutableComponent = Component.translatable("inventory.hotbarSaved", new Object[] { activatorKeyMessage, translatedKeyMessage });
/* 1056 */       minecraft.gui.setOverlayMessage((Component)mutableComponent, false);
/* 1057 */       minecraft.getNarrator().saySystemNow((Component)mutableComponent);
/*      */       
/* 1059 */       manager.save();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */