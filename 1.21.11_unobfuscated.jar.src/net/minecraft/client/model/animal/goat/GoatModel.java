/*    */ package net.minecraft.client.model.animal.goat;
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
/*    */ import net.minecraft.client.renderer.entity.state.GoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ 
/*    */ public class GoatModel
/*    */   extends QuadrupedModel<GoatRenderState> {
/* 18 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(true, 19.0F, 1.0F, 2.5F, 2.0F, 24.0F, Set.of("head"));
/*    */   
/*    */   public GoatModel(ModelPart root) {
/* 21 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 25 */     MeshDefinition mesh = new MeshDefinition();
/* 26 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 28 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 29 */         CubeListBuilder.create()
/* 30 */         .texOffs(2, 61).addBox("right ear", -6.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F)
/* 31 */         .texOffs(2, 61).mirror().addBox("left ear", 2.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F)
/* 32 */         .texOffs(23, 52).addBox("goatee", -0.5F, -3.0F, -14.0F, 0.0F, 7.0F, 5.0F), 
/* 33 */         PartPose.offset(1.0F, 14.0F, 0.0F));
/*    */     
/* 35 */     head.addOrReplaceChild("left_horn", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(12, 55).addBox(-0.01F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F), 
/* 38 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 40 */     head.addOrReplaceChild("right_horn", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(12, 55).addBox(-2.99F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F), 
/* 43 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 45 */     head.addOrReplaceChild("nose", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(34, 46).addBox(-3.0F, -4.0F, -8.0F, 5.0F, 7.0F, 10.0F), 
/* 48 */         PartPose.offsetAndRotation(0.0F, -8.0F, -8.0F, 0.9599F, 0.0F, 0.0F));
/*    */     
/* 50 */     root.addOrReplaceChild("body", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(1, 1).addBox(-4.0F, -17.0F, -7.0F, 9.0F, 11.0F, 16.0F)
/* 53 */         .texOffs(0, 28).addBox(-5.0F, -18.0F, -8.0F, 11.0F, 14.0F, 11.0F), 
/* 54 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*    */ 
/*    */     
/* 57 */     root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(36, 29).addBox(0.0F, 4.0F, 0.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(1.0F, 14.0F, 4.0F));
/* 58 */     root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(49, 29).addBox(0.0F, 4.0F, 0.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(-3.0F, 14.0F, 4.0F));
/* 59 */     root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(49, 2).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F), PartPose.offset(1.0F, 14.0F, -6.0F));
/* 60 */     root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(35, 2).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F), PartPose.offset(-3.0F, 14.0F, -6.0F));
/*    */     
/* 62 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(GoatRenderState state) {
/* 67 */     super.setupAnim((LivingEntityRenderState)state);
/*    */     
/* 69 */     (this.head.getChild("left_horn")).visible = state.hasLeftHorn;
/* 70 */     (this.head.getChild("right_horn")).visible = state.hasRightHorn;
/*    */     
/* 72 */     if (state.rammingXHeadRot != 0.0F)
/* 73 */       this.head.xRot = state.rammingXHeadRot; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/goat/GoatModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */