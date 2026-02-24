/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AnvilMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class AnvilBlock extends FallingBlock {
/*  33 */   public static final MapCodec<AnvilBlock> CODEC = simpleCodec(AnvilBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<AnvilBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  42 */   private static final java.util.Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or(
/*  43 */         Block.column(12.0D, 0.0D, 4.0D), new VoxelShape[] {
/*  44 */           Block.column(8.0D, 10.0D, 4.0D, 5.0D), 
/*  45 */           Block.column(4.0D, 8.0D, 5.0D, 10.0D), 
/*  46 */           Block.column(10.0D, 16.0D, 10.0D, 16.0D)
/*     */         }));
/*     */   
/*  49 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.repair");
/*     */   private static final float FALL_DAMAGE_PER_DISTANCE = 2.0F;
/*     */   private static final int FALL_DAMAGE_MAX = 40;
/*     */   
/*     */   public AnvilBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  60 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getClockWise());
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  65 */     if (!level.isClientSide()) {
/*  66 */       player.openMenu(state.getMenuProvider(level, pos));
/*  67 */       player.awardStat(Stats.INTERACT_WITH_ANVIL);
/*     */     } 
/*  69 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/*  74 */     return (MenuProvider)new SimpleMenuProvider((containerId, inventory, player) -> new AnvilMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  79 */     return SHAPES.get(((Direction)state.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void falling(FallingBlockEntity entity) {
/*  84 */     entity.setHurtsEntities(2.0F, 40);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {
/*  89 */     if (!entity.isSilent()) {
/*  90 */       level.levelEvent(1031, pos, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
/*  96 */     if (!entity.isSilent()) {
/*  97 */       level.levelEvent(1029, pos, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource getFallDamageSource(Entity entity) {
/* 103 */     return entity.damageSources().anvil(entity);
/*     */   }
/*     */   
/*     */   public static BlockState damage(BlockState blockState) {
/* 107 */     if (blockState.is(Blocks.ANVIL)) {
/* 108 */       return (BlockState)Blocks.CHIPPED_ANVIL.defaultBlockState().setValue((Property)FACING, blockState.getValue((Property)FACING));
/*     */     }
/* 110 */     if (blockState.is(Blocks.CHIPPED_ANVIL)) {
/* 111 */       return (BlockState)Blocks.DAMAGED_ANVIL.defaultBlockState().setValue((Property)FACING, blockState.getValue((Property)FACING));
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 118 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 123 */     builder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
/* 133 */     return (blockState.getMapColor(level, pos)).col;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AnvilBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */