/*     */ package net.minecraft.client.model.monster.spider;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class SpiderModel
/*     */   extends EntityModel<LivingEntityRenderState>
/*     */ {
/*     */   private static final String BODY_0 = "body0";
/*     */   private static final String BODY_1 = "body1";
/*     */   private static final String RIGHT_MIDDLE_FRONT_LEG = "right_middle_front_leg";
/*     */   private static final String LEFT_MIDDLE_FRONT_LEG = "left_middle_front_leg";
/*     */   private static final String RIGHT_MIDDLE_HIND_LEG = "right_middle_hind_leg";
/*     */   private static final String LEFT_MIDDLE_HIND_LEG = "left_middle_hind_leg";
/*     */   private final ModelPart head;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightMiddleHindLeg;
/*     */   private final ModelPart leftMiddleHindLeg;
/*     */   private final ModelPart rightMiddleFrontLeg;
/*     */   private final ModelPart leftMiddleFrontLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   
/*     */   public SpiderModel(ModelPart root) {
/*  33 */     super(root);
/*  34 */     this.head = root.getChild("head");
/*  35 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  36 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  37 */     this.rightMiddleHindLeg = root.getChild("right_middle_hind_leg");
/*  38 */     this.leftMiddleHindLeg = root.getChild("left_middle_hind_leg");
/*  39 */     this.rightMiddleFrontLeg = root.getChild("right_middle_front_leg");
/*  40 */     this.leftMiddleFrontLeg = root.getChild("left_middle_front_leg");
/*  41 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  42 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createSpiderBodyLayer() {
/*  46 */     MeshDefinition mesh = new MeshDefinition();
/*  47 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  49 */     int yo = 15;
/*  50 */     root.addOrReplaceChild("head", 
/*  51 */         CubeListBuilder.create()
/*  52 */         .texOffs(32, 4).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F), 
/*  53 */         PartPose.offset(0.0F, 15.0F, -3.0F));
/*     */     
/*  55 */     root.addOrReplaceChild("body0", 
/*  56 */         CubeListBuilder.create()
/*  57 */         .texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), 
/*  58 */         PartPose.offset(0.0F, 15.0F, 0.0F));
/*     */     
/*  60 */     root.addOrReplaceChild("body1", 
/*  61 */         CubeListBuilder.create()
/*  62 */         .texOffs(0, 12).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F), 
/*  63 */         PartPose.offset(0.0F, 15.0F, 9.0F));
/*     */     
/*  65 */     CubeListBuilder rightLeg = CubeListBuilder.create()
/*  66 */       .texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F);
/*  67 */     CubeListBuilder leftLeg = CubeListBuilder.create()
/*  68 */       .texOffs(18, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F);
/*     */     
/*  70 */     float legZRot = 0.7853982F;
/*  71 */     float legYRotSpan = 0.3926991F;
/*     */     
/*  73 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, 2.0F, 0.0F, 0.7853982F, -0.7853982F));
/*  74 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, 2.0F, 0.0F, -0.7853982F, 0.7853982F));
/*  75 */     root.addOrReplaceChild("right_middle_hind_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, 1.0F, 0.0F, 0.3926991F, -0.58119464F));
/*  76 */     root.addOrReplaceChild("left_middle_hind_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, 1.0F, 0.0F, -0.3926991F, 0.58119464F));
/*  77 */     root.addOrReplaceChild("right_middle_front_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, 0.0F, 0.0F, -0.3926991F, -0.58119464F));
/*  78 */     root.addOrReplaceChild("left_middle_front_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, 0.0F, 0.0F, 0.3926991F, 0.58119464F));
/*  79 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, -1.0F, 0.0F, -0.7853982F, -0.7853982F));
/*  80 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, -1.0F, 0.0F, 0.7853982F, 0.7853982F));
/*     */     
/*  82 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(LivingEntityRenderState state) {
/*  87 */     super.setupAnim(state);
/*     */     
/*  89 */     this.head.yRot = state.yRot * 0.017453292F;
/*  90 */     this.head.xRot = state.xRot * 0.017453292F;
/*     */     
/*  92 */     float animationPos = state.walkAnimationPos * 0.6662F;
/*  93 */     float animationSpeed = state.walkAnimationSpeed;
/*  94 */     float swingHind = -(Mth.cos((animationPos * 2.0F + 0.0F)) * 0.4F) * animationSpeed;
/*  95 */     float swingMiddleHind = -(Mth.cos((animationPos * 2.0F + 3.1415927F)) * 0.4F) * animationSpeed;
/*  96 */     float swingMiddleFront = -(Mth.cos((animationPos * 2.0F + 1.5707964F)) * 0.4F) * animationSpeed;
/*  97 */     float swingFront = -(Mth.cos((animationPos * 2.0F + 4.712389F)) * 0.4F) * animationSpeed;
/*     */     
/*  99 */     float stepHind = Math.abs(Mth.sin((animationPos + 0.0F)) * 0.4F) * animationSpeed;
/* 100 */     float stepMiddleHind = Math.abs(Mth.sin((animationPos + 3.1415927F)) * 0.4F) * animationSpeed;
/* 101 */     float stepMiddleFrontHind = Math.abs(Mth.sin((animationPos + 1.5707964F)) * 0.4F) * animationSpeed;
/* 102 */     float stepFront = Math.abs(Mth.sin((animationPos + 4.712389F)) * 0.4F) * animationSpeed;
/*     */     
/* 104 */     this.rightHindLeg.yRot += swingHind;
/* 105 */     this.leftHindLeg.yRot -= swingHind;
/* 106 */     this.rightMiddleHindLeg.yRot += swingMiddleHind;
/* 107 */     this.leftMiddleHindLeg.yRot -= swingMiddleHind;
/* 108 */     this.rightMiddleFrontLeg.yRot += swingMiddleFront;
/* 109 */     this.leftMiddleFrontLeg.yRot -= swingMiddleFront;
/* 110 */     this.rightFrontLeg.yRot += swingFront;
/* 111 */     this.leftFrontLeg.yRot -= swingFront;
/*     */     
/* 113 */     this.rightHindLeg.zRot += stepHind;
/* 114 */     this.leftHindLeg.zRot -= stepHind;
/* 115 */     this.rightMiddleHindLeg.zRot += stepMiddleHind;
/* 116 */     this.leftMiddleHindLeg.zRot -= stepMiddleHind;
/* 117 */     this.rightMiddleFrontLeg.zRot += stepMiddleFrontHind;
/* 118 */     this.leftMiddleFrontLeg.zRot -= stepMiddleFrontHind;
/* 119 */     this.rightFrontLeg.zRot += stepFront;
/* 120 */     this.leftFrontLeg.zRot -= stepFront;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/spider/SpiderModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */