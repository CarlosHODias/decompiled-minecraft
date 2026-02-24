/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class CherryTrunkPlacer extends TrunkPlacer {
/*     */   static {
/*  24 */     BRANCH_START_CODEC = UniformInt.CODEC.codec().validate(u -> (u.getMaxValue() - u.getMinValue() < 1) ? DataResult.error(()) : DataResult.success(u));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  31 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group((App)IntProvider.codec(1, 3).fieldOf("branch_count").forGetter(()), (App)IntProvider.codec(2, 16).fieldOf("branch_horizontal_length").forGetter(()), (App)IntProvider.validateCodec(-16, 0, BRANCH_START_CODEC).fieldOf("branch_start_offset_from_top").forGetter(()), (App)IntProvider.codec(-16, 16).fieldOf("branch_end_offset_from_top").forGetter(()))).apply((Applicative)i, CherryTrunkPlacer::new));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final com.mojang.serialization.Codec<UniformInt> BRANCH_START_CODEC;
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<CherryTrunkPlacer> CODEC;
/*     */   
/*     */   private final IntProvider branchCount;
/*     */   private final IntProvider branchHorizontalLength;
/*     */   private final UniformInt branchStartOffsetFromTop;
/*     */   private final UniformInt secondBranchStartOffsetFromTop;
/*     */   private final IntProvider branchEndOffsetFromTop;
/*     */   
/*     */   public CherryTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider branchCount, IntProvider branchHorizontalLength, UniformInt branchStartOffsetFromTop, IntProvider branchEndOffsetFromTop) {
/*  46 */     super(baseHeight, heightRandA, heightRandB);
/*  47 */     this.branchCount = branchCount;
/*  48 */     this.branchHorizontalLength = branchHorizontalLength;
/*  49 */     this.branchStartOffsetFromTop = branchStartOffsetFromTop;
/*  50 */     this.secondBranchStartOffsetFromTop = UniformInt.of(branchStartOffsetFromTop.getMinValue(), branchStartOffsetFromTop.getMaxValue() - 1);
/*  51 */     this.branchEndOffsetFromTop = branchEndOffsetFromTop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  56 */     return TrunkPlacerType.CHERRY_TRUNK_PLACER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/*     */     int trunkHeight;
/*  68 */     setDirtAt(level, trunkSetter, random, origin.below(), config);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     int firstBranchOffsetFromOrigin = Math.max(0, treeHeight - 1 + this.branchStartOffsetFromTop.sample(random));
/*  80 */     int secondBranchOffsetFromOrigin = Math.max(0, treeHeight - 1 + this.secondBranchStartOffsetFromTop.sample(random));
/*  81 */     if (secondBranchOffsetFromOrigin >= firstBranchOffsetFromOrigin) {
/*  82 */       secondBranchOffsetFromOrigin++;
/*     */     }
/*     */     
/*  85 */     int branchCount = this.branchCount.sample(random);
/*  86 */     boolean hasMiddleBranch = (branchCount == 3);
/*  87 */     boolean hasBothSideBranches = (branchCount >= 2);
/*     */ 
/*     */     
/*  90 */     if (hasMiddleBranch) {
/*  91 */       trunkHeight = treeHeight;
/*  92 */     } else if (hasBothSideBranches) {
/*  93 */       trunkHeight = Math.max(firstBranchOffsetFromOrigin, secondBranchOffsetFromOrigin) + 1;
/*     */     } else {
/*  95 */       trunkHeight = firstBranchOffsetFromOrigin + 1;
/*     */     } 
/*     */     
/*  98 */     for (int y = 0; y < trunkHeight; y++) {
/*  99 */       placeLog(level, trunkSetter, random, origin.above(y), config);
/*     */     }
/*     */     
/* 102 */     List<FoliagePlacer.FoliageAttachment> attachments = new java.util.ArrayList<>();
/*     */     
/* 104 */     if (hasMiddleBranch) {
/* 105 */       attachments.add(new FoliagePlacer.FoliageAttachment(origin.above(trunkHeight), 0, false));
/*     */     }
/*     */     
/* 108 */     BlockPos.MutableBlockPos logPos = new BlockPos.MutableBlockPos();
/* 109 */     Direction treeDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*     */     
/*     */     Function<BlockState, BlockState> sidewaysStateModifier = state -> (BlockState)state.trySetValue((Property)net.minecraft.world.level.block.RotatedPillarBlock.AXIS, (Comparable)treeDirection.getAxis());
/* 112 */     attachments.add(generateBranch(level, trunkSetter, random, treeHeight, origin, config, sidewaysStateModifier, treeDirection, firstBranchOffsetFromOrigin, (firstBranchOffsetFromOrigin < trunkHeight - 1), logPos));
/*     */     
/* 114 */     if (hasBothSideBranches) {
/* 115 */       attachments.add(generateBranch(level, trunkSetter, random, treeHeight, origin, config, sidewaysStateModifier, treeDirection.getOpposite(), secondBranchOffsetFromOrigin, (secondBranchOffsetFromOrigin < trunkHeight - 1), logPos));
/*     */     }
/*     */     
/* 118 */     return attachments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FoliagePlacer.FoliageAttachment generateBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config, Function<BlockState, BlockState> sidewaysStateModifier, Direction branchDirection, int offsetFromOrigin, boolean middleContinuesUpwards, BlockPos.MutableBlockPos logPos) {
/* 134 */     logPos.set((Vec3i)origin).move(Direction.UP, offsetFromOrigin);
/*     */     
/* 136 */     int branchEndPosOffsetFromOrigin = treeHeight - 1 + this.branchEndOffsetFromTop.sample(random);
/*     */     
/* 138 */     boolean extendBranchAwayFromTrunk = (middleContinuesUpwards || branchEndPosOffsetFromOrigin < offsetFromOrigin);
/* 139 */     int distanceToTrunk = this.branchHorizontalLength.sample(random) + (extendBranchAwayFromTrunk ? 1 : 0);
/*     */     
/* 141 */     BlockPos branchEndPos = origin.relative(branchDirection, distanceToTrunk)
/* 142 */       .above(branchEndPosOffsetFromOrigin);
/*     */     
/* 144 */     int stepsHorizontally = extendBranchAwayFromTrunk ? 2 : 1;
/*     */     
/* 146 */     for (int i = 0; i < stepsHorizontally; i++) {
/* 147 */       placeLog(level, trunkSetter, random, (BlockPos)logPos.move(branchDirection), config, sidewaysStateModifier);
/*     */     }
/*     */     
/* 150 */     Direction verticalDirection = (branchEndPos.getY() > logPos.getY()) ? Direction.UP : Direction.DOWN;
/*     */     
/*     */     while (true) {
/* 153 */       int distance = logPos.distManhattan((Vec3i)branchEndPos);
/* 154 */       if (distance == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 158 */       float chanceToGrowVertically = Math.abs(branchEndPos.getY() - logPos.getY()) / distance;
/* 159 */       boolean growVertically = (random.nextFloat() < chanceToGrowVertically);
/*     */       
/* 161 */       logPos.move(growVertically ? verticalDirection : branchDirection);
/* 162 */       placeLog(level, trunkSetter, random, (BlockPos)logPos, config, growVertically ? Function.<BlockState>identity() : sidewaysStateModifier);
/*     */     } 
/*     */     
/* 165 */     return new FoliagePlacer.FoliageAttachment(branchEndPos.above(), 0, false);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/trunkplacers/CherryTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */