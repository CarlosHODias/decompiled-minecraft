/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryReserve
/*    */ {
/*    */   private static byte[] reserve;
/*    */   
/*    */   public static void allocate() {
/* 10 */     reserve = new byte[10485760];
/*    */   }
/*    */   
/*    */   public static void release() {
/* 14 */     if (reserve != null) {
/* 15 */       reserve = null;
/*    */       try {
/* 17 */         System.gc();
/* 18 */         System.gc();
/* 19 */         System.gc();
/* 20 */       } catch (Throwable throwable) {}
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/MemoryReserve.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */