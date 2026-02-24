/*      */ package net.minecraft.server.commands;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.brigadier.Command;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.Message;
/*      */ import com.mojang.brigadier.RedirectModifier;
/*      */ import com.mojang.brigadier.arguments.ArgumentType;
/*      */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*      */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*      */ import com.mojang.brigadier.context.CommandContext;
/*      */ import com.mojang.brigadier.context.ContextChain;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*      */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*      */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*      */ import com.mojang.brigadier.tree.CommandNode;
/*      */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.IntList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntPredicate;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*      */ import net.minecraft.commands.CommandBuildContext;
/*      */ import net.minecraft.commands.CommandResultCallback;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.ExecutionCommandSource;
/*      */ import net.minecraft.commands.FunctionInstantiationException;
/*      */ import net.minecraft.commands.arguments.DimensionArgument;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.commands.arguments.EntityArgument;
/*      */ import net.minecraft.commands.arguments.HeightmapTypeArgument;
/*      */ import net.minecraft.commands.arguments.IdentifierArgument;
/*      */ import net.minecraft.commands.arguments.NbtPathArgument;
/*      */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*      */ import net.minecraft.commands.arguments.RangeArgument;
/*      */ import net.minecraft.commands.arguments.ResourceArgument;
/*      */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*      */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*      */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*      */ import net.minecraft.commands.arguments.SlotsArgument;
/*      */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*      */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*      */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*      */ import net.minecraft.commands.execution.ChainModifiers;
/*      */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*      */ import net.minecraft.commands.execution.EntryAction;
/*      */ import net.minecraft.commands.execution.ExecutionControl;
/*      */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*      */ import net.minecraft.commands.execution.tasks.CallFunction;
/*      */ import net.minecraft.commands.execution.tasks.FallthroughTask;
/*      */ import net.minecraft.commands.execution.tasks.IsolatedCall;
/*      */ import net.minecraft.commands.functions.CommandFunction;
/*      */ import net.minecraft.commands.functions.InstantiatedFunction;
/*      */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.ByteTag;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.DoubleTag;
/*      */ import net.minecraft.nbt.FloatTag;
/*      */ import net.minecraft.nbt.IntTag;
/*      */ import net.minecraft.nbt.LongTag;
/*      */ import net.minecraft.nbt.ShortTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.bossevents.CustomBossEvent;
/*      */ import net.minecraft.server.commands.data.DataAccessor;
/*      */ import net.minecraft.server.commands.data.DataCommands;
/*      */ import net.minecraft.server.level.FullChunkStatus;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Stopwatch;
/*      */ import net.minecraft.world.Stopwatches;
/*      */ import net.minecraft.world.entity.Attackable;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.OwnableEntity;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.SlotProvider;
/*      */ import net.minecraft.world.entity.Targeting;
/*      */ import net.minecraft.world.entity.TraceableEntity;
/*      */ import net.minecraft.world.inventory.SlotRange;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ExecuteCommand
/*      */ {
/*  165 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int MAX_TEST_AREA = 32768; private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
/*      */   
/*      */   static {
/*  168 */     ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.execute.blocks.toobig", new Object[] { max, count }));
/*      */   }
/*  170 */   private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.execute.conditional.fail")); private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT; @VisibleForTesting
/*  171 */   public static final Dynamic2CommandExceptionType ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE; static { ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType(count -> Component.translatableEscape("commands.execute.conditional.fail_count", new Object[] { count }));
/*      */ 
/*      */     
/*  174 */     ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE = new Dynamic2CommandExceptionType((id, reason) -> Component.translatableEscape("commands.execute.function.instantiationFailure", new Object[] { id, reason })); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  187 */     LiteralCommandNode<CommandSourceStack> execute = dispatcher.register((LiteralArgumentBuilder)Commands.literal("execute")
/*  188 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)));
/*      */     
/*  190 */     dispatcher.register(
/*  191 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("execute")
/*  192 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  193 */         .then(
/*  194 */           Commands.literal("run")
/*  195 */           .redirect((CommandNode)dispatcher.getRoot())))
/*      */         
/*  197 */         .then(
/*  198 */           addConditionals((CommandNode<CommandSourceStack>)execute, Commands.literal("if"), true, context)))
/*      */         
/*  200 */         .then(
/*  201 */           addConditionals((CommandNode<CommandSourceStack>)execute, Commands.literal("unless"), false, context)))
/*      */         
/*  203 */         .then(
/*  204 */           Commands.literal("as")
/*  205 */           .then(
/*  206 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  207 */             .fork((CommandNode)execute, c -> {
/*      */                 List<CommandSourceStack> result = Lists.newArrayList();
/*      */ 
/*      */                 
/*      */                 for (Entity entity : (Iterable<Entity>)EntityArgument.getOptionalEntities(c, "targets")) {
/*      */                   result.add(((CommandSourceStack)c.getSource()).withEntity(entity));
/*      */                 }
/*      */                 
/*      */                 return result;
/*  216 */               })))).then(
/*  217 */           Commands.literal("at")
/*  218 */           .then(
/*  219 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  220 */             .fork((CommandNode)execute, c -> {
/*      */                 List<CommandSourceStack> result = Lists.newArrayList();
/*      */ 
/*      */                 
/*      */                 for (Entity entity : (Iterable<Entity>)EntityArgument.getOptionalEntities(c, "targets")) {
/*      */                   result.add(((CommandSourceStack)c.getSource()).withLevel((ServerLevel)entity.level()).withPosition(entity.position()).withRotation(entity.getRotationVector()));
/*      */                 }
/*      */                 
/*      */                 return result;
/*  229 */               })))).then((
/*  230 */           (LiteralArgumentBuilder)Commands.literal("store")
/*  231 */           .then(wrapStores(execute, Commands.literal("result"), true)))
/*  232 */           .then(wrapStores(execute, Commands.literal("success"), false))))
/*      */         
/*  234 */         .then((
/*  235 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("positioned")
/*  236 */           .then(
/*  237 */             Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/*  238 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withPosition(Vec3Argument.getVec3(c, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET))))
/*      */           
/*  240 */           .then(
/*  241 */             Commands.literal("as")
/*  242 */             .then(
/*  243 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  244 */               .fork((CommandNode)execute, c -> {
/*      */                   List<CommandSourceStack> result = Lists.newArrayList();
/*      */ 
/*      */                   
/*      */                   for (Entity entity : (Iterable<Entity>)EntityArgument.getOptionalEntities(c, "targets")) {
/*      */                     result.add(((CommandSourceStack)c.getSource()).withPosition(entity.position()));
/*      */                   }
/*      */                   
/*      */                   return result;
/*  253 */                 })))).then(
/*  254 */             Commands.literal("over")
/*  255 */             .then(
/*  256 */               Commands.argument("heightmap", (ArgumentType)HeightmapTypeArgument.heightmap())
/*  257 */               .redirect((CommandNode)execute, c -> {
/*      */                   Vec3 position = ((CommandSourceStack)c.getSource()).getPosition();
/*      */ 
/*      */                   
/*      */                   ServerLevel level = ((CommandSourceStack)c.getSource()).getLevel();
/*      */ 
/*      */                   
/*      */                   double x = position.x(), z = position.z();
/*      */ 
/*      */                   
/*      */                   if (!level.hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))) {
/*      */                     throw BlockPosArgument.ERROR_NOT_LOADED.create();
/*      */                   }
/*      */                   
/*      */                   int height = level.getHeight(HeightmapTypeArgument.getHeightmap(c, "heightmap"), Mth.floor(x), Mth.floor(z));
/*      */                   
/*      */                   return ((CommandSourceStack)c.getSource()).withPosition(new Vec3(x, height, z));
/*  274 */                 }))))).then((
/*  275 */           (LiteralArgumentBuilder)Commands.literal("rotated")
/*  276 */           .then(
/*  277 */             Commands.argument("rot", (ArgumentType)RotationArgument.rotation())
/*  278 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withRotation(RotationArgument.getRotation(c, "rot").getRotation((CommandSourceStack)c.getSource())))))
/*      */           
/*  280 */           .then(
/*  281 */             Commands.literal("as")
/*  282 */             .then(
/*  283 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  284 */               .fork((CommandNode)execute, c -> {
/*      */                   List<CommandSourceStack> result = Lists.newArrayList();
/*      */ 
/*      */                   
/*      */                   for (Entity entity : (Iterable<Entity>)EntityArgument.getOptionalEntities(c, "targets")) {
/*      */                     result.add(((CommandSourceStack)c.getSource()).withRotation(entity.getRotationVector()));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   return result;
/*  294 */                 }))))).then((
/*  295 */           (LiteralArgumentBuilder)Commands.literal("facing")
/*  296 */           .then(
/*  297 */             Commands.literal("entity")
/*  298 */             .then(
/*  299 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  300 */               .then(
/*  301 */                 Commands.argument("anchor", (ArgumentType)EntityAnchorArgument.anchor())
/*  302 */                 .fork((CommandNode)execute, c -> {
/*      */                     List<CommandSourceStack> result = Lists.newArrayList();
/*      */ 
/*      */                     
/*      */                     EntityAnchorArgument.Anchor anchor = EntityAnchorArgument.getAnchor(c, "anchor");
/*      */                     
/*      */                     for (Entity entity : (Iterable<Entity>)EntityArgument.getOptionalEntities(c, "targets")) {
/*      */                       result.add(((CommandSourceStack)c.getSource()).facing(entity, anchor));
/*      */                     }
/*      */                     
/*      */                     return result;
/*  313 */                   }))))).then(
/*  314 */             Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/*  315 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).facing(Vec3Argument.getVec3(c, "pos"))))))
/*      */ 
/*      */         
/*  318 */         .then(
/*  319 */           Commands.literal("align")
/*  320 */           .then(
/*  321 */             Commands.argument("axes", (ArgumentType)SwizzleArgument.swizzle())
/*  322 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withPosition(((CommandSourceStack)c.getSource()).getPosition().align(SwizzleArgument.getSwizzle(c, "axes")))))))
/*      */ 
/*      */         
/*  325 */         .then(
/*  326 */           Commands.literal("anchored")
/*  327 */           .then(
/*  328 */             Commands.argument("anchor", (ArgumentType)EntityAnchorArgument.anchor())
/*  329 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withAnchor(EntityAnchorArgument.getAnchor(c, "anchor"))))))
/*      */ 
/*      */         
/*  332 */         .then(
/*  333 */           Commands.literal("in")
/*  334 */           .then(
/*  335 */             Commands.argument("dimension", (ArgumentType)DimensionArgument.dimension())
/*  336 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withLevel(DimensionArgument.getDimension(c, "dimension"))))))
/*      */ 
/*      */         
/*  339 */         .then(
/*  340 */           Commands.literal("summon")
/*  341 */           .then(
/*  342 */             Commands.argument("entity", (ArgumentType)ResourceArgument.resource(context, Registries.ENTITY_TYPE))
/*  343 */             .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
/*  344 */             .redirect((CommandNode)execute, c -> spawnEntityAndRedirect((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"))))))
/*      */ 
/*      */         
/*  347 */         .then(
/*  348 */           (ArgumentBuilder)createRelationOperations((CommandNode<CommandSourceStack>)execute, Commands.literal("on"))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(LiteralCommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> literal, boolean storeResult) {
/*  354 */     literal.then(
/*  355 */         Commands.literal("score")
/*  356 */         .then(
/*  357 */           Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/*  358 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  359 */           .then(
/*  360 */             Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/*  361 */             .redirect((CommandNode)execute, c -> storeValue((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective"), storeResult)))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  366 */     literal.then(
/*  367 */         Commands.literal("bossbar")
/*  368 */         .then((
/*  369 */           (RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  370 */           .suggests(BossBarCommands.SUGGEST_BOSS_BAR)
/*  371 */           .then(
/*  372 */             Commands.literal("value")
/*  373 */             .redirect((CommandNode)execute, c -> storeValue((CommandSourceStack)c.getSource(), BossBarCommands.getBossBar(c), true, storeResult))))
/*      */           
/*  375 */           .then(
/*  376 */             Commands.literal("max")
/*  377 */             .redirect((CommandNode)execute, c -> storeValue((CommandSourceStack)c.getSource(), BossBarCommands.getBossBar(c), false, storeResult)))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  382 */     for (Iterator<DataCommands.DataProvider> iterator = DataCommands.TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) { DataCommands.DataProvider provider = iterator.next();
/*  383 */       provider.wrap((ArgumentBuilder)literal, p -> p.then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).then(Commands.literal("int").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("float").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("short").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("long").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("double").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("byte").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ()))))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  437 */     return (ArgumentBuilder)literal;
/*      */   }
/*      */   
/*      */   private static CommandSourceStack storeValue(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, boolean storeResult) {
/*  441 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*      */     
/*  443 */     return source.withCallback((success, result) -> { for (ScoreHolder name : (Iterable<ScoreHolder>)names) { ScoreAccess score = scoreboard.getOrCreatePlayerScore(name, objective); int value = storeResult ? result : (success ? 1 : 0); score.set(value); }  }, CommandResultCallback::chain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static CommandSourceStack storeValue(CommandSourceStack source, CustomBossEvent event, boolean storeIntoValue, boolean storeResult) {
/*  453 */     return source.withCallback((success, result) -> { int value = storeResult ? result : (success ? 1 : 0); if (storeIntoValue) { event.setValue(value); } else { event.setMax(value); }  }, CommandResultCallback::chain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static CommandSourceStack storeData(CommandSourceStack source, DataAccessor accessor, NbtPathArgument.NbtPath path, IntFunction<Tag> constructor, boolean storeResult) {
/*  464 */     return source.withCallback((success, result) -> {
/*      */           try {
/*      */             CompoundTag data = accessor.getData();
/*      */             int value = storeResult ? result : (success ? 1 : 0);
/*      */             path.set((Tag)data, constructor.apply(value));
/*      */             accessor.setData(data);
/*  470 */           } catch (CommandSyntaxException commandSyntaxException) {}
/*      */         }, CommandResultCallback::chain);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isChunkLoaded(ServerLevel level, BlockPos pos) {
/*  476 */     ChunkPos chunkPos = new ChunkPos(pos);
/*      */     
/*  478 */     LevelChunk chunk = level.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
/*  479 */     if (chunk != null) {
/*  480 */       return (chunk.getFullStatus() == FullChunkStatus.ENTITY_TICKING && level.areEntitiesLoaded(chunkPos.toLong()));
/*      */     }
/*  482 */     return false;
/*      */   }
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(CommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> parent, boolean expected, CommandBuildContext context) {
/*  486 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)
/*  487 */       parent.then(
/*  488 */         Commands.literal("block")
/*  489 */         .then(
/*  490 */           Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  491 */           .then(
/*  492 */             addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("block", (ArgumentType)BlockPredicateArgument.blockPredicate(context)), expected, c -> BlockPredicateArgument.getBlockPredicate(c, "block").test(new BlockInWorld((LevelReader)((CommandSourceStack)c.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(c, "pos"), true)))))))
/*      */ 
/*      */ 
/*      */       
/*  496 */       .then(
/*  497 */         Commands.literal("biome")
/*  498 */         .then(
/*  499 */           Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  500 */           .then(
/*  501 */             addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("biome", (ArgumentType)ResourceOrTagArgument.resourceOrTag(context, Registries.BIOME)), expected, c -> ResourceOrTagArgument.getResourceOrTag(c, "biome", Registries.BIOME).test(((CommandSourceStack)c.getSource()).getLevel().getBiome(BlockPosArgument.getLoadedBlockPos(c, "pos"))))))))
/*      */ 
/*      */ 
/*      */       
/*  505 */       .then(
/*  506 */         Commands.literal("loaded")
/*  507 */         .then(
/*  508 */           addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()), expected, c -> isChunkLoaded(((CommandSourceStack)c.getSource()).getLevel(), BlockPosArgument.getBlockPos(c, "pos"))))))
/*      */ 
/*      */       
/*  511 */       .then(
/*  512 */         Commands.literal("dimension")
/*  513 */         .then(
/*  514 */           addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("dimension", (ArgumentType)DimensionArgument.dimension()), expected, c -> (DimensionArgument.getDimension(c, "dimension") == ((CommandSourceStack)c.getSource()).getLevel())))))
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  519 */       .then(
/*  520 */         Commands.literal("score")
/*  521 */         .then(
/*  522 */           Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  523 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  524 */           .then((
/*  525 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targetObjective", (ArgumentType)ObjectiveArgument.objective())
/*  526 */             .then(
/*  527 */               Commands.literal("=")
/*  528 */               .then(
/*  529 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  530 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  531 */                 .then(
/*  532 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  536 */             .then(
/*  537 */               Commands.literal("<")
/*  538 */               .then(
/*  539 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  540 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  541 */                 .then(
/*  542 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  546 */             .then(
/*  547 */               Commands.literal("<=")
/*  548 */               .then(
/*  549 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  550 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  551 */                 .then(
/*  552 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  556 */             .then(
/*  557 */               Commands.literal(">")
/*  558 */               .then(
/*  559 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  560 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  561 */                 .then(
/*  562 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  566 */             .then(
/*  567 */               Commands.literal(">=")
/*  568 */               .then(
/*  569 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  570 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  571 */                 .then(
/*  572 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  576 */             .then(
/*  577 */               Commands.literal("matches")
/*  578 */               .then(
/*  579 */                 addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("range", (ArgumentType)RangeArgument.intRange()), expected, c -> checkScore(c, RangeArgument.Ints.getRange(c, "range")))))))))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  585 */       .then(
/*  586 */         Commands.literal("blocks")
/*  587 */         .then(
/*  588 */           Commands.argument("start", (ArgumentType)BlockPosArgument.blockPos())
/*  589 */           .then(
/*  590 */             Commands.argument("end", (ArgumentType)BlockPosArgument.blockPos())
/*  591 */             .then((
/*  592 */               (RequiredArgumentBuilder)Commands.argument("destination", (ArgumentType)BlockPosArgument.blockPos())
/*  593 */               .then(
/*  594 */                 addIfBlocksConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("all"), expected, false)))
/*      */               
/*  596 */               .then(
/*  597 */                 addIfBlocksConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("masked"), expected, true)))))))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       .then(
/*  604 */         Commands.literal("entity")
/*  605 */         .then((
/*  606 */           (RequiredArgumentBuilder)Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/*  607 */           .fork(execute, c -> expect(c, expected, !EntityArgument.getOptionalEntities(c, "entities").isEmpty())))
/*  608 */           .executes(createNumericConditionalHandler(expected, c -> EntityArgument.getOptionalEntities(c, "entities").size())))))
/*      */ 
/*      */ 
/*      */       
/*  612 */       .then(
/*  613 */         Commands.literal("predicate")
/*  614 */         .then(
/*  615 */           addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("predicate", (ArgumentType)ResourceOrIdArgument.lootPredicate(context)), expected, c -> checkCustomPredicate((CommandSourceStack)c.getSource(), ResourceOrIdArgument.getLootPredicate(c, "predicate"))))))
/*      */ 
/*      */       
/*  618 */       .then(
/*  619 */         Commands.literal("function")
/*  620 */         .then(
/*  621 */           Commands.argument("name", (ArgumentType)FunctionArgument.functions())
/*  622 */           .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  623 */           .fork(execute, (RedirectModifier)new ExecuteIfFunctionCustomModifier(expected)))))
/*      */ 
/*      */       
/*  626 */       .then((
/*  627 */         (LiteralArgumentBuilder)Commands.literal("items")
/*  628 */         .then(
/*  629 */           Commands.literal("entity")
/*  630 */           .then(
/*  631 */             Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/*  632 */             .then(
/*  633 */               Commands.argument("slots", (ArgumentType)SlotsArgument.slots())
/*  634 */               .then((
/*  635 */                 (RequiredArgumentBuilder)Commands.argument("item_predicate", (ArgumentType)ItemPredicateArgument.itemPredicate(context))
/*  636 */                 .fork(execute, c -> expect(c, expected, (countItems(EntityArgument.getEntities(c, "entities"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate")) > 0))))
/*  637 */                 .executes(createNumericConditionalHandler(expected, c -> countItems(EntityArgument.getEntities(c, "entities"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate")))))))))
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  642 */         .then(
/*  643 */           Commands.literal("block")
/*  644 */           .then(
/*  645 */             Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  646 */             .then(
/*  647 */               Commands.argument("slots", (ArgumentType)SlotsArgument.slots())
/*  648 */               .then((
/*  649 */                 (RequiredArgumentBuilder)Commands.argument("item_predicate", (ArgumentType)ItemPredicateArgument.itemPredicate(context))
/*  650 */                 .fork(execute, c -> expect(c, expected, (countItems((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate")) > 0))))
/*  651 */                 .executes(createNumericConditionalHandler(expected, c -> countItems((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate"))))))))))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  657 */       .then(
/*  658 */         Commands.literal("stopwatch")
/*  659 */         .then(
/*  660 */           Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  661 */           .suggests(StopwatchCommand.SUGGEST_STOPWATCHES)
/*  662 */           .then(
/*  663 */             addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("range", (ArgumentType)RangeArgument.floatRange()), expected, c -> checkStopwatch(c, RangeArgument.Floats.getRange(c, "range"))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  669 */     for (DataCommands.DataProvider provider : (Iterable<DataCommands.DataProvider>)DataCommands.SOURCE_PROVIDERS)
/*      */     {
/*  671 */       parent.then(
/*  672 */           provider.wrap((ArgumentBuilder)Commands.literal("data"), p -> p.then(((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).fork(execute, ())).executes(createNumericConditionalHandler(expected, ())))));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  682 */     return (ArgumentBuilder)parent;
/*      */   }
/*      */   
/*      */   private static int countItems(Iterable<? extends SlotProvider> sources, SlotRange slotRange, Predicate<ItemStack> predicate) {
/*  686 */     int count = 0;
/*  687 */     for (SlotProvider slotProvider : sources) {
/*  688 */       IntList slots = slotRange.slots();
/*  689 */       for (int i = 0; i < slots.size(); i++) {
/*  690 */         int slotId = slots.getInt(i);
/*  691 */         SlotAccess slot = slotProvider.getSlot(slotId);
/*  692 */         if (slot != null) {
/*  693 */           ItemStack contents = slot.get();
/*  694 */           if (predicate.test(contents)) {
/*  695 */             count += contents.getCount();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  700 */     return count;
/*      */   }
/*      */   
/*      */   private static int countItems(CommandSourceStack source, BlockPos pos, SlotRange slotRange, Predicate<ItemStack> predicate) throws CommandSyntaxException {
/*  704 */     int count = 0;
/*  705 */     Container container = ItemCommands.getContainer(source, pos, ItemCommands.ERROR_SOURCE_NOT_A_CONTAINER);
/*  706 */     int containerSize = container.getContainerSize();
/*      */     
/*  708 */     IntList slots = slotRange.slots();
/*  709 */     for (int i = 0; i < slots.size(); i++) {
/*  710 */       int slotId = slots.getInt(i);
/*  711 */       if (slotId >= 0 && slotId < containerSize) {
/*      */ 
/*      */         
/*  714 */         ItemStack contents = container.getItem(slotId);
/*  715 */         if (predicate.test(contents)) {
/*  716 */           count += contents.getCount();
/*      */         }
/*      */       } 
/*      */     } 
/*  720 */     return count;
/*      */   }
/*      */   
/*      */   private static Command<CommandSourceStack> createNumericConditionalHandler(boolean expected, CommandNumericPredicate condition) {
/*  724 */     if (expected) {
/*  725 */       return c -> {
/*      */           int count = condition.test(c);
/*      */           
/*      */           if (count > 0) {
/*      */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*      */             return count;
/*      */           } 
/*      */           throw ERROR_CONDITIONAL_FAILED.create();
/*      */         };
/*      */     }
/*  735 */     return c -> {
/*      */         int count = condition.test(c);
/*      */         if (count == 0) {
/*      */           ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*      */           return 1;
/*      */         } 
/*      */         throw ERROR_CONDITIONAL_FAILED_COUNT.create(count);
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int checkMatchingData(DataAccessor accessor, NbtPathArgument.NbtPath path) throws CommandSyntaxException {
/*  748 */     return path.countMatching((Tag)accessor.getData());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkScore(CommandContext<CommandSourceStack> context, IntBiPredicate operation) throws CommandSyntaxException {
/*  757 */     ScoreHolder target = ScoreHolderArgument.getName(context, "target");
/*  758 */     Objective targetObjective = ObjectiveArgument.getObjective(context, "targetObjective");
/*  759 */     ScoreHolder source = ScoreHolderArgument.getName(context, "source");
/*  760 */     Objective sourceObjective = ObjectiveArgument.getObjective(context, "sourceObjective");
/*      */     
/*  762 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/*      */     
/*  764 */     ReadOnlyScoreInfo a = serverScoreboard.getPlayerScoreInfo(target, targetObjective);
/*  765 */     ReadOnlyScoreInfo b = serverScoreboard.getPlayerScoreInfo(source, sourceObjective);
/*      */     
/*  767 */     if (a == null || b == null) {
/*  768 */       return false;
/*      */     }
/*      */     
/*  771 */     return operation.test(a.value(), b.value());
/*      */   }
/*      */   
/*      */   private static boolean checkScore(CommandContext<CommandSourceStack> context, MinMaxBounds.Ints range) throws CommandSyntaxException {
/*  775 */     ScoreHolder target = ScoreHolderArgument.getName(context, "target");
/*  776 */     Objective targetObjective = ObjectiveArgument.getObjective(context, "targetObjective");
/*      */     
/*  778 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/*      */     
/*  780 */     ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(target, targetObjective);
/*      */     
/*  782 */     if (scoreInfo == null) {
/*  783 */       return false;
/*      */     }
/*      */     
/*  786 */     return range.matches(scoreInfo.value());
/*      */   }
/*      */   
/*      */   private static boolean checkStopwatch(CommandContext<CommandSourceStack> context, MinMaxBounds.Doubles range) throws CommandSyntaxException {
/*  790 */     Identifier id = IdentifierArgument.getId(context, "id");
/*  791 */     Stopwatches stopwatches = ((CommandSourceStack)context.getSource()).getServer().getStopwatches();
/*  792 */     Stopwatch stopwatch = stopwatches.get(id);
/*  793 */     if (stopwatch == null) {
/*  794 */       throw StopwatchCommand.ERROR_DOES_NOT_EXIST.create(id);
/*      */     }
/*  796 */     long currentTime = Stopwatches.currentTime();
/*  797 */     double elapsedSeconds = stopwatch.elapsedSeconds(currentTime);
/*  798 */     return range.matches(elapsedSeconds);
/*      */   }
/*      */   
/*      */   private static boolean checkCustomPredicate(CommandSourceStack source, Holder<LootItemCondition> predicate) {
/*  802 */     ServerLevel level = source.getLevel();
/*      */     
/*  804 */     LootParams lootParams = new LootParams.Builder(level)
/*  805 */       .withParameter(LootContextParams.ORIGIN, source.getPosition())
/*  806 */       .withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity())
/*  807 */       .create(LootContextParamSets.COMMAND);
/*  808 */     LootContext context = new LootContext.Builder(lootParams).create(Optional.empty());
/*  809 */     context.pushVisitedElement(LootContext.createVisitedEntry((LootItemCondition)predicate.value()));
/*  810 */     return ((LootItemCondition)predicate.value()).test(context);
/*      */   }
/*      */   
/*      */   private static Collection<CommandSourceStack> expect(CommandContext<CommandSourceStack> context, boolean expected, boolean result) {
/*  814 */     if (result == expected) {
/*  815 */       return Collections.singleton((CommandSourceStack)context.getSource());
/*      */     }
/*  817 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> argument, boolean expected, CommandPredicate predicate) {
/*  822 */     return 
/*  823 */       argument.fork(root, c -> expect(c, expected, predicate.test(c)))
/*  824 */       .executes(c -> {
/*      */           if (expected == predicate.test(c)) {
/*      */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*      */             return 1;
/*      */           } 
/*      */           throw ERROR_CONDITIONAL_FAILED.create();
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> argument, boolean expected, boolean skipAir) {
/*  835 */     return 
/*  836 */       argument.fork(root, c -> expect(c, expected, checkRegions(c, skipAir).isPresent()))
/*  837 */       .executes(expected ? (c -> checkIfRegions(c, skipAir)) : (c -> checkUnlessRegions(c, skipAir)));
/*      */   }
/*      */   
/*      */   private static int checkIfRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  841 */     OptionalInt count = checkRegions(context, skipAir);
/*  842 */     if (count.isPresent()) {
/*  843 */       ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", new Object[] { count.getAsInt() }), false);
/*  844 */       return count.getAsInt();
/*      */     } 
/*  846 */     throw ERROR_CONDITIONAL_FAILED.create();
/*      */   }
/*      */ 
/*      */   
/*      */   private static int checkUnlessRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  851 */     OptionalInt count = checkRegions(context, skipAir);
/*  852 */     if (count.isPresent()) {
/*  853 */       throw ERROR_CONDITIONAL_FAILED_COUNT.create(count.getAsInt());
/*      */     }
/*  855 */     ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
/*  856 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static OptionalInt checkRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  861 */     return checkRegions(((CommandSourceStack)context.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(context, "start"), BlockPosArgument.getLoadedBlockPos(context, "end"), BlockPosArgument.getLoadedBlockPos(context, "destination"), skipAir);
/*      */   }
/*      */   
/*      */   private static OptionalInt checkRegions(ServerLevel level, BlockPos startPos, BlockPos endPos, BlockPos destPos, boolean skipAir) throws CommandSyntaxException {
/*  865 */     BoundingBox from = BoundingBox.fromCorners((Vec3i)startPos, (Vec3i)endPos);
/*  866 */     BoundingBox destination = BoundingBox.fromCorners((Vec3i)destPos, (Vec3i)destPos.offset(from.getLength()));
/*  867 */     BlockPos offset = new BlockPos(destination.minX() - from.minX(), destination.minY() - from.minY(), destination.minZ() - from.minZ());
/*  868 */     int area = from.getXSpan() * from.getYSpan() * from.getZSpan();
/*      */     
/*  870 */     if (area > 32768) {
/*  871 */       throw ERROR_AREA_TOO_LARGE.create(32768, area);
/*      */     }
/*      */     
/*  874 */     int count = 0;
/*  875 */     RegistryAccess registryAccess = level.registryAccess();
/*  876 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/*  877 */     try { for (int z = from.minZ(); z <= from.maxZ(); z++)
/*  878 */       { for (int y = from.minY(); y <= from.maxY(); y++)
/*  879 */         { for (int x = from.minX(); x <= from.maxX(); x++)
/*  880 */           { BlockPos sourcePos = new BlockPos(x, y, z);
/*  881 */             BlockPos destinationPos = sourcePos.offset((Vec3i)offset);
/*      */             
/*  883 */             BlockState sourceBlock = level.getBlockState(sourcePos);
/*  884 */             if (!skipAir || !sourceBlock.is(Blocks.AIR))
/*      */             
/*      */             { 
/*      */               
/*  888 */               if (sourceBlock != level.getBlockState(destinationPos))
/*  889 */               { OptionalInt optionalInt = OptionalInt.empty();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  921 */                 reporter.close(); return optionalInt; }  BlockEntity sourceBlockEntity = level.getBlockEntity(sourcePos); BlockEntity destinationBlockEntity = level.getBlockEntity(destinationPos); if (sourceBlockEntity != null) { if (destinationBlockEntity == null) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  if (destinationBlockEntity.getType() != sourceBlockEntity.getType()) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  if (!sourceBlockEntity.components().equals(destinationBlockEntity.components())) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  TagValueOutput sourceOutput = TagValueOutput.createWithContext(reporter.forChild(sourceBlockEntity.problemPath()), (HolderLookup.Provider)registryAccess); sourceBlockEntity.saveCustomOnly((ValueOutput)sourceOutput); CompoundTag sourceTag = sourceOutput.buildResult(); TagValueOutput destinationOutput = TagValueOutput.createWithContext(reporter.forChild(destinationBlockEntity.problemPath()), (HolderLookup.Provider)registryAccess); destinationBlockEntity.saveCustomOnly((ValueOutput)destinationOutput); CompoundTag destinationTag = destinationOutput.buildResult(); if (!sourceTag.equals(destinationTag)) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  }  count++; }  }  }  }  reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/*  923 */      return OptionalInt.of(count);
/*      */   }
/*      */   
/*      */   private static RedirectModifier<CommandSourceStack> expandOneToOneEntityRelation(Function<Entity, Optional<Entity>> unpacker) {
/*  927 */     return context -> {
/*      */         CommandSourceStack source = (CommandSourceStack)context.getSource();
/*      */         Entity entity = source.getEntity();
/*      */         return (entity == null) ? List.of() : ((Optional)unpacker.apply(entity)).filter(()).map(()).orElse(List.of());
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static RedirectModifier<CommandSourceStack> expandOneToManyEntityRelation(Function<Entity, Stream<Entity>> unpacker) {
/*  938 */     return context -> {
/*      */         CommandSourceStack source = (CommandSourceStack)context.getSource();
/*      */         Entity entity = source.getEntity();
/*      */         if (entity == null)
/*      */           return List.of(); 
/*      */         Objects.requireNonNull(source);
/*      */         return ((Stream)unpacker.apply(entity)).filter(()).map(source::withEntity).toList();
/*      */       };
/*      */   }
/*      */   
/*      */   private static LiteralArgumentBuilder<CommandSourceStack> createRelationOperations(CommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> on) {
/*  949 */     return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)
/*  950 */       on.then(
/*  951 */         Commands.literal("owner")
/*  952 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               OwnableEntity ownableEntity = (OwnableEntity)e; return (e instanceof OwnableEntity) ? Optional.<LivingEntity>ofNullable(ownableEntity.getOwner()) : Optional.empty();
/*  954 */             })))).then(
/*  955 */         Commands.literal("leasher")
/*  956 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               Leashable leashable = (Leashable)e; return (e instanceof Leashable) ? Optional.<Entity>ofNullable(leashable.getLeashHolder()) : Optional.empty();
/*  958 */             })))).then(
/*  959 */         Commands.literal("target")
/*  960 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               Targeting targeting = (Targeting)e; return (e instanceof Targeting) ? Optional.<LivingEntity>ofNullable(targeting.getTarget()) : Optional.empty();
/*  962 */             })))).then(
/*  963 */         Commands.literal("attacker")
/*  964 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               Attackable attackable = (Attackable)e; return (e instanceof Attackable) ? Optional.<LivingEntity>ofNullable(attackable.getLastAttacker()) : Optional.empty();
/*  966 */             })))).then(
/*  967 */         Commands.literal("vehicle")
/*  968 */         .fork(execute, expandOneToOneEntityRelation(e -> Optional.ofNullable(e.getVehicle())))))
/*      */       
/*  970 */       .then(
/*  971 */         Commands.literal("controller")
/*  972 */         .fork(execute, expandOneToOneEntityRelation(e -> Optional.ofNullable(e.getControllingPassenger())))))
/*      */       
/*  974 */       .then(
/*  975 */         Commands.literal("origin")
/*  976 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               TraceableEntity traceable = (TraceableEntity)e; return (e instanceof TraceableEntity) ? Optional.<Entity>ofNullable(traceable.getOwner()) : Optional.empty();
/*  978 */             })))).then(
/*  979 */         Commands.literal("passengers")
/*  980 */         .fork(execute, expandOneToManyEntityRelation(e -> e.getPassengers().stream())));
/*      */   }
/*      */ 
/*      */   
/*      */   private static CommandSourceStack spawnEntityAndRedirect(CommandSourceStack source, Holder.Reference<EntityType<?>> type) throws CommandSyntaxException {
/*  985 */     Entity entity = SummonCommand.createEntity(source, type, source.getPosition(), new CompoundTag(), true);
/*  986 */     return source.withEntity(entity);
/*      */   } @FunctionalInterface
/*      */   private static interface CommandPredicate {
/*      */     boolean test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException; } @FunctionalInterface
/*      */   private static interface CommandNumericPredicate {
/*      */     int test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException; } private static class ExecuteIfFunctionCustomModifier implements CustomModifierExecutor.ModifierAdapter<CommandSourceStack> { private final IntPredicate check;
/*      */     private ExecuteIfFunctionCustomModifier(boolean check) {
/*  993 */       this.check = check ? (value -> (value != 0)) : (value -> (value == 0));
/*      */     }
/*      */ 
/*      */     
/*      */     public void apply(CommandSourceStack originalSource, List<CommandSourceStack> currentSources, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> output) {
/*  998 */       ExecuteCommand.scheduleFunctionConditionsAndTest(originalSource, currentSources, FunctionCommand::modifySenderForExecution, this.check, currentStep, null, output, c -> FunctionArgument.getFunctions(c, "name"), modifiers);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends ExecutionCommandSource<T>> void scheduleFunctionConditionsAndTest(T originalSource, List<T> currentSources, Function<T, T> functionContextModifier, IntPredicate check, ContextChain<T> currentStep, CompoundTag parameters, ExecutionControl<T> output, InCommandFunction<CommandContext<T>, Collection<CommandFunction<T>>> functionGetter, ChainModifiers modifiers) {
/*      */     Collection<CommandFunction<T>> functionsToRun;
/* 1013 */     List<T> filteredSources = new ArrayList<>(currentSources.size());
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1018 */       functionsToRun = functionGetter.apply(currentStep.getTopContext().copyFor(originalSource));
/* 1019 */     } catch (CommandSyntaxException e) {
/* 1020 */       originalSource.handleError(e, modifiers.isForked(), output.tracer());
/*      */       
/*      */       return;
/*      */     } 
/* 1024 */     int functionCount = functionsToRun.size();
/* 1025 */     if (functionCount == 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1030 */     List<InstantiatedFunction<T>> instantiatedFunctions = new ArrayList<>(functionCount);
/*      */     
/*      */     try {
/* 1033 */       for (CommandFunction<T> function : functionsToRun) {
/*      */         try {
/* 1035 */           instantiatedFunctions.add(function.instantiate(parameters, originalSource.dispatcher()));
/* 1036 */         } catch (FunctionInstantiationException e) {
/* 1037 */           throw ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE.create(function.id(), e.messageComponent());
/*      */         } 
/*      */       } 
/* 1040 */     } catch (CommandSyntaxException e) {
/* 1041 */       originalSource.handleError(e, modifiers.isForked(), output.tracer());
/*      */     } 
/*      */     
/* 1044 */     for (ExecutionCommandSource executionCommandSource1 : currentSources) {
/* 1045 */       ExecutionCommandSource executionCommandSource2 = (ExecutionCommandSource)functionContextModifier.apply((T)executionCommandSource1.clearCallbacks());
/*      */       
/*      */       CommandResultCallback functionCallback = (success, result) -> {
/*      */           if (check.test(result)) {
/*      */             filteredSources.add(source);
/*      */           }
/*      */         };
/* 1052 */       output.queueNext((EntryAction)new IsolatedCall(o -> { for (InstantiatedFunction<T> function : (Iterable<InstantiatedFunction<T>>)instantiatedFunctions) o.queueNext(new CallFunction(function, o.currentFrame().returnValueConsumer(), true).bind(newFunctionContext));  o.queueNext(FallthroughTask.instance()); }, functionCallback));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1060 */     ContextChain<T> nextStage = currentStep.nextStage();
/* 1061 */     String input = currentStep.getTopContext().getInput();
/* 1062 */     output.queueNext((EntryAction)new BuildContexts.Continuation(input, nextStage, modifiers, (ExecutionCommandSource)originalSource, filteredSources));
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface IntBiPredicate {
/*      */     boolean test(int param1Int1, int param1Int2);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/ExecuteCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */