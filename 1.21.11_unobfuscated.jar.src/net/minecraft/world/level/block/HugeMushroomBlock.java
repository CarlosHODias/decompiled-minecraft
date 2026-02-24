/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class HugeMushroomBlock extends Block {
/* 18 */   public static final MapCodec<HugeMushroomBlock> CODEC = simpleCodec(HugeMushroomBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<HugeMushroomBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/* 26 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/* 27 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/* 28 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/* 29 */   public static final BooleanProperty UP = PipeBlock.UP;
/* 30 */   public static final BooleanProperty DOWN = PipeBlock.DOWN;
/*    */   
/* 32 */   private static final java.util.Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*    */   
/*    */   public HugeMushroomBlock(BlockBehaviour.Properties properties) {
/* 35 */     super(properties);
/* 36 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, true)).setValue((Property)EAST, true)).setValue((Property)SOUTH, true)).setValue((Property)WEST, true)).setValue((Property)UP, true)).setValue((Property)DOWN, true));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 41 */     Level level = context.getLevel();
/* 42 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 44 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 45 */       .setValue((Property)DOWN, !level.getBlockState(pos.below()).is(this)))
/* 46 */       .setValue((Property)UP, !level.getBlockState(pos.above()).is(this)))
/* 47 */       .setValue((Property)NORTH, !level.getBlockState(pos.north()).is(this)))
/* 48 */       .setValue((Property)EAST, !level.getBlockState(pos.east()).is(this)))
/* 49 */       .setValue((Property)SOUTH, !level.getBlockState(pos.south()).is(this)))
/* 50 */       .setValue((Property)WEST, !level.getBlockState(pos.west()).is(this));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 56 */     if (neighbourState.is(this)) {
/* 57 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), false);
/*    */     }
/* 59 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 64 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)
/* 65 */       state.setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.NORTH)), state.getValue((Property)NORTH)))
/* 66 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.SOUTH)), state.getValue((Property)SOUTH)))
/* 67 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.EAST)), state.getValue((Property)EAST)))
/* 68 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.WEST)), state.getValue((Property)WEST)))
/* 69 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.UP)), state.getValue((Property)UP)))
/* 70 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.DOWN)), state.getValue((Property)DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 76 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)
/* 77 */       state.setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.NORTH)), state.getValue((Property)NORTH)))
/* 78 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.SOUTH)), state.getValue((Property)SOUTH)))
/* 79 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.EAST)), state.getValue((Property)EAST)))
/* 80 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.WEST)), state.getValue((Property)WEST)))
/* 81 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.UP)), state.getValue((Property)UP)))
/* 82 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.DOWN)), state.getValue((Property)DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 88 */     builder.add(new Property[] { (Property)UP, (Property)DOWN, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/HugeMushroomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */