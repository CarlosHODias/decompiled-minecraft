/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.DyedItemColor;
/*    */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*    */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*    */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EquipmentLayerRenderer
/*    */ {
/*    */   private static final int NO_LAYER_COLOR = 0;
/*    */   private final EquipmentAssetManager equipmentAssets;
/*    */   private final Function<LayerTextureKey, Identifier> layerTextureLookup;
/*    */   private final Function<TrimSpriteKey, TextureAtlasSprite> trimSpriteLookup;
/*    */   
/*    */   public EquipmentLayerRenderer(EquipmentAssetManager equipmentAssets, TextureAtlas armorTrimAtlas) {
/* 38 */     this.equipmentAssets = equipmentAssets;
/* 39 */     this.layerTextureLookup = Util.memoize(key -> key.layer.getTextureLocation(key.layerType));
/* 40 */     this.trimSpriteLookup = Util.memoize(key -> armorTrimAtlas.getSprite(key.spriteId()));
/*    */   }
/*    */   
/*    */   public <S> void renderLayers(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, Model<? super S> model, S state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor) {
/* 44 */     renderLayers(layerType, equipmentAssetId, model, state, itemStack, poseStack, submitNodeCollector, lightCoords, null, outlineColor, 1);
/*    */   }
/*    */   
/*    */   public <S> void renderLayers(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, Model<? super S> model, S state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier playerTextureOverride, int outlineColor, int order) {
/* 48 */     List<EquipmentClientInfo.Layer> layers = this.equipmentAssets.get(equipmentAssetId).getLayers(layerType);
/* 49 */     if (layers.isEmpty()) {
/*    */       return;
/*    */     }
/* 52 */     int dyeColor = DyedItemColor.getOrDefault(itemStack, 0);
/* 53 */     boolean renderFoil = itemStack.hasFoil();
/*    */     
/* 55 */     int nextOrder = order;
/* 56 */     for (EquipmentClientInfo.Layer layer : layers) {
/* 57 */       int color = getColorForLayer(layer, dyeColor);
/* 58 */       if (color == 0) {
/*    */         continue;
/*    */       }
/* 61 */       Identifier layerTexture = (layer.usePlayerTexture() && playerTextureOverride != null) ? playerTextureOverride : this.layerTextureLookup.apply(new LayerTextureKey(layerType, layer));
/* 62 */       submitNodeCollector.order(nextOrder++).submitModel(model, state, poseStack, RenderTypes.armorCutoutNoCull(layerTexture), lightCoords, OverlayTexture.NO_OVERLAY, color, null, outlineColor, null);
/* 63 */       if (renderFoil) {
/* 64 */         submitNodeCollector.order(nextOrder++).submitModel(model, state, poseStack, RenderTypes.armorEntityGlint(), lightCoords, OverlayTexture.NO_OVERLAY, color, null, outlineColor, null);
/*    */       }
/* 66 */       renderFoil = false;
/*    */     } 
/*    */     
/* 69 */     ArmorTrim trim = (ArmorTrim)itemStack.get(DataComponents.TRIM);
/* 70 */     if (trim != null) {
/* 71 */       TextureAtlasSprite sprite = this.trimSpriteLookup.apply(new TrimSpriteKey(trim, layerType, equipmentAssetId));
/* 72 */       RenderType renderType = Sheets.armorTrimsSheet(((TrimPattern)trim.pattern().value()).decal());
/* 73 */       submitNodeCollector.order(nextOrder++).submitModel(model, state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, outlineColor, null);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int getColorForLayer(EquipmentClientInfo.Layer layer, int dyeColor) {
/* 78 */     Optional<EquipmentClientInfo.Dyeable> dyeable = layer.dyeable();
/* 79 */     if (dyeable.isPresent()) {
/* 80 */       int colorWhenUndyed = (Integer)((EquipmentClientInfo.Dyeable)dyeable.get()).colorWhenUndyed().map(ARGB::opaque).orElse(0);
/* 81 */       return (dyeColor != 0) ? dyeColor : colorWhenUndyed;
/*    */     } 
/* 83 */     return -1;
/*    */   }
/*    */   private static final class LayerTextureKey extends Record { private final EquipmentClientInfo.LayerType layerType; private final EquipmentClientInfo.Layer layer;
/* 86 */     private LayerTextureKey(EquipmentClientInfo.LayerType layerType, EquipmentClientInfo.Layer layer) { this.layerType = layerType; this.layer = layer; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 86 */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey; } public EquipmentClientInfo.LayerType layerType() { return this.layerType; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;
/* 86 */       //   0	8	1	o	Ljava/lang/Object; } public EquipmentClientInfo.Layer layer() { return this.layer; }
/*    */      }
/*    */   private static final class TrimSpriteKey extends Record { private final ArmorTrim trim; private final EquipmentClientInfo.LayerType layerType; private final ResourceKey<EquipmentAsset> equipmentAssetId;
/* 89 */     private TrimSpriteKey(ArmorTrim trim, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId) { this.trim = trim; this.layerType = layerType; this.equipmentAssetId = equipmentAssetId; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #89	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #89	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #89	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;
/* 89 */       //   0	8	1	o	Ljava/lang/Object; } public ArmorTrim trim() { return this.trim; } public EquipmentClientInfo.LayerType layerType() { return this.layerType; } public ResourceKey<EquipmentAsset> equipmentAssetId() { return this.equipmentAssetId; }
/*    */      public Identifier spriteId() {
/* 91 */       return this.trim.layerAssetId(this.layerType.trimAssetPrefix(), this.equipmentAssetId);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */