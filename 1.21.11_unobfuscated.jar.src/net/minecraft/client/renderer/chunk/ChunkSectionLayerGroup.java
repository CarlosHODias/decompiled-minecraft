/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public enum ChunkSectionLayerGroup
/*    */ {
/*  9 */   OPAQUE(new ChunkSectionLayer[] { ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT }),
/* 10 */   TRANSLUCENT(new ChunkSectionLayer[] { ChunkSectionLayer.TRANSLUCENT }),
/* 11 */   TRIPWIRE(new ChunkSectionLayer[] { ChunkSectionLayer.TRIPWIRE });
/*    */   
/*    */   private final String label;
/*    */   
/*    */   private final ChunkSectionLayer[] layers;
/*    */   
/*    */   ChunkSectionLayerGroup(ChunkSectionLayer... layers) {
/* 18 */     this.layers = layers;
/* 19 */     this.label = toString().toLowerCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public String label() {
/* 23 */     return this.label;
/*    */   }
/*    */   
/*    */   public ChunkSectionLayer[] layers() {
/* 27 */     return this.layers;
/*    */   }
/*    */   
/*    */   public RenderTarget outputTarget() {
/* 31 */     Minecraft minecraft = Minecraft.getInstance();
/* 32 */     switch (ordinal()) { case 2: 
/*    */       case 1: 
/*    */       default:
/* 35 */         break; }  RenderTarget renderTarget = minecraft.getMainRenderTarget();
/*    */     
/* 37 */     return (renderTarget != null) ? renderTarget : minecraft.getMainRenderTarget();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/ChunkSectionLayerGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */