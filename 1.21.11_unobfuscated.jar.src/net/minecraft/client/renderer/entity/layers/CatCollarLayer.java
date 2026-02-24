/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.feline.CatModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.CatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class CatCollarLayer extends RenderLayer<CatRenderState, CatModel> {
/* 14 */   private static final Identifier CAT_COLLAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/cat/cat_collar.png");
/*    */   
/*    */   private final CatModel adultModel;
/*    */   private final CatModel babyModel;
/*    */   
/*    */   public CatCollarLayer(RenderLayerParent<CatRenderState, CatModel> renderer, EntityModelSet modelSet) {
/* 20 */     super(renderer);
/* 21 */     this.adultModel = new CatModel(modelSet.bakeLayer(ModelLayers.CAT_COLLAR));
/* 22 */     this.babyModel = new CatModel(modelSet.bakeLayer(ModelLayers.CAT_BABY_COLLAR));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CatRenderState state, float yRot, float xRot) {
/* 27 */     DyeColor collarColor = state.collarColor;
/* 28 */     if (collarColor == null) {
/*    */       return;
/*    */     }
/* 31 */     int color = collarColor.getTextureDiffuseColor();
/* 32 */     CatModel model = state.isBaby ? this.babyModel : this.adultModel;
/* 33 */     coloredCutoutModelCopyLayerRender((net.minecraft.client.model.Model<? super CatRenderState>)model, CAT_COLLAR_LOCATION, poseStack, submitNodeCollector, lightCoords, state, color, 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/CatCollarLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */