/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.LightTexture;
/*     */ import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.BlockDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.entity.Display;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public abstract class DisplayRenderer<T extends Display, S, ST extends DisplayEntityRenderState> extends EntityRenderer<T, ST> {
/*     */   private final EntityRenderDispatcher entityRenderDispatcher;
/*     */   
/*     */   protected DisplayRenderer(EntityRendererProvider.Context context) {
/*  36 */     super(context);
/*  37 */     this.entityRenderDispatcher = context.getEntityRenderDispatcher();
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/*  42 */     return entity.getBoundingBoxForCulling();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean affectedByCulling(T entity) {
/*  47 */     return entity.affectedByCulling();
/*     */   }
/*     */   
/*     */   private static int getBrightnessOverride(Display entity) {
/*  51 */     Display.RenderState renderState = entity.renderState();
/*  52 */     return (renderState != null) ? renderState.brightnessOverride() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSkyLightLevel(T entity, BlockPos blockPos) {
/*  57 */     int packedBrightnessOverride = getBrightnessOverride((Display)entity);
/*  58 */     if (packedBrightnessOverride != -1) {
/*  59 */       return LightTexture.sky(packedBrightnessOverride);
/*     */     }
/*  61 */     return super.getSkyLightLevel(entity, blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/*  66 */     int packedBrightnessOverride = getBrightnessOverride((Display)entity);
/*  67 */     if (packedBrightnessOverride != -1) {
/*  68 */       return LightTexture.block(packedBrightnessOverride);
/*     */     }
/*  70 */     return super.getBlockLightLevel(entity, blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getShadowRadius(ST state) {
/*  75 */     Display.RenderState renderState = ((DisplayEntityRenderState)state).renderState;
/*  76 */     if (renderState == null) {
/*  77 */       return 0.0F;
/*     */     }
/*  79 */     return renderState.shadowRadius().get(((DisplayEntityRenderState)state).interpolationProgress);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getShadowStrength(ST state) {
/*  84 */     Display.RenderState renderState = ((DisplayEntityRenderState)state).renderState;
/*  85 */     if (renderState == null) {
/*  86 */       return 0.0F;
/*     */     }
/*  88 */     return renderState.shadowStrength().get(((DisplayEntityRenderState)state).interpolationProgress);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(ST state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  93 */     Display.RenderState renderState = ((DisplayEntityRenderState)state).renderState;
/*  94 */     if (renderState == null || !state.hasSubState()) {
/*     */       return;
/*     */     }
/*  97 */     float interpolationProgress = ((DisplayEntityRenderState)state).interpolationProgress;
/*     */     
/*  99 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */     
/* 101 */     poseStack.pushPose();
/* 102 */     poseStack.mulPose((Quaternionfc)calculateOrientation(renderState, state, new Quaternionf()));
/* 103 */     Transformation transformation = (Transformation)renderState.transformation().get(interpolationProgress);
/* 104 */     poseStack.mulPose(transformation.getMatrix());
/* 105 */     submitInner(state, poseStack, submitNodeCollector, ((DisplayEntityRenderState)state).lightCoords, interpolationProgress);
/* 106 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private Quaternionf calculateOrientation(Display.RenderState renderState, ST state, Quaternionf output) {
/* 110 */     switch (renderState.billboardConstraints()) { default: throw new MatchException(null, null);case FIXED: case HORIZONTAL: case VERTICAL: case CENTER: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       output.rotationYXZ(-0.017453292F * transformYRot(((DisplayEntityRenderState)state).cameraYRot), 0.017453292F * transformXRot(((DisplayEntityRenderState)state).cameraXRot), 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private static float transformYRot(float cameraYRot) {
/* 123 */     return cameraYRot - 180.0F;
/*     */   }
/*     */   
/*     */   private static float transformXRot(float cameraXRot) {
/* 127 */     return -cameraXRot;
/*     */   }
/*     */   
/*     */   private static <T extends Display> float entityYRot(T entity, float partialTicks) {
/* 131 */     return entity.getYRot(partialTicks);
/*     */   }
/*     */   
/*     */   private static <T extends Display> float entityXRot(T entity, float partialTicks) {
/* 135 */     return entity.getXRot(partialTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void extractRenderState(T entity, ST state, float partialTicks) {
/* 142 */     super.extractRenderState(entity, state, partialTicks);
/* 143 */     ((DisplayEntityRenderState)state).renderState = entity.renderState();
/* 144 */     ((DisplayEntityRenderState)state).interpolationProgress = entity.calculateInterpolationProgress(partialTicks);
/* 145 */     ((DisplayEntityRenderState)state).entityYRot = entityYRot(entity, partialTicks);
/* 146 */     ((DisplayEntityRenderState)state).entityXRot = entityXRot(entity, partialTicks);
/* 147 */     Camera camera = this.entityRenderDispatcher.camera;
/* 148 */     ((DisplayEntityRenderState)state).cameraXRot = camera.xRot();
/* 149 */     ((DisplayEntityRenderState)state).cameraYRot = camera.yRot();
/*     */   }
/*     */   protected abstract void submitInner(ST paramST, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt, float paramFloat);
/*     */   
/*     */   public static class BlockDisplayRenderer extends DisplayRenderer<Display.BlockDisplay, Display.BlockDisplay.BlockRenderState, BlockDisplayEntityRenderState> { protected BlockDisplayRenderer(EntityRendererProvider.Context context) {
/* 154 */       super(context);
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockDisplayEntityRenderState createRenderState() {
/* 159 */       return new BlockDisplayEntityRenderState();
/*     */     }
/*     */ 
/*     */     
/*     */     public void extractRenderState(Display.BlockDisplay entity, BlockDisplayEntityRenderState state, float partialTicks) {
/* 164 */       super.extractRenderState(entity, state, partialTicks);
/* 165 */       state.blockRenderState = entity.blockRenderState();
/*     */     }
/*     */ 
/*     */     
/*     */     public void submitInner(BlockDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/* 170 */       submitNodeCollector.submitBlock(poseStack, state.blockRenderState.blockState(), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class ItemDisplayRenderer extends DisplayRenderer<Display.ItemDisplay, Display.ItemDisplay.ItemRenderState, ItemDisplayEntityRenderState> {
/*     */     private final ItemModelResolver itemModelResolver;
/*     */     
/*     */     protected ItemDisplayRenderer(EntityRendererProvider.Context context) {
/* 178 */       super(context);
/* 179 */       this.itemModelResolver = context.getItemModelResolver();
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemDisplayEntityRenderState createRenderState() {
/* 184 */       return new ItemDisplayEntityRenderState();
/*     */     }
/*     */ 
/*     */     
/*     */     public void extractRenderState(Display.ItemDisplay entity, ItemDisplayEntityRenderState state, float partialTicks) {
/* 189 */       super.extractRenderState(entity, state, partialTicks);
/* 190 */       Display.ItemDisplay.ItemRenderState itemRenderState = entity.itemRenderState();
/* 191 */       if (itemRenderState != null) {
/* 192 */         this.itemModelResolver.updateForNonLiving(state.item, itemRenderState.itemStack(), itemRenderState.itemTransform(), (Entity)entity);
/*     */       } else {
/* 194 */         state.item.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void submitInner(ItemDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/* 200 */       if (state.item.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 204 */       poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotation(3.1415927F));
/* 205 */       state.item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TextDisplayRenderer extends DisplayRenderer<Display.TextDisplay, Display.TextDisplay.TextRenderState, TextDisplayEntityRenderState> {
/*     */     private final Font font;
/*     */     
/*     */     protected TextDisplayRenderer(EntityRendererProvider.Context context) {
/* 213 */       super(context);
/* 214 */       this.font = context.getFont();
/*     */     }
/*     */ 
/*     */     
/*     */     public TextDisplayEntityRenderState createRenderState() {
/* 219 */       return new TextDisplayEntityRenderState();
/*     */     }
/*     */ 
/*     */     
/*     */     public void extractRenderState(Display.TextDisplay entity, TextDisplayEntityRenderState state, float partialTicks) {
/* 224 */       super.extractRenderState(entity, state, partialTicks);
/* 225 */       state.textRenderState = entity.textRenderState();
/* 226 */       state.cachedInfo = entity.cacheDisplay(this::splitLines);
/*     */     }
/*     */     
/*     */     private Display.TextDisplay.CachedInfo splitLines(Component input, int width) {
/* 230 */       List<FormattedCharSequence> lines = this.font.split((FormattedText)input, width);
/* 231 */       List<Display.TextDisplay.CachedLine> result = new ArrayList<>(lines.size());
/*     */       
/* 233 */       int maxLineWidth = 0;
/* 234 */       for (FormattedCharSequence line : lines) {
/* 235 */         int lineWidth = this.font.width(line);
/* 236 */         maxLineWidth = Math.max(maxLineWidth, lineWidth);
/* 237 */         result.add(new Display.TextDisplay.CachedLine(line, lineWidth));
/*     */       } 
/*     */       
/* 240 */       return new Display.TextDisplay.CachedInfo(result, maxLineWidth);
/*     */     }
/*     */     
/*     */     public void submitInner(TextDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/*     */       int backgroundColor;
/* 245 */       Display.TextDisplay.TextRenderState renderState = state.textRenderState;
/* 246 */       byte flags = renderState.flags();
/*     */       
/* 248 */       boolean seeThrough = ((flags & 0x2) != 0);
/* 249 */       boolean useDefaultBackground = ((flags & 0x4) != 0);
/* 250 */       boolean shadow = ((flags & 0x1) != 0);
/* 251 */       Display.TextDisplay.Align alignment = Display.TextDisplay.getAlign(flags);
/* 252 */       byte textOpacity = (byte)renderState.textOpacity().get(interpolationProgress);
/*     */       
/* 254 */       if (useDefaultBackground) {
/* 255 */         float backgroundAlpha = (net.minecraft.client.Minecraft.getInstance()).options.getBackgroundOpacity(0.25F);
/* 256 */         backgroundColor = (int)(backgroundAlpha * 255.0F) << 24;
/*     */       } else {
/* 258 */         backgroundColor = renderState.backgroundColor().get(interpolationProgress);
/*     */       } 
/*     */       
/* 261 */       float y = 0.0F;
/*     */       
/* 263 */       Matrix4f pose = poseStack.last().pose();
/*     */       
/* 265 */       pose.rotate(3.1415927F, 0.0F, 1.0F, 0.0F);
/* 266 */       pose.scale(-0.025F, -0.025F, -0.025F);
/* 267 */       Display.TextDisplay.CachedInfo cachedInfo = state.cachedInfo;
/*     */       
/* 269 */       int lineSpacing = 1;
/* 270 */       java.util.Objects.requireNonNull(this.font); int lineHeight = 9 + 1;
/*     */       
/* 272 */       int width = cachedInfo.width();
/* 273 */       int height = cachedInfo.lines().size() * lineHeight - 1;
/* 274 */       pose.translate(1.0F - width / 2.0F, -height, 0.0F);
/*     */       
/* 276 */       if (backgroundColor != 0) {
/* 277 */         submitNodeCollector.submitCustomGeometry(poseStack, seeThrough ? RenderTypes.textBackgroundSeeThrough() : RenderTypes.textBackground(), (lambdaPose, buffer) -> {
/*     */               buffer.addVertex(lambdaPose, -1.0F, -1.0F, 0.0F).setColor(backgroundColor).setLight(lightCoords);
/*     */               
/*     */               buffer.addVertex(lambdaPose, -1.0F, height, 0.0F).setColor(backgroundColor).setLight(lightCoords);
/*     */               buffer.addVertex(lambdaPose, width, height, 0.0F).setColor(backgroundColor).setLight(lightCoords);
/*     */               buffer.addVertex(lambdaPose, width, -1.0F, 0.0F).setColor(backgroundColor).setLight(lightCoords);
/*     */             });
/*     */       }
/* 285 */       OrderedSubmitNodeCollector textCollector = submitNodeCollector.order((backgroundColor != 0) ? 1 : 0);
/* 286 */       for (Display.TextDisplay.CachedLine line : (Iterable<Display.TextDisplay.CachedLine>)cachedInfo.lines()) {
/* 287 */         switch (alignment) { default: throw new MatchException(null, null);
/*     */           case LEFT: 
/*     */           case RIGHT: 
/* 290 */           case CENTER: break; }  float offset = width / 2.0F - line.width() / 2.0F;
/*     */         
/* 292 */         textCollector.submitText(poseStack, offset, y, line.contents(), shadow, seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.POLYGON_OFFSET, lightCoords, textOpacity << 24 | 0xFFFFFF, 0, 0);
/* 293 */         y += lineHeight;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/DisplayRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */