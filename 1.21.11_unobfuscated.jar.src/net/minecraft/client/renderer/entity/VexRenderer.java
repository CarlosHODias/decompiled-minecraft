/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.monster.vex.VexModel;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.VexRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Vex;
/*    */ 
/*    */ public class VexRenderer extends MobRenderer<Vex, VexRenderState, VexModel> {
/* 14 */   private static final Identifier VEX_LOCATION = Identifier.withDefaultNamespace("textures/entity/illager/vex.png");
/* 15 */   private static final Identifier VEX_CHARGING_LOCATION = Identifier.withDefaultNamespace("textures/entity/illager/vex_charging.png");
/*    */   
/*    */   public VexRenderer(EntityRendererProvider.Context context) {
/* 18 */     super(context, new VexModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.VEX)), 0.3F);
/* 19 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<VexRenderState, VexModel>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(Vex entity, BlockPos blockPos) {
/* 24 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(VexRenderState state) {
/* 29 */     if (state.isCharging) {
/* 30 */       return VEX_CHARGING_LOCATION;
/*    */     }
/* 32 */     return VEX_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public VexRenderState createRenderState() {
/* 37 */     return new VexRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Vex entity, VexRenderState state, float partialTicks) {
/* 42 */     super.extractRenderState(entity, state, partialTicks);
/* 43 */     ArmedEntityRenderState.extractArmedEntityRenderState((LivingEntity)entity, (ArmedEntityRenderState)state, this.itemModelResolver, partialTicks);
/* 44 */     state.isCharging = entity.isCharging();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/VexRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */