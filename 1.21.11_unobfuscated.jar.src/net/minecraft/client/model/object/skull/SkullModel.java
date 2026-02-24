/*    */ package net.minecraft.client.model.object.skull;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ 
/*    */ public class SkullModel
/*    */   extends SkullModelBase
/*    */ {
/*    */   protected final ModelPart head;
/*    */   
/*    */   public SkullModel(ModelPart root) {
/* 18 */     super(root);
/* 19 */     this.head = root.getChild("head");
/*    */   }
/*    */   
/*    */   public static MeshDefinition createHeadModel() {
/* 23 */     MeshDefinition mesh = new MeshDefinition();
/* 24 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 26 */     root.addOrReplaceChild("head", 
/* 27 */         CubeListBuilder.create()
/* 28 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 31 */     return mesh;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createHumanoidHeadLayer() {
/* 35 */     MeshDefinition mesh = createHeadModel();
/*    */     
/* 37 */     PartDefinition root = mesh.getRoot();
/* 38 */     root.getChild("head").addOrReplaceChild("hat", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 44 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createMobHeadLayer() {
/* 48 */     MeshDefinition mesh = createHeadModel();
/* 49 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SkullModelBase.State state) {
/* 54 */     super.setupAnim(state);
/* 55 */     this.head.yRot = state.yRot * 0.017453292F;
/* 56 */     this.head.xRot = state.xRot * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/skull/SkullModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */