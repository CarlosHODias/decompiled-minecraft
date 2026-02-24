/*     */ package net.minecraft.client.model.animal.turtle;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.model.BabyModelTransform;
/*     */ import net.minecraft.client.model.QuadrupedModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.TurtleRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class TurtleModel
/*     */   extends QuadrupedModel<TurtleRenderState> {
/*     */   private static final String EGG_BELLY = "egg_belly";
/*  20 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 120.0F, 0.0F, 9.0F, 6.0F, 120.0F, Set.of("head"));
/*     */   
/*     */   private final ModelPart eggBelly;
/*     */   
/*     */   public TurtleModel(ModelPart root) {
/*  25 */     super(root);
/*  26 */     this.eggBelly = root.getChild("egg_belly");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  30 */     MeshDefinition mesh = new MeshDefinition();
/*  31 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  33 */     root.addOrReplaceChild("head", 
/*  34 */         CubeListBuilder.create()
/*  35 */         .texOffs(3, 0).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 5.0F, 6.0F), 
/*  36 */         PartPose.offset(0.0F, 19.0F, -10.0F));
/*     */     
/*  38 */     root.addOrReplaceChild("body", 
/*  39 */         CubeListBuilder.create()
/*  40 */         .texOffs(7, 37).addBox("shell", -9.5F, 3.0F, -10.0F, 19.0F, 20.0F, 6.0F)
/*  41 */         .texOffs(31, 1).addBox("belly", -5.5F, 3.0F, -13.0F, 11.0F, 18.0F, 3.0F), 
/*  42 */         PartPose.offsetAndRotation(0.0F, 11.0F, -10.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  44 */     root.addOrReplaceChild("egg_belly", 
/*  45 */         CubeListBuilder.create()
/*  46 */         .texOffs(70, 33).addBox(-4.5F, 3.0F, -14.0F, 9.0F, 18.0F, 1.0F), 
/*  47 */         PartPose.offsetAndRotation(0.0F, 11.0F, -10.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  49 */     int legHeight = 1;
/*  50 */     root.addOrReplaceChild("right_hind_leg", 
/*  51 */         CubeListBuilder.create()
/*  52 */         .texOffs(1, 23).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 10.0F), 
/*  53 */         PartPose.offset(-3.5F, 22.0F, 11.0F));
/*     */     
/*  55 */     root.addOrReplaceChild("left_hind_leg", 
/*  56 */         CubeListBuilder.create()
/*  57 */         .texOffs(1, 12).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 10.0F), 
/*  58 */         PartPose.offset(3.5F, 22.0F, 11.0F));
/*     */     
/*  60 */     root.addOrReplaceChild("right_front_leg", 
/*  61 */         CubeListBuilder.create()
/*  62 */         .texOffs(27, 30).addBox(-13.0F, 0.0F, -2.0F, 13.0F, 1.0F, 5.0F), 
/*  63 */         PartPose.offset(-5.0F, 21.0F, -4.0F));
/*     */     
/*  65 */     root.addOrReplaceChild("left_front_leg", 
/*  66 */         CubeListBuilder.create()
/*  67 */         .texOffs(27, 24).addBox(0.0F, 0.0F, -2.0F, 13.0F, 1.0F, 5.0F), 
/*  68 */         PartPose.offset(5.0F, 21.0F, -4.0F));
/*     */ 
/*     */     
/*  71 */     return LayerDefinition.create(mesh, 128, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(TurtleRenderState state) {
/*  76 */     super.setupAnim((LivingEntityRenderState)state);
/*     */     
/*  78 */     float animationPos = state.walkAnimationPos;
/*  79 */     float animationSpeed = state.walkAnimationSpeed;
/*     */     
/*  81 */     if (state.isOnLand) {
/*  82 */       float layEgg = state.isLayingEgg ? 4.0F : 1.0F;
/*  83 */       float layEggAmplitude = state.isLayingEgg ? 2.0F : 1.0F;
/*  84 */       float swingPos = animationPos * 5.0F;
/*  85 */       float frontSwing = Mth.cos((layEgg * swingPos));
/*  86 */       float hindSwing = Mth.cos(swingPos);
/*     */       
/*  88 */       this.rightFrontLeg.yRot = -frontSwing * 8.0F * animationSpeed * layEggAmplitude;
/*  89 */       this.leftFrontLeg.yRot = frontSwing * 8.0F * animationSpeed * layEggAmplitude;
/*  90 */       this.rightHindLeg.yRot = -hindSwing * 3.0F * animationSpeed;
/*  91 */       this.leftHindLeg.yRot = hindSwing * 3.0F * animationSpeed;
/*     */     } else {
/*  93 */       float swingScale = 0.5F * animationSpeed;
/*  94 */       float swing = Mth.cos((animationPos * 0.6662F * 0.6F)) * swingScale;
/*  95 */       this.rightHindLeg.xRot = swing;
/*  96 */       this.leftHindLeg.xRot = -swing;
/*  97 */       this.rightFrontLeg.zRot = -swing;
/*  98 */       this.leftFrontLeg.zRot = swing;
/*     */     } 
/*     */     
/* 101 */     this.eggBelly.visible = state.hasEgg;
/*     */     
/* 103 */     if (this.eggBelly.visible)
/* 104 */       this.root.y--; 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/turtle/TurtleModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */