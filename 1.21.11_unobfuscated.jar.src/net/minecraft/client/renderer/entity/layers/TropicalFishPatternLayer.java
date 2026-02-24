/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.fish.TropicalFishLargeModel;
/*    */ import net.minecraft.client.model.animal.fish.TropicalFishSmallModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TropicalFishRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*    */ 
/*    */ public class TropicalFishPatternLayer extends RenderLayer<TropicalFishRenderState, EntityModel<TropicalFishRenderState>> {
/* 16 */   private static final Identifier KOB_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a_pattern_1.png");
/* 17 */   private static final Identifier SUNSTREAK_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a_pattern_2.png");
/* 18 */   private static final Identifier SNOOPER_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a_pattern_3.png");
/* 19 */   private static final Identifier DASHER_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a_pattern_4.png");
/* 20 */   private static final Identifier BRINELY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a_pattern_5.png");
/* 21 */   private static final Identifier SPOTTY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a_pattern_6.png");
/*    */   
/* 23 */   private static final Identifier FLOPPER_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b_pattern_1.png");
/* 24 */   private static final Identifier STRIPEY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b_pattern_2.png");
/* 25 */   private static final Identifier GLITTER_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b_pattern_3.png");
/* 26 */   private static final Identifier BLOCKFISH_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b_pattern_4.png");
/* 27 */   private static final Identifier BETTY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b_pattern_5.png");
/* 28 */   private static final Identifier CLAYFISH_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b_pattern_6.png");
/*    */   
/*    */   private final TropicalFishSmallModel modelSmall;
/*    */   private final TropicalFishLargeModel modelLarge;
/*    */   
/*    */   public TropicalFishPatternLayer(RenderLayerParent<TropicalFishRenderState, EntityModel<TropicalFishRenderState>> renderer, EntityModelSet modelSet) {
/* 34 */     super(renderer);
/* 35 */     this.modelSmall = new TropicalFishSmallModel(modelSet.bakeLayer(ModelLayers.TROPICAL_FISH_SMALL_PATTERN));
/* 36 */     this.modelLarge = new TropicalFishLargeModel(modelSet.bakeLayer(ModelLayers.TROPICAL_FISH_LARGE_PATTERN));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, TropicalFishRenderState state, float yRot, float xRot) {
/* 41 */     TropicalFish.Pattern variant = state.pattern;
/* 42 */     switch (variant.base()) { default: throw new MatchException(null, null);
/*    */       case SMALL: 
/* 44 */       case LARGE: break; }  TropicalFishLargeModel tropicalFishLargeModel = this.modelLarge;
/*    */     
/* 46 */     switch (variant) { default: throw new MatchException(null, null);
/*    */       case KOB: 
/*    */       case SUNSTREAK: 
/*    */       case SNOOPER: 
/*    */       case DASHER: 
/*    */       case BRINELY: 
/*    */       case SPOTTY: 
/*    */       case FLOPPER: 
/*    */       case STRIPEY: 
/*    */       case GLITTER: 
/*    */       case BLOCKFISH: 
/*    */       case BETTY: 
/*    */       case CLAYFISH:
/* 59 */         break; }  Identifier patternTexture = CLAYFISH_TEXTURE;
/*    */     
/* 61 */     coloredCutoutModelCopyLayerRender((net.minecraft.client.model.Model<? super TropicalFishRenderState>)tropicalFishLargeModel, patternTexture, poseStack, submitNodeCollector, lightCoords, state, state.patternColor, 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/TropicalFishPatternLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */