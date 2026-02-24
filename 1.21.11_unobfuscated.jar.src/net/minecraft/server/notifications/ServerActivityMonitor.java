/*    */ package net.minecraft.server.notifications;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class ServerActivityMonitor
/*    */ {
/*    */   private final long minimumMillisBetweenNotifications;
/* 11 */   private final AtomicLong lastNotificationTime = new AtomicLong();
/* 12 */   private final AtomicBoolean serverActivity = new AtomicBoolean(false);
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   public ServerActivityMonitor(NotificationManager notificationManager, int secondsBetweenNotifications) {
/* 16 */     this.notificationManager = notificationManager;
/* 17 */     this.minimumMillisBetweenNotifications = TimeUnit.SECONDS.toMillis(secondsBetweenNotifications);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 21 */     processWithRateLimit();
/*    */   }
/*    */   
/*    */   public void reportLoginActivity() {
/* 25 */     this.serverActivity.set(true);
/* 26 */     processWithRateLimit();
/*    */   }
/*    */   
/*    */   private void processWithRateLimit() {
/* 30 */     long now = Util.getMillis();
/* 31 */     if (this.serverActivity.get() && now - this.lastNotificationTime.get() >= this.minimumMillisBetweenNotifications) {
/* 32 */       this.notificationManager.serverActivityOccured();
/* 33 */       this.lastNotificationTime.set(Util.getMillis());
/*    */     } 
/* 35 */     this.serverActivity.set(false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/notifications/ServerActivityMonitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */