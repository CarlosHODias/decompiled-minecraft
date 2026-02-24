/*   */ package net.minecraft.network;
/*   */ 
/*   */ import io.netty.channel.ChannelHandlerContext;
/*   */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*   */ 
/*   */ public class LocalFrameDecoder
/*   */   extends ChannelInboundHandlerAdapter {
/*   */   public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 9 */     ctx.fireChannelRead(HiddenByteBuf.unpack(msg));
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/LocalFrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */