/*     */ package net.minecraft.client.model.player;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ 
/*     */ public class PlayerModel
/*     */   extends HumanoidModel<AvatarRenderState>
/*     */ {
/*     */   protected static final String LEFT_SLEEVE = "left_sleeve";
/*     */   protected static final String RIGHT_SLEEVE = "right_sleeve";
/*     */   protected static final String LEFT_PANTS = "left_pants";
/*     */   protected static final String RIGHT_PANTS = "right_pants";
/*     */   private final List<ModelPart> bodyParts;
/*     */   public final ModelPart leftSleeve;
/*     */   public final ModelPart rightSleeve;
/*     */   public final ModelPart leftPants;
/*     */   public final ModelPart rightPants;
/*     */   public final ModelPart jacket;
/*     */   private final boolean slim;
/*     */   
/*     */   public PlayerModel(ModelPart root, boolean slim) {
/*  37 */     super(root, RenderTypes::entityTranslucent);
/*  38 */     this.slim = slim;
/*     */     
/*  40 */     this.leftSleeve = this.leftArm.getChild("left_sleeve");
/*  41 */     this.rightSleeve = this.rightArm.getChild("right_sleeve");
/*  42 */     this.leftPants = this.leftLeg.getChild("left_pants");
/*  43 */     this.rightPants = this.rightLeg.getChild("right_pants");
/*  44 */     this.jacket = this.body.getChild("jacket");
/*     */     
/*  46 */     this.bodyParts = List.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
/*     */   }
/*     */   
/*     */   public static MeshDefinition createMesh(CubeDeformation scale, boolean slim) {
/*  50 */     MeshDefinition mesh = HumanoidModel.createMesh(scale, 0.0F);
/*  51 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  53 */     float overlayScale = 0.25F;
/*  54 */     if (slim) {
/*  55 */       PartDefinition leftArm = root.addOrReplaceChild("left_arm", 
/*  56 */           CubeListBuilder.create()
/*  57 */           .texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, scale), 
/*  58 */           PartPose.offset(5.0F, 2.0F, 0.0F));
/*     */       
/*  60 */       PartDefinition rightArm = root.addOrReplaceChild("right_arm", 
/*  61 */           CubeListBuilder.create()
/*  62 */           .texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, scale), 
/*  63 */           PartPose.offset(-5.0F, 2.0F, 0.0F));
/*     */       
/*  65 */       leftArm.addOrReplaceChild("left_sleeve", 
/*  66 */           CubeListBuilder.create()
/*  67 */           .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */ 
/*     */       
/*  70 */       rightArm.addOrReplaceChild("right_sleeve", 
/*  71 */           CubeListBuilder.create()
/*  72 */           .texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */     }
/*     */     else {
/*     */       
/*  76 */       PartDefinition leftArm = root.addOrReplaceChild("left_arm", 
/*  77 */           CubeListBuilder.create()
/*  78 */           .texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale), 
/*  79 */           PartPose.offset(5.0F, 2.0F, 0.0F));
/*     */ 
/*     */       
/*  82 */       PartDefinition rightArm = root.getChild("right_arm");
/*  83 */       leftArm.addOrReplaceChild("left_sleeve", 
/*  84 */           CubeListBuilder.create()
/*  85 */           .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */ 
/*     */       
/*  88 */       rightArm.addOrReplaceChild("right_sleeve", 
/*  89 */           CubeListBuilder.create()
/*  90 */           .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  95 */     PartDefinition leftLeg = root.addOrReplaceChild("left_leg", 
/*  96 */         CubeListBuilder.create()
/*  97 */         .texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale), 
/*  98 */         PartPose.offset(1.9F, 12.0F, 0.0F));
/*     */     
/* 100 */     PartDefinition rightLeg = root.getChild("right_leg");
/* 101 */     leftLeg.addOrReplaceChild("left_pants", 
/* 102 */         CubeListBuilder.create()
/* 103 */         .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */ 
/*     */     
/* 106 */     rightLeg.addOrReplaceChild("right_pants", 
/* 107 */         CubeListBuilder.create()
/* 108 */         .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */ 
/*     */     
/* 111 */     PartDefinition body = root.getChild("body");
/* 112 */     body.addOrReplaceChild("jacket", 
/* 113 */         CubeListBuilder.create()
/* 114 */         .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale.extend(0.25F)), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/* 118 */     return mesh;
/*     */   }
/*     */   
/*     */   public static ArmorModelSet<MeshDefinition> createArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 122 */     return HumanoidModel.createArmorMeshSet(innerDeformation, outerDeformation).map(mesh -> {
/*     */           PartDefinition root = mesh.getRoot(), leftArm = root.getChild("left_arm"), rightArm = root.getChild("right_arm");
/*     */           leftArm.addOrReplaceChild("left_sleeve", CubeListBuilder.create(), PartPose.ZERO);
/*     */           rightArm.addOrReplaceChild("right_sleeve", CubeListBuilder.create(), PartPose.ZERO);
/*     */           PartDefinition leftLeg = root.getChild("left_leg"), rightLeg = root.getChild("right_leg");
/*     */           leftLeg.addOrReplaceChild("left_pants", CubeListBuilder.create(), PartPose.ZERO);
/*     */           rightLeg.addOrReplaceChild("right_pants", CubeListBuilder.create(), PartPose.ZERO);
/*     */           PartDefinition body = root.getChild("body");
/*     */           body.addOrReplaceChild("jacket", CubeListBuilder.create(), PartPose.ZERO);
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setupAnim(AvatarRenderState state) {
/* 140 */     boolean showBody = !state.isSpectator;
/* 141 */     this.body.visible = showBody;
/* 142 */     this.rightArm.visible = showBody;
/* 143 */     this.leftArm.visible = showBody;
/* 144 */     this.rightLeg.visible = showBody;
/* 145 */     this.leftLeg.visible = showBody;
/* 146 */     this.hat.visible = state.showHat;
/* 147 */     this.jacket.visible = state.showJacket;
/* 148 */     this.leftPants.visible = state.showLeftPants;
/* 149 */     this.rightPants.visible = state.showRightPants;
/* 150 */     this.leftSleeve.visible = state.showLeftSleeve;
/* 151 */     this.rightSleeve.visible = state.showRightSleeve;
/* 152 */     super.setupAnim((HumanoidRenderState)state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllVisible(boolean visible) {
/* 157 */     super.setAllVisible(visible);
/* 158 */     this.leftSleeve.visible = visible;
/* 159 */     this.rightSleeve.visible = visible;
/* 160 */     this.leftPants.visible = visible;
/* 161 */     this.rightPants.visible = visible;
/* 162 */     this.jacket.visible = visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(AvatarRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 167 */     root().translateAndRotate(poseStack);
/* 168 */     ModelPart part = getArm(arm);
/* 169 */     if (this.slim) {
/* 170 */       float offset = 0.5F * ((arm == HumanoidArm.RIGHT) ? true : -1);
/* 171 */       part.x += offset;
/* 172 */       part.translateAndRotate(poseStack);
/* 173 */       part.x -= offset;
/*     */     } else {
/* 175 */       part.translateAndRotate(poseStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ModelPart getRandomBodyPart(RandomSource random) {
/* 180 */     return (ModelPart)Util.getRandom(this.bodyParts, random);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/player/PlayerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */