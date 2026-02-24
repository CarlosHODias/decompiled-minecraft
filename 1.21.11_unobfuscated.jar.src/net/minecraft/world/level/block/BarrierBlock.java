/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class BarrierBlock extends Block implements SimpleWaterloggedBlock {
/* 25 */   public static final MapCodec<BarrierBlock> CODEC = simpleCodec(BarrierBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BarrierBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/* 32 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/*    */   protected BarrierBlock(BlockBehaviour.Properties properties) {
/* 35 */     super(properties);
/* 36 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)WATERLOGGED, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState state) {
/* 41 */     return state.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderShape getRenderShape(BlockState state) {
/* 46 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/* 51 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 56 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 57 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/* 59 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 64 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 65 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 67 */     return super.getFluidState(state);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 72 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, (context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 77 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 82 */     if (user instanceof Player) { Player player = (Player)user; if (player.isCreative())
/*    */       {
/*    */         
/* 85 */         return super.pickupBlock(user, level, pos, state); }  }
/*    */     
/*    */     return ItemStack.EMPTY;
/*    */   }
/*    */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/* 90 */     if (user instanceof Player) { Player player = (Player)user; if (player.isCreative())
/*    */       {
/*    */         
/* 93 */         return super.canPlaceLiquid(user, level, pos, state, type);
/*    */       } }
/*    */     
/*    */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BarrierBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */