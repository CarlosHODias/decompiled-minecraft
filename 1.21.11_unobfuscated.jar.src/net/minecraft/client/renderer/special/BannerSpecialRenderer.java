/*    */ package net.minecraft.client.renderer.special;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class BannerSpecialRenderer implements SpecialModelRenderer<BannerPatternLayers> {
/*    */   private final BannerRenderer bannerRenderer;
/*    */   private final DyeColor baseColor;
/*    */   
/*    */   public BannerSpecialRenderer(DyeColor baseColor, BannerRenderer bannerRenderer) {
/* 24 */     this.bannerRenderer = bannerRenderer;
/* 25 */     this.baseColor = baseColor;
/*    */   }
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { private final DyeColor baseColor; public static final MapCodec<Unbaked> MAP_CODEC;
/* 28 */     public Unbaked(DyeColor baseColor) { this.baseColor = baseColor; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 28 */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked; } public DyeColor baseColor() { return this.baseColor; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;
/* 29 */       //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(Unbaked::baseColor)).apply((Applicative)i, Unbaked::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 35 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 40 */       return new BannerSpecialRenderer(this.baseColor, new BannerRenderer(context));
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public BannerPatternLayers extractArgument(ItemStack stack) {
/* 46 */     return (BannerPatternLayers)stack.get(DataComponents.BANNER_PATTERNS);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(BannerPatternLayers patterns, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 51 */     this.bannerRenderer.submitSpecial(poseStack, submitNodeCollector, lightCoords, overlayCoords, this.baseColor, Objects.<BannerPatternLayers>requireNonNullElse(patterns, BannerPatternLayers.EMPTY), outlineColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 56 */     this.bannerRenderer.getExtents(output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/BannerSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */