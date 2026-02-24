/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmCreationTask extends LongRunningTask {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 12 */   private static final Component TITLE = (Component)Component.translatable("mco.create.world.wait");
/*    */   
/*    */   private final String name;
/*    */   private final String motd;
/*    */   private final long realmId;
/*    */   
/*    */   public RealmCreationTask(long realmId, String name, String motd) {
/* 19 */     this.realmId = realmId;
/* 20 */     this.name = name;
/* 21 */     this.motd = motd;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 26 */     RealmsClient client = RealmsClient.getOrCreate();
/*    */     
/*    */     try {
/* 29 */       client.initializeRealm(this.realmId, this.name, this.motd);
/* 30 */     } catch (RealmsServiceException e) {
/* 31 */       LOGGER.error("Couldn't create world", (Throwable)e);
/* 32 */       error(e);
/* 33 */     } catch (Exception e) {
/* 34 */       LOGGER.error("Could not create world", e);
/* 35 */       error(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 41 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/RealmCreationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */