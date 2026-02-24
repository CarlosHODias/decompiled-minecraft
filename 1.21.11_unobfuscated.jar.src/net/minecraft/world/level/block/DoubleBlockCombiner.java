/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DoubleBlockCombiner {
/*    */   public enum BlockType {
/* 16 */     SINGLE,
/* 17 */     FIRST,
/* 18 */     SECOND;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <S extends BlockEntity> NeighborCombineResult<S> combineWithNeigbour(BlockEntityType<S> entityType, Function<BlockState, BlockType> typeResolver, Function<BlockState, Direction> connectionResolver, Property<Direction> facingProperty, BlockState state, LevelAccessor level, BlockPos pos, BiPredicate<LevelAccessor, BlockPos> blockedChecker) {
/* 23 */     BlockEntity blockEntity = entityType.getBlockEntity((BlockGetter)level, pos);
/* 24 */     if (blockEntity == null) {
/* 25 */       return Combiner::acceptNone;
/*    */     }
/*    */     
/* 28 */     if (blockedChecker.test(level, pos)) {
/* 29 */       return Combiner::acceptNone;
/*    */     }
/*    */     
/* 32 */     BlockType type = typeResolver.apply(state);
/*    */     
/* 34 */     boolean single = (type == BlockType.SINGLE);
/* 35 */     boolean isFirst = (type == BlockType.FIRST);
/*    */     
/* 37 */     if (single) {
/* 38 */       return new NeighborCombineResult.Single<>((S)blockEntity);
/*    */     }
/*    */     
/* 41 */     BlockPos neighborPos = pos.relative(connectionResolver.apply(state));
/* 42 */     BlockState neighbourState = level.getBlockState(neighborPos);
/* 43 */     if (neighbourState.is(state.getBlock())) {
/* 44 */       BlockType neighbourType = typeResolver.apply(neighbourState);
/* 45 */       if (neighbourType != BlockType.SINGLE && type != neighbourType && neighbourState.getValue(facingProperty) == state.getValue(facingProperty)) {
/* 46 */         if (blockedChecker.test(level, neighborPos)) {
/* 47 */           return Combiner::acceptNone;
/*    */         }
/*    */         
/* 50 */         BlockEntity blockEntity1 = entityType.getBlockEntity((BlockGetter)level, neighborPos);
/* 51 */         if (blockEntity1 != null) {
/* 52 */           BlockEntity blockEntity2 = isFirst ? blockEntity : blockEntity1;
/* 53 */           BlockEntity blockEntity3 = isFirst ? blockEntity1 : blockEntity;
/* 54 */           return new NeighborCombineResult.Double<>((S)blockEntity2, (S)blockEntity3);
/*    */         } 
/*    */       } 
/*    */     } 
/* 58 */     return new NeighborCombineResult.Single<>((S)blockEntity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Double<S>
/*    */     implements NeighborCombineResult<S>
/*    */   {
/*    */     private final S first;
/*    */ 
/*    */ 
/*    */     
/*    */     private final S second;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Double(S first, S second) {
/* 77 */       this.first = first;
/* 78 */       this.second = second;
/*    */     }
/*    */     
/*    */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback)
/*    */     {
/* 83 */       return callback.acceptDouble(this.first, this.second); } } public static interface NeighborCombineResult<S> { <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> param1Combiner); public static final class Double<S> implements NeighborCombineResult<S> { private final S first; public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return callback.acceptDouble(this.first, this.second); }
/*    */        private final S second;
/*    */       public Double(S first, S second) {
/*    */         this.first = first;
/*    */         this.second = second;
/*    */       } }
/*    */     public static final class Single<S> implements NeighborCombineResult<S> { private final S single;
/*    */       public Single(S single) {
/* 91 */         this.single = single;
/*    */       }
/*    */       
/*    */       public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback)
/*    */       {
/* 96 */         return callback.acceptSingle(this.single); } } } public static final class Single<S> implements NeighborCombineResult<S> { private final S single; public Single(S single) { this.single = single; } public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return callback.acceptSingle(this.single); }
/*    */      }
/*    */ 
/*    */   
/*    */   public static interface Combiner<S, T> {
/*    */     T acceptDouble(S param1S1, S param1S2);
/*    */     
/*    */     T acceptSingle(S param1S);
/*    */     
/*    */     T acceptNone();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DoubleBlockCombiner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */