/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MushroomBlock extends VegetationBlock implements BonemealableBlock {
/*     */   static {
/*  23 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, MushroomBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<MushroomBlock> CODEC;
/*     */   
/*     */   public MapCodec<MushroomBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 6.0D);
/*     */   
/*     */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*     */   
/*     */   public MushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
/*  38 */     super(properties);
/*  39 */     this.feature = feature;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  44 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  49 */     if (random.nextInt(25) == 0) {
/*  50 */       int max = 5;
/*  51 */       int r = 4;
/*  52 */       for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
/*  53 */         if (level.getBlockState(blockPos).is(this) && --max <= 0) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  59 */       BlockPos offset = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
/*  60 */       for (int i = 0; i < 4; i++) {
/*  61 */         if (level.isEmptyBlock(offset) && state.canSurvive((LevelReader)level, offset)) {
/*  62 */           pos = offset;
/*     */         }
/*  64 */         offset = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
/*     */       } 
/*     */       
/*  67 */       if (level.isEmptyBlock(offset) && state.canSurvive((LevelReader)level, offset)) {
/*  68 */         level.setBlock(offset, state, 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  75 */     return state.isSolidRender();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  80 */     BlockPos belowPos = pos.below();
/*  81 */     BlockState below = level.getBlockState(belowPos);
/*  82 */     if (below.is(net.minecraft.tags.BlockTags.MUSHROOM_GROW_BLOCK)) {
/*  83 */       return true;
/*     */     }
/*     */     
/*  86 */     return (level.getRawBrightness(pos, 0) < 13 && mayPlaceOn(below, (BlockGetter)level, belowPos));
/*     */   }
/*     */   
/*     */   public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/*  90 */     Optional<? extends Holder<ConfiguredFeature<?, ?>>> feature = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/*  91 */     if (feature.isEmpty()) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     level.removeBlock(pos, false);
/*     */     
/*  97 */     if (((ConfiguredFeature)((Holder)feature.get()).value()).place((net.minecraft.world.level.WorldGenLevel)level, level.getChunkSource().getGenerator(), random, pos)) {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     level.setBlock(pos, state, 3);
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 112 */     return (random.nextFloat() < 0.4D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 117 */     growMushroom(level, pos, state, random);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MushroomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */