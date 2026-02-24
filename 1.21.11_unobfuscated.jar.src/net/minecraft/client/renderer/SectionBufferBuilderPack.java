/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class SectionBufferBuilderPack
/*    */   implements AutoCloseable
/*    */ {
/*    */   public SectionBufferBuilderPack() {
/* 13 */     this.buffers = Util.makeEnumMap(ChunkSectionLayer.class, layer -> new ByteBufferBuilder(layer.bufferSize()));
/*    */   } public static final int TOTAL_BUFFERS_SIZE = Arrays.<ChunkSectionLayer>stream(ChunkSectionLayer.values()).mapToInt(ChunkSectionLayer::bufferSize).sum();
/*    */   public ByteBufferBuilder buffer(ChunkSectionLayer layer) {
/* 16 */     return this.buffers.get(layer);
/*    */   }
/*    */   private final Map<ChunkSectionLayer, ByteBufferBuilder> buffers;
/*    */   public void clearAll() {
/* 20 */     this.buffers.values().forEach(ByteBufferBuilder::clear);
/*    */   }
/*    */   
/*    */   public void discardAll() {
/* 24 */     this.buffers.values().forEach(ByteBufferBuilder::discard);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 29 */     this.buffers.values().forEach(ByteBufferBuilder::close);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SectionBufferBuilderPack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */