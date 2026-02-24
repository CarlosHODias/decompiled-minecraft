/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ClientIntent
/*    */ {
/*  7 */   STATUS,
/*  8 */   LOGIN,
/*  9 */   TRANSFER;
/*    */ 
/*    */   
/*    */   private static final int STATUS_ID = 1;
/*    */ 
/*    */   
/*    */   private static final int LOGIN_ID = 2;
/*    */ 
/*    */   
/*    */   private static final int TRANSFER_ID = 3;
/*    */ 
/*    */   
/*    */   public static ClientIntent byId(int id) {
/*    */     // Byte code:
/*    */     //   0: iload_0
/*    */     //   1: tableswitch default -> 46, 1 -> 28, 2 -> 34, 3 -> 40
/*    */     //   28: getstatic net/minecraft/network/protocol/handshake/ClientIntent.STATUS : Lnet/minecraft/network/protocol/handshake/ClientIntent;
/*    */     //   31: goto -> 60
/*    */     //   34: getstatic net/minecraft/network/protocol/handshake/ClientIntent.LOGIN : Lnet/minecraft/network/protocol/handshake/ClientIntent;
/*    */     //   37: goto -> 60
/*    */     //   40: getstatic net/minecraft/network/protocol/handshake/ClientIntent.TRANSFER : Lnet/minecraft/network/protocol/handshake/ClientIntent;
/*    */     //   43: goto -> 60
/*    */     //   46: new java/lang/IllegalArgumentException
/*    */     //   49: dup
/*    */     //   50: iload_0
/*    */     //   51: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*    */     //   56: invokespecial <init> : (Ljava/lang/String;)V
/*    */     //   59: athrow
/*    */     //   60: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     //   #19	-> 28
/*    */     //   #20	-> 34
/*    */     //   #21	-> 40
/*    */     //   #22	-> 46
/*    */     //   #18	-> 60
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	61	0	id	I
/*    */   }
/*    */ 
/*    */   
/*    */   public int id() {
/* 27 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*    */ 
/*    */       
/* 30 */       3;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/handshake/ClientIntent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */