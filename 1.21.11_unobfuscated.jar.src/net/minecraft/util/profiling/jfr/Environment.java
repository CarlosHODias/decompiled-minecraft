/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public enum Environment {
/*  6 */   CLIENT("client"), SERVER("server");
/*    */   
/*    */   private final String description;
/*    */   
/*    */   Environment(String description) {
/* 11 */     this.description = description;
/*    */   }
/*    */   
/*    */   public static Environment from(MinecraftServer server) {
/* 15 */     return server.isDedicatedServer() ? SERVER : CLIENT;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 19 */     return this.description;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/Environment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */