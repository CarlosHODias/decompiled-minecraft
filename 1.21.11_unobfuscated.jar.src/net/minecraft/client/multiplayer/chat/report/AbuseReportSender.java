/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*    */ import com.mojang.authlib.exceptions.MinecraftClientHttpException;
/*    */ import com.mojang.authlib.minecraft.UserApiService;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ThrowingComponent;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface AbuseReportSender
/*    */ {
/*    */   static AbuseReportSender create(ReportEnvironment environment, UserApiService userApiService) {
/* 21 */     return new Services(environment, userApiService);
/*    */   }
/*    */   
/*    */   CompletableFuture<Unit> send(UUID paramUUID, ReportType paramReportType, AbuseReport paramAbuseReport);
/*    */   
/*    */   boolean isEnabled();
/*    */   
/*    */   default AbuseReportLimits reportLimits() {
/* 29 */     return AbuseReportLimits.DEFAULTS;
/*    */   }
/*    */   public static final class Services extends Record implements AbuseReportSender { private final ReportEnvironment environment; private final UserApiService userApiService;
/* 32 */     public Services(ReportEnvironment environment, UserApiService userApiService) { this.environment = environment; this.userApiService = userApiService; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 32 */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services; } public ReportEnvironment environment() { return this.environment; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;
/* 32 */       //   0	8	1	o	Ljava/lang/Object; } public UserApiService userApiService() { return this.userApiService; }
/* 33 */      private static final Component SERVICE_UNAVAILABLE_TEXT = (Component)Component.translatable("gui.abuseReport.send.service_unavailable");
/* 34 */     private static final Component HTTP_ERROR_TEXT = (Component)Component.translatable("gui.abuseReport.send.http_error");
/* 35 */     private static final Component JSON_ERROR_TEXT = (Component)Component.translatable("gui.abuseReport.send.json_error");
/*    */ 
/*    */     
/*    */     public CompletableFuture<Unit> send(UUID id, ReportType reportType, AbuseReport report) {
/* 39 */       return CompletableFuture.supplyAsync(() -> {
/*    */             AbuseReportRequest request = new AbuseReportRequest(1, id, report, this.environment.clientInfo(), this.environment.thirdPartyServerInfo(), this.environment.realmInfo(), reportType.backendName());
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             try {
/*    */               this.userApiService.reportAbuse(request);
/*    */ 
/*    */ 
/*    */ 
/*    */               
/*    */               return Unit.INSTANCE;
/* 52 */             } catch (MinecraftClientHttpException e) {
/*    */               Component description = getHttpErrorDescription(e);
/*    */               throw new CompletionException(new AbuseReportSender.SendException(description, e));
/* 55 */             } catch (MinecraftClientException e) {
/*    */               Component description = getErrorDescription(e);
/*    */               throw new CompletionException(new AbuseReportSender.SendException(description, e));
/*    */             } 
/* 59 */           }, (Executor)Util.ioPool());
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isEnabled() {
/* 64 */       return this.userApiService.canSendReports();
/*    */     }
/*    */     
/*    */     private Component getHttpErrorDescription(MinecraftClientHttpException e) {
/* 68 */       return (Component)Component.translatable("gui.abuseReport.send.error_message", new Object[] { e.getMessage() });
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private Component getErrorDescription(MinecraftClientException e) {
/*    */       // Byte code:
/*    */       //   0: getstatic net/minecraft/client/multiplayer/chat/report/AbuseReportSender$1.$SwitchMap$com$mojang$authlib$exceptions$MinecraftClientException$ErrorType : [I
/*    */       //   3: aload_1
/*    */       //   4: invokevirtual getType : ()Lcom/mojang/authlib/exceptions/MinecraftClientException$ErrorType;
/*    */       //   7: invokevirtual ordinal : ()I
/*    */       //   10: iaload
/*    */       //   11: tableswitch default -> 36, 1 -> 46, 2 -> 52, 3 -> 58
/*    */       //   36: new java/lang/MatchException
/*    */       //   39: dup
/*    */       //   40: aconst_null
/*    */       //   41: aconst_null
/*    */       //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*    */       //   45: athrow
/*    */       //   46: getstatic net/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services.SERVICE_UNAVAILABLE_TEXT : Lnet/minecraft/network/chat/Component;
/*    */       //   49: goto -> 61
/*    */       //   52: getstatic net/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services.HTTP_ERROR_TEXT : Lnet/minecraft/network/chat/Component;
/*    */       //   55: goto -> 61
/*    */       //   58: getstatic net/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services.JSON_ERROR_TEXT : Lnet/minecraft/network/chat/Component;
/*    */       //   61: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       //   #73	-> 46
/*    */       //   #74	-> 52
/*    */       //   #75	-> 58
/*    */       //   #72	-> 61
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	62	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;
/*    */       //   0	62	1	e	Lcom/mojang/authlib/exceptions/MinecraftClientException;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public AbuseReportLimits reportLimits() {
/* 81 */       return this.userApiService.getAbuseReportLimits();
/*    */     } }
/*    */ 
/*    */   
/*    */   public static class SendException extends ThrowingComponent {
/*    */     public SendException(Component component, Throwable cause) {
/* 87 */       super(component, cause);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/AbuseReportSender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */