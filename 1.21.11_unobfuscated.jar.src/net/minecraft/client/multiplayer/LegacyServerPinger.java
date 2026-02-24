/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import com.google.common.base.Splitter;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.SimpleChannelInboundHandler;
/*    */ import io.netty.util.concurrent.GenericFutureListener;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*    */ import net.minecraft.server.network.LegacyProtocolUtils;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class LegacyServerPinger extends SimpleChannelInboundHandler<ByteBuf> {
/* 15 */   private static final Splitter SPLITTER = Splitter.on(Character.MIN_VALUE).limit(6);
/*    */   
/*    */   private final ServerAddress address;
/*    */   private final Output output;
/*    */   
/*    */   public LegacyServerPinger(ServerAddress address, Output output) {
/* 21 */     this.address = address;
/* 22 */     this.output = output;
/*    */   }
/*    */ 
/*    */   
/*    */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 27 */     super.channelActive(ctx);
/* 28 */     ByteBuf toSend = ctx.alloc().buffer();
/*    */     try {
/* 30 */       toSend.writeByte(254);
/* 31 */       toSend.writeByte(1);
/*    */ 
/*    */ 
/*    */       
/* 35 */       toSend.writeByte(250);
/* 36 */       LegacyProtocolUtils.writeLegacyString(toSend, "MC|PingHost");
/* 37 */       int sizeIndex = toSend.writerIndex();
/* 38 */       toSend.writeShort(0);
/* 39 */       int payloadStart = toSend.writerIndex();
/* 40 */       toSend.writeByte(127);
/* 41 */       LegacyProtocolUtils.writeLegacyString(toSend, this.address.getHost());
/* 42 */       toSend.writeInt(this.address.getPort());
/* 43 */       int payloadSize = toSend.writerIndex() - payloadStart;
/* 44 */       toSend.setShort(sizeIndex, payloadSize);
/*    */       
/* 46 */       ctx.channel().writeAndFlush(toSend).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/* 47 */     } catch (Exception e) {
/* 48 */       toSend.release();
/* 49 */       throw e;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
/* 55 */     short firstByte = msg.readUnsignedByte();
/*    */     
/* 57 */     if (firstByte == 255) {
/* 58 */       String str = LegacyProtocolUtils.readLegacyString(msg);
/* 59 */       List<String> split = SPLITTER.splitToList(str);
/*    */       
/* 61 */       if ("§1".equals(split.get(0))) {
/* 62 */         int protocolVersion = Mth.getInt(split.get(1), 0);
/* 63 */         String version = split.get(2);
/* 64 */         String motd = split.get(3);
/* 65 */         int curPlayers = Mth.getInt(split.get(4), -1);
/* 66 */         int maxPlayers = Mth.getInt(split.get(5), -1);
/*    */         
/* 68 */         this.output.handleResponse(protocolVersion, version, motd, curPlayers, maxPlayers);
/*    */       } 
/*    */     } 
/*    */     
/* 72 */     ctx.close();
/*    */   }
/*    */ 
/*    */   
/*    */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
/* 77 */     ctx.close();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Output {
/*    */     void handleResponse(int param1Int1, String param1String1, String param1String2, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/LegacyServerPinger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */