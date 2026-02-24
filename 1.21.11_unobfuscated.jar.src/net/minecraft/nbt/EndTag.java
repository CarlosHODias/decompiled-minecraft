/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public final class EndTag
/*    */   implements Tag
/*    */ {
/*    */   private static final int SELF_SIZE_IN_BYTES = 8;
/*    */   
/* 12 */   public static final TagType<EndTag> TYPE = new TagType<EndTag>()
/*    */     {
/*    */       public EndTag load(DataInput input, NbtAccounter accounter) {
/* 15 */         accounter.accountBytes(8L);
/* 16 */         return EndTag.INSTANCE;
/*    */       }
/*    */ 
/*    */       
/*    */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) {
/* 21 */         accounter.accountBytes(8L);
/* 22 */         return output.visitEnd();
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void skip(DataInput input, int count, NbtAccounter accounter) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public void skip(DataInput input, NbtAccounter accounter) {}
/*    */ 
/*    */       
/*    */       public String getName() {
/* 35 */         return "END";
/*    */       }
/*    */ 
/*    */       
/*    */       public String getPrettyName() {
/* 40 */         return "TAG_End";
/*    */       }
/*    */     };
/*    */   
/* 44 */   public static final EndTag INSTANCE = new EndTag();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(DataOutput output) throws IOException {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int sizeInBytes() {
/* 55 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 60 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagType<EndTag> getType() {
/* 65 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     StringTagVisitor visitor = new StringTagVisitor();
/* 71 */     visitor.visitEnd(this);
/* 72 */     return visitor.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public EndTag copy() {
/* 77 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(TagVisitor visitor) {
/* 82 */     visitor.visitEnd(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 87 */     return visitor.visitEnd();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/EndTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */