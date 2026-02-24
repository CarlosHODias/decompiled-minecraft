/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ public class VarLong {
/*    */   private static final int MAX_VARLONG_SIZE = 10;
/*    */   private static final int DATA_BITS_MASK = 127;
/*    */   private static final int CONTINUATION_BIT_MASK = 128;
/*    */   private static final int DATA_BITS_PER_BYTE = 7;
/*    */   
/*    */   public static int getByteSize(long value) {
/* 12 */     for (int i = 1; i < 10; i++) {
/* 13 */       if ((value & -1L << i * 7) == 0L) {
/* 14 */         return i;
/*    */       }
/*    */     } 
/* 17 */     return 10;
/*    */   }
/*    */   
/*    */   public static boolean hasContinuationBit(byte in) {
/* 21 */     return ((in & 0x80) == 128);
/*    */   }
/*    */   public static long read(ByteBuf input) {
/*    */     byte in;
/* 25 */     long out = 0L;
/* 26 */     int bytes = 0;
/*    */     do {
/* 28 */       in = input.readByte();
/*    */       
/* 30 */       out |= (in & Byte.MAX_VALUE) << bytes++ * 7;
/*    */       
/* 32 */       if (bytes > 10) {
/* 33 */         throw new RuntimeException("VarLong too big");
/*    */       }
/*    */     }
/* 36 */     while (hasContinuationBit(in));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return out;
/*    */   }
/*    */   
/*    */   public static ByteBuf write(ByteBuf output, long value) {
/*    */     while (true) {
/* 46 */       if ((value & 0xFFFFFFFFFFFFFF80L) == 0L) {
/* 47 */         output.writeByte((int)value);
/* 48 */         return output;
/*    */       } 
/*    */       
/* 51 */       output.writeByte((int)(value & 0x7FL) | 0x80);
/* 52 */       value >>>= 7L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/VarLong.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */