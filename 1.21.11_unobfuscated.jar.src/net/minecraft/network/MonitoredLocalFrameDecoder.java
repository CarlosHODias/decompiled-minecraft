/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ 
/*    */ public class MonitoredLocalFrameDecoder extends ChannelInboundHandlerAdapter {
/*    */   private final BandwidthDebugMonitor monitor;
/*    */   
/*    */   public MonitoredLocalFrameDecoder(BandwidthDebugMonitor monitor) {
/* 11 */     this.monitor = monitor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 16 */     msg = HiddenByteBuf.unpack(msg);
/* 17 */     if (msg instanceof ByteBuf) { ByteBuf in = (ByteBuf)msg;
/* 18 */       this.monitor.onReceive(in.readableBytes()); }
/*    */     
/* 20 */     ctx.fireChannelRead(msg);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/MonitoredLocalFrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */