/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ShulkerRenderState
/*    */   extends LivingEntityRenderState {
/*  9 */   public Vec3 renderOffset = Vec3.ZERO;
/*    */   public DyeColor color;
/*    */   public float peekAmount;
/*    */   public float yHeadRot;
/*    */   public float yBodyRot;
/* 14 */   public Direction attachFace = Direction.DOWN;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ShulkerRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */