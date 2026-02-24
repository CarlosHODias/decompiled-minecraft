/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ import net.minecraft.client.model.AnimationUtils;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
/*    */ 
/*    */ public class ZombifiedPiglinModel extends AbstractPiglinModel<ZombifiedPiglinRenderState> {
/*    */   public ZombifiedPiglinModel(ModelPart root) {
/* 10 */     super(root);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(ZombifiedPiglinRenderState state) {
/* 15 */     super.setupAnim(state);
/* 16 */     AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, state.isAggressive, (UndeadRenderState)state);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAllVisible(boolean visible) {
/* 21 */     super.setAllVisible(visible);
/* 22 */     this.leftSleeve.visible = visible;
/* 23 */     this.rightSleeve.visible = visible;
/* 24 */     this.leftPants.visible = visible;
/* 25 */     this.rightPants.visible = visible;
/* 26 */     this.jacket.visible = visible;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/piglin/ZombifiedPiglinModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */