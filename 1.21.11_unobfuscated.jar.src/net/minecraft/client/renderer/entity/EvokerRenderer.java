/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EvokerRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.illager.SpellcasterIllager;
/*    */ 
/*    */ public class EvokerRenderer<T extends SpellcasterIllager> extends IllagerRenderer<T, EvokerRenderState> {
/* 13 */   private static final Identifier EVOKER_ILLAGER = Identifier.withDefaultNamespace("textures/entity/illager/evoker.png");
/*    */   
/*    */   public EvokerRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new IllagerModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.EVOKER)), 0.5F);
/*    */     
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<EvokerRenderState, IllagerModel<EvokerRenderState>>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer<EvokerRenderState, IllagerModel<EvokerRenderState>>(this, this)
/*    */         {
/*    */           public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, EvokerRenderState state, float yRot, float xRot) {
/* 21 */             if (state.isCastingSpell) {
/* 22 */               super.submit(poseStack, submitNodeCollector, lightCoords, (ArmedEntityRenderState)state, yRot, xRot);
/*    */             }
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(EvokerRenderState state) {
/* 30 */     return EVOKER_ILLAGER;
/*    */   }
/*    */ 
/*    */   
/*    */   public EvokerRenderState createRenderState() {
/* 35 */     return new EvokerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, EvokerRenderState state, float partialTicks) {
/* 40 */     super.extractRenderState(entity, state, partialTicks);
/* 41 */     state.isCastingSpell = entity.isCastingSpell();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EvokerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */