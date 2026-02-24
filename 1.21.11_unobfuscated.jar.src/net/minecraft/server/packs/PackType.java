/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ public enum PackType {
/*  4 */   CLIENT_RESOURCES("assets"),
/*  5 */   SERVER_DATA("data");
/*    */   
/*    */   private final String directory;
/*    */ 
/*    */   
/*    */   PackType(String directory) {
/* 11 */     this.directory = directory;
/*    */   }
/*    */   
/*    */   public String getDirectory() {
/* 15 */     return this.directory;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/PackType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */