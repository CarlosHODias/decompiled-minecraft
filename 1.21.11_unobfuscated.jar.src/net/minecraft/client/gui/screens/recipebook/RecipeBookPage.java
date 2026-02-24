/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class RecipeBookPage {
/*     */   public static final int ITEMS_PER_PAGE = 20;
/*  29 */   private static final WidgetSprites PAGE_FORWARD_SPRITES = new WidgetSprites(
/*  30 */       Identifier.withDefaultNamespace("recipe_book/page_forward"), 
/*  31 */       Identifier.withDefaultNamespace("recipe_book/page_forward_highlighted"));
/*     */   
/*  33 */   private static final WidgetSprites PAGE_BACKWARD_SPRITES = new WidgetSprites(
/*  34 */       Identifier.withDefaultNamespace("recipe_book/page_backward"), 
/*  35 */       Identifier.withDefaultNamespace("recipe_book/page_backward_highlighted"));
/*     */   
/*  37 */   private static final Component NEXT_PAGE_TEXT = (Component)Component.translatable("gui.recipebook.next_page");
/*  38 */   private static final Component PREVIOUS_PAGE_TEXT = (Component)Component.translatable("gui.recipebook.previous_page");
/*     */   
/*     */   private static final int TURN_PAGE_SPRITE_WIDTH = 12;
/*     */   private static final int TURN_PAGE_SPRITE_HEIGHT = 17;
/*  42 */   private final List<RecipeButton> buttons = Lists.newArrayListWithCapacity(20);
/*     */   
/*     */   private RecipeButton hoveredButton;
/*     */   
/*     */   private final OverlayRecipeComponent overlay;
/*     */   
/*     */   private Minecraft minecraft;
/*     */   
/*     */   private final RecipeBookComponent<?> parent;
/*  51 */   private List<RecipeCollection> recipeCollections = (List<RecipeCollection>)ImmutableList.of();
/*     */   
/*     */   private ImageButton forwardButton;
/*     */   
/*     */   private ImageButton backButton;
/*     */   
/*     */   private int totalPages;
/*     */   
/*     */   private int currentPage;
/*     */   private ClientRecipeBook recipeBook;
/*     */   private RecipeDisplayId lastClickedRecipe;
/*     */   private RecipeCollection lastClickedRecipeCollection;
/*     */   private boolean isFiltering;
/*     */   
/*     */   public RecipeBookPage(RecipeBookComponent<?> parent, SlotSelectTime slotSelectTime, boolean isFurnaceMenu) {
/*  66 */     this.parent = parent;
/*  67 */     this.overlay = new OverlayRecipeComponent(slotSelectTime, isFurnaceMenu);
/*     */     
/*  69 */     for (int i = 0; i < 20; i++) {
/*  70 */       this.buttons.add(new RecipeButton(slotSelectTime));
/*     */     }
/*     */   }
/*     */   
/*     */   public void init(Minecraft minecraft, int xo, int yo) {
/*  75 */     this.minecraft = minecraft;
/*  76 */     this.recipeBook = minecraft.player.getRecipeBook();
/*     */     
/*  78 */     for (int i = 0; i < this.buttons.size(); i++) {
/*  79 */       ((RecipeButton)this.buttons.get(i)).setPosition(xo + 11 + 25 * i % 5, yo + 31 + 25 * i / 5);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     this.forwardButton = new ImageButton(xo + 93, yo + 137, 12, 17, PAGE_FORWARD_SPRITES, button -> updateArrowButtons(), NEXT_PAGE_TEXT);
/*  86 */     this.forwardButton.setTooltip(Tooltip.create(NEXT_PAGE_TEXT));
/*  87 */     this.backButton = new ImageButton(xo + 38, yo + 137, 12, 17, PAGE_BACKWARD_SPRITES, button -> updateArrowButtons(), PREVIOUS_PAGE_TEXT);
/*  88 */     this.backButton.setTooltip(Tooltip.create(PREVIOUS_PAGE_TEXT));
/*     */   }
/*     */   
/*     */   public void updateCollections(List<RecipeCollection> recipeCollections, boolean resetPage, boolean isFiltering) {
/*  92 */     this.recipeCollections = recipeCollections;
/*  93 */     this.isFiltering = isFiltering;
/*  94 */     this.totalPages = (int)Math.ceil(recipeCollections.size() / 20.0D);
/*     */     
/*  96 */     if (this.totalPages <= this.currentPage || resetPage) {
/*  97 */       this.currentPage = 0;
/*     */     }
/*     */     
/* 100 */     updateButtonsForPage();
/*     */   }
/*     */   
/*     */   private void updateButtonsForPage() {
/* 104 */     int startOffset = 20 * this.currentPage;
/* 105 */     ContextMap context = SlotDisplayContext.fromLevel((Level)this.minecraft.level);
/* 106 */     for (int i = 0; i < this.buttons.size(); i++) {
/* 107 */       RecipeButton button = this.buttons.get(i);
/*     */       
/* 109 */       if (startOffset + i < this.recipeCollections.size()) {
/* 110 */         RecipeCollection recipeCollection = this.recipeCollections.get(startOffset + i);
/*     */         
/* 112 */         button.init(recipeCollection, this.isFiltering, this, context);
/*     */         
/* 114 */         button.visible = true;
/*     */       } else {
/* 116 */         button.visible = false;
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     updateArrowButtons();
/*     */   }
/*     */   
/*     */   private void updateArrowButtons() {
/* 124 */     if (this.forwardButton != null) {
/* 125 */       this.forwardButton.visible = (this.totalPages > 1 && this.currentPage < this.totalPages - 1);
/*     */     }
/* 127 */     if (this.backButton != null) {
/* 128 */       this.backButton.visible = (this.totalPages > 1 && this.currentPage > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, int xo, int yo, int mouseX, int mouseY, float a) {
/* 133 */     if (this.totalPages > 1) {
/* 134 */       MutableComponent mutableComponent = Component.translatable("gui.recipebook.page", new Object[] { this.currentPage + 1, this.totalPages });
/* 135 */       int pWidth = this.minecraft.font.width((FormattedText)mutableComponent);
/* 136 */       graphics.drawString(this.minecraft.font, (Component)mutableComponent, xo - pWidth / 2 + 73, yo + 141, -1);
/*     */     } 
/*     */     
/* 139 */     this.hoveredButton = null;
/* 140 */     for (RecipeButton recipeBookButton : this.buttons) {
/* 141 */       recipeBookButton.render(graphics, mouseX, mouseY, a);
/* 142 */       if (recipeBookButton.visible && recipeBookButton.isHoveredOrFocused()) {
/* 143 */         this.hoveredButton = recipeBookButton;
/*     */       }
/*     */     } 
/*     */     
/* 147 */     if (this.forwardButton != null) {
/* 148 */       this.forwardButton.render(graphics, mouseX, mouseY, a);
/*     */     }
/* 150 */     if (this.backButton != null) {
/* 151 */       this.backButton.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/* 154 */     graphics.nextStratum();
/* 155 */     this.overlay.render(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
/* 159 */     if (this.minecraft.screen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
/* 160 */       ItemStack displayStack = this.hoveredButton.getDisplayStack();
/* 161 */       Identifier tooltipStyle = (Identifier)displayStack.get(DataComponents.TOOLTIP_STYLE);
/* 162 */       graphics.setComponentTooltipForNextFrame(this.minecraft.font, this.hoveredButton.getTooltipText(displayStack), mouseX, mouseY, tooltipStyle);
/*     */     } 
/*     */   }
/*     */   
/*     */   public RecipeDisplayId getLastClickedRecipe() {
/* 167 */     return this.lastClickedRecipe;
/*     */   }
/*     */   
/*     */   public RecipeCollection getLastClickedRecipeCollection() {
/* 171 */     return this.lastClickedRecipeCollection;
/*     */   }
/*     */   
/*     */   public void setInvisible() {
/* 175 */     this.overlay.setVisible(false);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, int xo, int yo, int imageWidth, int imageHeight, boolean doubleClick) {
/* 179 */     this.lastClickedRecipe = null;
/* 180 */     this.lastClickedRecipeCollection = null;
/*     */     
/* 182 */     if (this.overlay.isVisible()) {
/* 183 */       if (this.overlay.mouseClicked(event, doubleClick)) {
/* 184 */         this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
/* 185 */         this.lastClickedRecipeCollection = this.overlay.getRecipeCollection();
/*     */       } else {
/* 187 */         this.overlay.setVisible(false);
/*     */       } 
/*     */       
/* 190 */       return true;
/*     */     } 
/*     */     
/* 193 */     if (this.forwardButton.mouseClicked(event, doubleClick)) {
/* 194 */       this.currentPage++;
/* 195 */       updateButtonsForPage();
/* 196 */       return true;
/* 197 */     }  if (this.backButton.mouseClicked(event, doubleClick)) {
/* 198 */       this.currentPage--;
/* 199 */       updateButtonsForPage();
/* 200 */       return true;
/*     */     } 
/*     */     
/* 203 */     ContextMap context = SlotDisplayContext.fromLevel((Level)this.minecraft.level);
/* 204 */     for (RecipeButton button : this.buttons) {
/* 205 */       if (button.mouseClicked(event, doubleClick)) {
/* 206 */         if (event.button() == 0) {
/* 207 */           this.lastClickedRecipe = button.getCurrentRecipe();
/* 208 */           this.lastClickedRecipeCollection = button.getCollection();
/* 209 */         } else if (event.button() == 1 && 
/* 210 */           !this.overlay.isVisible() && !button.isOnlyOption()) {
/* 211 */           this.overlay.init(button.getCollection(), context, this.isFiltering, button.getX(), button.getY(), xo + imageWidth / 2, yo + 13 + imageHeight / 2, button.getWidth());
/*     */         } 
/*     */         
/* 214 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 218 */     return false;
/*     */   }
/*     */   
/*     */   public void recipeShown(RecipeDisplayId recipe) {
/* 222 */     this.parent.recipeShown(recipe);
/*     */   }
/*     */   
/*     */   public ClientRecipeBook getRecipeBook() {
/* 226 */     return this.recipeBook;
/*     */   }
/*     */   
/*     */   protected void listButtons(Consumer<AbstractWidget> buttonConsumer) {
/* 230 */     this.buttons.forEach(buttonConsumer);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/RecipeBookPage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */