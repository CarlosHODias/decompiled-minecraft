/*    */ package net.minecraft.client.model.monster.shulker;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.ShulkerRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class ShulkerModel
/*    */   extends EntityModel<ShulkerRenderState>
/*    */ {
/*    */   public static final String LID = "lid";
/*    */   private static final String BASE = "base";
/*    */   private final ModelPart lid;
/*    */   private final ModelPart head;
/*    */   
/*    */   public ShulkerModel(ModelPart root) {
/* 23 */     super(root, RenderTypes::entityCutoutNoCullZOffset);
/* 24 */     this.lid = root.getChild("lid");
/* 25 */     this.head = root.getChild("head");
/*    */   }
/*    */   
/*    */   private static MeshDefinition createShellMesh() {
/* 29 */     MeshDefinition mesh = new MeshDefinition();
/* 30 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 32 */     root.addOrReplaceChild("lid", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 12.0F, 16.0F), 
/* 35 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*    */     
/* 37 */     root.addOrReplaceChild("base", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F), 
/* 40 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*    */ 
/*    */     
/* 43 */     return mesh;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 47 */     MeshDefinition mesh = createShellMesh();
/*    */     
/* 49 */     mesh.getRoot().addOrReplaceChild("head", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(0, 52).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), 
/* 52 */         PartPose.offset(0.0F, 12.0F, 0.0F));
/*    */ 
/*    */     
/* 55 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBoxLayer() {
/* 59 */     MeshDefinition mesh = createShellMesh();
/* 60 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(ShulkerRenderState state) {
/* 65 */     super.setupAnim(state);
/*    */     
/* 67 */     float bs = (0.5F + state.peekAmount) * 3.1415927F;
/* 68 */     float q = -1.0F + Mth.sin(bs);
/* 69 */     float extra = 0.0F;
/* 70 */     if (bs > 3.1415927F) {
/* 71 */       extra = Mth.sin((state.ageInTicks * 0.1F)) * 0.7F;
/*    */     }
/* 73 */     this.lid.setPos(0.0F, 16.0F + Mth.sin(bs) * 8.0F + extra, 0.0F);
/*    */ 
/*    */     
/* 76 */     if (state.peekAmount > 0.3F) {
/* 77 */       this.lid.yRot = q * q * q * q * 3.1415927F * 0.125F;
/*    */     } else {
/* 79 */       this.lid.yRot = 0.0F;
/*    */     } 
/*    */     
/* 82 */     this.head.xRot = state.xRot * 0.017453292F;
/* 83 */     this.head.yRot = (state.yHeadRot - 180.0F - state.yBodyRot) * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/shulker/ShulkerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */