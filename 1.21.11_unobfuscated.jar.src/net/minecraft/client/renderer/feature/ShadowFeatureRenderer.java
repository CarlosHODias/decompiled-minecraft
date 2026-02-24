/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class ShadowFeatureRenderer
/*    */ {
/* 19 */   private static final RenderType SHADOW_RENDER_TYPE = RenderTypes.entityShadow(Identifier.withDefaultNamespace("textures/misc/shadow.png"));
/*    */   
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource) {
/* 22 */     VertexConsumer buffer = bufferSource.getBuffer(SHADOW_RENDER_TYPE);
/* 23 */     for (SubmitNodeStorage.ShadowSubmit submit : (Iterable<SubmitNodeStorage.ShadowSubmit>)nodeCollection.getShadowSubmits()) {
/* 24 */       for (EntityRenderState.ShadowPiece piece : (Iterable<EntityRenderState.ShadowPiece>)submit.pieces()) {
/*    */         
/* 26 */         AABB aabb = piece.shapeBelow().bounds();
/*    */         
/* 28 */         float x01 = piece.relativeX() + (float)aabb.minX;
/* 29 */         float x11 = piece.relativeX() + (float)aabb.maxX;
/* 30 */         float y01 = piece.relativeY() + (float)aabb.minY;
/* 31 */         float z01 = piece.relativeZ() + (float)aabb.minZ;
/* 32 */         float z11 = piece.relativeZ() + (float)aabb.maxZ;
/*    */         
/* 34 */         float radius = submit.radius();
/* 35 */         float u0 = -x01 / 2.0F / radius + 0.5F;
/* 36 */         float u1 = -x11 / 2.0F / radius + 0.5F;
/* 37 */         float v0 = -z01 / 2.0F / radius + 0.5F;
/* 38 */         float v1 = -z11 / 2.0F / radius + 0.5F;
/*    */         
/* 40 */         int color = ARGB.white(piece.alpha());
/* 41 */         shadowVertex(submit.pose(), buffer, color, x01, y01, z01, u0, v0);
/* 42 */         shadowVertex(submit.pose(), buffer, color, x01, y01, z11, u0, v1);
/* 43 */         shadowVertex(submit.pose(), buffer, color, x11, y01, z11, u1, v1);
/* 44 */         shadowVertex(submit.pose(), buffer, color, x11, y01, z01, u1, v0);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void shadowVertex(Matrix4f pose, VertexConsumer buffer, int color, float x, float y, float z, float u, float v) {
/* 50 */     Vector3f position = pose.transformPosition(x, y, z, new Vector3f());
/* 51 */     buffer.addVertex(position.x(), position.y(), position.z(), color, u, v, OverlayTexture.NO_OVERLAY, 15728880, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/ShadowFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */