/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.GraphicsWorkarounds;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer.Usage;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.opengl.ARBBufferStorage;
/*     */ import org.lwjgl.opengl.ARBDirectStateAccess;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GL31;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DirectStateAccess
/*     */ {
/*     */   public static DirectStateAccess create(GLCapabilities capabilities, Set<String> enabledExtensions, GraphicsWorkarounds workarounds) {
/*  20 */     if (capabilities.GL_ARB_direct_state_access && GlDevice.USE_GL_ARB_direct_state_access && !workarounds.isGlOnDx12()) {
/*  21 */       enabledExtensions.add("GL_ARB_direct_state_access");
/*  22 */       return new Core();
/*     */     } 
/*  24 */     return new Emulated();
/*     */   }
/*     */   
/*     */   abstract int createBuffer();
/*     */   
/*     */   abstract void bufferData(int paramInt1, long paramLong, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract void bufferData(int paramInt1, ByteBuffer paramByteBuffer, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract void bufferSubData(int paramInt1, long paramLong, ByteBuffer paramByteBuffer, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract void bufferStorage(int paramInt1, long paramLong, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract void bufferStorage(int paramInt1, ByteBuffer paramByteBuffer, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract ByteBuffer mapBufferRange(int paramInt1, long paramLong1, long paramLong2, int paramInt2, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt3);
/*     */   
/*     */   abstract void unmapBuffer(int paramInt1, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract int createFrameBufferObject();
/*     */   
/*     */   abstract void bindFrameBufferTextures(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   abstract void blitFrameBuffers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12);
/*     */   
/*     */   abstract void flushMappedBufferRange(int paramInt1, long paramLong1, long paramLong2, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int paramInt2);
/*     */   
/*     */   abstract void copyBufferSubData(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   private static class Core
/*     */     extends DirectStateAccess
/*     */   {
/*     */     int createBuffer() {
/*  57 */       GlStateManager.incrementTrackedBuffers();
/*  58 */       return ARBDirectStateAccess.glCreateBuffers();
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferData(int buffer, long size, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/*  63 */       ARBDirectStateAccess.glNamedBufferData(buffer, size, GlConst.bufferUsageToGlEnum(usage));
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferData(int buffer, ByteBuffer data, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/*  68 */       ARBDirectStateAccess.glNamedBufferData(buffer, data, GlConst.bufferUsageToGlEnum(usage));
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferSubData(int buffer, long offset, ByteBuffer data, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/*  73 */       ARBDirectStateAccess.glNamedBufferSubData(buffer, offset, data);
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferStorage(int buffer, long size, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/*  78 */       ARBDirectStateAccess.glNamedBufferStorage(buffer, size, GlConst.bufferUsageToGlFlag(usage));
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferStorage(int buffer, ByteBuffer data, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/*  83 */       ARBDirectStateAccess.glNamedBufferStorage(buffer, data, GlConst.bufferUsageToGlFlag(usage));
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuffer mapBufferRange(int buffer, long offset, long length, int flags, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/*  88 */       return ARBDirectStateAccess.glMapNamedBufferRange(buffer, offset, length, flags);
/*     */     }
/*     */ 
/*     */     
/*     */     void unmapBuffer(int buffer, int usage) {
/*  93 */       ARBDirectStateAccess.glUnmapNamedBuffer(buffer);
/*     */     }
/*     */ 
/*     */     
/*     */     public int createFrameBufferObject() {
/*  98 */       return ARBDirectStateAccess.glCreateFramebuffers();
/*     */     }
/*     */ 
/*     */     
/*     */     public void bindFrameBufferTextures(int fbo, int color0, int depth, int mipLevel, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int bindSlot) {
/* 103 */       ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36064, color0, mipLevel);
/* 104 */       ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36096, depth, mipLevel);
/* 105 */       if (bindSlot != 0) {
/* 106 */         GlStateManager._glBindFramebuffer(bindSlot, fbo);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void blitFrameBuffers(int source, int dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 112 */       ARBDirectStateAccess.glBlitNamedFramebuffer(source, dest, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */     }
/*     */ 
/*     */     
/*     */     void flushMappedBufferRange(int handle, long offset, long length, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 117 */       ARBDirectStateAccess.glFlushMappedNamedBufferRange(handle, offset, length);
/*     */     }
/*     */ 
/*     */     
/*     */     void copyBufferSubData(int source, int target, long sourceOffset, long targetOffset, long length) {
/* 122 */       ARBDirectStateAccess.glCopyNamedBufferSubData(source, target, sourceOffset, targetOffset, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Emulated
/*     */     extends DirectStateAccess
/*     */   {
/*     */     private int selectBufferBindTarget(@com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 131 */       if ((usage & 0x20) != 0)
/* 132 */         return 34962; 
/* 133 */       if ((usage & 0x40) != 0)
/* 134 */         return 34963; 
/* 135 */       if ((usage & 0x80) != 0) {
/* 136 */         return 35345;
/*     */       }
/* 138 */       return 36663;
/*     */     }
/*     */ 
/*     */     
/*     */     int createBuffer() {
/* 143 */       return GlStateManager._glGenBuffers();
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferData(int buffer, long size, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 148 */       int target = selectBufferBindTarget(usage);
/* 149 */       GlStateManager._glBindBuffer(target, buffer);
/* 150 */       GlStateManager._glBufferData(target, size, GlConst.bufferUsageToGlEnum(usage));
/* 151 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferData(int buffer, ByteBuffer data, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 156 */       int target = selectBufferBindTarget(usage);
/* 157 */       GlStateManager._glBindBuffer(target, buffer);
/* 158 */       GlStateManager._glBufferData(target, data, GlConst.bufferUsageToGlEnum(usage));
/* 159 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferSubData(int buffer, long offset, ByteBuffer data, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 164 */       int target = selectBufferBindTarget(usage);
/* 165 */       GlStateManager._glBindBuffer(target, buffer);
/* 166 */       GlStateManager._glBufferSubData(target, offset, data);
/* 167 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferStorage(int buffer, long size, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 172 */       int target = selectBufferBindTarget(usage);
/* 173 */       GlStateManager._glBindBuffer(target, buffer);
/* 174 */       ARBBufferStorage.glBufferStorage(target, size, GlConst.bufferUsageToGlFlag(usage));
/* 175 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     void bufferStorage(int buffer, ByteBuffer data, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 180 */       int target = selectBufferBindTarget(usage);
/* 181 */       GlStateManager._glBindBuffer(target, buffer);
/* 182 */       ARBBufferStorage.glBufferStorage(target, data, GlConst.bufferUsageToGlFlag(usage));
/* 183 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuffer mapBufferRange(int buffer, long offset, long length, int access, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 188 */       int target = selectBufferBindTarget(usage);
/* 189 */       GlStateManager._glBindBuffer(target, buffer);
/* 190 */       ByteBuffer byteBuffer = GlStateManager._glMapBufferRange(target, offset, length, access);
/* 191 */       GlStateManager._glBindBuffer(target, 0);
/* 192 */       return byteBuffer;
/*     */     }
/*     */ 
/*     */     
/*     */     void unmapBuffer(int buffer, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 197 */       int target = selectBufferBindTarget(usage);
/* 198 */       GlStateManager._glBindBuffer(target, buffer);
/* 199 */       GlStateManager._glUnmapBuffer(target);
/* 200 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     void flushMappedBufferRange(int buffer, long offset, long length, @com.mojang.blaze3d.buffers.GpuBuffer.Usage int usage) {
/* 205 */       int target = selectBufferBindTarget(usage);
/* 206 */       GlStateManager._glBindBuffer(target, buffer);
/* 207 */       GL30.glFlushMappedBufferRange(target, offset, length);
/* 208 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     void copyBufferSubData(int source, int target, long sourceOffset, long targetOffset, long length) {
/* 213 */       GlStateManager._glBindBuffer(36662, source);
/* 214 */       GlStateManager._glBindBuffer(36663, target);
/* 215 */       GL31.glCopyBufferSubData(36662, 36663, sourceOffset, targetOffset, length);
/* 216 */       GlStateManager._glBindBuffer(36662, 0);
/* 217 */       GlStateManager._glBindBuffer(36663, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int createFrameBufferObject() {
/* 222 */       return GlStateManager.glGenFramebuffers();
/*     */     }
/*     */ 
/*     */     
/*     */     public void bindFrameBufferTextures(int fbo, int color0, int depth, int mipLevel, int bindSlot) {
/* 227 */       int tempBindSlot = (bindSlot == 0) ? 36009 : bindSlot;
/* 228 */       int oldFbo = GlStateManager.getFrameBuffer(tempBindSlot);
/* 229 */       GlStateManager._glBindFramebuffer(tempBindSlot, fbo);
/* 230 */       GlStateManager._glFramebufferTexture2D(tempBindSlot, 36064, 3553, color0, mipLevel);
/* 231 */       GlStateManager._glFramebufferTexture2D(tempBindSlot, 36096, 3553, depth, mipLevel);
/* 232 */       if (bindSlot == 0) {
/* 233 */         GlStateManager._glBindFramebuffer(tempBindSlot, oldFbo);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void blitFrameBuffers(int source, int dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 239 */       int oldRead = GlStateManager.getFrameBuffer(36008);
/* 240 */       int oldDraw = GlStateManager.getFrameBuffer(36009);
/* 241 */       GlStateManager._glBindFramebuffer(36008, source);
/* 242 */       GlStateManager._glBindFramebuffer(36009, dest);
/* 243 */       GlStateManager._glBlitFrameBuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/* 244 */       GlStateManager._glBindFramebuffer(36008, oldRead);
/* 245 */       GlStateManager._glBindFramebuffer(36009, oldDraw);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/DirectStateAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */