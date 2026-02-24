/*    */ package net.minecraft.client.renderer.special;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.chest.ChestModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ChestSpecialRenderer implements NoDataSpecialModelRenderer {
/* 22 */   public static final Identifier GIFT_CHEST_TEXTURE = Identifier.withDefaultNamespace("christmas");
/* 23 */   public static final Identifier NORMAL_CHEST_TEXTURE = Identifier.withDefaultNamespace("normal");
/* 24 */   public static final Identifier TRAPPED_CHEST_TEXTURE = Identifier.withDefaultNamespace("trapped");
/* 25 */   public static final Identifier ENDER_CHEST_TEXTURE = Identifier.withDefaultNamespace("ender");
/* 26 */   public static final Identifier COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper");
/* 27 */   public static final Identifier EXPOSED_COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper_exposed");
/* 28 */   public static final Identifier WEATHERED_COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper_weathered");
/* 29 */   public static final Identifier OXIDIZED_COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper_oxidized");
/*    */   
/*    */   private final MaterialSet materials;
/*    */   private final ChestModel model;
/*    */   private final Material material;
/*    */   private final float openness;
/*    */   
/*    */   public ChestSpecialRenderer(MaterialSet materials, ChestModel model, Material material, float openness) {
/* 37 */     this.materials = materials;
/* 38 */     this.model = model;
/* 39 */     this.material = material;
/* 40 */     this.openness = openness;
/*    */   }
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { private final Identifier texture; private final float openness; public static final MapCodec<Unbaked> MAP_CODEC;
/* 43 */     public Unbaked(Identifier texture, float openness) { this.texture = texture; this.openness = openness; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 43 */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked; } public Identifier texture() { return this.texture; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;
/* 43 */       //   0	8	1	o	Ljava/lang/Object; } public float openness() { return this.openness; } static {
/* 44 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(Unbaked::openness)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Unbaked(Identifier texture) {
/* 51 */       this(texture, 0.0F);
/*    */     }
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 56 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 61 */       ChestModel model = new ChestModel(context.entityModelSet().bakeLayer(ModelLayers.CHEST));
/* 62 */       Material fullTexture = Sheets.CHEST_MAPPER.apply(this.texture);
/*    */       
/* 64 */       return new ChestSpecialRenderer(context.materials(), model, fullTexture, this.openness);
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 70 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, this.openness, poseStack, this.material.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, this.materials.get(this.material), outlineColor, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 75 */     PoseStack poseStack = new PoseStack();
/* 76 */     this.model.setupAnim(this.openness);
/* 77 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/ChestSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */