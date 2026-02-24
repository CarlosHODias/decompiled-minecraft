/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class TextInput extends Record implements InputControl {
/*    */   private final int width;
/*    */   private final net.minecraft.network.chat.Component label;
/*    */   private final boolean labelVisible;
/*    */   private final String initial;
/*    */   private final int maxLength;
/*    */   private final java.util.Optional<MultilineOptions> multiline;
/*    */   public static final com.mojang.serialization.MapCodec<TextInput> MAP_CODEC;
/*    */   
/* 15 */   public TextInput(int width, net.minecraft.network.chat.Component label, boolean labelVisible, String initial, int maxLength, java.util.Optional<MultilineOptions> multiline) { this.width = width; this.label = label; this.labelVisible = labelVisible; this.initial = initial; this.maxLength = maxLength; this.multiline = multiline; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.network.chat.Component label() { return this.label; } public boolean labelVisible() { return this.labelVisible; } public String initial() { return this.initial; } public int maxLength() { return this.maxLength; } public java.util.Optional<MultilineOptions> multiline() { return this.multiline; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 32 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.server.dialog.Dialog.WIDTH_CODEC.optionalFieldOf("width", 200).forGetter(TextInput::width), (App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("label").forGetter(TextInput::label), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("label_visible", true).forGetter(TextInput::labelVisible), (App)com.mojang.serialization.Codec.STRING.optionalFieldOf("initial", "").forGetter(TextInput::initial), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_length", 32).forGetter(TextInput::maxLength), (App)MultilineOptions.CODEC.optionalFieldOf("multiline").forGetter(TextInput::multiline)).apply((com.mojang.datafixers.kinds.Applicative)i, TextInput::new)).validate(o -> (o.initial.length() > o.maxLength()) ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(o));
/*    */   }
/*    */   
/*    */   public static final class MultilineOptions extends Record { private final java.util.Optional<Integer> maxLines;
/*    */     private final java.util.Optional<Integer> height;
/*    */     public static final int MAX_HEIGHT = 512;
/*    */     public static final com.mojang.serialization.Codec<MultilineOptions> CODEC;
/*    */     
/* 40 */     public MultilineOptions(java.util.Optional<Integer> maxLines, java.util.Optional<Integer> height) { this.maxLines = maxLines; this.height = height; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/* 40 */       //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<Integer> maxLines() { return this.maxLines; } public java.util.Optional<Integer> height() { return this.height; }
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 45 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_lines").forGetter(MultilineOptions::maxLines), (App)net.minecraft.util.ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(MultilineOptions::height)).apply((com.mojang.datafixers.kinds.Applicative)i, MultilineOptions::new));
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<TextInput> mapCodec() {
/* 53 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/input/TextInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */