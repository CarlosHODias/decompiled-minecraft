/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FungusBlock extends VegetationBlock implements BonemealableBlock {
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("grows_on").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, FungusBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FungusBlock> CODEC;
/*    */   private static final double BONEMEAL_SUCCESS_PROBABILITY = 0.4D;
/*    */   
/*    */   public MapCodec<FungusBlock> codec() {
/* 32 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 9.0D);
/*    */   
/*    */   private final Block requiredBlock;
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   protected FungusBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Block requiredBlock, BlockBehaviour.Properties properties) {
/* 43 */     super(properties);
/* 44 */     this.feature = feature;
/* 45 */     this.requiredBlock = requiredBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 50 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 55 */     return (state.is(net.minecraft.tags.BlockTags.NYLIUM) || state.is(Blocks.MYCELIUM) || state.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(state, level, pos));
/*    */   }
/*    */   
/*    */   private java.util.Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader level) {
/* 59 */     return level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 65 */     BlockState belowState = level.getBlockState(pos.below());
/* 66 */     return belowState.is(this.requiredBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 71 */     return (random.nextFloat() < 0.4D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 76 */     getFeature((LevelReader)level).ifPresent(feature -> ((ConfiguredFeature)feature.value()).place((WorldGenLevel)level, level.getChunkSource().getGenerator(), random, pos));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/FungusBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */