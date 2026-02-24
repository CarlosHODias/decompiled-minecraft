/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class ContentValidationException extends Exception {
/*    */   private final Path directory;
/*    */   private final List<ForbiddenSymlinkInfo> entries;
/*    */   
/*    */   public ContentValidationException(Path directory, List<ForbiddenSymlinkInfo> entries) {
/* 12 */     this.directory = directory;
/* 13 */     this.entries = entries;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 18 */     return getMessage(this.directory, this.entries);
/*    */   }
/*    */   
/*    */   public static String getMessage(Path directory, List<ForbiddenSymlinkInfo> entries) {
/* 22 */     return "Failed to validate '" + String.valueOf(directory) + "'. Found forbidden symlinks: " + (String)entries.stream().map(e -> String.valueOf(e.link()) + "->" + String.valueOf(e.link())).collect(Collectors.joining(", "));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/validation/ContentValidationException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */