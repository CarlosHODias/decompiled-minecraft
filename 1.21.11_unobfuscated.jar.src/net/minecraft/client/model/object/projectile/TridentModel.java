/*    */ package net.minecraft.client.model.object.projectile;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class TridentModel extends Model<Unit> {
/* 15 */   public static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/trident.png");
/*    */   
/*    */   public TridentModel(ModelPart root) {
/* 18 */     super(root, RenderTypes::entitySolid);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createLayer() {
/* 22 */     MeshDefinition mesh = new MeshDefinition();
/* 23 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 25 */     PartDefinition pole = root.addOrReplaceChild("pole", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 6).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 30 */     pole.addOrReplaceChild("base", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 35 */     pole.addOrReplaceChild("left_spike", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(4, 3).addBox(-2.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 40 */     pole.addOrReplaceChild("middle_spike", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(0, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 45 */     pole.addOrReplaceChild("right_spike", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(4, 3).mirror().addBox(1.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 51 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/projectile/TridentModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */