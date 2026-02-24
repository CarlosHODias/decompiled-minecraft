/*    */ package net.minecraft.client.model.object.skull;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.monster.piglin.PiglinModel;
/*    */ 
/*    */ public class PiglinHeadModel
/*    */   extends SkullModelBase
/*    */ {
/*    */   private final ModelPart head;
/*    */   private final ModelPart leftEar;
/*    */   private final ModelPart rightEar;
/*    */   
/*    */   public PiglinHeadModel(ModelPart root) {
/* 16 */     super(root);
/* 17 */     this.head = root.getChild("head");
/* 18 */     this.leftEar = this.head.getChild("left_ear");
/* 19 */     this.rightEar = this.head.getChild("right_ear");
/*    */   }
/*    */   
/*    */   public static MeshDefinition createHeadModel() {
/* 23 */     MeshDefinition mesh = new MeshDefinition();
/* 24 */     PiglinModel.addHead(CubeDeformation.NONE, mesh);
/*    */     
/* 26 */     return mesh;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SkullModelBase.State state) {
/* 31 */     super.setupAnim(state);
/* 32 */     this.head.yRot = state.yRot * 0.017453292F;
/* 33 */     this.head.xRot = state.xRot * 0.017453292F;
/*    */     
/* 35 */     float asymmetry = 1.2F;
/* 36 */     this.leftEar.zRot = (float)-(Math.cos((state.animationPos * 3.1415927F * 0.2F * 1.2F)) + 2.5D) * 0.2F;
/* 37 */     this.rightEar.zRot = (float)(Math.cos((state.animationPos * 3.1415927F * 0.2F)) + 2.5D) * 0.2F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/skull/PiglinHeadModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */