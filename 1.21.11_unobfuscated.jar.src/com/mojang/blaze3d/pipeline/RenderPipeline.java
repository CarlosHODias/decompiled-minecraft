/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.DepthTestFunction;
/*     */ import com.mojang.blaze3d.platform.LogicOp;
/*     */ import com.mojang.blaze3d.platform.PolygonMode;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RenderPipeline
/*     */ {
/*     */   private final Identifier location;
/*     */   private final Identifier vertexShader;
/*     */   private final Identifier fragmentShader;
/*     */   private final ShaderDefines shaderDefines;
/*     */   private final List<String> samplers;
/*     */   private final List<UniformDescription> uniforms;
/*     */   private final DepthTestFunction depthTestFunction;
/*     */   private final PolygonMode polygonMode;
/*     */   private final boolean cull;
/*     */   private final LogicOp colorLogic;
/*     */   private final Optional<BlendFunction> blendFunction;
/*     */   private final boolean writeColor;
/*     */   private final boolean writeAlpha;
/*     */   private final boolean writeDepth;
/*     */   private final VertexFormat vertexFormat;
/*     */   private final VertexFormat.Mode vertexFormatMode;
/*     */   private final float depthBiasScaleFactor;
/*     */   private final float depthBiasConstant;
/*     */   private final int sortKey;
/*     */   private static int sortKeySeed;
/*     */   
/*     */   protected RenderPipeline(Identifier location, Identifier vertexShader, Identifier fragmentShader, ShaderDefines shaderDefines, List<String> samplers, List<UniformDescription> uniforms, Optional<BlendFunction> blendFunction, DepthTestFunction depthTestFunction, PolygonMode polygonMode, boolean cull, boolean writeColor, boolean writeAlpha, boolean writeDepth, LogicOp colorLogic, VertexFormat vertexFormat, VertexFormat.Mode vertexFormatMode, float depthBiasScaleFactor, float depthBiasConstant, int sortKey) {
/*  44 */     this.location = location;
/*  45 */     this.vertexShader = vertexShader;
/*  46 */     this.fragmentShader = fragmentShader;
/*  47 */     this.shaderDefines = shaderDefines;
/*  48 */     this.samplers = samplers;
/*  49 */     this.uniforms = uniforms;
/*  50 */     this.depthTestFunction = depthTestFunction;
/*  51 */     this.polygonMode = polygonMode;
/*  52 */     this.cull = cull;
/*  53 */     this.blendFunction = blendFunction;
/*  54 */     this.writeColor = writeColor;
/*  55 */     this.writeAlpha = writeAlpha;
/*  56 */     this.writeDepth = writeDepth;
/*  57 */     this.colorLogic = colorLogic;
/*  58 */     this.vertexFormat = vertexFormat;
/*  59 */     this.vertexFormatMode = vertexFormatMode;
/*  60 */     this.depthBiasScaleFactor = depthBiasScaleFactor;
/*  61 */     this.depthBiasConstant = depthBiasConstant;
/*  62 */     this.sortKey = sortKey;
/*     */   }
/*     */   
/*     */   public int getSortKey() {
/*  66 */     return SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER ? (hashCode() * (sortKeySeed + 1)) : this.sortKey;
/*     */   }
/*     */   
/*     */   public static void updateSortKeySeed() {
/*  70 */     sortKeySeed = Math.round(100000.0F * (float)Math.random());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  75 */     return this.location.toString();
/*     */   }
/*     */   
/*     */   public DepthTestFunction getDepthTestFunction() {
/*  79 */     return this.depthTestFunction;
/*     */   }
/*     */   
/*     */   public PolygonMode getPolygonMode() {
/*  83 */     return this.polygonMode;
/*     */   }
/*     */   
/*     */   public boolean isCull() {
/*  87 */     return this.cull;
/*     */   }
/*     */   
/*     */   public LogicOp getColorLogic() {
/*  91 */     return this.colorLogic;
/*     */   }
/*     */   
/*     */   public Optional<BlendFunction> getBlendFunction() {
/*  95 */     return this.blendFunction;
/*     */   }
/*     */   
/*     */   public boolean isWriteColor() {
/*  99 */     return this.writeColor;
/*     */   }
/*     */   
/*     */   public boolean isWriteAlpha() {
/* 103 */     return this.writeAlpha;
/*     */   }
/*     */   
/*     */   public boolean isWriteDepth() {
/* 107 */     return this.writeDepth;
/*     */   }
/*     */   
/*     */   public float getDepthBiasScaleFactor() {
/* 111 */     return this.depthBiasScaleFactor;
/*     */   }
/*     */   
/*     */   public float getDepthBiasConstant() {
/* 115 */     return this.depthBiasConstant;
/*     */   }
/*     */   
/*     */   public Identifier getLocation() {
/* 119 */     return this.location;
/*     */   }
/*     */   
/*     */   public VertexFormat getVertexFormat() {
/* 123 */     return this.vertexFormat;
/*     */   }
/*     */   
/*     */   public VertexFormat.Mode getVertexFormatMode() {
/* 127 */     return this.vertexFormatMode;
/*     */   }
/*     */   
/*     */   public Identifier getVertexShader() {
/* 131 */     return this.vertexShader;
/*     */   }
/*     */   
/*     */   public Identifier getFragmentShader() {
/* 135 */     return this.fragmentShader;
/*     */   }
/*     */   
/*     */   public ShaderDefines getShaderDefines() {
/* 139 */     return this.shaderDefines;
/*     */   }
/*     */   
/*     */   public List<String> getSamplers() {
/* 143 */     return this.samplers;
/*     */   }
/*     */   
/*     */   public List<UniformDescription> getUniforms() {
/* 147 */     return this.uniforms;
/*     */   }
/*     */   
/*     */   public boolean wantsDepthTexture() {
/* 151 */     return (this.depthTestFunction != DepthTestFunction.NO_DEPTH_TEST || this.depthBiasConstant != 0.0F || this.depthBiasScaleFactor != 0.0F || this.writeDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder(Snippet... snippets) {
/* 159 */     Builder builder = new Builder();
/*     */     
/* 161 */     for (Snippet snippet : snippets) {
/* 162 */       builder.withSnippet(snippet);
/*     */     }
/* 164 */     return builder;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private static int nextPipelineSortKey;
/* 169 */     private Optional<Identifier> location = Optional.empty();
/* 170 */     private Optional<Identifier> fragmentShader = Optional.empty();
/* 171 */     private Optional<Identifier> vertexShader = Optional.empty();
/* 172 */     private Optional<ShaderDefines.Builder> definesBuilder = Optional.empty();
/* 173 */     private Optional<List<String>> samplers = Optional.empty();
/* 174 */     private Optional<List<RenderPipeline.UniformDescription>> uniforms = Optional.empty();
/* 175 */     private Optional<DepthTestFunction> depthTestFunction = Optional.empty();
/* 176 */     private Optional<PolygonMode> polygonMode = Optional.empty();
/* 177 */     private Optional<Boolean> cull = Optional.empty();
/* 178 */     private Optional<Boolean> writeColor = Optional.empty();
/* 179 */     private Optional<Boolean> writeAlpha = Optional.empty();
/* 180 */     private Optional<Boolean> writeDepth = Optional.empty();
/* 181 */     private Optional<LogicOp> colorLogic = Optional.empty();
/* 182 */     private Optional<BlendFunction> blendFunction = Optional.empty();
/* 183 */     private Optional<VertexFormat> vertexFormat = Optional.empty();
/* 184 */     private Optional<VertexFormat.Mode> vertexFormatMode = Optional.empty();
/*     */     
/*     */     private float depthBiasScaleFactor;
/*     */     
/*     */     private float depthBiasConstant;
/*     */ 
/*     */     
/*     */     public Builder withLocation(String location) {
/* 192 */       this.location = Optional.of(Identifier.withDefaultNamespace(location));
/* 193 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withLocation(Identifier location) {
/* 197 */       this.location = Optional.of(location);
/* 198 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFragmentShader(String fragmentShader) {
/* 202 */       this.fragmentShader = Optional.of(Identifier.withDefaultNamespace(fragmentShader));
/* 203 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFragmentShader(Identifier fragmentShader) {
/* 207 */       this.fragmentShader = Optional.of(fragmentShader);
/* 208 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withVertexShader(String vertexShader) {
/* 212 */       this.vertexShader = Optional.of(Identifier.withDefaultNamespace(vertexShader));
/* 213 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withVertexShader(Identifier vertexShader) {
/* 217 */       this.vertexShader = Optional.of(vertexShader);
/* 218 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withShaderDefine(String key) {
/* 222 */       if (this.definesBuilder.isEmpty()) {
/* 223 */         this.definesBuilder = Optional.of(ShaderDefines.builder());
/*     */       }
/* 225 */       ((ShaderDefines.Builder)this.definesBuilder.get()).define(key);
/* 226 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withShaderDefine(String key, int value) {
/* 230 */       if (this.definesBuilder.isEmpty()) {
/* 231 */         this.definesBuilder = Optional.of(ShaderDefines.builder());
/*     */       }
/* 233 */       ((ShaderDefines.Builder)this.definesBuilder.get()).define(key, value);
/* 234 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withShaderDefine(String key, float value) {
/* 238 */       if (this.definesBuilder.isEmpty()) {
/* 239 */         this.definesBuilder = Optional.of(ShaderDefines.builder());
/*     */       }
/* 241 */       ((ShaderDefines.Builder)this.definesBuilder.get()).define(key, value);
/* 242 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withSampler(String sampler) {
/* 246 */       if (this.samplers.isEmpty()) {
/* 247 */         this.samplers = Optional.of(new ArrayList<>());
/*     */       }
/* 249 */       ((List<String>)this.samplers.get()).add(sampler);
/* 250 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withUniform(String name, UniformType type) {
/* 254 */       if (this.uniforms.isEmpty()) {
/* 255 */         this.uniforms = Optional.of(new ArrayList<>());
/*     */       }
/* 257 */       if (type == UniformType.TEXEL_BUFFER) {
/* 258 */         throw new IllegalArgumentException("Cannot use texel buffer without specifying texture format");
/*     */       }
/* 260 */       ((List<RenderPipeline.UniformDescription>)this.uniforms.get()).add(new RenderPipeline.UniformDescription(name, type));
/* 261 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withUniform(String name, UniformType type, TextureFormat format) {
/* 265 */       if (this.uniforms.isEmpty()) {
/* 266 */         this.uniforms = Optional.of(new ArrayList<>());
/*     */       }
/* 268 */       if (type != UniformType.TEXEL_BUFFER) {
/* 269 */         throw new IllegalArgumentException("Only texel buffer can specify texture format");
/*     */       }
/* 271 */       ((List<RenderPipeline.UniformDescription>)this.uniforms.get()).add(new RenderPipeline.UniformDescription(name, format));
/* 272 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withDepthTestFunction(DepthTestFunction depthTestFunction) {
/* 276 */       this.depthTestFunction = Optional.of(depthTestFunction);
/* 277 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withPolygonMode(PolygonMode polygonMode) {
/* 281 */       this.polygonMode = Optional.of(polygonMode);
/* 282 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withCull(boolean cull) {
/* 286 */       this.cull = Optional.of(cull);
/* 287 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withBlend(BlendFunction blendFunction) {
/* 291 */       this.blendFunction = Optional.of(blendFunction);
/* 292 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withoutBlend() {
/* 296 */       this.blendFunction = Optional.empty();
/* 297 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withColorWrite(boolean writeColor) {
/* 301 */       this.writeColor = Optional.of(writeColor);
/* 302 */       this.writeAlpha = Optional.of(writeColor);
/* 303 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withColorWrite(boolean writeColor, boolean writeAlpha) {
/* 307 */       this.writeColor = Optional.of(writeColor);
/* 308 */       this.writeAlpha = Optional.of(writeAlpha);
/* 309 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withDepthWrite(boolean writeDepth) {
/* 313 */       this.writeDepth = Optional.of(writeDepth);
/* 314 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder withColorLogic(LogicOp colorLogic) {
/* 323 */       this.colorLogic = Optional.of(colorLogic);
/* 324 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withVertexFormat(VertexFormat vertexFormat, VertexFormat.Mode vertexFormatMode) {
/* 328 */       this.vertexFormat = Optional.of(vertexFormat);
/* 329 */       this.vertexFormatMode = Optional.of(vertexFormatMode);
/* 330 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withDepthBias(float scaleFactor, float constant) {
/* 334 */       this.depthBiasScaleFactor = scaleFactor;
/* 335 */       this.depthBiasConstant = constant;
/* 336 */       return this;
/*     */     }
/*     */     
/*     */     private void withSnippet(RenderPipeline.Snippet snippet) {
/* 340 */       if (snippet.vertexShader.isPresent()) {
/* 341 */         this.vertexShader = snippet.vertexShader;
/*     */       }
/* 343 */       if (snippet.fragmentShader.isPresent()) {
/* 344 */         this.fragmentShader = snippet.fragmentShader;
/*     */       }
/* 346 */       if (snippet.shaderDefines.isPresent()) {
/* 347 */         if (this.definesBuilder.isEmpty()) {
/* 348 */           this.definesBuilder = Optional.of(ShaderDefines.builder());
/*     */         }
/* 350 */         ShaderDefines snippetDefines = snippet.shaderDefines.get();
/* 351 */         for (Map.Entry<String, String> snippetValue : (Iterable<Map.Entry<String, String>>)snippetDefines.values().entrySet()) {
/* 352 */           ((ShaderDefines.Builder)this.definesBuilder.get()).define(snippetValue.getKey(), snippetValue.getValue());
/*     */         }
/* 354 */         for (String flag : (Iterable<String>)snippetDefines.flags()) {
/* 355 */           ((ShaderDefines.Builder)this.definesBuilder.get()).define(flag);
/*     */         }
/*     */       } 
/* 358 */       snippet.samplers.ifPresent(builderSamplers -> {
/*     */             if (this.samplers.isPresent()) {
/*     */               ((List)this.samplers.get()).addAll(builderSamplers);
/*     */             } else {
/*     */               this.samplers = Optional.of(new ArrayList<>(builderSamplers));
/*     */             } 
/*     */           });
/*     */       
/* 366 */       snippet.uniforms.ifPresent(builderUniforms -> {
/*     */             if (this.uniforms.isPresent()) {
/*     */               ((List)this.uniforms.get()).addAll(builderUniforms);
/*     */             } else {
/*     */               this.uniforms = Optional.of(new ArrayList<>(builderUniforms));
/*     */             } 
/*     */           });
/*     */ 
/*     */       
/* 375 */       if (snippet.depthTestFunction.isPresent()) {
/* 376 */         this.depthTestFunction = snippet.depthTestFunction;
/*     */       }
/*     */       
/* 379 */       if (snippet.cull.isPresent()) {
/* 380 */         this.cull = snippet.cull;
/*     */       }
/*     */       
/* 383 */       if (snippet.writeColor.isPresent()) {
/* 384 */         this.writeColor = snippet.writeColor;
/*     */       }
/*     */       
/* 387 */       if (snippet.writeAlpha.isPresent()) {
/* 388 */         this.writeAlpha = snippet.writeAlpha;
/*     */       }
/*     */       
/* 391 */       if (snippet.writeDepth.isPresent()) {
/* 392 */         this.writeDepth = snippet.writeDepth;
/*     */       }
/*     */       
/* 395 */       if (snippet.colorLogic.isPresent()) {
/* 396 */         this.colorLogic = snippet.colorLogic;
/*     */       }
/*     */       
/* 399 */       if (snippet.blendFunction.isPresent()) {
/* 400 */         this.blendFunction = snippet.blendFunction;
/*     */       }
/*     */       
/* 403 */       if (snippet.vertexFormat.isPresent()) {
/* 404 */         this.vertexFormat = snippet.vertexFormat;
/*     */       }
/*     */       
/* 407 */       if (snippet.vertexFormatMode.isPresent()) {
/* 408 */         this.vertexFormatMode = snippet.vertexFormatMode;
/*     */       }
/*     */     }
/*     */     
/*     */     public RenderPipeline.Snippet buildSnippet() {
/* 413 */       return new RenderPipeline.Snippet(this.vertexShader, this.fragmentShader, 
/*     */ 
/*     */           
/* 416 */           this.definesBuilder.map(ShaderDefines.Builder::build), 
/* 417 */           this.samplers.map(Collections::unmodifiableList), 
/* 418 */           this.uniforms.map(Collections::unmodifiableList), this.blendFunction, this.depthTestFunction, this.polygonMode, this.cull, this.writeColor, this.writeAlpha, this.writeDepth, this.colorLogic, this.vertexFormat, this.vertexFormatMode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RenderPipeline build() {
/* 433 */       if (this.location.isEmpty()) {
/* 434 */         throw new IllegalStateException("Missing location");
/*     */       }
/* 436 */       if (this.vertexShader.isEmpty()) {
/* 437 */         throw new IllegalStateException("Missing vertex shader");
/*     */       }
/* 439 */       if (this.fragmentShader.isEmpty()) {
/* 440 */         throw new IllegalStateException("Missing fragment shader");
/*     */       }
/* 442 */       if (this.vertexFormat.isEmpty()) {
/* 443 */         throw new IllegalStateException("Missing vertex buffer format");
/*     */       }
/* 445 */       if (this.vertexFormatMode.isEmpty()) {
/* 446 */         throw new IllegalStateException("Missing vertex mode");
/*     */       }
/* 448 */       return new RenderPipeline(this.location.get(), this.vertexShader.get(), this.fragmentShader.get(), ((ShaderDefines.Builder)
/* 449 */           this.definesBuilder.orElse(ShaderDefines.builder())).build(), List.copyOf(this.samplers.orElse(new ArrayList<>())), 
/* 450 */           this.uniforms.orElse(Collections.emptyList()), this.blendFunction, this.depthTestFunction.orElse(DepthTestFunction.LEQUAL_DEPTH_TEST), this.polygonMode.orElse(PolygonMode.FILL), (Boolean)
/* 451 */           this.cull.orElse(true), (Boolean)this.writeColor.orElse(true), (Boolean)this.writeAlpha.orElse(true), (Boolean)
/* 452 */           this.writeDepth.orElse(true), this.colorLogic.orElse(LogicOp.NONE), this.vertexFormat.get(), 
/* 453 */           this.vertexFormatMode.get(), this.depthBiasScaleFactor, this.depthBiasConstant, nextPipelineSortKey++);
/*     */     } }
/*     */   public static final class UniformDescription extends Record { private final String name; private final UniformType type; private final TextureFormat textureFormat;
/*     */     
/* 457 */     public UniformDescription(String name, UniformType type, TextureFormat textureFormat) { this.name = name; this.type = type; this.textureFormat = textureFormat; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #457	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #457	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #457	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription;
/* 457 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public UniformType type() { return this.type; } public TextureFormat textureFormat() { return this.textureFormat; }
/*     */      public UniformDescription(String name, UniformType type) {
/* 459 */       this(name, type, null);
/* 460 */       if (type == UniformType.TEXEL_BUFFER) {
/* 461 */         throw new IllegalArgumentException("Texel buffer needs a texture format");
/*     */       }
/*     */     }
/*     */     
/*     */     public UniformDescription(String name, TextureFormat textureFormat) {
/* 466 */       this(name, UniformType.TEXEL_BUFFER, textureFormat);
/*     */     } }
/*     */   
/*     */   public static final class Snippet extends Record { private final Optional<Identifier> vertexShader; private final Optional<Identifier> fragmentShader; private final Optional<ShaderDefines> shaderDefines; private final Optional<List<String>> samplers; private final Optional<List<RenderPipeline.UniformDescription>> uniforms; private final Optional<BlendFunction> blendFunction; private final Optional<DepthTestFunction> depthTestFunction;
/* 470 */     public Snippet(Optional<Identifier> vertexShader, Optional<Identifier> fragmentShader, Optional<ShaderDefines> shaderDefines, Optional<List<String>> samplers, Optional<List<RenderPipeline.UniformDescription>> uniforms, Optional<BlendFunction> blendFunction, Optional<DepthTestFunction> depthTestFunction, Optional<PolygonMode> polygonMode, Optional<Boolean> cull, Optional<Boolean> writeColor, Optional<Boolean> writeAlpha, Optional<Boolean> writeDepth, Optional<LogicOp> colorLogic, Optional<VertexFormat> vertexFormat, Optional<VertexFormat.Mode> vertexFormatMode) { this.vertexShader = vertexShader; this.fragmentShader = fragmentShader; this.shaderDefines = shaderDefines; this.samplers = samplers; this.uniforms = uniforms; this.blendFunction = blendFunction; this.depthTestFunction = depthTestFunction; this.polygonMode = polygonMode; this.cull = cull; this.writeColor = writeColor; this.writeAlpha = writeAlpha; this.writeDepth = writeDepth; this.colorLogic = colorLogic; this.vertexFormat = vertexFormat; this.vertexFormatMode = vertexFormatMode; } private final Optional<PolygonMode> polygonMode; private final Optional<Boolean> cull; private final Optional<Boolean> writeColor; private final Optional<Boolean> writeAlpha; private final Optional<Boolean> writeDepth; private final Optional<LogicOp> colorLogic; private final Optional<VertexFormat> vertexFormat; private final Optional<VertexFormat.Mode> vertexFormatMode; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #470	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #470	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #470	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/* 470 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> vertexShader() { return this.vertexShader; } public Optional<Identifier> fragmentShader() { return this.fragmentShader; } public Optional<ShaderDefines> shaderDefines() { return this.shaderDefines; } public Optional<List<String>> samplers() { return this.samplers; } public Optional<List<RenderPipeline.UniformDescription>> uniforms() { return this.uniforms; } public Optional<BlendFunction> blendFunction() { return this.blendFunction; } public Optional<DepthTestFunction> depthTestFunction() { return this.depthTestFunction; } public Optional<PolygonMode> polygonMode() { return this.polygonMode; } public Optional<Boolean> cull() { return this.cull; } public Optional<Boolean> writeColor() { return this.writeColor; } public Optional<Boolean> writeAlpha() { return this.writeAlpha; } public Optional<Boolean> writeDepth() { return this.writeDepth; } public Optional<LogicOp> colorLogic() { return this.colorLogic; } public Optional<VertexFormat> vertexFormat() { return this.vertexFormat; } public Optional<VertexFormat.Mode> vertexFormatMode() { return this.vertexFormatMode; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/pipeline/RenderPipeline.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */