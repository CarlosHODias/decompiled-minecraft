/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.MusicToastDisplayState;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ToastManager
/*     */ {
/*     */   private static final int SLOT_COUNT = 5;
/*     */   private static final int ALL_SLOTS_OCCUPIED = -1;
/*     */   private final Minecraft minecraft;
/*  29 */   private final List<ToastInstance<?>> visibleToasts = new ArrayList<>();
/*  30 */   private final BitSet occupiedSlots = new BitSet(5);
/*     */   
/*  32 */   private final Deque<Toast> queued = Queues.newArrayDeque();
/*     */   
/*  34 */   private final Set<SoundEvent> playedToastSounds = new HashSet<>();
/*     */   
/*     */   private ToastInstance<NowPlayingToast> nowPlayingToast;
/*     */   
/*     */   public ToastManager(Minecraft minecraft, Options options) {
/*  39 */     this.minecraft = minecraft;
/*  40 */     initializeMusicToast((MusicToastDisplayState)options.musicToast().get());
/*     */   }
/*     */   
/*     */   public void update() {
/*  44 */     MutableBoolean visibilityChangeSoundPlayed = new MutableBoolean(false);
/*  45 */     this.visibleToasts.removeIf(toast -> {
/*     */           Toast.Visibility previousVisibility = visibilityChangeSoundPlayed.visibility;
/*     */           
/*     */           visibilityChangeSoundPlayed.update();
/*     */           
/*     */           if (visibilityChangeSoundPlayed.visibility != previousVisibility && visibilityChangeSoundPlayed.isFalse()) {
/*     */             visibilityChangeSoundPlayed.setTrue();
/*     */             
/*     */             visibilityChangeSoundPlayed.visibility.playSound(this.minecraft.getSoundManager());
/*     */           } 
/*     */           if (visibilityChangeSoundPlayed.hasFinishedRendering()) {
/*     */             this.occupiedSlots.clear(visibilityChangeSoundPlayed.firstSlotIndex, visibilityChangeSoundPlayed.firstSlotIndex + visibilityChangeSoundPlayed.occupiedSlotCount);
/*     */             return true;
/*     */           } 
/*     */           return false;
/*     */         });
/*  61 */     if (!this.queued.isEmpty() && freeSlotCount() > 0) {
/*  62 */       this.queued.removeIf(toast -> {
/*     */             int occcupiedSlotCount = toast.occcupiedSlotCount(), firstSlotIndex = findFreeSlotsIndex(occcupiedSlotCount);
/*     */             
/*     */             if (firstSlotIndex == -1) {
/*     */               return false;
/*     */             }
/*     */             
/*     */             this.visibleToasts.add(new ToastInstance(toast, firstSlotIndex, occcupiedSlotCount));
/*     */             this.occupiedSlots.set(firstSlotIndex, firstSlotIndex + occcupiedSlotCount);
/*     */             SoundEvent toastSound = toast.getSoundEvent();
/*     */             if (toastSound != null && this.playedToastSounds.add(toastSound)) {
/*     */               this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI(toastSound, 1.0F, 1.0F));
/*     */             }
/*     */             return true;
/*     */           });
/*     */     }
/*  78 */     this.playedToastSounds.clear();
/*  79 */     if (this.nowPlayingToast != null) {
/*  80 */       this.nowPlayingToast.update();
/*     */     }
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics) {
/*  85 */     if (this.minecraft.options.hideGui) {
/*     */       return;
/*     */     }
/*  88 */     int screenWidth = graphics.guiWidth();
/*  89 */     if (!this.visibleToasts.isEmpty()) {
/*  90 */       graphics.nextStratum();
/*     */     }
/*  92 */     for (ToastInstance<?> toast : this.visibleToasts) {
/*  93 */       toast.render(graphics, screenWidth);
/*     */     }
/*  95 */     if (((MusicToastDisplayState)this.minecraft.options.musicToast().get()).renderToast() && this.nowPlayingToast != null && (this.minecraft.screen == null || !(this.minecraft.screen instanceof net.minecraft.client.gui.screens.PauseScreen))) {
/*  96 */       this.nowPlayingToast.render(graphics, screenWidth);
/*     */     }
/*     */   }
/*     */   
/*     */   private int findFreeSlotsIndex(int requiredCount) {
/* 101 */     if (freeSlotCount() >= requiredCount) {
/* 102 */       int consecutiveFreeSlotCount = 0;
/* 103 */       for (int i = 0; i < 5; i++) {
/* 104 */         if (this.occupiedSlots.get(i)) {
/* 105 */           consecutiveFreeSlotCount = 0;
/* 106 */         } else if (++consecutiveFreeSlotCount == requiredCount) {
/* 107 */           return i + 1 - consecutiveFreeSlotCount;
/*     */         } 
/*     */       } 
/*     */     } 
/* 111 */     return -1;
/*     */   }
/*     */   
/*     */   private int freeSlotCount() {
/* 115 */     return 5 - this.occupiedSlots.cardinality();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Toast> T getToast(Class<? extends T> clazz, Object token) {
/* 120 */     for (ToastInstance<?> instance : this.visibleToasts) {
/* 121 */       if (clazz.isAssignableFrom(instance.getToast().getClass()) && instance.getToast().getToken().equals(token)) {
/* 122 */         return (T)instance.getToast();
/*     */       }
/*     */     } 
/* 125 */     for (Toast toast : this.queued) {
/* 126 */       if (clazz.isAssignableFrom(toast.getClass()) && toast.getToken().equals(token)) {
/* 127 */         return (T)toast;
/*     */       }
/*     */     } 
/* 130 */     return null;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 134 */     this.occupiedSlots.clear();
/* 135 */     this.visibleToasts.clear();
/* 136 */     this.queued.clear();
/*     */   }
/*     */   
/*     */   public void addToast(Toast toast) {
/* 140 */     this.queued.add(toast);
/*     */   }
/*     */   
/*     */   public void showNowPlayingToast() {
/* 144 */     if (this.nowPlayingToast != null) {
/* 145 */       this.nowPlayingToast.resetToast();
/* 146 */       ((NowPlayingToast)this.nowPlayingToast.getToast()).showToast(this.minecraft.options);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hideNowPlayingToast() {
/* 151 */     if (this.nowPlayingToast != null) {
/* 152 */       ((NowPlayingToast)this.nowPlayingToast.getToast()).setWantedVisibility(Toast.Visibility.HIDE);
/*     */     }
/*     */   }
/*     */   
/*     */   public Minecraft getMinecraft() {
/* 157 */     return this.minecraft;
/*     */   }
/*     */   
/*     */   public double getNotificationDisplayTimeMultiplier() {
/* 161 */     return (Double)this.minecraft.options.notificationDisplayTime().get();
/*     */   }
/*     */   
/*     */   private void initializeMusicToast(MusicToastDisplayState state) {
/* 165 */     switch (state) { case PAUSE: case PAUSE_AND_TOAST:
/* 166 */         this.nowPlayingToast = new ToastInstance<>(new NowPlayingToast(), 0, 0);
/*     */         break; }
/*     */   
/*     */   }
/*     */   public void setMusicToastDisplayState(MusicToastDisplayState state) {
/* 171 */     switch (state) { case NEVER:
/* 172 */         this.nowPlayingToast = null; break;
/* 173 */       case PAUSE: this.nowPlayingToast = new ToastInstance<>(new NowPlayingToast(), 0, 0); break;
/*     */       case PAUSE_AND_TOAST:
/* 175 */         this.nowPlayingToast = new ToastInstance<>(new NowPlayingToast(), 0, 0);
/* 176 */         if (this.minecraft.options.getFinalSoundSourceVolume(SoundSource.MUSIC) > 0.0F) {
/* 177 */           ((NowPlayingToast)this.nowPlayingToast.getToast()).showToast(this.minecraft.options);
/*     */         }
/*     */         break; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private class ToastInstance<T extends Toast>
/*     */   {
/*     */     private static final long SLIDE_ANIMATION_DURATION_MS = 600L;
/*     */     private final T toast;
/*     */     private final int firstSlotIndex;
/*     */     private final int occupiedSlotCount;
/*     */     private long animationStartTime;
/*     */     private long becameFullyVisibleAt;
/*     */     private Toast.Visibility visibility;
/*     */     private long fullyVisibleFor;
/*     */     private float visiblePortion;
/*     */     protected boolean hasFinishedRendering;
/*     */     
/*     */     private ToastInstance(T toast, int firstSlotIndex, int occupiedSlotCount) {
/* 198 */       this.toast = toast;
/* 199 */       this.firstSlotIndex = firstSlotIndex;
/* 200 */       this.occupiedSlotCount = occupiedSlotCount;
/* 201 */       resetToast();
/*     */     }
/*     */     
/*     */     public T getToast() {
/* 205 */       return this.toast;
/*     */     }
/*     */     
/*     */     public void resetToast() {
/* 209 */       this.animationStartTime = -1L;
/* 210 */       this.becameFullyVisibleAt = -1L;
/* 211 */       this.visibility = Toast.Visibility.HIDE;
/* 212 */       this.fullyVisibleFor = 0L;
/* 213 */       this.visiblePortion = 0.0F;
/* 214 */       this.hasFinishedRendering = false;
/*     */     }
/*     */     
/*     */     public boolean hasFinishedRendering() {
/* 218 */       return this.hasFinishedRendering;
/*     */     }
/*     */     
/*     */     private void calculateVisiblePortion(long now) {
/* 222 */       float animationProgress = Mth.clamp((float)(now - this.animationStartTime) / 600.0F, 0.0F, 1.0F);
/* 223 */       animationProgress *= animationProgress;
/* 224 */       if (this.visibility == Toast.Visibility.HIDE) {
/* 225 */         this.visiblePortion = 1.0F - animationProgress;
/*     */       } else {
/* 227 */         this.visiblePortion = animationProgress;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void update() {
/* 232 */       long now = Util.getMillis();
/*     */       
/* 234 */       if (this.animationStartTime == -1L) {
/* 235 */         this.animationStartTime = now;
/* 236 */         this.visibility = Toast.Visibility.SHOW;
/*     */       } 
/*     */       
/* 239 */       if (this.visibility == Toast.Visibility.SHOW && now - this.animationStartTime <= 600L) {
/* 240 */         this.becameFullyVisibleAt = now;
/*     */       }
/*     */       
/* 243 */       this.fullyVisibleFor = now - this.becameFullyVisibleAt;
/* 244 */       calculateVisiblePortion(now);
/*     */       
/* 246 */       this.toast.update(ToastManager.this, this.fullyVisibleFor);
/*     */       
/* 248 */       Toast.Visibility wantedVisibility = this.toast.getWantedVisibility();
/*     */       
/* 250 */       if (wantedVisibility != this.visibility) {
/*     */         
/* 252 */         this.animationStartTime = now - (int)((1.0F - this.visiblePortion) * 600.0F);
/* 253 */         this.visibility = wantedVisibility;
/*     */       } 
/* 255 */       boolean wasAlreadyFinishedRendering = this.hasFinishedRendering;
/* 256 */       this.hasFinishedRendering = (this.visibility == Toast.Visibility.HIDE && now - this.animationStartTime > 600L);
/* 257 */       if (this.hasFinishedRendering && !wasAlreadyFinishedRendering) {
/* 258 */         this.toast.onFinishedRendering();
/*     */       }
/*     */     }
/*     */     
/*     */     public void render(GuiGraphics graphics, int screenWidth) {
/* 263 */       if (this.hasFinishedRendering) {
/*     */         return;
/*     */       }
/* 266 */       graphics.pose().pushMatrix();
/* 267 */       graphics.pose().translate(this.toast.xPos(screenWidth, this.visiblePortion), this.toast.yPos(this.firstSlotIndex));
/* 268 */       this.toast.render(graphics, ToastManager.this.minecraft.font, this.fullyVisibleFor);
/* 269 */       graphics.pose().popMatrix();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/ToastManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */