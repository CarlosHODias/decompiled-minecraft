/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.FileUtil;
/*    */ 
/*    */ public interface CachedOutput {
/*    */   static {
/* 11 */     NO_CACHE = ((path, input, hash) -> {
/*    */         FileUtil.createDirectoriesSafe(path.getParent());
/*    */         Files.write(path, input, new java.nio.file.OpenOption[0]);
/*    */       });
/*    */   }
/*    */   
/*    */   public static final CachedOutput NO_CACHE;
/*    */   
/*    */   void writeIfNeeded(Path paramPath, byte[] paramArrayOfbyte, HashCode paramHashCode) throws IOException;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/CachedOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */