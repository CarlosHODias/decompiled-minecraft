/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.CampfireRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.CampfireBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CampfireRenderer implements BlockEntityRenderer<CampfireBlockEntity, CampfireRenderState> {
/*    */   private static final float SIZE = 0.375F;
/*    */   
/*    */   public CampfireRenderer(BlockEntityRendererProvider.Context context) {
/* 29 */     this.itemModelResolver = context.itemModelResolver();
/*    */   }
/*    */   private final ItemModelResolver itemModelResolver;
/*    */   
/*    */   public CampfireRenderState createRenderState() {
/* 34 */     return new CampfireRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(CampfireBlockEntity blockEntity, CampfireRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 39 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 40 */     state.facing = (Direction)blockEntity.getBlockState().getValue((Property)CampfireBlock.FACING);
/*    */     
/* 42 */     int seed = (int)blockEntity.getBlockPos().asLong();
/* 43 */     state.items = new ArrayList();
/* 44 */     for (int slot = 0; slot < blockEntity.getItems().size(); slot++) {
/* 45 */       ItemStackRenderState itemState = new ItemStackRenderState();
/* 46 */       this.itemModelResolver.updateForTopItem(itemState, (ItemStack)blockEntity.getItems().get(slot), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + slot);
/* 47 */       state.items.add(itemState);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(CampfireRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 53 */     Direction facing = state.facing;
/*    */     
/* 55 */     List<ItemStackRenderState> items = state.items;
/* 56 */     for (int slot = 0; slot < items.size(); slot++) {
/* 57 */       ItemStackRenderState itemState = items.get(slot);
/* 58 */       if (!itemState.isEmpty()) {
/*    */ 
/*    */         
/* 61 */         poseStack.pushPose();
/* 62 */         poseStack.translate(0.5F, 0.44921875F, 0.5F);
/*    */         
/* 64 */         Direction direction = Direction.from2DDataValue((slot + facing.get2DDataValue()) % 4);
/* 65 */         float angle = -direction.toYRot();
/* 66 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(angle));
/* 67 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0F));
/* 68 */         poseStack.translate(-0.3125F, -0.3125F, 0.0F);
/* 69 */         poseStack.scale(0.375F, 0.375F, 0.375F);
/*    */         
/* 71 */         itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
/* 72 */         poseStack.popPose();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/CampfireRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */