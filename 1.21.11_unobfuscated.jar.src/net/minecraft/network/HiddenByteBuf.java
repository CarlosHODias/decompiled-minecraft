/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.util.ReferenceCounted;
/*    */ 
/*    */ public final class HiddenByteBuf
/*    */   extends Record implements ReferenceCounted {
/*    */   private final ByteBuf contents;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/HiddenByteBuf;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HiddenByteBuf;
/*    */   }
/*    */   
/*    */   public ByteBuf contents() {
/* 16 */     return this.contents; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/HiddenByteBuf;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HiddenByteBuf; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/HiddenByteBuf;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/HiddenByteBuf;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } public HiddenByteBuf(ByteBuf contents) {
/* 20 */     this.contents = ByteBufUtil.ensureAccessible(contents);
/*    */   }
/*    */   
/*    */   public static Object pack(Object msg) {
/* 24 */     if (msg instanceof ByteBuf) { ByteBuf buf = (ByteBuf)msg;
/* 25 */       return new HiddenByteBuf(buf); }
/*    */     
/* 27 */     return msg;
/*    */   }
/*    */   
/*    */   public static Object unpack(Object msg) {
/* 31 */     if (msg instanceof HiddenByteBuf) { HiddenByteBuf buf = (HiddenByteBuf)msg;
/* 32 */       return ByteBufUtil.ensureAccessible(buf.contents); }
/*    */     
/* 34 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public int refCnt() {
/* 39 */     return this.contents.refCnt();
/*    */   }
/*    */ 
/*    */   
/*    */   public HiddenByteBuf retain() {
/* 44 */     this.contents.retain();
/* 45 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public HiddenByteBuf retain(int increment) {
/* 50 */     this.contents.retain(increment);
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public HiddenByteBuf touch() {
/* 56 */     this.contents.touch();
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public HiddenByteBuf touch(Object hint) {
/* 62 */     this.contents.touch(hint);
/* 63 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean release() {
/* 68 */     return this.contents.release();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean release(int decrement) {
/* 73 */     return this.contents.release(decrement);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/HiddenByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */