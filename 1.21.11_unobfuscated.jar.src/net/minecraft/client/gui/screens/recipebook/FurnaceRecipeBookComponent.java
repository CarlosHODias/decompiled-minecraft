/*    */ package net.minecraft.client.gui.screens.recipebook;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.components.WidgetSprites;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.inventory.AbstractFurnaceMenu;
/*    */ import net.minecraft.world.inventory.Slot;
/*    */ import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ 
/*    */ public class FurnaceRecipeBookComponent
/*    */   extends RecipeBookComponent<AbstractFurnaceMenu> {
/* 16 */   private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
/* 17 */       Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"), 
/* 18 */       Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"), 
/* 19 */       Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"), 
/* 20 */       Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted"));
/*    */   
/*    */   private final Component recipeFilterName;
/*    */ 
/*    */   
/*    */   public FurnaceRecipeBookComponent(AbstractFurnaceMenu menu, Component recipeFilterName, List<RecipeBookComponent.TabInfo> tabInfos) {
/* 26 */     super(menu, tabInfos);
/* 27 */     this.recipeFilterName = recipeFilterName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected WidgetSprites getFilterButtonTextures() {
/* 32 */     return FILTER_SPRITES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isCraftingSlot(Slot slot) {
/* 37 */     switch (slot.index) { case 0: case 1: case 2: default: break; }  return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
/* 45 */     ghostSlots.setResult(this.menu.getResultSlot(), context, recipe.result());
/*    */     
/* 47 */     if (recipe instanceof FurnaceRecipeDisplay) { FurnaceRecipeDisplay furnaceRecipe = (FurnaceRecipeDisplay)recipe;
/* 48 */       ghostSlots.setInput((Slot)this.menu.slots.get(0), context, furnaceRecipe.ingredient());
/* 49 */       Slot fuelSlot = (Slot)this.menu.slots.get(1);
/* 50 */       if (fuelSlot.getItem().isEmpty()) {
/* 51 */         ghostSlots.setInput(fuelSlot, context, furnaceRecipe.fuel());
/*    */       } }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Component getRecipeFilterName() {
/* 59 */     return this.recipeFilterName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
/* 64 */     collection.selectRecipes(stackedContents, display -> display instanceof FurnaceRecipeDisplay);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/FurnaceRecipeBookComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */