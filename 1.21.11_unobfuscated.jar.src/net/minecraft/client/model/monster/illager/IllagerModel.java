/*     */ package net.minecraft.client.model.monster.illager;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.AnimationUtils;
/*     */ import net.minecraft.client.model.ArmedModel;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HeadedModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.IllagerRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ 
/*     */ public class IllagerModel<S extends IllagerRenderState>
/*     */   extends EntityModel<S>
/*     */   implements ArmedModel<S>, HeadedModel
/*     */ {
/*     */   private final ModelPart head;
/*     */   private final ModelPart hat;
/*     */   private final ModelPart arms;
/*     */   
/*     */   public IllagerModel(ModelPart root) {
/*  31 */     super(root);
/*  32 */     this.head = root.getChild("head");
/*  33 */     this.hat = this.head.getChild("hat");
/*  34 */     this.hat.visible = false;
/*  35 */     this.arms = root.getChild("arms");
/*  36 */     this.leftLeg = root.getChild("left_leg");
/*  37 */     this.rightLeg = root.getChild("right_leg");
/*  38 */     this.leftArm = root.getChild("left_arm");
/*  39 */     this.rightArm = root.getChild("right_arm");
/*     */   }
/*     */   private final ModelPart leftLeg; private final ModelPart rightLeg; private final ModelPart rightArm; private final ModelPart leftArm;
/*     */   public static LayerDefinition createBodyLayer() {
/*  43 */     MeshDefinition mesh = new MeshDefinition();
/*  44 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  46 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  47 */         CubeListBuilder.create()
/*  48 */         .texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), 
/*  49 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*     */     
/*  51 */     head.addOrReplaceChild("hat", 
/*  52 */         CubeListBuilder.create()
/*  53 */         .texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.45F)), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  57 */     head.addOrReplaceChild("nose", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), 
/*  60 */         PartPose.offset(0.0F, -2.0F, 0.0F));
/*     */     
/*  62 */     root.addOrReplaceChild("body", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F)
/*  65 */         .texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)), 
/*  66 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*     */     
/*  68 */     PartDefinition arms = root.addOrReplaceChild("arms", 
/*  69 */         CubeListBuilder.create()
/*  70 */         .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
/*  71 */         .texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F), 
/*  72 */         PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
/*     */     
/*  74 */     arms.addOrReplaceChild("left_shoulder", 
/*  75 */         CubeListBuilder.create()
/*  76 */         .texOffs(44, 22).mirror().addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  79 */     root.addOrReplaceChild("right_leg", 
/*  80 */         CubeListBuilder.create()
/*  81 */         .texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  82 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*     */     
/*  84 */     root.addOrReplaceChild("left_leg", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  87 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*     */     
/*  89 */     root.addOrReplaceChild("right_arm", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  92 */         PartPose.offset(-5.0F, 2.0F, 0.0F));
/*     */     
/*  94 */     root.addOrReplaceChild("left_arm", 
/*  95 */         CubeListBuilder.create()
/*  96 */         .texOffs(40, 46).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  97 */         PartPose.offset(5.0F, 2.0F, 0.0F));
/*     */ 
/*     */     
/* 100 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(S state) {
/* 105 */     super.setupAnim(state);
/*     */     
/* 107 */     this.head.yRot = ((IllagerRenderState)state).yRot * 0.017453292F;
/* 108 */     this.head.xRot = ((IllagerRenderState)state).xRot * 0.017453292F;
/*     */     
/* 110 */     if (((IllagerRenderState)state).isRiding) {
/* 111 */       this.rightArm.xRot = -0.62831855F;
/* 112 */       this.rightArm.yRot = 0.0F;
/* 113 */       this.rightArm.zRot = 0.0F;
/*     */       
/* 115 */       this.leftArm.xRot = -0.62831855F;
/* 116 */       this.leftArm.yRot = 0.0F;
/* 117 */       this.leftArm.zRot = 0.0F;
/*     */       
/* 119 */       this.rightLeg.xRot = -1.4137167F;
/* 120 */       this.rightLeg.yRot = 0.31415927F;
/* 121 */       this.rightLeg.zRot = 0.07853982F;
/*     */       
/* 123 */       this.leftLeg.xRot = -1.4137167F;
/* 124 */       this.leftLeg.yRot = -0.31415927F;
/* 125 */       this.leftLeg.zRot = -0.07853982F;
/*     */     } else {
/* 127 */       float animationSpeed = ((IllagerRenderState)state).walkAnimationSpeed;
/* 128 */       float animationPos = ((IllagerRenderState)state).walkAnimationPos;
/* 129 */       this.rightArm.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 2.0F * animationSpeed * 0.5F;
/* 130 */       this.rightArm.yRot = 0.0F;
/* 131 */       this.rightArm.zRot = 0.0F;
/*     */       
/* 133 */       this.leftArm.xRot = Mth.cos((animationPos * 0.6662F)) * 2.0F * animationSpeed * 0.5F;
/* 134 */       this.leftArm.yRot = 0.0F;
/* 135 */       this.leftArm.zRot = 0.0F;
/*     */       
/* 137 */       this.rightLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed * 0.5F;
/* 138 */       this.rightLeg.yRot = 0.0F;
/* 139 */       this.rightLeg.zRot = 0.0F;
/*     */       
/* 141 */       this.leftLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed * 0.5F;
/* 142 */       this.leftLeg.yRot = 0.0F;
/* 143 */       this.leftLeg.zRot = 0.0F;
/*     */     } 
/*     */     
/* 146 */     AbstractIllager.IllagerArmPose pose = ((IllagerRenderState)state).armPose;
/*     */     
/* 148 */     if (pose == AbstractIllager.IllagerArmPose.ATTACKING) {
/* 149 */       if (state.getMainHandItemState().isEmpty()) {
/* 150 */         AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, (UndeadRenderState)state);
/*     */       } else {
/* 152 */         AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, ((IllagerRenderState)state).mainArm, ((IllagerRenderState)state).attackAnim, ((IllagerRenderState)state).ageInTicks);
/*     */       } 
/* 154 */     } else if (pose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
/* 155 */       this.rightArm.z = 0.0F;
/* 156 */       this.rightArm.x = -5.0F;
/* 157 */       this.leftArm.z = 0.0F;
/* 158 */       this.leftArm.x = 5.0F;
/* 159 */       this.rightArm.xRot = Mth.cos((((IllagerRenderState)state).ageInTicks * 0.6662F)) * 0.25F;
/* 160 */       this.leftArm.xRot = Mth.cos((((IllagerRenderState)state).ageInTicks * 0.6662F)) * 0.25F;
/* 161 */       this.rightArm.zRot = 2.3561945F;
/* 162 */       this.leftArm.zRot = -2.3561945F;
/*     */       
/* 164 */       this.rightArm.yRot = 0.0F;
/* 165 */       this.leftArm.yRot = 0.0F;
/* 166 */     } else if (pose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
/* 167 */       this.rightArm.yRot = -0.1F + this.head.yRot;
/* 168 */       this.rightArm.xRot = -1.5707964F + this.head.xRot;
/* 169 */       this.leftArm.xRot = -0.9424779F + this.head.xRot;
/* 170 */       this.head.yRot -= 0.4F;
/* 171 */       this.leftArm.zRot = 1.5707964F;
/* 172 */     } else if (pose == AbstractIllager.IllagerArmPose.CROSSBOW_HOLD) {
/* 173 */       AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
/* 174 */     } else if (pose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE) {
/* 175 */       AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ((IllagerRenderState)state).maxCrossbowChargeDuration, ((IllagerRenderState)state).ticksUsingItem, true);
/* 176 */     } else if (pose == AbstractIllager.IllagerArmPose.CELEBRATING) {
/* 177 */       this.rightArm.z = 0.0F;
/* 178 */       this.rightArm.x = -5.0F;
/* 179 */       this.rightArm.xRot = Mth.cos((((IllagerRenderState)state).ageInTicks * 0.6662F)) * 0.05F;
/* 180 */       this.rightArm.zRot = 2.670354F;
/* 181 */       this.rightArm.yRot = 0.0F;
/*     */       
/* 183 */       this.leftArm.z = 0.0F;
/* 184 */       this.leftArm.x = 5.0F;
/* 185 */       this.leftArm.xRot = Mth.cos((((IllagerRenderState)state).ageInTicks * 0.6662F)) * 0.05F;
/* 186 */       this.leftArm.zRot = -2.3561945F;
/* 187 */       this.leftArm.yRot = 0.0F;
/*     */     } 
/*     */     
/* 190 */     boolean crossedArms = (pose == AbstractIllager.IllagerArmPose.CROSSED);
/* 191 */     this.arms.visible = crossedArms;
/* 192 */     this.leftArm.visible = !crossedArms;
/* 193 */     this.rightArm.visible = !crossedArms;
/*     */   }
/*     */   
/*     */   private ModelPart getArm(HumanoidArm arm) {
/* 197 */     if (arm == HumanoidArm.LEFT) {
/* 198 */       return this.leftArm;
/*     */     }
/* 200 */     return this.rightArm;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelPart getHat() {
/* 205 */     return this.hat;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelPart getHead() {
/* 210 */     return this.head;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(IllagerRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 215 */     this.root.translateAndRotate(poseStack);
/* 216 */     getArm(arm).translateAndRotate(poseStack);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/illager/IllagerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */