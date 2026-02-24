/*     */ package net.minecraft.client.model.monster.breeze;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.BreezeAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.BreezeRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BreezeModel
/*     */   extends EntityModel<BreezeRenderState>
/*     */ {
/*     */   private static final float WIND_TOP_SPEED = 0.6F;
/*     */   private static final float WIND_MIDDLE_SPEED = 0.8F;
/*     */   private static final float WIND_BOTTOM_SPEED = 1.0F;
/*     */   private final ModelPart head;
/*     */   private final ModelPart eyes;
/*     */   private final ModelPart wind;
/*     */   private final ModelPart windTop;
/*     */   private final ModelPart windMid;
/*     */   private final ModelPart windBottom;
/*     */   private final ModelPart rods;
/*     */   private final KeyframeAnimation idleAnimation;
/*     */   private final KeyframeAnimation shootAnimation;
/*     */   private final KeyframeAnimation slideAnimation;
/*     */   private final KeyframeAnimation slideBackAnimation;
/*     */   private final KeyframeAnimation inhaleAnimation;
/*     */   private final KeyframeAnimation jumpAnimation;
/*     */   
/*     */   public BreezeModel(ModelPart root) {
/*  42 */     super(root, RenderTypes::entityTranslucent);
/*  43 */     this.wind = root.getChild("wind_body");
/*  44 */     this.windBottom = this.wind.getChild("wind_bottom");
/*  45 */     this.windMid = this.windBottom.getChild("wind_mid");
/*  46 */     this.windTop = this.windMid.getChild("wind_top");
/*  47 */     this.head = root.getChild("body").getChild("head");
/*  48 */     this.eyes = this.head.getChild("eyes");
/*  49 */     this.rods = root.getChild("body").getChild("rods");
/*     */     
/*  51 */     this.idleAnimation = BreezeAnimation.IDLE.bake(root);
/*  52 */     this.shootAnimation = BreezeAnimation.SHOOT.bake(root);
/*  53 */     this.slideAnimation = BreezeAnimation.SLIDE.bake(root);
/*  54 */     this.slideBackAnimation = BreezeAnimation.SLIDE_BACK.bake(root);
/*  55 */     this.inhaleAnimation = BreezeAnimation.INHALE.bake(root);
/*  56 */     this.jumpAnimation = BreezeAnimation.JUMP.bake(root);
/*     */   }
/*     */   
/*     */   private static MeshDefinition createBaseMesh() {
/*  60 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  61 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*     */     
/*  63 */     PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
/*     */     
/*  65 */     PartDefinition rods = body.addOrReplaceChild("rods", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));
/*  66 */     rods.addOrReplaceChild("rod_1", CubeListBuilder.create()
/*  67 */         .texOffs(0, 17).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5981F, -3.0F, 1.5F, -2.7489F, -1.0472F, 3.1416F));
/*  68 */     rods.addOrReplaceChild("rod_2", CubeListBuilder.create()
/*  69 */         .texOffs(0, 17).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5981F, -3.0F, 1.5F, -2.7489F, 1.0472F, 3.1416F));
/*  70 */     rods.addOrReplaceChild("rod_3", CubeListBuilder.create()
/*  71 */         .texOffs(0, 17).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -3.0F, 0.3927F, 0.0F, 0.0F));
/*     */     
/*  73 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
/*  74 */         .texOffs(4, 24).addBox(-5.0F, -5.0F, -4.2F, 10.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
/*  75 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
/*     */     
/*  77 */     head.addOrReplaceChild("eyes", CubeListBuilder.create()
/*  78 */         .texOffs(4, 24).addBox(-5.0F, -5.0F, -4.2F, 10.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
/*  79 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
/*     */     
/*  81 */     PartDefinition windBody = partdefinition.addOrReplaceChild("wind_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
/*  82 */     PartDefinition windBottom = windBody.addOrReplaceChild("wind_bottom", CubeListBuilder.create()
/*  83 */         .texOffs(1, 83).addBox(-2.5F, -7.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
/*  84 */     PartDefinition windMid = windBottom.addOrReplaceChild("wind_mid", CubeListBuilder.create()
/*  85 */         .texOffs(74, 28).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
/*  86 */         .texOffs(78, 32).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
/*  87 */         .texOffs(49, 71).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));
/*  88 */     windMid.addOrReplaceChild("wind_top", CubeListBuilder.create()
/*  89 */         .texOffs(0, 0).addBox(-9.0F, -8.0F, -9.0F, 18.0F, 8.0F, 18.0F, new CubeDeformation(0.0F))
/*  90 */         .texOffs(6, 6).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
/*  91 */         .texOffs(105, 57).addBox(-2.5F, -8.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));
/*  92 */     return meshdefinition;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  96 */     MeshDefinition mesh = createBaseMesh();
/*  97 */     mesh.getRoot().retainPartsAndChildren(Set.of("head", "rods"));
/*  98 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createWindLayer() {
/* 102 */     MeshDefinition mesh = createBaseMesh();
/* 103 */     mesh.getRoot().retainPartsAndChildren(Set.of("wind_body"));
/* 104 */     return LayerDefinition.create(mesh, 128, 128);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createEyesLayer() {
/* 108 */     MeshDefinition mesh = createBaseMesh();
/* 109 */     mesh.getRoot().retainPartsAndChildren(Set.of("eyes"));
/* 110 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(BreezeRenderState state) {
/* 115 */     super.setupAnim(state);
/*     */     
/* 117 */     this.idleAnimation.apply(state.idle, state.ageInTicks);
/* 118 */     this.shootAnimation.apply(state.shoot, state.ageInTicks);
/* 119 */     this.slideAnimation.apply(state.slide, state.ageInTicks);
/* 120 */     this.slideBackAnimation.apply(state.slideBack, state.ageInTicks);
/* 121 */     this.inhaleAnimation.apply(state.inhale, state.ageInTicks);
/* 122 */     this.jumpAnimation.apply(state.longJump, state.ageInTicks);
/*     */   }
/*     */   
/*     */   public ModelPart head() {
/* 126 */     return this.head;
/*     */   }
/*     */   
/*     */   public ModelPart eyes() {
/* 130 */     return this.eyes;
/*     */   }
/*     */   
/*     */   public ModelPart rods() {
/* 134 */     return this.rods;
/*     */   }
/*     */   
/*     */   public ModelPart wind() {
/* 138 */     return this.wind;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/breeze/BreezeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */