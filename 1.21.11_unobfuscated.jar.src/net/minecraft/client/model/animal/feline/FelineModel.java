/*     */ package net.minecraft.client.model.animal.feline;
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
/*     */ import net.minecraft.client.renderer.entity.state.FelineRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class FelineModel<T extends FelineRenderState>
/*     */   extends EntityModel<T>
/*     */ {
/*  19 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 10.0F, 4.0F, Set.of("head"));
/*     */   
/*     */   private static final float XO = 0.0F;
/*     */   
/*     */   private static final float YO = 16.0F;
/*     */   
/*     */   private static final float ZO = -9.0F;
/*     */   
/*     */   protected static final float BACK_LEG_Y = 18.0F;
/*     */   protected static final float BACK_LEG_Z = 5.0F;
/*     */   protected static final float FRONT_LEG_Y = 14.1F;
/*     */   private static final float FRONT_LEG_Z = -5.0F;
/*     */   private static final String TAIL_1 = "tail1";
/*     */   private static final String TAIL_2 = "tail2";
/*     */   protected final ModelPart leftHindLeg;
/*     */   protected final ModelPart rightHindLeg;
/*     */   protected final ModelPart leftFrontLeg;
/*     */   protected final ModelPart rightFrontLeg;
/*     */   protected final ModelPart tail1;
/*     */   protected final ModelPart tail2;
/*     */   protected final ModelPart head;
/*     */   protected final ModelPart body;
/*     */   
/*     */   public FelineModel(ModelPart root) {
/*  43 */     super(root);
/*  44 */     this.head = root.getChild("head");
/*  45 */     this.body = root.getChild("body");
/*  46 */     this.tail1 = root.getChild("tail1");
/*  47 */     this.tail2 = root.getChild("tail2");
/*  48 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  49 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  50 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  51 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBodyMesh(CubeDeformation g) {
/*  55 */     MeshDefinition mesh = new MeshDefinition();
/*  56 */     PartDefinition root = mesh.getRoot();
/*  57 */     CubeDeformation tail_g = new CubeDeformation(-0.02F);
/*     */     
/*  59 */     root.addOrReplaceChild("head", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .addBox("main", -2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, g)
/*  62 */         .addBox("nose", -1.5F, -0.001F, -4.0F, 3, 2, 2, g, 0, 24)
/*  63 */         .addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2, g, 0, 10)
/*  64 */         .addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2, g, 6, 10), 
/*  65 */         PartPose.offset(0.0F, 15.0F, -9.0F));
/*     */     
/*  67 */     root.addOrReplaceChild("body", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(20, 0).addBox(-2.0F, 3.0F, -8.0F, 4.0F, 16.0F, 6.0F, g), 
/*  70 */         PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  72 */     root.addOrReplaceChild("tail1", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, g), 
/*  75 */         PartPose.offsetAndRotation(0.0F, 15.0F, 8.0F, 0.9F, 0.0F, 0.0F));
/*     */     
/*  77 */     root.addOrReplaceChild("tail2", 
/*  78 */         CubeListBuilder.create()
/*  79 */         .texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, tail_g), 
/*  80 */         PartPose.offset(0.0F, 20.0F, 14.0F));
/*     */     
/*  82 */     CubeListBuilder hindLeg = CubeListBuilder.create()
/*  83 */       .texOffs(8, 13).addBox(-1.0F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, g);
/*  84 */     root.addOrReplaceChild("left_hind_leg", hindLeg, PartPose.offset(1.1F, 18.0F, 5.0F));
/*  85 */     root.addOrReplaceChild("right_hind_leg", hindLeg, PartPose.offset(-1.1F, 18.0F, 5.0F));
/*     */     
/*  87 */     CubeListBuilder frontLeg = CubeListBuilder.create()
/*  88 */       .texOffs(40, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, g);
/*  89 */     root.addOrReplaceChild("left_front_leg", frontLeg, PartPose.offset(1.2F, 14.1F, -5.0F));
/*  90 */     root.addOrReplaceChild("right_front_leg", frontLeg, PartPose.offset(-1.2F, 14.1F, -5.0F));
/*     */     
/*  92 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(T state) {
/*  97 */     super.setupAnim(state);
/*     */     
/*  99 */     float ageScale = ((FelineRenderState)state).ageScale;
/*     */     
/* 101 */     if (((FelineRenderState)state).isCrouching) {
/* 102 */       this.body.y += 1.0F * ageScale;
/* 103 */       this.head.y += 2.0F * ageScale;
/* 104 */       this.tail1.y += 1.0F * ageScale;
/* 105 */       this.tail2.y += -4.0F * ageScale;
/* 106 */       this.tail2.z += 2.0F * ageScale;
/* 107 */       this.tail1.xRot = 1.5707964F;
/* 108 */       this.tail2.xRot = 1.5707964F;
/* 109 */     } else if (((FelineRenderState)state).isSprinting) {
/* 110 */       this.tail2.y = this.tail1.y;
/* 111 */       this.tail2.z += 2.0F * ageScale;
/* 112 */       this.tail1.xRot = 1.5707964F;
/* 113 */       this.tail2.xRot = 1.5707964F;
/*     */     } 
/*     */     
/* 116 */     this.head.xRot = ((FelineRenderState)state).xRot * 0.017453292F;
/* 117 */     this.head.yRot = ((FelineRenderState)state).yRot * 0.017453292F;
/*     */     
/* 119 */     if (!((FelineRenderState)state).isSitting) {
/* 120 */       this.body.xRot = 1.5707964F;
/* 121 */       float animationSpeed = ((FelineRenderState)state).walkAnimationSpeed;
/* 122 */       float animationPos = ((FelineRenderState)state).walkAnimationPos;
/* 123 */       if (((FelineRenderState)state).isSprinting) {
/* 124 */         this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/* 125 */         this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 0.3F)) * animationSpeed;
/* 126 */         this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F + 0.3F)) * animationSpeed;
/* 127 */         this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/* 128 */         this.tail2.xRot = 1.7278761F + 0.31415927F * Mth.cos(animationPos) * animationSpeed;
/*     */       } else {
/* 130 */         this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/* 131 */         this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/* 132 */         this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/* 133 */         this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/*     */         
/* 135 */         if (!((FelineRenderState)state).isCrouching) {
/* 136 */           this.tail2.xRot = 1.7278761F + 0.7853982F * Mth.cos(animationPos) * animationSpeed;
/*     */         } else {
/* 138 */           this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(animationPos) * animationSpeed;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     if (((FelineRenderState)state).isSitting) {
/* 144 */       this.body.xRot = 0.7853982F;
/* 145 */       this.body.y += -4.0F * ageScale;
/* 146 */       this.body.z += 5.0F * ageScale;
/* 147 */       this.head.y += -3.3F * ageScale;
/* 148 */       this.head.z += 1.0F * ageScale;
/*     */       
/* 150 */       this.tail1.y += 8.0F * ageScale;
/* 151 */       this.tail1.z += -2.0F * ageScale;
/* 152 */       this.tail2.y += 2.0F * ageScale;
/* 153 */       this.tail2.z += -0.8F * ageScale;
/* 154 */       this.tail1.xRot = 1.7278761F;
/* 155 */       this.tail2.xRot = 2.670354F;
/*     */       
/* 157 */       this.leftFrontLeg.xRot = -0.15707964F;
/* 158 */       this.leftFrontLeg.y += 2.0F * ageScale;
/* 159 */       this.leftFrontLeg.z -= 2.0F * ageScale;
/*     */       
/* 161 */       this.rightFrontLeg.xRot = -0.15707964F;
/* 162 */       this.rightFrontLeg.y += 2.0F * ageScale;
/* 163 */       this.rightFrontLeg.z -= 2.0F * ageScale;
/*     */       
/* 165 */       this.leftHindLeg.xRot = -1.5707964F;
/* 166 */       this.leftHindLeg.y += 3.0F * ageScale;
/* 167 */       this.leftHindLeg.z -= 4.0F * ageScale;
/*     */       
/* 169 */       this.rightHindLeg.xRot = -1.5707964F;
/* 170 */       this.rightHindLeg.y += 3.0F * ageScale;
/* 171 */       this.rightHindLeg.z -= 4.0F * ageScale;
/*     */     } 
/*     */     
/* 174 */     if (((FelineRenderState)state).lieDownAmount > 0.0F) {
/* 175 */       this.head.zRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmount, this.head.zRot, -1.2707963F);
/* 176 */       this.head.yRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmount, this.head.yRot, 1.2707963F);
/* 177 */       this.leftFrontLeg.xRot = -1.2707963F;
/* 178 */       this.rightFrontLeg.xRot = -0.47079635F;
/* 179 */       this.rightFrontLeg.zRot = -0.2F;
/* 180 */       this.rightFrontLeg.x += ageScale;
/* 181 */       this.leftHindLeg.xRot = -0.4F;
/* 182 */       this.rightHindLeg.xRot = 0.5F;
/* 183 */       this.rightHindLeg.zRot = -0.5F;
/* 184 */       this.rightHindLeg.x += 0.8F * ageScale;
/* 185 */       this.rightHindLeg.y += 2.0F * ageScale;
/*     */       
/* 187 */       this.tail1.xRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail1.xRot, 0.8F);
/* 188 */       this.tail2.xRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail2.xRot, -0.4F);
/*     */     } 
/*     */     
/* 191 */     if (((FelineRenderState)state).relaxStateOneAmount > 0.0F)
/* 192 */       this.head.xRot = Mth.rotLerp(((FelineRenderState)state).relaxStateOneAmount, this.head.xRot, -0.58177644F); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/feline/FelineModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */