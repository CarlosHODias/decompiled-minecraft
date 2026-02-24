/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class NetworkDataOutputStream {
/*    */   private final ByteArrayOutputStream outputStream;
/*    */   private final DataOutputStream dataOutputStream;
/*    */   
/*    */   public NetworkDataOutputStream(int size) {
/* 13 */     this.outputStream = new ByteArrayOutputStream(size);
/* 14 */     this.dataOutputStream = new DataOutputStream(this.outputStream);
/*    */   }
/*    */   
/*    */   public void writeBytes(byte[] data) throws IOException {
/* 18 */     this.dataOutputStream.write(data, 0, data.length);
/*    */   }
/*    */   
/*    */   public void writeString(String data) throws IOException {
/* 22 */     this.dataOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
/* 23 */     this.dataOutputStream.write(0);
/*    */   }
/*    */   
/*    */   public void write(int data) throws IOException {
/* 27 */     this.dataOutputStream.write(data);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeShort(short data) throws IOException {
/* 32 */     this.dataOutputStream.writeShort(Short.reverseBytes(data));
/*    */   }
/*    */   
/*    */   public void writeInt(int data) throws IOException {
/* 36 */     this.dataOutputStream.writeInt(Integer.reverseBytes(data));
/*    */   }
/*    */   
/*    */   public void writeFloat(float data) throws IOException {
/* 40 */     this.dataOutputStream.writeInt(Integer.reverseBytes(Float.floatToIntBits(data)));
/*    */   }
/*    */   
/*    */   public byte[] toByteArray() {
/* 44 */     return this.outputStream.toByteArray();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 48 */     this.outputStream.reset();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/rcon/NetworkDataOutputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */