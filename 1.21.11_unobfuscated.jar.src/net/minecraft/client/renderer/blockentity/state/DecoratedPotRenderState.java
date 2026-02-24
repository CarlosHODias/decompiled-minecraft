/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.PotDecorations;
/*    */ 
/*    */ public class DecoratedPotRenderState
/*    */   extends BlockEntityRenderState {
/*    */   public float yRot;
/*    */   public DecoratedPotBlockEntity.WobbleStyle wobbleStyle;
/*    */   public float wobbleProgress;
/* 12 */   public PotDecorations decorations = PotDecorations.EMPTY;
/* 13 */   public Direction direction = Direction.NORTH;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/DecoratedPotRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */