/*     */ package net.minecraft.client.model.animal.fox;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.model.BabyModelTransform;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class FoxModel
/*     */   extends EntityModel<FoxRenderState>
/*     */ {
/*  20 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 8.0F, 3.35F, Set.of("head"));
/*     */   
/*     */   public final ModelPart head;
/*     */   
/*     */   private final ModelPart body;
/*     */   
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart tail;
/*     */   private static final int LEG_SIZE = 6;
/*     */   private static final float HEAD_HEIGHT = 16.5F;
/*     */   private static final float LEG_POS = 17.5F;
/*     */   private float legMotionPos;
/*     */   
/*     */   public FoxModel(ModelPart root) {
/*  37 */     super(root);
/*  38 */     this.head = root.getChild("head");
/*  39 */     this.body = root.getChild("body");
/*  40 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  41 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  42 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  43 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  44 */     this.tail = this.body.getChild("tail");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  48 */     MeshDefinition mesh = new MeshDefinition();
/*  49 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  51 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  52 */         CubeListBuilder.create()
/*  53 */         .texOffs(1, 5).addBox(-3.0F, -2.0F, -5.0F, 8.0F, 6.0F, 6.0F), 
/*  54 */         PartPose.offset(-1.0F, 16.5F, -3.0F));
/*     */     
/*  56 */     head.addOrReplaceChild("right_ear", 
/*  57 */         CubeListBuilder.create()
/*  58 */         .texOffs(8, 1).addBox(-3.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  61 */     head.addOrReplaceChild("left_ear", 
/*  62 */         CubeListBuilder.create()
/*  63 */         .texOffs(15, 1).addBox(3.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  66 */     head.addOrReplaceChild("nose", 
/*  67 */         CubeListBuilder.create()
/*  68 */         .texOffs(6, 18).addBox(-1.0F, 2.01F, -8.0F, 4.0F, 2.0F, 3.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  72 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(24, 15).addBox(-3.0F, 3.999F, -3.5F, 6.0F, 11.0F, 6.0F), 
/*  75 */         PartPose.offsetAndRotation(0.0F, 16.0F, -6.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  77 */     CubeDeformation fudge = new CubeDeformation(0.001F);
/*  78 */     CubeListBuilder leftLeg = CubeListBuilder.create()
/*  79 */       .texOffs(4, 24).addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, fudge);
/*  80 */     CubeListBuilder rightLeg = CubeListBuilder.create()
/*  81 */       .texOffs(13, 24).addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, fudge);
/*     */     
/*  83 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-5.0F, 17.5F, 7.0F));
/*     */     
/*  85 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(-1.0F, 17.5F, 7.0F));
/*  86 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-5.0F, 17.5F, 0.0F));
/*  87 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(-1.0F, 17.5F, 0.0F));
/*  88 */     body.addOrReplaceChild("tail", 
/*  89 */         CubeListBuilder.create()
/*  90 */         .texOffs(30, 0).addBox(2.0F, 0.0F, -1.0F, 4.0F, 9.0F, 5.0F), 
/*  91 */         PartPose.offsetAndRotation(-4.0F, 15.0F, -1.0F, -0.05235988F, 0.0F, 0.0F));
/*     */ 
/*     */     
/*  94 */     return LayerDefinition.create(mesh, 48, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(FoxRenderState state) {
/*  99 */     super.setupAnim(state);
/*     */     
/* 101 */     float animationSpeed = state.walkAnimationSpeed;
/* 102 */     float animationPos = state.walkAnimationPos;
/* 103 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 104 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 105 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 106 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*     */     
/* 108 */     this.head.zRot = state.headRollAngle;
/*     */     
/* 110 */     this.rightHindLeg.visible = true;
/* 111 */     this.leftHindLeg.visible = true;
/* 112 */     this.rightFrontLeg.visible = true;
/* 113 */     this.leftFrontLeg.visible = true;
/*     */     
/* 115 */     float ageScale = state.ageScale;
/*     */     
/* 117 */     if (state.isCrouching) {
/* 118 */       this.body.xRot += 0.10471976F;
/* 119 */       float crouch = state.crouchAmount;
/* 120 */       this.body.y += crouch * ageScale;
/* 121 */       this.head.y += crouch * ageScale;
/* 122 */     } else if (state.isSleeping) {
/* 123 */       this.body.zRot = -1.5707964F;
/* 124 */       this.body.y += 5.0F * ageScale;
/* 125 */       this.tail.xRot = -2.6179938F;
/* 126 */       if (state.isBaby) {
/* 127 */         this.tail.xRot = -2.1816616F;
/* 128 */         this.body.z += 2.0F;
/*     */       } 
/* 130 */       this.head.x += 2.0F * ageScale;
/* 131 */       this.head.y += 2.99F * ageScale;
/* 132 */       this.head.yRot = -2.0943952F;
/* 133 */       this.head.zRot = 0.0F;
/*     */       
/* 135 */       this.rightHindLeg.visible = false;
/* 136 */       this.leftHindLeg.visible = false;
/* 137 */       this.rightFrontLeg.visible = false;
/* 138 */       this.leftFrontLeg.visible = false;
/* 139 */     } else if (state.isSitting) {
/* 140 */       this.body.xRot = 0.5235988F;
/* 141 */       this.body.y -= 7.0F * ageScale;
/* 142 */       this.body.z += 3.0F * ageScale;
/* 143 */       this.tail.xRot = 0.7853982F;
/* 144 */       this.tail.z -= 1.0F * ageScale;
/*     */       
/* 146 */       this.head.xRot = 0.0F;
/* 147 */       this.head.yRot = 0.0F;
/*     */       
/* 149 */       if (state.isBaby) {
/* 150 */         this.head.y -= 1.75F;
/* 151 */         this.head.z -= 0.375F;
/*     */       } else {
/* 153 */         this.head.y -= 6.5F;
/* 154 */         this.head.z += 2.75F;
/*     */       } 
/*     */       
/* 157 */       this.rightHindLeg.xRot = -1.3089969F;
/* 158 */       this.rightHindLeg.y += 4.0F * ageScale;
/* 159 */       this.rightHindLeg.z -= 0.25F * ageScale;
/* 160 */       this.leftHindLeg.xRot = -1.3089969F;
/* 161 */       this.leftHindLeg.y += 4.0F * ageScale;
/* 162 */       this.leftHindLeg.z -= 0.25F * ageScale;
/*     */       
/* 164 */       this.rightFrontLeg.xRot = -0.2617994F;
/* 165 */       this.leftFrontLeg.xRot = -0.2617994F;
/*     */     } 
/*     */     
/* 168 */     if (!state.isSleeping && !state.isFaceplanted && !state.isCrouching) {
/* 169 */       this.head.xRot = state.xRot * 0.017453292F;
/* 170 */       this.head.yRot = state.yRot * 0.017453292F;
/*     */     } 
/*     */ 
/*     */     
/* 174 */     if (state.isSleeping) {
/* 175 */       this.head.xRot = 0.0F;
/* 176 */       this.head.yRot = -2.0943952F;
/* 177 */       this.head.zRot = Mth.cos((state.ageInTicks * 0.027F)) / 22.0F;
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (state.isCrouching) {
/* 182 */       float wiggleAmount = Mth.cos(state.ageInTicks) * 0.01F;
/* 183 */       this.body.yRot = wiggleAmount;
/* 184 */       this.rightHindLeg.zRot = wiggleAmount;
/* 185 */       this.leftHindLeg.zRot = wiggleAmount;
/* 186 */       this.rightFrontLeg.zRot = wiggleAmount / 2.0F;
/* 187 */       this.leftFrontLeg.zRot = wiggleAmount / 2.0F;
/*     */     } 
/*     */ 
/*     */     
/* 191 */     if (state.isFaceplanted) {
/* 192 */       float legMoveFactor = 0.1F;
/* 193 */       this.legMotionPos += 0.67F;
/* 194 */       this.rightHindLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F)) * 0.1F;
/* 195 */       this.leftHindLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F + 3.1415927F)) * 0.1F;
/* 196 */       this.rightFrontLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F + 3.1415927F)) * 0.1F;
/* 197 */       this.leftFrontLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F)) * 0.1F;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/fox/FoxModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */