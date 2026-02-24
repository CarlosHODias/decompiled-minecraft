/*     */ package com.mojang.realmsclient.util;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import java.time.Instant;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.PlayerFaceRenderer;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsUtil
/*     */ {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  25 */   private static final Component RIGHT_NOW = (Component)Component.translatable("mco.util.time.now");
/*     */   
/*     */   private static final int MINUTES = 60;
/*     */   private static final int HOURS = 3600;
/*     */   private static final int DAYS = 86400;
/*     */   
/*     */   public static Component convertToAgePresentation(long timeDiff) {
/*  32 */     if (timeDiff < 0L) {
/*  33 */       return RIGHT_NOW;
/*     */     }
/*     */     
/*  36 */     long timeDiffInSeconds = timeDiff / 1000L;
/*     */     
/*  38 */     if (timeDiffInSeconds < 60L) {
/*  39 */       return (Component)Component.translatable("mco.time.secondsAgo", new Object[] { timeDiffInSeconds });
/*     */     }
/*     */     
/*  42 */     if (timeDiffInSeconds < 3600L) {
/*  43 */       long minutes = timeDiffInSeconds / 60L;
/*  44 */       return (Component)Component.translatable("mco.time.minutesAgo", new Object[] { minutes });
/*     */     } 
/*     */     
/*  47 */     if (timeDiffInSeconds < 86400L) {
/*  48 */       long hours = timeDiffInSeconds / 3600L;
/*  49 */       return (Component)Component.translatable("mco.time.hoursAgo", new Object[] { hours });
/*     */     } 
/*     */     
/*  52 */     long days = timeDiffInSeconds / 86400L;
/*  53 */     return (Component)Component.translatable("mco.time.daysAgo", new Object[] { days });
/*     */   }
/*     */   
/*     */   public static Component convertToAgePresentationFromInstant(Instant date) {
/*  57 */     return convertToAgePresentation(System.currentTimeMillis() - date.toEpochMilli());
/*     */   }
/*     */   
/*     */   public static void renderPlayerFace(GuiGraphics graphics, int x, int y, int size, UUID playerId) {
/*  61 */     PlayerSkinRenderCache.RenderInfo renderInfo = Minecraft.getInstance().playerSkinRenderCache().getOrDefault(ResolvableProfile.createUnresolved(playerId));
/*  62 */     PlayerFaceRenderer.draw(graphics, renderInfo.playerSkin(), x, y, size);
/*     */   }
/*     */   
/*     */   public static <T> CompletableFuture<T> supplyAsync(RealmsIoFunction<T> function, Consumer<RealmsServiceException> onFailure) {
/*  66 */     return CompletableFuture.supplyAsync(() -> {
/*     */           RealmsClient client = RealmsClient.getOrCreate();
/*     */           try {
/*     */             return function.apply(client);
/*  70 */           } catch (Throwable t) {
/*     */             if (t instanceof RealmsServiceException) {
/*     */               RealmsServiceException e = (RealmsServiceException)t;
/*     */               if (onFailure != null)
/*     */                 onFailure.accept(e); 
/*     */             } else {
/*     */               LOGGER.error("Unhandled exception", t);
/*     */             } 
/*     */             throw new RuntimeException(t);
/*     */           } 
/*  80 */         }, (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Void> runAsync(RealmsIoConsumer function, Consumer<RealmsServiceException> onFailure) {
/*  84 */     return supplyAsync(function, onFailure);
/*     */   }
/*     */   
/*     */   public static Consumer<RealmsServiceException> openScreenOnFailure(Function<RealmsServiceException, Screen> errorScreen) {
/*  88 */     Minecraft minecraft = Minecraft.getInstance();
/*  89 */     return e -> minecraft.execute(());
/*     */   }
/*     */   
/*     */   public static Consumer<RealmsServiceException> openScreenAndLogOnFailure(Function<RealmsServiceException, Screen> errorScreen, String errorMessage) {
/*  93 */     return openScreenOnFailure(errorScreen)
/*  94 */       .andThen(e -> LOGGER.error(errorMessage, (Throwable)e));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RealmsIoFunction<T>
/*     */   {
/*     */     T apply(RealmsClient param1RealmsClient) throws RealmsServiceException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RealmsIoConsumer
/*     */     extends RealmsIoFunction<Void>
/*     */   {
/*     */     default Void apply(RealmsClient client) throws RealmsServiceException {
/* 108 */       accept(client);
/* 109 */       return null;
/*     */     }
/*     */     
/*     */     void accept(RealmsClient param1RealmsClient) throws RealmsServiceException;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/RealmsUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */