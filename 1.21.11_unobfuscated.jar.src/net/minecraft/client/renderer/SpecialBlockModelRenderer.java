/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderers;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class SpecialBlockModelRenderer
/*    */ {
/* 12 */   public static final SpecialBlockModelRenderer EMPTY = new SpecialBlockModelRenderer(Map.of());
/*    */   
/*    */   private final Map<Block, SpecialModelRenderer<?>> renderers;
/*    */   
/*    */   public SpecialBlockModelRenderer(Map<Block, SpecialModelRenderer<?>> renderers) {
/* 17 */     this.renderers = renderers;
/*    */   }
/*    */   
/*    */   public static SpecialBlockModelRenderer vanilla(SpecialModelRenderer.BakingContext context) {
/* 21 */     return new SpecialBlockModelRenderer(SpecialModelRenderers.createBlockRenderers(context));
/*    */   }
/*    */   
/*    */   public void renderByBlock(Block block, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 25 */     SpecialModelRenderer<?> specialRenderer = this.renderers.get(block);
/* 26 */     if (specialRenderer != null)
/* 27 */       specialRenderer.submit(null, type, poseStack, submitNodeCollector, lightCoords, overlayCoords, false, outlineColor); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SpecialBlockModelRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */