/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.lang.management.GarbageCollectorMXBean;
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryMemory
/*    */   implements DebugScreenEntry
/*    */ {
/* 15 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("memory");
/* 16 */   private final AllocationRateCalculator allocationRateCalculator = new AllocationRateCalculator();
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     long max = Runtime.getRuntime().maxMemory();
/* 21 */     long total = Runtime.getRuntime().totalMemory();
/* 22 */     long free = Runtime.getRuntime().freeMemory();
/* 23 */     long used = total - free;
/*    */     
/* 25 */     displayer.addToGroup(GROUP, List.of(
/* 26 */           String.format(Locale.ROOT, "Mem: %2d%% %03d/%03dMB", new Object[] { used * 100L / max, bytesToMegabytes(used), bytesToMegabytes(max)
/* 27 */             }), String.format(Locale.ROOT, "Allocation rate: %03dMB/s", new Object[] { bytesToMegabytes(this.allocationRateCalculator.bytesAllocatedPerSecond(used))
/* 28 */             }), String.format(Locale.ROOT, "Allocated: %2d%% %03dMB", new Object[] { total * 100L / max, bytesToMegabytes(total) })));
/*    */   }
/*    */ 
/*    */   
/*    */   private static long bytesToMegabytes(long used) {
/* 33 */     return used / 1024L / 1024L;
/*    */   }
/*    */   
/*    */   private static class AllocationRateCalculator {
/*    */     private static final int UPDATE_INTERVAL_MS = 500;
/* 38 */     private static final List<GarbageCollectorMXBean> GC_MBEANS = ManagementFactory.getGarbageCollectorMXBeans();
/*    */     
/* 40 */     private long lastTime = 0L;
/* 41 */     private long lastHeapUsage = -1L;
/* 42 */     private long lastGcCounts = -1L;
/* 43 */     private long lastRate = 0L;
/*    */     
/*    */     private long bytesAllocatedPerSecond(long currentHeapUsage) {
/* 46 */       long time = System.currentTimeMillis();
/*    */       
/* 48 */       if (time - this.lastTime < 500L) {
/* 49 */         return this.lastRate;
/*    */       }
/*    */       
/* 52 */       long gcCounts = gcCounts();
/*    */ 
/*    */ 
/*    */       
/* 56 */       if (this.lastTime != 0L && gcCounts == this.lastGcCounts) {
/* 57 */         double multiplier = TimeUnit.SECONDS.toMillis(1L) / (time - this.lastTime);
/* 58 */         long delta = currentHeapUsage - this.lastHeapUsage;
/* 59 */         this.lastRate = Math.round(delta * multiplier);
/*    */       } 
/*    */       
/* 62 */       this.lastTime = time;
/* 63 */       this.lastHeapUsage = currentHeapUsage;
/* 64 */       this.lastGcCounts = gcCounts;
/* 65 */       return this.lastRate;
/*    */     }
/*    */     
/*    */     private static long gcCounts() {
/* 69 */       long total = 0L;
/* 70 */       for (GarbageCollectorMXBean gcBean : GC_MBEANS) {
/* 71 */         total += gcBean.getCollectionCount();
/*    */       }
/* 73 */       return total;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 79 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryMemory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */