/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.BiomeColors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GrassColor;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DoublePlantBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockColors
/*     */ {
/*     */   private static final int DEFAULT = -1;
/*     */   public static final int LILY_PAD_IN_WORLD = -14647248;
/*     */   public static final int LILY_PAD_DEFAULT = -9321636;
/*  35 */   private final IdMapper<BlockColor> blockColors = new IdMapper(32);
/*  36 */   private final Map<Block, Set<Property<?>>> coloringStates = Maps.newHashMap();
/*     */   
/*     */   public static BlockColors createDefault() {
/*  39 */     BlockColors colors = new BlockColors();
/*     */     
/*  41 */     colors.register((state, level, pos, tintIndex) -> 
/*  42 */         (level == null || pos == null) ? GrassColor.getDefaultColor() : BiomeColors.getAverageGrassColor(level, (state.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos), new Block[] { Blocks.LARGE_FERN, Blocks.TALL_GRASS });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     colors.addColoringState((Property<?>)DoublePlantBlock.HALF, new Block[] { Blocks.LARGE_FERN, Blocks.TALL_GRASS });
/*     */     
/*  49 */     colors.register((state, level, pos, tintIndex) -> 
/*  50 */         (level == null || pos == null) ? GrassColor.getDefaultColor() : BiomeColors.getAverageGrassColor(level, pos), new Block[] { Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.SHORT_GRASS, Blocks.POTTED_FERN, Blocks.BUSH });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     colors.register((state, level, pos, tintIndex) -> (tintIndex != 0) ? (
/*     */         
/*  58 */         (level == null || pos == null) ? GrassColor.getDefaultColor() : BiomeColors.getAverageGrassColor(level, pos)) : -1, new Block[] { Blocks.PINK_PETALS, Blocks.WILDFLOWERS });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     colors.register((state, level, pos, tintIndex) -> -10380959, new Block[] { Blocks.SPRUCE_LEAVES });
/*  68 */     colors.register((state, level, pos, tintIndex) -> -8345771, new Block[] { Blocks.BIRCH_LEAVES });
/*     */     
/*  70 */     colors.register((state, level, pos, tintIndex) -> 
/*  71 */         (level == null || pos == null) ? -12012264 : BiomeColors.getAverageFoliageColor(level, pos), new Block[] { Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     colors.register((state, level, pos, tintIndex) -> 
/*  78 */         (level == null || pos == null) ? -10732494 : BiomeColors.getAverageDryFoliageColor(level, pos), new Block[] { Blocks.LEAF_LITTER });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     colors.register((state, level, pos, tintIndex) -> 
/*  85 */         (level == null || pos == null) ? -1 : BiomeColors.getAverageWaterColor(level, pos), new Block[] { Blocks.WATER, Blocks.BUBBLE_COLUMN, Blocks.WATER_CAULDRON });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     colors.register((state, level, pos, tintIndex) -> RedStoneWireBlock.getColorForPower((Integer)state.getValue((Property)RedStoneWireBlock.POWER)), new Block[] { Blocks.REDSTONE_WIRE });
/*  92 */     colors.addColoringState((Property<?>)RedStoneWireBlock.POWER, new Block[] { Blocks.REDSTONE_WIRE });
/*     */     
/*  94 */     colors.register((state, level, pos, tintIndex) -> 
/*  95 */         (level == null || pos == null) ? -1 : BiomeColors.getAverageGrassColor(level, pos), new Block[] { Blocks.SUGAR_CANE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     colors.register((state, level, pos, tintIndex) -> -2046180, new Block[] { Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM });
/*     */     
/* 103 */     colors.register((state, level, pos, tintIndex) -> { int age = (Integer)state.getValue((Property)StemBlock.AGE); return ARGB.color(age * 32, 255 - age * 8, age * 4); }, new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM });
/*     */ 
/*     */ 
/*     */     
/* 107 */     colors.addColoringState((Property<?>)StemBlock.AGE, new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM });
/*     */     
/* 109 */     colors.register((state, level, pos, tintIndex) -> 
/* 110 */         (level == null || pos == null) ? -9321636 : -14647248, new Block[] { Blocks.LILY_PAD });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return colors;
/*     */   }
/*     */   
/*     */   public int getColor(BlockState state, Level level, BlockPos blockPos) {
/* 120 */     BlockColor blockColor = (BlockColor)this.blockColors.byId(BuiltInRegistries.BLOCK.getId(state.getBlock()));
/*     */     
/* 122 */     if (blockColor != null) {
/* 123 */       return blockColor.getColor(state, null, null, 0);
/*     */     }
/*     */     
/* 126 */     MapColor color = state.getMapColor((BlockGetter)level, blockPos);
/* 127 */     return (color != null) ? color.col : -1;
/*     */   }
/*     */   
/*     */   public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
/* 131 */     BlockColor blockColor = (BlockColor)this.blockColors.byId(BuiltInRegistries.BLOCK.getId(state.getBlock()));
/* 132 */     return (blockColor == null) ? -1 : blockColor.getColor(state, level, pos, tintIndex);
/*     */   }
/*     */   
/*     */   public void register(BlockColor color, Block... blocks) {
/* 136 */     for (Block block : blocks) {
/* 137 */       this.blockColors.addMapping(color, BuiltInRegistries.BLOCK.getId(block));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addColoringStates(Set<Property<?>> properties, Block... blocks) {
/* 142 */     for (Block block : blocks) {
/* 143 */       this.coloringStates.put(block, properties);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addColoringState(Property<?> property, Block... blocks) {
/* 148 */     addColoringStates((Set<Property<?>>)ImmutableSet.of(property), blocks);
/*     */   }
/*     */   
/*     */   public Set<Property<?>> getColoringProperties(Block block) {
/* 152 */     return (Set<Property<?>>)this.coloringStates.getOrDefault(block, ImmutableSet.of());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/block/BlockColors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */