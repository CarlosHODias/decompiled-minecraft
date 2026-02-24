/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ public class LivingEntityEmissiveLayer<S extends LivingEntityRenderState, M extends EntityModel<S>>
/*    */   extends RenderLayer<S, M> {
/*    */   private final Function<S, Identifier> textureProvider;
/*    */   private final AlphaFunction<S> alphaFunction;
/*    */   private final M model;
/*    */   private final Function<Identifier, RenderType> bufferProvider;
/*    */   private final boolean alwaysVisible;
/*    */   
/*    */   public LivingEntityEmissiveLayer(RenderLayerParent<S, M> renderer, Function<S, Identifier> textureProvider, AlphaFunction<S> alphaFunction, M model, Function<Identifier, RenderType> bufferProvider, boolean alwaysVisible) {
/* 25 */     super(renderer);
/* 26 */     this.textureProvider = textureProvider;
/* 27 */     this.alphaFunction = alphaFunction;
/* 28 */     this.model = model;
/* 29 */     this.bufferProvider = bufferProvider;
/* 30 */     this.alwaysVisible = alwaysVisible;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 35 */     if (((LivingEntityRenderState)state).isInvisible && !this.alwaysVisible) {
/*    */       return;
/*    */     }
/* 38 */     float alpha = this.alphaFunction.apply(state, ((LivingEntityRenderState)state).ageInTicks);
/* 39 */     if (alpha <= 1.0E-5F) {
/*    */       return;
/*    */     }
/* 42 */     int color = ARGB.white(alpha);
/* 43 */     RenderType renderType = this.bufferProvider.apply(this.textureProvider.apply(state));
/* 44 */     submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, renderType, lightCoords, LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F), color, null, ((LivingEntityRenderState)state).outlineColor, null);
/*    */   }
/*    */   
/*    */   public static interface AlphaFunction<S extends LivingEntityRenderState> {
/*    */     float apply(S param1S, float param1Float);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/LivingEntityEmissiveLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */