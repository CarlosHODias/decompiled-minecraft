/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class EndRodBlock extends RodBlock {
/* 14 */   public static final MapCodec<EndRodBlock> CODEC = simpleCodec(EndRodBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<EndRodBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected EndRodBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/* 23 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 29 */     Direction clickedFace = context.getClickedFace();
/*    */     
/* 31 */     BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(clickedFace.getOpposite()));
/* 32 */     if (blockState.is(this) && blockState.getValue((Property)FACING) == clickedFace) {
/* 33 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)clickedFace.getOpposite());
/*    */     }
/*    */     
/* 36 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)clickedFace);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 41 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 42 */     double x = pos.getX() + 0.55D - (random.nextFloat() * 0.1F);
/* 43 */     double y = pos.getY() + 0.55D - (random.nextFloat() * 0.1F);
/* 44 */     double z = pos.getZ() + 0.55D - (random.nextFloat() * 0.1F);
/* 45 */     double r = (0.4F - (random.nextFloat() + random.nextFloat()) * 0.4F);
/*    */     
/* 47 */     if (random.nextInt(5) == 0) {
/* 48 */       level.addParticle((net.minecraft.core.particles.ParticleOptions)net.minecraft.core.particles.ParticleTypes.END_ROD, x + direction.getStepX() * r, y + direction.getStepY() * r, z + direction.getStepZ() * r, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 54 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/EndRodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */