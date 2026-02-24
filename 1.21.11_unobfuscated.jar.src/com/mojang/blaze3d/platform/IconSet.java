/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ 
/*    */ public enum IconSet
/*    */ {
/* 13 */   RELEASE(new String[] { "icons" }),
/* 14 */   SNAPSHOT(new String[] { "icons", "snapshot" });
/*    */   
/*    */   private final String[] path;
/*    */ 
/*    */   
/*    */   IconSet(String... path) {
/* 20 */     this.path = path;
/*    */   }
/*    */   
/*    */   public List<IoSupplier<InputStream>> getStandardIcons(PackResources resources) throws IOException {
/* 24 */     return List.of(
/* 25 */         getFile(resources, "icon_16x16.png"), 
/* 26 */         getFile(resources, "icon_32x32.png"), 
/* 27 */         getFile(resources, "icon_48x48.png"), 
/* 28 */         getFile(resources, "icon_128x128.png"), 
/* 29 */         getFile(resources, "icon_256x256.png"));
/*    */   }
/*    */ 
/*    */   
/*    */   public IoSupplier<InputStream> getMacIcon(PackResources resources) throws IOException {
/* 34 */     return getFile(resources, "minecraft.icns");
/*    */   }
/*    */   
/*    */   private IoSupplier<InputStream> getFile(PackResources resources, String fileName) throws IOException {
/* 38 */     String[] fullPath = (String[])ArrayUtils.add((Object[])this.path, fileName);
/* 39 */     IoSupplier<InputStream> resource = resources.getRootResource(fullPath);
/* 40 */     if (resource == null) {
/* 41 */       throw new FileNotFoundException(String.join("/", fullPath));
/*    */     }
/* 43 */     return resource;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/IconSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */