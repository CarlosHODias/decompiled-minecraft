/*    */ package net.minecraft.client.model.monster.phantom;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.PhantomRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ public class PhantomModel
/*    */   extends EntityModel<PhantomRenderState>
/*    */ {
/*    */   private static final String TAIL_BASE = "tail_base";
/*    */   private static final String TAIL_TIP = "tail_tip";
/*    */   private final ModelPart leftWingBase;
/*    */   private final ModelPart leftWingTip;
/*    */   private final ModelPart rightWingBase;
/*    */   private final ModelPart rightWingTip;
/*    */   private final ModelPart tailBase;
/*    */   private final ModelPart tailTip;
/*    */   
/*    */   public PhantomModel(ModelPart root) {
/* 27 */     super(root);
/* 28 */     ModelPart body = root.getChild("body");
/* 29 */     this.tailBase = body.getChild("tail_base");
/* 30 */     this.tailTip = this.tailBase.getChild("tail_tip");
/* 31 */     this.leftWingBase = body.getChild("left_wing_base");
/* 32 */     this.leftWingTip = this.leftWingBase.getChild("left_wing_tip");
/* 33 */     this.rightWingBase = body.getChild("right_wing_base");
/* 34 */     this.rightWingTip = this.rightWingBase.getChild("right_wing_tip");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 38 */     MeshDefinition mesh = new MeshDefinition();
/* 39 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 41 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(0, 8).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 9.0F), 
/* 44 */         PartPose.rotation(-0.1F, 0.0F, 0.0F));
/*    */     
/* 46 */     PartDefinition tailBase = body.addOrReplaceChild("tail_base", 
/* 47 */         CubeListBuilder.create()
/* 48 */         .texOffs(3, 20).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 2.0F, 6.0F), 
/* 49 */         PartPose.offset(0.0F, -2.0F, 1.0F));
/*    */     
/* 51 */     tailBase.addOrReplaceChild("tail_tip", 
/* 52 */         CubeListBuilder.create()
/* 53 */         .texOffs(4, 29).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F), 
/* 54 */         PartPose.offset(0.0F, 0.5F, 6.0F));
/*    */     
/* 56 */     PartDefinition leftWingBase = body.addOrReplaceChild("left_wing_base", 
/* 57 */         CubeListBuilder.create()
/* 58 */         .texOffs(23, 12).addBox(0.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F), 
/* 59 */         PartPose.offsetAndRotation(2.0F, -2.0F, -8.0F, 0.0F, 0.0F, 0.1F));
/*    */     
/* 61 */     leftWingBase.addOrReplaceChild("left_wing_tip", 
/* 62 */         CubeListBuilder.create()
/* 63 */         .texOffs(16, 24).addBox(0.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F), 
/* 64 */         PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F));
/*    */     
/* 66 */     PartDefinition rightWingBase = body.addOrReplaceChild("right_wing_base", 
/* 67 */         CubeListBuilder.create()
/* 68 */         .texOffs(23, 12).mirror().addBox(-6.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F), 
/* 69 */         PartPose.offsetAndRotation(-3.0F, -2.0F, -8.0F, 0.0F, 0.0F, -0.1F));
/*    */     
/* 71 */     rightWingBase.addOrReplaceChild("right_wing_tip", 
/* 72 */         CubeListBuilder.create()
/* 73 */         .texOffs(16, 24).mirror().addBox(-13.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F), 
/* 74 */         PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1F));
/*    */     
/* 76 */     body.addOrReplaceChild("head", 
/* 77 */         CubeListBuilder.create()
/* 78 */         .texOffs(0, 0).addBox(-4.0F, -2.0F, -5.0F, 7.0F, 3.0F, 5.0F), 
/* 79 */         PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.2F, 0.0F, 0.0F));
/*    */ 
/*    */     
/* 82 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(PhantomRenderState state) {
/* 87 */     super.setupAnim(state);
/*    */     
/* 89 */     float anim = state.flapTime * 7.448451F * 0.017453292F;
/* 90 */     float flapAmount = 16.0F;
/* 91 */     this.leftWingBase.zRot = Mth.cos(anim) * 16.0F * 0.017453292F;
/* 92 */     this.leftWingTip.zRot = Mth.cos(anim) * 16.0F * 0.017453292F;
/* 93 */     this.rightWingBase.zRot = -this.leftWingBase.zRot;
/* 94 */     this.rightWingTip.zRot = -this.leftWingTip.zRot;
/*    */     
/* 96 */     this.tailBase.xRot = -(5.0F + Mth.cos((anim * 2.0F)) * 5.0F) * 0.017453292F;
/* 97 */     this.tailTip.xRot = -(5.0F + Mth.cos((anim * 2.0F)) * 5.0F) * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/phantom/PhantomModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */