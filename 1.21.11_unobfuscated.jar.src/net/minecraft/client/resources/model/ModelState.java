/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ModelState
/*    */ {
/* 12 */   public static final Matrix4fc NO_TRANSFORM = (Matrix4fc)new Matrix4f();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Transformation transformation() {
/* 65 */     return Transformation.identity();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Matrix4fc faceTransformation(Direction face) {
/* 72 */     return NO_TRANSFORM;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Matrix4fc inverseFaceTransformation(Direction face) {
/* 79 */     return NO_TRANSFORM;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ModelState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */