/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LeverBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  38 */   public static final MapCodec<LeverBlock> CODEC = simpleCodec(LeverBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<LeverBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected LeverBlock(BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, false)).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*     */     
/*  53 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  57 */     Map<AttachFace, Map<Direction, VoxelShape>> attachFace = Shapes.rotateAttachFace(Block.boxZ(6.0D, 8.0D, 10.0D, 16.0D));
/*     */     
/*  59 */     return getShapeForEachState(state -> (VoxelShape)((Map)attachFace.get(state.getValue((Property)FACE))).get(state.getValue((Property)FACING)), (Property<?>[])new Property[] { (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  64 */     return this.shapes.apply(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState stateBefore, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/*  70 */     if (level.isClientSide()) {
/*  71 */       BlockState stateAfter = (BlockState)stateBefore.cycle((Property)POWERED);
/*  72 */       if ((Boolean)stateAfter.getValue((Property)POWERED)) {
/*  73 */         makeParticle(stateAfter, (LevelAccessor)level, pos, 1.0F);
/*     */       }
/*     */     } else {
/*  76 */       pull(stateBefore, level, pos, null);
/*     */     } 
/*  78 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/*  83 */     if (explosion.canTriggerBlocks()) {
/*  84 */       pull(state, (Level)level, pos, null);
/*     */     }
/*  86 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   public void pull(BlockState state, Level level, BlockPos pos, Player player) {
/*  90 */     state = (BlockState)state.cycle((Property)POWERED);
/*  91 */     level.setBlock(pos, state, 3);
/*  92 */     updateNeighbours(state, level, pos);
/*  93 */     playSound(player, (LevelAccessor)level, pos, state);
/*  94 */     level.gameEvent((Entity)player, (Boolean)state.getValue((Property)POWERED) ? (Holder)GameEvent.BLOCK_ACTIVATE : (Holder)GameEvent.BLOCK_DEACTIVATE, pos);
/*     */   }
/*     */   
/*     */   protected static void playSound(Player player, LevelAccessor level, BlockPos pos, BlockState stateAfter) {
/*  98 */     float pitch = (Boolean)stateAfter.getValue((Property)POWERED) ? 0.6F : 0.5F;
/*  99 */     level.playSound((Entity)player, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, pitch);
/*     */   }
/*     */   
/*     */   private static void makeParticle(BlockState state, LevelAccessor level, BlockPos pos, float scale) {
/* 103 */     Direction opposite = ((Direction)state.getValue((Property)FACING)).getOpposite();
/* 104 */     Direction oppositeConnect = getConnectedDirection(state).getOpposite();
/* 105 */     double x = pos.getX() + 0.5D + 0.1D * opposite.getStepX() + 0.2D * oppositeConnect.getStepX();
/* 106 */     double y = pos.getY() + 0.5D + 0.1D * opposite.getStepY() + 0.2D * oppositeConnect.getStepY();
/* 107 */     double z = pos.getZ() + 0.5D + 0.1D * opposite.getStepZ() + 0.2D * oppositeConnect.getStepZ();
/*     */     
/* 109 */     level.addParticle((ParticleOptions)new DustParticleOptions(16711680, scale), x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 114 */     if ((Boolean)state.getValue((Property)POWERED) && random.nextFloat() < 0.25F) {
/* 115 */       makeParticle(state, (LevelAccessor)level, pos, 0.5F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 121 */     if (!movedByPiston && (Boolean)state.getValue((Property)POWERED)) {
/* 122 */       updateNeighbours(state, (Level)level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 128 */     return (Boolean)state.getValue((Property)POWERED) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 133 */     if ((Boolean)state.getValue((Property)POWERED) && getConnectedDirection(state) == direction) {
/* 134 */       return 15;
/*     */     }
/* 136 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
/* 146 */     Direction front = getConnectedDirection(state).getOpposite();
/* 147 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, front, front.getAxis().isHorizontal() ? Direction.UP : (Direction)state.getValue((Property)FACING));
/* 148 */     level.updateNeighborsAt(pos, this, orientation);
/* 149 */     level.updateNeighborsAt(pos.relative(front), this, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 154 */     builder.add(new Property[] { (Property)FACE, (Property)FACING, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LeverBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */