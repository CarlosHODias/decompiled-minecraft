/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ public class VertexMultiConsumer {
/*     */   public static VertexConsumer create() {
/*   7 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public static VertexConsumer create(VertexConsumer consumer) {
/*  11 */     return consumer;
/*     */   }
/*     */   
/*     */   public static VertexConsumer create(VertexConsumer first, VertexConsumer second) {
/*  15 */     return new Double(first, second);
/*     */   }
/*     */   
/*     */   public static VertexConsumer create(VertexConsumer... consumers) {
/*  19 */     return new Multiple(consumers);
/*     */   }
/*     */   
/*     */   private static class Double
/*     */     implements VertexConsumer {
/*     */     private final VertexConsumer first;
/*     */     private final VertexConsumer second;
/*     */     
/*     */     public Double(VertexConsumer first, VertexConsumer second) {
/*  28 */       if (first == second) {
/*  29 */         throw new IllegalArgumentException("Duplicate delegates");
/*     */       }
/*  31 */       this.first = first;
/*  32 */       this.second = second;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer addVertex(float x, float y, float z) {
/*  37 */       this.first.addVertex(x, y, z);
/*  38 */       this.second.addVertex(x, y, z);
/*  39 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setColor(int r, int g, int b, int a) {
/*  44 */       this.first.setColor(r, g, b, a);
/*  45 */       this.second.setColor(r, g, b, a);
/*  46 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setColor(int color) {
/*  51 */       this.first.setColor(color);
/*  52 */       this.second.setColor(color);
/*  53 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setUv(float u, float v) {
/*  58 */       this.first.setUv(u, v);
/*  59 */       this.second.setUv(u, v);
/*  60 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setUv1(int u, int v) {
/*  65 */       this.first.setUv1(u, v);
/*  66 */       this.second.setUv1(u, v);
/*  67 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setUv2(int u, int v) {
/*  72 */       this.first.setUv2(u, v);
/*  73 */       this.second.setUv2(u, v);
/*  74 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setNormal(float x, float y, float z) {
/*  79 */       this.first.setNormal(x, y, z);
/*  80 */       this.second.setNormal(x, y, z);
/*  81 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setLineWidth(float width) {
/*  86 */       this.first.setLineWidth(width);
/*  87 */       this.second.setLineWidth(width);
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz)
/*     */     {
/*  93 */       this.first.addVertex(x, y, z, color, u, v, overlayCoords, lightCoords, nx, ny, nz);
/*  94 */       this.second.addVertex(x, y, z, color, u, v, overlayCoords, lightCoords, nx, ny, nz);
/*     */     } } private static final class Multiple extends Record implements VertexConsumer { private final VertexConsumer[] delegates; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/VertexMultiConsumer$Multiple;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/VertexMultiConsumer$Multiple;
/*     */     }
/*  98 */     public VertexConsumer[] delegates() { return this.delegates; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/VertexMultiConsumer$Multiple;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/VertexMultiConsumer$Multiple; } private Multiple(VertexConsumer[] delegates) {
/* 100 */       for (int i = 0; i < delegates.length; i++) {
/* 101 */         for (int j = i + 1; j < delegates.length; j++) {
/* 102 */           if (delegates[i] == delegates[j])
/* 103 */             throw new IllegalArgumentException("Duplicate delegates"); 
/*     */         } 
/*     */       } 
/*     */       this.delegates = delegates;
/*     */     } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/VertexMultiConsumer$Multiple;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/VertexMultiConsumer$Multiple;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } private void forEach(Consumer<VertexConsumer> out) {
/* 110 */       for (VertexConsumer delegate : this.delegates) {
/* 111 */         out.accept(delegate);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer addVertex(float x, float y, float z) {
/* 117 */       forEach(d -> d.addVertex(x, y, z));
/* 118 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setColor(int r, int g, int b, int a) {
/* 123 */       forEach(d -> d.setColor(r, g, b, a));
/* 124 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setColor(int color) {
/* 129 */       forEach(d -> d.setColor(color));
/* 130 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setUv(float u, float v) {
/* 135 */       forEach(d -> d.setUv(u, v));
/* 136 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setUv1(int u, int v) {
/* 141 */       forEach(d -> d.setUv1(u, v));
/* 142 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setUv2(int u, int v) {
/* 147 */       forEach(d -> d.setUv2(u, v));
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setNormal(float x, float y, float z) {
/* 153 */       forEach(d -> d.setNormal(x, y, z));
/* 154 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexConsumer setLineWidth(float width) {
/* 159 */       forEach(d -> d.setLineWidth(width));
/* 160 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
/* 165 */       forEach(d -> d.addVertex(x, y, z, color, u, v, overlayCoords, lightCoords, nx, ny, nz));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/VertexMultiConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */