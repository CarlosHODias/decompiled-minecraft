/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.OptionalInt;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import org.lwjgl.openal.AL10;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SoundBuffer
/*    */ {
/*    */   private ByteBuffer data;
/*    */   private final AudioFormat format;
/*    */   private boolean hasAlBuffer;
/*    */   private int alBuffer;
/*    */   
/*    */   public SoundBuffer(ByteBuffer data, AudioFormat format) {
/* 19 */     this.data = data;
/* 20 */     this.format = format;
/*    */   }
/*    */   
/*    */   OptionalInt getAlBuffer() {
/* 24 */     if (!this.hasAlBuffer) {
/* 25 */       if (this.data == null) {
/* 26 */         return OptionalInt.empty();
/*    */       }
/* 28 */       int audioFormat = OpenAlUtil.audioFormatToOpenAl(this.format);
/* 29 */       int[] intBuffer = new int[1];
/* 30 */       AL10.alGenBuffers(intBuffer);
/* 31 */       if (OpenAlUtil.checkALError("Creating buffer")) {
/* 32 */         return OptionalInt.empty();
/*    */       }
/* 34 */       AL10.alBufferData(intBuffer[0], audioFormat, this.data, (int)this.format.getSampleRate());
/* 35 */       if (OpenAlUtil.checkALError("Assigning buffer data")) {
/* 36 */         return OptionalInt.empty();
/*    */       }
/* 38 */       this.alBuffer = intBuffer[0];
/* 39 */       this.hasAlBuffer = true;
/* 40 */       this.data = null;
/*    */     } 
/*    */     
/* 43 */     return OptionalInt.of(this.alBuffer);
/*    */   }
/*    */   
/*    */   public void discardAlBuffer() {
/* 47 */     if (this.hasAlBuffer) {
/* 48 */       AL10.alDeleteBuffers(new int[] { this.alBuffer });
/* 49 */       if (OpenAlUtil.checkALError("Deleting stream buffers")) {
/*    */         return;
/*    */       }
/*    */     } 
/* 53 */     this.hasAlBuffer = false;
/*    */   }
/*    */   
/*    */   public OptionalInt releaseAlBuffer() {
/* 57 */     OptionalInt result = getAlBuffer();
/* 58 */     this.hasAlBuffer = false;
/* 59 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/audio/SoundBuffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */