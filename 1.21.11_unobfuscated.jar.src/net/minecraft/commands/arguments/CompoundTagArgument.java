/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ 
/*    */ public class CompoundTagArgument
/*    */   implements ArgumentType<CompoundTag> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "{}", "{foo=bar}" });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CompoundTagArgument compoundTag() {
/* 20 */     return new CompoundTagArgument();
/*    */   }
/*    */   
/*    */   public static <S> CompoundTag getCompoundTag(CommandContext<S> context, String name) {
/* 24 */     return (CompoundTag)context.getArgument(name, CompoundTag.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag parse(StringReader reader) throws CommandSyntaxException {
/* 29 */     return TagParser.parseCompoundAsArgument(reader);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 34 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/CompoundTagArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */