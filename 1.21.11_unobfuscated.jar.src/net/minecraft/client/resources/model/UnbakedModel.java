/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.model.ItemTransforms;
/*    */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ public interface UnbakedModel
/*    */ {
/*    */   public static final String PARTICLE_TEXTURE_REFERENCE = "particle";
/*    */   
/*    */   default Boolean ambientOcclusion() {
/* 13 */     return null;
/*    */   }
/*    */   
/*    */   default GuiLight guiLight() {
/* 17 */     return null;
/*    */   }
/*    */   
/*    */   default ItemTransforms transforms() {
/* 21 */     return null;
/*    */   }
/*    */   
/*    */   default TextureSlots.Data textureSlots() {
/* 25 */     return TextureSlots.Data.EMPTY;
/*    */   }
/*    */   
/*    */   default UnbakedGeometry geometry() {
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   default Identifier parent() {
/* 33 */     return null;
/*    */   }
/*    */   
/*    */   public enum GuiLight {
/* 37 */     FRONT("front"),
/* 38 */     SIDE("side");
/*    */     
/*    */     private final String name;
/*    */     
/*    */     GuiLight(String name) {
/* 43 */       this.name = name;
/*    */     }
/*    */     
/*    */     public static GuiLight getByName(String name) {
/* 47 */       for (GuiLight target : values()) {
/* 48 */         if (target.name.equals(name)) {
/* 49 */           return target;
/*    */         }
/*    */       } 
/* 52 */       throw new IllegalArgumentException("Invalid gui light: " + name);
/*    */     }
/*    */     
/*    */     public boolean lightLikeBlock() {
/* 56 */       return (this == SIDE);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/UnbakedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */