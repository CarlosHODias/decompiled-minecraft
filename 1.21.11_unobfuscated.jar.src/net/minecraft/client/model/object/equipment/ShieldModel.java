/*    */ package net.minecraft.client.model.object.equipment;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class ShieldModel
/*    */   extends Model<Unit> {
/*    */   private static final String PLATE = "plate";
/*    */   private static final String HANDLE = "handle";
/*    */   private static final int SHIELD_WIDTH = 10;
/*    */   private static final int SHIELD_HEIGHT = 20;
/*    */   private final ModelPart plate;
/*    */   private final ModelPart handle;
/*    */   
/*    */   public ShieldModel(ModelPart root) {
/* 23 */     super(root, RenderTypes::entitySolid);
/* 24 */     this.plate = root.getChild("plate");
/* 25 */     this.handle = root.getChild("handle");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createLayer() {
/* 29 */     MeshDefinition mesh = new MeshDefinition();
/* 30 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 32 */     root.addOrReplaceChild("plate", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 37 */     root.addOrReplaceChild("handle", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 43 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public ModelPart plate() {
/* 47 */     return this.plate;
/*    */   }
/*    */   
/*    */   public ModelPart handle() {
/* 51 */     return this.handle;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/equipment/ShieldModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */