/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.BooleanOp;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public enum SupportType {
/* 12 */   FULL
/*    */   {
/*    */     public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 15 */       return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction);
/*    */     }
/*    */   },
/* 18 */   CENTER
/*    */   {
/*    */     private final VoxelShape CENTER_SUPPORT_SHAPE;
/*    */     
/*    */     public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 23 */       return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */     }
/*    */   },
/* 26 */   RIGID
/*    */   {
/*    */     private final VoxelShape RIGID_SUPPORT_SHAPE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 35 */       return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.RIGID_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean isSupporting(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SupportType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */