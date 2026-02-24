/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BannerBlock extends AbstractBannerBlock {
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, BannerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BannerBlock> CODEC;
/*    */   
/*    */   public MapCodec<BannerBlock> codec() {
/* 32 */     return CODEC;
/*    */   }
/*    */   
/* 35 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 37 */   private static final java.util.Map<DyeColor, Block> BY_COLOR = Maps.newHashMap();
/* 38 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*    */   
/*    */   public BannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 41 */     super(color, properties);
/* 42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, 0));
/*    */     
/* 44 */     BY_COLOR.put(color, this);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 49 */     return level.getBlockState(pos.below()).isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 54 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 59 */     return (BlockState)defaultBlockState().setValue((Property)ROTATION, RotationSegment.convertToSegment(context.getRotation() + 180.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 64 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 65 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 68 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 73 */     return (BlockState)state.setValue((Property)ROTATION, rotation.rotate((Integer)state.getValue((Property)ROTATION), 16));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 78 */     return (BlockState)state.setValue((Property)ROTATION, mirror.mirror((Integer)state.getValue((Property)ROTATION), 16));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 83 */     builder.add(new Property[] { (Property)ROTATION });
/*    */   }
/*    */   
/*    */   public static Block byColor(DyeColor color) {
/* 87 */     return BY_COLOR.getOrDefault(color, Blocks.WHITE_BANNER);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */