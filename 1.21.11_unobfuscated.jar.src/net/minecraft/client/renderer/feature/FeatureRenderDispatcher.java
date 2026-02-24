/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.OutlineBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*    */ import net.minecraft.client.resources.model.AtlasManager;
/*    */ 
/*    */ public class FeatureRenderDispatcher implements AutoCloseable {
/*    */   private final SubmitNodeStorage submitNodeStorage;
/*    */   private final BlockRenderDispatcher blockRenderDispatcher;
/*    */   private final MultiBufferSource.BufferSource bufferSource;
/*    */   private final AtlasManager atlasManager;
/*    */   private final OutlineBufferSource outlineBufferSource;
/*    */   private final MultiBufferSource.BufferSource crumblingBufferSource;
/*    */   private final Font font;
/* 20 */   private final ShadowFeatureRenderer shadowFeatureRenderer = new ShadowFeatureRenderer();
/* 21 */   private final FlameFeatureRenderer flameFeatureRenderer = new FlameFeatureRenderer();
/* 22 */   private final ModelFeatureRenderer modelFeatureRenderer = new ModelFeatureRenderer();
/* 23 */   private final ModelPartFeatureRenderer modelPartFeatureRenderer = new ModelPartFeatureRenderer();
/* 24 */   private final NameTagFeatureRenderer nameTagFeatureRenderer = new NameTagFeatureRenderer();
/* 25 */   private final TextFeatureRenderer textFeatureRenderer = new TextFeatureRenderer();
/* 26 */   private final LeashFeatureRenderer leashFeatureRenderer = new LeashFeatureRenderer();
/* 27 */   private final ItemFeatureRenderer itemFeatureRenderer = new ItemFeatureRenderer();
/* 28 */   private final CustomFeatureRenderer customFeatureRenderer = new CustomFeatureRenderer();
/* 29 */   private final BlockFeatureRenderer blockFeatureRenderer = new BlockFeatureRenderer();
/* 30 */   private final ParticleFeatureRenderer particleFeatureRenderer = new ParticleFeatureRenderer();
/*    */   
/*    */   public FeatureRenderDispatcher(SubmitNodeStorage submitNodeStorage, BlockRenderDispatcher blockRenderDispatcher, MultiBufferSource.BufferSource bufferSource, AtlasManager atlasManager, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource, Font font) {
/* 33 */     this.submitNodeStorage = submitNodeStorage;
/* 34 */     this.blockRenderDispatcher = blockRenderDispatcher;
/* 35 */     this.bufferSource = bufferSource;
/* 36 */     this.atlasManager = atlasManager;
/* 37 */     this.outlineBufferSource = outlineBufferSource;
/* 38 */     this.crumblingBufferSource = crumblingBufferSource;
/* 39 */     this.font = font;
/*    */   }
/*    */   
/*    */   public void renderAllFeatures() {
/* 43 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = this.submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) { SubmitNodeCollection collection = objectIterator.next();
/* 44 */       this.shadowFeatureRenderer.render(collection, this.bufferSource);
/* 45 */       this.modelFeatureRenderer.render(collection, this.bufferSource, this.outlineBufferSource, this.crumblingBufferSource);
/* 46 */       this.modelPartFeatureRenderer.render(collection, this.bufferSource, this.outlineBufferSource, this.crumblingBufferSource);
/* 47 */       this.flameFeatureRenderer.render(collection, this.bufferSource, this.atlasManager);
/* 48 */       this.nameTagFeatureRenderer.render(collection, this.bufferSource, this.font);
/* 49 */       this.textFeatureRenderer.render(collection, this.bufferSource);
/* 50 */       this.leashFeatureRenderer.render(collection, this.bufferSource);
/* 51 */       this.itemFeatureRenderer.render(collection, this.bufferSource, this.outlineBufferSource);
/* 52 */       this.blockFeatureRenderer.render(collection, this.bufferSource, this.blockRenderDispatcher, this.outlineBufferSource);
/* 53 */       this.customFeatureRenderer.render(collection, this.bufferSource);
/* 54 */       this.particleFeatureRenderer.render(collection); }
/*    */ 
/*    */     
/* 57 */     this.submitNodeStorage.clear();
/*    */   }
/*    */   
/*    */   public void endFrame() {
/* 61 */     this.particleFeatureRenderer.endFrame();
/*    */   }
/*    */   
/*    */   public SubmitNodeStorage getSubmitNodeStorage() {
/* 65 */     return this.submitNodeStorage;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 70 */     this.particleFeatureRenderer.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/FeatureRenderDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */