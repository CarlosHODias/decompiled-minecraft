/*    */ package com.mojang.blaze3d.buffers;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GpuBuffer
/*    */   implements AutoCloseable
/*    */ {
/*    */   public static final int USAGE_MAP_READ = 1;
/*    */   public static final int USAGE_MAP_WRITE = 2;
/*    */   public static final int USAGE_HINT_CLIENT_STORAGE = 4;
/*    */   public static final int USAGE_COPY_DST = 8;
/*    */   public static final int USAGE_COPY_SRC = 16;
/*    */   public static final int USAGE_VERTEX = 32;
/*    */   public static final int USAGE_INDEX = 64;
/*    */   public static final int USAGE_UNIFORM = 128;
/*    */   public static final int USAGE_UNIFORM_TEXEL_BUFFER = 256;
/*    */   @Usage
/*    */   private final int usage;
/*    */   private final long size;
/*    */   
/*    */   public GpuBuffer(@Usage int usage, long size) {
/* 62 */     this.size = size;
/* 63 */     this.usage = usage;
/*    */   }
/*    */   
/*    */   public long size() {
/* 67 */     return this.size;
/*    */   }
/*    */   
/*    */   @Usage
/*    */   public int usage() {
/* 72 */     return this.usage;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract boolean isClosed();
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public GpuBufferSlice slice(long offset, long length) {
/* 81 */     if (offset < 0L || length < 0L || offset + length > this.size) {
/* 82 */       throw new IllegalArgumentException("Offset of " + offset + " and length " + length + " would put new slice outside buffer's range (of 0," + length + ")");
/*    */     }
/* 84 */     return new GpuBufferSlice(this, offset, length);
/*    */   }
/*    */   
/*    */   public GpuBufferSlice slice() {
/* 88 */     return new GpuBufferSlice(this, 0L, this.size);
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
/*    */   public static @interface Usage {}
/*    */   
/*    */   public static interface MappedView extends AutoCloseable {
/*    */     ByteBuffer data();
/*    */     
/*    */     void close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/buffers/GpuBuffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */