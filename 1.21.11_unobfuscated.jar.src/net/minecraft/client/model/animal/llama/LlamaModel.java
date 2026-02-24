/*     */ package net.minecraft.client.model.animal.llama;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.LlamaRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class LlamaModel
/*     */   extends EntityModel<LlamaRenderState>
/*     */ {
/*  20 */   public static final MeshTransformer BABY_TRANSFORMER = LlamaModel::transformToBaby;
/*     */   
/*     */   private final ModelPart head;
/*     */   
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart leftFrontLeg;
/*     */   private final ModelPart rightChest;
/*     */   private final ModelPart leftChest;
/*     */   
/*     */   public LlamaModel(ModelPart root) {
/*  32 */     super(root);
/*  33 */     this.head = root.getChild("head");
/*  34 */     this.rightChest = root.getChild("right_chest");
/*  35 */     this.leftChest = root.getChild("left_chest");
/*  36 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  37 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  38 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  39 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/*  43 */     MeshDefinition mesh = new MeshDefinition();
/*  44 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  46 */     root.addOrReplaceChild("head", 
/*  47 */         CubeListBuilder.create()
/*  48 */         .texOffs(0, 0).addBox(-2.0F, -14.0F, -10.0F, 4.0F, 4.0F, 9.0F, g)
/*  49 */         .texOffs(0, 14).addBox("neck", -4.0F, -16.0F, -6.0F, 8.0F, 18.0F, 6.0F, g)
/*  50 */         .texOffs(17, 0).addBox("ear", -4.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, g)
/*  51 */         .texOffs(17, 0).addBox("ear", 1.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, g), 
/*  52 */         PartPose.offset(0.0F, 7.0F, -6.0F));
/*     */     
/*  54 */     root.addOrReplaceChild("body", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(29, 0).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, g), 
/*  57 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  59 */     root.addOrReplaceChild("right_chest", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .texOffs(45, 28).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, g), 
/*  62 */         PartPose.offsetAndRotation(-8.5F, 3.0F, 3.0F, 0.0F, 1.5707964F, 0.0F));
/*     */     
/*  64 */     root.addOrReplaceChild("left_chest", 
/*  65 */         CubeListBuilder.create()
/*  66 */         .texOffs(45, 41).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, g), 
/*  67 */         PartPose.offsetAndRotation(5.5F, 3.0F, 3.0F, 0.0F, 1.5707964F, 0.0F));
/*     */     
/*  69 */     int legWidth = 4;
/*  70 */     int legHeight = 14;
/*  71 */     CubeListBuilder leg = CubeListBuilder.create()
/*  72 */       .texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, g);
/*  73 */     root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-3.5F, 10.0F, 6.0F));
/*  74 */     root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(3.5F, 10.0F, 6.0F));
/*  75 */     root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-3.5F, 10.0F, -5.0F));
/*  76 */     root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(3.5F, 10.0F, -5.0F));
/*     */     
/*  78 */     return LayerDefinition.create(mesh, 128, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MeshDefinition transformToBaby(MeshDefinition mesh) {
/*  83 */     float scale = 2.0F;
/*  84 */     float headScale = 0.7F;
/*  85 */     float bodyScale = 1.1F;
/*     */     
/*     */     UnaryOperator<PartPose> headTransform = p -> p.translated(0.0F, 21.0F, 3.52F).scaled(0.71428573F, 0.64935064F, 0.7936508F);
/*     */     
/*     */     UnaryOperator<PartPose> bodyTransform = p -> p.translated(0.0F, 33.0F, 0.0F).scaled(0.625F, 0.45454544F, 0.45454544F);
/*     */     UnaryOperator<PartPose> defaultTransform = p -> p.translated(0.0F, 33.0F, 0.0F).scaled(0.45454544F, 0.41322312F, 0.45454544F);
/*  91 */     MeshDefinition babyMesh = new MeshDefinition();
/*     */     
/*  93 */     for (Map.Entry<String, PartDefinition> entry : (Iterable<Map.Entry<String, PartDefinition>>)mesh.getRoot().getChildren()) {
/*  94 */       String name = entry.getKey();
/*  95 */       PartDefinition part = entry.getValue();
/*  96 */       switch (name) { case "head": 
/*     */         case "body": 
/*     */         default:
/*  99 */           break; }  UnaryOperator<PartPose> transform = defaultTransform;
/*     */       
/* 101 */       babyMesh.getRoot().addOrReplaceChild(name, part.transformed(transform));
/*     */     } 
/*     */     
/* 104 */     return babyMesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(LlamaRenderState state) {
/* 109 */     super.setupAnim(state);
/*     */     
/* 111 */     this.head.xRot = state.xRot * 0.017453292F;
/* 112 */     this.head.yRot = state.yRot * 0.017453292F;
/*     */     
/* 114 */     float animationSpeed = state.walkAnimationSpeed;
/* 115 */     float animationPos = state.walkAnimationPos;
/* 116 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 117 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 118 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 119 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 120 */     this.rightChest.visible = state.hasChest;
/* 121 */     this.leftChest.visible = state.hasChest;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/llama/LlamaModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */