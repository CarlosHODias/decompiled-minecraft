/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallSkullBlock extends AbstractSkullBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SkullBlock.Type.CODEC.fieldOf("kind").forGetter(AbstractSkullBlock::getType), (App)propertiesCodec()).apply((Applicative)i, WallSkullBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WallSkullBlock> CODEC;
/*    */   
/*    */   public MapCodec<? extends WallSkullBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/* 29 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 31 */   private static final java.util.Map<Direction, VoxelShape> SHAPES = net.minecraft.world.phys.shapes.Shapes.rotateHorizontal(Block.boxZ(8.0D, 8.0D, 16.0D));
/*    */   
/*    */   protected WallSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
/* 34 */     super(type, properties);
/* 35 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 40 */     return SHAPES.get(state.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 45 */     BlockState state = super.getStateForPlacement(context);
/*    */     
/* 47 */     Level level = context.getLevel();
/* 48 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 50 */     Direction[] directions = context.getNearestLookingDirections();
/* 51 */     for (Direction direction : directions) {
/* 52 */       if (direction.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         Direction facing = direction.getOpposite();
/*    */         
/* 58 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)facing);
/* 59 */         if (!level.getBlockState(pos.relative(direction)).canBeReplaced(context)) {
/* 60 */           return state;
/*    */         }
/*    */       } 
/*    */     } 
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 69 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 74 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 79 */     super.createBlockStateDefinition(builder);
/* 80 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WallSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */