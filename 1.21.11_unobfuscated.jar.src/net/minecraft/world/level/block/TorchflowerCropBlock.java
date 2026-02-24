/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TorchflowerCropBlock extends CropBlock {
/* 19 */   public static final MapCodec<TorchflowerCropBlock> CODEC = simpleCodec(TorchflowerCropBlock::new);
/*    */   public static final int MAX_AGE = 1;
/*    */   
/*    */   public MapCodec<TorchflowerCropBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1; private static final VoxelShape[] SHAPES; private static final int BONEMEAL_INCREASE = 1;
/*    */   static {
/* 29 */     SHAPES = Block.boxes(1, age -> Block.column(6.0D, 0.0D, (6 + age * 4)));
/*    */   }
/*    */ 
/*    */   
/*    */   public TorchflowerCropBlock(BlockBehaviour.Properties properties) {
/* 34 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 39 */     builder.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 44 */     return SHAPES[getAge(state)];
/*    */   }
/*    */ 
/*    */   
/*    */   protected IntegerProperty getAgeProperty() {
/* 49 */     return AGE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMaxAge() {
/* 55 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemLike getBaseSeedId() {
/* 60 */     return (ItemLike)net.minecraft.world.item.Items.TORCHFLOWER_SEEDS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForAge(int age) {
/* 66 */     if (age == 2) {
/* 67 */       return Blocks.TORCHFLOWER.defaultBlockState();
/*    */     }
/* 69 */     return super.getStateForAge(age);
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 74 */     if (random.nextInt(3) != 0) {
/* 75 */       super.randomTick(state, level, pos, random);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBonemealAgeIncrease(net.minecraft.world.level.Level level) {
/* 81 */     return 1;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TorchflowerCropBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */