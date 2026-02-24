/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CamelRenderState extends LivingEntityRenderState {
/*  7 */   public ItemStack saddle = ItemStack.EMPTY;
/*    */   public boolean isRidden;
/*    */   public float jumpCooldown;
/* 10 */   public final AnimationState sitAnimationState = new AnimationState();
/* 11 */   public final AnimationState sitPoseAnimationState = new AnimationState();
/* 12 */   public final AnimationState sitUpAnimationState = new AnimationState();
/* 13 */   public final AnimationState idleAnimationState = new AnimationState();
/* 14 */   public final AnimationState dashAnimationState = new AnimationState();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/CamelRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */