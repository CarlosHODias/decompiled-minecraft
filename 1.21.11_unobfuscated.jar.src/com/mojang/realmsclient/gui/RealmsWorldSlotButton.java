/*     */ package com.mojang.realmsclient.gui;
/*     */ 
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.util.RealmsTextureManager;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class RealmsWorldSlotButton
/*     */   extends Button
/*     */ {
/*  22 */   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
/*  23 */   public static final Identifier EMPTY_SLOT_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/empty_frame.png");
/*  24 */   public static final Identifier DEFAULT_WORLD_SLOT_1 = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_0.png");
/*  25 */   public static final Identifier DEFAULT_WORLD_SLOT_2 = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_2.png");
/*  26 */   public static final Identifier DEFAULT_WORLD_SLOT_3 = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_3.png");
/*  27 */   private static final Component SWITCH_TO_MINIGAME_SLOT_TOOLTIP = (Component)Component.translatable("mco.configure.world.slot.tooltip.minigame");
/*  28 */   private static final Component SWITCH_TO_WORLD_SLOT_TOOLTIP = (Component)Component.translatable("mco.configure.world.slot.tooltip");
/*  29 */   private static final Component MINIGAME = (Component)Component.translatable("mco.worldSlot.minigame");
/*     */   
/*     */   private static final int WORLD_NAME_MAX_WIDTH = 64;
/*     */   
/*     */   private static final String DOTS = "...";
/*     */   
/*     */   private final int slotIndex;
/*     */   private State state;
/*     */   
/*     */   public RealmsWorldSlotButton(int x, int y, int width, int height, int slotIndex, RealmsServer serverData, Button.OnPress onPress) {
/*  39 */     super(x, y, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
/*  40 */     this.slotIndex = slotIndex;
/*  41 */     this.state = setServerData(serverData);
/*     */   }
/*     */   
/*     */   public State getState() {
/*  45 */     return this.state;
/*     */   }
/*     */   
/*     */   public enum Action {
/*  49 */     NOTHING,
/*  50 */     SWITCH_SLOT;
/*     */   }
/*     */   
/*     */   public static class State {
/*     */     private final String slotName;
/*     */     private final String slotVersion;
/*     */     private final RealmsServer.Compatibility compatibility;
/*     */     private final long imageId;
/*     */     private final String image;
/*     */     public final boolean empty;
/*     */     public final boolean minigame;
/*     */     public final RealmsWorldSlotButton.Action action;
/*     */     public final boolean hardcore;
/*     */     public final boolean activeSlot;
/*     */     
/*     */     public State(RealmsServer serverData, int slotIndex) {
/*  66 */       this.minigame = (slotIndex == 4);
/*  67 */       if (this.minigame) {
/*  68 */         this.slotName = RealmsWorldSlotButton.MINIGAME.getString();
/*  69 */         this.imageId = serverData.minigameId;
/*  70 */         this.image = serverData.minigameImage;
/*  71 */         this.empty = (serverData.minigameId == -1);
/*  72 */         this.slotVersion = "";
/*  73 */         this.compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
/*  74 */         this.hardcore = false;
/*  75 */         this.activeSlot = serverData.isMinigameActive();
/*     */       } else {
/*  77 */         RealmsSlot slot = (RealmsSlot)serverData.slots.get(slotIndex);
/*  78 */         this.slotName = slot.options.getSlotName(slotIndex);
/*  79 */         this.imageId = slot.options.templateId;
/*  80 */         this.image = slot.options.templateImage;
/*  81 */         this.empty = slot.options.empty;
/*  82 */         this.slotVersion = slot.options.version;
/*  83 */         this.compatibility = slot.options.compatibility;
/*  84 */         this.hardcore = slot.isHardcore();
/*  85 */         this.activeSlot = (serverData.activeSlot == slotIndex && !serverData.isMinigameActive());
/*     */       } 
/*  87 */       this.action = RealmsWorldSlotButton.getAction(this.activeSlot, this.empty, serverData.expired);
/*     */     }
/*     */   }
/*     */   
/*     */   public State setServerData(RealmsServer serverData) {
/*  92 */     this.state = new State(serverData, this.slotIndex);
/*  93 */     setTooltipAndNarration(this.state, serverData.minigameName);
/*  94 */     return this.state;
/*     */   }
/*     */   
/*     */   private void setTooltipAndNarration(State state, String minigameName) {
/*  98 */     switch (state.action.ordinal()) { case 1:
/*  99 */         if (state.minigame);
/* 100 */       default: break; }  Component tooltipComponent = null;
/*     */ 
/*     */     
/* 103 */     if (tooltipComponent != null) {
/* 104 */       setTooltip(Tooltip.create(tooltipComponent));
/*     */     }
/*     */     
/* 107 */     MutableComponent slotContents = Component.literal(state.slotName);
/* 108 */     if (state.minigame && minigameName != null) {
/* 109 */       slotContents = slotContents.append(CommonComponents.SPACE).append(minigameName);
/*     */     }
/*     */     
/* 112 */     setMessage((Component)slotContents);
/*     */   }
/*     */   
/*     */   private static Action getAction(boolean activeSlot, boolean empty, boolean expired) {
/* 116 */     if (!activeSlot && (!empty || !expired)) {
/* 117 */       return Action.SWITCH_SLOT;
/*     */     }
/* 119 */     return Action.NOTHING;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 124 */     return (this.state.action != Action.NOTHING && super.isActive());
/*     */   }
/*     */   
/*     */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */     Identifier texture;
/* 129 */     int x = getX();
/* 130 */     int y = getY();
/* 131 */     boolean hoveredOrFocused = isHoveredOrFocused();
/*     */ 
/*     */ 
/*     */     
/* 135 */     if (this.state.minigame) {
/* 136 */       texture = RealmsTextureManager.worldTemplate(String.valueOf(this.state.imageId), this.state.image);
/* 137 */     } else if (this.state.empty) {
/* 138 */       texture = EMPTY_SLOT_LOCATION;
/* 139 */     } else if (this.state.image != null && this.state.imageId != -1L) {
/* 140 */       texture = RealmsTextureManager.worldTemplate(String.valueOf(this.state.imageId), this.state.image);
/* 141 */     } else if (this.slotIndex == 1) {
/* 142 */       texture = DEFAULT_WORLD_SLOT_1;
/* 143 */     } else if (this.slotIndex == 2) {
/* 144 */       texture = DEFAULT_WORLD_SLOT_2;
/* 145 */     } else if (this.slotIndex == 3) {
/* 146 */       texture = DEFAULT_WORLD_SLOT_3;
/*     */     } else {
/* 148 */       texture = EMPTY_SLOT_LOCATION;
/*     */     } 
/*     */     
/* 151 */     int color = -1;
/* 152 */     if (!this.state.activeSlot) {
/* 153 */       color = ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F);
/*     */     }
/*     */     
/* 156 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + 1, y + 1, 0.0F, 0.0F, this.width - 2, this.height - 2, 74, 74, 74, 74, color);
/*     */     
/* 158 */     if (hoveredOrFocused && this.state.action != Action.NOTHING) {
/* 159 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, this.width, this.height);
/* 160 */     } else if (this.state.activeSlot) {
/* 161 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, this.width, this.height, ARGB.colorFromFloat(1.0F, 0.8F, 0.8F, 0.8F));
/*     */     } else {
/* 163 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, this.width, this.height, ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F));
/*     */     } 
/*     */     
/* 166 */     if (this.state.hardcore) {
/* 167 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.HARDCORE_MODE_SPRITE, x + 3, y + 4, 9, 8);
/*     */     }
/*     */     
/* 170 */     Font font = (Minecraft.getInstance()).font;
/* 171 */     String slotName = this.state.slotName;
/* 172 */     if (font.width(slotName) > 64) {
/* 173 */       slotName = font.plainSubstrByWidth(slotName, 64 - font.width("...")) + "...";
/*     */     }
/* 175 */     graphics.drawCenteredString(font, slotName, x + this.width / 2, y + this.height - 14, -1);
/*     */     
/* 177 */     if (this.state.activeSlot)
/* 178 */       graphics.drawCenteredString(font, RealmsMainScreen.getVersionComponent(this.state.slotVersion, this.state.compatibility.isCompatible()), x + this.width / 2, y + this.height + 2, -1); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/RealmsWorldSlotButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */