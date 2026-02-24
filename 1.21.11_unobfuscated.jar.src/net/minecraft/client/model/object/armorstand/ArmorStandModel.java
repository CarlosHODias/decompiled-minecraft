/*     */ package net.minecraft.client.model.object.armorstand;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ 
/*     */ public class ArmorStandModel
/*     */   extends ArmorStandArmorModel {
/*     */   private static final String RIGHT_BODY_STICK = "right_body_stick";
/*     */   private static final String LEFT_BODY_STICK = "left_body_stick";
/*     */   private static final String SHOULDER_STICK = "shoulder_stick";
/*     */   private static final String BASE_PLATE = "base_plate";
/*     */   private final ModelPart rightBodyStick;
/*     */   private final ModelPart leftBodyStick;
/*     */   private final ModelPart shoulderStick;
/*     */   private final ModelPart basePlate;
/*     */   
/*     */   public ArmorStandModel(ModelPart root) {
/*  29 */     super(root);
/*  30 */     this.rightBodyStick = root.getChild("right_body_stick");
/*  31 */     this.leftBodyStick = root.getChild("left_body_stick");
/*  32 */     this.shoulderStick = root.getChild("shoulder_stick");
/*  33 */     this.basePlate = root.getChild("base_plate");
/*     */     
/*  35 */     this.hat.visible = false;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  39 */     MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
/*  40 */     PartDefinition root = mesh.getRoot();
/*  41 */     root.addOrReplaceChild("head", 
/*  42 */         CubeListBuilder.create()
/*  43 */         .texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F), 
/*  44 */         PartPose.offset(0.0F, 1.0F, 0.0F));
/*     */     
/*  46 */     root.addOrReplaceChild("body", 
/*  47 */         CubeListBuilder.create()
/*  48 */         .texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 3.0F, 3.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  51 */     root.addOrReplaceChild("right_arm", 
/*  52 */         CubeListBuilder.create()
/*  53 */         .texOffs(24, 0).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), 
/*  54 */         PartPose.offset(-5.0F, 2.0F, 0.0F));
/*     */     
/*  56 */     root.addOrReplaceChild("left_arm", 
/*  57 */         CubeListBuilder.create()
/*  58 */         .texOffs(32, 16).mirror().addBox(0.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), 
/*  59 */         PartPose.offset(5.0F, 2.0F, 0.0F));
/*     */     
/*  61 */     root.addOrReplaceChild("right_leg", 
/*  62 */         CubeListBuilder.create()
/*  63 */         .texOffs(8, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), 
/*  64 */         PartPose.offset(-1.9F, 12.0F, 0.0F));
/*     */     
/*  66 */     root.addOrReplaceChild("left_leg", 
/*  67 */         CubeListBuilder.create()
/*  68 */         .texOffs(40, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), 
/*  69 */         PartPose.offset(1.9F, 12.0F, 0.0F));
/*     */     
/*  71 */     root.addOrReplaceChild("right_body_stick", 
/*  72 */         CubeListBuilder.create()
/*  73 */         .texOffs(16, 0).addBox(-3.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  76 */     root.addOrReplaceChild("left_body_stick", 
/*  77 */         CubeListBuilder.create()
/*  78 */         .texOffs(48, 16).addBox(1.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  81 */     root.addOrReplaceChild("shoulder_stick", 
/*  82 */         CubeListBuilder.create()
/*  83 */         .texOffs(0, 48).addBox(-4.0F, 10.0F, -1.0F, 8.0F, 2.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  86 */     root.addOrReplaceChild("base_plate", 
/*  87 */         CubeListBuilder.create()
/*  88 */         .texOffs(0, 32).addBox(-6.0F, 11.0F, -6.0F, 12.0F, 1.0F, 12.0F), 
/*  89 */         PartPose.offset(0.0F, 12.0F, 0.0F));
/*     */ 
/*     */     
/*  92 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(ArmorStandRenderState state) {
/*  97 */     super.setupAnim(state);
/*     */     
/*  99 */     this.basePlate.yRot = 0.017453292F * -state.yRot;
/*     */     
/* 101 */     this.leftArm.visible = state.showArms;
/* 102 */     this.rightArm.visible = state.showArms;
/* 103 */     this.basePlate.visible = state.showBasePlate;
/*     */     
/* 105 */     this.rightBodyStick.xRot = 0.017453292F * state.bodyPose.x();
/* 106 */     this.rightBodyStick.yRot = 0.017453292F * state.bodyPose.y();
/* 107 */     this.rightBodyStick.zRot = 0.017453292F * state.bodyPose.z();
/*     */     
/* 109 */     this.leftBodyStick.xRot = 0.017453292F * state.bodyPose.x();
/* 110 */     this.leftBodyStick.yRot = 0.017453292F * state.bodyPose.y();
/* 111 */     this.leftBodyStick.zRot = 0.017453292F * state.bodyPose.z();
/*     */     
/* 113 */     this.shoulderStick.xRot = 0.017453292F * state.bodyPose.x();
/* 114 */     this.shoulderStick.yRot = 0.017453292F * state.bodyPose.y();
/* 115 */     this.shoulderStick.zRot = 0.017453292F * state.bodyPose.z();
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(ArmorStandRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 120 */     ModelPart modelPart = getArm(arm);
/* 121 */     boolean handVisible = modelPart.visible;
/* 122 */     modelPart.visible = true;
/* 123 */     translateToHand((HumanoidRenderState)state, arm, poseStack);
/* 124 */     modelPart.visible = handVisible;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/armorstand/ArmorStandModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */