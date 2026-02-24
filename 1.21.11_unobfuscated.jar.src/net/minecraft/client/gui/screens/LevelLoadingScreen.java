/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class LevelLoadingScreen
/*     */   extends Screen {
/*  29 */   private static final Component DOWNLOADING_TERRAIN_TEXT = (Component)Component.translatable("multiplayer.downloadingTerrain");
/*  30 */   private static final Component READY_TO_PLAY_TEXT = (Component)Component.translatable("narrator.ready_to_play");
/*     */   
/*     */   private static final long NARRATION_DELAY_MS = 2000L;
/*     */   
/*     */   private static final int PROGRESS_BAR_WIDTH = 200;
/*     */   
/*     */   private LevelLoadTracker loadTracker;
/*     */   
/*     */   private float smoothedProgress;
/*  39 */   private long lastNarration = -1L;
/*     */   private Reason reason;
/*     */   private TextureAtlasSprite cachedNetherPortalSprite;
/*     */   private static final Object2IntMap<ChunkStatus> COLORS;
/*     */   
/*     */   public LevelLoadingScreen(LevelLoadTracker loadTracker, Reason reason) {
/*  45 */     super(GameNarrator.NO_TITLE);
/*  46 */     this.loadTracker = loadTracker;
/*  47 */     this.reason = reason;
/*     */   }
/*     */   
/*     */   public void update(LevelLoadTracker loadTracker, Reason reason) {
/*  51 */     this.loadTracker = loadTracker;
/*  52 */     this.reason = reason;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldNarrateNavigation() {
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateNarratedWidget(NarrationElementOutput output) {
/*  67 */     if (this.loadTracker.hasProgress()) {
/*  68 */       output.add(NarratedElementType.TITLE, (Component)Component.translatable("loading.progress", new Object[] { Mth.floor(this.loadTracker.serverProgress() * 100.0F) }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  74 */     super.tick();
/*  75 */     this.smoothedProgress += (this.loadTracker.serverProgress() - this.smoothedProgress) * 0.2F;
/*  76 */     if (this.loadTracker.isLevelReady()) {
/*  77 */       onClose();
/*     */     }
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */     int textTop;
/*  83 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  85 */     long current = Util.getMillis();
/*  86 */     if (current - this.lastNarration > 2000L) {
/*  87 */       this.lastNarration = current;
/*  88 */       triggerImmediateNarration(true);
/*     */     } 
/*     */     
/*  91 */     int xCenter = this.width / 2;
/*  92 */     int yCenter = this.height / 2;
/*     */     
/*  94 */     ChunkLoadStatusView statusView = this.loadTracker.statusView();
/*     */     
/*  96 */     if (statusView != null) {
/*  97 */       int size = 2;
/*  98 */       renderChunks(graphics, xCenter, yCenter, 2, 0, statusView);
/*  99 */       Objects.requireNonNull(this.font); textTop = yCenter - statusView.radius() * 2 - 9 * 3;
/*     */     } else {
/* 101 */       textTop = yCenter - 50;
/*     */     } 
/*     */     
/* 104 */     graphics.drawCenteredString(this.font, DOWNLOADING_TERRAIN_TEXT, xCenter, textTop, -1);
/*     */     
/* 106 */     if (this.loadTracker.hasProgress()) {
/* 107 */       Objects.requireNonNull(this.font); drawProgressBar(graphics, xCenter - 100, textTop + 9 + 3, 200, 2, this.smoothedProgress);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawProgressBar(GuiGraphics graphics, int left, int top, int width, int height, float progress) {
/* 112 */     graphics.fill(left, top, left + width, top + height, -16777216);
/* 113 */     graphics.fill(left, top, left + Math.round(progress * width), top + height, -16711936);
/*     */   }
/*     */   
/*     */   public static void renderChunks(GuiGraphics graphics, int xCenter, int yCenter, int size, int margin, ChunkLoadStatusView statusView) {
/* 117 */     int width = size + margin;
/*     */     
/* 119 */     int diameter = statusView.radius() * 2 + 1;
/* 120 */     int totalWidth = diameter * width - margin;
/*     */     
/* 122 */     int xStart = xCenter - totalWidth / 2;
/* 123 */     int yStart = yCenter - totalWidth / 2;
/*     */     
/* 125 */     if ((Minecraft.getInstance()).debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_CHUNKS_ON_SERVER)) {
/* 126 */       int centerWidth = width / 2 + 1;
/* 127 */       graphics.fill(xCenter - centerWidth, yCenter - centerWidth, xCenter + centerWidth, yCenter + centerWidth, -65536);
/*     */     } 
/*     */     
/* 130 */     for (int x = 0; x < diameter; x++) {
/* 131 */       for (int z = 0; z < diameter; z++) {
/* 132 */         ChunkStatus status = statusView.get(x, z);
/* 133 */         int xCellStart = xStart + x * width;
/* 134 */         int yCellStart = yStart + z * width;
/* 135 */         graphics.fill(xCellStart, yCellStart, xCellStart + size, yCellStart + size, ARGB.opaque(COLORS.getInt(status)));
/*     */       } 
/*     */     } 
/*     */   } public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */     TextureManager textureManager;
/*     */     AbstractTexture skyTexture, portalTexture;
/*     */     TextureSetup textureSetup;
/* 142 */     switch (this.reason.ordinal()) {
/*     */       case 2:
/* 144 */         renderPanorama(graphics, a);
/* 145 */         renderBlurredBackground(graphics);
/* 146 */         renderMenuBackground(graphics);
/*     */         break;
/*     */       case 0:
/* 149 */         graphics.blitSprite(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, getNetherPortalSprite(), 0, 0, graphics.guiWidth(), graphics.guiHeight());
/*     */         break;
/*     */       case 1:
/* 152 */         textureManager = Minecraft.getInstance().getTextureManager();
/* 153 */         skyTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_SKY_LOCATION);
/* 154 */         portalTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_PORTAL_LOCATION);
/* 155 */         textureSetup = TextureSetup.doubleTexture(skyTexture.getTextureView(), skyTexture.getSampler(), portalTexture.getTextureView(), portalTexture.getSampler());
/* 156 */         graphics.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private TextureAtlasSprite getNetherPortalSprite() {
/* 162 */     if (this.cachedNetherPortalSprite != null) {
/* 163 */       return this.cachedNetherPortalSprite;
/*     */     }
/* 165 */     this.cachedNetherPortalSprite = this.minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
/* 166 */     return this.cachedNetherPortalSprite;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 171 */     this.minecraft.getNarrator().saySystemNow(READY_TO_PLAY_TEXT);
/* 172 */     super.onClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 177 */     return false;
/*     */   }
/*     */   static {
/* 180 */     COLORS = (Object2IntMap<ChunkStatus>)Util.make(new Object2IntOpenHashMap(), map -> {
/*     */           map.defaultReturnValue(0);
/*     */           map.put(ChunkStatus.EMPTY, 5526612);
/*     */           map.put(ChunkStatus.STRUCTURE_STARTS, 10066329);
/*     */           map.put(ChunkStatus.STRUCTURE_REFERENCES, 6250897);
/*     */           map.put(ChunkStatus.BIOMES, 8434258);
/*     */           map.put(ChunkStatus.NOISE, 13750737);
/*     */           map.put(ChunkStatus.SURFACE, 7497737);
/*     */           map.put(ChunkStatus.CARVERS, 3159410);
/*     */           map.put(ChunkStatus.FEATURES, 2213376);
/*     */           map.put(ChunkStatus.INITIALIZE_LIGHT, 13421772);
/*     */           map.put(ChunkStatus.LIGHT, 16769184);
/*     */           map.put(ChunkStatus.SPAWN, 15884384);
/*     */           map.put(ChunkStatus.FULL, 16777215);
/*     */         });
/*     */   }
/*     */   
/* 197 */   public enum Reason { NETHER_PORTAL,
/* 198 */     END_PORTAL,
/* 199 */     OTHER; }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/LevelLoadingScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */