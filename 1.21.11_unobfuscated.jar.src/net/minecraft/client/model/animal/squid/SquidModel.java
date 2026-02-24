/*    */ package net.minecraft.client.model.animal.squid;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.SquidRenderState;
/*    */ 
/*    */ public class SquidModel
/*    */   extends EntityModel<SquidRenderState>
/*    */ {
/* 18 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/* 19 */   private final ModelPart[] tentacles = new ModelPart[8];
/*    */   
/*    */   public SquidModel(ModelPart root) {
/* 22 */     super(root);
/* 23 */     Arrays.setAll(this.tentacles, i -> root.getChild(createTentacleName(i)));
/*    */   }
/*    */   
/*    */   private static String createTentacleName(int i) {
/* 27 */     return "tentacle" + i;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 31 */     MeshDefinition mesh = new MeshDefinition();
/* 32 */     PartDefinition root = mesh.getRoot();
/* 33 */     CubeDeformation g = new CubeDeformation(0.02F);
/*    */     
/* 35 */     int yoffs = -16;
/* 36 */     root.addOrReplaceChild("body", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 0).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 16.0F, 12.0F, g), 
/* 39 */         PartPose.offset(0.0F, 8.0F, 0.0F));
/*    */ 
/*    */     
/* 42 */     int tentacleCount = 8;
/* 43 */     CubeListBuilder tentacle = CubeListBuilder.create()
/* 44 */       .texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F);
/*    */     
/* 46 */     for (int i = 0; i < 8; i++) {
/* 47 */       double angle = i * Math.PI * 2.0D / 8.0D;
/* 48 */       float x = (float)Math.cos(angle) * 5.0F;
/* 49 */       float y = 15.0F;
/* 50 */       float z = (float)Math.sin(angle) * 5.0F;
/*    */       
/* 52 */       angle = i * Math.PI * -2.0D / 8.0D + 1.5707963267948966D;
/* 53 */       float yRot = (float)angle;
/*    */       
/* 55 */       root.addOrReplaceChild(createTentacleName(i), tentacle, PartPose.offsetAndRotation(x, 15.0F, z, 0.0F, yRot, 0.0F));
/*    */     } 
/*    */     
/* 58 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SquidRenderState state) {
/* 63 */     super.setupAnim(state);
/* 64 */     for (ModelPart tentacle : this.tentacles)
/* 65 */       tentacle.xRot = state.tentacleAngle; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/squid/SquidModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */