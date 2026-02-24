/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.AdvancementTree;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ public class AdvancementCommands {
/*     */   private static final DynamicCommandExceptionType ERROR_NO_ACTION_PERFORMED;
/*     */   private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType ERROR_CRITERION_NOT_FOUND;
/*     */   
/*     */   static {
/*  34 */     ERROR_NO_ACTION_PERFORMED = new DynamicCommandExceptionType(msg -> (Component)msg);
/*  35 */     ERROR_CRITERION_NOT_FOUND = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((name, criterion) -> Component.translatableEscape("commands.advancement.criterionNotFound", new Object[] { name, criterion }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  38 */     dispatcher.register(
/*  39 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("advancement")
/*  40 */         .requires((java.util.function.Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  41 */         .then(
/*  42 */           Commands.literal("grant")
/*  43 */           .then((
/*  44 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  45 */             .then(
/*  46 */               Commands.literal("only")
/*  47 */               .then((
/*  48 */                 (RequiredArgumentBuilder)Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  49 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.ONLY))))
/*  50 */                 .then(
/*  51 */                   Commands.argument("criterion", (ArgumentType)StringArgumentType.greedyString())
/*  52 */                   .suggests((c, p) -> SharedSuggestionProvider.suggest(ResourceKeyArgument.getAdvancement(c, "advancement").value().criteria().keySet(), p))
/*  53 */                   .executes(c -> performCriterion((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, ResourceKeyArgument.getAdvancement(c, "advancement"), StringArgumentType.getString(c, "criterion")))))))
/*     */ 
/*     */ 
/*     */             
/*  57 */             .then(
/*  58 */               Commands.literal("from")
/*  59 */               .then(
/*  60 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  61 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.FROM))))))
/*     */ 
/*     */             
/*  64 */             .then(
/*  65 */               Commands.literal("until")
/*  66 */               .then(
/*  67 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  68 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.UNTIL))))))
/*     */ 
/*     */             
/*  71 */             .then(
/*  72 */               Commands.literal("through")
/*  73 */               .then(
/*  74 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  75 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.THROUGH))))))
/*     */ 
/*     */             
/*  78 */             .then(
/*  79 */               Commands.literal("everything")
/*  80 */               .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, ((CommandSourceStack)c.getSource()).getServer().getAdvancements().getAllAdvancements(), false))))))
/*     */ 
/*     */ 
/*     */         
/*  84 */         .then(
/*  85 */           Commands.literal("revoke")
/*  86 */           .then((
/*  87 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  88 */             .then(
/*  89 */               Commands.literal("only")
/*  90 */               .then((
/*  91 */                 (RequiredArgumentBuilder)Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  92 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.ONLY))))
/*  93 */                 .then(
/*  94 */                   Commands.argument("criterion", (ArgumentType)StringArgumentType.greedyString())
/*  95 */                   .suggests((c, p) -> SharedSuggestionProvider.suggest(ResourceKeyArgument.getAdvancement(c, "advancement").value().criteria().keySet(), p))
/*  96 */                   .executes(c -> performCriterion((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, ResourceKeyArgument.getAdvancement(c, "advancement"), StringArgumentType.getString(c, "criterion")))))))
/*     */ 
/*     */ 
/*     */             
/* 100 */             .then(
/* 101 */               Commands.literal("from")
/* 102 */               .then(
/* 103 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/* 104 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.FROM))))))
/*     */ 
/*     */             
/* 107 */             .then(
/* 108 */               Commands.literal("until")
/* 109 */               .then(
/* 110 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/* 111 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.UNTIL))))))
/*     */ 
/*     */             
/* 114 */             .then(
/* 115 */               Commands.literal("through")
/* 116 */               .then(
/* 117 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/* 118 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.THROUGH))))))
/*     */ 
/*     */             
/* 121 */             .then(
/* 122 */               Commands.literal("everything")
/* 123 */               .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, ((CommandSourceStack)c.getSource()).getServer().getAdvancements().getAllAdvancements()))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int perform(CommandSourceStack source, Collection<ServerPlayer> players, Action action, Collection<AdvancementHolder> advancements) throws CommandSyntaxException {
/* 131 */     return perform(source, players, action, advancements, true);
/*     */   }
/*     */   
/*     */   private static int perform(CommandSourceStack source, Collection<ServerPlayer> players, Action action, Collection<AdvancementHolder> advancements, boolean showAdvancements) throws CommandSyntaxException {
/* 135 */     int count = 0;
/* 136 */     for (ServerPlayer player : players) {
/* 137 */       count += action.perform(player, advancements, showAdvancements);
/*     */     }
/*     */     
/* 140 */     if (count == 0) {
/* 141 */       if (advancements.size() == 1) {
/* 142 */         if (players.size() == 1) {
/* 143 */           throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".one.to.one.failure", new Object[] { Advancement.name((AdvancementHolder)advancements.iterator().next()), ((ServerPlayer)players.iterator().next()).getDisplayName() }));
/*     */         }
/* 145 */         throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".one.to.many.failure", new Object[] { Advancement.name((AdvancementHolder)advancements.iterator().next()), players.size() }));
/*     */       } 
/*     */       
/* 148 */       if (players.size() == 1) {
/* 149 */         throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".many.to.one.failure", new Object[] { advancements.size(), ((ServerPlayer)players.iterator().next()).getDisplayName() }));
/*     */       }
/* 151 */       throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".many.to.many.failure", new Object[] { advancements.size(), players.size() }));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 156 */     if (advancements.size() == 1) {
/* 157 */       if (players.size() == 1) {
/* 158 */         source.sendSuccess(() -> Component.translatable(action.getKey() + ".one.to.one.success", new Object[] { Advancement.name(advancements.iterator().next()), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */       } else {
/* 160 */         source.sendSuccess(() -> Component.translatable(action.getKey() + ".one.to.many.success", new Object[] { Advancement.name(advancements.iterator().next()), players.size() }), true);
/*     */       }
/*     */     
/* 163 */     } else if (players.size() == 1) {
/* 164 */       source.sendSuccess(() -> Component.translatable(action.getKey() + ".many.to.one.success", new Object[] { advancements.size(), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 166 */       source.sendSuccess(() -> Component.translatable(action.getKey() + ".many.to.many.success", new Object[] { advancements.size(), players.size() }), true);
/*     */     } 
/*     */ 
/*     */     
/* 170 */     return count;
/*     */   }
/*     */   
/*     */   private static int performCriterion(CommandSourceStack source, Collection<ServerPlayer> players, Action action, AdvancementHolder holder, String criterion) throws CommandSyntaxException {
/* 174 */     int count = 0;
/*     */     
/* 176 */     Advancement advancement = holder.value();
/* 177 */     if (!advancement.criteria().containsKey(criterion)) {
/* 178 */       throw ERROR_CRITERION_NOT_FOUND.create(Advancement.name(holder), criterion);
/*     */     }
/*     */     
/* 181 */     for (ServerPlayer player : players) {
/* 182 */       if (action.performCriterion(player, holder, criterion)) {
/* 183 */         count++;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (count == 0) {
/* 188 */       if (players.size() == 1) {
/* 189 */         throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".criterion.to.one.failure", new Object[] { criterion, Advancement.name(holder), ((ServerPlayer)players.iterator().next()).getDisplayName() }));
/*     */       }
/* 191 */       throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".criterion.to.many.failure", new Object[] { criterion, Advancement.name(holder), players.size() }));
/*     */     } 
/*     */ 
/*     */     
/* 195 */     if (players.size() == 1) {
/* 196 */       source.sendSuccess(() -> Component.translatable(action.getKey() + ".criterion.to.one.success", new Object[] { criterion, Advancement.name(holder), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 198 */       source.sendSuccess(() -> Component.translatable(action.getKey() + ".criterion.to.many.success", new Object[] { criterion, Advancement.name(holder), players.size() }), true);
/*     */     } 
/*     */     
/* 201 */     return count;
/*     */   }
/*     */   
/*     */   private static List<AdvancementHolder> getAdvancements(CommandContext<CommandSourceStack> context, AdvancementHolder target, Mode mode) {
/* 205 */     AdvancementTree advancementTree = ((CommandSourceStack)context.getSource()).getServer().getAdvancements().tree();
/* 206 */     AdvancementNode targetNode = advancementTree.get(target);
/* 207 */     if (targetNode == null) {
/* 208 */       return List.of(target);
/*     */     }
/* 210 */     List<AdvancementHolder> advancements = new java.util.ArrayList<>();
/* 211 */     if (mode.parents) {
/* 212 */       AdvancementNode parent = targetNode.parent();
/* 213 */       while (parent != null) {
/* 214 */         advancements.add(parent.holder());
/* 215 */         parent = parent.parent();
/*     */       } 
/*     */     } 
/* 218 */     advancements.add(target);
/* 219 */     if (mode.children) {
/* 220 */       addChildren(targetNode, advancements);
/*     */     }
/* 222 */     return advancements;
/*     */   }
/*     */   
/*     */   private static void addChildren(AdvancementNode parent, List<AdvancementHolder> output) {
/* 226 */     for (AdvancementNode child : (Iterable<AdvancementNode>)parent.children()) {
/* 227 */       output.add(child.holder());
/* 228 */       addChildren(child, output);
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum Action {
/* 233 */     GRANT("grant")
/*     */     {
/*     */       protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 236 */         AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 237 */         if (progress.isDone()) {
/* 238 */           return false;
/*     */         }
/* 240 */         for (String criterion : (Iterable<String>)progress.getRemainingCriteria()) {
/* 241 */           player.getAdvancements().award(advancement, criterion);
/*     */         }
/* 243 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 248 */         return player.getAdvancements().award(advancement, criterion);
/*     */       }
/*     */     },
/* 251 */     REVOKE("revoke")
/*     */     {
/*     */       protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 254 */         AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 255 */         if (!progress.hasProgress()) {
/* 256 */           return false;
/*     */         }
/* 258 */         for (String criterion : (Iterable<String>)progress.getCompletedCriteria()) {
/* 259 */           player.getAdvancements().revoke(advancement, criterion);
/*     */         }
/* 261 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 266 */         return player.getAdvancements().revoke(advancement, criterion);
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     private final String key;
/*     */     
/*     */     Action(String key) {
/* 274 */       this.key = "commands.advancement." + key;
/*     */     }
/*     */     
/*     */     public int perform(ServerPlayer player, Iterable<AdvancementHolder> advancements, boolean showAdvancements) {
/* 278 */       int count = 0;
/* 279 */       if (!showAdvancements)
/*     */       {
/* 281 */         player.getAdvancements().flushDirty(player, true);
/*     */       }
/* 283 */       for (AdvancementHolder advancement : advancements) {
/* 284 */         if (perform(player, advancement)) {
/* 285 */           count++;
/*     */         }
/*     */       } 
/* 288 */       if (!showAdvancements) {
/* 289 */         player.getAdvancements().flushDirty(player, false);
/*     */       }
/* 291 */       return count;
/*     */     }
/*     */     
/*     */     protected abstract boolean perform(ServerPlayer param1ServerPlayer, AdvancementHolder param1AdvancementHolder);
/*     */     
/*     */     protected abstract boolean performCriterion(ServerPlayer param1ServerPlayer, AdvancementHolder param1AdvancementHolder, String param1String);
/*     */     
/*     */     protected String getKey() {
/* 299 */       return this.key;
/*     */     }
/*     */   } enum null { protected boolean perform(ServerPlayer player, AdvancementHolder advancement) { AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement); if (progress.isDone()) return false;  for (String criterion : (Iterable<String>)progress.getRemainingCriteria()) player.getAdvancements().award(advancement, criterion);  return true; } protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) { return player.getAdvancements().award(advancement, criterion); } } enum null {
/*     */     protected boolean perform(ServerPlayer player, AdvancementHolder advancement) { AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement); if (!progress.hasProgress()) return false;  for (String criterion : (Iterable<String>)progress.getCompletedCriteria()) player.getAdvancements().revoke(advancement, criterion);  return true; } protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) { return player.getAdvancements().revoke(advancement, criterion); }
/*     */   } private enum Mode {
/* 304 */     ONLY(false, false),
/* 305 */     THROUGH(true, true),
/* 306 */     FROM(false, true),
/* 307 */     UNTIL(true, false),
/* 308 */     EVERYTHING(true, true);
/*     */     
/*     */     private final boolean parents;
/*     */     
/*     */     private final boolean children;
/*     */     
/*     */     Mode(boolean parents, boolean children) {
/* 315 */       this.parents = parents;
/* 316 */       this.children = children;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/AdvancementCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */