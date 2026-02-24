/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.PendingInvite;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsPendingInvitesScreen extends RealmsScreen {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  41 */   private static final Component NO_PENDING_INVITES_TEXT = (Component)Component.translatable("mco.invites.nopending");
/*     */ 
/*     */ 
/*     */   
/*     */   private final Screen lastScreen;
/*     */ 
/*     */   
/*     */   private final CompletableFuture<List<PendingInvite>> pendingInvites;
/*     */ 
/*     */   
/*     */   private final HeaderAndFooterLayout layout;
/*     */ 
/*     */   
/*     */   private PendingInvitationSelectionList pendingInvitationSelectionList;
/*     */ 
/*     */ 
/*     */   
/*     */   public RealmsPendingInvitesScreen(Screen lastScreen, Component title) {
/*  59 */     super(title); this.pendingInvites = CompletableFuture.supplyAsync(() -> { try { return RealmsClient.getOrCreate().pendingInvites().pendingInvites(); } catch (RealmsServiceException e) { LOGGER.error("Couldn't list invites", (Throwable)e); return List.of(); } 
/*  60 */         }, (Executor)Util.ioPool()); this.layout = new HeaderAndFooterLayout((Screen)this); this.lastScreen = lastScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  65 */     RealmsMainScreen.refreshPendingInvites();
/*  66 */     this.layout.addTitleHeader(this.title, this.font);
/*  67 */     this.pendingInvitationSelectionList = (PendingInvitationSelectionList)this.layout.addToContents((LayoutElement)new PendingInvitationSelectionList(this, this.minecraft));
/*  68 */     this.pendingInvites.thenAcceptAsync(invites -> { List<Entry> entries = invites.stream().map(()).toList(); this.pendingInvitationSelectionList.replaceEntries(entries); if (entries.isEmpty()) this.minecraft.getNarrator().saySystemQueued(NO_PENDING_INVITES_TEXT);  }, this.screenExecutor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*  77 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  78 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  83 */     this.layout.arrangeElements();
/*  84 */     if (this.pendingInvitationSelectionList != null) {
/*  85 */       this.pendingInvitationSelectionList.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  91 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/*  96 */     super.render(graphics, xm, ym, a);
/*     */     
/*  98 */     if (this.pendingInvites.isDone() && this.pendingInvitationSelectionList.hasPendingInvites())
/*  99 */       graphics.drawCenteredString(this.font, NO_PENDING_INVITES_TEXT, this.width / 2, this.height / 2 - 20, -1); 
/*     */   }
/*     */   
/*     */   private class PendingInvitationSelectionList
/*     */     extends ContainerObjectSelectionList<Entry> {
/*     */     public static final int ITEM_HEIGHT = 36;
/*     */     
/*     */     public PendingInvitationSelectionList(RealmsPendingInvitesScreen this$0, Minecraft minecraft) {
/* 107 */       super(minecraft, this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 36);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 112 */       return 280;
/*     */     }
/*     */     
/*     */     public boolean hasPendingInvites() {
/* 116 */       return (getItemCount() == 0);
/*     */     }
/*     */     
/*     */     public void removeInvitation(RealmsPendingInvitesScreen.Entry entry) {
/* 120 */       removeEntry((net.minecraft.client.gui.components.AbstractSelectionList.Entry)entry);
/*     */     }
/*     */   }
/*     */   
/*     */   private class Entry extends ContainerObjectSelectionList.Entry<Entry> {
/* 125 */     private static final Component ACCEPT_INVITE = (Component)Component.translatable("mco.invites.button.accept");
/* 126 */     private static final Component REJECT_INVITE = (Component)Component.translatable("mco.invites.button.reject");
/*     */     
/* 128 */     private static final WidgetSprites ACCEPT_SPRITE = new WidgetSprites(
/* 129 */         Identifier.withDefaultNamespace("pending_invite/accept"), 
/* 130 */         Identifier.withDefaultNamespace("pending_invite/accept_highlighted"));
/*     */     
/* 132 */     private static final WidgetSprites REJECT_SPRITE = new WidgetSprites(
/* 133 */         Identifier.withDefaultNamespace("pending_invite/reject"), 
/* 134 */         Identifier.withDefaultNamespace("pending_invite/reject_highlighted"));
/*     */     
/*     */     private static final int SPRITE_TEXTURE_SIZE = 18;
/*     */     
/*     */     private static final int SPRITE_SIZE = 21;
/*     */     
/*     */     private static final int TEXT_LEFT = 38;
/*     */     
/*     */     private final PendingInvite pendingInvite;
/* 143 */     private final List<AbstractWidget> children = new ArrayList<>();
/*     */     
/*     */     private final SpriteIconButton acceptButton;
/*     */     private final SpriteIconButton rejectButton;
/*     */     private final StringWidget realmName;
/*     */     private final StringWidget realmOwnerName;
/*     */     private final StringWidget inviteDate;
/*     */     
/*     */     Entry(PendingInvite pendingInvite) {
/* 152 */       this.pendingInvite = pendingInvite;
/*     */       
/* 154 */       int maxTextWidth = RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.getRowWidth() - 32 - 32 - 42;
/* 155 */       this
/* 156 */         .realmName = new StringWidget((Component)Component.literal(pendingInvite.realmName()), RealmsPendingInvitesScreen.this.font).setMaxWidth(maxTextWidth);
/* 157 */       this
/* 158 */         .realmOwnerName = new StringWidget((Component)Component.literal(pendingInvite.realmOwnerName()).withColor(-6250336), RealmsPendingInvitesScreen.this.font).setMaxWidth(maxTextWidth);
/* 159 */       this
/* 160 */         .inviteDate = new StringWidget(ComponentUtils.mergeStyles(RealmsUtil.convertToAgePresentationFromInstant(pendingInvite.date()), Style.EMPTY.withColor(-6250336)), RealmsPendingInvitesScreen.this.font).setMaxWidth(maxTextWidth);
/*     */       
/* 162 */       Button.CreateNarration narration = getCreateNarration(pendingInvite);
/* 163 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 168 */         .acceptButton = SpriteIconButton.builder(ACCEPT_INVITE, button -> handleInvitation(true), false).sprite(ACCEPT_SPRITE, 18, 18).size(21, 21).narration(narration).withTootip().build();
/*     */       
/* 170 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 175 */         .rejectButton = SpriteIconButton.builder(REJECT_INVITE, button -> handleInvitation(false), false).sprite(REJECT_SPRITE, 18, 18).size(21, 21).narration(narration).withTootip().build();
/* 176 */       this.children.addAll((Collection)List.of(this.acceptButton, this.rejectButton));
/*     */     }
/*     */     
/*     */     private Button.CreateNarration getCreateNarration(PendingInvite pendingInvite) {
/* 180 */       return defaultNarrationSupplier -> {
/*     */           MutableComponent narration = CommonComponents.joinForNarration(new Component[] { defaultNarrationSupplier.get(), (Component)Component.literal(pendingInvite.realmName()), (Component)Component.literal(pendingInvite.realmOwnerName()), RealmsUtil.convertToAgePresentationFromInstant(pendingInvite.date()) });
/*     */           return Component.translatable("narrator.select", new Object[] { narration });
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 191 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
/* 196 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 201 */       int x = getContentX();
/* 202 */       int y = getContentY();
/* 203 */       int textX = x + 38;
/* 204 */       RealmsUtil.renderPlayerFace(graphics, x, y, 32, this.pendingInvite.realmOwnerUuid());
/* 205 */       this.realmName.setPosition(textX, y + 1);
/* 206 */       this.realmName.renderWidget(graphics, mouseX, mouseY, x);
/* 207 */       this.realmOwnerName.setPosition(textX, y + 12);
/* 208 */       this.realmOwnerName.renderWidget(graphics, mouseX, mouseY, x);
/* 209 */       this.inviteDate.setPosition(textX, y + 24);
/* 210 */       this.inviteDate.renderWidget(graphics, mouseX, mouseY, x);
/*     */       
/* 212 */       int buttonY = y + getContentHeight() / 2 - 10;
/* 213 */       this.acceptButton.setPosition(x + getContentWidth() - 16 - 42, buttonY);
/* 214 */       this.acceptButton.render(graphics, mouseX, mouseY, a);
/* 215 */       this.rejectButton.setPosition(x + getContentWidth() - 8 - 21, buttonY);
/* 216 */       this.rejectButton.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/*     */     private void handleInvitation(boolean accept) {
/* 220 */       String invitationId = this.pendingInvite.invitationId();
/* 221 */       CompletableFuture.supplyAsync(() -> {
/*     */             try {
/*     */               RealmsClient client = RealmsClient.getOrCreate();
/*     */               if (accept) {
/*     */                 client.acceptInvitation(invitationId);
/*     */               } else {
/*     */                 client.rejectInvitation(invitationId);
/*     */               } 
/*     */               return true;
/* 230 */             } catch (RealmsServiceException e) {
/*     */               RealmsPendingInvitesScreen.LOGGER.error("Couldn't handle invite", (Throwable)e);
/*     */               return false;
/*     */             } 
/* 234 */           }, (Executor)Util.ioPool()).thenAcceptAsync(result -> {
/*     */             if (accept) {
/*     */               RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.removeInvitation(this);
/*     */               RealmsDataFetcher dataFetcher = RealmsPendingInvitesScreen.this.minecraft.realmsDataFetcher();
/*     */               if (accept) {
/*     */                 dataFetcher.serverListUpdateTask.reset();
/*     */               }
/*     */               dataFetcher.pendingInvitesTask.reset();
/*     */             } 
/* 243 */           }, RealmsPendingInvitesScreen.this.screenExecutor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsPendingInvitesScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */