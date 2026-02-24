/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*    */ import java.util.SequencedMap;
/*    */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.resources.model.ModelBakery;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderBuffers
/*    */ {
/*    */   private final SectionBufferBuilderPack fixedBufferPack;
/*    */   private final SectionBufferBuilderPool sectionBufferPool;
/*    */   private final MultiBufferSource.BufferSource bufferSource;
/*    */   private final MultiBufferSource.BufferSource crumblingBufferSource;
/*    */   private final OutlineBufferSource outlineBufferSource;
/*    */   
/*    */   public RenderBuffers(int maxSectionBuilders) {
/* 23 */     this.fixedBufferPack = new SectionBufferBuilderPack();
/* 24 */     this.sectionBufferPool = SectionBufferBuilderPool.allocate(maxSectionBuilders);
/*    */     
/* 26 */     SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers = (SequencedMap<RenderType, ByteBufferBuilder>)Util.make(new Object2ObjectLinkedOpenHashMap(), map -> {
/*    */           map.put(Sheets.solidBlockSheet(), this.fixedBufferPack.buffer(ChunkSectionLayer.SOLID));
/*    */           
/*    */           map.put(Sheets.cutoutBlockSheet(), this.fixedBufferPack.buffer(ChunkSectionLayer.CUTOUT));
/*    */           
/*    */           map.put(Sheets.translucentItemSheet(), this.fixedBufferPack.buffer(ChunkSectionLayer.TRANSLUCENT));
/*    */           put(map, Sheets.translucentBlockItemSheet());
/*    */           put(map, Sheets.shieldSheet());
/*    */           put(map, Sheets.bedSheet());
/*    */           put(map, Sheets.shulkerBoxSheet());
/*    */           put(map, Sheets.signSheet());
/*    */           put(map, Sheets.hangingSignSheet());
/*    */           map.put(Sheets.chestSheet(), new ByteBufferBuilder(786432));
/*    */           put(map, RenderTypes.armorEntityGlint());
/*    */           put(map, RenderTypes.glint());
/*    */           put(map, RenderTypes.glintTranslucent());
/*    */           put(map, RenderTypes.entityGlint());
/*    */           put(map, RenderTypes.waterMask());
/*    */         });
/* 45 */     this.bufferSource = MultiBufferSource.immediateWithBuffers(fixedBuffers, new ByteBufferBuilder(786432));
/* 46 */     this.outlineBufferSource = new OutlineBufferSource();
/*    */     
/* 48 */     SequencedMap<RenderType, ByteBufferBuilder> crumblingBuffers = (SequencedMap<RenderType, ByteBufferBuilder>)Util.make(new Object2ObjectLinkedOpenHashMap(), map -> ModelBakery.DESTROY_TYPES.forEach(()));
/*    */ 
/*    */ 
/*    */     
/* 52 */     this.crumblingBufferSource = MultiBufferSource.immediateWithBuffers(crumblingBuffers, new ByteBufferBuilder(0));
/*    */   }
/*    */   
/*    */   private static void put(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map, RenderType type) {
/* 56 */     map.put(type, new ByteBufferBuilder(type.bufferSize()));
/*    */   }
/*    */   
/*    */   public SectionBufferBuilderPack fixedBufferPack() {
/* 60 */     return this.fixedBufferPack;
/*    */   }
/*    */   
/*    */   public SectionBufferBuilderPool sectionBufferPool() {
/* 64 */     return this.sectionBufferPool;
/*    */   }
/*    */   
/*    */   public MultiBufferSource.BufferSource bufferSource() {
/* 68 */     return this.bufferSource;
/*    */   }
/*    */   
/*    */   public MultiBufferSource.BufferSource crumblingBufferSource() {
/* 72 */     return this.crumblingBufferSource;
/*    */   }
/*    */   
/*    */   public OutlineBufferSource outlineBufferSource() {
/* 76 */     return this.outlineBufferSource;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/RenderBuffers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */