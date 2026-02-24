/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SlotProvider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ 
/*     */ public class ItemCommands
/*     */ {
/*     */   static final Dynamic3CommandExceptionType ERROR_TARGET_NOT_A_CONTAINER;
/*     */   static final Dynamic3CommandExceptionType ERROR_SOURCE_NOT_A_CONTAINER;
/*     */   static final DynamicCommandExceptionType ERROR_TARGET_INAPPLICABLE_SLOT;
/*     */   
/*     */   static {
/*  54 */     ERROR_TARGET_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((x, y, z) -> Component.translatableEscape("commands.item.target.not_a_container", new Object[] { x, y, z }));
/*  55 */     ERROR_SOURCE_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((x, y, z) -> Component.translatableEscape("commands.item.source.not_a_container", new Object[] { x, y, z }));
/*     */     
/*  57 */     ERROR_TARGET_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(slot -> Component.translatableEscape("commands.item.target.no_such_slot", new Object[] { slot }));
/*  58 */     ERROR_SOURCE_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(slot -> Component.translatableEscape("commands.item.source.no_such_slot", new Object[] { slot }));
/*     */     
/*  60 */     ERROR_TARGET_NO_CHANGES = new DynamicCommandExceptionType(slot -> Component.translatableEscape("commands.item.target.no_changes", new Object[] { slot }));
/*  61 */     ERROR_TARGET_NO_CHANGES_KNOWN_ITEM = new Dynamic2CommandExceptionType((item, slot) -> Component.translatableEscape("commands.item.target.no_changed.known_item", new Object[] { item, slot }));
/*     */   } private static final DynamicCommandExceptionType ERROR_SOURCE_INAPPLICABLE_SLOT; private static final DynamicCommandExceptionType ERROR_TARGET_NO_CHANGES; private static final Dynamic2CommandExceptionType ERROR_TARGET_NO_CHANGES_KNOWN_ITEM;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  64 */     dispatcher.register(
/*  65 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("item")
/*  66 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  67 */         .then((
/*  68 */           (LiteralArgumentBuilder)Commands.literal("replace")
/*  69 */           .then(
/*  70 */             Commands.literal("block")
/*  71 */             .then(
/*  72 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  73 */               .then((
/*  74 */                 (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/*  75 */                 .then(
/*  76 */                   Commands.literal("with")
/*  77 */                   .then((
/*  78 */                     (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(context))
/*  79 */                     .executes(c -> setBlockItem((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(1, false))))
/*  80 */                     .then(
/*  81 */                       Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 99))
/*  82 */                       .executes(c -> setBlockItem((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(IntegerArgumentType.getInteger(c, "count"), true)))))))
/*     */ 
/*     */ 
/*     */                 
/*  86 */                 .then((
/*  87 */                   (LiteralArgumentBuilder)Commands.literal("from")
/*  88 */                   .then(
/*  89 */                     Commands.literal("block")
/*  90 */                     .then(
/*  91 */                       Commands.argument("source", (ArgumentType)BlockPosArgument.blockPos())
/*  92 */                       .then((
/*  93 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/*  94 */                         .executes(c -> blockToBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"))))
/*  95 */                         .then(
/*  96 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/*  97 */                           .executes(c -> blockToBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 102 */                   .then(
/* 103 */                     Commands.literal("entity")
/* 104 */                     .then(
/* 105 */                       Commands.argument("source", (ArgumentType)EntityArgument.entity())
/* 106 */                       .then((
/* 107 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 108 */                         .executes(c -> entityToBlock((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"))))
/* 109 */                         .then(
/* 110 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 111 */                           .executes(c -> entityToBlock((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 120 */           .then(
/* 121 */             Commands.literal("entity")
/* 122 */             .then(
/* 123 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 124 */               .then((
/* 125 */                 (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 126 */                 .then(
/* 127 */                   Commands.literal("with")
/* 128 */                   .then((
/* 129 */                     (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(context))
/* 130 */                     .executes(c -> setEntityItem((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(1, false))))
/* 131 */                     .then(
/* 132 */                       Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 99))
/* 133 */                       .executes(c -> setEntityItem((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(IntegerArgumentType.getInteger(c, "count"), true)))))))
/*     */ 
/*     */ 
/*     */                 
/* 137 */                 .then((
/* 138 */                   (LiteralArgumentBuilder)Commands.literal("from")
/* 139 */                   .then(
/* 140 */                     Commands.literal("block")
/* 141 */                     .then(
/* 142 */                       Commands.argument("source", (ArgumentType)BlockPosArgument.blockPos())
/* 143 */                       .then((
/* 144 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 145 */                         .executes(c -> blockToEntities((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"))))
/* 146 */                         .then(
/* 147 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 148 */                           .executes(c -> blockToEntities((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 153 */                   .then(
/* 154 */                     Commands.literal("entity")
/* 155 */                     .then(
/* 156 */                       Commands.argument("source", (ArgumentType)EntityArgument.entity())
/* 157 */                       .then((
/* 158 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 159 */                         .executes(c -> entityToEntities((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"))))
/* 160 */                         .then(
/* 161 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 162 */                           .executes(c -> entityToEntities((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier")))))))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 172 */         .then((
/* 173 */           (LiteralArgumentBuilder)Commands.literal("modify")
/* 174 */           .then(
/* 175 */             Commands.literal("block")
/* 176 */             .then(
/* 177 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 178 */               .then(
/* 179 */                 Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 180 */                 .then(
/* 181 */                   Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 182 */                   .executes(c -> modifyBlockItem((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 187 */           .then(
/* 188 */             Commands.literal("entity")
/* 189 */             .then(
/* 190 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 191 */               .then(
/* 192 */                 Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 193 */                 .then(
/* 194 */                   Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 195 */                   .executes(c -> modifyEntityItem((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier")))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int modifyBlockItem(CommandSourceStack source, BlockPos pos, int slot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 205 */     Container container = getContainer(source, pos, ERROR_TARGET_NOT_A_CONTAINER);
/* 206 */     if (slot < 0 || slot >= container.getContainerSize()) {
/* 207 */       throw ERROR_TARGET_INAPPLICABLE_SLOT.create(slot);
/*     */     }
/*     */     
/* 210 */     ItemStack itemStack = applyModifier(source, modifier, container.getItem(slot));
/* 211 */     container.setItem(slot, itemStack);
/* 212 */     source.sendSuccess(() -> Component.translatable("commands.item.block.set.success", new Object[] { pos.getX(), pos.getY(), pos.getZ(), itemStack.getDisplayName() }), true);
/* 213 */     return 1;
/*     */   }
/*     */   
/*     */   private static int modifyEntityItem(CommandSourceStack source, Collection<? extends Entity> entities, int slot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 217 */     Map<Entity, ItemStack> changedEntities = Maps.newHashMapWithExpectedSize(entities.size());
/*     */     
/* 219 */     for (Entity entity : entities) {
/* 220 */       SlotAccess slotAccess = entity.getSlot(slot);
/* 221 */       if (slotAccess != null) {
/* 222 */         ItemStack itemStack = applyModifier(source, modifier, slotAccess.get().copy());
/* 223 */         if (slotAccess.set(itemStack)) {
/* 224 */           changedEntities.put(entity, itemStack);
/* 225 */           if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 226 */             serverPlayer.containerMenu.broadcastChanges(); }
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     if (changedEntities.isEmpty()) {
/* 233 */       throw ERROR_TARGET_NO_CHANGES.create(slot);
/*     */     }
/*     */     
/* 236 */     if (changedEntities.size() == 1) {
/* 237 */       Map.Entry<Entity, ItemStack> e = changedEntities.entrySet().iterator().next();
/* 238 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", new Object[] { ((Entity)e.getKey()).getDisplayName(), ((ItemStack)e.getValue()).getDisplayName() }), true);
/*     */     } else {
/* 240 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", new Object[] { changedEntities.size() }), true);
/*     */     } 
/*     */     
/* 243 */     return changedEntities.size();
/*     */   }
/*     */   
/*     */   private static int setBlockItem(CommandSourceStack source, BlockPos pos, int slot, ItemStack itemStack) throws CommandSyntaxException {
/* 247 */     Container container = getContainer(source, pos, ERROR_TARGET_NOT_A_CONTAINER);
/* 248 */     if (slot < 0 || slot >= container.getContainerSize()) {
/* 249 */       throw ERROR_TARGET_INAPPLICABLE_SLOT.create(slot);
/*     */     }
/*     */     
/* 252 */     container.setItem(slot, itemStack);
/* 253 */     source.sendSuccess(() -> Component.translatable("commands.item.block.set.success", new Object[] { pos.getX(), pos.getY(), pos.getZ(), itemStack.getDisplayName() }), true);
/* 254 */     return 1;
/*     */   }
/*     */   
/*     */   static Container getContainer(CommandSourceStack source, BlockPos pos, Dynamic3CommandExceptionType exceptionType) throws CommandSyntaxException {
/* 258 */     BlockEntity entity = source.getLevel().getBlockEntity(pos);
/* 259 */     if (entity instanceof Container) { Container container = (Container)entity;
/* 260 */       return container; }
/*     */     
/* 262 */     throw exceptionType.create(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   private static int setEntityItem(CommandSourceStack source, Collection<? extends Entity> entities, int slot, ItemStack itemStack) throws CommandSyntaxException {
/* 266 */     List<Entity> changedEntities = Lists.newArrayListWithCapacity(entities.size());
/*     */     
/* 268 */     for (Entity entity : entities) {
/* 269 */       SlotAccess slotAccess = entity.getSlot(slot);
/* 270 */       if (slotAccess != null && slotAccess.set(itemStack.copy())) {
/* 271 */         changedEntities.add(entity);
/* 272 */         if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 273 */           serverPlayer.containerMenu.broadcastChanges(); }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 278 */     if (changedEntities.isEmpty()) {
/* 279 */       throw ERROR_TARGET_NO_CHANGES_KNOWN_ITEM.create(itemStack.getDisplayName(), slot);
/*     */     }
/*     */     
/* 282 */     if (changedEntities.size() == 1) {
/* 283 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", new Object[] { ((Entity)changedEntities.getFirst()).getDisplayName(), itemStack.getDisplayName() }), true);
/*     */     } else {
/* 285 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", new Object[] { changedEntities.size(), itemStack.getDisplayName() }), true);
/*     */     } 
/*     */     
/* 288 */     return changedEntities.size();
/*     */   }
/*     */   
/*     */   private static int blockToEntities(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot) throws CommandSyntaxException {
/* 292 */     return setEntityItem(source, targetEntities, targetSlot, getBlockItem(source, sourcePos, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int blockToEntities(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 296 */     return setEntityItem(source, targetEntities, targetSlot, applyModifier(source, modifier, getBlockItem(source, sourcePos, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static int blockToBlock(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, BlockPos targetPos, int targetSlot) throws CommandSyntaxException {
/* 300 */     return setBlockItem(source, targetPos, targetSlot, getBlockItem(source, sourcePos, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int blockToBlock(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, BlockPos targetPos, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 304 */     return setBlockItem(source, targetPos, targetSlot, applyModifier(source, modifier, getBlockItem(source, sourcePos, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static int entityToBlock(CommandSourceStack source, Entity sourceEntity, int sourceSlot, BlockPos targetPos, int targetSlot) throws CommandSyntaxException {
/* 308 */     return setBlockItem(source, targetPos, targetSlot, getItemInSlot((SlotProvider)sourceEntity, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int entityToBlock(CommandSourceStack source, Entity sourceEntity, int sourceSlot, BlockPos targetPos, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 312 */     return setBlockItem(source, targetPos, targetSlot, applyModifier(source, modifier, getItemInSlot((SlotProvider)sourceEntity, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static int entityToEntities(CommandSourceStack source, Entity sourceEntity, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot) throws CommandSyntaxException {
/* 316 */     return setEntityItem(source, targetEntities, targetSlot, getItemInSlot((SlotProvider)sourceEntity, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int entityToEntities(CommandSourceStack source, Entity sourceEntity, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 320 */     return setEntityItem(source, targetEntities, targetSlot, applyModifier(source, modifier, getItemInSlot((SlotProvider)sourceEntity, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static ItemStack applyModifier(CommandSourceStack source, Holder<LootItemFunction> modifier, ItemStack item) {
/* 324 */     ServerLevel level = source.getLevel();
/*     */     
/* 326 */     LootParams lootParams = new LootParams.Builder(level)
/* 327 */       .withParameter(LootContextParams.ORIGIN, source.getPosition())
/* 328 */       .withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity())
/* 329 */       .create(LootContextParamSets.COMMAND);
/* 330 */     LootContext context = new LootContext.Builder(lootParams).create(Optional.empty());
/* 331 */     context.pushVisitedElement(LootContext.createVisitedEntry((LootItemFunction)modifier.value()));
/*     */     
/* 333 */     ItemStack newItem = (ItemStack)((LootItemFunction)modifier.value()).apply(item, context);
/* 334 */     newItem.limitSize(newItem.getMaxStackSize());
/* 335 */     return newItem;
/*     */   }
/*     */   
/*     */   private static ItemStack getItemInSlot(SlotProvider slotProvider, int slot) throws CommandSyntaxException {
/* 339 */     SlotAccess slotAccess = slotProvider.getSlot(slot);
/* 340 */     if (slotAccess == null) {
/* 341 */       throw ERROR_SOURCE_INAPPLICABLE_SLOT.create(slot);
/*     */     }
/* 343 */     return slotAccess.get().copy();
/*     */   }
/*     */   
/*     */   private static ItemStack getBlockItem(CommandSourceStack source, BlockPos pos, int slot) throws CommandSyntaxException {
/* 347 */     Container container = getContainer(source, pos, ERROR_SOURCE_NOT_A_CONTAINER);
/* 348 */     return getItemInSlot((SlotProvider)container, slot);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/ItemCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */