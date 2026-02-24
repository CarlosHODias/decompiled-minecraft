/*    */ package net.minecraft.client.model.animal.cow;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.model.BabyModelTransform;
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ 
/*    */ 
/*    */ public class CowModel
/*    */   extends QuadrupedModel<LivingEntityRenderState>
/*    */ {
/* 19 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(false, 8.0F, 6.0F, Set.of("head"));
/*    */   private static final int LEG_SIZE = 12;
/*    */   
/*    */   public CowModel(ModelPart root) {
/* 23 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 27 */     MeshDefinition mesh = createBaseCowModel();
/* 28 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   static MeshDefinition createBaseCowModel() {
/* 32 */     MeshDefinition mesh = new MeshDefinition();
/* 33 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 35 */     root.addOrReplaceChild("head", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
/* 38 */         .texOffs(1, 33).addBox(-3.0F, 1.0F, -7.0F, 6.0F, 3.0F, 1.0F)
/* 39 */         .texOffs(22, 0).addBox("right_horn", -5.0F, -5.0F, -5.0F, 1.0F, 3.0F, 1.0F)
/* 40 */         .texOffs(22, 0).addBox("left_horn", 4.0F, -5.0F, -5.0F, 1.0F, 3.0F, 1.0F), 
/* 41 */         PartPose.offset(0.0F, 4.0F, -8.0F));
/*    */     
/* 43 */     root.addOrReplaceChild("body", 
/* 44 */         CubeListBuilder.create()
/* 45 */         .texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
/* 46 */         .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), 
/* 47 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*    */ 
/*    */     
/* 50 */     CubeListBuilder leftLeg = CubeListBuilder.create()
/* 51 */       .mirror().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
/* 52 */     CubeListBuilder rightLeg = CubeListBuilder.create()
/* 53 */       .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
/* 54 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-4.0F, 12.0F, 7.0F));
/* 55 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(4.0F, 12.0F, 7.0F));
/* 56 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-4.0F, 12.0F, -5.0F));
/* 57 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(4.0F, 12.0F, -5.0F));
/* 58 */     return mesh;
/*    */   }
/*    */   public ModelPart getHead() {
/* 61 */     return this.head;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/cow/CowModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */