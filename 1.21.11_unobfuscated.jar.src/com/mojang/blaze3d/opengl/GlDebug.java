/*     */ package com.mojang.blaze3d.opengl;
/*     */ import com.google.common.collect.EvictingQueue;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.DebugMemoryUntracker;
/*     */ import com.mojang.blaze3d.platform.GLX;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.opengl.ARBDebugOutput;
/*     */ import org.lwjgl.opengl.GL;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.lwjgl.opengl.GLDebugMessageARBCallback;
/*     */ import org.lwjgl.opengl.GLDebugMessageARBCallbackI;
/*     */ import org.lwjgl.opengl.GLDebugMessageCallback;
/*     */ import org.lwjgl.opengl.GLDebugMessageCallbackI;
/*     */ import org.lwjgl.opengl.KHRDebug;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GlDebug {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int CIRCULAR_LOG_SIZE = 10;
/*     */   
/*     */   private static String printUnknownToken(int token) {
/*  28 */     return "Unknown (0x" + HexFormat.of().withUpperCase().toHexDigits(token) + ")";
/*     */   }
/*     */   
/*     */   public static String sourceToString(int source) {
/*  32 */     switch (source) {
/*     */       case 33350:
/*  34 */         return "API";
/*     */       case 33351:
/*  36 */         return "WINDOW SYSTEM";
/*     */       case 33352:
/*  38 */         return "SHADER COMPILER";
/*     */       case 33353:
/*  40 */         return "THIRD PARTY";
/*     */       case 33354:
/*  42 */         return "APPLICATION";
/*     */       case 33355:
/*  44 */         return "OTHER";
/*     */     } 
/*  46 */     return printUnknownToken(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String typeToString(int type) {
/*  51 */     switch (type) {
/*     */       case 33356:
/*  53 */         return "ERROR";
/*     */       case 33357:
/*  55 */         return "DEPRECATED BEHAVIOR";
/*     */       case 33358:
/*  57 */         return "UNDEFINED BEHAVIOR";
/*     */       case 33359:
/*  59 */         return "PORTABILITY";
/*     */       case 33360:
/*  61 */         return "PERFORMANCE";
/*     */       case 33361:
/*  63 */         return "OTHER";
/*     */       case 33384:
/*  65 */         return "MARKER";
/*     */     } 
/*  67 */     return printUnknownToken(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String severityToString(int severity) {
/*  72 */     switch (severity) {
/*     */       case 37190:
/*  74 */         return "HIGH";
/*     */       case 37191:
/*  76 */         return "MEDIUM";
/*     */       case 37192:
/*  78 */         return "LOW";
/*     */       case 33387:
/*  80 */         return "NOTIFICATION";
/*     */     } 
/*  82 */     return printUnknownToken(severity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  87 */   private final Queue<LogEntry> MESSAGE_BUFFER = (Queue<LogEntry>)EvictingQueue.create(10); private volatile LogEntry lastEntry;
/*     */   
/*     */   private void printDebugLog(int source, int type, int id, int severity, int length, long message, long userParam) {
/*     */     LogEntry entry;
/*  91 */     String msg = GLDebugMessageCallback.getMessage(length, message);
/*     */ 
/*     */     
/*  94 */     synchronized (this.MESSAGE_BUFFER) {
/*  95 */       entry = this.lastEntry;
/*  96 */       if (entry == null || !entry.isSame(source, type, id, severity, msg)) {
/*  97 */         entry = new LogEntry(source, type, id, severity, msg);
/*  98 */         this.MESSAGE_BUFFER.add(entry);
/*  99 */         this.lastEntry = entry;
/*     */       } else {
/* 101 */         entry.count++;
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     LOGGER.info("OpenGL debug message: {}", entry);
/*     */   }
/*     */   
/*     */   public List<String> getLastOpenGlDebugMessages() {
/* 109 */     synchronized (this.MESSAGE_BUFFER) {
/* 110 */       List<String> result = Lists.newArrayListWithCapacity(this.MESSAGE_BUFFER.size());
/* 111 */       for (LogEntry e : this.MESSAGE_BUFFER) {
/* 112 */         result.add(String.valueOf(e) + " x " + String.valueOf(e));
/*     */       }
/* 114 */       return result;
/*     */     } 
/*     */   }
/*     */   
/* 118 */   private static final List<Integer> DEBUG_LEVELS = (List<Integer>)ImmutableList.of(37190, 37191, 37192, 33387);
/* 119 */   private static final List<Integer> DEBUG_LEVELS_ARB = (List<Integer>)ImmutableList.of(37190, 37191, 37192);
/*     */   
/*     */   public static GlDebug enableDebugCallback(int verbosity, boolean debugSynchronousGlLogs, Set<String> enabledExtensions) {
/* 122 */     if (verbosity <= 0) {
/* 123 */       return null;
/*     */     }
/*     */     
/* 126 */     GLCapabilities caps = GL.getCapabilities();
/* 127 */     if (caps.GL_KHR_debug && GlDevice.USE_GL_KHR_debug) {
/* 128 */       GlDebug debug = new GlDebug();
/* 129 */       enabledExtensions.add("GL_KHR_debug");
/* 130 */       GL11.glEnable(37600);
/* 131 */       if (debugSynchronousGlLogs) {
/* 132 */         GL11.glEnable(33346);
/*     */       }
/* 134 */       for (int i = 0; i < DEBUG_LEVELS.size(); i++) {
/* 135 */         boolean isEnabled = (i < verbosity);
/* 136 */         KHRDebug.glDebugMessageControl(4352, 4352, (Integer)DEBUG_LEVELS.get(i), null, isEnabled);
/*     */       } 
/* 138 */       Objects.requireNonNull(debug); KHRDebug.glDebugMessageCallback((GLDebugMessageCallbackI)GLX.make(GLDebugMessageCallback.create(debug::printDebugLog), DebugMemoryUntracker::untrack), 0L);
/* 139 */       return debug;
/* 140 */     }  if (caps.GL_ARB_debug_output && GlDevice.USE_GL_ARB_debug_output) {
/* 141 */       GlDebug debug = new GlDebug();
/* 142 */       enabledExtensions.add("GL_ARB_debug_output");
/* 143 */       if (debugSynchronousGlLogs) {
/* 144 */         GL11.glEnable(33346);
/*     */       }
/* 146 */       for (int i = 0; i < DEBUG_LEVELS_ARB.size(); i++) {
/* 147 */         boolean isEnabled = (i < verbosity);
/* 148 */         ARBDebugOutput.glDebugMessageControlARB(4352, 4352, (Integer)DEBUG_LEVELS_ARB.get(i), null, isEnabled);
/*     */       } 
/* 150 */       Objects.requireNonNull(debug); ARBDebugOutput.glDebugMessageCallbackARB((GLDebugMessageARBCallbackI)GLX.make(GLDebugMessageARBCallback.create(debug::printDebugLog), DebugMemoryUntracker::untrack), 0L);
/* 151 */       return debug;
/*     */     } 
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   private static class LogEntry
/*     */   {
/*     */     private final int id;
/*     */     private final int source;
/*     */     private final int type;
/*     */     private final int severity;
/*     */     private final String message;
/* 163 */     private int count = 1;
/*     */     
/*     */     private LogEntry(int source, int type, int id, int severity, String message) {
/* 166 */       this.id = id;
/* 167 */       this.source = source;
/* 168 */       this.type = type;
/* 169 */       this.severity = severity;
/* 170 */       this.message = message;
/*     */     }
/*     */     
/*     */     private boolean isSame(int source, int type, int id, int severity, String message) {
/* 174 */       return (type == this.type && source == this.source && id == this.id && severity == this.severity && 
/*     */ 
/*     */ 
/*     */         
/* 178 */         message.equals(this.message));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 183 */       return "id=" + this.id + ", source=" + 
/* 184 */         GlDebug.sourceToString(this.source) + ", type=" + 
/* 185 */         GlDebug.typeToString(this.type) + ", severity=" + 
/* 186 */         GlDebug.severityToString(this.severity) + ", message='" + this.message + "'";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlDebug.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */