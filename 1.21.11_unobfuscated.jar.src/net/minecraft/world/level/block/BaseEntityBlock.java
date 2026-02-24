/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BaseEntityBlock extends Block implements EntityBlock {
/*    */   protected BaseEntityBlock(BlockBehaviour.Properties properties) {
/* 15 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends BaseEntityBlock> codec();
/*    */ 
/*    */   
/*    */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/* 23 */     super.triggerEvent(state, level, pos, b0, b1);
/*    */     
/* 25 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 26 */     if (blockEntity == null) {
/* 27 */       return false;
/*    */     }
/* 29 */     return blockEntity.triggerEvent(b0, b1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 34 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 35 */     return (blockEntity instanceof MenuProvider) ? (MenuProvider)blockEntity : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
/* 40 */     return (expected == actual) ? (BlockEntityTicker)ticker : null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BaseEntityBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */