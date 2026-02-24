/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.color.ColorLerper;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ 
/*     */ public class NowPlayingToast
/*     */   implements Toast {
/*  17 */   private static final Identifier NOW_PLAYING_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/now_playing");
/*  18 */   private static final Identifier MUSIC_NOTES_SPRITE = Identifier.parse("icon/music_notes");
/*     */   private static final int PADDING = 7;
/*     */   private static final int MUSIC_NOTES_SIZE = 16;
/*     */   private static final int HEIGHT = 30;
/*     */   private static final int MUSIC_NOTES_SPACE = 30;
/*     */   private static final int VISIBILITY_DURATION = 5000;
/*  24 */   private static final int TEXT_COLOR = DyeColor.LIGHT_GRAY.getTextColor();
/*     */   
/*     */   private static final long MUSIC_COLOR_CHANGE_FREQUENCY_MS = 25L;
/*     */   private static int musicNoteColorTick;
/*     */   private static long lastMusicNoteColorChange;
/*  29 */   private static int musicNoteColor = -1;
/*     */   
/*     */   private boolean updateToast;
/*     */   
/*     */   private double notificationDisplayTimeMultiplier;
/*     */   
/*     */   private final Minecraft minecraft;
/*  36 */   private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
/*     */   
/*     */   public NowPlayingToast() {
/*  39 */     this.minecraft = Minecraft.getInstance();
/*     */   }
/*     */   
/*     */   public static void renderToast(GuiGraphics graphics, Font font) {
/*  43 */     String currentSong = getCurrentSongName();
/*  44 */     if (currentSong != null) {
/*  45 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOW_PLAYING_BACKGROUND_SPRITE, 0, 0, getWidth(currentSong, font), 30);
/*  46 */       int notesOffset = 7;
/*  47 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MUSIC_NOTES_SPRITE, 7, 7, 16, 16, musicNoteColor);
/*  48 */       Objects.requireNonNull(font); graphics.drawString(font, getNowPlayingString(currentSong), 30, 15 - 9 / 2, TEXT_COLOR);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getCurrentSongName() {
/*  53 */     return Minecraft.getInstance().getMusicManager().getCurrentMusicTranslationKey();
/*     */   }
/*     */   
/*     */   public static void tickMusicNotes() {
/*  57 */     if (getCurrentSongName() != null) {
/*  58 */       long now = System.currentTimeMillis();
/*  59 */       if (now > lastMusicNoteColorChange + 25L) {
/*  60 */         musicNoteColorTick++;
/*  61 */         lastMusicNoteColorChange = now;
/*  62 */         musicNoteColor = ColorLerper.getLerpedColor(ColorLerper.Type.MUSIC_NOTE, musicNoteColorTick);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Component getNowPlayingString(String currentSongKey) {
/*  68 */     if (currentSongKey == null) {
/*  69 */       return (Component)Component.empty();
/*     */     }
/*  71 */     return (Component)Component.translatable(currentSongKey.replace("/", "."));
/*     */   }
/*     */   
/*     */   public void showToast(Options options) {
/*  75 */     this.updateToast = true;
/*  76 */     this.notificationDisplayTimeMultiplier = (Double)options.notificationDisplayTime().get();
/*  77 */     setWantedVisibility(Toast.Visibility.SHOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(ToastManager manager, long fullyVisibleForMs) {
/*  82 */     if (this.updateToast) {
/*  83 */       this.wantedVisibility = (fullyVisibleForMs < 5000.0D * this.notificationDisplayTimeMultiplier) ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
/*  84 */       tickMusicNotes();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
/*  90 */     renderToast(graphics, font);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFinishedRendering() {
/*  95 */     this.updateToast = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int width() {
/* 100 */     return getWidth(getCurrentSongName(), this.minecraft.font);
/*     */   }
/*     */   
/*     */   private static int getWidth(String currentSong, Font font) {
/* 104 */     return 30 + font.width((FormattedText)getNowPlayingString(currentSong)) + 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public int height() {
/* 109 */     return 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public float xPos(int screenWidth, float visiblePortion) {
/* 114 */     return width() * visiblePortion - width();
/*     */   }
/*     */ 
/*     */   
/*     */   public float yPos(int firstSlotIndex) {
/* 119 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Toast.Visibility getWantedVisibility() {
/* 124 */     return this.wantedVisibility;
/*     */   }
/*     */   
/*     */   public void setWantedVisibility(Toast.Visibility visibility) {
/* 128 */     this.wantedVisibility = visibility;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/NowPlayingToast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */