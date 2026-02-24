/*    */ package net.minecraft.client.sounds;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class LoopingAudioStream implements AudioStream {
/*    */   private final AudioStreamProvider provider;
/*    */   private AudioStream stream;
/*    */   private final java.io.BufferedInputStream bufferedInputStream;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface AudioStreamProvider {
/*    */     AudioStream create(InputStream param1InputStream) throws IOException;
/*    */   }
/*    */   
/*    */   private static class NoCloseBuffer extends java.io.FilterInputStream {
/*    */     private NoCloseBuffer(InputStream in) {
/* 18 */       super(in);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void close() {}
/*    */   }
/*    */ 
/*    */   
/*    */   public LoopingAudioStream(AudioStreamProvider provider, InputStream originalInputStream) throws IOException {
/* 28 */     this.provider = provider;
/* 29 */     this.bufferedInputStream = new java.io.BufferedInputStream(originalInputStream);
/* 30 */     this.bufferedInputStream.mark(Integer.MAX_VALUE);
/* 31 */     this.stream = provider.create(new NoCloseBuffer(this.bufferedInputStream));
/*    */   }
/*    */ 
/*    */   
/*    */   public javax.sound.sampled.AudioFormat getFormat() {
/* 36 */     return this.stream.getFormat();
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuffer read(int expectedSize) throws IOException {
/* 41 */     ByteBuffer result = this.stream.read(expectedSize);
/* 42 */     if (!result.hasRemaining()) {
/* 43 */       this.stream.close();
/* 44 */       this.bufferedInputStream.reset();
/* 45 */       this.stream = this.provider.create(new NoCloseBuffer(this.bufferedInputStream));
/* 46 */       result = this.stream.read(expectedSize);
/*    */     } 
/*    */     
/* 49 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 54 */     this.stream.close();
/* 55 */     this.bufferedInputStream.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/LoopingAudioStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */