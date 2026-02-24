/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelHeightAccessor
/*    */ {
/*    */   int getHeight();
/*    */   
/*    */   int getMinY();
/*    */   
/*    */   default int getMaxY() {
/* 16 */     return getMinY() + getHeight() - 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionsCount() {
/* 21 */     return getMaxSectionY() - getMinSectionY() + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default int getMinSectionY() {
/* 26 */     return SectionPos.blockToSectionCoord(getMinY());
/*    */   }
/*    */ 
/*    */   
/*    */   default int getMaxSectionY() {
/* 31 */     return SectionPos.blockToSectionCoord(getMaxY());
/*    */   }
/*    */   
/*    */   default boolean isInsideBuildHeight(int blockY) {
/* 35 */     return (blockY >= getMinY() && blockY <= getMaxY());
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isOutsideBuildHeight(BlockPos pos) {
/* 40 */     return isOutsideBuildHeight(pos.getY());
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isOutsideBuildHeight(int blockY) {
/* 45 */     return (blockY < getMinY() || blockY > getMaxY());
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionIndex(int blockY) {
/* 50 */     return getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(blockY));
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionIndexFromSectionY(int sectionY) {
/* 55 */     return sectionY - getMinSectionY();
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionYFromSectionIndex(int sectionIndex) {
/* 60 */     return sectionIndex + getMinSectionY();
/*    */   }
/*    */   
/*    */   static LevelHeightAccessor create(final int minY, final int height) {
/* 64 */     return new LevelHeightAccessor()
/*    */       {
/*    */         public int getHeight() {
/* 67 */           return height;
/*    */         }
/*    */ 
/*    */         
/*    */         public int getMinY() {
/* 72 */           return minY;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/LevelHeightAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */