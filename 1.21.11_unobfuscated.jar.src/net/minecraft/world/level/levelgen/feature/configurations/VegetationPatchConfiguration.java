/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class VegetationPatchConfiguration implements FeatureConfiguration {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("ground_state").forGetter(()), (App)PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter(()), (App)CaveSurface.CODEC.fieldOf("surface").forGetter(()), (App)IntProvider.codec(1, 128).fieldOf("depth").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(()), (App)Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(()), (App)IntProvider.CODEC.fieldOf("xz_radius").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, VegetationPatchConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<VegetationPatchConfiguration> CODEC;
/*    */   
/*    */   public final TagKey<Block> replaceable;
/*    */   
/*    */   public final BlockStateProvider groundState;
/*    */   
/*    */   public final Holder<PlacedFeature> vegetationFeature;
/*    */   
/*    */   public final CaveSurface surface;
/*    */   
/*    */   public final IntProvider depth;
/*    */   
/*    */   public final float extraBottomBlockChance;
/*    */   
/*    */   public final int verticalRange;
/*    */   
/*    */   public final float vegetationChance;
/*    */   
/*    */   public final IntProvider xzRadius;
/*    */   public final float extraEdgeColumnChance;
/*    */   
/*    */   public VegetationPatchConfiguration(TagKey<Block> replaceable, BlockStateProvider groundState, Holder<PlacedFeature> vegetationFeature, CaveSurface surface, IntProvider depth, float extraBottomBlockChance, int verticalRange, float vegetationChance, IntProvider xzRadius, float extraEdgeColumnChance) {
/* 42 */     this.replaceable = replaceable;
/* 43 */     this.groundState = groundState;
/* 44 */     this.vegetationFeature = vegetationFeature;
/* 45 */     this.surface = surface;
/* 46 */     this.depth = depth;
/* 47 */     this.extraBottomBlockChance = extraBottomBlockChance;
/* 48 */     this.verticalRange = verticalRange;
/* 49 */     this.vegetationChance = vegetationChance;
/* 50 */     this.xzRadius = xzRadius;
/* 51 */     this.extraEdgeColumnChance = extraEdgeColumnChance;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */