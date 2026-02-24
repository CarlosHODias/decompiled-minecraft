/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class TutorialToast
/*     */   implements Toast
/*     */ {
/*  17 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/tutorial");
/*     */   
/*     */   public static final int PROGRESS_BAR_WIDTH = 154;
/*     */   
/*     */   public static final int PROGRESS_BAR_HEIGHT = 1;
/*     */   
/*     */   public static final int PROGRESS_BAR_X = 3;
/*     */   public static final int PROGRESS_BAR_MARGIN_BOTTOM = 4;
/*     */   private static final int PADDING_TOP = 7;
/*     */   private static final int PADDING_BOTTOM = 3;
/*     */   private static final int LINE_SPACING = 11;
/*     */   private static final int TEXT_LEFT = 30;
/*     */   private static final int TEXT_WIDTH = 126;
/*     */   private final Icons icon;
/*     */   private final List<FormattedCharSequence> lines;
/*  32 */   private Toast.Visibility visibility = Toast.Visibility.SHOW;
/*     */   private long lastSmoothingTime;
/*     */   private float smoothedProgress;
/*     */   private float progress;
/*     */   private final boolean progressable;
/*     */   private final int timeToDisplayMs;
/*     */   
/*     */   public TutorialToast(Font font, Icons icon, Component title, Component message, boolean progressable, int timeToDisplayMs) {
/*  40 */     this.icon = icon;
/*  41 */     this.lines = new ArrayList<>(2);
/*  42 */     this.lines.addAll(font.split((FormattedText)title.copy().withColor(-11534256), 126));
/*  43 */     if (message != null) {
/*  44 */       this.lines.addAll(font.split((FormattedText)message, 126));
/*     */     }
/*  46 */     this.progressable = progressable;
/*  47 */     this.timeToDisplayMs = timeToDisplayMs;
/*     */   }
/*     */   
/*     */   public TutorialToast(Font font, Icons icon, Component title, Component message, boolean progressable) {
/*  51 */     this(font, icon, title, message, progressable, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Toast.Visibility getWantedVisibility() {
/*  56 */     return this.visibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(ToastManager manager, long fullyVisibleForMs) {
/*  61 */     if (this.timeToDisplayMs > 0) {
/*  62 */       this.progress = Math.min((float)fullyVisibleForMs / this.timeToDisplayMs, 1.0F);
/*  63 */       this.smoothedProgress = this.progress;
/*  64 */       this.lastSmoothingTime = fullyVisibleForMs;
/*  65 */       if (fullyVisibleForMs > this.timeToDisplayMs) {
/*  66 */         hide();
/*     */       }
/*  68 */     } else if (this.progressable) {
/*  69 */       this.smoothedProgress = Mth.clampedLerp((float)(fullyVisibleForMs - this.lastSmoothingTime) / 100.0F, this.smoothedProgress, this.progress);
/*  70 */       this.lastSmoothingTime = fullyVisibleForMs;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int height() {
/*  76 */     return 7 + contentHeight() + 3;
/*     */   }
/*     */   
/*     */   private int contentHeight() {
/*  80 */     return Math.max(this.lines.size(), 2) * 11;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
/*  85 */     int height = height();
/*  86 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height);
/*  87 */     this.icon.render(graphics, 6, 6);
/*     */     
/*  89 */     int textHeight = this.lines.size() * 11;
/*  90 */     int textTop = 7 + (contentHeight() - textHeight) / 2;
/*     */     
/*  92 */     for (int i = 0; i < this.lines.size(); i++) {
/*  93 */       graphics.drawString(font, this.lines.get(i), 30, textTop + i * 11, -16777216, false);
/*     */     }
/*     */     
/*  96 */     if (this.progressable) {
/*  97 */       int col; int progressBarY = height - 4;
/*  98 */       graphics.fill(3, progressBarY, 157, progressBarY + 1, -1);
/*     */ 
/*     */       
/* 101 */       if (this.progress >= this.smoothedProgress) {
/* 102 */         col = -16755456;
/*     */       } else {
/* 104 */         col = -11206656;
/*     */       } 
/* 106 */       graphics.fill(3, progressBarY, (int)(3.0F + 154.0F * this.smoothedProgress), progressBarY + 1, col);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hide() {
/* 111 */     this.visibility = Toast.Visibility.HIDE;
/*     */   }
/*     */   
/*     */   public void updateProgress(float progress) {
/* 115 */     this.progress = progress;
/*     */   }
/*     */   
/*     */   public enum Icons {
/* 119 */     MOVEMENT_KEYS(Identifier.withDefaultNamespace("toast/movement_keys")),
/* 120 */     MOUSE(Identifier.withDefaultNamespace("toast/mouse")),
/* 121 */     TREE(Identifier.withDefaultNamespace("toast/tree")),
/* 122 */     RECIPE_BOOK(Identifier.withDefaultNamespace("toast/recipe_book")),
/* 123 */     WOODEN_PLANKS(Identifier.withDefaultNamespace("toast/wooden_planks")),
/* 124 */     SOCIAL_INTERACTIONS(Identifier.withDefaultNamespace("toast/social_interactions")),
/* 125 */     RIGHT_CLICK(Identifier.withDefaultNamespace("toast/right_click"));
/*     */     
/*     */     private final Identifier sprite;
/*     */ 
/*     */     
/*     */     Icons(Identifier sprite) {
/* 131 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public void render(GuiGraphics graphics, int x, int y) {
/* 135 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, 20, 20);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/TutorialToast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */