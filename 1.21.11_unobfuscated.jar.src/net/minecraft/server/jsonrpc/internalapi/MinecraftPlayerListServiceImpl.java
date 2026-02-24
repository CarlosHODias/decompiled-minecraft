/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class MinecraftPlayerListServiceImpl
/*    */   implements MinecraftPlayerListService
/*    */ {
/*    */   private final JsonRpcLogger jsonRpcLogger;
/*    */   private final DedicatedServer server;
/*    */   
/*    */   public MinecraftPlayerListServiceImpl(DedicatedServer server, JsonRpcLogger jsonRpcLogger) {
/* 20 */     this.jsonRpcLogger = jsonRpcLogger;
/* 21 */     this.server = server;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ServerPlayer> getPlayers() {
/* 26 */     return this.server.getPlayerList().getPlayers();
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerPlayer getPlayer(UUID uuid) {
/* 31 */     return this.server.getPlayerList().getPlayer(uuid);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<NameAndId> fetchUserByName(String name) {
/* 36 */     return this.server.services().nameToIdCache().get(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<NameAndId> fetchUserById(UUID id) {
/* 41 */     return Optional.<ProfileResult>ofNullable(this.server.services().sessionService().fetchProfile(id, true))
/* 42 */       .map(profile -> new NameAndId(profile.profile()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<NameAndId> getCachedUserById(UUID id) {
/* 47 */     return this.server.services().nameToIdCache().get(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<ServerPlayer> getPlayer(Optional<UUID> id, Optional<String> name) {
/* 52 */     if (id.isPresent())
/* 53 */       return Optional.ofNullable(this.server.getPlayerList().getPlayer(id.get())); 
/* 54 */     if (name.isPresent()) {
/* 55 */       return Optional.ofNullable(this.server.getPlayerList().getPlayerByName(name.get()));
/*    */     }
/* 57 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ServerPlayer> getPlayersWithAddress(String ip) {
/* 62 */     return this.server.getPlayerList().getPlayersWithAddress(ip);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(ServerPlayer serverPlayer, ClientInfo clientInfo) {
/* 67 */     this.server.getPlayerList().remove(serverPlayer);
/* 68 */     this.jsonRpcLogger.log(clientInfo, "Remove player '{}'", new Object[] { serverPlayer.getPlainTextName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerPlayer getPlayerByName(String name) {
/* 73 */     return this.server.getPlayerList().getPlayerByName(name);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/internalapi/MinecraftPlayerListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */