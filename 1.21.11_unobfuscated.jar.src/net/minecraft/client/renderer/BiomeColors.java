/*    */ package net.minecraft.client.renderer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class BiomeColors {
/*    */   public static final net.minecraft.world.level.ColorResolver FOLIAGE_COLOR_RESOLVER;
/*    */   public static final net.minecraft.world.level.ColorResolver DRY_FOLIAGE_COLOR_RESOLVER;
/*  9 */   public static final net.minecraft.world.level.ColorResolver GRASS_COLOR_RESOLVER = Biome::getGrassColor; public static final net.minecraft.world.level.ColorResolver WATER_COLOR_RESOLVER; static {
/* 10 */     FOLIAGE_COLOR_RESOLVER = ((biome, x, z) -> biome.getFoliageColor());
/* 11 */     DRY_FOLIAGE_COLOR_RESOLVER = ((biome, x, z) -> biome.getDryFoliageColor());
/* 12 */     WATER_COLOR_RESOLVER = ((biome, x, z) -> biome.getWaterColor());
/*    */   }
/*    */   private static int getAverageColor(BlockAndTintGetter level, BlockPos pos, net.minecraft.world.level.ColorResolver colorResolver) {
/* 15 */     return level.getBlockTint(pos, colorResolver);
/*    */   }
/*    */   
/*    */   public static int getAverageGrassColor(BlockAndTintGetter level, BlockPos pos) {
/* 19 */     return getAverageColor(level, pos, GRASS_COLOR_RESOLVER);
/*    */   }
/*    */   
/*    */   public static int getAverageFoliageColor(BlockAndTintGetter level, BlockPos pos) {
/* 23 */     return getAverageColor(level, pos, FOLIAGE_COLOR_RESOLVER);
/*    */   }
/*    */   
/*    */   public static int getAverageDryFoliageColor(BlockAndTintGetter level, BlockPos pos) {
/* 27 */     return getAverageColor(level, pos, DRY_FOLIAGE_COLOR_RESOLVER);
/*    */   }
/*    */   
/*    */   public static int getAverageWaterColor(BlockAndTintGetter level, BlockPos pos) {
/* 31 */     return getAverageColor(level, pos, WATER_COLOR_RESOLVER);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/BiomeColors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */