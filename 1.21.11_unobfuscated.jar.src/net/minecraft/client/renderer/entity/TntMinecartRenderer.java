/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartTntRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TntMinecartRenderer extends AbstractMinecartRenderer<MinecartTNT, MinecartTntRenderState> {
/*    */   public TntMinecartRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, net.minecraft.client.model.geom.ModelLayers.TNT_MINECART);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void submitMinecartContents(MinecartTntRenderState state, BlockState blockState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 20 */     float fuse = state.fuseRemainingInTicks;
/*    */     
/* 22 */     if (fuse > -1.0F && fuse < 10.0F) {
/* 23 */       float g = 1.0F - fuse / 10.0F;
/* 24 */       g = net.minecraft.util.Mth.clamp(g, 0.0F, 1.0F);
/* 25 */       g *= g;
/* 26 */       g *= g;
/* 27 */       float s = 1.0F + g * 0.3F;
/* 28 */       poseStack.scale(s, s, s);
/*    */     } 
/*    */     
/* 31 */     submitWhiteSolidBlock(blockState, poseStack, submitNodeCollector, lightCoords, (fuse > -1.0F && (int)fuse / 5 % 2 == 0), state.outlineColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void submitWhiteSolidBlock(BlockState blockState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, boolean white, int outlineColor) {
/*    */     int overlayCoords;
/* 37 */     if (white) {
/* 38 */       overlayCoords = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
/*    */     } else {
/* 40 */       overlayCoords = OverlayTexture.NO_OVERLAY;
/*    */     } 
/*    */     
/* 43 */     submitNodeCollector.submitBlock(poseStack, blockState, lightCoords, overlayCoords, outlineColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecartTntRenderState createRenderState() {
/* 48 */     return new MinecartTntRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(MinecartTNT entity, MinecartTntRenderState state, float partialTicks) {
/* 53 */     super.extractRenderState(entity, state, partialTicks);
/*    */ 
/*    */     
/* 56 */     state.fuseRemainingInTicks = (entity.getFuse() > -1) ? (entity.getFuse() - partialTicks + 1.0F) : -1.0F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/TntMinecartRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */