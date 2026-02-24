/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundLoginFinishedPacket extends Record implements net.minecraft.network.protocol.Packet<ClientLoginPacketListener> {
/*    */   private final GameProfile gameProfile;
/*    */   
/* 10 */   public ClientboundLoginFinishedPacket(GameProfile gameProfile) { this.gameProfile = gameProfile; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket; } public GameProfile gameProfile() { return this.gameProfile; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 13 */   } public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundLoginFinishedPacket> STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.GAME_PROFILE, ClientboundLoginFinishedPacket::gameProfile, ClientboundLoginFinishedPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundLoginFinishedPacket> type() {
/* 20 */     return LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener listener) {
/* 25 */     listener.handleLoginFinished(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTerminal() {
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ClientboundLoginFinishedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */