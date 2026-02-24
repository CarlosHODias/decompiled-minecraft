/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.parsing.packrat.Atom;
/*     */ import net.minecraft.util.parsing.packrat.Dictionary;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ import net.minecraft.util.parsing.packrat.Rule;
/*     */ import net.minecraft.util.parsing.packrat.Scope;
/*     */ import net.minecraft.util.parsing.packrat.Term;
/*     */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*     */ import net.minecraft.util.parsing.packrat.commands.IdentifierParseRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*     */ import net.minecraft.util.parsing.packrat.commands.TagParseRule;
/*     */ 
/*     */ 
/*     */ public class ComponentPredicateParser
/*     */ {
/*     */   public static <T, C, P> Grammar<List<T>> createGrammar(Context<T, C, P> context) {
/*  34 */     Atom<List<T>> top = Atom.of("top");
/*  35 */     Atom<Optional<T>> type = Atom.of("type");
/*  36 */     Atom<Unit> anyType = Atom.of("any_type");
/*  37 */     Atom<T> elementType = Atom.of("element_type");
/*  38 */     Atom<T> tagType = Atom.of("tag_type");
/*  39 */     Atom<List<T>> conditions = Atom.of("conditions");
/*  40 */     Atom<List<T>> alternatives = Atom.of("alternatives");
/*  41 */     Atom<T> term = Atom.of("term");
/*  42 */     Atom<T> negation = Atom.of("negation");
/*  43 */     Atom<T> test = Atom.of("test");
/*  44 */     Atom<C> componentType = Atom.of("component_type");
/*  45 */     Atom<P> predicateType = Atom.of("predicate_type");
/*  46 */     Atom<Identifier> id = Atom.of("id");
/*  47 */     Atom<Dynamic<?>> tag = Atom.of("tag");
/*     */     
/*  49 */     Dictionary<StringReader> rules = new Dictionary();
/*     */     
/*  51 */     NamedRule<StringReader, Identifier> idRule = rules.put(id, IdentifierParseRule.INSTANCE);
/*     */     
/*  53 */     NamedRule<StringReader, List<T>> topRule = rules.put(top, 
/*  54 */         Term.alternative(new Term[] {
/*  55 */             Term.sequence(new Term[] { rules.named(type), StringReaderTerms.character('['), Term.cut(), Term.optional(rules.named(conditions)), StringReaderTerms.character(']')
/*  56 */               }), rules.named(type)
/*     */           }), scope -> {
/*     */           ImmutableList.Builder<T> builder = ImmutableList.builder();
/*     */           Objects.requireNonNull(builder);
/*     */           ((Optional)scope.getOrThrow(type)).ifPresent(builder::add);
/*     */           List<T> parsedConditions = (List<T>)scope.get(conditions);
/*     */           if (parsedConditions != null) {
/*     */             builder.addAll(parsedConditions);
/*     */           }
/*     */           return builder.build();
/*     */         });
/*  67 */     rules.put(type, Term.alternative(new Term[] {
/*  68 */             rules.named(elementType), 
/*  69 */             Term.sequence(new Term[] { StringReaderTerms.character('#'), Term.cut(), rules.named(tagType)
/*  70 */               }), rules.named(anyType)
/*     */           }), scope -> Optional.ofNullable(scope.getAny(new Atom[] { elementType, tagType })));
/*     */     
/*  73 */     rules.put(anyType, StringReaderTerms.character('*'), s -> Unit.INSTANCE);
/*  74 */     rules.put(elementType, (Rule)new ElementLookupRule<>(idRule, context));
/*  75 */     rules.put(tagType, (Rule)new TagLookupRule<>(idRule, context));
/*     */     
/*  77 */     rules.put(conditions, 
/*  78 */         Term.sequence(new Term[] {
/*  79 */             rules.named(alternatives), 
/*  80 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character(','), rules.named(conditions) }))
/*     */           }), scope -> {
/*     */           T parsedCondition = (T)context.anyOf((List)scope.getOrThrow(alternatives));
/*     */ 
/*     */ 
/*     */           
/*     */           return Optional.<List>ofNullable((List)scope.get(conditions)).map(()).orElse(List.of(parsedCondition));
/*     */         });
/*     */ 
/*     */     
/*  90 */     rules.put(alternatives, 
/*  91 */         Term.sequence(new Term[] {
/*  92 */             rules.named(term), 
/*  93 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character('|'), rules.named(alternatives) }))
/*     */           }), scope -> {
/*     */           T alternative = (T)scope.getOrThrow(term);
/*     */ 
/*     */ 
/*     */           
/*     */           return Optional.<List>ofNullable((List)scope.get(alternatives)).map(()).orElse(List.of(alternative));
/*     */         });
/*     */ 
/*     */     
/* 103 */     rules.put(term, 
/* 104 */         Term.alternative(new Term[] {
/* 105 */             rules.named(test), 
/* 106 */             Term.sequence(new Term[] { StringReaderTerms.character('!'), rules.named(negation) })
/*     */           }), scope -> scope.getAnyOrThrow(new Atom[] { test, negation }));
/*     */ 
/*     */ 
/*     */     
/* 111 */     rules.put(negation, 
/* 112 */         rules.named(test), scope -> context.negate(scope.getOrThrow(test)));
/*     */ 
/*     */ 
/*     */     
/* 116 */     rules.putComplex(test, 
/* 117 */         Term.alternative(new Term[] {
/* 118 */             Term.sequence(new Term[] { rules.named(componentType), StringReaderTerms.character('='), Term.cut(), rules.named(tag)
/* 119 */               }), Term.sequence(new Term[] { rules.named(predicateType), StringReaderTerms.character('~'), Term.cut(), rules.named(tag)
/* 120 */               }), rules.named(componentType)
/*     */           }), state -> {
/*     */           Scope scope = state.scope();
/*     */           
/*     */           P predicate = (P)scope.get(predicateType);
/*     */           
/*     */           try {
/*     */             if (predicate != null) {
/*     */               Dynamic<?> dynamic = (Dynamic)scope.getOrThrow(tag);
/*     */               
/*     */               return context.createPredicateTest((ImmutableStringReader)state.input(), predicate, dynamic);
/*     */             } 
/*     */             
/*     */             C component = (C)scope.getOrThrow(componentType);
/*     */             Dynamic<?> value = (Dynamic)scope.get(tag);
/*     */             return (value != null) ? context.createComponentTest((ImmutableStringReader)state.input(), component, value) : context.createComponentTest((ImmutableStringReader)state.input(), component);
/* 136 */           } catch (CommandSyntaxException e) {
/*     */             state.errorCollector().store(state.mark(), e);
/*     */             
/*     */             return null;
/*     */           } 
/*     */         });
/*     */     
/* 143 */     rules.put(componentType, (Rule)new ComponentLookupRule<>(idRule, context));
/* 144 */     rules.put(predicateType, (Rule)new PredicateLookupRule<>(idRule, context));
/* 145 */     rules.put(tag, (Rule)new TagParseRule((DynamicOps)NbtOps.INSTANCE));
/*     */     
/* 147 */     return new Grammar(rules, topRule);
/*     */   }
/*     */   
/*     */   private static class ElementLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, T> {
/*     */     private ElementLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 152 */       super(idParser, context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 157 */       return (T)((ComponentPredicateParser.Context)this.context).forElementType(reader, id);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 162 */       return ((ComponentPredicateParser.Context)this.context).listElementTypes();
/*     */     }
/*     */   } public static interface Context<T, C, P> { T forElementType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listElementTypes(); T forTagType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listTagTypes(); C lookupComponentType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listComponentTypes(); T createComponentTest(ImmutableStringReader param1ImmutableStringReader, C param1C, Dynamic<?> param1Dynamic) throws CommandSyntaxException; T createComponentTest(ImmutableStringReader param1ImmutableStringReader, C param1C); P lookupPredicateType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listPredicateTypes(); T createPredicateTest(ImmutableStringReader param1ImmutableStringReader, P param1P, Dynamic<?> param1Dynamic) throws CommandSyntaxException;
/*     */     T negate(T param1T);
/*     */     T anyOf(List<T> param1List); }
/*     */   private static class TagLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, T> { private TagLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 168 */       super(idParser, context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 173 */       return (T)((ComponentPredicateParser.Context)this.context).forTagType(reader, id);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 178 */       return ((ComponentPredicateParser.Context)this.context).listTagTypes();
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class ComponentLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, C> {
/*     */     private ComponentLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 184 */       super(idParser, context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected C validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 189 */       return (C)((ComponentPredicateParser.Context)this.context).lookupComponentType(reader, id);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 194 */       return ((ComponentPredicateParser.Context)this.context).listComponentTypes();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PredicateLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, P> {
/*     */     private PredicateLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 200 */       super(idParser, context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected P validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 205 */       return (P)((ComponentPredicateParser.Context)this.context).lookupPredicateType(reader, id);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 210 */       return ((ComponentPredicateParser.Context)this.context).listPredicateTypes();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/item/ComponentPredicateParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */