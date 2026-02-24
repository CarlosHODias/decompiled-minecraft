/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class JoinWorldTask
/*    */   implements ConfigurationTask {
/* 10 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("join_world");
/*    */ 
/*    */   
/*    */   public void start(Consumer<Packet<?>> connection) {
/* 14 */     connection.accept(ClientboundFinishConfigurationPacket.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 19 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/config/JoinWorldTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */