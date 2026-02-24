/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.ConduitRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ConduitSpecialRenderer
/*    */   implements NoDataSpecialModelRenderer
/*    */ {
/*    */   private final MaterialSet materials;
/*    */   private final ModelPart model;
/*    */   
/*    */   public ConduitSpecialRenderer(MaterialSet materials, ModelPart model) {
/* 22 */     this.materials = materials;
/* 23 */     this.model = model;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked {
/* 27 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked; } public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;
/*    */     } public MapCodec<Unbaked> type() {
/* 31 */       return MAP_CODEC;
/*    */     } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 36 */       return new ConduitSpecialRenderer(context.materials(), context.entityModelSet().bakeLayer(ModelLayers.CONDUIT_SHELL));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 42 */     poseStack.pushPose();
/* 43 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 44 */     submitNodeCollector.submitModelPart(this.model, poseStack, ConduitRenderer.SHELL_TEXTURE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.materials.get(ConduitRenderer.SHELL_TEXTURE), false, false, -1, null, outlineColor);
/* 45 */     poseStack.popPose();
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 50 */     PoseStack poseStack = new PoseStack();
/* 51 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 52 */     this.model.getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/ConduitSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */