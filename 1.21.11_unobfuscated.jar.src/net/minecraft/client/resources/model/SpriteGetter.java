/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SpriteGetter
/*    */ {
/*    */   TextureAtlasSprite get(Material paramMaterial, ModelDebugName paramModelDebugName);
/*    */   
/*    */   TextureAtlasSprite reportMissingReference(String paramString, ModelDebugName paramModelDebugName);
/*    */   
/*    */   default TextureAtlasSprite resolveSlot(TextureSlots slots, String id, ModelDebugName name) {
/* 15 */     Material resolvedMaterial = slots.getMaterial(id);
/* 16 */     return (resolvedMaterial != null) ? get(resolvedMaterial, name) : reportMissingReference(id, name);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/SpriteGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */