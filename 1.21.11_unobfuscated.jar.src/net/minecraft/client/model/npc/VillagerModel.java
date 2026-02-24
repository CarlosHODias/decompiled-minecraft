/*     */ package net.minecraft.client.model.npc;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HeadedModel;
/*     */ import net.minecraft.client.model.VillagerLikeModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.VillagerRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class VillagerModel extends EntityModel<VillagerRenderState> implements HeadedModel, VillagerLikeModel<VillagerRenderState> {
/*  19 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/*     */   
/*     */   private final ModelPart head;
/*     */   private final ModelPart rightLeg;
/*     */   private final ModelPart leftLeg;
/*     */   private final ModelPart arms;
/*     */   
/*     */   public VillagerModel(ModelPart root) {
/*  27 */     super(root);
/*  28 */     this.head = root.getChild("head");
/*  29 */     this.rightLeg = root.getChild("right_leg");
/*  30 */     this.leftLeg = root.getChild("left_leg");
/*  31 */     this.arms = root.getChild("arms");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBodyModel() {
/*  35 */     MeshDefinition mesh = new MeshDefinition();
/*  36 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  38 */     float offset = 0.5F;
/*     */     
/*  40 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  41 */         CubeListBuilder.create()
/*  42 */         .texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  45 */     PartDefinition hat = head.addOrReplaceChild("hat", 
/*  46 */         CubeListBuilder.create()
/*  47 */         .texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.ZERO);
/*     */ 
/*     */     
/*  50 */     hat.addOrReplaceChild("hat_rim", 
/*  51 */         CubeListBuilder.create()
/*  52 */         .texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F), 
/*  53 */         PartPose.rotation(-1.5707964F, 0.0F, 0.0F));
/*     */     
/*  55 */     head.addOrReplaceChild("nose", 
/*  56 */         CubeListBuilder.create()
/*  57 */         .texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), 
/*  58 */         PartPose.offset(0.0F, -2.0F, 0.0F));
/*     */     
/*  60 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  61 */         CubeListBuilder.create()
/*  62 */         .texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  65 */     body.addOrReplaceChild("jacket", 
/*  66 */         CubeListBuilder.create()
/*  67 */         .texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
/*     */ 
/*     */     
/*  70 */     root.addOrReplaceChild("arms", 
/*  71 */         CubeListBuilder.create()
/*  72 */         .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
/*  73 */         .texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, true)
/*  74 */         .texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F), 
/*  75 */         PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
/*     */     
/*  77 */     root.addOrReplaceChild("right_leg", 
/*  78 */         CubeListBuilder.create()
/*  79 */         .texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  80 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*     */     
/*  82 */     root.addOrReplaceChild("left_leg", 
/*  83 */         CubeListBuilder.create()
/*  84 */         .texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  85 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*     */ 
/*     */     
/*  88 */     return mesh;
/*     */   }
/*     */   
/*     */   public static MeshDefinition createNoHatModel() {
/*  92 */     MeshDefinition mesh = createBodyModel();
/*  93 */     mesh.getRoot().clearChild("head").clearRecursively();
/*  94 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(VillagerRenderState state) {
/*  99 */     super.setupAnim(state);
/*     */     
/* 101 */     this.head.yRot = state.yRot * 0.017453292F;
/* 102 */     this.head.xRot = state.xRot * 0.017453292F;
/*     */     
/* 104 */     if (state.isUnhappy) {
/* 105 */       this.head.zRot = 0.3F * Mth.sin((0.45F * state.ageInTicks));
/* 106 */       this.head.xRot = 0.4F;
/*     */     } else {
/* 108 */       this.head.zRot = 0.0F;
/*     */     } 
/*     */     
/* 111 */     this.rightLeg.xRot = Mth.cos((state.walkAnimationPos * 0.6662F)) * 1.4F * state.walkAnimationSpeed * 0.5F;
/* 112 */     this.leftLeg.xRot = Mth.cos((state.walkAnimationPos * 0.6662F + 3.1415927F)) * 1.4F * state.walkAnimationSpeed * 0.5F;
/* 113 */     this.rightLeg.yRot = 0.0F;
/* 114 */     this.leftLeg.yRot = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelPart getHead() {
/* 119 */     return this.head;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToArms(VillagerRenderState state, PoseStack outputPoseStack) {
/* 124 */     this.root.translateAndRotate(outputPoseStack);
/* 125 */     this.arms.translateAndRotate(outputPoseStack);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/npc/VillagerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */