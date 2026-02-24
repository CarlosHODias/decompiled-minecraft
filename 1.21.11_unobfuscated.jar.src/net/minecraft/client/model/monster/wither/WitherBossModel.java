/*    */ package net.minecraft.client.model.monster.wither;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.WitherRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WitherBossModel
/*    */   extends EntityModel<WitherRenderState>
/*    */ {
/*    */   private static final String RIBCAGE = "ribcage";
/*    */   private static final String CENTER_HEAD = "center_head";
/*    */   private static final String RIGHT_HEAD = "right_head";
/*    */   private static final String LEFT_HEAD = "left_head";
/*    */   private static final float RIBCAGE_X_ROT_OFFSET = 0.065F;
/*    */   private static final float TAIL_X_ROT_OFFSET = 0.265F;
/*    */   private final ModelPart centerHead;
/*    */   private final ModelPart rightHead;
/*    */   private final ModelPart leftHead;
/*    */   private final ModelPart ribcage;
/*    */   private final ModelPart tail;
/*    */   
/*    */   public WitherBossModel(ModelPart root) {
/* 32 */     super(root);
/* 33 */     this.ribcage = root.getChild("ribcage");
/* 34 */     this.tail = root.getChild("tail");
/*    */     
/* 36 */     this.centerHead = root.getChild("center_head");
/* 37 */     this.rightHead = root.getChild("right_head");
/* 38 */     this.leftHead = root.getChild("left_head");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 42 */     MeshDefinition mesh = new MeshDefinition();
/* 43 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 45 */     root.addOrReplaceChild("shoulders", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(0, 16).addBox(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F, g), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 51 */     float ribcageXRot = 0.20420352F;
/*    */     
/* 53 */     root.addOrReplaceChild("ribcage", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, g)
/* 56 */         .texOffs(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F, g)
/* 57 */         .texOffs(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11.0F, 2.0F, 2.0F, g)
/* 58 */         .texOffs(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11.0F, 2.0F, 2.0F, g), 
/* 59 */         PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F));
/*    */     
/* 61 */     root.addOrReplaceChild("tail", 
/* 62 */         CubeListBuilder.create()
/* 63 */         .texOffs(12, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, g), 
/* 64 */         PartPose.offsetAndRotation(-2.0F, 6.9F + Mth.cos(0.2042035162448883D) * 10.0F, -0.5F + Mth.sin(0.2042035162448883D) * 10.0F, 0.83252203F, 0.0F, 0.0F));
/*    */ 
/*    */     
/* 67 */     root.addOrReplaceChild("center_head", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, g), PartPose.ZERO);
/*    */ 
/*    */     
/* 72 */     CubeListBuilder sideHead = CubeListBuilder.create()
/* 73 */       .texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, g);
/* 74 */     root.addOrReplaceChild("right_head", sideHead, PartPose.offset(-8.0F, 4.0F, 0.0F));
/* 75 */     root.addOrReplaceChild("left_head", sideHead, PartPose.offset(10.0F, 4.0F, 0.0F));
/*    */     
/* 77 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(WitherRenderState state) {
/* 82 */     super.setupAnim(state);
/*    */     
/* 84 */     setupHeadRotation(state, this.rightHead, 0);
/* 85 */     setupHeadRotation(state, this.leftHead, 1);
/*    */     
/* 87 */     float anim = Mth.cos((state.ageInTicks * 0.1F));
/* 88 */     this.ribcage.xRot = (0.065F + 0.05F * anim) * 3.1415927F;
/*    */     
/* 90 */     this.tail.setPos(-2.0F, 6.9F + Mth.cos(this.ribcage.xRot) * 10.0F, -0.5F + Mth.sin(this.ribcage.xRot) * 10.0F);
/* 91 */     this.tail.xRot = (0.265F + 0.1F * anim) * 3.1415927F;
/*    */     
/* 93 */     this.centerHead.yRot = state.yRot * 0.017453292F;
/* 94 */     this.centerHead.xRot = state.xRot * 0.017453292F;
/*    */   }
/*    */   
/*    */   private static void setupHeadRotation(WitherRenderState state, ModelPart head, int headIndex) {
/* 98 */     head.yRot = (state.yHeadRots[headIndex] - state.bodyRot) * 0.017453292F;
/* 99 */     head.xRot = state.xHeadRots[headIndex] * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/wither/WitherBossModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */