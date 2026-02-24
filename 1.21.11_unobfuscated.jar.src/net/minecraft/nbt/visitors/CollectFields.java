/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Deque;
/*    */ import java.util.Set;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public class CollectFields
/*    */   extends CollectToTag {
/*    */   private int fieldsToGetCount;
/*    */   private final Set<TagType<?>> wantedTypes;
/* 15 */   private final Deque<FieldTree> stack = new ArrayDeque<>();
/*    */   
/*    */   public CollectFields(FieldSelector... wantedFields) {
/* 18 */     this.fieldsToGetCount = wantedFields.length;
/*    */     
/* 20 */     ImmutableSet.Builder<TagType<?>> wantedTypes = ImmutableSet.builder();
/* 21 */     FieldTree rootFrame = FieldTree.createRoot();
/* 22 */     for (FieldSelector wantedField : wantedFields) {
/* 23 */       rootFrame.addEntry(wantedField);
/* 24 */       wantedTypes.add(wantedField.type());
/*    */     } 
/* 26 */     this.stack.push(rootFrame);
/*    */     
/* 28 */     wantedTypes.add(CompoundTag.TYPE);
/* 29 */     this.wantedTypes = (Set<TagType<?>>)wantedTypes.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> type) {
/* 34 */     if (type != CompoundTag.TYPE) {
/* 35 */       return StreamTagVisitor.ValueResult.HALT;
/*    */     }
/* 37 */     return super.visitRootEntry(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> type) {
/* 42 */     FieldTree currentFrame = this.stack.element();
/* 43 */     if (depth() > currentFrame.depth()) {
/* 44 */       return super.visitEntry(type);
/*    */     }
/* 46 */     if (this.fieldsToGetCount <= 0) {
/* 47 */       return StreamTagVisitor.EntryResult.BREAK;
/*    */     }
/* 49 */     if (!this.wantedTypes.contains(type)) {
/* 50 */       return StreamTagVisitor.EntryResult.SKIP;
/*    */     }
/* 52 */     return super.visitEntry(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> type, String id) {
/* 57 */     FieldTree currentFrame = this.stack.element();
/* 58 */     if (depth() > currentFrame.depth()) {
/* 59 */       return super.visitEntry(type, id);
/*    */     }
/*    */     
/* 62 */     if (currentFrame.selectedFields().remove(id, type)) {
/* 63 */       this.fieldsToGetCount--;
/* 64 */       return super.visitEntry(type, id);
/*    */     } 
/*    */     
/* 67 */     if (type == CompoundTag.TYPE) {
/* 68 */       FieldTree newFrame = currentFrame.fieldsToRecurse().get(id);
/* 69 */       if (newFrame != null) {
/* 70 */         this.stack.push(newFrame);
/* 71 */         return super.visitEntry(type, id);
/*    */       } 
/*    */     } 
/*    */     
/* 75 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult visitContainerEnd() {
/* 80 */     if (depth() == ((FieldTree)this.stack.element()).depth()) {
/* 81 */       this.stack.pop();
/*    */     }
/* 83 */     return super.visitContainerEnd();
/*    */   }
/*    */   
/*    */   public int getMissingFieldCount() {
/* 87 */     return this.fieldsToGetCount;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/visitors/CollectFields.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */