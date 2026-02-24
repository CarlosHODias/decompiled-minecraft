/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class SourceFilter extends Record implements net.minecraft.client.renderer.texture.atlas.SpriteSource {
/*    */   private final net.minecraft.util.IdentifierPattern filter;
/*    */   public static final com.mojang.serialization.MapCodec<SourceFilter> MAP_CODEC;
/*    */   
/*  9 */   public net.minecraft.util.IdentifierPattern filter() { return this.filter; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.IdentifierPattern.CODEC.fieldOf("pattern").forGetter(SourceFilter::filter)).apply((com.mojang.datafixers.kinds.Applicative)i, SourceFilter::new)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public SourceFilter(net.minecraft.util.IdentifierPattern filter) {
/* 17 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(net.minecraft.server.packs.resources.ResourceManager resourceManager, net.minecraft.client.renderer.texture.atlas.SpriteSource.Output output) {
/* 22 */     output.removeAll(this.filter.locationPredicate());
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SourceFilter> codec() {
/* 27 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/sources/SourceFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */