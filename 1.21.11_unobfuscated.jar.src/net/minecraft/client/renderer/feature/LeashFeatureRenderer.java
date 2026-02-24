/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.LightTexture;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class LeashFeatureRenderer {
/*    */   private static final int LEASH_RENDER_STEPS = 24;
/*    */   
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource) {
/* 18 */     for (SubmitNodeStorage.LeashSubmit leashSubmit : (Iterable<SubmitNodeStorage.LeashSubmit>)nodeCollection.getLeashSubmits())
/* 19 */       renderLeash(leashSubmit.pose(), (MultiBufferSource)bufferSource, leashSubmit.leashState()); 
/*    */   }
/*    */   private static final float LEASH_WIDTH = 0.05F;
/*    */   
/*    */   private static void renderLeash(Matrix4f pose, MultiBufferSource bufferSource, EntityRenderState.LeashState leashState) {
/* 24 */     float dx = (float)(leashState.end.x - leashState.start.x);
/* 25 */     float dy = (float)(leashState.end.y - leashState.start.y);
/* 26 */     float dz = (float)(leashState.end.z - leashState.start.z);
/*    */     
/* 28 */     float offsetFactor = Mth.invSqrt(dx * dx + dz * dz) * 0.05F / 2.0F;
/*    */ 
/*    */     
/* 31 */     float dxOff = dz * offsetFactor;
/* 32 */     float dzOff = dx * offsetFactor;
/*    */     
/* 34 */     pose.translate((float)leashState.offset.x, (float)leashState.offset.y, (float)leashState.offset.z);
/*    */     
/* 36 */     VertexConsumer builder = bufferSource.getBuffer(RenderTypes.leash());
/*    */     
/* 38 */     for (int k = 0; k <= 24; k++) {
/* 39 */       addVertexPair(builder, pose, dx, dy, dz, 0.05F, dxOff, dzOff, k, false, leashState);
/*    */     }
/* 41 */     for (int i = 24; i >= 0; i--)
/* 42 */       addVertexPair(builder, pose, dx, dy, dz, 0.0F, dxOff, dzOff, i, true, leashState); 
/*    */   }
/*    */   
/*    */   private static void addVertexPair(VertexConsumer builder, Matrix4f pose, float dx, float dy, float dz, float fudge, float dxOff, float dzOff, int k, boolean backwards, EntityRenderState.LeashState state) {
/*    */     float y;
/* 47 */     float progress = k / 24.0F;
/* 48 */     int block = (int)Mth.lerp(progress, state.startBlockLight, state.endBlockLight);
/* 49 */     int sky = (int)Mth.lerp(progress, state.startSkyLight, state.endSkyLight);
/* 50 */     int lightCoords = LightTexture.pack(block, sky);
/*    */     
/* 52 */     float colorModifier = (k % 2 == (backwards ? 1 : 0)) ? 0.7F : 1.0F;
/* 53 */     float r = 0.5F * colorModifier;
/* 54 */     float g = 0.4F * colorModifier;
/* 55 */     float b = 0.3F * colorModifier;
/*    */     
/* 57 */     float x = dx * progress;
/*    */     
/* 59 */     if (state.slack) {
/* 60 */       y = (dy > 0.0F) ? (dy * progress * progress) : (dy - dy * (1.0F - progress) * (1.0F - progress));
/*    */     } else {
/* 62 */       y = dy * progress;
/*    */     } 
/* 64 */     float z = dz * progress;
/*    */     
/* 66 */     builder.addVertex((Matrix4fc)pose, x - dxOff, y + fudge, z + dzOff).setColor(r, g, b, 1.0F).setLight(lightCoords);
/* 67 */     builder.addVertex((Matrix4fc)pose, x + dxOff, y + 0.05F - fudge, z - dzOff).setColor(r, g, b, 1.0F).setLight(lightCoords);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/LeashFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */