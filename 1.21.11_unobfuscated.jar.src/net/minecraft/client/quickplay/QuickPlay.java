/*     */ package net.minecraft.client.quickplay;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsServerList;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.util.task.GetServerDetailsTask;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.ConnectScreen;
/*     */ import net.minecraft.client.gui.screens.DisconnectedScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.TitleScreen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
/*     */ import net.minecraft.client.main.GameConfig;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class QuickPlay
/*     */ {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   public static final Component ERROR_TITLE = (Component)Component.translatable("quickplay.error.title");
/*  37 */   private static final Component INVALID_IDENTIFIER = (Component)Component.translatable("quickplay.error.invalid_identifier");
/*  38 */   private static final Component REALM_CONNECT = (Component)Component.translatable("quickplay.error.realm_connect");
/*  39 */   private static final Component REALM_PERMISSION = (Component)Component.translatable("quickplay.error.realm_permission");
/*     */   
/*  41 */   private static final Component TO_TITLE = (Component)Component.translatable("gui.toTitle");
/*  42 */   private static final Component TO_WORLD_LIST = (Component)Component.translatable("gui.toWorld");
/*  43 */   private static final Component TO_REALMS_LIST = (Component)Component.translatable("gui.toRealms");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void connect(Minecraft minecraft, GameConfig.QuickPlayVariant quickPlayVariant, RealmsClient realmsClient) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface isEnabled : ()Z
/*     */     //   6: ifne -> 31
/*     */     //   9: getstatic net/minecraft/client/quickplay/QuickPlay.LOGGER : Lorg/slf4j/Logger;
/*     */     //   12: ldc 'Quick play disabled'
/*     */     //   14: invokeinterface error : (Ljava/lang/String;)V
/*     */     //   19: aload_0
/*     */     //   20: new net/minecraft/client/gui/screens/TitleScreen
/*     */     //   23: dup
/*     */     //   24: invokespecial <init> : ()V
/*     */     //   27: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   30: return
/*     */     //   31: aload_1
/*     */     //   32: dup
/*     */     //   33: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   36: pop
/*     */     //   37: astore_3
/*     */     //   38: iconst_0
/*     */     //   39: istore #4
/*     */     //   41: aload_3
/*     */     //   42: iload #4
/*     */     //   44: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   49: tableswitch default -> 80, 0 -> 90, 1 -> 108, 2 -> 127, 3 -> 166
/*     */     //   80: new java/lang/MatchException
/*     */     //   83: dup
/*     */     //   84: aconst_null
/*     */     //   85: aconst_null
/*     */     //   86: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   89: athrow
/*     */     //   90: aload_3
/*     */     //   91: checkcast net/minecraft/client/main/GameConfig$QuickPlayMultiplayerData
/*     */     //   94: astore #5
/*     */     //   96: aload_0
/*     */     //   97: aload #5
/*     */     //   99: invokevirtual serverAddress : ()Ljava/lang/String;
/*     */     //   102: invokestatic joinMultiplayerWorld : (Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V
/*     */     //   105: goto -> 193
/*     */     //   108: aload_3
/*     */     //   109: checkcast net/minecraft/client/main/GameConfig$QuickPlayRealmsData
/*     */     //   112: astore #6
/*     */     //   114: aload_0
/*     */     //   115: aload_2
/*     */     //   116: aload #6
/*     */     //   118: invokevirtual realmId : ()Ljava/lang/String;
/*     */     //   121: invokestatic joinRealmsWorld : (Lnet/minecraft/client/Minecraft;Lcom/mojang/realmsclient/client/RealmsClient;Ljava/lang/String;)V
/*     */     //   124: goto -> 193
/*     */     //   127: aload_3
/*     */     //   128: checkcast net/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData
/*     */     //   131: astore #7
/*     */     //   133: aload #7
/*     */     //   135: invokevirtual worldId : ()Ljava/lang/String;
/*     */     //   138: astore #8
/*     */     //   140: aload #8
/*     */     //   142: invokestatic isBlank : (Ljava/lang/String;)Z
/*     */     //   145: ifeq -> 157
/*     */     //   148: aload_0
/*     */     //   149: invokevirtual getLevelSource : ()Lnet/minecraft/world/level/storage/LevelStorageSource;
/*     */     //   152: invokestatic getLatestSingleplayerWorld : (Lnet/minecraft/world/level/storage/LevelStorageSource;)Ljava/lang/String;
/*     */     //   155: astore #8
/*     */     //   157: aload_0
/*     */     //   158: aload #8
/*     */     //   160: invokestatic joinSingleplayerWorld : (Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V
/*     */     //   163: goto -> 193
/*     */     //   166: aload_3
/*     */     //   167: checkcast net/minecraft/client/main/GameConfig$QuickPlayDisabled
/*     */     //   170: astore #8
/*     */     //   172: getstatic net/minecraft/client/quickplay/QuickPlay.LOGGER : Lorg/slf4j/Logger;
/*     */     //   175: ldc 'Quick play disabled'
/*     */     //   177: invokeinterface error : (Ljava/lang/String;)V
/*     */     //   182: aload_0
/*     */     //   183: new net/minecraft/client/gui/screens/TitleScreen
/*     */     //   186: dup
/*     */     //   187: invokespecial <init> : ()V
/*     */     //   190: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   193: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     //   #47	-> 9
/*     */     //   #48	-> 19
/*     */     //   #49	-> 30
/*     */     //   #52	-> 31
/*     */     //   #53	-> 90
/*     */     //   #54	-> 108
/*     */     //   #55	-> 127
/*     */     //   #56	-> 133
/*     */     //   #57	-> 140
/*     */     //   #58	-> 148
/*     */     //   #60	-> 157
/*     */     //   #61	-> 163
/*     */     //   #62	-> 166
/*     */     //   #63	-> 172
/*     */     //   #64	-> 182
/*     */     //   #67	-> 193
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   96	12	5	multiplayerData	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*     */     //   114	13	6	realmsData	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */     //   140	23	8	worldId	Ljava/lang/String;
/*     */     //   133	33	7	singlePlayerData	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*     */     //   172	21	8	disabled	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */     //   0	194	0	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   0	194	1	quickPlayVariant	Lnet/minecraft/client/main/GameConfig$QuickPlayVariant;
/*     */     //   0	194	2	realmsClient	Lcom/mojang/realmsclient/client/RealmsClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getLatestSingleplayerWorld(LevelStorageSource levelSource) {
/*     */     try {
/*  71 */       List<LevelSummary> levels = levelSource.loadLevelSummaries(levelSource.findLevelCandidates()).get();
/*  72 */       if (levels.isEmpty()) {
/*  73 */         LOGGER.warn("no latest singleplayer world found");
/*  74 */         return null;
/*     */       } 
/*  76 */       return ((LevelSummary)levels.getFirst()).getLevelId();
/*  77 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/*  78 */       LOGGER.error("failed to load singleplayer world summaries", e);
/*  79 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void joinSingleplayerWorld(Minecraft minecraft, String identifier) {
/*  84 */     if (StringUtil.isBlank(identifier) || !minecraft.getLevelSource().levelExists(identifier)) {
/*  85 */       SelectWorldScreen selectWorldScreen = new SelectWorldScreen((Screen)new TitleScreen());
/*  86 */       minecraft.setScreen((Screen)new DisconnectedScreen((Screen)selectWorldScreen, ERROR_TITLE, INVALID_IDENTIFIER, TO_WORLD_LIST));
/*     */       return;
/*     */     } 
/*  89 */     minecraft.createWorldOpenFlows().openWorld(identifier, () -> minecraft.setScreen((Screen)new TitleScreen()));
/*     */   }
/*     */   
/*     */   private static void joinMultiplayerWorld(Minecraft minecraft, String serverAddressString) {
/*  93 */     ServerList servers = new ServerList(minecraft);
/*  94 */     servers.load();
/*  95 */     ServerData serverData = servers.get(serverAddressString);
/*  96 */     if (serverData == null) {
/*  97 */       serverData = new ServerData(I18n.get("selectServer.defaultName", new Object[0]), serverAddressString, ServerData.Type.OTHER);
/*  98 */       servers.add(serverData, true);
/*  99 */       servers.save();
/*     */     } 
/*     */     
/* 102 */     ServerAddress serverAddress = ServerAddress.parseString(serverAddressString);
/* 103 */     ConnectScreen.startConnecting((Screen)new JoinMultiplayerScreen((Screen)new TitleScreen()), minecraft, serverAddress, serverData, true, null);
/*     */   }
/*     */   
/*     */   private static void joinRealmsWorld(Minecraft minecraft, RealmsClient realmsClient, String identifier) {
/*     */     long realmId;
/*     */     RealmsServerList realmsServerList;
/*     */     try {
/* 110 */       realmId = Long.parseLong(identifier);
/* 111 */       realmsServerList = realmsClient.listRealms();
/* 112 */     } catch (NumberFormatException e) {
/* 113 */       RealmsMainScreen realmsMainScreen = new RealmsMainScreen((Screen)new TitleScreen());
/* 114 */       minecraft.setScreen((Screen)new DisconnectedScreen((Screen)realmsMainScreen, ERROR_TITLE, INVALID_IDENTIFIER, TO_REALMS_LIST));
/*     */       return;
/* 116 */     } catch (RealmsServiceException e) {
/* 117 */       TitleScreen titleScreen1 = new TitleScreen();
/* 118 */       minecraft.setScreen((Screen)new DisconnectedScreen((Screen)titleScreen1, ERROR_TITLE, REALM_CONNECT, TO_TITLE));
/*     */       return;
/*     */     } 
/* 121 */     RealmsServer server = realmsServerList.servers().stream().filter(realmsServer -> (realmsServer.id == realmId)).findFirst().orElse(null);
/* 122 */     if (server == null) {
/* 123 */       RealmsMainScreen realmsMainScreen = new RealmsMainScreen((Screen)new TitleScreen());
/* 124 */       minecraft.setScreen((Screen)new DisconnectedScreen((Screen)realmsMainScreen, ERROR_TITLE, REALM_PERMISSION, TO_REALMS_LIST));
/*     */       
/*     */       return;
/*     */     } 
/* 128 */     TitleScreen titleScreen = new TitleScreen();
/* 129 */     minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)titleScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask((Screen)titleScreen, server) }));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/quickplay/QuickPlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */