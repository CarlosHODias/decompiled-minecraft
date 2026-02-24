/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class RealmsInviteScreen extends net.minecraft.realms.RealmsScreen {
/*  21 */   private static final Component TITLE = (Component)Component.translatable("mco.configure.world.buttons.invite");
/*  22 */   private static final Component NAME_LABEL = (Component)Component.translatable("mco.configure.world.invite.profile.name").withColor(-6250336);
/*  23 */   private static final Component INVITING_PLAYER_TEXT = (Component)Component.translatable("mco.configure.world.players.inviting").withColor(-6250336);
/*  24 */   private static final Component NO_SUCH_PLAYER_ERROR_TEXT = (Component)Component.translatable("mco.configure.world.players.error").withColor(-65536);
/*  25 */   private static final Component DUPLICATE_PLAYER_TEXT = (Component)Component.translatable("mco.configure.world.players.invite.duplicate").withColor(-65536);
/*     */   
/*  27 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private EditBox profileName;
/*     */   
/*     */   private Button inviteButton;
/*     */   private final RealmsServer serverData;
/*     */   private final RealmsConfigureWorldScreen configureScreen;
/*     */   private Component message;
/*     */   
/*     */   public RealmsInviteScreen(RealmsConfigureWorldScreen configureScreen, RealmsServer serverData) {
/*  37 */     super(TITLE);
/*  38 */     this.configureScreen = configureScreen;
/*  39 */     this.serverData = serverData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  44 */     this.layout.addTitleHeader(TITLE, this.font);
/*     */     
/*  46 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical().spacing(8));
/*     */     
/*  48 */     this.profileName = new EditBox(this.minecraft.font, 200, 20, (Component)Component.translatable("mco.configure.world.invite.profile.name"));
/*  49 */     content.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.profileName, NAME_LABEL));
/*  50 */     this.inviteButton = (Button)content.addChild((LayoutElement)Button.builder(TITLE, button -> onInvite()).width(200).build());
/*     */     
/*  52 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(200).build());
/*     */     
/*  54 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  55 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  60 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  65 */     if (this.profileName != null) {
/*  66 */       setInitialFocus((GuiEventListener)this.profileName);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onInvite() {
/*  71 */     if (this.inviteButton == null || this.profileName == null) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     if (net.minecraft.util.StringUtil.isBlank(this.profileName.getValue())) {
/*  76 */       showMessage(NO_SUCH_PLAYER_ERROR_TEXT);
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     if (this.serverData.players.stream().anyMatch(player -> player.name.equalsIgnoreCase(this.profileName.getValue()))) {
/*  81 */       showMessage(DUPLICATE_PLAYER_TEXT);
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     long serverId = this.serverData.id;
/*  86 */     String name = this.profileName.getValue().trim();
/*  87 */     this.inviteButton.active = false;
/*  88 */     this.profileName.setEditable(false);
/*  89 */     showMessage(INVITING_PLAYER_TEXT);
/*     */     
/*  91 */     CompletableFuture.supplyAsync(() -> this.configureScreen.invitePlayer(serverId, name), (Executor)Util.ioPool())
/*  92 */       .thenAcceptAsync(success -> {
/*     */           if (success) {
/*     */             this.minecraft.setScreen((Screen)this.configureScreen);
/*     */           } else {
/*     */             showMessage(NO_SUCH_PLAYER_ERROR_TEXT);
/*     */           } 
/*     */           this.profileName.setEditable(true);
/*     */           this.inviteButton.active = true;
/*     */         }, this.screenExecutor);
/*     */   }
/*     */   
/*     */   private void showMessage(Component message) {
/* 104 */     this.message = message;
/* 105 */     this.minecraft.getNarrator().saySystemNow(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 110 */     this.minecraft.setScreen((Screen)this.configureScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 115 */     super.render(graphics, xm, ym, a);
/*     */     
/* 117 */     if (this.message != null && this.inviteButton != null)
/* 118 */       graphics.drawCenteredString(this.font, this.message, this.width / 2, this.inviteButton.getY() + this.inviteButton.getHeight() + 8, -1); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsInviteScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */