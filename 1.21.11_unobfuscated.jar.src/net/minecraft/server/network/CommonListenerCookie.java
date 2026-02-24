/*    */ package net.minecraft.server.network;public final class CommonListenerCookie extends Record { private final com.mojang.authlib.GameProfile gameProfile;
/*    */   private final int latency;
/*    */   private final net.minecraft.server.level.ClientInformation clientInformation;
/*    */   private final boolean transferred;
/*    */   
/*  6 */   public CommonListenerCookie(com.mojang.authlib.GameProfile gameProfile, int latency, net.minecraft.server.level.ClientInformation clientInformation, boolean transferred) { this.gameProfile = gameProfile; this.latency = latency; this.clientInformation = clientInformation; this.transferred = transferred; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/CommonListenerCookie;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/server/network/CommonListenerCookie; } public com.mojang.authlib.GameProfile gameProfile() { return this.gameProfile; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/CommonListenerCookie;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/CommonListenerCookie; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/CommonListenerCookie;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/CommonListenerCookie;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public int latency() { return this.latency; } public net.minecraft.server.level.ClientInformation clientInformation() { return this.clientInformation; } public boolean transferred() { return this.transferred; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CommonListenerCookie createInitial(com.mojang.authlib.GameProfile gameProfile, boolean transferred) {
/* 13 */     return new CommonListenerCookie(gameProfile, 0, 
/*    */ 
/*    */         
/* 16 */         net.minecraft.server.level.ClientInformation.createDefault(), transferred);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/CommonListenerCookie.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */