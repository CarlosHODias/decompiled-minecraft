/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*    */ 
/*    */ public class MultifaceGrowthConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<MultifaceGrowthConfiguration> CODEC;
/*    */   
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").flatXmap(MultifaceGrowthConfiguration::apply, DataResult::success).orElse(Blocks.GLOW_LICHEN).forGetter(()), (App)Codec.intRange(1, 64).fieldOf("search_range").orElse(10).forGetter(()), (App)Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter(()), (App)Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false).forGetter(()), (App)Codec.BOOL.fieldOf("can_place_on_wall").orElse(false).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spreading").orElse(0.5F).forGetter(()), (App)net.minecraft.core.RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_be_placed_on").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, MultifaceGrowthConfiguration::new));
/*    */   }
/*    */   public final MultifaceSpreadeableBlock placeBlock; public final int searchRange;
/*    */   public final boolean canPlaceOnFloor;
/*    */   public final boolean canPlaceOnCeiling;
/*    */   public final boolean canPlaceOnWall;
/*    */   public final float chanceOfSpreading;
/*    */   public final HolderSet<Block> canBePlacedOn;
/*    */   private final ObjectArrayList<Direction> validDirections;
/*    */   
/*    */   private static DataResult<MultifaceSpreadeableBlock> apply(Block block) {
/* 32 */     MultifaceSpreadeableBlock multifaceBlock = (MultifaceSpreadeableBlock)block; return (block instanceof MultifaceSpreadeableBlock) ? 
/* 33 */       DataResult.success(multifaceBlock) : 
/* 34 */       DataResult.error(() -> "Growth block should be a multiface spreadeable block");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MultifaceGrowthConfiguration(MultifaceSpreadeableBlock placeBlock, int searchRange, boolean canPlaceOnFloor, boolean canPlaceOnCeiling, boolean canPlaceOnWall, float chanceOfSpreading, HolderSet<Block> canBePlacedOn) {
/* 49 */     this.placeBlock = placeBlock;
/* 50 */     this.searchRange = searchRange;
/* 51 */     this.canPlaceOnFloor = canPlaceOnFloor;
/* 52 */     this.canPlaceOnCeiling = canPlaceOnCeiling;
/* 53 */     this.canPlaceOnWall = canPlaceOnWall;
/* 54 */     this.chanceOfSpreading = chanceOfSpreading;
/* 55 */     this.canBePlacedOn = canBePlacedOn;
/*    */     
/* 57 */     this.validDirections = new ObjectArrayList(6);
/* 58 */     if (canPlaceOnCeiling) {
/* 59 */       this.validDirections.add(Direction.UP);
/*    */     }
/* 61 */     if (canPlaceOnFloor) {
/* 62 */       this.validDirections.add(Direction.DOWN);
/*    */     }
/* 64 */     if (canPlaceOnWall) {
/* 65 */       java.util.Objects.requireNonNull(this.validDirections); Direction.Plane.HORIZONTAL.forEach(this.validDirections::add);
/*    */     } 
/*    */   }
/*    */   
/*    */   public java.util.List<Direction> getShuffledDirectionsExcept(RandomSource random, Direction excludeDirection) {
/* 70 */     return Util.toShuffledList(this.validDirections.stream().filter(direction -> (direction != excludeDirection)), random);
/*    */   }
/*    */   
/*    */   public java.util.List<Direction> getShuffledDirections(RandomSource random) {
/* 74 */     return Util.shuffledCopy(this.validDirections, random);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/MultifaceGrowthConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */