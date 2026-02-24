/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.equine.HorseModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HorseRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.equine.Markings;
/*    */ 
/*    */ public class HorseMarkingLayer extends RenderLayer<HorseRenderState, HorseModel> {
/* 18 */   private static final Identifier INVISIBLE_TEXTURE = Identifier.withDefaultNamespace("invisible");
/*    */   
/* 20 */   private static final Map<Markings, Identifier> TEXTURE_BY_MARKINGS = Maps.newEnumMap(Map.of(Markings.NONE, INVISIBLE_TEXTURE, Markings.WHITE, 
/*    */         
/* 22 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_white.png"), Markings.WHITE_FIELD, 
/* 23 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_whitefield.png"), Markings.WHITE_DOTS, 
/* 24 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_whitedots.png"), Markings.BLACK_DOTS, 
/* 25 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_blackdots.png")));
/*    */ 
/*    */   
/*    */   public HorseMarkingLayer(RenderLayerParent<HorseRenderState, HorseModel> renderer) {
/* 29 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HorseRenderState state, float yRot, float xRot) {
/* 34 */     Identifier texture = TEXTURE_BY_MARKINGS.get(state.markings);
/*    */     
/* 36 */     if (texture == INVISIBLE_TEXTURE || state.isInvisible) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     submitNodeCollector.order(1).submitModel((Model)getParentModel(), state, poseStack, RenderTypes.entityTranslucent(texture), lightCoords, LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F), -1, null, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/HorseMarkingLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */