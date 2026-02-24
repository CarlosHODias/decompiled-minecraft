/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TheEndPortalBlockEntity extends BlockEntity {
/*    */   protected TheEndPortalBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
/*  9 */     super(type, worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public TheEndPortalBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 13 */     this(BlockEntityType.END_PORTAL, worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public boolean shouldRenderFace(Direction direction) {
/* 17 */     return (direction.getAxis() == Direction.Axis.Y);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/TheEndPortalBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */