/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.bee.BeeStingerModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.entity.EntityRendererProvider;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class BeeStingerLayer<M extends PlayerModel> extends StuckInBodyLayer<M, Unit> {
/* 13 */   private static final Identifier BEE_STINGER_LOCATION = Identifier.withDefaultNamespace("textures/entity/bee/bee_stinger.png");
/*    */   
/*    */   public BeeStingerLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, EntityRendererProvider.Context context) {
/* 16 */     super(renderer, (Model<Unit>)new BeeStingerModel(context.bakeLayer(ModelLayers.BEE_STINGER)), Unit.INSTANCE, BEE_STINGER_LOCATION, StuckInBodyLayer.PlacementStyle.ON_SURFACE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int numStuck(AvatarRenderState state) {
/* 21 */     return state.stingerCount;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/BeeStingerLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */