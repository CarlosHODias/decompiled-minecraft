/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ 
/*    */ public class SkipPacketDecoderException extends DecoderException implements IdDispatchCodec.DontDecorateException, SkipPacketException {
/*    */   public SkipPacketDecoderException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public SkipPacketDecoderException(Throwable cause) {
/* 12 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/SkipPacketDecoderException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */