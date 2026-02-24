/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.ClickType;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ 
/*     */ public abstract class AbstractRecipeBookScreen<T extends RecipeBookMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener {
/*     */   private final RecipeBookComponent<?> recipeBookComponent;
/*     */   
/*     */   public AbstractRecipeBookScreen(T menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
/*  23 */     super(menu, inventory, title);
/*  24 */     this.recipeBookComponent = recipeBookComponent;
/*     */   }
/*     */   private boolean widthTooNarrow;
/*     */   
/*     */   protected void init() {
/*  29 */     super.init();
/*  30 */     this.widthTooNarrow = (this.width < 379);
/*  31 */     this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow);
/*  32 */     this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
/*     */     
/*  34 */     initButton();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initButton() {
/*  40 */     ScreenPosition buttonPos = getRecipeBookButtonPosition();
/*  41 */     addRenderableWidget((GuiEventListener)new ImageButton(buttonPos.x(), buttonPos.y(), 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
/*     */             this.recipeBookComponent.toggleVisibility();
/*     */             this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
/*     */             ScreenPosition updatedButtonPos = getRecipeBookButtonPosition();
/*     */             button.setPosition(updatedButtonPos.x(), updatedButtonPos.y());
/*     */             onRecipeBookButtonClick();
/*     */           }));
/*  48 */     addWidget((GuiEventListener)this.recipeBookComponent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRecipeBookButtonClick() {}
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  56 */     if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
/*  57 */       renderBackground(graphics, mouseX, mouseY, a);
/*     */     } else {
/*  59 */       renderContents(graphics, mouseX, mouseY, a);
/*     */     } 
/*  61 */     graphics.nextStratum();
/*  62 */     this.recipeBookComponent.render(graphics, mouseX, mouseY, a);
/*     */     
/*  64 */     graphics.nextStratum();
/*  65 */     renderCarriedItem(graphics, mouseX, mouseY);
/*  66 */     renderSnapbackItem(graphics);
/*     */     
/*  68 */     renderTooltip(graphics, mouseX, mouseY);
/*  69 */     this.recipeBookComponent.renderTooltip(graphics, mouseX, mouseY, this.hoveredSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderSlots(GuiGraphics graphics, int mouseX, int mouseY) {
/*  74 */     super.renderSlots(graphics, mouseX, mouseY);
/*  75 */     this.recipeBookComponent.renderGhostRecipe(graphics, isBiggerResultSlot());
/*     */   }
/*     */   
/*     */   protected boolean isBiggerResultSlot() {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/*  84 */     if (this.recipeBookComponent.charTyped(event)) {
/*  85 */       return true;
/*     */     }
/*  87 */     return super.charTyped(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  92 */     if (this.recipeBookComponent.keyPressed(event)) {
/*  93 */       return true;
/*     */     }
/*  95 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 100 */     if (this.recipeBookComponent.mouseClicked(event, doubleClick)) {
/* 101 */       setFocused((GuiEventListener)this.recipeBookComponent);
/* 102 */       return true;
/*     */     } 
/*     */     
/* 105 */     if (this.widthTooNarrow && this.recipeBookComponent.isVisible()) {
/* 106 */       return true;
/*     */     }
/*     */     
/* 109 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 114 */     if (this.recipeBookComponent.mouseDragged(event, dx, dy)) {
/* 115 */       return true;
/*     */     }
/* 117 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
/* 122 */     return ((!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(left, top, w, h, xm, ym));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/* 127 */     boolean clickedOutside = (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/* 128 */     return (this.recipeBookComponent.hasClickedOutside(mx, my, this.leftPos, this.topPos, this.imageWidth, this.imageHeight) && clickedOutside);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ClickType clickType) {
/* 133 */     super.slotClicked(slot, slotId, buttonNum, clickType);
/*     */     
/* 135 */     this.recipeBookComponent.slotClicked(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerTick() {
/* 140 */     super.containerTick();
/*     */     
/* 142 */     this.recipeBookComponent.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void recipesUpdated() {
/* 147 */     this.recipeBookComponent.recipesUpdated();
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillGhostRecipe(RecipeDisplay display) {
/* 152 */     this.recipeBookComponent.fillGhostRecipe(display);
/*     */   }
/*     */   
/*     */   protected abstract ScreenPosition getRecipeBookButtonPosition();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/AbstractRecipeBookScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */