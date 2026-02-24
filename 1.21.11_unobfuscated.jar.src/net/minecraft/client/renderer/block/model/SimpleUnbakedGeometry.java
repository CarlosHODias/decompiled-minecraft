/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.model.QuadCollection;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class SimpleUnbakedGeometry extends Record implements net.minecraft.client.resources.model.UnbakedGeometry {
/*    */   private final List<BlockElement> elements;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/SimpleUnbakedGeometry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SimpleUnbakedGeometry;
/*    */   }
/*    */   
/* 15 */   public SimpleUnbakedGeometry(List<BlockElement> elements) { this.elements = elements; } public List<BlockElement> elements() { return this.elements; } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/SimpleUnbakedGeometry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SimpleUnbakedGeometry;
/*    */   } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/SimpleUnbakedGeometry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/SimpleUnbakedGeometry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } public QuadCollection bake(TextureSlots textures, net.minecraft.client.resources.model.ModelBaker modelBaker, net.minecraft.client.resources.model.ModelState modelState, net.minecraft.client.resources.model.ModelDebugName name) {
/* 20 */     return bake(this.elements, textures, modelBaker, modelState, name);
/*    */   }
/*    */   
/*    */   public static QuadCollection bake(List<BlockElement> elements, TextureSlots textures, net.minecraft.client.resources.model.ModelBaker modelBaker, net.minecraft.client.resources.model.ModelState modelState, net.minecraft.client.resources.model.ModelDebugName name) {
/* 24 */     QuadCollection.Builder builder = new QuadCollection.Builder();
/*    */     
/* 26 */     for (BlockElement element : elements) {
/*    */       boolean drawXFaces = true;
/*    */       
/*    */       boolean drawYFaces = true;
/*    */       boolean drawZFaces = true;
/* 31 */       Vector3fc from = element.from();
/* 32 */       Vector3fc to = element.to();
/*    */       
/* 34 */       if (from.x() == to.x()) {
/* 35 */         drawYFaces = false;
/* 36 */         drawZFaces = false;
/*    */       } 
/*    */       
/* 39 */       if (from.y() == to.y()) {
/* 40 */         drawXFaces = false;
/* 41 */         drawZFaces = false;
/*    */       } 
/*    */       
/* 44 */       if (from.z() == to.z()) {
/* 45 */         drawXFaces = false;
/* 46 */         drawYFaces = false;
/*    */       } 
/*    */       
/* 49 */       if (drawXFaces || drawYFaces || drawZFaces) {
/* 50 */         for (java.util.Map.Entry<Direction, BlockElementFace> entry : element.faces().entrySet()) {
/* 51 */           Direction facing = entry.getKey();
/* 52 */           BlockElementFace face = entry.getValue();
/*    */           
/* 54 */           switch (facing.getAxis()) { default: throw new MatchException(null, null);
/*    */             case X: 
/*    */             case Y: 
/* 57 */             case Z: break; }  boolean shouldDrawFace = drawZFaces;
/*    */ 
/*    */           
/* 60 */           if (shouldDrawFace) {
/* 61 */             net.minecraft.client.renderer.texture.TextureAtlasSprite icon = modelBaker.sprites().resolveSlot(textures, face.texture(), name);
/* 62 */             BakedQuad quad = FaceBakery.bakeQuad(modelBaker.parts(), from, to, face, icon, facing, modelState, element.rotation(), element.shade(), element.lightEmission());
/* 63 */             if (face.cullForDirection() == null) {
/* 64 */               builder.addUnculledFace(quad); continue;
/*    */             } 
/* 66 */             builder.addCulledFace(Direction.rotate(modelState.transformation().getMatrix(), face.cullForDirection()), quad);
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 73 */     return builder.build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/SimpleUnbakedGeometry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */