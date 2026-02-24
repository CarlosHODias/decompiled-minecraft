/*     */ package net.minecraft.client.gui.screens.multiplayer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.ConnectScreen;
/*     */ import net.minecraft.client.gui.screens.DirectJoinServerScreen;
/*     */ import net.minecraft.client.gui.screens.ManageServerScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.multiplayer.ServerStatusPinger;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.client.server.LanServer;
/*     */ import net.minecraft.client.server.LanServerDetection;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class JoinMultiplayerScreen extends Screen {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int TOP_ROW_BUTTON_WIDTH = 100;
/*     */   
/*     */   private static final int LOWER_ROW_BUTTON_WIDTH = 74;
/*  34 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 60);
/*     */ 
/*     */ 
/*     */   
/*  38 */   private final ServerStatusPinger pinger = new ServerStatusPinger();
/*     */   private final Screen lastScreen;
/*     */   protected ServerSelectionList serverSelectionList;
/*     */   private ServerList servers;
/*     */   private Button editButton;
/*     */   private Button selectButton;
/*     */   private Button deleteButton;
/*     */   private ServerData editingServer;
/*     */   private LanServerDetection.LanServerList lanServerList;
/*     */   private LanServerDetection.LanServerDetector lanServerDetector;
/*     */   
/*     */   public JoinMultiplayerScreen(Screen lastScreen) {
/*  50 */     super((Component)Component.translatable("multiplayer.title"));
/*  51 */     this.lastScreen = lastScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  56 */     this.layout.addTitleHeader(this.title, this.font);
/*  57 */     this.servers = new ServerList(this.minecraft);
/*  58 */     this.servers.load();
/*     */     
/*  60 */     this.lanServerList = new LanServerDetection.LanServerList();
/*     */     try {
/*  62 */       this.lanServerDetector = new LanServerDetection.LanServerDetector(this.lanServerList);
/*  63 */       this.lanServerDetector.start();
/*  64 */     } catch (Exception e) {
/*  65 */       LOGGER.warn("Unable to start LAN server detection: {}", e.getMessage());
/*     */     } 
/*     */     
/*  68 */     this.serverSelectionList = (ServerSelectionList)this.layout.addToContents((LayoutElement)new ServerSelectionList(this, this.minecraft, this.width, this.layout.getContentHeight(), this.layout.getHeaderHeight(), 36));
/*  69 */     this.serverSelectionList.updateOnlineServers(this.servers);
/*     */     
/*  71 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.vertical().spacing(4));
/*  72 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  73 */     LinearLayout topFooterButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/*  74 */     LinearLayout bottomFooterButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/*     */     
/*  76 */     this.selectButton = (Button)topFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectServer.select"), button -> {
/*     */             ServerSelectionList.Entry entry = (ServerSelectionList.Entry)this.serverSelectionList.getSelected();
/*     */             if (entry != null) {
/*     */               entry.join();
/*     */             }
/*  81 */           }).width(100).build());
/*  82 */     topFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectServer.direct"), button -> {
/*     */             this.editingServer = new ServerData(I18n.get("selectServer.defaultName", new Object[0]), "", ServerData.Type.OTHER);
/*     */             this.minecraft.setScreen((Screen)new DirectJoinServerScreen(this, this::directJoinCallback, this.editingServer));
/*  85 */           }).width(100).build());
/*  86 */     topFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectServer.add"), button -> {
/*     */             this.editingServer = new ServerData("", "", ServerData.Type.OTHER);
/*     */             this.minecraft.setScreen((Screen)new ManageServerScreen(this, (Component)Component.translatable("manageServer.add.title"), this::addServerCallback, this.editingServer));
/*  89 */           }).width(100).build());
/*     */     
/*  91 */     this.editButton = (Button)bottomFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectServer.edit"), button -> {
/*     */             ServerSelectionList.Entry entry = (ServerSelectionList.Entry)this.serverSelectionList.getSelected();
/*     */             
/*     */             if (entry instanceof ServerSelectionList.OnlineServerEntry) {
/*     */               ServerData current = ((ServerSelectionList.OnlineServerEntry)entry).getServerData();
/*     */               
/*     */               this.editingServer = new ServerData(current.name, current.ip, ServerData.Type.OTHER);
/*     */               this.editingServer.copyFrom(current);
/*     */               this.minecraft.setScreen((Screen)new ManageServerScreen(this, (Component)Component.translatable("manageServer.edit.title"), this::editServerCallback, this.editingServer));
/*     */             } 
/* 101 */           }).width(74).build());
/* 102 */     this.deleteButton = (Button)bottomFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectServer.delete"), button -> {
/*     */             ServerSelectionList.Entry entry = (ServerSelectionList.Entry)this.serverSelectionList.getSelected();
/*     */             
/*     */             if (entry instanceof ServerSelectionList.OnlineServerEntry) {
/*     */               String serverName = (((ServerSelectionList.OnlineServerEntry)entry).getServerData()).name;
/*     */               
/*     */               if (serverName != null) {
/*     */                 MutableComponent mutableComponent1 = Component.translatable("selectServer.deleteQuestion"), mutableComponent2 = Component.translatable("selectServer.deleteWarning", new Object[] { serverName }), mutableComponent3 = Component.translatable("selectServer.deleteButton");
/*     */                 
/*     */                 Component no = CommonComponents.GUI_CANCEL;
/*     */                 this.minecraft.setScreen((Screen)new ConfirmScreen(this::deleteCallback, (Component)mutableComponent1, (Component)mutableComponent2, (Component)mutableComponent3, no));
/*     */               } 
/*     */             } 
/* 115 */           }).width(74).build());
/* 116 */     bottomFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectServer.refresh"), button -> refreshServerList()).width(74).build());
/* 117 */     bottomFooterButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(74).build());
/*     */     
/* 119 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 120 */     repositionElements();
/* 121 */     onSelectedChange();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 126 */     this.layout.arrangeElements();
/* 127 */     if (this.serverSelectionList != null) {
/* 128 */       this.serverSelectionList.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 134 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 139 */     super.tick();
/*     */     
/* 141 */     List<LanServer> lanServers = this.lanServerList.takeDirtyServers();
/* 142 */     if (lanServers != null) {
/* 143 */       this.serverSelectionList.updateNetworkServers(lanServers);
/*     */     }
/*     */     
/* 146 */     this.pinger.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 151 */     if (this.lanServerDetector != null) {
/* 152 */       this.lanServerDetector.interrupt();
/* 153 */       this.lanServerDetector = null;
/*     */     } 
/* 155 */     this.pinger.removeAll();
/* 156 */     this.serverSelectionList.removed();
/*     */   }
/*     */   
/*     */   private void refreshServerList() {
/* 160 */     this.minecraft.setScreen(new JoinMultiplayerScreen(this.lastScreen));
/*     */   }
/*     */   
/*     */   private void deleteCallback(boolean result) {
/* 164 */     ServerSelectionList.Entry entry = (ServerSelectionList.Entry)this.serverSelectionList.getSelected();
/* 165 */     if (result && entry instanceof ServerSelectionList.OnlineServerEntry) {
/* 166 */       this.servers.remove(((ServerSelectionList.OnlineServerEntry)entry).getServerData());
/* 167 */       this.servers.save();
/* 168 */       this.serverSelectionList.setSelected((ServerSelectionList.Entry)null);
/* 169 */       this.serverSelectionList.updateOnlineServers(this.servers);
/*     */     } 
/* 171 */     this.minecraft.setScreen(this);
/*     */   }
/*     */   
/*     */   private void editServerCallback(boolean result) {
/* 175 */     ServerSelectionList.Entry entry = (ServerSelectionList.Entry)this.serverSelectionList.getSelected();
/* 176 */     if (result && entry instanceof ServerSelectionList.OnlineServerEntry) {
/* 177 */       ServerData current = ((ServerSelectionList.OnlineServerEntry)entry).getServerData();
/* 178 */       current.name = this.editingServer.name;
/* 179 */       current.ip = this.editingServer.ip;
/* 180 */       current.copyFrom(this.editingServer);
/* 181 */       this.servers.save();
/* 182 */       this.serverSelectionList.updateOnlineServers(this.servers);
/*     */     } 
/* 184 */     this.minecraft.setScreen(this);
/*     */   }
/*     */   
/*     */   private void addServerCallback(boolean result) {
/* 188 */     if (result) {
/* 189 */       ServerData serverData = this.servers.unhide(this.editingServer.ip);
/* 190 */       if (serverData != null) {
/* 191 */         serverData.copyNameIconFrom(this.editingServer);
/* 192 */         this.servers.save();
/*     */       } else {
/* 194 */         this.servers.add(this.editingServer, false);
/* 195 */         this.servers.save();
/*     */       } 
/* 197 */       this.serverSelectionList.setSelected((ServerSelectionList.Entry)null);
/* 198 */       this.serverSelectionList.updateOnlineServers(this.servers);
/*     */     } 
/* 200 */     this.minecraft.setScreen(this);
/*     */   }
/*     */   
/*     */   private void directJoinCallback(boolean result) {
/* 204 */     if (result) {
/* 205 */       ServerData serverData = this.servers.get(this.editingServer.ip);
/* 206 */       if (serverData == null) {
/* 207 */         this.servers.add(this.editingServer, true);
/* 208 */         this.servers.save();
/* 209 */         join(this.editingServer);
/*     */       } else {
/* 211 */         join(serverData);
/*     */       } 
/*     */     } else {
/* 214 */       this.minecraft.setScreen(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 220 */     if (super.keyPressed(event)) {
/* 221 */       return true;
/*     */     }
/* 223 */     if (event.key() == 294) {
/* 224 */       refreshServerList();
/* 225 */       return true;
/*     */     } 
/* 227 */     return false;
/*     */   }
/*     */   
/*     */   public void join(ServerData data) {
/* 231 */     ConnectScreen.startConnecting(this, this.minecraft, ServerAddress.parseString(data.ip), data, false, null);
/*     */   }
/*     */   
/*     */   protected void onSelectedChange() {
/* 235 */     this.selectButton.active = false;
/* 236 */     this.editButton.active = false;
/* 237 */     this.deleteButton.active = false;
/*     */     
/* 239 */     ServerSelectionList.Entry entry = (ServerSelectionList.Entry)this.serverSelectionList.getSelected();
/* 240 */     if (entry != null && !(entry instanceof ServerSelectionList.LANHeader)) {
/* 241 */       this.selectButton.active = true;
/* 242 */       if (entry instanceof ServerSelectionList.OnlineServerEntry) {
/* 243 */         this.editButton.active = true;
/* 244 */         this.deleteButton.active = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ServerStatusPinger getPinger() {
/* 250 */     return this.pinger;
/*     */   }
/*     */   
/*     */   public ServerList getServers() {
/* 254 */     return this.servers;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */