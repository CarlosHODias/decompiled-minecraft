/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ChatTypeDecoration extends Record {
/*    */   private final String translationKey;
/*    */   private final List<Parameter> parameters;
/*    */   private final Style style;
/*    */   public static final Codec<ChatTypeDecoration> CODEC;
/*    */   
/* 16 */   public ChatTypeDecoration(String translationKey, List<Parameter> parameters, Style style) { this.translationKey = translationKey; this.parameters = parameters; this.style = style; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ChatTypeDecoration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatTypeDecoration; } public String translationKey() { return this.translationKey; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ChatTypeDecoration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatTypeDecoration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ChatTypeDecoration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ChatTypeDecoration;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public List<Parameter> parameters() { return this.parameters; } public Style style() { return this.style; } static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("translation_key").forGetter(ChatTypeDecoration::translationKey), (App)Parameter.CODEC.listOf().fieldOf("parameters").forGetter(ChatTypeDecoration::parameters), (App)Style.Serializer.CODEC.optionalFieldOf("style", Style.EMPTY).forGetter(ChatTypeDecoration::style)).apply((com.mojang.datafixers.kinds.Applicative)i, ChatTypeDecoration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ChatTypeDecoration> STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8, ChatTypeDecoration::translationKey, 
/*    */       
/* 25 */       Parameter.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), ChatTypeDecoration::parameters, Style.Serializer.TRUSTED_STREAM_CODEC, ChatTypeDecoration::style, ChatTypeDecoration::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ChatTypeDecoration withSender(String translationKey) {
/* 31 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.SENDER, Parameter.CONTENT), Style.EMPTY);
/*    */   }
/*    */   
/*    */   public static ChatTypeDecoration incomingDirectMessage(String translationKey) {
/* 35 */     Style style = Style.EMPTY.withColor(net.minecraft.ChatFormatting.GRAY).withItalic(true);
/* 36 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.SENDER, Parameter.CONTENT), style);
/*    */   }
/*    */   
/*    */   public static ChatTypeDecoration outgoingDirectMessage(String translationKey) {
/* 40 */     Style style = Style.EMPTY.withColor(net.minecraft.ChatFormatting.GRAY).withItalic(true);
/* 41 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.TARGET, Parameter.CONTENT), style);
/*    */   }
/*    */   
/*    */   public static ChatTypeDecoration teamMessage(String translationKey) {
/* 45 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.TARGET, Parameter.SENDER, Parameter.CONTENT), Style.EMPTY);
/*    */   }
/*    */   
/*    */   public Component decorate(Component content, ChatType.Bound chatType) {
/* 49 */     Component[] arrayOfComponent = resolveParameters(content, chatType);
/* 50 */     return Component.translatable(this.translationKey, (Object[])arrayOfComponent).withStyle(this.style);
/*    */   }
/*    */   
/*    */   private Component[] resolveParameters(Component content, ChatType.Bound chatType) {
/* 54 */     Component[] resolved = new Component[this.parameters.size()];
/* 55 */     for (int i = 0; i < resolved.length; i++) {
/* 56 */       Parameter parameter = this.parameters.get(i);
/* 57 */       resolved[i] = parameter.select(content, chatType);
/*    */     } 
/* 59 */     return resolved;
/*    */   }
/*    */   public enum Parameter implements net.minecraft.util.StringRepresentable { CONTENT, TARGET, SENDER; private final Selector selector; private final String name; private final int id; public static final StreamCodec<io.netty.buffer.ByteBuf, Parameter> STREAM_CODEC;
/*    */     static {
/* 63 */       SENDER = new Parameter("SENDER", 0, 0, "sender", (content, chatType) -> chatType.name());
/* 64 */       TARGET = new Parameter("TARGET", 1, 1, "target", (content, chatType) -> (Component)chatType.targetName().orElse(CommonComponents.EMPTY));
/* 65 */       CONTENT = new Parameter("CONTENT", 2, 2, "content", (content, chatType) -> content);
/*    */     } static {
/* 67 */       BY_ID = net.minecraft.util.ByIdMap.continuous(p -> p.id, (Object[])values(), net.minecraft.util.ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     }
/* 69 */     public static final Codec<Parameter> CODEC = (Codec<Parameter>)net.minecraft.util.StringRepresentable.fromEnum(Parameter::values); private static final java.util.function.IntFunction<Parameter> BY_ID; static {
/* 70 */       STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.idMapper(BY_ID, p -> p.id);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     Parameter(int id, String name, Selector selector) {
/* 77 */       this.id = id;
/* 78 */       this.name = name;
/* 79 */       this.selector = selector;
/*    */     }
/*    */     
/*    */     public Component select(Component content, ChatType.Bound chatType) {
/* 83 */       return this.selector.select(content, chatType);
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 88 */       return this.name;
/*    */     }
/*    */     
/*    */     public static interface Selector {
/*    */       Component select(Component param2Component, ChatType.Bound param2Bound);
/*    */     } }
/*    */ 
/*    */   
/*    */   public static interface Selector {
/*    */     Component select(Component param1Component, ChatType.Bound param1Bound);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/ChatTypeDecoration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */