/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ 
/*    */ public class BlockBreakingRenderState
/*    */   extends MovingBlockRenderState {
/*    */   public BlockBreakingRenderState(ClientLevel level, BlockPos pos, int progress) {
/* 11 */     this.level = (BlockAndTintGetter)level;
/* 12 */     this.blockPos = pos;
/* 13 */     this.blockState = level.getBlockState(pos);
/* 14 */     this.progress = progress;
/* 15 */     this.biome = level.getBiome(pos);
/*    */   }
/*    */   
/*    */   public int progress;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/BlockBreakingRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */