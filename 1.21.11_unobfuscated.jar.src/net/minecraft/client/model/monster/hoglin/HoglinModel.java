/*     */ package net.minecraft.client.model.monster.hoglin;
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
/*     */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ public class HoglinModel
/*     */   extends EntityModel<HoglinRenderState>
/*     */ {
/*  21 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 8.0F, 6.0F, 1.9F, 2.0F, 24.0F, Set.of("head"));
/*     */   
/*     */   private static final float DEFAULT_HEAD_X_ROT = 0.87266463F;
/*     */   
/*     */   private static final float ATTACK_HEAD_X_ROT_END = -0.34906584F;
/*     */   private final ModelPart head;
/*     */   private final ModelPart rightEar;
/*     */   private final ModelPart leftEar;
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart mane;
/*     */   
/*     */   public HoglinModel(ModelPart root) {
/*  37 */     super(root);
/*  38 */     this.body = root.getChild("body");
/*  39 */     this.mane = this.body.getChild("mane");
/*  40 */     this.head = root.getChild("head");
/*  41 */     this.rightEar = this.head.getChild("right_ear");
/*  42 */     this.leftEar = this.head.getChild("left_ear");
/*  43 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  44 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  45 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  46 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*     */   }
/*     */   
/*     */   private static MeshDefinition createMesh() {
/*  50 */     MeshDefinition mesh = new MeshDefinition();
/*  51 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  53 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  54 */         CubeListBuilder.create()
/*  55 */         .texOffs(1, 1).addBox(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F), 
/*  56 */         PartPose.offset(0.0F, 7.0F, 0.0F));
/*  57 */     body.addOrReplaceChild("mane", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(90, 33).addBox(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.001F)), 
/*  60 */         PartPose.offset(0.0F, -14.0F, -7.0F));
/*     */     
/*  62 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(61, 1).addBox(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F), 
/*  65 */         PartPose.offsetAndRotation(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
/*     */     
/*  67 */     head.addOrReplaceChild("right_ear", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(1, 1).addBox(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), 
/*  70 */         PartPose.offsetAndRotation(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.6981317F));
/*     */     
/*  72 */     head.addOrReplaceChild("left_ear", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(1, 6).addBox(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), 
/*  75 */         PartPose.offsetAndRotation(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.6981317F));
/*     */     
/*  77 */     head.addOrReplaceChild("right_horn", 
/*  78 */         CubeListBuilder.create()
/*  79 */         .texOffs(10, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), 
/*  80 */         PartPose.offset(-7.0F, 2.0F, -12.0F));
/*     */     
/*  82 */     head.addOrReplaceChild("left_horn", 
/*  83 */         CubeListBuilder.create()
/*  84 */         .texOffs(1, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), 
/*  85 */         PartPose.offset(7.0F, 2.0F, -12.0F));
/*     */ 
/*     */     
/*  88 */     int frontLegHeight = 14;
/*  89 */     int backLegHeight = 11;
/*  90 */     root.addOrReplaceChild("right_front_leg", 
/*  91 */         CubeListBuilder.create()
/*  92 */         .texOffs(66, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), 
/*  93 */         PartPose.offset(-4.0F, 10.0F, -8.5F));
/*     */     
/*  95 */     root.addOrReplaceChild("left_front_leg", 
/*  96 */         CubeListBuilder.create()
/*  97 */         .texOffs(41, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), 
/*  98 */         PartPose.offset(4.0F, 10.0F, -8.5F));
/*     */     
/* 100 */     root.addOrReplaceChild("right_hind_leg", 
/* 101 */         CubeListBuilder.create()
/* 102 */         .texOffs(21, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), 
/* 103 */         PartPose.offset(-5.0F, 13.0F, 10.0F));
/*     */     
/* 105 */     root.addOrReplaceChild("left_hind_leg", 
/* 106 */         CubeListBuilder.create()
/* 107 */         .texOffs(0, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), 
/* 108 */         PartPose.offset(5.0F, 13.0F, 10.0F));
/*     */     
/* 110 */     return mesh;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/* 114 */     MeshDefinition mesh = createMesh();
/* 115 */     return LayerDefinition.create(mesh, 128, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBabyLayer() {
/* 119 */     MeshDefinition mesh = createMesh();
/*     */ 
/*     */     
/* 122 */     PartDefinition body = mesh.getRoot().getChild("body");
/* 123 */     body.addOrReplaceChild("mane", 
/* 124 */         CubeListBuilder.create()
/* 125 */         .texOffs(90, 33).addBox(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.001F)), 
/* 126 */         PartPose.offset(0.0F, -14.0F, -3.0F));
/*     */ 
/*     */     
/* 129 */     return LayerDefinition.create(mesh, 128, 64).apply(BABY_TRANSFORMER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(HoglinRenderState state) {
/* 134 */     super.setupAnim(state);
/*     */     
/* 136 */     float animationSpeed = state.walkAnimationSpeed;
/* 137 */     float animationPos = state.walkAnimationPos;
/* 138 */     this.rightEar.zRot = -0.6981317F - animationSpeed * Mth.sin(animationPos);
/* 139 */     this.leftEar.zRot = 0.6981317F + animationSpeed * Mth.sin(animationPos);
/* 140 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */ 
/*     */     
/* 143 */     float headbuttLerpFactor = 1.0F - Mth.abs(10 - 2 * state.attackAnimationRemainingTicks) / 10.0F;
/* 144 */     this.head.xRot = Mth.lerp(headbuttLerpFactor, 0.87266463F, -0.34906584F);
/* 145 */     if (state.isBaby)
/*     */     {
/* 147 */       this.head.y += headbuttLerpFactor * 2.5F;
/*     */     }
/*     */     
/* 150 */     float amplitudeMultiplier = 1.2F;
/* 151 */     this.rightFrontLeg.xRot = Mth.cos(animationPos) * 1.2F * animationSpeed;
/* 152 */     this.leftFrontLeg.xRot = Mth.cos((animationPos + 3.1415927F)) * 1.2F * animationSpeed;
/* 153 */     this.rightHindLeg.xRot = this.leftFrontLeg.xRot;
/* 154 */     this.leftHindLeg.xRot = this.rightFrontLeg.xRot;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/hoglin/HoglinModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */