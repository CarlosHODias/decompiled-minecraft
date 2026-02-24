/*    */ package net.minecraft.client.model.object.skull;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class DragonHeadModel
/*    */   extends SkullModelBase
/*    */ {
/*    */   private final ModelPart head;
/*    */   private final ModelPart jaw;
/*    */   
/*    */   public DragonHeadModel(ModelPart root) {
/* 17 */     super(root);
/* 18 */     this.head = root.getChild("head");
/* 19 */     this.jaw = this.head.getChild("jaw");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createHeadLayer() {
/* 23 */     MeshDefinition mesh = new MeshDefinition();
/* 24 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 26 */     float zo = -16.0F;
/* 27 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .addBox("upper_lip", -6.0F, -1.0F, -24.0F, 12, 5, 16, 176, 44)
/* 30 */         .addBox("upper_head", -8.0F, -8.0F, -10.0F, 16, 16, 16, 112, 30)
/* 31 */         .mirror(true)
/* 32 */         .addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6, 0, 0)
/* 33 */         .addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4, 112, 0)
/* 34 */         .mirror(false)
/* 35 */         .addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6, 0, 0)
/* 36 */         .addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4, 112, 0), 
/* 37 */         PartPose.offset(0.0F, -7.986666F, 0.0F).scaled(0.75F));
/*    */ 
/*    */     
/* 40 */     head.addOrReplaceChild("jaw", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(176, 65).addBox("jaw", -6.0F, 0.0F, -16.0F, 12.0F, 4.0F, 16.0F), 
/* 43 */         PartPose.offset(0.0F, 4.0F, -8.0F));
/*    */ 
/*    */     
/* 46 */     return LayerDefinition.create(mesh, 256, 256);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SkullModelBase.State state) {
/* 51 */     super.setupAnim(state);
/* 52 */     this.jaw.xRot = (float)(Math.sin((state.animationPos * 3.1415927F * 0.2F)) + 1.0D) * 0.2F;
/*    */     
/* 54 */     this.head.yRot = state.yRot * 0.017453292F;
/* 55 */     this.head.xRot = state.xRot * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/skull/DragonHeadModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */