/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.armorstand.ArmorStandArmorModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*     */ import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ArmorStandRenderer extends LivingEntityRenderer<ArmorStand, ArmorStandRenderState, ArmorStandArmorModel> {
/*  23 */   public static final Identifier DEFAULT_SKIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/armorstand/wood.png");
/*     */   
/*     */   private final ArmorStandArmorModel bigModel;
/*     */   private final ArmorStandArmorModel smallModel;
/*     */   
/*     */   public ArmorStandRenderer(EntityRendererProvider.Context context) {
/*  29 */     super(context, (ArmorStandArmorModel)new net.minecraft.client.model.object.armorstand.ArmorStandModel(context.bakeLayer(ModelLayers.ARMOR_STAND)), 0.0F);
/*  30 */     this.bigModel = getModel();
/*  31 */     this.smallModel = (ArmorStandArmorModel)new net.minecraft.client.model.object.armorstand.ArmorStandModel(context.bakeLayer(ModelLayers.ARMOR_STAND_SMALL));
/*  32 */     addLayer((RenderLayer<ArmorStandRenderState, ArmorStandArmorModel>)new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer(this, 
/*  33 */           ArmorModelSet.bake(ModelLayers.ARMOR_STAND_ARMOR, context.getModelSet(), ArmorStandArmorModel::new), 
/*  34 */           ArmorModelSet.bake(ModelLayers.ARMOR_STAND_SMALL_ARMOR, context.getModelSet(), ArmorStandArmorModel::new), 
/*  35 */           context.getEquipmentRenderer()));
/*     */     
/*  37 */     addLayer((RenderLayer<ArmorStandRenderState, ArmorStandArmorModel>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer(this));
/*  38 */     addLayer((RenderLayer<ArmorStandRenderState, ArmorStandArmorModel>)new net.minecraft.client.renderer.entity.layers.WingsLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
/*  39 */     addLayer((RenderLayer<ArmorStandRenderState, ArmorStandArmorModel>)new net.minecraft.client.renderer.entity.layers.CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getTextureLocation(ArmorStandRenderState state) {
/*  44 */     return DEFAULT_SKIN_LOCATION;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStandRenderState createRenderState() {
/*  49 */     return new ArmorStandRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(ArmorStand entity, ArmorStandRenderState state, float partialTicks) {
/*  54 */     super.extractRenderState(entity, state, partialTicks);
/*  55 */     HumanoidMobRenderer.extractHumanoidRenderState((LivingEntity)entity, (net.minecraft.client.renderer.entity.state.HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/*  56 */     state.yRot = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
/*  57 */     state.isMarker = entity.isMarker();
/*  58 */     state.isSmall = entity.isSmall();
/*  59 */     state.showArms = entity.showArms();
/*  60 */     state.showBasePlate = entity.showBasePlate();
/*  61 */     state.bodyPose = entity.getBodyPose();
/*  62 */     state.headPose = entity.getHeadPose();
/*  63 */     state.leftArmPose = entity.getLeftArmPose();
/*  64 */     state.rightArmPose = entity.getRightArmPose();
/*  65 */     state.leftLegPose = entity.getLeftLegPose();
/*  66 */     state.rightLegPose = entity.getRightLegPose();
/*  67 */     state.wiggle = (float)(entity.level().getGameTime() - entity.lastHit) + partialTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(ArmorStandRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  72 */     this.model = state.isSmall ? this.smallModel : this.bigModel;
/*  73 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupRotations(ArmorStandRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/*  78 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - bodyRot));
/*     */     
/*  80 */     if (state.wiggle < 5.0F) {
/*  81 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(Mth.sin((state.wiggle / 1.5F * 3.1415927F)) * 3.0F));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldShowName(ArmorStand entity, double distanceToCameraSq) {
/*  87 */     return entity.isCustomNameVisible();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderType getRenderType(ArmorStandRenderState state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
/*  92 */     if (!state.isMarker) {
/*  93 */       return super.getRenderType(state, isBodyVisible, forceTransparent, appearGlowing);
/*     */     }
/*     */     
/*  96 */     Identifier texture = getTextureLocation(state);
/*  97 */     if (forceTransparent) {
/*  98 */       return RenderTypes.entityTranslucent(texture, false);
/*     */     }
/* 100 */     if (isBodyVisible) {
/* 101 */       return RenderTypes.entityCutoutNoCull(texture, false);
/*     */     }
/* 103 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ArmorStandRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */