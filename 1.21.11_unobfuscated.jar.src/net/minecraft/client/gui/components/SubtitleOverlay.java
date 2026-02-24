/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.audio.ListenerTransform;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.client.sounds.SoundEventListener;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.client.sounds.WeighedSoundEvents;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SubtitleOverlay implements SoundEventListener {
/*     */   private static final long DISPLAY_TIME = 3000L;
/*     */   private final Minecraft minecraft;
/*  27 */   private final List<Subtitle> subtitles = Lists.newArrayList();
/*     */   
/*     */   private boolean isListening;
/*  30 */   private final List<Subtitle> audibleSubtitles = new ArrayList<>();
/*     */   
/*     */   public SubtitleOverlay(Minecraft minecraft) {
/*  33 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics) {
/*  37 */     SoundManager soundManager = this.minecraft.getSoundManager();
/*  38 */     if (!this.isListening && (Boolean)this.minecraft.options.showSubtitles().get()) {
/*  39 */       soundManager.addListener(this);
/*  40 */       this.isListening = true;
/*  41 */     } else if (this.isListening && !((Boolean)this.minecraft.options.showSubtitles().get())) {
/*  42 */       soundManager.removeListener(this);
/*  43 */       this.isListening = false;
/*     */     } 
/*     */     
/*  46 */     if (!this.isListening) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     ListenerTransform listener = soundManager.getListenerTransform();
/*  51 */     Vec3 position = listener.position();
/*  52 */     Vec3 forwards = listener.forward();
/*  53 */     Vec3 right = listener.right();
/*     */     
/*  55 */     this.audibleSubtitles.clear();
/*  56 */     for (Subtitle subtitle : this.subtitles) {
/*  57 */       if (subtitle.isAudibleFrom(position)) {
/*  58 */         this.audibleSubtitles.add(subtitle);
/*     */       }
/*     */     } 
/*     */     
/*  62 */     if (this.audibleSubtitles.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  66 */     int row = 0;
/*  67 */     int width = 0;
/*     */     
/*  69 */     double displayTimeMultiplier = (Double)this.minecraft.options.notificationDisplayTime().get();
/*     */     
/*  71 */     for (Iterator<Subtitle> iterator = this.audibleSubtitles.iterator(); iterator.hasNext(); ) {
/*  72 */       Subtitle subtitle = iterator.next();
/*  73 */       subtitle.purgeOldInstances(3000.0D * displayTimeMultiplier);
/*  74 */       if (!subtitle.isStillActive()) {
/*  75 */         iterator.remove(); continue;
/*     */       } 
/*  77 */       width = Math.max(width, this.minecraft.font.width((FormattedText)subtitle.getText()));
/*     */     } 
/*     */ 
/*     */     
/*  81 */     width += this.minecraft.font.width("<") + this.minecraft.font.width(" ") + this.minecraft.font.width(">") + this.minecraft.font.width(" ");
/*     */     
/*  83 */     if (!this.audibleSubtitles.isEmpty()) {
/*  84 */       graphics.nextStratum();
/*     */     }
/*     */     
/*  87 */     for (Subtitle subtitle : this.audibleSubtitles) {
/*  88 */       int alpha = 255;
/*  89 */       Component text = subtitle.getText();
/*     */       
/*  91 */       SoundPlayedAt closestRecentLocation = subtitle.getClosest(position);
/*     */       
/*  93 */       if (closestRecentLocation == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  97 */       Vec3 delta = closestRecentLocation.location.subtract(position).normalize();
/*  98 */       double rightness = right.dot(delta);
/*  99 */       double forwardness = forwards.dot(delta);
/* 100 */       boolean inView = (forwardness > 0.5D);
/*     */       
/* 102 */       int halfWidth = width / 2;
/* 103 */       Objects.requireNonNull(this.minecraft.font); int height = 9;
/* 104 */       int halfHeight = height / 2;
/* 105 */       float scale = 1.0F;
/* 106 */       int textWidth = this.minecraft.font.width((FormattedText)text);
/* 107 */       int brightness = Mth.floor(Mth.clampedLerp((float)(Util.getMillis() - closestRecentLocation.time) / (float)(3000.0D * displayTimeMultiplier), 255.0F, 75.0F));
/*     */       
/* 109 */       graphics.pose().pushMatrix();
/* 110 */       graphics.pose().translate(graphics.guiWidth() - halfWidth * 1.0F - 2.0F, (graphics.guiHeight() - 35) - (row * (height + 1)) * 1.0F);
/* 111 */       graphics.pose().scale(1.0F, 1.0F);
/*     */       
/* 113 */       graphics.fill(-halfWidth - 1, -halfHeight - 1, halfWidth + 1, halfHeight + 1, this.minecraft.options.getBackgroundColor(0.8F));
/*     */       
/* 115 */       int textColor = ARGB.color(255, brightness, brightness, brightness);
/* 116 */       if (!inView) {
/* 117 */         if (rightness > 0.0D) {
/* 118 */           graphics.drawString(this.minecraft.font, ">", halfWidth - this.minecraft.font.width(">"), -halfHeight, textColor);
/* 119 */         } else if (rightness < 0.0D) {
/* 120 */           graphics.drawString(this.minecraft.font, "<", -halfWidth, -halfHeight, textColor);
/*     */         } 
/*     */       }
/* 123 */       graphics.drawString(this.minecraft.font, text, -textWidth / 2, -halfHeight, textColor);
/* 124 */       graphics.pose().popMatrix();
/* 125 */       row++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlaySound(SoundInstance sound, WeighedSoundEvents soundEvent, float range) {
/* 131 */     if (soundEvent.getSubtitle() == null) {
/*     */       return;
/*     */     }
/* 134 */     Component text = soundEvent.getSubtitle();
/* 135 */     if (!this.subtitles.isEmpty()) {
/* 136 */       for (Subtitle subtitle : this.subtitles) {
/* 137 */         if (subtitle.getText().equals(text)) {
/* 138 */           subtitle.refresh(new Vec3(sound.getX(), sound.getY(), sound.getZ()));
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/* 144 */     this.subtitles.add(new Subtitle(text, range, new Vec3(sound.getX(), sound.getY(), sound.getZ())));
/*     */   }
/*     */   static final class SoundPlayedAt extends Record { private final Vec3 location; private final long time;
/* 147 */     SoundPlayedAt(Vec3 location, long time) { this.location = location; this.time = time; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 147 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt; } public Vec3 location() { return this.location; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 147 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt; } public long time() { return this.time; }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } }
/* 152 */   static class Subtitle { private final Component text; private final float range; private final List<SubtitleOverlay.SoundPlayedAt> playedAt = new ArrayList<>();
/*     */     
/*     */     public Subtitle(Component text, float range, Vec3 location) {
/* 155 */       this.text = text;
/* 156 */       this.range = range;
/* 157 */       this.playedAt.add(new SubtitleOverlay.SoundPlayedAt(location, Util.getMillis()));
/*     */     }
/*     */     
/*     */     public Component getText() {
/* 161 */       return this.text;
/*     */     }
/*     */     
/*     */     public SubtitleOverlay.SoundPlayedAt getClosest(Vec3 position) {
/* 165 */       if (this.playedAt.isEmpty()) {
/* 166 */         return null;
/*     */       }
/*     */       
/* 169 */       if (this.playedAt.size() == 1) {
/* 170 */         return this.playedAt.getFirst();
/*     */       }
/*     */       
/* 173 */       return this.playedAt.stream()
/* 174 */         .min(Comparator.comparingDouble(soundPlayedAt -> soundPlayedAt.location().distanceTo(position)))
/* 175 */         .orElse(null);
/*     */     }
/*     */     
/*     */     public void refresh(Vec3 location) {
/* 179 */       this.playedAt.removeIf(soundPlayedAt -> location.equals(soundPlayedAt.location()));
/* 180 */       this.playedAt.add(new SubtitleOverlay.SoundPlayedAt(location, Util.getMillis()));
/*     */     }
/*     */     
/*     */     public boolean isAudibleFrom(Vec3 camera) {
/* 184 */       if (Float.isInfinite(this.range)) {
/* 185 */         return true;
/*     */       }
/*     */       
/* 188 */       if (this.playedAt.isEmpty()) {
/* 189 */         return false;
/*     */       }
/*     */       
/* 192 */       SubtitleOverlay.SoundPlayedAt closest = getClosest(camera);
/*     */       
/* 194 */       if (closest == null) {
/* 195 */         return false;
/*     */       }
/*     */       
/* 198 */       return camera.closerThan((Position)closest.location, this.range);
/*     */     }
/*     */     
/*     */     public void purgeOldInstances(double maxAge) {
/* 202 */       long currentTime = Util.getMillis();
/* 203 */       this.playedAt.removeIf(soundPlayedAt -> ((currentTime - soundPlayedAt.time()) > maxAge));
/*     */     }
/*     */     
/*     */     public boolean isStillActive() {
/* 207 */       return !this.playedAt.isEmpty();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/SubtitleOverlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */