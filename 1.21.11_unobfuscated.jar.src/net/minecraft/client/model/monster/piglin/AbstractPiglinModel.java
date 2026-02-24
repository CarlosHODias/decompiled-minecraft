/*     */ package net.minecraft.client.model.monster.piglin;
/*     */ 
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.model.player.PlayerModel;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ public class AbstractPiglinModel<S extends HumanoidRenderState>
/*     */   extends HumanoidModel<S>
/*     */ {
/*     */   private static final String LEFT_SLEEVE = "left_sleeve";
/*     */   private static final String RIGHT_SLEEVE = "right_sleeve";
/*     */   private static final String LEFT_PANTS = "left_pants";
/*     */   private static final String RIGHT_PANTS = "right_pants";
/*     */   public final ModelPart leftSleeve;
/*     */   public final ModelPart rightSleeve;
/*     */   public final ModelPart leftPants;
/*     */   public final ModelPart rightPants;
/*     */   public final ModelPart jacket;
/*     */   public final ModelPart rightEar;
/*     */   public final ModelPart leftEar;
/*     */   
/*     */   public AbstractPiglinModel(ModelPart root) {
/*  33 */     super(root, RenderTypes::entityTranslucent);
/*     */     
/*  35 */     this.leftSleeve = this.leftArm.getChild("left_sleeve");
/*  36 */     this.rightSleeve = this.rightArm.getChild("right_sleeve");
/*  37 */     this.leftPants = this.leftLeg.getChild("left_pants");
/*  38 */     this.rightPants = this.rightLeg.getChild("right_pants");
/*  39 */     this.jacket = this.body.getChild("jacket");
/*     */     
/*  41 */     this.rightEar = this.head.getChild("right_ear");
/*  42 */     this.leftEar = this.head.getChild("left_ear");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createMesh(CubeDeformation g) {
/*  46 */     MeshDefinition mesh = PlayerModel.createMesh(g, false);
/*     */     
/*  48 */     PartDefinition root = mesh.getRoot();
/*  49 */     root.addOrReplaceChild("body", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, g), PartPose.ZERO);
/*     */ 
/*     */     
/*  54 */     PartDefinition head = addHead(g, mesh);
/*  55 */     head.clearChild("hat");
/*  56 */     return mesh;
/*     */   }
/*     */   
/*     */   public static ArmorModelSet<MeshDefinition> createArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/*  60 */     return PlayerModel.createArmorMeshSet(innerDeformation, outerDeformation).map(mesh -> {
/*     */           PartDefinition root = mesh.getRoot(), head = root.getChild("head");
/*     */           head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.ZERO);
/*     */           head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.ZERO);
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static PartDefinition addHead(CubeDeformation g, MeshDefinition mesh) {
/*  70 */     PartDefinition root = mesh.getRoot();
/*  71 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  72 */         CubeListBuilder.create()
/*  73 */         .texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, g)
/*  74 */         .texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, g)
/*  75 */         .texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, g)
/*  76 */         .texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, g), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  80 */     head.addOrReplaceChild("left_ear", 
/*  81 */         CubeListBuilder.create()
/*  82 */         .texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, g), 
/*  83 */         PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));
/*     */     
/*  85 */     head.addOrReplaceChild("right_ear", 
/*  86 */         CubeListBuilder.create()
/*  87 */         .texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, g), 
/*  88 */         PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));
/*     */     
/*  90 */     return head;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(S state) {
/*  95 */     super.setupAnim((HumanoidRenderState)state);
/*     */     
/*  97 */     float animationPos = ((HumanoidRenderState)state).walkAnimationPos;
/*  98 */     float animationSpeed = ((HumanoidRenderState)state).walkAnimationSpeed;
/*  99 */     float defaultAngle = 0.5235988F;
/* 100 */     float frequency = ((HumanoidRenderState)state).ageInTicks * 0.1F + animationPos * 0.5F;
/* 101 */     float amplitude = 0.08F + animationSpeed * 0.4F;
/* 102 */     this.leftEar.zRot = -0.5235988F - Mth.cos((frequency * 1.2F)) * amplitude;
/* 103 */     this.rightEar.zRot = 0.5235988F + Mth.cos(frequency) * amplitude;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllVisible(boolean visible) {
/* 108 */     super.setAllVisible(visible);
/* 109 */     this.leftSleeve.visible = visible;
/* 110 */     this.rightSleeve.visible = visible;
/* 111 */     this.leftPants.visible = visible;
/* 112 */     this.rightPants.visible = visible;
/* 113 */     this.jacket.visible = visible;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/piglin/AbstractPiglinModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */