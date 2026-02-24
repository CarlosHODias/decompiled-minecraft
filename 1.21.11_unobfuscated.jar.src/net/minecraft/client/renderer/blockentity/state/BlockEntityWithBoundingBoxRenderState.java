/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.world.level.block.entity.BoundingBoxRenderable;
/*    */ 
/*    */ public class BlockEntityWithBoundingBoxRenderState
/*    */   extends BlockEntityRenderState {
/*    */   public boolean isVisible;
/*    */   public BoundingBoxRenderable.Mode mode;
/*    */   public BoundingBoxRenderable.RenderableBox box;
/*    */   public InvisibleBlockType[] invisibleBlocks;
/*    */   public boolean[] structureVoids;
/*    */   
/*    */   public enum InvisibleBlockType {
/* 14 */     AIR,
/* 15 */     BARRIER,
/* 16 */     LIGHT,
/* 17 */     STRUCTURE_VOID;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/BlockEntityWithBoundingBoxRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */