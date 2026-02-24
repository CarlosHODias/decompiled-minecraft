/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.ColorResolver;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ public class RenderSectionRegion
/*    */   implements BlockAndTintGetter
/*    */ {
/*    */   public static final int RADIUS = 1;
/*    */   public static final int SIZE = 3;
/*    */   private final int minSectionX;
/*    */   private final int minSectionY;
/*    */   private final int minSectionZ;
/*    */   private final SectionCopy[] sections;
/*    */   private final Level level;
/*    */   
/*    */   RenderSectionRegion(Level level, int minSectionX, int minSectionY, int minSectionZ, SectionCopy[] sections) {
/* 27 */     this.level = level;
/* 28 */     this.minSectionX = minSectionX;
/* 29 */     this.minSectionY = minSectionY;
/* 30 */     this.minSectionZ = minSectionZ;
/* 31 */     this.sections = sections;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 36 */     return getSection(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockState(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 41 */     return getSection(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockState(pos).getFluidState();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getShade(Direction direction, boolean shade) {
/* 46 */     return this.level.getShade(direction, shade);
/*    */   }
/*    */ 
/*    */   
/*    */   public LevelLightEngine getLightEngine() {
/* 51 */     return this.level.getLightEngine();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 56 */     return getSection(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockEntity(pos);
/*    */   }
/*    */   
/*    */   private SectionCopy getSection(int sectionX, int sectionY, int sectionZ) {
/* 60 */     return this.sections[index(this.minSectionX, this.minSectionY, this.minSectionZ, sectionX, sectionY, sectionZ)];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBlockTint(BlockPos pos, ColorResolver resolver) {
/* 65 */     return this.level.getBlockTint(pos, resolver);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinY() {
/* 70 */     return this.level.getMinY();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 75 */     return this.level.getHeight();
/*    */   }
/*    */   
/*    */   public static int index(int minSectionX, int minSectionY, int minSectionZ, int sectionX, int sectionY, int sectionZ) {
/* 79 */     return sectionX - minSectionX + (sectionY - minSectionY) * 3 + (sectionZ - minSectionZ) * 3 * 3;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/RenderSectionRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */