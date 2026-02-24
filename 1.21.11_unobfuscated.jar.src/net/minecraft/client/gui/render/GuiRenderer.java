/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
/*     */ import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
/*     */ import net.minecraft.client.gui.render.state.BlitRenderState;
/*     */ import net.minecraft.client.gui.render.state.GlyphRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiElementRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiItemRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiTextRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.OversizedItemRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*     */ import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
/*     */ import net.minecraft.client.renderer.MappableRingBuffer;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GuiRenderer implements AutoCloseable {
/*  76 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final float MAX_GUI_Z = 10000.0F;
/*     */   
/*     */   public static final float MIN_GUI_Z = 0.0F;
/*     */   
/*     */   private static final float GUI_Z_NEAR = 1000.0F;
/*     */   public static final int GUI_3D_Z_FAR = 1000;
/*     */   public static final int GUI_3D_Z_NEAR = -1000;
/*     */   public static final int DEFAULT_ITEM_SIZE = 16;
/*     */   private static final int MINIMUM_ITEM_ATLAS_SIZE = 512;
/*  87 */   private static final int MAXIMUM_ITEM_ATLAS_SIZE = RenderSystem.getDevice().getMaxTextureSize();
/*     */   
/*     */   public static final int CLEAR_COLOR = 0;
/*  90 */   private static final Comparator<ScreenRectangle> SCISSOR_COMPARATOR = Comparator.nullsFirst(
/*  91 */       Comparator.comparing(ScreenRectangle::top)
/*  92 */       .thenComparing(ScreenRectangle::bottom)
/*  93 */       .thenComparing(ScreenRectangle::left)
/*  94 */       .thenComparing(ScreenRectangle::right));
/*     */ 
/*     */   
/*  97 */   private static final Comparator<TextureSetup> TEXTURE_COMPARATOR = Comparator.nullsFirst(
/*  98 */       Comparator.comparing(TextureSetup::getSortKey));
/*     */ 
/*     */ 
/*     */   
/* 102 */   private static final Comparator<GuiElementRenderState> ELEMENT_SORT_COMPARATOR = Comparator.comparing(GuiElementRenderState::scissorArea, SCISSOR_COMPARATOR)
/* 103 */     .thenComparing(GuiElementRenderState::pipeline, Comparator.comparing(RenderPipeline::getSortKey))
/* 104 */     .thenComparing(GuiElementRenderState::textureSetup, TEXTURE_COMPARATOR);
/*     */   
/* 106 */   private final Map<Object, AtlasPosition> atlasPositions = (Map<Object, AtlasPosition>)new Object2ObjectOpenHashMap();
/* 107 */   private final Map<Object, OversizedItemRenderer> oversizedItemRenderers = (Map<Object, OversizedItemRenderer>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final GuiRenderState renderState;
/*     */   
/* 111 */   private final List<Draw> draws = new ArrayList<>();
/* 112 */   private final List<MeshToDraw> meshesToDraw = new ArrayList<>();
/* 113 */   private final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(786432);
/* 114 */   private final Map<VertexFormat, MappableRingBuffer> vertexBuffers = (Map<VertexFormat, MappableRingBuffer>)new Object2ObjectOpenHashMap();
/* 115 */   private int firstDrawIndexAfterBlur = Integer.MAX_VALUE;
/*     */   
/* 117 */   private final CachedOrthoProjectionMatrixBuffer guiProjectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("gui", 1000.0F, 11000.0F, true);
/* 118 */   private final CachedOrthoProjectionMatrixBuffer itemsProjectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("items", -1000.0F, 1000.0F, true); private final MultiBufferSource.BufferSource bufferSource; private final SubmitNodeCollector submitNodeCollector; private final FeatureRenderDispatcher featureRenderDispatcher; private final Map<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>> pictureInPictureRenderers; private GpuTexture itemsAtlas; private GpuTextureView itemsAtlasView; private GpuTexture itemsAtlasDepth; private GpuTextureView itemsAtlasDepthView; private int itemAtlasX;
/*     */   private int itemAtlasY;
/*     */   private int cachedGuiScale;
/*     */   private int frameNumber;
/*     */   private ScreenRectangle previousScissorArea;
/*     */   private RenderPipeline previousPipeline;
/*     */   private TextureSetup previousTextureSetup;
/*     */   private BufferBuilder bufferBuilder;
/*     */   
/*     */   private static final class Draw extends Record { private final GpuBuffer vertexBuffer;
/*     */     private final int baseVertex;
/*     */     private final VertexFormat.Mode mode;
/*     */     private final int indexCount;
/*     */     private final RenderPipeline pipeline;
/*     */     private final TextureSetup textureSetup;
/*     */     private final ScreenRectangle scissorArea;
/*     */     
/* 135 */     private Draw(GpuBuffer vertexBuffer, int baseVertex, VertexFormat.Mode mode, int indexCount, RenderPipeline pipeline, TextureSetup textureSetup, ScreenRectangle scissorArea) { this.vertexBuffer = vertexBuffer; this.baseVertex = baseVertex; this.mode = mode; this.indexCount = indexCount; this.pipeline = pipeline; this.textureSetup = textureSetup; this.scissorArea = scissorArea; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #135	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 135 */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw; } public GpuBuffer vertexBuffer() { return this.vertexBuffer; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #135	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #135	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/* 135 */       //   0	8	1	o	Ljava/lang/Object; } public int baseVertex() { return this.baseVertex; } public VertexFormat.Mode mode() { return this.mode; } public int indexCount() { return this.indexCount; } public RenderPipeline pipeline() { return this.pipeline; } public TextureSetup textureSetup() { return this.textureSetup; } public ScreenRectangle scissorArea() { return this.scissorArea; }
/*     */      }
/*     */   private static final class MeshToDraw extends Record implements AutoCloseable { private final MeshData mesh; private final RenderPipeline pipeline; private final TextureSetup textureSetup; private final ScreenRectangle scissorArea;
/*     */     
/* 139 */     private MeshToDraw(MeshData mesh, RenderPipeline pipeline, TextureSetup textureSetup, ScreenRectangle scissorArea) { this.mesh = mesh; this.pipeline = pipeline; this.textureSetup = textureSetup; this.scissorArea = scissorArea; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/GuiRenderer$MeshToDraw;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$MeshToDraw; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/GuiRenderer$MeshToDraw;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$MeshToDraw; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/GuiRenderer$MeshToDraw;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$MeshToDraw;
/* 139 */       //   0	8	1	o	Ljava/lang/Object; } public MeshData mesh() { return this.mesh; } public RenderPipeline pipeline() { return this.pipeline; } public TextureSetup textureSetup() { return this.textureSetup; } public ScreenRectangle scissorArea() { return this.scissorArea; }
/*     */     
/*     */     public void close() {
/* 142 */       this.mesh.close();
/*     */     } }
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
/*     */   public void incrementFrameNumber() {
/* 159 */     this.frameNumber++;
/*     */   }
/*     */   
/* 162 */   public GuiRenderer(GuiRenderState renderState, MultiBufferSource.BufferSource bufferSource, SubmitNodeCollector submitNodeCollector, FeatureRenderDispatcher featureRenderDispatcher, List<PictureInPictureRenderer<?>> pictureInPictureRenderers) { this.previousScissorArea = null;
/* 163 */     this.previousPipeline = null;
/* 164 */     this.previousTextureSetup = null;
/* 165 */     this.bufferBuilder = null; this.renderState = renderState; this.bufferSource = bufferSource; this.submitNodeCollector = submitNodeCollector; this.featureRenderDispatcher = featureRenderDispatcher; ImmutableMap.Builder<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>> builder = ImmutableMap.builder();
/*     */     for (PictureInPictureRenderer<?> pictureInPictureRenderer : pictureInPictureRenderers)
/*     */       builder.put(pictureInPictureRenderer.getRenderStateClass(), pictureInPictureRenderer); 
/* 168 */     this.pictureInPictureRenderers = (Map<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>>)builder.buildOrThrow(); } public void render(GpuBufferSlice fogBuffer) { prepare();
/*     */     
/* 170 */     draw(fogBuffer);
/*     */     
/* 172 */     for (MappableRingBuffer buffer : this.vertexBuffers.values()) {
/* 173 */       buffer.rotate();
/*     */     }
/* 175 */     this.draws.clear();
/* 176 */     this.meshesToDraw.clear();
/* 177 */     this.renderState.reset();
/* 178 */     this.firstDrawIndexAfterBlur = Integer.MAX_VALUE;
/* 179 */     clearUnusedOversizedItemRenderers();
/* 180 */     if (SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER) {
/* 181 */       RenderPipeline.updateSortKeySeed();
/* 182 */       TextureSetup.updateSortKeySeed();
/*     */     }  }
/*     */ 
/*     */   
/*     */   private void clearUnusedOversizedItemRenderers() {
/* 187 */     Iterator<Map.Entry<Object, OversizedItemRenderer>> oversizedItemRendererIterator = this.oversizedItemRenderers.entrySet().iterator();
/* 188 */     while (oversizedItemRendererIterator.hasNext()) {
/* 189 */       Map.Entry<Object, OversizedItemRenderer> next = oversizedItemRendererIterator.next();
/* 190 */       OversizedItemRenderer renderer = next.getValue();
/* 191 */       if (!renderer.usedOnThisFrame()) {
/* 192 */         renderer.close();
/* 193 */         oversizedItemRendererIterator.remove(); continue;
/*     */       } 
/* 195 */       renderer.resetUsedOnThisFrame();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void prepare() {
/* 201 */     this.bufferSource.endBatch();
/*     */     
/* 203 */     preparePictureInPicture();
/* 204 */     prepareItemElements();
/* 205 */     prepareText();
/*     */     
/* 207 */     this.renderState.sortElements(ELEMENT_SORT_COMPARATOR);
/*     */     
/* 209 */     addElementsToMeshes(GuiRenderState.TraverseRange.BEFORE_BLUR);
/*     */     
/* 211 */     this.firstDrawIndexAfterBlur = this.meshesToDraw.size();
/*     */     
/* 213 */     addElementsToMeshes(GuiRenderState.TraverseRange.AFTER_BLUR);
/*     */     
/* 215 */     recordDraws();
/*     */   }
/*     */   
/*     */   private void addElementsToMeshes(GuiRenderState.TraverseRange range) {
/* 219 */     this.previousScissorArea = null;
/* 220 */     this.previousPipeline = null;
/* 221 */     this.previousTextureSetup = null;
/* 222 */     this.bufferBuilder = null;
/*     */     
/* 224 */     this.renderState.forEachElement(this::addElementToMesh, range);
/*     */     
/* 226 */     if (this.bufferBuilder != null) {
/* 227 */       recordMesh(this.bufferBuilder, this.previousPipeline, this.previousTextureSetup, this.previousScissorArea);
/*     */     }
/*     */   }
/*     */   
/*     */   private void draw(GpuBufferSlice fogBuffer) {
/* 232 */     if (this.draws.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 236 */     Minecraft minecraft = Minecraft.getInstance();
/*     */     
/* 238 */     Window window = minecraft.getWindow();
/*     */     
/* 240 */     RenderSystem.setProjectionMatrix(this.guiProjectionMatrixBuffer.getBuffer(window.getWidth() / window.getGuiScale(), window.getHeight() / window.getGuiScale()), ProjectionType.ORTHOGRAPHIC);
/*     */     
/* 242 */     RenderTarget mainRenderTarget = minecraft.getMainRenderTarget();
/* 243 */     int maxIndexCount = 0;
/* 244 */     for (Draw draw : this.draws) {
/* 245 */       if (draw.indexCount > maxIndexCount) {
/* 246 */         maxIndexCount = draw.indexCount;
/*     */       }
/*     */     } 
/* 249 */     RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/* 250 */     GpuBuffer indexBuffer = autoIndices.getBuffer(maxIndexCount);
/* 251 */     VertexFormat.IndexType indexType = autoIndices.type();
/* 252 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F), (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/* 253 */     if (this.firstDrawIndexAfterBlur > 0) {
/* 254 */       executeDrawRange(() -> "GUI before blur", mainRenderTarget, fogBuffer, dynamicTransforms, indexBuffer, indexType, 0, Math.min(this.firstDrawIndexAfterBlur, this.draws.size()));
/*     */     }
/*     */     
/* 257 */     if (this.draws.size() <= this.firstDrawIndexAfterBlur) {
/*     */       return;
/*     */     }
/*     */     
/* 261 */     RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mainRenderTarget.getDepthTexture(), 1.0D);
/* 262 */     minecraft.gameRenderer.processBlurEffect();
/*     */     
/* 264 */     executeDrawRange(() -> "GUI after blur", mainRenderTarget, fogBuffer, dynamicTransforms, indexBuffer, indexType, this.firstDrawIndexAfterBlur, this.draws.size());
/*     */   }
/*     */   
/*     */   private void executeDrawRange(Supplier<String> label, RenderTarget mainRenderTarget, GpuBufferSlice fogBuffer, GpuBufferSlice dynamicTransforms, GpuBuffer indexBuffer, VertexFormat.IndexType indexType, int startIndex, int endIndex) {
/* 268 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(label, mainRenderTarget.getColorTextureView(), OptionalInt.empty(), mainRenderTarget.useDepth ? mainRenderTarget.getDepthTextureView() : null, OptionalDouble.empty()); 
/* 269 */     try { RenderSystem.bindDefaultUniforms(renderPass);
/* 270 */       renderPass.setUniform("Fog", fogBuffer);
/* 271 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 272 */       for (int i = startIndex; i < endIndex; i++) {
/* 273 */         Draw draw = this.draws.get(i);
/* 274 */         executeDraw(draw, renderPass, indexBuffer, indexType);
/*     */       } 
/* 276 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 280 */      } private void addElementToMesh(GuiElementRenderState elementState) { RenderPipeline pipeline = elementState.pipeline();
/* 281 */     TextureSetup textureSetup = elementState.textureSetup();
/* 282 */     ScreenRectangle scissorArea = elementState.scissorArea();
/* 283 */     if (pipeline != this.previousPipeline || scissorChanged(scissorArea, this.previousScissorArea) || !textureSetup.equals(this.previousTextureSetup)) {
/* 284 */       if (this.bufferBuilder != null) {
/* 285 */         recordMesh(this.bufferBuilder, this.previousPipeline, this.previousTextureSetup, this.previousScissorArea);
/*     */       }
/* 287 */       this.bufferBuilder = getBufferBuilder(pipeline);
/* 288 */       this.previousPipeline = pipeline;
/* 289 */       this.previousTextureSetup = textureSetup;
/* 290 */       this.previousScissorArea = scissorArea;
/*     */     } 
/* 292 */     elementState.buildVertices((VertexConsumer)this.bufferBuilder); }
/*     */ 
/*     */   
/*     */   private void prepareText() {
/* 296 */     this.renderState.forEachText(text -> {
/*     */           final Matrix3x2fc pose = text.pose;
/*     */           final ScreenRectangle scissor = text.scissor;
/*     */           text.ensurePrepared().visit(new Font.GlyphVisitor()
/*     */               {
/*     */                 public void acceptGlyph(TextRenderable.Styled glyph) {
/* 302 */                   accept((TextRenderable)glyph);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void acceptEffect(TextRenderable effect) {
/* 307 */                   accept(effect);
/*     */                 }
/*     */                 
/*     */                 private void accept(TextRenderable glyph) {
/* 311 */                   GuiRenderer.this.renderState.submitGlyphToCurrentLayer((GuiElementRenderState)new GlyphRenderState(pose, glyph, scissor));
/*     */                 }
/*     */               });
/*     */         });
/*     */   }
/*     */   
/*     */   private void prepareItemElements() {
/* 318 */     if (this.renderState.getItemModelIdentities().isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 322 */     int guiScale = getGuiScaleInvalidatingItemAtlasIfChanged();
/* 323 */     int singleItemTextureSize = 16 * guiScale;
/* 324 */     int atlasSizeInPixels = calculateAtlasSizeInPixels(singleItemTextureSize);
/* 325 */     if (this.itemsAtlas == null) {
/* 326 */       createAtlasTextures(atlasSizeInPixels);
/*     */     }
/*     */     
/* 329 */     RenderSystem.outputColorTextureOverride = this.itemsAtlasView;
/* 330 */     RenderSystem.outputDepthTextureOverride = this.itemsAtlasDepthView;
/*     */     
/* 332 */     RenderSystem.setProjectionMatrix(this.itemsProjectionMatrixBuffer.getBuffer(atlasSizeInPixels, atlasSizeInPixels), ProjectionType.ORTHOGRAPHIC);
/* 333 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
/* 334 */     PoseStack poseStack = new PoseStack();
/* 335 */     MutableBoolean alreadyWarned = new MutableBoolean(false);
/* 336 */     MutableBoolean hasOversizedItems = new MutableBoolean(false);
/* 337 */     this.renderState.forEachItem(itemState -> {
/*     */           if (hasOversizedItems.oversizedItemBounds() != null) {
/*     */             hasOversizedItems.setTrue();
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*     */           TrackingItemStackRenderState itemStackRenderState = hasOversizedItems.itemStackRenderState();
/*     */           
/*     */           AtlasPosition atlasPosition = this.atlasPositions.get(itemStackRenderState.getModelIdentity());
/*     */           if (atlasPosition != null && (!itemStackRenderState.isAnimated() || atlasPosition.lastAnimatedOnFrame == this.frameNumber)) {
/*     */             submitBlitFromItemAtlas(hasOversizedItems, atlasPosition.u, atlasPosition.v, hasOversizedItems, hasOversizedItems);
/*     */             return;
/*     */           } 
/*     */           if (this.itemAtlasX + hasOversizedItems > hasOversizedItems) {
/*     */             this.itemAtlasX = 0;
/*     */             this.itemAtlasY += hasOversizedItems;
/*     */           } 
/* 355 */           boolean reDrawingAnimated = (itemStackRenderState.isAnimated() && atlasPosition != null);
/*     */           
/*     */           if (!reDrawingAnimated && this.itemAtlasY + hasOversizedItems > hasOversizedItems) {
/*     */             if (singleItemTextureSize.isFalse()) {
/*     */               LOGGER.warn("Trying to render too many items in GUI at the same time. Skipping some of them.");
/*     */               
/*     */               singleItemTextureSize.setTrue();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*     */           int renderX = reDrawingAnimated ? atlasPosition.x : this.itemAtlasX, renderY = reDrawingAnimated ? atlasPosition.y : this.itemAtlasY;
/*     */           
/*     */           if (reDrawingAnimated) {
/*     */             RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.itemsAtlas, 0, this.itemsAtlasDepth, 1.0D, renderX, hasOversizedItems - renderY - hasOversizedItems, hasOversizedItems, hasOversizedItems);
/*     */           }
/*     */           
/*     */           renderItemToAtlas(itemStackRenderState, alreadyWarned, renderX, renderY, hasOversizedItems);
/*     */           
/*     */           float u0 = renderX / hasOversizedItems, v0 = (hasOversizedItems - renderY) / hasOversizedItems;
/*     */           
/*     */           submitBlitFromItemAtlas(hasOversizedItems, u0, v0, hasOversizedItems, hasOversizedItems);
/*     */           if (reDrawingAnimated) {
/*     */             atlasPosition.lastAnimatedOnFrame = this.frameNumber;
/*     */           } else {
/*     */             this.atlasPositions.put(hasOversizedItems.itemStackRenderState().getModelIdentity(), new AtlasPosition(this.itemAtlasX, this.itemAtlasY, u0, v0, this.frameNumber));
/*     */             this.itemAtlasX += hasOversizedItems;
/*     */           } 
/*     */         });
/* 385 */     RenderSystem.outputColorTextureOverride = null;
/* 386 */     RenderSystem.outputDepthTextureOverride = null;
/*     */     
/* 388 */     if (hasOversizedItems.booleanValue()) {
/* 389 */       this.renderState.forEachItem(itemState -> {
/*     */             if (guiScale.oversizedItemBounds() != null) {
/*     */               TrackingItemStackRenderState itemStackRenderState = guiScale.itemStackRenderState();
/*     */               OversizedItemRenderer oversizedItemRenderer = this.oversizedItemRenderers.computeIfAbsent(itemStackRenderState.getModelIdentity(), ());
/*     */               ScreenRectangle actualItemBounds = guiScale.oversizedItemBounds();
/*     */               OversizedItemRenderState oversizedItemRenderState = new OversizedItemRenderState(guiScale, actualItemBounds.left(), actualItemBounds.top(), actualItemBounds.right(), actualItemBounds.bottom());
/*     */               oversizedItemRenderer.prepare((PictureInPictureRenderState)oversizedItemRenderState, this.renderState, guiScale);
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void preparePictureInPicture() {
/* 403 */     int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
/* 404 */     this.renderState.forEachPictureInPicture(pictureInPictureState -> preparePictureInPictureState(guiScale, guiScale));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends PictureInPictureRenderState> void preparePictureInPictureState(T picturesInPictureState, int guiScale) {
/* 410 */     PictureInPictureRenderer<T> renderer = (PictureInPictureRenderer<T>)this.pictureInPictureRenderers.get(picturesInPictureState.getClass());
/* 411 */     if (renderer != null) {
/* 412 */       renderer.prepare((PictureInPictureRenderState)picturesInPictureState, this.renderState, guiScale);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderItemToAtlas(TrackingItemStackRenderState itemStackRenderState, PoseStack poseStack, int renderX, int renderY, int singleItemTextureSize) {
/* 417 */     poseStack.pushPose();
/* 418 */     poseStack.translate(renderX + singleItemTextureSize / 2.0F, renderY + singleItemTextureSize / 2.0F, 0.0F);
/* 419 */     poseStack.scale(singleItemTextureSize, -singleItemTextureSize, singleItemTextureSize);
/*     */     
/* 421 */     boolean flat = !itemStackRenderState.usesBlockLight();
/* 422 */     if (flat) {
/* 423 */       (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
/*     */     } else {
/* 425 */       (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
/*     */     } 
/* 427 */     RenderSystem.enableScissorForRenderTypeDraws(renderX, this.itemsAtlas.getHeight(0) - renderY - singleItemTextureSize, singleItemTextureSize, singleItemTextureSize);
/* 428 */     itemStackRenderState.submit(poseStack, this.submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
/* 429 */     this.featureRenderDispatcher.renderAllFeatures();
/* 430 */     this.bufferSource.endBatch();
/* 431 */     RenderSystem.disableScissorForRenderTypeDraws();
/* 432 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void submitBlitFromItemAtlas(GuiItemRenderState itemState, float u0, float v0, int singleItemTextureSize, int atlasSizeInPixels) {
/* 436 */     float u1 = u0 + singleItemTextureSize / atlasSizeInPixels;
/* 437 */     float v1 = v0 + -singleItemTextureSize / atlasSizeInPixels;
/* 438 */     this.renderState.submitBlitToCurrentLayer(new BlitRenderState(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, 
/*     */           
/* 440 */           TextureSetup.singleTexture(this.itemsAtlasView, RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)), 
/* 441 */           itemState.pose(), 
/* 442 */           itemState.x(), itemState.y(), itemState.x() + 16, itemState.y() + 16, u0, u1, v0, v1, -1, 
/*     */ 
/*     */ 
/*     */           
/* 446 */           itemState.scissorArea(), null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createAtlasTextures(int atlasSizeInPixels) {
/* 452 */     GpuDevice device = RenderSystem.getDevice();
/* 453 */     this.itemsAtlas = device.createTexture("UI items atlas", 12, TextureFormat.RGBA8, atlasSizeInPixels, atlasSizeInPixels, 1, 1);
/* 454 */     this.itemsAtlasView = device.createTextureView(this.itemsAtlas);
/* 455 */     this.itemsAtlasDepth = device.createTexture("UI items atlas depth", 8, TextureFormat.DEPTH32, atlasSizeInPixels, atlasSizeInPixels, 1, 1);
/* 456 */     this.itemsAtlasDepthView = device.createTextureView(this.itemsAtlasDepth);
/* 457 */     device.createCommandEncoder().clearColorAndDepthTextures(this.itemsAtlas, 0, this.itemsAtlasDepth, 1.0D);
/*     */   }
/*     */   private int calculateAtlasSizeInPixels(int singleItemTextureSize) {
/*     */     int itemCount;
/* 461 */     Set<Object> itemStates = this.renderState.getItemModelIdentities();
/*     */     
/* 463 */     if (this.atlasPositions.isEmpty()) {
/* 464 */       itemCount = itemStates.size();
/*     */     } else {
/* 466 */       itemCount = this.atlasPositions.size();
/* 467 */       for (Object itemState : itemStates) {
/* 468 */         if (!this.atlasPositions.containsKey(itemState)) {
/* 469 */           itemCount++;
/*     */         }
/*     */       } 
/*     */     } 
/* 473 */     if (this.itemsAtlas != null) {
/* 474 */       int currentAtlasItemsPerRow = this.itemsAtlas.getWidth(0) / singleItemTextureSize;
/* 475 */       int currentAtlasCapacity = currentAtlasItemsPerRow * currentAtlasItemsPerRow;
/* 476 */       if (itemCount < currentAtlasCapacity) {
/* 477 */         return this.itemsAtlas.getWidth(0);
/*     */       }
/* 479 */       invalidateItemAtlas();
/*     */     } 
/* 481 */     int itemCountOnThisFrame = itemStates.size();
/* 482 */     int atlasSizeInItems = Mth.smallestSquareSide(itemCountOnThisFrame + itemCountOnThisFrame / 2);
/* 483 */     return Math.clamp(Mth.smallestEncompassingPowerOfTwo(atlasSizeInItems * singleItemTextureSize), 512, MAXIMUM_ITEM_ATLAS_SIZE);
/*     */   }
/*     */   
/*     */   private int getGuiScaleInvalidatingItemAtlasIfChanged() {
/* 487 */     int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
/* 488 */     if (guiScale != this.cachedGuiScale) {
/* 489 */       invalidateItemAtlas();
/* 490 */       for (OversizedItemRenderer renderer : this.oversizedItemRenderers.values()) {
/* 491 */         renderer.invalidateTexture();
/*     */       }
/* 493 */       this.cachedGuiScale = guiScale;
/*     */     } 
/* 495 */     return guiScale;
/*     */   }
/*     */   
/*     */   private void invalidateItemAtlas() {
/* 499 */     this.itemAtlasX = 0;
/* 500 */     this.itemAtlasY = 0;
/* 501 */     this.atlasPositions.clear();
/* 502 */     if (this.itemsAtlas != null) {
/* 503 */       this.itemsAtlas.close();
/* 504 */       this.itemsAtlas = null;
/*     */     } 
/* 506 */     if (this.itemsAtlasView != null) {
/* 507 */       this.itemsAtlasView.close();
/* 508 */       this.itemsAtlasView = null;
/*     */     } 
/* 510 */     if (this.itemsAtlasDepth != null) {
/* 511 */       this.itemsAtlasDepth.close();
/* 512 */       this.itemsAtlasDepth = null;
/*     */     } 
/* 514 */     if (this.itemsAtlasDepthView != null) {
/* 515 */       this.itemsAtlasDepthView.close();
/* 516 */       this.itemsAtlasDepthView = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recordMesh(BufferBuilder bufferBuilder, RenderPipeline pipeline, TextureSetup textureSetup, ScreenRectangle scissorArea) {
/* 521 */     MeshData mesh = bufferBuilder.build();
/* 522 */     if (mesh != null) {
/* 523 */       this.meshesToDraw.add(new MeshToDraw(mesh, pipeline, textureSetup, scissorArea));
/*     */     }
/*     */   }
/*     */   
/*     */   private void recordDraws() {
/* 528 */     ensureVertexBufferSizes();
/* 529 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/* 530 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 531 */     for (MeshToDraw meshToDraw : this.meshesToDraw) {
/* 532 */       MeshData mesh = meshToDraw.mesh;
/* 533 */       MeshData.DrawState drawState = mesh.drawState();
/* 534 */       VertexFormat format = drawState.format();
/* 535 */       MappableRingBuffer vertexBuffer = this.vertexBuffers.get(format);
/* 536 */       if (!object2IntOpenHashMap.containsKey(format)) {
/* 537 */         object2IntOpenHashMap.put(format, 0);
/*     */       }
/* 539 */       ByteBuffer meshVertexBuffer = mesh.vertexBuffer();
/* 540 */       int meshBufferSize = meshVertexBuffer.remaining();
/* 541 */       int offset = object2IntOpenHashMap.getInt(format);
/* 542 */       GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(vertexBuffer.currentBuffer().slice(offset, meshBufferSize), false, true); 
/* 543 */       try { MemoryUtil.memCopy(meshVertexBuffer, mappedView.data());
/* 544 */         if (mappedView != null) mappedView.close();  } catch (Throwable throwable) { if (mappedView != null)
/* 545 */           try { mappedView.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  object2IntOpenHashMap.put(format, offset + meshBufferSize);
/* 546 */       this.draws.add(new Draw(vertexBuffer.currentBuffer(), offset / format.getVertexSize(), drawState.mode(), drawState.indexCount(), meshToDraw.pipeline, meshToDraw.textureSetup, meshToDraw.scissorArea));
/* 547 */       meshToDraw.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ensureVertexBufferSizes() {
/* 552 */     Object2IntMap<VertexFormat> requiredSizes = calculatedRequiredVertexBufferSizes();
/* 553 */     for (ObjectIterator<Object2IntMap.Entry<VertexFormat>> objectIterator = requiredSizes.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<VertexFormat> entry = objectIterator.next();
/* 554 */       VertexFormat vertexFormat = (VertexFormat)entry.getKey();
/* 555 */       int requiredSize = entry.getIntValue();
/* 556 */       MappableRingBuffer vertexBuffer = this.vertexBuffers.get(vertexFormat);
/* 557 */       if (vertexBuffer == null || vertexBuffer.size() < requiredSize) {
/* 558 */         if (vertexBuffer != null) {
/* 559 */           vertexBuffer.close();
/*     */         }
/* 561 */         this.vertexBuffers.put(vertexFormat, new MappableRingBuffer(() -> "GUI vertex buffer for " + String.valueOf(vertexFormat), 34, requiredSize));
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private Object2IntMap<VertexFormat> calculatedRequiredVertexBufferSizes() {
/* 567 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 568 */     for (MeshToDraw meshToDraw : this.meshesToDraw) {
/* 569 */       MeshData.DrawState drawState = meshToDraw.mesh.drawState();
/* 570 */       VertexFormat format = drawState.format();
/* 571 */       if (!object2IntOpenHashMap.containsKey(format)) {
/* 572 */         object2IntOpenHashMap.put(format, 0);
/*     */       }
/* 574 */       object2IntOpenHashMap.put(format, object2IntOpenHashMap.getInt(format) + drawState.vertexCount() * format.getVertexSize());
/*     */     } 
/* 576 */     return (Object2IntMap<VertexFormat>)object2IntOpenHashMap;
/*     */   }
/*     */   
/*     */   private void executeDraw(Draw draw, RenderPass renderPass, GpuBuffer indexBuffer, VertexFormat.IndexType indexType) {
/* 580 */     RenderPipeline pipeline = draw.pipeline();
/*     */     
/* 582 */     renderPass.setPipeline(pipeline);
/* 583 */     renderPass.setVertexBuffer(0, draw.vertexBuffer);
/*     */     
/* 585 */     ScreenRectangle scissorArea = draw.scissorArea();
/* 586 */     if (scissorArea != null) {
/* 587 */       enableScissor(scissorArea, renderPass);
/*     */     } else {
/* 589 */       renderPass.disableScissor();
/*     */     } 
/*     */     
/* 592 */     if (draw.textureSetup.texure0() != null) {
/* 593 */       renderPass.bindTexture("Sampler0", draw.textureSetup.texure0(), draw.textureSetup.sampler0());
/*     */     }
/* 595 */     if (draw.textureSetup.texure1() != null) {
/* 596 */       renderPass.bindTexture("Sampler1", draw.textureSetup.texure1(), draw.textureSetup.sampler1());
/*     */     }
/* 598 */     if (draw.textureSetup.texure2() != null) {
/* 599 */       renderPass.bindTexture("Sampler2", draw.textureSetup.texure2(), draw.textureSetup.sampler2());
/*     */     }
/* 601 */     renderPass.setIndexBuffer(indexBuffer, indexType);
/*     */     
/* 603 */     renderPass.drawIndexed(draw.baseVertex, 0, draw.indexCount, 1);
/*     */   }
/*     */   
/*     */   private BufferBuilder getBufferBuilder(RenderPipeline pipeline) {
/* 607 */     return new BufferBuilder(this.byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
/*     */   }
/*     */   
/*     */   private boolean scissorChanged(ScreenRectangle newScissor, ScreenRectangle oldScissor) {
/* 611 */     if (newScissor == oldScissor) {
/* 612 */       return false;
/*     */     }
/* 614 */     if (newScissor != null) {
/* 615 */       return !newScissor.equals(oldScissor);
/*     */     }
/* 617 */     return true;
/*     */   }
/*     */   
/*     */   private void enableScissor(ScreenRectangle rectangle, RenderPass renderPass) {
/* 621 */     Window window = Minecraft.getInstance().getWindow();
/* 622 */     int windowHeight = window.getHeight();
/* 623 */     int guiScale = window.getGuiScale();
/*     */     
/* 625 */     double left = (rectangle.left() * guiScale);
/* 626 */     double bottom = (windowHeight - rectangle.bottom() * guiScale);
/* 627 */     double width = (rectangle.width() * guiScale);
/* 628 */     double height = (rectangle.height() * guiScale);
/* 629 */     renderPass.enableScissor((int)left, (int)bottom, Math.max(0, (int)width), Math.max(0, (int)height));
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 634 */     this.byteBufferBuilder.close();
/* 635 */     if (this.itemsAtlas != null) {
/* 636 */       this.itemsAtlas.close();
/*     */     }
/* 638 */     if (this.itemsAtlasView != null) {
/* 639 */       this.itemsAtlasView.close();
/*     */     }
/* 641 */     if (this.itemsAtlasDepth != null) {
/* 642 */       this.itemsAtlasDepth.close();
/*     */     }
/* 644 */     if (this.itemsAtlasDepthView != null) {
/* 645 */       this.itemsAtlasDepthView.close();
/*     */     }
/* 647 */     this.pictureInPictureRenderers.values().forEach(PictureInPictureRenderer::close);
/* 648 */     this.guiProjectionMatrixBuffer.close();
/* 649 */     this.itemsProjectionMatrixBuffer.close();
/* 650 */     for (MappableRingBuffer buffer : this.vertexBuffers.values()) {
/* 651 */       buffer.close();
/*     */     }
/* 653 */     this.oversizedItemRenderers.values().forEach(PictureInPictureRenderer::close);
/*     */   }
/*     */   
/*     */   private static final class AtlasPosition {
/*     */     final int x;
/*     */     final int y;
/*     */     final float u;
/*     */     final float v;
/*     */     int lastAnimatedOnFrame;
/*     */     
/*     */     private AtlasPosition(int x, int y, float u, float v, int lastAnimatedOnFrame) {
/* 664 */       this.x = x;
/* 665 */       this.y = y;
/* 666 */       this.u = u;
/* 667 */       this.v = v;
/* 668 */       this.lastAnimatedOnFrame = lastAnimatedOnFrame;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/GuiRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */