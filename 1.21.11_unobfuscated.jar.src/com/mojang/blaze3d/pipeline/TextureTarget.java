/*    */ package com.mojang.blaze3d.pipeline;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ 
/*    */ public class TextureTarget
/*    */   extends RenderTarget {
/*    */   public TextureTarget(String label, int width, int height, boolean useDepth) {
/*  8 */     super(label, useDepth);
/*  9 */     RenderSystem.assertOnRenderThread();
/* 10 */     resize(width, height);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/pipeline/TextureTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */