/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.object.boat.RaftModel;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class RaftRenderer extends AbstractBoatRenderer {
/*    */   private final EntityModel<BoatRenderState> model;
/*    */   private final Identifier texture;
/*    */   
/*    */   public RaftRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
/* 15 */     super(context);
/* 16 */     this.texture = modelId.model().withPath(p -> "textures/entity/" + p + ".png");
/* 17 */     this.model = (EntityModel<BoatRenderState>)new RaftModel(context.bakeLayer(modelId));
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityModel<BoatRenderState> model() {
/* 22 */     return this.model;
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderType renderType() {
/* 27 */     return this.model.renderType(this.texture);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/RaftRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */