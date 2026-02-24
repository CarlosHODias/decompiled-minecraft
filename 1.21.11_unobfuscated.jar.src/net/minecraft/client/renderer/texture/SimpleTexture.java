/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class SimpleTexture
/*    */   extends ReloadableTexture {
/*    */   public SimpleTexture(Identifier location) {
/* 10 */     super(location);
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 15 */     return TextureContents.load(resourceManager, resourceId());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/SimpleTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */