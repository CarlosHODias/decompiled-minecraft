/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ public class SetWorldSpawnCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 23 */     dispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setworldspawn")
/* 25 */         .requires((java.util.function.Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 26 */         .executes(c -> setSpawn((CommandSourceStack)c.getSource(), BlockPos.containing((Position)((CommandSourceStack)c.getSource()).getPosition()), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 27 */         .then((
/* 28 */           (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 29 */           .executes(c -> setSpawn((CommandSourceStack)c.getSource(), BlockPosArgument.getSpawnablePos(c, "pos"), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 30 */           .then(
/* 31 */             Commands.argument("rotation", (ArgumentType)RotationArgument.rotation())
/* 32 */             .executes(c -> setSpawn((CommandSourceStack)c.getSource(), BlockPosArgument.getSpawnablePos(c, "pos"), RotationArgument.getRotation(c, "rotation"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setSpawn(CommandSourceStack source, BlockPos pos, Coordinates rotation) {
/* 39 */     ServerLevel level = source.getLevel();
/* 40 */     Vec2 rotationVector = rotation.getRotation(source);
/* 41 */     float yaw = rotationVector.y;
/* 42 */     float pitch = rotationVector.x;
/* 43 */     LevelData.RespawnData respawnData = LevelData.RespawnData.of(level.dimension(), pos, yaw, pitch);
/* 44 */     level.setRespawnData(respawnData);
/* 45 */     source.sendSuccess(() -> Component.translatable("commands.setworldspawn.success", new Object[] { pos.getX(), pos.getY(), pos.getZ(), respawnData.yaw(), respawnData.pitch(), level.dimension().identifier().toString() }), true);
/* 46 */     return 1;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/SetWorldSpawnCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */