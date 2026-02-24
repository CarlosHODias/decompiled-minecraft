/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.blaze.BlazeModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.monster.Blaze;
/*    */ 
/*    */ public class BlazeRenderer extends MobRenderer<Blaze, LivingEntityRenderState, BlazeModel> {
/* 12 */   private static final Identifier BLAZE_LOCATION = Identifier.withDefaultNamespace("textures/entity/blaze.png");
/*    */   
/*    */   public BlazeRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new BlazeModel(context.bakeLayer(ModelLayers.BLAZE)), 0.5F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(Blaze entity, BlockPos blockPos) {
/* 20 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 25 */     return BLAZE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public LivingEntityRenderState createRenderState() {
/* 30 */     return new LivingEntityRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/BlazeRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */