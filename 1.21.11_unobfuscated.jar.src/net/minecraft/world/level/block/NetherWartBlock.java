/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherWartBlock extends VegetationBlock {
/* 19 */   public static final MapCodec<NetherWartBlock> CODEC = simpleCodec(NetherWartBlock::new);
/*    */   public static final int MAX_AGE = 3;
/*    */   
/*    */   public MapCodec<NetherWartBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3; private static final VoxelShape[] SHAPES;
/*    */   static {
/* 29 */     SHAPES = Block.boxes(3, age -> Block.column(16.0D, 0.0D, (5 + age * 3)));
/*    */   }
/*    */   protected NetherWartBlock(BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/* 33 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, 0));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 38 */     return SHAPES[(Integer)state.getValue((Property)AGE)];
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 43 */     return state.is(Blocks.SOUL_SAND);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 48 */     return ((Integer)state.getValue((Property)AGE) < 3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 53 */     int age = (Integer)state.getValue((Property)AGE);
/* 54 */     if (age < 3 && random.nextInt(10) == 0) {
/* 55 */       state = (BlockState)state.setValue((Property)AGE, age + 1);
/* 56 */       level.setBlock(pos, state, 2);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 62 */     return new ItemStack((net.minecraft.world.level.ItemLike)net.minecraft.world.item.Items.NETHER_WART);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 67 */     builder.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/NetherWartBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */