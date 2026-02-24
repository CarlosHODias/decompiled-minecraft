/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.List;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ResourceLoadStateTracker
/*     */ {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private ReloadState reloadState;
/*     */   
/*     */   private int reloadCount;
/*     */   
/*     */   public void startReload(ReloadReason reloadReason, List<PackResources> packs) {
/*  23 */     this.reloadCount++;
/*  24 */     if (this.reloadState != null && !this.reloadState.finished) {
/*  25 */       LOGGER.warn("Reload already ongoing, replacing");
/*     */     }
/*  27 */     this.reloadState = new ReloadState(reloadReason, (List<String>)packs.stream().map(PackResources::packId).collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */   
/*     */   public void startRecovery(Throwable reason) {
/*  31 */     if (this.reloadState == null) {
/*  32 */       LOGGER.warn("Trying to signal reload recovery, but nothing was started");
/*  33 */       this.reloadState = new ReloadState(ReloadReason.UNKNOWN, (List<String>)ImmutableList.of());
/*     */     } 
/*     */     
/*  36 */     this.reloadState.recoveryReloadInfo = new RecoveryInfo(reason);
/*     */   }
/*     */   
/*     */   public void finishReload() {
/*  40 */     if (this.reloadState == null) {
/*  41 */       LOGGER.warn("Trying to finish reload, but nothing was started");
/*     */     } else {
/*  43 */       this.reloadState.finished = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fillCrashReport(CrashReport report) {
/*  48 */     CrashReportCategory category = report.addCategory("Last reload");
/*  49 */     category.setDetail("Reload number", this.reloadCount);
/*  50 */     if (this.reloadState != null)
/*  51 */       this.reloadState.fillCrashInfo(category); 
/*     */   }
/*     */   
/*     */   private static class RecoveryInfo
/*     */   {
/*     */     private final Throwable error;
/*     */     
/*     */     private RecoveryInfo(Throwable error) {
/*  59 */       this.error = error;
/*     */     }
/*     */     
/*     */     public void fillCrashInfo(CrashReportCategory category) {
/*  63 */       category.setDetail("Recovery", "Yes");
/*     */       
/*  65 */       category.setDetail("Recovery reason", () -> {
/*     */             StringWriter writer = new StringWriter();
/*     */             this.error.printStackTrace(new PrintWriter(writer));
/*     */             return writer.toString();
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReloadState
/*     */   {
/*     */     private final ResourceLoadStateTracker.ReloadReason reloadReason;
/*     */     private final List<String> packs;
/*     */     private ResourceLoadStateTracker.RecoveryInfo recoveryReloadInfo;
/*     */     private boolean finished;
/*     */     
/*     */     private ReloadState(ResourceLoadStateTracker.ReloadReason reloadReason, List<String> packs) {
/*  82 */       this.reloadReason = reloadReason;
/*  83 */       this.packs = packs;
/*     */     }
/*     */     
/*     */     public void fillCrashInfo(CrashReportCategory category) {
/*  87 */       category.setDetail("Reload reason", this.reloadReason.name);
/*  88 */       category.setDetail("Finished", this.finished ? "Yes" : "No");
/*  89 */       category.setDetail("Packs", () -> String.join(", ", (Iterable)this.packs));
/*     */       
/*  91 */       if (this.recoveryReloadInfo != null)
/*  92 */         this.recoveryReloadInfo.fillCrashInfo(category); 
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ReloadReason
/*     */   {
/*  98 */     INITIAL("initial"),
/*  99 */     MANUAL("manual"),
/* 100 */     UNKNOWN("unknown");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     ReloadReason(String name) {
/* 105 */       this.name = name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/ResourceLoadStateTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */