/*    */ package net.minecraft.client.model.object.leash;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ 
/*    */ public class LeashKnotModel
/*    */   extends EntityModel<EntityRenderState> {
/*    */   private static final String KNOT = "knot";
/*    */   private final ModelPart knot;
/*    */   
/*    */   public LeashKnotModel(ModelPart root) {
/* 18 */     super(root);
/* 19 */     this.knot = root.getChild("knot");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 23 */     MeshDefinition mesh = new MeshDefinition();
/* 24 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 26 */     root.addOrReplaceChild("knot", 
/* 27 */         CubeListBuilder.create()
/* 28 */         .texOffs(0, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 32 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/leash/LeashKnotModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */