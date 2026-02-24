/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer.Usage;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BufferStorage
/*     */ {
/*     */   public static BufferStorage create(GLCapabilities capabilities, Set<String> enabledExtensions) {
/*  16 */     if (capabilities.GL_ARB_buffer_storage && GlDevice.USE_GL_ARB_buffer_storage) {
/*  17 */       enabledExtensions.add("GL_ARB_buffer_storage");
/*  18 */       return new Immutable();
/*     */     } 
/*  20 */     return new Mutable();
/*     */   }
/*     */   
/*     */   public abstract GlBuffer createBuffer(DirectStateAccess paramDirectStateAccess, Supplier<String> paramSupplier, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt, long paramLong);
/*     */   
/*     */   public abstract GlBuffer createBuffer(DirectStateAccess paramDirectStateAccess, Supplier<String> paramSupplier, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt, ByteBuffer paramByteBuffer);
/*     */   
/*     */   public abstract GlBuffer.GlMappedView mapBuffer(DirectStateAccess paramDirectStateAccess, GlBuffer paramGlBuffer, long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   private static class Mutable
/*     */     extends BufferStorage {
/*     */     public GlBuffer createBuffer(DirectStateAccess dsa, Supplier<String> label, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage, long size) {
/*  32 */       int buffer = dsa.createBuffer();
/*  33 */       dsa.bufferData(buffer, size, usage);
/*  34 */       return new GlBuffer(label, dsa, usage, size, buffer, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public GlBuffer createBuffer(DirectStateAccess dsa, Supplier<String> label, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage, ByteBuffer data) {
/*  39 */       int buffer = dsa.createBuffer();
/*  40 */       int size = data.remaining();
/*  41 */       dsa.bufferData(buffer, data, usage);
/*  42 */       return new GlBuffer(label, dsa, usage, size, buffer, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public GlBuffer.GlMappedView mapBuffer(DirectStateAccess dsa, GlBuffer buffer, long offset, long length, int flags) {
/*  48 */       GlStateManager.clearGlErrors();
/*  49 */       ByteBuffer byteBuffer = dsa.mapBufferRange(buffer.handle, offset, length, flags, buffer.usage());
/*  50 */       if (byteBuffer == null) {
/*  51 */         throw new IllegalStateException("Can't map buffer, opengl error " + GlStateManager._getError());
/*     */       }
/*  53 */       return new GlBuffer.GlMappedView(() -> dsa.unmapBuffer(buffer.handle, buffer.usage()), buffer, byteBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Immutable
/*     */     extends BufferStorage {
/*     */     public GlBuffer createBuffer(DirectStateAccess dsa, Supplier<String> label, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage, long size) {
/*  60 */       int buffer = dsa.createBuffer();
/*  61 */       dsa.bufferStorage(buffer, size, usage);
/*  62 */       ByteBuffer persistentBuffer = tryMapBufferPersistent(dsa, usage, buffer, size);
/*  63 */       return new GlBuffer(label, dsa, usage, size, buffer, persistentBuffer);
/*     */     }
/*     */ 
/*     */     
/*     */     public GlBuffer createBuffer(DirectStateAccess dsa, Supplier<String> label, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage, ByteBuffer data) {
/*  68 */       int buffer = dsa.createBuffer();
/*  69 */       int size = data.remaining();
/*  70 */       dsa.bufferStorage(buffer, data, usage);
/*  71 */       ByteBuffer persistentBuffer = tryMapBufferPersistent(dsa, usage, buffer, size);
/*  72 */       return new GlBuffer(label, dsa, usage, size, buffer, persistentBuffer);
/*     */     }
/*     */     
/*     */     private ByteBuffer tryMapBufferPersistent(DirectStateAccess dsa, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage, int buffer, long size) {
/*     */       ByteBuffer persistentBuffer;
/*  77 */       int mapFlags = 0;
/*  78 */       if ((usage & 0x1) != 0) {
/*  79 */         mapFlags |= 0x1;
/*     */       }
/*  81 */       if ((usage & 0x2) != 0) {
/*  82 */         mapFlags |= 0x12;
/*     */       }
/*  84 */       if (mapFlags != 0) {
/*     */         
/*  86 */         GlStateManager.clearGlErrors();
/*  87 */         persistentBuffer = dsa.mapBufferRange(buffer, 0L, size, mapFlags | 0x40, usage);
/*  88 */         if (persistentBuffer == null) {
/*  89 */           throw new IllegalStateException("Can't persistently map buffer, opengl error " + GlStateManager._getError());
/*     */         }
/*     */       } else {
/*  92 */         persistentBuffer = null;
/*     */       } 
/*  94 */       return persistentBuffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public GlBuffer.GlMappedView mapBuffer(DirectStateAccess dsa, GlBuffer buffer, long offset, long length, int flags) {
/*  99 */       if (buffer.persistentBuffer == null)
/*     */       {
/* 101 */         throw new IllegalStateException("Somehow trying to map an unmappable buffer");
/*     */       }
/* 103 */       if (offset > 2147483647L || length > 2147483647L) {
/* 104 */         throw new IllegalArgumentException("Mapping buffers larger than 2GB is not supported");
/*     */       }
/* 106 */       if (offset < 0L || length < 0L) {
/* 107 */         throw new IllegalArgumentException("Offset or length must be positive integer values");
/*     */       }
/* 109 */       return new GlBuffer.GlMappedView(() -> { if ((flags & 0x2) != 0) dsa.flushMappedBufferRange(buffer.handle, offset, length, buffer.usage());  }, buffer, 
/*     */ 
/*     */ 
/*     */           
/* 113 */           MemoryUtil.memSlice(buffer.persistentBuffer, (int)offset, (int)length));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/BufferStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */