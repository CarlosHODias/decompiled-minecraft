/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class BossHealthOverlay
/*     */ {
/*     */   private static final int BAR_WIDTH = 182;
/*     */   private static final int BAR_HEIGHT = 5;
/*  23 */   private static final Identifier[] BAR_BACKGROUND_SPRITES = new Identifier[] {
/*  24 */       Identifier.withDefaultNamespace("boss_bar/pink_background"), 
/*  25 */       Identifier.withDefaultNamespace("boss_bar/blue_background"), 
/*  26 */       Identifier.withDefaultNamespace("boss_bar/red_background"), 
/*  27 */       Identifier.withDefaultNamespace("boss_bar/green_background"), 
/*  28 */       Identifier.withDefaultNamespace("boss_bar/yellow_background"), 
/*  29 */       Identifier.withDefaultNamespace("boss_bar/purple_background"), 
/*  30 */       Identifier.withDefaultNamespace("boss_bar/white_background")
/*     */     };
/*     */   
/*  33 */   private static final Identifier[] BAR_PROGRESS_SPRITES = new Identifier[] {
/*  34 */       Identifier.withDefaultNamespace("boss_bar/pink_progress"), 
/*  35 */       Identifier.withDefaultNamespace("boss_bar/blue_progress"), 
/*  36 */       Identifier.withDefaultNamespace("boss_bar/red_progress"), 
/*  37 */       Identifier.withDefaultNamespace("boss_bar/green_progress"), 
/*  38 */       Identifier.withDefaultNamespace("boss_bar/yellow_progress"), 
/*  39 */       Identifier.withDefaultNamespace("boss_bar/purple_progress"), 
/*  40 */       Identifier.withDefaultNamespace("boss_bar/white_progress")
/*     */     };
/*     */   
/*  43 */   private static final Identifier[] OVERLAY_BACKGROUND_SPRITES = new Identifier[] {
/*  44 */       Identifier.withDefaultNamespace("boss_bar/notched_6_background"), 
/*  45 */       Identifier.withDefaultNamespace("boss_bar/notched_10_background"), 
/*  46 */       Identifier.withDefaultNamespace("boss_bar/notched_12_background"), 
/*  47 */       Identifier.withDefaultNamespace("boss_bar/notched_20_background")
/*     */     };
/*     */   
/*  50 */   private static final Identifier[] OVERLAY_PROGRESS_SPRITES = new Identifier[] {
/*  51 */       Identifier.withDefaultNamespace("boss_bar/notched_6_progress"), 
/*  52 */       Identifier.withDefaultNamespace("boss_bar/notched_10_progress"), 
/*  53 */       Identifier.withDefaultNamespace("boss_bar/notched_12_progress"), 
/*  54 */       Identifier.withDefaultNamespace("boss_bar/notched_20_progress")
/*     */     };
/*     */   
/*     */   private final Minecraft minecraft;
/*  58 */   private final Map<UUID, LerpingBossEvent> events = Maps.newLinkedHashMap();
/*     */   
/*     */   public BossHealthOverlay(Minecraft minecraft) {
/*  61 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics) {
/*  65 */     if (this.events.isEmpty()) {
/*     */       return;
/*     */     }
/*  68 */     graphics.nextStratum();
/*     */     
/*  70 */     ProfilerFiller profiler = Profiler.get();
/*  71 */     profiler.push("bossHealth");
/*     */     
/*  73 */     int screenWidth = graphics.guiWidth();
/*     */     
/*  75 */     int yOffset = 12;
/*  76 */     for (LerpingBossEvent event : this.events.values()) {
/*  77 */       int xLeft = screenWidth / 2 - 91;
/*     */       
/*  79 */       int yo = yOffset;
/*  80 */       drawBar(graphics, xLeft, yo, event);
/*  81 */       Component msg = event.getName();
/*  82 */       int width = this.minecraft.font.width((FormattedText)msg);
/*  83 */       int x = screenWidth / 2 - width / 2;
/*  84 */       int y = yo - 9;
/*  85 */       graphics.drawString(this.minecraft.font, msg, x, y, -1);
/*  86 */       Objects.requireNonNull(this.minecraft.font); yOffset += 10 + 9;
/*     */       
/*  88 */       if (yOffset >= graphics.guiHeight() / 3) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  93 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void drawBar(GuiGraphics graphics, int x, int y, BossEvent event) {
/*  97 */     drawBar(graphics, x, y, event, 182, BAR_BACKGROUND_SPRITES, OVERLAY_BACKGROUND_SPRITES);
/*  98 */     int width = Mth.lerpDiscrete(event.getProgress(), 0, 182);
/*  99 */     if (width > 0) {
/* 100 */       drawBar(graphics, x, y, event, width, BAR_PROGRESS_SPRITES, OVERLAY_PROGRESS_SPRITES);
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawBar(GuiGraphics graphics, int x, int y, BossEvent event, int width, Identifier[] sprites, Identifier[] overlaySprites) {
/* 105 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprites[event.getColor().ordinal()], 182, 5, 0, 0, x, y, width, 5);
/* 106 */     if (event.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
/* 107 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, overlaySprites[event.getOverlay().ordinal() - 1], 182, 5, 0, 0, x, y, width, 5);
/*     */     }
/*     */   }
/*     */   
/*     */   public void update(ClientboundBossEventPacket packet) {
/* 112 */     packet.dispatch(new ClientboundBossEventPacket.Handler()
/*     */         {
/*     */           public void add(UUID id, Component name, float progress, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 115 */             BossHealthOverlay.this.events.put(id, new LerpingBossEvent(id, name, progress, color, overlay, darkenScreen, playMusic, createWorldFog));
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove(UUID id) {
/* 120 */             BossHealthOverlay.this.events.remove(id);
/*     */           }
/*     */ 
/*     */           
/*     */           public void updateProgress(UUID id, float progress) {
/* 125 */             ((LerpingBossEvent)BossHealthOverlay.this.events.get(id)).setProgress(progress);
/*     */           }
/*     */ 
/*     */           
/*     */           public void updateName(UUID id, Component name) {
/* 130 */             ((LerpingBossEvent)BossHealthOverlay.this.events.get(id)).setName(name);
/*     */           }
/*     */ 
/*     */           
/*     */           public void updateStyle(UUID id, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
/* 135 */             LerpingBossEvent event = BossHealthOverlay.this.events.get(id);
/* 136 */             event.setColor(color);
/* 137 */             event.setOverlay(overlay);
/*     */           }
/*     */ 
/*     */           
/*     */           public void updateProperties(UUID id, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 142 */             LerpingBossEvent event = BossHealthOverlay.this.events.get(id);
/* 143 */             event.setDarkenScreen(darkenScreen);
/* 144 */             event.setPlayBossMusic(playMusic);
/* 145 */             event.setCreateWorldFog(createWorldFog);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void reset() {
/* 151 */     this.events.clear();
/*     */   }
/*     */   
/*     */   public boolean shouldPlayMusic() {
/* 155 */     if (!this.events.isEmpty()) {
/* 156 */       for (BossEvent event : this.events.values()) {
/* 157 */         if (event.shouldPlayBossMusic()) {
/* 158 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldDarkenScreen() {
/* 167 */     if (!this.events.isEmpty()) {
/* 168 */       for (BossEvent event : this.events.values()) {
/* 169 */         if (event.shouldDarkenScreen()) {
/* 170 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 175 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldCreateWorldFog() {
/* 179 */     if (!this.events.isEmpty()) {
/* 180 */       for (BossEvent event : this.events.values()) {
/* 181 */         if (event.shouldCreateWorldFog()) {
/* 182 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 187 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/BossHealthOverlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */