/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.Hopper;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class MinecartHopper extends AbstractMinecartContainer implements Hopper {
/*     */   private static final boolean DEFAULT_ENABLED = true;
/*     */   private boolean enabled = true;
/*     */   private boolean consumedItemThisFrame = false;
/*     */   
/*     */   public MinecartHopper(EntityType<? extends MinecartHopper> type, Level level) {
/*  32 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  37 */     return Blocks.HOPPER.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultDisplayOffset() {
/*  42 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  47 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/*  52 */     boolean newEnabled = !state;
/*     */     
/*  54 */     if (newEnabled != isEnabled()) {
/*  55 */       setEnabled(newEnabled);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  60 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/*  64 */     this.enabled = enabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelX() {
/*  69 */     return getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelY() {
/*  74 */     return getY() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelZ() {
/*  79 */     return getZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGridAligned() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  89 */     this.consumedItemThisFrame = false;
/*  90 */     super.tick();
/*  91 */     tryConsumeItems();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) {
/*  96 */     double left = super.makeStepAlongTrack(pos, shape, movementLeft);
/*  97 */     tryConsumeItems();
/*  98 */     return left;
/*     */   }
/*     */   
/*     */   private void tryConsumeItems() {
/* 102 */     if (!level().isClientSide() && isAlive() && isEnabled() && !this.consumedItemThisFrame && 
/* 103 */       suckInItems()) {
/* 104 */       this.consumedItemThisFrame = true;
/* 105 */       setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean suckInItems() {
/* 111 */     if (HopperBlockEntity.suckInItems(level(), this)) {
/* 112 */       return true;
/*     */     }
/*     */     
/* 115 */     List<ItemEntity> entities = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.25D, 0.0D, 0.25D), EntitySelector.ENTITY_STILL_ALIVE);
/*     */     
/* 117 */     for (ItemEntity entity : entities) {
/* 118 */       if (HopperBlockEntity.addItem((Container)this, entity)) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 128 */     return Items.HOPPER_MINECART;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 133 */     return new ItemStack((ItemLike)Items.HOPPER_MINECART);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 138 */     super.addAdditionalSaveData(output);
/* 139 */     output.putBoolean("Enabled", this.enabled);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 144 */     super.readAdditionalSaveData(input);
/* 145 */     this.enabled = input.getBooleanOr("Enabled", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 150 */     return (AbstractContainerMenu)new HopperMenu(containerId, inventory, (Container)this);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/minecart/MinecartHopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */