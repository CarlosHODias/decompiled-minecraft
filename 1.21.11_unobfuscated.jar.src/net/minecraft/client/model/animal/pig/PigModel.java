/*    */ package net.minecraft.client.model.animal.pig;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.model.BabyModelTransform;
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ 
/*    */ public class PigModel
/*    */   extends QuadrupedModel<LivingEntityRenderState>
/*    */ {
/* 19 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(false, 4.0F, 4.0F, Set.of("head"));
/*    */   
/*    */   public PigModel(ModelPart root) {
/* 22 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 26 */     return LayerDefinition.create(createBasePigModel(g), 64, 64);
/*    */   }
/*    */   
/*    */   protected static MeshDefinition createBasePigModel(CubeDeformation g) {
/* 30 */     MeshDefinition mesh = QuadrupedModel.createBodyMesh(6, true, false, g);
/* 31 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 33 */     root.addOrReplaceChild("head", CubeListBuilder.create()
/* 34 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, g)
/* 35 */         .texOffs(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, g), 
/* 36 */         PartPose.offset(0.0F, 12.0F, -6.0F));
/*    */     
/* 38 */     return mesh;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/pig/PigModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */