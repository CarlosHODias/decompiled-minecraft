/*     */ package net.minecraft.client.gui.screens.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.SelectableEntry;
/*     */ import net.minecraft.client.gui.screens.FaviconTexture;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.server.LanServer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerSelectionList
/*     */   extends ObjectSelectionList<ServerSelectionList.Entry> {
/*  42 */   private static final Identifier INCOMPATIBLE_SPRITE = Identifier.withDefaultNamespace("server_list/incompatible");
/*  43 */   private static final Identifier UNREACHABLE_SPRITE = Identifier.withDefaultNamespace("server_list/unreachable");
/*  44 */   private static final Identifier PING_1_SPRITE = Identifier.withDefaultNamespace("server_list/ping_1");
/*  45 */   private static final Identifier PING_2_SPRITE = Identifier.withDefaultNamespace("server_list/ping_2");
/*  46 */   private static final Identifier PING_3_SPRITE = Identifier.withDefaultNamespace("server_list/ping_3");
/*  47 */   private static final Identifier PING_4_SPRITE = Identifier.withDefaultNamespace("server_list/ping_4");
/*  48 */   private static final Identifier PING_5_SPRITE = Identifier.withDefaultNamespace("server_list/ping_5");
/*  49 */   private static final Identifier PINGING_1_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_1");
/*  50 */   private static final Identifier PINGING_2_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_2");
/*  51 */   private static final Identifier PINGING_3_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_3");
/*  52 */   private static final Identifier PINGING_4_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_4");
/*  53 */   private static final Identifier PINGING_5_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_5");
/*  54 */   private static final Identifier JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("server_list/join_highlighted");
/*  55 */   private static final Identifier JOIN_SPRITE = Identifier.withDefaultNamespace("server_list/join");
/*  56 */   private static final Identifier MOVE_UP_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("server_list/move_up_highlighted");
/*  57 */   private static final Identifier MOVE_UP_SPRITE = Identifier.withDefaultNamespace("server_list/move_up");
/*  58 */   private static final Identifier MOVE_DOWN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("server_list/move_down_highlighted");
/*  59 */   private static final Identifier MOVE_DOWN_SPRITE = Identifier.withDefaultNamespace("server_list/move_down");
/*  60 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  61 */   private static final ThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER)).build());
/*     */   
/*  63 */   private static final Component SCANNING_LABEL = (Component)Component.translatable("lanServer.scanning");
/*  64 */   private static final Component CANT_RESOLVE_TEXT = (Component)Component.translatable("multiplayer.status.cannot_resolve").withColor(-65536);
/*  65 */   private static final Component CANT_CONNECT_TEXT = (Component)Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
/*  66 */   private static final Component INCOMPATIBLE_STATUS = (Component)Component.translatable("multiplayer.status.incompatible");
/*  67 */   private static final Component NO_CONNECTION_STATUS = (Component)Component.translatable("multiplayer.status.no_connection");
/*  68 */   private static final Component PINGING_STATUS = (Component)Component.translatable("multiplayer.status.pinging");
/*  69 */   private static final Component ONLINE_STATUS = (Component)Component.translatable("multiplayer.status.online");
/*     */   
/*     */   private final JoinMultiplayerScreen screen;
/*  72 */   private final List<OnlineServerEntry> onlineServers = Lists.newArrayList();
/*  73 */   private final Entry lanHeader = new LANHeader();
/*  74 */   private final List<NetworkServerEntry> networkServers = Lists.newArrayList();
/*     */   
/*     */   public ServerSelectionList(JoinMultiplayerScreen screen, Minecraft minecraft, int width, int height, int y, int itemHeight) {
/*  77 */     super(minecraft, width, height, y, itemHeight);
/*  78 */     this.screen = screen;
/*     */   }
/*     */   
/*     */   private void refreshEntries() {
/*  82 */     Entry previouslySelected = (Entry)getSelected();
/*  83 */     List<Entry> entriesToAdd = new ArrayList<>((Collection)this.onlineServers);
/*  84 */     entriesToAdd.add(this.lanHeader);
/*  85 */     entriesToAdd.addAll((Collection)this.networkServers);
/*  86 */     replaceEntries(entriesToAdd);
/*  87 */     if (previouslySelected != null) {
/*  88 */       for (Entry entry : entriesToAdd) {
/*  89 */         if (entry.matches(previouslySelected)) {
/*  90 */           setSelected(entry);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelected(Entry selected) {
/*  99 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 100 */     this.screen.onSelectedChange();
/*     */   }
/*     */   
/*     */   public void updateOnlineServers(ServerList servers) {
/* 104 */     this.onlineServers.clear();
/*     */     
/* 106 */     for (int i = 0; i < servers.size(); i++) {
/* 107 */       this.onlineServers.add(new OnlineServerEntry(this.screen, servers.get(i)));
/*     */     }
/*     */     
/* 110 */     refreshEntries();
/*     */   }
/*     */   
/*     */   public void updateNetworkServers(List<LanServer> servers) {
/* 114 */     int newServerCount = servers.size() - this.networkServers.size();
/* 115 */     this.networkServers.clear();
/*     */     
/* 117 */     for (LanServer server : servers) {
/* 118 */       this.networkServers.add(new NetworkServerEntry(this.screen, server));
/*     */     }
/* 120 */     refreshEntries();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     for (int i = this.networkServers.size() - newServerCount; i < this.networkServers.size(); i++) {
/* 126 */       NetworkServerEntry newServer = this.networkServers.get(i);
/* 127 */       int entryIndex = i - this.networkServers.size() + children().size();
/* 128 */       int rowTop = getRowTop(entryIndex);
/* 129 */       int rowBottom = getRowBottom(entryIndex);
/* 130 */       if (rowBottom >= getY() && rowTop <= getBottom()) {
/* 131 */         this.minecraft.getNarrator().saySystemQueued((Component)Component.translatable("multiplayer.lan.server_found", new Object[] { newServer.getServerNarration() }));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRowWidth() {
/* 138 */     return 305;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {}
/*     */ 
/*     */   
/*     */   public static abstract class Entry
/*     */     extends ObjectSelectionList.Entry<Entry>
/*     */     implements AutoCloseable
/*     */   {
/*     */     public void close() {}
/*     */     
/*     */     abstract boolean matches(Entry param1Entry);
/*     */     
/*     */     public abstract void join();
/*     */   }
/*     */   
/*     */   public static class LANHeader
/*     */     extends Entry
/*     */   {
/* 159 */     private final Minecraft minecraft = Minecraft.getInstance();
/* 160 */     private final LoadingDotsWidget loadingDotsWidget = new LoadingDotsWidget(this.minecraft.font, ServerSelectionList.SCANNING_LABEL);
/*     */ 
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 165 */       this.loadingDotsWidget.setPosition(getContentXMiddle() - this.minecraft.font.width((FormattedText)ServerSelectionList.SCANNING_LABEL) / 2, getContentY());
/* 166 */       this.loadingDotsWidget.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 171 */       return ServerSelectionList.SCANNING_LABEL;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean matches(ServerSelectionList.Entry other) {
/* 176 */       return other instanceof LANHeader;
/*     */     }
/*     */     
/*     */     public void join() {}
/*     */   }
/*     */   
/*     */   public static class NetworkServerEntry
/*     */     extends Entry
/*     */   {
/*     */     private static final int ICON_WIDTH = 32;
/* 186 */     private static final Component LAN_SERVER_HEADER = (Component)Component.translatable("lanServer.title");
/* 187 */     private static final Component HIDDEN_ADDRESS_TEXT = (Component)Component.translatable("selectServer.hiddenAddress");
/*     */     
/*     */     private final JoinMultiplayerScreen screen;
/*     */     protected final Minecraft minecraft;
/*     */     protected final LanServer serverData;
/*     */     
/*     */     protected NetworkServerEntry(JoinMultiplayerScreen screen, LanServer serverData) {
/* 194 */       this.screen = screen;
/* 195 */       this.serverData = serverData;
/* 196 */       this.minecraft = Minecraft.getInstance();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 201 */       graphics.drawString(this.minecraft.font, LAN_SERVER_HEADER, getContentX() + 32 + 3, getContentY() + 1, -1);
/* 202 */       graphics.drawString(this.minecraft.font, this.serverData.getMotd(), getContentX() + 32 + 3, getContentY() + 12, -8355712);
/*     */       
/* 204 */       if (this.minecraft.options.hideServerAddress) {
/* 205 */         graphics.drawString(this.minecraft.font, HIDDEN_ADDRESS_TEXT, getContentX() + 32 + 3, getContentY() + 12 + 11, -8355712);
/*     */       } else {
/* 207 */         graphics.drawString(this.minecraft.font, this.serverData.getAddress(), getContentX() + 32 + 3, getContentY() + 12 + 11, -8355712);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 213 */       if (doubleClick) {
/* 214 */         join();
/*     */       }
/* 216 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 221 */       if (event.isSelection()) {
/* 222 */         join();
/* 223 */         return true;
/*     */       } 
/* 225 */       return super.keyPressed(event);
/*     */     }
/*     */ 
/*     */     
/*     */     public void join() {
/* 230 */       this.screen.join(new ServerData(this.serverData.getMotd(), this.serverData.getAddress(), ServerData.Type.LAN));
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 235 */       return (Component)Component.translatable("narrator.select", new Object[] { getServerNarration() });
/*     */     }
/*     */     
/*     */     public Component getServerNarration() {
/* 239 */       return (Component)Component.empty().append(LAN_SERVER_HEADER).append(CommonComponents.SPACE).append(this.serverData.getMotd());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean matches(ServerSelectionList.Entry other) {
/* 244 */       if (other instanceof NetworkServerEntry) { NetworkServerEntry networkServerEntry = (NetworkServerEntry)other; if (networkServerEntry.serverData == this.serverData); }  return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public class OnlineServerEntry
/*     */     extends Entry implements SelectableEntry {
/*     */     private static final int ICON_SIZE = 32;
/*     */     private static final int SPACING = 5;
/*     */     private static final int STATUS_ICON_WIDTH = 10;
/*     */     private static final int STATUS_ICON_HEIGHT = 8;
/*     */     private final JoinMultiplayerScreen screen;
/*     */     private final Minecraft minecraft;
/*     */     private final ServerData serverData;
/*     */     private final FaviconTexture icon;
/*     */     private byte[] lastIconBytes;
/*     */     private List<Component> onlinePlayersTooltip;
/*     */     private Identifier statusIcon;
/*     */     private Component statusIconTooltip;
/*     */     
/*     */     protected OnlineServerEntry(JoinMultiplayerScreen screen, ServerData serverData) {
/* 264 */       this.screen = screen;
/* 265 */       this.serverData = serverData;
/* 266 */       this.minecraft = Minecraft.getInstance();
/* 267 */       this.icon = FaviconTexture.forServer(this.minecraft.getTextureManager(), serverData.ip);
/* 268 */       refreshStatus();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 273 */       if (this.serverData.state() == ServerData.State.INITIAL) {
/* 274 */         this.serverData.setState(ServerData.State.PINGING);
/* 275 */         this.serverData.motd = CommonComponents.EMPTY;
/* 276 */         this.serverData.status = CommonComponents.EMPTY;
/*     */         
/* 278 */         ServerSelectionList.THREAD_POOL.submit(() -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/*     */                 this.screen.getPinger().pingServer(this.serverData, (), (), EventLoopGroupHolder.remote(this.minecraft.options.useNativeTransport()));
/* 288 */               } catch (UnknownHostException ignored) {
/*     */                 this.serverData.setState(ServerData.State.UNREACHABLE);
/*     */                 this.serverData.motd = ServerSelectionList.CANT_RESOLVE_TEXT;
/*     */                 this.minecraft.execute(this::refreshStatus);
/* 292 */               } catch (Exception ignored) {
/*     */                 this.serverData.setState(ServerData.State.UNREACHABLE);
/*     */                 this.serverData.motd = ServerSelectionList.CANT_CONNECT_TEXT;
/*     */                 this.minecraft.execute(this::refreshStatus);
/*     */               } 
/*     */             });
/*     */       } 
/* 299 */       graphics.drawString(this.minecraft.font, this.serverData.name, getContentX() + 32 + 3, getContentY() + 1, -1);
/*     */       
/* 301 */       List<FormattedCharSequence> lines = this.minecraft.font.split((FormattedText)this.serverData.motd, getContentWidth() - 32 - 2);
/* 302 */       for (int i = 0; i < Math.min(lines.size(), 2); i++) {
/* 303 */         Objects.requireNonNull(this.minecraft.font); graphics.drawString(this.minecraft.font, lines.get(i), getContentX() + 32 + 3, getContentY() + 12 + 9 * i, -8355712);
/*     */       } 
/* 305 */       drawIcon(graphics, getContentX(), getContentY(), this.icon.textureLocation());
/*     */       
/* 307 */       int index = ServerSelectionList.this.children().indexOf(this);
/* 308 */       if (this.serverData.state() == ServerData.State.PINGING) {
/* 309 */         int iconIndex = (int)(Util.getMillis() / 100L + (index * 2) & 0x7L);
/* 310 */         if (iconIndex > 4) {
/* 311 */           iconIndex = 8 - iconIndex;
/*     */         }
/* 313 */         switch (iconIndex) { default: case 1: case 2: case 3: case 4: break; }  this
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 318 */           .statusIcon = ServerSelectionList.PINGING_5_SPRITE;
/*     */       } 
/*     */ 
/*     */       
/* 322 */       int statusIconX = getContentRight() - 10 - 5;
/* 323 */       if (this.statusIcon != null) {
/* 324 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.statusIcon, statusIconX, getContentY(), 10, 8);
/*     */       }
/*     */       
/* 327 */       byte[] currentIconBytes = this.serverData.getIconBytes();
/* 328 */       if (!Arrays.equals(currentIconBytes, this.lastIconBytes)) {
/* 329 */         if (uploadServerIcon(currentIconBytes)) {
/* 330 */           this.lastIconBytes = currentIconBytes;
/*     */         } else {
/* 332 */           this.serverData.setIconBytes(null);
/* 333 */           updateServerList();
/*     */         } 
/*     */       }
/*     */       
/* 337 */       Component status = (this.serverData.state() == ServerData.State.INCOMPATIBLE) ? (Component)this.serverData.version.copy().withStyle(ChatFormatting.RED) : this.serverData.status;
/* 338 */       int statusWidth = this.minecraft.font.width((FormattedText)status);
/* 339 */       int statusX = statusIconX - statusWidth - 5;
/* 340 */       graphics.drawString(this.minecraft.font, status, statusX, getContentY() + 1, -8355712);
/*     */       
/* 342 */       if (this.statusIconTooltip != null && mouseX >= statusIconX && mouseX <= statusIconX + 10 && mouseY >= getContentY() && mouseY <= getContentY() + 8)
/* 343 */       { graphics.setTooltipForNextFrame(this.statusIconTooltip, mouseX, mouseY); }
/* 344 */       else { Objects.requireNonNull(this.minecraft.font); if (this.onlinePlayersTooltip != null && mouseX >= statusX && mouseX <= statusX + statusWidth && mouseY >= getContentY() && mouseY <= getContentY() - 1 + 9) {
/* 345 */           graphics.setTooltipForNextFrame(Lists.transform(this.onlinePlayersTooltip, Component::getVisualOrderText), mouseX, mouseY);
/*     */         } }
/*     */       
/* 348 */       if ((Boolean)this.minecraft.options.touchscreen().get() || hovered) {
/* 349 */         graphics.fill(getContentX(), getContentY(), getContentX() + 32, getContentY() + 32, -1601138544);
/* 350 */         int relX = mouseX - getContentX();
/* 351 */         int relY = mouseY - getContentY();
/*     */         
/* 353 */         if (mouseOverRightHalf(relX, relY, 32)) {
/* 354 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.JOIN_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 355 */           ServerSelectionList.this.handleCursor(graphics);
/*     */         } else {
/* 357 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.JOIN_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */         } 
/* 359 */         if (index > 0) {
/* 360 */           if (mouseOverTopLeftQuarter(relX, relY, 32)) {
/* 361 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_UP_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 362 */             ServerSelectionList.this.handleCursor(graphics);
/*     */           } else {
/* 364 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_UP_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */           } 
/*     */         }
/* 367 */         if (index < this.screen.getServers().size() - 1) {
/* 368 */           if (mouseOverBottomLeftQuarter(relX, relY, 32)) {
/* 369 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_DOWN_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 370 */             ServerSelectionList.this.handleCursor(graphics);
/*     */           } else {
/* 372 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_DOWN_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void refreshStatus() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aconst_null
/*     */       //   2: putfield onlinePlayersTooltip : Ljava/util/List;
/*     */       //   5: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList$1.$SwitchMap$net$minecraft$client$multiplayer$ServerData$State : [I
/*     */       //   8: aload_0
/*     */       //   9: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   12: invokevirtual state : ()Lnet/minecraft/client/multiplayer/ServerData$State;
/*     */       //   15: invokevirtual ordinal : ()I
/*     */       //   18: iaload
/*     */       //   19: tableswitch default -> 255, 1 -> 52, 2 -> 52, 3 -> 69, 4 -> 97, 5 -> 114
/*     */       //   52: aload_0
/*     */       //   53: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PING_1_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   56: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   59: aload_0
/*     */       //   60: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PINGING_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   63: putfield statusIconTooltip : Lnet/minecraft/network/chat/Component;
/*     */       //   66: goto -> 255
/*     */       //   69: aload_0
/*     */       //   70: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.INCOMPATIBLE_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   73: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   76: aload_0
/*     */       //   77: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.INCOMPATIBLE_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   80: putfield statusIconTooltip : Lnet/minecraft/network/chat/Component;
/*     */       //   83: aload_0
/*     */       //   84: aload_0
/*     */       //   85: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   88: getfield playerList : Ljava/util/List;
/*     */       //   91: putfield onlinePlayersTooltip : Ljava/util/List;
/*     */       //   94: goto -> 255
/*     */       //   97: aload_0
/*     */       //   98: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.UNREACHABLE_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   101: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   104: aload_0
/*     */       //   105: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.NO_CONNECTION_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   108: putfield statusIconTooltip : Lnet/minecraft/network/chat/Component;
/*     */       //   111: goto -> 255
/*     */       //   114: aload_0
/*     */       //   115: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   118: getfield ping : J
/*     */       //   121: ldc2_w 150
/*     */       //   124: lcmp
/*     */       //   125: ifge -> 138
/*     */       //   128: aload_0
/*     */       //   129: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PING_5_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   132: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   135: goto -> 217
/*     */       //   138: aload_0
/*     */       //   139: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   142: getfield ping : J
/*     */       //   145: ldc2_w 300
/*     */       //   148: lcmp
/*     */       //   149: ifge -> 162
/*     */       //   152: aload_0
/*     */       //   153: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PING_4_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   156: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   159: goto -> 217
/*     */       //   162: aload_0
/*     */       //   163: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   166: getfield ping : J
/*     */       //   169: ldc2_w 600
/*     */       //   172: lcmp
/*     */       //   173: ifge -> 186
/*     */       //   176: aload_0
/*     */       //   177: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PING_3_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   180: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   183: goto -> 217
/*     */       //   186: aload_0
/*     */       //   187: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   190: getfield ping : J
/*     */       //   193: ldc2_w 1000
/*     */       //   196: lcmp
/*     */       //   197: ifge -> 210
/*     */       //   200: aload_0
/*     */       //   201: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PING_2_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   204: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   207: goto -> 217
/*     */       //   210: aload_0
/*     */       //   211: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PING_1_SPRITE : Lnet/minecraft/resources/Identifier;
/*     */       //   214: putfield statusIcon : Lnet/minecraft/resources/Identifier;
/*     */       //   217: aload_0
/*     */       //   218: ldc_w 'multiplayer.status.ping'
/*     */       //   221: iconst_1
/*     */       //   222: anewarray java/lang/Object
/*     */       //   225: dup
/*     */       //   226: iconst_0
/*     */       //   227: aload_0
/*     */       //   228: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   231: getfield ping : J
/*     */       //   234: invokestatic valueOf : (J)Ljava/lang/Long;
/*     */       //   237: aastore
/*     */       //   238: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   241: putfield statusIconTooltip : Lnet/minecraft/network/chat/Component;
/*     */       //   244: aload_0
/*     */       //   245: aload_0
/*     */       //   246: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   249: getfield playerList : Ljava/util/List;
/*     */       //   252: putfield onlinePlayersTooltip : Ljava/util/List;
/*     */       //   255: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #379	-> 0
/*     */       //   #381	-> 5
/*     */       //   #383	-> 52
/*     */       //   #384	-> 59
/*     */       //   #385	-> 66
/*     */       //   #387	-> 69
/*     */       //   #388	-> 76
/*     */       //   #389	-> 83
/*     */       //   #390	-> 94
/*     */       //   #392	-> 97
/*     */       //   #393	-> 104
/*     */       //   #394	-> 111
/*     */       //   #396	-> 114
/*     */       //   #397	-> 128
/*     */       //   #398	-> 138
/*     */       //   #399	-> 152
/*     */       //   #400	-> 162
/*     */       //   #401	-> 176
/*     */       //   #402	-> 186
/*     */       //   #403	-> 200
/*     */       //   #405	-> 210
/*     */       //   #407	-> 217
/*     */       //   #408	-> 244
/*     */       //   #411	-> 255
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	256	0	this	Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList$OnlineServerEntry;
/*     */     }
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
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateServerList() {
/* 414 */       this.screen.getServers().save();
/*     */     }
/*     */     
/*     */     protected void drawIcon(GuiGraphics graphics, int rowLeft, int rowTop, Identifier location) {
/* 418 */       graphics.blit(RenderPipelines.GUI_TEXTURED, location, rowLeft, rowTop, 0.0F, 0.0F, 32, 32, 32, 32);
/*     */     }
/*     */     
/*     */     private boolean uploadServerIcon(byte[] serverIconBytes) {
/* 422 */       if (serverIconBytes == null) {
/* 423 */         this.icon.clear();
/*     */       } else {
/*     */         try {
/* 426 */           this.icon.upload(NativeImage.read(serverIconBytes));
/* 427 */         } catch (Throwable t) {
/* 428 */           ServerSelectionList.LOGGER.error("Invalid icon for server {} ({})", new Object[] { this.serverData.name, this.serverData.ip, t });
/* 429 */           return false;
/*     */         } 
/*     */       } 
/* 432 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 437 */       if (event.isSelection()) {
/* 438 */         join();
/* 439 */         return true;
/*     */       } 
/* 441 */       if (event.hasShiftDown()) {
/*     */         
/* 443 */         ServerSelectionList list = this.screen.serverSelectionList;
/* 444 */         int currentIndex = list.children().indexOf(this);
/*     */         
/* 446 */         if (currentIndex == -1) {
/* 447 */           return true;
/*     */         }
/*     */         
/* 450 */         if ((event.isDown() && currentIndex < this.screen.getServers().size() - 1) || (
/* 451 */           event.isUp() && currentIndex > 0)) {
/*     */           
/* 453 */           swap(currentIndex, event.isDown() ? (currentIndex + 1) : (currentIndex - 1));
/* 454 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 458 */       return super.keyPressed(event);
/*     */     }
/*     */ 
/*     */     
/*     */     public void join() {
/* 463 */       this.screen.join(this.serverData);
/*     */     }
/*     */     
/*     */     private void swap(int currentIndex, int newIndex) {
/* 467 */       this.screen.getServers().swap(currentIndex, newIndex);
/* 468 */       this.screen.serverSelectionList.swap(currentIndex, newIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 473 */       int relX = (int)event.x() - getContentX();
/* 474 */       int relY = (int)event.y() - getContentY();
/*     */       
/* 476 */       if (mouseOverRightHalf(relX, relY, 32)) {
/* 477 */         join();
/* 478 */         return true;
/*     */       } 
/*     */       
/* 481 */       int currentIndex = this.screen.serverSelectionList.children().indexOf(this);
/* 482 */       if (currentIndex > 0 && mouseOverTopLeftQuarter(relX, relY, 32)) {
/* 483 */         swap(currentIndex, currentIndex - 1);
/* 484 */         return true;
/*     */       } 
/*     */       
/* 487 */       if (currentIndex < this.screen.getServers().size() - 1 && mouseOverBottomLeftQuarter(relX, relY, 32)) {
/* 488 */         swap(currentIndex, currentIndex + 1);
/* 489 */         return true;
/*     */       } 
/*     */       
/* 492 */       if (doubleClick) {
/* 493 */         join();
/*     */       }
/*     */       
/* 496 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     public ServerData getServerData() {
/* 500 */       return this.serverData;
/*     */     }
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/*     */       // Byte code:
/*     */       //   0: invokestatic empty : ()Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   3: astore_1
/*     */       //   4: aload_1
/*     */       //   5: ldc_w 'narrator.select'
/*     */       //   8: iconst_1
/*     */       //   9: anewarray java/lang/Object
/*     */       //   12: dup
/*     */       //   13: iconst_0
/*     */       //   14: aload_0
/*     */       //   15: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   18: getfield name : Ljava/lang/String;
/*     */       //   21: aastore
/*     */       //   22: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   25: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   28: pop
/*     */       //   29: aload_1
/*     */       //   30: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   33: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   36: pop
/*     */       //   37: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList$1.$SwitchMap$net$minecraft$client$multiplayer$ServerData$State : [I
/*     */       //   40: aload_0
/*     */       //   41: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   44: invokevirtual state : ()Lnet/minecraft/client/multiplayer/ServerData$State;
/*     */       //   47: invokevirtual ordinal : ()I
/*     */       //   50: iaload
/*     */       //   51: tableswitch default -> 175, 2 -> 164, 3 -> 76, 4 -> 153
/*     */       //   76: aload_1
/*     */       //   77: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.INCOMPATIBLE_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   80: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   83: pop
/*     */       //   84: aload_1
/*     */       //   85: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   88: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   91: pop
/*     */       //   92: aload_1
/*     */       //   93: ldc_w 'multiplayer.status.version.narration'
/*     */       //   96: iconst_1
/*     */       //   97: anewarray java/lang/Object
/*     */       //   100: dup
/*     */       //   101: iconst_0
/*     */       //   102: aload_0
/*     */       //   103: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   106: getfield version : Lnet/minecraft/network/chat/Component;
/*     */       //   109: aastore
/*     */       //   110: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   113: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   116: pop
/*     */       //   117: aload_1
/*     */       //   118: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   121: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   124: pop
/*     */       //   125: aload_1
/*     */       //   126: ldc_w 'multiplayer.status.motd.narration'
/*     */       //   129: iconst_1
/*     */       //   130: anewarray java/lang/Object
/*     */       //   133: dup
/*     */       //   134: iconst_0
/*     */       //   135: aload_0
/*     */       //   136: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   139: getfield motd : Lnet/minecraft/network/chat/Component;
/*     */       //   142: aastore
/*     */       //   143: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   146: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   149: pop
/*     */       //   150: goto -> 346
/*     */       //   153: aload_1
/*     */       //   154: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.NO_CONNECTION_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   157: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   160: pop
/*     */       //   161: goto -> 346
/*     */       //   164: aload_1
/*     */       //   165: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.PINGING_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   168: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   171: pop
/*     */       //   172: goto -> 346
/*     */       //   175: aload_1
/*     */       //   176: getstatic net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.ONLINE_STATUS : Lnet/minecraft/network/chat/Component;
/*     */       //   179: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   182: pop
/*     */       //   183: aload_1
/*     */       //   184: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   187: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   190: pop
/*     */       //   191: aload_1
/*     */       //   192: ldc_w 'multiplayer.status.ping.narration'
/*     */       //   195: iconst_1
/*     */       //   196: anewarray java/lang/Object
/*     */       //   199: dup
/*     */       //   200: iconst_0
/*     */       //   201: aload_0
/*     */       //   202: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   205: getfield ping : J
/*     */       //   208: invokestatic valueOf : (J)Ljava/lang/Long;
/*     */       //   211: aastore
/*     */       //   212: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   215: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   218: pop
/*     */       //   219: aload_1
/*     */       //   220: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   223: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   226: pop
/*     */       //   227: aload_1
/*     */       //   228: ldc_w 'multiplayer.status.motd.narration'
/*     */       //   231: iconst_1
/*     */       //   232: anewarray java/lang/Object
/*     */       //   235: dup
/*     */       //   236: iconst_0
/*     */       //   237: aload_0
/*     */       //   238: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   241: getfield motd : Lnet/minecraft/network/chat/Component;
/*     */       //   244: aastore
/*     */       //   245: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   248: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   251: pop
/*     */       //   252: aload_0
/*     */       //   253: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   256: getfield players : Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*     */       //   259: ifnull -> 346
/*     */       //   262: aload_1
/*     */       //   263: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   266: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   269: pop
/*     */       //   270: aload_1
/*     */       //   271: ldc_w 'multiplayer.status.player_count.narration'
/*     */       //   274: iconst_2
/*     */       //   275: anewarray java/lang/Object
/*     */       //   278: dup
/*     */       //   279: iconst_0
/*     */       //   280: aload_0
/*     */       //   281: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   284: getfield players : Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*     */       //   287: invokevirtual online : ()I
/*     */       //   290: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */       //   293: aastore
/*     */       //   294: dup
/*     */       //   295: iconst_1
/*     */       //   296: aload_0
/*     */       //   297: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   300: getfield players : Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*     */       //   303: invokevirtual max : ()I
/*     */       //   306: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */       //   309: aastore
/*     */       //   310: invokestatic translatable : (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   313: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   316: pop
/*     */       //   317: aload_1
/*     */       //   318: getstatic net/minecraft/network/chat/CommonComponents.NARRATION_SEPARATOR : Lnet/minecraft/network/chat/Component;
/*     */       //   321: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   324: pop
/*     */       //   325: aload_1
/*     */       //   326: aload_0
/*     */       //   327: getfield serverData : Lnet/minecraft/client/multiplayer/ServerData;
/*     */       //   330: getfield playerList : Ljava/util/List;
/*     */       //   333: ldc_w ', '
/*     */       //   336: invokestatic literal : (Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   339: invokestatic formatList : (Ljava/util/Collection;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/Component;
/*     */       //   342: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */       //   345: pop
/*     */       //   346: aload_1
/*     */       //   347: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #505	-> 0
/*     */       //   #506	-> 4
/*     */       //   #507	-> 29
/*     */       //   #508	-> 37
/*     */       //   #510	-> 76
/*     */       //   #511	-> 84
/*     */       //   #512	-> 92
/*     */       //   #513	-> 117
/*     */       //   #514	-> 125
/*     */       //   #515	-> 150
/*     */       //   #516	-> 153
/*     */       //   #517	-> 164
/*     */       //   #519	-> 175
/*     */       //   #520	-> 183
/*     */       //   #521	-> 191
/*     */       //   #522	-> 219
/*     */       //   #523	-> 227
/*     */       //   #524	-> 252
/*     */       //   #525	-> 262
/*     */       //   #526	-> 270
/*     */       //   #527	-> 317
/*     */       //   #528	-> 325
/*     */       //   #532	-> 346
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	348	0	this	Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList$OnlineServerEntry;
/*     */       //   4	344	1	narrationComponent	Lnet/minecraft/network/chat/MutableComponent;
/*     */     }
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
/*     */ 
/*     */     
/*     */     public void close() {
/* 537 */       this.icon.close();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean matches(ServerSelectionList.Entry other) {
/* 542 */       if (other instanceof OnlineServerEntry) { OnlineServerEntry onlineServerEntry = (OnlineServerEntry)other; if (onlineServerEntry.serverData == this.serverData); }  return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */