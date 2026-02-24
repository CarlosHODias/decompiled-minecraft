/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SlotProvider;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Container
/*     */   extends Clearable, Iterable<ItemStack>, SlotProvider
/*     */ {
/*     */   public static final float DEFAULT_DISTANCE_BUFFER = 4.0F;
/*     */   
/*     */   default int getMaxStackSize() {
/*  36 */     return 99;
/*     */   }
/*     */   
/*     */   default int getMaxStackSize(ItemStack itemStack) {
/*  40 */     return Math.min(getMaxStackSize(), itemStack.getMaxStackSize());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default void startOpen(ContainerUser containerUser) {}
/*     */ 
/*     */ 
/*     */   
/*     */   default void stopOpen(ContainerUser containerUser) {}
/*     */ 
/*     */ 
/*     */   
/*     */   default List<ContainerUser> getEntitiesWithContainerOpen() {
/*  54 */     return List.of();
/*     */   }
/*     */   
/*     */   default boolean canPlaceItem(int slot, ItemStack itemStack) {
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   default boolean canTakeItem(Container into, int slot, ItemStack itemStack) {
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   default int countItem(Item item) {
/*  66 */     int count = 0;
/*  67 */     for (ItemStack slotItem : (Iterable<ItemStack>)this) {
/*  68 */       if (slotItem.getItem().equals(item)) {
/*  69 */         count += slotItem.getCount();
/*     */       }
/*     */     } 
/*  72 */     return count;
/*     */   }
/*     */   
/*     */   default boolean hasAnyOf(Set<Item> item) {
/*  76 */     return hasAnyMatching(stack -> (!stack.isEmpty() && item.contains(stack.getItem())));
/*     */   }
/*     */   
/*     */   default boolean hasAnyMatching(Predicate<ItemStack> predicate) {
/*  80 */     for (ItemStack slotItem : (Iterable<ItemStack>)this) {
/*  81 */       if (predicate.test(slotItem)) {
/*  82 */         return true;
/*     */       }
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   static boolean stillValidBlockEntity(BlockEntity blockEntity, Player player) {
/*  89 */     return stillValidBlockEntity(blockEntity, player, 4.0F);
/*     */   }
/*     */   
/*     */   static boolean stillValidBlockEntity(BlockEntity blockEntity, Player player, float distanceBuffer) {
/*  93 */     Level level = blockEntity.getLevel();
/*  94 */     BlockPos worldPosition = blockEntity.getBlockPos();
/*     */     
/*  96 */     if (level == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (level.getBlockEntity(worldPosition) != blockEntity) {
/* 100 */       return false;
/*     */     }
/*     */     
/* 103 */     return player.isWithinBlockInteractionRange(worldPosition, distanceBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   default SlotAccess getSlot(final int slot) {
/* 108 */     if (slot < 0 || slot >= getContainerSize()) {
/* 109 */       return null;
/*     */     }
/* 111 */     return new SlotAccess()
/*     */       {
/*     */         public ItemStack get() {
/* 114 */           return Container.this.getItem(slot);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean set(ItemStack itemStack) {
/* 119 */           Container.this.setItem(slot, itemStack);
/* 120 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static class ContainerIterator implements Iterator<ItemStack> {
/*     */     private final Container container;
/*     */     private int index;
/*     */     private final int size;
/*     */     
/*     */     public ContainerIterator(Container container) {
/* 131 */       this.container = container;
/* 132 */       this.size = container.getContainerSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 137 */       return (this.index < this.size);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack next() {
/* 142 */       if (!hasNext()) {
/* 143 */         throw new NoSuchElementException();
/*     */       }
/* 145 */       return this.container.getItem(this.index++);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   default Iterator<ItemStack> iterator() {
/* 151 */     return new ContainerIterator(this);
/*     */   }
/*     */   
/*     */   int getContainerSize();
/*     */   
/*     */   boolean isEmpty();
/*     */   
/*     */   ItemStack getItem(int paramInt);
/*     */   
/*     */   ItemStack removeItem(int paramInt1, int paramInt2);
/*     */   
/*     */   ItemStack removeItemNoUpdate(int paramInt);
/*     */   
/*     */   void setItem(int paramInt, ItemStack paramItemStack);
/*     */   
/*     */   void setChanged();
/*     */   
/*     */   boolean stillValid(Player paramPlayer);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/Container.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */