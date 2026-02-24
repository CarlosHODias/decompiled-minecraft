/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LpVec3
/*    */ {
/*    */   private static final int DATA_BITS = 15;
/*    */   private static final int DATA_BITS_MASK = 32767;
/*    */   private static final double MAX_QUANTIZED_VALUE = 32766.0D;
/*    */   private static final int SCALE_BITS = 2;
/*    */   private static final int SCALE_BITS_MASK = 3;
/*    */   private static final int CONTINUATION_FLAG = 4;
/*    */   private static final int X_OFFSET = 3;
/*    */   private static final int Y_OFFSET = 18;
/*    */   private static final int Z_OFFSET = 33;
/*    */   public static final double ABS_MAX_VALUE = 1.7179869183E10D;
/*    */   public static final double ABS_MIN_VALUE = 3.051944088384301E-5D;
/*    */   
/*    */   public static boolean hasContinuationBit(int in) {
/* 24 */     return ((in & 0x4) == 4);
/*    */   }
/*    */   
/*    */   public static Vec3 read(ByteBuf input) {
/* 28 */     int lowest = input.readUnsignedByte();
/* 29 */     if (lowest == 0) {
/* 30 */       return Vec3.ZERO;
/*    */     }
/*    */     
/* 33 */     int middle = input.readUnsignedByte();
/* 34 */     long highest = input.readUnsignedInt();
/* 35 */     long buffer = highest << 16L | (middle << 8) | lowest;
/* 36 */     long scale = (lowest & 0x3);
/*    */     
/* 38 */     if (hasContinuationBit(lowest)) {
/* 39 */       scale |= (VarInt.read(input) & 0xFFFFFFFFL) << 2L;
/*    */     }
/* 41 */     return new Vec3(
/* 42 */         unpack(buffer >> 3L) * scale, 
/* 43 */         unpack(buffer >> 18L) * scale, 
/* 44 */         unpack(buffer >> 33L) * scale);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void write(ByteBuf output, Vec3 value) {
/* 49 */     double x = sanitize(value.x);
/* 50 */     double y = sanitize(value.y);
/* 51 */     double z = sanitize(value.z);
/* 52 */     double chessboardLength = Mth.absMax(x, Mth.absMax(y, z));
/* 53 */     if (chessboardLength < 3.051944088384301E-5D) {
/* 54 */       output.writeByte(0);
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     long scale = Mth.ceilLong(chessboardLength);
/* 59 */     boolean isPartial = ((scale & 0x3L) != scale);
/* 60 */     long markers = isPartial ? (scale & 0x3L | 0x4L) : scale;
/* 61 */     long xn = pack(x / scale) << 3L;
/* 62 */     long yn = pack(y / scale) << 18L;
/* 63 */     long zn = pack(z / scale) << 33L;
/*    */     
/* 65 */     long buffer = markers | xn | yn | zn;
/* 66 */     output.writeByte((byte)(int)buffer);
/* 67 */     output.writeByte((byte)(int)(buffer >> 8L));
/* 68 */     output.writeInt((int)(buffer >> 16L));
/*    */     
/* 70 */     if (isPartial) {
/* 71 */       VarInt.write(output, (int)(scale >> 2L));
/*    */     }
/*    */   }
/*    */   
/*    */   private static double sanitize(double value) {
/* 76 */     return Double.isNaN(value) ? 0.0D : Math.clamp(value, -1.7179869183E10D, 1.7179869183E10D);
/*    */   }
/*    */   
/*    */   private static long pack(double value) {
/* 80 */     return Math.round((value * 0.5D + 0.5D) * 32766.0D);
/*    */   }
/*    */   
/*    */   private static double unpack(long value) {
/* 84 */     return Math.min((value & 0x7FFFL), 32766.0D) * 2.0D / 32766.0D - 1.0D;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/LpVec3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */