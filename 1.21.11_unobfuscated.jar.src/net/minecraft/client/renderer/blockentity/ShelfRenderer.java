/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import it.unimi.dsi.fastutil.HashCommon;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.ShelfRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.ShelfBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.ShelfBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity, ShelfRenderState> {
/*    */   private static final float ITEM_SIZE = 0.25F;
/*    */   
/*    */   public ShelfRenderer(BlockEntityRendererProvider.Context context) {
/* 31 */     this.itemModelResolver = context.itemModelResolver();
/*    */   }
/*    */   private static final float ALIGN_ITEMS_TO_BOTTOM = -0.25F; private final ItemModelResolver itemModelResolver;
/*    */   
/*    */   public ShelfRenderState createRenderState() {
/* 36 */     return new ShelfRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ShelfBlockEntity blockEntity, ShelfRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 41 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 42 */     state.alignToBottom = blockEntity.getAlignItemsToBottom();
/*    */     
/* 44 */     NonNullList<ItemStack> items = blockEntity.getItems();
/* 45 */     int seed = HashCommon.long2int(blockEntity.getBlockPos().asLong());
/*    */     
/* 47 */     for (int slot = 0; slot < items.size(); slot++) {
/* 48 */       ItemStack itemStack = (ItemStack)items.get(slot);
/* 49 */       if (!itemStack.isEmpty()) {
/* 50 */         ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
/* 51 */         this.itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.ON_SHELF, blockEntity.level(), (ItemOwner)blockEntity, seed + slot);
/* 52 */         state.items[slot] = itemStackRenderState;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(ShelfRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 59 */     Direction direction = (Direction)state.blockState.getValue((Property)ShelfBlock.FACING);
/* 60 */     float yRot = direction.getAxis().isHorizontal() ? -direction.toYRot() : 180.0F;
/*    */     
/* 62 */     for (int slot = 0; slot < state.items.length; slot++) {
/* 63 */       ItemStackRenderState itemStackRenderState = state.items[slot];
/* 64 */       if (itemStackRenderState != null) {
/* 65 */         submitItem(state, itemStackRenderState, poseStack, submitNodeCollector, slot, yRot);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void submitItem(ShelfRenderState state, ItemStackRenderState itemStackRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int slot, float yRot) {
/* 71 */     float itemSlotPosition = (slot - 1) * 0.3125F;
/*    */     
/* 73 */     Vec3 itemOffset = new Vec3(itemSlotPosition, state.alignToBottom ? -0.25D : 0.0D, -0.25D);
/*    */     
/* 75 */     poseStack.pushPose();
/* 76 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 77 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot));
/* 78 */     poseStack.translate(itemOffset);
/* 79 */     poseStack.scale(0.25F, 0.25F, 0.25F);
/*    */     
/* 81 */     AABB box = itemStackRenderState.getModelBoundingBox();
/* 82 */     double offsetY = -box.minY;
/* 83 */     if (!state.alignToBottom) {
/* 84 */       offsetY += -(box.maxY - box.minY) / 2.0D;
/*    */     }
/* 86 */     poseStack.translate(0.0D, offsetY, 0.0D);
/*    */     
/* 88 */     itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
/* 89 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/ShelfRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */