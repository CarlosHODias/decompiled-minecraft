/*     */ package net.minecraft.commands.arguments;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public class ResourceOrTagArgument<T> implements ArgumentType<ResourceOrTagArgument.Result<T>> {
/*  34 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons" }); private static final Dynamic2CommandExceptionType ERROR_UNKNOWN_TAG; private static final Dynamic3CommandExceptionType ERROR_INVALID_TAG_TYPE; private final HolderLookup<T> registryLookup; private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   static {
/*  36 */     ERROR_UNKNOWN_TAG = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource_tag.not_found", new Object[] { id, registry }));
/*  37 */     ERROR_INVALID_TAG_TYPE = new Dynamic3CommandExceptionType((id, actualRegistry, expectedRegistry) -> Component.translatableEscape("argument.resource_tag.invalid_type", new Object[] { id, actualRegistry, expectedRegistry }));
/*     */   }
/*     */   
/*     */   private static final class ResourceResult<T>
/*     */     extends Record
/*     */     implements Result<T>
/*     */   {
/*     */     private final Holder.Reference<T> value;
/*     */     
/*     */     private ResourceResult(Holder.Reference<T> value) {
/*  47 */       this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  47 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; } public Holder.Reference<T> value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; } public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() {
/*  50 */       return Either.left(this.value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/*  56 */       return this.value.key().isFor(registryKey) ? (Optional)Optional.of(this) : Optional.<ResourceOrTagArgument.Result<E>>empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Holder<T> holder) {
/*  61 */       return holder.equals(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String asPrintable() {
/*  66 */       return this.value.key().identifier().toString();
/*     */     } }
/*     */   private static final class TagResult<T> extends Record implements Result<T> { private final HolderSet.Named<T> tag;
/*     */     
/*  70 */     private TagResult(HolderSet.Named<T> tag) { this.tag = tag; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  70 */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>; } public HolderSet.Named<T> tag() { return this.tag; }
/*     */     
/*     */     public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() {
/*  73 */       return Either.right(this.tag);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/*  79 */       return this.tag.key().isFor(registryKey) ? (Optional)Optional.of(this) : Optional.<ResourceOrTagArgument.Result<E>>empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Holder<T> holder) {
/*  84 */       return this.tag.contains(holder);
/*     */     }
/*     */ 
/*     */     
/*     */     public String asPrintable() {
/*  89 */       return "#" + String.valueOf(this.tag.key().location());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOrTagArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
/*  97 */     this.registryKey = registryKey;
/*  98 */     this.registryLookup = (HolderLookup<T>)context.lookupOrThrow(registryKey);
/*     */   }
/*     */   
/*     */   public static <T> ResourceOrTagArgument<T> resourceOrTag(CommandBuildContext context, ResourceKey<? extends Registry<T>> key) {
/* 102 */     return new ResourceOrTagArgument<>(context, key);
/*     */   }
/*     */   
/*     */   public static <T> Result<T> getResourceOrTag(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey) throws CommandSyntaxException {
/* 106 */     Result<?> argument = (Result)context.getArgument(name, Result.class);
/*     */     
/* 108 */     Optional<Result<T>> value = argument.cast(registryKey);
/* 109 */     return value.orElseThrow(() -> (CommandSyntaxException)argument.unwrap().map((), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Result<T> parse(StringReader reader) throws CommandSyntaxException {
/* 123 */     if (reader.canRead() && reader.peek() == '#') {
/* 124 */       int cursor = reader.getCursor();
/*     */       try {
/* 126 */         reader.skip();
/* 127 */         Identifier tagId = Identifier.read(reader);
/* 128 */         TagKey<T> tagKey = TagKey.create(this.registryKey, tagId);
/* 129 */         HolderSet.Named<T> holderSet = (HolderSet.Named<T>)this.registryLookup.get(tagKey).orElseThrow(() -> ERROR_UNKNOWN_TAG.createWithContext((ImmutableStringReader)reader, tagId, this.registryKey.identifier()));
/* 130 */         return new TagResult<>(holderSet);
/* 131 */       } catch (CommandSyntaxException e) {
/* 132 */         reader.setCursor(cursor);
/* 133 */         throw e;
/*     */       } 
/*     */     } 
/* 136 */     Identifier resourceId = Identifier.read(reader);
/* 137 */     ResourceKey<T> resourceKey = ResourceKey.create(this.registryKey, resourceId);
/* 138 */     Holder.Reference<T> holder = (Holder.Reference<T>)this.registryLookup.get(resourceKey).orElseThrow(() -> ResourceArgument.ERROR_UNKNOWN_RESOURCE.createWithContext((ImmutableStringReader)reader, resourceId, this.registryKey.identifier()));
/* 139 */     return new ResourceResult<>(holder);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 144 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ALL);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 149 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info<T> implements ArgumentTypeInfo<ResourceOrTagArgument<T>, Info<T>.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/*     */       private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 157 */         this.registryKey = registryKey;
/*     */       }
/*     */ 
/*     */       
/*     */       public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) {
/* 162 */         return new ResourceOrTagArgument<>(context, this.registryKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() {
/* 167 */         return ResourceOrTagArgument.Info.this;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 173 */       out.writeResourceKey(template.registryKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 178 */       return new Template(in.readRegistryKey());
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 183 */       out.addProperty("registry", template.registryKey.identifier().toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public Template unpack(ResourceOrTagArgument<T> argument) {
/* 188 */       return new Template(argument.registryKey);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/*     */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) {
/*     */       return new ResourceOrTagArgument<>(context, this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() {
/*     */       return ResourceOrTagArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Result<T> extends Predicate<Holder<T>> {
/*     */     Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap();
/*     */     
/*     */     <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> param1ResourceKey);
/*     */     
/*     */     String asPrintable();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ResourceOrTagArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */