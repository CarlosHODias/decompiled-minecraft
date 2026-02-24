/*    */ package net.minecraft.client.model.monster.slime;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ 
/*    */ public class MagmaCubeModel
/*    */   extends EntityModel<SlimeRenderState> {
/*    */   private static final int SEGMENT_COUNT = 8;
/* 16 */   private final ModelPart[] bodyCubes = new ModelPart[8];
/*    */   
/*    */   public MagmaCubeModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     Arrays.setAll(this.bodyCubes, i -> root.getChild(getSegmentName(i)));
/*    */   }
/*    */   
/*    */   private static String getSegmentName(int i) {
/* 24 */     return "cube" + i;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 28 */     MeshDefinition mesh = new MeshDefinition();
/* 29 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 31 */     for (int i = 0; i < 8; i++) {
/* 32 */       int u = 0;
/* 33 */       int v = 0;
/* 34 */       if (i > 0 && i < 4) {
/* 35 */         v += 9 * i;
/* 36 */       } else if (i > 3) {
/* 37 */         u = 32;
/* 38 */         v += 9 * i - 36;
/*    */       } 
/* 40 */       root.addOrReplaceChild(getSegmentName(i), 
/* 41 */           CubeListBuilder.create()
/* 42 */           .texOffs(u, v).addBox(-4.0F, (16 + i), -4.0F, 8.0F, 1.0F, 8.0F), PartPose.ZERO);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 47 */     root.addOrReplaceChild("inside_cube", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(24, 40).addBox(-2.0F, 18.0F, -2.0F, 4.0F, 4.0F, 4.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 52 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SlimeRenderState state) {
/* 57 */     super.setupAnim(state);
/* 58 */     float slimeSquish = Math.max(0.0F, state.squish);
/* 59 */     for (int i = 0; i < this.bodyCubes.length; i++)
/* 60 */       (this.bodyCubes[i]).y = -(4 - i) * slimeSquish * 1.7F; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/slime/MagmaCubeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */