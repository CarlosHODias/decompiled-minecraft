/*     */ package net.minecraft.client.model.animal.allay;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.model.ArmedModel;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.AllayRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class AllayModel extends EntityModel<AllayRenderState> implements ArmedModel<AllayRenderState> {
/*     */   private final ModelPart head;
/*     */   private final ModelPart body;
/*     */   private final ModelPart right_arm;
/*     */   private final ModelPart left_arm;
/*     */   private final ModelPart right_wing;
/*     */   private final ModelPart left_wing;
/*     */   private static final float FLYING_ANIMATION_X_ROT = 0.7853982F;
/*     */   private static final float MAX_HAND_HOLDING_ITEM_X_ROT_RAD = -1.134464F;
/*     */   private static final float MIN_HAND_HOLDING_ITEM_X_ROT_RAD = -1.0471976F;
/*     */   
/*     */   public AllayModel(ModelPart root) {
/*  33 */     super(root.getChild("root"), RenderTypes::entityTranslucent);
/*  34 */     this.head = this.root.getChild("head");
/*  35 */     this.body = this.root.getChild("body");
/*  36 */     this.right_arm = this.body.getChild("right_arm");
/*  37 */     this.left_arm = this.body.getChild("left_arm");
/*  38 */     this.right_wing = this.body.getChild("right_wing");
/*  39 */     this.left_wing = this.body.getChild("left_wing");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  43 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  44 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*     */     
/*  46 */     PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, 0.0F));
/*  47 */     root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.99F, 0.0F));
/*  48 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
/*  49 */         .texOffs(0, 16).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -4.0F, 0.0F));
/*  50 */     body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(-1.75F, 0.5F, 0.0F));
/*  51 */     body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.75F, 0.5F, 0.0F));
/*  52 */     body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.6F));
/*  53 */     body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.6F));
/*     */     
/*  55 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(AllayRenderState state) {
/*  60 */     super.setupAnim(state);
/*     */     
/*  62 */     float animationSpeed = state.walkAnimationSpeed;
/*  63 */     float animationPos = state.walkAnimationPos;
/*  64 */     float flapSpeed = state.ageInTicks * 20.0F * 0.017453292F + animationPos;
/*  65 */     float flapAmount = Mth.cos(flapSpeed) * 3.1415927F * 0.15F + animationSpeed;
/*  66 */     float idleBobSpeed = state.ageInTicks * 9.0F * 0.017453292F;
/*  67 */     float flyingFactor = Math.min(animationSpeed / 0.3F, 1.0F);
/*  68 */     float idleBobFactor = 1.0F - flyingFactor;
/*  69 */     float holdingItemFactor = state.holdingAnimationProgress;
/*     */     
/*  71 */     if (state.isDancing) {
/*  72 */       float danceSpeed = state.ageInTicks * 8.0F * 0.017453292F + animationSpeed;
/*  73 */       float danceFrequency = Mth.cos(danceSpeed) * 16.0F * 0.017453292F;
/*  74 */       float spinningRotation = state.spinningProgress;
/*  75 */       float headTiltZ = Mth.cos(danceSpeed) * 14.0F * 0.017453292F;
/*  76 */       float headTiltY = Mth.cos(danceSpeed) * 30.0F * 0.017453292F;
/*  77 */       this.root.yRot = state.isSpinning ? (12.566371F * spinningRotation) : this.root.yRot;
/*  78 */       this.root.zRot = danceFrequency * (1.0F - spinningRotation);
/*  79 */       this.head.yRot = headTiltY * (1.0F - spinningRotation);
/*  80 */       this.head.zRot = headTiltZ * (1.0F - spinningRotation);
/*     */     } else {
/*  82 */       this.head.xRot = state.xRot * 0.017453292F;
/*  83 */       this.head.yRot = state.yRot * 0.017453292F;
/*     */     } 
/*     */     
/*  86 */     this.right_wing.xRot = 0.43633232F * (1.0F - flyingFactor);
/*  87 */     this.right_wing.yRot = -0.7853982F + flapAmount;
/*  88 */     this.left_wing.xRot = 0.43633232F * (1.0F - flyingFactor);
/*  89 */     this.left_wing.yRot = 0.7853982F - flapAmount;
/*  90 */     this.body.xRot = flyingFactor * 0.7853982F;
/*  91 */     float armFlyingRotX = holdingItemFactor * Mth.lerp(flyingFactor, -1.0471976F, -1.134464F);
/*  92 */     this.root.y += (float)Math.cos(idleBobSpeed) * 0.25F * idleBobFactor;
/*  93 */     this.right_arm.xRot = armFlyingRotX;
/*  94 */     this.left_arm.xRot = armFlyingRotX;
/*  95 */     float armIdleBobFactor = idleBobFactor * (1.0F - holdingItemFactor);
/*  96 */     float armIdleBobAmount = 0.43633232F - Mth.cos((idleBobSpeed + 4.712389F)) * 3.1415927F * 0.075F * armIdleBobFactor;
/*  97 */     this.left_arm.zRot = -armIdleBobAmount;
/*  98 */     this.right_arm.zRot = armIdleBobAmount;
/*  99 */     this.right_arm.yRot = 0.27925268F * holdingItemFactor;
/* 100 */     this.left_arm.yRot = -0.27925268F * holdingItemFactor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(AllayRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 105 */     float yOffset = 1.0F;
/* 106 */     float zOffset = 3.0F;
/* 107 */     this.root.translateAndRotate(poseStack);
/* 108 */     this.body.translateAndRotate(poseStack);
/* 109 */     poseStack.translate(0.0F, 0.0625F, 0.1875F);
/* 110 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(this.right_arm.xRot));
/* 111 */     poseStack.scale(0.7F, 0.7F, 0.7F);
/* 112 */     poseStack.translate(0.0625F, 0.0F, 0.0F);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/allay/AllayModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */