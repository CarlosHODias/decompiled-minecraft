/*    */ package net.minecraft.client.model.effects;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class SpinAttackEffectModel
/*    */   extends EntityModel<AvatarRenderState> {
/*    */   private static final int BOX_COUNT = 2;
/* 16 */   private final ModelPart[] boxes = new ModelPart[2];
/*    */   
/*    */   public SpinAttackEffectModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     for (int i = 0; i < 2; i++) {
/* 21 */       this.boxes[i] = root.getChild(boxName(i));
/*    */     }
/*    */   }
/*    */   
/*    */   private static String boxName(int i) {
/* 26 */     return "box" + i;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createLayer() {
/* 30 */     MeshDefinition mesh = new MeshDefinition();
/* 31 */     PartDefinition root = mesh.getRoot();
/* 32 */     for (int i = 0; i < 2; i++) {
/* 33 */       float yOffset = -3.2F + 9.6F * (i + 1);
/* 34 */       float scale = 0.75F * (i + 1);
/* 35 */       root.addOrReplaceChild(boxName(i), 
/* 36 */           CubeListBuilder.create()
/* 37 */           .texOffs(0, 0).addBox(-8.0F, -16.0F + yOffset, -8.0F, 16.0F, 32.0F, 16.0F), 
/* 38 */           PartPose.ZERO.withScale(scale));
/*    */     } 
/*    */ 
/*    */     
/* 42 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(AvatarRenderState state) {
/* 47 */     super.setupAnim(state);
/* 48 */     for (int i = 0; i < this.boxes.length; i++) {
/* 49 */       float angle = state.ageInTicks * -(45 + (i + 1) * 5);
/* 50 */       (this.boxes[i]).yRot = Mth.wrapDegrees(angle) * 0.017453292F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/effects/SpinAttackEffectModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */