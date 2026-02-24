/*    */ package net.minecraft.client.model.animal.camel;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*    */ 
/*    */ public class CamelSaddleModel
/*    */   extends CamelModel
/*    */ {
/*    */   private static final String SADDLE = "saddle";
/*    */   private static final String BRIDLE = "bridle";
/*    */   private static final String REINS = "reins";
/*    */   private final ModelPart reins;
/*    */   
/*    */   public CamelSaddleModel(ModelPart root) {
/* 21 */     super(root);
/* 22 */     this.reins = this.head.getChild("reins");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSaddleLayer() {
/* 26 */     MeshDefinition mesh = createBodyMesh();
/*    */     
/* 28 */     PartDefinition root = mesh.getRoot();
/* 29 */     PartDefinition body = root.getChild("body");
/* 30 */     PartDefinition head = body.getChild("head");
/*    */     
/* 32 */     CubeDeformation inflate = new CubeDeformation(0.05F);
/*    */     
/* 34 */     body.addOrReplaceChild("saddle", CubeListBuilder.create()
/* 35 */         .texOffs(74, 64).addBox(-4.5F, -17.0F, -15.5F, 9.0F, 5.0F, 11.0F, inflate)
/* 36 */         .texOffs(92, 114).addBox(-3.5F, -20.0F, -15.5F, 7.0F, 3.0F, 11.0F, inflate)
/* 37 */         .texOffs(0, 89).addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F, inflate), 
/* 38 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 40 */     head.addOrReplaceChild("reins", CubeListBuilder.create()
/* 41 */         .texOffs(98, 42).addBox(3.51F, -18.0F, -17.0F, 0.0F, 7.0F, 15.0F)
/* 42 */         .texOffs(84, 57).addBox(-3.5F, -18.0F, -2.0F, 7.0F, 7.0F, 0.0F)
/* 43 */         .texOffs(98, 42).addBox(-3.51F, -18.0F, -17.0F, 0.0F, 7.0F, 15.0F), 
/* 44 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 46 */     head.addOrReplaceChild("bridle", CubeListBuilder.create()
/* 47 */         .texOffs(60, 87).addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F, inflate)
/* 48 */         .texOffs(21, 64).addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F, inflate)
/* 49 */         .texOffs(50, 64).addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F, inflate)
/* 50 */         .texOffs(74, 70).addBox(2.5F, -19.0F, -18.0F, 1.0F, 2.0F, 2.0F)
/* 51 */         .texOffs(74, 70).mirror().addBox(-3.5F, -19.0F, -18.0F, 1.0F, 2.0F, 2.0F), 
/* 52 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 54 */     return LayerDefinition.create(mesh, 128, 128);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(CamelRenderState state) {
/* 59 */     super.setupAnim(state);
/* 60 */     this.reins.visible = state.isRidden;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/camel/CamelSaddleModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */