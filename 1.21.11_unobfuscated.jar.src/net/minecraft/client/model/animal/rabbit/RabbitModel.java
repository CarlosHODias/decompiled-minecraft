/*     */ package net.minecraft.client.model.animal.rabbit;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.model.BabyModelTransform;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.RabbitRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RabbitModel
/*     */   extends EntityModel<RabbitRenderState>
/*     */ {
/*     */   private static final float REAR_JUMP_ANGLE = 50.0F;
/*     */   private static final float FRONT_JUMP_ANGLE = -40.0F;
/*     */   private static final float NEW_SCALE = 0.6F;
/*  24 */   private static final MeshTransformer ADULT_TRANSFORMER = MeshTransformer.scaling(0.6F);
/*  25 */   private static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 22.0F, 2.0F, 2.65F, 2.5F, 36.0F, Set.of("head", "left_ear", "right_ear", "nose"));
/*     */   
/*     */   private static final String LEFT_HAUNCH = "left_haunch";
/*     */   
/*     */   private static final String RIGHT_HAUNCH = "right_haunch";
/*     */   private final ModelPart leftHaunch;
/*     */   private final ModelPart rightHaunch;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart head;
/*     */   
/*     */   public RabbitModel(ModelPart root) {
/*  37 */     super(root);
/*  38 */     this.leftHaunch = root.getChild("left_haunch");
/*  39 */     this.rightHaunch = root.getChild("right_haunch");
/*  40 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  41 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  42 */     this.head = root.getChild("head");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer(boolean baby) {
/*  46 */     MeshDefinition mesh = new MeshDefinition();
/*  47 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  49 */     PartDefinition leftHaunch = root.addOrReplaceChild("left_haunch", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(30, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F), 
/*  52 */         PartPose.offsetAndRotation(3.0F, 17.5F, 3.7F, -0.36651915F, 0.0F, 0.0F));
/*     */     
/*  54 */     PartDefinition rightHaunch = root.addOrReplaceChild("right_haunch", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(16, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F), 
/*  57 */         PartPose.offsetAndRotation(-3.0F, 17.5F, 3.7F, -0.36651915F, 0.0F, 0.0F));
/*     */     
/*  59 */     leftHaunch.addOrReplaceChild("left_hind_foot", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .texOffs(26, 24).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F), 
/*  62 */         PartPose.rotation(0.36651915F, 0.0F, 0.0F));
/*     */     
/*  64 */     rightHaunch.addOrReplaceChild("right_hind_foot", 
/*  65 */         CubeListBuilder.create()
/*  66 */         .texOffs(8, 24).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F), 
/*  67 */         PartPose.rotation(0.36651915F, 0.0F, 0.0F));
/*     */     
/*  69 */     root.addOrReplaceChild("body", 
/*  70 */         CubeListBuilder.create()
/*  71 */         .texOffs(0, 0).addBox(-3.0F, -2.0F, -10.0F, 6.0F, 5.0F, 10.0F), 
/*  72 */         PartPose.offsetAndRotation(0.0F, 19.0F, 8.0F, -0.34906584F, 0.0F, 0.0F));
/*     */     
/*  74 */     root.addOrReplaceChild("left_front_leg", 
/*  75 */         CubeListBuilder.create()
/*  76 */         .texOffs(8, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), 
/*  77 */         PartPose.offsetAndRotation(3.0F, 17.0F, -1.0F, -0.19198622F, 0.0F, 0.0F));
/*     */     
/*  79 */     root.addOrReplaceChild("right_front_leg", 
/*  80 */         CubeListBuilder.create()
/*  81 */         .texOffs(0, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), 
/*  82 */         PartPose.offsetAndRotation(-3.0F, 17.0F, -1.0F, -0.19198622F, 0.0F, 0.0F));
/*     */     
/*  84 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(32, 0).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F), 
/*  87 */         PartPose.offset(0.0F, 16.0F, -1.0F));
/*     */     
/*  89 */     head.addOrReplaceChild("right_ear", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(52, 0).addBox(-2.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), 
/*  92 */         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2617994F, 0.0F));
/*     */     
/*  94 */     head.addOrReplaceChild("left_ear", 
/*  95 */         CubeListBuilder.create()
/*  96 */         .texOffs(58, 0).addBox(0.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), 
/*  97 */         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2617994F, 0.0F));
/*     */     
/*  99 */     root.addOrReplaceChild("tail", 
/* 100 */         CubeListBuilder.create()
/* 101 */         .texOffs(52, 6).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 2.0F), 
/* 102 */         PartPose.offsetAndRotation(0.0F, 20.0F, 7.0F, -0.3490659F, 0.0F, 0.0F));
/*     */     
/* 104 */     head.addOrReplaceChild("nose", 
/* 105 */         CubeListBuilder.create()
/* 106 */         .texOffs(32, 9).addBox(-0.5F, -2.5F, -5.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/* 110 */     return LayerDefinition.create(mesh, 64, 32).apply(baby ? BABY_TRANSFORMER : ADULT_TRANSFORMER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(RabbitRenderState state) {
/* 115 */     super.setupAnim(state);
/*     */     
/* 117 */     this.head.xRot = state.xRot * 0.017453292F;
/* 118 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */     
/* 120 */     float jumpRotation = Mth.sin((state.jumpCompletion * 3.1415927F));
/*     */     
/* 122 */     this.leftHaunch.xRot += jumpRotation * 50.0F * 0.017453292F;
/* 123 */     this.rightHaunch.xRot += jumpRotation * 50.0F * 0.017453292F;
/* 124 */     this.leftFrontLeg.xRot += jumpRotation * -40.0F * 0.017453292F;
/* 125 */     this.rightFrontLeg.xRot += jumpRotation * -40.0F * 0.017453292F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/rabbit/RabbitModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */