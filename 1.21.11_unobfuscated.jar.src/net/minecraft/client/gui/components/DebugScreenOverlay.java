/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.KeyMapping;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntry;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntryList;
/*     */ import net.minecraft.client.gui.components.debugchart.BandwidthDebugChart;
/*     */ import net.minecraft.client.gui.components.debugchart.FpsDebugChart;
/*     */ import net.minecraft.client.gui.components.debugchart.PingDebugChart;
/*     */ import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
/*     */ import net.minecraft.client.gui.components.debugchart.TpsDebugChart;
/*     */ import net.minecraft.client.gui.screens.LevelLoadingScreen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ChunkLevel;
/*     */ import net.minecraft.server.level.ChunkResult;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*     */ import net.minecraft.util.debugchart.SampleStorage;
/*     */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class DebugScreenOverlay
/*     */ {
/*     */   private static final float CROSSHAIR_SCALE = 0.01F;
/*     */   private static final int CROSSHAIR_INDEX_COUNT = 36;
/*     */   private static final int MARGIN_RIGHT = 2;
/*     */   private static final int MARGIN_LEFT = 2;
/*     */   private static final int MARGIN_TOP = 2;
/*     */   private final Minecraft minecraft;
/*     */   private final Font font;
/*     */   private final GpuBuffer crosshairBuffer;
/*  80 */   private final RenderSystem.AutoStorageIndexBuffer crosshairIndicies = RenderSystem.getSequentialBuffer(VertexFormat.Mode.LINES);
/*     */   
/*     */   private ChunkPos lastPos;
/*     */   private LevelChunk clientChunk;
/*     */   private CompletableFuture<LevelChunk> serverChunk;
/*     */   private boolean renderProfilerChart;
/*     */   private boolean renderFpsCharts;
/*     */   private boolean renderNetworkCharts;
/*  88 */   private final LocalSampleLogger frameTimeLogger = new LocalSampleLogger(1);
/*  89 */   private final LocalSampleLogger tickTimeLogger = new LocalSampleLogger((TpsDebugDimensions.values()).length);
/*  90 */   private final LocalSampleLogger pingLogger = new LocalSampleLogger(1);
/*  91 */   private final LocalSampleLogger bandwidthLogger = new LocalSampleLogger(1);
/*  92 */   private final Map<RemoteDebugSampleType, LocalSampleLogger> remoteSupportingLoggers = Map.of(RemoteDebugSampleType.TICK_TIME, this.tickTimeLogger);
/*     */   private final FpsDebugChart fpsChart;
/*     */   private final TpsDebugChart tpsChart;
/*     */   private final PingDebugChart pingChart;
/*     */   private final BandwidthDebugChart bandwidthChart;
/*     */   private final ProfilerPieChart profilerPieChart;
/*     */   
/*     */   public DebugScreenOverlay(Minecraft minecraft) {
/* 100 */     this.minecraft = minecraft;
/* 101 */     this.font = minecraft.font;
/* 102 */     this.fpsChart = new FpsDebugChart(this.font, (SampleStorage)this.frameTimeLogger);
/* 103 */     this.tpsChart = new TpsDebugChart(this.font, (SampleStorage)this.tickTimeLogger, () -> (minecraft.level == null) ? 0.0F : minecraft.level.tickRateManager().millisecondsPerTick());
/* 104 */     this.pingChart = new PingDebugChart(this.font, (SampleStorage)this.pingLogger);
/* 105 */     this.bandwidthChart = new BandwidthDebugChart(this.font, (SampleStorage)this.bandwidthLogger);
/* 106 */     this.profilerPieChart = new ProfilerPieChart(this.font);
/*     */     
/* 108 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH.getVertexSize() * 12 * 2); 
/* 109 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
/* 110 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(4.0F);
/* 111 */       bufferBuilder.addVertex(1.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(4.0F);
/* 112 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(4.0F);
/* 113 */       bufferBuilder.addVertex(0.0F, 1.0F, 0.0F).setColor(-16777216).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(4.0F);
/* 114 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(4.0F);
/* 115 */       bufferBuilder.addVertex(0.0F, 0.0F, 1.0F).setColor(-16777216).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(4.0F);
/*     */       
/* 117 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-65536).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
/* 118 */       bufferBuilder.addVertex(1.0F, 0.0F, 0.0F).setColor(-65536).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
/* 119 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16711936).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
/* 120 */       bufferBuilder.addVertex(0.0F, 1.0F, 0.0F).setColor(-16711936).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
/* 121 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-8421377).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
/* 122 */       bufferBuilder.addVertex(0.0F, 0.0F, 1.0F).setColor(-8421377).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
/* 123 */       MeshData meshData = bufferBuilder.buildOrThrow(); 
/* 124 */       try { this.crosshairBuffer = RenderSystem.getDevice().createBuffer(() -> "Crosshair vertex buffer", 32, meshData.vertexBuffer());
/* 125 */         if (meshData != null) meshData.close();  } catch (Throwable throwable) { if (meshData != null)
/* 126 */           try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (byteBufferBuilder != null) byteBufferBuilder.close();  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 130 */      } public void clearChunkCache() { this.serverChunk = null;
/* 131 */     this.clientChunk = null; }
/*     */   
/*     */   public void render(GuiGraphics graphics) {
/*     */     ChunkPos chunkPos;
/* 135 */     Options options = this.minecraft.options;
/* 136 */     if (!this.minecraft.isGameLoadFinished() || (options.hideGui && this.minecraft.screen == null)) {
/*     */       return;
/*     */     }
/* 139 */     Collection<Identifier> visibleEntries = this.minecraft.debugEntries.getCurrentlyEnabled();
/* 140 */     if (visibleEntries.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     graphics.nextStratum();
/* 145 */     ProfilerFiller profiler = Profiler.get();
/* 146 */     profiler.push("debug");
/*     */ 
/*     */     
/* 149 */     if (this.minecraft.getCameraEntity() != null && this.minecraft.level != null) {
/* 150 */       BlockPos feetPos = this.minecraft.getCameraEntity().blockPosition();
/* 151 */       chunkPos = new ChunkPos(feetPos);
/*     */     } else {
/* 153 */       chunkPos = null;
/*     */     } 
/* 155 */     if (!Objects.equals(this.lastPos, chunkPos)) {
/* 156 */       this.lastPos = chunkPos;
/* 157 */       clearChunkCache();
/*     */     } 
/*     */     
/* 160 */     final List<String> leftLines = new ArrayList<>();
/* 161 */     final List<String> rightLines = new ArrayList<>();
/* 162 */     final Map<Identifier, Collection<String>> groups = new LinkedHashMap<>();
/* 163 */     final List<String> regularLines = new ArrayList<>();
/* 164 */     DebugScreenDisplayer displayer = new DebugScreenDisplayer(this)
/*     */       {
/*     */         public void addPriorityLine(String line) {
/* 167 */           if (leftLines.size() > rightLines.size()) {
/* 168 */             rightLines.add(line);
/*     */           } else {
/* 170 */             leftLines.add(line);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void addLine(String line) {
/* 176 */           regularLines.add(line);
/*     */         }
/*     */ 
/*     */         
/*     */         public void addToGroup(Identifier group, Collection<String> lines) {
/* 181 */           ((Collection<String>)groups.computeIfAbsent(group, k -> new ArrayList())).addAll(lines);
/*     */         }
/*     */ 
/*     */         
/*     */         public void addToGroup(Identifier group, String lines) {
/* 186 */           ((Collection<String>)groups.computeIfAbsent(group, k -> new ArrayList())).add(lines);
/*     */         }
/*     */       };
/*     */     
/* 190 */     Level level = getLevel();
/* 191 */     for (Identifier id : visibleEntries) {
/* 192 */       DebugScreenEntry entry = DebugScreenEntries.getEntry(id);
/* 193 */       if (entry != null) {
/* 194 */         entry.display(displayer, level, getClientChunk(), getServerChunk());
/*     */       }
/*     */     } 
/*     */     
/* 198 */     if (!leftLines.isEmpty()) {
/* 199 */       leftLines.add("");
/*     */     }
/* 201 */     if (!rightLines.isEmpty()) {
/* 202 */       rightLines.add("");
/*     */     }
/*     */     
/* 205 */     if (!regularLines.isEmpty()) {
/* 206 */       int mid = (regularLines.size() + 1) / 2;
/* 207 */       leftLines.addAll(regularLines.subList(0, mid));
/* 208 */       rightLines.addAll(regularLines.subList(mid, regularLines.size()));
/*     */       
/* 210 */       leftLines.add("");
/* 211 */       if (mid < regularLines.size()) {
/* 212 */         rightLines.add("");
/*     */       }
/*     */     } 
/*     */     
/* 216 */     List<Collection<String>> finalGroups = new ArrayList<>(groups.values());
/* 217 */     if (!finalGroups.isEmpty()) {
/* 218 */       int mid = (finalGroups.size() + 1) / 2;
/* 219 */       for (int i = 0; i < finalGroups.size(); i++) {
/* 220 */         Collection<String> lines = finalGroups.get(i);
/* 221 */         if (!lines.isEmpty()) {
/* 222 */           if (i < mid) {
/* 223 */             leftLines.addAll(lines);
/* 224 */             leftLines.add("");
/*     */           } else {
/* 226 */             rightLines.addAll(lines);
/* 227 */             rightLines.add("");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 233 */     if (this.minecraft.debugEntries.isOverlayVisible()) {
/* 234 */       leftLines.add("");
/* 235 */       boolean hasServer = (this.minecraft.getSingleplayerServer() != null);
/* 236 */       KeyMapping keyDebugModifier = options.keyDebugModifier;
/* 237 */       String modifierBind = keyDebugModifier.getTranslatedKeyMessage().getString();
/* 238 */       String bindOutputPrefix = "[" + (keyDebugModifier.isUnbound() ? "" : (modifierBind + "+"));
/* 239 */       String profilerBind = bindOutputPrefix + bindOutputPrefix + "]";
/* 240 */       String fpsBind = bindOutputPrefix + bindOutputPrefix + "]";
/* 241 */       String networkBind = bindOutputPrefix + bindOutputPrefix + "]";
/*     */       
/* 243 */       leftLines.add("Debug charts: " + profilerBind + " Profiler " + (
/* 244 */           this.renderProfilerChart ? "visible" : "hidden") + "; " + fpsBind + " " + (
/* 245 */           hasServer ? "FPS + TPS " : "FPS ") + (this.renderFpsCharts ? "visible" : "hidden") + "; " + networkBind + " " + (
/* 246 */           !this.minecraft.isLocalServer() ? "Bandwidth + Ping" : "Ping") + (this.renderNetworkCharts ? " visible" : " hidden"));
/*     */       
/* 248 */       String optionsBind = bindOutputPrefix + bindOutputPrefix + "]";
/* 249 */       leftLines.add("To edit: press " + optionsBind);
/*     */     } 
/*     */     
/* 252 */     renderLines(graphics, leftLines, true);
/* 253 */     renderLines(graphics, rightLines, false);
/*     */     
/* 255 */     graphics.nextStratum();
/* 256 */     this.profilerPieChart.setBottomOffset(10);
/* 257 */     if (showFpsCharts()) {
/* 258 */       int scaledWidth = graphics.guiWidth();
/* 259 */       int maxWidth = scaledWidth / 2;
/* 260 */       this.fpsChart.drawChart(graphics, 0, this.fpsChart.getWidth(maxWidth));
/* 261 */       if (this.tickTimeLogger.size() > 0) {
/* 262 */         int width = this.tpsChart.getWidth(maxWidth);
/* 263 */         this.tpsChart.drawChart(graphics, scaledWidth - width, width);
/*     */       } 
/* 265 */       this.profilerPieChart.setBottomOffset(this.tpsChart.getFullHeight());
/*     */     } 
/* 267 */     if (showNetworkCharts() && this.minecraft.getConnection() != null) {
/* 268 */       int scaledWidth = graphics.guiWidth();
/* 269 */       int maxWidth = scaledWidth / 2;
/* 270 */       if (!this.minecraft.isLocalServer()) {
/* 271 */         this.bandwidthChart.drawChart(graphics, 0, this.bandwidthChart.getWidth(maxWidth));
/*     */       }
/* 273 */       int width = this.pingChart.getWidth(maxWidth);
/* 274 */       this.pingChart.drawChart(graphics, scaledWidth - width, width);
/* 275 */       this.profilerPieChart.setBottomOffset(this.pingChart.getFullHeight());
/*     */     } 
/*     */     
/* 278 */     if (this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_CHUNKS_ON_SERVER)) {
/* 279 */       IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/* 280 */       if (singleplayerServer != null && this.minecraft.player != null) {
/* 281 */         ChunkLoadStatusView statusView = singleplayerServer.createChunkLoadStatusView(16 + ChunkLevel.RADIUS_AROUND_FULL_CHUNK);
/* 282 */         statusView.moveTo(this.minecraft.player.level().dimension(), this.minecraft.player.chunkPosition());
/* 283 */         LevelLoadingScreen.renderChunks(graphics, graphics.guiWidth() / 2, graphics.guiHeight() / 2, 4, 1, statusView);
/*     */       } 
/*     */     } 
/*     */     
/* 287 */     Zone ignored = profiler.zone("profilerPie"); 
/* 288 */     try { this.profilerPieChart.render(graphics);
/* 289 */       if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*     */         try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 291 */      profiler.pop();
/*     */   }
/*     */   
/*     */   private void renderLines(GuiGraphics graphics, List<String> lines, boolean alignLeft) {
/* 295 */     Objects.requireNonNull(this.font); int height = 9;
/* 296 */     for (int i = 0; i < lines.size(); i++) {
/* 297 */       String line = lines.get(i);
/* 298 */       if (!Strings.isNullOrEmpty(line)) {
/* 299 */         int width = this.font.width(line);
/* 300 */         int left = alignLeft ? 2 : (graphics.guiWidth() - 2 - width);
/* 301 */         int top = 2 + height * i;
/* 302 */         graphics.fill(left - 1, top - 1, left + width + 1, top + height - 1, -1873784752);
/*     */       } 
/*     */     } 
/* 305 */     for (int j = 0; j < lines.size(); j++) {
/* 306 */       String line = lines.get(j);
/* 307 */       if (!Strings.isNullOrEmpty(line)) {
/* 308 */         int width = this.font.width(line);
/* 309 */         int left = alignLeft ? 2 : (graphics.guiWidth() - 2 - width);
/* 310 */         int top = 2 + height * j;
/* 311 */         graphics.drawString(this.font, line, left, top, -2039584, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ServerLevel getServerLevel() {
/* 317 */     if (this.minecraft.level == null) {
/* 318 */       return null;
/*     */     }
/* 320 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/* 321 */     if (server != null) {
/* 322 */       return server.getLevel(this.minecraft.level.dimension());
/*     */     }
/*     */     
/* 325 */     return null;
/*     */   }
/*     */   
/*     */   private Level getLevel() {
/* 329 */     if (this.minecraft.level == null) {
/* 330 */       return null;
/*     */     }
/* 332 */     return (Level)DataFixUtils.orElse(
/* 333 */         Optional.<IntegratedServer>ofNullable(this.minecraft.getSingleplayerServer()).flatMap(s -> Optional.ofNullable(s.getLevel(this.minecraft.level.dimension()))), this.minecraft.level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelChunk getServerChunk() {
/* 339 */     if (this.minecraft.level == null || this.lastPos == null) {
/* 340 */       return null;
/*     */     }
/* 342 */     if (this.serverChunk == null) {
/* 343 */       ServerLevel level = getServerLevel();
/* 344 */       if (level == null) {
/* 345 */         return null;
/*     */       }
/* 347 */       this.serverChunk = level.getChunkSource().getChunkFuture(this.lastPos.x, this.lastPos.z, ChunkStatus.FULL, false).thenApply(chunkResult -> (LevelChunk)chunkResult.orElse(null));
/*     */     } 
/* 349 */     return this.serverChunk.getNow(null);
/*     */   }
/*     */   
/*     */   private LevelChunk getClientChunk() {
/* 353 */     if (this.minecraft.level == null || this.lastPos == null) {
/* 354 */       return null;
/*     */     }
/* 356 */     if (this.clientChunk == null) {
/* 357 */       this.clientChunk = this.minecraft.level.getChunk(this.lastPos.x, this.lastPos.z);
/*     */     }
/* 359 */     return this.clientChunk;
/*     */   }
/*     */   
/*     */   public boolean showDebugScreen() {
/* 363 */     DebugScreenEntryList entries = this.minecraft.debugEntries;
/* 364 */     return ((entries.isOverlayVisible() || !entries.getCurrentlyEnabled().isEmpty()) && (!this.minecraft.options.hideGui || this.minecraft.screen != null));
/*     */   }
/*     */   
/*     */   public boolean showProfilerChart() {
/* 368 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderProfilerChart);
/*     */   }
/*     */   
/*     */   public boolean showNetworkCharts() {
/* 372 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderNetworkCharts);
/*     */   }
/*     */   
/*     */   public boolean showFpsCharts() {
/* 376 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderFpsCharts);
/*     */   }
/*     */   
/*     */   public void toggleNetworkCharts() {
/* 380 */     this.renderNetworkCharts = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderNetworkCharts);
/* 381 */     if (this.renderNetworkCharts) {
/* 382 */       this.minecraft.debugEntries.setOverlayVisible(true);
/* 383 */       this.renderFpsCharts = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleFpsCharts() {
/* 388 */     this.renderFpsCharts = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderFpsCharts);
/* 389 */     if (this.renderFpsCharts) {
/* 390 */       this.minecraft.debugEntries.setOverlayVisible(true);
/* 391 */       this.renderNetworkCharts = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleProfilerChart() {
/* 396 */     this.renderProfilerChart = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderProfilerChart);
/* 397 */     if (this.renderProfilerChart) {
/* 398 */       this.minecraft.debugEntries.setOverlayVisible(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void logFrameDuration(long frameDuration) {
/* 403 */     this.frameTimeLogger.logSample(frameDuration);
/*     */   }
/*     */   
/*     */   public LocalSampleLogger getTickTimeLogger() {
/* 407 */     return this.tickTimeLogger;
/*     */   }
/*     */   
/*     */   public LocalSampleLogger getPingLogger() {
/* 411 */     return this.pingLogger;
/*     */   }
/*     */   
/*     */   public LocalSampleLogger getBandwidthLogger() {
/* 415 */     return this.bandwidthLogger;
/*     */   }
/*     */   
/*     */   public ProfilerPieChart getProfilerPieChart() {
/* 419 */     return this.profilerPieChart;
/*     */   }
/*     */   
/*     */   public void logRemoteSample(long[] sample, RemoteDebugSampleType type) {
/* 423 */     LocalSampleLogger logger = this.remoteSupportingLoggers.get(type);
/* 424 */     if (logger != null) {
/* 425 */       logger.logFullSample(sample);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/* 430 */     this.tickTimeLogger.reset();
/* 431 */     this.pingLogger.reset();
/* 432 */     this.bandwidthLogger.reset();
/*     */   }
/*     */   
/*     */   public void render3dCrosshair(Camera camera) {
/* 436 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 437 */     modelViewStack.pushMatrix();
/* 438 */     modelViewStack.translate(0.0F, 0.0F, -1.0F);
/* 439 */     modelViewStack.rotateX(camera.xRot() * 0.017453292F);
/* 440 */     modelViewStack.rotateY(camera.yRot() * 0.017453292F);
/* 441 */     float crosshairScale = 0.01F * this.minecraft.getWindow().getGuiScale();
/* 442 */     modelViewStack.scale(-crosshairScale, crosshairScale, -crosshairScale);
/*     */     
/* 444 */     RenderPipeline renderPipeline = RenderPipelines.LINES;
/*     */     
/* 446 */     RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
/* 447 */     GpuTextureView colorTexture = mainRenderTarget.getColorTextureView();
/* 448 */     GpuTextureView depthTexture = mainRenderTarget.getDepthTextureView();
/* 449 */     GpuBuffer indexBuffer = this.crosshairIndicies.getBuffer(36);
/* 450 */     GpuBufferSlice dynamicTransform = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)modelViewStack, (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */     
/* 452 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "3d crosshair", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 453 */     try { renderPass.setPipeline(renderPipeline);
/* 454 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 455 */       renderPass.setVertexBuffer(0, this.crosshairBuffer);
/* 456 */       renderPass.setIndexBuffer(indexBuffer, this.crosshairIndicies.type());
/*     */       
/* 458 */       renderPass.setUniform("DynamicTransforms", dynamicTransform);
/* 459 */       renderPass.drawIndexed(0, 0, 36, 1);
/* 460 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/* 461 */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  modelViewStack.popMatrix();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/DebugScreenOverlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */