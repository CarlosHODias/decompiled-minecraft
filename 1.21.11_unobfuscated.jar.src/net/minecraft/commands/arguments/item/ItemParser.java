/*     */ package net.minecraft.commands.arguments.item;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.PatchedDataComponentMap;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.item.Item;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class ItemParser {
/*     */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM;
/*     */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_COMPONENT;
/*     */   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_COMPONENT;
/*     */   
/*     */   static {
/*  41 */     ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(id -> Component.translatableEscape("argument.item.id.invalid", new Object[] { id }));
/*  42 */     ERROR_UNKNOWN_COMPONENT = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.component.unknown", new Object[] { id }));
/*  43 */     ERROR_MALFORMED_COMPONENT = new Dynamic2CommandExceptionType((type, message) -> Component.translatableEscape("arguments.item.component.malformed", new Object[] { type, message }));
/*  44 */   } private static final SimpleCommandExceptionType ERROR_EXPECTED_COMPONENT = new SimpleCommandExceptionType((Message)Component.translatable("arguments.item.component.expected")); private static final DynamicCommandExceptionType ERROR_REPEATED_COMPONENT; private static final DynamicCommandExceptionType ERROR_MALFORMED_ITEM; public static final char SYNTAX_START_COMPONENTS = '['; public static final char SYNTAX_END_COMPONENTS = ']'; public static final char SYNTAX_COMPONENT_SEPARATOR = ','; public static final char SYNTAX_COMPONENT_ASSIGNMENT = '='; public static final char SYNTAX_REMOVED_COMPONENT = '!'; static {
/*  45 */     ERROR_REPEATED_COMPONENT = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.component.repeated", new Object[] { id }));
/*  46 */     ERROR_MALFORMED_ITEM = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.malformed", new Object[] { id }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_NOTHING = SuggestionsBuilder::buildFuture;
/*     */   
/*     */   private final HolderLookup.RegistryLookup<Item> items;
/*     */   private final RegistryOps<Tag> registryOps;
/*     */   private final TagParser<Tag> tagParser;
/*     */   
/*     */   public ItemParser(HolderLookup.Provider registries) {
/*  61 */     this.items = registries.lookupOrThrow(Registries.ITEM);
/*  62 */     this.registryOps = registries.createSerializationContext((DynamicOps)net.minecraft.nbt.NbtOps.INSTANCE);
/*  63 */     this.tagParser = TagParser.create((DynamicOps)this.registryOps);
/*     */   }
/*     */   
/*     */   public ItemResult parse(StringReader reader) throws CommandSyntaxException {
/*  67 */     final MutableObject<Holder<Item>> itemResult = new MutableObject();
/*  68 */     final DataComponentPatch.Builder componentsBuilder = DataComponentPatch.builder();
/*  69 */     parse(reader, new Visitor(this)
/*     */         {
/*     */           public void visitItem(Holder<Item> item) {
/*  72 */             itemResult.setValue(item);
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> void visitComponent(DataComponentType<T> type, T value) {
/*  77 */             componentsBuilder.set(type, value);
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> void visitRemovedComponent(DataComponentType<T> type) {
/*  82 */             componentsBuilder.remove(type);
/*     */           }
/*     */         });
/*  85 */     Holder<Item> item = java.util.Objects.<Holder<Item>>requireNonNull((Holder<Item>)itemResult.get(), "Parser gave no item");
/*  86 */     DataComponentPatch components = componentsBuilder.build();
/*  87 */     validateComponents(reader, item, components);
/*  88 */     return new ItemResult(item, components);
/*     */   }
/*     */   
/*     */   private static void validateComponents(StringReader reader, Holder<Item> item, DataComponentPatch components) throws CommandSyntaxException {
/*  92 */     PatchedDataComponentMap patchedDataComponentMap = PatchedDataComponentMap.fromPatch(((Item)item.value()).components(), components);
/*  93 */     DataResult<Unit> result = net.minecraft.world.item.ItemStack.validateComponents((net.minecraft.core.component.DataComponentMap)patchedDataComponentMap);
/*  94 */     result.getOrThrow(error -> ERROR_MALFORMED_ITEM.createWithContext((ImmutableStringReader)reader, error));
/*     */   }
/*     */   
/*     */   public void parse(StringReader reader, Visitor visitor) throws CommandSyntaxException {
/*  98 */     int cursor = reader.getCursor();
/*     */     try {
/* 100 */       new State(reader, visitor).parse();
/* 101 */     } catch (CommandSyntaxException e) {
/* 102 */       reader.setCursor(cursor);
/* 103 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder) {
/* 108 */     StringReader reader = new StringReader(builder.getInput());
/* 109 */     reader.setCursor(builder.getStart());
/* 110 */     SuggestionsVisitor handler = new SuggestionsVisitor();
/* 111 */     State state = new State(reader, handler);
/*     */     try {
/* 113 */       state.parse();
/* 114 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*     */ 
/*     */     
/* 117 */     return handler.resolveSuggestions(builder, reader);
/*     */   }
/*     */   
/*     */   private class State {
/*     */     private final StringReader reader;
/*     */     private final ItemParser.Visitor visitor;
/*     */     
/*     */     private State(StringReader reader, ItemParser.Visitor visitor) {
/* 125 */       this.reader = reader;
/* 126 */       this.visitor = visitor;
/*     */     }
/*     */     
/*     */     public void parse() throws CommandSyntaxException {
/* 130 */       this.visitor.visitSuggestions(this::suggestItem);
/* 131 */       readItem();
/* 132 */       this.visitor.visitSuggestions(this::suggestStartComponents);
/* 133 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 134 */         this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 135 */         readComponents();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void readItem() throws CommandSyntaxException {
/* 140 */       int cursor = this.reader.getCursor();
/* 141 */       Identifier id = Identifier.read(this.reader);
/* 142 */       this.visitor.visitItem((Holder<Item>)ItemParser.this.items.get(ResourceKey.create(Registries.ITEM, id)).orElseThrow(() -> {
/*     */               this.reader.setCursor(cursor);
/*     */               return ItemParser.ERROR_UNKNOWN_ITEM.createWithContext((ImmutableStringReader)this.reader, id);
/*     */             }));
/*     */     }
/*     */     
/*     */     private void readComponents() throws CommandSyntaxException {
/* 149 */       this.reader.expect('[');
/*     */       
/* 151 */       this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
/*     */       
/* 153 */       ReferenceArraySet<DataComponentType<?>> referenceArraySet = new ReferenceArraySet();
/*     */       
/* 155 */       while (this.reader.canRead() && this.reader.peek() != ']') {
/* 156 */         this.reader.skipWhitespace();
/*     */         
/* 158 */         if (this.reader.canRead() && this.reader.peek() == '!') {
/* 159 */           this.reader.skip();
/* 160 */           this.visitor.visitSuggestions(this::suggestComponent);
/* 161 */           DataComponentType<?> componentType = readComponentType(this.reader);
/* 162 */           if (!referenceArraySet.add(componentType)) {
/* 163 */             throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType);
/*     */           }
/* 165 */           this.visitor.visitRemovedComponent(componentType);
/* 166 */           this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 167 */           this.reader.skipWhitespace();
/*     */         } else {
/* 169 */           DataComponentType<?> componentType = readComponentType(this.reader);
/* 170 */           if (!referenceArraySet.add(componentType)) {
/* 171 */             throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType);
/*     */           }
/*     */           
/* 174 */           this.visitor.visitSuggestions(this::suggestAssignment);
/*     */           
/* 176 */           this.reader.skipWhitespace();
/* 177 */           this.reader.expect('=');
/* 178 */           this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/*     */           
/* 180 */           this.reader.skipWhitespace();
/* 181 */           readComponent(ItemParser.this.tagParser, ItemParser.this.registryOps, componentType);
/* 182 */           this.reader.skipWhitespace();
/*     */         } 
/*     */         
/* 185 */         this.visitor.visitSuggestions(this::suggestNextOrEndComponents);
/*     */         
/* 187 */         if (!this.reader.canRead() || this.reader.peek() != ',') {
/*     */           break;
/*     */         }
/*     */         
/* 191 */         this.reader.skip();
/* 192 */         this.reader.skipWhitespace();
/* 193 */         this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
/*     */         
/* 195 */         if (!this.reader.canRead()) {
/* 196 */           throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(this.reader);
/*     */         }
/*     */       } 
/*     */       
/* 200 */       this.reader.expect(']');
/* 201 */       this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/*     */     }
/*     */     
/*     */     public static DataComponentType<?> readComponentType(StringReader reader) throws CommandSyntaxException {
/* 205 */       if (!reader.canRead()) {
/* 206 */         throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(reader);
/*     */       }
/*     */       
/* 209 */       int cursor = reader.getCursor();
/* 210 */       Identifier id = Identifier.read(reader);
/* 211 */       DataComponentType<?> component = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
/* 212 */       if (component == null || component.isTransient()) {
/* 213 */         reader.setCursor(cursor);
/* 214 */         throw ItemParser.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
/*     */       } 
/*     */       
/* 217 */       return component;
/*     */     }
/*     */     
/*     */     private <T, O> void readComponent(TagParser<O> tagParser, RegistryOps<O> registryOps, DataComponentType<T> componentType) throws CommandSyntaxException {
/* 221 */       int cursor = this.reader.getCursor();
/* 222 */       O tag = (O)tagParser.parseAsArgument(this.reader);
/* 223 */       DataResult<T> result = componentType.codecOrThrow().parse((DynamicOps)registryOps, tag);
/* 224 */       this.visitor.visitComponent(componentType, (T)result.getOrThrow(message -> {
/*     */               this.reader.setCursor(cursor);
/*     */               return ItemParser.ERROR_MALFORMED_COMPONENT.createWithContext((ImmutableStringReader)this.reader, cursor.toString(), componentType);
/*     */             }));
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestStartComponents(SuggestionsBuilder builder) {
/* 231 */       if (builder.getRemaining().isEmpty()) {
/* 232 */         builder.suggest(String.valueOf('['));
/*     */       }
/* 234 */       return builder.buildFuture();
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestNextOrEndComponents(SuggestionsBuilder builder) {
/* 238 */       if (builder.getRemaining().isEmpty()) {
/* 239 */         builder.suggest(String.valueOf(','));
/* 240 */         builder.suggest(String.valueOf(']'));
/*     */       } 
/* 242 */       return builder.buildFuture();
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestAssignment(SuggestionsBuilder builder) {
/* 246 */       if (builder.getRemaining().isEmpty()) {
/* 247 */         builder.suggest(String.valueOf('='));
/*     */       }
/* 249 */       return builder.buildFuture();
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestItem(SuggestionsBuilder builder) {
/* 253 */       return SharedSuggestionProvider.suggestResource(ItemParser.this.items.listElementIds().map(ResourceKey::identifier), builder);
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestComponentAssignmentOrRemoval(SuggestionsBuilder builder) {
/* 257 */       builder.suggest(String.valueOf('!'));
/* 258 */       return suggestComponent(builder, String.valueOf('='));
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder) {
/* 262 */       return suggestComponent(builder, "");
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder, String suffix) {
/* 266 */       String contents = builder.getRemaining().toLowerCase(java.util.Locale.ROOT);
/* 267 */       SharedSuggestionProvider.filterResources(BuiltInRegistries.DATA_COMPONENT_TYPE.entrySet(), contents, entry -> ((ResourceKey)entry.getKey()).identifier(), entry -> {
/*     */             DataComponentType<?> type = (DataComponentType)entry.getValue();
/*     */             if (type.codec() != null) {
/*     */               Identifier id = ((ResourceKey)entry.getKey()).identifier();
/*     */               builder.suggest(String.valueOf(id) + String.valueOf(id));
/*     */             } 
/*     */           });
/* 274 */       return builder.buildFuture();
/*     */     } }
/*     */   public static final class ItemResult extends Record { private final Holder<Item> item; private final DataComponentPatch components;
/*     */     
/* 278 */     public ItemResult(Holder<Item> item, DataComponentPatch components) { this.item = item; this.components = components; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemParser$ItemResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #278	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 278 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemParser$ItemResult; } public Holder<Item> item() { return this.item; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemParser$ItemResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #278	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemParser$ItemResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemParser$ItemResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #278	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemParser$ItemResult;
/* 278 */       //   0	8	1	o	Ljava/lang/Object; } public DataComponentPatch components() { return this.components; }
/*     */      } private static class SuggestionsVisitor implements Visitor { private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions;
/*     */     private SuggestionsVisitor() {
/* 281 */       this.suggestions = ItemParser.SUGGEST_NOTHING;
/*     */     }
/*     */     
/*     */     public void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {
/* 285 */       this.suggestions = suggestions;
/*     */     }
/*     */     
/*     */     public CompletableFuture<Suggestions> resolveSuggestions(SuggestionsBuilder builder, StringReader reader) {
/* 289 */       return this.suggestions.apply(builder.createOffset(reader.getCursor()));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface Visitor {
/*     */     default void visitItem(Holder<Item> item) {}
/*     */     
/*     */     default <T> void visitComponent(DataComponentType<T> type, T value) {}
/*     */     
/*     */     default <T> void visitRemovedComponent(DataComponentType<T> type) {}
/*     */     
/*     */     default void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {}
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/item/ItemParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */