/*    */ package net.minecraft.client.model.animal.polarbear;
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
/*    */ import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
/*    */ 
/*    */ public class PolarBearModel
/*    */   extends QuadrupedModel<PolarBearRenderState>
/*    */ {
/*    */   private static final float BABY_HEAD_SCALE = 2.25F;
/* 20 */   private static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 16.0F, 4.0F, 2.25F, 2.0F, 24.0F, Set.of("head"));
/*    */   
/*    */   public PolarBearModel(ModelPart root) {
/* 23 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(boolean baby) {
/* 27 */     MeshDefinition mesh = new MeshDefinition();
/* 28 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 30 */     root.addOrReplaceChild("head", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 0).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 7.0F, 7.0F)
/* 33 */         .texOffs(0, 44).addBox("mouth", -2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F)
/* 34 */         .texOffs(26, 0).addBox("right_ear", -4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F)
/* 35 */         .texOffs(26, 0).mirror().addBox("left_ear", 2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F), 
/* 36 */         PartPose.offset(0.0F, 10.0F, -16.0F));
/*    */     
/* 38 */     root.addOrReplaceChild("body", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F)
/* 41 */         .texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F), 
/* 42 */         PartPose.offsetAndRotation(-2.0F, 9.0F, 12.0F, 1.5707964F, 0.0F, 0.0F));
/*    */ 
/*    */     
/* 45 */     int legSize = 10;
/* 46 */     CubeListBuilder hindLeg = CubeListBuilder.create()
/* 47 */       .texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F);
/* 48 */     root.addOrReplaceChild("right_hind_leg", hindLeg, PartPose.offset(-4.5F, 14.0F, 6.0F));
/* 49 */     root.addOrReplaceChild("left_hind_leg", hindLeg, PartPose.offset(4.5F, 14.0F, 6.0F));
/*    */     
/* 51 */     CubeListBuilder frontLeg = CubeListBuilder.create()
/* 52 */       .texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F);
/* 53 */     root.addOrReplaceChild("right_front_leg", frontLeg, PartPose.offset(-3.5F, 14.0F, -8.0F));
/* 54 */     root.addOrReplaceChild("left_front_leg", frontLeg, PartPose.offset(3.5F, 14.0F, -8.0F));
/*    */     
/* 56 */     return LayerDefinition.create(mesh, 128, 64)
/* 57 */       .apply(baby ? BABY_TRANSFORMER : MeshTransformer.IDENTITY)
/* 58 */       .apply(MeshTransformer.scaling(1.2F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(PolarBearRenderState state) {
/* 63 */     super.setupAnim((LivingEntityRenderState)state);
/*    */     
/* 65 */     float standScale = state.standScale * state.standScale;
/* 66 */     float bodyAgeScale = state.ageScale;
/* 67 */     float headAgeScale = state.isBaby ? 0.44444445F : 1.0F;
/*    */     
/* 69 */     this.body.xRot -= standScale * 3.1415927F * 0.35F;
/* 70 */     this.body.y += standScale * bodyAgeScale * 2.0F;
/*    */     
/* 72 */     this.rightFrontLeg.y -= standScale * bodyAgeScale * 20.0F;
/* 73 */     this.rightFrontLeg.z += standScale * bodyAgeScale * 4.0F;
/* 74 */     this.rightFrontLeg.xRot -= standScale * 3.1415927F * 0.45F;
/*    */     
/* 76 */     this.leftFrontLeg.y = this.rightFrontLeg.y;
/* 77 */     this.leftFrontLeg.z = this.rightFrontLeg.z;
/* 78 */     this.leftFrontLeg.xRot -= standScale * 3.1415927F * 0.45F;
/*    */     
/* 80 */     this.head.y -= standScale * headAgeScale * 24.0F;
/* 81 */     this.head.z += standScale * headAgeScale * 13.0F;
/* 82 */     this.head.xRot += standScale * 3.1415927F * 0.15F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/polarbear/PolarBearModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */