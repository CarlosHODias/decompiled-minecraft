/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ 
/*    */ public final class ModelRenderProperties extends Record {
/*    */   private final boolean usesBlockLight;
/*    */   private final TextureAtlasSprite particleIcon;
/*    */   private final net.minecraft.client.renderer.block.model.ItemTransforms transforms;
/*    */   
/* 10 */   public ModelRenderProperties(boolean usesBlockLight, TextureAtlasSprite particleIcon, net.minecraft.client.renderer.block.model.ItemTransforms transforms) { this.usesBlockLight = usesBlockLight; this.particleIcon = particleIcon; this.transforms = transforms; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ModelRenderProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ModelRenderProperties; } public boolean usesBlockLight() { return this.usesBlockLight; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ModelRenderProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ModelRenderProperties; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ModelRenderProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/ModelRenderProperties;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public TextureAtlasSprite particleIcon() { return this.particleIcon; } public net.minecraft.client.renderer.block.model.ItemTransforms transforms() { return this.transforms; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ModelRenderProperties fromResolvedModel(net.minecraft.client.resources.model.ModelBaker baker, net.minecraft.client.resources.model.ResolvedModel resolvedModel, net.minecraft.client.renderer.block.model.TextureSlots textureSlots) {
/* 17 */     TextureAtlasSprite particleSprite = resolvedModel.resolveParticleSprite(textureSlots, baker);
/*    */     
/* 19 */     return new ModelRenderProperties(
/* 20 */         resolvedModel.getTopGuiLight().lightLikeBlock(), particleSprite, 
/*    */         
/* 22 */         resolvedModel.getTopTransforms());
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyToLayer(ItemStackRenderState.LayerRenderState layer, net.minecraft.world.item.ItemDisplayContext displayContext) {
/* 27 */     layer.setUsesBlockLight(this.usesBlockLight);
/* 28 */     layer.setParticleIcon(this.particleIcon);
/* 29 */     layer.setTransform(this.transforms.getTransform(displayContext));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ModelRenderProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */