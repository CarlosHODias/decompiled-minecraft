/*     */ package net.minecraft.client.model.ambient;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.BatAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.BatRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ 
/*     */ 
/*     */ public class BatModel
/*     */   extends EntityModel<BatRenderState>
/*     */ {
/*     */   private final ModelPart head;
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightWing;
/*     */   private final ModelPart leftWing;
/*     */   private final ModelPart rightWingTip;
/*     */   private final ModelPart leftWingTip;
/*     */   private final ModelPart feet;
/*     */   private final KeyframeAnimation flyingAnimation;
/*     */   private final KeyframeAnimation restingAnimation;
/*     */   
/*     */   public BatModel(ModelPart root) {
/*  30 */     super(root, RenderTypes::entityCutout);
/*  31 */     this.body = root.getChild("body");
/*  32 */     this.head = root.getChild("head");
/*  33 */     this.rightWing = this.body.getChild("right_wing");
/*  34 */     this.rightWingTip = this.rightWing.getChild("right_wing_tip");
/*  35 */     this.leftWing = this.body.getChild("left_wing");
/*  36 */     this.leftWingTip = this.leftWing.getChild("left_wing_tip");
/*  37 */     this.feet = this.body.getChild("feet");
/*     */     
/*  39 */     this.flyingAnimation = BatAnimation.BAT_FLYING.bake(root);
/*  40 */     this.restingAnimation = BatAnimation.BAT_RESTING.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  44 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  45 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*     */     
/*  47 */     PartDefinition body = partdefinition.addOrReplaceChild("body", 
/*  48 */         CubeListBuilder.create()
/*  49 */         .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F), 
/*  50 */         PartPose.offset(0.0F, 17.0F, 0.0F));
/*     */     
/*  52 */     PartDefinition head = partdefinition.addOrReplaceChild("head", 
/*  53 */         CubeListBuilder.create()
/*  54 */         .texOffs(0, 7).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 3.0F, 2.0F), 
/*  55 */         PartPose.offset(0.0F, 17.0F, 0.0F));
/*     */     
/*  57 */     head.addOrReplaceChild("right_ear", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(1, 15).addBox(-2.5F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F), 
/*  60 */         PartPose.offset(-1.5F, -2.0F, 0.0F));
/*     */     
/*  62 */     head.addOrReplaceChild("left_ear", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(8, 15).addBox(-0.1F, -3.0F, 0.0F, 3.0F, 5.0F, 0.0F), 
/*  65 */         PartPose.offset(1.1F, -3.0F, 0.0F));
/*     */     
/*  67 */     PartDefinition rightWing = body.addOrReplaceChild("right_wing", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(12, 0).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 7.0F, 0.0F), 
/*  70 */         PartPose.offset(-1.5F, 0.0F, 0.0F));
/*     */     
/*  72 */     rightWing.addOrReplaceChild("right_wing_tip", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(16, 0).addBox(-6.0F, -2.0F, 0.0F, 6.0F, 8.0F, 0.0F), 
/*  75 */         PartPose.offset(-2.0F, 0.0F, 0.0F));
/*     */     
/*  77 */     PartDefinition leftWing = body.addOrReplaceChild("left_wing", 
/*  78 */         CubeListBuilder.create()
/*  79 */         .texOffs(12, 7).addBox(0.0F, -2.0F, 0.0F, 2.0F, 7.0F, 0.0F), 
/*  80 */         PartPose.offset(1.5F, 0.0F, 0.0F));
/*     */     
/*  82 */     leftWing.addOrReplaceChild("left_wing_tip", 
/*  83 */         CubeListBuilder.create().texOffs(16, 8)
/*  84 */         .addBox(0.0F, -2.0F, 0.0F, 6.0F, 8.0F, 0.0F), 
/*  85 */         PartPose.offset(2.0F, 0.0F, 0.0F));
/*     */     
/*  87 */     body.addOrReplaceChild("feet", 
/*  88 */         CubeListBuilder.create().texOffs(16, 16)
/*  89 */         .addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F), 
/*  90 */         PartPose.offset(0.0F, 5.0F, 0.0F));
/*     */     
/*  92 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(BatRenderState state) {
/*  97 */     super.setupAnim(state);
/*  98 */     if (state.isResting) {
/*  99 */       applyHeadRotation(state.yRot);
/*     */     }
/* 101 */     this.flyingAnimation.apply(state.flyAnimationState, state.ageInTicks);
/* 102 */     this.restingAnimation.apply(state.restAnimationState, state.ageInTicks);
/*     */   }
/*     */   
/*     */   private void applyHeadRotation(float yRot) {
/* 106 */     this.head.yRot = yRot * 0.017453292F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/ambient/BatModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */