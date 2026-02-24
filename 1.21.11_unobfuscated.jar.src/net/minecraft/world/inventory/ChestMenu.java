/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ChestMenu extends AbstractContainerMenu {
/*     */   private final Container container;
/*     */   
/*     */   private ChestMenu(MenuType<?> menuType, int containerId, Inventory inventory, int rows) {
/*  14 */     this(menuType, containerId, inventory, (Container)new SimpleContainer(9 * rows), rows);
/*     */   }
/*     */   private final int containerRows;
/*     */   public static ChestMenu oneRow(int containerId, Inventory inventory) {
/*  18 */     return new ChestMenu(MenuType.GENERIC_9x1, containerId, inventory, 1);
/*     */   }
/*     */   
/*     */   public static ChestMenu twoRows(int containerId, Inventory inventory) {
/*  22 */     return new ChestMenu(MenuType.GENERIC_9x2, containerId, inventory, 2);
/*     */   }
/*     */   
/*     */   public static ChestMenu threeRows(int containerId, Inventory inventory) {
/*  26 */     return new ChestMenu(MenuType.GENERIC_9x3, containerId, inventory, 3);
/*     */   }
/*     */   
/*     */   public static ChestMenu fourRows(int containerId, Inventory inventory) {
/*  30 */     return new ChestMenu(MenuType.GENERIC_9x4, containerId, inventory, 4);
/*     */   }
/*     */   
/*     */   public static ChestMenu fiveRows(int containerId, Inventory inventory) {
/*  34 */     return new ChestMenu(MenuType.GENERIC_9x5, containerId, inventory, 5);
/*     */   }
/*     */   
/*     */   public static ChestMenu sixRows(int containerId, Inventory inventory) {
/*  38 */     return new ChestMenu(MenuType.GENERIC_9x6, containerId, inventory, 6);
/*     */   }
/*     */   
/*     */   public static ChestMenu threeRows(int containerId, Inventory inventory, Container container) {
/*  42 */     return new ChestMenu(MenuType.GENERIC_9x3, containerId, inventory, container, 3);
/*     */   }
/*     */   
/*     */   public static ChestMenu sixRows(int containerId, Inventory inventory, Container container) {
/*  46 */     return new ChestMenu(MenuType.GENERIC_9x6, containerId, inventory, container, 6);
/*     */   }
/*     */   
/*     */   public ChestMenu(MenuType<?> menuType, int containerId, Inventory inventory, Container container, int rows) {
/*  50 */     super(menuType, containerId);
/*  51 */     checkContainerSize(container, rows * 9);
/*  52 */     this.container = container;
/*  53 */     this.containerRows = rows;
/*  54 */     container.startOpen((ContainerUser)inventory.player);
/*     */     
/*  56 */     int chestGridTop = 18;
/*  57 */     addChestGrid(container, 8, 18);
/*     */     
/*  59 */     int inventoryTop = 18 + this.containerRows * 18 + 13;
/*  60 */     addStandardInventorySlots((Container)inventory, 8, inventoryTop);
/*     */   }
/*     */   
/*     */   private void addChestGrid(Container container, int left, int top) {
/*  64 */     for (int y = 0; y < this.containerRows; y++) {
/*  65 */       for (int x = 0; x < 9; x++) {
/*  66 */         addSlot(new Slot(container, x + y * 9, left + x * 18, top + y * 18));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  73 */     return this.container.stillValid(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  78 */     ItemStack clicked = ItemStack.EMPTY;
/*  79 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  80 */     if (slot != null && slot.hasItem()) {
/*  81 */       ItemStack stack = slot.getItem();
/*  82 */       clicked = stack.copy();
/*     */       
/*  84 */       if (slotIndex < this.containerRows * 9) {
/*  85 */         if (!moveItemStackTo(stack, this.containerRows * 9, this.slots.size(), true)) {
/*  86 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/*  89 */       else if (!moveItemStackTo(stack, 0, this.containerRows * 9, false)) {
/*  90 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/*  93 */       if (stack.isEmpty()) {
/*  94 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/*  96 */         slot.setChanged();
/*     */       } 
/*     */     } 
/*  99 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 104 */     super.removed(player);
/* 105 */     this.container.stopOpen((ContainerUser)player);
/*     */   }
/*     */   
/*     */   public Container getContainer() {
/* 109 */     return this.container;
/*     */   }
/*     */   
/*     */   public int getRowCount() {
/* 113 */     return this.containerRows;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ChestMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */