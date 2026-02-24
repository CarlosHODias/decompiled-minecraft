/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ItemEntityRenderer extends EntityRenderer<ItemEntity, ItemEntityRenderState> {
/*     */   private static final float ITEM_MIN_HOVER_HEIGHT = 0.0625F;
/*     */   private static final float ITEM_BUNDLE_OFFSET_SCALE = 0.15F;
/*  23 */   private final RandomSource random = RandomSource.create(); private static final float FLAT_ITEM_DEPTH_THRESHOLD = 0.0625F; private final ItemModelResolver itemModelResolver;
/*     */   
/*     */   public ItemEntityRenderer(EntityRendererProvider.Context context) {
/*  26 */     super(context);
/*  27 */     this.itemModelResolver = context.getItemModelResolver();
/*     */     
/*  29 */     this.shadowRadius = 0.15F;
/*  30 */     this.shadowStrength = 0.75F;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemEntityRenderState createRenderState() {
/*  35 */     return new ItemEntityRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(ItemEntity entity, ItemEntityRenderState state, float partialTicks) {
/*  40 */     super.extractRenderState(entity, state, partialTicks);
/*     */     
/*  42 */     state.bobOffset = entity.bobOffs;
/*     */     
/*  44 */     state.extractItemGroupRenderState((Entity)entity, entity.getItem(), this.itemModelResolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  49 */     if (state.item.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  53 */     poseStack.pushPose();
/*     */     
/*  55 */     AABB boundingBox = state.item.getModelBoundingBox();
/*     */     
/*  57 */     float minOffsetY = -((float)boundingBox.minY) + 0.0625F;
/*  58 */     float bob = Mth.sin((state.ageInTicks / 10.0F + state.bobOffset)) * 0.1F + 0.1F;
/*  59 */     poseStack.translate(0.0F, bob + minOffsetY, 0.0F);
/*     */ 
/*     */     
/*  62 */     float spin = ItemEntity.getSpin(state.ageInTicks, state.bobOffset);
/*  63 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotation(spin));
/*     */     
/*  65 */     submitMultipleFromCount(poseStack, submitNodeCollector, state.lightCoords, (ItemClusterRenderState)state, this.random, boundingBox);
/*  66 */     poseStack.popPose();
/*     */     
/*  68 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   public static void submitMultipleFromCount(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemClusterRenderState state, RandomSource random) {
/*  72 */     submitMultipleFromCount(poseStack, submitNodeCollector, lightCoords, state, random, state.item.getModelBoundingBox());
/*     */   }
/*     */   
/*     */   public static void submitMultipleFromCount(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemClusterRenderState state, RandomSource random, AABB modelBoundingBox) {
/*  76 */     int amount = state.count;
/*  77 */     if (amount == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     random.setSeed(state.seed);
/*  82 */     ItemStackRenderState item = state.item;
/*     */ 
/*     */ 
/*     */     
/*  86 */     float modelDepth = (float)modelBoundingBox.getZsize();
/*  87 */     if (modelDepth > 0.0625F) {
/*     */       
/*  89 */       item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */       
/*  91 */       for (int i = 1; i < amount; i++) {
/*  92 */         poseStack.pushPose();
/*  93 */         float xo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
/*  94 */         float yo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
/*  95 */         float zo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
/*  96 */         poseStack.translate(xo, yo, zo);
/*  97 */         item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*  98 */         poseStack.popPose();
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 103 */       float offsetZ = modelDepth * 1.5F;
/* 104 */       poseStack.translate(0.0F, 0.0F, -(offsetZ * (amount - 1) / 2.0F));
/*     */       
/* 106 */       item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 107 */       poseStack.translate(0.0F, 0.0F, offsetZ);
/*     */       
/* 109 */       for (int i = 1; i < amount; i++) {
/* 110 */         poseStack.pushPose();
/* 111 */         float xo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
/* 112 */         float yo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
/* 113 */         poseStack.translate(xo, yo, 0.0F);
/* 114 */         item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 115 */         poseStack.popPose();
/* 116 */         poseStack.translate(0.0F, 0.0F, offsetZ);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void renderMultipleFromCount(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemClusterRenderState state, RandomSource random) {
/* 122 */     AABB modelBoundingBox = state.item.getModelBoundingBox();
/* 123 */     int amount = state.count;
/* 124 */     if (amount == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     random.setSeed(state.seed);
/* 129 */     ItemStackRenderState item = state.item;
/*     */ 
/*     */ 
/*     */     
/* 133 */     float modelDepth = (float)modelBoundingBox.getZsize();
/* 134 */     if (modelDepth > 0.0625F) {
/*     */       
/* 136 */       item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */       
/* 138 */       for (int i = 1; i < amount; i++) {
/* 139 */         poseStack.pushPose();
/* 140 */         float xo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
/* 141 */         float yo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
/* 142 */         float zo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
/* 143 */         poseStack.translate(xo, yo, zo);
/* 144 */         item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 145 */         poseStack.popPose();
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 150 */       float offsetZ = modelDepth * 1.5F;
/* 151 */       poseStack.translate(0.0F, 0.0F, -(offsetZ * (amount - 1) / 2.0F));
/*     */       
/* 153 */       item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 154 */       poseStack.translate(0.0F, 0.0F, offsetZ);
/*     */       
/* 156 */       for (int i = 1; i < amount; i++) {
/* 157 */         poseStack.pushPose();
/* 158 */         float xo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
/* 159 */         float yo = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
/* 160 */         poseStack.translate(xo, yo, 0.0F);
/* 161 */         item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 162 */         poseStack.popPose();
/* 163 */         poseStack.translate(0.0F, 0.0F, offsetZ);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ItemEntityRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */