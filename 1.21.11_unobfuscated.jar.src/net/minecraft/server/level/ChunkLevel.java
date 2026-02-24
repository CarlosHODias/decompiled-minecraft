/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChunkLevel
/*    */ {
/*    */   private static final int FULL_CHUNK_LEVEL = 33;
/*    */   private static final int BLOCK_TICKING_LEVEL = 32;
/*    */   private static final int ENTITY_TICKING_LEVEL = 31;
/* 16 */   private static final ChunkStep FULL_CHUNK_STEP = ChunkPyramid.GENERATION_PYRAMID.getStepTo(ChunkStatus.FULL);
/* 17 */   public static final int RADIUS_AROUND_FULL_CHUNK = FULL_CHUNK_STEP.accumulatedDependencies().getRadius();
/* 18 */   public static final int MAX_LEVEL = 33 + RADIUS_AROUND_FULL_CHUNK;
/*    */   
/*    */   public static ChunkStatus generationStatus(int level) {
/* 21 */     return getStatusAroundFullChunk(level - 33, null);
/*    */   }
/*    */   
/*    */   @Contract("_,!null->!null;_,_->_")
/*    */   public static ChunkStatus getStatusAroundFullChunk(int distanceToFullChunk, ChunkStatus defaultValue) {
/* 26 */     if (distanceToFullChunk > RADIUS_AROUND_FULL_CHUNK) {
/* 27 */       return defaultValue;
/*    */     }
/* 29 */     if (distanceToFullChunk <= 0) {
/* 30 */       return ChunkStatus.FULL;
/*    */     }
/* 32 */     return FULL_CHUNK_STEP.accumulatedDependencies().get(distanceToFullChunk);
/*    */   }
/*    */   
/*    */   public static ChunkStatus getStatusAroundFullChunk(int distanceToFullChunk) {
/* 36 */     return getStatusAroundFullChunk(distanceToFullChunk, ChunkStatus.EMPTY);
/*    */   }
/*    */   
/*    */   public static int byStatus(ChunkStatus status) {
/* 40 */     return 33 + FULL_CHUNK_STEP.getAccumulatedRadiusOf(status);
/*    */   }
/*    */   
/*    */   public static FullChunkStatus fullStatus(int level) {
/* 44 */     if (level <= 31)
/* 45 */       return FullChunkStatus.ENTITY_TICKING; 
/* 46 */     if (level <= 32)
/* 47 */       return FullChunkStatus.BLOCK_TICKING; 
/* 48 */     if (level <= 33) {
/* 49 */       return FullChunkStatus.FULL;
/*    */     }
/* 51 */     return FullChunkStatus.INACCESSIBLE;
/*    */   }
/*    */   
/*    */   public static int byStatus(FullChunkStatus status) {
/* 55 */     switch (status) { default: throw new MatchException(null, null);case INACCESSIBLE: case FULL: case BLOCK_TICKING: case ENTITY_TICKING: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 59 */       31;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isEntityTicking(int level) {
/* 64 */     return (level <= 31);
/*    */   }
/*    */   
/*    */   public static boolean isBlockTicking(int level) {
/* 68 */     return (level <= 32);
/*    */   }
/*    */   
/*    */   public static boolean isLoaded(int level) {
/* 72 */     return (level <= MAX_LEVEL);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ChunkLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */