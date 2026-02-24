/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FireworkStarFadeRecipe extends CustomRecipe {
/* 15 */   private static final Ingredient STAR_INGREDIENT = Ingredient.of((ItemLike)Items.FIREWORK_STAR);
/*    */   
/*    */   public FireworkStarFadeRecipe(CraftingBookCategory category) {
/* 18 */     super(category);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 24 */     if (input.ingredientCount() < 2) {
/* 25 */       return false;
/*    */     }
/*    */     
/*    */     boolean color = false;
/*    */     
/*    */     boolean star = false;
/* 31 */     for (int slot = 0; slot < input.size(); slot++) {
/* 32 */       ItemStack itemStack = input.getItem(slot);
/* 33 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 37 */         if (itemStack.getItem() instanceof DyeItem) {
/* 38 */           color = true;
/* 39 */         } else if (STAR_INGREDIENT.test(itemStack)) {
/* 40 */           if (star) {
/* 41 */             return false;
/*    */           }
/* 43 */           star = true;
/*    */         } else {
/* 45 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 49 */     return (star && color);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 54 */     IntArrayList intArrayList = new IntArrayList();
/* 55 */     ItemStack result = null;
/*    */     
/* 57 */     for (int slot = 0; slot < input.size(); slot++) {
/* 58 */       ItemStack itemStack = input.getItem(slot);
/*    */       
/* 60 */       Item item = itemStack.getItem();
/* 61 */       if (item instanceof DyeItem) { DyeItem dyeItem = (DyeItem)item;
/* 62 */         intArrayList.add(dyeItem.getDyeColor().getFireworkColor()); }
/* 63 */       else if (STAR_INGREDIENT.test(itemStack))
/* 64 */       { result = itemStack.copyWithCount(1); }
/*    */     
/*    */     } 
/*    */ 
/*    */     
/* 69 */     if (result == null || intArrayList.isEmpty()) {
/* 70 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 73 */     result.update(DataComponents.FIREWORK_EXPLOSION, FireworkExplosion.DEFAULT, intArrayList, FireworkExplosion::withFadeColors);
/* 74 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<FireworkStarFadeRecipe> getSerializer() {
/* 79 */     return RecipeSerializer.FIREWORK_STAR_FADE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/FireworkStarFadeRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */