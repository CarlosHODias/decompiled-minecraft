/*     */ package net.minecraft.client.model.monster.strider;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.StriderRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class StriderModel
/*     */   extends EntityModel<StriderRenderState> {
/*  16 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/*     */   
/*     */   private static final String RIGHT_BOTTOM_BRISTLE = "right_bottom_bristle";
/*     */   
/*     */   private static final String RIGHT_MIDDLE_BRISTLE = "right_middle_bristle";
/*     */   private static final String RIGHT_TOP_BRISTLE = "right_top_bristle";
/*     */   private static final String LEFT_TOP_BRISTLE = "left_top_bristle";
/*     */   private static final String LEFT_MIDDLE_BRISTLE = "left_middle_bristle";
/*     */   private static final String LEFT_BOTTOM_BRISTLE = "left_bottom_bristle";
/*     */   private final ModelPart rightLeg;
/*     */   private final ModelPart leftLeg;
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightBottomBristle;
/*     */   private final ModelPart rightMiddleBristle;
/*     */   private final ModelPart rightTopBristle;
/*     */   private final ModelPart leftTopBristle;
/*     */   private final ModelPart leftMiddleBristle;
/*     */   private final ModelPart leftBottomBristle;
/*     */   
/*     */   public StriderModel(ModelPart root) {
/*  36 */     super(root);
/*  37 */     this.rightLeg = root.getChild("right_leg");
/*  38 */     this.leftLeg = root.getChild("left_leg");
/*  39 */     this.body = root.getChild("body");
/*  40 */     this.rightBottomBristle = this.body.getChild("right_bottom_bristle");
/*  41 */     this.rightMiddleBristle = this.body.getChild("right_middle_bristle");
/*  42 */     this.rightTopBristle = this.body.getChild("right_top_bristle");
/*  43 */     this.leftTopBristle = this.body.getChild("left_top_bristle");
/*  44 */     this.leftMiddleBristle = this.body.getChild("left_middle_bristle");
/*  45 */     this.leftBottomBristle = this.body.getChild("left_bottom_bristle");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  49 */     MeshDefinition mesh = new MeshDefinition();
/*  50 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  52 */     root.addOrReplaceChild("right_leg", 
/*  53 */         CubeListBuilder.create()
/*  54 */         .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F), 
/*  55 */         PartPose.offset(-4.0F, 8.0F, 0.0F));
/*     */     
/*  57 */     root.addOrReplaceChild("left_leg", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(0, 55).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F), 
/*  60 */         PartPose.offset(4.0F, 8.0F, 0.0F));
/*     */     
/*  62 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(0, 0).addBox(-8.0F, -6.0F, -8.0F, 16.0F, 14.0F, 16.0F), 
/*  65 */         PartPose.offset(0.0F, 1.0F, 0.0F));
/*     */ 
/*     */     
/*  68 */     body.addOrReplaceChild("right_bottom_bristle", 
/*  69 */         CubeListBuilder.create()
/*  70 */         .texOffs(16, 65).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), 
/*  71 */         PartPose.offsetAndRotation(-8.0F, 4.0F, -8.0F, 0.0F, 0.0F, -1.2217305F));
/*     */     
/*  73 */     body.addOrReplaceChild("right_middle_bristle", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(16, 49).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), 
/*  76 */         PartPose.offsetAndRotation(-8.0F, -1.0F, -8.0F, 0.0F, 0.0F, -1.134464F));
/*     */     
/*  78 */     body.addOrReplaceChild("right_top_bristle", 
/*  79 */         CubeListBuilder.create()
/*  80 */         .texOffs(16, 33).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), 
/*  81 */         PartPose.offsetAndRotation(-8.0F, -5.0F, -8.0F, 0.0F, 0.0F, -0.87266463F));
/*     */     
/*  83 */     body.addOrReplaceChild("left_top_bristle", 
/*  84 */         CubeListBuilder.create()
/*  85 */         .texOffs(16, 33).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), 
/*  86 */         PartPose.offsetAndRotation(8.0F, -6.0F, -8.0F, 0.0F, 0.0F, 0.87266463F));
/*     */     
/*  88 */     body.addOrReplaceChild("left_middle_bristle", 
/*  89 */         CubeListBuilder.create()
/*  90 */         .texOffs(16, 49).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), 
/*  91 */         PartPose.offsetAndRotation(8.0F, -2.0F, -8.0F, 0.0F, 0.0F, 1.134464F));
/*     */     
/*  93 */     body.addOrReplaceChild("left_bottom_bristle", 
/*  94 */         CubeListBuilder.create()
/*  95 */         .texOffs(16, 65).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), 
/*  96 */         PartPose.offsetAndRotation(8.0F, 3.0F, -8.0F, 0.0F, 0.0F, 1.2217305F));
/*     */ 
/*     */     
/*  99 */     return LayerDefinition.create(mesh, 64, 128);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(StriderRenderState state) {
/* 104 */     super.setupAnim(state);
/*     */     
/* 106 */     float animationPos = state.walkAnimationPos;
/* 107 */     float animationSpeed = Math.min(state.walkAnimationSpeed, 0.25F);
/*     */     
/* 109 */     if (!state.isRidden) {
/* 110 */       this.body.xRot = state.xRot * 0.017453292F;
/* 111 */       this.body.yRot = state.yRot * 0.017453292F;
/*     */     } else {
/* 113 */       this.body.xRot = 0.0F;
/* 114 */       this.body.yRot = 0.0F;
/*     */     } 
/*     */     
/* 117 */     float speed = 1.5F;
/*     */     
/* 119 */     this.body.zRot = 0.1F * Mth.sin((animationPos * 1.5F)) * 4.0F * animationSpeed;
/*     */     
/* 121 */     this.body.y = 2.0F;
/* 122 */     this.body.y -= 2.0F * Mth.cos((animationPos * 1.5F)) * 2.0F * animationSpeed;
/*     */     
/* 124 */     this.leftLeg.xRot = Mth.sin((animationPos * 1.5F * 0.5F)) * 2.0F * animationSpeed;
/* 125 */     this.rightLeg.xRot = Mth.sin((animationPos * 1.5F * 0.5F + 3.1415927F)) * 2.0F * animationSpeed;
/*     */     
/* 127 */     this.leftLeg.zRot = 0.17453292F * Mth.cos((animationPos * 1.5F * 0.5F)) * animationSpeed;
/* 128 */     this.rightLeg.zRot = 0.17453292F * Mth.cos((animationPos * 1.5F * 0.5F + 3.1415927F)) * animationSpeed;
/*     */     
/* 130 */     this.leftLeg.y = 8.0F + 2.0F * Mth.sin((animationPos * 1.5F * 0.5F + 3.1415927F)) * 2.0F * animationSpeed;
/* 131 */     this.rightLeg.y = 8.0F + 2.0F * Mth.sin((animationPos * 1.5F * 0.5F)) * 2.0F * animationSpeed;
/*     */     
/* 133 */     this.rightBottomBristle.zRot = -1.2217305F;
/* 134 */     this.rightMiddleBristle.zRot = -1.134464F;
/* 135 */     this.rightTopBristle.zRot = -0.87266463F;
/* 136 */     this.leftTopBristle.zRot = 0.87266463F;
/* 137 */     this.leftMiddleBristle.zRot = 1.134464F;
/* 138 */     this.leftBottomBristle.zRot = 1.2217305F;
/*     */     
/* 140 */     float bristleFlow = Mth.cos((animationPos * 1.5F + 3.1415927F)) * animationSpeed;
/*     */     
/* 142 */     this.rightBottomBristle.zRot += bristleFlow * 1.3F;
/* 143 */     this.rightMiddleBristle.zRot += bristleFlow * 1.2F;
/* 144 */     this.rightTopBristle.zRot += bristleFlow * 0.6F;
/*     */     
/* 146 */     this.leftTopBristle.zRot += bristleFlow * 0.6F;
/* 147 */     this.leftMiddleBristle.zRot += bristleFlow * 1.2F;
/* 148 */     this.leftBottomBristle.zRot += bristleFlow * 1.3F;
/*     */     
/* 150 */     float bristleRangeMod = 1.0F;
/* 151 */     float bristleSpeedMod = 1.0F;
/*     */     
/* 153 */     this.rightBottomBristle.zRot += 0.05F * Mth.sin((state.ageInTicks * 1.0F * -0.4F));
/* 154 */     this.rightMiddleBristle.zRot += 0.1F * Mth.sin((state.ageInTicks * 1.0F * 0.2F));
/* 155 */     this.rightTopBristle.zRot += 0.1F * Mth.sin((state.ageInTicks * 1.0F * 0.4F));
/*     */     
/* 157 */     this.leftTopBristle.zRot += 0.1F * Mth.sin((state.ageInTicks * 1.0F * 0.4F));
/* 158 */     this.leftMiddleBristle.zRot += 0.1F * Mth.sin((state.ageInTicks * 1.0F * 0.2F));
/* 159 */     this.leftBottomBristle.zRot += 0.05F * Mth.sin((state.ageInTicks * 1.0F * -0.4F));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/strider/StriderModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */