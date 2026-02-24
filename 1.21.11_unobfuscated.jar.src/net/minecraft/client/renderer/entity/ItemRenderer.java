/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexMultiConsumer;
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ 
/*     */ 
/*     */ public class ItemRenderer
/*     */ {
/*  23 */   public static final Identifier ENCHANTED_GLINT_ARMOR = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_armor.png");
/*  24 */   public static final Identifier ENCHANTED_GLINT_ITEM = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_item.png");
/*     */ 
/*     */   
/*     */   public static final float SPECIAL_FOIL_UI_SCALE = 0.5F;
/*     */ 
/*     */   
/*     */   public static final float SPECIAL_FOIL_FIRST_PERSON_SCALE = 0.75F;
/*     */   
/*     */   public static final float SPECIAL_FOIL_TEXTURE_SCALE = 0.0078125F;
/*     */   
/*     */   public static final int NO_TINT = -1;
/*     */ 
/*     */   
/*     */   public static void renderItem(ItemDisplayContext type, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int overlayCoords, int[] tintLayers, List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) {
/*     */     VertexConsumer builder;
/*  39 */     if (foilType == ItemStackRenderState.FoilType.SPECIAL) {
/*     */       
/*  41 */       PoseStack.Pose cameraPose = poseStack.last().copy();
/*  42 */       if (type == ItemDisplayContext.GUI) {
/*  43 */         MatrixUtil.mulComponentWise(cameraPose.pose(), 0.5F);
/*  44 */       } else if (type.firstPerson()) {
/*  45 */         MatrixUtil.mulComponentWise(cameraPose.pose(), 0.75F);
/*     */       } 
/*  47 */       builder = getSpecialFoilBuffer(bufferSource, renderType, cameraPose);
/*     */     } else {
/*  49 */       builder = getFoilBuffer(bufferSource, renderType, true, (foilType != ItemStackRenderState.FoilType.NONE));
/*     */     } 
/*     */     
/*  52 */     renderQuadList(poseStack, builder, quads, tintLayers, lightCoords, overlayCoords);
/*     */   }
/*     */   
/*     */   private static VertexConsumer getSpecialFoilBuffer(MultiBufferSource bufferSource, RenderType renderType, PoseStack.Pose cameraPose) {
/*  56 */     return VertexMultiConsumer.create((VertexConsumer)new SheetedDecalTextureGenerator(
/*  57 */           bufferSource.getBuffer(useTransparentGlint(renderType) ? RenderTypes.glintTranslucent() : RenderTypes.glint()), cameraPose, 0.0078125F), 
/*  58 */         bufferSource.getBuffer(renderType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static VertexConsumer getFoilBuffer(MultiBufferSource bufferSource, RenderType renderType, boolean sheeted, boolean hasFoil) {
/*  63 */     if (hasFoil) {
/*  64 */       if (useTransparentGlint(renderType)) {
/*  65 */         return VertexMultiConsumer.create(
/*  66 */             bufferSource.getBuffer(RenderTypes.glintTranslucent()), 
/*  67 */             bufferSource.getBuffer(renderType));
/*     */       }
/*     */ 
/*     */       
/*  71 */       return VertexMultiConsumer.create(
/*  72 */           bufferSource.getBuffer(sheeted ? RenderTypes.glint() : RenderTypes.entityGlint()), 
/*  73 */           bufferSource.getBuffer(renderType));
/*     */     } 
/*     */     
/*  76 */     return bufferSource.getBuffer(renderType);
/*     */   }
/*     */   
/*     */   public static List<RenderType> getFoilRenderTypes(RenderType baseRenderType, boolean sheeted, boolean hasFoil) {
/*  80 */     if (hasFoil) {
/*  81 */       if (useTransparentGlint(baseRenderType)) {
/*  82 */         return List.of(baseRenderType, RenderTypes.glintTranslucent());
/*     */       }
/*  84 */       return List.of(baseRenderType, sheeted ? RenderTypes.glint() : RenderTypes.entityGlint());
/*     */     } 
/*  86 */     return List.of(baseRenderType);
/*     */   }
/*     */   
/*     */   private static boolean useTransparentGlint(RenderType renderType) {
/*  90 */     return (Minecraft.useShaderTransparency() && (renderType == Sheets.translucentItemSheet() || renderType == Sheets.translucentBlockItemSheet()));
/*     */   }
/*     */   
/*     */   private static int getLayerColorSafe(int[] layers, int layer) {
/*  94 */     if (layer < 0 || layer >= layers.length) {
/*  95 */       return -1;
/*     */     }
/*  97 */     return layers[layer];
/*     */   }
/*     */   
/*     */   private static void renderQuadList(PoseStack poseStack, VertexConsumer builder, List<BakedQuad> quads, int[] tintLayers, int lightCoords, int overlayCoords) {
/* 101 */     PoseStack.Pose pose = poseStack.last();
/*     */     
/* 103 */     for (BakedQuad quad : quads) {
/*     */       float alpha, red, green, blue;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       if (quad.isTinted()) {
/* 110 */         int color = getLayerColorSafe(tintLayers, quad.tintIndex());
/* 111 */         alpha = ARGB.alpha(color) / 255.0F;
/* 112 */         red = ARGB.red(color) / 255.0F;
/* 113 */         green = ARGB.green(color) / 255.0F;
/* 114 */         blue = ARGB.blue(color) / 255.0F;
/*     */       } else {
/* 116 */         alpha = 1.0F;
/* 117 */         red = 1.0F;
/* 118 */         green = 1.0F;
/* 119 */         blue = 1.0F;
/*     */       } 
/* 121 */       builder.putBulkData(pose, quad, red, green, blue, alpha, lightCoords, overlayCoords);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ItemRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */