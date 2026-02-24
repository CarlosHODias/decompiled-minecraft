/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.SnbtGrammar;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.util.parsing.packrat.Atom;
/*     */ import net.minecraft.util.parsing.packrat.Dictionary;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.Scope;
/*     */ import net.minecraft.util.parsing.packrat.Term;
/*     */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*     */ import net.minecraft.util.parsing.packrat.commands.IdentifierParseRule;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class ResourceOrIdArgument<T>
/*     */   implements ArgumentType<Holder<T>> {
/*  47 */   private static final Collection<String> EXAMPLES = List.of("foo", "foo:bar", "012", "{}", "true"); public static final DynamicCommandExceptionType ERROR_FAILED_TO_PARSE; public static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ELEMENT;
/*     */   static {
/*  49 */     ERROR_FAILED_TO_PARSE = new DynamicCommandExceptionType(error -> Component.translatableEscape("argument.resource_or_id.failed_to_parse", new Object[] { error }));
/*  50 */     ERROR_NO_SUCH_ELEMENT = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource_or_id.no_such_element", new Object[] { id, registry }));
/*     */   }
/*  52 */   public static final DynamicOps<Tag> OPS = (DynamicOps<Tag>)NbtOps.INSTANCE;
/*     */   
/*     */   private final HolderLookup.Provider registryLookup;
/*     */   private final Optional<? extends HolderLookup.RegistryLookup<T>> elementLookup;
/*     */   private final Codec<T> codec;
/*     */   private final Grammar<Result<T, Tag>> grammar;
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   protected ResourceOrIdArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
/*  61 */     this.registryLookup = (HolderLookup.Provider)context;
/*  62 */     this.elementLookup = context.lookup(registryKey);
/*  63 */     this.registryKey = registryKey;
/*  64 */     this.codec = codec;
/*     */     
/*  66 */     this.grammar = createGrammar(registryKey, OPS);
/*     */   }
/*     */   
/*     */   public static <T, O> Grammar<Result<T, O>> createGrammar(ResourceKey<? extends Registry<T>> registryKey, DynamicOps<O> ops) {
/*  70 */     Grammar<O> inlineValueGrammar = SnbtGrammar.createParser(ops);
/*     */     
/*  72 */     Dictionary<StringReader> rules = new Dictionary();
/*  73 */     Atom<Result<T, O>> result = Atom.of("result");
/*  74 */     Atom<Identifier> id = Atom.of("id");
/*  75 */     Atom<O> value = Atom.of("value");
/*     */     
/*  77 */     rules.put(id, IdentifierParseRule.INSTANCE);
/*  78 */     rules.put(value, inlineValueGrammar.top().value());
/*     */     
/*  80 */     NamedRule<StringReader, Result<T, O>> topRule = rules.put(result, 
/*  81 */         Term.alternative(new Term[] {
/*     */ 
/*     */             
/*  84 */             rules.named(id), 
/*  85 */             rules.named(value)
/*     */           }), scope -> {
/*     */           Identifier parsedId = (Identifier)scope.get(id);
/*     */           
/*     */           if (parsedId != null) {
/*     */             return new ReferenceResult(ResourceKey.create(registryKey, parsedId));
/*     */           }
/*     */           
/*     */           O parsedInline = (O)scope.getOrThrow(value);
/*     */           
/*     */           return new InlineResult(parsedInline);
/*     */         });
/*  97 */     return new Grammar(rules, topRule);
/*     */   }
/*     */   
/*     */   public static class LootTableArgument extends ResourceOrIdArgument<LootTable> {
/*     */     protected LootTableArgument(CommandBuildContext context) {
/* 102 */       super(context, Registries.LOOT_TABLE, LootTable.DIRECT_CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   public static LootTableArgument lootTable(CommandBuildContext context) {
/* 107 */     return new LootTableArgument(context);
/*     */   }
/*     */   
/*     */   public static Holder<LootTable> getLootTable(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 111 */     return getResource(context, name);
/*     */   }
/*     */   
/*     */   public static class LootModifierArgument extends ResourceOrIdArgument<LootItemFunction> {
/*     */     protected LootModifierArgument(CommandBuildContext context) {
/* 116 */       super(context, Registries.ITEM_MODIFIER, LootItemFunctions.ROOT_CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   public static LootModifierArgument lootModifier(CommandBuildContext context) {
/* 121 */     return new LootModifierArgument(context);
/*     */   }
/*     */   
/*     */   public static Holder<LootItemFunction> getLootModifier(CommandContext<CommandSourceStack> context, String name) {
/* 125 */     return getResource(context, name);
/*     */   }
/*     */   
/*     */   public static class LootPredicateArgument extends ResourceOrIdArgument<LootItemCondition> {
/*     */     protected LootPredicateArgument(CommandBuildContext context) {
/* 130 */       super(context, Registries.PREDICATE, LootItemCondition.DIRECT_CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   public static LootPredicateArgument lootPredicate(CommandBuildContext context) {
/* 135 */     return new LootPredicateArgument(context);
/*     */   }
/*     */   
/*     */   public static Holder<LootItemCondition> getLootPredicate(CommandContext<CommandSourceStack> context, String name) {
/* 139 */     return getResource(context, name);
/*     */   }
/*     */   
/*     */   public static class DialogArgument extends ResourceOrIdArgument<Dialog> {
/*     */     protected DialogArgument(CommandBuildContext context) {
/* 144 */       super(context, Registries.DIALOG, Dialog.DIRECT_CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   public static DialogArgument dialog(CommandBuildContext context) {
/* 149 */     return new DialogArgument(context);
/*     */   }
/*     */   
/*     */   public static Holder<Dialog> getDialog(CommandContext<CommandSourceStack> context, String name) {
/* 153 */     return getResource(context, name);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> Holder<T> getResource(CommandContext<CommandSourceStack> context, String name) {
/* 158 */     return (Holder<T>)context.getArgument(name, Holder.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<T> parse(StringReader reader) throws CommandSyntaxException {
/* 163 */     return parse(reader, this.grammar, OPS);
/*     */   }
/*     */   
/*     */   private <O> Holder<T> parse(StringReader reader, Grammar<Result<T, O>> grammar, DynamicOps<O> ops) throws CommandSyntaxException {
/* 167 */     Result<T, O> contents = (Result<T, O>)grammar.parseForCommands(reader);
/*     */     
/* 169 */     if (this.elementLookup.isEmpty())
/*     */     {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     return contents.parse((ImmutableStringReader)reader, this.registryLookup, ops, this.codec, this.elementLookup.get());
/*     */   }
/*     */   
/*     */   public static final class InlineResult<T, O> extends Record implements Result<T, O>
/*     */   {
/*     */     private final O value;
/*     */     
/* 181 */     public InlineResult(O value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 181 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>; } public O value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>; } public Holder<T> parse(ImmutableStringReader reader, HolderLookup.Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup.RegistryLookup<T> elementLookup) throws CommandSyntaxException {
/* 184 */       return Holder.direct(codec.parse((DynamicOps)lookup.createSerializationContext(ops), this.value)
/* 185 */           .getOrThrow(msg -> ResourceOrIdArgument.ERROR_FAILED_TO_PARSE.createWithContext(reader, msg)));
/*     */     } }
/*     */   public static final class ReferenceResult<T, O> extends Record implements Result<T, O> { private final ResourceKey<T> key;
/*     */     
/* 189 */     public ReferenceResult(ResourceKey<T> key) { this.key = key; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 189 */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>; } public ResourceKey<T> key() { return this.key; }
/*     */     
/*     */     public Holder<T> parse(ImmutableStringReader reader, HolderLookup.Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup.RegistryLookup<T> elementLookup) throws CommandSyntaxException {
/* 192 */       return (Holder<T>)elementLookup.get(this.key).orElseThrow(() -> ResourceOrIdArgument.ERROR_NO_SUCH_ELEMENT.createWithContext(reader, this.key.identifier(), this.key.registry()));
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 198 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 203 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static interface Result<T, O> {
/*     */     Holder<T> parse(ImmutableStringReader param1ImmutableStringReader, HolderLookup.Provider param1Provider, DynamicOps<O> param1DynamicOps, Codec<T> param1Codec, HolderLookup.RegistryLookup<T> param1RegistryLookup) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ResourceOrIdArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */