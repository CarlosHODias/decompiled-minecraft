/*    */ package net.minecraft.network.chat.contents;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public class KeybindContents implements net.minecraft.network.chat.ComponentContents {
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("keybind").forGetter(())).apply((Applicative)i, KeybindContents::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<KeybindContents> MAP_CODEC;
/*    */   private final String name;
/*    */   private Supplier<Component> nameResolver;
/*    */   
/*    */   public KeybindContents(String name) {
/* 24 */     this.name = name;
/*    */   }
/*    */   
/*    */   private Component getNestedComponent() {
/* 28 */     if (this.nameResolver == null) {
/* 29 */       this.nameResolver = KeybindResolver.keyResolver.apply(this.name);
/*    */     }
/*    */     
/* 32 */     return this.nameResolver.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 37 */     return getNestedComponent().visit(output);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 42 */     return getNestedComponent().visit(output, currentStyle);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: if_acmpne -> 7
/*    */     //   5: iconst_1
/*    */     //   6: ireturn
/*    */     //   7: aload_1
/*    */     //   8: instanceof net/minecraft/network/chat/contents/KeybindContents
/*    */     //   11: ifeq -> 37
/*    */     //   14: aload_1
/*    */     //   15: checkcast net/minecraft/network/chat/contents/KeybindContents
/*    */     //   18: astore_2
/*    */     //   19: aload_0
/*    */     //   20: getfield name : Ljava/lang/String;
/*    */     //   23: aload_2
/*    */     //   24: getfield name : Ljava/lang/String;
/*    */     //   27: invokevirtual equals : (Ljava/lang/Object;)Z
/*    */     //   30: ifeq -> 37
/*    */     //   33: iconst_1
/*    */     //   34: goto -> 38
/*    */     //   37: iconst_0
/*    */     //   38: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     //   #48	-> 5
/*    */     //   #51	-> 7
/*    */     //   #50	-> 14
/*    */     //   #51	-> 27
/*    */     //   #50	-> 38
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   19	18	2	that	Lnet/minecraft/network/chat/contents/KeybindContents;
/*    */     //   0	39	0	this	Lnet/minecraft/network/chat/contents/KeybindContents;
/*    */     //   0	39	1	o	Ljava/lang/Object;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "keybind{" + this.name + "}";
/*    */   }
/*    */   
/*    */   public String getName() {
/* 65 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<KeybindContents> codec() {
/* 70 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/KeybindContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */