/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EnchantTableRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class EnchantTableRenderer implements BlockEntityRenderer<EnchantingTableBlockEntity, EnchantTableRenderState> {
/* 24 */   public static final Material BOOK_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.defaultNamespaceApply("enchanting_table_book");
/*    */   
/*    */   private final MaterialSet materials;
/*    */   private final BookModel bookModel;
/*    */   
/*    */   public EnchantTableRenderer(BlockEntityRendererProvider.Context context) {
/* 30 */     this.materials = context.materials();
/* 31 */     this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public EnchantTableRenderState createRenderState() {
/* 36 */     return new EnchantTableRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(EnchantingTableBlockEntity blockEntity, EnchantTableRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 41 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 42 */     state.flip = Mth.lerp(partialTicks, blockEntity.oFlip, blockEntity.flip);
/* 43 */     state.open = Mth.lerp(partialTicks, blockEntity.oOpen, blockEntity.open);
/* 44 */     state.time = blockEntity.time + partialTicks;
/*    */     
/* 46 */     float or = blockEntity.rot - blockEntity.oRot;
/*    */     
/* 48 */     while (or >= 3.1415927F) {
/* 49 */       or -= 6.2831855F;
/*    */     }
/* 51 */     while (or < -3.1415927F) {
/* 52 */       or += 6.2831855F;
/*    */     }
/*    */     
/* 55 */     state.yRot = blockEntity.oRot + or * partialTicks;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EnchantTableRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 60 */     poseStack.pushPose();
/* 61 */     poseStack.translate(0.5F, 0.75F, 0.5F);
/*    */     
/* 63 */     poseStack.translate(0.0F, 0.1F + Mth.sin((state.time * 0.1F)) * 0.01F, 0.0F);
/*    */     
/* 65 */     float yRot = state.yRot;
/*    */     
/* 67 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotation(-yRot));
/* 68 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(80.0F));
/*    */     
/* 70 */     float ff1 = Mth.frac(state.flip + 0.25F) * 1.6F - 0.3F;
/* 71 */     float ff2 = Mth.frac(state.flip + 0.75F) * 1.6F - 0.3F;
/*    */     
/* 73 */     BookModel.State bookState = new BookModel.State(state.time, Mth.clamp(ff1, 0.0F, 1.0F), Mth.clamp(ff2, 0.0F, 1.0F), state.open);
/* 74 */     submitNodeCollector.submitModel((Model)this.bookModel, bookState, poseStack, BOOK_TEXTURE.renderType(RenderTypes::entitySolid), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, this.materials.get(BOOK_TEXTURE), 0, state.breakProgress);
/* 75 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/EnchantTableRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */