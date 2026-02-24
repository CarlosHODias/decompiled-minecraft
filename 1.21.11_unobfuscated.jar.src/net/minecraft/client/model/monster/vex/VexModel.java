/*     */ package net.minecraft.client.model.monster.vex;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.ArmedModel;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.VexRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ 
/*     */ public class VexModel
/*     */   extends EntityModel<VexRenderState> implements ArmedModel<VexRenderState> {
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightArm;
/*     */   private final ModelPart leftArm;
/*     */   private final ModelPart rightWing;
/*     */   private final ModelPart leftWing;
/*     */   private final ModelPart head;
/*     */   
/*     */   public VexModel(ModelPart root) {
/*  29 */     super(root.getChild("root"), RenderTypes::entityTranslucent);
/*  30 */     this.body = this.root.getChild("body");
/*  31 */     this.rightArm = this.body.getChild("right_arm");
/*  32 */     this.leftArm = this.body.getChild("left_arm");
/*  33 */     this.rightWing = this.body.getChild("right_wing");
/*  34 */     this.leftWing = this.body.getChild("left_wing");
/*  35 */     this.head = this.root.getChild("head");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  39 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  40 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*     */     
/*  42 */     PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));
/*  43 */     root.addOrReplaceChild("head", CubeListBuilder.create()
/*  44 */         .texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), 
/*  45 */         PartPose.offset(0.0F, 20.0F, 0.0F));
/*     */     
/*  47 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
/*  48 */         .texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
/*  49 */         .texOffs(0, 16).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), 
/*  50 */         PartPose.offset(0.0F, 20.0F, 0.0F));
/*     */     
/*  52 */     body.addOrReplaceChild("right_arm", CubeListBuilder.create()
/*  53 */         .texOffs(23, 0).addBox(-1.25F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), 
/*  54 */         PartPose.offset(-1.75F, 0.25F, 0.0F));
/*  55 */     body.addOrReplaceChild("left_arm", CubeListBuilder.create()
/*  56 */         .texOffs(23, 6).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), 
/*  57 */         PartPose.offset(1.75F, 0.25F, 0.0F));
/*     */     
/*  59 */     body.addOrReplaceChild("left_wing", CubeListBuilder.create()
/*  60 */         .texOffs(16, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), 
/*  61 */         PartPose.offset(0.5F, 1.0F, 1.0F));
/*  62 */     body.addOrReplaceChild("right_wing", CubeListBuilder.create()
/*  63 */         .texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), 
/*  64 */         PartPose.offset(-0.5F, 1.0F, 1.0F));
/*     */     
/*  66 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(VexRenderState state) {
/*  71 */     super.setupAnim(state);
/*  72 */     this.head.yRot = state.yRot * 0.017453292F;
/*  73 */     this.head.xRot = state.xRot * 0.017453292F;
/*  74 */     float movingArmZBob = Mth.cos((state.ageInTicks * 5.5F * 0.017453292F)) * 0.1F;
/*     */     
/*  76 */     this.rightArm.zRot = 0.62831855F + movingArmZBob;
/*  77 */     this.leftArm.zRot = -(0.62831855F + movingArmZBob);
/*     */     
/*  79 */     if (state.isCharging) {
/*  80 */       this.body.xRot = 0.0F;
/*  81 */       setArmsCharging(!state.rightHandItemState.isEmpty(), !state.leftHandItemState.isEmpty(), movingArmZBob);
/*     */     } else {
/*  83 */       this.body.xRot = 0.15707964F;
/*     */     } 
/*  85 */     this.leftWing.yRot = 1.0995574F + Mth.cos((state.ageInTicks * 45.836624F * 0.017453292F)) * 0.017453292F * 16.2F;
/*  86 */     this.rightWing.yRot = -this.leftWing.yRot;
/*  87 */     this.leftWing.xRot = 0.47123888F;
/*  88 */     this.leftWing.zRot = -0.47123888F;
/*  89 */     this.rightWing.xRot = 0.47123888F;
/*  90 */     this.rightWing.zRot = 0.47123888F;
/*     */   }
/*     */   
/*     */   private void setArmsCharging(boolean hasItemInRightHand, boolean hasItemInLeftHand, float movingArmZBob) {
/*  94 */     if (!hasItemInRightHand && !hasItemInLeftHand) {
/*  95 */       this.rightArm.xRot = -1.2217305F;
/*  96 */       this.rightArm.yRot = 0.2617994F;
/*  97 */       this.rightArm.zRot = -0.47123888F - movingArmZBob;
/*  98 */       this.leftArm.xRot = -1.2217305F;
/*  99 */       this.leftArm.yRot = -0.2617994F;
/* 100 */       this.leftArm.zRot = 0.47123888F + movingArmZBob;
/*     */       
/*     */       return;
/*     */     } 
/* 104 */     if (hasItemInRightHand) {
/* 105 */       this.rightArm.xRot = 3.6651914F;
/* 106 */       this.rightArm.yRot = 0.2617994F;
/* 107 */       this.rightArm.zRot = -0.47123888F - movingArmZBob;
/*     */     } 
/* 109 */     if (hasItemInLeftHand) {
/* 110 */       this.leftArm.xRot = 3.6651914F;
/* 111 */       this.leftArm.yRot = -0.2617994F;
/* 112 */       this.leftArm.zRot = 0.47123888F + movingArmZBob;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(VexRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 118 */     boolean mainArm = (arm == HumanoidArm.RIGHT);
/* 119 */     ModelPart activeArm = mainArm ? this.rightArm : this.leftArm;
/* 120 */     this.root.translateAndRotate(poseStack);
/* 121 */     this.body.translateAndRotate(poseStack);
/* 122 */     activeArm.translateAndRotate(poseStack);
/* 123 */     poseStack.scale(0.55F, 0.55F, 0.55F);
/* 124 */     offsetStackPosition(poseStack, mainArm);
/*     */   }
/*     */   
/*     */   private void offsetStackPosition(PoseStack poseStack, boolean mainArm) {
/* 128 */     if (mainArm) {
/* 129 */       poseStack.translate(0.046875D, -0.15625D, 0.078125D);
/*     */     } else {
/* 131 */       poseStack.translate(-0.046875D, -0.15625D, 0.078125D);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/vex/VexModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */