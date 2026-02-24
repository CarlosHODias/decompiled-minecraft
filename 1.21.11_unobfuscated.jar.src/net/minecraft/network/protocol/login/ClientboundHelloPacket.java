/*    */ package net.minecraft.network.protocol.login;
/*    */ import java.security.PublicKey;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.Crypt;
/*    */ import net.minecraft.util.CryptException;
/*    */ 
/*    */ public class ClientboundHelloPacket implements Packet<ClientLoginPacketListener> {
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundHelloPacket> STREAM_CODEC = Packet.codec(ClientboundHelloPacket::write, ClientboundHelloPacket::new);
/*    */   
/*    */   private final String serverId;
/*    */   private final byte[] publicKey;
/*    */   private final byte[] challenge;
/*    */   private final boolean shouldAuthenticate;
/*    */   
/*    */   public ClientboundHelloPacket(String serverId, byte[] publicKey, byte[] challenge, boolean shouldAuthenticate) {
/* 21 */     this.serverId = serverId;
/* 22 */     this.publicKey = publicKey;
/* 23 */     this.challenge = challenge;
/* 24 */     this.shouldAuthenticate = shouldAuthenticate;
/*    */   }
/*    */   
/*    */   private ClientboundHelloPacket(FriendlyByteBuf input) {
/* 28 */     this.serverId = input.readUtf(20);
/* 29 */     this.publicKey = input.readByteArray();
/* 30 */     this.challenge = input.readByteArray();
/* 31 */     this.shouldAuthenticate = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 35 */     output.writeUtf(this.serverId);
/* 36 */     output.writeByteArray(this.publicKey);
/* 37 */     output.writeByteArray(this.challenge);
/* 38 */     output.writeBoolean(this.shouldAuthenticate);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundHelloPacket> type() {
/* 43 */     return LoginPacketTypes.CLIENTBOUND_HELLO;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener listener) {
/* 48 */     listener.handleHello(this);
/*    */   }
/*    */   
/*    */   public String getServerId() {
/* 52 */     return this.serverId;
/*    */   }
/*    */   
/*    */   public PublicKey getPublicKey() throws CryptException {
/* 56 */     return Crypt.byteToPublicKey(this.publicKey);
/*    */   }
/*    */   
/*    */   public byte[] getChallenge() {
/* 60 */     return this.challenge;
/*    */   }
/*    */   
/*    */   public boolean shouldAuthenticate() {
/* 64 */     return this.shouldAuthenticate;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ClientboundHelloPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */