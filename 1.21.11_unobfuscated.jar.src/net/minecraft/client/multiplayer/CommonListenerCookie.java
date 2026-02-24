/*    */ package net.minecraft.client.multiplayer;public final class CommonListenerCookie extends Record { private final LevelLoadTracker levelLoadTracker;
/*    */   private final com.mojang.authlib.GameProfile localGameProfile;
/*    */   private final net.minecraft.client.telemetry.WorldSessionTelemetryManager telemetryManager;
/*    */   private final net.minecraft.core.RegistryAccess.Frozen receivedRegistries;
/*    */   private final net.minecraft.world.flag.FeatureFlagSet enabledFeatures;
/*    */   private final String serverBrand;
/*    */   private final ServerData serverData;
/*    */   private final net.minecraft.client.gui.screens.Screen postDisconnectScreen;
/*    */   private final java.util.Map<net.minecraft.resources.Identifier, byte[]> serverCookies;
/*    */   private final net.minecraft.client.gui.components.ChatComponent.State chatState;
/*    */   private final java.util.Map<String, String> customReportDetails;
/*    */   private final net.minecraft.server.ServerLinks serverLinks;
/*    */   private final java.util.Map<java.util.UUID, PlayerInfo> seenPlayers;
/*    */   private final boolean seenInsecureChatWarning;
/*    */   
/* 16 */   public CommonListenerCookie(LevelLoadTracker levelLoadTracker, com.mojang.authlib.GameProfile localGameProfile, net.minecraft.client.telemetry.WorldSessionTelemetryManager telemetryManager, net.minecraft.core.RegistryAccess.Frozen receivedRegistries, net.minecraft.world.flag.FeatureFlagSet enabledFeatures, String serverBrand, ServerData serverData, net.minecraft.client.gui.screens.Screen postDisconnectScreen, java.util.Map<net.minecraft.resources.Identifier, byte[]> serverCookies, net.minecraft.client.gui.components.ChatComponent.State chatState, java.util.Map<String, String> customReportDetails, net.minecraft.server.ServerLinks serverLinks, java.util.Map<java.util.UUID, PlayerInfo> seenPlayers, boolean seenInsecureChatWarning) { this.levelLoadTracker = levelLoadTracker; this.localGameProfile = localGameProfile; this.telemetryManager = telemetryManager; this.receivedRegistries = receivedRegistries; this.enabledFeatures = enabledFeatures; this.serverBrand = serverBrand; this.serverData = serverData; this.postDisconnectScreen = postDisconnectScreen; this.serverCookies = serverCookies; this.chatState = chatState; this.customReportDetails = customReportDetails; this.serverLinks = serverLinks; this.seenPlayers = seenPlayers; this.seenInsecureChatWarning = seenInsecureChatWarning; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/CommonListenerCookie;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/CommonListenerCookie; } public LevelLoadTracker levelLoadTracker() { return this.levelLoadTracker; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/CommonListenerCookie;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/CommonListenerCookie; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/CommonListenerCookie;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/CommonListenerCookie;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public com.mojang.authlib.GameProfile localGameProfile() { return this.localGameProfile; } public net.minecraft.client.telemetry.WorldSessionTelemetryManager telemetryManager() { return this.telemetryManager; } public net.minecraft.core.RegistryAccess.Frozen receivedRegistries() { return this.receivedRegistries; } public net.minecraft.world.flag.FeatureFlagSet enabledFeatures() { return this.enabledFeatures; } public String serverBrand() { return this.serverBrand; } public ServerData serverData() { return this.serverData; } public net.minecraft.client.gui.screens.Screen postDisconnectScreen() { return this.postDisconnectScreen; } public java.util.Map<net.minecraft.resources.Identifier, byte[]> serverCookies() { return this.serverCookies; } public net.minecraft.client.gui.components.ChatComponent.State chatState() { return this.chatState; } public java.util.Map<String, String> customReportDetails() { return this.customReportDetails; } public net.minecraft.server.ServerLinks serverLinks() { return this.serverLinks; } public java.util.Map<java.util.UUID, PlayerInfo> seenPlayers() { return this.seenPlayers; } public boolean seenInsecureChatWarning() { return this.seenInsecureChatWarning; }
/*    */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/CommonListenerCookie.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */