/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import org.lwjgl.openal.AL10;
/*    */ import org.lwjgl.openal.ALC10;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class OpenAlUtil
/*    */ {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static String alErrorToString(int error) {
/* 14 */     switch (error) {
/*    */       case 40961:
/* 16 */         return "Invalid name parameter.";
/*    */       case 40962:
/* 18 */         return "Invalid enumerated parameter value.";
/*    */       case 40963:
/* 20 */         return "Invalid parameter parameter value.";
/*    */       case 40964:
/* 22 */         return "Invalid operation.";
/*    */       case 40965:
/* 24 */         return "Unable to allocate memory.";
/*    */     } 
/* 26 */     return "An unrecognized error occurred.";
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean checkALError(String location) {
/* 31 */     int error = AL10.alGetError();
/* 32 */     if (error != 0) {
/* 33 */       LOGGER.error("{}: {}", location, alErrorToString(error));
/* 34 */       return true;
/*    */     } 
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   private static String alcErrorToString(int error) {
/* 40 */     switch (error) {
/*    */       case 40961:
/* 42 */         return "Invalid device.";
/*    */       case 40962:
/* 44 */         return "Invalid context.";
/*    */       case 40964:
/* 46 */         return "Invalid value.";
/*    */       case 40963:
/* 48 */         return "Illegal enum.";
/*    */       case 40965:
/* 50 */         return "Unable to allocate memory.";
/*    */     } 
/* 52 */     return "An unrecognized error occurred.";
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean checkALCError(long device, String location) {
/* 57 */     int error = ALC10.alcGetError(device);
/* 58 */     if (error != 0) {
/* 59 */       LOGGER.error("{} ({}): {}", new Object[] { location, device, alcErrorToString(error) });
/* 60 */       return true;
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   static int audioFormatToOpenAl(AudioFormat audioFormat) {
/* 66 */     AudioFormat.Encoding encoding = audioFormat.getEncoding();
/* 67 */     int channels = audioFormat.getChannels();
/* 68 */     int sampleSizeInBits = audioFormat.getSampleSizeInBits();
/*    */     
/* 70 */     if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
/* 71 */       if (channels == 1) {
/* 72 */         if (sampleSizeInBits == 8)
/* 73 */           return 4352; 
/* 74 */         if (sampleSizeInBits == 16) {
/* 75 */           return 4353;
/*    */         }
/* 77 */       } else if (channels == 2) {
/* 78 */         if (sampleSizeInBits == 8)
/* 79 */           return 4354; 
/* 80 */         if (sampleSizeInBits == 16) {
/* 81 */           return 4355;
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 86 */     throw new IllegalArgumentException("Invalid audio format: " + String.valueOf(audioFormat));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/audio/OpenAlUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */