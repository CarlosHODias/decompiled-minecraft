/*     */ package net.minecraft.client.model.animal.parrot;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.ParrotRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ 
/*     */ public class ParrotModel
/*     */   extends EntityModel<ParrotRenderState>
/*     */ {
/*     */   private static final String FEATHER = "feather";
/*     */   private final ModelPart body;
/*     */   private final ModelPart tail;
/*     */   private final ModelPart leftWing;
/*     */   private final ModelPart rightWing;
/*     */   private final ModelPart head;
/*     */   private final ModelPart leftLeg;
/*     */   private final ModelPart rightLeg;
/*     */   
/*     */   public enum Pose {
/*  27 */     FLYING,
/*  28 */     STANDING,
/*  29 */     SITTING,
/*  30 */     PARTY,
/*  31 */     ON_SHOULDER;
/*     */   }
/*     */   
/*     */   public ParrotModel(ModelPart root) {
/*  35 */     super(root);
/*  36 */     this.body = root.getChild("body");
/*  37 */     this.tail = root.getChild("tail");
/*  38 */     this.leftWing = root.getChild("left_wing");
/*  39 */     this.rightWing = root.getChild("right_wing");
/*  40 */     this.head = root.getChild("head");
/*  41 */     this.leftLeg = root.getChild("left_leg");
/*  42 */     this.rightLeg = root.getChild("right_leg");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  46 */     MeshDefinition mesh = new MeshDefinition();
/*  47 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  49 */     root.addOrReplaceChild("body", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(2, 8).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/*  52 */         PartPose.offsetAndRotation(0.0F, 16.5F, -3.0F, 0.4937F, 0.0F, 0.0F));
/*     */     
/*  54 */     root.addOrReplaceChild("tail", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(22, 1).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F), 
/*  57 */         PartPose.offsetAndRotation(0.0F, 21.07F, 1.16F, 1.015F, 0.0F, 0.0F));
/*     */     
/*  59 */     root.addOrReplaceChild("left_wing", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .texOffs(19, 8).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), 
/*  62 */         PartPose.offsetAndRotation(1.5F, 16.94F, -2.76F, -0.6981F, -3.1415927F, 0.0F));
/*     */     
/*  64 */     root.addOrReplaceChild("right_wing", 
/*  65 */         CubeListBuilder.create()
/*  66 */         .texOffs(19, 8).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), 
/*  67 */         PartPose.offsetAndRotation(-1.5F, 16.94F, -2.76F, -0.6981F, -3.1415927F, 0.0F));
/*     */     
/*  69 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  70 */         CubeListBuilder.create()
/*  71 */         .texOffs(2, 2).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F), 
/*  72 */         PartPose.offset(0.0F, 15.69F, -2.76F));
/*     */     
/*  74 */     head.addOrReplaceChild("head2", 
/*  75 */         CubeListBuilder.create()
/*  76 */         .texOffs(10, 0).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F), 
/*  77 */         PartPose.offset(0.0F, -2.0F, -1.0F));
/*     */     
/*  79 */     head.addOrReplaceChild("beak1", 
/*  80 */         CubeListBuilder.create()
/*  81 */         .texOffs(11, 7).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F), 
/*  82 */         PartPose.offset(0.0F, -0.5F, -1.5F));
/*     */     
/*  84 */     head.addOrReplaceChild("beak2", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(16, 7).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), 
/*  87 */         PartPose.offset(0.0F, -1.75F, -2.45F));
/*     */     
/*  89 */     head.addOrReplaceChild("feather", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(2, 18).addBox(0.0F, -4.0F, -2.0F, 0.0F, 5.0F, 4.0F), 
/*  92 */         PartPose.offsetAndRotation(0.0F, -2.15F, 0.15F, -0.2214F, 0.0F, 0.0F));
/*     */     
/*  94 */     CubeListBuilder leg = CubeListBuilder.create()
/*  95 */       .texOffs(14, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
/*  96 */     root.addOrReplaceChild("left_leg", leg, PartPose.offsetAndRotation(1.0F, 22.0F, -1.05F, -0.0299F, 0.0F, 0.0F));
/*  97 */     root.addOrReplaceChild("right_leg", leg, PartPose.offsetAndRotation(-1.0F, 22.0F, -1.05F, -0.0299F, 0.0F, 0.0F));
/*     */     
/*  99 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */   
/*     */   public void setupAnim(ParrotRenderState state) {
/*     */     float xPos, yPos, bobbingBody;
/* 104 */     super.setupAnim(state);
/*     */     
/* 106 */     prepare(state.pose);
/*     */     
/* 108 */     this.head.xRot = state.xRot * 0.017453292F;
/* 109 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */     
/* 111 */     switch (state.pose.ordinal()) {
/*     */       case 2:
/*     */         break;
/*     */       case 3:
/* 115 */         xPos = Mth.cos(state.ageInTicks);
/* 116 */         yPos = Mth.sin(state.ageInTicks);
/* 117 */         this.head.x += xPos;
/* 118 */         this.head.y += yPos;
/*     */         
/* 120 */         this.head.xRot = 0.0F;
/* 121 */         this.head.yRot = 0.0F;
/* 122 */         this.head.zRot = Mth.sin(state.ageInTicks) * 0.4F;
/*     */         
/* 124 */         this.body.x += xPos;
/* 125 */         this.body.y += yPos;
/*     */         
/* 127 */         this.leftWing.zRot = -0.0873F - state.flapAngle;
/* 128 */         this.leftWing.x += xPos;
/* 129 */         this.leftWing.y += yPos;
/*     */         
/* 131 */         this.rightWing.zRot = 0.0873F + state.flapAngle;
/* 132 */         this.rightWing.x += xPos;
/* 133 */         this.rightWing.y += yPos;
/*     */         
/* 135 */         this.tail.x += xPos;
/* 136 */         this.tail.y += yPos;
/*     */         break;
/*     */       
/*     */       case 1:
/* 140 */         this.leftLeg.xRot += Mth.cos((state.walkAnimationPos * 0.6662F)) * 1.4F * state.walkAnimationSpeed;
/* 141 */         this.rightLeg.xRot += Mth.cos((state.walkAnimationPos * 0.6662F + 3.1415927F)) * 1.4F * state.walkAnimationSpeed;
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 146 */         bobbingBody = state.flapAngle * 0.3F;
/* 147 */         this.head.y += bobbingBody;
/*     */         
/* 149 */         this.tail.xRot += Mth.cos((state.walkAnimationPos * 0.6662F)) * 0.3F * state.walkAnimationSpeed;
/* 150 */         this.tail.y += bobbingBody;
/*     */         
/* 152 */         this.body.y += bobbingBody;
/*     */         
/* 154 */         this.leftWing.zRot = -0.0873F - state.flapAngle;
/* 155 */         this.leftWing.y += bobbingBody;
/*     */         
/* 157 */         this.rightWing.zRot = 0.0873F + state.flapAngle;
/* 158 */         this.rightWing.y += bobbingBody;
/*     */         
/* 160 */         this.leftLeg.y += bobbingBody;
/* 161 */         this.rightLeg.y += bobbingBody;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   private void prepare(Pose pose) {
/*     */     float sittingYOffset;
/* 167 */     switch (pose.ordinal()) {
/*     */       case 0:
/* 169 */         this.leftLeg.xRot += 0.6981317F;
/* 170 */         this.rightLeg.xRot += 0.6981317F;
/*     */         break;
/*     */       case 2:
/* 173 */         sittingYOffset = 1.9F;
/*     */         
/* 175 */         this.head.y += 1.9F;
/*     */         
/* 177 */         this.tail.xRot += 0.5235988F;
/* 178 */         this.tail.y += 1.9F;
/*     */         
/* 180 */         this.body.y += 1.9F;
/*     */         
/* 182 */         this.leftWing.zRot = -0.0873F;
/* 183 */         this.leftWing.y += 1.9F;
/*     */         
/* 185 */         this.rightWing.zRot = 0.0873F;
/* 186 */         this.rightWing.y += 1.9F;
/*     */         
/* 188 */         this.leftLeg.y += 1.9F;
/* 189 */         this.rightLeg.y += 1.9F;
/*     */         
/* 191 */         this.leftLeg.xRot += 1.5707964F;
/* 192 */         this.rightLeg.xRot += 1.5707964F;
/*     */         break;
/*     */       case 3:
/* 195 */         this.leftLeg.zRot = -0.34906584F;
/* 196 */         this.rightLeg.zRot = 0.34906584F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pose getPose(Parrot entity) {
/* 207 */     if (entity.isPartyParrot())
/* 208 */       return Pose.PARTY; 
/* 209 */     if (entity.isInSittingPose())
/* 210 */       return Pose.SITTING; 
/* 211 */     if (entity.isFlying()) {
/* 212 */       return Pose.FLYING;
/*     */     }
/* 214 */     return Pose.STANDING;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/parrot/ParrotModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */