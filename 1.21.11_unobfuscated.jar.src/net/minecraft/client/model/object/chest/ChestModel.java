/*    */ package net.minecraft.client.model.object.chest;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class ChestModel
/*    */   extends Model<Float>
/*    */ {
/*    */   private static final String BOTTOM = "bottom";
/*    */   private static final String LID = "lid";
/*    */   private static final String LOCK = "lock";
/*    */   private final ModelPart lid;
/*    */   private final ModelPart lock;
/*    */   
/*    */   public ChestModel(ModelPart root) {
/* 22 */     super(root, RenderTypes::entitySolid);
/* 23 */     this.lid = root.getChild("lid");
/* 24 */     this.lock = root.getChild("lock");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSingleBodyLayer() {
/* 28 */     MeshDefinition mesh = new MeshDefinition();
/* 29 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 31 */     root.addOrReplaceChild("bottom", 
/* 32 */         CubeListBuilder.create()
/* 33 */         .texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 36 */     root.addOrReplaceChild("lid", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), 
/* 39 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*    */     
/* 41 */     root.addOrReplaceChild("lock", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), 
/* 44 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*    */ 
/*    */     
/* 47 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createDoubleBodyRightLayer() {
/* 51 */     MeshDefinition mesh = new MeshDefinition();
/* 52 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 54 */     root.addOrReplaceChild("bottom", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 59 */     root.addOrReplaceChild("lid", 
/* 60 */         CubeListBuilder.create()
/* 61 */         .texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), 
/* 62 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*    */     
/* 64 */     root.addOrReplaceChild("lock", 
/* 65 */         CubeListBuilder.create()
/* 66 */         .texOffs(0, 0).addBox(15.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F), 
/* 67 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*    */     
/* 69 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createDoubleBodyLeftLayer() {
/* 73 */     MeshDefinition mesh = new MeshDefinition();
/* 74 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 76 */     root.addOrReplaceChild("bottom", 
/* 77 */         CubeListBuilder.create()
/* 78 */         .texOffs(0, 19).addBox(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 81 */     root.addOrReplaceChild("lid", 
/* 82 */         CubeListBuilder.create()
/* 83 */         .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), 
/* 84 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*    */     
/* 86 */     root.addOrReplaceChild("lock", 
/* 87 */         CubeListBuilder.create()
/* 88 */         .texOffs(0, 0).addBox(0.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F), 
/* 89 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*    */ 
/*    */     
/* 92 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(Float open) {
/* 97 */     super.setupAnim(open);
/* 98 */     this.lid.xRot = -(open * 1.5707964F);
/* 99 */     this.lock.xRot = this.lid.xRot;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/chest/ChestModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */