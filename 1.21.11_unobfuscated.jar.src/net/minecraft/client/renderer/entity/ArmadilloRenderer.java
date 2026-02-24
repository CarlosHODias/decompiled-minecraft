/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.armadillo.ArmadilloModel;
/*    */ import net.minecraft.client.renderer.entity.state.ArmadilloRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*    */ 
/*    */ public class ArmadilloRenderer extends AgeableMobRenderer<Armadillo, ArmadilloRenderState, ArmadilloModel> {
/* 10 */   private static final Identifier ARMADILLO_LOCATION = Identifier.withDefaultNamespace("textures/entity/armadillo.png");
/*    */   
/*    */   public ArmadilloRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new ArmadilloModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.ARMADILLO)), new ArmadilloModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.ARMADILLO_BABY)), 0.4F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ArmadilloRenderState state) {
/* 18 */     return ARMADILLO_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public ArmadilloRenderState createRenderState() {
/* 23 */     return new ArmadilloRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Armadillo entity, ArmadilloRenderState state, float partialTicks) {
/* 28 */     super.extractRenderState(entity, state, partialTicks);
/* 29 */     state.isHidingInShell = entity.shouldHideInShell();
/* 30 */     state.peekAnimationState.copyFrom(entity.peekAnimationState);
/* 31 */     state.rollOutAnimationState.copyFrom(entity.rollOutAnimationState);
/* 32 */     state.rollUpAnimationState.copyFrom(entity.rollUpAnimationState);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ArmadilloRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */