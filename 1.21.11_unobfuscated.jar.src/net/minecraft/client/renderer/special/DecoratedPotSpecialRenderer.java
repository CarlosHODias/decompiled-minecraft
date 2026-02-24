/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.PotDecorations;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class DecoratedPotSpecialRenderer
/*    */   implements SpecialModelRenderer<PotDecorations>
/*    */ {
/*    */   private final DecoratedPotRenderer decoratedPotRenderer;
/*    */   
/*    */   public DecoratedPotSpecialRenderer(DecoratedPotRenderer decoratedPotRenderer) {
/* 21 */     this.decoratedPotRenderer = decoratedPotRenderer;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked {
/* 25 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/DecoratedPotSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/DecoratedPotSpecialRenderer$Unbaked; } public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/DecoratedPotSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/DecoratedPotSpecialRenderer$Unbaked;
/*    */     } public MapCodec<Unbaked> type() {
/* 29 */       return MAP_CODEC;
/*    */     } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/DecoratedPotSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/DecoratedPotSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 34 */       return new DecoratedPotSpecialRenderer(new DecoratedPotRenderer(context));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public PotDecorations extractArgument(ItemStack stack) {
/* 40 */     return (PotDecorations)stack.get(DataComponents.POT_DECORATIONS);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PotDecorations decorations, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 45 */     this.decoratedPotRenderer.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, Objects.<PotDecorations>requireNonNullElse(decorations, PotDecorations.EMPTY), outlineColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 50 */     this.decoratedPotRenderer.getExtents(output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/DecoratedPotSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */