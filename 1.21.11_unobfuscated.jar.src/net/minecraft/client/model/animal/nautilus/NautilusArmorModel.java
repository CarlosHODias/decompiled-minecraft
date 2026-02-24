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
/*    */ public class NautilusArmorModel
/*    */   extends NautilusModel {
/*    */   private final ModelPart nautilus;
/*    */   private final ModelPart shell;
/*    */   
/*    */   public NautilusArmorModel(ModelPart root) {
/* 17 */     super(root);
/* 18 */     this.nautilus = root.getChild("root");
/* 19 */     this.shell = this.nautilus.getChild("shell");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 23 */     MeshDefinition meshdefinition = createBodyMesh();
/* 24 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*    */     
/* 26 */     PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
/*    */     
/* 28 */     PartDefinition shell = nautilus.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.01F))
/* 29 */         .texOffs(0, 26).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 20.0F, new CubeDeformation(0.01F))
/* 30 */         .texOffs(48, 26).addBox(-7.0F, 0.0F, 6.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 5.0F));
/*    */     
/* 32 */     return LayerDefinition.create(meshdefinition, 128, 128);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/nautilus/NautilusArmorModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */