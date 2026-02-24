/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public interface TagType<T extends Tag> {
/*    */   T load(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   StreamTagVisitor.ValueResult parse(DataInput paramDataInput, StreamTagVisitor paramStreamTagVisitor, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   default void parseRoot(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 12 */     switch (output.visitRootEntry(this)) { case CONTINUE:
/* 13 */         parse(input, output, accounter);
/*    */         break;
/*    */       case BREAK:
/* 16 */         skip(input, accounter);
/*    */         break; }
/*    */   
/*    */   }
/*    */   
/*    */   void skip(DataInput paramDataInput, int paramInt, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   void skip(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   String getName();
/*    */   
/*    */   String getPrettyName();
/*    */   
/*    */   public static interface StaticSize<T extends Tag> extends TagType<T> {
/*    */     default void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 31 */       input.skipBytes(size());
/*    */     }
/*    */ 
/*    */     
/*    */     default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 36 */       input.skipBytes(size() * count);
/*    */     }
/*    */     
/*    */     int size();
/*    */   }
/*    */   
/*    */   public static interface VariableSize<T extends Tag>
/*    */     extends TagType<T> {
/*    */     default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 45 */       for (int i = 0; i < count; i++) {
/* 46 */         skip(input, accounter);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   static TagType<EndTag> createInvalid(final int id) {
/* 52 */     return new TagType<EndTag>() {
/*    */         private IOException createException() {
/* 54 */           return new IOException("Invalid tag id: " + id);
/*    */         }
/*    */ 
/*    */         
/*    */         public EndTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 59 */           throw createException();
/*    */         }
/*    */ 
/*    */         
/*    */         public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 64 */           throw createException();
/*    */         }
/*    */ 
/*    */         
/*    */         public void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 69 */           throw createException();
/*    */         }
/*    */ 
/*    */         
/*    */         public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 74 */           throw createException();
/*    */         }
/*    */ 
/*    */         
/*    */         public String getName() {
/* 79 */           return "INVALID[" + id + "]";
/*    */         }
/*    */ 
/*    */         
/*    */         public String getPrettyName() {
/* 84 */           return "UNKNOWN_" + id;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/TagType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */