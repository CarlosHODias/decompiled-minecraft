/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class JigsawBlock extends Block implements EntityBlock, GameMasterBlock {
/*  21 */   public static final MapCodec<JigsawBlock> CODEC = simpleCodec(JigsawBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<JigsawBlock> codec() {
/*  25 */     return CODEC;
/*     */   }
/*     */   
/*  28 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
/*     */   
/*     */   protected JigsawBlock(BlockBehaviour.Properties properties) {
/*  31 */     super(properties);
/*  32 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ORIENTATION, (Comparable)FrontAndTop.NORTH_UP));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  37 */     builder.add(new Property[] { (Property)ORIENTATION });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  42 */     return (BlockState)state.setValue((Property)ORIENTATION, (Comparable)rotation.rotation().rotate((FrontAndTop)state.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  47 */     return (BlockState)state.setValue((Property)ORIENTATION, (Comparable)mirror.rotation().rotate((FrontAndTop)state.getValue((Property)ORIENTATION)));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*     */     Direction top;
/*  52 */     Direction front = context.getClickedFace();
/*     */     
/*  54 */     if (front.getAxis() == Direction.Axis.Y) {
/*  55 */       top = context.getHorizontalDirection().getOpposite();
/*     */     } else {
/*  57 */       top = Direction.UP;
/*     */     } 
/*     */     
/*  60 */     return (BlockState)defaultBlockState().setValue((Property)ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(front, top));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  65 */     return (BlockEntity)new JigsawBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  70 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  71 */     if (blockEntity instanceof JigsawBlockEntity && player.canUseGameMasterBlocks()) {
/*  72 */       player.openJigsawBlock((JigsawBlockEntity)blockEntity);
/*  73 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/*  77 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static boolean canAttach(StructureTemplate.JigsawBlockInfo source, StructureTemplate.JigsawBlockInfo target) {
/*  81 */     Direction sourceFront = getFrontFacing(source.info().state());
/*  82 */     Direction targetFront = getFrontFacing(target.info().state());
/*  83 */     Direction sourceTop = getTopFacing(source.info().state());
/*  84 */     Direction targetTop = getTopFacing(target.info().state());
/*     */ 
/*     */     
/*  87 */     JigsawBlockEntity.JointType jointType = source.jointType();
/*  88 */     boolean rollable = (jointType == JigsawBlockEntity.JointType.ROLLABLE);
/*     */     
/*  90 */     return (sourceFront == targetFront.getOpposite() && (rollable || sourceTop == targetTop) && 
/*     */       
/*  92 */       source.target().equals(target.name()));
/*     */   }
/*     */   
/*     */   public static Direction getFrontFacing(BlockState state) {
/*  96 */     return ((FrontAndTop)state.getValue((Property)ORIENTATION)).front();
/*     */   }
/*     */   
/*     */   public static Direction getTopFacing(BlockState state) {
/* 100 */     return ((FrontAndTop)state.getValue((Property)ORIENTATION)).top();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/JigsawBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */