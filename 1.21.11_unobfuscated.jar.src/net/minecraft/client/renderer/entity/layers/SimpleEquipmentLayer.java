/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class SimpleEquipmentLayer<S extends LivingEntityRenderState, RM extends EntityModel<? super S>, EM extends EntityModel<? super S>> extends RenderLayer<S, RM> {
/*    */   private final EquipmentLayerRenderer equipmentRenderer;
/*    */   private final EquipmentClientInfo.LayerType layer;
/*    */   private final Function<S, ItemStack> itemGetter;
/*    */   private final EM adultModel;
/*    */   private final EM babyModel;
/*    */   private final int order;
/*    */   
/*    */   public SimpleEquipmentLayer(RenderLayerParent<S, RM> renderer, EquipmentLayerRenderer equipmentRenderer, EquipmentClientInfo.LayerType layer, Function<S, ItemStack> itemGetter, EM adultModel, EM babyModel, int order) {
/* 25 */     super(renderer);
/* 26 */     this.equipmentRenderer = equipmentRenderer;
/* 27 */     this.layer = layer;
/* 28 */     this.itemGetter = itemGetter;
/* 29 */     this.adultModel = adultModel;
/* 30 */     this.babyModel = babyModel;
/* 31 */     this.order = order;
/*    */   }
/*    */   
/*    */   public SimpleEquipmentLayer(RenderLayerParent<S, RM> renderer, EquipmentLayerRenderer equipmentRenderer, EquipmentClientInfo.LayerType layer, Function<S, ItemStack> itemGetter, EM adultModel, EM babyModel) {
/* 35 */     this(renderer, equipmentRenderer, layer, itemGetter, adultModel, babyModel, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 40 */     ItemStack equipment = this.itemGetter.apply(state);
/* 41 */     Equippable equippable = (Equippable)equipment.get(DataComponents.EQUIPPABLE);
/* 42 */     if (equippable == null || equippable.assetId().isEmpty() || (((LivingEntityRenderState)state).isBaby && this.babyModel == null)) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     EM model = ((LivingEntityRenderState)state).isBaby ? this.babyModel : this.adultModel;
/* 47 */     this.equipmentRenderer.renderLayers(this.layer, equippable.assetId().get(), (Model<? super S>)model, state, equipment, poseStack, submitNodeCollector, lightCoords, null, ((LivingEntityRenderState)state).outlineColor, this.order);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/SimpleEquipmentLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */