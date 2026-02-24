/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherEncoder
/*    */   extends MessageToByteEncoder<ByteBuf> {
/*    */   private final CipherBase cipher;
/*    */   
/*    */   public CipherEncoder(Cipher cipher) {
/* 13 */     this.cipher = new CipherBase(cipher);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
/* 18 */     this.cipher.encipher(msg, out);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/CipherEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */