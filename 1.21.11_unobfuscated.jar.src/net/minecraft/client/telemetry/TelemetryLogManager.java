/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.file.Path;
/*    */ import java.time.Clock;
/*    */ import java.time.LocalDate;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.eventlog.EventLogDirectory;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class TelemetryLogManager
/*    */   implements AutoCloseable {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final String RAW_EXTENSION = ".json";
/*    */   
/*    */   private static final int EXPIRY_DAYS = 7;
/*    */   private final EventLogDirectory directory;
/*    */   private CompletableFuture<Optional<TelemetryEventLog>> sessionLog;
/*    */   
/*    */   private TelemetryLogManager(EventLogDirectory directory) {
/* 27 */     this.directory = directory;
/*    */   }
/*    */   
/*    */   public static CompletableFuture<Optional<TelemetryLogManager>> open(Path root) {
/* 31 */     return CompletableFuture.supplyAsync(() -> {
/*    */           try {
/*    */             EventLogDirectory directory = EventLogDirectory.open(root, ".json");
/*    */             
/*    */             directory.listFiles().prune(LocalDate.now(Clock.systemDefaultZone()), 7).compressAll();
/*    */             
/*    */             return Optional.of(new TelemetryLogManager(directory));
/* 38 */           } catch (Exception e) {
/*    */             LOGGER.error("Failed to create telemetry log manager", e);
/*    */             return Optional.empty();
/*    */           } 
/* 42 */         }, (Executor)Util.backgroundExecutor());
/*    */   }
/*    */   
/*    */   public CompletableFuture<Optional<TelemetryEventLogger>> openLogger() {
/* 46 */     if (this.sessionLog == null) {
/* 47 */       this.sessionLog = CompletableFuture.supplyAsync(() -> {
/*    */             try {
/*    */               EventLogDirectory.RawFile file = this.directory.createNewFile(LocalDate.now(Clock.systemDefaultZone()));
/*    */               FileChannel channel = file.openChannel();
/*    */               return Optional.of(new TelemetryEventLog(channel, (Executor)Util.backgroundExecutor()));
/* 52 */             } catch (IOException e) {
/*    */               LOGGER.error("Failed to open channel for telemetry event log", e);
/*    */               return Optional.empty();
/*    */             } 
/* 56 */           }, (Executor)Util.backgroundExecutor());
/*    */     }
/* 58 */     return this.sessionLog.thenApply(log -> log.map(TelemetryEventLog::logger));
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 63 */     if (this.sessionLog != null)
/* 64 */       this.sessionLog.thenAccept(log -> log.ifPresent(TelemetryEventLog::close)); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/TelemetryLogManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */