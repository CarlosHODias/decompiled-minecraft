/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockAgeProcessor extends StructureProcessor {
/*     */   public static final com.mojang.serialization.MapCodec<BlockAgeProcessor> CODEC;
/*     */   
/*     */   static {
/*  19 */     CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(BlockAgeProcessor::new, p -> p.mossiness);
/*     */   }
/*     */   private static final float PROBABILITY_OF_REPLACING_FULL_BLOCK = 0.5F;
/*     */   private static final float PROBABILITY_OF_REPLACING_STAIRS = 0.5F;
/*     */   private static final float PROBABILITY_OF_REPLACING_OBSIDIAN = 0.15F;
/*  24 */   private static final BlockState[] NON_MOSSY_REPLACEMENTS = new BlockState[] {
/*  25 */       Blocks.STONE_SLAB.defaultBlockState(), 
/*  26 */       Blocks.STONE_BRICK_SLAB.defaultBlockState()
/*     */     };
/*     */   
/*     */   private final float mossiness;
/*     */   
/*     */   public BlockAgeProcessor(float mossiness) {
/*  32 */     this.mossiness = mossiness;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/*  37 */     RandomSource random = settings.getRandom(processedBlockInfo.pos());
/*     */     
/*  39 */     BlockState state = processedBlockInfo.state();
/*  40 */     BlockPos pos = processedBlockInfo.pos();
/*  41 */     BlockState newState = null;
/*  42 */     if (state.is(Blocks.STONE_BRICKS) || state.is(Blocks.STONE) || state.is(Blocks.CHISELED_STONE_BRICKS)) {
/*  43 */       newState = maybeReplaceFullStoneBlock(random);
/*  44 */     } else if (state.is(BlockTags.STAIRS)) {
/*  45 */       newState = maybeReplaceStairs(state, random);
/*  46 */     } else if (state.is(BlockTags.SLABS)) {
/*  47 */       newState = maybeReplaceSlab(state, random);
/*  48 */     } else if (state.is(BlockTags.WALLS)) {
/*  49 */       newState = maybeReplaceWall(state, random);
/*  50 */     } else if (state.is(Blocks.OBSIDIAN)) {
/*  51 */       newState = maybeReplaceObsidian(random);
/*     */     } 
/*  53 */     if (newState != null) {
/*  54 */       return new StructureTemplate.StructureBlockInfo(pos, newState, processedBlockInfo.nbt());
/*     */     }
/*  56 */     return processedBlockInfo;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceFullStoneBlock(RandomSource random) {
/*  60 */     if (random.nextFloat() >= 0.5F) {
/*  61 */       return null;
/*     */     }
/*     */     
/*  64 */     BlockState[] nonMossyReplacements = { Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 
/*  65 */         getRandomFacingStairs(random, Blocks.STONE_BRICK_STAIRS) };
/*     */ 
/*     */     
/*  68 */     BlockState[] mossyReplacements = { Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 
/*  69 */         getRandomFacingStairs(random, Blocks.MOSSY_STONE_BRICK_STAIRS) };
/*     */ 
/*     */     
/*  72 */     return getRandomBlock(random, nonMossyReplacements, mossyReplacements);
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceStairs(BlockState blockState, RandomSource random) {
/*  76 */     if (random.nextFloat() >= 0.5F) {
/*  77 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  81 */     BlockState[] mossyReplacements = { Blocks.MOSSY_STONE_BRICK_STAIRS.withPropertiesOf(blockState), 
/*  82 */         Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() };
/*     */ 
/*     */     
/*  85 */     return getRandomBlock(random, NON_MOSSY_REPLACEMENTS, mossyReplacements);
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceSlab(BlockState blockState, RandomSource random) {
/*  89 */     if (random.nextFloat() < this.mossiness) {
/*  90 */       return Blocks.MOSSY_STONE_BRICK_SLAB.withPropertiesOf(blockState);
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceWall(BlockState blockState, RandomSource random) {
/*  96 */     if (random.nextFloat() < this.mossiness) {
/*  97 */       return Blocks.MOSSY_STONE_BRICK_WALL.withPropertiesOf(blockState);
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceObsidian(RandomSource random) {
/* 103 */     if (random.nextFloat() < 0.15F) {
/* 104 */       return Blocks.CRYING_OBSIDIAN.defaultBlockState();
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   private static BlockState getRandomFacingStairs(RandomSource random, Block stairBlock) {
/* 110 */     return (BlockState)((BlockState)stairBlock.defaultBlockState()
/* 111 */       .setValue((Property)StairBlock.FACING, (Comparable)net.minecraft.core.Direction.Plane.HORIZONTAL.getRandomDirection(random)))
/* 112 */       .setValue((Property)StairBlock.HALF, (Comparable)Util.getRandom((Object[])net.minecraft.world.level.block.state.properties.Half.values(), random));
/*     */   }
/*     */   
/*     */   private BlockState getRandomBlock(RandomSource random, BlockState[] nonMossyBlocks, BlockState[] mossyBlocks) {
/* 116 */     if (random.nextFloat() < this.mossiness) {
/* 117 */       return getRandomBlock(random, mossyBlocks);
/*     */     }
/* 119 */     return getRandomBlock(random, nonMossyBlocks);
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockState getRandomBlock(RandomSource random, BlockState[] blocks) {
/* 124 */     return blocks[random.nextInt(blocks.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   protected StructureProcessorType<?> getType() {
/* 129 */     return StructureProcessorType.BLOCK_AGE;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/BlockAgeProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */