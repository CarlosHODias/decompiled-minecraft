/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.SkullBlock;
/*    */ 
/*    */ public class SkullBlockRenderState extends BlockEntityRenderState {
/*    */   public float animationProgress;
/*  9 */   public Direction direction = Direction.NORTH;
/*    */   public float rotationDegrees;
/* 11 */   public SkullBlock.Type skullType = (SkullBlock.Type)SkullBlock.Types.ZOMBIE;
/*    */   public RenderType renderType;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/SkullBlockRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */