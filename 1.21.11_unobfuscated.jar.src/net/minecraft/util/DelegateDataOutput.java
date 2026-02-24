/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.SuppressForbidden;
/*    */ 
/*    */ public class DelegateDataOutput
/*    */   implements DataOutput {
/*    */   private final DataOutput parent;
/*    */   
/*    */   public DelegateDataOutput(DataOutput parent) {
/* 12 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 17 */     this.parent.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 22 */     this.parent.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 27 */     this.parent.write(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeBoolean(boolean v) throws IOException {
/* 32 */     this.parent.writeBoolean(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeByte(int v) throws IOException {
/* 37 */     this.parent.writeByte(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeShort(int v) throws IOException {
/* 42 */     this.parent.writeShort(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeChar(int v) throws IOException {
/* 47 */     this.parent.writeChar(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInt(int v) throws IOException {
/* 52 */     this.parent.writeInt(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeLong(long v) throws IOException {
/* 57 */     this.parent.writeLong(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeFloat(float v) throws IOException {
/* 62 */     this.parent.writeFloat(v);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeDouble(double v) throws IOException {
/* 67 */     this.parent.writeDouble(v);
/*    */   }
/*    */ 
/*    */   
/*    */   @SuppressForbidden(reason = "Delegation is not use")
/*    */   public void writeBytes(String s) throws IOException {
/* 73 */     this.parent.writeBytes(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeChars(String s) throws IOException {
/* 78 */     this.parent.writeChars(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeUTF(String s) throws IOException {
/* 83 */     this.parent.writeUTF(s);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/DelegateDataOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */