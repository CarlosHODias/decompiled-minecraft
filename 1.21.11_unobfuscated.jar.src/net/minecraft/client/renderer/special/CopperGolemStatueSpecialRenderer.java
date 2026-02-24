/*    */ package net.minecraft.client.renderer.special;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class CopperGolemStatueSpecialRenderer implements NoDataSpecialModelRenderer {
/* 23 */   private static final Direction MODEL_STATE = Direction.SOUTH;
/*    */   
/*    */   private final CopperGolemStatueModel model;
/*    */   private final Identifier texture;
/*    */   
/*    */   public CopperGolemStatueSpecialRenderer(CopperGolemStatueModel model, Identifier texture) {
/* 29 */     this.model = model;
/* 30 */     this.texture = texture;
/*    */   }
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked { private final Identifier texture; private final CopperGolemStatueBlock.Pose pose; public static final MapCodec<Unbaked> MAP_CODEC;
/* 33 */     public Unbaked(Identifier texture, CopperGolemStatueBlock.Pose pose) { this.texture = texture; this.pose = pose; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 33 */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked; } public Identifier texture() { return this.texture; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;
/* 33 */       //   0	8	1	o	Ljava/lang/Object; } public CopperGolemStatueBlock.Pose pose() { return this.pose; } static {
/* 34 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)CopperGolemStatueBlock.Pose.CODEC.fieldOf("pose").forGetter(Unbaked::pose)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Unbaked(WeatheringCopper.WeatherState state, CopperGolemStatueBlock.Pose pose) {
/* 40 */       this(CopperGolemOxidationLevels.getOxidationLevel(state).texture(), pose);
/*    */     }
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 45 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
/* 50 */       CopperGolemStatueModel model = new CopperGolemStatueModel(context.entityModelSet().bakeLayer(getModel(this.pose)));
/* 51 */       return new CopperGolemStatueSpecialRenderer(model, this.texture);
/*    */     }
/*    */     
/*    */     private static ModelLayerLocation getModel(CopperGolemStatueBlock.Pose pose) {
/* 55 */       switch (pose) { default: throw new MatchException(null, null);case STANDING: case SITTING: case STAR: case RUNNING: break; }  return 
/*    */ 
/*    */ 
/*    */         
/* 59 */         ModelLayers.COPPER_GOLEM_RUNNING;
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 66 */     positionModel(poseStack);
/* 67 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, Direction.SOUTH, poseStack, RenderTypes.entityCutoutNoCull(this.texture), lightCoords, overlayCoords, -1, null, outlineColor, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 72 */     PoseStack poseStack = new PoseStack();
/* 73 */     positionModel(poseStack);
/* 74 */     this.model.setupAnim(MODEL_STATE);
/* 75 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */   
/*    */   private static void positionModel(PoseStack poseStack) {
/* 79 */     poseStack.translate(0.5F, 1.5F, 0.5F);
/* 80 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */