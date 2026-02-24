/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.Subscription;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ class RealmsSubscriptionTab extends GridLayoutTab implements RealmsConfigurationTab {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final int DEFAULT_COMPONENT_WIDTH = 200;
/*     */   private static final int EXTRA_SPACING = 2;
/*     */   private static final int DEFAULT_SPACING = 6;
/*  39 */   static final Component TITLE = (Component)Component.translatable("mco.configure.world.subscription.tab");
/*  40 */   private static final Component SUBSCRIPTION_START_LABEL = (Component)Component.translatable("mco.configure.world.subscription.start");
/*  41 */   private static final Component TIME_LEFT_LABEL = (Component)Component.translatable("mco.configure.world.subscription.timeleft");
/*  42 */   private static final Component DAYS_LEFT_LABEL = (Component)Component.translatable("mco.configure.world.subscription.recurring.daysleft");
/*  43 */   private static final Component SUBSCRIPTION_EXPIRED_TEXT = (Component)Component.translatable("mco.configure.world.subscription.expired").withStyle(ChatFormatting.GRAY);
/*  44 */   private static final Component SUBSCRIPTION_LESS_THAN_A_DAY_TEXT = (Component)Component.translatable("mco.configure.world.subscription.less_than_a_day").withStyle(ChatFormatting.GRAY);
/*  45 */   private static final Component UNKNOWN = (Component)Component.translatable("mco.configure.world.subscription.unknown");
/*  46 */   private static final Component RECURRING_INFO = (Component)Component.translatable("mco.configure.world.subscription.recurring.info");
/*     */   
/*     */   private final RealmsConfigureWorldScreen configurationScreen;
/*     */   private final Minecraft minecraft;
/*     */   private final Button deleteButton;
/*     */   private final FocusableTextWidget subscriptionInfo;
/*     */   private final StringWidget startDateWidget;
/*     */   private final StringWidget daysLeftLabelWidget;
/*     */   private final StringWidget daysLeftWidget;
/*     */   private RealmsServer serverData;
/*  56 */   private Component daysLeft = UNKNOWN;
/*  57 */   private Component startDate = UNKNOWN;
/*     */   private Subscription.SubscriptionType type;
/*     */   
/*     */   RealmsSubscriptionTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData) {
/*  61 */     super(TITLE);
/*  62 */     this.configurationScreen = configurationScreen;
/*  63 */     this.minecraft = minecraft;
/*  64 */     this.serverData = serverData;
/*     */     
/*  66 */     GridLayout.RowHelper helper = this.layout.rowSpacing(6).createRowHelper(1);
/*     */     
/*  68 */     Font font = configurationScreen.getFont();
/*  69 */     Objects.requireNonNull(font); helper.addChild((LayoutElement)new StringWidget(200, 9, SUBSCRIPTION_START_LABEL, font));
/*  70 */     Objects.requireNonNull(font); this.startDateWidget = (StringWidget)helper.addChild((LayoutElement)new StringWidget(200, 9, this.startDate, font));
/*     */     
/*  72 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  73 */     Objects.requireNonNull(font); this.daysLeftLabelWidget = (StringWidget)helper.addChild((LayoutElement)new StringWidget(200, 9, TIME_LEFT_LABEL, font));
/*  74 */     Objects.requireNonNull(font); this.daysLeftWidget = (StringWidget)helper.addChild((LayoutElement)new StringWidget(200, 9, this.daysLeft, font));
/*     */     
/*  76 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  77 */     helper.addChild((LayoutElement)Button.builder(
/*  78 */           (Component)Component.translatable("mco.configure.world.subscription.extend"), button -> ConfirmLinkScreen.confirmLinkNow((Screen)configurationScreen, CommonLinks.extendRealms(serverData.remoteSubscriptionId, minecraft.getUser().getProfileId())))
/*     */         
/*  80 */         .bounds(0, 0, 200, 20).build());
/*     */     
/*  82 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  83 */     this.deleteButton = (Button)helper.addChild((LayoutElement)Button.builder(
/*  84 */           (Component)Component.translatable("mco.configure.world.delete.button"), button -> minecraft.setScreen((Screen)RealmsPopups.warningPopupScreen((Screen)minecraft, (Component)Component.translatable("mco.configure.world.delete.question.line1"), ())))
/*     */ 
/*     */         
/*  87 */         .bounds(0, 0, 200, 20).build());
/*     */ 
/*     */     
/*  90 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  91 */     this.subscriptionInfo = (FocusableTextWidget)helper.addChild((LayoutElement)FocusableTextWidget.builder((Component)Component.empty(), font).maxWidth(200).build(), net.minecraft.client.gui.layouts.LayoutSettings.defaults().alignHorizontallyCenter());
/*  92 */     this.subscriptionInfo.setCentered(false);
/*     */     
/*  94 */     updateData(serverData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteRealm() {
/* 100 */     Objects.requireNonNull(this.configurationScreen); RealmsUtil.runAsync(client -> client.deleteRealm(this.serverData.id), RealmsUtil.openScreenAndLogOnFailure(this.configurationScreen::createErrorScreen, "Couldn't delete world"))
/* 101 */       .thenRunAsync(() -> this.minecraft.setScreen(this.configurationScreen.getLastScreen()), (Executor)this.minecraft);
/*     */     
/* 103 */     this.minecraft.setScreen((Screen)this.configurationScreen);
/*     */   }
/*     */   
/*     */   private void getSubscription(long realmId) {
/* 107 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 109 */       Subscription subscription = client.subscriptionFor(realmId);
/* 110 */       this.daysLeft = daysLeftPresentation(subscription.daysLeft());
/* 111 */       this.startDate = localPresentation(subscription.startDate());
/* 112 */       this.type = subscription.type();
/* 113 */     } catch (RealmsServiceException e) {
/* 114 */       LOGGER.error("Couldn't get subscription", (Throwable)e);
/* 115 */       this.minecraft.setScreen(this.configurationScreen.createErrorScreen(e));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Component localPresentation(Instant time) {
/* 120 */     String formattedDate = ZonedDateTime.ofInstant(time, java.time.ZoneId.systemDefault()).format(Util.localizedDateFormatter(FormatStyle.MEDIUM));
/* 121 */     return (Component)Component.literal(formattedDate).withStyle(ChatFormatting.GRAY);
/*     */   }
/*     */   
/*     */   private Component daysLeftPresentation(int daysLeft) {
/* 125 */     if (daysLeft < 0 && this.serverData.expired)
/* 126 */       return SUBSCRIPTION_EXPIRED_TEXT; 
/* 127 */     if (daysLeft <= 1) {
/* 128 */       return SUBSCRIPTION_LESS_THAN_A_DAY_TEXT;
/*     */     }
/*     */     
/* 131 */     int months = daysLeft / 30;
/* 132 */     int days = daysLeft % 30;
/* 133 */     boolean showMonths = (months > 0);
/* 134 */     boolean showDays = (days > 0);
/*     */     
/* 136 */     if (showMonths && showDays)
/* 137 */       return (Component)Component.translatable("mco.configure.world.subscription.remaining.months.days", new Object[] { months, days }).withStyle(ChatFormatting.GRAY); 
/* 138 */     if (showMonths)
/* 139 */       return (Component)Component.translatable("mco.configure.world.subscription.remaining.months", new Object[] { months }).withStyle(ChatFormatting.GRAY); 
/* 140 */     if (showDays) {
/* 141 */       return (Component)Component.translatable("mco.configure.world.subscription.remaining.days", new Object[] { days }).withStyle(ChatFormatting.GRAY);
/*     */     }
/* 143 */     return (Component)Component.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/* 149 */     this.serverData = serverData;
/* 150 */     getSubscription(serverData.id);
/*     */     
/* 152 */     this.startDateWidget.setMessage(this.startDate);
/* 153 */     if (this.type == Subscription.SubscriptionType.NORMAL) {
/* 154 */       this.daysLeftLabelWidget.setMessage(TIME_LEFT_LABEL);
/* 155 */     } else if (this.type == Subscription.SubscriptionType.RECURRING) {
/* 156 */       this.daysLeftLabelWidget.setMessage(DAYS_LEFT_LABEL);
/*     */     } 
/* 158 */     this.daysLeftWidget.setMessage(this.daysLeft);
/*     */     
/* 160 */     boolean snapshotWorld = (RealmsMainScreen.isSnapshot() && serverData.parentWorldName != null);
/* 161 */     this.deleteButton.active = serverData.expired;
/*     */     
/* 163 */     if (snapshotWorld) {
/* 164 */       this.subscriptionInfo.setMessage((Component)Component.translatable("mco.snapshot.subscription.info", new Object[] { serverData.parentWorldName }));
/*     */     } else {
/* 166 */       this.subscriptionInfo.setMessage(RECURRING_INFO);
/*     */     } 
/* 168 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getTabExtraNarration() {
/* 173 */     return CommonComponents.joinLines(new Component[] { TITLE, SUBSCRIPTION_START_LABEL, this.startDate, TIME_LEFT_LABEL, this.daysLeft });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsSubscriptionTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */