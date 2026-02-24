/*    */ package net.minecraft.client.renderer.entity;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.animal.cow.CowModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*    */ 
/*    */ public class MushroomCowRenderer extends AgeableMobRenderer<MushroomCow, MushroomCowRenderState, CowModel> {
/*    */   static {
/* 15 */     TEXTURES = (Map<MushroomCow.Variant, Identifier>)net.minecraft.util.Util.make(com.google.common.collect.Maps.newHashMap(), map -> {
/*    */           map.put(MushroomCow.Variant.BROWN, Identifier.withDefaultNamespace("textures/entity/cow/brown_mooshroom.png"));
/*    */           map.put(MushroomCow.Variant.RED, Identifier.withDefaultNamespace("textures/entity/cow/red_mooshroom.png"));
/*    */         });
/*    */   } private static final Map<MushroomCow.Variant, Identifier> TEXTURES;
/*    */   public MushroomCowRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context, new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM)), new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM_BABY)), 0.7F);
/*    */     
/* 23 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<MushroomCowRenderState, CowModel>)new net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer(this, context.getBlockRenderDispatcher()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(MushroomCowRenderState state) {
/* 28 */     return TEXTURES.get(state.variant);
/*    */   }
/*    */ 
/*    */   
/*    */   public MushroomCowRenderState createRenderState() {
/* 33 */     return new MushroomCowRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(MushroomCow entity, MushroomCowRenderState state, float partialTicks) {
/* 38 */     super.extractRenderState(entity, state, partialTicks);
/* 39 */     state.variant = entity.getVariant();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/MushroomCowRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */