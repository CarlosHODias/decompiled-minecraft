/*      */ package net.minecraft.client.renderer;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*      */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*      */ import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
/*      */ import com.mojang.blaze3d.framegraph.FramePass;
/*      */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*      */ import com.mojang.blaze3d.pipeline.TextureTarget;
/*      */ import com.mojang.blaze3d.platform.Lighting;
/*      */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*      */ import com.mojang.blaze3d.resource.RenderTargetDescriptor;
/*      */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*      */ import com.mojang.blaze3d.resource.ResourceHandle;
/*      */ import com.mojang.blaze3d.systems.RenderPass;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import com.mojang.blaze3d.textures.AddressMode;
/*      */ import com.mojang.blaze3d.textures.FilterMode;
/*      */ import com.mojang.blaze3d.textures.GpuSampler;
/*      */ import com.mojang.blaze3d.textures.GpuTextureView;
/*      */ import com.mojang.blaze3d.vertex.PoseStack;
/*      */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*      */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*      */ import com.mojang.blaze3d.vertex.VertexFormat;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.OptionalDouble;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.Camera;
/*      */ import net.minecraft.client.CloudStatus;
/*      */ import net.minecraft.client.DeltaTracker;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.PrioritizeChunkUpdates;
/*      */ import net.minecraft.client.TextureFilteringMethod;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
/*      */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*      */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*      */ import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
/*      */ import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
/*      */ import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
/*      */ import net.minecraft.client.renderer.chunk.RenderRegionCache;
/*      */ import net.minecraft.client.renderer.chunk.SectionBuffers;
/*      */ import net.minecraft.client.renderer.chunk.SectionMesh;
/*      */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*      */ import net.minecraft.client.renderer.chunk.TranslucencyPointOfView;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.debug.DebugRenderer;
/*      */ import net.minecraft.client.renderer.debug.GameTestBlockHighlightRenderer;
/*      */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*      */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*      */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*      */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*      */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*      */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*      */ import net.minecraft.client.renderer.state.BlockBreakingRenderState;
/*      */ import net.minecraft.client.renderer.state.BlockOutlineRenderState;
/*      */ import net.minecraft.client.renderer.state.CameraRenderState;
/*      */ import net.minecraft.client.renderer.state.LevelRenderState;
/*      */ import net.minecraft.client.renderer.state.ParticlesRenderState;
/*      */ import net.minecraft.client.renderer.state.SkyRenderState;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*      */ import net.minecraft.client.resources.model.ModelBakery;
/*      */ import net.minecraft.client.server.IntegratedServer;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.gizmos.GizmoCollector;
/*      */ import net.minecraft.gizmos.GizmoPrimitives;
/*      */ import net.minecraft.gizmos.Gizmos;
/*      */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.level.BlockDestructionProgress;
/*      */ import net.minecraft.server.level.ParticleStatus;
/*      */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Brightness;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.level.BlockAndTintGetter;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LightLayer;
/*      */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*      */ import net.minecraft.world.level.block.LeavesBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.material.FogType;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import org.joml.Matrix4f;
/*      */ import org.joml.Matrix4fStack;
/*      */ import org.joml.Matrix4fc;
/*      */ import org.joml.Vector4f;
/*      */ 
/*      */ public class LevelRenderer implements ResourceManagerReloadListener, AutoCloseable {
/*  123 */   private static final Identifier TRANSPARENCY_POST_CHAIN_ID = Identifier.withDefaultNamespace("transparency");
/*  124 */   private static final Identifier ENTITY_OUTLINE_POST_CHAIN_ID = Identifier.withDefaultNamespace("entity_outline");
/*      */   
/*      */   public static final int SECTION_SIZE = 16;
/*      */   
/*      */   public static final int HALF_SECTION_SIZE = 8;
/*      */   
/*      */   public static final int NEARBY_SECTION_DISTANCE_IN_BLOCKS = 32;
/*      */   
/*      */   private static final int MINIMUM_TRANSPARENT_SORT_COUNT = 15;
/*      */   
/*      */   private static final float CHUNK_VISIBILITY_THRESHOLD = 0.3F;
/*      */   
/*      */   private final Minecraft minecraft;
/*      */   
/*      */   private final EntityRenderDispatcher entityRenderDispatcher;
/*      */   
/*      */   private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
/*      */   private final RenderBuffers renderBuffers;
/*      */   private SkyRenderer skyRenderer;
/*  143 */   private final CloudRenderer cloudRenderer = new CloudRenderer();
/*  144 */   private final WorldBorderRenderer worldBorderRenderer = new WorldBorderRenderer();
/*  145 */   private final WeatherEffectRenderer weatherEffectRenderer = new WeatherEffectRenderer();
/*  146 */   private final ParticlesRenderState particlesRenderState = new ParticlesRenderState();
/*  147 */   public final DebugRenderer debugRenderer = new DebugRenderer();
/*  148 */   public final GameTestBlockHighlightRenderer gameTestBlockHighlightRenderer = new GameTestBlockHighlightRenderer();
/*      */   
/*      */   private ClientLevel level;
/*      */   
/*  152 */   private final SectionOcclusionGraph sectionOcclusionGraph = new SectionOcclusionGraph();
/*  153 */   private final ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections = new ObjectArrayList(10000);
/*  154 */   private final ObjectArrayList<SectionRenderDispatcher.RenderSection> nearbyVisibleSections = new ObjectArrayList(50);
/*      */   
/*      */   private ViewArea viewArea;
/*      */   
/*      */   private int ticks;
/*      */   
/*  160 */   private final Int2ObjectMap<BlockDestructionProgress> destroyingBlocks = (Int2ObjectMap<BlockDestructionProgress>)new Int2ObjectOpenHashMap();
/*  161 */   private final Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress = (Long2ObjectMap<SortedSet<BlockDestructionProgress>>)new Long2ObjectOpenHashMap();
/*      */ 
/*      */   
/*      */   private RenderTarget entityOutlineTarget;
/*      */   
/*  166 */   private final LevelTargetBundle targets = new LevelTargetBundle();
/*      */   
/*  168 */   private int lastCameraSectionX = Integer.MIN_VALUE;
/*  169 */   private int lastCameraSectionY = Integer.MIN_VALUE;
/*  170 */   private int lastCameraSectionZ = Integer.MIN_VALUE;
/*      */   
/*  172 */   private double prevCamX = Double.MIN_VALUE;
/*  173 */   private double prevCamY = Double.MIN_VALUE;
/*  174 */   private double prevCamZ = Double.MIN_VALUE;
/*  175 */   private double prevCamRotX = Double.MIN_VALUE;
/*  176 */   private double prevCamRotY = Double.MIN_VALUE;
/*      */   
/*      */   private SectionRenderDispatcher sectionRenderDispatcher;
/*      */   
/*  180 */   private int lastViewDistance = -1;
/*      */   
/*      */   private boolean captureFrustum;
/*      */   
/*      */   private Frustum capturedFrustum;
/*      */   
/*      */   private BlockPos lastTranslucentSortBlockPos;
/*      */   
/*      */   private int translucencyResortIterationIndex;
/*      */   
/*      */   private final LevelRenderState levelRenderState;
/*      */   
/*      */   private final SubmitNodeStorage submitNodeStorage;
/*      */   private final FeatureRenderDispatcher featureRenderDispatcher;
/*      */   private GpuSampler chunkLayerSampler;
/*  195 */   private final SimpleGizmoCollector collectedGizmos = new SimpleGizmoCollector();
/*      */ 
/*      */   
/*  198 */   private FinalizedGizmos finalizedGizmos = new FinalizedGizmos(new DrawableGizmoPrimitives(), new DrawableGizmoPrimitives());
/*      */   
/*      */   public LevelRenderer(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, RenderBuffers renderBuffers, LevelRenderState levelRenderState, FeatureRenderDispatcher featureRenderDispatcher) {
/*  201 */     this.minecraft = minecraft;
/*  202 */     this.entityRenderDispatcher = entityRenderDispatcher;
/*  203 */     this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
/*  204 */     this.renderBuffers = renderBuffers;
/*  205 */     this.submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();
/*  206 */     this.levelRenderState = levelRenderState;
/*  207 */     this.featureRenderDispatcher = featureRenderDispatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/*  213 */     if (this.entityOutlineTarget != null) {
/*  214 */       this.entityOutlineTarget.destroyBuffers();
/*      */     }
/*      */     
/*  217 */     if (this.skyRenderer != null) {
/*  218 */       this.skyRenderer.close();
/*      */     }
/*  220 */     if (this.chunkLayerSampler != null) {
/*  221 */       this.chunkLayerSampler.close();
/*      */     }
/*  223 */     this.cloudRenderer.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onResourceManagerReload(net.minecraft.server.packs.resources.ResourceManager resourceManager) {
/*  228 */     initOutline();
/*  229 */     if (this.skyRenderer != null) {
/*  230 */       this.skyRenderer.close();
/*      */     }
/*  232 */     this.skyRenderer = new SkyRenderer(this.minecraft.getTextureManager(), this.minecraft.getAtlasManager());
/*      */   }
/*      */   
/*      */   public void initOutline() {
/*  236 */     if (this.entityOutlineTarget != null) {
/*  237 */       this.entityOutlineTarget.destroyBuffers();
/*      */     }
/*  239 */     this.entityOutlineTarget = (RenderTarget)new TextureTarget("Entity Outline", this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), true);
/*      */   }
/*      */   
/*      */   private PostChain getTransparencyChain() {
/*  243 */     if (!Minecraft.useShaderTransparency()) {
/*  244 */       return null;
/*      */     }
/*      */     
/*  247 */     PostChain chain = this.minecraft.getShaderManager().getPostChain(TRANSPARENCY_POST_CHAIN_ID, LevelTargetBundle.SORTING_TARGETS);
/*  248 */     if (chain == null) {
/*      */       
/*  250 */       this.minecraft.options.improvedTransparency().set(false);
/*  251 */       this.minecraft.options.save();
/*      */     } 
/*      */     
/*  254 */     return chain;
/*      */   }
/*      */   
/*      */   public void doEntityOutline() {
/*  258 */     if (shouldShowEntityOutlines()) {
/*  259 */       this.entityOutlineTarget.blitAndBlendToTexture(this.minecraft.getMainRenderTarget().getColorTextureView());
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean shouldShowEntityOutlines() {
/*  264 */     return (!this.minecraft.gameRenderer.isPanoramicMode() && this.entityOutlineTarget != null && this.minecraft.player != null);
/*      */   }
/*      */   
/*      */   public void setLevel(ClientLevel level) {
/*  268 */     this.lastCameraSectionX = Integer.MIN_VALUE;
/*  269 */     this.lastCameraSectionY = Integer.MIN_VALUE;
/*  270 */     this.lastCameraSectionZ = Integer.MIN_VALUE;
/*      */     
/*  272 */     this.level = level;
/*  273 */     if (level != null) {
/*  274 */       allChanged();
/*      */     } else {
/*  276 */       this.entityRenderDispatcher.resetCamera();
/*  277 */       if (this.viewArea != null) {
/*  278 */         this.viewArea.releaseAllBuffers();
/*  279 */         this.viewArea = null;
/*      */       } 
/*  281 */       if (this.sectionRenderDispatcher != null) {
/*  282 */         this.sectionRenderDispatcher.dispose();
/*      */       }
/*  284 */       this.sectionRenderDispatcher = null;
/*  285 */       this.sectionOcclusionGraph.waitAndReset(null);
/*  286 */       clearVisibleSections();
/*      */     } 
/*  288 */     this.gameTestBlockHighlightRenderer.clear();
/*      */   }
/*      */   
/*      */   private void clearVisibleSections() {
/*  292 */     this.visibleSections.clear();
/*  293 */     this.nearbyVisibleSections.clear();
/*      */   }
/*      */   
/*      */   public void allChanged() {
/*  297 */     if (this.level == null) {
/*      */       return;
/*      */     }
/*      */     
/*  301 */     this.level.clearTintCaches();
/*      */     
/*  303 */     if (this.sectionRenderDispatcher == null) {
/*  304 */       this.sectionRenderDispatcher = new SectionRenderDispatcher(this.level, this, Util.backgroundExecutor(), this.renderBuffers, this.minecraft.getBlockRenderer(), this.minecraft.getBlockEntityRenderDispatcher());
/*      */     } else {
/*  306 */       this.sectionRenderDispatcher.setLevel(this.level);
/*      */     } 
/*      */     
/*  309 */     this.cloudRenderer.markForRebuild();
/*      */     
/*  311 */     ItemBlockRenderTypes.setCutoutLeaves((Boolean)this.minecraft.options.cutoutLeaves().get());
/*  312 */     LeavesBlock.setCutoutLeaves((Boolean)this.minecraft.options.cutoutLeaves().get());
/*      */     
/*  314 */     this.lastViewDistance = this.minecraft.options.getEffectiveRenderDistance();
/*      */     
/*  316 */     if (this.viewArea != null) {
/*  317 */       this.viewArea.releaseAllBuffers();
/*      */     }
/*      */     
/*  320 */     this.sectionRenderDispatcher.clearCompileQueue();
/*      */     
/*  322 */     this.viewArea = new ViewArea(this.sectionRenderDispatcher, (Level)this.level, this.minecraft.options.getEffectiveRenderDistance(), this);
/*  323 */     this.sectionOcclusionGraph.waitAndReset(this.viewArea);
/*  324 */     clearVisibleSections();
/*      */     
/*  326 */     Camera camera = this.minecraft.gameRenderer.getMainCamera();
/*  327 */     this.viewArea.repositionCamera(SectionPos.of((Position)camera.position()));
/*      */   }
/*      */   
/*      */   public void resize(int width, int height) {
/*  331 */     needsUpdate();
/*  332 */     if (this.entityOutlineTarget != null) {
/*  333 */       this.entityOutlineTarget.resize(width, height);
/*      */     }
/*      */   }
/*      */   
/*      */   public String getSectionStatistics() {
/*  338 */     if (this.viewArea == null) {
/*  339 */       return null;
/*      */     }
/*  341 */     int totalSections = this.viewArea.sections.length;
/*  342 */     int rendered = countRenderedSections();
/*      */     
/*  344 */     return String.format(Locale.ROOT, "C: %d/%d %sD: %d, %s", new Object[] { rendered, 
/*  345 */           totalSections, 
/*      */           
/*  347 */           this.minecraft.smartCull ? "(s) " : "", this.lastViewDistance, 
/*      */           
/*  349 */           (this.sectionRenderDispatcher == null) ? "null" : this.sectionRenderDispatcher.getStats() });
/*      */   }
/*      */ 
/*      */   
/*      */   public SectionRenderDispatcher getSectionRenderDispatcher() {
/*  354 */     return this.sectionRenderDispatcher;
/*      */   }
/*      */   
/*      */   public double getTotalSections() {
/*  358 */     return (this.viewArea == null) ? 0.0D : this.viewArea.sections.length;
/*      */   }
/*      */   
/*      */   public double getLastViewDistance() {
/*  362 */     return this.lastViewDistance;
/*      */   }
/*      */   
/*      */   public int countRenderedSections() {
/*  366 */     int rendered = 0;
/*  367 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.visibleSections.iterator(); objectListIterator.hasNext(); ) { SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/*  368 */       if (section.getSectionMesh().hasRenderableLayers()) {
/*  369 */         rendered++;
/*      */       } }
/*      */     
/*  372 */     return rendered;
/*      */   }
/*      */   
/*      */   public void resetSampler() {
/*  376 */     if (this.chunkLayerSampler != null) {
/*  377 */       this.chunkLayerSampler.close();
/*      */     }
/*  379 */     this.chunkLayerSampler = null;
/*      */   }
/*      */   
/*      */   public String getEntityStatistics() {
/*  383 */     if (this.level == null) {
/*  384 */       return null;
/*      */     }
/*  386 */     return "E: " + this.levelRenderState.entityRenderStates.size() + "/" + this.level.getEntityCount() + ", SD: " + this.level.getServerSimulationDistance();
/*      */   }
/*      */   
/*      */   private void cullTerrain(Camera camera, Frustum frustum, boolean spectator) {
/*  390 */     Vec3 cameraPos = camera.position();
/*      */     
/*  392 */     if (this.minecraft.options.getEffectiveRenderDistance() != this.lastViewDistance) {
/*  393 */       allChanged();
/*      */     }
/*      */     
/*  396 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/*  398 */     profiler.push("repositionCamera");
/*  399 */     int cameraSectionX = SectionPos.posToSectionCoord(cameraPos.x());
/*  400 */     int cameraSectionY = SectionPos.posToSectionCoord(cameraPos.y());
/*  401 */     int cameraSectionZ = SectionPos.posToSectionCoord(cameraPos.z());
/*  402 */     if (this.lastCameraSectionX != cameraSectionX || this.lastCameraSectionY != cameraSectionY || this.lastCameraSectionZ != cameraSectionZ) {
/*  403 */       this.lastCameraSectionX = cameraSectionX;
/*  404 */       this.lastCameraSectionY = cameraSectionY;
/*  405 */       this.lastCameraSectionZ = cameraSectionZ;
/*      */       
/*  407 */       this.viewArea.repositionCamera(SectionPos.of((Position)cameraPos));
/*  408 */       this.worldBorderRenderer.invalidate();
/*      */     } 
/*      */     
/*  411 */     this.sectionRenderDispatcher.setCameraPosition(cameraPos);
/*      */     
/*  413 */     double camX = Math.floor(cameraPos.x / 8.0D);
/*  414 */     double camY = Math.floor(cameraPos.y / 8.0D);
/*  415 */     double camZ = Math.floor(cameraPos.z / 8.0D);
/*      */     
/*  417 */     if (camX != this.prevCamX || camY != this.prevCamY || camZ != this.prevCamZ) {
/*  418 */       this.sectionOcclusionGraph.invalidate();
/*      */     }
/*      */     
/*  421 */     this.prevCamX = camX;
/*  422 */     this.prevCamY = camY;
/*  423 */     this.prevCamZ = camZ;
/*      */     
/*  425 */     profiler.pop();
/*  426 */     if (this.capturedFrustum == null) {
/*  427 */       boolean smartCull = this.minecraft.smartCull;
/*  428 */       if (spectator && this.level.getBlockState(camera.blockPosition()).isSolidRender()) {
/*  429 */         smartCull = false;
/*      */       }
/*  431 */       profiler.push("updateSOG");
/*  432 */       this.sectionOcclusionGraph.update(smartCull, camera, frustum, (List<SectionRenderDispatcher.RenderSection>)this.visibleSections, this.level.getChunkSource().getLoadedEmptySections());
/*  433 */       profiler.pop();
/*      */       
/*  435 */       double camRotX = Math.floor((camera.xRot() / 2.0F));
/*  436 */       double camRotY = Math.floor((camera.yRot() / 2.0F));
/*  437 */       if (this.sectionOcclusionGraph.consumeFrustumUpdate() || camRotX != this.prevCamRotX || camRotY != this.prevCamRotY) {
/*  438 */         profiler.push("applyFrustum");
/*  439 */         applyFrustum(offsetFrustum(frustum));
/*  440 */         profiler.pop();
/*  441 */         this.prevCamRotX = camRotX;
/*  442 */         this.prevCamRotY = camRotY;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Frustum offsetFrustum(Frustum frustum) {
/*  448 */     return new Frustum(frustum).offsetToFullyIncludeCameraCube(8);
/*      */   }
/*      */   
/*      */   private void applyFrustum(Frustum frustum) {
/*  452 */     if (!Minecraft.getInstance().isSameThread()) {
/*  453 */       throw new IllegalStateException("applyFrustum called from wrong thread: " + Thread.currentThread().getName());
/*      */     }
/*  455 */     clearVisibleSections();
/*  456 */     this.sectionOcclusionGraph.addSectionsInFrustum(frustum, (List<SectionRenderDispatcher.RenderSection>)this.visibleSections, (List<SectionRenderDispatcher.RenderSection>)this.nearbyVisibleSections);
/*      */   }
/*      */   
/*      */   public void addRecentlyCompiledSection(SectionRenderDispatcher.RenderSection section) {
/*  460 */     this.sectionOcclusionGraph.schedulePropagationFrom(section);
/*      */   }
/*      */   
/*      */   private Frustum prepareCullFrustum(Matrix4f modelViewMatrix, Matrix4f projectionMatrixForCulling, Vec3 cameraPos) {
/*      */     Frustum frustum;
/*  465 */     if (this.capturedFrustum != null && !this.captureFrustum) {
/*  466 */       frustum = this.capturedFrustum;
/*      */     } else {
/*  468 */       frustum = new Frustum(modelViewMatrix, projectionMatrixForCulling);
/*  469 */       frustum.prepare(cameraPos.x(), cameraPos.y(), cameraPos.z());
/*      */     } 
/*  471 */     if (this.captureFrustum) {
/*  472 */       this.capturedFrustum = frustum;
/*  473 */       this.captureFrustum = false;
/*      */     } 
/*  475 */     return frustum;
/*      */   }
/*      */   
/*      */   public void renderLevel(GraphicsResourceAllocator resourceAllocator, DeltaTracker deltaTracker, boolean renderOutline, Camera camera, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Matrix4f projectionMatrixForCulling, GpuBufferSlice terrainFog, Vector4f fogColor, boolean shouldRenderSky) {
/*  479 */     float deltaPartialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
/*      */     
/*  481 */     this.levelRenderState.gameTime = this.level.getGameTime();
/*      */     
/*  483 */     this.blockEntityRenderDispatcher.prepare(camera);
/*  484 */     this.entityRenderDispatcher.prepare(camera, this.minecraft.crosshairPickEntity);
/*      */     
/*  486 */     final ProfilerFiller profiler = Profiler.get();
/*  487 */     profiler.push("populateLightUpdates");
/*  488 */     this.level.pollLightUpdates();
/*  489 */     profiler.popPush("runLightUpdates");
/*  490 */     this.level.getChunkSource().getLightEngine().runLightUpdates();
/*      */     
/*  492 */     profiler.popPush("prepareCullFrustum");
/*  493 */     Vec3 cameraPos = camera.position();
/*  494 */     Frustum frustum = prepareCullFrustum(modelViewMatrix, projectionMatrixForCulling, cameraPos);
/*      */     
/*  496 */     profiler.popPush("cullTerrain");
/*  497 */     cullTerrain(camera, frustum, this.minecraft.player.isSpectator());
/*      */     
/*  499 */     profiler.popPush("compileSections");
/*  500 */     compileSections(camera);
/*      */     
/*  502 */     profiler.popPush("extract");
/*  503 */     profiler.push("entities");
/*  504 */     extractVisibleEntities(camera, frustum, deltaTracker, this.levelRenderState);
/*      */     
/*  506 */     profiler.popPush("blockEntities");
/*  507 */     extractVisibleBlockEntities(camera, deltaPartialTick, this.levelRenderState);
/*      */     
/*  509 */     profiler.popPush("blockOutline");
/*  510 */     extractBlockOutline(camera, this.levelRenderState);
/*      */     
/*  512 */     profiler.popPush("blockBreaking");
/*  513 */     extractBlockDestroyAnimation(camera, this.levelRenderState);
/*      */     
/*  515 */     profiler.popPush("weather");
/*  516 */     this.weatherEffectRenderer.extractRenderState((Level)this.level, this.ticks, deltaPartialTick, cameraPos, this.levelRenderState.weatherRenderState);
/*      */     
/*  518 */     profiler.popPush("sky");
/*  519 */     this.skyRenderer.extractRenderState(this.level, deltaPartialTick, camera, this.levelRenderState.skyRenderState);
/*      */     
/*  521 */     profiler.popPush("border");
/*  522 */     this.worldBorderRenderer.extract(this.level.getWorldBorder(), deltaPartialTick, cameraPos, (this.minecraft.options.getEffectiveRenderDistance() * 16), this.levelRenderState.worldBorderRenderState);
/*  523 */     profiler.pop();
/*      */     
/*  525 */     profiler.popPush("debug");
/*      */ 
/*      */ 
/*      */     
/*  529 */     this.debugRenderer.emitGizmos(frustum, cameraPos.x, cameraPos.y, cameraPos.z, deltaTracker.getGameTimeDeltaPartialTick(false));
/*  530 */     this.gameTestBlockHighlightRenderer.emitGizmos();
/*      */     
/*  532 */     profiler.popPush("setupFrameGraph");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  537 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/*  538 */     modelViewStack.pushMatrix();
/*  539 */     modelViewStack.mul((Matrix4fc)modelViewMatrix);
/*      */     
/*  541 */     FrameGraphBuilder frame = new FrameGraphBuilder();
/*  542 */     this.targets.main = frame.importExternal("main", this.minecraft.getMainRenderTarget());
/*      */     
/*  544 */     int screenWidth = (this.minecraft.getMainRenderTarget()).width;
/*  545 */     int screenHeight = (this.minecraft.getMainRenderTarget()).height;
/*  546 */     RenderTargetDescriptor screenSizeTargetDescriptor = new RenderTargetDescriptor(screenWidth, screenHeight, true, 0);
/*  547 */     PostChain transparencyChain = getTransparencyChain();
/*  548 */     if (transparencyChain != null) {
/*  549 */       this.targets.translucent = frame.createInternal("translucent", (ResourceDescriptor)screenSizeTargetDescriptor);
/*  550 */       this.targets.itemEntity = frame.createInternal("item_entity", (ResourceDescriptor)screenSizeTargetDescriptor);
/*  551 */       this.targets.particles = frame.createInternal("particles", (ResourceDescriptor)screenSizeTargetDescriptor);
/*  552 */       this.targets.weather = frame.createInternal("weather", (ResourceDescriptor)screenSizeTargetDescriptor);
/*  553 */       this.targets.clouds = frame.createInternal("clouds", (ResourceDescriptor)screenSizeTargetDescriptor);
/*      */     } 
/*  555 */     if (this.entityOutlineTarget != null) {
/*  556 */       this.targets.entityOutline = frame.importExternal("entity_outline", this.entityOutlineTarget);
/*      */     }
/*      */     
/*  559 */     FramePass clearPass = frame.addPass("clear");
/*  560 */     this.targets.main = clearPass.readsAndWrites(this.targets.main);
/*  561 */     clearPass.executes(() -> {
/*      */           RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();
/*      */           
/*      */           RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(mainRenderTarget.getColorTexture(), ARGB.colorFromFloat(0.0F, fogColor.x, fogColor.y, fogColor.z), mainRenderTarget.getDepthTexture(), 1.0D);
/*      */         });
/*  566 */     if (shouldRenderSky) {
/*  567 */       addSkyPass(frame, camera, terrainFog);
/*      */     }
/*      */     
/*  570 */     addMainPass(frame, frustum, modelViewMatrix, terrainFog, renderOutline, this.levelRenderState, deltaTracker, profiler);
/*      */     
/*  572 */     PostChain entityOutlineChain = this.minecraft.getShaderManager().getPostChain(ENTITY_OUTLINE_POST_CHAIN_ID, LevelTargetBundle.OUTLINE_TARGETS);
/*      */     
/*  574 */     if (this.levelRenderState.haveGlowingEntities && entityOutlineChain != null) {
/*  575 */       entityOutlineChain.addToFrame(frame, screenWidth, screenHeight, this.targets);
/*      */     }
/*      */     
/*  578 */     this.minecraft.particleEngine.extract(this.particlesRenderState, new Frustum(frustum).offset(-3.0F), camera, deltaPartialTick);
/*  579 */     addParticlesPass(frame, terrainFog);
/*      */     
/*  581 */     CloudStatus cloudsType = this.minecraft.options.getCloudsType();
/*  582 */     if (cloudsType != CloudStatus.OFF) {
/*  583 */       int cloudColor = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_COLOR, deltaPartialTick);
/*  584 */       if (ARGB.alpha(cloudColor) > 0) {
/*  585 */         float cloudHeight = (Float)camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_HEIGHT, deltaPartialTick);
/*  586 */         addCloudsPass(frame, cloudsType, this.levelRenderState.cameraRenderState.pos, this.levelRenderState.gameTime, deltaPartialTick, cloudColor, cloudHeight);
/*      */       } 
/*      */     } 
/*      */     
/*  590 */     addWeatherPass(frame, terrainFog);
/*      */     
/*  592 */     if (transparencyChain != null) {
/*  593 */       transparencyChain.addToFrame(frame, screenWidth, screenHeight, this.targets);
/*      */     }
/*      */     
/*  596 */     addLateDebugPass(frame, this.levelRenderState.cameraRenderState, terrainFog, modelViewMatrix);
/*      */     
/*  598 */     profiler.popPush("executeFrameGraph");
/*  599 */     frame.execute(resourceAllocator, new FrameGraphBuilder.Inspector(this)
/*      */         {
/*      */           public void beforeExecutePass(String name) {
/*  602 */             profiler.push(name);
/*      */           }
/*      */ 
/*      */           
/*      */           public void afterExecutePass(String name) {
/*  607 */             profiler.pop();
/*      */           }
/*      */         });
/*      */     
/*  611 */     this.targets.clear();
/*      */     
/*  613 */     modelViewStack.popMatrix();
/*  614 */     profiler.pop();
/*  615 */     this.levelRenderState.reset();
/*      */   }
/*      */   
/*      */   private void addMainPass(FrameGraphBuilder frame, Frustum frustum, Matrix4f modelViewMatrix, GpuBufferSlice terrainFog, boolean renderOutline, LevelRenderState levelRenderState, DeltaTracker deltaTracker, ProfilerFiller profiler) {
/*  619 */     FramePass pass = frame.addPass("main");
/*  620 */     this.targets.main = pass.readsAndWrites(this.targets.main);
/*  621 */     if (this.targets.translucent != null) {
/*  622 */       this.targets.translucent = pass.readsAndWrites(this.targets.translucent);
/*      */     }
/*  624 */     if (this.targets.itemEntity != null) {
/*  625 */       this.targets.itemEntity = pass.readsAndWrites(this.targets.itemEntity);
/*      */     }
/*  627 */     if (this.targets.weather != null) {
/*  628 */       this.targets.weather = pass.readsAndWrites(this.targets.weather);
/*      */     }
/*  630 */     if (levelRenderState.haveGlowingEntities && this.targets.entityOutline != null) {
/*  631 */       this.targets.entityOutline = pass.readsAndWrites(this.targets.entityOutline);
/*      */     }
/*      */     
/*  634 */     ResourceHandle<RenderTarget> mainTarget = this.targets.main;
/*  635 */     ResourceHandle<RenderTarget> translucentTarget = this.targets.translucent;
/*  636 */     ResourceHandle<RenderTarget> itemEntityTarget = this.targets.itemEntity;
/*  637 */     ResourceHandle<RenderTarget> entityOutlineTarget = this.targets.entityOutline;
/*      */     
/*  639 */     pass.executes(() -> {
/*      */           RenderSystem.setShaderFog(terrainFog);
/*      */           Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/*      */           double camX = cameraPos.x(), camY = cameraPos.y(), camZ = cameraPos.z();
/*      */           profiler.push("terrain");
/*      */           if (this.chunkLayerSampler == null) {
/*      */             int maxAnisotropy = (this.minecraft.options.textureFiltering().get() == TextureFilteringMethod.ANISOTROPIC) ? this.minecraft.options.maxAnisotropyValue() : 1;
/*      */             this.chunkLayerSampler = RenderSystem.getDevice().createSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.LINEAR, maxAnisotropy, OptionalDouble.empty());
/*      */           } 
/*      */           ChunkSectionsToRender chunkSectionsToRender = prepareChunkRenders((Matrix4fc)modelViewMatrix, camX, camY, camZ);
/*      */           chunkSectionsToRender.renderGroup(ChunkSectionLayerGroup.OPAQUE, this.chunkLayerSampler);
/*      */           this.minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.LEVEL);
/*      */           if (itemEntityTarget != null) {
/*      */             ((RenderTarget)itemEntityTarget.get()).copyDepthFrom(this.minecraft.getMainRenderTarget());
/*      */           }
/*      */           if (shouldShowEntityOutlines() && entityOutlineTarget != null) {
/*      */             RenderTarget outlineTarget = (RenderTarget)entityOutlineTarget.get();
/*      */             RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(outlineTarget.getColorTexture(), 0, outlineTarget.getDepthTexture(), 1.0D);
/*      */           } 
/*      */           PoseStack poseStack = new PoseStack();
/*      */           MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource(), crumblingBufferSource = this.renderBuffers.crumblingBufferSource();
/*      */           profiler.popPush("submitEntities");
/*      */           submitEntities(poseStack, levelRenderState, this.submitNodeStorage);
/*      */           profiler.popPush("submitBlockEntities");
/*      */           submitBlockEntities(poseStack, levelRenderState, this.submitNodeStorage);
/*      */           profiler.popPush("renderFeatures");
/*      */           this.featureRenderDispatcher.renderAllFeatures();
/*      */           bufferSource.endLastBatch();
/*      */           checkPoseStack(poseStack);
/*      */           bufferSource.endBatch(RenderTypes.solidMovingBlock());
/*      */           bufferSource.endBatch(RenderTypes.endPortal());
/*      */           bufferSource.endBatch(RenderTypes.endGateway());
/*      */           bufferSource.endBatch(Sheets.solidBlockSheet());
/*      */           bufferSource.endBatch(Sheets.cutoutBlockSheet());
/*      */           bufferSource.endBatch(Sheets.bedSheet());
/*      */           bufferSource.endBatch(Sheets.shulkerBoxSheet());
/*      */           bufferSource.endBatch(Sheets.signSheet());
/*      */           bufferSource.endBatch(Sheets.hangingSignSheet());
/*      */           bufferSource.endBatch(Sheets.chestSheet());
/*      */           this.renderBuffers.outlineBufferSource().endOutlineBatch();
/*      */           if (renderOutline) {
/*      */             renderBlockOutline(bufferSource, poseStack, false, levelRenderState);
/*      */           }
/*      */           profiler.pop();
/*      */           finalizeGizmoCollection();
/*      */           this.finalizedGizmos.standardPrimitives().render(poseStack, bufferSource, levelRenderState.cameraRenderState, modelViewMatrix);
/*      */           bufferSource.endLastBatch();
/*      */           checkPoseStack(poseStack);
/*      */           bufferSource.endBatch(Sheets.translucentItemSheet());
/*      */           bufferSource.endBatch(Sheets.bannerSheet());
/*      */           bufferSource.endBatch(Sheets.shieldSheet());
/*      */           bufferSource.endBatch(RenderTypes.armorEntityGlint());
/*      */           bufferSource.endBatch(RenderTypes.glint());
/*      */           bufferSource.endBatch(RenderTypes.glintTranslucent());
/*      */           bufferSource.endBatch(RenderTypes.entityGlint());
/*      */           profiler.push("destroyProgress");
/*      */           renderBlockDestroyAnimation(poseStack, crumblingBufferSource, levelRenderState);
/*      */           crumblingBufferSource.endBatch();
/*      */           profiler.pop();
/*      */           checkPoseStack(poseStack);
/*      */           bufferSource.endBatch(RenderTypes.waterMask());
/*      */           bufferSource.endBatch();
/*      */           if (translucentTarget != null) {
/*      */             ((RenderTarget)translucentTarget.get()).copyDepthFrom((RenderTarget)mainTarget.get());
/*      */           }
/*      */           profiler.push("translucent");
/*      */           chunkSectionsToRender.renderGroup(ChunkSectionLayerGroup.TRANSLUCENT, this.chunkLayerSampler);
/*      */           profiler.popPush("string");
/*      */           chunkSectionsToRender.renderGroup(ChunkSectionLayerGroup.TRIPWIRE, this.chunkLayerSampler);
/*      */           if (renderOutline) {
/*      */             renderBlockOutline(bufferSource, poseStack, true, levelRenderState);
/*      */           }
/*      */           bufferSource.endBatch();
/*      */           profiler.pop();
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addParticlesPass(FrameGraphBuilder frame, GpuBufferSlice fog) {
/*  750 */     FramePass pass = frame.addPass("particles");
/*  751 */     if (this.targets.particles != null) {
/*  752 */       this.targets.particles = pass.readsAndWrites(this.targets.particles);
/*  753 */       pass.reads(this.targets.main);
/*      */     } else {
/*  755 */       this.targets.main = pass.readsAndWrites(this.targets.main);
/*      */     } 
/*      */     
/*  758 */     ResourceHandle<RenderTarget> mainTarget = this.targets.main;
/*  759 */     ResourceHandle<RenderTarget> particlesTarget = this.targets.particles;
/*  760 */     pass.executes(() -> {
/*      */           RenderSystem.setShaderFog(fog);
/*      */           if (particlesTarget != null) {
/*      */             ((RenderTarget)particlesTarget.get()).copyDepthFrom((RenderTarget)mainTarget.get());
/*      */           }
/*      */           this.particlesRenderState.submit(this.submitNodeStorage, this.levelRenderState.cameraRenderState);
/*      */           this.featureRenderDispatcher.renderAllFeatures();
/*      */           this.particlesRenderState.reset();
/*      */         });
/*      */   }
/*      */   
/*      */   private void addCloudsPass(FrameGraphBuilder frame, CloudStatus cloudsType, Vec3 cameraPosition, long gameTime, float partialTicks, int cloudColor, float cloudHeight) {
/*  772 */     FramePass pass = frame.addPass("clouds");
/*  773 */     if (this.targets.clouds != null) {
/*  774 */       this.targets.clouds = pass.readsAndWrites(this.targets.clouds);
/*      */     } else {
/*  776 */       this.targets.main = pass.readsAndWrites(this.targets.main);
/*      */     } 
/*  778 */     pass.executes(() -> this.cloudRenderer.render(cloudColor, cloudsType, cloudHeight, cameraPosition, gameTime, partialTicks));
/*      */   }
/*      */   
/*      */   private void addWeatherPass(FrameGraphBuilder frame, GpuBufferSlice fog) {
/*  782 */     int renderDistance = this.minecraft.options.getEffectiveRenderDistance() * 16;
/*  783 */     float depthFar = this.minecraft.gameRenderer.getDepthFar();
/*      */     
/*  785 */     FramePass pass = frame.addPass("weather");
/*  786 */     if (this.targets.weather != null) {
/*  787 */       this.targets.weather = pass.readsAndWrites(this.targets.weather);
/*      */     } else {
/*  789 */       this.targets.main = pass.readsAndWrites(this.targets.main);
/*      */     } 
/*  791 */     pass.executes(() -> {
/*      */           RenderSystem.setShaderFog(fog);
/*      */           MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
/*      */           CameraRenderState cameraState = this.levelRenderState.cameraRenderState;
/*      */           this.weatherEffectRenderer.render(bufferSource, cameraState.pos, this.levelRenderState.weatherRenderState);
/*      */           this.worldBorderRenderer.render(this.levelRenderState.worldBorderRenderState, cameraState.pos, renderDistance, depthFar);
/*      */           bufferSource.endBatch();
/*      */         });
/*      */   }
/*      */   
/*      */   private void addLateDebugPass(FrameGraphBuilder frame, CameraRenderState camera, GpuBufferSlice fog, Matrix4f modelViewMatrix) {
/*  802 */     FramePass pass = frame.addPass("late_debug");
/*  803 */     this.targets.main = pass.readsAndWrites(this.targets.main);
/*  804 */     if (this.targets.itemEntity != null)
/*      */     {
/*  806 */       this.targets.itemEntity = pass.readsAndWrites(this.targets.itemEntity);
/*      */     }
/*  808 */     ResourceHandle<RenderTarget> mainTarget = this.targets.main;
/*  809 */     pass.executes(() -> {
/*      */           RenderSystem.setShaderFog(fog);
/*      */           PoseStack poseStack = new PoseStack();
/*      */           MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
/*      */           RenderSystem.outputColorTextureOverride = ((RenderTarget)mainTarget.get()).getColorTextureView();
/*      */           RenderSystem.outputDepthTextureOverride = ((RenderTarget)mainTarget.get()).getDepthTextureView();
/*      */           if (!this.finalizedGizmos.alwaysOnTopPrimitives().isEmpty()) {
/*      */             RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
/*      */             RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mainRenderTarget.getDepthTexture(), 1.0D);
/*      */             this.finalizedGizmos.alwaysOnTopPrimitives().render(poseStack, bufferSource, camera, modelViewMatrix);
/*      */             bufferSource.endLastBatch();
/*      */           } 
/*      */           RenderSystem.outputColorTextureOverride = null;
/*      */           RenderSystem.outputDepthTextureOverride = null;
/*      */           checkPoseStack(poseStack);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void extractVisibleEntities(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState output) {
/*  833 */     Vec3 cameraPos = camera.position();
/*  834 */     double camX = cameraPos.x();
/*  835 */     double camY = cameraPos.y();
/*  836 */     double camZ = cameraPos.z();
/*  837 */     TickRateManager tickRateManager = this.minecraft.level.tickRateManager();
/*  838 */     boolean shouldShowEntityOutlines = shouldShowEntityOutlines();
/*      */     
/*  840 */     Entity.setViewScale(Mth.clamp(this.minecraft.options.getEffectiveRenderDistance() / 8.0D, 1.0D, 2.5D) * (Double)this.minecraft.options.entityDistanceScaling().get());
/*      */     
/*  842 */     for (Entity entity : (Iterable<Entity>)this.level.entitiesForRendering()) {
/*  843 */       if (!this.entityRenderDispatcher.shouldRender(entity, frustum, camX, camY, camZ) && !entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*      */         continue;
/*      */       }
/*  846 */       BlockPos blockPos = entity.blockPosition();
/*  847 */       if (!this.level.isOutsideBuildHeight(blockPos.getY()) && !isSectionCompiledAndVisible(blockPos)) {
/*      */         continue;
/*      */       }
/*  850 */       if (entity == camera.entity() && !camera.isDetached() && (!(camera.entity() instanceof LivingEntity) || !((LivingEntity)camera.entity()).isSleeping())) {
/*      */         continue;
/*      */       }
/*  853 */       if (entity instanceof net.minecraft.client.player.LocalPlayer && camera.entity() != entity) {
/*      */         continue;
/*      */       }
/*      */       
/*  857 */       if (entity.tickCount == 0) {
/*  858 */         entity.xOld = entity.getX();
/*  859 */         entity.yOld = entity.getY();
/*  860 */         entity.zOld = entity.getZ();
/*      */       } 
/*  862 */       float partialEntity = deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(entity));
/*  863 */       EntityRenderState state = extractEntity(entity, partialEntity);
/*  864 */       output.entityRenderStates.add(state);
/*  865 */       if (state.appearsGlowing() && shouldShowEntityOutlines) {
/*  866 */         output.haveGlowingEntities = true;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void submitEntities(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output) {
/*  872 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/*  873 */     double camX = cameraPos.x();
/*  874 */     double camY = cameraPos.y();
/*  875 */     double camZ = cameraPos.z();
/*  876 */     for (EntityRenderState state : (Iterable<EntityRenderState>)levelRenderState.entityRenderStates) {
/*  877 */       if (!levelRenderState.haveGlowingEntities) {
/*  878 */         state.outlineColor = 0;
/*      */       }
/*  880 */       this.entityRenderDispatcher.submit(state, levelRenderState.cameraRenderState, state.x - camX, state.y - camY, state.z - camZ, poseStack, output);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractVisibleBlockEntities(Camera camera, float deltaPartialTick, LevelRenderState levelRenderState) {
/*  885 */     Vec3 cameraPos = camera.position();
/*  886 */     double camX = cameraPos.x();
/*  887 */     double camY = cameraPos.y();
/*  888 */     double camZ = cameraPos.z();
/*      */ 
/*      */     
/*  891 */     PoseStack poseStack = new PoseStack();
/*      */     
/*  893 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.visibleSections.iterator(); objectListIterator.hasNext(); ) { SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/*  894 */       List<BlockEntity> renderableBlockEntities = section.getSectionMesh().getRenderableBlockEntities();
/*  895 */       if (renderableBlockEntities.isEmpty() || section.getVisibility(Util.getMillis()) < 0.3F) {
/*      */         continue;
/*      */       }
/*      */       
/*  899 */       for (BlockEntity blockEntity : renderableBlockEntities) {
/*  900 */         ModelFeatureRenderer.CrumblingOverlay breakProgress; BlockPos blockPos = blockEntity.getBlockPos();
/*  901 */         SortedSet<BlockDestructionProgress> progresses = (SortedSet<BlockDestructionProgress>)this.destructionProgress.get(blockPos.asLong());
/*      */         
/*  903 */         if (progresses == null || progresses.isEmpty()) {
/*  904 */           breakProgress = null;
/*      */         } else {
/*  906 */           poseStack.pushPose();
/*  907 */           poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
/*  908 */           breakProgress = new ModelFeatureRenderer.CrumblingOverlay(((BlockDestructionProgress)progresses.last()).getProgress(), poseStack.last());
/*  909 */           poseStack.popPose();
/*      */         } 
/*  911 */         BlockEntityRenderState state = this.blockEntityRenderDispatcher.tryExtractRenderState(blockEntity, deltaPartialTick, breakProgress);
/*  912 */         if (state != null) {
/*  913 */           levelRenderState.blockEntityRenderStates.add(state);
/*      */         }
/*      */       }  }
/*      */ 
/*      */     
/*  918 */     for (Iterator<BlockEntity> iterator = this.level.getGloballyRenderedBlockEntities().iterator(); iterator.hasNext(); ) {
/*  919 */       BlockEntity blockEntity = iterator.next();
/*  920 */       if (blockEntity.isRemoved()) {
/*  921 */         iterator.remove();
/*      */         continue;
/*      */       } 
/*  924 */       BlockEntityRenderState state = this.blockEntityRenderDispatcher.tryExtractRenderState(blockEntity, deltaPartialTick, null);
/*  925 */       if (state != null) {
/*  926 */         levelRenderState.blockEntityRenderStates.add(state);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void submitBlockEntities(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeStorage submitNodeStorage) {
/*  932 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/*  933 */     double camX = cameraPos.x();
/*  934 */     double camY = cameraPos.y();
/*  935 */     double camZ = cameraPos.z();
/*  936 */     for (BlockEntityRenderState renderState : (Iterable<BlockEntityRenderState>)levelRenderState.blockEntityRenderStates) {
/*  937 */       BlockPos blockPos = renderState.blockPos;
/*  938 */       poseStack.pushPose();
/*  939 */       poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
/*  940 */       this.blockEntityRenderDispatcher.submit(renderState, poseStack, submitNodeStorage, levelRenderState.cameraRenderState);
/*  941 */       poseStack.popPose();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractBlockDestroyAnimation(Camera camera, LevelRenderState levelRenderState) {
/*  946 */     Vec3 cameraPos = camera.position();
/*  947 */     double camX = cameraPos.x();
/*  948 */     double camY = cameraPos.y();
/*  949 */     double camZ = cameraPos.z();
/*      */     
/*  951 */     levelRenderState.blockBreakingRenderStates.clear();
/*  952 */     for (ObjectIterator<Long2ObjectMap.Entry<SortedSet<BlockDestructionProgress>>> objectIterator = this.destructionProgress.long2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<SortedSet<BlockDestructionProgress>> entry = objectIterator.next();
/*  953 */       BlockPos pos = BlockPos.of(entry.getLongKey());
/*  954 */       if (pos.distToCenterSqr(camX, camY, camZ) > 1024.0D) {
/*      */         continue;
/*      */       }
/*      */       
/*  958 */       SortedSet<BlockDestructionProgress> progresses = (SortedSet<BlockDestructionProgress>)entry.getValue();
/*  959 */       if (progresses == null || progresses.isEmpty()) {
/*      */         continue;
/*      */       }
/*  962 */       int progress = ((BlockDestructionProgress)progresses.last()).getProgress();
/*  963 */       levelRenderState.blockBreakingRenderStates.add(new BlockBreakingRenderState(this.level, pos, progress)); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderBlockDestroyAnimation(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LevelRenderState levelRenderState) {
/*  971 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/*  972 */     double camX = cameraPos.x();
/*  973 */     double camY = cameraPos.y();
/*  974 */     double camZ = cameraPos.z();
/*      */     
/*  976 */     for (BlockBreakingRenderState state : (Iterable<BlockBreakingRenderState>)levelRenderState.blockBreakingRenderStates) {
/*  977 */       poseStack.pushPose();
/*  978 */       BlockPos pos = state.blockPos;
/*  979 */       poseStack.translate(pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ);
/*  980 */       PoseStack.Pose cameraPose = poseStack.last();
/*  981 */       SheetedDecalTextureGenerator sheetedDecalTextureGenerator = new SheetedDecalTextureGenerator(bufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(state.progress)), cameraPose, 1.0F);
/*  982 */       this.minecraft.getBlockRenderer().renderBreakingTexture(state.blockState, pos, (BlockAndTintGetter)state, poseStack, (VertexConsumer)sheetedDecalTextureGenerator);
/*  983 */       poseStack.popPose();
/*      */     } 
/*      */   }
/*      */   private void extractBlockOutline(Camera camera, LevelRenderState levelRenderState) {
/*      */     BlockHitResult blockHitResult;
/*  988 */     levelRenderState.blockOutlineRenderState = null;
/*  989 */     HitResult hitResult = this.minecraft.hitResult; if (hitResult instanceof BlockHitResult) { blockHitResult = (BlockHitResult)hitResult; }
/*      */     else
/*      */     { return; }
/*      */     
/*  993 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/*      */       return;
/*      */     }
/*  996 */     BlockPos pos = blockHitResult.getBlockPos();
/*  997 */     BlockState state = this.level.getBlockState(pos);
/*  998 */     if (!state.isAir() && this.level.getWorldBorder().isWithinBounds(pos)) {
/*  999 */       boolean isBlockTranslucent = ItemBlockRenderTypes.getChunkRenderType(state).sortOnUpload();
/* 1000 */       boolean highContrast = (Boolean)this.minecraft.options.highContrastBlockOutline().get();
/* 1001 */       CollisionContext context = CollisionContext.of(camera.entity());
/* 1002 */       VoxelShape shape = state.getShape((BlockGetter)this.level, pos, context);
/* 1003 */       if (SharedConstants.DEBUG_SHAPES) {
/* 1004 */         VoxelShape collisionShape = state.getCollisionShape((BlockGetter)this.level, pos, context);
/* 1005 */         VoxelShape occlusionShape = state.getOcclusionShape();
/* 1006 */         VoxelShape interactionShape = state.getInteractionShape((BlockGetter)this.level, pos);
/* 1007 */         levelRenderState.blockOutlineRenderState = new BlockOutlineRenderState(pos, isBlockTranslucent, highContrast, shape, collisionShape, occlusionShape, interactionShape);
/*      */       } else {
/* 1009 */         levelRenderState.blockOutlineRenderState = new BlockOutlineRenderState(pos, isBlockTranslucent, highContrast, shape);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderBlockOutline(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean onlyTranslucentBlocks, LevelRenderState levelRenderState) {
/* 1015 */     BlockOutlineRenderState state = levelRenderState.blockOutlineRenderState;
/* 1016 */     if (state == null) {
/*      */       return;
/*      */     }
/* 1019 */     if (state.isTranslucent() != onlyTranslucentBlocks) {
/*      */       return;
/*      */     }
/* 1022 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/* 1023 */     if (state.highContrast()) {
/* 1024 */       VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderTypes.secondaryBlockOutline());
/* 1025 */       renderHitOutline(poseStack, vertexConsumer, cameraPos.x, cameraPos.y, cameraPos.z, state, -16777216, 7.0F);
/*      */     } 
/* 1027 */     VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lines());
/* 1028 */     int outlineColor = state.highContrast() ? -11010079 : ARGB.black(102);
/* 1029 */     renderHitOutline(poseStack, buffer, cameraPos.x, cameraPos.y, cameraPos.z, state, outlineColor, this.minecraft.getWindow().getAppropriateLineWidth());
/* 1030 */     bufferSource.endLastBatch();
/*      */   }
/*      */   
/*      */   private void checkPoseStack(PoseStack poseStack) {
/* 1034 */     if (!poseStack.isEmpty()) {
/* 1035 */       throw new IllegalStateException("Pose stack not empty");
/*      */     }
/*      */   }
/*      */   
/*      */   private EntityRenderState extractEntity(Entity entity, float partialTickTime) {
/* 1040 */     return this.entityRenderDispatcher.extractEntity(entity, partialTickTime);
/*      */   }
/*      */   
/*      */   private void scheduleTranslucentSectionResort(Vec3 cameraPos) {
/* 1044 */     if (this.visibleSections.isEmpty()) {
/*      */       return;
/*      */     }
/* 1047 */     BlockPos cameraBlockPos = BlockPos.containing((Position)cameraPos);
/* 1048 */     boolean blockPosChanged = !cameraBlockPos.equals(this.lastTranslucentSortBlockPos);
/* 1049 */     TranslucencyPointOfView pointOfView = new TranslucencyPointOfView();
/* 1050 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.nearbyVisibleSections.iterator(); objectListIterator.hasNext(); ) { SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/* 1051 */       scheduleResort(section, pointOfView, cameraPos, blockPosChanged, true); }
/*      */     
/* 1053 */     this.translucencyResortIterationIndex %= this.visibleSections.size();
/* 1054 */     int resortsLeft = Math.max(this.visibleSections.size() / 8, 15);
/* 1055 */     while (resortsLeft-- > 0) {
/* 1056 */       int index = this.translucencyResortIterationIndex++ % this.visibleSections.size();
/* 1057 */       scheduleResort((SectionRenderDispatcher.RenderSection)this.visibleSections.get(index), pointOfView, cameraPos, blockPosChanged, false);
/*      */     } 
/* 1059 */     this.lastTranslucentSortBlockPos = cameraBlockPos;
/*      */   }
/*      */   
/*      */   private void scheduleResort(SectionRenderDispatcher.RenderSection section, TranslucencyPointOfView pointOfView, Vec3 cameraPos, boolean blockPosChanged, boolean isNearby) {
/* 1063 */     pointOfView.set(cameraPos, section.getSectionNode());
/* 1064 */     boolean pointOfViewChanged = section.getSectionMesh().isDifferentPointOfView(pointOfView);
/* 1065 */     boolean resortBecauseBlockPosChanged = (blockPosChanged && (pointOfView.isAxisAligned() || isNearby));
/*      */ 
/*      */ 
/*      */     
/* 1069 */     if ((resortBecauseBlockPosChanged || pointOfViewChanged) && !section.transparencyResortingScheduled() && section.hasTranslucentGeometry()) {
/* 1070 */       section.resortTransparency(this.sectionRenderDispatcher);
/*      */     }
/*      */   }
/*      */   
/*      */   private ChunkSectionsToRender prepareChunkRenders(Matrix4fc modelViewMatrix, double camX, double camY, double camZ) {
/* 1075 */     ObjectListIterator<SectionRenderDispatcher.RenderSection> iterator = this.visibleSections.listIterator(0);
/*      */     
/* 1077 */     EnumMap<ChunkSectionLayer, List<RenderPass.Draw<GpuBufferSlice[]>>> drawsByLayer = new EnumMap<>(ChunkSectionLayer.class);
/* 1078 */     int largestIndexCount = 0;
/*      */     
/* 1080 */     for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
/* 1081 */       drawsByLayer.put(layer, new ArrayList<>());
/*      */     }
/*      */     
/* 1084 */     List<DynamicUniforms.ChunkSectionInfo> sectionInfos = new ArrayList<>();
/* 1085 */     GpuTextureView blockAtlas = this.minecraft.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).getTextureView();
/* 1086 */     int textureAtlasWidth = blockAtlas.getWidth(0);
/* 1087 */     int textureAtlasHeight = blockAtlas.getHeight(0);
/*      */     
/* 1089 */     while (iterator.hasNext()) {
/* 1090 */       SectionRenderDispatcher.RenderSection section = (SectionRenderDispatcher.RenderSection)iterator.next();
/*      */       
/* 1092 */       SectionMesh sectionMesh = section.getSectionMesh();
/* 1093 */       BlockPos renderOffset = section.getRenderOrigin();
/* 1094 */       long now = Util.getMillis();
/* 1095 */       int uboIndex = -1;
/*      */       
/* 1097 */       for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
/* 1098 */         SectionBuffers buffers = sectionMesh.getBuffers(layer);
/* 1099 */         if (buffers != null) {
/*      */           GpuBuffer indexBuffer;
/*      */           VertexFormat.IndexType indexType;
/* 1102 */           if (uboIndex == -1) {
/*      */ 
/*      */             
/* 1105 */             uboIndex = sectionInfos.size();
/* 1106 */             sectionInfos.add(new DynamicUniforms.ChunkSectionInfo((Matrix4fc)new Matrix4f(modelViewMatrix), renderOffset.getX(), renderOffset.getY(), renderOffset.getZ(), section.getVisibility(now), textureAtlasWidth, textureAtlasHeight));
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1112 */           if (buffers.getIndexBuffer() == null) {
/* 1113 */             if (buffers.getIndexCount() > largestIndexCount) {
/* 1114 */               largestIndexCount = buffers.getIndexCount();
/*      */             }
/* 1116 */             indexBuffer = null;
/* 1117 */             indexType = null;
/*      */           } else {
/* 1119 */             indexBuffer = buffers.getIndexBuffer();
/* 1120 */             indexType = buffers.getIndexType();
/*      */           } 
/*      */           
/* 1123 */           int finalUboIndex = uboIndex;
/* 1124 */           ((List<RenderPass.Draw>)drawsByLayer.get(layer)).add(new RenderPass.Draw(0, buffers.getVertexBuffer(), indexBuffer, indexType, 0, buffers.getIndexCount(), (sectionUbos, uploader) -> uploader.upload("ChunkSection", sectionUbos[finalUboIndex])));
/*      */         } 
/*      */       } 
/*      */     } 
/* 1128 */     GpuBufferSlice[] chunkSectionInfos = RenderSystem.getDynamicUniforms().writeChunkSections(sectionInfos.<DynamicUniforms.ChunkSectionInfo>toArray(new DynamicUniforms.ChunkSectionInfo[0]));
/* 1129 */     return new ChunkSectionsToRender(blockAtlas, drawsByLayer, largestIndexCount, chunkSectionInfos);
/*      */   }
/*      */   
/*      */   public void endFrame() {
/* 1133 */     this.cloudRenderer.endFrame();
/*      */   }
/*      */   
/*      */   public void captureFrustum() {
/* 1137 */     this.captureFrustum = true;
/*      */   }
/*      */   
/*      */   public void killFrustum() {
/* 1141 */     this.capturedFrustum = null;
/*      */   }
/*      */   
/*      */   public void tick(Camera camera) {
/* 1145 */     if (this.level.tickRateManager().runsNormally()) {
/* 1146 */       this.ticks++;
/*      */     }
/* 1148 */     this.weatherEffectRenderer.tickRainParticles(this.level, camera, this.ticks, (ParticleStatus)this.minecraft.options.particles().get(), (Integer)this.minecraft.options.weatherRadius().get());
/*      */     
/* 1150 */     removeBlockBreakingProgress();
/*      */   }
/*      */   
/*      */   private void removeBlockBreakingProgress() {
/* 1154 */     if (this.ticks % 20 != 0) {
/*      */       return;
/*      */     }
/*      */     
/* 1158 */     ObjectIterator<BlockDestructionProgress> objectIterator = this.destroyingBlocks.values().iterator();
/* 1159 */     while (objectIterator.hasNext()) {
/* 1160 */       BlockDestructionProgress block = objectIterator.next();
/*      */       
/* 1162 */       int updatedRenderTick = block.getUpdatedRenderTick();
/*      */       
/* 1164 */       if (this.ticks - updatedRenderTick > 400) {
/* 1165 */         objectIterator.remove();
/* 1166 */         removeProgress(block);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void removeProgress(BlockDestructionProgress block) {
/* 1172 */     long pos = block.getPos().asLong();
/* 1173 */     Set<BlockDestructionProgress> progresses = (Set<BlockDestructionProgress>)this.destructionProgress.get(pos);
/* 1174 */     progresses.remove(block);
/* 1175 */     if (progresses.isEmpty()) {
/* 1176 */       this.destructionProgress.remove(pos);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addSkyPass(FrameGraphBuilder frame, Camera camera, GpuBufferSlice skyFog) {
/* 1181 */     FogType fogType = camera.getFluidInCamera();
/* 1182 */     if (fogType == FogType.POWDER_SNOW || fogType == FogType.LAVA || doesMobEffectBlockSky(camera)) {
/*      */       return;
/*      */     }
/*      */     
/* 1186 */     SkyRenderState state = this.levelRenderState.skyRenderState;
/* 1187 */     if (state.skybox == DimensionType.Skybox.NONE) {
/*      */       return;
/*      */     }
/*      */     
/* 1191 */     SkyRenderer skyRenderer = this.skyRenderer;
/* 1192 */     if (skyRenderer == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1196 */     FramePass pass = frame.addPass("sky");
/* 1197 */     this.targets.main = pass.readsAndWrites(this.targets.main);
/*      */     
/* 1199 */     pass.executes(() -> {
/*      */           RenderSystem.setShaderFog(skyFog);
/*      */           if (state.skybox == DimensionType.Skybox.END) {
/*      */             skyRenderer.renderEndSky();
/*      */             if (state.endFlashIntensity > 1.0E-5F) {
/*      */               PoseStack poseStack1 = new PoseStack();
/*      */               skyRenderer.renderEndFlash(poseStack1, state.endFlashIntensity, state.endFlashXAngle, state.endFlashYAngle);
/*      */             } 
/*      */             return;
/*      */           } 
/*      */           PoseStack poseStack = new PoseStack();
/*      */           skyRenderer.renderSkyDisc(state.skyColor);
/*      */           skyRenderer.renderSunriseAndSunset(poseStack, state.sunAngle, state.sunriseAndSunsetColor);
/*      */           skyRenderer.renderSunMoonAndStars(poseStack, state.sunAngle, state.moonAngle, state.starAngle, state.moonPhase, state.rainBrightness, state.starBrightness);
/*      */           if (state.shouldRenderDarkDisc) {
/*      */             skyRenderer.renderDarkDisc();
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean doesMobEffectBlockSky(Camera camera) {
/* 1226 */     Entity entity = camera.entity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 1227 */       return (livingEntity.hasEffect(MobEffects.BLINDNESS) || livingEntity.hasEffect(MobEffects.DARKNESS)); }
/*      */     
/* 1229 */     return false;
/*      */   }
/*      */   
/*      */   private void compileSections(Camera camera) {
/* 1233 */     ProfilerFiller profiler = Profiler.get();
/* 1234 */     profiler.push("populateSectionsToCompile");
/*      */     
/* 1236 */     RenderRegionCache cache = new RenderRegionCache();
/* 1237 */     BlockPos cameraPosition = camera.blockPosition();
/* 1238 */     List<SectionRenderDispatcher.RenderSection> sectionsToCompile = Lists.newArrayList();
/* 1239 */     long fadeDuration = Mth.floor((Double)this.minecraft.options.chunkSectionFadeInTime().get() * 1000.0D);
/* 1240 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.visibleSections.iterator(); objectListIterator.hasNext(); ) { SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/* 1241 */       if (section.isDirty() && (section.getSectionMesh() != CompiledSectionMesh.UNCOMPILED || section.hasAllNeighbors())) {
/* 1242 */         BlockPos center = SectionPos.of(section.getSectionNode()).center();
/* 1243 */         double distSqr = center.distSqr((Vec3i)cameraPosition);
/* 1244 */         boolean isNearby = (distSqr < 768.0D);
/*      */         boolean rebuildSync = false;
/* 1246 */         if (this.minecraft.options.prioritizeChunkUpdates().get() == PrioritizeChunkUpdates.NEARBY) {
/* 1247 */           rebuildSync = (isNearby || section.isDirtyFromPlayer());
/* 1248 */         } else if (this.minecraft.options.prioritizeChunkUpdates().get() == PrioritizeChunkUpdates.PLAYER_AFFECTED) {
/* 1249 */           rebuildSync = section.isDirtyFromPlayer();
/*      */         } 
/* 1251 */         if (isNearby || section.wasPreviouslyEmpty()) {
/* 1252 */           section.setFadeDuration(0L);
/*      */         } else {
/* 1254 */           section.setFadeDuration(fadeDuration);
/*      */         } 
/* 1256 */         section.setWasPreviouslyEmpty(false);
/* 1257 */         if (rebuildSync) {
/* 1258 */           profiler.push("compileSectionSynchronously");
/* 1259 */           this.sectionRenderDispatcher.rebuildSectionSync(section, cache);
/* 1260 */           section.setNotDirty();
/* 1261 */           profiler.pop(); continue;
/*      */         } 
/* 1263 */         sectionsToCompile.add(section);
/*      */       }  }
/*      */ 
/*      */     
/* 1267 */     profiler.popPush("uploadSectionMeshes");
/*      */     
/* 1269 */     this.sectionRenderDispatcher.uploadAllPendingUploads();
/*      */     
/* 1271 */     profiler.popPush("scheduleAsyncCompile");
/*      */     
/* 1273 */     for (SectionRenderDispatcher.RenderSection renderSection : sectionsToCompile) {
/* 1274 */       renderSection.rebuildSectionAsync(cache);
/* 1275 */       renderSection.setNotDirty();
/*      */     } 
/*      */     
/* 1278 */     profiler.popPush("scheduleTranslucentResort");
/* 1279 */     scheduleTranslucentSectionResort(camera.position());
/* 1280 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private void renderHitOutline(PoseStack poseStack, VertexConsumer builder, double camX, double camY, double camZ, BlockOutlineRenderState state, int color, float width) {
/* 1284 */     BlockPos pos = state.pos();
/* 1285 */     if (SharedConstants.DEBUG_SHAPES) {
/* 1286 */       ShapeRenderer.renderShape(poseStack, builder, state.shape(), pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, ARGB.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F), width);
/* 1287 */       if (state.collisionShape() != null) {
/* 1288 */         ShapeRenderer.renderShape(poseStack, builder, state.collisionShape(), pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, ARGB.colorFromFloat(0.4F, 0.0F, 0.0F, 0.0F), width);
/*      */       }
/* 1290 */       if (state.occlusionShape() != null) {
/* 1291 */         ShapeRenderer.renderShape(poseStack, builder, state.occlusionShape(), pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, ARGB.colorFromFloat(0.4F, 0.0F, 1.0F, 0.0F), width);
/*      */       }
/* 1293 */       if (state.interactionShape() != null) {
/* 1294 */         ShapeRenderer.renderShape(poseStack, builder, state.interactionShape(), pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, ARGB.colorFromFloat(0.4F, 0.0F, 0.0F, 1.0F), width);
/*      */       }
/*      */     } else {
/* 1297 */       ShapeRenderer.renderShape(poseStack, builder, state.shape(), pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, color, width);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void blockChanged(BlockGetter level, BlockPos pos, BlockState old, BlockState current, @net.minecraft.world.level.block.Block.UpdateFlags int updateFlags) {
/* 1302 */     setBlockDirty(pos, ((updateFlags & 0x8) != 0));
/*      */   }
/*      */   
/*      */   private void setBlockDirty(BlockPos pos, boolean playerChanged) {
/* 1306 */     for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
/* 1307 */       for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
/* 1308 */         for (int y = pos.getY() - 1; y <= pos.getY() + 1; y++) {
/* 1309 */           setSectionDirty(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(y), SectionPos.blockToSectionCoord(z), playerChanged);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setBlocksDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
/* 1316 */     for (int z = z0 - 1; z <= z1 + 1; z++) {
/* 1317 */       for (int x = x0 - 1; x <= x1 + 1; x++) {
/* 1318 */         for (int y = y0 - 1; y <= y1 + 1; y++) {
/* 1319 */           setSectionDirty(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(y), SectionPos.blockToSectionCoord(z));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setBlockDirty(BlockPos pos, BlockState oldState, BlockState newState) {
/* 1326 */     if (this.minecraft.getModelManager().requiresRender(oldState, newState)) {
/* 1327 */       setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
/*      */     }
/*      */   }
/*      */   
/*      */   public void setSectionDirtyWithNeighbors(int sectionX, int sectionY, int sectionZ) {
/* 1332 */     setSectionRangeDirty(sectionX - 1, sectionY - 1, sectionZ - 1, sectionX + 1, sectionY + 1, sectionZ + 1);
/*      */   }
/*      */   
/*      */   public void setSectionRangeDirty(int minSectionX, int minSectionY, int minSectionZ, int maxSectionX, int maxSectionY, int maxSectionZ) {
/* 1336 */     for (int z = minSectionZ; z <= maxSectionZ; z++) {
/* 1337 */       for (int x = minSectionX; x <= maxSectionX; x++) {
/* 1338 */         for (int y = minSectionY; y <= maxSectionY; y++) {
/* 1339 */           setSectionDirty(x, y, z);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setSectionDirty(int sectionX, int sectionY, int sectionZ) {
/* 1346 */     setSectionDirty(sectionX, sectionY, sectionZ, false);
/*      */   }
/*      */   
/*      */   private void setSectionDirty(int sectionX, int sectionY, int sectionZ, boolean playerChanged) {
/* 1350 */     this.viewArea.setDirty(sectionX, sectionY, sectionZ, playerChanged);
/*      */   }
/*      */   
/*      */   public void onSectionBecomingNonEmpty(long sectionNode) {
/* 1354 */     SectionRenderDispatcher.RenderSection section = this.viewArea.getRenderSection(sectionNode);
/* 1355 */     if (section != null) {
/* 1356 */       this.sectionOcclusionGraph.schedulePropagationFrom(section);
/* 1357 */       section.setWasPreviouslyEmpty(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void destroyBlockProgress(int id, BlockPos pos, int progress) {
/* 1362 */     if (progress < 0 || progress >= 10) {
/* 1363 */       BlockDestructionProgress removed = (BlockDestructionProgress)this.destroyingBlocks.remove(id);
/* 1364 */       if (removed != null) {
/* 1365 */         removeProgress(removed);
/*      */       }
/*      */     } else {
/* 1368 */       BlockDestructionProgress entry = (BlockDestructionProgress)this.destroyingBlocks.get(id);
/* 1369 */       if (entry != null) {
/* 1370 */         removeProgress(entry);
/*      */       }
/*      */       
/* 1373 */       if (entry == null || entry.getPos().getX() != pos.getX() || entry.getPos().getY() != pos.getY() || entry.getPos().getZ() != pos.getZ()) {
/* 1374 */         entry = new BlockDestructionProgress(id, pos);
/* 1375 */         this.destroyingBlocks.put(id, entry);
/*      */       } 
/*      */       
/* 1378 */       entry.setProgress(progress);
/* 1379 */       entry.updateTick(this.ticks);
/* 1380 */       ((SortedSet<BlockDestructionProgress>)this.destructionProgress.computeIfAbsent(entry.getPos().asLong(), k -> Sets.newTreeSet())).add(entry);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasRenderedAllSections() {
/* 1385 */     return this.sectionRenderDispatcher.isQueueEmpty();
/*      */   }
/*      */   
/*      */   public void onChunkReadyToRender(ChunkPos pos) {
/* 1389 */     this.sectionOcclusionGraph.onChunkReadyToRender(pos);
/*      */   }
/*      */   
/*      */   public void needsUpdate() {
/* 1393 */     this.sectionOcclusionGraph.invalidate();
/* 1394 */     this.cloudRenderer.markForRebuild();
/*      */   }
/*      */   
/*      */   public static int getLightColor(BlockAndTintGetter level, BlockPos pos) {
/* 1398 */     return getLightColor(BrightnessGetter.DEFAULT, level, level.getBlockState(pos), pos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getLightColor(BrightnessGetter brightnessGetter, BlockAndTintGetter level, BlockState state, BlockPos pos) {
/* 1406 */     if (state.emissiveRendering((BlockGetter)level, pos))
/*      */     {
/*      */       
/* 1409 */       return 15728880;
/*      */     }
/*      */     
/* 1412 */     int packedBrightness = brightnessGetter.packedBrightness(level, pos);
/*      */     
/* 1414 */     int block = LightTexture.block(packedBrightness);
/*      */     
/* 1416 */     int blockSelfEmission = state.getLightEmission();
/* 1417 */     if (block < blockSelfEmission) {
/* 1418 */       int sky = LightTexture.sky(packedBrightness);
/* 1419 */       return LightTexture.pack(blockSelfEmission, sky);
/*      */     } 
/* 1421 */     return packedBrightness;
/*      */   }
/*      */   
/*      */   public boolean isSectionCompiledAndVisible(BlockPos blockPos) {
/* 1425 */     SectionRenderDispatcher.RenderSection renderSection = this.viewArea.getRenderSectionAt(blockPos);
/* 1426 */     if (renderSection == null || renderSection.sectionMesh.get() == CompiledSectionMesh.UNCOMPILED) {
/* 1427 */       return false;
/*      */     }
/* 1429 */     return (renderSection.getVisibility(Util.getMillis()) >= 0.3F);
/*      */   }
/*      */   
/*      */   public RenderTarget entityOutlineTarget() {
/* 1433 */     return (this.targets.entityOutline != null) ? (RenderTarget)this.targets.entityOutline.get() : null;
/*      */   }
/*      */   
/*      */   public RenderTarget getTranslucentTarget() {
/* 1437 */     return (this.targets.translucent != null) ? (RenderTarget)this.targets.translucent.get() : null;
/*      */   }
/*      */   
/*      */   public RenderTarget getItemEntityTarget() {
/* 1441 */     return (this.targets.itemEntity != null) ? (RenderTarget)this.targets.itemEntity.get() : null;
/*      */   }
/*      */   
/*      */   public RenderTarget getParticlesTarget() {
/* 1445 */     return (this.targets.particles != null) ? (RenderTarget)this.targets.particles.get() : null;
/*      */   }
/*      */   
/*      */   public RenderTarget getWeatherTarget() {
/* 1449 */     return (this.targets.weather != null) ? (RenderTarget)this.targets.weather.get() : null;
/*      */   }
/*      */   
/*      */   public RenderTarget getCloudsTarget() {
/* 1453 */     return (this.targets.clouds != null) ? (RenderTarget)this.targets.clouds.get() : null;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public ObjectArrayList<SectionRenderDispatcher.RenderSection> getVisibleSections() {
/* 1458 */     return this.visibleSections;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public SectionOcclusionGraph getSectionOcclusionGraph() {
/* 1463 */     return this.sectionOcclusionGraph;
/*      */   }
/*      */   
/*      */   public Frustum getCapturedFrustum() {
/* 1467 */     return this.capturedFrustum;
/*      */   }
/*      */   
/*      */   public CloudRenderer getCloudRenderer() {
/* 1471 */     return this.cloudRenderer;
/*      */   }
/*      */   @FunctionalInterface
/*      */   public static interface BrightnessGetter { public static final BrightnessGetter DEFAULT;
/*      */     static {
/* 1476 */       DEFAULT = ((level, pos) -> {
/*      */           int sky = level.getBrightness(LightLayer.SKY, pos), block = level.getBrightness(LightLayer.BLOCK, pos);
/*      */           return Brightness.pack(block, sky);
/*      */         });
/*      */     }
/*      */     
/*      */     int packedBrightness(BlockAndTintGetter param1BlockAndTintGetter, BlockPos param1BlockPos); }
/*      */ 
/*      */   
/*      */   public Gizmos.TemporaryCollection collectPerFrameGizmos() {
/* 1486 */     return Gizmos.withCollector((GizmoCollector)this.collectedGizmos);
/*      */   }
/*      */   
/*      */   private void finalizeGizmoCollection() {
/* 1490 */     DrawableGizmoPrimitives standardPrimitives = new DrawableGizmoPrimitives();
/* 1491 */     DrawableGizmoPrimitives alwaysOnTopPrimitives = new DrawableGizmoPrimitives();
/* 1492 */     this.collectedGizmos.addTemporaryGizmos(this.minecraft.getPerTickGizmos());
/* 1493 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/* 1494 */     if (server != null) {
/* 1495 */       this.collectedGizmos.addTemporaryGizmos(server.getPerTickGizmos());
/*      */     }
/* 1497 */     long currentMillis = Util.getMillis();
/* 1498 */     for (SimpleGizmoCollector.GizmoInstance instance : (Iterable<SimpleGizmoCollector.GizmoInstance>)this.collectedGizmos.drainGizmos()) {
/* 1499 */       instance.gizmo().emit(instance.isAlwaysOnTop() ? (GizmoPrimitives)alwaysOnTopPrimitives : (GizmoPrimitives)standardPrimitives, instance.getAlphaMultiplier(currentMillis));
/*      */     }
/* 1501 */     this.finalizedGizmos = new FinalizedGizmos(standardPrimitives, alwaysOnTopPrimitives);
/*      */   }
/*      */   private static final class FinalizedGizmos extends Record { private final DrawableGizmoPrimitives standardPrimitives; private final DrawableGizmoPrimitives alwaysOnTopPrimitives;
/* 1504 */     private FinalizedGizmos(DrawableGizmoPrimitives standardPrimitives, DrawableGizmoPrimitives alwaysOnTopPrimitives) { this.standardPrimitives = standardPrimitives; this.alwaysOnTopPrimitives = alwaysOnTopPrimitives; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1504	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/* 1504 */       //   0	7	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos; } public DrawableGizmoPrimitives standardPrimitives() { return this.standardPrimitives; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1504	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1504	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/* 1504 */       //   0	8	1	o	Ljava/lang/Object; } public DrawableGizmoPrimitives alwaysOnTopPrimitives() { return this.alwaysOnTopPrimitives; }
/*      */      }
/*      */ 
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/LevelRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */