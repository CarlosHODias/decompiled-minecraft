/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ButtonBlock extends FaceAttachedHorizontalDirectionalBlock {
/*     */   static {
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)Codec.intRange(1, 1024).fieldOf("ticks_to_stay_pressed").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ButtonBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<ButtonBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<ButtonBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   private final int ticksToStayPressed;
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected ButtonBlock(BlockSetType type, int ticksToStayPressed, BlockBehaviour.Properties properties) {
/*  63 */     super(properties.sound(type.soundType()));
/*     */     
/*  65 */     this.type = type;
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, false)).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*  67 */     this.ticksToStayPressed = ticksToStayPressed;
/*  68 */     this.shapes = makeShapes();
/*     */   }
/*     */ 
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  73 */     VoxelShape pressedShaper = Block.cube(14.0D);
/*  74 */     VoxelShape unpressedShaper = Block.cube(12.0D);
/*     */ 
/*     */     
/*  77 */     Map<AttachFace, Map<Direction, VoxelShape>> attachFace = Shapes.rotateAttachFace(Block.boxZ(6.0D, 4.0D, 8.0D, 16.0D));
/*     */     
/*  79 */     return getShapeForEachState(state -> Shapes.join((VoxelShape)((Map)attachFace.get(state.getValue((Property)FACE))).get(state.getValue((Property)FACING)), (Boolean)state.getValue((Property)POWERED) ? pressedShaper : unpressedShaper, BooleanOp.ONLY_FIRST));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  88 */     return this.shapes.apply(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/*  93 */     if ((Boolean)state.getValue((Property)POWERED)) {
/*  94 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/*  96 */     press(state, level, pos, player);
/*  97 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 102 */     if (explosion.canTriggerBlocks() && !((Boolean)state.getValue((Property)POWERED))) {
/* 103 */       press(state, (Level)level, pos, null);
/*     */     }
/* 105 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   public void press(BlockState state, Level level, BlockPos pos, Player player) {
/* 109 */     level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, true), 3);
/* 110 */     updateNeighbours(state, level, pos);
/* 111 */     level.scheduleTick(pos, this, this.ticksToStayPressed);
/* 112 */     playSound(player, (LevelAccessor)level, pos, true);
/* 113 */     level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_ACTIVATE, pos);
/*     */   }
/*     */   
/*     */   protected void playSound(Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
/* 117 */     level.playSound(pressed ? (Entity)player : null, pos, getSound(pressed), SoundSource.BLOCKS);
/*     */   }
/*     */   
/*     */   protected net.minecraft.sounds.SoundEvent getSound(boolean pressed) {
/* 121 */     return pressed ? this.type.buttonClickOn() : this.type.buttonClickOff();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 126 */     if (!movedByPiston && (Boolean)state.getValue((Property)POWERED)) {
/* 127 */       updateNeighbours(state, (Level)level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 133 */     return (Boolean)state.getValue((Property)POWERED) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 138 */     if ((Boolean)state.getValue((Property)POWERED) && getConnectedDirection(state) == direction) {
/* 139 */       return 15;
/*     */     }
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 151 */     if (!((Boolean)state.getValue((Property)POWERED))) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     checkPressed(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 160 */     if (level.isClientSide() || !this.type.canButtonBeActivatedByArrows() || (Boolean)state.getValue((Property)POWERED)) {
/*     */       return;
/*     */     }
/*     */     
/* 164 */     checkPressed(state, level, pos);
/*     */   }
/*     */   
/*     */   protected void checkPressed(BlockState state, Level level, BlockPos pos) {
/* 168 */     AbstractArrow firstArrow = this.type.canButtonBeActivatedByArrows() ? level.getEntitiesOfClass(AbstractArrow.class, state.getShape((BlockGetter)level, pos).bounds().move(pos)).stream().findFirst().orElse(null) : null;
/*     */     
/* 170 */     boolean shouldBePressed = (firstArrow != null);
/* 171 */     boolean wasPressed = (Boolean)state.getValue((Property)POWERED);
/*     */     
/* 173 */     if (shouldBePressed != wasPressed) {
/* 174 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, shouldBePressed), 3);
/* 175 */       updateNeighbours(state, level, pos);
/* 176 */       playSound(null, (LevelAccessor)level, pos, shouldBePressed);
/* 177 */       level.gameEvent((Entity)firstArrow, shouldBePressed ? (Holder)GameEvent.BLOCK_ACTIVATE : (Holder)GameEvent.BLOCK_DEACTIVATE, pos);
/*     */     } 
/*     */     
/* 180 */     if (shouldBePressed) {
/* 181 */       level.scheduleTick(new BlockPos((net.minecraft.core.Vec3i)pos), this, this.ticksToStayPressed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
/* 187 */     Direction front = getConnectedDirection(state).getOpposite();
/* 188 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, front, front.getAxis().isHorizontal() ? Direction.UP : (Direction)state.getValue((Property)FACING));
/* 189 */     level.updateNeighborsAt(pos, this, orientation);
/* 190 */     level.updateNeighborsAt(pos.relative(front), this, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 195 */     builder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)FACE });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ButtonBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */