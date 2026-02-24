/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OverlayRecipeComponent
/*     */   implements GuiEventListener, Renderable
/*     */ {
/*  32 */   private static final Identifier OVERLAY_RECIPE_SPRITE = Identifier.withDefaultNamespace("recipe_book/overlay_recipe");
/*     */   
/*     */   private static final int MAX_ROW = 4;
/*     */   private static final int MAX_ROW_LARGE = 5;
/*     */   private static final float ITEM_RENDER_SCALE = 0.375F;
/*     */   public static final int BUTTON_SIZE = 25;
/*  38 */   private final List<OverlayRecipeButton> recipeButtons = Lists.newArrayList();
/*     */   
/*     */   private boolean isVisible;
/*     */   
/*     */   private int x;
/*     */   private int y;
/*  44 */   private RecipeCollection collection = RecipeCollection.EMPTY;
/*     */   
/*     */   private RecipeDisplayId lastRecipeClicked;
/*     */   
/*     */   private final SlotSelectTime slotSelectTime;
/*     */   private final boolean isFurnaceMenu;
/*     */   
/*     */   public OverlayRecipeComponent(SlotSelectTime slotSelectTime, boolean isFurnaceMenu) {
/*  52 */     this.slotSelectTime = slotSelectTime;
/*  53 */     this.isFurnaceMenu = isFurnaceMenu;
/*     */   }
/*     */   
/*     */   public void init(RecipeCollection collection, ContextMap context, boolean isFiltering, int buttonX, int buttonY, int centerX, int centerY, float buttonWidth) {
/*  57 */     this.collection = collection;
/*     */     
/*  59 */     List<RecipeDisplayEntry> craftable = collection.getSelectedRecipes(RecipeCollection.CraftableStatus.CRAFTABLE);
/*  60 */     List<RecipeDisplayEntry> unCraftable = isFiltering ? Collections.<RecipeDisplayEntry>emptyList() : collection.getSelectedRecipes(RecipeCollection.CraftableStatus.NOT_CRAFTABLE);
/*     */     
/*  62 */     int craftables = craftable.size();
/*  63 */     int total = craftables + unCraftable.size();
/*     */     
/*  65 */     int maxRow = (total <= 16) ? 4 : 5;
/*  66 */     int rows = (int)Math.ceil((total / maxRow));
/*  67 */     this.x = buttonX;
/*  68 */     this.y = buttonY;
/*     */ 
/*     */     
/*  71 */     float rightPos = (this.x + Math.min(total, maxRow) * 25);
/*  72 */     float maxLeftPos = (centerX + 50);
/*  73 */     if (rightPos > maxLeftPos) {
/*  74 */       this.x = (int)(this.x - buttonWidth * (int)((rightPos - maxLeftPos) / buttonWidth));
/*     */     }
/*     */     
/*  77 */     float bottomPos = (this.y + rows * 25);
/*  78 */     float maxBottomPos = (centerY + 50);
/*  79 */     if (bottomPos > maxBottomPos) {
/*  80 */       this.y = (int)(this.y - buttonWidth * Mth.ceil((bottomPos - maxBottomPos) / buttonWidth));
/*     */     }
/*     */     
/*  83 */     float topPos = this.y;
/*  84 */     float maxTopPos = (centerY - 100);
/*  85 */     if (topPos < maxTopPos) {
/*  86 */       this.y = (int)(this.y - buttonWidth * Mth.ceil((topPos - maxTopPos) / buttonWidth));
/*     */     }
/*     */     
/*  89 */     this.isVisible = true;
/*     */     
/*  91 */     this.recipeButtons.clear();
/*  92 */     for (int i = 0; i < total; i++) {
/*  93 */       boolean canCraft = (i < craftables);
/*  94 */       RecipeDisplayEntry recipe = canCraft ? craftable.get(i) : unCraftable.get(i - craftables);
/*     */       
/*  96 */       int x = this.x + 4 + 25 * i % maxRow;
/*  97 */       int y = this.y + 5 + 25 * i / maxRow;
/*  98 */       if (this.isFurnaceMenu) {
/*  99 */         this.recipeButtons.add(new OverlaySmeltingRecipeButton(this, x, y, recipe.id(), recipe.display(), context, canCraft));
/*     */       } else {
/* 101 */         this.recipeButtons.add(new OverlayCraftingRecipeButton(this, x, y, recipe.id(), recipe.display(), context, canCraft));
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     this.lastRecipeClicked = null;
/*     */   }
/*     */   
/*     */   public RecipeCollection getRecipeCollection() {
/* 109 */     return this.collection;
/*     */   }
/*     */   
/*     */   public RecipeDisplayId getLastRecipeClicked() {
/* 113 */     return this.lastRecipeClicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 118 */     if (event.button() != 0) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     for (OverlayRecipeButton recipeButton : this.recipeButtons) {
/* 123 */       if (recipeButton.mouseClicked(event, doubleClick)) {
/* 124 */         this.lastRecipeClicked = recipeButton.recipe;
/* 125 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 139 */     if (!this.isVisible) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     int maxRow = (this.recipeButtons.size() <= 16) ? 4 : 5;
/* 144 */     int width = Math.min(this.recipeButtons.size(), maxRow);
/* 145 */     int height = Mth.ceil(this.recipeButtons.size() / maxRow);
/*     */     
/* 147 */     int border = 4;
/* 148 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, OVERLAY_RECIPE_SPRITE, this.x, this.y, width * 25 + 8, height * 25 + 8);
/*     */     
/* 150 */     for (OverlayRecipeButton component : this.recipeButtons) {
/* 151 */       component.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 156 */     this.isVisible = visible;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 160 */     return this.isVisible;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFocused() {
/* 170 */     return false;
/*     */   }
/*     */   
/*     */   private class OverlaySmeltingRecipeButton extends OverlayRecipeButton {
/* 174 */     private static final Identifier ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay");
/* 175 */     private static final Identifier HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_highlighted");
/* 176 */     private static final Identifier DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled");
/* 177 */     private static final Identifier HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled_highlighted");
/*     */     
/*     */     public OverlaySmeltingRecipeButton(OverlayRecipeComponent this$0, int x, int y, RecipeDisplayId id, RecipeDisplay recipe, ContextMap context, boolean isCraftable) {
/* 180 */       super(this$0, x, y, id, isCraftable, calculateIngredientsPositions(recipe, context));
/*     */     }
/*     */     
/*     */     private static List<OverlayRecipeComponent.OverlayRecipeButton.Pos> calculateIngredientsPositions(RecipeDisplay recipe, ContextMap context) {
/* 184 */       if (recipe instanceof FurnaceRecipeDisplay) { FurnaceRecipeDisplay furnaceRecipe = (FurnaceRecipeDisplay)recipe;
/* 185 */         List<ItemStack> items = furnaceRecipe.ingredient().resolveForStacks(context);
/* 186 */         if (!items.isEmpty()) {
/* 187 */           return List.of(createGridPos(1, 1, items));
/*     */         } }
/*     */       
/* 190 */       return List.of();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Identifier getSprite(boolean isCraftable) {
/* 195 */       if (isCraftable) {
/* 196 */         return isHoveredOrFocused() ? HIGHLIGHTED_ENABLED_SPRITE : ENABLED_SPRITE;
/*     */       }
/* 198 */       return isHoveredOrFocused() ? HIGHLIGHTED_DISABLED_SPRITE : DISABLED_SPRITE;
/*     */     }
/*     */   }
/*     */   
/*     */   private class OverlayCraftingRecipeButton
/*     */     extends OverlayRecipeButton {
/* 204 */     private static final Identifier ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay");
/* 205 */     private static final Identifier HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_highlighted");
/* 206 */     private static final Identifier DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled");
/* 207 */     private static final Identifier HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled_highlighted");
/*     */     
/*     */     private static final int GRID_WIDTH = 3;
/*     */     private static final int GRID_HEIGHT = 3;
/*     */     
/*     */     public OverlayCraftingRecipeButton(OverlayRecipeComponent this$0, int x, int y, RecipeDisplayId id, RecipeDisplay recipe, ContextMap context, boolean isCraftable) {
/* 213 */       super(this$0, x, y, id, isCraftable, calculateIngredientsPositions(recipe, context));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static List<OverlayRecipeComponent.OverlayRecipeButton.Pos> calculateIngredientsPositions(RecipeDisplay recipe, ContextMap context) {
/*     */       // Byte code:
/*     */       //   0: new java/util/ArrayList
/*     */       //   3: dup
/*     */       //   4: invokespecial <init> : ()V
/*     */       //   7: astore_2
/*     */       //   8: aload_0
/*     */       //   9: dup
/*     */       //   10: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   13: pop
/*     */       //   14: astore_3
/*     */       //   15: iconst_0
/*     */       //   16: istore #4
/*     */       //   18: aload_3
/*     */       //   19: iload #4
/*     */       //   21: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   26: lookupswitch default -> 175, 0 -> 52, 1 -> 88
/*     */       //   52: aload_3
/*     */       //   53: checkcast net/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay
/*     */       //   56: astore #5
/*     */       //   58: iconst_3
/*     */       //   59: iconst_3
/*     */       //   60: aload #5
/*     */       //   62: invokevirtual width : ()I
/*     */       //   65: aload #5
/*     */       //   67: invokevirtual height : ()I
/*     */       //   70: aload #5
/*     */       //   72: invokevirtual ingredients : ()Ljava/util/List;
/*     */       //   75: aload_1
/*     */       //   76: aload_2
/*     */       //   77: <illegal opcode> addItemToSlot : (Lnet/minecraft/util/context/ContextMap;Ljava/util/List;)Lnet/minecraft/recipebook/PlaceRecipeHelper$Output;
/*     */       //   82: invokestatic placeRecipe : (IIIILjava/lang/Iterable;Lnet/minecraft/recipebook/PlaceRecipeHelper$Output;)V
/*     */       //   85: goto -> 175
/*     */       //   88: aload_3
/*     */       //   89: checkcast net/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay
/*     */       //   92: astore #6
/*     */       //   94: aload #6
/*     */       //   96: invokevirtual ingredients : ()Ljava/util/List;
/*     */       //   99: astore #7
/*     */       //   101: iconst_0
/*     */       //   102: istore #8
/*     */       //   104: iload #8
/*     */       //   106: aload #7
/*     */       //   108: invokeinterface size : ()I
/*     */       //   113: if_icmpge -> 172
/*     */       //   116: aload #7
/*     */       //   118: iload #8
/*     */       //   120: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   125: checkcast net/minecraft/world/item/crafting/display/SlotDisplay
/*     */       //   128: aload_1
/*     */       //   129: invokeinterface resolveForStacks : (Lnet/minecraft/util/context/ContextMap;)Ljava/util/List;
/*     */       //   134: astore #9
/*     */       //   136: aload #9
/*     */       //   138: invokeinterface isEmpty : ()Z
/*     */       //   143: ifne -> 166
/*     */       //   146: aload_2
/*     */       //   147: iload #8
/*     */       //   149: iconst_3
/*     */       //   150: irem
/*     */       //   151: iload #8
/*     */       //   153: iconst_3
/*     */       //   154: idiv
/*     */       //   155: aload #9
/*     */       //   157: invokestatic createGridPos : (IILjava/util/List;)Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */       //   160: invokeinterface add : (Ljava/lang/Object;)Z
/*     */       //   165: pop
/*     */       //   166: iinc #8, 1
/*     */       //   169: goto -> 104
/*     */       //   172: goto -> 175
/*     */       //   175: aload_2
/*     */       //   176: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       //   #218	-> 8
/*     */       //   #219	-> 52
/*     */       //   #220	-> 58
/*     */       //   #228	-> 88
/*     */       //   #229	-> 94
/*     */       //   #230	-> 101
/*     */       //   #231	-> 116
/*     */       //   #232	-> 136
/*     */       //   #233	-> 146
/*     */       //   #230	-> 166
/*     */       //   #236	-> 172
/*     */       //   #241	-> 175
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   58	30	5	shaped	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*     */       //   136	30	9	items	Ljava/util/List;
/*     */       //   104	68	8	i	I
/*     */       //   101	71	7	ingredients	Ljava/util/List;
/*     */       //   94	81	6	shapeless	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*     */       //   0	177	0	recipe	Lnet/minecraft/world/item/crafting/display/RecipeDisplay;
/*     */       //   0	177	1	context	Lnet/minecraft/util/context/ContextMap;
/*     */       //   8	169	2	result	Ljava/util/List;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   136	30	9	items	Ljava/util/List<Lnet/minecraft/world/item/ItemStack;>;
/*     */       //   101	71	7	ingredients	Ljava/util/List<Lnet/minecraft/world/item/crafting/display/SlotDisplay;>;
/*     */       //   8	169	2	result	Ljava/util/List<Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;>;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Identifier getSprite(boolean isCraftable) {
/* 246 */       if (isCraftable) {
/* 247 */         return isHoveredOrFocused() ? HIGHLIGHTED_ENABLED_SPRITE : ENABLED_SPRITE;
/*     */       }
/* 249 */       return isHoveredOrFocused() ? HIGHLIGHTED_DISABLED_SPRITE : DISABLED_SPRITE;
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class OverlayRecipeButton
/*     */     extends AbstractWidget {
/*     */     private final RecipeDisplayId recipe;
/*     */     private final boolean isCraftable;
/*     */     private final List<Pos> slots;
/*     */     
/*     */     public OverlayRecipeButton(int x, int y, RecipeDisplayId recipe, boolean isCraftable, List<Pos> slots) {
/* 260 */       super(x, y, 24, 24, CommonComponents.EMPTY);
/* 261 */       this.slots = slots;
/* 262 */       this.recipe = recipe;
/* 263 */       this.isCraftable = isCraftable;
/*     */     }
/*     */     
/*     */     protected static Pos createGridPos(int gridXPos, int gridYPos, List<ItemStack> itemStacks) {
/* 267 */       return new Pos(3 + gridXPos * 7, 3 + gridYPos * 7, itemStacks);
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract Identifier getSprite(boolean param1Boolean);
/*     */     
/*     */     public void updateWidgetNarration(NarrationElementOutput output) {
/* 274 */       defaultButtonNarrationText(output);
/*     */     }
/*     */     
/*     */     public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a)
/*     */     {
/* 279 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getSprite(this.isCraftable), getX(), getY(), this.width, this.height);
/*     */       
/* 281 */       float gridPosX = (getX() + 2);
/* 282 */       float gridPosY = (getY() + 2);
/*     */       
/* 284 */       for (Pos pos : this.slots) {
/* 285 */         graphics.pose().pushMatrix();
/*     */         
/* 287 */         graphics.pose().translate(gridPosX + pos.x, gridPosY + pos.y);
/* 288 */         graphics.pose().scale(0.375F, 0.375F);
/* 289 */         graphics.pose().translate(-8.0F, -8.0F);
/*     */         
/* 291 */         graphics.renderItem(pos.selectIngredient(OverlayRecipeComponent.this.slotSelectTime.currentIndex()), 0, 0);
/*     */         
/* 293 */         graphics.pose().popMatrix();
/*     */       } 
/*     */     } protected static final class Pos extends Record {
/*     */       private final int x; private final int y; private final List<ItemStack> ingredients;
/* 297 */       public int x() { return this.x; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #297	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #297	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #297	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/* 297 */         //   0	8	1	o	Ljava/lang/Object; } public int y() { return this.y; } public List<ItemStack> ingredients() { return this.ingredients; }
/*     */        public Pos(int x, int y, List<ItemStack> ingredients) {
/* 299 */         if (ingredients.isEmpty())
/* 300 */           throw new IllegalArgumentException("Ingredient list must be non-empty"); 
/*     */         this.x = x;
/*     */         this.y = y;
/*     */         this.ingredients = ingredients;
/*     */       }
/* 305 */       public ItemStack selectIngredient(int currentIndex) { return this.ingredients.get(currentIndex % this.ingredients.size()); } } } protected static final class Pos extends Record { private final int x; private final int y; public ItemStack selectIngredient(int currentIndex) { return this.ingredients.get(currentIndex % this.ingredients.size()); }
/*     */ 
/*     */     
/*     */     private final List<ItemStack> ingredients;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int x() {
/*     */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/*     */       return this.y;
/*     */     }
/*     */     
/*     */     public List<ItemStack> ingredients() {
/*     */       return this.ingredients;
/*     */     }
/*     */     
/*     */     public Pos(int x, int y, List<ItemStack> ingredients) {
/*     */       if (ingredients.isEmpty())
/*     */         throw new IllegalArgumentException("Ingredient list must be non-empty"); 
/*     */       this.x = x;
/*     */       this.y = y;
/*     */       this.ingredients = ingredients;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */