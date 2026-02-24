/*    */ package net.minecraft.client.gui.screens.recipebook;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.components.WidgetSprites;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.inventory.AbstractCraftingMenu;
/*    */ import net.minecraft.world.inventory.Slot;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategories;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ 
/*    */ 
/*    */ public class CraftingRecipeBookComponent
/*    */   extends RecipeBookComponent<AbstractCraftingMenu>
/*    */ {
/* 20 */   private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
/* 21 */       Identifier.withDefaultNamespace("recipe_book/filter_enabled"), 
/* 22 */       Identifier.withDefaultNamespace("recipe_book/filter_disabled"), 
/* 23 */       Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"), 
/* 24 */       Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"));
/*    */ 
/*    */   
/* 27 */   private static final Component ONLY_CRAFTABLES_TOOLTIP = (Component)Component.translatable("gui.recipebook.toggleRecipes.craftable");
/*    */   
/* 29 */   private static final List<RecipeBookComponent.TabInfo> TABS = List.of(new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.CRAFTING), new RecipeBookComponent.TabInfo(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT), new RecipeBookComponent.TabInfo(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS), new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC), new RecipeBookComponent.TabInfo(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CraftingRecipeBookComponent(AbstractCraftingMenu menu) {
/* 38 */     super(menu, TABS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isCraftingSlot(Slot slot) {
/* 43 */     return (this.menu.getResultSlot() == slot || this.menu.getInputGridSlots().contains(slot));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean canDisplay(RecipeDisplay display) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   4: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   7: invokevirtual getGridWidth : ()I
/*    */     //   10: istore_2
/*    */     //   11: aload_0
/*    */     //   12: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   15: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   18: invokevirtual getGridHeight : ()I
/*    */     //   21: istore_3
/*    */     //   22: aload_1
/*    */     //   23: dup
/*    */     //   24: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   27: pop
/*    */     //   28: astore #4
/*    */     //   30: iconst_0
/*    */     //   31: istore #5
/*    */     //   33: aload #4
/*    */     //   35: iload #5
/*    */     //   37: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*    */     //   42: lookupswitch default -> 132, 0 -> 68, 1 -> 101
/*    */     //   68: aload #4
/*    */     //   70: checkcast net/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay
/*    */     //   73: astore #6
/*    */     //   75: iload_2
/*    */     //   76: aload #6
/*    */     //   78: invokevirtual width : ()I
/*    */     //   81: if_icmplt -> 97
/*    */     //   84: iload_3
/*    */     //   85: aload #6
/*    */     //   87: invokevirtual height : ()I
/*    */     //   90: if_icmplt -> 97
/*    */     //   93: iconst_1
/*    */     //   94: goto -> 133
/*    */     //   97: iconst_0
/*    */     //   98: goto -> 133
/*    */     //   101: aload #4
/*    */     //   103: checkcast net/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay
/*    */     //   106: astore #7
/*    */     //   108: iload_2
/*    */     //   109: iload_3
/*    */     //   110: imul
/*    */     //   111: aload #7
/*    */     //   113: invokevirtual ingredients : ()Ljava/util/List;
/*    */     //   116: invokeinterface size : ()I
/*    */     //   121: if_icmplt -> 128
/*    */     //   124: iconst_1
/*    */     //   125: goto -> 133
/*    */     //   128: iconst_0
/*    */     //   129: goto -> 133
/*    */     //   132: iconst_0
/*    */     //   133: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     //   #48	-> 11
/*    */     //   #50	-> 22
/*    */     //   #51	-> 68
/*    */     //   #52	-> 75
/*    */     //   #53	-> 101
/*    */     //   #54	-> 108
/*    */     //   #55	-> 132
/*    */     //   #50	-> 133
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   75	26	6	shaped	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*    */     //   108	24	7	shapeless	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*    */     //   0	134	0	this	Lnet/minecraft/client/gui/screens/recipebook/CraftingRecipeBookComponent;
/*    */     //   0	134	1	display	Lnet/minecraft/world/item/crafting/display/RecipeDisplay;
/*    */     //   11	123	2	gridWidth	I
/*    */     //   22	112	3	gridHeight	I
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: aload_0
/*    */     //   2: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   5: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   8: invokevirtual getResultSlot : ()Lnet/minecraft/world/inventory/Slot;
/*    */     //   11: aload_3
/*    */     //   12: aload_2
/*    */     //   13: invokeinterface result : ()Lnet/minecraft/world/item/crafting/display/SlotDisplay;
/*    */     //   18: invokevirtual setResult : (Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/util/context/ContextMap;Lnet/minecraft/world/item/crafting/display/SlotDisplay;)V
/*    */     //   21: aload_2
/*    */     //   22: dup
/*    */     //   23: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   26: pop
/*    */     //   27: astore #4
/*    */     //   29: iconst_0
/*    */     //   30: istore #5
/*    */     //   32: aload #4
/*    */     //   34: iload #5
/*    */     //   36: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*    */     //   41: lookupswitch default -> 229, 0 -> 68, 1 -> 137
/*    */     //   68: aload #4
/*    */     //   70: checkcast net/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay
/*    */     //   73: astore #6
/*    */     //   75: aload_0
/*    */     //   76: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   79: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   82: invokevirtual getInputGridSlots : ()Ljava/util/List;
/*    */     //   85: astore #7
/*    */     //   87: aload_0
/*    */     //   88: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   91: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   94: invokevirtual getGridWidth : ()I
/*    */     //   97: aload_0
/*    */     //   98: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   101: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   104: invokevirtual getGridHeight : ()I
/*    */     //   107: aload #6
/*    */     //   109: invokevirtual width : ()I
/*    */     //   112: aload #6
/*    */     //   114: invokevirtual height : ()I
/*    */     //   117: aload #6
/*    */     //   119: invokevirtual ingredients : ()Ljava/util/List;
/*    */     //   122: aload #7
/*    */     //   124: aload_1
/*    */     //   125: aload_3
/*    */     //   126: <illegal opcode> addItemToSlot : (Ljava/util/List;Lnet/minecraft/client/gui/screens/recipebook/GhostSlots;Lnet/minecraft/util/context/ContextMap;)Lnet/minecraft/recipebook/PlaceRecipeHelper$Output;
/*    */     //   131: invokestatic placeRecipe : (IIIILjava/lang/Iterable;Lnet/minecraft/recipebook/PlaceRecipeHelper$Output;)V
/*    */     //   134: goto -> 229
/*    */     //   137: aload #4
/*    */     //   139: checkcast net/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay
/*    */     //   142: astore #7
/*    */     //   144: aload_0
/*    */     //   145: getfield menu : Lnet/minecraft/world/inventory/RecipeBookMenu;
/*    */     //   148: checkcast net/minecraft/world/inventory/AbstractCraftingMenu
/*    */     //   151: invokevirtual getInputGridSlots : ()Ljava/util/List;
/*    */     //   154: astore #8
/*    */     //   156: aload #7
/*    */     //   158: invokevirtual ingredients : ()Ljava/util/List;
/*    */     //   161: invokeinterface size : ()I
/*    */     //   166: aload #8
/*    */     //   168: invokeinterface size : ()I
/*    */     //   173: invokestatic min : (II)I
/*    */     //   176: istore #9
/*    */     //   178: iconst_0
/*    */     //   179: istore #10
/*    */     //   181: iload #10
/*    */     //   183: iload #9
/*    */     //   185: if_icmpge -> 226
/*    */     //   188: aload_1
/*    */     //   189: aload #8
/*    */     //   191: iload #10
/*    */     //   193: invokeinterface get : (I)Ljava/lang/Object;
/*    */     //   198: checkcast net/minecraft/world/inventory/Slot
/*    */     //   201: aload_3
/*    */     //   202: aload #7
/*    */     //   204: invokevirtual ingredients : ()Ljava/util/List;
/*    */     //   207: iload #10
/*    */     //   209: invokeinterface get : (I)Ljava/lang/Object;
/*    */     //   214: checkcast net/minecraft/world/item/crafting/display/SlotDisplay
/*    */     //   217: invokevirtual setInput : (Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/util/context/ContextMap;Lnet/minecraft/world/item/crafting/display/SlotDisplay;)V
/*    */     //   220: iinc #10, 1
/*    */     //   223: goto -> 181
/*    */     //   226: goto -> 229
/*    */     //   229: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     //   #62	-> 21
/*    */     //   #63	-> 68
/*    */     //   #64	-> 75
/*    */     //   #65	-> 87
/*    */     //   #70	-> 134
/*    */     //   #71	-> 137
/*    */     //   #72	-> 144
/*    */     //   #73	-> 156
/*    */     //   #74	-> 178
/*    */     //   #75	-> 188
/*    */     //   #74	-> 220
/*    */     //   #77	-> 226
/*    */     //   #82	-> 229
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   87	47	7	inputSlots	Ljava/util/List;
/*    */     //   75	62	6	shaped	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*    */     //   181	45	10	i	I
/*    */     //   156	70	8	inputSlots	Ljava/util/List;
/*    */     //   178	48	9	slotCount	I
/*    */     //   144	85	7	shapeless	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*    */     //   0	230	0	this	Lnet/minecraft/client/gui/screens/recipebook/CraftingRecipeBookComponent;
/*    */     //   0	230	1	ghostSlots	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots;
/*    */     //   0	230	2	recipe	Lnet/minecraft/world/item/crafting/display/RecipeDisplay;
/*    */     //   0	230	3	context	Lnet/minecraft/util/context/ContextMap;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   87	47	7	inputSlots	Ljava/util/List<Lnet/minecraft/world/inventory/Slot;>;
/*    */     //   156	70	8	inputSlots	Ljava/util/List<Lnet/minecraft/world/inventory/Slot;>;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WidgetSprites getFilterButtonTextures() {
/* 86 */     return FILTER_BUTTON_SPRITES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getRecipeFilterName() {
/* 91 */     return ONLY_CRAFTABLES_TOOLTIP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
/* 96 */     collection.selectRecipes(stackedContents, this::canDisplay);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/CraftingRecipeBookComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */