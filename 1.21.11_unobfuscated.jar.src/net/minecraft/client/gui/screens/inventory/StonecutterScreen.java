/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.StonecutterMenu;
/*     */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*     */ import net.minecraft.world.item.crafting.StonecutterRecipe;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class StonecutterScreen extends AbstractContainerScreen<StonecutterMenu> {
/*  22 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller");
/*  23 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller_disabled");
/*  24 */   private static final Identifier RECIPE_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_selected");
/*  25 */   private static final Identifier RECIPE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_highlighted");
/*  26 */   private static final Identifier RECIPE_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe");
/*  27 */   private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/stonecutter.png");
/*     */   
/*     */   private static final int SCROLLER_WIDTH = 12;
/*     */   
/*     */   private static final int SCROLLER_HEIGHT = 15;
/*     */   
/*     */   private static final int RECIPES_COLUMNS = 4;
/*     */   private static final int RECIPES_ROWS = 3;
/*     */   private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
/*     */   private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
/*     */   private static final int SCROLLER_FULL_HEIGHT = 54;
/*     */   private static final int RECIPES_X = 52;
/*     */   private static final int RECIPES_Y = 14;
/*     */   private float scrollOffs;
/*     */   private boolean scrolling;
/*     */   private int startIndex;
/*     */   private boolean displayRecipes;
/*     */   
/*     */   public StonecutterScreen(StonecutterMenu menu, Inventory inventory, Component title) {
/*  46 */     super(menu, inventory, title);
/*  47 */     menu.registerUpdateListener(this::containerChanged);
/*  48 */     this.titleLabelY--;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  53 */     super.render(graphics, mouseX, mouseY, a);
/*  54 */     renderTooltip(graphics, mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/*  59 */     int xo = this.leftPos;
/*  60 */     int yo = this.topPos;
/*  61 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*     */     
/*  63 */     int sy = (int)(41.0F * this.scrollOffs);
/*  64 */     Identifier sprite = isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
/*  65 */     int scrollerX = xo + 119;
/*  66 */     int scrollerY = yo + 15 + sy;
/*  67 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, scrollerX, scrollerY, 12, 15);
/*  68 */     if (xm >= scrollerX && xm < scrollerX + 12 && ym >= scrollerY && ym < scrollerY + 15) {
/*  69 */       graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
/*     */     }
/*     */     
/*  72 */     int x = this.leftPos + 52;
/*  73 */     int y = this.topPos + 14;
/*     */     
/*  75 */     int endIndex = this.startIndex + 12;
/*     */     
/*  77 */     renderButtons(graphics, xm, ym, x, y, endIndex);
/*  78 */     renderRecipes(graphics, x, y, endIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
/*  83 */     super.renderTooltip(graphics, mouseX, mouseY);
/*     */     
/*  85 */     if (this.displayRecipes) {
/*  86 */       int edgeLeft = this.leftPos + 52;
/*  87 */       int edgeTop = this.topPos + 14;
/*     */       
/*  89 */       int endIndex = this.startIndex + 12;
/*  90 */       SelectableRecipe.SingleInputSet<StonecutterRecipe> visibleRecipes = this.menu.getVisibleRecipes();
/*  91 */       for (int index = this.startIndex; index < endIndex && index < visibleRecipes.size(); index++) {
/*  92 */         int posIndex = index - this.startIndex;
/*     */         
/*  94 */         int itemLeft = edgeLeft + posIndex % 4 * 16;
/*  95 */         int itemRight = edgeTop + posIndex / 4 * 18 + 2;
/*  96 */         if (mouseX >= itemLeft && mouseX < itemLeft + 16 && mouseY >= itemRight && mouseY < itemRight + 18) {
/*  97 */           ContextMap context = SlotDisplayContext.fromLevel((Level)this.minecraft.level);
/*  98 */           SlotDisplay buttonIcon = ((SelectableRecipe.SingleInputEntry)visibleRecipes.entries().get(index)).recipe().optionDisplay();
/*  99 */           graphics.setTooltipForNextFrame(this.font, buttonIcon.resolveForFirstStack(context), mouseX, mouseY);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderButtons(GuiGraphics graphics, int xm, int ym, int x, int y, int endIndex) {
/* 106 */     for (int index = this.startIndex; index < endIndex && index < this.menu.getNumberOfVisibleRecipes(); index++) {
/* 107 */       Identifier sprite; int posIndex = index - this.startIndex;
/* 108 */       int posX = x + posIndex % 4 * 16;
/* 109 */       int row = posIndex / 4;
/* 110 */       int posY = y + row * 18 + 2;
/*     */ 
/*     */       
/* 113 */       if (index == this.menu.getSelectedRecipeIndex()) {
/* 114 */         sprite = RECIPE_SELECTED_SPRITE;
/* 115 */       } else if (xm >= posX && ym >= posY && xm < posX + 16 && ym < posY + 18) {
/* 116 */         sprite = RECIPE_HIGHLIGHTED_SPRITE;
/*     */       } else {
/* 118 */         sprite = RECIPE_SPRITE;
/*     */       } 
/*     */       
/* 121 */       int textureY = posY - 1;
/* 122 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, posX, textureY, 16, 18);
/* 123 */       if (xm >= posX && ym >= textureY && xm < posX + 16 && ym < textureY + 18) {
/* 124 */         graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderRecipes(GuiGraphics graphics, int x, int y, int endIndex) {
/* 130 */     SelectableRecipe.SingleInputSet<StonecutterRecipe> visibleRecipes = this.menu.getVisibleRecipes();
/* 131 */     ContextMap context = SlotDisplayContext.fromLevel((Level)this.minecraft.level);
/* 132 */     for (int index = this.startIndex; index < endIndex && index < visibleRecipes.size(); index++) {
/* 133 */       int posIndex = index - this.startIndex;
/* 134 */       int posX = x + posIndex % 4 * 16;
/* 135 */       int row = posIndex / 4;
/* 136 */       int posY = y + row * 18 + 2;
/*     */       
/* 138 */       SlotDisplay buttonIcon = ((SelectableRecipe.SingleInputEntry)visibleRecipes.entries().get(index)).recipe().optionDisplay();
/* 139 */       graphics.renderItem(buttonIcon.resolveForFirstStack(context), posX, posY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 145 */     if (this.displayRecipes) {
/* 146 */       int xo = this.leftPos + 52;
/* 147 */       int yo = this.topPos + 14;
/*     */       
/* 149 */       int endIndex = this.startIndex + 12;
/* 150 */       for (int index = this.startIndex; index < endIndex; index++) {
/* 151 */         int posIndex = index - this.startIndex;
/* 152 */         double xx = event.x() - (xo + posIndex % 4 * 16);
/* 153 */         double yy = event.y() - (yo + posIndex / 4 * 18);
/* 154 */         if (xx >= 0.0D && yy >= 0.0D && xx < 16.0D && yy < 18.0D && this.menu.clickMenuButton((Player)this.minecraft.player, index)) {
/* 155 */           net.minecraft.client.Minecraft.getInstance().getSoundManager().play((SoundInstance)net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
/* 156 */           this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
/* 157 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 161 */       xo = this.leftPos + 119;
/* 162 */       yo = this.topPos + 9;
/* 163 */       if (event.x() >= xo && event.x() < (xo + 12) && event.y() >= yo && event.y() < (yo + 54)) {
/* 164 */         this.scrolling = true;
/*     */       }
/*     */     } 
/*     */     
/* 168 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 173 */     if (this.scrolling && isScrollBarActive()) {
/* 174 */       int yscr = this.topPos + 14;
/* 175 */       int yscr2 = yscr + 54;
/*     */       
/* 177 */       this.scrollOffs = ((float)event.y() - yscr - 7.5F) / ((yscr2 - yscr) - 15.0F);
/* 178 */       this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
/*     */       
/* 180 */       this.startIndex = (int)((this.scrollOffs * getOffscreenRows()) + 0.5D) * 4;
/*     */       
/* 182 */       return true;
/*     */     } 
/* 184 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 189 */     this.scrolling = false;
/* 190 */     return super.mouseReleased(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 196 */     if (super.mouseScrolled(x, y, scrollX, scrollY)) {
/* 197 */       return true;
/*     */     }
/*     */     
/* 200 */     if (isScrollBarActive()) {
/* 201 */       int offscreenRows = getOffscreenRows();
/* 202 */       float scrolledDelta = (float)scrollY / offscreenRows;
/* 203 */       this.scrollOffs = Mth.clamp(this.scrollOffs - scrolledDelta, 0.0F, 1.0F);
/* 204 */       this.startIndex = (int)((this.scrollOffs * offscreenRows) + 0.5D) * 4;
/*     */     } 
/* 206 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isScrollBarActive() {
/* 210 */     return (this.displayRecipes && this.menu.getNumberOfVisibleRecipes() > 12);
/*     */   }
/*     */   
/*     */   protected int getOffscreenRows() {
/* 214 */     return (this.menu.getNumberOfVisibleRecipes() + 4 - 1) / 4 - 3;
/*     */   }
/*     */   
/*     */   private void containerChanged() {
/* 218 */     this.displayRecipes = this.menu.hasInputItem();
/* 219 */     if (!this.displayRecipes) {
/* 220 */       this.scrollOffs = 0.0F;
/* 221 */       this.startIndex = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/StonecutterScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */