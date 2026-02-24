/*     */ package net.minecraft.client.multiplayer.chat.report;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*     */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.time.Instant;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Report
/*     */ {
/*     */   protected final UUID reportId;
/*     */   protected final Instant createdAt;
/*     */   protected final UUID reportedProfileId;
/*  19 */   protected String comments = "";
/*     */   protected ReportReason reason;
/*     */   protected boolean attested;
/*     */   
/*     */   public Report(UUID reportId, Instant createdAt, UUID reportedProfileId) {
/*  24 */     this.reportId = reportId;
/*  25 */     this.createdAt = createdAt;
/*  26 */     this.reportedProfileId = reportedProfileId;
/*     */   }
/*     */   
/*     */   public boolean isReportedPlayer(UUID playerId) {
/*  30 */     return playerId.equals(this.reportedProfileId);
/*     */   }
/*     */   
/*     */   public abstract Report copy();
/*     */   
/*     */   public abstract Screen createScreen(Screen paramScreen, ReportingContext paramReportingContext);
/*     */   
/*     */   public static abstract class Builder<R extends Report> {
/*     */     protected final R report;
/*     */     protected final AbuseReportLimits limits;
/*     */     
/*     */     protected Builder(R report, AbuseReportLimits limits) {
/*  42 */       this.report = report;
/*  43 */       this.limits = limits;
/*     */     }
/*     */     
/*     */     public R report() {
/*  47 */       return this.report;
/*     */     }
/*     */     
/*     */     public UUID reportedProfileId() {
/*  51 */       return ((Report)this.report).reportedProfileId;
/*     */     }
/*     */     
/*     */     public String comments() {
/*  55 */       return ((Report)this.report).comments;
/*     */     }
/*     */     
/*     */     public boolean attested() {
/*  59 */       return ((Report)report()).attested;
/*     */     }
/*     */     
/*     */     public void setComments(String comments) {
/*  63 */       ((Report)this.report).comments = comments;
/*     */     }
/*     */     
/*     */     public ReportReason reason() {
/*  67 */       return ((Report)this.report).reason;
/*     */     }
/*     */     
/*     */     public void setReason(ReportReason reason) {
/*  71 */       ((Report)this.report).reason = reason;
/*     */     }
/*     */     
/*     */     public void setAttested(boolean attested) {
/*  75 */       ((Report)this.report).attested = attested;
/*     */     }
/*     */     
/*     */     public abstract boolean hasContent();
/*     */     
/*     */     public Report.CannotBuildReason checkBuildable() {
/*  81 */       if (!((Report)report()).attested) {
/*  82 */         return Report.CannotBuildReason.NOT_ATTESTED;
/*     */       }
/*  84 */       return null;
/*     */     }
/*     */     public abstract Either<Report.Result, Report.CannotBuildReason> build(ReportingContext param1ReportingContext); }
/*     */   public static final class Result extends Record { private final UUID id; private final ReportType reportType;
/*     */     private final AbuseReport report;
/*     */     
/*  90 */     public Result(UUID id, ReportType reportType, AbuseReport report) { this.id = id; this.reportType = reportType; this.report = report; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/Report$Result;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  90 */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$Result; } public UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/Report$Result;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$Result; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/Report$Result;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$Result;
/*  90 */       //   0	8	1	o	Ljava/lang/Object; } public ReportType reportType() { return this.reportType; } public AbuseReport report() { return this.report; }
/*     */      }
/*     */   public static final class CannotBuildReason extends Record { private final Component message;
/*  93 */     public CannotBuildReason(Component message) { this.message = message; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;
/*  93 */       //   0	8	1	o	Ljava/lang/Object; } public Component message() { return this.message; }
/*  94 */      public static final CannotBuildReason NO_REASON = new CannotBuildReason((Component)Component.translatable("gui.abuseReport.send.no_reason"));
/*  95 */     public static final CannotBuildReason NO_REPORTED_MESSAGES = new CannotBuildReason((Component)Component.translatable("gui.chatReport.send.no_reported_messages"));
/*  96 */     public static final CannotBuildReason TOO_MANY_MESSAGES = new CannotBuildReason((Component)Component.translatable("gui.chatReport.send.too_many_messages"));
/*  97 */     public static final CannotBuildReason COMMENT_TOO_LONG = new CannotBuildReason((Component)Component.translatable("gui.abuseReport.send.comment_too_long"));
/*  98 */     public static final CannotBuildReason NOT_ATTESTED = new CannotBuildReason((Component)Component.translatable("gui.abuseReport.send.not_attested"));
/*     */     
/*     */     public Tooltip tooltip() {
/* 101 */       return Tooltip.create(this.message);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/Report.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */