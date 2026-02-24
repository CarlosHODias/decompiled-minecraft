/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.ShaderManager;
/*     */ import org.lwjgl.opengl.GL31;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GlProgram implements AutoCloseable {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*  24 */   public static Set<String> BUILT_IN_UNIFORMS = Sets.newHashSet((Object[])new String[] { "Projection", "Lighting", "Fog", "Globals" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static GlProgram INVALID_PROGRAM = new GlProgram(-1, "invalid");
/*     */   
/*  33 */   private final Map<String, Uniform> uniformsByName = new HashMap<>();
/*     */   
/*     */   private final int programId;
/*     */   
/*     */   private final String debugLabel;
/*     */   
/*     */   private GlProgram(int programId, String debugLabel) {
/*  40 */     this.programId = programId;
/*  41 */     this.debugLabel = debugLabel;
/*     */   }
/*     */   
/*     */   public static GlProgram link(GlShaderModule vertexShader, GlShaderModule fragmentShader, VertexFormat vertexFormat, String debugLabel) throws ShaderManager.CompilationException {
/*  45 */     int programId = GlStateManager.glCreateProgram();
/*  46 */     if (programId <= 0) {
/*  47 */       throw new ShaderManager.CompilationException("Could not create shader program (returned program ID " + programId + ")");
/*     */     }
/*     */     
/*  50 */     int attributeLocation = 0;
/*  51 */     for (String attributeName : (Iterable<String>)vertexFormat.getElementAttributeNames()) {
/*  52 */       GlStateManager._glBindAttribLocation(programId, attributeLocation, attributeName);
/*  53 */       attributeLocation++;
/*     */     } 
/*     */     
/*  56 */     GlStateManager.glAttachShader(programId, vertexShader.getShaderId());
/*  57 */     GlStateManager.glAttachShader(programId, fragmentShader.getShaderId());
/*  58 */     GlStateManager.glLinkProgram(programId);
/*     */     
/*  60 */     int linkStatus = GlStateManager.glGetProgrami(programId, 35714);
/*  61 */     String linkMessage = GlStateManager.glGetProgramInfoLog(programId, 32768);
/*     */     
/*  63 */     if (linkStatus == 0 || linkMessage.contains("Failed for unknown reason"))
/*  64 */       throw new ShaderManager.CompilationException("Error encountered when linking program containing VS " + String.valueOf(vertexShader.getId()) + " and FS " + String.valueOf(fragmentShader.getId()) + ". Log output: " + linkMessage); 
/*  65 */     if (!linkMessage.isEmpty()) {
/*  66 */       LOGGER.info("Info log when linking program containing VS {} and FS {}. Log output: {}", new Object[] { vertexShader.getId(), fragmentShader.getId(), linkMessage });
/*     */     }
/*     */     
/*  69 */     return new GlProgram(programId, debugLabel);
/*     */   }
/*     */   
/*     */   public void setupUniforms(List<RenderPipeline.UniformDescription> uniforms, List<String> samplers) {
/*  73 */     int nextUboBinding = 0;
/*  74 */     int nextSamplerIndex = 0;
/*     */     
/*  76 */     for (RenderPipeline.UniformDescription uniformDescription : uniforms) {
/*  77 */       int index, location, uboBinding, samplerIndex; String uniformName = uniformDescription.name();
/*  78 */       switch (uniformDescription.type()) { default: throw new MatchException(null, null);
/*     */         case UNIFORM_BUFFER:
/*  80 */           index = GL31.glGetUniformBlockIndex(this.programId, uniformName);
/*  81 */           if (index == -1);
/*     */ 
/*     */           
/*  84 */           uboBinding = nextUboBinding++;
/*  85 */           GL31.glUniformBlockBinding(this.programId, index, uboBinding);
/*     */ 
/*     */         
/*     */         case TEXEL_BUFFER:
/*  89 */           location = GlStateManager._glGetUniformLocation(this.programId, uniformName);
/*  90 */           if (location == -1) {
/*  91 */             LOGGER.warn("{} shader program does not use utb {} defined in the pipeline. This might be a bug.", this.debugLabel, uniformName);
/*     */           }
/*     */           
/*  94 */           samplerIndex = nextSamplerIndex++; }
/*  95 */        Uniform uniform = new Uniform.Utb(location, samplerIndex, Objects.<TextureFormat>requireNonNull(uniformDescription.textureFormat()));
/*     */ 
/*     */       
/*  98 */       if (uniform == null) {
/*     */         continue;
/*     */       }
/* 101 */       this.uniformsByName.put(uniformName, uniform);
/*     */     } 
/*     */     
/* 104 */     for (String sampler : samplers) {
/* 105 */       int location = GlStateManager._glGetUniformLocation(this.programId, sampler);
/* 106 */       if (location == -1) {
/* 107 */         LOGGER.warn("{} shader program does not use sampler {} defined in the pipeline. This might be a bug.", this.debugLabel, sampler);
/*     */         continue;
/*     */       } 
/* 110 */       int samplerIndex = nextSamplerIndex++;
/* 111 */       this.uniformsByName.put(sampler, new Uniform.Sampler(location, samplerIndex));
/*     */     } 
/*     */     
/* 114 */     int totalDefinedBlocks = GlStateManager.glGetProgrami(this.programId, 35382);
/* 115 */     for (int i = 0; i < totalDefinedBlocks; i++) {
/* 116 */       String name = GL31.glGetActiveUniformBlockName(this.programId, i);
/* 117 */       if (!this.uniformsByName.containsKey(name)) {
/* 118 */         if (!samplers.contains(name) && BUILT_IN_UNIFORMS.contains(name)) {
/* 119 */           int uboBinding = nextUboBinding++;
/* 120 */           GL31.glUniformBlockBinding(this.programId, i, uboBinding);
/* 121 */           this.uniformsByName.put(name, new Uniform.Ubo(uboBinding));
/*     */         } else {
/* 123 */           LOGGER.warn("Found unknown and unsupported uniform {} in {}", name, this.debugLabel);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 131 */     this.uniformsByName.values().forEach(Uniform::close);
/* 132 */     GlStateManager.glDeleteProgram(this.programId);
/*     */   }
/*     */   
/*     */   public Uniform getUniform(String name) {
/* 136 */     RenderSystem.assertOnRenderThread();
/* 137 */     return this.uniformsByName.get(name);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getProgramId() {
/* 142 */     return this.programId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return this.debugLabel;
/*     */   }
/*     */   
/*     */   public String getDebugLabel() {
/* 151 */     return this.debugLabel;
/*     */   }
/*     */   
/*     */   public Map<String, Uniform> getUniforms() {
/* 155 */     return this.uniformsByName;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlProgram.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */