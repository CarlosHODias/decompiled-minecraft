/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ModelState;
/*    */ import net.minecraft.client.resources.model.QuadCollection;
/*    */ import net.minecraft.client.resources.model.ResolvedModel;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class SimpleModelWrapper extends Record implements BlockModelPart {
/*    */   private final QuadCollection quads;
/*    */   private final boolean useAmbientOcclusion;
/*    */   private final TextureAtlasSprite particleIcon;
/*    */   
/* 19 */   public SimpleModelWrapper(QuadCollection quads, boolean useAmbientOcclusion, TextureAtlasSprite particleIcon) { this.quads = quads; this.useAmbientOcclusion = useAmbientOcclusion; this.particleIcon = particleIcon; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/SimpleModelWrapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SimpleModelWrapper; } public QuadCollection quads() { return this.quads; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/SimpleModelWrapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SimpleModelWrapper; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/SimpleModelWrapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/SimpleModelWrapper;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public boolean useAmbientOcclusion() { return this.useAmbientOcclusion; } public TextureAtlasSprite particleIcon() { return this.particleIcon; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   public static BlockModelPart bake(ModelBaker modelBakery, Identifier location, ModelState state) {
/*    */     HashMultimap hashMultimap;
/* 27 */     ResolvedModel model = modelBakery.getModel(location);
/*    */     
/* 29 */     TextureSlots textureSlots = model.getTopTextureSlots();
/* 30 */     boolean hasAmbientOcclusion = model.getTopAmbientOcclusion();
/* 31 */     TextureAtlasSprite particleSprite = model.resolveParticleSprite(textureSlots, modelBakery);
/* 32 */     QuadCollection geometry = model.bakeTopGeometry(textureSlots, modelBakery, state);
/*    */     
/* 34 */     Multimap<Identifier, Identifier> forbiddenSprites = null;
/* 35 */     for (BakedQuad bakedQuad : (Iterable<BakedQuad>)geometry.getAll()) {
/* 36 */       TextureAtlasSprite sprite = bakedQuad.sprite();
/* 37 */       if (!sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)) {
/* 38 */         if (forbiddenSprites == null) {
/* 39 */           hashMultimap = HashMultimap.create();
/*    */         }
/* 41 */         hashMultimap.put(sprite.atlasLocation(), sprite.contents().name());
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     if (hashMultimap != null) {
/* 46 */       LOGGER.warn("Rejecting block model {}, since it contains sprites from outside of supported atlas: {}", location, hashMultimap);
/* 47 */       return modelBakery.missingBlockModelPart();
/*    */     } 
/*    */     
/* 50 */     return new SimpleModelWrapper(geometry, hasAmbientOcclusion, particleSprite);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.List<BakedQuad> getQuads(Direction direction) {
/* 55 */     return this.quads.getQuads(direction);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/SimpleModelWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */