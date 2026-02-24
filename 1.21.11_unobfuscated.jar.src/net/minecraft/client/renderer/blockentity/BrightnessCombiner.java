/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import net.minecraft.client.renderer.LevelRenderer;
/*    */ import net.minecraft.client.renderer.LightTexture;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.block.DoubleBlockCombiner;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public class BrightnessCombiner<S extends BlockEntity> implements DoubleBlockCombiner.Combiner<S, Int2IntFunction> {
/*    */   public Int2IntFunction acceptDouble(S first, S second) {
/* 12 */     return i -> {
/*    */         int firstCoords = LevelRenderer.getLightColor((BlockAndTintGetter)first.getLevel(), first.getBlockPos()), secondCoords = LevelRenderer.getLightColor((BlockAndTintGetter)second.getLevel(), second.getBlockPos()), firstBlock = LightTexture.block(firstCoords), secondBlock = LightTexture.block(secondCoords), firstSky = LightTexture.sky(firstCoords), secondSky = LightTexture.sky(secondCoords);
/*    */         return LightTexture.pack(Math.max(firstBlock, secondBlock), Math.max(firstSky, secondSky));
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Int2IntFunction acceptSingle(S single) {
/* 28 */     return i -> i;
/*    */   }
/*    */ 
/*    */   
/*    */   public Int2IntFunction acceptNone() {
/* 33 */     return i -> i;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BrightnessCombiner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */