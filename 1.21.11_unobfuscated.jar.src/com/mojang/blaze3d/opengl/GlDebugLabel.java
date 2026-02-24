/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.lwjgl.opengl.EXTDebugLabel;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.lwjgl.opengl.KHRDebug;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class GlDebugLabel
/*     */ {
/*  15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   public void applyLabel(GlBuffer buffer) {}
/*     */ 
/*     */   
/*     */   public void applyLabel(GlTexture texture) {}
/*     */ 
/*     */   
/*     */   public void applyLabel(GlShaderModule shaderModule) {}
/*     */ 
/*     */   
/*     */   public void applyLabel(GlProgram program) {}
/*     */ 
/*     */   
/*     */   public void applyLabel(VertexArrayCache.VertexArray vertexArray) {}
/*     */ 
/*     */   
/*     */   public void pushDebugGroup(Supplier<String> label) {}
/*     */ 
/*     */   
/*     */   public void popDebugGroup() {}
/*     */   
/*     */   public static GlDebugLabel create(GLCapabilities caps, boolean wantsLabels, Set<String> enabledExtensions) {
/*  39 */     if (wantsLabels) {
/*  40 */       if (caps.GL_KHR_debug && GlDevice.USE_GL_KHR_debug) {
/*  41 */         enabledExtensions.add("GL_KHR_debug");
/*  42 */         return new Core();
/*     */       } 
/*  44 */       if (caps.GL_EXT_debug_label && GlDevice.USE_GL_EXT_debug_label) {
/*  45 */         enabledExtensions.add("GL_EXT_debug_label");
/*  46 */         return new Ext();
/*     */       } 
/*  48 */       LOGGER.warn("Debug labels unavailable: neither KHR_debug nor EXT_debug_label are supported");
/*     */     } 
/*  50 */     return new Empty();
/*     */   }
/*     */   
/*     */   public boolean exists() {
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Empty
/*     */     extends GlDebugLabel {}
/*     */   
/*     */   private static class Core
/*     */     extends GlDebugLabel
/*     */   {
/*  64 */     private final int maxLabelLength = GL11.glGetInteger(33512);
/*     */ 
/*     */ 
/*     */     
/*     */     public void applyLabel(GlBuffer buffer) {
/*  69 */       Supplier<String> label = buffer.label;
/*  70 */       if (label != null) {
/*  71 */         KHRDebug.glObjectLabel(33504, buffer.handle, StringUtil.truncateStringIfNecessary(label.get(), this.maxLabelLength, true));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(GlTexture texture) {
/*  77 */       KHRDebug.glObjectLabel(5890, texture.id, StringUtil.truncateStringIfNecessary(texture.getLabel(), this.maxLabelLength, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(GlShaderModule shaderModule) {
/*  82 */       KHRDebug.glObjectLabel(33505, shaderModule.getShaderId(), StringUtil.truncateStringIfNecessary(shaderModule.getDebugLabel(), this.maxLabelLength, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(GlProgram program) {
/*  87 */       KHRDebug.glObjectLabel(33506, program.getProgramId(), StringUtil.truncateStringIfNecessary(program.getDebugLabel(), this.maxLabelLength, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(VertexArrayCache.VertexArray vertexArray) {
/*  92 */       KHRDebug.glObjectLabel(32884, vertexArray.id, StringUtil.truncateStringIfNecessary(vertexArray.format.toString(), this.maxLabelLength, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void pushDebugGroup(Supplier<String> label) {
/*  97 */       KHRDebug.glPushDebugGroup(33354, 0, label.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void popDebugGroup() {
/* 102 */       KHRDebug.glPopDebugGroup();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 107 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Ext
/*     */     extends GlDebugLabel {
/*     */     public void applyLabel(GlBuffer buffer) {
/* 114 */       Supplier<String> label = buffer.label;
/* 115 */       if (label != null) {
/* 116 */         EXTDebugLabel.glLabelObjectEXT(37201, buffer.handle, StringUtil.truncateStringIfNecessary(label.get(), 256, true));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(GlTexture texture) {
/* 122 */       EXTDebugLabel.glLabelObjectEXT(5890, texture.id, StringUtil.truncateStringIfNecessary(texture.getLabel(), 256, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(GlShaderModule shaderModule) {
/* 127 */       EXTDebugLabel.glLabelObjectEXT(35656, shaderModule.getShaderId(), StringUtil.truncateStringIfNecessary(shaderModule.getDebugLabel(), 256, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(GlProgram program) {
/* 132 */       EXTDebugLabel.glLabelObjectEXT(35648, program.getProgramId(), StringUtil.truncateStringIfNecessary(program.getDebugLabel(), 256, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyLabel(VertexArrayCache.VertexArray vertexArray) {
/* 137 */       EXTDebugLabel.glLabelObjectEXT(32884, vertexArray.id, StringUtil.truncateStringIfNecessary(vertexArray.format.toString(), 256, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 142 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlDebugLabel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */