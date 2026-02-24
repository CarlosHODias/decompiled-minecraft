/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.endermite.EndermiteModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.Endermite;
/*    */ 
/*    */ public class EndermiteRenderer extends MobRenderer<Endermite, LivingEntityRenderState, EndermiteModel> {
/* 10 */   private static final Identifier ENDERMITE_LOCATION = Identifier.withDefaultNamespace("textures/entity/endermite.png");
/*    */   
/*    */   public EndermiteRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new EndermiteModel(context.bakeLayer(ModelLayers.ENDERMITE)), 0.3F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getFlipDegrees() {
/* 18 */     return 180.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 23 */     return ENDERMITE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public LivingEntityRenderState createRenderState() {
/* 28 */     return new LivingEntityRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EndermiteRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */