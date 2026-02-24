/*     */ package net.minecraft.world.level.levelgen.blockpredicates;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public interface BlockPredicate
/*     */   extends BiPredicate<WorldGenLevel, BlockPos> {
/*  21 */   public static final Codec<BlockPredicate> CODEC = BuiltInRegistries.BLOCK_PREDICATE_TYPE.byNameCodec().dispatch(BlockPredicate::type, BlockPredicateType::codec);
/*     */ 
/*     */ 
/*     */   
/*  25 */   public static final BlockPredicate ONLY_IN_AIR_PREDICATE = matchesBlocks(new Block[] { Blocks.AIR });
/*  26 */   public static final BlockPredicate ONLY_IN_AIR_OR_WATER_PREDICATE = matchesBlocks(new Block[] { Blocks.AIR, Blocks.WATER });
/*     */   
/*     */   BlockPredicateType<?> type();
/*     */   
/*     */   static BlockPredicate allOf(List<BlockPredicate> predicates) {
/*  31 */     return new AllOfPredicate(predicates);
/*     */   }
/*     */   
/*     */   static BlockPredicate allOf(BlockPredicate... predicates) {
/*  35 */     return allOf(List.of(predicates));
/*     */   }
/*     */   
/*     */   static BlockPredicate allOf(BlockPredicate a, BlockPredicate b) {
/*  39 */     return allOf(List.of(a, b));
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(List<BlockPredicate> predicates) {
/*  43 */     return new AnyOfPredicate(predicates);
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(BlockPredicate... predicates) {
/*  47 */     return anyOf(List.of(predicates));
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(BlockPredicate a, BlockPredicate b) {
/*  51 */     return anyOf(List.of(a, b));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Vec3i offset, List<Block> blocks) {
/*  55 */     return new MatchingBlocksPredicate(offset, (HolderSet<Block>)HolderSet.direct(Block::builtInRegistryHolder, blocks));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(List<Block> blocks) {
/*  59 */     return matchesBlocks(Vec3i.ZERO, blocks);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Vec3i offset, Block... blocks) {
/*  63 */     return matchesBlocks(offset, List.of(blocks));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Block... blocks) {
/*  67 */     return matchesBlocks(Vec3i.ZERO, blocks);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesTag(Vec3i offset, TagKey<Block> tag) {
/*  71 */     return new MatchingBlockTagPredicate(offset, tag);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesTag(TagKey<Block> tag) {
/*  75 */     return matchesTag(Vec3i.ZERO, tag);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Vec3i offset, List<Fluid> fluids) {
/*  79 */     return new MatchingFluidsPredicate(offset, (HolderSet<Fluid>)HolderSet.direct(Fluid::builtInRegistryHolder, fluids));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Vec3i offset, Fluid... fluids) {
/*  83 */     return matchesFluids(offset, List.of(fluids));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Fluid... fluids) {
/*  87 */     return matchesFluids(Vec3i.ZERO, fluids);
/*     */   }
/*     */   
/*     */   static BlockPredicate not(BlockPredicate predicate) {
/*  91 */     return new NotPredicate(predicate);
/*     */   }
/*     */   
/*     */   static BlockPredicate replaceable(Vec3i offset) {
/*  95 */     return new ReplaceablePredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate replaceable() {
/*  99 */     return replaceable(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate wouldSurvive(BlockState state, Vec3i offset) {
/* 103 */     return new WouldSurvivePredicate(offset, state);
/*     */   }
/*     */   
/*     */   static BlockPredicate hasSturdyFace(Vec3i offset, Direction direction) {
/* 107 */     return new HasSturdyFacePredicate(offset, direction);
/*     */   }
/*     */   
/*     */   static BlockPredicate hasSturdyFace(Direction direction) {
/* 111 */     return hasSturdyFace(Vec3i.ZERO, direction);
/*     */   }
/*     */   
/*     */   static BlockPredicate solid(Vec3i offset) {
/* 115 */     return new SolidPredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate solid() {
/* 119 */     return solid(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate noFluid() {
/* 123 */     return noFluid(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate noFluid(Vec3i offset) {
/* 127 */     return matchesFluids(offset, new Fluid[] { Fluids.EMPTY });
/*     */   }
/*     */   
/*     */   static BlockPredicate insideWorld(Vec3i offset) {
/* 131 */     return new InsideWorldBoundsPredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate alwaysTrue() {
/* 135 */     return TrueBlockPredicate.INSTANCE;
/*     */   }
/*     */   
/*     */   static BlockPredicate unobstructed(Vec3i offset) {
/* 139 */     return new UnobstructedPredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate unobstructed() {
/* 143 */     return unobstructed(Vec3i.ZERO);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/BlockPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */