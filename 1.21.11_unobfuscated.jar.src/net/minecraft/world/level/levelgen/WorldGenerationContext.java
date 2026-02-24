/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ 
/*    */ public class WorldGenerationContext {
/*    */   private final int minY;
/*    */   private final int height;
/*    */   
/*    */   public WorldGenerationContext(ChunkGenerator generator, LevelHeightAccessor heightAccessor) {
/* 11 */     this.minY = Math.max(heightAccessor.getMinY(), generator.getMinY());
/* 12 */     this.height = Math.min(heightAccessor.getHeight(), generator.getGenDepth());
/*    */   }
/*    */   
/*    */   public int getMinGenY() {
/* 16 */     return this.minY;
/*    */   }
/*    */   
/*    */   public int getGenDepth() {
/* 20 */     return this.height;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/WorldGenerationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */