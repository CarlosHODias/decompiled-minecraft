/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public abstract class AbstractEndPortalRenderer<T extends TheEndPortalBlockEntity, S extends EndPortalRenderState> implements BlockEntityRenderer<T, S> {
/* 21 */   public static final Identifier END_SKY_LOCATION = Identifier.withDefaultNamespace("textures/environment/end_sky.png");
/* 22 */   public static final Identifier END_PORTAL_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_portal.png");
/*    */ 
/*    */   
/*    */   public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 26 */     super.extractRenderState((BlockEntity)blockEntity, (BlockEntityRenderState)state, partialTicks, cameraPosition, breakProgress);
/* 27 */     ((EndPortalRenderState)state).facesToShow.clear();
/* 28 */     for (Direction direction : Direction.values()) {
/* 29 */       if (blockEntity.shouldRenderFace(direction)) {
/* 30 */         ((EndPortalRenderState)state).facesToShow.add(direction);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 37 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType(), (pose1, buffer) -> renderCube(state.facesToShow, state.pose(), buffer));
/*    */   }
/*    */   
/*    */   private void renderCube(EnumSet<Direction> facesToShow, Matrix4f pose, VertexConsumer builder) {
/* 41 */     float offsetDown = getOffsetDown();
/* 42 */     float offsetUp = getOffsetUp();
/*    */     
/* 44 */     renderFace(facesToShow, pose, builder, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
/* 45 */     renderFace(facesToShow, pose, builder, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
/* 46 */     renderFace(facesToShow, pose, builder, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
/* 47 */     renderFace(facesToShow, pose, builder, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
/* 48 */     renderFace(facesToShow, pose, builder, 0.0F, 1.0F, offsetDown, offsetDown, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
/* 49 */     renderFace(facesToShow, pose, builder, 0.0F, 1.0F, offsetUp, offsetUp, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
/*    */   }
/*    */   
/*    */   private void renderFace(EnumSet<Direction> facesToShow, Matrix4f pose, VertexConsumer builder, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction face) {
/* 53 */     if (facesToShow.contains(face)) {
/* 54 */       builder.addVertex((Matrix4fc)pose, x1, y1, z1);
/* 55 */       builder.addVertex((Matrix4fc)pose, x2, y1, z2);
/* 56 */       builder.addVertex((Matrix4fc)pose, x2, y2, z3);
/* 57 */       builder.addVertex((Matrix4fc)pose, x1, y2, z4);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected float getOffsetUp() {
/* 62 */     return 0.75F;
/*    */   }
/*    */   
/*    */   protected float getOffsetDown() {
/* 66 */     return 0.375F;
/*    */   }
/*    */   
/*    */   protected RenderType renderType() {
/* 70 */     return RenderTypes.endPortal();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/AbstractEndPortalRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */