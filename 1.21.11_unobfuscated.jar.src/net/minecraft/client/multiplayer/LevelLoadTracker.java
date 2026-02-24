/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.level.progress.LevelLoadProgressTracker;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LevelLoadTracker
/*     */   implements LevelLoadListener
/*     */ {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  21 */   private static final long CLIENT_WAIT_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30L);
/*     */ 
/*     */   
/*     */   public static final long LEVEL_LOAD_CLOSE_DELAY_MS = 500L;
/*     */   
/*  26 */   private final LevelLoadProgressTracker serverProgressTracker = new LevelLoadProgressTracker(true);
/*     */   
/*     */   private ChunkLoadStatusView serverChunkStatusView;
/*     */   private volatile LevelLoadListener.Stage serverStage;
/*     */   private ClientState clientState;
/*     */   private final long closeDelayMs;
/*     */   
/*     */   public LevelLoadTracker() {
/*  34 */     this(0L);
/*     */   }
/*     */   
/*     */   public LevelLoadTracker(long closeDelayMs) {
/*  38 */     this.closeDelayMs = closeDelayMs;
/*     */   }
/*     */   
/*     */   public void setServerChunkStatusView(ChunkLoadStatusView serverChunkStatusView) {
/*  42 */     this.serverChunkStatusView = serverChunkStatusView;
/*     */   }
/*     */   
/*     */   public void startClientLoad(LocalPlayer player, ClientLevel level, LevelRenderer levelRenderer) {
/*  46 */     this.clientState = new WaitingForServer(player, level, levelRenderer, Util.getMillis() + CLIENT_WAIT_TIMEOUT_MS);
/*     */   }
/*     */   
/*     */   public void tickClientLoad() {
/*  50 */     if (this.clientState != null) {
/*  51 */       this.clientState = this.clientState.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLevelReady() {
/*  56 */     ClientState clientState = this.clientState; if (clientState instanceof ClientLevelReady) { ClientLevelReady clientLevelReady = (ClientLevelReady)clientState; try { long l1 = clientLevelReady.readyAt(), readyAt = l1;
/*  57 */         if (Util.getMillis() >= readyAt + this.closeDelayMs); return false; } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */        }
/*     */     
/*     */     return false; } public void loadingPacketsReceived() {
/*  61 */     if (this.clientState != null) {
/*  62 */       this.clientState = this.clientState.loadingPacketsReceived();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(LevelLoadListener.Stage stage, int totalChunks) {
/*  68 */     this.serverProgressTracker.start(stage, totalChunks);
/*  69 */     this.serverStage = stage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(LevelLoadListener.Stage stage, int currentChunks, int totalChunks) {
/*  74 */     this.serverProgressTracker.update(stage, currentChunks, totalChunks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void finish(LevelLoadListener.Stage stage) {
/*  79 */     this.serverProgressTracker.finish(stage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos) {
/*  84 */     if (this.serverChunkStatusView != null) {
/*  85 */       this.serverChunkStatusView.moveTo(dimension, chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChunkLoadStatusView statusView() {
/*  90 */     return this.serverChunkStatusView;
/*     */   }
/*     */   
/*     */   public float serverProgress() {
/*  94 */     return this.serverProgressTracker.get();
/*     */   }
/*     */   
/*     */   public boolean hasProgress() {
/*  98 */     return (this.serverStage != null);
/*     */   }
/*     */   
/*     */   private static interface ClientState {
/*     */     default ClientState tick() {
/* 103 */       return this;
/*     */     }
/*     */     
/*     */     default ClientState loadingPacketsReceived() {
/* 107 */       return this;
/*     */     } }
/*     */   private static final class WaitingForServer extends Record implements ClientState { private final LocalPlayer player; private final ClientLevel level; private final LevelRenderer levelRenderer;
/*     */     private final long timeoutAfter;
/*     */     
/* 112 */     private WaitingForServer(LocalPlayer player, ClientLevel level, LevelRenderer levelRenderer, long timeoutAfter) { this.player = player; this.level = level; this.levelRenderer = levelRenderer; this.timeoutAfter = timeoutAfter; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 112 */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer; } public LocalPlayer player() { return this.player; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/* 112 */       //   0	8	1	o	Ljava/lang/Object; } public ClientLevel level() { return this.level; } public LevelRenderer levelRenderer() { return this.levelRenderer; } public long timeoutAfter() { return this.timeoutAfter; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LevelLoadTracker.ClientState loadingPacketsReceived() {
/* 120 */       return new LevelLoadTracker.WaitingForPlayerChunk(this.player, this.level, this.levelRenderer, this.timeoutAfter);
/*     */     } }
/*     */   private static final class WaitingForPlayerChunk extends Record implements ClientState { private final LocalPlayer player; private final ClientLevel level; private final LevelRenderer levelRenderer;
/*     */     private final long timeoutAfter;
/*     */     
/* 125 */     private WaitingForPlayerChunk(LocalPlayer player, ClientLevel level, LevelRenderer levelRenderer, long timeoutAfter) { this.player = player; this.level = level; this.levelRenderer = levelRenderer; this.timeoutAfter = timeoutAfter; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/* 125 */       //   0	8	1	o	Ljava/lang/Object; } public LocalPlayer player() { return this.player; } public ClientLevel level() { return this.level; } public LevelRenderer levelRenderer() { return this.levelRenderer; } public long timeoutAfter() { return this.timeoutAfter; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LevelLoadTracker.ClientState tick() {
/* 133 */       return isReady() ? new LevelLoadTracker.ClientLevelReady(Util.getMillis()) : this;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isReady() {
/* 138 */       if (Util.getMillis() > this.timeoutAfter) {
/* 139 */         LevelLoadTracker.LOGGER.warn("Timed out while waiting for the client to load chunks, letting the player into the world anyway");
/* 140 */         return true;
/*     */       } 
/* 142 */       BlockPos playerPos = this.player.blockPosition();
/* 143 */       if (this.level.isOutsideBuildHeight(playerPos.getY()) || this.player.isSpectator() || !this.player.isAlive()) {
/* 144 */         return true;
/*     */       }
/* 146 */       return this.levelRenderer.isSectionCompiledAndVisible(playerPos);
/*     */     } }
/*     */   
/*     */   private static final class ClientLevelReady extends Record implements ClientState { private final long readyAt;
/*     */     
/* 151 */     private ClientLevelReady(long readyAt) { this.readyAt = readyAt; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/* 151 */       //   0	8	1	o	Ljava/lang/Object; } public long readyAt() { return this.readyAt; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/LevelLoadTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */