/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public class ResultContainer implements Container, RecipeCraftingHolder {
/* 12 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
/*    */   
/*    */   private RecipeHolder<?> recipeUsed;
/*    */   
/*    */   public int getContainerSize() {
/* 17 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 22 */     for (ItemStack itemStack : this.itemStacks) {
/* 23 */       if (!itemStack.isEmpty()) {
/* 24 */         return false;
/*    */       }
/*    */     } 
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getItem(int slot) {
/* 32 */     return (ItemStack)this.itemStacks.get(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItem(int slot, int count) {
/* 37 */     return ContainerHelper.takeItem((List)this.itemStacks, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItemNoUpdate(int slot) {
/* 42 */     return ContainerHelper.takeItem((List)this.itemStacks, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setItem(int slot, ItemStack itemStack) {
/* 47 */     this.itemStacks.set(0, itemStack);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChanged() {}
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player player) {
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearContent() {
/* 61 */     this.itemStacks.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRecipeUsed(RecipeHolder<?> recipeUsed) {
/* 66 */     this.recipeUsed = recipeUsed;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeHolder<?> getRecipeUsed() {
/* 71 */     return this.recipeUsed;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ResultContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */