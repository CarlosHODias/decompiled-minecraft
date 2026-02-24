/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.LecternBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.LecternBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class LecternRenderer implements BlockEntityRenderer<LecternBlockEntity, LecternRenderState> {
/*    */   private final MaterialSet materials;
/* 24 */   private final BookModel.State bookState = new BookModel.State(0.0F, 0.1F, 0.9F, 1.2F); private final BookModel bookModel;
/*    */   
/*    */   public LecternRenderer(BlockEntityRendererProvider.Context context) {
/* 27 */     this.materials = context.materials();
/* 28 */     this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public LecternRenderState createRenderState() {
/* 33 */     return new LecternRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(LecternBlockEntity blockEntity, LecternRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 38 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 39 */     state.hasBook = (Boolean)blockEntity.getBlockState().getValue((Property)LecternBlock.HAS_BOOK);
/* 40 */     state.yRot = ((Direction)blockEntity.getBlockState().getValue((Property)LecternBlock.FACING)).getClockWise().toYRot();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(LecternRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 45 */     if (!state.hasBook) {
/*    */       return;
/*    */     }
/*    */     
/* 49 */     poseStack.pushPose();
/* 50 */     poseStack.translate(0.5F, 1.0625F, 0.5F);
/*    */     
/* 52 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-state.yRot));
/* 53 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(67.5F));
/*    */     
/* 55 */     poseStack.translate(0.0F, -0.125F, 0.0F);
/*    */     
/* 57 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.bookModel, this.bookState, poseStack, EnchantTableRenderer.BOOK_TEXTURE.renderType(RenderTypes::entitySolid), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, this.materials.get(EnchantTableRenderer.BOOK_TEXTURE), 0, state.breakProgress);
/*    */     
/* 59 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/LecternRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */