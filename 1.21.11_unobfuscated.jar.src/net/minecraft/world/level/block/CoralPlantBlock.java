/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CoralPlantBlock extends BaseCoralPlantTypeBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CoralBlock.DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CoralPlantBlock::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<CoralPlantBlock> CODEC;
/*    */   private final Block deadBlock;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CoralPlantBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 15.0D);
/*    */   
/*    */   protected CoralPlantBlock(Block deadBlock, net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
/* 34 */     super(properties);
/* 35 */     this.deadBlock = deadBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 40 */     tryScheduleDieTick(state, (BlockGetter)level, (ScheduledTickAccess)level, level.random, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 45 */     if (!scanForWater(state, (BlockGetter)level, pos)) {
/* 46 */       level.setBlock(pos, (BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, false), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 52 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 53 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 56 */     tryScheduleDieTick(state, (BlockGetter)level, ticks, random, pos);
/*    */     
/* 58 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 59 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)net.minecraft.world.level.material.Fluids.WATER, net.minecraft.world.level.material.Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 62 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 67 */     return SHAPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CoralPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */