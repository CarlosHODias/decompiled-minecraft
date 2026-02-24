/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenAxis;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.language.LanguageInfo;
/*     */ import net.minecraft.client.resources.language.LanguageManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.RecipeBookType;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class RecipeBookComponent<T extends RecipeBookMenu>
/*     */   implements GuiEventListener, Renderable, NarratableEntry {
/*  57 */   public static final WidgetSprites RECIPE_BUTTON_SPRITES = new WidgetSprites(
/*  58 */       Identifier.withDefaultNamespace("recipe_book/button"), 
/*  59 */       Identifier.withDefaultNamespace("recipe_book/button_highlighted"));
/*     */ 
/*     */   
/*  62 */   protected static final Identifier RECIPE_BOOK_LOCATION = Identifier.withDefaultNamespace("textures/gui/recipe_book.png");
/*     */   
/*     */   private static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*  66 */   private static final Component SEARCH_HINT = (Component)Component.translatable("gui.recipebook.search_hint").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   public static final int IMAGE_WIDTH = 147;
/*     */   public static final int IMAGE_HEIGHT = 166;
/*     */   private static final int OFFSET_X_POSITION = 86;
/*     */   private static final int BORDER_WIDTH = 8;
/*  72 */   private static final Component ALL_RECIPES_TOOLTIP = (Component)Component.translatable("gui.recipebook.toggleRecipes.all");
/*     */   
/*     */   private static final int TICKS_TO_SWAP_SLOT = 30;
/*     */   
/*     */   private int xOffset;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/*     */   private float time;
/*     */   private RecipeDisplayId lastPlacedRecipe;
/*     */   private final GhostSlots ghostSlots;
/*  85 */   private final List<RecipeBookTabButton> tabButtons = Lists.newArrayList();
/*     */   
/*     */   private RecipeBookTabButton selectedTab;
/*     */   
/*     */   protected CycleButton<Boolean> filterButton;
/*     */   protected final T menu;
/*     */   protected Minecraft minecraft;
/*     */   private EditBox searchBox;
/*  93 */   private String lastSearch = "";
/*     */   
/*     */   private final List<TabInfo> tabInfos;
/*     */   
/*     */   private ClientRecipeBook book;
/*     */   
/*     */   private final RecipeBookPage recipeBookPage;
/*     */   private RecipeDisplayId lastRecipe;
/*     */   private RecipeCollection lastRecipeCollection;
/* 102 */   private final StackedItemContents stackedContents = new StackedItemContents();
/*     */   
/*     */   private int timesInventoryChanged;
/*     */   
/*     */   private boolean ignoreTextInput;
/*     */   
/*     */   private boolean visible;
/*     */   
/*     */   private boolean widthTooNarrow;
/*     */   private ScreenRectangle magnifierIconPlacement;
/*     */   
/*     */   public RecipeBookComponent(T menu, List<TabInfo> tabInfos) {
/* 114 */     this.menu = menu;
/* 115 */     this.tabInfos = tabInfos;
/*     */     
/*     */     SlotSelectTime slotSelectTime = () -> Mth.floor(this.time / 30.0F);
/* 118 */     this.ghostSlots = new GhostSlots(slotSelectTime);
/* 119 */     this.recipeBookPage = new RecipeBookPage(this, slotSelectTime, menu instanceof net.minecraft.world.inventory.AbstractFurnaceMenu);
/*     */   }
/*     */   
/*     */   public void init(int width, int height, Minecraft minecraft, boolean widthTooNarrow) {
/* 123 */     this.minecraft = minecraft;
/* 124 */     this.width = width;
/* 125 */     this.height = height;
/* 126 */     this.widthTooNarrow = widthTooNarrow;
/* 127 */     this.book = minecraft.player.getRecipeBook();
/*     */     
/* 129 */     this.timesInventoryChanged = minecraft.player.getInventory().getTimesChanged();
/*     */     
/* 131 */     this.visible = isVisibleAccordingToBookData();
/* 132 */     if (this.visible) {
/* 133 */       initVisuals();
/*     */     }
/*     */   }
/*     */   
/*     */   private void initVisuals() {
/* 138 */     boolean isFiltering = isFiltering();
/*     */     
/* 140 */     this.xOffset = this.widthTooNarrow ? 0 : 86;
/* 141 */     int xo = getXOrigin();
/* 142 */     int yo = getYOrigin();
/*     */     
/* 144 */     this.stackedContents.clear();
/* 145 */     this.minecraft.player.getInventory().fillStackedContents(this.stackedContents);
/* 146 */     this.menu.fillCraftSlotsStackedContents(this.stackedContents);
/*     */     
/* 148 */     String oldEdit = (this.searchBox != null) ? this.searchBox.getValue() : "";
/* 149 */     Objects.requireNonNull(this.minecraft.font); this.searchBox = new EditBox(this.minecraft.font, xo + 25, yo + 13, 81, 9 + 5, (Component)Component.translatable("itemGroup.search"));
/* 150 */     this.searchBox.setMaxLength(50);
/* 151 */     this.searchBox.setVisible(true);
/* 152 */     this.searchBox.setTextColor(-1);
/* 153 */     this.searchBox.setValue(oldEdit);
/* 154 */     this.searchBox.setHint(SEARCH_HINT);
/*     */ 
/*     */     
/* 157 */     this.magnifierIconPlacement = ScreenRectangle.of(ScreenAxis.HORIZONTAL, xo + 8, 
/*     */ 
/*     */         
/* 160 */         this.searchBox.getY(), 
/* 161 */         this.searchBox.getX() - getXOrigin(), 
/* 162 */         this.searchBox.getHeight());
/*     */ 
/*     */     
/* 165 */     this.recipeBookPage.init(this.minecraft, xo, yo);
/*     */     
/* 167 */     this
/*     */ 
/*     */ 
/*     */       
/* 171 */       .filterButton = CycleButton.booleanBuilder(getRecipeFilterName(), ALL_RECIPES_TOOLTIP, isFiltering).withTooltip(filtering -> filtering ? Tooltip.create(getRecipeFilterName()) : Tooltip.create(ALL_RECIPES_TOOLTIP)).withSprite((cycleButton, filtering) -> getFilterButtonTextures().get(filtering, cycleButton.isHoveredOrFocused())).displayState(CycleButton.DisplayState.HIDE).create(xo + 110, yo + 12, 26, 16, CommonComponents.EMPTY, (button, value) -> {
/*     */           toggleFiltering();
/*     */           
/*     */           sendUpdateSettings();
/*     */           updateCollections(false, value);
/*     */         });
/* 177 */     this.tabButtons.clear();
/*     */     
/* 179 */     for (TabInfo tabInfo : this.tabInfos) {
/* 180 */       this.tabButtons.add(new RecipeBookTabButton(0, 0, tabInfo, this::onTabButtonPress));
/*     */     }
/*     */     
/* 183 */     if (this.selectedTab != null) {
/* 184 */       this.selectedTab = this.tabButtons.stream().filter(o -> o.getCategory().equals(this.selectedTab.getCategory())).findFirst().orElse(null);
/*     */     }
/* 186 */     if (this.selectedTab == null) {
/* 187 */       this.selectedTab = this.tabButtons.get(0);
/*     */     }
/* 189 */     this.selectedTab.select();
/*     */     
/* 191 */     selectMatchingRecipes();
/* 192 */     updateTabs(isFiltering);
/* 193 */     updateCollections(false, isFiltering);
/*     */   }
/*     */   
/*     */   private int getYOrigin() {
/* 197 */     return (this.height - 166) / 2;
/*     */   }
/*     */   
/*     */   private int getXOrigin() {
/* 201 */     return (this.width - 147) / 2 - this.xOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int updateScreenPosition(int width, int imageWidth) {
/*     */     int leftPos;
/* 208 */     if (isVisible() && !this.widthTooNarrow) {
/* 209 */       leftPos = 177 + (width - imageWidth - 200) / 2;
/*     */     } else {
/* 211 */       leftPos = (width - imageWidth) / 2;
/*     */     } 
/*     */     
/* 214 */     return leftPos;
/*     */   }
/*     */   
/*     */   public void toggleVisibility() {
/* 218 */     setVisible(!isVisible());
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 222 */     return this.visible;
/*     */   }
/*     */   
/*     */   private boolean isVisibleAccordingToBookData() {
/* 226 */     return this.book.isOpen(this.menu.getRecipeBookType());
/*     */   }
/*     */   
/*     */   protected void setVisible(boolean visible) {
/* 230 */     if (visible) {
/* 231 */       initVisuals();
/*     */     }
/*     */     
/* 234 */     this.visible = visible;
/* 235 */     this.book.setOpen(this.menu.getRecipeBookType(), visible);
/* 236 */     if (!visible) {
/* 237 */       this.recipeBookPage.setInvisible();
/*     */     }
/* 239 */     sendUpdateSettings();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotClicked(Slot slot) {
/* 245 */     if (slot != null && isCraftingSlot(slot)) {
/* 246 */       this.lastPlacedRecipe = null;
/* 247 */       this.ghostSlots.clear();
/*     */       
/* 249 */       if (isVisible()) {
/* 250 */         updateStackedContents();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void selectMatchingRecipes() {
/* 257 */     for (TabInfo tabInfo : this.tabInfos) {
/* 258 */       for (RecipeCollection recipeCollection : (Iterable<RecipeCollection>)this.book.getCollection(tabInfo.category())) {
/* 259 */         selectMatchingRecipes(recipeCollection, this.stackedContents);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateCollections(boolean resetPage, boolean isFiltering) {
/* 267 */     List<RecipeCollection> tabCollection = this.book.getCollection(this.selectedTab.getCategory());
/*     */     
/* 269 */     List<RecipeCollection> collection = Lists.newArrayList(tabCollection);
/*     */ 
/*     */     
/* 272 */     collection.removeIf(c -> !c.hasAnySelected());
/*     */ 
/*     */     
/* 275 */     String searchTarget = this.searchBox.getValue();
/* 276 */     if (!searchTarget.isEmpty()) {
/* 277 */       ClientPacketListener connection = this.minecraft.getConnection();
/* 278 */       if (connection != null) {
/* 279 */         ObjectLinkedOpenHashSet objectLinkedOpenHashSet = new ObjectLinkedOpenHashSet(connection.searchTrees().recipes().search(searchTarget.toLowerCase(Locale.ROOT)));
/* 280 */         collection.removeIf(e -> !set.contains(e));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 285 */     if (isFiltering) {
/* 286 */       collection.removeIf(c -> !c.hasCraftable());
/*     */     }
/*     */     
/* 289 */     this.recipeBookPage.updateCollections(collection, resetPage, isFiltering);
/*     */   }
/*     */   
/*     */   private void updateTabs(boolean isFiltering) {
/* 293 */     int xPosTab = (this.width - 147) / 2 - this.xOffset - 30;
/* 294 */     int yPosTab = (this.height - 166) / 2 + 3;
/* 295 */     int yOffset = 27;
/*     */     
/* 297 */     int index = 0;
/* 298 */     for (RecipeBookTabButton tabButton : this.tabButtons) {
/* 299 */       ExtendedRecipeBookCategory category = tabButton.getCategory();
/*     */       
/* 301 */       if (category instanceof SearchRecipeBookCategory) {
/* 302 */         tabButton.visible = true;
/* 303 */         tabButton.setPosition(xPosTab, yPosTab + 27 * index++); continue;
/* 304 */       }  if (tabButton.updateVisibility(this.book)) {
/* 305 */         tabButton.setPosition(xPosTab, yPosTab + 27 * index++);
/* 306 */         tabButton.startAnimation(this.book, isFiltering);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 313 */     boolean shouldBeVisible = isVisibleAccordingToBookData();
/* 314 */     if (isVisible() != shouldBeVisible) {
/* 315 */       setVisible(shouldBeVisible);
/*     */     }
/*     */     
/* 318 */     if (!isVisible()) {
/*     */       return;
/*     */     }
/*     */     
/* 322 */     if (this.timesInventoryChanged != this.minecraft.player.getInventory().getTimesChanged()) {
/* 323 */       updateStackedContents();
/* 324 */       this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateStackedContents() {
/* 329 */     this.stackedContents.clear();
/* 330 */     this.minecraft.player.getInventory().fillStackedContents(this.stackedContents);
/* 331 */     this.menu.fillCraftSlotsStackedContents(this.stackedContents);
/*     */     
/* 333 */     selectMatchingRecipes();
/* 334 */     updateCollections(false, isFiltering());
/*     */   }
/*     */   
/*     */   private boolean isFiltering() {
/* 338 */     return this.book.isFiltering(this.menu.getRecipeBookType());
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 343 */     if (!isVisible()) {
/*     */       return;
/*     */     }
/*     */     
/* 347 */     if (!this.minecraft.hasControlDown()) {
/* 348 */       this.time += a;
/*     */     }
/*     */     
/* 351 */     int xo = getXOrigin();
/* 352 */     int yo = getYOrigin();
/* 353 */     graphics.blit(RenderPipelines.GUI_TEXTURED, RECIPE_BOOK_LOCATION, xo, yo, 1.0F, 1.0F, 147, 166, 256, 256);
/*     */     
/* 355 */     this.searchBox.render(graphics, mouseX, mouseY, a);
/*     */     
/* 357 */     for (RecipeBookTabButton tabButton : this.tabButtons) {
/* 358 */       tabButton.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/* 361 */     this.filterButton.render(graphics, mouseX, mouseY, a);
/*     */     
/* 363 */     this.recipeBookPage.render(graphics, xo, yo, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, Slot hoveredSlot) {
/* 367 */     if (!isVisible()) {
/*     */       return;
/*     */     }
/*     */     
/* 371 */     this.recipeBookPage.renderTooltip(graphics, mouseX, mouseY);
/*     */     
/* 373 */     this.ghostSlots.renderTooltip(graphics, this.minecraft, mouseX, mouseY, hoveredSlot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderGhostRecipe(GuiGraphics graphics, boolean isResultSlotBig) {
/* 379 */     this.ghostSlots.render(graphics, this.minecraft, isResultSlotBig);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 384 */     if (!isVisible() || this.minecraft.player.isSpectator()) {
/* 385 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 389 */     if (this.recipeBookPage.mouseClicked(event, getXOrigin(), getYOrigin(), 147, 166, doubleClick)) {
/* 390 */       RecipeDisplayId recipe = this.recipeBookPage.getLastClickedRecipe();
/* 391 */       RecipeCollection recipeCollection = this.recipeBookPage.getLastClickedRecipeCollection();
/*     */       
/* 393 */       if (recipe != null && recipeCollection != null) {
/* 394 */         if (!tryPlaceRecipe(recipeCollection, recipe, event.hasShiftDown())) {
/* 395 */           return false;
/*     */         }
/*     */         
/* 398 */         this.lastRecipeCollection = recipeCollection;
/* 399 */         this.lastRecipe = recipe;
/*     */         
/* 401 */         if (!isOffsetNextToMainGUI()) {
/* 402 */           setVisible(false);
/*     */         }
/*     */       } 
/*     */       
/* 406 */       return true;
/*     */     } 
/*     */     
/* 409 */     if (this.searchBox != null) {
/* 410 */       boolean clickedMagnifierIcon = (this.magnifierIconPlacement != null && this.magnifierIconPlacement.containsPoint(Mth.floor(event.x()), Mth.floor(event.y())));
/* 411 */       if (clickedMagnifierIcon || this.searchBox.mouseClicked(event, doubleClick)) {
/* 412 */         this.searchBox.setFocused(true);
/* 413 */         return true;
/*     */       } 
/* 415 */       this.searchBox.setFocused(false);
/*     */     } 
/*     */ 
/*     */     
/* 419 */     if (this.filterButton.mouseClicked(event, doubleClick)) {
/* 420 */       return true;
/*     */     }
/*     */     
/* 423 */     for (RecipeBookTabButton tabButton : this.tabButtons) {
/* 424 */       if (tabButton.mouseClicked(event, doubleClick)) {
/* 425 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 429 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 434 */     if (this.searchBox != null && this.searchBox.isFocused()) {
/* 435 */       return this.searchBox.mouseDragged(event, dx, dy);
/*     */     }
/* 437 */     return false;
/*     */   }
/*     */   
/*     */   private boolean tryPlaceRecipe(RecipeCollection recipeCollection, RecipeDisplayId recipe, boolean useMaxItems) {
/* 441 */     if (!recipeCollection.isCraftable(recipe) && recipe.equals(this.lastPlacedRecipe)) {
/* 442 */       return false;
/*     */     }
/*     */     
/* 445 */     this.lastPlacedRecipe = recipe;
/* 446 */     this.ghostSlots.clear();
/* 447 */     this.minecraft.gameMode.handlePlaceRecipe(this.minecraft.player.containerMenu.containerId, recipe, useMaxItems);
/* 448 */     return true;
/*     */   }
/*     */   
/*     */   private void onTabButtonPress(Button button) {
/* 452 */     if (this.selectedTab != button && button instanceof RecipeBookTabButton) { RecipeBookTabButton recipeBookTabButton = (RecipeBookTabButton)button;
/* 453 */       replaceSelected(recipeBookTabButton);
/* 454 */       updateCollections(true, isFiltering()); }
/*     */   
/*     */   }
/*     */   
/*     */   private void replaceSelected(RecipeBookTabButton tabButton) {
/* 459 */     if (this.selectedTab != null) {
/* 460 */       this.selectedTab.unselect();
/*     */     }
/* 462 */     tabButton.select();
/* 463 */     this.selectedTab = tabButton;
/*     */   }
/*     */   
/*     */   private void toggleFiltering() {
/* 467 */     RecipeBookType type = this.menu.getRecipeBookType();
/* 468 */     boolean newSetting = !this.book.isFiltering(type);
/* 469 */     this.book.setFiltering(type, newSetting);
/*     */   }
/*     */   
/*     */   public boolean hasClickedOutside(double mx, double my, int leftPos, int topPos, int imageWidth, int imageHeight) {
/* 473 */     if (!isVisible()) {
/* 474 */       return true;
/*     */     }
/*     */     
/* 477 */     boolean clickedOutside = (mx < leftPos || my < topPos || mx >= (leftPos + imageWidth) || my >= (topPos + imageHeight));
/* 478 */     boolean clickedOnRecipeBook = ((leftPos - 147) < mx && mx < leftPos && topPos < my && my < (topPos + imageHeight));
/*     */     
/* 480 */     return (clickedOutside && !clickedOnRecipeBook && !this.selectedTab.isHoveredOrFocused());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 485 */     this.ignoreTextInput = false;
/* 486 */     if (!isVisible() || this.minecraft.player.isSpectator()) {
/* 487 */       return false;
/*     */     }
/*     */     
/* 490 */     if (event.isEscape() && !isOffsetNextToMainGUI()) {
/* 491 */       setVisible(false);
/* 492 */       return true;
/*     */     } 
/*     */     
/* 495 */     if (this.searchBox.keyPressed(event)) {
/* 496 */       checkSearchStringUpdate();
/* 497 */       return true;
/*     */     } 
/* 499 */     if (this.searchBox.isFocused() && this.searchBox.isVisible() && !event.isEscape())
/*     */     {
/* 501 */       return true;
/*     */     }
/*     */     
/* 504 */     if (this.minecraft.options.keyChat.matches(event) && !this.searchBox.isFocused()) {
/* 505 */       this.ignoreTextInput = true;
/* 506 */       this.searchBox.setFocused(true);
/*     */       
/* 508 */       return true;
/*     */     } 
/*     */     
/* 511 */     if (event.isSelection() && this.lastRecipeCollection != null && this.lastRecipe != null) {
/* 512 */       AbstractWidget.playButtonClickSound(Minecraft.getInstance().getSoundManager());
/* 513 */       return tryPlaceRecipe(this.lastRecipeCollection, this.lastRecipe, event.hasShiftDown());
/*     */     } 
/*     */     
/* 516 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyReleased(KeyEvent event) {
/* 521 */     this.ignoreTextInput = false;
/* 522 */     return super.keyReleased(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 527 */     if (this.ignoreTextInput) {
/* 528 */       return false;
/*     */     }
/* 530 */     if (!isVisible() || this.minecraft.player.isSpectator()) {
/* 531 */       return false;
/*     */     }
/*     */     
/* 534 */     if (this.searchBox.charTyped(event)) {
/* 535 */       checkSearchStringUpdate();
/* 536 */       return true;
/*     */     } 
/*     */     
/* 539 */     return super.charTyped(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 544 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFocused() {
/* 554 */     return false;
/*     */   }
/*     */   
/*     */   private void checkSearchStringUpdate() {
/* 558 */     String searchText = this.searchBox.getValue().toLowerCase(Locale.ROOT);
/* 559 */     pirateSpeechForThePeople(searchText);
/*     */     
/* 561 */     if (!searchText.equals(this.lastSearch)) {
/* 562 */       updateCollections(false, isFiltering());
/* 563 */       this.lastSearch = searchText;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pirateSpeechForThePeople(String searchTarget) {
/* 568 */     if ("excitedze".equals(searchTarget)) {
/* 569 */       LanguageManager languageManager = this.minecraft.getLanguageManager();
/* 570 */       String arrrrCode = "en_pt";
/* 571 */       LanguageInfo language = languageManager.getLanguage("en_pt");
/* 572 */       if (language == null || languageManager.getSelected().equals("en_pt")) {
/*     */         return;
/*     */       }
/* 575 */       languageManager.setSelected("en_pt");
/* 576 */       this.minecraft.options.languageCode = "en_pt";
/* 577 */       this.minecraft.reloadResourcePacks();
/* 578 */       this.minecraft.options.save();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOffsetNextToMainGUI() {
/* 583 */     return (this.xOffset == 86);
/*     */   }
/*     */   
/*     */   public void recipesUpdated() {
/* 587 */     selectMatchingRecipes();
/* 588 */     updateTabs(isFiltering());
/*     */     
/* 590 */     if (isVisible())
/*     */     {
/* 592 */       updateCollections(false, isFiltering());
/*     */     }
/*     */   }
/*     */   
/*     */   public void recipeShown(RecipeDisplayId recipe) {
/* 597 */     this.minecraft.player.removeRecipeHighlight(recipe);
/*     */   }
/*     */   
/*     */   public void fillGhostRecipe(RecipeDisplay recipe) {
/* 601 */     this.ghostSlots.clear();
/* 602 */     ContextMap context = SlotDisplayContext.fromLevel((Level)Objects.requireNonNull(this.minecraft.level));
/* 603 */     fillGhostRecipe(this.ghostSlots, recipe, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendUpdateSettings() {
/* 609 */     if (this.minecraft.getConnection() != null) {
/* 610 */       RecipeBookType type = this.menu.getRecipeBookType();
/* 611 */       boolean open = this.book.getBookSettings().isOpen(type);
/* 612 */       boolean filtering = this.book.getBookSettings().isFiltering(type);
/* 613 */       this.minecraft.getConnection().send((Packet)new ServerboundRecipeBookChangeSettingsPacket(type, open, filtering));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 621 */     return this.visible ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNarration(NarrationElementOutput output) {
/* 626 */     List<NarratableEntry> narratableEntries = Lists.newArrayList();
/* 627 */     this.recipeBookPage.listButtons(e -> {
/*     */           if (e.isActive()) {
/*     */             narratableEntries.add(e);
/*     */           }
/*     */         });
/* 632 */     narratableEntries.add(this.searchBox);
/* 633 */     narratableEntries.add(this.filterButton);
/* 634 */     narratableEntries.addAll(this.tabButtons);
/*     */     
/* 636 */     Screen.NarratableSearchResult narratable = Screen.findNarratableWidget(narratableEntries, null);
/* 637 */     if (narratable != null)
/* 638 */       narratable.entry().updateNarration(output.nest()); 
/*     */   } protected abstract WidgetSprites getFilterButtonTextures(); protected abstract boolean isCraftingSlot(Slot paramSlot); protected abstract void selectMatchingRecipes(RecipeCollection paramRecipeCollection, StackedItemContents paramStackedItemContents); protected abstract Component getRecipeFilterName();
/*     */   protected abstract void fillGhostRecipe(GhostSlots paramGhostSlots, RecipeDisplay paramRecipeDisplay, ContextMap paramContextMap);
/*     */   public static final class TabInfo extends Record { private final ItemStack primaryIcon; private final Optional<ItemStack> secondaryIcon; private final ExtendedRecipeBookCategory category;
/* 642 */     public TabInfo(ItemStack primaryIcon, Optional<ItemStack> secondaryIcon, ExtendedRecipeBookCategory category) { this.primaryIcon = primaryIcon; this.secondaryIcon = secondaryIcon; this.category = category; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #642	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 642 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo; } public ItemStack primaryIcon() { return this.primaryIcon; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #642	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #642	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/* 642 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ItemStack> secondaryIcon() { return this.secondaryIcon; } public ExtendedRecipeBookCategory category() { return this.category; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TabInfo(SearchRecipeBookCategory category) {
/* 649 */       this(new ItemStack((ItemLike)Items.COMPASS), Optional.empty(), category);
/*     */     }
/*     */     
/*     */     public TabInfo(Item icon, RecipeBookCategory category) {
/* 653 */       this(new ItemStack((ItemLike)icon), Optional.empty(), (ExtendedRecipeBookCategory)category);
/*     */     }
/*     */     
/*     */     public TabInfo(Item primaryIcon, Item secondaryIcon, RecipeBookCategory category) {
/* 657 */       this(new ItemStack((ItemLike)primaryIcon), Optional.of(new ItemStack((ItemLike)secondaryIcon)), (ExtendedRecipeBookCategory)category);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/RecipeBookComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */