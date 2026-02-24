/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmeltingRecipe extends AbstractCookingRecipe {
/*    */   public SmeltingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
/*  9 */     super(group, category, ingredient, result, experience, cookingTime);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.FURNACE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<SmeltingRecipe> getSerializer() {
/* 19 */     return RecipeSerializer.SMELTING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<SmeltingRecipe> getType() {
/* 24 */     return RecipeType.SMELTING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     switch (category()) { default: throw new MatchException(null, null);case BLOCKS: case FOOD: case MISC: break; }  return 
/*    */ 
/*    */       
/* 32 */       RecipeBookCategories.FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/SmeltingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */