/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfos;
/*    */ 
/*    */ public final class ObjectContents extends Record implements net.minecraft.network.chat.ComponentContents {
/*    */   private final ObjectInfo contents;
/*    */   
/* 13 */   public ObjectContents(ObjectInfo contents) { this.contents = contents; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/ObjectContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ObjectContents; } public ObjectInfo contents() { return this.contents; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/ObjectContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ObjectContents; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/ObjectContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/ObjectContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 16 */   } private static final String PLACEHOLDER = Character.toString('￼'); public static final com.mojang.serialization.MapCodec<ObjectContents> MAP_CODEC;
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ObjectInfos.CODEC.forGetter(ObjectContents::contents)).apply((com.mojang.datafixers.kinds.Applicative)i, ObjectContents::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ObjectContents> codec() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> java.util.Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 29 */     return output.accept(this.contents.description());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> java.util.Optional<T> visit(FormattedText.StyledContentConsumer<T> output, net.minecraft.network.chat.Style currentStyle) {
/* 34 */     return output.accept(currentStyle.withFont(this.contents.fontDescription()), PLACEHOLDER);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/ObjectContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */