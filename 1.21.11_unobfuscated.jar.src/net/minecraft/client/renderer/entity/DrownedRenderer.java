/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.DrownedModel;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class DrownedRenderer extends AbstractZombieRenderer<Drowned, ZombieRenderState, DrownedModel> {
/* 18 */   private static final Identifier DROWNED_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned.png");
/*    */   
/*    */   public DrownedRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context, new DrownedModel(
/* 22 */           context.bakeLayer(ModelLayers.DROWNED)), new DrownedModel(
/* 23 */           context.bakeLayer(ModelLayers.DROWNED_BABY)), 
/* 24 */         ArmorModelSet.bake(ModelLayers.DROWNED_ARMOR, context.getModelSet(), DrownedModel::new), 
/* 25 */         ArmorModelSet.bake(ModelLayers.DROWNED_BABY_ARMOR, context.getModelSet(), DrownedModel::new));
/*    */ 
/*    */     
/* 28 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<ZombieRenderState, DrownedModel>)new net.minecraft.client.renderer.entity.layers.DrownedOuterLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   public ZombieRenderState createRenderState() {
/* 33 */     return new ZombieRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ZombieRenderState state) {
/* 38 */     return DROWNED_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(ZombieRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 43 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*    */     
/* 45 */     float swimAmount = state.swimAmount;
/* 46 */     if (swimAmount > 0.0F) {
/* 47 */       float targetRotationX = -10.0F - state.xRot;
/* 48 */       float rotationX = Mth.lerp(swimAmount, 0.0F, targetRotationX);
/* 49 */       poseStack.rotateAround((org.joml.Quaternionfc)com.mojang.math.Axis.XP.rotationDegrees(rotationX), 0.0F, state.boundingBoxHeight / 2.0F / entityScale, 0.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected HumanoidModel.ArmPose getArmPose(Drowned mob, HumanoidArm arm) {
/* 55 */     ItemStack item = mob.getItemHeldByArm(arm);
/* 56 */     if (mob.getMainArm() == arm && mob.isAggressive() && item.is(net.minecraft.world.item.Items.TRIDENT)) {
/* 57 */       return HumanoidModel.ArmPose.THROW_TRIDENT;
/*    */     }
/* 59 */     return super.getArmPose(mob, arm);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/DrownedRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */