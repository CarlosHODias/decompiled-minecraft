/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DropperBlockEntity extends DispenserBlockEntity {
/*  8 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.dropper");
/*    */   
/*    */   public DropperBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 11 */     super(BlockEntityType.DROPPER, worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 16 */     return DEFAULT_NAME;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/DropperBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */