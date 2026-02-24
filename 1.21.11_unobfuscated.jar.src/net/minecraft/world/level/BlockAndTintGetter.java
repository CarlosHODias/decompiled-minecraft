/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public interface BlockAndTintGetter
/*    */   extends BlockGetter {
/*    */   float getShade(Direction paramDirection, boolean paramBoolean);
/*    */   
/*    */   LevelLightEngine getLightEngine();
/*    */   
/*    */   int getBlockTint(BlockPos paramBlockPos, ColorResolver paramColorResolver);
/*    */   
/*    */   default int getBrightness(LightLayer layer, BlockPos pos) {
/* 16 */     return getLightEngine().getLayerListener(layer).getLightValue(pos);
/*    */   }
/*    */   
/*    */   default int getRawBrightness(BlockPos pos, int darkening) {
/* 20 */     return getLightEngine().getRawBrightness(pos, darkening);
/*    */   }
/*    */   
/*    */   default boolean canSeeSky(BlockPos pos) {
/* 24 */     return (getBrightness(LightLayer.SKY, pos) >= 15);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/BlockAndTintGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */