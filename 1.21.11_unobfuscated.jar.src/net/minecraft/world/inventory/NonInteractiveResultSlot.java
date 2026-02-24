/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class NonInteractiveResultSlot
/*    */   extends Slot
/*    */ {
/*    */   public NonInteractiveResultSlot(Container container, int id, int x, int y) {
/* 12 */     super(container, id, x, y);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onQuickCraft(ItemStack picked, ItemStack original) {}
/*    */ 
/*    */   
/*    */   public boolean mayPickup(Player player) {
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<ItemStack> tryRemove(int amount, int maxAmount, Player player) {
/* 26 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack safeTake(int amount, int maxAmount, Player player) {
/* 31 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack safeInsert(ItemStack stack) {
/* 36 */     return stack;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack safeInsert(ItemStack inputStack, int inputAmount) {
/* 41 */     return safeInsert(inputStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean allowModification(Player player) {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int amount) {
/* 56 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {}
/*    */ 
/*    */   
/*    */   public boolean isHighlightable() {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFake() {
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/NonInteractiveResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */