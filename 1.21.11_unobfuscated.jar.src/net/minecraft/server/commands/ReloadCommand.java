/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.world.level.storage.WorldData;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadCommand {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static void reloadPacks(Collection<String> selectedPacks, CommandSourceStack source) {
/* 22 */     source.getServer().reloadResources(selectedPacks).exceptionally(throwable -> {
/*    */           LOGGER.warn("Failed to execute reload", throwable);
/*    */           source.sendFailure((Component)Component.translatable("commands.reload.failure"));
/*    */           return null;
/*    */         });
/*    */   }
/*    */   
/*    */   private static Collection<String> discoverNewPacks(PackRepository packRepository, WorldData worldData, Collection<String> currentPacks) {
/* 30 */     packRepository.reload();
/* 31 */     Collection<String> selected = Lists.newArrayList(currentPacks);
/* 32 */     Collection<String> disabled = worldData.getDataConfiguration().dataPacks().getDisabled();
/*    */     
/* 34 */     for (String pack : (Iterable<String>)packRepository.getAvailableIds()) {
/* 35 */       if (!disabled.contains(pack) && !selected.contains(pack)) {
/* 36 */         selected.add(pack);
/*    */       }
/*    */     } 
/* 39 */     return selected;
/*    */   }
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 43 */     dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reload")
/* 44 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 45 */         .executes(s -> {
/*    */             CommandSourceStack source = (CommandSourceStack)s.getSource();
/*    */             MinecraftServer server = source.getServer();
/*    */             PackRepository packRepository = server.getPackRepository();
/*    */             WorldData worldData = server.getWorldData();
/*    */             Collection<String> currentPacks = packRepository.getSelectedIds(), newSelectedPacks = discoverNewPacks(packRepository, worldData, currentPacks);
/*    */             source.sendSuccess((), true);
/*    */             reloadPacks(newSelectedPacks, source);
/*    */             return 0;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/ReloadCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */