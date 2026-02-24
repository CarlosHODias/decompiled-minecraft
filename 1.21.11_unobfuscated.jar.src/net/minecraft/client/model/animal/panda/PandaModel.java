/*     */ package net.minecraft.client.model.animal.panda;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.model.BabyModelTransform;
/*     */ import net.minecraft.client.model.QuadrupedModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.PandaRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class PandaModel
/*     */   extends QuadrupedModel<PandaRenderState> {
/*  19 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 23.0F, 4.8F, 2.7F, 3.0F, 49.0F, Set.of("head"));
/*     */   
/*     */   public PandaModel(ModelPart root) {
/*  22 */     super(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  26 */     MeshDefinition mesh = new MeshDefinition();
/*  27 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  29 */     root.addOrReplaceChild("head", 
/*  30 */         CubeListBuilder.create()
/*  31 */         .texOffs(0, 6).addBox(-6.5F, -5.0F, -4.0F, 13.0F, 10.0F, 9.0F)
/*  32 */         .texOffs(45, 16).addBox("nose", -3.5F, 0.0F, -6.0F, 7.0F, 5.0F, 2.0F)
/*  33 */         .texOffs(52, 25).addBox("left_ear", 3.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F)
/*  34 */         .texOffs(52, 25).addBox("right_ear", -8.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F), 
/*  35 */         PartPose.offset(0.0F, 11.5F, -17.0F));
/*     */     
/*  37 */     root.addOrReplaceChild("body", 
/*  38 */         CubeListBuilder.create()
/*  39 */         .texOffs(0, 25).addBox(-9.5F, -13.0F, -6.5F, 19.0F, 26.0F, 13.0F), 
/*  40 */         PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
/*     */ 
/*     */     
/*  43 */     int legH = 9;
/*  44 */     int legW = 6;
/*  45 */     CubeListBuilder leg = CubeListBuilder.create()
/*  46 */       .texOffs(40, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F);
/*  47 */     root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-5.5F, 15.0F, 9.0F));
/*  48 */     root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(5.5F, 15.0F, 9.0F));
/*  49 */     root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-5.5F, 15.0F, -9.0F));
/*  50 */     root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(5.5F, 15.0F, -9.0F));
/*     */     
/*  52 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(PandaRenderState state) {
/*  57 */     super.setupAnim((LivingEntityRenderState)state);
/*     */     
/*  59 */     if (state.isUnhappy) {
/*  60 */       this.head.yRot = 0.35F * Mth.sin((0.6F * state.ageInTicks));
/*  61 */       this.head.zRot = 0.35F * Mth.sin((0.6F * state.ageInTicks));
/*     */       
/*  63 */       this.rightFrontLeg.xRot = -0.75F * Mth.sin((0.3F * state.ageInTicks));
/*  64 */       this.leftFrontLeg.xRot = 0.75F * Mth.sin((0.3F * state.ageInTicks));
/*     */     } else {
/*  66 */       this.head.zRot = 0.0F;
/*     */     } 
/*     */     
/*  69 */     if (state.isSneezing) {
/*  70 */       if (state.sneezeTime < 15) {
/*  71 */         this.head.xRot = -0.7853982F * state.sneezeTime / 14.0F;
/*  72 */       } else if (state.sneezeTime < 20) {
/*  73 */         float internalSneezePos = ((state.sneezeTime - 15) / 5);
/*  74 */         this.head.xRot = -0.7853982F + 0.7853982F * internalSneezePos;
/*     */       } 
/*     */     }
/*     */     
/*  78 */     if (state.sitAmount > 0.0F) {
/*  79 */       this.body.xRot = Mth.rotLerpRad(state.sitAmount, this.body.xRot, 1.7407963F);
/*  80 */       this.head.xRot = Mth.rotLerpRad(state.sitAmount, this.head.xRot, 1.5707964F);
/*     */ 
/*     */       
/*  83 */       this.rightFrontLeg.zRot = -0.27079642F;
/*  84 */       this.leftFrontLeg.zRot = 0.27079642F;
/*     */ 
/*     */       
/*  87 */       this.rightHindLeg.zRot = 0.5707964F;
/*  88 */       this.leftHindLeg.zRot = -0.5707964F;
/*     */       
/*  90 */       if (state.isEating) {
/*  91 */         this.head.xRot = 1.5707964F + 0.2F * Mth.sin((state.ageInTicks * 0.6F));
/*     */         
/*  93 */         this.rightFrontLeg.xRot = -0.4F - 0.2F * Mth.sin((state.ageInTicks * 0.6F));
/*  94 */         this.leftFrontLeg.xRot = -0.4F - 0.2F * Mth.sin((state.ageInTicks * 0.6F));
/*     */       } 
/*     */       
/*  97 */       if (state.isScared) {
/*  98 */         this.head.xRot = 2.1707964F;
/*  99 */         this.rightFrontLeg.xRot = -0.9F;
/* 100 */         this.leftFrontLeg.xRot = -0.9F;
/*     */       } 
/*     */     } else {
/* 103 */       this.rightHindLeg.zRot = 0.0F;
/* 104 */       this.leftHindLeg.zRot = 0.0F;
/* 105 */       this.rightFrontLeg.zRot = 0.0F;
/* 106 */       this.leftFrontLeg.zRot = 0.0F;
/*     */     } 
/*     */     
/* 109 */     if (state.lieOnBackAmount > 0.0F) {
/* 110 */       this.rightHindLeg.xRot = -0.6F * Mth.sin((state.ageInTicks * 0.15F));
/* 111 */       this.leftHindLeg.xRot = 0.6F * Mth.sin((state.ageInTicks * 0.15F));
/* 112 */       this.rightFrontLeg.xRot = 0.3F * Mth.sin((state.ageInTicks * 0.25F));
/* 113 */       this.leftFrontLeg.xRot = -0.3F * Mth.sin((state.ageInTicks * 0.25F));
/*     */       
/* 115 */       this.head.xRot = Mth.rotLerpRad(state.lieOnBackAmount, this.head.xRot, 1.5707964F);
/*     */     } 
/*     */     
/* 118 */     if (state.rollAmount > 0.0F) {
/* 119 */       this.head.xRot = Mth.rotLerpRad(state.rollAmount, this.head.xRot, 2.0561945F);
/*     */       
/* 121 */       this.rightHindLeg.xRot = -0.5F * Mth.sin((state.ageInTicks * 0.5F));
/* 122 */       this.leftHindLeg.xRot = 0.5F * Mth.sin((state.ageInTicks * 0.5F));
/* 123 */       this.rightFrontLeg.xRot = 0.5F * Mth.sin((state.ageInTicks * 0.5F));
/* 124 */       this.leftFrontLeg.xRot = -0.5F * Mth.sin((state.ageInTicks * 0.5F));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/panda/PandaModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */