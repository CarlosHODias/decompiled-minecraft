/*     */ package net.minecraft.client.renderer;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.resource.CrossFrameResourcePool;
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.math.Axis;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.Screenshot;
/*     */ import net.minecraft.client.TextureFilteringMethod;
/*     */ import net.minecraft.client.entity.ClientAvatarState;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.font.ActiveArea;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.render.GuiRenderer;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.gui.render.pip.GuiBannerResultRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiBookModelRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiSignRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiSkinRenderer;
/*     */ import net.minecraft.client.gui.render.state.ColoredRectangleRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiElementRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiTextRenderState;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.AbstractClientPlayer;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.fog.FogRenderer;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.state.LevelRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.AtlasManager;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameRenderer implements AutoCloseable, TrackedWaypoint.Projector {
/* 104 */   private static final Identifier BLUR_POST_CHAIN_ID = Identifier.withDefaultNamespace("blur");
/*     */   
/*     */   public static final int MAX_BLUR_RADIUS = 10;
/*     */   
/* 108 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final float PROJECTION_Z_NEAR = 0.05F;
/*     */   
/*     */   public static final float PROJECTION_3D_HUD_Z_FAR = 100.0F;
/*     */   
/*     */   private static final float PORTAL_SPINNING_SPEED = 20.0F;
/*     */   
/*     */   private static final float NAUSEA_SPINNING_SPEED = 7.0F;
/*     */   private final Minecraft minecraft;
/* 118 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   private float renderDistance;
/*     */   
/*     */   public final ItemInHandRenderer itemInHandRenderer;
/*     */   
/*     */   private final ScreenEffectRenderer screenEffectRenderer;
/*     */   
/*     */   private final RenderBuffers renderBuffers;
/*     */   
/*     */   private float spinningEffectTime;
/*     */   
/*     */   private float spinningEffectSpeed;
/*     */   private float fovModifier;
/*     */   private float oldFovModifier;
/*     */   private float darkenWorldAmount;
/*     */   private float darkenWorldAmountO;
/*     */   private boolean renderBlockOutline = true;
/*     */   private long lastScreenshotAttempt;
/*     */   private boolean hasWorldScreenshot;
/* 138 */   private long lastActiveTime = Util.getMillis();
/*     */ 
/*     */   
/*     */   private final LightTexture lightTexture;
/*     */   
/* 143 */   private final OverlayTexture overlayTexture = new OverlayTexture();
/*     */   
/*     */   private PanoramicScreenshotParameters panoramicScreenshotParameters;
/*     */   
/* 147 */   protected final CubeMap cubeMap = new CubeMap(Identifier.withDefaultNamespace("textures/gui/title/background/panorama"));
/* 148 */   protected final PanoramaRenderer panorama = new PanoramaRenderer(this.cubeMap);
/*     */   
/* 150 */   private final CrossFrameResourcePool resourcePool = new CrossFrameResourcePool(3);
/* 151 */   private final FogRenderer fogRenderer = new FogRenderer();
/*     */   private final GuiRenderer guiRenderer;
/*     */   private final GuiRenderState guiRenderState;
/* 154 */   private final LevelRenderState levelRenderState = new LevelRenderState();
/*     */   
/*     */   private final SubmitNodeStorage submitNodeStorage;
/*     */   private final FeatureRenderDispatcher featureRenderDispatcher;
/*     */   private Identifier postEffectId;
/*     */   private boolean effectActive;
/* 160 */   private final Camera mainCamera = new Camera();
/* 161 */   private final Lighting lighting = new Lighting();
/* 162 */   private final GlobalSettingsUniform globalSettingsUniform = new GlobalSettingsUniform();
/* 163 */   private final PerspectiveProjectionMatrixBuffer levelProjectionMatrixBuffer = new PerspectiveProjectionMatrixBuffer("level");
/* 164 */   private final CachedPerspectiveProjectionMatrixBuffer hud3dProjectionMatrixBuffer = new CachedPerspectiveProjectionMatrixBuffer("3d hud", 0.05F, 100.0F);
/*     */   
/*     */   public GameRenderer(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, RenderBuffers renderBuffers, BlockRenderDispatcher blockRenderer) {
/* 167 */     this.minecraft = minecraft;
/* 168 */     this.itemInHandRenderer = itemInHandRenderer;
/* 169 */     this.lightTexture = new LightTexture(this, minecraft);
/* 170 */     this.renderBuffers = renderBuffers;
/* 171 */     this.guiRenderState = new GuiRenderState();
/* 172 */     MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
/* 173 */     AtlasManager atlasManager = minecraft.getAtlasManager();
/* 174 */     this.submitNodeStorage = new SubmitNodeStorage();
/* 175 */     this.featureRenderDispatcher = new FeatureRenderDispatcher(this.submitNodeStorage, blockRenderer, bufferSource, atlasManager, renderBuffers.outlineBufferSource(), renderBuffers.crumblingBufferSource(), minecraft.font);
/* 176 */     this.guiRenderer = new GuiRenderer(this.guiRenderState, bufferSource, this.submitNodeStorage, this.featureRenderDispatcher, List.of(new GuiEntityRenderer(bufferSource, 
/* 177 */             minecraft.getEntityRenderDispatcher()), new GuiSkinRenderer(bufferSource), new GuiBookModelRenderer(bufferSource), new GuiBannerResultRenderer(bufferSource, (MaterialSet)atlasManager), new GuiSignRenderer(bufferSource, (MaterialSet)atlasManager), new net.minecraft.client.gui.render.pip.GuiProfilerChartRenderer(bufferSource)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     this.screenEffectRenderer = new ScreenEffectRenderer(minecraft, (MaterialSet)atlasManager, bufferSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 189 */     this.globalSettingsUniform.close();
/* 190 */     this.lightTexture.close();
/* 191 */     this.overlayTexture.close();
/* 192 */     this.resourcePool.close();
/* 193 */     this.guiRenderer.close();
/* 194 */     this.levelProjectionMatrixBuffer.close();
/* 195 */     this.hud3dProjectionMatrixBuffer.close();
/* 196 */     this.lighting.close();
/* 197 */     this.cubeMap.close();
/* 198 */     this.fogRenderer.close();
/* 199 */     this.featureRenderDispatcher.close();
/*     */   }
/*     */   
/*     */   public SubmitNodeStorage getSubmitNodeStorage() {
/* 203 */     return this.submitNodeStorage;
/*     */   }
/*     */   
/*     */   public FeatureRenderDispatcher getFeatureRenderDispatcher() {
/* 207 */     return this.featureRenderDispatcher;
/*     */   }
/*     */   
/*     */   public LevelRenderState getLevelRenderState() {
/* 211 */     return this.levelRenderState;
/*     */   }
/*     */   
/*     */   public void setRenderBlockOutline(boolean renderBlockOutline) {
/* 215 */     this.renderBlockOutline = renderBlockOutline;
/*     */   }
/*     */   
/*     */   public void setPanoramicScreenshotParameters(PanoramicScreenshotParameters panoramicScreenshotParameters) {
/* 219 */     this.panoramicScreenshotParameters = panoramicScreenshotParameters;
/*     */   }
/*     */   
/*     */   public PanoramicScreenshotParameters getPanoramicScreenshotParameters() {
/* 223 */     return this.panoramicScreenshotParameters;
/*     */   }
/*     */   
/*     */   public boolean isPanoramicMode() {
/* 227 */     return (this.panoramicScreenshotParameters != null);
/*     */   }
/*     */   
/*     */   public void clearPostEffect() {
/* 231 */     this.postEffectId = null;
/* 232 */     this.effectActive = false;
/*     */   }
/*     */   
/*     */   public void togglePostEffect() {
/* 236 */     this.effectActive = !this.effectActive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkEntityPostEffect(Entity cameraEntity) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: astore_2
/*     */     //   2: iconst_0
/*     */     //   3: istore_3
/*     */     //   4: aload_2
/*     */     //   5: iload_3
/*     */     //   6: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   11: tableswitch default -> 97, -1 -> 97, 0 -> 40, 1 -> 59, 2 -> 78
/*     */     //   40: aload_2
/*     */     //   41: checkcast net/minecraft/world/entity/monster/Creeper
/*     */     //   44: astore #4
/*     */     //   46: aload_0
/*     */     //   47: ldc_w 'creeper'
/*     */     //   50: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */     //   53: invokevirtual setPostEffect : (Lnet/minecraft/resources/Identifier;)V
/*     */     //   56: goto -> 101
/*     */     //   59: aload_2
/*     */     //   60: checkcast net/minecraft/world/entity/monster/spider/Spider
/*     */     //   63: astore #5
/*     */     //   65: aload_0
/*     */     //   66: ldc_w 'spider'
/*     */     //   69: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */     //   72: invokevirtual setPostEffect : (Lnet/minecraft/resources/Identifier;)V
/*     */     //   75: goto -> 101
/*     */     //   78: aload_2
/*     */     //   79: checkcast net/minecraft/world/entity/monster/EnderMan
/*     */     //   82: astore #6
/*     */     //   84: aload_0
/*     */     //   85: ldc_w 'invert'
/*     */     //   88: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */     //   91: invokevirtual setPostEffect : (Lnet/minecraft/resources/Identifier;)V
/*     */     //   94: goto -> 101
/*     */     //   97: aload_0
/*     */     //   98: invokevirtual clearPostEffect : ()V
/*     */     //   101: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #240	-> 0
/*     */     //   #241	-> 40
/*     */     //   #242	-> 59
/*     */     //   #243	-> 78
/*     */     //   #244	-> 97
/*     */     //   #246	-> 101
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   46	13	4	ignored	Lnet/minecraft/world/entity/monster/Creeper;
/*     */     //   65	13	5	ignored	Lnet/minecraft/world/entity/monster/spider/Spider;
/*     */     //   84	13	6	ignored	Lnet/minecraft/world/entity/monster/EnderMan;
/*     */     //   0	102	0	this	Lnet/minecraft/client/renderer/GameRenderer;
/*     */     //   0	102	1	cameraEntity	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setPostEffect(Identifier id) {
/* 249 */     this.postEffectId = id;
/* 250 */     this.effectActive = true;
/*     */   }
/*     */   
/*     */   public void processBlurEffect() {
/* 254 */     PostChain postChain = this.minecraft.getShaderManager().getPostChain(BLUR_POST_CHAIN_ID, LevelTargetBundle.MAIN_TARGETS);
/* 255 */     if (postChain != null) {
/* 256 */       postChain.process(this.minecraft.getMainRenderTarget(), (GraphicsResourceAllocator)this.resourcePool);
/*     */     }
/*     */   }
/*     */   
/*     */   public void preloadUiShader(ResourceProvider resourceProvider) {
/* 261 */     GpuDevice device = RenderSystem.getDevice(); ShaderSource shaderSource = (id, type) -> { Identifier location = type.idConverter().idToFile(id); try { Reader reader = resourceProvider.getResourceOrThrow(location).openAsReader(); 
/*     */           try { String str = IOUtils.toString(reader); if (reader != null)
/*     */               reader.close();  return str; }
/* 264 */           catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */            }
/* 266 */         catch (IOException exception)
/*     */         { LOGGER.error("Coudln't preload {} shader {}: {}", new Object[] { type, id, exception });
/*     */           return null; }
/*     */       
/*     */       };
/* 271 */     device.precompilePipeline(RenderPipelines.GUI, shaderSource);
/* 272 */     device.precompilePipeline(RenderPipelines.GUI_TEXTURED, shaderSource);
/* 273 */     if (TracyClient.isAvailable()) {
/* 274 */       device.precompilePipeline(RenderPipelines.TRACY_BLIT, shaderSource);
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick() {
/* 279 */     tickFov();
/* 280 */     this.lightTexture.tick();
/*     */     
/* 282 */     LocalPlayer player = this.minecraft.player;
/* 283 */     if (this.minecraft.getCameraEntity() == null) {
/* 284 */       this.minecraft.setCameraEntity((Entity)player);
/*     */     }
/* 286 */     this.mainCamera.tick();
/* 287 */     this.itemInHandRenderer.tick();
/*     */     
/* 289 */     float portalIntensity = player.portalEffectIntensity;
/* 290 */     float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, 1.0F);
/* 291 */     if (portalIntensity > 0.0F || nauseaIntensity > 0.0F) {
/* 292 */       this.spinningEffectSpeed = (portalIntensity * 20.0F + nauseaIntensity * 7.0F) / (portalIntensity + nauseaIntensity);
/* 293 */       this.spinningEffectTime += this.spinningEffectSpeed;
/*     */     } else {
/* 295 */       this.spinningEffectSpeed = 0.0F;
/*     */     } 
/*     */     
/* 298 */     if (!this.minecraft.level.tickRateManager().runsNormally()) {
/*     */       return;
/*     */     }
/*     */     
/* 302 */     this.darkenWorldAmountO = this.darkenWorldAmount;
/* 303 */     if (this.minecraft.gui.getBossOverlay().shouldDarkenScreen()) {
/* 304 */       this.darkenWorldAmount += 0.05F;
/* 305 */       if (this.darkenWorldAmount > 1.0F) {
/* 306 */         this.darkenWorldAmount = 1.0F;
/*     */       }
/* 308 */     } else if (this.darkenWorldAmount > 0.0F) {
/* 309 */       this.darkenWorldAmount -= 0.0125F;
/*     */     } 
/*     */     
/* 312 */     this.screenEffectRenderer.tick();
/*     */     
/* 314 */     ProfilerFiller profiler = Profiler.get();
/* 315 */     profiler.push("levelRenderer");
/* 316 */     this.minecraft.levelRenderer.tick(this.mainCamera);
/* 317 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public Identifier currentPostEffect() {
/* 321 */     return this.postEffectId;
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/* 325 */     this.resourcePool.clear();
/* 326 */     this.minecraft.levelRenderer.resize(width, height);
/*     */   }
/*     */   
/*     */   public void pick(float a) {
/* 330 */     Entity cameraEntity = this.minecraft.getCameraEntity();
/* 331 */     if (cameraEntity == null) {
/*     */       return;
/*     */     }
/* 334 */     if (this.minecraft.level == null || this.minecraft.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 338 */     Profiler.get().push("pick");
/* 339 */     this.minecraft.hitResult = this.minecraft.player.raycastHitResult(a, cameraEntity);
/* 340 */     HitResult hitResult = this.minecraft.hitResult; EntityHitResult entityHitResult = (EntityHitResult)hitResult; this.minecraft.crosshairPickEntity = (hitResult instanceof EntityHitResult) ? entityHitResult.getEntity() : null;
/* 341 */     Profiler.get().pop();
/*     */   }
/*     */   
/*     */   private void tickFov() {
/*     */     float targetFovModifier;
/* 346 */     Entity entity = this.minecraft.getCameraEntity(); if (entity instanceof AbstractClientPlayer) { AbstractClientPlayer player = (AbstractClientPlayer)entity;
/* 347 */       Options options = this.minecraft.options;
/* 348 */       boolean firstPerson = options.getCameraType().isFirstPerson();
/* 349 */       float effectScale = ((Double)options.fovEffectScale().get()).floatValue();
/* 350 */       targetFovModifier = player.getFieldOfViewModifier(firstPerson, effectScale); }
/*     */     else
/* 352 */     { targetFovModifier = 1.0F; }
/*     */ 
/*     */     
/* 355 */     this.oldFovModifier = this.fovModifier;
/* 356 */     this.fovModifier += (targetFovModifier - this.fovModifier) * 0.5F;
/* 357 */     this.fovModifier = Mth.clamp(this.fovModifier, 0.1F, 1.5F);
/*     */   }
/*     */   
/*     */   private float getFov(Camera camera, float partialTicks, boolean applyEffects) {
/* 361 */     if (isPanoramicMode()) {
/* 362 */       return 90.0F;
/*     */     }
/*     */     
/* 365 */     float fov = 70.0F;
/* 366 */     if (applyEffects) {
/* 367 */       fov = (Integer)this.minecraft.options.fov().get();
/* 368 */       fov *= Mth.lerp(partialTicks, this.oldFovModifier, this.fovModifier);
/*     */     } 
/*     */     
/* 371 */     Entity entity = camera.entity(); if (entity instanceof LivingEntity) { LivingEntity cameraEntity = (LivingEntity)entity; if (cameraEntity.isDeadOrDying()) {
/* 372 */         float duration = Math.min(cameraEntity.deathTime + partialTicks, 20.0F);
/* 373 */         fov /= (1.0F - 500.0F / (duration + 500.0F)) * 2.0F + 1.0F;
/*     */       }  }
/*     */     
/* 376 */     FogType state = camera.getFluidInCamera();
/* 377 */     if (state == FogType.LAVA || state == FogType.WATER) {
/* 378 */       float effectScale = ((Double)this.minecraft.options.fovEffectScale().get()).floatValue();
/* 379 */       fov *= Mth.lerp(effectScale, 1.0F, 0.85714287F);
/*     */     } 
/*     */     
/* 382 */     return fov;
/*     */   }
/*     */   
/*     */   private void bobHurt(PoseStack poseStack, float a) {
/* 386 */     Entity entity = this.minecraft.getCameraEntity(); if (entity instanceof LivingEntity) { LivingEntity camera = (LivingEntity)entity;
/*     */       
/* 388 */       float hurt = camera.hurtTime - a;
/*     */       
/* 390 */       if (camera.isDeadOrDying()) {
/* 391 */         float duration = Math.min(camera.deathTime + a, 20.0F);
/*     */         
/* 393 */         poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(40.0F - 8000.0F / (duration + 200.0F)));
/*     */       } 
/*     */       
/* 396 */       if (hurt < 0.0F) {
/*     */         return;
/*     */       }
/* 399 */       hurt /= camera.hurtDuration;
/* 400 */       hurt = Mth.sin((hurt * hurt * hurt * hurt * 3.1415927F));
/*     */       
/* 402 */       float rr = camera.getHurtDir();
/*     */       
/* 404 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-rr));
/* 405 */       float tiltAmount = (float)(-hurt * 14.0D * (Double)this.minecraft.options.damageTiltStrength().get());
/* 406 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(tiltAmount));
/* 407 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(rr)); }
/*     */   
/*     */   }
/*     */   private void bobView(PoseStack poseStack, float a) {
/*     */     AbstractClientPlayer player;
/* 412 */     Entity entity = this.minecraft.getCameraEntity(); if (entity instanceof AbstractClientPlayer) { player = (AbstractClientPlayer)entity; }
/*     */     else
/*     */     { return; }
/*     */     
/* 416 */     ClientAvatarState avatarState = player.avatarState();
/* 417 */     float b = avatarState.getBackwardsInterpolatedWalkDistance(a);
/* 418 */     float bob = avatarState.getInterpolatedBob(a);
/* 419 */     poseStack.translate(Mth.sin((b * 3.1415927F)) * bob * 0.5F, -Math.abs(Mth.cos((b * 3.1415927F)) * bob), 0.0F);
/* 420 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(Mth.sin((b * 3.1415927F)) * bob * 3.0F));
/* 421 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Math.abs(Mth.cos((b * 3.1415927F - 0.2F)) * bob) * 5.0F));
/*     */   }
/*     */   
/*     */   private void renderItemInHand(float deltaPartialTick, boolean isSleeping, Matrix4f modelViewMatrix) {
/* 425 */     if (isPanoramicMode()) {
/*     */       return;
/*     */     }
/*     */     
/* 429 */     this.featureRenderDispatcher.renderAllFeatures();
/* 430 */     this.renderBuffers.bufferSource().endBatch();
/*     */     
/* 432 */     PoseStack poseStack = new PoseStack();
/* 433 */     poseStack.pushPose();
/* 434 */     poseStack.mulPose((Matrix4fc)modelViewMatrix.invert(new Matrix4f()));
/* 435 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 436 */     modelViewStack.pushMatrix().mul((Matrix4fc)modelViewMatrix);
/*     */     
/* 438 */     bobHurt(poseStack, deltaPartialTick);
/* 439 */     if ((Boolean)this.minecraft.options.bobView().get()) {
/* 440 */       bobView(poseStack, deltaPartialTick);
/*     */     }
/*     */     
/* 443 */     if (this.minecraft.options.getCameraType().isFirstPerson() && !isSleeping && !this.minecraft.options.hideGui && this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
/* 444 */       this.itemInHandRenderer.renderHandsWithItems(deltaPartialTick, poseStack, this.minecraft.gameRenderer.getSubmitNodeStorage(), this.minecraft.player, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords((Entity)this.minecraft.player, deltaPartialTick));
/*     */     }
/*     */     
/* 447 */     modelViewStack.popMatrix();
/* 448 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public Matrix4f getProjectionMatrix(float fov) {
/* 452 */     Matrix4f projection = new Matrix4f();
/* 453 */     return projection.perspective(fov * 0.017453292F, this.minecraft.getWindow().getWidth() / this.minecraft.getWindow().getHeight(), 0.05F, getDepthFar());
/*     */   }
/*     */   
/*     */   public float getDepthFar() {
/* 457 */     return Math.max(this.renderDistance * 4.0F, ((Integer)this.minecraft.options.cloudRange().get() * 16));
/*     */   }
/*     */   
/*     */   public static float getNightVisionScale(LivingEntity camera, float a) {
/* 461 */     MobEffectInstance nightVision = camera.getEffect(MobEffects.NIGHT_VISION);
/* 462 */     if (!nightVision.endsWithin(200)) {
/* 463 */       return 1.0F;
/*     */     }
/* 465 */     return 0.7F + Mth.sin(((nightVision.getDuration() - a) * 3.1415927F * 0.2F)) * 0.3F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(DeltaTracker deltaTracker, boolean renderLevel) {
/* 470 */     if (this.minecraft.isWindowActive() || !this.minecraft.options.pauseOnLostFocus || ((Boolean)this.minecraft.options.touchscreen().get() && this.minecraft.mouseHandler.isRightPressed())) {
/* 471 */       this.lastActiveTime = Util.getMillis();
/*     */     }
/* 473 */     else if (Util.getMillis() - this.lastActiveTime > 500L) {
/* 474 */       this.minecraft.pauseGame(false);
/*     */     } 
/*     */ 
/*     */     
/* 478 */     if (this.minecraft.noRender) {
/*     */       return;
/*     */     }
/*     */     
/* 482 */     ProfilerFiller profiler = Profiler.get();
/* 483 */     profiler.push("camera");
/* 484 */     updateCamera(deltaTracker);
/* 485 */     profiler.pop();
/*     */     
/* 487 */     this.globalSettingsUniform.update(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), (Double)this.minecraft.options.glintStrength().get(), (this.minecraft.level == null) ? 0L : this.minecraft.level.getGameTime(), deltaTracker, this.minecraft.options.getMenuBackgroundBlurriness(), this.mainCamera, (this.minecraft.options.textureFiltering().get() == TextureFilteringMethod.RGSS));
/*     */     
/* 489 */     boolean resourcesLoaded = this.minecraft.isGameLoadFinished();
/*     */     
/* 491 */     int xMouse = (int)this.minecraft.mouseHandler.getScaledXPos(this.minecraft.getWindow());
/* 492 */     int yMouse = (int)this.minecraft.mouseHandler.getScaledYPos(this.minecraft.getWindow());
/*     */     
/* 494 */     if (resourcesLoaded && renderLevel && this.minecraft.level != null) {
/* 495 */       profiler.push("world");
/*     */       
/* 497 */       renderLevel(deltaTracker);
/*     */       
/* 499 */       tryTakeScreenshotIfNeeded();
/*     */       
/* 501 */       this.minecraft.levelRenderer.doEntityOutline();
/*     */       
/* 503 */       if (this.postEffectId != null && this.effectActive) {
/* 504 */         PostChain postChain = this.minecraft.getShaderManager().getPostChain(this.postEffectId, LevelTargetBundle.MAIN_TARGETS);
/* 505 */         if (postChain != null) {
/* 506 */           postChain.process(this.minecraft.getMainRenderTarget(), (GraphicsResourceAllocator)this.resourcePool);
/*     */         }
/*     */       } 
/* 509 */       profiler.pop();
/*     */     } 
/* 511 */     this.fogRenderer.endFrame();
/*     */     
/* 513 */     RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();
/* 514 */     RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mainRenderTarget.getDepthTexture(), 1.0D);
/*     */     
/* 516 */     this.minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
/*     */     
/* 518 */     this.guiRenderState.reset();
/* 519 */     profiler.push("guiExtraction");
/* 520 */     GuiGraphics graphics = new GuiGraphics(this.minecraft, this.guiRenderState, xMouse, yMouse);
/* 521 */     if (resourcesLoaded && renderLevel && this.minecraft.level != null) {
/* 522 */       this.minecraft.gui.render(graphics, deltaTracker);
/*     */     }
/* 524 */     if (this.minecraft.getOverlay() != null) {
/*     */       try {
/* 526 */         this.minecraft.getOverlay().render(graphics, xMouse, yMouse, deltaTracker.getGameTimeDeltaTicks());
/* 527 */       } catch (Throwable t) {
/* 528 */         CrashReport report = CrashReport.forThrowable(t, "Rendering overlay");
/* 529 */         CrashReportCategory category = report.addCategory("Overlay render details");
/*     */         
/* 531 */         category.setDetail("Overlay name", () -> this.minecraft.getOverlay().getClass().getCanonicalName());
/*     */         
/* 533 */         throw new ReportedException(report);
/*     */       } 
/* 535 */     } else if (resourcesLoaded && this.minecraft.screen != null) {
/*     */       try {
/* 537 */         this.minecraft.screen.renderWithTooltipAndSubtitles(graphics, xMouse, yMouse, deltaTracker.getGameTimeDeltaTicks());
/* 538 */       } catch (Throwable t) {
/* 539 */         CrashReport report = CrashReport.forThrowable(t, "Rendering screen");
/* 540 */         CrashReportCategory category = report.addCategory("Screen render details");
/*     */         
/* 542 */         category.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
/* 543 */         this.minecraft.mouseHandler.fillMousePositionDetails(category, this.minecraft.getWindow());
/*     */         
/* 545 */         throw new ReportedException(report);
/*     */       } 
/* 547 */       if (SharedConstants.DEBUG_CURSOR_POS) {
/* 548 */         this.minecraft.mouseHandler.drawDebugMouseInfo(this.minecraft.font, graphics);
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 553 */         if (this.minecraft.screen != null) {
/* 554 */           this.minecraft.screen.handleDelayedNarration();
/*     */         }
/* 556 */       } catch (Throwable t) {
/* 557 */         CrashReport report = CrashReport.forThrowable(t, "Narrating screen");
/* 558 */         CrashReportCategory category = report.addCategory("Screen details");
/* 559 */         category.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
/*     */         
/* 561 */         throw new ReportedException(report);
/*     */       } 
/*     */     } 
/*     */     
/* 565 */     if (resourcesLoaded && renderLevel && this.minecraft.level != null) {
/* 566 */       this.minecraft.gui.renderSavingIndicator(graphics, deltaTracker);
/*     */     }
/*     */     
/* 569 */     if (resourcesLoaded) {
/* 570 */       Zone ignored = profiler.zone("toasts"); 
/* 571 */       try { this.minecraft.getToastManager().render(graphics);
/* 572 */         if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*     */           try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 574 */     }  if (!(this.minecraft.screen instanceof net.minecraft.client.gui.screens.debug.DebugOptionsScreen)) {
/* 575 */       this.minecraft.gui.renderDebugOverlay(graphics);
/*     */     }
/*     */     
/* 578 */     this.minecraft.gui.renderDeferredSubtitles();
/*     */     
/* 580 */     if (SharedConstants.DEBUG_ACTIVE_TEXT_AREAS) {
/* 581 */       renderActiveTextDebug();
/*     */     }
/*     */     
/* 584 */     profiler.popPush("guiRendering");
/* 585 */     this.guiRenderer.render(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
/* 586 */     this.guiRenderer.incrementFrameNumber();
/* 587 */     profiler.pop();
/*     */     
/* 589 */     graphics.applyCursor(this.minecraft.getWindow());
/* 590 */     this.submitNodeStorage.endFrame();
/* 591 */     this.featureRenderDispatcher.endFrame();
/*     */     
/* 593 */     this.resourcePool.endFrame();
/*     */   }
/*     */   
/*     */   private void renderActiveTextDebug() {
/* 597 */     this.guiRenderState.nextStratum();
/* 598 */     this.guiRenderState.forEachText(text -> text.ensurePrepared().visit(new Font.GlyphVisitor()
/*     */           {
/*     */             private int index;
/*     */ 
/*     */             
/*     */             public void acceptGlyph(TextRenderable.Styled glyph) {
/* 604 */               renderDebugMarkers((ActiveArea)glyph, false);
/*     */             }
/*     */ 
/*     */             
/*     */             public void acceptEmptyArea(EmptyArea empty) {
/* 609 */               renderDebugMarkers((ActiveArea)empty, true);
/*     */             }
/*     */             
/*     */             private void renderDebugMarkers(ActiveArea glyph, boolean isEmpty) {
/* 613 */               int intensity = (isEmpty ? 128 : 255) - (this.index++ & 0x1) * 64;
/*     */               
/* 615 */               Style style = glyph.style();
/* 616 */               int red = (style.getClickEvent() != null) ? intensity : 0;
/* 617 */               int green = (style.getHoverEvent() != null) ? intensity : 0;
/* 618 */               int blue = (red == 0 || green == 0) ? intensity : 0;
/* 619 */               int color = ARGB.color(128, red, green, blue);
/*     */               
/* 621 */               GameRenderer.this.guiRenderState.submitGuiElement((GuiElementRenderState)new ColoredRectangleRenderState(RenderPipelines.GUI, 
/*     */                     
/* 623 */                     TextureSetup.noTexture(), text.pose, 
/*     */                     
/* 625 */                     (int)glyph.activeLeft(), 
/* 626 */                     (int)glyph.activeTop(), 
/* 627 */                     (int)glyph.activeRight(), 
/* 628 */                     (int)glyph.activeBottom(), color, color, text.scissor));
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryTakeScreenshotIfNeeded() {
/* 639 */     if (this.hasWorldScreenshot || !this.minecraft.isLocalServer()) {
/*     */       return;
/*     */     }
/*     */     
/* 643 */     long time = Util.getMillis();
/* 644 */     if (time - this.lastScreenshotAttempt < 1000L) {
/*     */       return;
/*     */     }
/* 647 */     this.lastScreenshotAttempt = time;
/*     */     
/* 649 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/* 650 */     if (server == null || server.isStopped()) {
/*     */       return;
/*     */     }
/*     */     
/* 654 */     server.getWorldScreenshotFile().ifPresent(path -> {
/*     */           if (Files.isRegularFile(path, new java.nio.file.LinkOption[0])) {
/*     */             this.hasWorldScreenshot = true;
/*     */           } else {
/*     */             takeAutoScreenshot(path);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void takeAutoScreenshot(Path screenshotFile) {
/* 665 */     if (this.minecraft.levelRenderer.countRenderedSections() > 10 && this.minecraft.levelRenderer.hasRenderedAllSections()) {
/* 666 */       Screenshot.takeScreenshot(this.minecraft.getMainRenderTarget(), screenshot -> Util.ioPool().execute(()));
/*     */     }
/*     */   }
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
/*     */   private boolean shouldRenderBlockOutline() {
/* 692 */     if (!this.renderBlockOutline) {
/* 693 */       return false;
/*     */     }
/*     */     
/* 696 */     Entity cameraEntity = this.minecraft.getCameraEntity();
/* 697 */     boolean renderOutline = (cameraEntity instanceof Player && !this.minecraft.options.hideGui);
/* 698 */     if (renderOutline && !(((Player)cameraEntity).getAbilities()).mayBuild) {
/*     */       
/* 700 */       ItemStack itemStack = ((LivingEntity)cameraEntity).getMainHandItem();
/* 701 */       HitResult hitResult = this.minecraft.hitResult;
/* 702 */       if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
/* 703 */         BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
/* 704 */         BlockState blockState = this.minecraft.level.getBlockState(pos);
/* 705 */         if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
/* 706 */           renderOutline = (blockState.getMenuProvider((Level)this.minecraft.level, pos) != null);
/*     */         } else {
/* 708 */           BlockInWorld blockInWorld = new BlockInWorld((LevelReader)this.minecraft.level, pos, false);
/* 709 */           net.minecraft.core.Registry<Block> blockRegistry = this.minecraft.level.registryAccess().lookupOrThrow(Registries.BLOCK);
/* 710 */           renderOutline = (!itemStack.isEmpty() && (itemStack.canBreakBlockInAdventureMode(blockInWorld) || itemStack.canPlaceOnBlockInAdventureMode(blockInWorld)));
/*     */         } 
/*     */       } 
/*     */     } 
/* 714 */     return renderOutline;
/*     */   }
/*     */   
/*     */   public void updateCamera(DeltaTracker deltaTracker) {
/* 718 */     float deltaPartialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
/* 719 */     LocalPlayer player = this.minecraft.player;
/* 720 */     if (player == null || this.minecraft.level == null) {
/*     */       return;
/*     */     }
/* 723 */     if (this.minecraft.getCameraEntity() == null) {
/* 724 */       this.minecraft.setCameraEntity((Entity)player);
/*     */     }
/* 726 */     Entity cameraEntity = (this.minecraft.getCameraEntity() == null) ? (Entity)player : this.minecraft.getCameraEntity();
/*     */     
/* 728 */     float cameraDeltaPartialTicks = this.minecraft.level.tickRateManager().isEntityFrozen(cameraEntity) ? 1.0F : deltaPartialTick;
/* 729 */     this.mainCamera.setup((Level)this.minecraft.level, cameraEntity, !this.minecraft.options.getCameraType().isFirstPerson(), this.minecraft.options.getCameraType().isMirrored(), cameraDeltaPartialTicks);
/*     */   }
/*     */   
/*     */   public void renderLevel(DeltaTracker deltaTracker) {
/* 733 */     float deltaPartialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
/* 734 */     LocalPlayer player = this.minecraft.player;
/*     */ 
/*     */     
/* 737 */     this.lightTexture.updateLightTexture(1.0F);
/*     */     
/* 739 */     pick(deltaPartialTick);
/*     */     
/* 741 */     ProfilerFiller profiler = Profiler.get();
/* 742 */     boolean renderOutline = shouldRenderBlockOutline();
/*     */     
/* 744 */     extractCamera(deltaPartialTick);
/*     */     
/* 746 */     this.renderDistance = (this.minecraft.options.getEffectiveRenderDistance() * 16);
/*     */     
/* 748 */     profiler.push("matrices");
/* 749 */     float fovWithEffects = getFov(this.mainCamera, deltaPartialTick, true);
/* 750 */     Matrix4f projectionMatrix = getProjectionMatrix(fovWithEffects);
/*     */     
/* 752 */     PoseStack bobStack = new PoseStack();
/* 753 */     bobHurt(bobStack, this.mainCamera.getPartialTickTime());
/* 754 */     if ((Boolean)this.minecraft.options.bobView().get()) {
/* 755 */       bobView(bobStack, this.mainCamera.getPartialTickTime());
/*     */     }
/* 757 */     projectionMatrix.mul((Matrix4fc)bobStack.last().pose());
/*     */     
/* 759 */     float screenEffectScale = ((Double)this.minecraft.options.screenEffectScale().get()).floatValue();
/* 760 */     float portalIntensity = Mth.lerp(deltaPartialTick, player.oPortalEffectIntensity, player.portalEffectIntensity);
/* 761 */     float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, deltaPartialTick);
/* 762 */     float spinningEffectIntensity = Math.max(portalIntensity, nauseaIntensity) * screenEffectScale * screenEffectScale;
/* 763 */     if (spinningEffectIntensity > 0.0F) {
/* 764 */       float skew = 5.0F / (spinningEffectIntensity * spinningEffectIntensity + 5.0F) - spinningEffectIntensity * 0.04F;
/* 765 */       skew *= skew;
/*     */       
/* 767 */       Vector3f axis = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
/* 768 */       float angle = (this.spinningEffectTime + deltaPartialTick * this.spinningEffectSpeed) * 0.017453292F;
/* 769 */       projectionMatrix.rotate(angle, (Vector3fc)axis);
/* 770 */       projectionMatrix.scale(1.0F / skew, 1.0F, 1.0F);
/* 771 */       projectionMatrix.rotate(-angle, (Vector3fc)axis);
/*     */     } 
/*     */     
/* 774 */     RenderSystem.setProjectionMatrix(this.levelProjectionMatrixBuffer.getBuffer(projectionMatrix), ProjectionType.PERSPECTIVE);
/*     */     
/* 776 */     Quaternionf inverseRotation = this.mainCamera.rotation().conjugate(new Quaternionf());
/* 777 */     Matrix4f modelViewMatrix = new Matrix4f().rotation((Quaternionfc)inverseRotation);
/*     */     
/* 779 */     profiler.popPush("fog");
/* 780 */     Vector4f fogColor = this.fogRenderer.setupFog(this.mainCamera, this.minecraft.options.getEffectiveRenderDistance(), deltaTracker, getDarkenWorldAmount(deltaPartialTick), this.minecraft.level);
/* 781 */     GpuBufferSlice terrainFog = this.fogRenderer.getBuffer(FogRenderer.FogMode.WORLD);
/*     */     
/* 783 */     profiler.popPush("level");
/* 784 */     boolean shouldCreateBossFog = this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
/* 785 */     this.minecraft.levelRenderer.renderLevel((GraphicsResourceAllocator)this.resourcePool, deltaTracker, renderOutline, this.mainCamera, modelViewMatrix, projectionMatrix, getProjectionMatrixForCulling(fovWithEffects), terrainFog, fogColor, !shouldCreateBossFog);
/*     */     
/* 787 */     profiler.popPush("hand");
/* 788 */     boolean isSleeping = (this.minecraft.getCameraEntity() instanceof LivingEntity && ((LivingEntity)this.minecraft.getCameraEntity()).isSleeping());
/* 789 */     RenderSystem.setProjectionMatrix(this.hud3dProjectionMatrixBuffer.getBuffer(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), getFov(this.mainCamera, deltaPartialTick, false)), ProjectionType.PERSPECTIVE);
/* 790 */     RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.minecraft.getMainRenderTarget().getDepthTexture(), 1.0D);
/*     */     
/* 792 */     renderItemInHand(deltaPartialTick, isSleeping, modelViewMatrix);
/*     */     
/* 794 */     profiler.popPush("screenEffects");
/* 795 */     MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
/* 796 */     this.screenEffectRenderer.renderScreenEffect(isSleeping, deltaPartialTick, this.submitNodeStorage);
/* 797 */     this.featureRenderDispatcher.renderAllFeatures();
/* 798 */     bufferSource.endBatch();
/* 799 */     profiler.pop();
/* 800 */     RenderSystem.setShaderFog(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
/*     */     
/* 802 */     if (this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR) && this.minecraft.options.getCameraType().isFirstPerson() && !this.minecraft.options.hideGui) {
/* 803 */       this.minecraft.getDebugOverlay().render3dCrosshair(this.mainCamera);
/*     */     }
/*     */   }
/*     */   
/*     */   private void extractCamera(float partialTicks) {
/* 808 */     CameraRenderState cameraState = this.levelRenderState.cameraRenderState;
/* 809 */     cameraState.initialized = this.mainCamera.isInitialized();
/* 810 */     cameraState.pos = this.mainCamera.position();
/* 811 */     cameraState.blockPos = this.mainCamera.blockPosition();
/* 812 */     cameraState.entityPos = this.mainCamera.entity().getPosition(partialTicks);
/* 813 */     cameraState.orientation = new Quaternionf((Quaternionfc)this.mainCamera.rotation());
/*     */   }
/*     */ 
/*     */   
/*     */   private Matrix4f getProjectionMatrixForCulling(float fovWithEffects) {
/* 818 */     float fovForCulling = Math.max(fovWithEffects, (Integer)this.minecraft.options.fov().get());
/* 819 */     return getProjectionMatrix(fovForCulling);
/*     */   }
/*     */   
/*     */   public void resetData() {
/* 823 */     this.screenEffectRenderer.resetItemActivation();
/* 824 */     this.minecraft.getMapTextureManager().resetData();
/* 825 */     this.mainCamera.reset();
/* 826 */     this.hasWorldScreenshot = false;
/*     */   }
/*     */   
/*     */   public void displayItemActivation(ItemStack itemStack) {
/* 830 */     this.screenEffectRenderer.displayItemActivation(itemStack, this.random);
/*     */   }
/*     */   
/*     */   public Minecraft getMinecraft() {
/* 834 */     return this.minecraft;
/*     */   }
/*     */   
/*     */   public float getDarkenWorldAmount(float a) {
/* 838 */     return Mth.lerp(a, this.darkenWorldAmountO, this.darkenWorldAmount);
/*     */   }
/*     */   
/*     */   public float getRenderDistance() {
/* 842 */     return this.renderDistance;
/*     */   }
/*     */   
/*     */   public Camera getMainCamera() {
/* 846 */     return this.mainCamera;
/*     */   }
/*     */   
/*     */   public LightTexture lightTexture() {
/* 850 */     return this.lightTexture;
/*     */   }
/*     */   
/*     */   public OverlayTexture overlayTexture() {
/* 854 */     return this.overlayTexture;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 projectPointToScreen(Vec3 point) {
/* 859 */     Matrix4f projectionMatrix = getProjectionMatrix(getFov(this.mainCamera, 0.0F, true));
/*     */     
/* 861 */     Quaternionf inverseRotation = this.mainCamera.rotation().conjugate(new Quaternionf());
/* 862 */     Matrix4f modelViewMatrix = new Matrix4f().rotation((Quaternionfc)inverseRotation);
/*     */     
/* 864 */     Matrix4f mvp = projectionMatrix.mul((Matrix4fc)modelViewMatrix);
/*     */     
/* 866 */     Vec3 camPos = this.mainCamera.position();
/* 867 */     Vec3 offset = point.subtract(camPos);
/*     */     
/* 869 */     Vector3f vector3f = mvp.transformProject(offset.toVector3f());
/* 870 */     return new Vec3((Vector3fc)vector3f);
/*     */   }
/*     */ 
/*     */   
/*     */   public double projectHorizonToScreen() {
/* 875 */     float xRot = this.mainCamera.xRot();
/* 876 */     if (xRot <= -90.0F)
/* 877 */       return Double.NEGATIVE_INFINITY; 
/* 878 */     if (xRot >= 90.0F) {
/* 879 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 881 */     float fov = getFov(this.mainCamera, 0.0F, true);
/* 882 */     return Math.tan((xRot * 0.017453292F)) / Math.tan((fov / 2.0F * 0.017453292F));
/*     */   }
/*     */ 
/*     */   
/*     */   public GlobalSettingsUniform getGlobalSettingsUniform() {
/* 887 */     return this.globalSettingsUniform;
/*     */   }
/*     */   
/*     */   public Lighting getLighting() {
/* 891 */     return this.lighting;
/*     */   }
/*     */   
/*     */   public void setLevel(ClientLevel level) {
/* 895 */     if (level != null) {
/* 896 */       this.lighting.updateLevel(level.dimensionType().cardinalLightType());
/*     */     }
/*     */   }
/*     */   
/*     */   public PanoramaRenderer getPanorama() {
/* 901 */     return this.panorama;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/GameRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */