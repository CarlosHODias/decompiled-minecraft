/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.jtracy.MemoryPool;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ByteBufferBuilder
/*     */   implements AutoCloseable
/*     */ {
/*  14 */   private static final MemoryPool MEMORY_POOL = TracyClient.createMemoryPool("ByteBufferBuilder");
/*  15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  16 */   private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);
/*     */   
/*     */   private static final long DEFAULT_MAX_CAPACITY = 4294967295L;
/*     */   
/*     */   private static final int MAX_GROWTH_SIZE = 2097152;
/*     */   
/*     */   private static final int BUFFER_FREED_GENERATION = -1;
/*     */   
/*     */   private long pointer;
/*     */   
/*     */   private long capacity;
/*     */   private final long maxCapacity;
/*     */   private long writeOffset;
/*     */   private long nextResultOffset;
/*     */   private int resultCount;
/*     */   private int generation;
/*     */   
/*     */   public ByteBufferBuilder(int initialCapacity, long maxCapacity) {
/*  34 */     this.capacity = initialCapacity;
/*  35 */     this.maxCapacity = maxCapacity;
/*  36 */     this.pointer = ALLOCATOR.malloc(initialCapacity);
/*  37 */     MEMORY_POOL.malloc(this.pointer, initialCapacity);
/*  38 */     if (this.pointer == 0L) {
/*  39 */       throw new OutOfMemoryError("Failed to allocate " + initialCapacity + " bytes");
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBufferBuilder(int initialCapacity) {
/*  44 */     this(initialCapacity, 4294967295L);
/*     */   }
/*     */   
/*     */   public static ByteBufferBuilder exactlySized(int capacity) {
/*  48 */     return new ByteBufferBuilder(capacity, capacity);
/*     */   }
/*     */   
/*     */   public long reserve(int size) {
/*  52 */     long offset = this.writeOffset;
/*  53 */     long nextOffset = Math.addExact(offset, size);
/*  54 */     ensureCapacity(nextOffset);
/*  55 */     this.writeOffset = nextOffset;
/*  56 */     return Math.addExact(this.pointer, offset);
/*     */   }
/*     */   
/*     */   private void ensureCapacity(long requiredCapacity) {
/*  60 */     if (requiredCapacity > this.capacity) {
/*  61 */       if (requiredCapacity > this.maxCapacity) {
/*  62 */         throw new IllegalArgumentException("Maximum capacity of ByteBufferBuilder (" + this.maxCapacity + ") exceeded, required " + requiredCapacity);
/*     */       }
/*  64 */       long preferredGrowth = Math.min(this.capacity, 2097152L);
/*  65 */       long newCapacity = Mth.clamp(this.capacity + preferredGrowth, requiredCapacity, this.maxCapacity);
/*  66 */       resize(newCapacity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resize(long newCapacity) {
/*  71 */     MEMORY_POOL.free(this.pointer);
/*  72 */     this.pointer = ALLOCATOR.realloc(this.pointer, newCapacity);
/*     */     
/*  74 */     MEMORY_POOL.malloc(this.pointer, (int)Math.min(newCapacity, 2147483647L));
/*  75 */     LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", this.capacity, newCapacity);
/*  76 */     if (this.pointer == 0L) {
/*  77 */       throw new OutOfMemoryError("Failed to resize buffer from " + this.capacity + " bytes to " + newCapacity + " bytes");
/*     */     }
/*  79 */     this.capacity = newCapacity;
/*     */   }
/*     */   
/*     */   public Result build() {
/*  83 */     checkOpen();
/*  84 */     long offset = this.nextResultOffset;
/*  85 */     long size = this.writeOffset - offset;
/*  86 */     if (size == 0L)
/*  87 */       return null; 
/*  88 */     if (size > 2147483647L)
/*     */     {
/*  90 */       throw new IllegalStateException("Cannot build buffer larger than 2147483647 bytes (was " + size + ")");
/*     */     }
/*  92 */     this.nextResultOffset = this.writeOffset;
/*  93 */     this.resultCount++;
/*  94 */     return new Result(offset, (int)size, this.generation);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  98 */     if (this.resultCount > 0) {
/*  99 */       LOGGER.warn("Clearing BufferBuilder with unused batches");
/*     */     }
/* 101 */     discard();
/*     */   }
/*     */   
/*     */   public void discard() {
/* 105 */     checkOpen();
/* 106 */     if (this.resultCount > 0) {
/* 107 */       discardResults();
/* 108 */       this.resultCount = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isValid(int generation) {
/* 113 */     return (generation == this.generation);
/*     */   }
/*     */   
/*     */   private void freeResult() {
/* 117 */     if (--this.resultCount <= 0) {
/* 118 */       discardResults();
/*     */     }
/*     */   }
/*     */   
/*     */   private void discardResults() {
/* 123 */     long currentSize = this.writeOffset - this.nextResultOffset;
/*     */     
/* 125 */     if (currentSize > 0L) {
/* 126 */       MemoryUtil.memCopy(this.pointer + this.nextResultOffset, this.pointer, currentSize);
/*     */     }
/* 128 */     this.writeOffset = currentSize;
/* 129 */     this.nextResultOffset = 0L;
/* 130 */     this.generation++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 135 */     if (this.pointer != 0L) {
/* 136 */       MEMORY_POOL.free(this.pointer);
/* 137 */       ALLOCATOR.free(this.pointer);
/* 138 */       this.pointer = 0L;
/* 139 */       this.generation = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkOpen() {
/* 144 */     if (this.pointer == 0L)
/* 145 */       throw new IllegalStateException("Buffer has been freed"); 
/*     */   }
/*     */   
/*     */   public class Result
/*     */     implements AutoCloseable
/*     */   {
/*     */     private final long offset;
/*     */     private final int capacity;
/*     */     private final int generation;
/*     */     private boolean closed;
/*     */     
/*     */     private Result(long offset, int capacity, int generation) {
/* 157 */       this.offset = offset;
/* 158 */       this.capacity = capacity;
/* 159 */       this.generation = generation;
/*     */     }
/*     */     
/*     */     public ByteBuffer byteBuffer() {
/* 163 */       if (!ByteBufferBuilder.this.isValid(this.generation)) {
/* 164 */         throw new IllegalStateException("Buffer is no longer valid");
/*     */       }
/* 166 */       return MemoryUtil.memByteBuffer(ByteBufferBuilder.this.pointer + this.offset, this.capacity);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 171 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 174 */       this.closed = true;
/* 175 */       if (ByteBufferBuilder.this.isValid(this.generation))
/* 176 */         ByteBufferBuilder.this.freeResult(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/ByteBufferBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */