/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public class SeedCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean checkPermissions) {
/* 13 */     dispatcher.register(
/* 14 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("seed")
/* 15 */         .requires((java.util.function.Predicate)Commands.hasPermission(checkPermissions ? Commands.LEVEL_GAMEMASTERS : Commands.LEVEL_ALL)))
/* 16 */         .executes(c -> {
/*    */             long seed = ((CommandSourceStack)c.getSource()).getLevel().getSeed();
/*    */             MutableComponent mutableComponent = ComponentUtils.copyOnClickText(String.valueOf(seed));
/*    */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*    */             return (int)seed;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/SeedCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */