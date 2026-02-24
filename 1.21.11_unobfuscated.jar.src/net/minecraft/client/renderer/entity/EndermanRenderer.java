/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.monster.enderman.EndermanModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EndermanRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.EnderMan;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EndermanRenderer extends MobRenderer<EnderMan, EndermanRenderState, EndermanModel<EndermanRenderState>> {
/* 14 */   private static final Identifier ENDERMAN_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderman/enderman.png");
/*    */   
/* 16 */   private final RandomSource random = RandomSource.create();
/*    */   
/*    */   public EndermanRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context, new EndermanModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.ENDERMAN)), 0.5F);
/*    */     
/* 21 */     addLayer((RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>>)new net.minecraft.client.renderer.entity.layers.EnderEyesLayer(this));
/* 22 */     addLayer((RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>>)new net.minecraft.client.renderer.entity.layers.CarriedBlockLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getRenderOffset(EndermanRenderState state) {
/* 27 */     Vec3 offset = super.getRenderOffset(state);
/* 28 */     if (state.isCreepy) {
/* 29 */       double d = 0.02D * state.scale;
/* 30 */       return offset.add(this.random.nextGaussian() * d, 0.0D, this.random.nextGaussian() * d);
/*    */     } 
/* 32 */     return offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(EndermanRenderState state) {
/* 37 */     return ENDERMAN_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public EndermanRenderState createRenderState() {
/* 42 */     return new EndermanRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(EnderMan entity, EndermanRenderState state, float partialTicks) {
/* 47 */     super.extractRenderState(entity, state, partialTicks);
/* 48 */     HumanoidMobRenderer.extractHumanoidRenderState((LivingEntity)entity, (net.minecraft.client.renderer.entity.state.HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/* 49 */     state.isCreepy = entity.isCreepy();
/* 50 */     state.carriedBlock = entity.getCarriedBlock();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EndermanRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */