/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  41 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, TrapDoorBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<TrapDoorBlock> CODEC;
/*     */   
/*     */   public MapCodec<? extends TrapDoorBlock> codec() {
/*  48 */     return CODEC;
/*     */   }
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  52 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  53 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  54 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  56 */   private static final java.util.Map<Direction, VoxelShape> SHAPES = net.minecraft.world.phys.shapes.Shapes.rotateAll(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected TrapDoorBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/*  61 */     super(properties.sound(type.soundType()));
/*  62 */     this.type = type;
/*  63 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, false)).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)POWERED, false)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  68 */     return SHAPES.get((Boolean)state.getValue((Property)OPEN) ? 
/*  69 */         state.getValue((Property)FACING) : (
/*  70 */         (state.getValue((Property)HALF) == Half.TOP) ? Direction.DOWN : Direction.UP));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  76 */     switch (type) {
/*     */       case LAND:
/*  78 */         return (Boolean)state.getValue((Property)OPEN);
/*     */       case WATER:
/*  80 */         return (Boolean)state.getValue((Property)WATERLOGGED);
/*     */       case AIR:
/*  82 */         return (Boolean)state.getValue((Property)OPEN);
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/*  90 */     if (!this.type.canOpenByHand()) {
/*  91 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  94 */     toggle(state, level, pos, player);
/*  95 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 100 */     if (explosion.canTriggerBlocks() && this.type.canOpenByWindCharge() && !((Boolean)state.getValue((Property)POWERED))) {
/* 101 */       toggle(state, (Level)level, pos, null);
/*     */     }
/* 103 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   private void toggle(BlockState state, Level level, BlockPos pos, Player player) {
/* 107 */     BlockState updated = (BlockState)state.cycle((Property)OPEN);
/* 108 */     level.setBlock(pos, updated, 2);
/*     */     
/* 110 */     if ((Boolean)updated.getValue((Property)WATERLOGGED)) {
/* 111 */       level.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)level));
/*     */     }
/*     */     
/* 114 */     playSound(player, level, pos, (Boolean)updated.getValue((Property)OPEN));
/*     */   }
/*     */   
/*     */   protected void playSound(Player player, Level level, BlockPos pos, boolean opening) {
/* 118 */     level.playSound((Entity)player, pos, opening ? this.type.trapdoorOpen() : this.type.trapdoorClose(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 119 */     level.gameEvent((Entity)player, opening ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
/* 124 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     boolean signal = level.hasNeighborSignal(pos);
/* 129 */     if (signal != (Boolean)state.getValue((Property)POWERED)) {
/* 130 */       if ((Boolean)state.getValue((Property)OPEN) != signal) {
/* 131 */         state = (BlockState)state.setValue((Property)OPEN, signal);
/* 132 */         playSound(null, level, pos, signal);
/*     */       } 
/* 134 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, signal), 2);
/*     */       
/* 136 */       if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 137 */         level.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)level));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 144 */     BlockState state = defaultBlockState();
/* 145 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 147 */     Direction clickedFace = context.getClickedFace();
/* 148 */     if (context.replacingClickedOnBlock() || !clickedFace.getAxis().isHorizontal()) {
/* 149 */       state = (BlockState)((BlockState)state.setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)HALF, (clickedFace == Direction.UP) ? (Comparable)Half.BOTTOM : (Comparable)Half.TOP);
/*     */     } else {
/* 151 */       state = (BlockState)((BlockState)state.setValue((Property)FACING, (Comparable)clickedFace)).setValue((Property)HALF, ((context.getClickLocation()).y - context.getClickedPos().getY() > 0.5D) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM);
/*     */     } 
/* 153 */     if (context.getLevel().hasNeighborSignal(context.getClickedPos())) {
/* 154 */       state = (BlockState)((BlockState)state.setValue((Property)OPEN, true)).setValue((Property)POWERED, true);
/*     */     }
/* 156 */     return (BlockState)state.setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 161 */     builder.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)HALF, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 166 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 167 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 169 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 174 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 175 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 178 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected BlockSetType getType() {
/* 182 */     return this.type;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TrapDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */