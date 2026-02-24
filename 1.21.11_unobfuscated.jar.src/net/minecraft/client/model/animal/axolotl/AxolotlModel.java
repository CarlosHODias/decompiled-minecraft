/*     */ package net.minecraft.client.model.animal.axolotl;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class AxolotlModel
/*     */   extends EntityModel<AxolotlRenderState>
/*     */ {
/*     */   public static final float SWIMMING_LEG_XROT = 1.8849558F;
/*  19 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/*     */   
/*     */   private final ModelPart tail;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart body;
/*     */   private final ModelPart head;
/*     */   private final ModelPart topGills;
/*     */   private final ModelPart leftGills;
/*     */   private final ModelPart rightGills;
/*     */   
/*     */   public AxolotlModel(ModelPart root) {
/*  33 */     super(root);
/*  34 */     this.body = root.getChild("body");
/*  35 */     this.head = this.body.getChild("head");
/*  36 */     this.rightHindLeg = this.body.getChild("right_hind_leg");
/*  37 */     this.leftHindLeg = this.body.getChild("left_hind_leg");
/*  38 */     this.rightFrontLeg = this.body.getChild("right_front_leg");
/*  39 */     this.leftFrontLeg = this.body.getChild("left_front_leg");
/*  40 */     this.tail = this.body.getChild("tail");
/*  41 */     this.topGills = this.head.getChild("top_gills");
/*  42 */     this.leftGills = this.head.getChild("left_gills");
/*  43 */     this.rightGills = this.head.getChild("right_gills");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  47 */     MeshDefinition mesh = new MeshDefinition();
/*  48 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  50 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  51 */         CubeListBuilder.create()
/*  52 */         .texOffs(0, 11).addBox(-4.0F, -2.0F, -9.0F, 8.0F, 4.0F, 10.0F)
/*  53 */         .texOffs(2, 17).addBox(0.0F, -3.0F, -8.0F, 0.0F, 5.0F, 9.0F), 
/*  54 */         PartPose.offset(0.0F, 20.0F, 5.0F));
/*     */ 
/*     */     
/*  57 */     CubeDeformation fudge = new CubeDeformation(0.001F);
/*  58 */     PartDefinition head = body.addOrReplaceChild("head", 
/*  59 */         CubeListBuilder.create()
/*  60 */         .texOffs(0, 1).addBox(-4.0F, -3.0F, -5.0F, 8.0F, 5.0F, 5.0F, fudge), 
/*  61 */         PartPose.offset(0.0F, 0.0F, -9.0F));
/*     */ 
/*     */     
/*  64 */     CubeListBuilder topGills = CubeListBuilder.create()
/*  65 */       .texOffs(3, 37).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 3.0F, 0.0F, fudge);
/*  66 */     CubeListBuilder leftGills = CubeListBuilder.create()
/*  67 */       .texOffs(0, 40).addBox(-3.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, fudge);
/*  68 */     CubeListBuilder rightGills = CubeListBuilder.create()
/*  69 */       .texOffs(11, 40).addBox(0.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, fudge);
/*     */     
/*  71 */     head.addOrReplaceChild("top_gills", topGills, PartPose.offset(0.0F, -3.0F, -1.0F));
/*  72 */     head.addOrReplaceChild("left_gills", leftGills, PartPose.offset(-4.0F, 0.0F, -1.0F));
/*  73 */     head.addOrReplaceChild("right_gills", rightGills, PartPose.offset(4.0F, 0.0F, -1.0F));
/*     */     
/*  75 */     CubeListBuilder leftLeg = CubeListBuilder.create()
/*  76 */       .texOffs(2, 13).addBox(-1.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, fudge);
/*  77 */     CubeListBuilder rightLeg = CubeListBuilder.create()
/*  78 */       .texOffs(2, 13).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, fudge);
/*     */     
/*  80 */     body.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-3.5F, 1.0F, -1.0F));
/*  81 */     body.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(3.5F, 1.0F, -1.0F));
/*  82 */     body.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-3.5F, 1.0F, -8.0F));
/*  83 */     body.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(3.5F, 1.0F, -8.0F));
/*  84 */     body.addOrReplaceChild("tail", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(2, 19).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 12.0F), 
/*  87 */         PartPose.offset(0.0F, 0.0F, 1.0F));
/*     */ 
/*     */     
/*  90 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(AxolotlRenderState state) {
/*  95 */     super.setupAnim(state);
/*     */     
/*  97 */     float playingDeadFactor = state.playingDeadFactor;
/*  98 */     float inWaterFactor = state.inWaterFactor;
/*  99 */     float onGroundFactor = state.onGroundFactor;
/* 100 */     float movingFactor = state.movingFactor;
/* 101 */     float notMovingFactor = 1.0F - movingFactor;
/*     */     
/* 103 */     float mirroredLegsFactor = 1.0F - Math.min(onGroundFactor, movingFactor);
/*     */     
/* 105 */     this.body.yRot += state.yRot * 0.017453292F;
/*     */     
/* 107 */     setupSwimmingAnimation(state.ageInTicks, state.xRot, Math.min(movingFactor, inWaterFactor));
/* 108 */     setupWaterHoveringAnimation(state.ageInTicks, Math.min(notMovingFactor, inWaterFactor));
/* 109 */     setupGroundCrawlingAnimation(state.ageInTicks, Math.min(movingFactor, onGroundFactor));
/* 110 */     setupLayStillOnGroundAnimation(state.ageInTicks, Math.min(notMovingFactor, onGroundFactor));
/*     */     
/* 112 */     setupPlayDeadAnimation(playingDeadFactor);
/*     */     
/* 114 */     applyMirrorLegRotations(mirroredLegsFactor);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupLayStillOnGroundAnimation(float ageInTicks, float factor) {
/* 119 */     if (factor <= 1.0E-5F) {
/*     */       return;
/*     */     }
/*     */     
/* 123 */     float animMoveSpeed = ageInTicks * 0.09F;
/* 124 */     float sineSway = Mth.sin(animMoveSpeed);
/* 125 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 126 */     float movement = sineSway * sineSway - 2.0F * sineSway;
/* 127 */     float movement2 = cosineSway * cosineSway - 3.0F * sineSway;
/*     */     
/* 129 */     this.head.xRot += -0.09F * movement * factor;
/* 130 */     this.head.zRot += -0.2F * factor;
/*     */     
/* 132 */     this.tail.yRot += (-0.1F + 0.1F * movement) * factor;
/*     */     
/* 134 */     float gillAngle = (0.6F + 0.05F * movement2) * factor;
/* 135 */     this.topGills.xRot += gillAngle;
/* 136 */     this.leftGills.yRot -= gillAngle;
/* 137 */     this.rightGills.yRot += gillAngle;
/*     */     
/* 139 */     this.leftHindLeg.xRot += 1.1F * factor;
/* 140 */     this.leftHindLeg.yRot += 1.0F * factor;
/* 141 */     this.leftFrontLeg.xRot += 0.8F * factor;
/* 142 */     this.leftFrontLeg.yRot += 2.3F * factor;
/* 143 */     this.leftFrontLeg.zRot -= 0.5F * factor;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupGroundCrawlingAnimation(float ageInTicks, float factor) {
/* 148 */     if (factor <= 1.0E-5F) {
/*     */       return;
/*     */     }
/*     */     
/* 152 */     float animMoveSpeed = ageInTicks * 0.11F;
/* 153 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 154 */     float hindLegYRotSway = (cosineSway * cosineSway - 2.0F * cosineSway) / 5.0F;
/* 155 */     float frontLegYRotSway = 0.7F * cosineSway;
/*     */     
/* 157 */     float headAndTailYRot = 0.09F * cosineSway * factor;
/* 158 */     this.head.yRot += headAndTailYRot;
/* 159 */     this.tail.yRot += headAndTailYRot;
/*     */     
/* 161 */     float gillAngle = (0.6F - 0.08F * (cosineSway * cosineSway + 2.0F * Mth.sin(animMoveSpeed))) * factor;
/* 162 */     this.topGills.xRot += gillAngle;
/* 163 */     this.leftGills.yRot -= gillAngle;
/* 164 */     this.rightGills.yRot += gillAngle;
/*     */     
/* 166 */     float hindLegXRot = 0.9424779F * factor;
/* 167 */     float frontLegXRot = 1.0995574F * factor;
/* 168 */     this.leftHindLeg.xRot += hindLegXRot;
/* 169 */     this.leftHindLeg.yRot += (1.5F - hindLegYRotSway) * factor;
/* 170 */     this.leftHindLeg.zRot += -0.1F * factor;
/* 171 */     this.leftFrontLeg.xRot += frontLegXRot;
/* 172 */     this.leftFrontLeg.yRot += (1.5707964F - frontLegYRotSway) * factor;
/* 173 */     this.rightHindLeg.xRot += hindLegXRot;
/* 174 */     this.rightHindLeg.yRot += (-1.0F - hindLegYRotSway) * factor;
/* 175 */     this.rightFrontLeg.xRot += frontLegXRot;
/* 176 */     this.rightFrontLeg.yRot += (-1.5707964F - frontLegYRotSway) * factor;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupWaterHoveringAnimation(float ageInTicks, float factor) {
/* 181 */     if (factor <= 1.0E-5F) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     float animMoveSpeed = ageInTicks * 0.075F;
/* 186 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 187 */     float sineSway = Mth.sin(animMoveSpeed) * 0.15F;
/*     */     
/* 189 */     float bodyXRot = (-0.15F + 0.075F * cosineSway) * factor;
/* 190 */     this.body.xRot += bodyXRot;
/* 191 */     this.body.y -= sineSway * factor;
/*     */     
/* 193 */     this.head.xRot -= bodyXRot;
/*     */     
/* 195 */     this.topGills.xRot += 0.2F * cosineSway * factor;
/* 196 */     float gillYRot = (-0.3F * cosineSway - 0.19F) * factor;
/* 197 */     this.leftGills.yRot += gillYRot;
/* 198 */     this.rightGills.yRot -= gillYRot;
/*     */     
/* 200 */     this.leftHindLeg.xRot += (2.3561945F - cosineSway * 0.11F) * factor;
/* 201 */     this.leftHindLeg.yRot += 0.47123894F * factor;
/* 202 */     this.leftHindLeg.zRot += 1.7278761F * factor;
/* 203 */     this.leftFrontLeg.xRot += (0.7853982F - cosineSway * 0.2F) * factor;
/* 204 */     this.leftFrontLeg.yRot += 2.042035F * factor;
/*     */     
/* 206 */     this.tail.yRot += 0.5F * cosineSway * factor;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupSwimmingAnimation(float ageInTicks, float xRot, float factor) {
/* 211 */     if (factor <= 1.0E-5F) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     float animMoveSpeed = ageInTicks * 0.33F;
/* 216 */     float sineSway = Mth.sin(animMoveSpeed);
/* 217 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 218 */     float bodySway = 0.13F * sineSway;
/*     */     
/* 220 */     this.body.xRot += (xRot * 0.017453292F + bodySway) * factor;
/* 221 */     this.head.xRot -= bodySway * 1.8F * factor;
/* 222 */     this.body.y -= 0.45F * cosineSway * factor;
/*     */     
/* 224 */     this.topGills.xRot += (-0.5F * sineSway - 0.8F) * factor;
/* 225 */     float gillYRot = (0.3F * sineSway + 0.9F) * factor;
/* 226 */     this.leftGills.yRot += gillYRot;
/* 227 */     this.rightGills.yRot -= gillYRot;
/*     */     
/* 229 */     this.tail.yRot += 0.3F * Mth.cos((animMoveSpeed * 0.9F)) * factor;
/*     */     
/* 231 */     this.leftHindLeg.xRot += 1.8849558F * factor;
/* 232 */     this.leftHindLeg.yRot += -0.4F * sineSway * factor;
/* 233 */     this.leftHindLeg.zRot += 1.5707964F * factor;
/* 234 */     this.leftFrontLeg.xRot += 1.8849558F * factor;
/* 235 */     this.leftFrontLeg.yRot += (-0.2F * cosineSway - 0.1F) * factor;
/* 236 */     this.leftFrontLeg.zRot += 1.5707964F * factor;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupPlayDeadAnimation(float factor) {
/* 241 */     if (factor <= 1.0E-5F) {
/*     */       return;
/*     */     }
/*     */     
/* 245 */     this.leftHindLeg.xRot += 1.4137167F * factor;
/* 246 */     this.leftHindLeg.yRot += 1.0995574F * factor;
/* 247 */     this.leftHindLeg.zRot += 0.7853982F * factor;
/* 248 */     this.leftFrontLeg.xRot += 0.7853982F * factor;
/* 249 */     this.leftFrontLeg.yRot += 2.042035F * factor;
/*     */     
/* 251 */     this.body.xRot += -0.15F * factor;
/* 252 */     this.body.zRot += 0.35F * factor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyMirrorLegRotations(float factor) {
/* 259 */     if (factor <= 1.0E-5F) {
/*     */       return;
/*     */     }
/* 262 */     this.rightHindLeg.xRot += this.leftHindLeg.xRot * factor;
/* 263 */     ModelPart modelPart = this.rightHindLeg; modelPart.yRot += -this.leftHindLeg.yRot * factor;
/* 264 */     modelPart = this.rightHindLeg; modelPart.zRot += -this.leftHindLeg.zRot * factor;
/* 265 */     this.rightFrontLeg.xRot += this.leftFrontLeg.xRot * factor;
/* 266 */     modelPart = this.rightFrontLeg; modelPart.yRot += -this.leftFrontLeg.yRot * factor;
/* 267 */     modelPart = this.rightFrontLeg; modelPart.zRot += -this.leftFrontLeg.zRot * factor;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/axolotl/AxolotlModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */