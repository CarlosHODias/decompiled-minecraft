/*     */ package net.minecraft.client.model.animal.fish;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class PufferfishBigModel
/*     */   extends EntityModel<EntityRenderState> {
/*     */   private final ModelPart leftBlueFin;
/*     */   private final ModelPart rightBlueFin;
/*     */   
/*     */   public PufferfishBigModel(ModelPart root) {
/*  19 */     super(root);
/*  20 */     this.leftBlueFin = root.getChild("left_blue_fin");
/*  21 */     this.rightBlueFin = root.getChild("right_blue_fin");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  25 */     MeshDefinition mesh = new MeshDefinition();
/*  26 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  28 */     int yo = 22;
/*  29 */     root.addOrReplaceChild("body", 
/*  30 */         CubeListBuilder.create()
/*  31 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), 
/*  32 */         PartPose.offset(0.0F, 22.0F, 0.0F));
/*     */     
/*  34 */     root.addOrReplaceChild("right_blue_fin", 
/*  35 */         CubeListBuilder.create()
/*  36 */         .texOffs(24, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F), 
/*  37 */         PartPose.offset(-4.0F, 15.0F, -2.0F));
/*     */     
/*  39 */     root.addOrReplaceChild("left_blue_fin", 
/*  40 */         CubeListBuilder.create()
/*  41 */         .texOffs(24, 3).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F), 
/*  42 */         PartPose.offset(4.0F, 15.0F, -2.0F));
/*     */     
/*  44 */     root.addOrReplaceChild("top_front_fin", 
/*  45 */         CubeListBuilder.create()
/*  46 */         .texOffs(15, 17).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F), 
/*  47 */         PartPose.offsetAndRotation(0.0F, 14.0F, -4.0F, 0.7853982F, 0.0F, 0.0F));
/*     */     
/*  49 */     root.addOrReplaceChild("top_middle_fin", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(14, 16).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F), 
/*  52 */         PartPose.offset(0.0F, 14.0F, 0.0F));
/*     */     
/*  54 */     root.addOrReplaceChild("top_back_fin", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(23, 18).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F), 
/*  57 */         PartPose.offsetAndRotation(0.0F, 14.0F, 4.0F, -0.7853982F, 0.0F, 0.0F));
/*     */     
/*  59 */     root.addOrReplaceChild("right_front_fin", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .texOffs(5, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), 
/*  62 */         PartPose.offsetAndRotation(-4.0F, 22.0F, -4.0F, 0.0F, -0.7853982F, 0.0F));
/*     */     
/*  64 */     root.addOrReplaceChild("left_front_fin", 
/*  65 */         CubeListBuilder.create()
/*  66 */         .texOffs(1, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), 
/*  67 */         PartPose.offsetAndRotation(4.0F, 22.0F, -4.0F, 0.0F, 0.7853982F, 0.0F));
/*     */     
/*  69 */     root.addOrReplaceChild("bottom_front_fin", 
/*  70 */         CubeListBuilder.create()
/*  71 */         .texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), 
/*  72 */         PartPose.offsetAndRotation(0.0F, 22.0F, -4.0F, -0.7853982F, 0.0F, 0.0F));
/*     */     
/*  74 */     root.addOrReplaceChild("bottom_middle_fin", 
/*  75 */         CubeListBuilder.create()
/*  76 */         .texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), 
/*  77 */         PartPose.offset(0.0F, 22.0F, 0.0F));
/*     */     
/*  79 */     root.addOrReplaceChild("bottom_back_fin", 
/*  80 */         CubeListBuilder.create()
/*  81 */         .texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), 
/*  82 */         PartPose.offsetAndRotation(0.0F, 22.0F, 4.0F, 0.7853982F, 0.0F, 0.0F));
/*     */     
/*  84 */     root.addOrReplaceChild("right_back_fin", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(9, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), 
/*  87 */         PartPose.offsetAndRotation(-4.0F, 22.0F, 4.0F, 0.0F, 0.7853982F, 0.0F));
/*     */     
/*  89 */     root.addOrReplaceChild("left_back_fin", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(9, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), 
/*  92 */         PartPose.offsetAndRotation(4.0F, 22.0F, 4.0F, 0.0F, -0.7853982F, 0.0F));
/*     */ 
/*     */     
/*  95 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(EntityRenderState state) {
/* 100 */     super.setupAnim(state);
/* 101 */     this.rightBlueFin.zRot = -0.2F + 0.4F * Mth.sin((state.ageInTicks * 0.2F));
/* 102 */     this.leftBlueFin.zRot = 0.2F - 0.4F * Mth.sin((state.ageInTicks * 0.2F));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/fish/PufferfishBigModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */