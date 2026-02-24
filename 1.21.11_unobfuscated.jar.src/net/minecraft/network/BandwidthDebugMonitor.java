/*    */ package net.minecraft.network;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*    */ 
/*    */ public class BandwidthDebugMonitor
/*    */ {
/*  8 */   private final AtomicInteger bytesReceived = new AtomicInteger();
/*    */   private final LocalSampleLogger bandwidthLogger;
/*    */   
/*    */   public BandwidthDebugMonitor(LocalSampleLogger bandwidthLogger) {
/* 12 */     this.bandwidthLogger = bandwidthLogger;
/*    */   }
/*    */   
/*    */   public void onReceive(int bytes) {
/* 16 */     this.bytesReceived.getAndAdd(bytes);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 20 */     this.bandwidthLogger.logSample(this.bytesReceived.getAndSet(0));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/BandwidthDebugMonitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */