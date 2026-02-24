/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.minecraft.report.ReportedEntity;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.time.Instant;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.reporting.NameReportScreen;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class NameReport
/*    */   extends Report
/*    */ {
/*    */   private final String reportedName;
/*    */   
/*    */   private NameReport(UUID reportId, Instant createdAt, UUID reportedProfileId, String reportedName) {
/* 19 */     super(reportId, createdAt, reportedProfileId);
/* 20 */     this.reportedName = reportedName;
/*    */   }
/*    */   
/*    */   public String getReportedName() {
/* 24 */     return this.reportedName;
/*    */   }
/*    */ 
/*    */   
/*    */   public NameReport copy() {
/* 29 */     NameReport result = new NameReport(this.reportId, this.createdAt, this.reportedProfileId, this.reportedName);
/* 30 */     result.comments = this.comments;
/* 31 */     result.attested = this.attested;
/* 32 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public Screen createScreen(Screen lastScreen, ReportingContext context) {
/* 37 */     return (Screen)new NameReportScreen(lastScreen, context, this);
/*    */   }
/*    */   
/*    */   public static class Builder extends Report.Builder<NameReport> {
/*    */     public Builder(NameReport report, AbuseReportLimits limits) {
/* 42 */       super(report, limits);
/*    */     }
/*    */     
/*    */     public Builder(UUID reportedProfileId, String reportedName, AbuseReportLimits limits) {
/* 46 */       super(new NameReport(UUID.randomUUID(), Instant.now(), reportedProfileId, reportedName), limits);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean hasContent() {
/* 51 */       return StringUtils.isNotEmpty(comments());
/*    */     }
/*    */ 
/*    */     
/*    */     public Report.CannotBuildReason checkBuildable() {
/* 56 */       if (this.report.comments.length() > this.limits.maxOpinionCommentsLength()) {
/* 57 */         return Report.CannotBuildReason.COMMENT_TOO_LONG;
/*    */       }
/* 59 */       return super.checkBuildable();
/*    */     }
/*    */ 
/*    */     
/*    */     public Either<Report.Result, Report.CannotBuildReason> build(ReportingContext reportingContext) {
/* 64 */       Report.CannotBuildReason error = checkBuildable();
/* 65 */       if (error != null) {
/* 66 */         return Either.right(error);
/*    */       }
/*    */       
/* 69 */       ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
/*    */       
/* 71 */       AbuseReport abuseReport = AbuseReport.name(this.report.comments, reportedEntity, this.report.createdAt);
/* 72 */       return Either.left(new Report.Result(this.report.reportId, ReportType.USERNAME, abuseReport));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/NameReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */