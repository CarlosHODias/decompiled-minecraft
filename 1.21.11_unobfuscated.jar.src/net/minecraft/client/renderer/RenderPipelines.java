/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.BlendFunction;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.platform.DepthTestFunction;
/*     */ import com.mojang.blaze3d.platform.DestFactor;
/*     */ import com.mojang.blaze3d.platform.PolygonMode;
/*     */ import com.mojang.blaze3d.platform.SourceFactor;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class RenderPipelines
/*     */ {
/*  20 */   private static final Map<Identifier, RenderPipeline> PIPELINES_BY_LOCATION = new HashMap<>();
/*     */   
/*     */   private static RenderPipeline register(RenderPipeline pipeline) {
/*  23 */     PIPELINES_BY_LOCATION.put(pipeline.getLocation(), pipeline);
/*  24 */     return pipeline;
/*     */   }
/*     */   
/*     */   public static List<RenderPipeline> getStaticPipelines() {
/*  28 */     return PIPELINES_BY_LOCATION.values().stream().toList();
/*     */   }
/*     */   
/*  31 */   private static final RenderPipeline.Snippet MATRICES_PROJECTION_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[0])
/*  32 */     .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
/*  33 */     .withUniform("Projection", UniformType.UNIFORM_BUFFER)
/*  34 */     .buildSnippet();
/*     */   
/*  36 */   private static final RenderPipeline.Snippet FOG_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[0])
/*  37 */     .withUniform("Fog", UniformType.UNIFORM_BUFFER)
/*  38 */     .buildSnippet();
/*     */   
/*  40 */   private static final RenderPipeline.Snippet GLOBALS_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[0])
/*  41 */     .withUniform("Globals", UniformType.UNIFORM_BUFFER)
/*  42 */     .buildSnippet();
/*     */   
/*  44 */   private static final RenderPipeline.Snippet MATRICES_FOG_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET
/*  45 */       }).buildSnippet();
/*     */   
/*  47 */   private static final RenderPipeline.Snippet MATRICES_FOG_LIGHT_DIR_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET
/*  48 */       }).withUniform("Lighting", UniformType.UNIFORM_BUFFER)
/*  49 */     .buildSnippet();
/*     */   
/*  51 */   private static final RenderPipeline.Snippet GENERIC_BLOCKS_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { FOG_SNIPPET
/*  52 */       }).withSampler("Sampler0")
/*  53 */     .withSampler("Sampler2")
/*  54 */     .withVertexFormat(DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS)
/*  55 */     .buildSnippet();
/*     */   
/*  57 */   private static final RenderPipeline.Snippet TERRAIN_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GENERIC_BLOCKS_SNIPPET
/*  58 */       }).withUniform("Projection", UniformType.UNIFORM_BUFFER)
/*  59 */     .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
/*  60 */     .withVertexShader("core/terrain")
/*  61 */     .withFragmentShader("core/terrain")
/*  62 */     .buildSnippet();
/*     */   
/*  64 */   private static final RenderPipeline.Snippet BLOCK_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GENERIC_BLOCKS_SNIPPET, MATRICES_PROJECTION_SNIPPET
/*  65 */       }).withVertexShader("core/block")
/*  66 */     .withFragmentShader("core/block")
/*  67 */     .buildSnippet();
/*     */   
/*  69 */   private static final RenderPipeline.Snippet ENTITY_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET
/*  70 */       }).withVertexShader("core/entity")
/*  71 */     .withFragmentShader("core/entity")
/*  72 */     .withSampler("Sampler0")
/*  73 */     .withSampler("Sampler2")
/*  74 */     .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/*  75 */     .buildSnippet();
/*     */   
/*  77 */   private static final RenderPipeline.Snippet ENTITY_EMISSIVE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET
/*  78 */       }).withVertexShader("core/entity")
/*  79 */     .withFragmentShader("core/entity")
/*  80 */     .withSampler("Sampler0")
/*  81 */     .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/*  82 */     .withShaderDefine("EMISSIVE")
/*  83 */     .buildSnippet();
/*     */   
/*  85 */   private static final RenderPipeline.Snippet BEACON_BEAM_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/*  86 */       }).withVertexShader("core/rendertype_beacon_beam")
/*  87 */     .withFragmentShader("core/rendertype_beacon_beam")
/*  88 */     .withSampler("Sampler0")
/*  89 */     .withVertexFormat(DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS)
/*  90 */     .buildSnippet();
/*     */   
/*  92 */   private static final RenderPipeline.Snippet TEXT_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/*  93 */       }).withBlend(BlendFunction.TRANSLUCENT)
/*  94 */     .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS)
/*  95 */     .buildSnippet();
/*     */   
/*  97 */   private static final RenderPipeline.Snippet END_PORTAL_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET, GLOBALS_SNIPPET
/*  98 */       }).withVertexShader("core/rendertype_end_portal")
/*  99 */     .withFragmentShader("core/rendertype_end_portal")
/* 100 */     .withSampler("Sampler0")
/* 101 */     .withSampler("Sampler1")
/* 102 */     .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
/* 103 */     .buildSnippet();
/*     */   
/* 105 */   private static final RenderPipeline.Snippet CLOUDS_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 106 */       }).withVertexShader("core/rendertype_clouds")
/* 107 */     .withFragmentShader("core/rendertype_clouds")
/* 108 */     .withBlend(BlendFunction.TRANSLUCENT)
/* 109 */     .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.QUADS)
/* 110 */     .withUniform("CloudInfo", UniformType.UNIFORM_BUFFER)
/* 111 */     .withUniform("CloudFaces", UniformType.TEXEL_BUFFER, TextureFormat.RED8I)
/* 112 */     .buildSnippet();
/*     */   
/* 114 */   private static final RenderPipeline.Snippet LINES_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET, GLOBALS_SNIPPET
/* 115 */       }).withVertexShader("core/rendertype_lines")
/* 116 */     .withFragmentShader("core/rendertype_lines")
/* 117 */     .withBlend(BlendFunction.TRANSLUCENT)
/* 118 */     .withCull(false)
/* 119 */     .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH, VertexFormat.Mode.LINES)
/* 120 */     .buildSnippet();
/*     */   
/* 122 */   private static final RenderPipeline.Snippet DEBUG_FILLED_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 123 */       }).withVertexShader("core/position_color")
/* 124 */     .withFragmentShader("core/position_color")
/* 125 */     .withBlend(BlendFunction.TRANSLUCENT)
/* 126 */     .withDepthWrite(false)
/* 127 */     .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
/* 128 */     .buildSnippet();
/*     */   
/* 130 */   private static final RenderPipeline.Snippet PARTICLE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 131 */       }).withVertexShader("core/particle")
/* 132 */     .withFragmentShader("core/particle")
/* 133 */     .withSampler("Sampler0")
/* 134 */     .withSampler("Sampler2")
/* 135 */     .withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
/* 136 */     .buildSnippet();
/*     */   
/* 138 */   private static final RenderPipeline.Snippet WEATHER_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { PARTICLE_SNIPPET
/* 139 */       }).withBlend(BlendFunction.TRANSLUCENT)
/* 140 */     .withCull(false)
/* 141 */     .buildSnippet();
/*     */   
/* 143 */   private static final RenderPipeline.Snippet GUI_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 144 */       }).withVertexShader("core/gui")
/* 145 */     .withFragmentShader("core/gui")
/* 146 */     .withBlend(BlendFunction.TRANSLUCENT)
/* 147 */     .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
/* 148 */     .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 149 */     .buildSnippet();
/*     */   
/* 151 */   private static final RenderPipeline.Snippet GUI_TEXTURED_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 152 */       }).withVertexShader("core/position_tex_color")
/* 153 */     .withFragmentShader("core/position_tex_color")
/* 154 */     .withSampler("Sampler0")
/* 155 */     .withBlend(BlendFunction.TRANSLUCENT)
/* 156 */     .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
/* 157 */     .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 158 */     .buildSnippet();
/*     */   
/* 160 */   private static final RenderPipeline.Snippet GUI_TEXT_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET
/* 161 */       }).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 162 */     .buildSnippet();
/*     */   
/* 164 */   private static final RenderPipeline.Snippet OUTLINE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 165 */       }).withVertexShader("core/rendertype_outline")
/* 166 */     .withFragmentShader("core/rendertype_outline")
/* 167 */     .withSampler("Sampler0")
/* 168 */     .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 169 */     .withDepthWrite(false)
/* 170 */     .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
/* 171 */     .buildSnippet();
/*     */   
/* 173 */   public static final RenderPipeline.Snippet POST_PROCESSING_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[0])
/* 174 */     .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 175 */     .withDepthWrite(false)
/* 176 */     .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
/* 177 */     .buildSnippet();
/*     */   
/* 179 */   public static final RenderPipeline SOLID_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BLOCK_SNIPPET
/* 180 */         }).withLocation("pipeline/solid_block")
/* 181 */       .build());
/*     */   
/* 183 */   public static final RenderPipeline SOLID_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET
/* 184 */         }).withLocation("pipeline/solid_terrain")
/* 185 */       .build());
/*     */   
/* 187 */   public static final RenderPipeline WIREFRAME = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET
/* 188 */         }).withLocation("pipeline/wireframe")
/* 189 */       .withPolygonMode(PolygonMode.WIREFRAME)
/* 190 */       .build());
/*     */   
/* 192 */   public static final RenderPipeline CUTOUT_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BLOCK_SNIPPET
/* 193 */         }).withLocation("pipeline/cutout_block")
/* 194 */       .withShaderDefine("ALPHA_CUTOUT", 0.5F)
/* 195 */       .build());
/*     */   
/* 197 */   public static final RenderPipeline CUTOUT_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET
/* 198 */         }).withLocation("pipeline/cutout_terrain")
/* 199 */       .withShaderDefine("ALPHA_CUTOUT", 0.5F)
/* 200 */       .build());
/*     */   
/* 202 */   public static final RenderPipeline TRANSLUCENT_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET
/* 203 */         }).withLocation("pipeline/translucent_terrain")
/* 204 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 205 */       .withShaderDefine("ALPHA_CUTOUT", 0.01F)
/* 206 */       .build());
/*     */   
/* 208 */   public static final RenderPipeline TRIPWIRE_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BLOCK_SNIPPET
/* 209 */         }).withLocation("pipeline/tripwire_block")
/* 210 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 211 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 212 */       .build());
/*     */   
/* 214 */   public static final RenderPipeline TRIPWIRE_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET
/* 215 */         }).withLocation("pipeline/tripwire_terrain")
/* 216 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 217 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 218 */       .build());
/*     */   
/* 220 */   public static final RenderPipeline TRANSLUCENT_MOVING_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 221 */         }).withLocation("pipeline/translucent_moving_block")
/* 222 */       .withVertexShader("core/rendertype_translucent_moving_block")
/* 223 */       .withFragmentShader("core/rendertype_translucent_moving_block")
/* 224 */       .withSampler("Sampler0")
/* 225 */       .withSampler("Sampler2")
/* 226 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 227 */       .withVertexFormat(DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS)
/* 228 */       .build());
/*     */   
/* 230 */   public static final RenderPipeline ARMOR_CUTOUT_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 231 */         }).withLocation("pipeline/armor_cutout_no_cull")
/* 232 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 233 */       .withShaderDefine("NO_OVERLAY")
/* 234 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 235 */       .withCull(false)
/* 236 */       .build());
/*     */   
/* 238 */   public static final RenderPipeline ARMOR_DECAL_CUTOUT_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 239 */         }).withLocation("pipeline/armor_decal_cutout_no_cull")
/* 240 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 241 */       .withShaderDefine("NO_OVERLAY")
/* 242 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 243 */       .withCull(false)
/* 244 */       .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
/* 245 */       .build());
/*     */   
/* 247 */   public static final RenderPipeline ARMOR_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 248 */         }).withLocation("pipeline/armor_translucent")
/* 249 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 250 */       .withShaderDefine("NO_OVERLAY")
/* 251 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 252 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 253 */       .withCull(false)
/* 254 */       .build());
/*     */   
/* 256 */   public static final RenderPipeline ENTITY_SOLID = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 257 */         }).withLocation("pipeline/entity_solid")
/* 258 */       .withSampler("Sampler1")
/* 259 */       .build());
/*     */   
/* 261 */   public static final RenderPipeline ENTITY_SOLID_Z_OFFSET_FORWARD = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 262 */         }).withLocation("pipeline/entity_solid_offset_forward")
/* 263 */       .withSampler("Sampler1")
/* 264 */       .build());
/*     */   
/* 266 */   public static final RenderPipeline ENTITY_CUTOUT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 267 */         }).withLocation("pipeline/entity_cutout")
/* 268 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 269 */       .withSampler("Sampler1")
/* 270 */       .build());
/*     */   
/* 272 */   public static final RenderPipeline ENTITY_CUTOUT_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 273 */         }).withLocation("pipeline/entity_cutout_no_cull")
/* 274 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 275 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 276 */       .withSampler("Sampler1")
/* 277 */       .withCull(false)
/* 278 */       .build());
/*     */   
/* 280 */   public static final RenderPipeline ENTITY_CUTOUT_NO_CULL_Z_OFFSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 281 */         }).withLocation("pipeline/entity_cutout_no_cull_z_offset")
/* 282 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 283 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 284 */       .withSampler("Sampler1")
/* 285 */       .withCull(false)
/* 286 */       .build());
/*     */   
/* 288 */   public static final RenderPipeline ENTITY_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 289 */         }).withLocation("pipeline/entity_translucent")
/* 290 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 291 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 292 */       .withSampler("Sampler1")
/* 293 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 294 */       .withCull(false)
/* 295 */       .build());
/*     */   
/* 297 */   public static final RenderPipeline ENTITY_TRANSLUCENT_EMISSIVE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_EMISSIVE_SNIPPET
/* 298 */         }).withLocation("pipeline/entity_translucent_emissive")
/* 299 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 300 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 301 */       .withSampler("Sampler1")
/* 302 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 303 */       .withCull(false)
/* 304 */       .withDepthWrite(false)
/* 305 */       .build());
/*     */   
/* 307 */   public static final RenderPipeline ENTITY_SMOOTH_CUTOUT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 308 */         }).withLocation("pipeline/entity_smooth_cutout")
/* 309 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 310 */       .withSampler("Sampler1")
/* 311 */       .withCull(false)
/* 312 */       .build());
/*     */   
/* 314 */   public static final RenderPipeline ENTITY_NO_OUTLINE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 315 */         }).withLocation("pipeline/entity_no_outline")
/* 316 */       .withShaderDefine("NO_OVERLAY")
/* 317 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 318 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 319 */       .withCull(false)
/* 320 */       .withDepthWrite(false)
/* 321 */       .build());
/*     */   
/* 323 */   public static final RenderPipeline BREEZE_WIND = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET
/* 324 */         }).withLocation("pipeline/breeze_wind")
/* 325 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 326 */       .withShaderDefine("APPLY_TEXTURE_MATRIX")
/* 327 */       .withShaderDefine("NO_OVERLAY")
/* 328 */       .withShaderDefine("NO_CARDINAL_LIGHTING")
/* 329 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 330 */       .withCull(false)
/* 331 */       .build());
/*     */   
/* 333 */   public static final RenderPipeline ENERGY_SWIRL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 334 */         }).withLocation("pipeline/energy_swirl")
/* 335 */       .withVertexShader("core/entity")
/* 336 */       .withFragmentShader("core/entity")
/* 337 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 338 */       .withShaderDefine("EMISSIVE")
/* 339 */       .withShaderDefine("NO_OVERLAY")
/* 340 */       .withShaderDefine("NO_CARDINAL_LIGHTING")
/* 341 */       .withShaderDefine("APPLY_TEXTURE_MATRIX")
/* 342 */       .withSampler("Sampler0")
/* 343 */       .withBlend(BlendFunction.ADDITIVE)
/* 344 */       .withCull(false)
/* 345 */       .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/* 346 */       .build());
/*     */   
/* 348 */   public static final RenderPipeline EYES = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 349 */         }).withLocation("pipeline/eyes")
/* 350 */       .withVertexShader("core/entity")
/* 351 */       .withFragmentShader("core/entity")
/* 352 */       .withShaderDefine("EMISSIVE")
/* 353 */       .withShaderDefine("NO_OVERLAY")
/* 354 */       .withShaderDefine("NO_CARDINAL_LIGHTING")
/* 355 */       .withSampler("Sampler0")
/* 356 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 357 */       .withDepthWrite(false)
/* 358 */       .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/* 359 */       .build());
/*     */   
/* 361 */   public static final RenderPipeline ENTITY_DECAL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET
/* 362 */         }).withLocation("pipeline/entity_decal")
/* 363 */       .withVertexShader("core/rendertype_entity_decal")
/* 364 */       .withFragmentShader("core/rendertype_entity_decal")
/* 365 */       .withSampler("Sampler0")
/* 366 */       .withSampler("Sampler1")
/* 367 */       .withSampler("Sampler2")
/* 368 */       .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
/* 369 */       .withCull(false)
/* 370 */       .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/* 371 */       .build());
/*     */   
/* 373 */   public static final RenderPipeline ENTITY_SHADOW = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 374 */         }).withLocation("pipeline/entity_shadow")
/* 375 */       .withVertexShader("core/rendertype_entity_shadow")
/* 376 */       .withFragmentShader("core/rendertype_entity_shadow")
/* 377 */       .withSampler("Sampler0")
/* 378 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 379 */       .withDepthWrite(false)
/* 380 */       .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/* 381 */       .build());
/*     */   
/* 383 */   public static final RenderPipeline ITEM_ENTITY_TRANSLUCENT_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET
/* 384 */         }).withLocation("pipeline/item_entity_translucent_cull")
/* 385 */       .withVertexShader("core/rendertype_item_entity_translucent_cull")
/* 386 */       .withFragmentShader("core/rendertype_item_entity_translucent_cull")
/* 387 */       .withSampler("Sampler0")
/* 388 */       .withSampler("Sampler2")
/* 389 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 390 */       .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/* 391 */       .build());
/*     */   
/* 393 */   public static final RenderPipeline BEACON_BEAM_OPAQUE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BEACON_BEAM_SNIPPET
/* 394 */         }).withLocation("pipeline/beacon_beam_opaque")
/* 395 */       .build());
/*     */   
/* 397 */   public static final RenderPipeline BEACON_BEAM_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BEACON_BEAM_SNIPPET
/* 398 */         }).withLocation("pipeline/beacon_beam_translucent")
/* 399 */       .withDepthWrite(false)
/* 400 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 401 */       .build());
/*     */   
/* 403 */   public static final RenderPipeline DRAGON_EXPLOSION_ALPHA = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 404 */         }).withLocation("pipeline/dragon_explosion_alpha")
/* 405 */       .withVertexShader("core/rendertype_entity_alpha")
/* 406 */       .withFragmentShader("core/rendertype_entity_alpha")
/* 407 */       .withSampler("Sampler0")
/* 408 */       .withCull(false)
/* 409 */       .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
/* 410 */       .build());
/*     */   
/* 412 */   public static final RenderPipeline LEASH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 413 */         }).withLocation("pipeline/leash")
/* 414 */       .withVertexShader("core/rendertype_leash")
/* 415 */       .withFragmentShader("core/rendertype_leash")
/* 416 */       .withSampler("Sampler2")
/* 417 */       .withCull(false)
/* 418 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.TRIANGLE_STRIP)
/* 419 */       .build());
/*     */   
/* 421 */   public static final RenderPipeline WATER_MASK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 422 */         }).withLocation("pipeline/water_mask")
/* 423 */       .withVertexShader("core/rendertype_water_mask")
/* 424 */       .withFragmentShader("core/rendertype_water_mask")
/* 425 */       .withColorWrite(false)
/* 426 */       .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
/* 427 */       .build());
/*     */   
/* 429 */   public static final RenderPipeline GLINT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET, GLOBALS_SNIPPET
/* 430 */         }).withLocation("pipeline/glint")
/* 431 */       .withVertexShader("core/glint")
/* 432 */       .withFragmentShader("core/glint")
/* 433 */       .withSampler("Sampler0")
/* 434 */       .withDepthWrite(false)
/* 435 */       .withCull(false)
/* 436 */       .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
/* 437 */       .withBlend(BlendFunction.GLINT)
/* 438 */       .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
/* 439 */       .build());
/*     */   
/* 441 */   public static final RenderPipeline CRUMBLING = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 442 */         }).withLocation("pipeline/crumbling")
/* 443 */       .withVertexShader("core/rendertype_crumbling")
/* 444 */       .withFragmentShader("core/rendertype_crumbling")
/* 445 */       .withSampler("Sampler0")
/* 446 */       .withBlend(new BlendFunction(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO))
/* 447 */       .withDepthWrite(false)
/* 448 */       .withVertexFormat(DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS)
/* 449 */       .withDepthBias(-1.0F, -10.0F)
/* 450 */       .build());
/*     */   
/* 452 */   public static final RenderPipeline TEXT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET, FOG_SNIPPET
/* 453 */         }).withLocation("pipeline/text")
/* 454 */       .withVertexShader("core/rendertype_text")
/* 455 */       .withFragmentShader("core/rendertype_text")
/* 456 */       .withSampler("Sampler0")
/* 457 */       .withSampler("Sampler2")
/* 458 */       .build());
/*     */   
/* 460 */   public static final RenderPipeline GUI_TEXT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXT_SNIPPET, FOG_SNIPPET
/* 461 */         }).withLocation("pipeline/gui_text")
/* 462 */       .withVertexShader("core/rendertype_text")
/* 463 */       .withFragmentShader("core/rendertype_text")
/* 464 */       .withSampler("Sampler0")
/* 465 */       .withSampler("Sampler2")
/* 466 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 467 */       .build());
/*     */   
/* 469 */   public static final RenderPipeline TEXT_BACKGROUND = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET, FOG_SNIPPET
/* 470 */         }).withLocation("pipeline/text_background")
/* 471 */       .withVertexShader("core/rendertype_text_background")
/* 472 */       .withFragmentShader("core/rendertype_text_background")
/* 473 */       .withSampler("Sampler2")
/* 474 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS)
/* 475 */       .build());
/*     */   
/* 477 */   public static final RenderPipeline TEXT_INTENSITY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET, FOG_SNIPPET
/* 478 */         }).withLocation("pipeline/text_intensity")
/* 479 */       .withVertexShader("core/rendertype_text_intensity")
/* 480 */       .withFragmentShader("core/rendertype_text_intensity")
/* 481 */       .withSampler("Sampler0")
/* 482 */       .withSampler("Sampler2")
/* 483 */       .withDepthBias(-1.0F, -10.0F)
/* 484 */       .build());
/*     */   
/* 486 */   public static final RenderPipeline GUI_TEXT_INTENSITY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXT_SNIPPET, FOG_SNIPPET
/* 487 */         }).withLocation("pipeline/gui_text_intensity")
/* 488 */       .withVertexShader("core/rendertype_text_intensity")
/* 489 */       .withFragmentShader("core/rendertype_text_intensity")
/* 490 */       .withSampler("Sampler0")
/* 491 */       .withSampler("Sampler2")
/* 492 */       .build());
/*     */   
/* 494 */   public static final RenderPipeline TEXT_POLYGON_OFFSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET, FOG_SNIPPET
/* 495 */         }).withLocation("pipeline/text_polygon_offset")
/* 496 */       .withVertexShader("core/rendertype_text")
/* 497 */       .withFragmentShader("core/rendertype_text")
/* 498 */       .withSampler("Sampler0")
/* 499 */       .withSampler("Sampler2")
/* 500 */       .withDepthBias(-1.0F, -10.0F)
/* 501 */       .build());
/*     */   
/* 503 */   public static final RenderPipeline TEXT_SEE_THROUGH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET
/* 504 */         }).withLocation("pipeline/text_see_through")
/* 505 */       .withVertexShader("core/rendertype_text_see_through")
/* 506 */       .withFragmentShader("core/rendertype_text_see_through")
/* 507 */       .withSampler("Sampler0")
/* 508 */       .withDepthWrite(false)
/* 509 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 510 */       .build());
/*     */   
/* 512 */   public static final RenderPipeline TEXT_BACKGROUND_SEE_THROUGH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET
/* 513 */         }).withLocation("pipeline/text_background_see_through")
/* 514 */       .withVertexShader("core/rendertype_text_background_see_through")
/* 515 */       .withFragmentShader("core/rendertype_text_background_see_through")
/* 516 */       .withDepthWrite(false)
/* 517 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 518 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS)
/* 519 */       .build());
/*     */   
/* 521 */   public static final RenderPipeline TEXT_INTENSITY_SEE_THROUGH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET
/* 522 */         }).withLocation("pipeline/text_intensity_see_through")
/* 523 */       .withVertexShader("core/rendertype_text_intensity_see_through")
/* 524 */       .withFragmentShader("core/rendertype_text_intensity_see_through")
/* 525 */       .withSampler("Sampler0")
/* 526 */       .withDepthWrite(false)
/* 527 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 528 */       .build());
/*     */   
/* 530 */   public static final RenderPipeline LIGHTNING = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 531 */         }).withLocation("pipeline/lightning")
/* 532 */       .withVertexShader("core/rendertype_lightning")
/* 533 */       .withFragmentShader("core/rendertype_lightning")
/* 534 */       .withBlend(BlendFunction.LIGHTNING)
/* 535 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
/* 536 */       .build());
/*     */   
/* 538 */   public static final RenderPipeline DRAGON_RAYS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 539 */         }).withLocation("pipeline/dragon_rays")
/* 540 */       .withVertexShader("core/rendertype_lightning")
/* 541 */       .withFragmentShader("core/rendertype_lightning")
/* 542 */       .withDepthWrite(false)
/* 543 */       .withBlend(BlendFunction.LIGHTNING)
/* 544 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
/* 545 */       .build());
/*     */   
/* 547 */   public static final RenderPipeline DRAGON_RAYS_DEPTH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 548 */         }).withLocation("pipeline/dragon_rays_depth")
/* 549 */       .withVertexShader("core/position")
/* 550 */       .withFragmentShader("core/position")
/* 551 */       .withColorWrite(false)
/* 552 */       .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLES)
/* 553 */       .build());
/*     */   
/* 555 */   public static final RenderPipeline END_PORTAL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { END_PORTAL_SNIPPET
/* 556 */         }).withLocation("pipeline/end_portal")
/* 557 */       .withShaderDefine("PORTAL_LAYERS", 15)
/* 558 */       .build());
/*     */   
/* 560 */   public static final RenderPipeline END_GATEWAY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { END_PORTAL_SNIPPET
/* 561 */         }).withLocation("pipeline/end_gateway")
/* 562 */       .withShaderDefine("PORTAL_LAYERS", 16)
/* 563 */       .build());
/*     */   
/* 565 */   public static final RenderPipeline FLAT_CLOUDS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { CLOUDS_SNIPPET
/* 566 */         }).withLocation("pipeline/flat_clouds")
/* 567 */       .withCull(false)
/* 568 */       .build());
/*     */   
/* 570 */   public static final RenderPipeline CLOUDS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { CLOUDS_SNIPPET
/* 571 */         }).withLocation("pipeline/clouds")
/* 572 */       .build());
/*     */   
/* 574 */   public static final RenderPipeline LINES = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET
/* 575 */         }).withLocation("pipeline/lines")
/* 576 */       .build());
/*     */   
/* 578 */   public static final RenderPipeline LINES_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET
/* 579 */         }).withDepthWrite(false)
/* 580 */       .withLocation("pipeline/lines_translucent")
/* 581 */       .build());
/*     */   
/* 583 */   public static final RenderPipeline SECONDARY_BLOCK_OUTLINE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET
/* 584 */         }).withLocation("pipeline/secondary_block_outline")
/* 585 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 586 */       .withDepthWrite(false)
/* 587 */       .build());
/*     */   
/* 589 */   public static final RenderPipeline DEBUG_POINTS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 590 */         }).withLocation("pipeline/debug_points")
/* 591 */       .withVertexShader("core/debug_point")
/* 592 */       .withFragmentShader("core/position_color")
/* 593 */       .withCull(false)
/* 594 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_LINE_WIDTH, VertexFormat.Mode.POINTS)
/* 595 */       .build());
/*     */   
/* 597 */   public static final RenderPipeline DEBUG_FILLED_BOX = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { DEBUG_FILLED_SNIPPET
/* 598 */         }).withLocation("pipeline/debug_filled_box")
/* 599 */       .build());
/*     */   
/* 601 */   public static final RenderPipeline DEBUG_QUADS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { DEBUG_FILLED_SNIPPET
/* 602 */         }).withLocation("pipeline/debug_quads")
/* 603 */       .withCull(false)
/* 604 */       .build());
/*     */   
/* 606 */   public static final RenderPipeline DEBUG_TRIANGLE_FAN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { DEBUG_FILLED_SNIPPET
/* 607 */         }).withLocation("pipeline/debug_triangle_fan")
/* 608 */       .withCull(false)
/* 609 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_FAN)
/* 610 */       .build());
/*     */   
/* 612 */   public static final RenderPipeline WORLD_BORDER = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 613 */         }).withLocation("pipeline/world_border")
/* 614 */       .withVertexShader("core/rendertype_world_border")
/* 615 */       .withFragmentShader("core/rendertype_world_border")
/* 616 */       .withSampler("Sampler0")
/* 617 */       .withBlend(BlendFunction.OVERLAY)
/* 618 */       .withCull(false)
/* 619 */       .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
/* 620 */       .withDepthBias(-3.0F, -3.0F)
/* 621 */       .build());
/*     */   
/* 623 */   public static final RenderPipeline OPAQUE_PARTICLE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { PARTICLE_SNIPPET
/* 624 */         }).withLocation("pipeline/opaque_particle")
/* 625 */       .build());
/*     */   
/* 627 */   public static final RenderPipeline TRANSLUCENT_PARTICLE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { PARTICLE_SNIPPET
/* 628 */         }).withLocation("pipeline/translucent_particle")
/* 629 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 630 */       .build());
/*     */   
/* 632 */   public static final RenderPipeline WEATHER_DEPTH_WRITE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WEATHER_SNIPPET
/* 633 */         }).withLocation("pipeline/weather_depth_write")
/* 634 */       .build());
/*     */   
/* 636 */   public static final RenderPipeline WEATHER_NO_DEPTH_WRITE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WEATHER_SNIPPET
/* 637 */         }).withLocation("pipeline/weather_no_depth_write")
/* 638 */       .withDepthWrite(false)
/* 639 */       .build());
/*     */   
/* 641 */   public static final RenderPipeline SKY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET
/* 642 */         }).withLocation("pipeline/sky")
/* 643 */       .withVertexShader("core/sky")
/* 644 */       .withFragmentShader("core/sky")
/* 645 */       .withDepthWrite(false)
/* 646 */       .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLE_FAN)
/* 647 */       .build());
/*     */   
/* 649 */   public static final RenderPipeline END_SKY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 650 */         }).withLocation("pipeline/end_sky")
/* 651 */       .withVertexShader("core/position_tex_color")
/* 652 */       .withFragmentShader("core/position_tex_color")
/* 653 */       .withSampler("Sampler0")
/* 654 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 655 */       .withDepthWrite(false)
/* 656 */       .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
/* 657 */       .build());
/*     */   
/* 659 */   public static final RenderPipeline SUNRISE_SUNSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 660 */         }).withLocation("pipeline/sunrise_sunset")
/* 661 */       .withVertexShader("core/position_color")
/* 662 */       .withFragmentShader("core/position_color")
/* 663 */       .withBlend(BlendFunction.TRANSLUCENT)
/* 664 */       .withDepthWrite(false)
/* 665 */       .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_FAN)
/* 666 */       .build());
/*     */   
/* 668 */   public static final RenderPipeline STARS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 669 */         }).withLocation("pipeline/stars")
/* 670 */       .withVertexShader("core/stars")
/* 671 */       .withFragmentShader("core/stars")
/* 672 */       .withBlend(BlendFunction.OVERLAY)
/* 673 */       .withDepthWrite(false)
/* 674 */       .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
/* 675 */       .build());
/*     */   
/* 677 */   public static final RenderPipeline CELESTIAL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 678 */         }).withLocation("pipeline/celestial")
/* 679 */       .withVertexShader("core/position_tex")
/* 680 */       .withFragmentShader("core/position_tex")
/* 681 */       .withSampler("Sampler0")
/* 682 */       .withBlend(BlendFunction.OVERLAY)
/* 683 */       .withDepthWrite(false)
/* 684 */       .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
/* 685 */       .build());
/*     */   
/* 687 */   public static final RenderPipeline GUI = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_SNIPPET
/* 688 */         }).withLocation("pipeline/gui")
/* 689 */       .build());
/*     */   
/* 691 */   public static final RenderPipeline GUI_INVERT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_SNIPPET
/* 692 */         }).withLocation("pipeline/gui_invert")
/* 693 */       .withBlend(BlendFunction.INVERT)
/* 694 */       .build());
/*     */   
/* 696 */   public static final RenderPipeline GUI_TEXT_HIGHLIGHT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_SNIPPET
/* 697 */         }).withLocation("pipeline/gui_text_highlight")
/* 698 */       .withBlend(BlendFunction.ADDITIVE)
/* 699 */       .build());
/*     */   
/* 701 */   public static final RenderPipeline GUI_TEXTURED = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 702 */         }).withLocation("pipeline/gui_textured")
/* 703 */       .build());
/*     */   
/* 705 */   public static final RenderPipeline GUI_TEXTURED_PREMULTIPLIED_ALPHA = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 706 */         }).withLocation("pipeline/gui_textured_premultiplied_alpha")
/* 707 */       .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
/* 708 */       .build());
/*     */   
/* 710 */   public static final RenderPipeline BLOCK_SCREEN_EFFECT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 711 */         }).withLocation("pipeline/block_screen_effect")
/* 712 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 713 */       .withDepthWrite(false)
/* 714 */       .build());
/*     */   
/* 716 */   public static final RenderPipeline FIRE_SCREEN_EFFECT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 717 */         }).withLocation("pipeline/fire_screen_effect")
/* 718 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 719 */       .withDepthWrite(false)
/* 720 */       .build());
/*     */   
/* 722 */   public static final RenderPipeline GUI_OPAQUE_TEXTURED_BACKGROUND = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 723 */         }).withLocation("pipeline/gui_opaque_textured_background")
/* 724 */       .withoutBlend()
/* 725 */       .build());
/*     */   
/* 727 */   public static final RenderPipeline GUI_NAUSEA_OVERLAY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 728 */         }).withLocation("pipeline/gui_nausea_overlay")
/* 729 */       .withBlend(BlendFunction.ADDITIVE)
/* 730 */       .build());
/*     */   
/* 732 */   public static final RenderPipeline VIGNETTE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 733 */         }).withLocation("pipeline/vignette")
/* 734 */       .withBlend(new BlendFunction(SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_COLOR))
/* 735 */       .build());
/*     */   
/* 737 */   public static final RenderPipeline CROSSHAIR = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 738 */         }).withLocation("pipeline/crosshair")
/* 739 */       .withBlend(BlendFunction.INVERT)
/* 740 */       .build());
/*     */   
/* 742 */   public static final RenderPipeline MOJANG_LOGO = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET
/* 743 */         }).withLocation("pipeline/mojang_logo")
/* 744 */       .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE))
/* 745 */       .build());
/*     */   
/* 747 */   public static final RenderPipeline ENTITY_OUTLINE_BLIT = register(RenderPipeline.builder(new RenderPipeline.Snippet[0])
/* 748 */       .withLocation("pipeline/entity_outline_blit")
/* 749 */       .withVertexShader("core/screenquad")
/* 750 */       .withFragmentShader("core/blit_screen")
/* 751 */       .withSampler("InSampler")
/* 752 */       .withBlend(BlendFunction.ENTITY_OUTLINE_BLIT)
/* 753 */       .withDepthWrite(false)
/* 754 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 755 */       .withColorWrite(true, false)
/* 756 */       .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
/* 757 */       .build());
/*     */   
/* 759 */   public static final RenderPipeline TRACY_BLIT = register(RenderPipeline.builder(new RenderPipeline.Snippet[0])
/* 760 */       .withLocation("pipeline/tracy_blit")
/* 761 */       .withVertexShader("core/screenquad")
/* 762 */       .withFragmentShader("core/blit_screen")
/* 763 */       .withSampler("InSampler")
/* 764 */       .withDepthWrite(false)
/* 765 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 766 */       .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
/* 767 */       .build());
/*     */   
/* 769 */   public static final RenderPipeline PANORAMA = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_PROJECTION_SNIPPET
/* 770 */         }).withLocation("pipeline/panorama")
/* 771 */       .withVertexShader("core/panorama")
/* 772 */       .withFragmentShader("core/panorama")
/* 773 */       .withSampler("Sampler0")
/* 774 */       .withDepthWrite(false)
/* 775 */       .withColorWrite(true, false)
/* 776 */       .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
/* 777 */       .build());
/*     */   
/* 779 */   public static final RenderPipeline OUTLINE_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { OUTLINE_SNIPPET
/* 780 */         }).withLocation("pipeline/outline_cull")
/* 781 */       .build());
/*     */   
/* 783 */   public static final RenderPipeline OUTLINE_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { OUTLINE_SNIPPET
/* 784 */         }).withLocation("pipeline/outline_no_cull")
/* 785 */       .withCull(false)
/* 786 */       .build());
/*     */   
/* 788 */   public static final RenderPipeline LIGHTMAP = register(RenderPipeline.builder(new RenderPipeline.Snippet[0])
/* 789 */       .withLocation("pipeline/lightmap")
/* 790 */       .withVertexShader("core/screenquad")
/* 791 */       .withFragmentShader("core/lightmap")
/* 792 */       .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
/* 793 */       .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
/* 794 */       .withDepthWrite(false)
/* 795 */       .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 796 */       .build());
/*     */   
/* 798 */   public static final RenderPipeline.Snippet ANIMATE_SPRITE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[0])
/* 799 */     .withVertexShader("core/animate_sprite")
/* 800 */     .withUniform("SpriteAnimationInfo", UniformType.UNIFORM_BUFFER)
/* 801 */     .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
/* 802 */     .withDepthWrite(false)
/* 803 */     .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
/* 804 */     .buildSnippet();
/*     */   
/* 806 */   public static final RenderPipeline ANIMATE_SPRITE_BLIT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ANIMATE_SPRITE_SNIPPET
/* 807 */         }).withFragmentShader("core/animate_sprite_blit")
/* 808 */       .withLocation("pipeline/animate_sprite_blit")
/* 809 */       .withSampler("Sprite")
/* 810 */       .build());
/*     */   
/* 812 */   public static final RenderPipeline ANIMATE_SPRITE_INTERPOLATE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ANIMATE_SPRITE_SNIPPET
/* 813 */         }).withFragmentShader("core/animate_sprite_interpolate")
/* 814 */       .withLocation("pipeline/animate_sprite_interpolate")
/* 815 */       .withSampler("CurrentSprite")
/* 816 */       .withSampler("NextSprite")
/* 817 */       .build());
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/RenderPipelines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */