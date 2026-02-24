/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ 
/*    */ public interface HeadedModel {
/*    */   ModelPart getHead();
/*    */   
/*    */   default void translateToHead(PoseStack poseStack) {
/* 10 */     getHead().translateAndRotate(poseStack);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/HeadedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */