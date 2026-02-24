/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.eventlog.JsonEventLog;
/*    */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class TelemetryEventLog implements AutoCloseable {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final JsonEventLog<TelemetryEventInstance> log;
/*    */   private final ConsecutiveExecutor consecutiveExecutor;
/*    */   
/*    */   public TelemetryEventLog(FileChannel channel, Executor executor) {
/* 20 */     this.log = new JsonEventLog(TelemetryEventInstance.CODEC, channel);
/* 21 */     this.consecutiveExecutor = new ConsecutiveExecutor(executor, "telemetry-event-log");
/*    */   }
/*    */   
/*    */   public TelemetryEventLogger logger() {
/* 25 */     return event -> this.consecutiveExecutor.schedule(());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 36 */     this.consecutiveExecutor.schedule(() -> IOUtils.closeQuietly((Closeable)this.log));
/* 37 */     this.consecutiveExecutor.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/TelemetryEventLog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */