/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.golem.IronGolemModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Crackiness;
/*    */ 
/*    */ public class IronGolemCrackinessLayer extends RenderLayer<IronGolemRenderState, IronGolemModel> {
/* 16 */   private static final Map<Crackiness.Level, Identifier> identifiers = (Map<Crackiness.Level, Identifier>)ImmutableMap.of(Crackiness.Level.LOW, 
/* 17 */       Identifier.withDefaultNamespace("textures/entity/iron_golem/iron_golem_crackiness_low.png"), Crackiness.Level.MEDIUM, 
/* 18 */       Identifier.withDefaultNamespace("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), Crackiness.Level.HIGH, 
/* 19 */       Identifier.withDefaultNamespace("textures/entity/iron_golem/iron_golem_crackiness_high.png"));
/*    */ 
/*    */   
/*    */   public IronGolemCrackinessLayer(RenderLayerParent<IronGolemRenderState, IronGolemModel> renderer) {
/* 23 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IronGolemRenderState state, float yRot, float xRot) {
/* 28 */     if (state.isInvisible) {
/*    */       return;
/*    */     }
/* 31 */     Crackiness.Level crackiness = state.crackiness;
/* 32 */     if (crackiness == Crackiness.Level.NONE) {
/*    */       return;
/*    */     }
/* 35 */     Identifier damageTexture = identifiers.get(crackiness);
/* 36 */     renderColoredCutoutModel((Model<? super IronGolemRenderState>)getParentModel(), damageTexture, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/IronGolemCrackinessLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */