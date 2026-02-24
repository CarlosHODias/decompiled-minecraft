/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.WorldDownload;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.RealmsWorldSlotButton;
/*     */ import com.mojang.realmsclient.util.RealmsTextureManager;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchSlotTask;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsBrokenWorldScreen extends RealmsScreen {
/*  37 */   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DEFAULT_BUTTON_WIDTH = 80;
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private RealmsServer serverData;
/*     */   
/*     */   private final long serverId;
/*  47 */   private final Component[] message = new Component[] {
/*  48 */       (Component)Component.translatable("mco.brokenworld.message.line1"), 
/*  49 */       (Component)Component.translatable("mco.brokenworld.message.line2")
/*     */     };
/*     */   
/*     */   private int leftX;
/*     */   
/*  54 */   private final java.util.List<Integer> slotsThatHasBeenDownloaded = Lists.newArrayList();
/*     */   
/*     */   private int animTick;
/*     */   
/*     */   public RealmsBrokenWorldScreen(Screen lastScreen, long serverId, boolean isMinigame) {
/*  59 */     super(isMinigame ? (Component)Component.translatable("mco.brokenworld.minigame.title") : (Component)Component.translatable("mco.brokenworld.title"));
/*  60 */     this.lastScreen = lastScreen;
/*  61 */     this.serverId = serverId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  66 */     this.leftX = this.width / 2 - 150;
/*     */     
/*  68 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).bounds((this.width - 150) / 2, row(13) - 5, 150, 20).build());
/*     */     
/*  70 */     if (this.serverData == null) {
/*  71 */       fetchServerData(this.serverId);
/*     */     } else {
/*  73 */       addButtons();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/*  79 */     return ComponentUtils.formatList((Collection)Stream.concat(Stream.of(this.title), Stream.of((Object[])this.message)).collect(Collectors.toList()), CommonComponents.SPACE);
/*     */   }
/*     */   
/*     */   private void addButtons() {
/*  83 */     for (Iterator<Map.Entry<Integer, RealmsSlot>> iterator = this.serverData.slots.entrySet().iterator(); iterator.hasNext(); ) { Button playOrDownloadButton; Map.Entry<Integer, RealmsSlot> entry = iterator.next();
/*  84 */       int slot = (Integer)entry.getKey();
/*  85 */       boolean canPlay = (slot != this.serverData.activeSlot || this.serverData.isMinigameActive());
/*     */ 
/*     */       
/*  88 */       if (canPlay) {
/*     */ 
/*     */         
/*  91 */         playOrDownloadButton = Button.builder((Component)Component.translatable("mco.brokenworld.play"), button -> this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(this.lastScreen, new LongRunningTask[] { (LongRunningTask)new SwitchSlotTask(this.serverData.id, slot, this::doSwitchOrReset) }))).bounds(getFramePositionX(slot), row(8), 80, 20).build();
/*  92 */         playOrDownloadButton.active = !((RealmsSlot)this.serverData.slots.get(slot)).options.empty;
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 100 */         playOrDownloadButton = Button.builder((Component)Component.translatable("mco.brokenworld.download"), button -> this.minecraft.setScreen((Screen)RealmsPopups.infoPopupScreen((Screen)this, (Component)Component.translatable("mco.configure.world.restore.download.question.line1"), ()))).bounds(getFramePositionX(slot), row(8), 80, 20).build();
/*     */       } 
/*     */       
/* 103 */       if (this.slotsThatHasBeenDownloaded.contains(slot)) {
/* 104 */         playOrDownloadButton.active = false;
/* 105 */         playOrDownloadButton.setMessage((Component)Component.translatable("mco.brokenworld.downloaded"));
/*     */       } 
/*     */       
/* 108 */       addRenderableWidget((GuiEventListener)playOrDownloadButton); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 114 */     this.animTick++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 119 */     super.render(graphics, xm, ym, a);
/*     */     
/* 121 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 17, -1);
/*     */     
/* 123 */     for (int i = 0; i < this.message.length; i++) {
/* 124 */       graphics.drawCenteredString(this.font, this.message[i], this.width / 2, row(-1) + 3 + i * 12, -6250336);
/*     */     }
/*     */     
/* 127 */     if (this.serverData == null) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     for (Map.Entry<Integer, RealmsSlot> entry : (Iterable<Map.Entry<Integer, RealmsSlot>>)this.serverData.slots.entrySet()) {
/* 132 */       if (((RealmsSlot)entry.getValue()).options.templateImage != null && ((RealmsSlot)entry.getValue()).options.templateId != -1L) {
/* 133 */         drawSlotFrame(graphics, getFramePositionX((Integer)entry.getKey()), row(1) + 5, xm, ym, (this.serverData.activeSlot == (Integer)entry.getKey() && !isMinigame()), ((RealmsSlot)entry.getValue()).options.getSlotName((Integer)entry.getKey()), (Integer)entry.getKey(), ((RealmsSlot)entry.getValue()).options.templateId, ((RealmsSlot)entry.getValue()).options.templateImage, ((RealmsSlot)entry.getValue()).options.empty); continue;
/*     */       } 
/* 135 */       drawSlotFrame(graphics, getFramePositionX((Integer)entry.getKey()), row(1) + 5, xm, ym, (this.serverData.activeSlot == (Integer)entry.getKey() && !isMinigame()), ((RealmsSlot)entry.getValue()).options.getSlotName((Integer)entry.getKey()), (Integer)entry.getKey(), -1L, null, ((RealmsSlot)entry.getValue()).options.empty);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int getFramePositionX(int i) {
/* 141 */     return this.leftX + (i - 1) * 110;
/*     */   }
/*     */   
/*     */   public Screen createErrorScreen(RealmsServiceException exception) {
/* 145 */     return (Screen)new RealmsGenericErrorScreen(exception, this.lastScreen);
/*     */   }
/*     */   
/*     */   private void fetchServerData(long realmId) {
/* 149 */     RealmsUtil.supplyAsync(client -> client.getOwnRealm(realmId), 
/*     */         
/* 151 */         RealmsUtil.openScreenAndLogOnFailure(this::createErrorScreen, "Couldn't get own world"))
/* 152 */       .thenAcceptAsync(serverData -> {
/*     */           this.serverData = serverData;
/*     */           addButtons();
/*     */         }, (Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   public void doSwitchOrReset() {
/* 159 */     new Thread(() -> {
/*     */           RealmsClient client = RealmsClient.getOrCreate();
/*     */           
/*     */           if (this.serverData.state == RealmsServer.State.CLOSED) {
/*     */             this.minecraft.execute(());
/*     */           } else {
/*     */             try {
/*     */               RealmsServer ownRealm = client.getOwnRealm(this.serverId);
/*     */               this.minecraft.execute(());
/* 168 */             } catch (RealmsServiceException e) {
/*     */               LOGGER.error("Couldn't get own world", (Throwable)e);
/*     */               this.minecraft.execute(());
/*     */             } 
/*     */           } 
/* 173 */         }).start();
/*     */   }
/*     */   
/*     */   private void downloadWorld(int slotId) {
/* 177 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     
/*     */     try {
/* 180 */       WorldDownload worldDownload = client.requestDownloadInfo(this.serverData.id, slotId);
/* 181 */       RealmsDownloadLatestWorldScreen downloadScreen = new RealmsDownloadLatestWorldScreen((Screen)this, worldDownload, this.serverData.getWorldName(slotId), result -> {
/*     */             if (slotId) {
/*     */               this.slotsThatHasBeenDownloaded.add(slotId);
/*     */               
/*     */               clearWidgets();
/*     */               addButtons();
/*     */             } else {
/*     */               this.minecraft.setScreen((Screen)this);
/*     */             } 
/*     */           });
/* 191 */       this.minecraft.setScreen((Screen)downloadScreen);
/* 192 */     } catch (RealmsServiceException e) {
/* 193 */       LOGGER.error("Couldn't download world data", (Throwable)e);
/* 194 */       this.minecraft.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)this));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 200 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private boolean isMinigame() {
/* 204 */     return (this.serverData != null && this.serverData.isMinigameActive());
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawSlotFrame(GuiGraphics graphics, int x, int y, int xm, int ym, boolean active, String text, int i, long imageId, String image, boolean empty) {
/*     */     Identifier texture;
/* 210 */     if (empty) {
/* 211 */       texture = RealmsWorldSlotButton.EMPTY_SLOT_LOCATION;
/* 212 */     } else if (image != null && imageId != -1L) {
/* 213 */       texture = RealmsTextureManager.worldTemplate(String.valueOf(imageId), image);
/* 214 */     } else if (i == 1) {
/* 215 */       texture = RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_1;
/* 216 */     } else if (i == 2) {
/* 217 */       texture = RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_2;
/* 218 */     } else if (i == 3) {
/* 219 */       texture = RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_3;
/*     */     } else {
/* 221 */       texture = RealmsTextureManager.worldTemplate(String.valueOf(this.serverData.minigameId), this.serverData.minigameImage);
/*     */     } 
/*     */     
/* 224 */     if (active) {
/* 225 */       float c = 0.9F + 0.1F * Mth.cos((this.animTick * 0.2F));
/* 226 */       graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + 3, y + 3, 0.0F, 0.0F, 74, 74, 74, 74, 74, 74, ARGB.colorFromFloat(1.0F, c, c, c));
/* 227 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, 80, 80);
/*     */     } else {
/* 229 */       int color = ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F);
/* 230 */       graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + 3, y + 3, 0.0F, 0.0F, 74, 74, 74, 74, 74, 74, color);
/* 231 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, 80, 80, color);
/*     */     } 
/*     */     
/* 234 */     graphics.drawCenteredString(this.font, text, x + 40, y + 66, -1);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsBrokenWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */