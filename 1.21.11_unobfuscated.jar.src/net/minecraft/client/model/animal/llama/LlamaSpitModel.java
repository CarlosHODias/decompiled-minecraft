/*    */ package net.minecraft.client.model.animal.llama;
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
/*    */ public class LlamaSpitModel extends EntityModel<EntityRenderState> {
/*    */   private static final String MAIN = "main";
/*    */   
/*    */   public LlamaSpitModel(ModelPart root) {
/* 16 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 20 */     MeshDefinition mesh = new MeshDefinition();
/* 21 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 23 */     int edge = 2;
/* 24 */     root.addOrReplaceChild("main", 
/* 25 */         CubeListBuilder.create()
/* 26 */         .texOffs(0, 0)
/* 27 */         .addBox(-4.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
/* 28 */         .addBox(0.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F)
/* 29 */         .addBox(0.0F, 0.0F, -4.0F, 2.0F, 2.0F, 2.0F)
/*    */         
/* 31 */         .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
/* 32 */         .addBox(2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
/* 33 */         .addBox(0.0F, 2.0F, 0.0F, 2.0F, 2.0F, 2.0F)
/* 34 */         .addBox(0.0F, 0.0F, 2.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 37 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/llama/LlamaSpitModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */