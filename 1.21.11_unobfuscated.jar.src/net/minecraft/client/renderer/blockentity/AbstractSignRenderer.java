/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.SignRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.SignBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public abstract class AbstractSignRenderer implements BlockEntityRenderer<SignBlockEntity, SignRenderState> {
/*  38 */   private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
/*     */   private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
/*     */   private final Font font;
/*     */   private final MaterialSet materials;
/*     */   
/*     */   public AbstractSignRenderer(BlockEntityRendererProvider.Context context) {
/*  44 */     this.font = context.font();
/*  45 */     this.materials = context.materials();
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
/*     */   public void submit(SignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  62 */     BlockState blockState = state.blockState;
/*  63 */     SignBlock signBlock = (SignBlock)blockState.getBlock();
/*  64 */     Model.Simple signModel = getSignModel(blockState, signBlock.type());
/*  65 */     submitSignWithText(state, poseStack, blockState, signBlock, signBlock.type(), signModel, state.breakProgress, submitNodeCollector);
/*     */   }
/*     */   
/*     */   private void submitSignWithText(SignRenderState state, PoseStack poseStack, BlockState blockState, SignBlock signBlock, WoodType type, Model.Simple signModel, ModelFeatureRenderer.CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector) {
/*  69 */     poseStack.pushPose();
/*  70 */     translateSign(poseStack, -signBlock.getYRotationDegrees(blockState), blockState);
/*  71 */     submitSign(poseStack, state.lightCoords, type, signModel, breakProgress, submitNodeCollector);
/*  72 */     submitSignText(state, poseStack, submitNodeCollector, true);
/*  73 */     submitSignText(state, poseStack, submitNodeCollector, false);
/*  74 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   protected void submitSign(PoseStack poseStack, int lightCoords, WoodType type, Model.Simple signModel, ModelFeatureRenderer.CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector) {
/*  78 */     poseStack.pushPose();
/*  79 */     float scale = getSignModelRenderScale();
/*  80 */     poseStack.scale(scale, -scale, -scale);
/*  81 */     Material material = getSignMaterial(type);
/*  82 */     Objects.requireNonNull(signModel); RenderType renderType = material.renderType(signModel::renderType);
/*  83 */     submitNodeCollector.submitModel((Model)signModel, Unit.INSTANCE, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, -1, this.materials.get(material), 0, breakProgress);
/*  84 */     poseStack.popPose();
/*     */   } private void submitSignText(SignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, boolean isFrontText) {
/*     */     int textColor;
/*     */     boolean drawOutline;
/*     */     int lightVal;
/*  89 */     SignText signText = isFrontText ? state.frontText : state.backText;
/*  90 */     if (signText == null) {
/*     */       return;
/*     */     }
/*  93 */     poseStack.pushPose();
/*  94 */     translateSignText(poseStack, isFrontText, getTextOffset());
/*  95 */     int darkColor = getDarkColor(signText);
/*  96 */     int signMidpoint = 4 * state.textLineHeight / 2;
/*  97 */     FormattedCharSequence[] formattedLines = signText.getRenderMessages(state.isTextFilteringEnabled, input -> {
/*     */           List<FormattedCharSequence> components = this.font.split((FormattedText)state, state.maxTextLineWidth);
/*     */ 
/*     */           
/*     */           return components.isEmpty() ? FormattedCharSequence.EMPTY : components.get(0);
/*     */         });
/*     */ 
/*     */     
/* 105 */     if (signText.hasGlowingText()) {
/* 106 */       textColor = signText.getColor().getTextColor();
/* 107 */       drawOutline = (textColor == DyeColor.BLACK.getTextColor() || state.drawOutline);
/* 108 */       lightVal = 15728880;
/*     */     } else {
/* 110 */       textColor = darkColor;
/* 111 */       drawOutline = false;
/* 112 */       lightVal = state.lightCoords;
/*     */     } 
/*     */     
/* 115 */     for (int i = 0; i < 4; i++) {
/* 116 */       FormattedCharSequence actualLine = formattedLines[i];
/* 117 */       float x1 = (-this.font.width(actualLine) / 2);
/*     */       
/* 119 */       submitNodeCollector.submitText(poseStack, x1, (i * state.textLineHeight - signMidpoint), actualLine, false, Font.DisplayMode.POLYGON_OFFSET, lightVal, textColor, 0, drawOutline ? darkColor : 0);
/*     */     } 
/* 121 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void translateSignText(PoseStack poseStack, boolean isFrontText, Vec3 textOffset) {
/* 125 */     if (!isFrontText) {
/* 126 */       poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(180.0F));
/*     */     }
/*     */     
/* 129 */     float s = 0.015625F * getSignTextRenderScale();
/* 130 */     poseStack.translate(textOffset);
/* 131 */     poseStack.scale(s, -s, s);
/*     */   }
/*     */   
/*     */   private static boolean isOutlineVisible(BlockPos pos) {
/* 135 */     Minecraft minecraft = Minecraft.getInstance();
/* 136 */     LocalPlayer player = minecraft.player;
/* 137 */     if (player != null && minecraft.options.getCameraType().isFirstPerson() && player.isScoping()) {
/* 138 */       return true;
/*     */     }
/*     */     
/* 141 */     Entity camera = minecraft.getCameraEntity();
/* 142 */     if (camera != null && camera.distanceToSqr(Vec3.atCenterOf((Vec3i)pos)) < OUTLINE_RENDER_DISTANCE) {
/* 143 */       return true;
/*     */     }
/*     */     
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public static int getDarkColor(SignText signText) {
/* 150 */     int color = signText.getColor().getTextColor();
/*     */     
/* 152 */     if (color == DyeColor.BLACK.getTextColor() && signText.hasGlowingText()) {
/* 153 */       return -988212;
/*     */     }
/*     */     
/* 156 */     return ARGB.scaleRGB(color, 0.4F);
/*     */   }
/*     */ 
/*     */   
/*     */   public SignRenderState createRenderState() {
/* 161 */     return new SignRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(SignBlockEntity blockEntity, SignRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 166 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 167 */     state.maxTextLineWidth = blockEntity.getMaxTextLineWidth();
/* 168 */     state.textLineHeight = blockEntity.getTextLineHeight();
/* 169 */     state.frontText = blockEntity.getFrontText();
/* 170 */     state.backText = blockEntity.getBackText();
/* 171 */     state.isTextFilteringEnabled = Minecraft.getInstance().isTextFilteringEnabled();
/* 172 */     state.drawOutline = isOutlineVisible(blockEntity.getBlockPos());
/*     */   }
/*     */   
/*     */   protected abstract Model.Simple getSignModel(BlockState paramBlockState, WoodType paramWoodType);
/*     */   
/*     */   protected abstract Material getSignMaterial(WoodType paramWoodType);
/*     */   
/*     */   protected abstract float getSignModelRenderScale();
/*     */   
/*     */   protected abstract float getSignTextRenderScale();
/*     */   
/*     */   protected abstract Vec3 getTextOffset();
/*     */   
/*     */   protected abstract void translateSign(PoseStack paramPoseStack, float paramFloat, BlockState paramBlockState);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/AbstractSignRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */