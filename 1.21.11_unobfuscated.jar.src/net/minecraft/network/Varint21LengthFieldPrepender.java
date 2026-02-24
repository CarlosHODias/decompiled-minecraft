/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ 
/*    */ @io.netty.channel.ChannelHandler.Sharable
/*    */ public class Varint21LengthFieldPrepender
/*    */   extends MessageToByteEncoder<ByteBuf>
/*    */ {
/*    */   public static final int MAX_VARINT21_BYTES = 3;
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
/* 16 */     int bodyLength = msg.readableBytes();
/* 17 */     int headerLength = VarInt.getByteSize(bodyLength);
/*    */     
/* 19 */     if (headerLength > 3) {
/* 20 */       throw new EncoderException("Packet too large: size " + bodyLength + " is over 8");
/*    */     }
/*    */     
/* 23 */     out.ensureWritable(headerLength + bodyLength);
/*    */     
/* 25 */     VarInt.write(out, bodyLength);
/* 26 */     out.writeBytes(msg, msg.readerIndex(), bodyLength);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/Varint21LengthFieldPrepender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */