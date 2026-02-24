/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CandleBlock extends AbstractCandleBlock implements SimpleWaterloggedBlock {
/*  37 */   public static final MapCodec<CandleBlock> CODEC = simpleCodec(CandleBlock::new); public static final int MIN_CANDLES = 1;
/*     */   public static final int MAX_CANDLES = 4;
/*     */   
/*     */   public MapCodec<CandleBlock> codec() {
/*  41 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty CANDLES = BlockStateProperties.CANDLES;
/*  48 */   public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
/*  49 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED; public static final ToIntFunction<BlockState> LIGHT_EMISSION; private static final Int2ObjectMap<List<Vec3>> PARTICLE_OFFSETS;
/*     */   static {
/*  51 */     LIGHT_EMISSION = (state -> (Boolean)state.getValue((Property)LIT) ? (3 * (Integer)state.getValue((Property)CANDLES)) : 0);
/*     */     
/*  53 */     PARTICLE_OFFSETS = (Int2ObjectMap<List<Vec3>>)Util.make(new Int2ObjectOpenHashMap(4), map -> {
/*     */           float s = 0.0625F;
/*     */           map.put(1, List.of(new Vec3(8.0D, 8.0D, 8.0D).scale(0.0625D)));
/*     */           map.put(2, List.of(new Vec3(6.0D, 7.0D, 8.0D).scale(0.0625D), new Vec3(10.0D, 8.0D, 7.0D).scale(0.0625D)));
/*     */           map.put(3, List.of(new Vec3(8.0D, 5.0D, 10.0D).scale(0.0625D), new Vec3(6.0D, 7.0D, 8.0D).scale(0.0625D), new Vec3(9.0D, 8.0D, 7.0D).scale(0.0625D)));
/*     */           map.put(4, List.of(new Vec3(7.0D, 5.0D, 9.0D).scale(0.0625D), new Vec3(10.0D, 7.0D, 9.0D).scale(0.0625D), new Vec3(6.0D, 7.0D, 6.0D).scale(0.0625D), new Vec3(9.0D, 8.0D, 6.0D).scale(0.0625D)));
/*     */         });
/*     */   }
/*  61 */   private static final VoxelShape[] SHAPES = new VoxelShape[] {
/*  62 */       Block.column(2.0D, 0.0D, 6.0D), 
/*  63 */       Block.box(5.0D, 0.0D, 6.0D, 11.0D, 6.0D, 9.0D), 
/*  64 */       Block.box(5.0D, 0.0D, 6.0D, 10.0D, 6.0D, 11.0D), 
/*  65 */       Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 10.0D)
/*     */     };
/*     */   
/*     */   public CandleBlock(BlockBehaviour.Properties properties) {
/*  69 */     super(properties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)CANDLES, 1)).setValue((Property)LIT, false)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  75 */     if (itemStack.isEmpty() && (player.getAbilities()).mayBuild && (Boolean)state.getValue((Property)LIT)) {
/*  76 */       extinguish(player, state, (LevelAccessor)level, pos);
/*  77 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  80 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  85 */     if (!context.isSecondaryUseActive() && context.getItemInHand().getItem() == asItem() && (Integer)state.getValue((Property)CANDLES) < 4) {
/*  86 */       return true;
/*     */     }
/*  88 */     return super.canBeReplaced(state, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  93 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/*  94 */     if (state.is(this)) {
/*  95 */       return (BlockState)state.cycle((Property)CANDLES);
/*     */     }
/*     */     
/*  98 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  99 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 100 */     return (BlockState)super.getStateForPlacement(context).setValue((Property)WATERLOGGED, isWaterSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 105 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 106 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 109 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 114 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 115 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/* 118 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 123 */     return SHAPES[(Integer)state.getValue((Property)CANDLES) - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 128 */     builder.add(new Property[] { (Property)CANDLES, (Property)LIT, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 133 */     if ((Boolean)state.getValue((Property)WATERLOGGED) || fluidState.getType() != Fluids.WATER) {
/* 134 */       return false;
/*     */     }
/*     */     
/* 137 */     BlockState newState = (BlockState)state.setValue((Property)WATERLOGGED, true);
/* 138 */     if ((Boolean)state.getValue((Property)LIT)) {
/* 139 */       extinguish(null, newState, level, pos);
/*     */     } else {
/* 141 */       level.setBlock(pos, newState, 3);
/*     */     } 
/*     */     
/* 144 */     level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay((LevelReader)level));
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState state) {
/* 149 */     return (state.is(BlockTags.CANDLES, s -> (s.hasProperty((Property)LIT) && s.hasProperty((Property)WATERLOGGED))) && !((Boolean)state.getValue((Property)LIT)) && !((Boolean)state.getValue((Property)WATERLOGGED)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterable<Vec3> getParticleOffsets(BlockState state) {
/* 155 */     return (Iterable<Vec3>)PARTICLE_OFFSETS.get((Integer)state.getValue((Property)CANDLES));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeLit(BlockState state) {
/* 160 */     return (!((Boolean)state.getValue((Property)WATERLOGGED)) && super.canBeLit(state));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 165 */     return Block.canSupportCenter(level, pos.below(), Direction.UP);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CandleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */