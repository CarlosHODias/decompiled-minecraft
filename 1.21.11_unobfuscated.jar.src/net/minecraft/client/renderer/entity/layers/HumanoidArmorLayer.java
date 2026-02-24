/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class HumanoidArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
/*    */   private final ArmorModelSet<A> modelSet;
/*    */   
/*    */   public HumanoidArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 21 */     this(renderer, modelSet, modelSet, equipmentRenderer);
/*    */   }
/*    */   private final ArmorModelSet<A> babyModelSet; private final EquipmentLayerRenderer equipmentRenderer;
/*    */   public HumanoidArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, ArmorModelSet<A> babyModelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 25 */     super(renderer);
/* 26 */     this.modelSet = modelSet;
/* 27 */     this.babyModelSet = babyModelSet;
/* 28 */     this.equipmentRenderer = equipmentRenderer;
/*    */   }
/*    */   
/*    */   public static boolean shouldRender(ItemStack itemStack, EquipmentSlot slot) {
/* 32 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 33 */     return (equippable != null && shouldRender(equippable, slot));
/*    */   }
/*    */   
/*    */   private static boolean shouldRender(Equippable equippable, EquipmentSlot slot) {
/* 37 */     return (equippable.assetId().isPresent() && equippable.slot() == slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 42 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).chestEquipment, EquipmentSlot.CHEST, lightCoords, state);
/* 43 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).legsEquipment, EquipmentSlot.LEGS, lightCoords, state);
/* 44 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).feetEquipment, EquipmentSlot.FEET, lightCoords, state);
/* 45 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).headEquipment, EquipmentSlot.HEAD, lightCoords, state);
/*    */   }
/*    */   
/*    */   private void renderArmorPiece(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, S state) {
/* 49 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 50 */     if (equippable == null || !shouldRender(equippable, slot)) {
/*    */       return;
/*    */     }
/*    */     
/* 54 */     A model = getArmorModel(state, slot);
/*    */     
/* 56 */     EquipmentClientInfo.LayerType layerType = usesInnerModel(slot) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID;
/* 57 */     this.equipmentRenderer.renderLayers(layerType, equippable.assetId().orElseThrow(), (Model<? super S>)model, state, itemStack, poseStack, submitNodeCollector, lightCoords, ((HumanoidRenderState)state).outlineColor);
/*    */   }
/*    */   
/*    */   private A getArmorModel(S state, EquipmentSlot slot) {
/* 61 */     return (A)(((HumanoidRenderState)state).isBaby ? this.babyModelSet : this.modelSet).get(slot);
/*    */   }
/*    */   
/*    */   private boolean usesInnerModel(EquipmentSlot slot) {
/* 65 */     return (slot == EquipmentSlot.LEGS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */