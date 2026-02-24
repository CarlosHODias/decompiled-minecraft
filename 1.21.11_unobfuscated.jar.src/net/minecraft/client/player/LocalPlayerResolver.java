/*    */ package net.minecraft.client.player;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.client.multiplayer.PlayerInfo;
/*    */ import net.minecraft.server.players.ProfileResolver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalPlayerResolver
/*    */   implements ProfileResolver
/*    */ {
/*    */   private final Minecraft minecraft;
/*    */   private final ProfileResolver parentResolver;
/*    */   
/*    */   public LocalPlayerResolver(Minecraft minecraft, ProfileResolver parentResolver) {
/* 21 */     this.minecraft = minecraft;
/* 22 */     this.parentResolver = parentResolver;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<GameProfile> fetchByName(String name) {
/* 27 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 28 */     if (connection != null) {
/*    */       
/* 30 */       PlayerInfo playerInfo = connection.getPlayerInfoIgnoreCase(name);
/* 31 */       if (playerInfo != null) {
/* 32 */         return Optional.of(playerInfo.getProfile());
/*    */       }
/*    */     } 
/*    */     
/* 36 */     return this.parentResolver.fetchByName(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<GameProfile> fetchById(UUID id) {
/* 41 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 42 */     if (connection != null) {
/* 43 */       PlayerInfo playerInfo = connection.getPlayerInfo(id);
/* 44 */       if (playerInfo != null) {
/* 45 */         return Optional.of(playerInfo.getProfile());
/*    */       }
/*    */     } 
/*    */     
/* 49 */     return this.parentResolver.fetchById(id);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/player/LocalPlayerResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */