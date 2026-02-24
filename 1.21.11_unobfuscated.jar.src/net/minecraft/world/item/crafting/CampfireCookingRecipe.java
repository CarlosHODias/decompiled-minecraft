/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class CampfireCookingRecipe extends AbstractCookingRecipe {
/*    */   public CampfireCookingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
/*  9 */     super(group, category, ingredient, result, experience, cookingTime);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.CAMPFIRE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<CampfireCookingRecipe> getSerializer() {
/* 19 */     return RecipeSerializer.CAMPFIRE_COOKING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<CampfireCookingRecipe> getType() {
/* 24 */     return RecipeType.CAMPFIRE_COOKING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     return RecipeBookCategories.CAMPFIRE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/CampfireCookingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */