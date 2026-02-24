/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CandleCakeBlock extends AbstractCandleBlock {
/*     */   static {
/*  34 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("candle").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CandleCakeBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CandleCakeBlock> CODEC;
/*     */   
/*     */   public MapCodec<CandleCakeBlock> codec() {
/*  41 */     return CODEC;
/*     */   }
/*     */   
/*  44 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = AbstractCandleBlock.LIT;
/*     */   
/*  46 */   private static final VoxelShape SHAPE = Shapes.or(
/*  47 */       Block.column(2.0D, 8.0D, 14.0D), 
/*  48 */       Block.column(14.0D, 0.0D, 8.0D));
/*     */ 
/*     */   
/*  51 */   private static final java.util.Map<CandleBlock, CandleCakeBlock> BY_CANDLE = Maps.newHashMap();
/*     */   
/*  53 */   private static final Iterable<Vec3> PARTICLE_OFFSETS = java.util.List.of(new Vec3(8.0D, 16.0D, 8.0D).scale(0.0625D));
/*     */   
/*     */   private final CandleBlock candleBlock;
/*     */   
/*     */   protected CandleCakeBlock(Block block, BlockBehaviour.Properties properties) {
/*  58 */     super(properties); CandleBlock matchingCandleBlock;
/*  59 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, false));
/*     */     
/*  61 */     if (block instanceof CandleBlock) { matchingCandleBlock = (CandleBlock)block; }
/*  62 */     else { throw new IllegalArgumentException("Expected block to be of " + String.valueOf(CandleBlock.class) + " was " + String.valueOf(block.getClass())); }
/*     */ 
/*     */     
/*  65 */     BY_CANDLE.put(matchingCandleBlock, this);
/*  66 */     this.candleBlock = matchingCandleBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Iterable<Vec3> getParticleOffsets(BlockState state) {
/*  71 */     return PARTICLE_OFFSETS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  76 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  81 */     if (itemStack.is(Items.FLINT_AND_STEEL) || itemStack.is(Items.FIRE_CHARGE)) {
/*  82 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  85 */     if (candleHit(hitResult) && itemStack.isEmpty() && (Boolean)state.getValue((Property)LIT)) {
/*  86 */       extinguish(player, state, (LevelAccessor)level, pos);
/*  87 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  90 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  95 */     InteractionResult eatResult = CakeBlock.eat((LevelAccessor)level, pos, Blocks.CAKE.defaultBlockState(), player);
/*  96 */     if (eatResult.consumesAction()) {
/*  97 */       dropResources(state, level, pos);
/*     */     }
/*  99 */     return eatResult;
/*     */   }
/*     */   
/*     */   private static boolean candleHit(BlockHitResult hitResult) {
/* 103 */     return ((hitResult.getLocation()).y - hitResult.getBlockPos().getY() > 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 108 */     builder.add(new Property[] { (Property)LIT });
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 113 */     return new ItemStack(Blocks.CAKE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 118 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 119 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 122 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 127 */     return level.getBlockState(pos.below()).isSolid();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 132 */     return CakeBlock.FULL_CAKE_SIGNAL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   public static BlockState byCandle(CandleBlock block) {
/* 146 */     return ((CandleCakeBlock)BY_CANDLE.get(block)).defaultBlockState();
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState state) {
/* 150 */     return state.is(BlockTags.CANDLE_CAKES, s -> (s.hasProperty((Property)LIT) && !((Boolean)state.getValue((Property)LIT))));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CandleCakeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */