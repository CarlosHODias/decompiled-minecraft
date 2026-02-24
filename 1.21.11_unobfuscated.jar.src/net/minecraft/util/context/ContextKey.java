/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ContextKey<T>
/*    */ {
/*    */   private final Identifier name;
/*    */   
/*    */   public ContextKey(Identifier name) {
/* 10 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static <T> ContextKey<T> vanilla(String name) {
/* 14 */     return new ContextKey<>(Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public Identifier name() {
/* 18 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return "<parameter " + String.valueOf(this.name) + ">";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/context/ContextKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */