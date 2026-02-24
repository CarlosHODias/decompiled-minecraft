/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.minecraft.report.ReportedEntity;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.time.Instant;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.reporting.SkinReportScreen;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class SkinReport
/*    */   extends Report
/*    */ {
/*    */   private final Supplier<PlayerSkin> skinGetter;
/*    */   
/*    */   private SkinReport(UUID reportId, Instant createdAt, UUID reportedProfileId, Supplier<PlayerSkin> skinGetter) {
/* 23 */     super(reportId, createdAt, reportedProfileId);
/* 24 */     this.skinGetter = skinGetter;
/*    */   }
/*    */   
/*    */   public Supplier<PlayerSkin> getSkinGetter() {
/* 28 */     return this.skinGetter;
/*    */   }
/*    */ 
/*    */   
/*    */   public SkinReport copy() {
/* 33 */     SkinReport result = new SkinReport(this.reportId, this.createdAt, this.reportedProfileId, this.skinGetter);
/* 34 */     result.comments = this.comments;
/* 35 */     result.reason = this.reason;
/* 36 */     result.attested = this.attested;
/* 37 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public Screen createScreen(Screen lastScreen, ReportingContext context) {
/* 42 */     return (Screen)new SkinReportScreen(lastScreen, context, this);
/*    */   }
/*    */   
/*    */   public static class Builder extends Report.Builder<SkinReport> {
/*    */     public Builder(SkinReport report, AbuseReportLimits limits) {
/* 47 */       super(report, limits);
/*    */     }
/*    */     
/*    */     public Builder(UUID reportedProfileId, Supplier<PlayerSkin> skin, AbuseReportLimits limits) {
/* 51 */       super(new SkinReport(UUID.randomUUID(), Instant.now(), reportedProfileId, skin), limits);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean hasContent() {
/* 56 */       return (StringUtils.isNotEmpty(comments()) || reason() != null);
/*    */     }
/*    */ 
/*    */     
/*    */     public Report.CannotBuildReason checkBuildable() {
/* 61 */       if (this.report.reason == null) {
/* 62 */         return Report.CannotBuildReason.NO_REASON;
/*    */       }
/* 64 */       if (this.report.comments.length() > this.limits.maxOpinionCommentsLength()) {
/* 65 */         return Report.CannotBuildReason.COMMENT_TOO_LONG;
/*    */       }
/* 67 */       return super.checkBuildable();
/*    */     }
/*    */ 
/*    */     
/*    */     public Either<Report.Result, Report.CannotBuildReason> build(ReportingContext reportingContext) {
/* 72 */       Report.CannotBuildReason error = checkBuildable();
/* 73 */       if (error != null) {
/* 74 */         return Either.right(error);
/*    */       }
/*    */       
/* 77 */       String reason = ((ReportReason)Objects.<ReportReason>requireNonNull(this.report.reason)).backendName();
/*    */       
/* 79 */       ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
/*    */ 
/*    */       
/* 82 */       PlayerSkin skin = this.report.skinGetter.get();
/* 83 */       ClientAsset.Texture texture = skin.body(); ClientAsset.DownloadedTexture downloadedTexture = (ClientAsset.DownloadedTexture)texture; String skinUrl = (texture instanceof ClientAsset.DownloadedTexture) ? downloadedTexture.url() : null;
/*    */       
/* 85 */       AbuseReport abuseReport = AbuseReport.skin(this.report.comments, reason, skinUrl, reportedEntity, this.report.createdAt);
/* 86 */       return Either.left(new Report.Result(this.report.reportId, ReportType.SKIN, abuseReport));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/SkinReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */