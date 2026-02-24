/*     */ package net.minecraft.client.model.monster.witch;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HeadedModel;
/*     */ import net.minecraft.client.model.VillagerLikeModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.model.npc.VillagerModel;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.WitchRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class WitchModel extends EntityModel<WitchRenderState> implements HeadedModel, VillagerLikeModel<WitchRenderState> {
/*     */   protected final ModelPart nose;
/*     */   private final ModelPart head;
/*     */   private final ModelPart rightLeg;
/*     */   private final ModelPart leftLeg;
/*     */   private final ModelPart arms;
/*     */   
/*     */   public WitchModel(ModelPart root) {
/*  27 */     super(root);
/*  28 */     this.head = root.getChild("head");
/*  29 */     this.nose = this.head.getChild("nose");
/*  30 */     this.rightLeg = root.getChild("right_leg");
/*  31 */     this.leftLeg = root.getChild("left_leg");
/*  32 */     this.arms = root.getChild("arms");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  36 */     MeshDefinition mesh = VillagerModel.createBodyModel();
/*  37 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  39 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  40 */         CubeListBuilder.create()
/*  41 */         .texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  44 */     PartDefinition hat = head.addOrReplaceChild("hat", 
/*  45 */         CubeListBuilder.create()
/*  46 */         .texOffs(0, 64).addBox(0.0F, 0.0F, 0.0F, 10.0F, 2.0F, 10.0F), 
/*  47 */         PartPose.offset(-5.0F, -10.03125F, -5.0F));
/*     */     
/*  49 */     PartDefinition hat2 = hat.addOrReplaceChild("hat2", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 7.0F, 4.0F, 7.0F), 
/*  52 */         PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.05235988F, 0.0F, 0.02617994F));
/*     */     
/*  54 */     PartDefinition hat3 = hat2.addOrReplaceChild("hat3", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(0, 87).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F), 
/*  57 */         PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.10471976F, 0.0F, 0.05235988F));
/*     */     
/*  59 */     hat3.addOrReplaceChild("hat4", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .texOffs(0, 95).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)), 
/*  62 */         PartPose.offsetAndRotation(1.75F, -2.0F, 2.0F, -0.20943952F, 0.0F, 0.10471976F));
/*     */ 
/*     */     
/*  65 */     PartDefinition nose = head.getChild("nose");
/*  66 */     nose.addOrReplaceChild("mole", 
/*  67 */         CubeListBuilder.create()
/*  68 */         .texOffs(0, 0).addBox(0.0F, 3.0F, -6.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), 
/*  69 */         PartPose.offset(0.0F, -2.0F, 0.0F));
/*     */ 
/*     */     
/*  72 */     return LayerDefinition.create(mesh, 64, 128);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(WitchRenderState state) {
/*  77 */     super.setupAnim(state);
/*     */     
/*  79 */     this.head.yRot = state.yRot * 0.017453292F;
/*  80 */     this.head.xRot = state.xRot * 0.017453292F;
/*     */     
/*  82 */     this.rightLeg.xRot = Mth.cos((state.walkAnimationPos * 0.6662F)) * 1.4F * state.walkAnimationSpeed * 0.5F;
/*  83 */     this.leftLeg.xRot = Mth.cos((state.walkAnimationPos * 0.6662F + 3.1415927F)) * 1.4F * state.walkAnimationSpeed * 0.5F;
/*     */     
/*  85 */     float speed = 0.01F * (state.entityId % 10);
/*  86 */     this.nose.xRot = Mth.sin((state.ageInTicks * speed)) * 4.5F * 0.017453292F;
/*  87 */     this.nose.zRot = Mth.cos((state.ageInTicks * speed)) * 2.5F * 0.017453292F;
/*     */     
/*  89 */     if (state.isHoldingItem) {
/*  90 */       this.nose.setPos(0.0F, 1.0F, -1.5F);
/*  91 */       this.nose.xRot = -0.9F;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ModelPart getNose() {
/*  96 */     return this.nose;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelPart getHead() {
/* 101 */     return this.head;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToArms(WitchRenderState state, PoseStack outputPoseStack) {
/* 106 */     this.root.translateAndRotate(outputPoseStack);
/* 107 */     this.arms.translateAndRotate(outputPoseStack);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/witch/WitchModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */