/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ 
/*    */ public class PaintingRenderState
/*    */   extends EntityRenderState {
/*  8 */   public Direction direction = Direction.NORTH;
/*    */   public PaintingVariant variant;
/* 10 */   public int[] lightCoordsPerBlock = new int[0];
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/PaintingRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */