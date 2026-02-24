/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.IntConsumer;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class ZeroBitStorage
/*    */   implements BitStorage {
/*  9 */   public static final long[] RAW = new long[0];
/*    */   
/*    */   private final int size;
/*    */   
/*    */   public ZeroBitStorage(int size) {
/* 14 */     this.size = size;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAndSet(int index, int value) {
/* 19 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 20 */     Validate.inclusiveBetween(0L, 0L, value);
/* 21 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int index, int value) {
/* 26 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 27 */     Validate.inclusiveBetween(0L, 0L, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int index) {
/* 32 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public long[] getRaw() {
/* 38 */     return RAW;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 43 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBits() {
/* 48 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void getAll(IntConsumer output) {
/* 53 */     for (int i = 0; i < this.size; i++) {
/* 54 */       output.accept(0);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void unpack(int[] output) {
/* 60 */     Arrays.fill(output, 0, this.size, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public BitStorage copy() {
/* 65 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/ZeroBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */