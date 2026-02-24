/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ 
/*    */ public class OutlineBufferSource
/*    */   implements MultiBufferSource
/*    */ {
/* 11 */   private final MultiBufferSource.BufferSource outlineBufferSource = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
/*    */   
/* 13 */   private int outlineColor = -1;
/*    */ 
/*    */   
/*    */   public VertexConsumer getBuffer(RenderType renderType) {
/* 17 */     if (renderType.isOutline()) {
/* 18 */       VertexConsumer delegate = this.outlineBufferSource.getBuffer(renderType);
/* 19 */       return new EntityOutlineGenerator(delegate, this.outlineColor);
/*    */     } 
/* 21 */     Optional<RenderType> outline = renderType.outline();
/* 22 */     if (outline.isPresent()) {
/* 23 */       VertexConsumer delegate = this.outlineBufferSource.getBuffer(outline.get());
/* 24 */       return new EntityOutlineGenerator(delegate, this.outlineColor);
/*    */     } 
/* 26 */     throw new IllegalStateException("Can't render an outline for this rendertype!");
/*    */   }
/*    */   
/*    */   public void setColor(int color) {
/* 30 */     this.outlineColor = color;
/*    */   }
/*    */   
/*    */   public void endOutlineBatch() {
/* 34 */     this.outlineBufferSource.endBatch();
/*    */   }
/*    */   private static final class EntityOutlineGenerator extends Record implements VertexConsumer { private final VertexConsumer delegate; private final int color;
/*    */     
/* 38 */     private EntityOutlineGenerator(VertexConsumer delegate, int color) { this.delegate = delegate; this.color = color; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 38 */       //   0	7	0	this	Lnet/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator; } public VertexConsumer delegate() { return this.delegate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator;
/* 38 */       //   0	8	1	o	Ljava/lang/Object; } public int color() { return this.color; }
/*    */     
/*    */     public VertexConsumer addVertex(float x, float y, float z) {
/* 41 */       this.delegate.addVertex(x, y, z).setColor(this.color);
/* 42 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setColor(int r, int g, int b, int a) {
/* 47 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setColor(int color) {
/* 52 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setUv(float u, float v) {
/* 57 */       this.delegate.setUv(u, v);
/* 58 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setUv1(int u, int v) {
/* 63 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setUv2(int u, int v) {
/* 68 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setNormal(float x, float y, float z) {
/* 73 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer setLineWidth(float width) {
/* 78 */       return this;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/OutlineBufferSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */