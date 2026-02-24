/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.commands.PublishCommand;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class ShareToLanScreen
/*     */   extends Screen {
/*     */   private static final int PORT_LOWER_BOUND = 1024;
/*     */   private static final int PORT_HIGHER_BOUND = 65535;
/*  20 */   private static final Component ALLOW_COMMANDS_LABEL = (Component)Component.translatable("selectWorld.allowCommands");
/*  21 */   private static final Component GAME_MODE_LABEL = (Component)Component.translatable("selectWorld.gameMode");
/*  22 */   private static final Component INFO_TEXT = (Component)Component.translatable("lanServer.otherPlayers");
/*  23 */   private static final Component PORT_INFO_TEXT = (Component)Component.translatable("lanServer.port");
/*  24 */   private static final Component PORT_UNAVAILABLE = (Component)Component.translatable("lanServer.port.unavailable", new Object[] { 1024, 65535 });
/*  25 */   private static final Component INVALID_PORT = (Component)Component.translatable("lanServer.port.invalid", new Object[] { 1024, 65535 });
/*     */   
/*     */   private final Screen lastScreen;
/*  28 */   private GameType gameMode = GameType.SURVIVAL;
/*     */   private boolean commands;
/*  30 */   private int port = HttpUtil.getAvailablePort();
/*     */   private EditBox portEdit;
/*     */   
/*     */   public ShareToLanScreen(Screen lastScreen) {
/*  34 */     super((Component)Component.translatable("lanServer.title"));
/*  35 */     this.lastScreen = lastScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  40 */     IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/*  41 */     this.gameMode = singleplayerServer.getDefaultGameType();
/*  42 */     this.commands = singleplayerServer.getWorldData().isAllowCommands();
/*  43 */     addRenderableWidget(CycleButton.builder(GameType::getShortDisplayName, this.gameMode)
/*  44 */         .withValues((Object[])new GameType[] { GameType.SURVIVAL, GameType.SPECTATOR, GameType.CREATIVE, GameType.ADVENTURE
/*  45 */           }).create(this.width / 2 - 155, 100, 150, 20, GAME_MODE_LABEL, (button, value) -> this.gameMode = value));
/*     */     
/*  47 */     addRenderableWidget(CycleButton.onOffBuilder(this.commands).create(this.width / 2 + 5, 100, 150, 20, ALLOW_COMMANDS_LABEL, (button, value) -> this.commands = value));
/*     */     
/*  49 */     Button startButton = Button.builder((Component)Component.translatable("lanServer.start"), button -> {
/*     */           MutableComponent mutableComponent;
/*     */           
/*     */           this.minecraft.setScreen(null);
/*     */           
/*     */           if (singleplayerServer.publishServer(this.gameMode, this.commands, this.port)) {
/*     */             mutableComponent = PublishCommand.getSuccessMessage(this.port);
/*     */           } else {
/*     */             mutableComponent = Component.translatable("commands.publish.failed");
/*     */           } 
/*     */           this.minecraft.gui.getChat().addMessage((Component)mutableComponent);
/*     */           this.minecraft.getNarrator().saySystemQueued((Component)mutableComponent);
/*     */           this.minecraft.updateTitle();
/*  62 */         }).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build();
/*     */     
/*  64 */     this.portEdit = new EditBox(this.font, this.width / 2 - 75, 160, 150, 20, (Component)Component.translatable("lanServer.port"));
/*  65 */     this.portEdit.setResponder(value -> {
/*     */           Component errorMessage = tryParsePort(startButton);
/*     */           this.portEdit.setHint((Component)Component.literal("" + this.port));
/*     */           if (errorMessage == null) {
/*     */             this.portEdit.setTextColor(-2039584);
/*     */             this.portEdit.setTooltip(null);
/*     */             startButton.active = true;
/*     */           } else {
/*     */             this.portEdit.setTextColor(-2142128);
/*     */             this.portEdit.setTooltip(Tooltip.create(errorMessage));
/*     */             startButton.active = false;
/*     */           } 
/*     */         });
/*  78 */     this.portEdit.setHint((Component)Component.literal("" + this.port));
/*  79 */     addRenderableWidget(this.portEdit);
/*  80 */     addRenderableWidget(startButton);
/*  81 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  86 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private Component tryParsePort(String value) {
/*  90 */     if (value.isBlank()) {
/*  91 */       this.port = HttpUtil.getAvailablePort();
/*  92 */       return null;
/*     */     } 
/*     */     try {
/*  95 */       this.port = Integer.parseInt(value);
/*     */       
/*  97 */       if (this.port < 1024 || this.port > 65535)
/*  98 */         return INVALID_PORT; 
/*  99 */       if (!HttpUtil.isPortAvailable(this.port)) {
/* 100 */         return PORT_UNAVAILABLE;
/*     */       }
/* 102 */       return null;
/*     */     }
/* 104 */     catch (NumberFormatException e) {
/* 105 */       this.port = HttpUtil.getAvailablePort();
/* 106 */       return INVALID_PORT;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 113 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 115 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 50, -1);
/* 116 */     graphics.drawCenteredString(this.font, INFO_TEXT, this.width / 2, 82, -1);
/* 117 */     graphics.drawCenteredString(this.font, PORT_INFO_TEXT, this.width / 2, 142, -1);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ShareToLanScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */