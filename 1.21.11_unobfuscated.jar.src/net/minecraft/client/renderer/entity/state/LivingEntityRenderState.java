/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.block.SkullBlock;
/*    */ 
/*    */ public class LivingEntityRenderState
/*    */   extends EntityRenderState {
/*    */   public float bodyRot;
/*    */   public float yRot;
/*    */   public float xRot;
/*    */   public float deathTime;
/*    */   public float walkAnimationPos;
/*    */   public float walkAnimationSpeed;
/* 17 */   public float scale = 1.0F;
/* 18 */   public float ageScale = 1.0F;
/*    */   
/*    */   public float ticksSinceKineticHitFeedback;
/*    */   public boolean isUpsideDown;
/*    */   public boolean isFullyFrozen;
/*    */   public boolean isBaby;
/*    */   public boolean isInWater;
/*    */   public boolean isAutoSpinAttack;
/*    */   public boolean hasRedOverlay;
/*    */   public boolean isInvisibleToPlayer;
/*    */   public Direction bedOrientation;
/* 29 */   public Pose pose = Pose.STANDING;
/* 30 */   public final ItemStackRenderState headItem = new ItemStackRenderState();
/*    */   
/*    */   public float wornHeadAnimationPos;
/*    */   public SkullBlock.Type wornHeadType;
/*    */   public ResolvableProfile wornHeadProfile;
/*    */   
/*    */   public boolean hasPose(Pose pose) {
/* 37 */     return (this.pose == pose);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/LivingEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */