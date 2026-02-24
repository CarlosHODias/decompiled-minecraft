/*     */ package net.minecraft.client.model;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.model.effects.SpearAnimations;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ 
/*     */ public class HumanoidModel<T extends HumanoidRenderState> extends EntityModel<T> implements ArmedModel<T>, HeadedModel {
/*     */   public static final float OVERLAY_SCALE = 0.25F;
/*     */   public static final float HAT_OVERLAY_SCALE = 0.5F;
/*     */   public static final float LEGGINGS_OVERLAY_SCALE = -0.1F;
/*     */   private static final float DUCK_WALK_ROTATION = 0.005F;
/*     */   private static final float SPYGLASS_ARM_ROT_Y = 0.2617994F;
/*     */   private static final float SPYGLASS_ARM_ROT_X = 1.9198622F;
/*     */   private static final float SPYGLASS_ARM_CROUCH_ROT_X = 0.2617994F;
/*     */   private static final float HIGHEST_SHIELD_BLOCKING_ANGLE = -1.3962634F;
/*     */   private static final float LOWEST_SHIELD_BLOCKING_ANGLE = 0.43633232F;
/*     */   private static final float HORIZONTAL_SHIELD_MOVEMENT_LIMIT = 0.5235988F;
/*  29 */   public static final net.minecraft.client.model.geom.builders.MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(true, 16.0F, 0.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));
/*     */   
/*     */   public static final float TOOT_HORN_XROT_BASE = 1.4835298F;
/*     */   
/*     */   public static final float TOOT_HORN_YROT_BASE = 0.5235988F;
/*     */   
/*     */   public final ModelPart head;
/*     */   
/*     */   public final ModelPart hat;
/*     */   public final ModelPart body;
/*     */   public final ModelPart rightArm;
/*     */   public final ModelPart leftArm;
/*     */   public final ModelPart rightLeg;
/*     */   public final ModelPart leftLeg;
/*     */   
/*     */   public enum ArmPose
/*     */   {
/*  46 */     EMPTY(false, false),
/*  47 */     ITEM(false, false),
/*  48 */     BLOCK(false, false),
/*  49 */     BOW_AND_ARROW(true, true),
/*  50 */     THROW_TRIDENT(false, true),
/*  51 */     CROSSBOW_CHARGE(true, true),
/*  52 */     CROSSBOW_HOLD(true, true),
/*  53 */     SPYGLASS(false, false),
/*  54 */     TOOT_HORN(false, false),
/*  55 */     BRUSH(false, false),
/*  56 */     SPEAR(false, true)
/*     */     {
/*     */       public <S extends net.minecraft.client.renderer.entity.state.ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, net.minecraft.world.item.ItemStack actualItem) {
/*  59 */         SpearAnimations.thirdPersonUseItem((net.minecraft.client.renderer.entity.state.ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, actualItem);
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     private final boolean twoHanded;
/*     */     private final boolean affectsOffhandPose;
/*     */     
/*     */     ArmPose(boolean twoHanded, boolean affectsOffhandPose) {
/*  68 */       this.twoHanded = twoHanded;
/*  69 */       this.affectsOffhandPose = affectsOffhandPose;
/*     */     }
/*     */     
/*     */     public boolean isTwoHanded() {
/*  73 */       return this.twoHanded;
/*     */     }
/*     */     
/*     */     public boolean affectsOffhandPose() {
/*  77 */       return this.affectsOffhandPose;
/*     */     }
/*     */ 
/*     */     
/*     */     public <S extends net.minecraft.client.renderer.entity.state.ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, net.minecraft.world.item.ItemStack actualItem) {}
/*     */   }
/*     */ 
/*     */   
/*     */   enum null
/*     */   {
/*     */     public <S extends net.minecraft.client.renderer.entity.state.ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, net.minecraft.world.item.ItemStack actualItem) {
/*     */       SpearAnimations.thirdPersonUseItem((net.minecraft.client.renderer.entity.state.ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, actualItem);
/*     */     }
/*     */   }
/*     */   
/*     */   public HumanoidModel(ModelPart root) {
/*  93 */     this(root, net.minecraft.client.renderer.rendertype.RenderTypes::entityCutoutNoCull);
/*     */   }
/*     */   
/*     */   public HumanoidModel(ModelPart root, Function<net.minecraft.resources.Identifier, net.minecraft.client.renderer.rendertype.RenderType> renderType) {
/*  97 */     super(root, renderType);
/*  98 */     this.head = root.getChild("head");
/*  99 */     this.hat = this.head.getChild("hat");
/* 100 */     this.body = root.getChild("body");
/* 101 */     this.rightArm = root.getChild("right_arm");
/* 102 */     this.leftArm = root.getChild("left_arm");
/* 103 */     this.rightLeg = root.getChild("right_leg");
/* 104 */     this.leftLeg = root.getChild("left_leg");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createMesh(CubeDeformation g, float yOffset) {
/* 108 */     MeshDefinition mesh = new MeshDefinition();
/* 109 */     PartDefinition root = mesh.getRoot();
/* 110 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 111 */         CubeListBuilder.create()
/* 112 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g), 
/* 113 */         PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
/*     */     
/* 115 */     head.addOrReplaceChild("hat", 
/* 116 */         CubeListBuilder.create()
/* 117 */         .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g.extend(0.5F)), PartPose.ZERO);
/*     */ 
/*     */     
/* 120 */     root.addOrReplaceChild("body", 
/* 121 */         CubeListBuilder.create()
/* 122 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, g), 
/* 123 */         PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
/*     */     
/* 125 */     root.addOrReplaceChild("right_arm", 
/* 126 */         CubeListBuilder.create()
/* 127 */         .texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 128 */         PartPose.offset(-5.0F, 2.0F + yOffset, 0.0F));
/*     */     
/* 130 */     root.addOrReplaceChild("left_arm", 
/* 131 */         CubeListBuilder.create()
/* 132 */         .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 133 */         PartPose.offset(5.0F, 2.0F + yOffset, 0.0F));
/*     */     
/* 135 */     root.addOrReplaceChild("right_leg", 
/* 136 */         CubeListBuilder.create()
/* 137 */         .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 138 */         PartPose.offset(-1.9F, 12.0F + yOffset, 0.0F));
/*     */     
/* 140 */     root.addOrReplaceChild("left_leg", 
/* 141 */         CubeListBuilder.create()
/* 142 */         .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 143 */         PartPose.offset(1.9F, 12.0F + yOffset, 0.0F));
/*     */     
/* 145 */     return mesh;
/*     */   }
/*     */   
/*     */   public static ArmorModelSet<MeshDefinition> createArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 149 */     return createArmorMeshSet(HumanoidModel::createBaseArmorMesh, innerDeformation, outerDeformation);
/*     */   }
/*     */   
/*     */   protected static ArmorModelSet<MeshDefinition> createArmorMeshSet(Function<CubeDeformation, MeshDefinition> baseFactory, CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 153 */     MeshDefinition head = baseFactory.apply(outerDeformation);
/* 154 */     head.getRoot().retainPartsAndChildren(Set.of("head"));
/*     */     
/* 156 */     MeshDefinition chest = baseFactory.apply(outerDeformation);
/* 157 */     chest.getRoot().retainExactParts(Set.of("body", "left_arm", "right_arm"));
/*     */     
/* 159 */     MeshDefinition legs = baseFactory.apply(innerDeformation);
/* 160 */     legs.getRoot().retainExactParts(Set.of("left_leg", "right_leg", "body"));
/*     */     
/* 162 */     MeshDefinition feet = baseFactory.apply(outerDeformation);
/* 163 */     feet.getRoot().retainExactParts(Set.of("left_leg", "right_leg"));
/*     */     
/* 165 */     return new ArmorModelSet(head, chest, legs, feet);
/*     */   }
/*     */   
/*     */   private static MeshDefinition createBaseArmorMesh(CubeDeformation g) {
/* 169 */     MeshDefinition mesh = createMesh(g, 0.0F);
/* 170 */     PartDefinition root = mesh.getRoot();
/* 171 */     root.addOrReplaceChild("right_leg", 
/* 172 */         CubeListBuilder.create()
/* 173 */         .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(-0.1F)), 
/* 174 */         PartPose.offset(-1.9F, 12.0F, 0.0F));
/*     */     
/* 176 */     root.addOrReplaceChild("left_leg", 
/* 177 */         CubeListBuilder.create()
/* 178 */         .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(-0.1F)), 
/* 179 */         PartPose.offset(1.9F, 12.0F, 0.0F));
/*     */     
/* 181 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(T state) {
/* 186 */     super.setupAnim(state);
/*     */     
/* 188 */     ArmPose leftArmPose = ((HumanoidRenderState)state).leftArmPose;
/* 189 */     ArmPose rightArmPose = ((HumanoidRenderState)state).rightArmPose;
/* 190 */     float swimAmount = ((HumanoidRenderState)state).swimAmount;
/*     */     
/* 192 */     boolean fallFlying = ((HumanoidRenderState)state).isFallFlying;
/*     */     
/* 194 */     this.head.xRot = ((HumanoidRenderState)state).xRot * 0.017453292F;
/* 195 */     this.head.yRot = ((HumanoidRenderState)state).yRot * 0.017453292F;
/*     */     
/* 197 */     if (fallFlying) {
/* 198 */       this.head.xRot = -0.7853982F;
/* 199 */     } else if (swimAmount > 0.0F) {
/* 200 */       this.head.xRot = Mth.rotLerpRad(swimAmount, this.head.xRot, -0.7853982F);
/*     */     } 
/*     */     
/* 203 */     float animationPos = ((HumanoidRenderState)state).walkAnimationPos;
/* 204 */     float animationSpeed = ((HumanoidRenderState)state).walkAnimationSpeed;
/* 205 */     this.rightArm.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 2.0F * animationSpeed * 0.5F / ((HumanoidRenderState)state).speedValue;
/* 206 */     this.leftArm.xRot = Mth.cos((animationPos * 0.6662F)) * 2.0F * animationSpeed * 0.5F / ((HumanoidRenderState)state).speedValue;
/*     */     
/* 208 */     this.rightLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed / ((HumanoidRenderState)state).speedValue;
/* 209 */     this.leftLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed / ((HumanoidRenderState)state).speedValue;
/* 210 */     this.rightLeg.yRot = 0.005F;
/* 211 */     this.leftLeg.yRot = -0.005F;
/* 212 */     this.rightLeg.zRot = 0.005F;
/* 213 */     this.leftLeg.zRot = -0.005F;
/*     */     
/* 215 */     if (((HumanoidRenderState)state).isPassenger) {
/* 216 */       this.rightArm.xRot += -0.62831855F;
/* 217 */       this.leftArm.xRot += -0.62831855F;
/*     */       
/* 219 */       this.rightLeg.xRot = -1.4137167F;
/* 220 */       this.rightLeg.yRot = 0.31415927F;
/* 221 */       this.rightLeg.zRot = 0.07853982F;
/*     */       
/* 223 */       this.leftLeg.xRot = -1.4137167F;
/* 224 */       this.leftLeg.yRot = -0.31415927F;
/* 225 */       this.leftLeg.zRot = -0.07853982F;
/*     */     } 
/*     */ 
/*     */     
/* 229 */     boolean rightHanded = (((HumanoidRenderState)state).mainArm == HumanoidArm.RIGHT);
/* 230 */     if (((HumanoidRenderState)state).isUsingItem) {
/* 231 */       boolean mainHandUsed = (((HumanoidRenderState)state).useItemHand == net.minecraft.world.InteractionHand.MAIN_HAND);
/* 232 */       if (mainHandUsed == rightHanded) {
/* 233 */         poseRightArm(state);
/* 234 */         if (!((HumanoidRenderState)state).rightArmPose.affectsOffhandPose())
/* 235 */           poseLeftArm(state); 
/*     */       } else {
/* 237 */         poseLeftArm(state);
/* 238 */         if (!((HumanoidRenderState)state).leftArmPose.affectsOffhandPose())
/* 239 */           poseRightArm(state); 
/*     */       } 
/*     */     } else {
/* 242 */       boolean twoHandedOffhand = rightHanded ? leftArmPose.isTwoHanded() : rightArmPose.isTwoHanded();
/* 243 */       if (rightHanded != twoHandedOffhand) {
/* 244 */         poseLeftArm(state);
/* 245 */         if (!((HumanoidRenderState)state).leftArmPose.affectsOffhandPose())
/* 246 */           poseRightArm(state); 
/*     */       } else {
/* 248 */         poseRightArm(state);
/* 249 */         if (!((HumanoidRenderState)state).rightArmPose.affectsOffhandPose()) {
/* 250 */           poseLeftArm(state);
/*     */         }
/*     */       } 
/*     */     } 
/* 254 */     setupAttackAnimation(state);
/*     */     
/* 256 */     if (((HumanoidRenderState)state).isCrouching) {
/* 257 */       this.body.xRot = 0.5F;
/* 258 */       this.rightArm.xRot += 0.4F;
/* 259 */       this.leftArm.xRot += 0.4F;
/* 260 */       this.rightLeg.z += 4.0F;
/* 261 */       this.leftLeg.z += 4.0F;
/* 262 */       this.head.y += 4.2F;
/* 263 */       this.body.y += 3.2F;
/* 264 */       this.leftArm.y += 3.2F;
/* 265 */       this.rightArm.y += 3.2F;
/*     */     } 
/*     */     
/* 268 */     if (rightArmPose != ArmPose.SPYGLASS) {
/* 269 */       AnimationUtils.bobModelPart(this.rightArm, ((HumanoidRenderState)state).ageInTicks, 1.0F);
/*     */     }
/* 271 */     if (leftArmPose != ArmPose.SPYGLASS) {
/* 272 */       AnimationUtils.bobModelPart(this.leftArm, ((HumanoidRenderState)state).ageInTicks, -1.0F);
/*     */     }
/*     */     
/* 275 */     if (swimAmount > 0.0F) {
/* 276 */       float swimPos = animationPos % 26.0F;
/*     */ 
/*     */       
/* 279 */       HumanoidArm attackArm = ((HumanoidRenderState)state).attackArm;
/* 280 */       float rightArmSwimAmount = (((HumanoidRenderState)state).rightArmPose == ArmPose.SPEAR || (attackArm == HumanoidArm.RIGHT && ((HumanoidRenderState)state).attackTime > 0.0F)) ? 0.0F : swimAmount;
/* 281 */       float leftArmSwimAmount = (((HumanoidRenderState)state).leftArmPose == ArmPose.SPEAR || (attackArm == HumanoidArm.LEFT && ((HumanoidRenderState)state).attackTime > 0.0F)) ? 0.0F : swimAmount;
/*     */       
/* 283 */       if (!((HumanoidRenderState)state).isUsingItem) {
/* 284 */         if (swimPos < 14.0F) {
/* 285 */           this.leftArm.xRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.xRot, 0.0F);
/* 286 */           this.rightArm.xRot = Mth.lerp(rightArmSwimAmount, this.rightArm.xRot, 0.0F);
/*     */           
/* 288 */           this.leftArm.yRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.yRot, 3.1415927F);
/* 289 */           this.rightArm.yRot = Mth.lerp(rightArmSwimAmount, this.rightArm.yRot, 3.1415927F);
/*     */           
/* 291 */           this.leftArm.zRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.zRot, 3.1415927F + 1.8707964F * quadraticArmUpdate(swimPos) / quadraticArmUpdate(14.0F));
/* 292 */           this.rightArm.zRot = Mth.lerp(rightArmSwimAmount, this.rightArm.zRot, 3.1415927F - 1.8707964F * quadraticArmUpdate(swimPos) / quadraticArmUpdate(14.0F));
/* 293 */         } else if (swimPos >= 14.0F && swimPos < 22.0F) {
/* 294 */           float internalSwimPos = (swimPos - 14.0F) / 8.0F;
/*     */           
/* 296 */           this.leftArm.xRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.xRot, 1.5707964F * internalSwimPos);
/* 297 */           this.rightArm.xRot = Mth.lerp(rightArmSwimAmount, this.rightArm.xRot, 1.5707964F * internalSwimPos);
/*     */           
/* 299 */           this.leftArm.yRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.yRot, 3.1415927F);
/* 300 */           this.rightArm.yRot = Mth.lerp(rightArmSwimAmount, this.rightArm.yRot, 3.1415927F);
/*     */           
/* 302 */           this.leftArm.zRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.zRot, 5.012389F - 1.8707964F * internalSwimPos);
/* 303 */           this.rightArm.zRot = Mth.lerp(rightArmSwimAmount, this.rightArm.zRot, 1.2707963F + 1.8707964F * internalSwimPos);
/* 304 */         } else if (swimPos >= 22.0F && swimPos < 26.0F) {
/* 305 */           float internalSwimPos = (swimPos - 22.0F) / 4.0F;
/*     */           
/* 307 */           this.leftArm.xRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.xRot, 1.5707964F - 1.5707964F * internalSwimPos);
/* 308 */           this.rightArm.xRot = Mth.lerp(rightArmSwimAmount, this.rightArm.xRot, 1.5707964F - 1.5707964F * internalSwimPos);
/*     */           
/* 310 */           this.leftArm.yRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.yRot, 3.1415927F);
/* 311 */           this.rightArm.yRot = Mth.lerp(rightArmSwimAmount, this.rightArm.yRot, 3.1415927F);
/*     */           
/* 313 */           this.leftArm.zRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.zRot, 3.1415927F);
/* 314 */           this.rightArm.zRot = Mth.lerp(rightArmSwimAmount, this.rightArm.zRot, 3.1415927F);
/*     */         } 
/*     */       }
/*     */       
/* 318 */       float amplitude = 0.3F;
/* 319 */       float slowdown = 0.33333334F;
/* 320 */       this.leftLeg.xRot = Mth.lerp(swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos((animationPos * 0.33333334F + 3.1415927F)));
/* 321 */       this.rightLeg.xRot = Mth.lerp(swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos((animationPos * 0.33333334F)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseRightArm(T state) {
/* 326 */     switch (((HumanoidRenderState)state).rightArmPose.ordinal()) {
/*     */       case 0:
/* 328 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 2:
/* 331 */         poseBlockingArm(this.rightArm, true);
/*     */         break;
/*     */       case 1:
/* 334 */         this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.31415927F;
/* 335 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 4:
/* 338 */         this.rightArm.xRot = this.rightArm.xRot * 0.5F - 3.1415927F;
/* 339 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 10:
/* 342 */         SpearAnimations.thirdPersonHandUse(this.rightArm, this.head, true, state.getUseItemStackForArm(HumanoidArm.RIGHT), (HumanoidRenderState)state);
/*     */         break;
/*     */       case 3:
/* 345 */         this.rightArm.yRot = -0.1F + this.head.yRot;
/* 346 */         this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
/* 347 */         this.rightArm.xRot = -1.5707964F + this.head.xRot;
/* 348 */         this.leftArm.xRot = -1.5707964F + this.head.xRot;
/*     */         break;
/*     */       case 5:
/* 351 */         AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ((HumanoidRenderState)state).maxCrossbowChargeDuration, ((HumanoidRenderState)state).ticksUsingItem, true);
/*     */         break;
/*     */       case 6:
/* 354 */         AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
/*     */         break;
/*     */       case 9:
/* 357 */         this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.62831855F;
/* 358 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 7:
/* 361 */         this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (((HumanoidRenderState)state).isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
/* 362 */         this.head.yRot -= 0.2617994F;
/*     */         break;
/*     */       case 8:
/* 365 */         this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
/* 366 */         this.head.yRot -= 0.5235988F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseLeftArm(T state) {
/* 372 */     switch (((HumanoidRenderState)state).leftArmPose.ordinal()) {
/*     */       case 0:
/* 374 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 2:
/* 377 */         poseBlockingArm(this.leftArm, false);
/*     */         break;
/*     */       case 1:
/* 380 */         this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.31415927F;
/* 381 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 4:
/* 384 */         this.leftArm.xRot = this.leftArm.xRot * 0.5F - 3.1415927F;
/* 385 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 10:
/* 388 */         SpearAnimations.thirdPersonHandUse(this.leftArm, this.head, false, state.getUseItemStackForArm(HumanoidArm.LEFT), (HumanoidRenderState)state);
/*     */         break;
/*     */       case 3:
/* 391 */         this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
/* 392 */         this.leftArm.yRot = 0.1F + this.head.yRot;
/* 393 */         this.rightArm.xRot = -1.5707964F + this.head.xRot;
/* 394 */         this.leftArm.xRot = -1.5707964F + this.head.xRot;
/*     */         break;
/*     */       case 5:
/* 397 */         AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ((HumanoidRenderState)state).maxCrossbowChargeDuration, ((HumanoidRenderState)state).ticksUsingItem, false);
/*     */         break;
/*     */       case 6:
/* 400 */         AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
/*     */         break;
/*     */       case 9:
/* 403 */         this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.62831855F;
/* 404 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 7:
/* 407 */         this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (((HumanoidRenderState)state).isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
/* 408 */         this.head.yRot += 0.2617994F;
/*     */         break;
/*     */       case 8:
/* 411 */         this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
/* 412 */         this.head.yRot += 0.5235988F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseBlockingArm(ModelPart arm, boolean right) {
/* 418 */     arm.xRot = arm.xRot * 0.5F - 0.9424779F + Mth.clamp(this.head.xRot, -1.3962634F, 0.43633232F);
/* 419 */     arm.yRot = (right ? -30.0F : 30.0F) * 0.017453292F + Mth.clamp(this.head.yRot, -0.5235988F, 0.5235988F);
/*     */   } protected void setupAttackAnimation(T state) {
/*     */     float swing, aa, bb;
/*     */     ModelPart attackArm;
/* 423 */     float attackTime = ((HumanoidRenderState)state).attackTime;
/* 424 */     if (attackTime <= 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/* 428 */     this.body.yRot = Mth.sin((Mth.sqrt(attackTime) * 6.2831855F)) * 0.2F;
/* 429 */     if (((HumanoidRenderState)state).attackArm == HumanoidArm.LEFT) {
/* 430 */       this.body.yRot *= -1.0F;
/*     */     }
/* 432 */     float ageScale = ((HumanoidRenderState)state).ageScale;
/* 433 */     this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F * ageScale;
/* 434 */     this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F * ageScale;
/* 435 */     this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F * ageScale;
/* 436 */     this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F * ageScale;
/* 437 */     this.rightArm.yRot += this.body.yRot;
/* 438 */     this.leftArm.yRot += this.body.yRot;
/* 439 */     this.leftArm.xRot += this.body.yRot;
/*     */     
/* 441 */     switch (((HumanoidRenderState)state).swingAnimationType) {
/*     */       case WHACK:
/* 443 */         swing = net.minecraft.util.Ease.outQuart(attackTime);
/*     */         
/* 445 */         aa = Mth.sin((swing * 3.1415927F));
/* 446 */         bb = Mth.sin((attackTime * 3.1415927F)) * -(this.head.xRot - 0.7F) * 0.75F;
/*     */         
/* 448 */         attackArm = getArm(((HumanoidRenderState)state).attackArm);
/*     */         
/* 450 */         attackArm.xRot -= aa * 1.2F + bb;
/* 451 */         attackArm.yRot += this.body.yRot * 2.0F;
/* 452 */         attackArm.zRot += Mth.sin((attackTime * 3.1415927F)) * -0.4F;
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case STAB:
/* 459 */         SpearAnimations.thirdPersonAttackHand(this, (HumanoidRenderState)state);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float quadraticArmUpdate(float x) {
/* 465 */     return -65.0F * x + x * x;
/*     */   }
/*     */   
/*     */   public void setAllVisible(boolean visible) {
/* 469 */     this.head.visible = visible;
/* 470 */     this.hat.visible = visible;
/* 471 */     this.body.visible = visible;
/* 472 */     this.rightArm.visible = visible;
/* 473 */     this.leftArm.visible = visible;
/* 474 */     this.rightLeg.visible = visible;
/* 475 */     this.leftLeg.visible = visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(HumanoidRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 480 */     this.root.translateAndRotate(poseStack);
/* 481 */     getArm(arm).translateAndRotate(poseStack);
/*     */   }
/*     */   
/*     */   public ModelPart getArm(HumanoidArm arm) {
/* 485 */     if (arm == HumanoidArm.LEFT) {
/* 486 */       return this.leftArm;
/*     */     }
/* 488 */     return this.rightArm;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelPart getHead() {
/* 494 */     return this.head;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/HumanoidModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */