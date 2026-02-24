/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class StringArgumentSerializer implements ArgumentTypeInfo<StringArgumentType, StringArgumentSerializer.Template> {
/*    */   public final class Template implements ArgumentTypeInfo.Template<StringArgumentType> {
/*    */     private final StringArgumentType.StringType type;
/*    */     
/*    */     public Template(StringArgumentType.StringType type) {
/* 14 */       this.type = type;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public StringArgumentType instantiate(CommandBuildContext context) {
/*    */       // Byte code:
/*    */       //   0: getstatic net/minecraft/commands/synchronization/brigadier/StringArgumentSerializer$1.$SwitchMap$com$mojang$brigadier$arguments$StringArgumentType$StringType : [I
/*    */       //   3: aload_0
/*    */       //   4: getfield type : Lcom/mojang/brigadier/arguments/StringArgumentType$StringType;
/*    */       //   7: invokevirtual ordinal : ()I
/*    */       //   10: iaload
/*    */       //   11: tableswitch default -> 36, 1 -> 46, 2 -> 52, 3 -> 58
/*    */       //   36: new java/lang/MatchException
/*    */       //   39: dup
/*    */       //   40: aconst_null
/*    */       //   41: aconst_null
/*    */       //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*    */       //   45: athrow
/*    */       //   46: invokestatic word : ()Lcom/mojang/brigadier/arguments/StringArgumentType;
/*    */       //   49: goto -> 61
/*    */       //   52: invokestatic string : ()Lcom/mojang/brigadier/arguments/StringArgumentType;
/*    */       //   55: goto -> 61
/*    */       //   58: invokestatic greedyString : ()Lcom/mojang/brigadier/arguments/StringArgumentType;
/*    */       //   61: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       //   #20	-> 46
/*    */       //   #21	-> 52
/*    */       //   #22	-> 58
/*    */       //   #19	-> 61
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	62	0	this	Lnet/minecraft/commands/synchronization/brigadier/StringArgumentSerializer$Template;
/*    */       //   0	62	1	context	Lnet/minecraft/commands/CommandBuildContext;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public ArgumentTypeInfo<StringArgumentType, ?> type() {
/* 28 */       return StringArgumentSerializer.this;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 34 */     out.writeEnum((Enum)template.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 39 */     StringArgumentType.StringType type = (StringArgumentType.StringType)in.readEnum(StringArgumentType.StringType.class);
/* 40 */     return new Template(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {
/* 45 */     switch (template.type) { default: throw new MatchException(null, null);case SINGLE_WORD: case QUOTABLE_PHRASE: case GREEDY_PHRASE: break; }  out.addProperty("type", 
/*    */ 
/*    */         
/* 48 */         "greedy");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Template unpack(StringArgumentType argument) {
/* 54 */     return new Template(argument.getType());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/synchronization/brigadier/StringArgumentSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */