/*     */ package net.minecraft.client.model.animal.equine;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.model.BabyModelTransform;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEquineModel<T extends EquineRenderState>
/*     */   extends EntityModel<T>
/*     */ {
/*     */   private static final float DEG_125 = 2.1816616F;
/*     */   private static final float DEG_60 = 1.0471976F;
/*     */   private static final float DEG_45 = 0.7853982F;
/*     */   private static final float DEG_30 = 0.5235988F;
/*     */   private static final float DEG_15 = 0.2617994F;
/*     */   protected static final String HEAD_PARTS = "head_parts";
/*  27 */   protected static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 16.2F, 1.36F, 2.7272F, 2.0F, 20.0F, Set.of("head_parts"));
/*     */   
/*     */   protected final ModelPart body;
/*     */   
/*     */   protected final ModelPart headParts;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart tail;
/*     */   
/*     */   public AbstractEquineModel(ModelPart root) {
/*  39 */     super(root);
/*  40 */     this.body = root.getChild("body");
/*  41 */     this.headParts = root.getChild("head_parts");
/*     */     
/*  43 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  44 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  45 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  46 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*     */     
/*  48 */     this.tail = this.body.getChild("tail");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBodyMesh(CubeDeformation g) {
/*  52 */     MeshDefinition mesh = new MeshDefinition();
/*  53 */     PartDefinition root = mesh.getRoot();
/*  54 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(0, 32).addBox(-5.0F, -8.0F, -17.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.05F)), 
/*  57 */         PartPose.offset(0.0F, 11.0F, 5.0F));
/*     */ 
/*     */ 
/*     */     
/*  61 */     PartDefinition headParts = root.addOrReplaceChild("head_parts", 
/*  62 */         CubeListBuilder.create()
/*  63 */         .texOffs(0, 35).addBox(-2.05F, -6.0F, -2.0F, 4.0F, 12.0F, 7.0F), 
/*  64 */         PartPose.offsetAndRotation(0.0F, 4.0F, -12.0F, 0.5235988F, 0.0F, 0.0F));
/*     */ 
/*     */     
/*  67 */     PartDefinition head = headParts.addOrReplaceChild("head", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(0, 13).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, g), PartPose.ZERO);
/*     */     
/*  71 */     headParts.addOrReplaceChild("mane", 
/*  72 */         CubeListBuilder.create()
/*  73 */         .texOffs(56, 36).addBox(-1.0F, -11.0F, 5.01F, 2.0F, 16.0F, 2.0F, g), PartPose.ZERO);
/*     */ 
/*     */     
/*  76 */     headParts.addOrReplaceChild("upper_mouth", 
/*  77 */         CubeListBuilder.create()
/*  78 */         .texOffs(0, 25).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 5.0F, 5.0F, g), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  82 */     root.addOrReplaceChild("left_hind_leg", 
/*  83 */         CubeListBuilder.create()
/*  84 */         .texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, g), 
/*  85 */         PartPose.offset(4.0F, 14.0F, 7.0F));
/*     */     
/*  87 */     root.addOrReplaceChild("right_hind_leg", 
/*  88 */         CubeListBuilder.create()
/*  89 */         .texOffs(48, 21).addBox(-1.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, g), 
/*  90 */         PartPose.offset(-4.0F, 14.0F, 7.0F));
/*     */     
/*  92 */     root.addOrReplaceChild("left_front_leg", 
/*  93 */         CubeListBuilder.create()
/*  94 */         .texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, g), 
/*  95 */         PartPose.offset(4.0F, 14.0F, -10.0F));
/*     */     
/*  97 */     root.addOrReplaceChild("right_front_leg", 
/*  98 */         CubeListBuilder.create()
/*  99 */         .texOffs(48, 21).addBox(-1.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, g), 
/* 100 */         PartPose.offset(-4.0F, 14.0F, -10.0F));
/*     */ 
/*     */     
/* 103 */     body.addOrReplaceChild("tail", 
/* 104 */         CubeListBuilder.create()
/* 105 */         .texOffs(42, 36).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 4.0F, g), 
/* 106 */         PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, 0.5235988F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 109 */     head.addOrReplaceChild("left_ear", 
/* 110 */         CubeListBuilder.create()
/* 111 */         .texOffs(19, 16).addBox(0.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.ZERO);
/*     */ 
/*     */     
/* 114 */     head.addOrReplaceChild("right_ear", 
/* 115 */         CubeListBuilder.create()
/* 116 */         .texOffs(19, 16).addBox(-2.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/* 120 */     return mesh;
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBabyMesh(CubeDeformation g) {
/* 124 */     return BABY_TRANSFORMER.apply(createFullScaleBabyMesh(g));
/*     */   }
/*     */   
/*     */   protected static MeshDefinition createFullScaleBabyMesh(CubeDeformation g) {
/* 128 */     MeshDefinition mesh = createBodyMesh(g);
/*     */     
/* 130 */     PartDefinition root = mesh.getRoot();
/*     */     
/* 132 */     CubeDeformation babyLegFudge = g.extend(0.0F, 5.5F, 0.0F);
/* 133 */     root.addOrReplaceChild("left_hind_leg", 
/* 134 */         CubeListBuilder.create()
/* 135 */         .texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, babyLegFudge), 
/* 136 */         PartPose.offset(4.0F, 14.0F, 7.0F));
/*     */     
/* 138 */     root.addOrReplaceChild("right_hind_leg", 
/* 139 */         CubeListBuilder.create()
/* 140 */         .texOffs(48, 21).addBox(-1.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, babyLegFudge), 
/* 141 */         PartPose.offset(-4.0F, 14.0F, 7.0F));
/*     */     
/* 143 */     root.addOrReplaceChild("left_front_leg", 
/* 144 */         CubeListBuilder.create()
/* 145 */         .texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, babyLegFudge), 
/* 146 */         PartPose.offset(4.0F, 14.0F, -10.0F));
/*     */     
/* 148 */     root.addOrReplaceChild("right_front_leg", 
/* 149 */         CubeListBuilder.create()
/* 150 */         .texOffs(48, 21).addBox(-1.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, babyLegFudge), 
/* 151 */         PartPose.offset(-4.0F, 14.0F, -10.0F));
/*     */     
/* 153 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(T state) {
/* 158 */     super.setupAnim(state);
/*     */ 
/*     */     
/* 161 */     float clampedYRot = Mth.clamp(((EquineRenderState)state).yRot, -20.0F, 20.0F);
/*     */     
/* 163 */     float headRotXRad = ((EquineRenderState)state).xRot * 0.017453292F;
/* 164 */     float animationSpeed = ((EquineRenderState)state).walkAnimationSpeed;
/* 165 */     float animationPos = ((EquineRenderState)state).walkAnimationPos;
/* 166 */     if (animationSpeed > 0.2F) {
/* 167 */       headRotXRad += Mth.cos((animationPos * 0.8F)) * 0.15F * animationSpeed;
/*     */     }
/*     */     
/* 170 */     float eating = ((EquineRenderState)state).eatAnimation;
/* 171 */     float standing = ((EquineRenderState)state).standAnimation;
/* 172 */     float iStanding = 1.0F - standing;
/* 173 */     float feedingAnim = ((EquineRenderState)state).feedingAnimation;
/* 174 */     boolean animateTail = ((EquineRenderState)state).animateTail;
/*     */     
/* 176 */     this.headParts.xRot = 0.5235988F + headRotXRad;
/* 177 */     this.headParts.yRot = clampedYRot * 0.017453292F;
/*     */     
/* 179 */     float waterMultiplier = ((EquineRenderState)state).isInWater ? 0.2F : 1.0F;
/* 180 */     float legAnim1 = Mth.cos((waterMultiplier * animationPos * 0.6662F + 3.1415927F));
/* 181 */     float legXRotAnim = legAnim1 * 0.8F * animationSpeed;
/*     */ 
/*     */     
/* 184 */     float baseHeadAngle = (1.0F - Math.max(standing, eating)) * (0.5235988F + headRotXRad + feedingAnim * Mth.sin(((EquineRenderState)state).ageInTicks) * 0.05F);
/* 185 */     this.headParts.xRot = standing * (0.2617994F + headRotXRad) + eating * (2.1816616F + Mth.sin(((EquineRenderState)state).ageInTicks) * 0.05F) + baseHeadAngle;
/* 186 */     this.headParts.yRot = standing * clampedYRot * 0.017453292F + (1.0F - Math.max(standing, eating)) * this.headParts.yRot;
/*     */     
/* 188 */     float ageScale = ((EquineRenderState)state).ageScale;
/*     */     
/* 190 */     this.headParts.y += Mth.lerp(eating, Mth.lerp(standing, 0.0F, -8.0F * ageScale), 7.0F * ageScale);
/* 191 */     this.headParts.z = Mth.lerp(standing, this.headParts.z, -4.0F * ageScale);
/*     */     
/* 193 */     this.body.xRot = standing * -0.7853982F + iStanding * this.body.xRot;
/*     */     
/* 195 */     float standAngle = 0.2617994F * standing;
/* 196 */     float bobValue = Mth.cos((((EquineRenderState)state).ageInTicks * 0.6F + 3.1415927F));
/*     */     
/* 198 */     this.leftFrontLeg.y -= 12.0F * ageScale * standing;
/* 199 */     this.leftFrontLeg.z += 4.0F * ageScale * standing;
/*     */     
/* 201 */     this.rightFrontLeg.y = this.leftFrontLeg.y;
/* 202 */     this.rightFrontLeg.z = this.leftFrontLeg.z;
/*     */     
/* 204 */     float rlegRot = (-1.0471976F + bobValue) * standing + legXRotAnim * iStanding;
/* 205 */     float llegRot = (-1.0471976F - bobValue) * standing - legXRotAnim * iStanding;
/*     */     
/* 207 */     this.leftHindLeg.xRot = standAngle - legAnim1 * 0.5F * animationSpeed * iStanding;
/* 208 */     this.rightHindLeg.xRot = standAngle + legAnim1 * 0.5F * animationSpeed * iStanding;
/* 209 */     this.leftFrontLeg.xRot = rlegRot;
/* 210 */     this.rightFrontLeg.xRot = llegRot;
/*     */     
/* 212 */     this.tail.xRot = 0.5235988F + animationSpeed * 0.75F;
/* 213 */     this.tail.y += animationSpeed * ageScale;
/* 214 */     this.tail.z += animationSpeed * 2.0F * ageScale;
/*     */     
/* 216 */     if (animateTail) {
/* 217 */       this.tail.yRot = Mth.cos((((EquineRenderState)state).ageInTicks * 0.7F));
/*     */     } else {
/* 219 */       this.tail.yRot = 0.0F;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/equine/AbstractEquineModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */