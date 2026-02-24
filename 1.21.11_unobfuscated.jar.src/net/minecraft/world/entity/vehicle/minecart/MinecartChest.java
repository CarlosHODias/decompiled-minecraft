/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class MinecartChest extends AbstractMinecartContainer {
/*    */   public MinecartChest(EntityType<? extends MinecartChest> type, Level level) {
/* 25 */     super(type, level);
/*    */   }
/*    */ 
/*    */   
/*    */   protected net.minecraft.world.item.Item getDropItem() {
/* 30 */     return Items.CHEST_MINECART;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getPickResult() {
/* 35 */     return new ItemStack((net.minecraft.world.level.ItemLike)Items.CHEST_MINECART);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getContainerSize() {
/* 40 */     return 27;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getDefaultDisplayBlockState() {
/* 45 */     return (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)net.minecraft.world.level.block.ChestBlock.FACING, (Comparable)net.minecraft.core.Direction.NORTH);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDefaultDisplayOffset() {
/* 50 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 55 */     return (AbstractContainerMenu)ChestMenu.threeRows(containerId, inventory, (Container)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopOpen(ContainerUser containerUser) {
/* 60 */     level().gameEvent((Holder)GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of((Entity)containerUser.getLivingEntity()));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 65 */     InteractionResult result = interactWithContainerVehicle(player);
/* 66 */     if (result.consumesAction()) { Level level = player.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 67 */         gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)player);
/* 68 */         PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*    */        }
/* 70 */      return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/minecart/MinecartChest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */