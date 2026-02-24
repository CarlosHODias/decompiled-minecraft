/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BarrelBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class BarrelBlockEntity extends RandomizableContainerBlockEntity {
/*  28 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.barrel");
/*  29 */   private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
/*  30 */   private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter()
/*     */     {
/*     */       protected void onOpen(Level level, BlockPos pos, BlockState state) {
/*  33 */         BarrelBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
/*  34 */         BarrelBlockEntity.this.updateBlockState(state, true);
/*     */       }
/*     */ 
/*     */       
/*     */       protected void onClose(Level level, BlockPos pos, BlockState state) {
/*  39 */         BarrelBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
/*  40 */         BarrelBlockEntity.this.updateBlockState(state, false);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) {}
/*     */ 
/*     */       
/*     */       public boolean isOwnContainer(Player player) {
/*  49 */         if (player.containerMenu instanceof ChestMenu) {
/*  50 */           Container container = ((ChestMenu)player.containerMenu).getContainer();
/*  51 */           return (container == BarrelBlockEntity.this);
/*     */         } 
/*  53 */         return false;
/*     */       }
/*     */     };
/*     */   
/*     */   public BarrelBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  58 */     super(BlockEntityType.BARREL, worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  63 */     super.saveAdditional(output);
/*  64 */     if (!trySaveLootTable(output)) {
/*  65 */       ContainerHelper.saveAllItems(output, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  71 */     super.loadAdditional(input);
/*     */     
/*  73 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  74 */     if (!tryLoadLootTable(input)) {
/*  75 */       ContainerHelper.loadAllItems(input, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  81 */     return 27;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/*  86 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> items) {
/*  91 */     this.items = items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/*  96 */     return DEFAULT_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 101 */     return (AbstractContainerMenu)ChestMenu.threeRows(containerId, inventory, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(ContainerUser containerUser) {
/* 106 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 107 */       this.openersCounter.incrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState(), containerUser.getContainerInteractionRange());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser containerUser) {
/* 113 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 114 */       this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ContainerUser> getEntitiesWithContainerOpen() {
/* 120 */     return this.openersCounter.getEntitiesWithContainerOpen(getLevel(), getBlockPos());
/*     */   }
/*     */   
/*     */   public void recheckOpen() {
/* 124 */     if (!this.remove) {
/* 125 */       this.openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateBlockState(BlockState state, boolean isOpen) {
/* 130 */     this.level.setBlock(getBlockPos(), (BlockState)state.setValue((Property)BarrelBlock.OPEN, isOpen), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   private void playSound(BlockState state, SoundEvent event) {
/* 135 */     Vec3i direction = ((Direction)state.getValue((Property)BarrelBlock.FACING)).getUnitVec3i();
/* 136 */     double x = this.worldPosition.getX() + 0.5D + direction.getX() / 2.0D;
/* 137 */     double y = this.worldPosition.getY() + 0.5D + direction.getY() / 2.0D;
/* 138 */     double z = this.worldPosition.getZ() + 0.5D + direction.getZ() / 2.0D;
/*     */     
/* 140 */     this.level.playSound(null, x, y, z, event, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BarrelBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */