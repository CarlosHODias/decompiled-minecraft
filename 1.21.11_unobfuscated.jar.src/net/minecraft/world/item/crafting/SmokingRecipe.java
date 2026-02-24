/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmokingRecipe extends AbstractCookingRecipe {
/*    */   public SmokingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
/*  9 */     super(group, category, ingredient, result, experience, cookingTime);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.SMOKER;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<SmokingRecipe> getType() {
/* 19 */     return RecipeType.SMOKING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<SmokingRecipe> getSerializer() {
/* 24 */     return RecipeSerializer.SMOKING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     return RecipeBookCategories.SMOKER_FOOD;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/SmokingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */