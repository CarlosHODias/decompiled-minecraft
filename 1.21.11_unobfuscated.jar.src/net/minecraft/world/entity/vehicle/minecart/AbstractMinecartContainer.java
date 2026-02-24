/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractMinecartContainer
/*     */   extends AbstractMinecart implements ContainerEntity {
/*  27 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(36, ItemStack.EMPTY);
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*     */   protected AbstractMinecartContainer(EntityType<?> type, Level level) {
/*  32 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  37 */     super.destroy(level, source);
/*  38 */     chestVehicleDestroyed(source, level, (Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  43 */     return getChestVehicleItem(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  48 */     return removeChestVehicleItem(slot, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  53 */     return removeChestVehicleItemNoUpdate(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  58 */     setChestVehicleItem(slot, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/*  63 */     return getChestVehicleSlot(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  72 */     return isChestVehicleStillValid(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/*  77 */     if (!level().isClientSide() && reason.shouldDestroy()) {
/*  78 */       Containers.dropContents(level(), (Entity)this, (Container)this);
/*     */     }
/*     */     
/*  81 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  86 */     super.addAdditionalSaveData(output);
/*  87 */     addChestVehicleSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  92 */     super.readAdditionalSaveData(input);
/*  93 */     readChestVehicleSaveData(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  98 */     return interactWithContainerVehicle(player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 deltaMovement) {
/* 103 */     float keep = 0.98F;
/*     */     
/* 105 */     if (this.lootTable == null) {
/* 106 */       int emptiness = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer((Container)this);
/* 107 */       keep += emptiness * 0.001F;
/*     */     } 
/*     */     
/* 110 */     if (isInWater()) {
/* 111 */       keep *= 0.95F;
/*     */     }
/*     */     
/* 114 */     return deltaMovement.multiply(keep, 0.0D, keep);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 123 */     clearChestVehicleContent();
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
/* 127 */     this.lootTable = lootTable;
/* 128 */     this.lootTableSeed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 133 */     if (this.lootTable == null || !player.isSpectator()) {
/* 134 */       unpackChestVehicleLootTable(inventory.player);
/* 135 */       return createMenu(containerId, inventory);
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*     */   
/*     */   public ResourceKey<LootTable> getContainerLootTable() {
/* 144 */     return this.lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTable(ResourceKey<LootTable> lootTable) {
/* 149 */     this.lootTable = lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContainerLootTableSeed() {
/* 154 */     return this.lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTableSeed(long lootTableSeed) {
/* 159 */     this.lootTableSeed = lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItemStacks() {
/* 164 */     return this.itemStacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearItemStacks() {
/* 169 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/minecart/AbstractMinecartContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */