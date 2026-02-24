/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public abstract class ItemCombinerMenu extends AbstractContainerMenu {
/*     */   private static final int INVENTORY_SLOTS_PER_ROW = 9;
/*     */   private static final int INVENTORY_ROWS = 3;
/*     */   private static final int INPUT_SLOT_START = 0;
/*     */   protected final ContainerLevelAccess access;
/*     */   protected final Player player;
/*     */   protected final Container inputSlots;
/*     */   
/*  20 */   protected final ResultContainer resultSlots = new ResultContainer()
/*     */     {
/*     */       public void setChanged() {
/*  23 */         ItemCombinerMenu.this.slotsChanged(this);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   protected boolean mayPickup(Player player, boolean hasItem) {
/*  29 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int resultSlotIndex;
/*     */ 
/*     */   
/*     */   public ItemCombinerMenu(MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
/*  37 */     super(menuType, containerId);
/*  38 */     this.access = access;
/*  39 */     this.player = inventory.player;
/*     */     
/*  41 */     this.inputSlots = (Container)createContainer(itemInputSlots.getNumOfInputSlots());
/*  42 */     this.resultSlotIndex = itemInputSlots.getResultSlotIndex();
/*     */     
/*  44 */     createInputSlots(itemInputSlots);
/*  45 */     createResultSlot(itemInputSlots);
/*     */ 
/*     */     
/*  48 */     addStandardInventorySlots((Container)inventory, 8, 84);
/*     */   }
/*     */   
/*     */   private void createInputSlots(ItemCombinerMenuSlotDefinition itemInputSlots) {
/*  52 */     for (ItemCombinerMenuSlotDefinition.SlotDefinition slot : itemInputSlots.getSlots()) {
/*  53 */       addSlot(new Slot(this, this.inputSlots, slot.slotIndex(), slot.x(), slot.y())
/*     */           {
/*     */             public boolean mayPlace(ItemStack itemStack) {
/*  56 */               return slot.mayPlace().test(itemStack);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createResultSlot(ItemCombinerMenuSlotDefinition itemInputSlots) {
/*  63 */     addSlot(new Slot(this.resultSlots, itemInputSlots.getResultSlot().slotIndex(), itemInputSlots.getResultSlot().x(), itemInputSlots.getResultSlot().y())
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  66 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean mayPickup(Player player) {
/*  71 */             return ItemCombinerMenu.this.mayPickup(player, hasItem());
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/*  76 */             ItemCombinerMenu.this.onTake(player, carried);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SimpleContainer createContainer(int size) {
/*  84 */     return new SimpleContainer(size)
/*     */       {
/*     */         public void setChanged() {
/*  87 */           super.setChanged();
/*  88 */           ItemCombinerMenu.this.slotsChanged((Container)this);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/*  95 */     super.slotsChanged(container);
/*     */     
/*  97 */     if (container == this.inputSlots) {
/*  98 */       createResult();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 104 */     super.removed(player);
/* 105 */     this.access.execute((level, pos) -> clearContainer(player, this.inputSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 110 */     return (Boolean)this.access.<Boolean>evaluate((level, pos) -> !isValidBlock(player.getBlockState(pos)) ? false : player.isWithinBlockInteractionRange(pos, 4.0D), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 120 */     ItemStack clicked = ItemStack.EMPTY;
/* 121 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 122 */     if (slot != null && slot.hasItem()) {
/* 123 */       ItemStack stack = slot.getItem();
/* 124 */       clicked = stack.copy();
/*     */       
/* 126 */       int inventorySlotStart = getInventorySlotStart();
/* 127 */       int useRowSlotEnd = getUseRowEnd();
/* 128 */       if (slotIndex == getResultSlot()) {
/* 129 */         if (!moveItemStackTo(stack, inventorySlotStart, useRowSlotEnd, true)) {
/* 130 */           return ItemStack.EMPTY;
/*     */         }
/* 132 */         slot.onQuickCraft(stack, clicked);
/* 133 */       } else if (slotIndex >= 0 && slotIndex < getResultSlot()) {
/* 134 */         if (!moveItemStackTo(stack, inventorySlotStart, useRowSlotEnd, false)) {
/* 135 */           return ItemStack.EMPTY;
/*     */         }
/* 137 */       } else if (canMoveIntoInputSlots(stack) && slotIndex >= getInventorySlotStart() && slotIndex < getUseRowEnd()) {
/* 138 */         if (!moveItemStackTo(stack, 0, getResultSlot(), false)) {
/* 139 */           return ItemStack.EMPTY;
/*     */         }
/* 141 */       } else if (slotIndex >= getInventorySlotStart() && slotIndex < getInventorySlotEnd()) {
/* 142 */         if (!moveItemStackTo(stack, getUseRowStart(), getUseRowEnd(), false)) {
/* 143 */           return ItemStack.EMPTY;
/*     */         }
/* 145 */       } else if (slotIndex >= getUseRowStart() && slotIndex < getUseRowEnd() && 
/* 146 */         !moveItemStackTo(stack, getInventorySlotStart(), getInventorySlotEnd(), false)) {
/* 147 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 151 */       if (stack.isEmpty()) {
/* 152 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 154 */         slot.setChanged();
/*     */       } 
/* 156 */       if (stack.getCount() == clicked.getCount()) {
/* 157 */         return ItemStack.EMPTY;
/*     */       }
/* 159 */       slot.onTake(player, stack);
/*     */     } 
/*     */     
/* 162 */     return clicked;
/*     */   }
/*     */   
/*     */   protected boolean canMoveIntoInputSlots(ItemStack stack) {
/* 166 */     return true;
/*     */   }
/*     */   
/*     */   public int getResultSlot() {
/* 170 */     return this.resultSlotIndex;
/*     */   }
/*     */   
/*     */   private int getInventorySlotStart() {
/* 174 */     return getResultSlot() + 1;
/*     */   }
/*     */   
/*     */   private int getInventorySlotEnd() {
/* 178 */     return getInventorySlotStart() + 27;
/*     */   }
/*     */   
/*     */   private int getUseRowStart() {
/* 182 */     return getInventorySlotEnd();
/*     */   }
/*     */   
/*     */   private int getUseRowEnd() {
/* 186 */     return getUseRowStart() + 9;
/*     */   }
/*     */   
/*     */   protected abstract void onTake(Player paramPlayer, ItemStack paramItemStack);
/*     */   
/*     */   protected abstract boolean isValidBlock(BlockState paramBlockState);
/*     */   
/*     */   public abstract void createResult();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ItemCombinerMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */