/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class BlockDestructionProgress implements Comparable<BlockDestructionProgress> {
/*    */   private final int id;
/*    */   private final BlockPos pos;
/*    */   private int progress;
/*    */   private int updatedRenderTick;
/*    */   
/*    */   public BlockDestructionProgress(int id, BlockPos pos) {
/* 12 */     this.id = id;
/* 13 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 17 */     return this.id;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 21 */     return this.pos;
/*    */   }
/*    */   
/*    */   public void setProgress(int progress) {
/* 25 */     if (progress > 10) {
/* 26 */       progress = 10;
/*    */     }
/* 28 */     this.progress = progress;
/*    */   }
/*    */   
/*    */   public int getProgress() {
/* 32 */     return this.progress;
/*    */   }
/*    */   
/*    */   public void updateTick(int tick) {
/* 36 */     this.updatedRenderTick = tick;
/*    */   }
/*    */   
/*    */   public int getUpdatedRenderTick() {
/* 40 */     return this.updatedRenderTick;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 45 */     if (this == o) {
/* 46 */       return true;
/*    */     }
/* 48 */     if (o == null || getClass() != o.getClass()) {
/* 49 */       return false;
/*    */     }
/* 51 */     BlockDestructionProgress that = (BlockDestructionProgress)o;
/* 52 */     return (this.id == that.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return Integer.hashCode(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(BlockDestructionProgress o) {
/* 62 */     if (this.progress != o.progress) {
/* 63 */       return Integer.compare(this.progress, o.progress);
/*    */     }
/* 65 */     return Integer.compare(this.id, o.id);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/BlockDestructionProgress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */