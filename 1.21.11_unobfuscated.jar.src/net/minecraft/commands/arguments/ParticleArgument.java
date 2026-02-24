/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleType;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class ParticleArgument implements com.mojang.brigadier.arguments.ArgumentType<ParticleOptions> {
/* 29 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "particle{foo:bar}" }); public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE; static {
/* 30 */     ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType(value -> Component.translatableEscape("particle.notFound", new Object[] { value }));
/* 31 */     ERROR_INVALID_OPTIONS = new DynamicCommandExceptionType(message -> Component.translatableEscape("particle.invalidOptions", new Object[] { message }));
/*    */   }
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_OPTIONS;
/*    */   private final HolderLookup.Provider registries;
/* 35 */   private static final TagParser<?> VALUE_PARSER = TagParser.create((DynamicOps)net.minecraft.nbt.NbtOps.INSTANCE);
/*    */   
/*    */   public ParticleArgument(CommandBuildContext context) {
/* 38 */     this.registries = (HolderLookup.Provider)context;
/*    */   }
/*    */   
/*    */   public static ParticleArgument particle(CommandBuildContext context) {
/* 42 */     return new ParticleArgument(context);
/*    */   }
/*    */   
/*    */   public static ParticleOptions getParticle(CommandContext<CommandSourceStack> context, String name) {
/* 46 */     return (ParticleOptions)context.getArgument(name, ParticleOptions.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleOptions parse(StringReader reader) throws CommandSyntaxException {
/* 51 */     return readParticle(reader, this.registries);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 56 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static ParticleOptions readParticle(StringReader reader, HolderLookup.Provider registries) throws CommandSyntaxException {
/* 60 */     ParticleType<?> type = readParticleType(reader, (HolderLookup<ParticleType<?>>)registries.lookupOrThrow(Registries.PARTICLE_TYPE));
/* 61 */     return (ParticleOptions)readParticle(VALUE_PARSER, reader, type, registries);
/*    */   }
/*    */   
/*    */   private static ParticleType<?> readParticleType(StringReader reader, HolderLookup<ParticleType<?>> particles) throws CommandSyntaxException {
/* 65 */     Identifier id = Identifier.read(reader);
/* 66 */     ResourceKey<ParticleType<?>> key = ResourceKey.create(Registries.PARTICLE_TYPE, id);
/* 67 */     return (ParticleType)((Holder.Reference)particles.get(key).orElseThrow(() -> ERROR_UNKNOWN_PARTICLE.createWithContext((ImmutableStringReader)reader, id))).value();
/*    */   }
/*    */   
/*    */   private static <T extends ParticleOptions, O> T readParticle(TagParser<O> parser, StringReader reader, ParticleType<T> type, HolderLookup.Provider registries) throws CommandSyntaxException {
/*    */     O extraData;
/* 72 */     RegistryOps<O> ops = registries.createSerializationContext(parser.getOps());
/* 73 */     if (reader.canRead() && reader.peek() == '{') {
/* 74 */       extraData = (O)parser.parseAsArgument(reader);
/*    */     } else {
/* 76 */       extraData = (O)ops.emptyMap();
/*    */     } 
/* 78 */     java.util.Objects.requireNonNull(ERROR_INVALID_OPTIONS); return (T)type.codec().codec().parse((DynamicOps)ops, extraData).getOrThrow(ERROR_INVALID_OPTIONS::create);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 83 */     HolderLookup.RegistryLookup<ParticleType<?>> particles = this.registries.lookupOrThrow(Registries.PARTICLE_TYPE);
/* 84 */     return SharedSuggestionProvider.suggestResource(particles.listElementIds().map(ResourceKey::identifier), builder);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ParticleArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */