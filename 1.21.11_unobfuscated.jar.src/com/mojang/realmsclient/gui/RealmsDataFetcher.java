/*    */ package com.mojang.realmsclient.gui;
/*    */ import com.mojang.realmsclient.RealmsMainScreen;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.RealmsNews;
/*    */ import com.mojang.realmsclient.dto.RealmsNotification;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
/*    */ import com.mojang.realmsclient.gui.task.DataFetcher;
/*    */ import com.mojang.realmsclient.gui.task.RepeatedDelayStrategy;
/*    */ import com.mojang.realmsclient.util.RealmsPersistence;
/*    */ import java.time.Duration;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.util.TimeSource;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class RealmsDataFetcher {
/* 20 */   public final DataFetcher dataFetcher = new DataFetcher((Executor)Util.ioPool(), TimeUnit.MILLISECONDS, (TimeSource)Util.timeSource);
/*    */   
/*    */   private final List<DataFetcher.Task<?>> tasks;
/*    */   
/*    */   public final DataFetcher.Task<List<RealmsNotification>> notificationsTask;
/*    */   
/*    */   public final DataFetcher.Task<ServerListData> serverListUpdateTask;
/*    */   
/*    */   public final DataFetcher.Task<Integer> pendingInvitesTask;
/*    */   public final DataFetcher.Task<Boolean> trialAvailabilityTask;
/*    */   public final DataFetcher.Task<RealmsNews> newsTask;
/*    */   public final DataFetcher.Task<RealmsServerPlayerLists> onlinePlayersTask;
/* 32 */   public final RealmsNewsManager newsManager = new RealmsNewsManager(new RealmsPersistence());
/*    */   
/*    */   public RealmsDataFetcher(RealmsClient realmsClient) {
/* 35 */     this.serverListUpdateTask = this.dataFetcher.createTask("server list", () -> {
/*    */           com.mojang.realmsclient.dto.RealmsServerList realmsServerList = realmsClient.listRealms();
/*    */ 
/*    */ 
/*    */           
/*    */           return RealmsMainScreen.isSnapshot() ? new ServerListData(realmsServerList.servers(), realmsClient.listSnapshotEligibleRealms()) : new ServerListData(realmsServerList.servers(), List.of());
/* 41 */         }, Duration.ofSeconds(60L), RepeatedDelayStrategy.CONSTANT);
/* 42 */     Objects.requireNonNull(realmsClient); this.pendingInvitesTask = this.dataFetcher.createTask("pending invite count", realmsClient::pendingInvitesCount, Duration.ofSeconds(10L), RepeatedDelayStrategy.exponentialBackoff(360));
/* 43 */     Objects.requireNonNull(realmsClient); this.trialAvailabilityTask = this.dataFetcher.createTask("trial availablity", realmsClient::trialAvailable, Duration.ofSeconds(60L), RepeatedDelayStrategy.exponentialBackoff(60));
/* 44 */     Objects.requireNonNull(realmsClient); this.newsTask = this.dataFetcher.createTask("unread news", realmsClient::getNews, Duration.ofMinutes(5L), RepeatedDelayStrategy.CONSTANT);
/* 45 */     Objects.requireNonNull(realmsClient); this.notificationsTask = this.dataFetcher.createTask("notifications", realmsClient::getNotifications, Duration.ofMinutes(5L), RepeatedDelayStrategy.CONSTANT);
/* 46 */     Objects.requireNonNull(realmsClient); this.onlinePlayersTask = this.dataFetcher.createTask("online players", realmsClient::getLiveStats, Duration.ofSeconds(10L), RepeatedDelayStrategy.CONSTANT);
/* 47 */     this.tasks = List.of(this.notificationsTask, this.serverListUpdateTask, this.pendingInvitesTask, this.trialAvailabilityTask, this.newsTask, this.onlinePlayersTask);
/*    */   }
/*    */   
/*    */   public List<DataFetcher.Task<?>> getTasks() {
/* 51 */     return this.tasks;
/*    */   }
/*    */   public static final class ServerListData extends Record { private final List<RealmsServer> serverList; private final List<RealmsServer> availableSnapshotServers;
/* 54 */     public ServerListData(List<RealmsServer> serverList, List<RealmsServer> availableSnapshotServers) { this.serverList = serverList; this.availableSnapshotServers = availableSnapshotServers; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 54 */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData; } public List<RealmsServer> serverList() { return this.serverList; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;
/* 54 */       //   0	8	1	o	Ljava/lang/Object; } public List<RealmsServer> availableSnapshotServers() { return this.availableSnapshotServers; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/RealmsDataFetcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */