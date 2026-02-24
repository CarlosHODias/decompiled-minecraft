/*    */ package net.minecraft.client.model.animal.nautilus;
/*    */ 
/*    */ import net.minecraft.client.animation.KeyframeAnimation;
/*    */ import net.minecraft.client.animation.definitions.NautilusAnimation;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.NautilusRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NautilusModel
/*    */   extends EntityModel<NautilusRenderState>
/*    */ {
/*    */   private static final float SWIM_ANIMATION_SPEED_MAX = 2.0F;
/*    */   private static final float SWIM_ANIMATION_SCALE_FACTOR = 3.0F;
/*    */   private static final float IDLE_SWIM_ANIMATION_SPEED = 0.2F;
/*    */   private static final float IDLE_SWIM_ANIMATION_SCALE = 5.0F;
/*    */   protected final ModelPart body;
/*    */   protected final ModelPart nautilus;
/*    */   private final KeyframeAnimation swimAnimation;
/*    */   
/*    */   public NautilusModel(ModelPart root) {
/* 30 */     super(root);
/* 31 */     this.nautilus = root.getChild("root");
/* 32 */     this.body = this.nautilus.getChild("body");
/*    */     
/* 34 */     this.swimAnimation = NautilusAnimation.SWIMMING.bake(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 38 */     return LayerDefinition.create(createBodyMesh(), 128, 128);
/*    */   }
/*    */   
/*    */   public static MeshDefinition createBodyMesh() {
/* 42 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 43 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*    */     
/* 45 */     PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
/*    */     
/* 47 */     nautilus.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.0F))
/* 48 */         .texOffs(0, 26).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 20.0F, new CubeDeformation(0.0F))
/* 49 */         .texOffs(48, 26).addBox(-7.0F, 0.0F, 6.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 5.0F));
/*    */     
/* 51 */     PartDefinition body = nautilus.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 54).addBox(-5.0F, -4.51F, -3.0F, 10.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
/* 52 */         .texOffs(0, 76).addBox(-5.0F, -4.51F, 7.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 12.3F));
/*    */     
/* 54 */     body.addOrReplaceChild("upper_mouth", CubeListBuilder.create().texOffs(54, 54).addBox(-5.0F, -2.0F, 0.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, -2.51F, 7.0F));
/* 55 */     body.addOrReplaceChild("inner_mouth", CubeListBuilder.create().texOffs(54, 70).addBox(-3.0F, -2.0F, -0.5F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.51F, 7.5F));
/* 56 */     body.addOrReplaceChild("lower_mouth", CubeListBuilder.create().texOffs(54, 62).addBox(-5.0F, -1.98F, 0.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 1.49F, 7.0F));
/*    */     
/* 58 */     return meshdefinition;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBabyBodyLayer() {
/* 62 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 63 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*    */     
/* 65 */     PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-0.5F, 28.0F, -0.5F));
/*    */     
/* 67 */     nautilus.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -4.0F, -1.0F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
/* 68 */         .texOffs(0, 11).addBox(-6.0F, 0.0F, -1.0F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
/* 69 */         .texOffs(23, 11).addBox(-6.0F, 0.0F, 5.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -8.0F, -2.0F));
/*    */     
/* 71 */     PartDefinition body = nautilus.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -3.01F, -1.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
/* 72 */         .texOffs(0, 35).addBox(-2.5F, -3.01F, 4.1F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -5.0F, 3.0F));
/*    */     
/* 74 */     body.addOrReplaceChild("upper_mouth", CubeListBuilder.create().texOffs(24, 24).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, -2.01F, 3.9F));
/* 75 */     body.addOrReplaceChild("inner_mouth", CubeListBuilder.create().texOffs(24, 32).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.01F, 4.9F));
/* 76 */     body.addOrReplaceChild("lower_mouth", CubeListBuilder.create().texOffs(24, 28).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, -0.01F, 3.9F));
/*    */     
/* 78 */     return LayerDefinition.create(meshdefinition, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(NautilusRenderState state) {
/* 83 */     super.setupAnim(state);
/* 84 */     applyBodyRotation(state.yRot, state.xRot);
/* 85 */     this.swimAnimation.applyWalk(state.walkAnimationPos + state.ageInTicks / 5.0F, state.walkAnimationSpeed + 0.2F, 2.0F, 3.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   private void applyBodyRotation(float yRot, float xRot) {
/* 90 */     yRot = Mth.clamp(yRot, -10.0F, 10.0F);
/* 91 */     xRot = Mth.clamp(xRot, -10.0F, 10.0F);
/* 92 */     this.body.yRot = yRot * 0.017453292F;
/* 93 */     this.body.xRot = xRot * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/nautilus/NautilusModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */