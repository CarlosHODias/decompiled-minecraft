/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class BlastingRecipe extends AbstractCookingRecipe {
/*    */   public BlastingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
/*  9 */     super(group, category, ingredient, result, experience, cookingTime);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.BLAST_FURNACE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<BlastingRecipe> getSerializer() {
/* 19 */     return RecipeSerializer.BLASTING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<BlastingRecipe> getType() {
/* 24 */     return RecipeType.BLASTING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     switch (category()) { default: throw new MatchException(null, null);case BLOCKS: case FOOD: case MISC: break; }  return 
/*    */       
/* 31 */       RecipeBookCategories.BLAST_FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/BlastingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */