/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Column;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnderwaterMagmaFeature
/*    */   extends Feature<UnderwaterMagmaConfiguration>
/*    */ {
/*    */   public UnderwaterMagmaFeature(Codec<UnderwaterMagmaConfiguration> codec) {
/* 30 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<UnderwaterMagmaConfiguration> context) {
/* 35 */     WorldGenLevel level = context.level();
/* 36 */     BlockPos origin = context.origin();
/* 37 */     UnderwaterMagmaConfiguration config = context.config();
/* 38 */     RandomSource random = context.random();
/*    */     
/* 40 */     OptionalInt floorY = getFloorY(level, origin, config);
/* 41 */     if (floorY.isEmpty()) {
/* 42 */       return false;
/*    */     }
/* 44 */     BlockPos floorPos = origin.atY(floorY.getAsInt());
/*    */     
/* 46 */     Vec3i radius = new Vec3i(config.placementRadiusAroundFloor, config.placementRadiusAroundFloor, config.placementRadiusAroundFloor);
/* 47 */     BoundingBox bounds = BoundingBox.fromCorners((Vec3i)floorPos.subtract(radius), (Vec3i)floorPos.offset(radius));
/* 48 */     return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 54 */       (BlockPos.betweenClosedStream(bounds).filter(pos -> (random.nextFloat() < config.placementProbabilityPerValidPosition)).filter(pos -> isValidPlacement(level, level)).mapToInt(pos -> { level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2); return 1; }).sum() > 0);
/*    */   }
/*    */   
/*    */   private static OptionalInt getFloorY(WorldGenLevel level, BlockPos origin, UnderwaterMagmaConfiguration config) {
/*    */     Predicate<BlockState> insideColumn = state -> state.is(Blocks.WATER);
/*    */     Predicate<BlockState> validEdge = state -> !state.is(Blocks.WATER);
/* 60 */     Optional<Column> waterColumn = Column.scan((LevelSimulatedReader)level, origin, config.floorSearchRange, insideColumn, validEdge);
/* 61 */     return waterColumn.<OptionalInt>map(Column::getFloor).orElseGet(OptionalInt::empty);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isValidPlacement(WorldGenLevel level, BlockPos pos) {
/* 68 */     if (isWaterOrAir(level.getBlockState(pos)) || isVisibleFromOutside((LevelAccessor)level, pos.below(), Direction.UP)) {
/* 69 */       return false;
/*    */     }
/* 71 */     for (Direction neighbourDir : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 72 */       if (isVisibleFromOutside((LevelAccessor)level, pos.relative(neighbourDir), neighbourDir.getOpposite())) {
/* 73 */         return false;
/*    */       }
/*    */     } 
/* 76 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean isWaterOrAir(BlockState state) {
/* 80 */     return (state.is(Blocks.WATER) || state.isAir());
/*    */   }
/*    */   
/*    */   private boolean isVisibleFromOutside(LevelAccessor level, BlockPos pos, Direction coveredDirection) {
/* 84 */     BlockState state = level.getBlockState(pos);
/* 85 */     VoxelShape faceOcclusionShape = state.getFaceOcclusionShape(coveredDirection);
/* 86 */     return (faceOcclusionShape == Shapes.empty() || !Block.isShapeFullBlock(faceOcclusionShape));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/UnderwaterMagmaFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */