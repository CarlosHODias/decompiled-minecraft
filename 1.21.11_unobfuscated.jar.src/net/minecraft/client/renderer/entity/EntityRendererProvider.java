/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.MapRenderer;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*    */ import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.resources.model.AtlasManager;
/*    */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.data.AtlasIds;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface EntityRendererProvider<T extends net.minecraft.world.entity.Entity>
/*    */ {
/*    */   EntityRenderer<T, ?> create(Context paramContext);
/*    */   
/*    */   public static class Context
/*    */   {
/*    */     private final EntityRenderDispatcher entityRenderDispatcher;
/*    */     private final ItemModelResolver itemModelResolver;
/*    */     private final MapRenderer mapRenderer;
/*    */     private final BlockRenderDispatcher blockRenderDispatcher;
/*    */     private final ResourceManager resourceManager;
/*    */     
/*    */     public Context(EntityRenderDispatcher entityRenderDispatcher, ItemModelResolver itemModelResolver, MapRenderer mapRenderer, BlockRenderDispatcher blockRenderDispatcher, ResourceManager resourceManager, EntityModelSet modelSet, EquipmentAssetManager equipmentAssets, AtlasManager atlasManager, Font font, PlayerSkinRenderCache playerSkinRenderCache) {
/* 37 */       this.entityRenderDispatcher = entityRenderDispatcher;
/* 38 */       this.itemModelResolver = itemModelResolver;
/* 39 */       this.mapRenderer = mapRenderer;
/* 40 */       this.blockRenderDispatcher = blockRenderDispatcher;
/* 41 */       this.resourceManager = resourceManager;
/* 42 */       this.modelSet = modelSet;
/* 43 */       this.equipmentAssets = equipmentAssets;
/* 44 */       this.font = font;
/* 45 */       this.atlasManager = atlasManager;
/* 46 */       this.playerSkinRenderCache = playerSkinRenderCache;
/* 47 */       this.equipmentRenderer = new EquipmentLayerRenderer(equipmentAssets, atlasManager.getAtlasOrThrow(AtlasIds.ARMOR_TRIMS));
/*    */     }
/*    */     private final EntityModelSet modelSet; private final EquipmentAssetManager equipmentAssets; private final Font font; private final EquipmentLayerRenderer equipmentRenderer; private final AtlasManager atlasManager; private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */     public EntityRenderDispatcher getEntityRenderDispatcher() {
/* 51 */       return this.entityRenderDispatcher;
/*    */     }
/*    */     
/*    */     public ItemModelResolver getItemModelResolver() {
/* 55 */       return this.itemModelResolver;
/*    */     }
/*    */     
/*    */     public MapRenderer getMapRenderer() {
/* 59 */       return this.mapRenderer;
/*    */     }
/*    */     
/*    */     public BlockRenderDispatcher getBlockRenderDispatcher() {
/* 63 */       return this.blockRenderDispatcher;
/*    */     }
/*    */     
/*    */     public ResourceManager getResourceManager() {
/* 67 */       return this.resourceManager;
/*    */     }
/*    */     
/*    */     public EntityModelSet getModelSet() {
/* 71 */       return this.modelSet;
/*    */     }
/*    */     
/*    */     public EquipmentAssetManager getEquipmentAssets() {
/* 75 */       return this.equipmentAssets;
/*    */     }
/*    */     
/*    */     public EquipmentLayerRenderer getEquipmentRenderer() {
/* 79 */       return this.equipmentRenderer;
/*    */     }
/*    */     
/*    */     public MaterialSet getMaterials() {
/* 83 */       return (MaterialSet)this.atlasManager;
/*    */     }
/*    */     
/*    */     public TextureAtlas getAtlas(Identifier sheet) {
/* 87 */       return this.atlasManager.getAtlasOrThrow(sheet);
/*    */     }
/*    */     
/*    */     public ModelPart bakeLayer(ModelLayerLocation id) {
/* 91 */       return this.modelSet.bakeLayer(id);
/*    */     }
/*    */     
/*    */     public Font getFont() {
/* 95 */       return this.font;
/*    */     }
/*    */     
/*    */     public PlayerSkinRenderCache getPlayerSkinRenderCache() {
/* 99 */       return this.playerSkinRenderCache;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EntityRendererProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */