/*    */ package net.minecraft.client.profiling;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.LongSupplier;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.LevelRenderer;
/*    */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*    */ import net.minecraft.util.profiling.ProfileCollector;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*    */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*    */ import net.minecraft.util.profiling.metrics.profiling.ProfilerSamplerAdapter;
/*    */ import net.minecraft.util.profiling.metrics.profiling.ServerMetricsSamplersProvider;
/*    */ 
/*    */ public class ClientMetricsSamplersProvider
/*    */   implements MetricsSamplerProvider {
/*    */   private final LevelRenderer levelRenderer;
/* 20 */   private final Set<MetricSampler> samplers = (Set<MetricSampler>)new ObjectOpenHashSet();
/* 21 */   private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();
/*    */   
/*    */   public ClientMetricsSamplersProvider(LongSupplier wallTimeSource, LevelRenderer levelRenderer) {
/* 24 */     this.levelRenderer = levelRenderer;
/* 25 */     this.samplers.add(ServerMetricsSamplersProvider.tickTimeSampler(wallTimeSource));
/* 26 */     registerStaticSamplers();
/*    */   }
/*    */   
/*    */   private void registerStaticSamplers() {
/* 30 */     this.samplers.addAll(ServerMetricsSamplersProvider.runtimeIndependentSamplers());
/*    */     
/* 32 */     this.samplers.add(MetricSampler.create("totalChunks", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::getTotalSections));
/* 33 */     this.samplers.add(MetricSampler.create("renderedChunks", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::countRenderedSections));
/* 34 */     this.samplers.add(MetricSampler.create("lastViewDistance", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::getLastViewDistance));
/*    */     
/* 36 */     SectionRenderDispatcher sectionRenderDispatcher = this.levelRenderer.getSectionRenderDispatcher();
/* 37 */     if (sectionRenderDispatcher != null) {
/* 38 */       this.samplers.add(MetricSampler.create("toUpload", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher, SectionRenderDispatcher::getToUpload));
/* 39 */       this.samplers.add(MetricSampler.create("freeBufferCount", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher, SectionRenderDispatcher::getFreeBufferCount));
/* 40 */       this.samplers.add(MetricSampler.create("compileQueueSize", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher, SectionRenderDispatcher::getCompileQueueSize));
/*    */     } 
/*    */ 
/*    */     
/* 44 */     this.samplers.add(MetricSampler.create("gpuUtilization", MetricCategory.GPU, Minecraft.getInstance(), Minecraft::getGpuUtilization));
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MetricSampler> samplers(Supplier<ProfileCollector> singleTickProfiler) {
/* 49 */     this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(singleTickProfiler));
/* 50 */     return this.samplers;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/profiling/ClientMetricsSamplersProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */