/*    */ package net.minecraft.client.model.animal.nautilus;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class NautilusSaddleModel
/*    */   extends NautilusModel
/*    */ {
/*    */   private final ModelPart nautilus;
/*    */   private final ModelPart shell;
/*    */   
/*    */   public NautilusSaddleModel(ModelPart root) {
/* 18 */     super(root);
/* 19 */     this.nautilus = root.getChild("root");
/* 20 */     this.shell = this.nautilus.getChild("shell");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSaddleLayer() {
/* 24 */     MeshDefinition meshdefinition = createBodyMesh();
/* 25 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*    */     
/* 27 */     PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
/*    */     
/* 29 */     nautilus.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -13.0F, 5.0F));
/*    */     
/* 31 */     return LayerDefinition.create(meshdefinition, 128, 128);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/nautilus/NautilusSaddleModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */