/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.floats.FloatConsumer;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ public interface FloatSampleSource
/*    */   extends FiniteAudioStream
/*    */ {
/*    */   public static final int EXPECTED_MAX_FRAME_SIZE = 8192;
/*    */   
/*    */   boolean readChunk(FloatConsumer paramFloatConsumer) throws IOException;
/*    */   
/*    */   default ByteBuffer read(int expectedSize) throws IOException {
/* 16 */     ChunkedSampleByteBuf output = new ChunkedSampleByteBuf(expectedSize + 8192);
/* 17 */     while (readChunk(output) && output.size() < expectedSize);
/*    */ 
/*    */     
/* 20 */     return output.get();
/*    */   }
/*    */ 
/*    */   
/*    */   default ByteBuffer readAll() throws IOException {
/* 25 */     ChunkedSampleByteBuf output = new ChunkedSampleByteBuf(16384);
/* 26 */     while (readChunk(output));
/*    */ 
/*    */     
/* 29 */     return output.get();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/FloatSampleSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */