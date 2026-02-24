/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.FurnaceMenu;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/* 12 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.furnace");
/*    */   
/*    */   public FurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 15 */     super(BlockEntityType.FURNACE, worldPosition, blockState, RecipeType.SMELTING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 20 */     return DEFAULT_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 25 */     return (AbstractContainerMenu)new FurnaceMenu(containerId, inventory, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/FurnaceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */