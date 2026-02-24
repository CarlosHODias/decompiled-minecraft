/*    */ package net.minecraft.client.model.animal.golem;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class SnowGolemModel
/*    */   extends EntityModel<LivingEntityRenderState>
/*    */ {
/*    */   private static final String UPPER_BODY = "upper_body";
/*    */   private final ModelPart upperBody;
/*    */   private final ModelPart head;
/*    */   private final ModelPart leftArm;
/*    */   private final ModelPart rightArm;
/*    */   
/*    */   public SnowGolemModel(ModelPart root) {
/* 24 */     super(root);
/* 25 */     this.head = root.getChild("head");
/* 26 */     this.leftArm = root.getChild("left_arm");
/* 27 */     this.rightArm = root.getChild("right_arm");
/* 28 */     this.upperBody = root.getChild("upper_body");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 32 */     MeshDefinition mesh = new MeshDefinition();
/* 33 */     PartDefinition root = mesh.getRoot();
/* 34 */     float yOffset = 4.0F;
/*    */     
/* 36 */     CubeDeformation deformation = new CubeDeformation(-0.5F);
/*    */     
/* 38 */     root.addOrReplaceChild("head", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation), 
/* 41 */         PartPose.offset(0.0F, 4.0F, 0.0F));
/*    */     
/* 43 */     CubeListBuilder arm = CubeListBuilder.create()
/* 44 */       .texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 12.0F, 2.0F, 2.0F, deformation);
/* 45 */     root.addOrReplaceChild("left_arm", arm, PartPose.offsetAndRotation(5.0F, 6.0F, 1.0F, 0.0F, 0.0F, 1.0F));
/* 46 */     root.addOrReplaceChild("right_arm", arm, PartPose.offsetAndRotation(-5.0F, 6.0F, -1.0F, 0.0F, 3.1415927F, -1.0F));
/*    */     
/* 48 */     root.addOrReplaceChild("upper_body", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(0, 16).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, deformation), 
/* 51 */         PartPose.offset(0.0F, 13.0F, 0.0F));
/*    */     
/* 53 */     root.addOrReplaceChild("lower_body", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(0, 36).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, deformation), 
/* 56 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*    */ 
/*    */     
/* 59 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(LivingEntityRenderState state) {
/* 64 */     super.setupAnim(state);
/*    */     
/* 66 */     this.head.yRot = state.yRot * 0.017453292F;
/* 67 */     this.head.xRot = state.xRot * 0.017453292F;
/* 68 */     this.upperBody.yRot = state.yRot * 0.017453292F * 0.25F;
/*    */     
/* 70 */     float sin = Mth.sin(this.upperBody.yRot);
/* 71 */     float cos = Mth.cos(this.upperBody.yRot);
/*    */     
/* 73 */     this.leftArm.yRot = this.upperBody.yRot;
/* 74 */     this.upperBody.yRot += 3.1415927F;
/*    */     
/* 76 */     this.leftArm.x = cos * 5.0F;
/* 77 */     this.leftArm.z = -sin * 5.0F;
/*    */     
/* 79 */     this.rightArm.x = -cos * 5.0F;
/* 80 */     this.rightArm.z = sin * 5.0F;
/*    */   }
/*    */   
/*    */   public ModelPart getHead() {
/* 84 */     return this.head;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/golem/SnowGolemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */