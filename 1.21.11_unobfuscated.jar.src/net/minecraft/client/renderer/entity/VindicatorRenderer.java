/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IllagerRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class VindicatorRenderer extends IllagerRenderer<net.minecraft.world.entity.monster.illager.Vindicator, IllagerRenderState> {
/* 13 */   private static final Identifier VINDICATOR = Identifier.withDefaultNamespace("textures/entity/illager/vindicator.png");
/*    */   
/*    */   public VindicatorRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new IllagerModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.VINDICATOR)), 0.5F);
/*    */     
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<IllagerRenderState, IllagerModel<IllagerRenderState>>)new ItemInHandLayer<IllagerRenderState, IllagerModel<IllagerRenderState>>(this, this)
/*    */         {
/*    */           public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllagerRenderState state, float yRot, float xRot) {
/* 21 */             if (state.isAggressive) {
/* 22 */               super.submit(poseStack, submitNodeCollector, lightCoords, (ArmedEntityRenderState)state, yRot, xRot);
/*    */             }
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(IllagerRenderState state) {
/* 30 */     return VINDICATOR;
/*    */   }
/*    */ 
/*    */   
/*    */   public IllagerRenderState createRenderState() {
/* 35 */     return new IllagerRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/VindicatorRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */