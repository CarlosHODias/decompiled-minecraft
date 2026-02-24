/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.DrownedModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DrownedOuterLayer extends RenderLayer<ZombieRenderState, DrownedModel> {
/* 14 */   private static final Identifier DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
/*    */   
/*    */   private final DrownedModel model;
/*    */   private final DrownedModel babyModel;
/*    */   
/*    */   public DrownedOuterLayer(RenderLayerParent<ZombieRenderState, DrownedModel> renderer, EntityModelSet modelSet) {
/* 20 */     super(renderer);
/* 21 */     this.model = new DrownedModel(modelSet.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
/* 22 */     this.babyModel = new DrownedModel(modelSet.bakeLayer(ModelLayers.DROWNED_BABY_OUTER_LAYER));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ZombieRenderState state, float yRot, float xRot) {
/* 27 */     DrownedModel model = state.isBaby ? this.babyModel : this.model;
/* 28 */     coloredCutoutModelCopyLayerRender((Model<? super ZombieRenderState>)model, DROWNED_OUTER_LAYER_LOCATION, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/DrownedOuterLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */