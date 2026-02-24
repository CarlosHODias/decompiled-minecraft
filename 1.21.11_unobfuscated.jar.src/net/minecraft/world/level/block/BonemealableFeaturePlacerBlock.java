/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class BonemealableFeaturePlacerBlock extends Block implements BonemealableBlock {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, BonemealableFeaturePlacerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<BonemealableFeaturePlacerBlock> CODEC;
/*    */   
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<BonemealableFeaturePlacerBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   public BonemealableFeaturePlacerBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.feature = feature;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(net.minecraft.world.level.LevelReader level, BlockPos pos, BlockState state) {
/* 35 */     return level.getBlockState(pos.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 45 */     level.registryAccess()
/* 46 */       .lookup(Registries.CONFIGURED_FEATURE)
/* 47 */       .flatMap(registry -> registry.get(this.feature))
/* 48 */       .ifPresent(mossPatch -> ((ConfiguredFeature)mossPatch.value()).place((net.minecraft.world.level.WorldGenLevel)level, level.getChunkSource().getGenerator(), random, pos.above()));
/*    */   }
/*    */ 
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 53 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BonemealableFeaturePlacerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */