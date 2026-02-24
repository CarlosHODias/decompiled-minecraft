/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallTorchBlock extends TorchBlock {
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PARTICLE_OPTIONS_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)i, WallTorchBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<WallTorchBlock> CODEC;
/*     */   
/*     */   public MapCodec<WallTorchBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */   
/*  36 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  38 */   private static final java.util.Map<Direction, VoxelShape> SHAPES = net.minecraft.world.phys.shapes.Shapes.rotateHorizontal(Block.boxZ(5.0D, 3.0D, 13.0D, 11.0D, 16.0D));
/*     */   
/*     */   protected WallTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
/*  41 */     super(flameParticle, properties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  47 */     return getShape(state);
/*     */   }
/*     */   
/*     */   public static VoxelShape getShape(BlockState state) {
/*  51 */     return SHAPES.get(state.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  56 */     return canSurvive(level, pos, (Direction)state.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
/*  60 */     BlockPos relativePos = pos.relative(facing.getOpposite());
/*  61 */     BlockState relativeState = level.getBlockState(relativePos);
/*  62 */     return relativeState.isFaceSturdy((BlockGetter)level, relativePos, facing);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  67 */     BlockState state = defaultBlockState();
/*     */     
/*  69 */     Level level = context.getLevel();
/*  70 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  72 */     Direction[] directions = context.getNearestLookingDirections();
/*  73 */     for (Direction direction : directions) {
/*  74 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  78 */         Direction facing = direction.getOpposite();
/*     */         
/*  80 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)facing);
/*  81 */         if (state.canSurvive((LevelReader)level, pos)) {
/*  82 */           return state;
/*     */         }
/*     */       } 
/*     */     } 
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  91 */     if (directionToNeighbour.getOpposite() == state.getValue((Property)FACING) && !state.canSurvive(level, pos)) {
/*  92 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  94 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  99 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 100 */     double x = pos.getX() + 0.5D;
/* 101 */     double y = pos.getY() + 0.7D;
/* 102 */     double z = pos.getZ() + 0.5D;
/* 103 */     double h = 0.22D;
/* 104 */     double r = 0.27D;
/*     */     
/* 106 */     Direction opposite = direction.getOpposite();
/* 107 */     level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x + 0.27D * opposite.getStepX(), y + 0.22D, z + 0.27D * opposite.getStepZ(), 0.0D, 0.0D, 0.0D);
/* 108 */     level.addParticle((ParticleOptions)this.flameParticle, x + 0.27D * opposite.getStepX(), y + 0.22D, z + 0.27D * opposite.getStepZ(), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 113 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 118 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 123 */     builder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WallTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */