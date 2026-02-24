/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface NoDataSpecialModelRenderer
/*    */   extends SpecialModelRenderer<Void>
/*    */ {
/*    */   default Void extractArgument(ItemStack stack) {
/* 12 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   default void submit(Void argument, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 17 */     submit(type, poseStack, submitNodeCollector, lightCoords, overlayCoords, hasFoil, outlineColor);
/*    */   }
/*    */   
/*    */   void submit(ItemDisplayContext paramItemDisplayContext, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/NoDataSpecialModelRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */