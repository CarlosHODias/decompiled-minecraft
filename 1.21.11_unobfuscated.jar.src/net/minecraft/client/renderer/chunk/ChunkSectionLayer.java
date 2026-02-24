/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ChunkSectionLayer
/*    */ {
/* 11 */   SOLID(RenderPipelines.SOLID_TERRAIN, 4194304, false),
/* 12 */   CUTOUT(RenderPipelines.CUTOUT_TERRAIN, 4194304, false),
/* 13 */   TRANSLUCENT(RenderPipelines.TRANSLUCENT_TERRAIN, 786432, true),
/* 14 */   TRIPWIRE(RenderPipelines.TRIPWIRE_TERRAIN, 1536, true);
/*    */   
/*    */   private final RenderPipeline pipeline;
/*    */   
/*    */   private final int bufferSize;
/*    */   private final boolean sortOnUpload;
/*    */   private final String label;
/*    */   
/*    */   ChunkSectionLayer(RenderPipeline pipeline, int bufferSize, boolean sortOnUpload) {
/* 23 */     this.pipeline = pipeline;
/* 24 */     this.bufferSize = bufferSize;
/* 25 */     this.sortOnUpload = sortOnUpload;
/* 26 */     this.label = toString().toLowerCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public RenderPipeline pipeline() {
/* 30 */     return this.pipeline;
/*    */   }
/*    */   
/*    */   public int bufferSize() {
/* 34 */     return this.bufferSize;
/*    */   }
/*    */   
/*    */   public String label() {
/* 38 */     return this.label;
/*    */   }
/*    */   
/*    */   public boolean sortOnUpload() {
/* 42 */     return this.sortOnUpload;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/ChunkSectionLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */