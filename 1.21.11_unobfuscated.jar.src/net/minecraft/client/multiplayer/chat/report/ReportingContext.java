/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.UserApiService;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.multiplayer.chat.ChatLog;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ReportingContext
/*    */ {
/*    */   private static final int LOG_CAPACITY = 1024;
/*    */   private final AbuseReportSender sender;
/*    */   private final ReportEnvironment environment;
/*    */   private final ChatLog chatLog;
/*    */   private Report draftReport;
/*    */   
/*    */   public ReportingContext(AbuseReportSender sender, ReportEnvironment environment, ChatLog chatLog) {
/* 23 */     this.sender = sender;
/* 24 */     this.environment = environment;
/* 25 */     this.chatLog = chatLog;
/*    */   }
/*    */   
/*    */   public static ReportingContext create(ReportEnvironment environment, UserApiService userApiService) {
/* 29 */     ChatLog chatLog = new ChatLog(1024);
/* 30 */     AbuseReportSender sender = AbuseReportSender.create(environment, userApiService);
/* 31 */     return new ReportingContext(sender, environment, chatLog);
/*    */   }
/*    */   
/*    */   public void draftReportHandled(Minecraft minecraft, Screen lastScreen, Runnable onDiscard, boolean quitToTitle) {
/* 35 */     if (this.draftReport != null) {
/* 36 */       Report report = this.draftReport.copy();
/* 37 */       minecraft.setScreen((Screen)new ConfirmScreen(response -> {
/*    */               setReportDraft(null);
/*    */               
/*    */               if (onDiscard) {
/*    */                 minecraft.setScreen(minecraft.createScreen(report, this));
/*    */               } else {
/*    */                 lastScreen.run();
/*    */               } 
/* 45 */             }, (Component)Component.translatable(quitToTitle ? "gui.abuseReport.draft.quittotitle.title" : "gui.abuseReport.draft.title"), 
/* 46 */             (Component)Component.translatable(quitToTitle ? "gui.abuseReport.draft.quittotitle.content" : "gui.abuseReport.draft.content"), 
/* 47 */             (Component)Component.translatable("gui.abuseReport.draft.edit"), 
/* 48 */             (Component)Component.translatable("gui.abuseReport.draft.discard")));
/*    */     } else {
/*    */       
/* 51 */       onDiscard.run();
/*    */     } 
/*    */   }
/*    */   
/*    */   public AbuseReportSender sender() {
/* 56 */     return this.sender;
/*    */   }
/*    */   
/*    */   public ChatLog chatLog() {
/* 60 */     return this.chatLog;
/*    */   }
/*    */   
/*    */   public boolean matches(ReportEnvironment environment) {
/* 64 */     return Objects.equals(this.environment, environment);
/*    */   }
/*    */   
/*    */   public void setReportDraft(Report draftReport) {
/* 68 */     this.draftReport = draftReport;
/*    */   }
/*    */   
/*    */   public boolean hasDraftReport() {
/* 72 */     return (this.draftReport != null);
/*    */   }
/*    */   
/*    */   public boolean hasDraftReportFor(UUID playerId) {
/* 76 */     return (hasDraftReport() && this.draftReport.isReportedPlayer(playerId));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/ReportingContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */