/*     */ package net.minecraft.client.model.object.boat;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ 
/*     */ 
/*     */ public class BoatModel
/*     */   extends AbstractBoatModel
/*     */ {
/*     */   private static final int BOTTOM_WIDTH = 28;
/*     */   private static final int WIDTH = 32;
/*     */   private static final int DEPTH = 6;
/*     */   private static final int LENGTH = 20;
/*     */   private static final int Y_OFFSET = 4;
/*     */   private static final String WATER_PATCH = "water_patch";
/*     */   private static final String BACK = "back";
/*     */   private static final String FRONT = "front";
/*     */   private static final String RIGHT = "right";
/*     */   private static final String LEFT = "left";
/*     */   
/*     */   public BoatModel(ModelPart root) {
/*  26 */     super(root);
/*     */   }
/*     */   
/*     */   private static void addCommonParts(PartDefinition root) {
/*  30 */     int halfWidth = 16;
/*  31 */     int halfBottomWidth = 14;
/*  32 */     int halfLength = 10;
/*     */     
/*  34 */     root.addOrReplaceChild("bottom", 
/*  35 */         CubeListBuilder.create()
/*  36 */         .texOffs(0, 0).addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F), 
/*  37 */         PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 1.5707964F, 0.0F, 0.0F));
/*     */     
/*  39 */     root.addOrReplaceChild("back", 
/*  40 */         CubeListBuilder.create()
/*  41 */         .texOffs(0, 19).addBox(-13.0F, -7.0F, -1.0F, 18.0F, 6.0F, 2.0F), 
/*  42 */         PartPose.offsetAndRotation(-15.0F, 4.0F, 4.0F, 0.0F, 4.712389F, 0.0F));
/*     */     
/*  44 */     root.addOrReplaceChild("front", 
/*  45 */         CubeListBuilder.create()
/*  46 */         .texOffs(0, 27).addBox(-8.0F, -7.0F, -1.0F, 16.0F, 6.0F, 2.0F), 
/*  47 */         PartPose.offsetAndRotation(15.0F, 4.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
/*     */     
/*  49 */     root.addOrReplaceChild("right", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(0, 35).addBox(-14.0F, -7.0F, -1.0F, 28.0F, 6.0F, 2.0F), 
/*  52 */         PartPose.offsetAndRotation(0.0F, 4.0F, -9.0F, 0.0F, 3.1415927F, 0.0F));
/*     */     
/*  54 */     root.addOrReplaceChild("left", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(0, 43).addBox(-14.0F, -7.0F, -1.0F, 28.0F, 6.0F, 2.0F), 
/*  57 */         PartPose.offset(0.0F, 4.0F, 9.0F));
/*     */ 
/*     */     
/*  60 */     int totalLength = 20;
/*  61 */     int bladeLength = 7;
/*  62 */     int bladeWidth = 6;
/*  63 */     float pivot = -5.0F;
/*     */     
/*  65 */     root.addOrReplaceChild("left_paddle", 
/*  66 */         CubeListBuilder.create()
/*  67 */         .texOffs(62, 0)
/*  68 */         .addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F)
/*  69 */         .addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), 
/*  70 */         PartPose.offsetAndRotation(3.0F, -5.0F, 9.0F, 0.0F, 0.0F, 0.19634955F));
/*     */     
/*  72 */     root.addOrReplaceChild("right_paddle", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(62, 20)
/*  75 */         .addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F)
/*  76 */         .addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), 
/*  77 */         PartPose.offsetAndRotation(3.0F, -5.0F, -9.0F, 0.0F, 3.1415927F, 0.19634955F));
/*     */   }
/*     */ 
/*     */   
/*     */   public static LayerDefinition createBoatModel() {
/*  82 */     MeshDefinition mesh = new MeshDefinition();
/*  83 */     PartDefinition root = mesh.getRoot();
/*  84 */     addCommonParts(root);
/*  85 */     return LayerDefinition.create(mesh, 128, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createChestBoatModel() {
/*  89 */     MeshDefinition mesh = new MeshDefinition();
/*  90 */     PartDefinition root = mesh.getRoot();
/*  91 */     addCommonParts(root);
/*     */     
/*  93 */     root.addOrReplaceChild("chest_bottom", 
/*  94 */         CubeListBuilder.create()
/*  95 */         .texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 12.0F, 8.0F, 12.0F), 
/*  96 */         PartPose.offsetAndRotation(-2.0F, -5.0F, -6.0F, 0.0F, -1.5707964F, 0.0F));
/*     */ 
/*     */     
/*  99 */     root.addOrReplaceChild("chest_lid", 
/* 100 */         CubeListBuilder.create()
/* 101 */         .texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 12.0F, 4.0F, 12.0F), 
/* 102 */         PartPose.offsetAndRotation(-2.0F, -9.0F, -6.0F, 0.0F, -1.5707964F, 0.0F));
/*     */ 
/*     */     
/* 105 */     root.addOrReplaceChild("chest_lock", 
/* 106 */         CubeListBuilder.create()
/* 107 */         .texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F), 
/* 108 */         PartPose.offsetAndRotation(-1.0F, -6.0F, -1.0F, 0.0F, -1.5707964F, 0.0F));
/*     */ 
/*     */     
/* 111 */     return LayerDefinition.create(mesh, 128, 128);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createWaterPatch() {
/* 115 */     MeshDefinition mesh = new MeshDefinition();
/* 116 */     PartDefinition root = mesh.getRoot();
/*     */     
/* 118 */     root.addOrReplaceChild("water_patch", 
/* 119 */         CubeListBuilder.create()
/* 120 */         .texOffs(0, 0)
/* 121 */         .addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F), 
/* 122 */         PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 1.5707964F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 125 */     return LayerDefinition.create(mesh, 0, 0);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/boat/BoatModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */