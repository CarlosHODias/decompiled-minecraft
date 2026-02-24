/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Model<S>
/*    */ {
/*    */   protected final ModelPart root;
/*    */   protected final Function<Identifier, RenderType> renderType;
/*    */   private final List<ModelPart> allParts;
/*    */   
/*    */   public Model(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 21 */     this.root = root;
/* 22 */     this.renderType = renderType;
/* 23 */     this.allParts = root.getAllParts();
/*    */   }
/*    */   
/*    */   public final RenderType renderType(Identifier texture) {
/* 27 */     return this.renderType.apply(texture);
/*    */   }
/*    */   
/*    */   public final void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords, int color) {
/* 31 */     root().render(poseStack, buffer, lightCoords, overlayCoords, color);
/*    */   }
/*    */   
/*    */   public final void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords) {
/* 35 */     renderToBuffer(poseStack, buffer, lightCoords, overlayCoords, -1);
/*    */   }
/*    */   
/*    */   public final ModelPart root() {
/* 39 */     return this.root;
/*    */   }
/*    */   
/*    */   public final List<ModelPart> allParts() {
/* 43 */     return this.allParts;
/*    */   }
/*    */   
/*    */   public void setupAnim(S state) {
/* 47 */     resetPose();
/*    */   }
/*    */   
/*    */   public final void resetPose() {
/* 51 */     for (ModelPart part : this.allParts)
/* 52 */       part.resetPose(); 
/*    */   }
/*    */   
/*    */   public static class Simple
/*    */     extends Model<Unit> {
/*    */     public Simple(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 58 */       super(root, renderType);
/*    */     }
/*    */     
/*    */     public void setupAnim(Unit state) {}
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/Model.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */