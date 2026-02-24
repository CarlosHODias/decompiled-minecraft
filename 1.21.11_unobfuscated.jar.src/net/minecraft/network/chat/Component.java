/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*     */ import net.minecraft.network.chat.contents.KeybindContents;
/*     */ import net.minecraft.network.chat.contents.NbtContents;
/*     */ import net.minecraft.network.chat.contents.ObjectContents;
/*     */ import net.minecraft.network.chat.contents.PlainTextContents;
/*     */ import net.minecraft.network.chat.contents.ScoreContents;
/*     */ import net.minecraft.network.chat.contents.SelectorContents;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Component
/*     */   extends Message, FormattedText
/*     */ {
/*     */   default String getString() {
/*  36 */     return super.getString();
/*     */   }
/*     */   
/*     */   default String getString(int limit) {
/*  40 */     StringBuilder builder = new StringBuilder();
/*  41 */     visit(contents -> {
/*     */           int remaining = limit - builder.length();
/*     */           if (remaining <= 0) {
/*     */             return STOP_ITERATION;
/*     */           }
/*     */           builder.append((contents.length() <= remaining) ? contents : contents.substring(0, remaining));
/*     */           return Optional.empty();
/*     */         });
/*  49 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default String tryCollapseToString() {
/*  55 */     ComponentContents componentContents = getContents(); if (componentContents instanceof PlainTextContents) { PlainTextContents text = (PlainTextContents)componentContents; if (getSiblings().isEmpty() && getStyle().isEmpty())
/*  56 */         return text.text();  }
/*     */     
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default MutableComponent plainCopy() {
/*  68 */     return MutableComponent.create(getContents());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default MutableComponent copy() {
/*  78 */     return new MutableComponent(getContents(), new ArrayList<>(getSiblings()), getStyle());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/*  85 */     Style selfStyle = getStyle().applyTo(parentStyle);
/*     */     
/*  87 */     Optional<T> selfResult = getContents().visit(output, selfStyle);
/*  88 */     if (selfResult.isPresent()) {
/*  89 */       return selfResult;
/*     */     }
/*     */     
/*  92 */     for (Component sibling : getSiblings()) {
/*  93 */       Optional<T> result = sibling.visit(output, selfStyle);
/*  94 */       if (result.isPresent()) {
/*  95 */         return result;
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 104 */     Optional<T> selfResult = getContents().visit(output);
/* 105 */     if (selfResult.isPresent()) {
/* 106 */       return selfResult;
/*     */     }
/*     */     
/* 109 */     for (Component sibling : getSiblings()) {
/* 110 */       Optional<T> result = sibling.visit(output);
/* 111 */       if (result.isPresent()) {
/* 112 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 116 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default List<Component> toFlatList() {
/* 120 */     return toFlatList(Style.EMPTY);
/*     */   }
/*     */   
/*     */   default List<Component> toFlatList(Style rootStyle) {
/* 124 */     List<Component> result = Lists.newArrayList();
/* 125 */     visit((style, contents) -> { if (!contents.isEmpty()) result.add(literal(contents).withStyle(style));  return Optional.empty(); }, rootStyle);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     return result;
/*     */   }
/*     */   
/*     */   default boolean contains(Component other) {
/* 135 */     if (equals(other)) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     List<Component> flat = toFlatList();
/* 140 */     List<Component> otherFlat = other.toFlatList(getStyle());
/* 141 */     return (Collections.indexOfSubList(flat, otherFlat) != -1);
/*     */   }
/*     */   
/*     */   static Component nullToEmpty(String text) {
/* 145 */     return (text != null) ? literal(text) : CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   static MutableComponent literal(String text) {
/* 149 */     return MutableComponent.create((ComponentContents)PlainTextContents.create(text));
/*     */   }
/*     */   
/*     */   static MutableComponent translatable(String key) {
/* 153 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, null, TranslatableContents.NO_ARGS));
/*     */   }
/*     */   
/*     */   static MutableComponent translatable(String key, Object... args) {
/* 157 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, null, args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MutableComponent translatableEscape(String key, Object... args) {
/* 164 */     for (int i = 0; i < args.length; i++) {
/* 165 */       Object arg = args[i];
/* 166 */       if (!TranslatableContents.isAllowedPrimitiveArgument(arg) && !(arg instanceof Component)) {
/* 167 */         args[i] = String.valueOf(arg);
/*     */       }
/*     */     } 
/* 170 */     return translatable(key, args);
/*     */   }
/*     */   
/*     */   static MutableComponent translatableWithFallback(String key, String fallback) {
/* 174 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, fallback, TranslatableContents.NO_ARGS));
/*     */   }
/*     */   
/*     */   static MutableComponent translatableWithFallback(String key, String fallback, Object... args) {
/* 178 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, fallback, args));
/*     */   }
/*     */   
/*     */   static MutableComponent empty() {
/* 182 */     return MutableComponent.create((ComponentContents)PlainTextContents.EMPTY);
/*     */   }
/*     */   
/*     */   static MutableComponent keybind(String name) {
/* 186 */     return MutableComponent.create((ComponentContents)new KeybindContents(name));
/*     */   }
/*     */   
/*     */   static MutableComponent nbt(String nbtPath, boolean interpreting, Optional<Component> separator, DataSource dataSource) {
/* 190 */     return MutableComponent.create((ComponentContents)new NbtContents(nbtPath, interpreting, separator, dataSource));
/*     */   }
/*     */   
/*     */   static MutableComponent score(SelectorPattern pattern, String objective) {
/* 194 */     return MutableComponent.create((ComponentContents)new ScoreContents(Either.left(pattern), objective));
/*     */   }
/*     */   
/*     */   static MutableComponent score(String name, String objective) {
/* 198 */     return MutableComponent.create((ComponentContents)new ScoreContents(Either.right(name), objective));
/*     */   }
/*     */   
/*     */   static MutableComponent selector(SelectorPattern pattern, Optional<Component> separator) {
/* 202 */     return MutableComponent.create((ComponentContents)new SelectorContents(pattern, separator));
/*     */   }
/*     */   
/*     */   static MutableComponent object(ObjectInfo info) {
/* 206 */     return MutableComponent.create((ComponentContents)new ObjectContents(info));
/*     */   }
/*     */ 
/*     */   
/*     */   static Component translationArg(Date date) {
/* 211 */     return literal(date.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(Message message) {
/* 215 */     Component component = (Component)message; return (message instanceof Component) ? component : literal(message.getString());
/*     */   }
/*     */   
/*     */   static Component translationArg(UUID uuid) {
/* 219 */     return literal(uuid.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(Identifier id) {
/* 223 */     return literal(id.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(ChunkPos chunkPos) {
/* 227 */     return literal(chunkPos.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(URI uri) {
/* 231 */     return literal(uri.toString());
/*     */   }
/*     */   
/*     */   Style getStyle();
/*     */   
/*     */   ComponentContents getContents();
/*     */   
/*     */   List<Component> getSiblings();
/*     */   
/*     */   FormattedCharSequence getVisualOrderText();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/Component.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */