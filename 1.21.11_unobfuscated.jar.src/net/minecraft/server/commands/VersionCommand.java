/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.WorldVersion;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ 
/*    */ public class VersionCommand {
/* 16 */   private static final Component HEADER = (Component)Component.translatable("commands.version.header");
/* 17 */   private static final Component STABLE = (Component)Component.translatable("commands.version.stable.yes");
/* 18 */   private static final Component UNSTABLE = (Component)Component.translatable("commands.version.stable.no");
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean checkPermissions) {
/* 21 */     dispatcher.register(
/* 22 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("version")
/*    */         
/* 24 */         .requires((java.util.function.Predicate)Commands.hasPermission(checkPermissions ? Commands.LEVEL_GAMEMASTERS : Commands.LEVEL_ALL)))
/* 25 */         .executes(c -> {
/*    */             CommandSourceStack source = (CommandSourceStack)c.getSource();
/*    */             source.sendSystemMessage(HEADER);
/*    */             Objects.requireNonNull(source);
/*    */             dumpVersion(source::sendSystemMessage);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */   
/*    */   public static void dumpVersion(Consumer<Component> output) {
/* 35 */     WorldVersion version = SharedConstants.getCurrentVersion();
/* 36 */     output.accept(Component.translatable("commands.version.id", new Object[] { version.id() }));
/* 37 */     output.accept(Component.translatable("commands.version.name", new Object[] { version.name() }));
/* 38 */     output.accept(Component.translatable("commands.version.data", new Object[] { version.dataVersion().version() }));
/* 39 */     output.accept(Component.translatable("commands.version.series", new Object[] { version.dataVersion().series() }));
/* 40 */     output.accept(Component.translatable("commands.version.protocol", new Object[] { version.protocolVersion(), "0x" + Integer.toHexString(version.protocolVersion()) }));
/* 41 */     output.accept(Component.translatable("commands.version.build_time", new Object[] { Component.translationArg(version.buildTime()) }));
/* 42 */     output.accept(Component.translatable("commands.version.pack.resource", new Object[] { version.packVersion(PackType.CLIENT_RESOURCES).toString() }));
/* 43 */     output.accept(Component.translatable("commands.version.pack.data", new Object[] { version.packVersion(PackType.SERVER_DATA).toString() }));
/* 44 */     output.accept(version.stable() ? STABLE : UNSTABLE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/VersionCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */