/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface BlockEntityRendererProvider<T extends net.minecraft.world.level.block.entity.BlockEntity, S extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState> {
/*    */   BlockEntityRenderer<T, S> create(Context paramContext);
/*    */   
/*    */   public static final class Context extends Record {
/*    */     private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
/*    */     private final net.minecraft.client.renderer.block.BlockRenderDispatcher blockRenderDispatcher;
/*    */     private final net.minecraft.client.renderer.item.ItemModelResolver itemModelResolver;
/*    */     private final net.minecraft.client.renderer.entity.ItemRenderer itemRenderer;
/*    */     private final net.minecraft.client.renderer.entity.EntityRenderDispatcher entityRenderer;
/*    */     private final net.minecraft.client.model.geom.EntityModelSet entityModelSet;
/*    */     private final net.minecraft.client.gui.Font font;
/*    */     private final net.minecraft.client.resources.model.MaterialSet materials;
/*    */     private final net.minecraft.client.renderer.PlayerSkinRenderCache playerSkinRenderCache;
/*    */     
/* 18 */     public Context(BlockEntityRenderDispatcher blockEntityRenderDispatcher, net.minecraft.client.renderer.block.BlockRenderDispatcher blockRenderDispatcher, net.minecraft.client.renderer.item.ItemModelResolver itemModelResolver, net.minecraft.client.renderer.entity.ItemRenderer itemRenderer, net.minecraft.client.renderer.entity.EntityRenderDispatcher entityRenderer, net.minecraft.client.model.geom.EntityModelSet entityModelSet, net.minecraft.client.gui.Font font, net.minecraft.client.resources.model.MaterialSet materials, net.minecraft.client.renderer.PlayerSkinRenderCache playerSkinRenderCache) { this.blockEntityRenderDispatcher = blockEntityRenderDispatcher; this.blockRenderDispatcher = blockRenderDispatcher; this.itemModelResolver = itemModelResolver; this.itemRenderer = itemRenderer; this.entityRenderer = entityRenderer; this.entityModelSet = entityModelSet; this.font = font; this.materials = materials; this.playerSkinRenderCache = playerSkinRenderCache; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 18 */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context; } public BlockEntityRenderDispatcher blockEntityRenderDispatcher() { return this.blockEntityRenderDispatcher; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;
/* 18 */       //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.client.renderer.block.BlockRenderDispatcher blockRenderDispatcher() { return this.blockRenderDispatcher; } public net.minecraft.client.renderer.item.ItemModelResolver itemModelResolver() { return this.itemModelResolver; } public net.minecraft.client.renderer.entity.ItemRenderer itemRenderer() { return this.itemRenderer; } public net.minecraft.client.renderer.entity.EntityRenderDispatcher entityRenderer() { return this.entityRenderer; } public net.minecraft.client.model.geom.EntityModelSet entityModelSet() { return this.entityModelSet; } public net.minecraft.client.gui.Font font() { return this.font; } public net.minecraft.client.resources.model.MaterialSet materials() { return this.materials; } public net.minecraft.client.renderer.PlayerSkinRenderCache playerSkinRenderCache() { return this.playerSkinRenderCache; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public net.minecraft.client.model.geom.ModelPart bakeLayer(net.minecraft.client.model.geom.ModelLayerLocation id) {
/* 30 */       return this.entityModelSet.bakeLayer(id);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BlockEntityRendererProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */