/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class ChunkBatchSizeCalculator {
/*    */   private static final int MAX_OLD_SAMPLES_WEIGHT = 49;
/*    */   private static final int CLAMP_COEFFICIENT = 3;
/*  9 */   private double aggregatedNanosPerChunk = 2000000.0D;
/* 10 */   private int oldSamplesWeight = 1;
/* 11 */   private volatile long chunkBatchStartTime = Util.getNanos();
/*    */   
/*    */   public void onBatchStart() {
/* 14 */     this.chunkBatchStartTime = Util.getNanos();
/*    */   }
/*    */   
/*    */   public void onBatchFinished(int batchSize) {
/* 18 */     if (batchSize > 0) {
/*    */ 
/*    */ 
/*    */       
/* 22 */       double batchDuration = (Util.getNanos() - this.chunkBatchStartTime);
/* 23 */       double nanosPerChunk = batchDuration / batchSize;
/* 24 */       double clampedNanosPerChunk = Mth.clamp(nanosPerChunk, this.aggregatedNanosPerChunk / 3.0D, this.aggregatedNanosPerChunk * 3.0D);
/* 25 */       this.aggregatedNanosPerChunk = (this.aggregatedNanosPerChunk * this.oldSamplesWeight + clampedNanosPerChunk) / (this.oldSamplesWeight + 1);
/* 26 */       this.oldSamplesWeight = Math.min(49, this.oldSamplesWeight + 1);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getDesiredChunksPerTick() {
/* 33 */     return (float)(7000000.0D / this.aggregatedNanosPerChunk);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ChunkBatchSizeCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */