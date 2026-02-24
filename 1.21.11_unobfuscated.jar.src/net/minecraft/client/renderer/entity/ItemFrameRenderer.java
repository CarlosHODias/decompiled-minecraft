/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.MapRenderer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.resources.model.BlockStateDefinitions;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ItemFrameRenderer<T extends ItemFrame> extends EntityRenderer<T, ItemFrameRenderState> {
/*     */   public static final int GLOW_FRAME_BRIGHTNESS = 5;
/*     */   public static final int BRIGHT_MAP_LIGHT_ADJUSTMENT = 30;
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   private final MapRenderer mapRenderer;
/*     */   private final BlockRenderDispatcher blockRenderer;
/*     */   
/*     */   public ItemFrameRenderer(EntityRendererProvider.Context context) {
/*  40 */     super(context);
/*  41 */     this.itemModelResolver = context.getItemModelResolver();
/*  42 */     this.mapRenderer = context.getMapRenderer();
/*  43 */     this.blockRenderer = context.getBlockRenderDispatcher();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/*  48 */     if (entity.getType() == EntityType.GLOW_ITEM_FRAME) {
/*  49 */       return Math.max(5, super.getBlockLightLevel(entity, blockPos));
/*     */     }
/*  51 */     return super.getBlockLightLevel(entity, blockPos);
/*     */   }
/*     */   
/*     */   public void submit(ItemFrameRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*     */     float xRot, yRot;
/*  56 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*  57 */     poseStack.pushPose();
/*     */     
/*  59 */     Direction direction = state.direction;
/*     */     
/*  61 */     Vec3 renderOffset = getRenderOffset(state);
/*     */     
/*  63 */     poseStack.translate(-renderOffset.x(), -renderOffset.y(), -renderOffset.z());
/*     */     
/*  65 */     double offs = 0.46875D;
/*  66 */     poseStack.translate(direction.getStepX() * 0.46875D, direction.getStepY() * 0.46875D, direction.getStepZ() * 0.46875D);
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (direction.getAxis().isHorizontal()) {
/*  71 */       xRot = 0.0F;
/*  72 */       yRot = 180.0F - direction.toYRot();
/*     */     } else {
/*  74 */       xRot = (-90 * direction.getAxisDirection().getStep());
/*  75 */       yRot = 180.0F;
/*     */     } 
/*  77 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xRot));
/*  78 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot));
/*     */     
/*  80 */     if (!state.isInvisible) {
/*  81 */       BlockState fakeBlockState = BlockStateDefinitions.getItemFrameFakeState(state.isGlowFrame, (state.mapId != null));
/*  82 */       BlockStateModel blockModel = this.blockRenderer.getBlockModel(fakeBlockState);
/*     */       
/*  84 */       poseStack.pushPose();
/*  85 */       poseStack.translate(-0.5F, -0.5F, -0.5F);
/*  86 */       submitNodeCollector.submitBlockModel(poseStack, RenderTypes.entitySolidZOffsetForward(TextureAtlas.LOCATION_BLOCKS), blockModel, 1.0F, 1.0F, 1.0F, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*  87 */       poseStack.popPose();
/*     */     } 
/*     */     
/*  90 */     if (state.isInvisible) {
/*  91 */       poseStack.translate(0.0F, 0.0F, 0.5F);
/*     */     } else {
/*  93 */       poseStack.translate(0.0F, 0.0F, 0.4375F);
/*     */     } 
/*     */     
/*  96 */     if (state.mapId != null) {
/*  97 */       int rotation = state.rotation % 4 * 2;
/*  98 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(rotation * 360.0F / 8.0F));
/*     */       
/* 100 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/* 101 */       float s = 0.0078125F;
/* 102 */       poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
/* 103 */       poseStack.translate(-64.0F, -64.0F, 0.0F);
/* 104 */       poseStack.translate(0.0F, 0.0F, -1.0F);
/* 105 */       int lightCoords = getLightCoords(state.isGlowFrame, 15728850, state.lightCoords);
/* 106 */       this.mapRenderer.render(state.mapRenderState, poseStack, submitNodeCollector, true, lightCoords);
/* 107 */     } else if (!state.item.isEmpty()) {
/* 108 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.rotation * 360.0F / 8.0F));
/*     */       
/* 110 */       int lightVal = getLightCoords(state.isGlowFrame, 15728880, state.lightCoords);
/*     */       
/* 112 */       poseStack.scale(0.5F, 0.5F, 0.5F);
/* 113 */       state.item.submit(poseStack, submitNodeCollector, lightVal, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */     } 
/*     */     
/* 116 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private int getLightCoords(boolean isGlowFrame, int glowLightCoords, int originalLightCoords) {
/* 120 */     return isGlowFrame ? glowLightCoords : originalLightCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRenderOffset(ItemFrameRenderState state) {
/* 125 */     return new Vec3((state.direction.getStepX() * 0.3F), -0.25D, (state.direction.getStepZ() * 0.3F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldShowName(T entity, double distanceToCameraSq) {
/* 130 */     return (Minecraft.renderNames() && this.entityRenderDispatcher.crosshairPickEntity == entity && entity.getItem().getCustomName() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getNameTag(T entity) {
/* 135 */     return entity.getItem().getHoverName();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemFrameRenderState createRenderState() {
/* 140 */     return new ItemFrameRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(T entity, ItemFrameRenderState state, float partialTicks) {
/* 145 */     super.extractRenderState(entity, state, partialTicks);
/* 146 */     state.direction = entity.getDirection();
/* 147 */     ItemStack itemStack = entity.getItem();
/* 148 */     this.itemModelResolver.updateForNonLiving(state.item, itemStack, ItemDisplayContext.FIXED, (Entity)entity);
/* 149 */     state.rotation = entity.getRotation();
/* 150 */     state.isGlowFrame = (entity.getType() == EntityType.GLOW_ITEM_FRAME);
/* 151 */     state.mapId = null;
/* 152 */     if (!itemStack.isEmpty()) {
/* 153 */       MapId framedMapId = entity.getFramedMapId(itemStack);
/* 154 */       if (framedMapId != null) {
/* 155 */         MapItemSavedData mapData = entity.level().getMapData(framedMapId);
/* 156 */         if (mapData != null) {
/* 157 */           this.mapRenderer.extractRenderState(framedMapId, mapData, state.mapRenderState);
/* 158 */           state.mapId = framedMapId;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ItemFrameRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */