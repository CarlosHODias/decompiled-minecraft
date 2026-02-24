/*     */ package net.minecraft.client.model.animal.golem;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class IronGolemModel
/*     */   extends EntityModel<IronGolemRenderState> {
/*     */   private final ModelPart head;
/*     */   private final ModelPart rightArm;
/*     */   private final ModelPart leftArm;
/*     */   private final ModelPart rightLeg;
/*     */   private final ModelPart leftLeg;
/*     */   
/*     */   public IronGolemModel(ModelPart root) {
/*  23 */     super(root);
/*  24 */     this.head = root.getChild("head");
/*  25 */     this.rightArm = root.getChild("right_arm");
/*  26 */     this.leftArm = root.getChild("left_arm");
/*  27 */     this.rightLeg = root.getChild("right_leg");
/*  28 */     this.leftLeg = root.getChild("left_leg");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  32 */     MeshDefinition mesh = new MeshDefinition();
/*  33 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  35 */     root.addOrReplaceChild("head", 
/*  36 */         CubeListBuilder.create()
/*  37 */         .texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F)
/*  38 */         .texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F), 
/*  39 */         PartPose.offset(0.0F, -7.0F, -2.0F));
/*     */     
/*  41 */     root.addOrReplaceChild("body", 
/*  42 */         CubeListBuilder.create()
/*  43 */         .texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F)
/*  44 */         .texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)), 
/*  45 */         PartPose.offset(0.0F, -7.0F, 0.0F));
/*     */     
/*  47 */     root.addOrReplaceChild("right_arm", 
/*  48 */         CubeListBuilder.create()
/*  49 */         .texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F), 
/*  50 */         PartPose.offset(0.0F, -7.0F, 0.0F));
/*     */     
/*  52 */     root.addOrReplaceChild("left_arm", 
/*  53 */         CubeListBuilder.create()
/*  54 */         .texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F), 
/*  55 */         PartPose.offset(0.0F, -7.0F, 0.0F));
/*     */     
/*  57 */     root.addOrReplaceChild("right_leg", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), 
/*  60 */         PartPose.offset(-4.0F, 11.0F, 0.0F));
/*     */     
/*  62 */     root.addOrReplaceChild("left_leg", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(60, 0).mirror().addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), 
/*  65 */         PartPose.offset(5.0F, 11.0F, 0.0F));
/*     */ 
/*     */     
/*  68 */     return LayerDefinition.create(mesh, 128, 128);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(IronGolemRenderState state) {
/*  73 */     super.setupAnim(state);
/*     */     
/*  75 */     float attackTick = state.attackTicksRemaining;
/*  76 */     float animationSpeed = state.walkAnimationSpeed;
/*  77 */     float animationPos = state.walkAnimationPos;
/*  78 */     if (attackTick > 0.0F) {
/*  79 */       this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attackTick, 10.0F);
/*  80 */       this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attackTick, 10.0F);
/*     */     } else {
/*  82 */       int offerFlowerTick = state.offerFlowerTick;
/*  83 */       if (offerFlowerTick > 0) {
/*  84 */         this.rightArm.xRot = -0.8F + 0.025F * Mth.triangleWave(offerFlowerTick, 70.0F);
/*  85 */         this.leftArm.xRot = 0.0F;
/*     */       } else {
/*  87 */         this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(animationPos, 13.0F)) * animationSpeed;
/*  88 */         this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(animationPos, 13.0F)) * animationSpeed;
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     this.head.yRot = state.yRot * 0.017453292F;
/*  93 */     this.head.xRot = state.xRot * 0.017453292F;
/*     */     
/*  95 */     this.rightLeg.xRot = -1.5F * Mth.triangleWave(animationPos, 13.0F) * animationSpeed;
/*  96 */     this.leftLeg.xRot = 1.5F * Mth.triangleWave(animationPos, 13.0F) * animationSpeed;
/*  97 */     this.rightLeg.yRot = 0.0F;
/*  98 */     this.leftLeg.yRot = 0.0F;
/*     */   }
/*     */   
/*     */   public ModelPart getFlowerHoldingArm() {
/* 102 */     return this.rightArm;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/golem/IronGolemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */