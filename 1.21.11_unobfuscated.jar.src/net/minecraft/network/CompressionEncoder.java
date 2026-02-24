/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.util.zip.Deflater;
/*    */ 
/*    */ public class CompressionEncoder
/*    */   extends MessageToByteEncoder<ByteBuf> {
/* 10 */   private final byte[] encodeBuf = new byte[8192];
/*    */   private final Deflater deflater;
/*    */   private int threshold;
/*    */   
/*    */   public CompressionEncoder(int threshold) {
/* 15 */     this.threshold = threshold;
/* 16 */     this.deflater = new Deflater();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf uncompressed, ByteBuf out) {
/* 21 */     int uncompressedLength = uncompressed.readableBytes();
/*    */     
/* 23 */     if (uncompressedLength > 8388608) {
/* 24 */       throw new IllegalArgumentException("Packet too big (is " + uncompressedLength + ", should be less than 8388608)");
/*    */     }
/* 26 */     if (uncompressedLength < this.threshold) {
/* 27 */       VarInt.write(out, 0);
/* 28 */       out.writeBytes(uncompressed);
/*    */     } else {
/* 30 */       byte[] input = new byte[uncompressedLength];
/* 31 */       uncompressed.readBytes(input);
/*    */       
/* 33 */       VarInt.write(out, input.length);
/*    */       
/* 35 */       this.deflater.setInput(input, 0, uncompressedLength);
/* 36 */       this.deflater.finish();
/* 37 */       while (!this.deflater.finished()) {
/* 38 */         int written = this.deflater.deflate(this.encodeBuf);
/* 39 */         out.writeBytes(this.encodeBuf, 0, written);
/*    */       } 
/* 41 */       this.deflater.reset();
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getThreshold() {
/* 46 */     return this.threshold;
/*    */   }
/*    */   
/*    */   public void setThreshold(int threshold) {
/* 50 */     this.threshold = threshold;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/CompressionEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */