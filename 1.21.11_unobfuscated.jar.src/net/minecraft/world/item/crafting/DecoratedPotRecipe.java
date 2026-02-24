/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.PotDecorations;
/*    */ 
/*    */ public class DecoratedPotRecipe extends CustomRecipe {
/*    */   public DecoratedPotRecipe(CraftingBookCategory category) {
/* 12 */     super(category);
/*    */   }
/*    */   
/*    */   private static ItemStack back(CraftingInput input) {
/* 16 */     return input.getItem(1, 0);
/*    */   }
/*    */   
/*    */   private static ItemStack left(CraftingInput input) {
/* 20 */     return input.getItem(0, 1);
/*    */   }
/*    */   
/*    */   private static ItemStack right(CraftingInput input) {
/* 24 */     return input.getItem(2, 1);
/*    */   }
/*    */   
/*    */   private static ItemStack front(CraftingInput input) {
/* 28 */     return input.getItem(1, 2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 34 */     if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 4) {
/* 35 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 39 */     return (back(input).is(ItemTags.DECORATED_POT_INGREDIENTS) && 
/* 40 */       left(input).is(ItemTags.DECORATED_POT_INGREDIENTS) && 
/* 41 */       right(input).is(ItemTags.DECORATED_POT_INGREDIENTS) && 
/* 42 */       front(input).is(ItemTags.DECORATED_POT_INGREDIENTS));
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 47 */     PotDecorations decorations = new PotDecorations(
/* 48 */         back(input).getItem(), 
/* 49 */         left(input).getItem(), 
/* 50 */         right(input).getItem(), 
/* 51 */         front(input).getItem());
/*    */     
/* 53 */     return DecoratedPotBlockEntity.createDecoratedPotItem(decorations);
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<DecoratedPotRecipe> getSerializer() {
/* 58 */     return RecipeSerializer.DECORATED_POT_RECIPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/DecoratedPotRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */