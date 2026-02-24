/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class CommandTemplate extends Record implements Action {
/*    */   private final ParsedTemplate template;
/*    */   public static final com.mojang.serialization.MapCodec<CommandTemplate> MAP_CODEC;
/*    */   
/* 10 */   public CommandTemplate(ParsedTemplate template) { this.template = template; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/CommandTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate; } public ParsedTemplate template() { return this.template; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/CommandTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/CommandTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ParsedTemplate.CODEC.fieldOf("template").forGetter(CommandTemplate::template)).apply((com.mojang.datafixers.kinds.Applicative)i, CommandTemplate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CommandTemplate> codec() {
/* 19 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Optional<net.minecraft.network.chat.ClickEvent> createAction(java.util.Map<String, Action.ValueGetter> parameters) {
/* 24 */     String command = this.template.instantiate(Action.ValueGetter.getAsTemplateSubstitutions(parameters));
/* 25 */     return (java.util.Optional)java.util.Optional.of(new net.minecraft.network.chat.ClickEvent.RunCommand(command));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/action/CommandTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */