/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.skull.PiglinHeadModel;
/*     */ import net.minecraft.client.model.object.skull.SkullModelBase;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.SkullBlockRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.block.AbstractSkullBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.WallSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SkullBlockRenderer implements BlockEntityRenderer<SkullBlockEntity, SkullBlockRenderState> {
/*     */   private final java.util.function.Function<SkullBlock.Type, SkullModelBase> modelByType;
/*     */   private static final Map<SkullBlock.Type, Identifier> SKIN_BY_TYPE;
/*     */   private final net.minecraft.client.renderer.PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*     */   static {
/*  40 */     SKIN_BY_TYPE = (Map<SkullBlock.Type, Identifier>)Util.make(Maps.newHashMap(), map -> {
/*     */           map.put(SkullBlock.Types.SKELETON, Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png"));
/*     */           map.put(SkullBlock.Types.WITHER_SKELETON, Identifier.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png"));
/*     */           map.put(SkullBlock.Types.ZOMBIE, Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png"));
/*     */           map.put(SkullBlock.Types.CREEPER, Identifier.withDefaultNamespace("textures/entity/creeper/creeper.png"));
/*     */           map.put(SkullBlock.Types.DRAGON, Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png"));
/*     */           map.put(SkullBlock.Types.PIGLIN, Identifier.withDefaultNamespace("textures/entity/piglin/piglin.png"));
/*     */           map.put(SkullBlock.Types.PLAYER, DefaultPlayerSkin.getDefaultTexture());
/*     */         });
/*     */   }
/*     */   public static SkullModelBase createModel(EntityModelSet modelSet, SkullBlock.Type type) {
/*  51 */     if (type instanceof SkullBlock.Types) { SkullBlock.Types vanillaType = (SkullBlock.Types)type;
/*  52 */       switch (vanillaType) { default: throw new MatchException(null, null);case SKELETON: case WITHER_SKELETON: case PLAYER: case ZOMBIE: case CREEPER: case DRAGON: case PIGLIN: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  59 */         (SkullModelBase)new PiglinHeadModel(modelSet.bakeLayer(ModelLayers.PIGLIN_HEAD)); }
/*     */ 
/*     */ 
/*     */     
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SkullBlockRenderer(BlockEntityRendererProvider.Context context) {
/*  69 */     EntityModelSet modelSet = context.entityModelSet();
/*  70 */     this.playerSkinRenderCache = context.playerSkinRenderCache();
/*  71 */     this.modelByType = Util.memoize(type -> createModel(modelSet, type));
/*     */   }
/*     */ 
/*     */   
/*     */   public SkullBlockRenderState createRenderState() {
/*  76 */     return new SkullBlockRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(SkullBlockEntity blockEntity, SkullBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  81 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  82 */     state.animationProgress = blockEntity.getAnimation(partialTicks);
/*  83 */     BlockState blockState = blockEntity.getBlockState();
/*  84 */     boolean isWallSkull = blockState.getBlock() instanceof WallSkullBlock;
/*  85 */     state.direction = isWallSkull ? (Direction)blockState.getValue((Property)WallSkullBlock.FACING) : null;
/*  86 */     int rotationSegment = isWallSkull ? RotationSegment.convertToSegment(state.direction.getOpposite()) : (Integer)blockState.getValue((Property)SkullBlock.ROTATION);
/*  87 */     state.rotationDegrees = RotationSegment.convertToDegrees(rotationSegment);
/*  88 */     state.skullType = ((AbstractSkullBlock)blockState.getBlock()).getType();
/*  89 */     state.renderType = resolveSkullRenderType(state.skullType, blockEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(SkullBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  94 */     SkullModelBase model = this.modelByType.apply(state.skullType);
/*  95 */     submitSkull(state.direction, state.rotationDegrees, state.animationProgress, poseStack, submitNodeCollector, state.lightCoords, model, state.renderType, 0, state.breakProgress);
/*     */   }
/*     */   
/*     */   public static void submitSkull(Direction direction, float rot, float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  99 */     poseStack.pushPose();
/*     */     
/* 101 */     if (direction == null) {
/* 102 */       poseStack.translate(0.5F, 0.0F, 0.5F);
/*     */     } else {
/* 104 */       float offset = 0.25F;
/* 105 */       poseStack.translate(0.5F - 
/* 106 */           direction.getStepX() * 0.25F, 0.25F, 0.5F - 
/*     */           
/* 108 */           direction.getStepZ() * 0.25F);
/*     */     } 
/*     */ 
/*     */     
/* 112 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*     */     
/* 114 */     SkullModelBase.State modelState = new SkullModelBase.State();
/* 115 */     modelState.animationPos = animationValue;
/* 116 */     modelState.yRot = rot;
/* 117 */     submitNodeCollector.submitModel((Model)model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, breakProgress);
/*     */     
/* 119 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private RenderType resolveSkullRenderType(SkullBlock.Type type, SkullBlockEntity entity) {
/* 123 */     if (type == SkullBlock.Types.PLAYER) {
/* 124 */       ResolvableProfile ownerProfile = entity.getOwnerProfile();
/* 125 */       if (ownerProfile != null) {
/* 126 */         return this.playerSkinRenderCache.getOrDefault(ownerProfile).renderType();
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return getSkullRenderType(type, null);
/*     */   }
/*     */   
/*     */   public static RenderType getSkullRenderType(SkullBlock.Type type, Identifier texture) {
/* 134 */     return RenderTypes.entityCutoutNoCullZOffset((texture != null) ? texture : SKIN_BY_TYPE.get(type));
/*     */   }
/*     */   
/*     */   public static RenderType getPlayerSkinRenderType(Identifier texture) {
/* 138 */     return RenderTypes.entityTranslucent(texture);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/SkullBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */