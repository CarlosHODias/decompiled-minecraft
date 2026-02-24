/*     */ package net.minecraft.client.model.monster.ravager;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.RavagerRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class RavagerModel
/*     */   extends EntityModel<RavagerRenderState>
/*     */ {
/*     */   private final ModelPart head;
/*     */   private final ModelPart mouth;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart neck;
/*     */   
/*     */   public RavagerModel(ModelPart root) {
/*  25 */     super(root);
/*  26 */     this.neck = root.getChild("neck");
/*  27 */     this.head = this.neck.getChild("head");
/*  28 */     this.mouth = this.head.getChild("mouth");
/*  29 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  30 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  31 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  32 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  36 */     MeshDefinition mesh = new MeshDefinition();
/*  37 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  39 */     int legSize = 16;
/*  40 */     PartDefinition neck = root.addOrReplaceChild("neck", 
/*  41 */         CubeListBuilder.create()
/*  42 */         .texOffs(68, 73).addBox(-5.0F, -1.0F, -18.0F, 10.0F, 10.0F, 18.0F), 
/*  43 */         PartPose.offset(0.0F, -7.0F, 5.5F));
/*     */     
/*  45 */     PartDefinition head = neck.addOrReplaceChild("head", 
/*  46 */         CubeListBuilder.create()
/*  47 */         .texOffs(0, 0).addBox(-8.0F, -20.0F, -14.0F, 16.0F, 20.0F, 16.0F)
/*  48 */         .texOffs(0, 0).addBox(-2.0F, -6.0F, -18.0F, 4.0F, 8.0F, 4.0F), 
/*  49 */         PartPose.offset(0.0F, 16.0F, -17.0F));
/*     */ 
/*     */     
/*  52 */     head.addOrReplaceChild("right_horn", 
/*  53 */         CubeListBuilder.create()
/*  54 */         .texOffs(74, 55).addBox(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), 
/*  55 */         PartPose.offsetAndRotation(-10.0F, -14.0F, -8.0F, 1.0995574F, 0.0F, 0.0F));
/*     */     
/*  57 */     head.addOrReplaceChild("left_horn", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(74, 55).mirror().addBox(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), 
/*  60 */         PartPose.offsetAndRotation(8.0F, -14.0F, -8.0F, 1.0995574F, 0.0F, 0.0F));
/*     */     
/*  62 */     head.addOrReplaceChild("mouth", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(0, 36).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 3.0F, 16.0F), 
/*  65 */         PartPose.offset(0.0F, -2.0F, 2.0F));
/*     */     
/*  67 */     root.addOrReplaceChild("body", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(0, 55).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 16.0F, 20.0F)
/*  70 */         .texOffs(0, 91).addBox(-6.0F, 6.0F, -7.0F, 12.0F, 13.0F, 18.0F), 
/*  71 */         PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  73 */     root.addOrReplaceChild("right_hind_leg", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(96, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), 
/*  76 */         PartPose.offset(-8.0F, -13.0F, 18.0F));
/*     */     
/*  78 */     root.addOrReplaceChild("left_hind_leg", 
/*  79 */         CubeListBuilder.create()
/*  80 */         .texOffs(96, 0).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), 
/*  81 */         PartPose.offset(8.0F, -13.0F, 18.0F));
/*     */     
/*  83 */     root.addOrReplaceChild("right_front_leg", 
/*  84 */         CubeListBuilder.create()
/*  85 */         .texOffs(64, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), 
/*  86 */         PartPose.offset(-8.0F, -13.0F, -5.0F));
/*     */     
/*  88 */     root.addOrReplaceChild("left_front_leg", 
/*  89 */         CubeListBuilder.create()
/*  90 */         .texOffs(64, 0).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), 
/*  91 */         PartPose.offset(8.0F, -13.0F, -5.0F));
/*     */ 
/*     */     
/*  94 */     return LayerDefinition.create(mesh, 128, 128);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(RavagerRenderState state) {
/*  99 */     super.setupAnim(state);
/*     */     
/* 101 */     float stunnedTick = state.stunnedTicksRemaining;
/* 102 */     float attackTick = state.attackTicksRemaining;
/* 103 */     int attackTime = 10;
/*     */ 
/*     */     
/* 106 */     if (attackTick > 0.0F) {
/* 107 */       float headAnim = Mth.triangleWave(attackTick, 10.0F);
/* 108 */       float scaled = (1.0F + headAnim) * 0.5F;
/* 109 */       float headPos = scaled * scaled * scaled * 12.0F;
/* 110 */       float yOffset = headPos * Mth.sin(this.neck.xRot);
/* 111 */       this.neck.z = -6.5F + headPos;
/* 112 */       this.neck.y = -7.0F - yOffset;
/*     */       
/* 114 */       if (attackTick > 5.0F) {
/* 115 */         this.mouth.xRot = Mth.sin(((-4.0F + attackTick) / 4.0F)) * 3.1415927F * 0.4F;
/*     */       } else {
/* 117 */         this.mouth.xRot = 0.15707964F * Mth.sin((3.1415927F * attackTick / 10.0F));
/*     */       } 
/*     */     } else {
/* 120 */       float headPos = -1.0F;
/* 121 */       float yOffset = -1.0F * Mth.sin(this.neck.xRot);
/* 122 */       this.neck.x = 0.0F;
/* 123 */       this.neck.y = -7.0F - yOffset;
/* 124 */       this.neck.z = 5.5F;
/*     */       
/* 126 */       boolean isStunned = (stunnedTick > 0.0F);
/* 127 */       this.neck.xRot = isStunned ? 0.21991149F : 0.0F;
/* 128 */       this.mouth.xRot = 3.1415927F * (isStunned ? 0.05F : 0.01F);
/*     */       
/* 130 */       if (isStunned) {
/* 131 */         double speed = stunnedTick / 40.0D;
/* 132 */         this.neck.x = (float)Math.sin(speed * 10.0D) * 3.0F;
/* 133 */       } else if (state.roarAnimation > 0.0D) {
/* 134 */         float mouthAnim = Mth.sin((state.roarAnimation * 3.1415927F * 0.25F));
/* 135 */         this.mouth.xRot = 1.5707964F * mouthAnim;
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     this.head.xRot = state.xRot * 0.017453292F;
/* 140 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */     
/* 142 */     float animationPos = state.walkAnimationPos;
/* 143 */     float legRot = 0.4F * state.walkAnimationSpeed;
/* 144 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * legRot;
/* 145 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * legRot;
/* 146 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * legRot;
/* 147 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * legRot;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/ravager/RavagerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */