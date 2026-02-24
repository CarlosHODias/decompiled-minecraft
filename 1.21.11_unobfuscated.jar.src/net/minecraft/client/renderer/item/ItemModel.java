/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.util.RegistryContextSwapper;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ 
/*    */ public interface ItemModel {
/*    */   void update(ItemStackRenderState paramItemStackRenderState, net.minecraft.world.item.ItemStack paramItemStack, ItemModelResolver paramItemModelResolver, ItemDisplayContext paramItemDisplayContext, net.minecraft.client.multiplayer.ClientLevel paramClientLevel, net.minecraft.world.entity.ItemOwner paramItemOwner, int paramInt);
/*    */   
/*    */   public static final class BakingContext extends Record implements net.minecraft.client.renderer.special.SpecialModelRenderer.BakingContext {
/*    */     private final ModelBaker blockModelBaker;
/*    */     private final EntityModelSet entityModelSet;
/*    */     private final MaterialSet materials;
/*    */     private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */     private final ItemModel missingItemModel;
/*    */     private final RegistryContextSwapper contextSwapper;
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;
/*    */     }
/*    */     
/* 29 */     public BakingContext(ModelBaker blockModelBaker, EntityModelSet entityModelSet, MaterialSet materials, PlayerSkinRenderCache playerSkinRenderCache, ItemModel missingItemModel, RegistryContextSwapper contextSwapper) { this.blockModelBaker = blockModelBaker; this.entityModelSet = entityModelSet; this.materials = materials; this.playerSkinRenderCache = playerSkinRenderCache; this.missingItemModel = missingItemModel; this.contextSwapper = contextSwapper; } public ModelBaker blockModelBaker() { return this.blockModelBaker; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;
/* 29 */       //   0	8	1	o	Ljava/lang/Object; } public EntityModelSet entityModelSet() { return this.entityModelSet; } public MaterialSet materials() { return this.materials; } public PlayerSkinRenderCache playerSkinRenderCache() { return this.playerSkinRenderCache; } public ItemModel missingItemModel() { return this.missingItemModel; } public RegistryContextSwapper contextSwapper() { return this.contextSwapper; }
/*    */   
/*    */   }
/*    */   
/*    */   public static interface Unbaked extends net.minecraft.client.resources.model.ResolvableModel {
/*    */     com.mojang.serialization.MapCodec<? extends Unbaked> type();
/*    */     
/*    */     ItemModel bake(ItemModel.BakingContext param1BakingContext);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ItemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */