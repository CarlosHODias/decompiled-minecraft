/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SnowyDirtBlock extends Block {
/* 17 */   public static final MapCodec<SnowyDirtBlock> CODEC = simpleCodec(SnowyDirtBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends SnowyDirtBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/* 24 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty SNOWY = BlockStateProperties.SNOWY;
/*    */   
/*    */   protected SnowyDirtBlock(BlockBehaviour.Properties properties) {
/* 27 */     super(properties);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SNOWY, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 33 */     if (directionToNeighbour == Direction.UP) {
/* 34 */       return (BlockState)state.setValue((Property)SNOWY, isSnowySetting(neighbourState));
/*    */     }
/* 36 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 41 */     BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
/* 42 */     return (BlockState)defaultBlockState().setValue((Property)SNOWY, isSnowySetting(aboveState));
/*    */   }
/*    */   
/*    */   protected static boolean isSnowySetting(BlockState aboveState) {
/* 46 */     return aboveState.is(net.minecraft.tags.BlockTags.SNOW);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 51 */     builder.add(new Property[] { (Property)SNOWY });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SnowyDirtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */