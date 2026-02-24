/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.sheep.SheepFurModel;
/*    */ import net.minecraft.client.model.animal.sheep.SheepModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class SheepWoolUndercoatLayer extends RenderLayer<SheepRenderState, SheepModel> {
/* 16 */   private static final Identifier SHEEP_WOOL_UNDERCOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool_undercoat.png");
/*    */   
/*    */   private final EntityModel<SheepRenderState> adultModel;
/*    */   private final EntityModel<SheepRenderState> babyModel;
/*    */   
/*    */   public SheepWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
/* 22 */     super(renderer);
/* 23 */     this.adultModel = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT));
/* 24 */     this.babyModel = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_BABY_WOOL_UNDERCOAT));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
/* 29 */     if (state.isInvisible || (!state.isJebSheep && state.woolColor == DyeColor.WHITE)) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     EntityModel<SheepRenderState> model = state.isBaby ? this.babyModel : this.adultModel;
/* 34 */     coloredCutoutModelCopyLayerRender((net.minecraft.client.model.Model<? super SheepRenderState>)model, SHEEP_WOOL_UNDERCOAT_LOCATION, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/SheepWoolUndercoatLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */