/*    */ package net.minecraft.client.model.monster.creaking;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.animation.KeyframeAnimation;
/*    */ import net.minecraft.client.animation.definitions.CreakingAnimation;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.CreakingRenderState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreakingModel
/*    */   extends EntityModel<CreakingRenderState>
/*    */ {
/*    */   private final ModelPart head;
/*    */   private final KeyframeAnimation walkAnimation;
/*    */   private final KeyframeAnimation attackAnimation;
/*    */   private final KeyframeAnimation invulnerableAnimation;
/*    */   private final KeyframeAnimation deathAnimation;
/*    */   
/*    */   public CreakingModel(ModelPart roots) {
/* 27 */     super(roots);
/* 28 */     ModelPart root = roots.getChild("root");
/* 29 */     ModelPart upperBody = root.getChild("upper_body");
/* 30 */     this.head = upperBody.getChild("head");
/*    */     
/* 32 */     this.walkAnimation = CreakingAnimation.CREAKING_WALK.bake(root);
/* 33 */     this.attackAnimation = CreakingAnimation.CREAKING_ATTACK.bake(root);
/* 34 */     this.invulnerableAnimation = CreakingAnimation.CREAKING_INVULNERABLE.bake(root);
/* 35 */     this.deathAnimation = CreakingAnimation.CREAKING_DEATH.bake(root);
/*    */   }
/*    */   
/*    */   private static MeshDefinition createMesh() {
/* 39 */     MeshDefinition meshDefinition = new MeshDefinition();
/* 40 */     PartDefinition partDefinition = meshDefinition.getRoot();
/*    */     
/* 42 */     PartDefinition root = partDefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
/*    */     
/* 44 */     PartDefinition upperBody = root.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(-1.0F, -19.0F, 0.0F));
/*    */     
/* 46 */     upperBody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -10.0F, -3.0F, 6.0F, 10.0F, 6.0F)
/* 47 */         .texOffs(28, 31).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 3.0F, 6.0F)
/* 48 */         .texOffs(12, 40).addBox(3.0F, -13.0F, 0.0F, 9.0F, 14.0F, 0.0F)
/* 49 */         .texOffs(34, 12).addBox(-12.0F, -14.0F, 0.0F, 9.0F, 14.0F, 0.0F), PartPose.offset(-3.0F, -11.0F, 0.0F));
/*    */     
/* 51 */     upperBody.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -3.0F, -3.0F, 6.0F, 13.0F, 5.0F)
/* 52 */         .texOffs(24, 0).addBox(-6.0F, -4.0F, -3.0F, 6.0F, 7.0F, 5.0F), PartPose.offset(0.0F, -7.0F, 1.0F));
/*    */     
/* 54 */     upperBody.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(22, 13).addBox(-2.0F, -1.5F, -1.5F, 3.0F, 21.0F, 3.0F)
/* 55 */         .texOffs(46, 0).addBox(-2.0F, 19.5F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(-7.0F, -9.5F, 1.5F));
/*    */     
/* 57 */     upperBody.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(30, 40).addBox(0.0F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F)
/* 58 */         .texOffs(52, 12).addBox(0.0F, -5.0F, -1.5F, 3.0F, 4.0F, 3.0F)
/* 59 */         .texOffs(52, 19).addBox(0.0F, 15.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(6.0F, -9.0F, 0.5F));
/*    */     
/* 61 */     root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 40).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 16.0F, 3.0F)
/* 62 */         .texOffs(45, 55).addBox(-1.5F, 15.7F, -4.5F, 5.0F, 0.0F, 9.0F), PartPose.offset(1.5F, -16.0F, 0.5F));
/*    */     
/* 64 */     root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -1.5F, -1.5F, 3.0F, 19.0F, 3.0F)
/* 65 */         .texOffs(45, 46).addBox(-5.0F, 17.2F, -4.5F, 5.0F, 0.0F, 9.0F)
/* 66 */         .texOffs(12, 34).addBox(-3.0F, -4.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offset(-1.0F, -17.5F, 0.5F));
/*    */     
/* 68 */     return meshDefinition;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 72 */     MeshDefinition mesh = createMesh();
/* 73 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createEyesLayer() {
/* 77 */     MeshDefinition mesh = createMesh();
/* 78 */     mesh.getRoot().retainExactParts(Set.of("head"));
/* 79 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(CreakingRenderState state) {
/* 84 */     super.setupAnim(state);
/* 85 */     this.head.xRot = state.xRot * 0.017453292F;
/* 86 */     this.head.yRot = state.yRot * 0.017453292F;
/* 87 */     if (state.canMove)
/*    */     {
/* 89 */       this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1.0F, 1.0F);
/*    */     }
/* 91 */     this.attackAnimation.apply(state.attackAnimationState, state.ageInTicks);
/* 92 */     this.invulnerableAnimation.apply(state.invulnerabilityAnimationState, state.ageInTicks);
/* 93 */     this.deathAnimation.apply(state.deathAnimationState, state.ageInTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/creaking/CreakingModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */