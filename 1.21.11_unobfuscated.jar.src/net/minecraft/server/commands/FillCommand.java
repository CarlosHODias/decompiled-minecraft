/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class FillCommand
/*     */ {
/*     */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
/*     */   
/*     */   static {
/*  39 */     ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.fill.toobig", new Object[] { max, count }));
/*  40 */   } private static final BlockInput HOLLOW_CORE = new BlockInput(Blocks.AIR.defaultBlockState(), Collections.emptySet(), null);
/*  41 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.fill.failed"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  49 */     dispatcher.register(
/*  50 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fill")
/*  51 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  52 */         .then(
/*  53 */           Commands.argument("from", (ArgumentType)BlockPosArgument.blockPos())
/*  54 */           .then(
/*  55 */             Commands.argument("to", (ArgumentType)BlockPosArgument.blockPos())
/*  56 */             .then(
/*  57 */               wrapWithMode(context, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("block", (ArgumentType)BlockStateArgument.block(context)), c -> BlockPosArgument.getLoadedBlockPos(c, "from"), c -> BlockPosArgument.getLoadedBlockPos(c, "to"), c -> BlockStateArgument.getBlock(c, "block"), c -> null)
/*  58 */               .then((
/*  59 */                 (LiteralArgumentBuilder)Commands.literal("replace")
/*  60 */                 .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners((Vec3i)BlockPosArgument.getLoadedBlockPos(c, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(c, "to")), BlockStateArgument.getBlock(c, "block"), Mode.REPLACE, null, false)))
/*  61 */                 .then(
/*  62 */                   wrapWithMode(context, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("filter", (ArgumentType)BlockPredicateArgument.blockPredicate(context)), c -> BlockPosArgument.getLoadedBlockPos(c, "from"), c -> BlockPosArgument.getLoadedBlockPos(c, "to"), c -> BlockStateArgument.getBlock(c, "block"), c -> BlockPredicateArgument.getBlockPredicate(c, "filter"))))
/*     */ 
/*     */               
/*  65 */               .then(
/*  66 */                 Commands.literal("keep")
/*  67 */                 .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners((Vec3i)BlockPosArgument.getLoadedBlockPos(c, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(c, "to")), BlockStateArgument.getBlock(c, "block"), Mode.REPLACE, (), false)))))));
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
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> wrapWithMode(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> builder, InCommandFunction<CommandContext<CommandSourceStack>, BlockPos> from, InCommandFunction<CommandContext<CommandSourceStack>, BlockPos> to, InCommandFunction<CommandContext<CommandSourceStack>, BlockInput> block, NullableCommandFunction<CommandContext<CommandSourceStack>, Predicate<BlockInWorld>> filter) {
/*  83 */     return 
/*  84 */       builder.executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners(from.apply(c), to.apply(c)), block.apply(c), Mode.REPLACE, filter.apply(c), false))
/*  85 */       .then(
/*  86 */         Commands.literal("outline")
/*  87 */         .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners(from.apply(c), to.apply(c)), block.apply(c), Mode.OUTLINE, filter.apply(c), false)))
/*     */       
/*  89 */       .then(
/*  90 */         Commands.literal("hollow")
/*  91 */         .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners(from.apply(c), to.apply(c)), block.apply(c), Mode.HOLLOW, filter.apply(c), false)))
/*     */       
/*  93 */       .then(
/*  94 */         Commands.literal("destroy")
/*  95 */         .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners(from.apply(c), to.apply(c)), block.apply(c), Mode.DESTROY, filter.apply(c), false)))
/*     */       
/*  97 */       .then(
/*  98 */         Commands.literal("strict")
/*  99 */         .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BoundingBox.fromCorners(from.apply(c), to.apply(c)), block.apply(c), Mode.REPLACE, filter.apply(c), true)));
/*     */   }
/*     */   private static int fillBlocks(CommandSourceStack source, BoundingBox region, BlockInput target, Mode mode, Predicate<BlockInWorld> predicate, boolean strict) throws CommandSyntaxException {
/*     */     static final class UpdatedPosition extends Record { private final BlockPos pos; private final BlockState oldState;
/*     */       
/* 104 */       UpdatedPosition(BlockPos pos, BlockState oldState) { this.pos = pos; this.oldState = oldState; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/FillCommand$1UpdatedPosition;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #104	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/* 104 */         //   0	7	0	this	Lnet/minecraft/server/commands/FillCommand$1UpdatedPosition; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/FillCommand$1UpdatedPosition;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #104	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/* 104 */         //   0	7	0	this	Lnet/minecraft/server/commands/FillCommand$1UpdatedPosition; } public BlockState oldState() { return this.oldState; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/FillCommand$1UpdatedPosition;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #104	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/server/commands/FillCommand$1UpdatedPosition;
/*     */         //   0	8	1	o	Ljava/lang/Object; } }
/* 105 */     ; int area = region.getXSpan() * region.getYSpan() * region.getZSpan();
/* 106 */     int limit = (Integer)source.getLevel().getGameRules().get(GameRules.MAX_BLOCK_MODIFICATIONS);
/* 107 */     if (area > limit) {
/* 108 */       throw ERROR_AREA_TOO_LARGE.create(limit, area);
/*     */     }
/*     */     
/* 111 */     List<UpdatedPosition> updatePositions = Lists.newArrayList();
/* 112 */     ServerLevel level = source.getLevel();
/* 113 */     if (level.isDebug()) {
/* 114 */       throw ERROR_FAILED.create();
/*     */     }
/* 116 */     int count = 0;
/*     */     
/* 118 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(region.minX(), region.minY(), region.minZ(), region.maxX(), region.maxY(), region.maxZ())) {
/* 119 */       if (predicate != null && !predicate.test(new BlockInWorld((LevelReader)level, pos, true))) {
/*     */         continue;
/*     */       }
/* 122 */       BlockState oldState = level.getBlockState(pos);
/*     */       boolean affected = false;
/* 124 */       if (mode.affector.affect(level, pos)) {
/* 125 */         affected = true;
/*     */       }
/* 127 */       BlockInput block = mode.filter.filter(region, pos, target, level);
/* 128 */       if (block == null) {
/* 129 */         if (affected) {
/* 130 */           count++;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 136 */       if (!block.place(level, pos, 0x2 | (strict ? 816 : 256))) {
/* 137 */         if (affected) {
/* 138 */           count++;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 143 */       if (!strict) {
/* 144 */         updatePositions.add(new UpdatedPosition(pos.immutable(), oldState));
/*     */       }
/* 146 */       count++;
/*     */     } 
/*     */     
/* 149 */     for (UpdatedPosition pos : updatePositions) {
/* 150 */       level.updateNeighboursOnBlockSet(pos.pos, pos.oldState);
/*     */     }
/*     */     
/* 153 */     if (count == 0) {
/* 154 */       throw ERROR_FAILED.create();
/*     */     }
/*     */     
/* 157 */     int finalCount = count;
/* 158 */     source.sendSuccess(() -> Component.translatable("commands.fill.success", new Object[] { finalCount }), true);
/*     */     
/* 160 */     return count;
/*     */   } private enum Mode { DESTROY,
/*     */     HOLLOW,
/*     */     OUTLINE,
/* 164 */     REPLACE(FillCommand.Affector.NOOP, FillCommand.Filter.NOOP); public final FillCommand.Affector affector; public final FillCommand.Filter filter; static {
/* 165 */       OUTLINE = new Mode("OUTLINE", 1, FillCommand.Affector.NOOP, (r, p, b, l) -> 
/* 166 */           (p.getX() == r.minX() || p.getX() == r.maxX() || p.getY() == r.minY() || p.getY() == r.maxY() || p.getZ() == r.minZ() || p.getZ() == r.maxZ()) ? b : null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 172 */       HOLLOW = new Mode("HOLLOW", 2, FillCommand.Affector.NOOP, (r, p, b, l) -> 
/* 173 */           (p.getX() == r.minX() || p.getX() == r.maxX() || p.getY() == r.minY() || p.getY() == r.maxY() || p.getZ() == r.minZ() || p.getZ() == r.maxZ()) ? b : FillCommand.HOLLOW_CORE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 179 */       DESTROY = new Mode("DESTROY", 3, (l, p) -> l.destroyBlock(p, true), FillCommand.Filter.NOOP);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Mode(FillCommand.Affector affector, FillCommand.Filter filter) {
/* 185 */       this.affector = affector;
/* 186 */       this.filter = filter;
/*     */     } }
/*     */   @FunctionalInterface
/*     */   public static interface Filter { public static final Filter NOOP;
/*     */     BlockInput filter(BoundingBox param1BoundingBox, BlockPos param1BlockPos, BlockInput param1BlockInput, ServerLevel param1ServerLevel);
/*     */     static {
/* 192 */       NOOP = ((r, p, b, l) -> b);
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Affector {
/*     */     public static final Affector NOOP = (l, p) -> false;
/*     */     
/*     */     boolean affect(ServerLevel param1ServerLevel, BlockPos param1BlockPos);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface NullableCommandFunction<T, R> {
/*     */     R apply(T param1T) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/FillCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */