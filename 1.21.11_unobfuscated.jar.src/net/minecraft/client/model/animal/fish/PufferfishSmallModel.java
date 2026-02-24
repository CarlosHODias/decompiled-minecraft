/*    */ package net.minecraft.client.model.animal.fish;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class PufferfishSmallModel
/*    */   extends EntityModel<EntityRenderState> {
/*    */   private final ModelPart leftFin;
/*    */   private final ModelPart rightFin;
/*    */   
/*    */   public PufferfishSmallModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     this.leftFin = root.getChild("left_fin");
/* 21 */     this.rightFin = root.getChild("right_fin");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 25 */     MeshDefinition mesh = new MeshDefinition();
/* 26 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 28 */     int yo = 23;
/* 29 */     root.addOrReplaceChild("body", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 27).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/* 32 */         PartPose.offset(0.0F, 23.0F, 0.0F));
/*    */     
/* 34 */     root.addOrReplaceChild("right_eye", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(24, 6).addBox(-1.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F), 
/* 37 */         PartPose.offset(0.0F, 20.0F, 0.0F));
/*    */     
/* 39 */     root.addOrReplaceChild("left_eye", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(28, 6).addBox(0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F), 
/* 42 */         PartPose.offset(0.0F, 20.0F, 0.0F));
/*    */     
/* 44 */     root.addOrReplaceChild("back_fin", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(-3, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F), 
/* 47 */         PartPose.offset(0.0F, 22.0F, 1.5F));
/*    */     
/* 49 */     root.addOrReplaceChild("right_fin", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(25, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 2.0F), 
/* 52 */         PartPose.offset(-1.5F, 22.0F, -1.5F));
/*    */     
/* 54 */     root.addOrReplaceChild("left_fin", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(25, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 2.0F), 
/* 57 */         PartPose.offset(1.5F, 22.0F, -1.5F));
/*    */ 
/*    */     
/* 60 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(EntityRenderState state) {
/* 65 */     super.setupAnim(state);
/* 66 */     this.rightFin.zRot = -0.2F + 0.4F * Mth.sin((state.ageInTicks * 0.2F));
/* 67 */     this.leftFin.zRot = 0.2F - 0.4F * Mth.sin((state.ageInTicks * 0.2F));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/fish/PufferfishSmallModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */