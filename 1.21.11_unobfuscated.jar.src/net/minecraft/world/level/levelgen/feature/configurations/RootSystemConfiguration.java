/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RootSystemConfiguration implements FeatureConfiguration {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("required_vertical_space_for_tree").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("root_radius").forGetter(()), (App)TagKey.hashedCodec(net.minecraft.core.registries.Registries.BLOCK).fieldOf("root_replaceable").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("root_state_provider").forGetter(()), (App)Codec.intRange(1, 256).fieldOf("root_placement_attempts").forGetter(()), (App)Codec.intRange(1, 4096).fieldOf("root_column_max_height").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("hanging_root_radius").forGetter(()), (App)Codec.intRange(1, 16).fieldOf("hanging_roots_vertical_span").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("hanging_root_state_provider").forGetter(()), (App)Codec.intRange(1, 256).fieldOf("hanging_root_placement_attempts").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("allowed_vertical_water_for_tree").forGetter(()), (App)BlockPredicate.CODEC.fieldOf("allowed_tree_position").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, RootSystemConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RootSystemConfiguration> CODEC;
/*    */   
/*    */   public final Holder<PlacedFeature> treeFeature;
/*    */   
/*    */   public final int requiredVerticalSpaceForTree;
/*    */   
/*    */   public final int rootRadius;
/*    */   
/*    */   public final TagKey<Block> rootReplaceable;
/*    */   
/*    */   public final BlockStateProvider rootStateProvider;
/*    */   
/*    */   public final int rootPlacementAttempts;
/*    */   
/*    */   public final int rootColumnMaxHeight;
/*    */   
/*    */   public final int hangingRootRadius;
/*    */   
/*    */   public final int hangingRootsVerticalSpan;
/*    */   
/*    */   public final BlockStateProvider hangingRootStateProvider;
/*    */   
/*    */   public final int hangingRootPlacementAttempts;
/*    */   public final int allowedVerticalWaterForTree;
/*    */   public final BlockPredicate allowedTreePosition;
/*    */   
/*    */   public RootSystemConfiguration(Holder<PlacedFeature> treeFeature, int requiredVerticalSpaceForTree, int rootRadius, TagKey<Block> rootReplaceable, BlockStateProvider rootStateProvider, int rootPlacementAttempts, int rootColumnMaxHeight, int hangingRootRadius, int hangingRootsVerticalSpan, BlockStateProvider hangingRootStateProvider, int hangingRootPlacementAttempts, int allowedVerticalWaterForTree, BlockPredicate allowedTreePosition) {
/* 45 */     this.treeFeature = treeFeature;
/* 46 */     this.requiredVerticalSpaceForTree = requiredVerticalSpaceForTree;
/* 47 */     this.rootRadius = rootRadius;
/* 48 */     this.rootReplaceable = rootReplaceable;
/* 49 */     this.rootStateProvider = rootStateProvider;
/* 50 */     this.rootPlacementAttempts = rootPlacementAttempts;
/* 51 */     this.rootColumnMaxHeight = rootColumnMaxHeight;
/* 52 */     this.hangingRootRadius = hangingRootRadius;
/* 53 */     this.hangingRootsVerticalSpan = hangingRootsVerticalSpan;
/* 54 */     this.hangingRootStateProvider = hangingRootStateProvider;
/* 55 */     this.hangingRootPlacementAttempts = hangingRootPlacementAttempts;
/* 56 */     this.allowedVerticalWaterForTree = allowedVerticalWaterForTree;
/* 57 */     this.allowedTreePosition = allowedTreePosition;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */