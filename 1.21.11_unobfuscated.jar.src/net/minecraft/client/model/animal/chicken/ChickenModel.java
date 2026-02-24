/*     */ package net.minecraft.client.model.animal.chicken;
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
/*     */ import net.minecraft.client.renderer.entity.state.ChickenRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class ChickenModel
/*     */   extends EntityModel<ChickenRenderState>
/*     */ {
/*     */   public static final String RED_THING = "red_thing";
/*     */   public static final float Y_OFFSET = 16.0F;
/*  21 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(false, 5.0F, 2.0F, 2.0F, 1.99F, 24.0F, Set.of("head", "beak", "red_thing"));
/*     */   
/*     */   private final ModelPart head;
/*     */   private final ModelPart rightLeg;
/*     */   private final ModelPart leftLeg;
/*     */   private final ModelPart rightWing;
/*     */   private final ModelPart leftWing;
/*     */   
/*     */   public ChickenModel(ModelPart root) {
/*  30 */     super(root);
/*  31 */     this.head = root.getChild("head");
/*  32 */     this.rightLeg = root.getChild("right_leg");
/*  33 */     this.leftLeg = root.getChild("left_leg");
/*  34 */     this.rightWing = root.getChild("right_wing");
/*  35 */     this.leftWing = root.getChild("left_wing");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  39 */     MeshDefinition mesh = createBaseChickenModel();
/*  40 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   
/*     */   protected static MeshDefinition createBaseChickenModel() {
/*  44 */     MeshDefinition mesh = new MeshDefinition();
/*  45 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  47 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  48 */         CubeListBuilder.create()
/*  49 */         .texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), 
/*  50 */         PartPose.offset(0.0F, 15.0F, -4.0F));
/*     */     
/*  52 */     head.addOrReplaceChild("beak", 
/*  53 */         CubeListBuilder.create()
/*  54 */         .texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  57 */     head.addOrReplaceChild("red_thing", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  62 */     root.addOrReplaceChild("body", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), 
/*  65 */         PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  67 */     CubeListBuilder leg = CubeListBuilder.create()
/*  68 */       .texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
/*     */     
/*  70 */     root.addOrReplaceChild("right_leg", leg, PartPose.offset(-2.0F, 19.0F, 1.0F));
/*  71 */     root.addOrReplaceChild("left_leg", leg, PartPose.offset(1.0F, 19.0F, 1.0F));
/*     */     
/*  73 */     root.addOrReplaceChild("right_wing", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), 
/*  76 */         PartPose.offset(-4.0F, 13.0F, 0.0F));
/*     */     
/*  78 */     root.addOrReplaceChild("left_wing", 
/*  79 */         CubeListBuilder.create()
/*  80 */         .texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), 
/*  81 */         PartPose.offset(4.0F, 13.0F, 0.0F));
/*     */ 
/*     */     
/*  84 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(ChickenRenderState state) {
/*  89 */     super.setupAnim(state);
/*     */     
/*  91 */     float flapAngle = (Mth.sin(state.flap) + 1.0F) * state.flapSpeed;
/*     */     
/*  93 */     this.head.xRot = state.xRot * 0.017453292F;
/*  94 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */     
/*  96 */     float animationSpeed = state.walkAnimationSpeed;
/*  97 */     float animationPos = state.walkAnimationPos;
/*  98 */     this.rightLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*  99 */     this.leftLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 100 */     this.rightWing.zRot = flapAngle;
/* 101 */     this.leftWing.zRot = -flapAngle;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/chicken/ChickenModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */