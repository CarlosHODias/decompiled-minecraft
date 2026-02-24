/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.MacosUtil;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.jtracy.Plot;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.stream.IntStream;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL13;
/*     */ import org.lwjgl.opengl.GL14;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.GL20;
/*     */ import org.lwjgl.opengl.GL20C;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GL32;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlStateManager
/*     */ {
/*  26 */   private static final Plot PLOT_TEXTURES = TracyClient.createPlot("GPU Textures");
/*  27 */   private static int numTextures = 0;
/*  28 */   private static final Plot PLOT_BUFFERS = TracyClient.createPlot("GPU Buffers");
/*  29 */   private static int numBuffers = 0;
/*     */   
/*  31 */   private static final BlendState BLEND = new BlendState();
/*  32 */   private static final DepthState DEPTH = new DepthState();
/*  33 */   private static final CullState CULL = new CullState();
/*  34 */   private static final PolygonOffsetState POLY_OFFSET = new PolygonOffsetState();
/*  35 */   private static final ColorLogicState COLOR_LOGIC = new ColorLogicState();
/*  36 */   private static final ScissorState SCISSOR = new ScissorState(); private static int activeTexture;
/*     */   private static final int TEXTURE_COUNT = 12;
/*     */   private static final TextureState[] TEXTURES;
/*     */   
/*     */   static {
/*  41 */     TEXTURES = (TextureState[])IntStream.range(0, 12).mapToObj(i -> new TextureState()).toArray(x$0 -> new TextureState[x$0]);
/*     */   }
/*  43 */   private static final ColorMask COLOR_MASK = new ColorMask();
/*     */   private static int readFbo;
/*     */   private static int writeFbo;
/*     */   
/*     */   public static void _disableScissorTest() {
/*  48 */     RenderSystem.assertOnRenderThread();
/*  49 */     SCISSOR.mode.disable();
/*     */   }
/*     */   
/*     */   public static void _enableScissorTest() {
/*  53 */     RenderSystem.assertOnRenderThread();
/*  54 */     SCISSOR.mode.enable();
/*     */   }
/*     */   
/*     */   public static void _scissorBox(int x, int y, int width, int height) {
/*  58 */     RenderSystem.assertOnRenderThread();
/*  59 */     GL20.glScissor(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void _disableDepthTest() {
/*  63 */     RenderSystem.assertOnRenderThread();
/*  64 */     DEPTH.mode.disable();
/*     */   }
/*     */   
/*     */   public static void _enableDepthTest() {
/*  68 */     RenderSystem.assertOnRenderThread();
/*  69 */     DEPTH.mode.enable();
/*     */   }
/*     */   
/*     */   public static void _depthFunc(int func) {
/*  73 */     RenderSystem.assertOnRenderThread();
/*  74 */     if (func != DEPTH.func) {
/*  75 */       DEPTH.func = func;
/*  76 */       GL11.glDepthFunc(func);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _depthMask(boolean mask) {
/*  81 */     RenderSystem.assertOnRenderThread();
/*  82 */     if (mask != DEPTH.mask) {
/*  83 */       DEPTH.mask = mask;
/*  84 */       GL11.glDepthMask(mask);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _disableBlend() {
/*  89 */     RenderSystem.assertOnRenderThread();
/*  90 */     BLEND.mode.disable();
/*     */   }
/*     */   
/*     */   public static void _enableBlend() {
/*  94 */     RenderSystem.assertOnRenderThread();
/*  95 */     BLEND.mode.enable();
/*     */   }
/*     */   
/*     */   public static void _blendFuncSeparate(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha) {
/*  99 */     RenderSystem.assertOnRenderThread();
/* 100 */     if (srcRgb != BLEND.srcRgb || dstRgb != BLEND.dstRgb || srcAlpha != BLEND.srcAlpha || dstAlpha != BLEND.dstAlpha) {
/* 101 */       BLEND.srcRgb = srcRgb;
/* 102 */       BLEND.dstRgb = dstRgb;
/* 103 */       BLEND.srcAlpha = srcAlpha;
/* 104 */       BLEND.dstAlpha = dstAlpha;
/* 105 */       glBlendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int glGetProgrami(int program, int pname) {
/* 110 */     RenderSystem.assertOnRenderThread();
/* 111 */     return GL20.glGetProgrami(program, pname);
/*     */   }
/*     */   
/*     */   public static void glAttachShader(int program, int shader) {
/* 115 */     RenderSystem.assertOnRenderThread();
/* 116 */     GL20.glAttachShader(program, shader);
/*     */   }
/*     */   
/*     */   public static void glDeleteShader(int shader) {
/* 120 */     RenderSystem.assertOnRenderThread();
/* 121 */     GL20.glDeleteShader(shader);
/*     */   }
/*     */   
/*     */   public static int glCreateShader(int type) {
/* 125 */     RenderSystem.assertOnRenderThread();
/* 126 */     return GL20.glCreateShader(type);
/*     */   }
/*     */   
/*     */   public static void glShaderSource(int shader, String source) {
/* 130 */     RenderSystem.assertOnRenderThread();
/*     */ 
/*     */ 
/*     */     
/* 134 */     byte[] encoded = source.getBytes(StandardCharsets.UTF_8);
/* 135 */     ByteBuffer buffer = MemoryUtil.memAlloc(encoded.length + 1);
/* 136 */     buffer.put(encoded);
/* 137 */     buffer.put((byte)0);
/* 138 */     buffer.flip();
/*     */     
/* 140 */     try { MemoryStack stack = MemoryStack.stackPush(); 
/* 141 */       try { PointerBuffer pointers = stack.mallocPointer(1);
/* 142 */         pointers.put(buffer);
/* 143 */         GL20C.nglShaderSource(shader, 1, pointers.address0(), 0L);
/* 144 */         if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/* 145 */           try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } finally { MemoryUtil.memFree(buffer); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void glCompileShader(int shader) {
/* 150 */     RenderSystem.assertOnRenderThread();
/* 151 */     GL20.glCompileShader(shader);
/*     */   }
/*     */   
/*     */   public static int glGetShaderi(int shader, int pname) {
/* 155 */     RenderSystem.assertOnRenderThread();
/* 156 */     return GL20.glGetShaderi(shader, pname);
/*     */   }
/*     */   
/*     */   public static void _glUseProgram(int program) {
/* 160 */     RenderSystem.assertOnRenderThread();
/* 161 */     GL20.glUseProgram(program);
/*     */   }
/*     */   
/*     */   public static int glCreateProgram() {
/* 165 */     RenderSystem.assertOnRenderThread();
/* 166 */     return GL20.glCreateProgram();
/*     */   }
/*     */   
/*     */   public static void glDeleteProgram(int program) {
/* 170 */     RenderSystem.assertOnRenderThread();
/* 171 */     GL20.glDeleteProgram(program);
/*     */   }
/*     */   
/*     */   public static void glLinkProgram(int program) {
/* 175 */     RenderSystem.assertOnRenderThread();
/* 176 */     GL20.glLinkProgram(program);
/*     */   }
/*     */   
/*     */   public static int _glGetUniformLocation(int program, CharSequence name) {
/* 180 */     RenderSystem.assertOnRenderThread();
/* 181 */     return GL20.glGetUniformLocation(program, name);
/*     */   }
/*     */   
/*     */   public static void _glUniform1i(int location, int v0) {
/* 185 */     RenderSystem.assertOnRenderThread();
/* 186 */     GL20.glUniform1i(location, v0);
/*     */   }
/*     */   
/*     */   public static void _glBindAttribLocation(int program, int location, CharSequence name) {
/* 190 */     RenderSystem.assertOnRenderThread();
/* 191 */     GL20.glBindAttribLocation(program, location, name);
/*     */   }
/*     */   
/*     */   static void incrementTrackedBuffers() {
/* 195 */     numBuffers++;
/* 196 */     PLOT_BUFFERS.setValue(numBuffers);
/*     */   }
/*     */   
/*     */   public static int _glGenBuffers() {
/* 200 */     RenderSystem.assertOnRenderThread();
/* 201 */     incrementTrackedBuffers();
/* 202 */     return GL15.glGenBuffers();
/*     */   }
/*     */   
/*     */   public static int _glGenVertexArrays() {
/* 206 */     RenderSystem.assertOnRenderThread();
/* 207 */     return GL30.glGenVertexArrays();
/*     */   }
/*     */   
/*     */   public static void _glBindBuffer(int target, int buffer) {
/* 211 */     RenderSystem.assertOnRenderThread();
/* 212 */     GL15.glBindBuffer(target, buffer);
/*     */   }
/*     */   
/*     */   public static void _glBindVertexArray(int arrayId) {
/* 216 */     RenderSystem.assertOnRenderThread();
/* 217 */     GL30.glBindVertexArray(arrayId);
/*     */   }
/*     */   
/*     */   public static void _glBufferData(int target, ByteBuffer data, int usage) {
/* 221 */     RenderSystem.assertOnRenderThread();
/* 222 */     GL15.glBufferData(target, data, usage);
/*     */   }
/*     */   
/*     */   public static void _glBufferSubData(int target, long offset, ByteBuffer data) {
/* 226 */     RenderSystem.assertOnRenderThread();
/* 227 */     GL15.glBufferSubData(target, offset, data);
/*     */   }
/*     */   
/*     */   public static void _glBufferData(int target, long size, int usage) {
/* 231 */     RenderSystem.assertOnRenderThread();
/* 232 */     GL15.glBufferData(target, size, usage);
/*     */   }
/*     */   
/*     */   public static ByteBuffer _glMapBufferRange(int target, long offset, long length, int access) {
/* 236 */     RenderSystem.assertOnRenderThread();
/* 237 */     return GL30.glMapBufferRange(target, offset, length, access);
/*     */   }
/*     */   
/*     */   public static void _glUnmapBuffer(int target) {
/* 241 */     RenderSystem.assertOnRenderThread();
/* 242 */     GL15.glUnmapBuffer(target);
/*     */   }
/*     */   
/*     */   public static void _glDeleteBuffers(int buffer) {
/* 246 */     RenderSystem.assertOnRenderThread();
/* 247 */     numBuffers--;
/* 248 */     PLOT_BUFFERS.setValue(numBuffers);
/* 249 */     GL15.glDeleteBuffers(buffer);
/*     */   }
/*     */   
/*     */   public static void _glBindFramebuffer(int target, int framebuffer) {
/* 253 */     if ((target == 36008 || target == 36160) && readFbo != framebuffer) {
/* 254 */       GL30.glBindFramebuffer(36008, framebuffer);
/* 255 */       readFbo = framebuffer;
/*     */     } 
/* 257 */     if ((target == 36009 || target == 36160) && writeFbo != framebuffer) {
/* 258 */       GL30.glBindFramebuffer(36009, framebuffer);
/* 259 */       writeFbo = framebuffer;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getFrameBuffer(int target) {
/* 264 */     if (target == 36008) {
/* 265 */       return readFbo;
/*     */     }
/* 267 */     if (target == 36009) {
/* 268 */       return writeFbo;
/*     */     }
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */   public static void _glBlitFrameBuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 274 */     RenderSystem.assertOnRenderThread();
/* 275 */     GL30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */   }
/*     */   
/*     */   public static void _glDeleteFramebuffers(int framebuffer) {
/* 279 */     RenderSystem.assertOnRenderThread();
/* 280 */     GL30.glDeleteFramebuffers(framebuffer);
/* 281 */     if (readFbo == framebuffer) {
/* 282 */       readFbo = 0;
/*     */     }
/* 284 */     if (writeFbo == framebuffer) {
/* 285 */       writeFbo = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glGenFramebuffers() {
/* 290 */     RenderSystem.assertOnRenderThread();
/*     */     
/* 292 */     return GL30.glGenFramebuffers();
/*     */   }
/*     */   
/*     */   public static void _glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/* 296 */     RenderSystem.assertOnRenderThread();
/*     */     
/* 298 */     GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */   }
/*     */   
/*     */   public static void glBlendFuncSeparate(int srcColor, int dstColor, int srcAlpha, int dstAlpha) {
/* 302 */     RenderSystem.assertOnRenderThread();
/* 303 */     GL14.glBlendFuncSeparate(srcColor, dstColor, srcAlpha, dstAlpha);
/*     */   }
/*     */   
/*     */   public static String glGetShaderInfoLog(int shader, int maxLength) {
/* 307 */     RenderSystem.assertOnRenderThread();
/* 308 */     return GL20.glGetShaderInfoLog(shader, maxLength);
/*     */   }
/*     */   
/*     */   public static String glGetProgramInfoLog(int program, int maxLength) {
/* 312 */     RenderSystem.assertOnRenderThread();
/* 313 */     return GL20.glGetProgramInfoLog(program, maxLength);
/*     */   }
/*     */   
/*     */   public static void _enableCull() {
/* 317 */     RenderSystem.assertOnRenderThread();
/* 318 */     CULL.enable.enable();
/*     */   }
/*     */   
/*     */   public static void _disableCull() {
/* 322 */     RenderSystem.assertOnRenderThread();
/* 323 */     CULL.enable.disable();
/*     */   }
/*     */   
/*     */   public static void _polygonMode(int face, int mode) {
/* 327 */     RenderSystem.assertOnRenderThread();
/* 328 */     GL11.glPolygonMode(face, mode);
/*     */   }
/*     */   
/*     */   public static void _enablePolygonOffset() {
/* 332 */     RenderSystem.assertOnRenderThread();
/* 333 */     POLY_OFFSET.fill.enable();
/*     */   }
/*     */   
/*     */   public static void _disablePolygonOffset() {
/* 337 */     RenderSystem.assertOnRenderThread();
/* 338 */     POLY_OFFSET.fill.disable();
/*     */   }
/*     */   
/*     */   public static void _polygonOffset(float factor, float units) {
/* 342 */     RenderSystem.assertOnRenderThread();
/* 343 */     if (factor != POLY_OFFSET.factor || units != POLY_OFFSET.units) {
/* 344 */       POLY_OFFSET.factor = factor;
/* 345 */       POLY_OFFSET.units = units;
/* 346 */       GL11.glPolygonOffset(factor, units);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _enableColorLogicOp() {
/* 351 */     RenderSystem.assertOnRenderThread();
/* 352 */     COLOR_LOGIC.enable.enable();
/*     */   }
/*     */   
/*     */   public static void _disableColorLogicOp() {
/* 356 */     RenderSystem.assertOnRenderThread();
/* 357 */     COLOR_LOGIC.enable.disable();
/*     */   }
/*     */   
/*     */   public static void _logicOp(int op) {
/* 361 */     RenderSystem.assertOnRenderThread();
/* 362 */     if (op != COLOR_LOGIC.op) {
/* 363 */       COLOR_LOGIC.op = op;
/* 364 */       GL11.glLogicOp(op);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _activeTexture(int texture) {
/* 369 */     RenderSystem.assertOnRenderThread();
/* 370 */     if (activeTexture != texture - 33984) {
/* 371 */       activeTexture = texture - 33984;
/* 372 */       GL13.glActiveTexture(texture);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _texParameter(int target, int name, int value) {
/* 377 */     RenderSystem.assertOnRenderThread();
/* 378 */     GL11.glTexParameteri(target, name, value);
/*     */   }
/*     */   
/*     */   public static int _getTexLevelParameter(int target, int level, int name) {
/* 382 */     return GL11.glGetTexLevelParameteri(target, level, name);
/*     */   }
/*     */   
/*     */   public static int _genTexture() {
/* 386 */     RenderSystem.assertOnRenderThread();
/* 387 */     numTextures++;
/* 388 */     PLOT_TEXTURES.setValue(numTextures);
/* 389 */     return GL11.glGenTextures();
/*     */   }
/*     */   
/*     */   public static void _deleteTexture(int id) {
/* 393 */     RenderSystem.assertOnRenderThread();
/* 394 */     GL11.glDeleteTextures(id);
/* 395 */     for (TextureState state : TEXTURES) {
/* 396 */       if (state.binding == id) {
/* 397 */         state.binding = -1;
/*     */       }
/*     */     } 
/* 400 */     numTextures--;
/* 401 */     PLOT_TEXTURES.setValue(numTextures);
/*     */   }
/*     */   
/*     */   public static void _bindTexture(int id) {
/* 405 */     RenderSystem.assertOnRenderThread();
/* 406 */     if (id != (TEXTURES[activeTexture]).binding) {
/* 407 */       (TEXTURES[activeTexture]).binding = id;
/* 408 */       GL11.glBindTexture(3553, id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _texImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
/* 413 */     RenderSystem.assertOnRenderThread();
/* 414 */     GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static void _texSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels) {
/* 418 */     RenderSystem.assertOnRenderThread();
/* 419 */     GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static void _texSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
/* 423 */     RenderSystem.assertOnRenderThread();
/* 424 */     GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static void _viewport(int x, int y, int width, int height) {
/* 428 */     GL11.glViewport(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void _colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
/* 432 */     RenderSystem.assertOnRenderThread();
/* 433 */     if (red != COLOR_MASK.red || green != COLOR_MASK.green || blue != COLOR_MASK.blue || alpha != COLOR_MASK.alpha) {
/* 434 */       COLOR_MASK.red = red;
/* 435 */       COLOR_MASK.green = green;
/* 436 */       COLOR_MASK.blue = blue;
/* 437 */       COLOR_MASK.alpha = alpha;
/* 438 */       GL11.glColorMask(red, green, blue, alpha);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _clear(int mask) {
/* 443 */     RenderSystem.assertOnRenderThread();
/* 444 */     GL11.glClear(mask);
/*     */     
/* 446 */     if (MacosUtil.IS_MACOS) {
/* 447 */       _getError();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void _vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long value) {
/* 452 */     RenderSystem.assertOnRenderThread();
/* 453 */     GL20.glVertexAttribPointer(index, size, type, normalized, stride, value);
/*     */   }
/*     */   
/*     */   public static void _vertexAttribIPointer(int index, int size, int type, int stride, long value) {
/* 457 */     RenderSystem.assertOnRenderThread();
/* 458 */     GL30.glVertexAttribIPointer(index, size, type, stride, value);
/*     */   }
/*     */   
/*     */   public static void _enableVertexAttribArray(int index) {
/* 462 */     RenderSystem.assertOnRenderThread();
/* 463 */     GL20.glEnableVertexAttribArray(index);
/*     */   }
/*     */   
/*     */   public static void _drawElements(int mode, int count, int type, long indices) {
/* 467 */     RenderSystem.assertOnRenderThread();
/* 468 */     GL11.glDrawElements(mode, count, type, indices);
/*     */   }
/*     */   
/*     */   public static void _drawArrays(int mode, int first, int count) {
/* 472 */     RenderSystem.assertOnRenderThread();
/* 473 */     GL11.glDrawArrays(mode, first, count);
/*     */   }
/*     */   
/*     */   public static void _pixelStore(int name, int value) {
/* 477 */     RenderSystem.assertOnRenderThread();
/* 478 */     GL11.glPixelStorei(name, value);
/*     */   }
/*     */   
/*     */   public static void _readPixels(int x, int y, int width, int height, int format, int type, long pixels) {
/* 482 */     RenderSystem.assertOnRenderThread();
/* 483 */     GL11.glReadPixels(x, y, width, height, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static int _getError() {
/* 487 */     RenderSystem.assertOnRenderThread();
/* 488 */     return GL11.glGetError();
/*     */   }
/*     */   
/*     */   public static void clearGlErrors() {
/* 492 */     RenderSystem.assertOnRenderThread();
/*     */     
/* 494 */     while (GL11.glGetError() != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String _getString(int id) {
/* 499 */     RenderSystem.assertOnRenderThread();
/* 500 */     return GL11.glGetString(id);
/*     */   }
/*     */   
/*     */   public static int _getInteger(int name) {
/* 504 */     RenderSystem.assertOnRenderThread();
/* 505 */     return GL11.glGetInteger(name);
/*     */   }
/*     */   
/*     */   public static long _glFenceSync(int condition, int flags) {
/* 509 */     RenderSystem.assertOnRenderThread();
/* 510 */     return GL32.glFenceSync(condition, flags);
/*     */   }
/*     */   
/*     */   public static int _glClientWaitSync(long sync, int flags, long timeout) {
/* 514 */     RenderSystem.assertOnRenderThread();
/* 515 */     return GL32.glClientWaitSync(sync, flags, timeout);
/*     */   }
/*     */   
/*     */   public static void _glDeleteSync(long sync) {
/* 519 */     RenderSystem.assertOnRenderThread();
/* 520 */     GL32.glDeleteSync(sync);
/*     */   }
/*     */   
/*     */   private static class TextureState
/*     */   {
/*     */     public int binding;
/*     */   }
/*     */   
/*     */   private static class BlendState {
/* 529 */     public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(3042);
/* 530 */     public int srcRgb = 1;
/* 531 */     public int dstRgb = 0;
/* 532 */     public int srcAlpha = 1;
/* 533 */     public int dstAlpha = 0;
/*     */   }
/*     */   
/*     */   private static class DepthState {
/* 537 */     public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(2929);
/*     */     public boolean mask = true;
/* 539 */     public int func = 513;
/*     */   }
/*     */   
/*     */   private static class CullState {
/* 543 */     public final GlStateManager.BooleanState enable = new GlStateManager.BooleanState(2884);
/*     */   }
/*     */   
/*     */   private static class PolygonOffsetState {
/* 547 */     public final GlStateManager.BooleanState fill = new GlStateManager.BooleanState(32823);
/*     */     public float factor;
/*     */     public float units;
/*     */   }
/*     */   
/*     */   private static class ColorLogicState {
/* 553 */     public final GlStateManager.BooleanState enable = new GlStateManager.BooleanState(3058);
/* 554 */     public int op = 5379;
/*     */   }
/*     */   
/*     */   private static class ScissorState {
/* 558 */     public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(3089);
/*     */   }
/*     */   
/*     */   private static class ColorMask {
/*     */     public boolean red = true;
/*     */     public boolean green = true;
/*     */     public boolean blue = true;
/*     */     public boolean alpha = true;
/*     */   }
/*     */   
/*     */   private static class BooleanState {
/*     */     private final int state;
/*     */     private boolean enabled;
/*     */     
/*     */     public BooleanState(int state) {
/* 573 */       this.state = state;
/*     */     }
/*     */     
/*     */     public void disable() {
/* 577 */       setEnabled(false);
/*     */     }
/*     */     
/*     */     public void enable() {
/* 581 */       setEnabled(true);
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 585 */       RenderSystem.assertOnRenderThread();
/* 586 */       if (enabled != this.enabled) {
/* 587 */         this.enabled = enabled;
/* 588 */         if (enabled) {
/* 589 */           GL11.glEnable(this.state);
/*     */         } else {
/* 591 */           GL11.glDisable(this.state);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlStateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */