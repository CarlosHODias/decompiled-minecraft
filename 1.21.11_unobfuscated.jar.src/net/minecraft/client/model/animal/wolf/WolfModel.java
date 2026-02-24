/*     */ package net.minecraft.client.model.animal.wolf;
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
/*     */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class WolfModel
/*     */   extends EntityModel<WolfRenderState>
/*     */ {
/*  19 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(Set.of("head"));
/*     */   
/*     */   private static final String REAL_HEAD = "real_head";
/*     */   
/*     */   private static final String UPPER_BODY = "upper_body";
/*     */   
/*     */   private static final String REAL_TAIL = "real_tail";
/*     */   private final ModelPart head;
/*     */   private final ModelPart realHead;
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart tail;
/*     */   private final ModelPart realTail;
/*     */   private final ModelPart upperBody;
/*     */   private static final int LEG_SIZE = 8;
/*     */   
/*     */   public WolfModel(ModelPart root) {
/*  39 */     super(root);
/*  40 */     this.head = root.getChild("head");
/*  41 */     this.realHead = this.head.getChild("real_head");
/*  42 */     this.body = root.getChild("body");
/*  43 */     this.upperBody = root.getChild("upper_body");
/*  44 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  45 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  46 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  47 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  48 */     this.tail = root.getChild("tail");
/*  49 */     this.realTail = this.tail.getChild("real_tail");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createMeshDefinition(CubeDeformation g) {
/*  53 */     MeshDefinition mesh = new MeshDefinition();
/*  54 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  56 */     float headHeight = 13.5F;
/*     */     
/*  58 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  59 */         CubeListBuilder.create(), 
/*  60 */         PartPose.offset(-1.0F, 13.5F, -7.0F));
/*     */     
/*  62 */     head.addOrReplaceChild("real_head", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, g)
/*  65 */         .texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, g)
/*  66 */         .texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, g)
/*  67 */         .texOffs(0, 10).addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F, g), PartPose.ZERO);
/*     */ 
/*     */     
/*  70 */     root.addOrReplaceChild("body", 
/*  71 */         CubeListBuilder.create()
/*  72 */         .texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, g), 
/*  73 */         PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  75 */     root.addOrReplaceChild("upper_body", 
/*  76 */         CubeListBuilder.create()
/*  77 */         .texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, g), 
/*  78 */         PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5707964F, 0.0F, 0.0F));
/*     */ 
/*     */     
/*  81 */     CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, g);
/*  82 */     CubeListBuilder rightLeg = CubeListBuilder.create().mirror().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, g);
/*  83 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-2.5F, 16.0F, 7.0F));
/*  84 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(0.5F, 16.0F, 7.0F));
/*  85 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-2.5F, 16.0F, -4.0F));
/*  86 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(0.5F, 16.0F, -4.0F));
/*     */     
/*  88 */     PartDefinition tail = root.addOrReplaceChild("tail", 
/*  89 */         CubeListBuilder.create(), 
/*  90 */         PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, 0.62831855F, 0.0F, 0.0F));
/*     */     
/*  92 */     tail.addOrReplaceChild("real_tail", 
/*  93 */         CubeListBuilder.create()
/*  94 */         .texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, g), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  98 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(WolfRenderState state) {
/* 103 */     super.setupAnim(state);
/*     */     
/* 105 */     float animationPos = state.walkAnimationPos;
/* 106 */     float animationSpeed = state.walkAnimationSpeed;
/*     */     
/* 108 */     if (state.isAngry) {
/* 109 */       this.tail.yRot = 0.0F;
/*     */     } else {
/* 111 */       this.tail.yRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*     */     } 
/*     */     
/* 114 */     if (state.isSitting) {
/* 115 */       float ageScale = state.ageScale;
/*     */       
/* 117 */       this.upperBody.y += 2.0F * ageScale;
/* 118 */       this.upperBody.xRot = 1.2566371F;
/* 119 */       this.upperBody.yRot = 0.0F;
/*     */       
/* 121 */       this.body.y += 4.0F * ageScale;
/* 122 */       this.body.z -= 2.0F * ageScale;
/* 123 */       this.body.xRot = 0.7853982F;
/*     */       
/* 125 */       this.tail.y += 9.0F * ageScale;
/* 126 */       this.tail.z -= 2.0F * ageScale;
/*     */       
/* 128 */       this.rightHindLeg.y += 6.7F * ageScale;
/* 129 */       this.rightHindLeg.z -= 5.0F * ageScale;
/* 130 */       this.rightHindLeg.xRot = 4.712389F;
/* 131 */       this.leftHindLeg.y += 6.7F * ageScale;
/* 132 */       this.leftHindLeg.z -= 5.0F * ageScale;
/* 133 */       this.leftHindLeg.xRot = 4.712389F;
/*     */       
/* 135 */       this.rightFrontLeg.xRot = 5.811947F;
/* 136 */       this.rightFrontLeg.x += 0.01F * ageScale;
/* 137 */       this.rightFrontLeg.y += 1.0F * ageScale;
/* 138 */       this.leftFrontLeg.xRot = 5.811947F;
/* 139 */       this.leftFrontLeg.x -= 0.01F * ageScale;
/* 140 */       this.leftFrontLeg.y += 1.0F * ageScale;
/*     */     } else {
/* 142 */       this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 143 */       this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 144 */       this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 145 */       this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*     */     } 
/*     */     
/* 148 */     this.realHead.zRot = state.headRollAngle + state.getBodyRollAngle(0.0F);
/*     */     
/* 150 */     this.upperBody.zRot = state.getBodyRollAngle(-0.08F);
/* 151 */     this.body.zRot = state.getBodyRollAngle(-0.16F);
/* 152 */     this.realTail.zRot = state.getBodyRollAngle(-0.2F);
/*     */     
/* 154 */     this.head.xRot = state.xRot * 0.017453292F;
/* 155 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */     
/* 157 */     this.tail.xRot = state.tailAngle;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/wolf/WolfModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */