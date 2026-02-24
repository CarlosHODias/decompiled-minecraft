/*    */ package net.minecraft.client.model.monster.zombie;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ 
/*    */ public abstract class AbstractZombieModel<S extends ZombieRenderState> extends HumanoidModel<S> {
/*    */   protected AbstractZombieModel(ModelPart root) {
/* 10 */     super(root);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(S state) {
/* 15 */     super.setupAnim((HumanoidRenderState)state);
/* 16 */     net.minecraft.client.model.AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, ((ZombieRenderState)state).isAggressive, (UndeadRenderState)state);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/zombie/AbstractZombieModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */