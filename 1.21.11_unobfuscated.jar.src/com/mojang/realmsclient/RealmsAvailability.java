/*    */ package com.mojang.realmsclient;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RealmsAvailability
/*    */ {
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static CompletableFuture<Result> future;
/*    */   
/*    */   public static CompletableFuture<Result> get() {
/* 27 */     if (future == null || shouldRefresh(future)) {
/* 28 */       future = check();
/*    */     }
/* 30 */     return future;
/*    */   }
/*    */   
/*    */   private static boolean shouldRefresh(CompletableFuture<Result> future) {
/* 34 */     Result result = future.getNow(null);
/* 35 */     return (result != null && result.exception() != null);
/*    */   }
/*    */   
/*    */   private static CompletableFuture<Result> check() {
/* 39 */     if (Minecraft.getInstance().isOfflineDeveloperMode()) {
/* 40 */       return CompletableFuture.completedFuture(new Result(Type.AUTHENTICATION_ERROR));
/*    */     }
/* 42 */     if (SharedConstants.DEBUG_BYPASS_REALMS_VERSION_CHECK) {
/* 43 */       return CompletableFuture.completedFuture(new Result(Type.SUCCESS));
/*    */     }
/* 45 */     return CompletableFuture.supplyAsync(() -> {
/*    */           RealmsClient client = RealmsClient.getOrCreate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           try {
/*    */             return (client.clientCompatible() != RealmsClient.CompatibleVersionResponse.COMPATIBLE) ? new Result(Type.INCOMPATIBLE_CLIENT) : (!client.hasParentalConsent() ? new Result(Type.NEEDS_PARENTAL_CONSENT) : new Result(Type.SUCCESS));
/* 55 */           } catch (RealmsServiceException e) {
/*    */             LOGGER.error("Couldn't connect to realms", (Throwable)e);
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             return (e.realmsError.errorCode() == 401) ? new Result(Type.AUTHENTICATION_ERROR) : new Result(e);
/*    */           } 
/* 63 */         }, (Executor)Util.ioPool());
/*    */   }
/*    */   public static final class Result extends Record { private final RealmsAvailability.Type type; private final RealmsServiceException exception;
/* 66 */     public Result(RealmsAvailability.Type type, RealmsServiceException exception) { this.type = type; this.exception = exception; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/RealmsAvailability$Result;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 66 */       //   0	7	0	this	Lcom/mojang/realmsclient/RealmsAvailability$Result; } public RealmsAvailability.Type type() { return this.type; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/RealmsAvailability$Result;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/RealmsAvailability$Result; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/RealmsAvailability$Result;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/realmsclient/RealmsAvailability$Result;
/* 66 */       //   0	8	1	o	Ljava/lang/Object; } public RealmsServiceException exception() { return this.exception; }
/*    */      public Result(RealmsAvailability.Type type) {
/* 68 */       this(type, null);
/*    */     }
/*    */     
/*    */     public Result(RealmsServiceException exception) {
/* 72 */       this(RealmsAvailability.Type.UNEXPECTED_ERROR, exception);
/*    */     }
/*    */     
/*    */     public Screen createErrorScreen(Screen lastScreen) {
/* 76 */       switch (this.type.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: case 4: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 81 */         (Screen)new RealmsGenericErrorScreen(Objects.<RealmsServiceException>requireNonNull(this.exception), lastScreen);
/*    */     } }
/*    */ 
/*    */   
/*    */   public enum Type
/*    */   {
/* 87 */     SUCCESS,
/* 88 */     INCOMPATIBLE_CLIENT,
/* 89 */     NEEDS_PARENTAL_CONSENT,
/* 90 */     AUTHENTICATION_ERROR,
/* 91 */     UNEXPECTED_ERROR;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/RealmsAvailability.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */