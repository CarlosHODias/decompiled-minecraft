/*    */ package net.minecraft.client.model.monster.nautilus;
/*    */ 
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.NautilusRenderState;
/*    */ 
/*    */ public class ZombieNautilusCoralModel
/*    */   extends NautilusModel
/*    */ {
/*    */   private final ModelPart corals;
/*    */   
/*    */   public ZombieNautilusCoralModel(ModelPart root) {
/* 18 */     super(root);
/* 19 */     ModelPart shell = this.nautilus.getChild("shell");
/* 20 */     this.corals = shell.getChild("corals");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 24 */     MeshDefinition mesh = createBodyMesh();
/*    */     
/* 26 */     PartDefinition corals = mesh.getRoot().getChild("root").getChild("shell").addOrReplaceChild("corals", CubeListBuilder.create(), PartPose.offset(8.0F, 4.5F, -8.0F));
/*    */     
/* 28 */     PartDefinition yellowCoral = corals.addOrReplaceChild("yellow_coral", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 11.0F));
/* 29 */     yellowCoral.addOrReplaceChild("yellow_coral_second", CubeListBuilder.create().texOffs(0, 85).addBox(-4.5F, -3.5F, 0.0F, 6.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, -0.7854F, 0.0F));
/* 30 */     yellowCoral.addOrReplaceChild("yellow_coral_first", CubeListBuilder.create().texOffs(0, 85).addBox(-4.5F, -3.5F, 0.0F, 6.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
/*    */     
/* 32 */     PartDefinition pinkCoral = corals.addOrReplaceChild("pink_coral", CubeListBuilder.create().texOffs(-8, 94).addBox(-4.5F, 4.5F, 0.0F, 6.0F, 0.0F, 8.0F), PartPose.offset(-12.5F, -18.0F, 11.0F));
/* 33 */     pinkCoral.addOrReplaceChild("pink_coral_second", CubeListBuilder.create().texOffs(-8, 94).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F), PartPose.offsetAndRotation(-1.5F, 4.5F, 4.0F, 0.0F, 0.0F, 1.5708F));
/*    */     
/* 35 */     PartDefinition blueCoral = corals.addOrReplaceChild("blue_coral", CubeListBuilder.create(), PartPose.offset(-14.0F, 0.0F, 5.5F));
/* 36 */     blueCoral.addOrReplaceChild("blue_second", CubeListBuilder.create().texOffs(0, 102).addBox(-3.5F, -5.5F, 0.0F, 5.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 0.7854F, 0.0F));
/* 37 */     blueCoral.addOrReplaceChild("blue_first", CubeListBuilder.create().texOffs(0, 102).addBox(-3.5F, -5.5F, 0.0F, 5.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
/*    */     
/* 39 */     PartDefinition redCoral = corals.addOrReplaceChild("red_coral", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
/* 40 */     redCoral.addOrReplaceChild("red_coral_second", CubeListBuilder.create().texOffs(0, 112).addBox(-2.5F, -5.5F, 0.0F, 4.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(-0.5F, -1.0F, 1.5F, 0.0F, -0.829F, 0.0F));
/* 41 */     redCoral.addOrReplaceChild("red_coral_first", CubeListBuilder.create().texOffs(0, 112).addBox(-4.5F, -5.5F, 0.0F, 6.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
/*    */     
/* 43 */     return LayerDefinition.create(mesh, 128, 128);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(NautilusRenderState state) {
/* 48 */     super.setupAnim(state);
/* 49 */     this.corals.visible = state.bodyArmorItem.isEmpty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/nautilus/ZombieNautilusCoralModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */