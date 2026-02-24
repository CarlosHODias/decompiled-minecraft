/*     */ package net.minecraft.client.renderer;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.SkyRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeProbe;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class SkyRenderer implements AutoCloseable {
/*  45 */   private static final Identifier SUN_SPRITE = Identifier.withDefaultNamespace("sun");
/*  46 */   private static final Identifier END_FLASH_SPRITE = Identifier.withDefaultNamespace("end_flash");
/*  47 */   private static final Identifier END_SKY_LOCATION = Identifier.withDefaultNamespace("textures/environment/end_sky.png");
/*     */   
/*     */   private static final float SKY_DISC_RADIUS = 512.0F;
/*     */   
/*     */   private static final int SKY_VERTICES = 10;
/*     */   
/*     */   private static final int STAR_COUNT = 1500;
/*     */   private static final float SUN_SIZE = 30.0F;
/*     */   private static final float SUN_HEIGHT = 100.0F;
/*     */   private static final float MOON_SIZE = 20.0F;
/*     */   private static final float MOON_HEIGHT = 100.0F;
/*     */   private static final int SUNRISE_STEPS = 16;
/*     */   private static final int END_SKY_QUAD_COUNT = 6;
/*     */   private static final float END_FLASH_HEIGHT = 100.0F;
/*     */   private static final float END_FLASH_SCALE = 60.0F;
/*     */   private final TextureAtlas celestialsAtlas;
/*     */   private final GpuBuffer starBuffer;
/*     */   private final GpuBuffer topSkyBuffer;
/*     */   private final GpuBuffer bottomSkyBuffer;
/*     */   private final GpuBuffer endSkyBuffer;
/*     */   private final GpuBuffer sunBuffer;
/*     */   private final GpuBuffer moonBuffer;
/*     */   private final GpuBuffer sunriseBuffer;
/*     */   private final GpuBuffer endFlashBuffer;
/*  71 */   private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/*     */   
/*     */   private final net.minecraft.client.renderer.texture.AbstractTexture endSkyTexture;
/*     */   
/*     */   private int starIndexCount;
/*     */   
/*     */   public SkyRenderer(TextureManager textureManager, net.minecraft.client.resources.model.AtlasManager atlasManager) {
/*  78 */     this.celestialsAtlas = atlasManager.getAtlasOrThrow(net.minecraft.data.AtlasIds.CELESTIALS);
/*     */     
/*  80 */     this.starBuffer = buildStars();
/*  81 */     this.endSkyBuffer = buildEndSky();
/*  82 */     this.endSkyTexture = getTexture(textureManager, END_SKY_LOCATION);
/*  83 */     this.endFlashBuffer = buildEndFlashQuad(this.celestialsAtlas);
/*  84 */     this.sunBuffer = buildSunQuad(this.celestialsAtlas);
/*  85 */     this.moonBuffer = buildMoonPhases(this.celestialsAtlas);
/*  86 */     this.sunriseBuffer = buildSunriseFan();
/*     */     
/*  88 */     ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize()); 
/*  89 */     try { BufferBuilder bufferBuilder = new BufferBuilder(builder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
/*  90 */       buildSkyDisc((VertexConsumer)bufferBuilder, 16.0F);
/*  91 */       MeshData meshData = bufferBuilder.buildOrThrow(); 
/*  92 */       try { this.topSkyBuffer = RenderSystem.getDevice().createBuffer(() -> "Top sky vertex buffer", 32, meshData.vertexBuffer());
/*  93 */         if (meshData != null) meshData.close();  } catch (Throwable throwable) { if (meshData != null)
/*  94 */           try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  bufferBuilder = new BufferBuilder(builder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
/*  95 */       buildSkyDisc((VertexConsumer)bufferBuilder, -16.0F);
/*  96 */       meshData = bufferBuilder.buildOrThrow(); 
/*  97 */       try { this.bottomSkyBuffer = RenderSystem.getDevice().createBuffer(() -> "Bottom sky vertex buffer", 32, meshData.vertexBuffer());
/*  98 */         if (meshData != null) meshData.close();  } catch (Throwable throwable) { if (meshData != null)
/*  99 */           try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (builder != null) builder.close();  } catch (Throwable throwable) { if (builder != null)
/*     */         try { builder.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 103 */      } private net.minecraft.client.renderer.texture.AbstractTexture getTexture(TextureManager textureManager, Identifier location) { return textureManager.getTexture(location); }
/*     */ 
/*     */   
/*     */   private GpuBuffer buildSunriseFan() {
/* 107 */     int vertices = 18;
/* 108 */     int vtxSize = DefaultVertexFormat.POSITION_COLOR.getVertexSize();
/* 109 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(18 * vtxSize); 
/* 110 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
/* 111 */       int centerColor = ARGB.white(1.0F);
/* 112 */       int ringColor = ARGB.white(0.0F);
/* 113 */       bufferBuilder.addVertex(0.0F, 100.0F, 0.0F).setColor(centerColor);
/* 114 */       for (int i = 0; i <= 16; i++) {
/* 115 */         float angle = i * 6.2831855F / 16.0F;
/* 116 */         float sinAngle = Mth.sin(angle);
/* 117 */         float cosAngle = Mth.cos(angle);
/* 118 */         bufferBuilder.addVertex(sinAngle * 120.0F, cosAngle * 120.0F, -cosAngle * 40.0F).setColor(ringColor);
/*     */       } 
/* 120 */       MeshData mesh = bufferBuilder.buildOrThrow(); 
/* 121 */       try { GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Sunrise/Sunset fan", 32, mesh.vertexBuffer());
/* 122 */         if (mesh != null) mesh.close(); 
/* 123 */         if (byteBufferBuilder != null) byteBufferBuilder.close();  return gpuBuffer; } catch (Throwable throwable) { if (mesh != null)
/*     */           try { mesh.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 127 */      } private static GpuBuffer buildSunQuad(TextureAtlas atlas) { return buildCelestialQuad("Sun quad", atlas.getSprite(SUN_SPRITE)); }
/*     */ 
/*     */   
/*     */   private static GpuBuffer buildEndFlashQuad(TextureAtlas atlas) {
/* 131 */     return buildCelestialQuad("End flash quad", atlas.getSprite(END_FLASH_SPRITE));
/*     */   }
/*     */   
/*     */   private static GpuBuffer buildCelestialQuad(String name, TextureAtlasSprite sprite) {
/* 135 */     VertexFormat format = DefaultVertexFormat.POSITION_TEX;
/* 136 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * format.getVertexSize()); 
/* 137 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, format);
/* 138 */       bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV0());
/* 139 */       bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV0());
/* 140 */       bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV1());
/* 141 */       bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV1());
/* 142 */       MeshData mesh = bufferBuilder.buildOrThrow(); 
/* 143 */       try { GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> name, 32, mesh.vertexBuffer());
/* 144 */         if (mesh != null) mesh.close(); 
/* 145 */         if (byteBufferBuilder != null) byteBufferBuilder.close();  return gpuBuffer; } catch (Throwable throwable) { if (mesh != null)
/*     */           try { mesh.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 149 */      } private static GpuBuffer buildMoonPhases(TextureAtlas atlas) { MoonPhase[] phases = MoonPhase.values();
/* 150 */     VertexFormat format = DefaultVertexFormat.POSITION_TEX;
/* 151 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(phases.length * 4 * format.getVertexSize()); 
/* 152 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, format);
/* 153 */       for (MoonPhase phase : phases) {
/* 154 */         TextureAtlasSprite sprite = atlas.getSprite(Identifier.withDefaultNamespace("moon/" + phase.getSerializedName()));
/* 155 */         bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV1());
/* 156 */         bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV1());
/* 157 */         bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0());
/* 158 */         bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0());
/*     */       } 
/* 160 */       MeshData mesh = bufferBuilder.buildOrThrow(); 
/* 161 */       try { GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Moon phases", 32, mesh.vertexBuffer());
/* 162 */         if (mesh != null) mesh.close(); 
/* 163 */         if (byteBufferBuilder != null) byteBufferBuilder.close();  return gpuBuffer; } catch (Throwable throwable) { if (mesh != null)
/*     */           try { mesh.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 167 */      } private GpuBuffer buildStars() { RandomSource random = RandomSource.create(10842L);
/* 168 */     float starDistance = 100.0F;
/*     */     
/* 170 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 1500 * 4); 
/* 171 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
/* 172 */       for (int i = 0; i < 1500; i++) {
/* 173 */         float x = random.nextFloat() * 2.0F - 1.0F;
/* 174 */         float y = random.nextFloat() * 2.0F - 1.0F;
/* 175 */         float z = random.nextFloat() * 2.0F - 1.0F;
/*     */         
/* 177 */         float starSize = 0.15F + random.nextFloat() * 0.1F;
/*     */         
/* 179 */         float lengthSq = Mth.lengthSquared(x, y, z);
/* 180 */         if (lengthSq > 0.010000001F && lengthSq < 1.0F) {
/*     */ 
/*     */ 
/*     */           
/* 184 */           Vector3f starCenter = new Vector3f(x, y, z).normalize(100.0F);
/*     */ 
/*     */           
/* 187 */           float zRot = (float)(random.nextDouble() * 3.1415927410125732D * 2.0D);
/*     */           
/* 189 */           Matrix3f rotation = new Matrix3f()
/* 190 */             .rotateTowards((Vector3fc)new Vector3f((Vector3fc)starCenter).negate(), (Vector3fc)new Vector3f(0.0F, 1.0F, 0.0F))
/* 191 */             .rotateZ(-zRot);
/*     */           
/* 193 */           bufferBuilder.addVertex((Vector3fc)new Vector3f(starSize, -starSize, 0.0F).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/* 194 */           bufferBuilder.addVertex((Vector3fc)new Vector3f(starSize, starSize, 0.0F).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/* 195 */           bufferBuilder.addVertex((Vector3fc)new Vector3f(-starSize, starSize, 0.0F).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/* 196 */           bufferBuilder.addVertex((Vector3fc)new Vector3f(-starSize, -starSize, 0.0F).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/*     */         } 
/* 198 */       }  MeshData mesh = bufferBuilder.buildOrThrow(); 
/* 199 */       try { this.starIndexCount = mesh.drawState().indexCount();
/* 200 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
/* 201 */         if (mesh != null) mesh.close(); 
/* 202 */         if (byteBufferBuilder != null) byteBufferBuilder.close();  return gpuBuffer; } catch (Throwable throwable) { if (mesh != null)
/*     */           try { mesh.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 206 */      } private void buildSkyDisc(VertexConsumer builder, float yy) { float x = Math.signum(yy) * 512.0F;
/*     */     
/* 208 */     builder.addVertex(0.0F, yy, 0.0F);
/* 209 */     for (int i = -180; i <= 180; i += 45) {
/* 210 */       builder.addVertex(x * Mth.cos((i * 0.017453292F)), yy, 512.0F * Mth.sin((i * 0.017453292F)));
/*     */     } }
/*     */ 
/*     */   
/*     */   private static GpuBuffer buildEndSky() {
/* 215 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(24 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize()); 
/* 216 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
/* 217 */       for (int i = 0; i < 6; i++) {
/* 218 */         Matrix4f pose = new Matrix4f();
/* 219 */         switch (i) { case 1:
/* 220 */             pose.rotationX(1.5707964F); break;
/* 221 */           case 2: pose.rotationX(-1.5707964F); break;
/* 222 */           case 3: pose.rotationX(3.1415927F); break;
/* 223 */           case 4: pose.rotationZ(1.5707964F); break;
/* 224 */           case 5: pose.rotationZ(-1.5707964F);
/*     */             break; }
/*     */         
/* 227 */         bufferBuilder.addVertex((Matrix4fc)pose, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(-14145496);
/* 228 */         bufferBuilder.addVertex((Matrix4fc)pose, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(-14145496);
/* 229 */         bufferBuilder.addVertex((Matrix4fc)pose, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(-14145496);
/* 230 */         bufferBuilder.addVertex((Matrix4fc)pose, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(-14145496);
/*     */       } 
/*     */       
/* 233 */       MeshData meshData = bufferBuilder.buildOrThrow(); 
/* 234 */       try { GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "End sky vertex buffer", 40, meshData.vertexBuffer());
/* 235 */         if (meshData != null) meshData.close(); 
/* 236 */         if (byteBufferBuilder != null) byteBufferBuilder.close();  return gpuBuffer; } catch (Throwable throwable) { if (meshData != null)
/*     */           try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 240 */      } public void renderSkyDisc(int skyColor) { GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)ARGB.vector4fFromARGB32(skyColor), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/* 241 */     GpuTextureView colorTexture = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 242 */     GpuTextureView depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 243 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky disc", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 244 */     try { renderPass.setPipeline(RenderPipelines.SKY);
/* 245 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 246 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 247 */       renderPass.setVertexBuffer(0, this.topSkyBuffer);
/* 248 */       renderPass.draw(0, 10);
/* 249 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 253 */      } public void extractRenderState(ClientLevel level, float partialTicks, Camera camera, SkyRenderState state) { state.skybox = level.dimensionType().skybox();
/* 254 */     if (state.skybox == DimensionType.Skybox.NONE) {
/*     */       return;
/*     */     }
/* 257 */     if (state.skybox == DimensionType.Skybox.END) {
/* 258 */       EndFlashState endFlashState = level.endFlashState();
/* 259 */       if (endFlashState == null) {
/*     */         return;
/*     */       }
/* 262 */       state.endFlashIntensity = endFlashState.getIntensity(partialTicks);
/* 263 */       state.endFlashXAngle = endFlashState.getXAngle();
/* 264 */       state.endFlashYAngle = endFlashState.getYAngle();
/*     */       
/*     */       return;
/*     */     } 
/* 268 */     EnvironmentAttributeProbe attributeProbe = camera.attributeProbe();
/* 269 */     state.sunAngle = (Float)attributeProbe.getValue(EnvironmentAttributes.SUN_ANGLE, partialTicks) * 0.017453292F;
/* 270 */     state.moonAngle = (Float)attributeProbe.getValue(EnvironmentAttributes.MOON_ANGLE, partialTicks) * 0.017453292F;
/* 271 */     state.starAngle = (Float)attributeProbe.getValue(EnvironmentAttributes.STAR_ANGLE, partialTicks) * 0.017453292F;
/* 272 */     state.rainBrightness = 1.0F - level.getRainLevel(partialTicks);
/* 273 */     state.starBrightness = (Float)attributeProbe.getValue(EnvironmentAttributes.STAR_BRIGHTNESS, partialTicks);
/* 274 */     state.sunriseAndSunsetColor = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, partialTicks);
/* 275 */     state.moonPhase = (MoonPhase)attributeProbe.getValue(EnvironmentAttributes.MOON_PHASE, partialTicks);
/* 276 */     state.skyColor = (Integer)attributeProbe.getValue(EnvironmentAttributes.SKY_COLOR, partialTicks);
/* 277 */     state.shouldRenderDarkDisc = shouldRenderDarkDisc(partialTicks, level); }
/*     */ 
/*     */   
/*     */   private boolean shouldRenderDarkDisc(float deltaPartialTick, ClientLevel level) {
/* 281 */     return (((Minecraft.getInstance()).player.getEyePosition(deltaPartialTick)).y - level.getLevelData().getHorizonHeight((net.minecraft.world.level.LevelHeightAccessor)level) < 0.0D);
/*     */   }
/*     */   
/*     */   public void renderDarkDisc() {
/* 285 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 286 */     modelViewStack.pushMatrix();
/* 287 */     modelViewStack.translate(0.0F, 12.0F, 0.0F);
/* 288 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, (Vector4fc)new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */     
/* 290 */     GpuTextureView colorTexture = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 291 */     GpuTextureView depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 292 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky dark", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 293 */     try { renderPass.setPipeline(RenderPipelines.SKY);
/* 294 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 295 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 296 */       renderPass.setVertexBuffer(0, this.bottomSkyBuffer);
/* 297 */       renderPass.draw(0, 10);
/* 298 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 300 */      modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   public void renderSunMoonAndStars(PoseStack poseStack, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase, float rainBrightness, float starBrightness) {
/* 304 */     poseStack.pushPose();
/* 305 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-90.0F));
/*     */     
/* 307 */     poseStack.pushPose();
/* 308 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(sunAngle));
/* 309 */     renderSun(rainBrightness, poseStack);
/* 310 */     poseStack.popPose();
/*     */     
/* 312 */     poseStack.pushPose();
/* 313 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(moonAngle));
/* 314 */     renderMoon(moonPhase, rainBrightness, poseStack);
/* 315 */     poseStack.popPose();
/*     */     
/* 317 */     if (starBrightness > 0.0F) {
/* 318 */       poseStack.pushPose();
/* 319 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotation(starAngle));
/* 320 */       renderStars(starBrightness, poseStack);
/* 321 */       poseStack.popPose();
/*     */     } 
/*     */     
/* 324 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void renderSun(float rainBrightness, PoseStack poseStack) {
/* 328 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 329 */     modelViewStack.pushMatrix();
/* 330 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 331 */     modelViewStack.translate(0.0F, 100.0F, 0.0F);
/* 332 */     modelViewStack.scale(30.0F, 1.0F, 30.0F);
/*     */     
/* 334 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 342 */     GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 343 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
/*     */     
/* 345 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
/* 346 */       .createRenderPass(() -> "Sky sun", color, OptionalInt.empty(), depth, OptionalDouble.empty()); 
/* 347 */     try { renderPass.setPipeline(RenderPipelines.CELESTIAL);
/* 348 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 349 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 350 */       renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
/* 351 */       renderPass.setVertexBuffer(0, this.sunBuffer);
/* 352 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 353 */       renderPass.drawIndexed(0, 0, 6, 1);
/* 354 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 356 */      modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderMoon(MoonPhase moonPhase, float rainBrightness, PoseStack poseStack) {
/* 360 */     int baseVertex = moonPhase.index() * 4;
/*     */     
/* 362 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 363 */     modelViewStack.pushMatrix();
/* 364 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 365 */     modelViewStack.translate(0.0F, 100.0F, 0.0F);
/* 366 */     modelViewStack.scale(20.0F, 1.0F, 20.0F);
/*     */     
/* 368 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 375 */     GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 376 */     GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 377 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
/*     */     
/* 379 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
/* 380 */       .createRenderPass(() -> "Sky moon", color, OptionalInt.empty(), depth, OptionalDouble.empty()); 
/* 381 */     try { renderPass.setPipeline(RenderPipelines.CELESTIAL);
/* 382 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 383 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 384 */       renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
/* 385 */       renderPass.setVertexBuffer(0, this.moonBuffer);
/* 386 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 387 */       renderPass.drawIndexed(baseVertex, 0, 6, 1);
/* 388 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 390 */      modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderStars(float starBrightness, PoseStack poseStack) {
/* 394 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 395 */     modelViewStack.pushMatrix();
/* 396 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 397 */     RenderPipeline renderPipeline = RenderPipelines.STARS;
/*     */     
/* 399 */     GpuTextureView colorTexture = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 400 */     GpuTextureView depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 401 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(this.starIndexCount);
/* 402 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, (Vector4fc)new Vector4f(starBrightness, starBrightness, starBrightness, starBrightness), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */     
/* 404 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Stars", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 405 */     try { renderPass.setPipeline(renderPipeline);
/* 406 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 407 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 408 */       renderPass.setVertexBuffer(0, this.starBuffer);
/* 409 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 410 */       renderPass.drawIndexed(0, 0, this.starIndexCount, 1);
/* 411 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/* 412 */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   public void renderSunriseAndSunset(PoseStack poseStack, float sunAngle, int sunriseAndSunsetColor) {
/* 416 */     float alpha = ARGB.alphaFloat(sunriseAndSunsetColor);
/* 417 */     if (alpha <= 0.001F) {
/*     */       return;
/*     */     }
/*     */     
/* 421 */     poseStack.pushPose();
/* 422 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0F));
/* 423 */     float angle = (Mth.sin(sunAngle) < 0.0F) ? 180.0F : 0.0F;
/* 424 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(angle + 90.0F));
/*     */     
/* 426 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 427 */     modelViewStack.pushMatrix();
/* 428 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 429 */     modelViewStack.scale(1.0F, 1.0F, alpha);
/*     */     
/* 431 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, 
/*     */         
/* 433 */         (Vector4fc)ARGB.vector4fFromARGB32(sunriseAndSunsetColor), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 438 */     GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 439 */     GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/*     */     
/* 441 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
/* 442 */       .createRenderPass(() -> "Sunrise sunset", color, OptionalInt.empty(), depth, OptionalDouble.empty()); 
/* 443 */     try { renderPass.setPipeline(RenderPipelines.SUNRISE_SUNSET);
/* 444 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 445 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 446 */       renderPass.setVertexBuffer(0, this.sunriseBuffer);
/* 447 */       renderPass.draw(0, 18);
/* 448 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 450 */      modelViewStack.popMatrix();
/* 451 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void renderEndSky() {
/* 455 */     RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/* 456 */     GpuBuffer indexBuffer = autoIndices.getBuffer(36);
/*     */     
/* 458 */     GpuTextureView colorTexture = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 459 */     GpuTextureView depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 460 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */     
/* 462 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 463 */     try { renderPass.setPipeline(RenderPipelines.END_SKY);
/* 464 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 465 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 466 */       renderPass.bindTexture("Sampler0", this.endSkyTexture.getTextureView(), this.endSkyTexture.getSampler());
/* 467 */       renderPass.setVertexBuffer(0, this.endSkyBuffer);
/* 468 */       renderPass.setIndexBuffer(indexBuffer, autoIndices.type());
/* 469 */       renderPass.drawIndexed(0, 0, 36, 1);
/* 470 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 474 */      } public void renderEndFlash(PoseStack poseStack, float intensity, float xAngle, float yAngle) { poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - yAngle));
/* 475 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F - xAngle));
/*     */     
/* 477 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 478 */     modelViewStack.pushMatrix();
/* 479 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 480 */     modelViewStack.translate(0.0F, 100.0F, 0.0F);
/* 481 */     modelViewStack.scale(60.0F, 1.0F, 60.0F);
/*     */     
/* 483 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, (Vector4fc)new Vector4f(intensity, intensity, intensity, intensity), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 490 */     GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
/* 491 */     GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
/* 492 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
/*     */     
/* 494 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
/* 495 */       .createRenderPass(() -> "End flash", color, OptionalInt.empty(), depth, OptionalDouble.empty()); 
/* 496 */     try { renderPass.setPipeline(RenderPipelines.CELESTIAL);
/* 497 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 498 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 499 */       renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
/* 500 */       renderPass.setVertexBuffer(0, this.endFlashBuffer);
/* 501 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 502 */       renderPass.drawIndexed(0, 0, 6, 1);
/* 503 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 505 */      modelViewStack.popMatrix(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 510 */     this.sunBuffer.close();
/* 511 */     this.moonBuffer.close();
/* 512 */     this.starBuffer.close();
/* 513 */     this.topSkyBuffer.close();
/* 514 */     this.bottomSkyBuffer.close();
/* 515 */     this.endSkyBuffer.close();
/* 516 */     this.sunriseBuffer.close();
/* 517 */     this.endFlashBuffer.close();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SkyRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */