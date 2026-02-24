/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.List;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherDecoder
/*    */   extends MessageToMessageDecoder<ByteBuf> {
/*    */   private final CipherBase cipher;
/*    */   
/*    */   public CipherDecoder(Cipher cipher) {
/* 14 */     this.cipher = new CipherBase(cipher);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
/* 19 */     out.add(this.cipher.decipher(ctx, msg));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/CipherDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */