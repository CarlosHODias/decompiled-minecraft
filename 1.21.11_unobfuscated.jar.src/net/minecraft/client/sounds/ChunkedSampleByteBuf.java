/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.floats.FloatConsumer;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.lwjgl.BufferUtils;
/*    */ 
/*    */ public class ChunkedSampleByteBuf implements FloatConsumer {
/* 12 */   private final List<ByteBuffer> buffers = Lists.newArrayList();
/*    */   
/*    */   private final int bufferSize;
/*    */   private int byteCount;
/*    */   private ByteBuffer currentBuffer;
/*    */   
/*    */   public ChunkedSampleByteBuf(int bufferSize) {
/* 19 */     this.bufferSize = bufferSize + 1 & 0xFFFFFFFE;
/* 20 */     this.currentBuffer = BufferUtils.createByteBuffer(bufferSize);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(float sample) {
/* 25 */     if (this.currentBuffer.remaining() == 0) {
/* 26 */       this.currentBuffer.flip();
/* 27 */       this.buffers.add(this.currentBuffer);
/* 28 */       this.currentBuffer = BufferUtils.createByteBuffer(this.bufferSize);
/*    */     } 
/*    */     
/* 31 */     int intVal = Mth.clamp((int)(sample * 32767.5F - 0.5F), -32768, 32767);
/* 32 */     this.currentBuffer.putShort((short)intVal);
/* 33 */     this.byteCount += 2;
/*    */   }
/*    */   
/*    */   public ByteBuffer get() {
/* 37 */     this.currentBuffer.flip();
/*    */     
/* 39 */     if (this.buffers.isEmpty()) {
/* 40 */       return this.currentBuffer;
/*    */     }
/*    */     
/* 43 */     ByteBuffer result = BufferUtils.createByteBuffer(this.byteCount);
/* 44 */     Objects.requireNonNull(result); this.buffers.forEach(result::put);
/* 45 */     result.put(this.currentBuffer);
/* 46 */     result.flip();
/* 47 */     return result;
/*    */   }
/*    */   
/*    */   public int size() {
/* 51 */     return this.byteCount;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/ChunkedSampleByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */