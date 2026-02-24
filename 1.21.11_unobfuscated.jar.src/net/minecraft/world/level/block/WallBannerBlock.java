/*    */ package net.minecraft.world.level.block;
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
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallBannerBlock extends AbstractBannerBlock {
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, WallBannerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WallBannerBlock> CODEC;
/*    */   
/*    */   public MapCodec<WallBannerBlock> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */   
/* 33 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 35 */   private static final java.util.Map<Direction, VoxelShape> SHAPES = net.minecraft.world.phys.shapes.Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 12.5D, 14.0D, 16.0D));
/*    */   
/*    */   public WallBannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 38 */     super(color, properties);
/* 39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 45 */     return level.getBlockState(pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite())).isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 50 */     if (directionToNeighbour == ((Direction)state.getValue((Property)FACING)).getOpposite() && !state.canSurvive(level, pos)) {
/* 51 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 59 */     return SHAPES.get(state.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 64 */     BlockState state = defaultBlockState();
/*    */     
/* 66 */     Level level = context.getLevel();
/* 67 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 69 */     Direction[] directions = context.getNearestLookingDirections();
/* 70 */     for (Direction direction : directions) {
/* 71 */       if (direction.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 75 */         Direction facing = direction.getOpposite();
/*    */         
/* 77 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)facing);
/* 78 */         if (state.canSurvive((LevelReader)level, pos)) {
/* 79 */           return state;
/*    */         }
/*    */       } 
/*    */     } 
/* 83 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 88 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 93 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 98 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WallBannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */