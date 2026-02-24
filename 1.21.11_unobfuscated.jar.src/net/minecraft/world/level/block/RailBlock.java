/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ 
/*    */ public class RailBlock extends BaseRailBlock {
/* 14 */   public static final MapCodec<RailBlock> CODEC = simpleCodec(RailBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<RailBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/* 21 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;
/*    */   
/*    */   protected RailBlock(BlockBehaviour.Properties properties) {
/* 24 */     super(false, properties);
/* 25 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)WATERLOGGED, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
/* 30 */     if (block.defaultBlockState().isSignalSource() && 
/* 31 */       new RailState(level, pos, state).countPotentialConnections() == 3) {
/* 32 */       updateDir(level, pos, state, false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Property<RailShape> getShapeProperty() {
/* 39 */     return (Property<RailShape>)SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 44 */     RailShape currentShape = (RailShape)state.getValue((Property)SHAPE);
/* 45 */     RailShape newShape = rotate(currentShape, rotation);
/* 46 */     return (BlockState)state.setValue((Property)SHAPE, (Comparable)newShape);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 51 */     RailShape currentShape = (RailShape)state.getValue((Property)SHAPE);
/* 52 */     RailShape newShape = mirror(currentShape, mirror);
/* 53 */     return (BlockState)state.setValue((Property)SHAPE, (Comparable)newShape);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 58 */     builder.add(new Property[] { (Property)SHAPE, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/RailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */