/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ 
/*    */ public class ServerStateService {
/*    */   public static final class ServerState extends Record {
/*    */     private final boolean started;
/*    */     private final List<PlayerDto> players;
/*    */     private final net.minecraft.network.protocol.status.ServerStatus.Version version;
/*    */     public static final com.mojang.serialization.Codec<ServerState> CODEC;
/*    */     
/* 16 */     public ServerState(boolean started, List<PlayerDto> players, net.minecraft.network.protocol.status.ServerStatus.Version version) { this.started = started; this.players = players; this.version = version; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 16 */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState; } public boolean started() { return this.started; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;
/* 16 */       //   0	8	1	o	Ljava/lang/Object; } public List<PlayerDto> players() { return this.players; } public net.minecraft.network.protocol.status.ServerStatus.Version version() { return this.version; } static {
/* 17 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)com.mojang.serialization.Codec.BOOL.fieldOf("started").forGetter(ServerState::started), (App)PlayerDto.CODEC.codec().listOf().lenientOptionalFieldOf("players", List.of()).forGetter(ServerState::players), (App)net.minecraft.network.protocol.status.ServerStatus.Version.CODEC.fieldOf("version").forGetter(ServerState::version)).apply((com.mojang.datafixers.kinds.Applicative)i, ServerState::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     public static final ServerState NOT_STARTED = new ServerState(false, List.of(), net.minecraft.network.protocol.status.ServerStatus.Version.current());
/*    */   }
/*    */   
/*    */   public static ServerState status(MinecraftApi minecraftApi) {
/* 27 */     if (!minecraftApi.serverStateService().isReady()) {
/* 28 */       return ServerState.NOT_STARTED;
/*    */     }
/* 30 */     return new ServerState(true, 
/*    */         
/* 32 */         PlayerService.get(minecraftApi), 
/* 33 */         net.minecraft.network.protocol.status.ServerStatus.Version.current());
/*    */   }
/*    */   
/*    */   public static boolean save(MinecraftApi minecraftApi, boolean flush, ClientInfo clientInfo) {
/* 37 */     return minecraftApi.serverStateService().saveEverything(true, flush, true, clientInfo);
/*    */   }
/*    */   
/*    */   public static boolean stop(MinecraftApi minecraftApi, ClientInfo clientInfo) {
/* 41 */     minecraftApi.submit(() -> minecraftApi.serverStateService().halt(false, clientInfo));
/* 42 */     return true;
/*    */   }
/*    */   public static final class SystemMessage extends Record { private final Message message; private final boolean overlay; private final java.util.Optional<List<PlayerDto>> receivingPlayers; public static final com.mojang.serialization.Codec<SystemMessage> CODEC;
/* 45 */     public SystemMessage(Message message, boolean overlay, java.util.Optional<List<PlayerDto>> receivingPlayers) { this.message = message; this.overlay = overlay; this.receivingPlayers = receivingPlayers; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;
/* 45 */       //   0	8	1	o	Ljava/lang/Object; } public Message message() { return this.message; } public boolean overlay() { return this.overlay; } public java.util.Optional<List<PlayerDto>> receivingPlayers() { return this.receivingPlayers; } static {
/* 46 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Message.CODEC.fieldOf("message").forGetter(SystemMessage::message), (App)com.mojang.serialization.Codec.BOOL.fieldOf("overlay").forGetter(SystemMessage::overlay), (App)PlayerDto.CODEC.codec().listOf().lenientOptionalFieldOf("receivingPlayers").forGetter(SystemMessage::receivingPlayers)).apply((com.mojang.datafixers.kinds.Applicative)i, SystemMessage::new));
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean systemMessage(MinecraftApi minecraftApi, SystemMessage systemMessage, ClientInfo clientInfo) {
/* 54 */     net.minecraft.network.chat.Component component = systemMessage.message().asComponent().orElse(null);
/* 55 */     if (component == null) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     if (systemMessage.receivingPlayers().isPresent()) {
/* 60 */       if (((List)systemMessage.receivingPlayers().get()).isEmpty()) {
/* 61 */         return false;
/*    */       }
/* 63 */       for (PlayerDto playerDto : systemMessage.receivingPlayers().get()) {
/*    */         net.minecraft.server.level.ServerPlayer player;
/* 65 */         if (playerDto.id().isPresent()) {
/* 66 */           player = minecraftApi.playerListService().getPlayer(playerDto.id().get());
/* 67 */         } else if (playerDto.name().isPresent()) {
/* 68 */           player = minecraftApi.playerListService().getPlayerByName(playerDto.name().get());
/*    */         } else {
/*    */           continue;
/*    */         } 
/* 72 */         if (player != null) {
/* 73 */           player.sendSystemMessage(component, systemMessage.overlay());
/*    */         }
/*    */       } 
/*    */     } else {
/* 77 */       minecraftApi.serverStateService().broadcastSystemMessage(component, systemMessage.overlay(), clientInfo);
/*    */     } 
/*    */     
/* 80 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/methods/ServerStateService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */