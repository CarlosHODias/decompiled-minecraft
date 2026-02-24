/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RotatedPillarBlock extends Block {
/* 12 */   public static final MapCodec<RotatedPillarBlock> CODEC = simpleCodec(RotatedPillarBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends RotatedPillarBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/* 19 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*    */   
/*    */   public RotatedPillarBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/* 23 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 28 */     return rotatePillar(state, rotation);
/*    */   }
/*    */   
/*    */   public static BlockState rotatePillar(BlockState state, Rotation rotation) {
/* 32 */     switch (rotation) {
/*    */       case COUNTERCLOCKWISE_90:
/*    */       case CLOCKWISE_90:
/* 35 */         switch ((Direction.Axis)state.getValue((Property)AXIS)) {
/*    */           case X:
/* 37 */             return (BlockState)state.setValue((Property)AXIS, (Comparable)Direction.Axis.Z);
/*    */           case Z:
/* 39 */             return (BlockState)state.setValue((Property)AXIS, (Comparable)Direction.Axis.X);
/*    */         } 
/* 41 */         return state;
/*    */     } 
/*    */     
/* 44 */     return state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 50 */     builder.add(new Property[] { (Property)AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 55 */     return (BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)context.getClickedFace().getAxis());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/RotatedPillarBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */