/*     */ package net.minecraft.client.model.animal.bee;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.BeeRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BeeModel
/*     */   extends EntityModel<BeeRenderState>
/*     */ {
/*  18 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/*     */   
/*     */   private static final String BONE = "bone";
/*     */   
/*     */   private static final String STINGER = "stinger";
/*     */   
/*     */   private static final String LEFT_ANTENNA = "left_antenna";
/*     */   private static final String RIGHT_ANTENNA = "right_antenna";
/*     */   private static final String FRONT_LEGS = "front_legs";
/*     */   private static final String MIDDLE_LEGS = "middle_legs";
/*     */   private static final String BACK_LEGS = "back_legs";
/*     */   private final ModelPart bone;
/*     */   private final ModelPart rightWing;
/*     */   private final ModelPart leftWing;
/*     */   private final ModelPart frontLeg;
/*     */   private final ModelPart midLeg;
/*     */   private final ModelPart backLeg;
/*     */   private final ModelPart stinger;
/*     */   private final ModelPart leftAntenna;
/*     */   private final ModelPart rightAntenna;
/*     */   private float rollAmount;
/*     */   
/*     */   public BeeModel(ModelPart root) {
/*  41 */     super(root);
/*  42 */     this.bone = root.getChild("bone");
/*     */     
/*  44 */     ModelPart body = this.bone.getChild("body");
/*  45 */     this.stinger = body.getChild("stinger");
/*  46 */     this.leftAntenna = body.getChild("left_antenna");
/*  47 */     this.rightAntenna = body.getChild("right_antenna");
/*     */     
/*  49 */     this.rightWing = this.bone.getChild("right_wing");
/*  50 */     this.leftWing = this.bone.getChild("left_wing");
/*  51 */     this.frontLeg = this.bone.getChild("front_legs");
/*  52 */     this.midLeg = this.bone.getChild("middle_legs");
/*  53 */     this.backLeg = this.bone.getChild("back_legs");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  57 */     MeshDefinition mesh = new MeshDefinition();
/*  58 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  60 */     PartDefinition bone = root.addOrReplaceChild("bone", 
/*  61 */         CubeListBuilder.create(), 
/*  62 */         PartPose.offset(0.0F, 19.0F, 0.0F));
/*     */     
/*  64 */     PartDefinition body = bone.addOrReplaceChild("body", 
/*  65 */         CubeListBuilder.create()
/*  66 */         .texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO);
/*     */     
/*  68 */     body.addOrReplaceChild("stinger", 
/*  69 */         CubeListBuilder.create()
/*  70 */         .texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  73 */     body.addOrReplaceChild("left_antenna", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), 
/*  76 */         PartPose.offset(0.0F, -2.0F, -5.0F));
/*     */     
/*  78 */     body.addOrReplaceChild("right_antenna", 
/*  79 */         CubeListBuilder.create()
/*  80 */         .texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), 
/*  81 */         PartPose.offset(0.0F, -2.0F, -5.0F));
/*     */ 
/*     */     
/*  84 */     CubeDeformation wingDeformation = new CubeDeformation(0.001F);
/*  85 */     bone.addOrReplaceChild("right_wing", 
/*  86 */         CubeListBuilder.create()
/*  87 */         .texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, wingDeformation), 
/*  88 */         PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
/*     */     
/*  90 */     bone.addOrReplaceChild("left_wing", 
/*  91 */         CubeListBuilder.create()
/*  92 */         .texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, wingDeformation), 
/*  93 */         PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
/*     */     
/*  95 */     bone.addOrReplaceChild("front_legs", 
/*  96 */         CubeListBuilder.create()
/*  97 */         .addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), 
/*  98 */         PartPose.offset(1.5F, 3.0F, -2.0F));
/*     */     
/* 100 */     bone.addOrReplaceChild("middle_legs", 
/* 101 */         CubeListBuilder.create()
/* 102 */         .addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), 
/* 103 */         PartPose.offset(1.5F, 3.0F, 0.0F));
/*     */     
/* 105 */     bone.addOrReplaceChild("back_legs", 
/* 106 */         CubeListBuilder.create()
/* 107 */         .addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), 
/* 108 */         PartPose.offset(1.5F, 3.0F, 2.0F));
/*     */ 
/*     */     
/* 111 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(BeeRenderState state) {
/* 116 */     super.setupAnim(state);
/* 117 */     this.rollAmount = state.rollAmount;
/*     */ 
/*     */     
/* 120 */     this.stinger.visible = state.hasStinger;
/*     */     
/* 122 */     if (!state.isOnGround) {
/* 123 */       float speed = state.ageInTicks * 120.32113F * 0.017453292F;
/* 124 */       this.rightWing.yRot = 0.0F;
/* 125 */       this.rightWing.zRot = Mth.cos(speed) * 3.1415927F * 0.15F;
/*     */       
/* 127 */       this.leftWing.xRot = this.rightWing.xRot;
/* 128 */       this.leftWing.yRot = this.rightWing.yRot;
/* 129 */       this.leftWing.zRot = -this.rightWing.zRot;
/*     */       
/* 131 */       this.frontLeg.xRot = 0.7853982F;
/* 132 */       this.midLeg.xRot = 0.7853982F;
/* 133 */       this.backLeg.xRot = 0.7853982F;
/*     */     } 
/*     */     
/* 136 */     if (!state.isAngry)
/*     */     {
/* 138 */       if (!state.isOnGround) {
/* 139 */         float speed = Mth.cos((state.ageInTicks * 0.18F));
/* 140 */         this.bone.xRot = 0.1F + speed * 3.1415927F * 0.025F;
/*     */         
/* 142 */         this.leftAntenna.xRot = speed * 3.1415927F * 0.03F;
/* 143 */         this.rightAntenna.xRot = speed * 3.1415927F * 0.03F;
/*     */         
/* 145 */         this.frontLeg.xRot = -speed * 3.1415927F * 0.1F + 0.3926991F;
/* 146 */         this.backLeg.xRot = -speed * 3.1415927F * 0.05F + 0.7853982F;
/*     */         
/* 148 */         this.bone.y -= Mth.cos((state.ageInTicks * 0.18F)) * 0.9F;
/*     */       } 
/*     */     }
/*     */     
/* 152 */     if (this.rollAmount > 0.0F)
/* 153 */       this.bone.xRot = Mth.rotLerpRad(this.rollAmount, this.bone.xRot, 3.0915928F); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/bee/BeeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */