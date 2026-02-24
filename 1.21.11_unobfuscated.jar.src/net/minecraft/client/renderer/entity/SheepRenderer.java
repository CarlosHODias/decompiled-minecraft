/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.sheep.SheepModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ 
/*    */ public class SheepRenderer extends AgeableMobRenderer<Sheep, SheepRenderState, SheepModel> {
/* 12 */   private static final Identifier SHEEP_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep.png");
/*    */   
/*    */   public SheepRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new SheepModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SHEEP)), new SheepModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SHEEP_BABY)), 0.7F);
/*    */     
/* 17 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<SheepRenderState, SheepModel>)new net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer(this, context.getModelSet()));
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<SheepRenderState, SheepModel>)new net.minecraft.client.renderer.entity.layers.SheepWoolLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SheepRenderState state) {
/* 23 */     return SHEEP_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SheepRenderState createRenderState() {
/* 28 */     return new SheepRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Sheep entity, SheepRenderState state, float partialTicks) {
/* 33 */     super.extractRenderState(entity, state, partialTicks);
/* 34 */     state.headEatAngleScale = entity.getHeadEatAngleScale(partialTicks);
/* 35 */     state.headEatPositionScale = entity.getHeadEatPositionScale(partialTicks);
/* 36 */     state.isSheared = entity.isSheared();
/* 37 */     state.woolColor = entity.getColor();
/*    */     
/* 39 */     state.isJebSheep = checkMagicName((Entity)entity, "jeb_");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SheepRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */