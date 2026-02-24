/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.equipment.ShieldModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.client.resources.model.ModelBakery;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ShieldSpecialRenderer
/*    */   implements SpecialModelRenderer<DataComponentMap>
/*    */ {
/*    */   private final MaterialSet materials;
/*    */   private final ShieldModel model;
/*    */   
/*    */   public ShieldSpecialRenderer(MaterialSet materials, ShieldModel model) {
/* 31 */     this.materials = materials;
/* 32 */     this.model = model;
/*    */   } public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;
/*    */     }
/* 36 */     public static final Unbaked INSTANCE = new Unbaked();
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;
/* 38 */       //   0	8	1	o	Ljava/lang/Object; } public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 42 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 47 */       return new ShieldSpecialRenderer(context.materials(), new ShieldModel(context.entityModelSet().bakeLayer(ModelLayers.SHIELD)));
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public DataComponentMap extractArgument(ItemStack stack) {
/* 53 */     return stack.immutableComponents();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(DataComponentMap components, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 58 */     BannerPatternLayers patterns = (components != null) ? (BannerPatternLayers)components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY) : BannerPatternLayers.EMPTY;
/* 59 */     DyeColor baseColor = (components != null) ? (DyeColor)components.get(DataComponents.BASE_COLOR) : null;
/* 60 */     boolean hasPatterns = (!patterns.layers().isEmpty() || baseColor != null);
/*    */     
/* 62 */     poseStack.pushPose();
/* 63 */     poseStack.scale(1.0F, -1.0F, -1.0F);
/*    */     
/* 65 */     Material base = hasPatterns ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
/* 66 */     submitNodeCollector.submitModelPart(this.model.handle(), poseStack, this.model.renderType(base.atlasLocation()), lightCoords, overlayCoords, this.materials.get(base), false, false, -1, null, outlineColor);
/*    */     
/* 68 */     if (hasPatterns) {
/* 69 */       BannerRenderer.submitPatterns(this.materials, poseStack, submitNodeCollector, lightCoords, overlayCoords, (Model)this.model, Unit.INSTANCE, base, false, Objects.<DyeColor>requireNonNullElse(baseColor, DyeColor.WHITE), patterns, hasFoil, null, outlineColor);
/*    */     } else {
/* 71 */       submitNodeCollector.submitModelPart(this.model.plate(), poseStack, this.model.renderType(base.atlasLocation()), lightCoords, overlayCoords, this.materials.get(base), false, hasFoil, -1, null, outlineColor);
/*    */     } 
/*    */     
/* 74 */     poseStack.popPose();
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 79 */     PoseStack poseStack = new PoseStack();
/* 80 */     poseStack.scale(1.0F, -1.0F, -1.0F);
/* 81 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/ShieldSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */