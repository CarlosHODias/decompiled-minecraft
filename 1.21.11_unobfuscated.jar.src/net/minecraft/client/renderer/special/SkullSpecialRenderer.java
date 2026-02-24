/*    */ package net.minecraft.client.renderer.special;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.object.skull.SkullModelBase;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.level.block.SkullBlock;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class SkullSpecialRenderer implements NoDataSpecialModelRenderer {
/*    */   private final SkullModelBase model;
/*    */   private final float animation;
/*    */   private final RenderType renderType;
/*    */   
/*    */   public SkullSpecialRenderer(SkullModelBase model, float animation, RenderType renderType) {
/* 26 */     this.model = model;
/* 27 */     this.animation = animation;
/* 28 */     this.renderType = renderType;
/*    */   }
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { private final SkullBlock.Type kind; private final Optional<Identifier> textureOverride; private final float animation; public static final MapCodec<Unbaked> MAP_CODEC;
/* 31 */     public Unbaked(SkullBlock.Type kind, Optional<Identifier> textureOverride, float animation) { this.kind = kind; this.textureOverride = textureOverride; this.animation = animation; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 31 */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked; } public SkullBlock.Type kind() { return this.kind; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;
/* 31 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> textureOverride() { return this.textureOverride; } public float animation() { return this.animation; } static {
/* 32 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SkullBlock.Type.CODEC.fieldOf("kind").forGetter(Unbaked::kind), (App)Identifier.CODEC.optionalFieldOf("texture").forGetter(Unbaked::textureOverride), (App)Codec.FLOAT.optionalFieldOf("animation", 0.0F).forGetter(Unbaked::animation)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Unbaked(SkullBlock.Type kind) {
/* 39 */       this(kind, Optional.empty(), 0.0F);
/*    */     }
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 44 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 49 */       SkullModelBase model = SkullBlockRenderer.createModel(context.entityModelSet(), this.kind);
/* 50 */       Identifier textureOverride = this.textureOverride.<Identifier>map(t -> t.withPath(())).orElse(null);
/*    */       
/* 52 */       if (model == null) {
/* 53 */         return null;
/*    */       }
/*    */       
/* 56 */       RenderType renderType = SkullBlockRenderer.getSkullRenderType(this.kind, textureOverride);
/* 57 */       return new SkullSpecialRenderer(model, this.animation, renderType);
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 63 */     SkullBlockRenderer.submitSkull(null, 180.0F, this.animation, poseStack, submitNodeCollector, lightCoords, this.model, this.renderType, outlineColor, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 68 */     PoseStack poseStack = new PoseStack();
/* 69 */     poseStack.translate(0.5F, 0.0F, 0.5F);
/* 70 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 71 */     SkullModelBase.State modelState = new SkullModelBase.State();
/* 72 */     modelState.animationPos = this.animation;
/* 73 */     modelState.yRot = 180.0F;
/* 74 */     this.model.setupAnim(modelState);
/* 75 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/SkullSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */