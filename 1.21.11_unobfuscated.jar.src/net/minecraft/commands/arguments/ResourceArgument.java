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
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ public class ResourceArgument<T> implements ArgumentType<Holder.Reference<T>> {
/*     */   private static final DynamicCommandExceptionType ERROR_NOT_SUMMONABLE_ENTITY;
/*  37 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" }); public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_RESOURCE;
/*     */   static {
/*  39 */     ERROR_NOT_SUMMONABLE_ENTITY = new DynamicCommandExceptionType(value -> Component.translatableEscape("entity.not_summonable", new Object[] { value }));
/*     */     
/*  41 */     ERROR_UNKNOWN_RESOURCE = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource.not_found", new Object[] { id, registry }));
/*  42 */     ERROR_INVALID_RESOURCE_TYPE = new Dynamic3CommandExceptionType((id, actualRegistry, expectedRegistry) -> Component.translatableEscape("argument.resource.invalid_type", new Object[] { id, actualRegistry, expectedRegistry }));
/*     */   }
/*     */   public static final Dynamic3CommandExceptionType ERROR_INVALID_RESOURCE_TYPE; private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   private final HolderLookup<T> registryLookup;
/*     */   
/*     */   public ResourceArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
/*  48 */     this.registryKey = registryKey;
/*  49 */     this.registryLookup = (HolderLookup<T>)context.lookupOrThrow(registryKey);
/*     */   }
/*     */   
/*     */   public static <T> ResourceArgument<T> resource(CommandBuildContext context, ResourceKey<? extends Registry<T>> key) {
/*  53 */     return new ResourceArgument<>(context, key);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Holder.Reference<T> getResource(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey) throws CommandSyntaxException {
/*  58 */     Holder.Reference<T> argument = (Holder.Reference<T>)context.getArgument(name, Holder.Reference.class);
/*     */     
/*  60 */     ResourceKey<?> argumentKey = argument.key();
/*  61 */     if (argumentKey.isFor(registryKey)) {
/*  62 */       return argument;
/*     */     }
/*     */     
/*  65 */     throw ERROR_INVALID_RESOURCE_TYPE.create(argumentKey.identifier(), argumentKey.registry(), registryKey.identifier());
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Attribute> getAttribute(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  69 */     return getResource(context, name, Registries.ATTRIBUTE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  73 */     return getResource(context, name, Registries.CONFIGURED_FEATURE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  77 */     return getResource(context, name, Registries.STRUCTURE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<EntityType<?>> getEntityType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  81 */     return getResource(context, name, Registries.ENTITY_TYPE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<EntityType<?>> getSummonableEntityType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  85 */     Holder.Reference<EntityType<?>> result = getResource(context, name, Registries.ENTITY_TYPE);
/*  86 */     if (!((EntityType)result.value()).canSummon()) {
/*  87 */       throw ERROR_NOT_SUMMONABLE_ENTITY.create(result.key().identifier().toString());
/*     */     }
/*  89 */     return result;
/*     */   }
/*     */   
/*     */   public static Holder.Reference<net.minecraft.world.effect.MobEffect> getMobEffect(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  93 */     return getResource(context, name, Registries.MOB_EFFECT);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Enchantment> getEnchantment(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  97 */     return getResource(context, name, Registries.ENCHANTMENT);
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder.Reference<T> parse(StringReader reader) throws CommandSyntaxException {
/* 102 */     Identifier resourceId = Identifier.read(reader);
/* 103 */     ResourceKey<T> keyInRegistry = ResourceKey.create(this.registryKey, resourceId);
/* 104 */     return (Holder.Reference<T>)this.registryLookup.get(keyInRegistry).orElseThrow(() -> ERROR_UNKNOWN_RESOURCE.createWithContext((ImmutableStringReader)reader, resourceId, this.registryKey.identifier()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 109 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 114 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info<T> implements ArgumentTypeInfo<ResourceArgument<T>, Info<T>.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/*     */       private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 122 */         this.registryKey = registryKey;
/*     */       }
/*     */ 
/*     */       
/*     */       public ResourceArgument<T> instantiate(CommandBuildContext context) {
/* 127 */         return new ResourceArgument<>(context, this.registryKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
/* 132 */         return ResourceArgument.Info.this;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 138 */       out.writeResourceKey(template.registryKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 143 */       return new Template(in.readRegistryKey());
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 148 */       out.addProperty("registry", template.registryKey.identifier().toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public Template unpack(ResourceArgument<T> argument) {
/* 153 */       return new Template(argument.registryKey);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/*     */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceArgument<T> instantiate(CommandBuildContext context) {
/*     */       return new ResourceArgument<>(context, this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
/*     */       return ResourceArgument.Info.this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ResourceArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */