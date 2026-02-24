/*    */ package net.minecraft.client.model.object.projectile;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ 
/*    */ public class WindChargeModel
/*    */   extends EntityModel<EntityRenderState>
/*    */ {
/*    */   private static final int ROTATION_SPEED = 16;
/*    */   private final ModelPart bone;
/*    */   private final ModelPart windCharge;
/*    */   private final ModelPart wind;
/*    */   
/*    */   public WindChargeModel(ModelPart root) {
/* 24 */     super(root, RenderTypes::entityTranslucent);
/* 25 */     this.bone = root.getChild("bone");
/* 26 */     this.wind = this.bone.getChild("wind");
/* 27 */     this.windCharge = this.bone.getChild("wind_charge");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 31 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 32 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*    */     
/* 34 */     PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 36 */     bone.addOrReplaceChild("wind", CubeListBuilder.create()
/* 37 */         .texOffs(15, 20).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
/* 38 */         .texOffs(0, 9).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
/*    */     
/* 40 */     bone.addOrReplaceChild("wind_charge", CubeListBuilder.create()
/* 41 */         .texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
/*    */     
/* 43 */     return LayerDefinition.create(meshdefinition, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(EntityRenderState state) {
/* 48 */     super.setupAnim(state);
/* 49 */     this.windCharge.yRot = -state.ageInTicks * 16.0F * 0.017453292F;
/* 50 */     this.wind.yRot = state.ageInTicks * 16.0F * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/projectile/WindChargeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */