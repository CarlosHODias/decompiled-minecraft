/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.wolf.WolfModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Crackiness;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class WolfArmorLayer
/*    */   extends RenderLayer<WolfRenderState, WolfModel> {
/*    */   private final WolfModel adultModel;
/*    */   private final WolfModel babyModel;
/*    */   private final EquipmentLayerRenderer equipmentRenderer;
/* 27 */   private static final Map<Crackiness.Level, Identifier> ARMOR_CRACK_LOCATIONS = Map.of(Crackiness.Level.LOW, 
/* 28 */       Identifier.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_low.png"), Crackiness.Level.MEDIUM, 
/* 29 */       Identifier.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_medium.png"), Crackiness.Level.HIGH, 
/* 30 */       Identifier.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_high.png"));
/*    */ 
/*    */   
/*    */   public WolfArmorLayer(RenderLayerParent<WolfRenderState, WolfModel> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 34 */     super(renderer);
/* 35 */     this.adultModel = new WolfModel(modelSet.bakeLayer(ModelLayers.WOLF_ARMOR));
/* 36 */     this.babyModel = new WolfModel(modelSet.bakeLayer(ModelLayers.WOLF_BABY_ARMOR));
/* 37 */     this.equipmentRenderer = equipmentRenderer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, WolfRenderState state, float yRot, float xRot) {
/* 42 */     ItemStack armorItem = state.bodyArmorItem;
/* 43 */     Equippable equippable = (Equippable)armorItem.get(DataComponents.EQUIPPABLE);
/* 44 */     if (equippable == null || equippable.assetId().isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     WolfModel model = state.isBaby ? this.babyModel : this.adultModel;
/* 49 */     this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.WOLF_BODY, equippable.assetId().get(), (Model<? super WolfRenderState>)model, state, armorItem, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
/*    */     
/* 51 */     maybeRenderCracks(poseStack, submitNodeCollector, lightCoords, armorItem, (Model<WolfRenderState>)model, state);
/*    */   }
/*    */   
/*    */   private void maybeRenderCracks(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemStack armorItem, Model<WolfRenderState> model, WolfRenderState state) {
/* 55 */     Crackiness.Level crackiness = Crackiness.WOLF_ARMOR.byDamage(armorItem);
/* 56 */     if (crackiness == Crackiness.Level.NONE) {
/*    */       return;
/*    */     }
/* 59 */     Identifier damageTexture = ARMOR_CRACK_LOCATIONS.get(crackiness);
/* 60 */     submitNodeCollector.submitModel(model, state, poseStack, RenderTypes.armorTranslucent(damageTexture), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/WolfArmorLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */