/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class ServerResourcePackConfigurationTask
/*    */   implements ConfigurationTask {
/* 12 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_resource_pack");
/*    */   
/*    */   private final MinecraftServer.ServerResourcePackInfo info;
/*    */   
/*    */   public ServerResourcePackConfigurationTask(MinecraftServer.ServerResourcePackInfo info) {
/* 17 */     this.info = info;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(Consumer<Packet<?>> connection) {
/* 22 */     connection.accept(new ClientboundResourcePackPushPacket(this.info.id(), this.info.url(), this.info.hash(), this.info.isRequired(), Optional.ofNullable(this.info.prompt())));
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 27 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/config/ServerResourcePackConfigurationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */