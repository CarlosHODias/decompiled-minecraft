/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.AtlasManager;
/*    */ import net.minecraft.client.resources.model.ModelBakery;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ 
/*    */ public class FlameFeatureRenderer
/*    */ {
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, AtlasManager atlasManager) {
/* 20 */     for (SubmitNodeStorage.FlameSubmit flameSubmit : (Iterable<SubmitNodeStorage.FlameSubmit>)nodeCollection.getFlameSubmits()) {
/* 21 */       renderFlame(flameSubmit.pose(), (MultiBufferSource)bufferSource, flameSubmit.entityRenderState(), flameSubmit.rotation(), atlasManager);
/*    */     }
/*    */   }
/*    */   
/*    */   private void renderFlame(PoseStack.Pose pose, MultiBufferSource bufferSource, EntityRenderState state, Quaternionf rotation, AtlasManager atlasManager) {
/* 26 */     TextureAtlasSprite fire1 = atlasManager.get(ModelBakery.FIRE_0);
/* 27 */     TextureAtlasSprite fire2 = atlasManager.get(ModelBakery.FIRE_1);
/*    */     
/* 29 */     float s = state.boundingBoxWidth * 1.4F;
/* 30 */     pose.scale(s, s, s);
/*    */     
/* 32 */     float r = 0.5F;
/* 33 */     float xo = 0.0F;
/*    */     
/* 35 */     float h = state.boundingBoxHeight / s;
/* 36 */     float yo = 0.0F;
/*    */     
/* 38 */     pose.rotate((Quaternionfc)rotation);
/*    */     
/* 40 */     pose.translate(0.0F, 0.0F, 0.3F - (int)h * 0.02F);
/* 41 */     float zo = 0.0F;
/* 42 */     int ss = 0;
/* 43 */     VertexConsumer buffer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
/*    */     
/* 45 */     while (h > 0.0F) {
/* 46 */       TextureAtlasSprite tex = (ss % 2 == 0) ? fire1 : fire2;
/*    */       
/* 48 */       float u0 = tex.getU0();
/* 49 */       float v0 = tex.getV0();
/* 50 */       float u1 = tex.getU1();
/* 51 */       float v1 = tex.getV1();
/* 52 */       if (ss / 2 % 2 == 0) {
/* 53 */         float tmp = u1;
/* 54 */         u1 = u0;
/* 55 */         u0 = tmp;
/*    */       } 
/* 57 */       fireVertex(pose, buffer, -r - 0.0F, 0.0F - yo, zo, u1, v1);
/* 58 */       fireVertex(pose, buffer, r - 0.0F, 0.0F - yo, zo, u0, v1);
/* 59 */       fireVertex(pose, buffer, r - 0.0F, 1.4F - yo, zo, u0, v0);
/* 60 */       fireVertex(pose, buffer, -r - 0.0F, 1.4F - yo, zo, u1, v0);
/* 61 */       h -= 0.45F;
/* 62 */       yo -= 0.45F;
/* 63 */       r *= 0.9F;
/* 64 */       zo -= 0.03F;
/* 65 */       ss++;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void fireVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v) {
/* 70 */     buffer.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setUv1(0, 10).setLight(240).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/FlameFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */