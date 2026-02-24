/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ public class LegacyStuffWrapper
/*    */ {
/*    */   @Deprecated
/*    */   public static int[] getPixels(ResourceManager resourceManager, Identifier location) throws IOException {
/* 14 */     InputStream resource = resourceManager.open(location); try {
/* 15 */       NativeImage image = NativeImage.read(resource);
/*    */       try {
/* 17 */         int[] arrayOfInt = image.makePixelArray();
/* 18 */         if (image != null) image.close();  if (resource != null) resource.close(); 
/*    */         return arrayOfInt;
/*    */       } catch (Throwable throwable) {
/*    */         if (image != null)
/*    */           try {
/*    */             image.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/*    */     } catch (Throwable throwable) {
/*    */       if (resource != null)
/*    */         try {
/*    */           resource.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/LegacyStuffWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */