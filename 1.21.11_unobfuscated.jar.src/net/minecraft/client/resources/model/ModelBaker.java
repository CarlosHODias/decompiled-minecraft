/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ModelBaker
/*    */ {
/*    */   ResolvedModel getModel(Identifier paramIdentifier);
/*    */   
/*    */   BlockModelPart missingBlockModelPart();
/*    */   
/*    */   SpriteGetter sprites();
/*    */   
/*    */   PartCache parts();
/*    */   
/*    */   <T> T compute(SharedOperationKey<T> paramSharedOperationKey);
/*    */   
/*    */   public static interface PartCache
/*    */   {
/*    */     default Vector3fc vector(float x, float y, float z) {
/* 26 */       return vector((Vector3fc)new Vector3f(x, y, z));
/*    */     }
/*    */     
/*    */     Vector3fc vector(Vector3fc param1Vector3fc);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface SharedOperationKey<T> {
/*    */     T compute(ModelBaker param1ModelBaker);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ModelBaker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */