/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class BaseContainerBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {
/*  30 */   private LockCode lockKey = LockCode.NO_LOCK;
/*     */   private Component name;
/*     */   
/*     */   protected BaseContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
/*  34 */     super(type, worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  39 */     super.loadAdditional(input);
/*     */     
/*  41 */     this.lockKey = LockCode.fromTag(input);
/*     */     
/*  43 */     this.name = parseCustomNameSafe(input, "CustomName");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  48 */     super.saveAdditional(output);
/*  49 */     this.lockKey.addToTag(output);
/*     */     
/*  51 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/*  56 */     if (this.name != null) {
/*  57 */       return this.name;
/*     */     }
/*  59 */     return getDefaultName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getDisplayName() {
/*  64 */     return getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getCustomName() {
/*  69 */     return this.name;
/*     */   }
/*     */   
/*     */   protected abstract Component getDefaultName();
/*     */   
/*     */   public boolean canOpen(Player player) {
/*  75 */     return this.lockKey.canUnlock(player);
/*     */   }
/*     */   
/*     */   public static void sendChestLockedNotifications(Vec3 pos, Player player, Component displayName) {
/*  79 */     Level level = player.level();
/*  80 */     player.displayClientMessage((Component)Component.translatable("container.isLocked", new Object[] { displayName }), true);
/*  81 */     if (!level.isClientSide()) {
/*  82 */       level.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLocked() {
/*  87 */     return !this.lockKey.equals(LockCode.NO_LOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract NonNullList<ItemStack> getItems();
/*     */   
/*     */   protected abstract void setItems(NonNullList<ItemStack> paramNonNullList);
/*     */   
/*     */   public boolean isEmpty() {
/*  96 */     for (ItemStack itemStack : getItems()) {
/*  97 */       if (!itemStack.isEmpty()) {
/*  98 */         return false;
/*     */       }
/*     */     } 
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/* 106 */     return (ItemStack)getItems().get(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/* 111 */     ItemStack result = ContainerHelper.removeItem((List)getItems(), slot, count);
/* 112 */     if (!result.isEmpty()) {
/* 113 */       setChanged();
/*     */     }
/* 115 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/* 120 */     return ContainerHelper.takeItem((List)getItems(), slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 125 */     getItems().set(slot, itemStack);
/* 126 */     itemStack.limitSize(getMaxStackSize(itemStack));
/* 127 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 132 */     return Container.stillValidBlockEntity(this, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 137 */     getItems().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 142 */     if (canOpen(player)) {
/* 143 */       return createMenu(containerId, inventory);
/*     */     }
/*     */     
/* 146 */     sendChestLockedNotifications(getBlockPos().getCenter(), player, getDisplayName());
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 154 */     super.applyImplicitComponents(components);
/* 155 */     this.name = (Component)components.get(DataComponents.CUSTOM_NAME);
/* 156 */     this.lockKey = (LockCode)components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
/* 157 */     ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(getItems());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 162 */     super.collectImplicitComponents(components);
/* 163 */     components.set(DataComponents.CUSTOM_NAME, this.name);
/* 164 */     if (isLocked()) {
/* 165 */       components.set(DataComponents.LOCK, this.lockKey);
/*     */     }
/* 167 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems((List)getItems()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 172 */     output.discard("CustomName");
/* 173 */     output.discard("lock");
/* 174 */     output.discard("Items");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BaseContainerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */