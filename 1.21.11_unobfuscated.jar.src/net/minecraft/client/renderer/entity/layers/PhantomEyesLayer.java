/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.monster.phantom.PhantomModel;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.PhantomRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PhantomEyesLayer extends EyesLayer<PhantomRenderState, PhantomModel> {
/* 11 */   private static final RenderType PHANTOM_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/phantom_eyes.png"));
/*    */   
/*    */   public PhantomEyesLayer(RenderLayerParent<PhantomRenderState, PhantomModel> renderer) {
/* 14 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderType renderType() {
/* 19 */     return PHANTOM_EYES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/PhantomEyesLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */