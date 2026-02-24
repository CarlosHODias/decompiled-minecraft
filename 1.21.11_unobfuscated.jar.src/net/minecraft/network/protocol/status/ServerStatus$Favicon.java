/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Base64;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Favicon
/*    */   extends Record
/*    */ {
/*    */   private final byte[] iconBytes;
/*    */   private static final String PREFIX = "data:image/png;base64,";
/*    */   public static final Codec<Favicon> CODEC;
/*    */   
/*    */   public Favicon(byte[] iconBytes) {
/* 48 */     this.iconBytes = iconBytes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 48 */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon; } public byte[] iconBytes() { return this.iconBytes; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 51 */     CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */           if (!string.startsWith("data:image/png;base64,")) {
/*    */             return DataResult.error(());
/*    */           }
/*    */           
/*    */           try {
/*    */             String base64 = string.substring("data:image/png;base64,".length()).replaceAll("\n", "");
/*    */             
/*    */             byte[] iconBytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
/*    */             return DataResult.success(new Favicon(iconBytes));
/* 61 */           } catch (IllegalArgumentException e) {
/*    */             return DataResult.error(());
/*    */           } 
/*    */         }, favicon -> "data:image/png;base64," + new String(Base64.getEncoder().encode(favicon.iconBytes), StandardCharsets.UTF_8));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/status/ServerStatus$Favicon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */