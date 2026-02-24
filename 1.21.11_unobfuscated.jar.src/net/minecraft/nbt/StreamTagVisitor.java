/*    */ package net.minecraft.nbt;public interface StreamTagVisitor { ValueResult visitEnd();
/*    */   ValueResult visit(String paramString);
/*    */   ValueResult visit(byte paramByte);
/*    */   ValueResult visit(short paramShort);
/*    */   ValueResult visit(int paramInt);
/*    */   ValueResult visit(long paramLong);
/*    */   ValueResult visit(float paramFloat);
/*    */   ValueResult visit(double paramDouble);
/*    */   ValueResult visit(byte[] paramArrayOfbyte);
/*    */   ValueResult visit(int[] paramArrayOfint);
/*    */   ValueResult visit(long[] paramArrayOflong);
/*    */   ValueResult visitList(TagType<?> paramTagType, int paramInt);
/*    */   EntryResult visitEntry(TagType<?> paramTagType);
/*    */   EntryResult visitEntry(TagType<?> paramTagType, String paramString);
/*    */   EntryResult visitElement(TagType<?> paramTagType, int paramInt);
/*    */   ValueResult visitContainerEnd();
/*    */   ValueResult visitRootEntry(TagType<?> paramTagType);
/* 18 */   public enum ValueResult { CONTINUE,
/*    */ 
/*    */ 
/*    */     
/* 22 */     BREAK,
/*    */ 
/*    */ 
/*    */     
/* 26 */     HALT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public enum EntryResult
/*    */   {
/* 33 */     ENTER,
/*    */ 
/*    */ 
/*    */     
/* 37 */     SKIP,
/*    */ 
/*    */ 
/*    */     
/* 41 */     BREAK,
/*    */ 
/*    */ 
/*    */     
/* 45 */     HALT;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/StreamTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */