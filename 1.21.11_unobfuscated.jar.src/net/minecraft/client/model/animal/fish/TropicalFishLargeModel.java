/*    */ package net.minecraft.client.model.animal.fish;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.TropicalFishRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class TropicalFishLargeModel
/*    */   extends EntityModel<TropicalFishRenderState> {
/*    */   private final ModelPart tail;
/*    */   
/*    */   public TropicalFishLargeModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     this.tail = root.getChild("tail");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 24 */     MeshDefinition mesh = new MeshDefinition();
/* 25 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 27 */     int yo = 19;
/*    */     
/* 29 */     root.addOrReplaceChild("body", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 20).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, g), 
/* 32 */         PartPose.offset(0.0F, 19.0F, 0.0F));
/*    */     
/* 34 */     root.addOrReplaceChild("tail", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(21, 16).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, g), 
/* 37 */         PartPose.offset(0.0F, 19.0F, 3.0F));
/*    */     
/* 39 */     root.addOrReplaceChild("right_fin", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(2, 16).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, g), 
/* 42 */         PartPose.offsetAndRotation(-1.0F, 20.0F, 0.0F, 0.0F, 0.7853982F, 0.0F));
/*    */     
/* 44 */     root.addOrReplaceChild("left_fin", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(2, 12).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, g), 
/* 47 */         PartPose.offsetAndRotation(1.0F, 20.0F, 0.0F, 0.0F, -0.7853982F, 0.0F));
/*    */     
/* 49 */     root.addOrReplaceChild("top_fin", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(20, 11).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, g), 
/* 52 */         PartPose.offset(0.0F, 16.0F, -3.0F));
/*    */     
/* 54 */     root.addOrReplaceChild("bottom_fin", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(20, 21).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F, g), 
/* 57 */         PartPose.offset(0.0F, 22.0F, -3.0F));
/*    */ 
/*    */     
/* 60 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(TropicalFishRenderState state) {
/* 65 */     super.setupAnim(state);
/* 66 */     float amplitudeMultiplier = state.isInWater ? 1.0F : 1.5F;
/* 67 */     this.tail.yRot = -amplitudeMultiplier * 0.45F * Mth.sin((0.6F * state.ageInTicks));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/fish/TropicalFishLargeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */