/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.llama.LlamaModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LlamaRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*    */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class LlamaDecorLayer extends RenderLayer<LlamaRenderState, LlamaModel> {
/*    */   private final LlamaModel adultModel;
/*    */   
/*    */   public LlamaDecorLayer(RenderLayerParent<LlamaRenderState, LlamaModel> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 24 */     super(renderer);
/* 25 */     this.equipmentRenderer = equipmentRenderer;
/* 26 */     this.adultModel = new LlamaModel(modelSet.bakeLayer(ModelLayers.LLAMA_DECOR));
/* 27 */     this.babyModel = new LlamaModel(modelSet.bakeLayer(ModelLayers.LLAMA_BABY_DECOR));
/*    */   }
/*    */   private final LlamaModel babyModel; private final EquipmentLayerRenderer equipmentRenderer;
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LlamaRenderState state, float yRot, float xRot) {
/* 32 */     ItemStack itemStack = state.bodyItem;
/* 33 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 34 */     if (equippable != null && equippable.assetId().isPresent()) {
/* 35 */       renderEquipment(poseStack, submitNodeCollector, state, itemStack, equippable.assetId().get(), lightCoords);
/* 36 */     } else if (state.isTraderLlama) {
/* 37 */       renderEquipment(poseStack, submitNodeCollector, state, ItemStack.EMPTY, EquipmentAssets.TRADER_LLAMA, lightCoords);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void renderEquipment(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LlamaRenderState state, ItemStack itemStack, ResourceKey<EquipmentAsset> equipmentAssetId, int lightCoords) {
/* 42 */     LlamaModel model = state.isBaby ? this.babyModel : this.adultModel;
/* 43 */     this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.LLAMA_BODY, equipmentAssetId, (Model<? super LlamaRenderState>)model, state, itemStack, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/LlamaDecorLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */