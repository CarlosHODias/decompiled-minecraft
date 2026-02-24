/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class SystemToast
/*     */   implements Toast {
/*  19 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/system");
/*     */   
/*     */   private static final int MAX_LINE_SIZE = 200;
/*     */   private static final int LINE_SPACING = 12;
/*     */   private static final int MARGIN = 10;
/*     */   private final SystemToastId id;
/*     */   private Component title;
/*     */   private List<FormattedCharSequence> messageLines;
/*     */   private long lastChanged;
/*     */   private boolean changed;
/*     */   private final int width;
/*     */   private boolean forceHide;
/*  31 */   private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
/*     */   
/*     */   public SystemToast(SystemToastId id, Component title, Component message) {
/*  34 */     this(id, title, (List<FormattedCharSequence>)nullToEmpty(message), Math.max(160, 30 + Math.max(
/*  35 */             (Minecraft.getInstance()).font.width((FormattedText)title), 
/*  36 */             (message == null) ? 0 : (Minecraft.getInstance()).font.width((FormattedText)message))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static SystemToast multiline(Minecraft minecraft, SystemToastId id, Component title, Component message) {
/*  41 */     Font font = minecraft.font;
/*  42 */     List<FormattedCharSequence> lines = font.split((FormattedText)message, 200);
/*  43 */     Objects.requireNonNull(font); int width = Math.max(200, lines.stream().mapToInt(font::width).max().orElse(200));
/*  44 */     return new SystemToast(id, title, lines, width + 30);
/*     */   }
/*     */   
/*     */   private SystemToast(SystemToastId id, Component title, List<FormattedCharSequence> messageLines, int width) {
/*  48 */     this.id = id;
/*  49 */     this.title = title;
/*  50 */     this.messageLines = messageLines;
/*  51 */     this.width = width;
/*     */   }
/*     */   
/*     */   private static ImmutableList<FormattedCharSequence> nullToEmpty(Component message) {
/*  55 */     return (message == null) ? ImmutableList.of() : ImmutableList.of(message.getVisualOrderText());
/*     */   }
/*     */ 
/*     */   
/*     */   public int width() {
/*  60 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int height() {
/*  65 */     return 20 + Math.max(this.messageLines.size(), 1) * 12;
/*     */   }
/*     */   
/*     */   public void forceHide() {
/*  69 */     this.forceHide = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Toast.Visibility getWantedVisibility() {
/*  74 */     return this.wantedVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(ToastManager manager, long fullyVisibleForMs) {
/*  79 */     if (this.changed) {
/*  80 */       this.lastChanged = fullyVisibleForMs;
/*  81 */       this.changed = false;
/*     */     } 
/*  83 */     double timeToDisplayUpdate = this.id.displayTime * manager.getNotificationDisplayTimeMultiplier();
/*  84 */     long timeSinceUpdate = fullyVisibleForMs - this.lastChanged;
/*     */     
/*  86 */     this.wantedVisibility = (!this.forceHide && timeSinceUpdate < timeToDisplayUpdate) ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
/*  91 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());
/*  92 */     if (this.messageLines.isEmpty()) {
/*  93 */       graphics.drawString(font, this.title, 18, 12, -256, false);
/*     */     } else {
/*  95 */       graphics.drawString(font, this.title, 18, 7, -256, false);
/*  96 */       for (int i = 0; i < this.messageLines.size(); i++) {
/*  97 */         graphics.drawString(font, this.messageLines.get(i), 18, 18 + i * 12, -1, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reset(Component title, Component message) {
/* 103 */     this.title = title;
/* 104 */     this.messageLines = (List<FormattedCharSequence>)nullToEmpty(message);
/* 105 */     this.changed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public SystemToastId getToken() {
/* 110 */     return this.id;
/*     */   }
/*     */   
/*     */   public static class SystemToastId {
/* 114 */     public static final SystemToastId NARRATOR_TOGGLE = new SystemToastId();
/* 115 */     public static final SystemToastId WORLD_BACKUP = new SystemToastId();
/* 116 */     public static final SystemToastId PACK_LOAD_FAILURE = new SystemToastId();
/* 117 */     public static final SystemToastId WORLD_ACCESS_FAILURE = new SystemToastId();
/* 118 */     public static final SystemToastId PACK_COPY_FAILURE = new SystemToastId();
/* 119 */     public static final SystemToastId FILE_DROP_FAILURE = new SystemToastId();
/* 120 */     public static final SystemToastId PERIODIC_NOTIFICATION = new SystemToastId();
/* 121 */     public static final SystemToastId LOW_DISK_SPACE = new SystemToastId(10000L);
/* 122 */     public static final SystemToastId CHUNK_LOAD_FAILURE = new SystemToastId();
/* 123 */     public static final SystemToastId CHUNK_SAVE_FAILURE = new SystemToastId();
/* 124 */     public static final SystemToastId UNSECURE_SERVER_WARNING = new SystemToastId(10000L);
/*     */     
/*     */     private final long displayTime;
/*     */     
/*     */     public SystemToastId(long displayTime) {
/* 129 */       this.displayTime = displayTime;
/*     */     }
/*     */     
/*     */     public SystemToastId() {
/* 133 */       this(5000L);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void add(ToastManager toastManager, SystemToastId id, Component title, Component message) {
/* 138 */     toastManager.addToast(new SystemToast(id, title, message));
/*     */   }
/*     */   
/*     */   public static void addOrUpdate(ToastManager toastManager, SystemToastId id, Component title, Component message) {
/* 142 */     SystemToast toast = toastManager.<SystemToast>getToast(SystemToast.class, id);
/* 143 */     if (toast == null) {
/* 144 */       add(toastManager, id, title, message);
/*     */     } else {
/* 146 */       toast.reset(title, message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void forceHide(ToastManager toastManager, SystemToastId id) {
/* 151 */     SystemToast toast = toastManager.<SystemToast>getToast(SystemToast.class, id);
/* 152 */     if (toast != null) {
/* 153 */       toast.forceHide();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void onWorldAccessFailure(Minecraft minecraft, String levelId) {
/* 158 */     add(minecraft.getToastManager(), SystemToastId.WORLD_ACCESS_FAILURE, (Component)Component.translatable("selectWorld.access_failure"), (Component)Component.literal(levelId));
/*     */   }
/*     */   
/*     */   public static void onWorldDeleteFailure(Minecraft minecraft, String levelId) {
/* 162 */     add(minecraft.getToastManager(), SystemToastId.WORLD_ACCESS_FAILURE, (Component)Component.translatable("selectWorld.delete_failure"), (Component)Component.literal(levelId));
/*     */   }
/*     */   
/*     */   public static void onPackCopyFailure(Minecraft minecraft, String extraInfo) {
/* 166 */     add(minecraft.getToastManager(), SystemToastId.PACK_COPY_FAILURE, (Component)Component.translatable("pack.copyFailure"), (Component)Component.literal(extraInfo));
/*     */   }
/*     */   
/*     */   public static void onFileDropFailure(Minecraft minecraft, int count) {
/* 170 */     add(minecraft.getToastManager(), SystemToastId.FILE_DROP_FAILURE, (Component)Component.translatable("gui.fileDropFailure.title"), (Component)Component.translatable("gui.fileDropFailure.detail", new Object[] { count }));
/*     */   }
/*     */   
/*     */   public static void onLowDiskSpace(Minecraft minecraft) {
/* 174 */     addOrUpdate(minecraft.getToastManager(), SystemToastId.LOW_DISK_SPACE, (Component)Component.translatable("chunk.toast.lowDiskSpace"), (Component)Component.translatable("chunk.toast.lowDiskSpace.description"));
/*     */   }
/*     */   
/*     */   public static void onChunkLoadFailure(Minecraft minecraft, ChunkPos pos) {
/* 178 */     addOrUpdate(
/* 179 */         minecraft.getToastManager(), SystemToastId.CHUNK_LOAD_FAILURE, 
/*     */         
/* 181 */         (Component)Component.translatable("chunk.toast.loadFailure", new Object[] { Component.translationArg(pos) }).withStyle(ChatFormatting.RED), 
/* 182 */         (Component)Component.translatable("chunk.toast.checkLog"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void onChunkSaveFailure(Minecraft minecraft, ChunkPos pos) {
/* 187 */     addOrUpdate(
/* 188 */         minecraft.getToastManager(), SystemToastId.CHUNK_SAVE_FAILURE, 
/*     */         
/* 190 */         (Component)Component.translatable("chunk.toast.saveFailure", new Object[] { Component.translationArg(pos) }).withStyle(ChatFormatting.RED), 
/* 191 */         (Component)Component.translatable("chunk.toast.checkLog"));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/SystemToast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */