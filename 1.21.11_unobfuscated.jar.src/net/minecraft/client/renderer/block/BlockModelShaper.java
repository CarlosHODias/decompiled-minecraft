/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.ModelManager;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockModelShaper
/*    */ {
/* 11 */   private Map<BlockState, BlockStateModel> modelByStateCache = Map.of();
/*    */   private final ModelManager modelManager;
/*    */   
/*    */   public BlockModelShaper(ModelManager modelManager) {
/* 15 */     this.modelManager = modelManager;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite getParticleIcon(BlockState blockState) {
/* 19 */     return getBlockModel(blockState).particleIcon();
/*    */   }
/*    */   
/*    */   public BlockStateModel getBlockModel(BlockState state) {
/* 23 */     BlockStateModel model = this.modelByStateCache.get(state);
/* 24 */     if (model == null) {
/* 25 */       model = this.modelManager.getMissingBlockStateModel();
/*    */     }
/*    */     
/* 28 */     return model;
/*    */   }
/*    */   
/*    */   public ModelManager getModelManager() {
/* 32 */     return this.modelManager;
/*    */   }
/*    */   
/*    */   public void replaceCache(Map<BlockState, BlockStateModel> modelByStateCache) {
/* 36 */     this.modelByStateCache = modelByStateCache;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/BlockModelShaper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */