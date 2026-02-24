/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.BanDetails;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.net.URI;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.multiplayer.chat.report.BanReason;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class BanNoticeScreens {
/*  20 */   private static final Component TEMPORARY_BAN_TITLE = (Component)Component.translatable("gui.banned.title.temporary").withStyle(ChatFormatting.BOLD);
/*  21 */   private static final Component PERMANENT_BAN_TITLE = (Component)Component.translatable("gui.banned.title.permanent").withStyle(ChatFormatting.BOLD);
/*     */   
/*  23 */   public static final Component NAME_BAN_TITLE = (Component)Component.translatable("gui.banned.name.title").withStyle(ChatFormatting.BOLD);
/*     */   
/*  25 */   private static final Component SKIN_BAN_TITLE = (Component)Component.translatable("gui.banned.skin.title").withStyle(ChatFormatting.BOLD);
/*  26 */   private static final Component SKIN_BAN_DESCRIPTION = (Component)Component.translatable("gui.banned.skin.description", new Object[] { Component.translationArg(CommonLinks.SUSPENSION_HELP) });
/*     */   
/*     */   public static ConfirmLinkScreen create(BooleanConsumer callback, BanDetails multiplayerBanned) {
/*  29 */     return new ConfirmLinkScreen(callback, getBannedTitle(multiplayerBanned), getBannedScreenText(multiplayerBanned), CommonLinks.SUSPENSION_HELP, CommonComponents.GUI_ACKNOWLEDGE, true);
/*     */   }
/*     */   
/*     */   public static ConfirmLinkScreen createSkinBan(Runnable onClose) {
/*  33 */     URI uri = CommonLinks.SUSPENSION_HELP;
/*  34 */     return new ConfirmLinkScreen(result -> { if (result) Util.getPlatform().openUri(uri);  onClose.run(); }, SKIN_BAN_TITLE, SKIN_BAN_DESCRIPTION, uri, CommonComponents.GUI_ACKNOWLEDGE, true);
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
/*     */   public static ConfirmLinkScreen createNameBan(String name, Runnable onClose) {
/*  50 */     URI uri = CommonLinks.SUSPENSION_HELP;
/*  51 */     return new ConfirmLinkScreen(result -> { if (result) Util.getPlatform().openUri(uri);  onClose.run(); }, NAME_BAN_TITLE, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  59 */         (Component)Component.translatable("gui.banned.name.description", new Object[] { Component.literal(name).withStyle(ChatFormatting.YELLOW), Component.translationArg(CommonLinks.SUSPENSION_HELP) }), uri, CommonComponents.GUI_ACKNOWLEDGE, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Component getBannedTitle(BanDetails multiplayerBanned) {
/*  67 */     return isTemporaryBan(multiplayerBanned) ? TEMPORARY_BAN_TITLE : PERMANENT_BAN_TITLE;
/*     */   }
/*     */   
/*     */   private static Component getBannedScreenText(BanDetails multiplayerBanned) {
/*  71 */     return (Component)Component.translatable("gui.banned.description", new Object[] {
/*  72 */           getBanReasonText(multiplayerBanned), 
/*  73 */           getBanStatusText(multiplayerBanned), 
/*  74 */           Component.translationArg(CommonLinks.SUSPENSION_HELP)
/*     */         });
/*     */   }
/*     */   
/*     */   private static Component getBanReasonText(BanDetails multiplayerBanned) {
/*  79 */     String reasonString = multiplayerBanned.reason();
/*  80 */     String reasonMessage = multiplayerBanned.reasonMessage();
/*  81 */     if (StringUtils.isNumeric(reasonString)) {
/*  82 */       MutableComponent mutableComponent; int reasonId = Integer.parseInt(reasonString);
/*  83 */       BanReason reason = BanReason.byId(reasonId);
/*     */       
/*  85 */       if (reason != null) {
/*  86 */         Component reasonText = ComponentUtils.mergeStyles(reason.title(), Style.EMPTY.withBold(true));
/*  87 */       } else if (reasonMessage != null) {
/*  88 */         mutableComponent = Component.translatable("gui.banned.description.reason_id_message", new Object[] { reasonId, reasonMessage }).withStyle(ChatFormatting.BOLD);
/*     */       } else {
/*  90 */         mutableComponent = Component.translatable("gui.banned.description.reason_id", new Object[] { reasonId }).withStyle(ChatFormatting.BOLD);
/*     */       } 
/*  92 */       return (Component)Component.translatable("gui.banned.description.reason", new Object[] { mutableComponent });
/*     */     } 
/*  94 */     return (Component)Component.translatable("gui.banned.description.unknownreason");
/*     */   }
/*     */   
/*     */   private static Component getBanStatusText(BanDetails multiplayerBanned) {
/*  98 */     if (isTemporaryBan(multiplayerBanned)) {
/*  99 */       Component banDurationText = getBanDurationText(multiplayerBanned);
/* 100 */       return (Component)Component.translatable("gui.banned.description.temporary", new Object[] {
/* 101 */             Component.translatable("gui.banned.description.temporary.duration", new Object[] { banDurationText }).withStyle(ChatFormatting.BOLD)
/*     */           });
/*     */     } 
/* 104 */     return (Component)Component.translatable("gui.banned.description.permanent").withStyle(ChatFormatting.BOLD);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Component getBanDurationText(BanDetails multiplayerBanned) {
/* 109 */     Duration banDuration = Duration.between(Instant.now(), multiplayerBanned.expires());
/* 110 */     long durationHours = banDuration.toHours();
/* 111 */     if (durationHours > 72L)
/* 112 */       return (Component)CommonComponents.days(banDuration.toDays()); 
/* 113 */     if (durationHours < 1L) {
/* 114 */       return (Component)CommonComponents.minutes(banDuration.toMinutes());
/*     */     }
/* 116 */     return (Component)CommonComponents.hours(banDuration.toHours());
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isTemporaryBan(BanDetails multiplayerBanned) {
/* 121 */     return (multiplayerBanned.expires() != null);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/BanNoticeScreens.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */