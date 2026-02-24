/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.ItemTransform;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ItemStackRenderState
/*     */ {
/*  26 */   ItemDisplayContext displayContext = ItemDisplayContext.NONE;
/*     */   
/*     */   private int activeLayerCount;
/*     */   
/*     */   private boolean animated;
/*     */   private boolean oversizedInGui;
/*     */   private AABB cachedModelBoundingBox;
/*  33 */   private LayerRenderState[] layers = new LayerRenderState[] { new LayerRenderState() };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int requestedCount) {
/*  40 */     int currentCapacity = this.layers.length;
/*  41 */     int requiredNewCapacity = this.activeLayerCount + requestedCount;
/*  42 */     if (requiredNewCapacity > currentCapacity) {
/*  43 */       this.layers = Arrays.<LayerRenderState>copyOf(this.layers, requiredNewCapacity);
/*  44 */       for (int i = currentCapacity; i < requiredNewCapacity; i++) {
/*  45 */         this.layers[i] = new LayerRenderState();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public LayerRenderState newLayer() {
/*  51 */     ensureCapacity(1);
/*  52 */     return this.layers[this.activeLayerCount++];
/*     */   }
/*     */   
/*     */   public void clear() {
/*  56 */     this.displayContext = ItemDisplayContext.NONE;
/*     */ 
/*     */     
/*  59 */     for (int i = 0; i < this.activeLayerCount; i++) {
/*  60 */       this.layers[i].clear();
/*     */     }
/*  62 */     this.activeLayerCount = 0;
/*  63 */     this.animated = false;
/*  64 */     this.oversizedInGui = false;
/*  65 */     this.cachedModelBoundingBox = null;
/*     */   }
/*     */   
/*     */   public void setAnimated() {
/*  69 */     this.animated = true;
/*     */   }
/*     */   
/*     */   public boolean isAnimated() {
/*  73 */     return this.animated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendModelIdentityElement(Object element) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LayerRenderState firstLayer() {
/*  85 */     return this.layers[0];
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  89 */     return (this.activeLayerCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesBlockLight() {
/*  94 */     return (firstLayer()).usesBlockLight;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite pickParticleIcon(RandomSource randomSource) {
/*  98 */     if (this.activeLayerCount == 0) {
/*  99 */       return null;
/*     */     }
/* 101 */     return (this.layers[randomSource.nextInt(this.activeLayerCount)]).particleIcon;
/*     */   }
/*     */   
/*     */   public void visitExtents(Consumer<Vector3fc> output) {
/* 105 */     Vector3f scratch = new Vector3f();
/* 106 */     PoseStack.Pose pose = new PoseStack.Pose();
/* 107 */     for (int i = 0; i < this.activeLayerCount; i++) {
/* 108 */       LayerRenderState layer = this.layers[i];
/* 109 */       layer.transform.apply(this.displayContext.leftHand(), pose);
/* 110 */       Matrix4f poseTransform = pose.pose();
/* 111 */       Vector3fc[] layerExtents = layer.extents.get();
/* 112 */       for (Vector3fc extent : layerExtents) {
/* 113 */         output.accept(scratch.set(extent).mulPosition((Matrix4fc)poseTransform));
/*     */       }
/* 115 */       pose.setIdentity();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 120 */     for (int i = 0; i < this.activeLayerCount; i++) {
/* 121 */       this.layers[i].submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*     */     }
/*     */   }
/*     */   
/*     */   public AABB getModelBoundingBox() {
/* 126 */     if (this.cachedModelBoundingBox != null) {
/* 127 */       return this.cachedModelBoundingBox;
/*     */     }
/* 129 */     AABB.Builder collector = new AABB.Builder();
/* 130 */     Objects.requireNonNull(collector); visitExtents(collector::include);
/* 131 */     AABB aabb = collector.build();
/* 132 */     this.cachedModelBoundingBox = aabb;
/* 133 */     return aabb;
/*     */   }
/*     */   
/*     */   public void setOversizedInGui(boolean oversizedInGui) {
/* 137 */     this.oversizedInGui = oversizedInGui;
/*     */   }
/*     */   
/*     */   public boolean isOversizedInGui() {
/* 141 */     return this.oversizedInGui;
/*     */   }
/*     */   
/*     */   public enum FoilType {
/* 145 */     NONE,
/* 146 */     STANDARD,
/* 147 */     SPECIAL;
/*     */   }
/*     */   
/*     */   public class LayerRenderState {
/* 151 */     private static final Vector3fc[] NO_EXTENTS = new Vector3fc[0];
/*     */     
/*     */     public static final Supplier<Vector3fc[]> NO_EXTENTS_SUPPLIER = () -> NO_EXTENTS;
/* 154 */     private final List<BakedQuad> quads = new ArrayList<>();
/*     */     private boolean usesBlockLight;
/*     */     private TextureAtlasSprite particleIcon;
/* 157 */     private ItemTransform transform = ItemTransform.NO_TRANSFORM;
/*     */     
/*     */     private RenderType renderType;
/* 160 */     private ItemStackRenderState.FoilType foilType = ItemStackRenderState.FoilType.NONE;
/*     */ 
/*     */     
/* 163 */     private int[] tintLayers = new int[0];
/*     */     
/*     */     private SpecialModelRenderer<Object> specialRenderer;
/*     */     private Object argumentForSpecialRendering;
/* 167 */     private Supplier<Vector3fc[]> extents = NO_EXTENTS_SUPPLIER;
/*     */     
/*     */     public void clear() {
/* 170 */       this.quads.clear();
/* 171 */       this.renderType = null;
/* 172 */       this.foilType = ItemStackRenderState.FoilType.NONE;
/* 173 */       this.specialRenderer = null;
/* 174 */       this.argumentForSpecialRendering = null;
/* 175 */       Arrays.fill(this.tintLayers, -1);
/* 176 */       this.usesBlockLight = false;
/* 177 */       this.particleIcon = null;
/* 178 */       this.transform = ItemTransform.NO_TRANSFORM;
/* 179 */       this.extents = NO_EXTENTS_SUPPLIER;
/*     */     }
/*     */     
/*     */     public List<BakedQuad> prepareQuadList() {
/* 183 */       return this.quads;
/*     */     }
/*     */     
/*     */     public void setRenderType(RenderType renderType) {
/* 187 */       this.renderType = renderType;
/*     */     }
/*     */     
/*     */     public void setUsesBlockLight(boolean usesBlockLight) {
/* 191 */       this.usesBlockLight = usesBlockLight;
/*     */     }
/*     */     
/*     */     public void setExtents(Supplier<Vector3fc[]> extents) {
/* 195 */       this.extents = extents;
/*     */     }
/*     */     
/*     */     public void setParticleIcon(TextureAtlasSprite particleIcon) {
/* 199 */       this.particleIcon = particleIcon;
/*     */     }
/*     */     
/*     */     public void setTransform(ItemTransform transform) {
/* 203 */       this.transform = transform;
/*     */     }
/*     */     
/*     */     public <T> void setupSpecialModel(SpecialModelRenderer<T> renderer, T argument) {
/* 207 */       this.specialRenderer = eraseSpecialRenderer(renderer);
/* 208 */       this.argumentForSpecialRendering = argument;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static SpecialModelRenderer<Object> eraseSpecialRenderer(SpecialModelRenderer<?> renderer) {
/* 214 */       return (SpecialModelRenderer)renderer;
/*     */     }
/*     */     
/*     */     public void setFoilType(ItemStackRenderState.FoilType foilType) {
/* 218 */       this.foilType = foilType;
/*     */     }
/*     */     
/*     */     public int[] prepareTintLayers(int activeTints) {
/* 222 */       if (activeTints > this.tintLayers.length) {
/* 223 */         this.tintLayers = new int[activeTints];
/* 224 */         Arrays.fill(this.tintLayers, -1);
/*     */       } 
/* 226 */       return this.tintLayers;
/*     */     }
/*     */     
/*     */     private void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 230 */       poseStack.pushPose();
/* 231 */       this.transform.apply(ItemStackRenderState.this.displayContext.leftHand(), poseStack.last());
/*     */       
/* 233 */       if (this.specialRenderer != null) {
/* 234 */         this.specialRenderer.submit(this.argumentForSpecialRendering, ItemStackRenderState.this.displayContext, poseStack, submitNodeCollector, lightCoords, overlayCoords, (this.foilType != ItemStackRenderState.FoilType.NONE), outlineColor);
/* 235 */       } else if (this.renderType != null) {
/* 236 */         submitNodeCollector.submitItem(poseStack, ItemStackRenderState.this.displayContext, lightCoords, overlayCoords, outlineColor, this.tintLayers, this.quads, this.renderType, this.foilType);
/*     */       } 
/* 238 */       poseStack.popPose();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ItemStackRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */