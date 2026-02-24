/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface CraftingRecipe
/*    */   extends Recipe<CraftingInput> {
/*    */   default RecipeType<CraftingRecipe> getType() {
/* 10 */     return RecipeType.CRAFTING;
/*    */   }
/*    */ 
/*    */   
/*    */   RecipeSerializer<? extends CraftingRecipe> getSerializer();
/*    */ 
/*    */   
/*    */   CraftingBookCategory category();
/*    */   
/*    */   default NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
/* 20 */     return defaultCraftingReminder(input);
/*    */   }
/*    */   
/*    */   static NonNullList<ItemStack> defaultCraftingReminder(CraftingInput input) {
/* 24 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/*    */     
/* 26 */     for (int slot = 0; slot < result.size(); slot++) {
/* 27 */       Item item = input.getItem(slot).getItem();
/* 28 */       result.set(slot, item.getCraftingRemainder());
/*    */     } 
/*    */     
/* 31 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   default RecipeBookCategory recipeBookCategory() {
/* 36 */     switch (category()) { default: throw new MatchException(null, null);case BUILDING: case EQUIPMENT: case REDSTONE: case MISC: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 40 */       RecipeBookCategories.CRAFTING_MISC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/CraftingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */