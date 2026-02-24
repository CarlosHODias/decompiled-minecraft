/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.LightTexture;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class NameTagFeatureRenderer
/*    */ {
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, Font font) {
/* 24 */     Storage storage = nodeCollection.getNameTagSubmits();
/* 25 */     storage.nameTagSubmitsSeethrough.sort(Comparator.comparing(SubmitNodeStorage.NameTagSubmit::distanceToCameraSq).reversed());
/*    */     
/* 27 */     for (SubmitNodeStorage.NameTagSubmit nameTag : storage.nameTagSubmitsSeethrough) {
/* 28 */       font.drawInBatch(nameTag.text(), nameTag.x(), nameTag.y(), nameTag.color(), false, nameTag.pose(), (MultiBufferSource)bufferSource, Font.DisplayMode.SEE_THROUGH, nameTag.backgroundColor(), nameTag.lightCoords());
/*    */     }
/* 30 */     for (SubmitNodeStorage.NameTagSubmit nameTag : storage.nameTagSubmitsNormal)
/* 31 */       font.drawInBatch(nameTag.text(), nameTag.x(), nameTag.y(), nameTag.color(), false, nameTag.pose(), (MultiBufferSource)bufferSource, Font.DisplayMode.NORMAL, nameTag.backgroundColor(), nameTag.lightCoords()); 
/*    */   }
/*    */   
/*    */   public static class Storage
/*    */   {
/* 36 */     private final List<SubmitNodeStorage.NameTagSubmit> nameTagSubmitsSeethrough = new ArrayList<>();
/* 37 */     private final List<SubmitNodeStorage.NameTagSubmit> nameTagSubmitsNormal = new ArrayList<>();
/*    */     
/*    */     public void add(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, double distanceToCameraSq, CameraRenderState camera) {
/* 40 */       if (nameTagAttachment == null) {
/*    */         return;
/*    */       }
/*    */       
/* 44 */       Minecraft minecraft = Minecraft.getInstance();
/*    */       
/* 46 */       poseStack.pushPose();
/* 47 */       poseStack.translate(nameTagAttachment.x, nameTagAttachment.y + 0.5D, nameTagAttachment.z);
/* 48 */       poseStack.mulPose((Quaternionfc)camera.orientation);
/* 49 */       poseStack.scale(0.025F, -0.025F, 0.025F);
/*    */       
/* 51 */       Matrix4f pose = new Matrix4f((Matrix4fc)poseStack.last().pose());
/*    */       
/* 53 */       float x = -minecraft.font.width((FormattedText)name) / 2.0F;
/* 54 */       int backgroundColor = (int)(minecraft.options.getBackgroundOpacity(0.25F) * 255.0F) << 24;
/*    */       
/* 56 */       if (seeThrough) {
/* 57 */         this.nameTagSubmitsNormal.add(new SubmitNodeStorage.NameTagSubmit(pose, x, offset, name, LightTexture.lightCoordsWithEmission(lightCoords, 2), -1, 0, distanceToCameraSq));
/* 58 */         this.nameTagSubmitsSeethrough.add(new SubmitNodeStorage.NameTagSubmit(pose, x, offset, name, lightCoords, -2130706433, backgroundColor, distanceToCameraSq));
/*    */       } else {
/* 60 */         this.nameTagSubmitsNormal.add(new SubmitNodeStorage.NameTagSubmit(pose, x, offset, name, lightCoords, -2130706433, backgroundColor, distanceToCameraSq));
/*    */       } 
/*    */       
/* 63 */       poseStack.popPose();
/*    */     }
/*    */     
/*    */     public void clear() {
/* 67 */       this.nameTagSubmitsNormal.clear();
/* 68 */       this.nameTagSubmitsSeethrough.clear();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/NameTagFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */