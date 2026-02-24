/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public interface Control {
/*  4 */   public static final Control UNBOUND = new Control()
/*    */     {
/*    */       public void cut() {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean hasCut() {
/* 11 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   void cut();
/*    */   
/*    */   boolean hasCut();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/Control.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */