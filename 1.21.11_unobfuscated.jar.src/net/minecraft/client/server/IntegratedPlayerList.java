/*    */ package net.minecraft.client.server;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.net.SocketAddress;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*    */ import net.minecraft.world.level.storage.TagValueOutput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class IntegratedPlayerList extends PlayerList {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private CompoundTag playerData;
/*    */   
/*    */   public IntegratedPlayerList(IntegratedServer server, LayeredRegistryAccess<RegistryLayer> registryHolder, PlayerDataStorage playerDataStorage) {
/* 24 */     super(server, registryHolder, playerDataStorage, (net.minecraft.server.notifications.NotificationService)server.notificationManager());
/*    */     
/* 26 */     setViewDistance(10);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void save(ServerPlayer player) {
/* 31 */     if (getServer().isSingleplayerOwner(player.nameAndId())) {
/* 32 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(player.problemPath(), LOGGER); 
/* 33 */       try { TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)player.registryAccess());
/* 34 */         player.saveWithoutId((ValueOutput)output);
/* 35 */         this.playerData = output.buildResult();
/* 36 */         reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */          throw throwable; }
/*    */     
/* 39 */     }  super.save(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component canPlayerLogin(SocketAddress address, NameAndId nameAndId) {
/* 44 */     if (getServer().isSingleplayerOwner(nameAndId) && getPlayerByName(nameAndId.name()) != null) {
/* 45 */       return (Component)Component.translatable("multiplayer.disconnect.name_taken");
/*    */     }
/*    */     
/* 48 */     return super.canPlayerLogin(address, nameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public IntegratedServer getServer() {
/* 53 */     return (IntegratedServer)super.getServer();
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getSingleplayerData() {
/* 58 */     return this.playerData;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/server/IntegratedPlayerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */