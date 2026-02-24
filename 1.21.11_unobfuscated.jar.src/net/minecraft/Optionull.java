/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Optionull
/*    */ {
/*    */   @Deprecated
/*    */   public static <T> T orElse(T t, T defaultValue) {
/* 17 */     return Objects.requireNonNullElse(t, defaultValue);
/*    */   }
/*    */   
/*    */   public static <T, R> R map(T t, Function<T, R> map) {
/* 21 */     return (t == null) ? null : map.apply(t);
/*    */   }
/*    */   
/*    */   public static <T, R> R mapOrDefault(T t, Function<T, R> map, R defaultValue) {
/* 25 */     return (t == null) ? defaultValue : map.apply(t);
/*    */   }
/*    */   
/*    */   public static <T, R> R mapOrElse(T t, Function<T, R> map, Supplier<R> elseSupplier) {
/* 29 */     return (t == null) ? elseSupplier.get() : map.apply(t);
/*    */   }
/*    */   
/*    */   public static <T> T first(Collection<T> collection) {
/* 33 */     Iterator<T> iterator = collection.iterator();
/* 34 */     return iterator.hasNext() ? iterator.next() : null;
/*    */   }
/*    */   
/*    */   public static <T> T firstOrDefault(Collection<T> collection, T defaultValue) {
/* 38 */     Iterator<T> iterator = collection.iterator();
/* 39 */     return iterator.hasNext() ? iterator.next() : defaultValue;
/*    */   }
/*    */   
/*    */   public static <T> T firstOrElse(Collection<T> collection, Supplier<T> elseSupplier) {
/* 43 */     Iterator<T> iterator = collection.iterator();
/* 44 */     return iterator.hasNext() ? iterator.next() : elseSupplier.get();
/*    */   }
/*    */   
/*    */   public static <T> boolean isNullOrEmpty(T[] t) {
/* 48 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(boolean[] t) {
/* 52 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(byte[] t) {
/* 56 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(char[] t) {
/* 60 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(short[] t) {
/* 64 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(int[] t) {
/* 68 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(long[] t) {
/* 72 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(float[] t) {
/* 76 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(double[] t) {
/* 80 */     return (t == null || t.length == 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/Optionull.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */