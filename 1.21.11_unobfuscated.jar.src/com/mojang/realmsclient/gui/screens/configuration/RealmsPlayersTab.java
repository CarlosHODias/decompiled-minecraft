/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.Ops;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsConfirmScreen;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class RealmsPlayersTab extends net.minecraft.client.gui.components.tabs.GridLayoutTab implements RealmsConfigurationTab {
/*  34 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   
/*  36 */   static final Component TITLE = (Component)Component.translatable("mco.configure.world.players.title");
/*  37 */   private static final Component QUESTION_TITLE = (Component)Component.translatable("mco.question");
/*     */   
/*     */   private static final int PADDING = 8;
/*     */   
/*     */   private final RealmsConfigureWorldScreen configurationScreen;
/*     */   private final Minecraft minecraft;
/*     */   private final net.minecraft.client.gui.Font font;
/*     */   private RealmsServer serverData;
/*     */   private final InvitedObjectSelectionList invitedList;
/*     */   
/*     */   RealmsPlayersTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData) {
/*  48 */     super(TITLE);
/*  49 */     this.configurationScreen = configurationScreen;
/*  50 */     this.minecraft = minecraft;
/*  51 */     this.font = configurationScreen.getFont();
/*  52 */     this.serverData = serverData;
/*  53 */     GridLayout.RowHelper helper = this.layout.spacing(8).createRowHelper(1);
/*  54 */     this.invitedList = (InvitedObjectSelectionList)helper.addChild((LayoutElement)new InvitedObjectSelectionList(configurationScreen.width, calculateListHeight()), net.minecraft.client.gui.layouts.LayoutSettings.defaults().alignVerticallyTop().alignHorizontallyCenter());
/*  55 */     helper.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.buttons.invite"), button -> minecraft.setScreen((Screen)new RealmsInviteScreen(configurationScreen, serverData)))
/*     */         
/*  57 */         .build(), net.minecraft.client.gui.layouts.LayoutSettings.defaults().alignVerticallyBottom().alignHorizontallyCenter());
/*     */     
/*  59 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   public int calculateListHeight() {
/*  63 */     return this.configurationScreen.getContentHeight() - 20 - 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doLayout(ScreenRectangle screenRectangle) {
/*  68 */     this.invitedList.updateSizeAndPosition(this.configurationScreen.width, calculateListHeight(), this.configurationScreen.layout.getHeaderHeight());
/*  69 */     super.doLayout(screenRectangle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/*  74 */     this.serverData = serverData;
/*  75 */     this.invitedList.updateList(serverData);
/*     */   }
/*     */   
/*     */   private class InvitedObjectSelectionList extends ContainerObjectSelectionList<Entry> {
/*     */     private static final int PLAYER_ENTRY_HEIGHT = 36;
/*     */     
/*     */     public InvitedObjectSelectionList(int width, int height) {
/*  82 */       super(Minecraft.getInstance(), width, height, RealmsPlayersTab.this.configurationScreen.getHeaderHeight(), 36);
/*     */     }
/*     */     
/*     */     private void updateList(RealmsServer serverData) {
/*  86 */       clearEntries();
/*  87 */       populateList(serverData);
/*     */     }
/*     */     
/*     */     private void populateList(RealmsServer serverData) {
/*  91 */       RealmsPlayersTab.HeaderEntry entry = new RealmsPlayersTab.HeaderEntry();
/*  92 */       java.util.Objects.requireNonNull(RealmsPlayersTab.this.font); addEntry((AbstractSelectionList.Entry)entry, entry.height(9));
/*  93 */       for (RealmsPlayersTab.PlayerEntry newChild : (Iterable<RealmsPlayersTab.PlayerEntry>)serverData.players.stream().map(x$0 -> new RealmsPlayersTab.PlayerEntry(x$0)).toList()) {
/*  94 */         addEntry((AbstractSelectionList.Entry)newChild);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderListBackground(GuiGraphics graphics) {}
/*     */ 
/*     */     
/*     */     protected void renderListSeparators(GuiGraphics graphics) {}
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 106 */       return 300;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class Entry
/*     */     extends ContainerObjectSelectionList.Entry<Entry> {}
/*     */   
/*     */   private class PlayerEntry extends Entry {
/*     */     protected static final int SKIN_FACE_SIZE = 32;
/* 115 */     private static final Component NORMAL_USER_TEXT = (Component)Component.translatable("mco.configure.world.invites.normal.tooltip");
/* 116 */     private static final Component OP_TEXT = (Component)Component.translatable("mco.configure.world.invites.ops.tooltip");
/* 117 */     private static final Component REMOVE_TEXT = (Component)Component.translatable("mco.configure.world.invites.remove.tooltip");
/*     */     
/* 119 */     private static final Identifier MAKE_OP_SPRITE = Identifier.withDefaultNamespace("player_list/make_operator");
/* 120 */     private static final Identifier REMOVE_OP_SPRITE = Identifier.withDefaultNamespace("player_list/remove_operator");
/* 121 */     private static final Identifier REMOVE_PLAYER_SPRITE = Identifier.withDefaultNamespace("player_list/remove_player");
/*     */     
/*     */     private static final int ICON_WIDTH = 8;
/*     */     
/*     */     private static final int ICON_HEIGHT = 7;
/*     */     private final PlayerInfo playerInfo;
/*     */     private final Button removeButton;
/*     */     private final Button makeOpButton;
/*     */     private final Button removeOpButton;
/*     */     
/*     */     public PlayerEntry(PlayerInfo playerInfo) {
/* 132 */       this.playerInfo = playerInfo;
/* 133 */       int index = RealmsPlayersTab.this.serverData.players.indexOf(this.playerInfo);
/*     */       
/* 135 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 142 */         .makeOpButton = (Button)SpriteIconButton.builder(NORMAL_USER_TEXT, button -> op(index), false).sprite(MAKE_OP_SPRITE, 8, 7).width(16 + RealmsPlayersTab.this.configurationScreen.getFont().width((FormattedText)NORMAL_USER_TEXT)).narration(defaultNarrationSupplier -> CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.invited.player.narration", new Object[] { playerInfo.name }), defaultNarrationSupplier.get(), (Component)Component.translatable("narration.cycle_button.usage.focused", new Object[] { OP_TEXT }) })).build();
/*     */       
/* 144 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 151 */         .removeOpButton = (Button)SpriteIconButton.builder(OP_TEXT, button -> deop(index), false).sprite(REMOVE_OP_SPRITE, 8, 7).width(16 + RealmsPlayersTab.this.configurationScreen.getFont().width((FormattedText)OP_TEXT)).narration(defaultNarrationSupplier -> CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.invited.player.narration", new Object[] { playerInfo.name }), defaultNarrationSupplier.get(), (Component)Component.translatable("narration.cycle_button.usage.focused", new Object[] { NORMAL_USER_TEXT }) })).build();
/*     */       
/* 153 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 159 */         .removeButton = (Button)SpriteIconButton.builder(REMOVE_TEXT, button -> uninvite(index), false).sprite(REMOVE_PLAYER_SPRITE, 8, 7).width(16 + RealmsPlayersTab.this.configurationScreen.getFont().width((FormattedText)REMOVE_TEXT)).narration(defaultNarrationSupplier -> CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.invited.player.narration", new Object[] { playerInfo.name }), defaultNarrationSupplier.get() })).build();
/*     */       
/* 161 */       updateOpButtons();
/*     */     }
/*     */     
/*     */     private void op(int index) {
/* 165 */       UUID selectedInvite = ((PlayerInfo)RealmsPlayersTab.this.serverData.players.get(index)).uuid;
/* 166 */       RealmsUtil.supplyAsync(client -> selectedInvite.op(RealmsPlayersTab.this.serverData.id, selectedInvite), e -> RealmsPlayersTab.LOGGER.error("Couldn't op the user", (Throwable)e))
/*     */ 
/*     */         
/* 169 */         .thenAcceptAsync(ops -> {
/*     */             updateOps(ops);
/*     */             updateOpButtons();
/*     */             setFocused((GuiEventListener)this.removeOpButton);
/*     */           }, (Executor)RealmsPlayersTab.this.minecraft);
/*     */     }
/*     */     
/*     */     private void deop(int index) {
/* 177 */       UUID selectedInvite = ((PlayerInfo)RealmsPlayersTab.this.serverData.players.get(index)).uuid;
/* 178 */       RealmsUtil.supplyAsync(client -> selectedInvite.deop(RealmsPlayersTab.this.serverData.id, selectedInvite), e -> RealmsPlayersTab.LOGGER.error("Couldn't deop the user", (Throwable)e))
/*     */ 
/*     */         
/* 181 */         .thenAcceptAsync(ops -> {
/*     */             updateOps(ops);
/*     */             updateOpButtons();
/*     */             setFocused((GuiEventListener)this.makeOpButton);
/*     */           }, (Executor)RealmsPlayersTab.this.minecraft);
/*     */     }
/*     */     
/*     */     private void uninvite(int index) {
/* 189 */       if (index >= 0 && index < RealmsPlayersTab.this.serverData.players.size()) {
/* 190 */         PlayerInfo playerInfo = RealmsPlayersTab.this.serverData.players.get(index);
/* 191 */         RealmsConfirmScreen confirmScreen = new RealmsConfirmScreen(result -> { if (index) { RealmsUtil.runAsync((), ()); RealmsPlayersTab.this.serverData.players.remove(playerInfo); RealmsPlayersTab.this.updateData(RealmsPlayersTab.this.serverData); }  RealmsPlayersTab.this.minecraft.setScreen((Screen)RealmsPlayersTab.this.configurationScreen); }, RealmsPlayersTab.QUESTION_TITLE, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 201 */             (Component)Component.translatable("mco.configure.world.uninvite.player", new Object[] { playerInfo.name }));
/* 202 */         RealmsPlayersTab.this.minecraft.setScreen((Screen)confirmScreen);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void updateOps(Ops ops) {
/* 207 */       for (PlayerInfo playerInfo : (Iterable<PlayerInfo>)RealmsPlayersTab.this.serverData.players) {
/* 208 */         playerInfo.operator = ops.ops().contains(playerInfo.name);
/*     */       }
/*     */     }
/*     */     
/*     */     private void updateOpButtons() {
/* 213 */       this.makeOpButton.visible = !this.playerInfo.operator;
/* 214 */       this.removeOpButton.visible = !this.makeOpButton.visible;
/*     */     }
/*     */     
/*     */     private Button activeOpButton() {
/* 218 */       if (this.makeOpButton.visible) {
/* 219 */         return this.makeOpButton;
/*     */       }
/* 221 */       return this.removeOpButton;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 227 */       return (List<? extends GuiEventListener>)ImmutableList.of(activeOpButton(), this.removeButton);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 232 */       return (List<? extends NarratableEntry>)ImmutableList.of(activeOpButton(), this.removeButton);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       int inviteColor;
/* 239 */       if (!this.playerInfo.accepted) {
/* 240 */         inviteColor = -6250336;
/*     */       }
/* 242 */       else if (this.playerInfo.online) {
/* 243 */         inviteColor = -16711936;
/*     */       } else {
/* 245 */         inviteColor = -1;
/*     */       } 
/*     */       
/* 248 */       int skinYPos = getContentYMiddle() - 16;
/* 249 */       RealmsUtil.renderPlayerFace(graphics, getContentX(), skinYPos, 32, this.playerInfo.uuid);
/*     */       
/* 251 */       java.util.Objects.requireNonNull(RealmsPlayersTab.this.font); int textYPos = getContentYMiddle() - 9 / 2;
/* 252 */       graphics.drawString(RealmsPlayersTab.this.font, this.playerInfo.name, getContentX() + 8 + 32, textYPos, inviteColor);
/*     */       
/* 254 */       int iconYPos = getContentYMiddle() - 10;
/* 255 */       int removeButtonXPos = getContentRight() - this.removeButton.getWidth();
/* 256 */       this.removeButton.setPosition(removeButtonXPos, iconYPos);
/* 257 */       this.removeButton.render(graphics, mouseX, mouseY, a);
/* 258 */       int opButtonXPos = removeButtonXPos - activeOpButton().getWidth() - 8;
/* 259 */       this.makeOpButton.setPosition(opButtonXPos, iconYPos);
/* 260 */       this.makeOpButton.render(graphics, mouseX, mouseY, a);
/* 261 */       this.removeOpButton.setPosition(opButtonXPos, iconYPos);
/* 262 */       this.removeOpButton.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   private class HeaderEntry extends Entry {
/* 267 */     private String cachedNumberOfInvites = "";
/*     */     private final FocusableTextWidget invitedWidget;
/*     */     
/*     */     public HeaderEntry() {
/* 271 */       MutableComponent mutableComponent = Component.translatable("mco.configure.world.invited.number", new Object[] { "" }).withStyle(net.minecraft.ChatFormatting.UNDERLINE);
/* 272 */       this.invitedWidget = FocusableTextWidget.builder((Component)mutableComponent, RealmsPlayersTab.this.font).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 277 */       String numberOfInvites = (RealmsPlayersTab.this.serverData.players != null) ? Integer.toString(RealmsPlayersTab.this.serverData.players.size()) : "0";
/* 278 */       if (!numberOfInvites.equals(this.cachedNumberOfInvites)) {
/* 279 */         this.cachedNumberOfInvites = numberOfInvites;
/* 280 */         MutableComponent mutableComponent = Component.translatable("mco.configure.world.invited.number", new Object[] { numberOfInvites }).withStyle(net.minecraft.ChatFormatting.UNDERLINE);
/* 281 */         this.invitedWidget.setMessage((Component)mutableComponent);
/*     */       } 
/* 283 */       this.invitedWidget.setPosition(RealmsPlayersTab.this.invitedList.getRowLeft() + RealmsPlayersTab.this.invitedList.getRowWidth() / 2 - this.invitedWidget.getWidth() / 2, getY() + getHeight() / 2 - this.invitedWidget.getHeight() / 2);
/* 284 */       this.invitedWidget.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/*     */     private int height(int lineHeight) {
/* 288 */       return lineHeight + this.invitedWidget.getPadding() * 2;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 293 */       return (List)List.of(this.invitedWidget);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 298 */       return (List)List.of(this.invitedWidget);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsPlayersTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */