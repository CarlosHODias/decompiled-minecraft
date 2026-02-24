/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.player.PlayerCapeModel;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class CapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
/*    */   private final HumanoidModel<AvatarRenderState> model;
/*    */   
/*    */   public CapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet, EquipmentAssetManager equipmentAssets) {
/* 26 */     super(renderer);
/* 27 */     this.model = (HumanoidModel<AvatarRenderState>)new PlayerCapeModel(modelSet.bakeLayer(ModelLayers.PLAYER_CAPE));
/* 28 */     this.equipmentAssets = equipmentAssets;
/*    */   }
/*    */   private final EquipmentAssetManager equipmentAssets;
/*    */   private boolean hasLayer(ItemStack itemStack, EquipmentClientInfo.LayerType layerType) {
/* 32 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 33 */     if (equippable == null || equippable.assetId().isEmpty()) {
/* 34 */       return false;
/*    */     }
/* 36 */     EquipmentClientInfo equipmentClientInfo = this.equipmentAssets.get(equippable.assetId().get());
/* 37 */     return !equipmentClientInfo.getLayers(layerType).isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
/* 42 */     if (state.isInvisible || !state.showCape) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     PlayerSkin skin = state.skin;
/* 47 */     if (skin.cape() == null) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     if (hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     poseStack.pushPose();
/* 56 */     if (hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID)) {
/* 57 */       poseStack.translate(0.0F, -0.053125F, 0.06875F);
/*    */     }
/*    */     
/* 60 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, RenderTypes.entitySolid(skin.cape().texturePath()), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 62 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/CapeLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */