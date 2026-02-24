/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ 
/*     */ public class TreeConfiguration implements FeatureConfiguration {
/*     */   static {
/*  18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(()), (App)FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter(()), (App)RootPlacer.CODEC.optionalFieldOf("root_placer").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("dirt_provider").forGetter(()), (App)FeatureSize.CODEC.fieldOf("minimum_size").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(()), (App)Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter(()), (App)Codec.BOOL.fieldOf("force_dirt").orElse(false).forGetter(())).apply((Applicative)i, TreeConfiguration::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<TreeConfiguration> CODEC;
/*     */   
/*     */   public final BlockStateProvider trunkProvider;
/*     */   
/*     */   public final BlockStateProvider dirtProvider;
/*     */   
/*     */   public final TrunkPlacer trunkPlacer;
/*     */   
/*     */   public final BlockStateProvider foliageProvider;
/*     */   
/*     */   public final FoliagePlacer foliagePlacer;
/*     */   
/*     */   public final Optional<RootPlacer> rootPlacer;
/*     */   
/*     */   public final FeatureSize minimumSize;
/*     */   
/*     */   public final List<TreeDecorator> decorators;
/*     */   public final boolean ignoreVines;
/*     */   public final boolean forceDirt;
/*     */   
/*     */   protected TreeConfiguration(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, BlockStateProvider dirtProvider, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, boolean forceDirt) {
/*  43 */     this.trunkProvider = trunkProvider;
/*  44 */     this.trunkPlacer = trunkPlacer;
/*  45 */     this.foliageProvider = foliageProvider;
/*  46 */     this.foliagePlacer = foliagePlacer;
/*  47 */     this.rootPlacer = rootPlacer;
/*  48 */     this.dirtProvider = dirtProvider;
/*  49 */     this.minimumSize = minimumSize;
/*  50 */     this.decorators = decorators;
/*  51 */     this.ignoreVines = ignoreVines;
/*  52 */     this.forceDirt = forceDirt;
/*     */   }
/*     */   
/*     */   public static class TreeConfigurationBuilder {
/*     */     public final BlockStateProvider trunkProvider;
/*     */     private final TrunkPlacer trunkPlacer;
/*     */     public final BlockStateProvider foliageProvider;
/*     */     private final FoliagePlacer foliagePlacer;
/*     */     private final Optional<RootPlacer> rootPlacer;
/*     */     private BlockStateProvider dirtProvider;
/*     */     private final FeatureSize minimumSize;
/*  63 */     private List<TreeDecorator> decorators = (List<TreeDecorator>)com.google.common.collect.ImmutableList.of();
/*     */     private boolean ignoreVines;
/*     */     private boolean forceDirt;
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize) {
/*  68 */       this.trunkProvider = trunkProvider;
/*  69 */       this.trunkPlacer = trunkPlacer;
/*  70 */       this.foliageProvider = foliageProvider;
/*  71 */       this.dirtProvider = (BlockStateProvider)BlockStateProvider.simple(net.minecraft.world.level.block.Blocks.DIRT);
/*  72 */       this.foliagePlacer = foliagePlacer;
/*  73 */       this.rootPlacer = rootPlacer;
/*  74 */       this.minimumSize = minimumSize;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, FeatureSize minimumSize) {
/*  78 */       this(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, Optional.empty(), minimumSize);
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder dirt(BlockStateProvider dirtProvider) {
/*  82 */       this.dirtProvider = dirtProvider;
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder decorators(List<TreeDecorator> decorators) {
/*  87 */       this.decorators = decorators;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder ignoreVines() {
/*  92 */       this.ignoreVines = true;
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder forceDirt() {
/*  97 */       this.forceDirt = true;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfiguration build() {
/* 102 */       return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */