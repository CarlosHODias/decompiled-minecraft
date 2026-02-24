/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class KeybindResolver
/*    */ {
/*    */   static Function<String, Supplier<Component>> keyResolver = name -> ();
/*    */   
/*    */   public static void setKeyResolver(Function<String, Supplier<Component>> resolver) {
/* 12 */     keyResolver = resolver;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/KeybindResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */