/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.sheep.SheepFurModel;
/*    */ import net.minecraft.client.model.animal.sheep.SheepModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class SheepWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
/* 18 */   private static final Identifier SHEEP_WOOL_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool.png");
/*    */   
/*    */   private final EntityModel<SheepRenderState> adultModel;
/*    */   private final EntityModel<SheepRenderState> babyModel;
/*    */   
/*    */   public SheepWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
/* 24 */     super(renderer);
/* 25 */     this.adultModel = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL));
/* 26 */     this.babyModel = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_BABY_WOOL));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
/* 31 */     if (state.isSheared) {
/*    */       return;
/*    */     }
/*    */     
/* 35 */     EntityModel<SheepRenderState> model = state.isBaby ? this.babyModel : this.adultModel;
/*    */     
/* 37 */     if (state.isInvisible) {
/* 38 */       if (state.appearsGlowing()) {
/* 39 */         submitNodeCollector.submitModel((Model)model, state, poseStack, net.minecraft.client.renderer.rendertype.RenderTypes.outline(SHEEP_WOOL_LOCATION), lightCoords, LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F), -16777216, null, state.outlineColor, null);
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 45 */     coloredCutoutModelCopyLayerRender((Model<? super SheepRenderState>)model, SHEEP_WOOL_LOCATION, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/SheepWoolLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */