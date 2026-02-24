/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DeltaFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("contents").forGetter(()), (App)BlockState.CODEC.fieldOf("rim").forGetter(()), (App)IntProvider.codec(0, 16).fieldOf("size").forGetter(()), (App)IntProvider.codec(0, 16).fieldOf("rim_size").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, DeltaFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<DeltaFeatureConfiguration> CODEC;
/*    */   
/*    */   private final BlockState contents;
/*    */   
/*    */   private final BlockState rim;
/*    */   private final IntProvider size;
/*    */   private final IntProvider rimSize;
/*    */   
/*    */   public DeltaFeatureConfiguration(BlockState contents, BlockState rim, IntProvider size, IntProvider rimSize) {
/* 22 */     this.contents = contents;
/* 23 */     this.rim = rim;
/* 24 */     this.size = size;
/* 25 */     this.rimSize = rimSize;
/*    */   }
/*    */   
/*    */   public BlockState contents() {
/* 29 */     return this.contents;
/*    */   }
/*    */   
/*    */   public BlockState rim() {
/* 33 */     return this.rim;
/*    */   }
/*    */   
/*    */   public IntProvider size() {
/* 37 */     return this.size;
/*    */   }
/*    */   
/*    */   public IntProvider rimSize() {
/* 41 */     return this.rimSize;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/DeltaFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */