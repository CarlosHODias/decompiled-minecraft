/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public interface SkipAll extends StreamTagVisitor {
/*  7 */   public static final SkipAll INSTANCE = new SkipAll() {
/*    */     
/*    */     };
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitEnd() {
/* 12 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(String value) {
/* 17 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(byte value) {
/* 22 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(short value) {
/* 27 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(int value) {
/* 32 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(long value) {
/* 37 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(float value) {
/* 42 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(double value) {
/* 47 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(byte[] value) {
/* 52 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(int[] value) {
/* 57 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(long[] value) {
/* 62 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitList(TagType<?> elementType, int size) {
/* 67 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.EntryResult visitElement(TagType<?> type, int index) {
/* 72 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.EntryResult visitEntry(TagType<?> type) {
/* 77 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.EntryResult visitEntry(TagType<?> type, String id) {
/* 82 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitContainerEnd() {
/* 87 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitRootEntry(TagType<?> type) {
/* 92 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/visitors/SkipAll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */