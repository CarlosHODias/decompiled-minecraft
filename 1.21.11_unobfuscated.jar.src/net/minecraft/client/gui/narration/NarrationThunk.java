/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ 
/*    */ public class NarrationThunk<T>
/*    */ {
/*    */   private final T contents;
/*    */   private final BiConsumer<Consumer<String>, T> converter;
/* 14 */   public static final NarrationThunk<?> EMPTY = new NarrationThunk((T)Unit.INSTANCE, (o, c) -> {
/*    */       
/*    */       }); private NarrationThunk(T contents, BiConsumer<Consumer<String>, T> converter) {
/* 17 */     this.contents = contents;
/* 18 */     this.converter = converter;
/*    */   }
/*    */   
/*    */   public static NarrationThunk<?> from(String text) {
/* 22 */     return new NarrationThunk(text, Consumer::accept);
/*    */   }
/*    */   
/*    */   public static NarrationThunk<?> from(Component text) {
/* 26 */     return new NarrationThunk(text, (o, c) -> o.accept(c.getString()));
/*    */   }
/*    */   
/*    */   public static NarrationThunk<?> from(List<Component> lines) {
/* 30 */     return new NarrationThunk(lines, (o, c) -> lines.stream().map(Component::getString).forEach(o));
/*    */   }
/*    */   
/*    */   public void getText(Consumer<String> output) {
/* 34 */     this.converter.accept(output, this.contents);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 39 */     if (this == o) {
/* 40 */       return true;
/*    */     }
/*    */     
/* 43 */     if (o instanceof NarrationThunk) { NarrationThunk<?> thunk = (NarrationThunk)o;
/* 44 */       return (thunk.converter == this.converter && thunk.contents.equals(this.contents)); }
/*    */ 
/*    */     
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     int result = this.contents.hashCode();
/* 53 */     result = 31 * result + this.converter.hashCode();
/* 54 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/narration/NarrationThunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */