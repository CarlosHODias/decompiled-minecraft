/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.grower.TreeGrower;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SaplingBlock extends VegetationBlock implements BonemealableBlock {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TreeGrower.CODEC.fieldOf("tree").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, SaplingBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SaplingBlock> CODEC;
/*    */   
/*    */   public MapCodec<? extends SaplingBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty STAGE = net.minecraft.world.level.block.state.properties.BlockStateProperties.STAGE;
/*    */   
/* 32 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*    */   
/*    */   protected final TreeGrower treeGrower;
/*    */   
/*    */   protected SaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
/* 37 */     super(properties);
/* 38 */     this.treeGrower = treeGrower;
/* 39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STAGE, 0));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 44 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 49 */     if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && 
/* 50 */       random.nextInt(7) == 0) {
/* 51 */       advanceTree(level, pos, state, random);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/* 57 */     if ((Integer)state.getValue((Property)STAGE) == 0) {
/* 58 */       level.setBlock(pos, (BlockState)state.cycle((Property)STAGE), 260);
/*    */     } else {
/* 60 */       this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(net.minecraft.world.level.LevelReader level, BlockPos pos, BlockState state) {
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 71 */     return (level.random.nextFloat() < 0.45D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 76 */     advanceTree(level, pos, state, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 81 */     builder.add(new Property[] { (Property)STAGE });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SaplingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */