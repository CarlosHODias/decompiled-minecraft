/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceOrTagKeyArgument<T>
/*     */   implements ArgumentType<ResourceOrTagKeyArgument.Result<T>>
/*     */ {
/*  34 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons" });
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private static final class ResourceResult<T>
/*     */     extends Record
/*     */     implements Result<T>
/*     */   {
/*     */     private final ResourceKey<T> key;
/*     */     
/*     */     private ResourceResult(ResourceKey<T> key) {
/*  44 */       this.key = key; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  44 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; } public ResourceKey<T> key() { return this.key; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; } public Either<ResourceKey<T>, TagKey<T>> unwrap() {
/*  47 */       return Either.left(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/*  52 */       return this.key.cast(registryKey).map(ResourceResult::new);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Holder<T> holder) {
/*  57 */       return holder.is(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public String asPrintable() {
/*  62 */       return this.key.identifier().toString();
/*     */     } }
/*     */   private static final class TagResult<T> extends Record implements Result<T> { private final TagKey<T> key;
/*     */     
/*  66 */     private TagResult(TagKey<T> key) { this.key = key; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  66 */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>; } public TagKey<T> key() { return this.key; }
/*     */     
/*     */     public Either<ResourceKey<T>, TagKey<T>> unwrap() {
/*  69 */       return Either.right(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/*  74 */       return this.key.cast(registryKey).map(TagResult::new);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Holder<T> holder) {
/*  79 */       return holder.is(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public String asPrintable() {
/*  84 */       return "#" + String.valueOf(this.key.location());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOrTagKeyArgument(ResourceKey<? extends Registry<T>> registryKey) {
/*  91 */     this.registryKey = registryKey;
/*     */   }
/*     */   
/*     */   public static <T> ResourceOrTagKeyArgument<T> resourceOrTagKey(ResourceKey<? extends Registry<T>> key) {
/*  95 */     return new ResourceOrTagKeyArgument<>(key);
/*     */   }
/*     */   
/*     */   public static <T> Result<T> getResourceOrTagKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
/*  99 */     Result<?> argument = (Result)context.getArgument(name, Result.class);
/*     */     
/* 101 */     Optional<Result<T>> value = argument.cast(registryKey);
/* 102 */     return value.orElseThrow(() -> exceptionType.create(argument));
/*     */   }
/*     */ 
/*     */   
/*     */   public Result<T> parse(StringReader reader) throws CommandSyntaxException {
/* 107 */     if (reader.canRead() && reader.peek() == '#') {
/* 108 */       int cursor = reader.getCursor();
/*     */       try {
/* 110 */         reader.skip();
/* 111 */         Identifier tagId = Identifier.read(reader);
/* 112 */         return new TagResult<>(TagKey.create(this.registryKey, tagId));
/* 113 */       } catch (CommandSyntaxException e) {
/* 114 */         reader.setCursor(cursor);
/* 115 */         throw e;
/*     */       } 
/*     */     } 
/* 118 */     Identifier resourceId = Identifier.read(reader);
/* 119 */     return new ResourceResult<>(ResourceKey.create(this.registryKey, resourceId));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 124 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ALL);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 129 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info<T> implements ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, Info<T>.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/*     */       private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 137 */         this.registryKey = registryKey;
/*     */       }
/*     */ 
/*     */       
/*     */       public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) {
/* 142 */         return new ResourceOrTagKeyArgument<>(this.registryKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
/* 147 */         return ResourceOrTagKeyArgument.Info.this;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 153 */       out.writeResourceKey(template.registryKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 158 */       return new Template(in.readRegistryKey());
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 163 */       out.addProperty("registry", template.registryKey.identifier().toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public Template unpack(ResourceOrTagKeyArgument<T> argument) {
/* 168 */       return new Template(argument.registryKey);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/*     */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) {
/*     */       return new ResourceOrTagKeyArgument<>(this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
/*     */       return ResourceOrTagKeyArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Result<T> extends Predicate<Holder<T>> {
/*     */     Either<ResourceKey<T>, TagKey<T>> unwrap();
/*     */     
/*     */     <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> param1ResourceKey);
/*     */     
/*     */     String asPrintable();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ResourceOrTagKeyArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */