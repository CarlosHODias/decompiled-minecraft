/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.RealmsMainScreen;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.dto.RealmsSetting;
/*    */ import com.mojang.realmsclient.dto.RealmsSlot;
/*    */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*    */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*    */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.AlertScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsCreateWorldFlow {
/* 35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static void createWorld(Minecraft minecraft, Screen returnScreen, Screen lastScreen, int slot, RealmsServer realmsServer, RealmCreationTask realmCreationTask) {
/* 38 */     CreateWorldScreen.openFresh(minecraft, () -> minecraft.setScreen(returnScreen), (createWorldScreen, finalLayers, worldData, tempDataPackDir) -> {
/*    */           Path worldFolder;
/*    */           try {
/*    */             worldFolder = createTemporaryWorldFolder(finalLayers, worldData, tempDataPackDir);
/* 42 */           } catch (IOException e) {
/*    */             LOGGER.warn("Failed to create temporary world folder.");
/*    */             minecraft.setScreen((Screen)new RealmsGenericErrorScreen((Component)Component.translatable("mco.create.world.failed"), lastScreen));
/*    */             return true;
/*    */           } 
/*    */           RealmsWorldOptions realmsWorldOptions = RealmsWorldOptions.createFromSettings(worldData.getLevelSettings(), SharedConstants.getCurrentVersion().name());
/*    */           RealmsSlot realmsSlot = new RealmsSlot(slot, realmsWorldOptions, List.of(RealmsSetting.hardcoreSetting(worldData.getLevelSettings().hardcore())));
/*    */           RealmsWorldUpload realmsWorldUpload = new RealmsWorldUpload(worldFolder, realmsSlot, minecraft.getUser(), realmsServer.id, RealmsWorldUploadStatusTracker.noOp());
/*    */           Objects.requireNonNull(realmsWorldUpload);
/*    */           minecraft.setScreenAndShow((Screen)new AlertScreen(realmsWorldUpload::cancel, (Component)Component.translatable("mco.create.world.reset.title"), (Component)Component.empty(), CommonComponents.GUI_CANCEL, false));
/*    */           if (realmCreationTask != null) {
/*    */             realmCreationTask.run();
/*    */           }
/*    */           realmsWorldUpload.packAndUpload().handleAsync((), (Executor)minecraft);
/*    */           return true;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Path createTemporaryWorldFolder(LayeredRegistryAccess<RegistryLayer> finalLayers, PrimaryLevelData worldData, Path tempDataPackDir) throws IOException {
/* 88 */     Path worldFolder = Files.createTempDirectory("minecraft_realms_world_upload", (FileAttribute<?>[])new FileAttribute[0]);
/* 89 */     if (tempDataPackDir != null) {
/* 90 */       Files.move(tempDataPackDir, worldFolder.resolve("datapacks"), new java.nio.file.CopyOption[0]);
/*    */     }
/*    */     
/* 93 */     CompoundTag dataTag = worldData.createTag((RegistryAccess)finalLayers.compositeAccess(), null);
/* 94 */     CompoundTag root = new CompoundTag();
/* 95 */     root.put("Data", (net.minecraft.nbt.Tag)dataTag);
/* 96 */     Path levelDat = Files.createFile(worldFolder.resolve("level.dat"), (FileAttribute<?>[])new FileAttribute[0]);
/* 97 */     NbtIo.writeCompressed(root, levelDat);
/* 98 */     return worldFolder;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsCreateWorldFlow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */