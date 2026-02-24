/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ 
/*     */ public class RecipeBookTabButton
/*     */   extends ImageButton {
/*  16 */   private static final WidgetSprites SPRITES = new WidgetSprites(
/*  17 */       Identifier.withDefaultNamespace("recipe_book/tab"), 
/*  18 */       Identifier.withDefaultNamespace("recipe_book/tab_selected"));
/*     */   
/*     */   public static final int WIDTH = 35;
/*     */   
/*     */   public static final int HEIGHT = 27;
/*     */   
/*     */   private final RecipeBookComponent.TabInfo tabInfo;
/*     */   
/*     */   private static final float ANIMATION_TIME = 15.0F;
/*     */   private float animationTime;
/*     */   private boolean selected = false;
/*     */   
/*     */   public RecipeBookTabButton(int x, int y, RecipeBookComponent.TabInfo tabInfo, Button.OnPress onPress) {
/*  31 */     super(x, y, 35, 27, SPRITES, onPress);
/*  32 */     this.tabInfo = tabInfo;
/*     */   }
/*     */   
/*     */   public void startAnimation(ClientRecipeBook recipeBook, boolean isFiltering) {
/*  36 */     RecipeCollection.CraftableStatus recipesToShow = isFiltering ? RecipeCollection.CraftableStatus.CRAFTABLE : RecipeCollection.CraftableStatus.ANY;
/*  37 */     List<RecipeCollection> collection = recipeBook.getCollection(this.tabInfo.category());
/*  38 */     for (RecipeCollection recipeCollection : collection) {
/*  39 */       for (RecipeDisplayEntry recipe : recipeCollection.getSelectedRecipes(recipesToShow)) {
/*  40 */         if (recipeBook.willHighlight(recipe.id())) {
/*  41 */           this.animationTime = 15.0F;
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  51 */     if (this.animationTime > 0.0F) {
/*  52 */       float squeeze = 1.0F + 0.1F * (float)Math.sin((this.animationTime / 15.0F * 3.1415927F));
/*  53 */       graphics.pose().pushMatrix();
/*  54 */       graphics.pose().translate((getX() + 8), (getY() + 12));
/*  55 */       graphics.pose().scale(1.0F, squeeze);
/*  56 */       graphics.pose().translate(-(getX() + 8), -(getY() + 12));
/*     */     } 
/*     */     
/*  59 */     Identifier sprite = this.sprites.get(true, this.selected);
/*     */     
/*  61 */     int xPos = getX();
/*  62 */     if (this.selected) {
/*  63 */       xPos -= 2;
/*     */     }
/*     */     
/*  66 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xPos, getY(), this.width, this.height);
/*     */     
/*  68 */     renderIcon(graphics);
/*     */     
/*  70 */     if (this.animationTime > 0.0F) {
/*  71 */       graphics.pose().popMatrix();
/*  72 */       this.animationTime -= a;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleCursor(GuiGraphics graphics) {
/*  78 */     if (!this.selected) {
/*  79 */       super.handleCursor(graphics);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderIcon(GuiGraphics graphics) {
/*  84 */     int moveLeft = this.selected ? -2 : 0;
/*     */     
/*  86 */     if (this.tabInfo.secondaryIcon().isPresent()) {
/*  87 */       graphics.renderFakeItem(this.tabInfo.primaryIcon(), getX() + 3 + moveLeft, getY() + 5);
/*  88 */       graphics.renderFakeItem(this.tabInfo.secondaryIcon().get(), getX() + 14 + moveLeft, getY() + 5);
/*     */     } else {
/*  90 */       graphics.renderFakeItem(this.tabInfo.primaryIcon(), getX() + 9 + moveLeft, getY() + 5);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ExtendedRecipeBookCategory getCategory() {
/*  95 */     return this.tabInfo.category();
/*     */   }
/*     */   
/*     */   public boolean updateVisibility(ClientRecipeBook book) {
/*  99 */     List<RecipeCollection> collections = book.getCollection(this.tabInfo.category());
/* 100 */     this.visible = false;
/*     */     
/* 102 */     for (RecipeCollection collection : collections) {
/* 103 */       if (collection.hasAnySelected()) {
/* 104 */         this.visible = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 109 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void select() {
/* 113 */     this.selected = true;
/*     */   }
/*     */   
/*     */   public void unselect() {
/* 117 */     this.selected = false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/RecipeBookTabButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */