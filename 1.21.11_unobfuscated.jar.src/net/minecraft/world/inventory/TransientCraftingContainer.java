/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class TransientCraftingContainer implements CraftingContainer {
/*     */   private final NonNullList<ItemStack> items;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final AbstractContainerMenu menu;
/*     */   
/*     */   public TransientCraftingContainer(AbstractContainerMenu menu, int width, int height) {
/*  18 */     this(menu, width, height, NonNullList.withSize(width * height, ItemStack.EMPTY));
/*     */   }
/*     */   
/*     */   private TransientCraftingContainer(AbstractContainerMenu menu, int width, int height, NonNullList<ItemStack> items) {
/*  22 */     this.items = items;
/*  23 */     this.menu = menu;
/*  24 */     this.width = width;
/*  25 */     this.height = height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  30 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  35 */     for (ItemStack itemStack : this.items) {
/*  36 */       if (!itemStack.isEmpty()) {
/*  37 */         return false;
/*     */       }
/*     */     } 
/*  40 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  45 */     if (slot >= getContainerSize()) {
/*  46 */       return ItemStack.EMPTY;
/*     */     }
/*  48 */     return (ItemStack)this.items.get(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  53 */     return ContainerHelper.takeItem((List)this.items, slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  58 */     ItemStack result = ContainerHelper.removeItem((List)this.items, slot, count);
/*  59 */     if (!result.isEmpty()) {
/*  60 */       this.menu.slotsChanged(this);
/*     */     }
/*  62 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  67 */     this.items.set(slot, itemStack);
/*  68 */     this.menu.slotsChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/*  82 */     this.items.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  87 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  92 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ItemStack> getItems() {
/*  97 */     return List.copyOf((Collection<? extends ItemStack>)this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 102 */     for (ItemStack itemStack : this.items)
/* 103 */       contents.accountSimpleStack(itemStack); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/TransientCraftingContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */