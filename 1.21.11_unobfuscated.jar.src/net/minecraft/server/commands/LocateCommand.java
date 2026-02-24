/*     */ package net.minecraft.server.commands;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LocateCommand {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND; private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID;
/*     */   static {
/*  47 */     ERROR_STRUCTURE_NOT_FOUND = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.locate.structure.not_found", new Object[] { value }));
/*  48 */     ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.locate.structure.invalid", new Object[] { value }));
/*     */     
/*  50 */     ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.locate.biome.not_found", new Object[] { value }));
/*     */     
/*  52 */     ERROR_POI_NOT_FOUND = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.locate.poi.not_found", new Object[] { value }));
/*     */   }
/*     */   private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND;
/*     */   private static final DynamicCommandExceptionType ERROR_POI_NOT_FOUND;
/*     */   private static final int MAX_STRUCTURE_SEARCH_RADIUS = 100;
/*     */   private static final int MAX_BIOME_SEARCH_RADIUS = 6400;
/*     */   private static final int BIOME_SAMPLE_RESOLUTION_HORIZONTAL = 32;
/*     */   private static final int BIOME_SAMPLE_RESOLUTION_VERTICAL = 64;
/*     */   private static final int POI_SEARCH_RADIUS = 256;
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  63 */     dispatcher.register(
/*  64 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("locate")
/*  65 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  66 */         .then(
/*  67 */           Commands.literal("structure")
/*  68 */           .then(
/*  69 */             Commands.argument("structure", (ArgumentType)ResourceOrTagKeyArgument.resourceOrTagKey(Registries.STRUCTURE))
/*  70 */             .executes(c -> locateStructure((CommandSourceStack)c.getSource(), ResourceOrTagKeyArgument.getResourceOrTagKey(c, "structure", Registries.STRUCTURE, ERROR_STRUCTURE_INVALID))))))
/*     */         
/*  72 */         .then(
/*  73 */           Commands.literal("biome")
/*  74 */           .then(
/*  75 */             Commands.argument("biome", (ArgumentType)ResourceOrTagArgument.resourceOrTag(context, Registries.BIOME))
/*  76 */             .executes(c -> locateBiome((CommandSourceStack)c.getSource(), ResourceOrTagArgument.getResourceOrTag(c, "biome", Registries.BIOME))))))
/*     */         
/*  78 */         .then(
/*  79 */           Commands.literal("poi")
/*  80 */           .then(
/*  81 */             Commands.argument("poi", (ArgumentType)ResourceOrTagArgument.resourceOrTag(context, Registries.POINT_OF_INTEREST_TYPE))
/*  82 */             .executes(c -> locatePoi((CommandSourceStack)c.getSource(), ResourceOrTagArgument.getResourceOrTag(c, "poi", Registries.POINT_OF_INTEREST_TYPE))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<? extends HolderSet.ListBacked<Structure>> getHolders(ResourceOrTagKeyArgument.Result<Structure> resourceOrTag, Registry<Structure> registry) {
/*  90 */     java.util.Objects.requireNonNull(registry); return (Optional<? extends HolderSet.ListBacked<Structure>>)resourceOrTag.unwrap().map(id -> registry.get(id).map(()), registry::get);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int locateStructure(CommandSourceStack source, ResourceOrTagKeyArgument.Result<Structure> resourceOrTag) throws CommandSyntaxException {
/*  95 */     Registry<Structure> registry = source.getLevel().registryAccess().lookupOrThrow(Registries.STRUCTURE);
/*     */     
/*  97 */     HolderSet<Structure> target = (HolderSet<Structure>)getHolders(resourceOrTag, registry).orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(resourceOrTag.asPrintable()));
/*     */     
/*  99 */     BlockPos sourcePos = BlockPos.containing((Position)source.getPosition());
/* 100 */     ServerLevel serverLevel = source.getLevel();
/* 101 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/* 102 */     Pair<BlockPos, Holder<Structure>> nearest = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, target, sourcePos, 100, false);
/* 103 */     stopwatch.stop();
/* 104 */     if (nearest == null) {
/* 105 */       throw ERROR_STRUCTURE_NOT_FOUND.create(resourceOrTag.asPrintable());
/*     */     }
/*     */     
/* 108 */     return showLocateResult(source, resourceOrTag, sourcePos, nearest, "commands.locate.structure.success", false, stopwatch.elapsed());
/*     */   }
/*     */   
/*     */   private static int locateBiome(CommandSourceStack source, ResourceOrTagArgument.Result<Biome> elementOrTag) throws CommandSyntaxException {
/* 112 */     BlockPos sourcePos = BlockPos.containing((Position)source.getPosition());
/* 113 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/* 114 */     Pair<BlockPos, Holder<Biome>> nearest = source.getLevel().findClosestBiome3d((Predicate)elementOrTag, sourcePos, 6400, 32, 64);
/* 115 */     stopwatch.stop();
/* 116 */     if (nearest == null) {
/* 117 */       throw ERROR_BIOME_NOT_FOUND.create(elementOrTag.asPrintable());
/*     */     }
/* 119 */     return showLocateResult(source, elementOrTag, sourcePos, nearest, "commands.locate.biome.success", true, stopwatch.elapsed());
/*     */   }
/*     */   
/*     */   private static int locatePoi(CommandSourceStack source, ResourceOrTagArgument.Result<PoiType> resourceOrTag) throws CommandSyntaxException {
/* 123 */     BlockPos sourcePos = BlockPos.containing((Position)source.getPosition());
/* 124 */     ServerLevel serverLevel = source.getLevel();
/* 125 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/* 126 */     Optional<Pair<Holder<PoiType>, BlockPos>> closestWithType = serverLevel.getPoiManager().findClosestWithType((Predicate)resourceOrTag, sourcePos, 256, net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy.ANY);
/* 127 */     stopwatch.stop();
/*     */     
/* 129 */     if (closestWithType.isEmpty()) {
/* 130 */       throw ERROR_POI_NOT_FOUND.create(resourceOrTag.asPrintable());
/*     */     }
/*     */     
/* 133 */     return showLocateResult(source, resourceOrTag, sourcePos, ((Pair)closestWithType.get()).swap(), "commands.locate.poi.success", false, stopwatch.elapsed());
/*     */   }
/*     */   
/*     */   public static int showLocateResult(CommandSourceStack source, ResourceOrTagArgument.Result<?> name, BlockPos sourcePos, Pair<BlockPos, ? extends Holder<?>> found, String successMessageKey, boolean includeY, Duration taskDuration) {
/* 137 */     String foundName = (String)name.unwrap().map(element -> name.asPrintable(), tag -> name.asPrintable() + " (" + name.asPrintable() + ")");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     return showLocateResult(source, sourcePos, found, successMessageKey, includeY, foundName, taskDuration);
/*     */   }
/*     */   
/*     */   public static int showLocateResult(CommandSourceStack source, ResourceOrTagKeyArgument.Result<?> name, BlockPos sourcePos, Pair<BlockPos, ? extends Holder<?>> found, String successMessageKey, boolean includeY, Duration taskDuration) {
/* 146 */     String foundName = (String)name.unwrap().map(element -> element.identifier().toString(), tag -> "#" + String.valueOf(tag.location()) + " (" + ((Holder)found.getSecond()).getRegisteredName() + ")");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     return showLocateResult(source, sourcePos, found, successMessageKey, includeY, foundName, taskDuration);
/*     */   }
/*     */   
/*     */   private static int showLocateResult(CommandSourceStack source, BlockPos sourcePos, Pair<BlockPos, ? extends Holder<?>> found, String successMessageKey, boolean includeY, String foundName, Duration taskDuration) {
/* 155 */     BlockPos foundPos = (BlockPos)found.getFirst();
/*     */     
/* 157 */     int distance = includeY ? 
/* 158 */       Mth.floor(Mth.sqrt((float)sourcePos.distSqr((Vec3i)foundPos))) : 
/* 159 */       Mth.floor(dist(sourcePos.getX(), sourcePos.getZ(), foundPos.getX(), foundPos.getZ()));
/* 160 */     String displayedY = includeY ? String.valueOf(foundPos.getY()) : "~";
/* 161 */     MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets((Component)Component.translatable("chat.coordinates", new Object[] { foundPos.getX(), displayedY, foundPos.getZ() })).withStyle(s -> s.withColor(ChatFormatting.GREEN).withClickEvent((ClickEvent)new ClickEvent.SuggestCommand("/tp @s " + foundPos.getX() + " " + displayedY + " " + foundPos.getZ())).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat.coordinates.tooltip"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     source.sendSuccess(() -> Component.translatable(successMessageKey, new Object[] { foundName, coordinates, distance }), false);
/* 168 */     LOGGER.info("Locating element {} took {} ms", foundName, taskDuration.toMillis());
/* 169 */     return distance;
/*     */   }
/*     */   
/*     */   private static float dist(int x1, int z1, int x2, int z2) {
/* 173 */     int dx = x2 - x1;
/* 174 */     int dz = z2 - z1;
/* 175 */     return Mth.sqrt((dx * dx + dz * dz));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/LocateCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */