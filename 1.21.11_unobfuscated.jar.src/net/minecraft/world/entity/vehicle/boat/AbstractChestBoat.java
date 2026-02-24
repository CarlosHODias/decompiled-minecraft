/*     */ package net.minecraft.world.entity.vehicle.boat;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public abstract class AbstractChestBoat extends AbstractBoat implements HasCustomInventoryScreen, ContainerEntity {
/*  34 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY); private static final int CONTAINER_SIZE = 27;
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*     */   public AbstractChestBoat(EntityType<? extends AbstractChestBoat> type, Level level, Supplier<Item> dropItem) {
/*  39 */     super((EntityType)type, level, dropItem);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getSinglePassengerXOffset() {
/*  45 */     return 0.15F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getMaxPassengers() {
/*  50 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  55 */     super.addAdditionalSaveData(output);
/*  56 */     addChestVehicleSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  61 */     super.readAdditionalSaveData(input);
/*  62 */     readChestVehicleSaveData(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  67 */     destroy(level, getDropItem());
/*  68 */     chestVehicleDestroyed(source, level, (Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/*  73 */     if (!level().isClientSide() && reason.shouldDestroy()) {
/*  74 */       Containers.dropContents(level(), (Entity)this, (Container)this);
/*     */     }
/*  76 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  81 */     InteractionResult superInteraction = super.interact(player, hand);
/*  82 */     if (superInteraction != InteractionResult.PASS) {
/*  83 */       return superInteraction;
/*     */     }
/*  85 */     if (!canAddPassenger((Entity)player) || player.isSecondaryUseActive()) {
/*  86 */       InteractionResult result = interactWithContainerVehicle(player);
/*  87 */       if (result.consumesAction()) { Level level = player.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  88 */           gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)player);
/*  89 */           PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*     */          }
/*  91 */        return result;
/*     */     } 
/*  93 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/*  98 */     player.openMenu((MenuProvider)this);
/*  99 */     Level level = player.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 100 */       gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)player);
/* 101 */       PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 107 */     clearChestVehicleContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 112 */     return 27;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/* 117 */     return getChestVehicleItem(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/* 122 */     return removeChestVehicleItem(slot, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/* 127 */     return removeChestVehicleItemNoUpdate(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 132 */     setChestVehicleItem(slot, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 137 */     return getChestVehicleSlot(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 146 */     return isChestVehicleStillValid(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 151 */     if (this.lootTable == null || !player.isSpectator()) {
/* 152 */       unpackLootTable(inventory.player);
/* 153 */       return (AbstractContainerMenu)ChestMenu.threeRows(containerId, inventory, (Container)this);
/*     */     } 
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public void unpackLootTable(Player player) {
/* 159 */     unpackChestVehicleLootTable(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceKey<LootTable> getContainerLootTable() {
/* 164 */     return this.lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTable(ResourceKey<LootTable> lootTable) {
/* 169 */     this.lootTable = lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContainerLootTableSeed() {
/* 174 */     return this.lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTableSeed(long lootTableSeed) {
/* 179 */     this.lootTableSeed = lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItemStacks() {
/* 184 */     return this.itemStacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearItemStacks() {
/* 189 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser containerUser) {
/* 194 */     level().gameEvent((Holder)GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of((Entity)containerUser.getLivingEntity()));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/boat/AbstractChestBoat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */