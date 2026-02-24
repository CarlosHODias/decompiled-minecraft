/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class SingleOptionInput extends Record implements InputControl {
/*    */   private final int width;
/*    */   private final java.util.List<Entry> entries;
/*    */   private final Component label;
/*    */   private final boolean labelVisible;
/*    */   public static final com.mojang.serialization.MapCodec<SingleOptionInput> MAP_CODEC;
/*    */   
/* 15 */   public SingleOptionInput(int width, java.util.List<Entry> entries, Component label, boolean labelVisible) { this.width = width; this.entries = entries; this.label = label; this.labelVisible = labelVisible; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/SingleOptionInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/SingleOptionInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/SingleOptionInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.List<Entry> entries() { return this.entries; } public Component label() { return this.label; } public boolean labelVisible() { return this.labelVisible; }
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
/* 28 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.server.dialog.Dialog.WIDTH_CODEC.optionalFieldOf("width", 200).forGetter(SingleOptionInput::width), (App)net.minecraft.util.ExtraCodecs.nonEmptyList(Entry.CODEC.listOf()).fieldOf("options").forGetter(SingleOptionInput::entries), (App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("label").forGetter(SingleOptionInput::label), (App)Codec.BOOL.optionalFieldOf("label_visible", true).forGetter(SingleOptionInput::labelVisible)).apply((com.mojang.datafixers.kinds.Applicative)i, SingleOptionInput::new)).validate(o -> {
/*    */           long initialCount = o.entries.stream().filter(Entry::initial).count();
/*    */           return (initialCount > 1L) ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(o);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SingleOptionInput> mapCodec() {
/* 38 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public java.util.Optional<Entry> initial() {
/* 42 */     return this.entries.stream().filter(Entry::initial).findFirst();
/*    */   }
/*    */   public static final class Entry extends Record { private final String id; private final java.util.Optional<Component> display; private final boolean initial; public static final Codec<Entry> FULL_CODEC; public static final Codec<Entry> CODEC;
/* 45 */     public Entry(String id, java.util.Optional<Component> display, boolean initial) { this.id = id; this.display = display; this.initial = initial; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;
/* 45 */       //   0	8	1	o	Ljava/lang/Object; } public String id() { return this.id; } public java.util.Optional<Component> display() { return this.display; } public boolean initial() { return this.initial; }
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 50 */       FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("id").forGetter(Entry::id), (App)net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("display").forGetter(Entry::display), (App)Codec.BOOL.optionalFieldOf("initial", false).forGetter(Entry::initial)).apply((com.mojang.datafixers.kinds.Applicative)i, Entry::new));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 56 */       CODEC = Codec.withAlternative(FULL_CODEC, (Codec)Codec.STRING, id -> new Entry(id, java.util.Optional.empty(), false));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Component displayOrDefault() {
/* 62 */       return this.display.orElseGet(() -> Component.literal(this.id));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/input/SingleOptionInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */