/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.time.Duration;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.server.dedicated.ServerWatchdog;
/*    */ 
/*    */ 
/*    */ public class ClientShutdownWatchdog
/*    */ {
/* 12 */   private static final Duration CRASH_REPORT_PRELOAD_LOAD = Duration.ofSeconds(15L);
/*    */   
/*    */   public static void startShutdownWatchdog(File gameDirectory, long mainThreadId) {
/* 15 */     Thread thread = new Thread(() -> {
/*    */           try {
/*    */             Thread.sleep(CRASH_REPORT_PRELOAD_LOAD);
/* 18 */           } catch (InterruptedException e) {
/*    */             return;
/*    */           } 
/*    */ 
/*    */           
/*    */           CrashReport report = ServerWatchdog.createWatchdogCrashReport("Client shutdown", mainThreadId);
/*    */ 
/*    */           
/*    */           Minecraft.saveReport(gameDirectory, report);
/*    */         });
/*    */     
/* 29 */     thread.setDaemon(true);
/* 30 */     thread.setName("Client shutdown watchdog");
/* 31 */     thread.start();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/ClientShutdownWatchdog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */