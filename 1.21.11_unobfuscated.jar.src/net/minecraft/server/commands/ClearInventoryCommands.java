/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClearInventoryCommands {
/*    */   private static final DynamicCommandExceptionType ERROR_SINGLE;
/*    */   
/*    */   static {
/* 27 */     ERROR_SINGLE = new DynamicCommandExceptionType(name -> Component.translatableEscape("clear.failed.single", new Object[] { name }));
/* 28 */     ERROR_MULTIPLE = new DynamicCommandExceptionType(count -> Component.translatableEscape("clear.failed.multiple", new Object[] { count }));
/*    */   } private static final DynamicCommandExceptionType ERROR_MULTIPLE;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 31 */     dispatcher.register(
/* 32 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clear")
/* 33 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 34 */         .executes(c -> clearUnlimited((CommandSourceStack)c.getSource(), java.util.Collections.singleton(((CommandSourceStack)c.getSource()).getPlayerOrException()), ())))
/* 35 */         .then((
/* 36 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 37 */           .executes(c -> clearUnlimited((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ())))
/* 38 */           .then((
/* 39 */             (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemPredicateArgument.itemPredicate(context))
/* 40 */             .executes(c -> clearUnlimited((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item"))))
/* 41 */             .then(
/* 42 */               Commands.argument("maxCount", (ArgumentType)IntegerArgumentType.integer(0))
/* 43 */               .executes(c -> clearInventory((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item"), IntegerArgumentType.getInteger(c, "maxCount")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int clearUnlimited(CommandSourceStack source, Collection<ServerPlayer> players, Predicate<ItemStack> predicate) throws CommandSyntaxException {
/* 51 */     return clearInventory(source, players, predicate, -1);
/*    */   }
/*    */   
/*    */   private static int clearInventory(CommandSourceStack source, Collection<ServerPlayer> players, Predicate<ItemStack> predicate, int maxCount) throws CommandSyntaxException {
/* 55 */     int count = 0;
/*    */     
/* 57 */     for (ServerPlayer player : players) {
/* 58 */       count += player.getInventory().clearOrCountMatchingItems(predicate, maxCount, (Container)player.inventoryMenu.getCraftSlots());
/*    */       
/* 60 */       player.containerMenu.broadcastChanges();
/*    */ 
/*    */       
/* 63 */       player.inventoryMenu.slotsChanged((Container)player.getInventory());
/*    */     } 
/*    */     
/* 66 */     if (count == 0) {
/* 67 */       if (players.size() == 1) {
/* 68 */         throw ERROR_SINGLE.create(((ServerPlayer)players.iterator().next()).getName());
/*    */       }
/* 70 */       throw ERROR_MULTIPLE.create(players.size());
/*    */     } 
/*    */ 
/*    */     
/* 74 */     int finalCount = count;
/* 75 */     if (maxCount == 0) {
/* 76 */       if (players.size() == 1) {
/* 77 */         source.sendSuccess(() -> Component.translatable("commands.clear.test.single", new Object[] { finalCount, ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*    */       } else {
/* 79 */         source.sendSuccess(() -> Component.translatable("commands.clear.test.multiple", new Object[] { finalCount, players.size() }), true);
/*    */       }
/*    */     
/* 82 */     } else if (players.size() == 1) {
/* 83 */       source.sendSuccess(() -> Component.translatable("commands.clear.success.single", new Object[] { finalCount, ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 85 */       source.sendSuccess(() -> Component.translatable("commands.clear.success.multiple", new Object[] { finalCount, players.size() }), true);
/*    */     } 
/*    */ 
/*    */     
/* 89 */     return count;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/ClearInventoryCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */