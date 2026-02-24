/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.golem.SnowGolemModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*    */ 
/*    */ public class SnowGolemRenderer extends MobRenderer<SnowGolem, SnowGolemRenderState, SnowGolemModel> {
/* 11 */   private static final Identifier SNOW_GOLEM_LOCATION = Identifier.withDefaultNamespace("textures/entity/snow_golem.png");
/*    */   
/*    */   public SnowGolemRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, new SnowGolemModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SNOW_GOLEM)), 0.5F);
/*    */     
/* 16 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<SnowGolemRenderState, SnowGolemModel>)new net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer(this, context.getBlockRenderDispatcher()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SnowGolemRenderState state) {
/* 21 */     return SNOW_GOLEM_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SnowGolemRenderState createRenderState() {
/* 26 */     return new SnowGolemRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(SnowGolem entity, SnowGolemRenderState state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     state.hasPumpkin = entity.hasPumpkin();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SnowGolemRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */