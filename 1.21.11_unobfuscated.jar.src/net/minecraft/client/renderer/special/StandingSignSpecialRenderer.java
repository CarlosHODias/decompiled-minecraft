/*    */ package net.minecraft.client.renderer.special;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.SignRenderer;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.state.properties.WoodType;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class StandingSignSpecialRenderer implements NoDataSpecialModelRenderer {
/*    */   private final MaterialSet materials;
/*    */   private final Model.Simple model;
/*    */   private final Material material;
/*    */   
/*    */   public StandingSignSpecialRenderer(MaterialSet materials, Model.Simple model, Material material) {
/* 26 */     this.materials = materials;
/* 27 */     this.model = model;
/* 28 */     this.material = material;
/*    */   }
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { private final WoodType woodType; private final Optional<Identifier> texture; public static final MapCodec<Unbaked> MAP_CODEC;
/* 31 */     public Unbaked(WoodType woodType, Optional<Identifier> texture) { this.woodType = woodType; this.texture = texture; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/StandingSignSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 31 */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/StandingSignSpecialRenderer$Unbaked; } public WoodType woodType() { return this.woodType; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/StandingSignSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/StandingSignSpecialRenderer$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/StandingSignSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/StandingSignSpecialRenderer$Unbaked;
/* 31 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> texture() { return this.texture; } static {
/* 32 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(Unbaked::woodType), (App)Identifier.CODEC.optionalFieldOf("texture").forGetter(Unbaked::texture)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Unbaked(WoodType woodType) {
/* 38 */       this(woodType, Optional.empty());
/*    */     }
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 43 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 48 */       Model.Simple model = SignRenderer.createSignModel(context.entityModelSet(), this.woodType, true);
/* 49 */       java.util.Objects.requireNonNull(Sheets.SIGN_MAPPER); Material material = this.texture.<Material>map(Sheets.SIGN_MAPPER::apply).orElseGet(() -> Sheets.getSignMaterial(this.woodType));
/* 50 */       return new StandingSignSpecialRenderer(context.materials(), model, material);
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(net.minecraft.world.item.ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 56 */     SignRenderer.submitSpecial(this.materials, poseStack, submitNodeCollector, lightCoords, overlayCoords, this.model, this.material);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 61 */     PoseStack poseStack = new PoseStack();
/* 62 */     SignRenderer.applyInHandTransforms(poseStack);
/* 63 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/StandingSignSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */