/*    */ package net.minecraft.client.model.object.boat;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class RaftModel
/*    */   extends AbstractBoatModel
/*    */ {
/*    */   public RaftModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   private static void addCommonParts(PartDefinition root) {
/* 18 */     root.addOrReplaceChild("bottom", 
/* 19 */         CubeListBuilder.create()
/* 20 */         .texOffs(0, 0).addBox(-14.0F, -11.0F, -4.0F, 28.0F, 20.0F, 4.0F)
/* 21 */         .texOffs(0, 0).addBox(-14.0F, -9.0F, -8.0F, 28.0F, 16.0F, 4.0F), 
/* 22 */         PartPose.offsetAndRotation(0.0F, -2.1F, 1.0F, 1.5708F, 0.0F, 0.0F));
/*    */ 
/*    */     
/* 25 */     int totalLength = 20;
/* 26 */     int bladeLength = 7;
/* 27 */     int bladeWidth = 6;
/* 28 */     float pivot = -5.0F;
/*    */     
/* 30 */     root.addOrReplaceChild("left_paddle", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 24)
/* 33 */         .addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F)
/* 34 */         .addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), 
/* 35 */         PartPose.offsetAndRotation(3.0F, -4.0F, 9.0F, 0.0F, 0.0F, 0.19634955F));
/*    */     
/* 37 */     root.addOrReplaceChild("right_paddle", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(40, 24)
/* 40 */         .addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F)
/* 41 */         .addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), 
/* 42 */         PartPose.offsetAndRotation(3.0F, -4.0F, -9.0F, 0.0F, 3.1415927F, 0.19634955F));
/*    */   }
/*    */ 
/*    */   
/*    */   public static LayerDefinition createRaftModel() {
/* 47 */     MeshDefinition mesh = new MeshDefinition();
/* 48 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 50 */     addCommonParts(root);
/*    */     
/* 52 */     return LayerDefinition.create(mesh, 128, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createChestRaftModel() {
/* 56 */     MeshDefinition mesh = new MeshDefinition();
/* 57 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 59 */     addCommonParts(root);
/*    */     
/* 61 */     root.addOrReplaceChild("chest_bottom", 
/* 62 */         CubeListBuilder.create()
/* 63 */         .texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 12.0F, 8.0F, 12.0F), 
/* 64 */         PartPose.offsetAndRotation(-2.0F, -10.1F, -6.0F, 0.0F, -1.5707964F, 0.0F));
/*    */ 
/*    */     
/* 67 */     root.addOrReplaceChild("chest_lid", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 12.0F, 4.0F, 12.0F), 
/* 70 */         PartPose.offsetAndRotation(-2.0F, -14.1F, -6.0F, 0.0F, -1.5707964F, 0.0F));
/*    */ 
/*    */     
/* 73 */     root.addOrReplaceChild("chest_lock", 
/* 74 */         CubeListBuilder.create()
/* 75 */         .texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F), 
/* 76 */         PartPose.offsetAndRotation(-1.0F, -11.1F, -1.0F, 0.0F, -1.5707964F, 0.0F));
/*    */ 
/*    */     
/* 79 */     return LayerDefinition.create(mesh, 128, 128);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/boat/RaftModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */