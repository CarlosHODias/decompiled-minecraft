/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.equipment.ElytraModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class WingsLayer<S extends HumanoidRenderState, M extends EntityModel<S>>
/*    */   extends RenderLayer<S, M> {
/*    */   private final ElytraModel elytraModel;
/*    */   
/*    */   public WingsLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 26 */     super(renderer);
/* 27 */     this.elytraModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA));
/* 28 */     this.elytraBabyModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA_BABY));
/* 29 */     this.equipmentRenderer = equipmentRenderer;
/*    */   }
/*    */   private final ElytraModel elytraBabyModel; private final EquipmentLayerRenderer equipmentRenderer;
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 34 */     ItemStack itemStack = ((HumanoidRenderState)state).chestEquipment;
/* 35 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 36 */     if (equippable == null || equippable.assetId().isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     Identifier playerElytraTexture = getPlayerElytraTexture((HumanoidRenderState)state);
/* 41 */     ElytraModel model = ((HumanoidRenderState)state).isBaby ? this.elytraBabyModel : this.elytraModel;
/*    */     
/* 43 */     poseStack.pushPose();
/* 44 */     poseStack.translate(0.0F, 0.0F, 0.125F);
/*    */     
/* 46 */     this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.WINGS, equippable.assetId().get(), (Model<? super S>)model, state, itemStack, poseStack, submitNodeCollector, lightCoords, playerElytraTexture, ((HumanoidRenderState)state).outlineColor, 0);
/*    */     
/* 48 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   private static Identifier getPlayerElytraTexture(HumanoidRenderState state) {
/* 52 */     if (state instanceof AvatarRenderState) { AvatarRenderState playerState = (AvatarRenderState)state;
/* 53 */       PlayerSkin skin = playerState.skin;
/* 54 */       if (skin.elytra() != null)
/* 55 */         return skin.elytra().texturePath(); 
/* 56 */       if (skin.cape() != null && playerState.showCape) {
/* 57 */         return skin.cape().texturePath();
/*    */       } }
/*    */     
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/WingsLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */