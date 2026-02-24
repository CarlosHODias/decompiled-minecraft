/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
/*    */ 
/*    */ public class StonecutterRecipe
/*    */   extends SingleItemRecipe {
/*    */   public StonecutterRecipe(String group, Ingredient ingredient, ItemStack result) {
/* 13 */     super(group, ingredient, result);
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<StonecutterRecipe> getType() {
/* 18 */     return RecipeType.STONECUTTING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<StonecutterRecipe> getSerializer() {
/* 23 */     return RecipeSerializer.STONECUTTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 28 */     return (List)List.of(new StonecutterRecipeDisplay(
/* 29 */           input().display(), 
/* 30 */           resultDisplay(), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.STONECUTTER)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotDisplay resultDisplay() {
/* 36 */     return (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(result());
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 41 */     return RecipeBookCategories.STONECUTTER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/StonecutterRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */