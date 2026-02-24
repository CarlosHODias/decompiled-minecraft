/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.SnbtGrammar;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.parsing.packrat.commands.CommandArgumentParser;
/*    */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*    */ 
/*    */ public class NbtTagArgument extends ParserBasedArgument<Tag> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "0b", "0l", "0.0", "\"foo\"", "{foo=bar}", "[0]" });
/*    */   
/* 16 */   private static final CommandArgumentParser<Tag> TAG_PARSER = (CommandArgumentParser<Tag>)SnbtGrammar.createParser((DynamicOps)NbtOps.INSTANCE);
/*    */   
/*    */   private NbtTagArgument() {
/* 19 */     super(TAG_PARSER);
/*    */   }
/*    */   
/*    */   public static NbtTagArgument nbtTag() {
/* 23 */     return new NbtTagArgument();
/*    */   }
/*    */   
/*    */   public static <S> Tag getNbtTag(CommandContext<S> context, String name) {
/* 27 */     return (Tag)context.getArgument(name, Tag.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 32 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/NbtTagArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */