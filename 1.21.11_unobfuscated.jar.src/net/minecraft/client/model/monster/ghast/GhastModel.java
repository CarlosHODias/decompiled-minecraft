/*    */ package net.minecraft.client.model.monster.ghast;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartNames;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.GhastRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class GhastModel extends EntityModel<GhastRenderState> {
/* 18 */   private final ModelPart[] tentacles = new ModelPart[9];
/*    */   
/*    */   public GhastModel(ModelPart root) {
/* 21 */     super(root);
/*    */     
/* 23 */     for (int i = 0; i < this.tentacles.length; i++) {
/* 24 */       this.tentacles[i] = root.getChild(PartNames.tentacle(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 29 */     MeshDefinition mesh = new MeshDefinition();
/* 30 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 32 */     root.addOrReplaceChild("body", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), 
/* 35 */         PartPose.offset(0.0F, 17.6F, 0.0F));
/*    */ 
/*    */     
/* 38 */     RandomSource random = RandomSource.create(1660L);
/* 39 */     for (int i = 0; i < 9; i++) {
/* 40 */       float xo = (((i % 3) - (i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
/* 41 */       float yo = ((i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
/* 42 */       int len = random.nextInt(7) + 8;
/* 43 */       root.addOrReplaceChild(PartNames.tentacle(i), 
/* 44 */           CubeListBuilder.create()
/* 45 */           .texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, len, 2.0F), 
/* 46 */           PartPose.offset(xo, 24.6F, yo));
/*    */     } 
/*    */ 
/*    */     
/* 50 */     return LayerDefinition.create(mesh, 64, 32).apply(MeshTransformer.scaling(4.5F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(GhastRenderState state) {
/* 55 */     super.setupAnim(state);
/* 56 */     animateTentacles((EntityRenderState)state, this.tentacles);
/*    */   }
/*    */   
/*    */   public static void animateTentacles(EntityRenderState state, ModelPart[] tentacles) {
/* 60 */     for (int i = 0; i < tentacles.length; i++)
/* 61 */       (tentacles[i]).xRot = 0.2F * Mth.sin((state.ageInTicks * 0.3F + i)) + 0.4F; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/ghast/GhastModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */