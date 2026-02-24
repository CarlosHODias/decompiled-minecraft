/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.SnbtGrammar;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.util.parsing.packrat.commands.CommandArgumentParser;
/*    */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ComponentArgument extends ParserBasedArgument<Component> {
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_COMPONENT;
/* 25 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "\"hello world\"", "'hello world'", "\"\"", "{text:\"hello world\"}", "[\"\"]" }); static {
/* 26 */     ERROR_INVALID_COMPONENT = new DynamicCommandExceptionType(message -> Component.translatableEscape("argument.component.invalid", new Object[] { message }));
/*    */   }
/* 28 */   private static final DynamicOps<Tag> OPS = (DynamicOps<Tag>)NbtOps.INSTANCE;
/* 29 */   private static final CommandArgumentParser<Tag> TAG_PARSER = (CommandArgumentParser<Tag>)SnbtGrammar.createParser(OPS);
/*    */   
/*    */   private ComponentArgument(HolderLookup.Provider registries) {
/* 32 */     super(TAG_PARSER.withCodec((DynamicOps)
/* 33 */           registries.createSerializationContext(OPS), TAG_PARSER, ComponentSerialization.CODEC, ERROR_INVALID_COMPONENT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Component getRawComponent(CommandContext<CommandSourceStack> context, String name) {
/* 41 */     return (Component)context.getArgument(name, Component.class);
/*    */   }
/*    */   
/*    */   public static Component getResolvedComponent(CommandContext<CommandSourceStack> context, String name, Entity contentEntity) throws CommandSyntaxException {
/* 45 */     return (Component)ComponentUtils.updateForEntity((CommandSourceStack)context.getSource(), getRawComponent(context, name), contentEntity, 0);
/*    */   }
/*    */   
/*    */   public static Component getResolvedComponent(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 49 */     return getResolvedComponent(context, name, ((CommandSourceStack)context.getSource()).getEntity());
/*    */   }
/*    */   
/*    */   public static ComponentArgument textComponent(CommandBuildContext context) {
/* 53 */     return new ComponentArgument((HolderLookup.Provider)context);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 58 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ComponentArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */