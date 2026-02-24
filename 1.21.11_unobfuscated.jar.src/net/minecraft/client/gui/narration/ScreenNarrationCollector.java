/*    */ package net.minecraft.client.gui.narration;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Comparator;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public class ScreenNarrationCollector {
/*    */   private int generation;
/*    */   private final java.util.Map<EntryKey, NarrationEntry> entries;
/*    */   
/*    */   public ScreenNarrationCollector() {
/* 11 */     this.entries = Maps.newTreeMap(Comparator.comparing(e -> e.type).thenComparing(e -> e.depth));
/*    */   }
/*    */   
/*    */   private class Output implements NarrationElementOutput { private final int depth;
/*    */     
/*    */     private Output(int depth) {
/* 17 */       this.depth = depth;
/*    */     }
/*    */ 
/*    */     
/*    */     public void add(NarratedElementType type, NarrationThunk<?> contents) {
/* 22 */       ((ScreenNarrationCollector.NarrationEntry)ScreenNarrationCollector.this.entries.computeIfAbsent(new ScreenNarrationCollector.EntryKey(type, this.depth), k -> new ScreenNarrationCollector.NarrationEntry())).update(ScreenNarrationCollector.this.generation, contents);
/*    */     }
/*    */ 
/*    */     
/*    */     public NarrationElementOutput nest() {
/* 27 */       return new Output(this.depth + 1);
/*    */     } }
/*    */ 
/*    */   
/*    */   public void update(Consumer<NarrationElementOutput> updater) {
/* 32 */     this.generation++;
/*    */     
/* 34 */     updater.accept(new Output(0));
/*    */   }
/*    */   
/*    */   public String collectNarrationText(boolean force) {
/* 38 */     final StringBuilder result = new StringBuilder();
/* 39 */     Consumer<String> appender = new Consumer<String>(this) { private boolean firstEntry; {
/* 40 */           this.firstEntry = true;
/*    */         }
/*    */         
/*    */         public void accept(String s) {
/* 44 */           if (!this.firstEntry) {
/* 45 */             result.append(". ");
/*    */           }
/* 47 */           this.firstEntry = false;
/* 48 */           result.append(s);
/*    */         } }
/*    */       ;
/*    */     
/* 52 */     this.entries.forEach((k, v) -> {
/*    */           if (v.generation == this.generation && (force || !v.alreadyNarrated)) {
/*    */             v.contents.getText(force);
/*    */             v.alreadyNarrated = true;
/*    */           } 
/*    */         });
/* 58 */     return result.toString();
/*    */   }
/*    */   private static final class EntryKey extends Record { private final NarratedElementType type; private final int depth;
/* 61 */     private EntryKey(NarratedElementType type, int depth) { this.type = type; this.depth = depth; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 61 */       //   0	7	0	this	Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey; } public NarratedElementType type() { return this.type; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 61 */       //   0	7	0	this	Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey; } public int depth() { return this.depth; }
/*    */      public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     } } private static class NarrationEntry { private NarrationThunk<?> contents; private int generation; private boolean alreadyNarrated; private NarrationEntry() {
/* 65 */       this.contents = NarrationThunk.EMPTY;
/* 66 */       this.generation = -1;
/*    */     }
/*    */     
/*    */     public NarrationEntry update(int generation, NarrationThunk<?> contents) {
/* 70 */       if (!this.contents.equals(contents)) {
/* 71 */         this.contents = contents;
/* 72 */         this.alreadyNarrated = false;
/* 73 */       } else if (this.generation + 1 != generation) {
/* 74 */         this.alreadyNarrated = false;
/*    */       } 
/*    */       
/* 77 */       this.generation = generation;
/* 78 */       return this;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/narration/ScreenNarrationCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */