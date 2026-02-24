/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.BlastFurnaceMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlastFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/* 13 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.blast_furnace");
/*    */   
/*    */   public BlastFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 16 */     super(BlockEntityType.BLAST_FURNACE, worldPosition, blockState, RecipeType.BLASTING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 21 */     return DEFAULT_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
/* 26 */     return super.getBurnDuration(fuelValues, itemStack) / 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 31 */     return (AbstractContainerMenu)new BlastFurnaceMenu(containerId, inventory, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BlastFurnaceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */