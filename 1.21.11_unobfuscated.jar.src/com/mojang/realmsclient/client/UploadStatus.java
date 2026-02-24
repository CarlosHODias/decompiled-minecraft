/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ public class UploadStatus
/*    */ {
/*    */   private volatile long bytesWritten;
/*    */   private volatile long totalBytes;
/* 10 */   private long previousTimeSnapshot = Util.getMillis();
/*    */   private long previousBytesWritten;
/*    */   private long bytesPerSecond;
/*    */   
/*    */   public void setTotalBytes(long totalBytes) {
/* 15 */     this.totalBytes = totalBytes;
/*    */   }
/*    */   
/*    */   public void restart() {
/* 19 */     this.bytesWritten = 0L;
/* 20 */     this.previousTimeSnapshot = Util.getMillis();
/* 21 */     this.previousBytesWritten = 0L;
/* 22 */     this.bytesPerSecond = 0L;
/*    */   }
/*    */   
/*    */   public long getTotalBytes() {
/* 26 */     return this.totalBytes;
/*    */   }
/*    */   
/*    */   public long getBytesWritten() {
/* 30 */     return this.bytesWritten;
/*    */   }
/*    */   
/*    */   public void onWrite(long bytesWritten) {
/* 34 */     this.bytesWritten = bytesWritten;
/*    */   }
/*    */   
/*    */   public boolean uploadStarted() {
/* 38 */     return (this.bytesWritten > 0L);
/*    */   }
/*    */   
/*    */   public boolean uploadCompleted() {
/* 42 */     return (this.bytesWritten >= this.totalBytes);
/*    */   }
/*    */   
/*    */   public double getPercentage() {
/* 46 */     return Math.min(getBytesWritten() / getTotalBytes(), 1.0D);
/*    */   }
/*    */   
/*    */   public void refreshBytesPerSecond() {
/* 50 */     long currentMillis = Util.getMillis();
/* 51 */     long timeElapsed = currentMillis - this.previousTimeSnapshot;
/* 52 */     if (timeElapsed < 1000L) {
/*    */       return;
/*    */     }
/* 55 */     long bytesWritten = this.bytesWritten;
/* 56 */     this.bytesPerSecond = 1000L * (bytesWritten - this.previousBytesWritten) / timeElapsed;
/*    */     
/* 58 */     this.previousBytesWritten = bytesWritten;
/* 59 */     this.previousTimeSnapshot = currentMillis;
/*    */   }
/*    */   
/*    */   public long getBytesPerSecond() {
/* 63 */     return this.bytesPerSecond;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/UploadStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */