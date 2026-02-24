/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ 
/*    */ public class DebugMobSpawningCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 18 */     LiteralArgumentBuilder<CommandSourceStack> base = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("debugmobspawning")
/* 19 */       .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*    */     
/* 21 */     for (MobCategory mobCategory : MobCategory.values()) {
/* 22 */       base.then(
/* 23 */           Commands.literal(mobCategory.getName())
/* 24 */           .then(
/* 25 */             Commands.argument("at", (ArgumentType)BlockPosArgument.blockPos())
/* 26 */             .executes(c -> spawnMobs((CommandSourceStack)c.getSource(), mobCategory, BlockPosArgument.getLoadedBlockPos(c, "at")))));
/*    */     }
/*    */ 
/*    */     
/* 30 */     dispatcher.register(base);
/*    */   }
/*    */   
/*    */   private static int spawnMobs(CommandSourceStack source, MobCategory mobCategory, BlockPos at) {
/* 34 */     NaturalSpawner.spawnCategoryForPosition(mobCategory, source.getLevel(), at);
/* 35 */     return 1;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/DebugMobSpawningCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */