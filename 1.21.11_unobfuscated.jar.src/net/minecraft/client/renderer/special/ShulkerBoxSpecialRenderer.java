/*    */ package net.minecraft.client.renderer.special;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ShulkerBoxSpecialRenderer implements NoDataSpecialModelRenderer {
/*    */   private final ShulkerBoxRenderer shulkerBoxRenderer;
/*    */   private final float openness;
/*    */   private final Direction orientation;
/*    */   private final Material material;
/*    */   
/*    */   public ShulkerBoxSpecialRenderer(ShulkerBoxRenderer shulkerBoxRenderer, float openness, Direction orientation, Material material) {
/* 26 */     this.shulkerBoxRenderer = shulkerBoxRenderer;
/* 27 */     this.openness = openness;
/* 28 */     this.orientation = orientation;
/* 29 */     this.material = material;
/*    */   }
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { private final Identifier texture; private final float openness; private final Direction orientation; public static final MapCodec<Unbaked> MAP_CODEC;
/* 32 */     public Unbaked(Identifier texture, float openness, Direction orientation) { this.texture = texture; this.openness = openness; this.orientation = orientation; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 32 */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked; } public Identifier texture() { return this.texture; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;
/* 32 */       //   0	8	1	o	Ljava/lang/Object; } public float openness() { return this.openness; } public Direction orientation() { return this.orientation; } static {
/* 33 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(Unbaked::openness), (App)Direction.CODEC.optionalFieldOf("orientation", Direction.UP).forGetter(Unbaked::orientation)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Unbaked() {
/* 41 */       this(Identifier.withDefaultNamespace("shulker"), 0.0F, Direction.UP);
/*    */     }
/*    */     
/*    */     public Unbaked(DyeColor color) {
/* 45 */       this(Sheets.colorToShulkerMaterial(color), 0.0F, Direction.UP);
/*    */     }
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 50 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 55 */       return new ShulkerBoxSpecialRenderer(new ShulkerBoxRenderer(context), this.openness, this.orientation, Sheets.SHULKER_MAPPER.apply(this.texture));
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(net.minecraft.world.item.ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 61 */     this.shulkerBoxRenderer.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, this.orientation, this.openness, null, this.material, outlineColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 66 */     this.shulkerBoxRenderer.getExtents(this.orientation, this.openness, output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */