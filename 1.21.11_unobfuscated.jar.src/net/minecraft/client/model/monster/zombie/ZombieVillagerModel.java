/*     */ package net.minecraft.client.model.monster.zombie;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.AnimationUtils;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.VillagerLikeModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ 
/*     */ public class ZombieVillagerModel<S extends ZombieVillagerRenderState> extends HumanoidModel<S> implements VillagerLikeModel<S> {
/*     */   public ZombieVillagerModel(ModelPart root) {
/*  22 */     super(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  26 */     MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
/*  27 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  29 */     PartDefinition head = root.addOrReplaceChild("head", new CubeListBuilder()
/*     */         
/*  31 */         .texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F)
/*  32 */         .texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  35 */     PartDefinition hat = head.addOrReplaceChild("hat", 
/*  36 */         CubeListBuilder.create()
/*  37 */         .texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
/*     */ 
/*     */     
/*  40 */     hat.addOrReplaceChild("hat_rim", 
/*  41 */         CubeListBuilder.create()
/*  42 */         .texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F), 
/*  43 */         PartPose.rotation(-1.5707964F, 0.0F, 0.0F));
/*     */     
/*  45 */     root.addOrReplaceChild("body", 
/*  46 */         CubeListBuilder.create()
/*  47 */         .texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F)
/*  48 */         .texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.05F)), PartPose.ZERO);
/*     */ 
/*     */     
/*  51 */     root.addOrReplaceChild("right_arm", 
/*  52 */         CubeListBuilder.create()
/*  53 */         .texOffs(44, 22).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  54 */         PartPose.offset(-5.0F, 2.0F, 0.0F));
/*     */     
/*  56 */     root.addOrReplaceChild("left_arm", 
/*  57 */         CubeListBuilder.create()
/*  58 */         .texOffs(44, 22).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  59 */         PartPose.offset(5.0F, 2.0F, 0.0F));
/*     */     
/*  61 */     root.addOrReplaceChild("right_leg", 
/*  62 */         CubeListBuilder.create()
/*  63 */         .texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  64 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*     */     
/*  66 */     root.addOrReplaceChild("left_leg", 
/*  67 */         CubeListBuilder.create()
/*  68 */         .texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  69 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*     */ 
/*     */     
/*  72 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createNoHatLayer() {
/*  76 */     return createBodyLayer().apply(mesh -> {
/*     */           mesh.getRoot().clearChild("head").clearRecursively();
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */   
/*     */   public static ArmorModelSet<LayerDefinition> createArmorLayerSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/*  83 */     return createArmorMeshSet(ZombieVillagerModel::createBaseArmorMesh, innerDeformation, outerDeformation)
/*  84 */       .map(mesh -> LayerDefinition.create(mesh, 64, 32));
/*     */   }
/*     */   
/*     */   private static MeshDefinition createBaseArmorMesh(CubeDeformation g) {
/*  88 */     MeshDefinition mesh = HumanoidModel.createMesh(g, 0.0F);
/*  89 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  91 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  92 */         CubeListBuilder.create()
/*  93 */         .texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, g), PartPose.ZERO);
/*     */ 
/*     */     
/*  96 */     root.addOrReplaceChild("body", 
/*  97 */         CubeListBuilder.create()
/*  98 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, g.extend(0.1F)), PartPose.ZERO);
/*     */ 
/*     */     
/* 101 */     root.addOrReplaceChild("right_leg", 
/* 102 */         CubeListBuilder.create()
/* 103 */         .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(0.1F)), 
/* 104 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*     */     
/* 106 */     root.addOrReplaceChild("left_leg", 
/* 107 */         CubeListBuilder.create()
/* 108 */         .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(0.1F)), 
/* 109 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*     */ 
/*     */ 
/*     */     
/* 113 */     head.getChild("hat").addOrReplaceChild("hat_rim", 
/* 114 */         CubeListBuilder.create(), PartPose.ZERO);
/*     */ 
/*     */     
/* 117 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(S state) {
/* 122 */     super.setupAnim((HumanoidRenderState)state);
/*     */     
/* 124 */     AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, ((ZombieVillagerRenderState)state).isAggressive, (UndeadRenderState)state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToArms(ZombieVillagerRenderState state, PoseStack outputPoseStack) {
/* 129 */     translateToHand((HumanoidRenderState)state, HumanoidArm.RIGHT, outputPoseStack);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/zombie/ZombieVillagerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */