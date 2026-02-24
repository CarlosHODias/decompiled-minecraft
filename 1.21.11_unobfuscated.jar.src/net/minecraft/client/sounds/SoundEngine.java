/*     */ package net.minecraft.client.sounds;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.audio.Channel;
/*     */ import com.mojang.blaze3d.audio.Library;
/*     */ import com.mojang.blaze3d.audio.Listener;
/*     */ import com.mojang.blaze3d.audio.ListenerTransform;
/*     */ import com.mojang.blaze3d.audio.SoundBuffer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.client.resources.sounds.TickableSoundInstance;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.MarkerFactory;
/*     */ 
/*     */ public class SoundEngine {
/*  43 */   private static final Marker MARKER = MarkerFactory.getMarker("SOUNDS");
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final float PITCH_MIN = 0.5F;
/*     */   private static final float PITCH_MAX = 2.0F;
/*     */   private static final float VOLUME_MIN = 0.0F;
/*     */   private static final float VOLUME_MAX = 1.0F;
/*     */   private static final int MIN_SOURCE_LIFETIME = 20;
/*  50 */   private static final Set<Identifier> ONLY_WARN_ONCE = Sets.newHashSet(); private static final long DEFAULT_DEVICE_CHECK_INTERVAL_MS = 1000L;
/*     */   public static final String MISSING_SOUND = "FOR THE DEBUG!";
/*     */   public static final String OPEN_AL_SOFT_PREFIX = "OpenAL Soft on ";
/*     */   
/*  54 */   private enum DeviceCheckState { ONGOING, CHANGE_DETECTED, NO_CHANGE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final int OPEN_AL_SOFT_PREFIX_LENGTH = "OpenAL Soft on ".length();
/*     */   
/*     */   private final SoundManager soundManager;
/*     */   
/*     */   private final Options options;
/*     */   
/*     */   private boolean loaded;
/*  67 */   private final Library library = new Library();
/*  68 */   private final Listener listener = this.library.getListener();
/*     */   
/*     */   private final SoundBufferLibrary soundBuffers;
/*  71 */   private final SoundEngineExecutor executor = new SoundEngineExecutor();
/*     */   
/*  73 */   private final ChannelAccess channelAccess = new ChannelAccess(this.library, (Executor)this.executor);
/*     */   
/*     */   private int tickCount;
/*     */   private long lastDeviceCheckTime;
/*  77 */   private final AtomicReference<DeviceCheckState> devicePoolState = new AtomicReference<>(DeviceCheckState.NO_CHANGE);
/*     */   
/*  79 */   private final Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel = Maps.newHashMap();
/*  80 */   private final Multimap<SoundSource, SoundInstance> instanceBySource = (Multimap<SoundSource, SoundInstance>)HashMultimap.create(); private final Object2FloatMap<SoundSource> gainBySource; private final List<TickableSoundInstance> tickingSounds; private final Map<SoundInstance, Integer> queuedSounds; public SoundEngine(SoundManager soundManager, Options options, ResourceProvider resourceProvider) {
/*  81 */     this.gainBySource = (Object2FloatMap<SoundSource>)Util.make(new Object2FloatOpenHashMap(), map -> map.defaultReturnValue(1.0F));
/*     */     
/*  83 */     this.tickingSounds = Lists.newArrayList();
/*  84 */     this.queuedSounds = Maps.newHashMap();
/*  85 */     this.soundDeleteTime = Maps.newHashMap();
/*  86 */     this.listeners = Lists.newArrayList();
/*  87 */     this.queuedTickableSounds = Lists.newArrayList();
/*     */     
/*  89 */     this.preloadQueue = Lists.newArrayList();
/*     */ 
/*     */     
/*  92 */     this.soundManager = soundManager;
/*  93 */     this.options = options;
/*  94 */     this.soundBuffers = new SoundBufferLibrary(resourceProvider);
/*     */   }
/*     */   private final Map<SoundInstance, Integer> soundDeleteTime; private final List<SoundEventListener> listeners; private final List<TickableSoundInstance> queuedTickableSounds; private final List<Sound> preloadQueue;
/*     */   public void reload() {
/*  98 */     ONLY_WARN_ONCE.clear();
/*  99 */     for (SoundEvent sound : (Iterable<SoundEvent>)BuiltInRegistries.SOUND_EVENT) {
/* 100 */       if (sound != SoundEvents.EMPTY) {
/* 101 */         Identifier location = sound.location();
/* 102 */         if (this.soundManager.getSoundEvent(location) == null) {
/* 103 */           LOGGER.warn("Missing sound for event: {}", BuiltInRegistries.SOUND_EVENT.getKey(sound));
/* 104 */           ONLY_WARN_ONCE.add(location);
/*     */         } 
/*     */       } 
/*     */     } 
/* 108 */     destroy();
/* 109 */     loadLibrary();
/*     */   }
/*     */   
/*     */   private synchronized void loadLibrary() {
/* 113 */     if (this.loaded) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 118 */       String soundDevice = (String)this.options.soundDevice().get();
/* 119 */       this.library.init("".equals(soundDevice) ? null : soundDevice, (Boolean)this.options.directionalAudio().get());
/* 120 */       this.listener.reset();
/* 121 */       java.util.Objects.requireNonNull(this.preloadQueue); this.soundBuffers.preload(this.preloadQueue).thenRun(this.preloadQueue::clear);
/* 122 */       this.loaded = true;
/* 123 */       LOGGER.info(MARKER, "Sound engine started");
/* 124 */     } catch (RuntimeException e) {
/* 125 */       LOGGER.error(MARKER, "Error starting SoundSystem. Turning off sounds & music", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refreshCategoryVolume(SoundSource source) {
/* 130 */     if (!this.loaded) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     this.instanceToChannel.forEach((soundInstance, channelHandle) -> {
/*     */           if (source == source.getSource() || source == SoundSource.MASTER) {
/*     */             float newVolume = calculateVolume(source);
/*     */             channelHandle.execute(());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 143 */     if (this.loaded) {
/* 144 */       stopAll();
/* 145 */       this.soundBuffers.clear();
/* 146 */       this.library.cleanup();
/* 147 */       this.loaded = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void emergencyShutdown() {
/* 152 */     if (this.loaded) {
/* 153 */       this.library.cleanup();
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop(SoundInstance soundInstance) {
/* 158 */     if (this.loaded) {
/* 159 */       ChannelAccess.ChannelHandle handle = this.instanceToChannel.get(soundInstance);
/* 160 */       if (handle != null) {
/* 161 */         handle.execute(Channel::stop);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateCategoryVolume(SoundSource source, float gain) {
/* 167 */     this.gainBySource.put(source, Mth.clamp(gain, 0.0F, 1.0F));
/* 168 */     refreshCategoryVolume(source);
/*     */   }
/*     */   
/*     */   public void stopAll() {
/* 172 */     if (this.loaded) {
/* 173 */       this.executor.shutDown();
/* 174 */       this.instanceToChannel.clear();
/* 175 */       this.channelAccess.clear();
/* 176 */       this.queuedSounds.clear();
/* 177 */       this.tickingSounds.clear();
/* 178 */       this.instanceBySource.clear();
/* 179 */       this.soundDeleteTime.clear();
/* 180 */       this.queuedTickableSounds.clear();
/* 181 */       this.gainBySource.clear();
/* 182 */       this.executor.startUp();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addEventListener(SoundEventListener listener) {
/* 187 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeEventListener(SoundEventListener listener) {
/* 191 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldChangeDevice() {
/* 200 */     if (this.library.isCurrentDeviceDisconnected()) {
/* 201 */       LOGGER.info("Audio device was lost!");
/* 202 */       return true;
/*     */     } 
/*     */     
/* 205 */     long now = Util.getMillis();
/* 206 */     boolean doExpensiveChecks = (now - this.lastDeviceCheckTime >= 1000L);
/* 207 */     if (doExpensiveChecks) {
/* 208 */       this.lastDeviceCheckTime = now;
/*     */       
/* 210 */       if (this.devicePoolState.compareAndSet(DeviceCheckState.NO_CHANGE, DeviceCheckState.ONGOING)) {
/* 211 */         String currentDevice = (String)this.options.soundDevice().get();
/* 212 */         Util.ioPool().execute(() -> {
/*     */               if ("".equals(currentDevice)) {
/*     */                 if (this.library.hasDefaultDeviceChanged()) {
/*     */                   LOGGER.info("System default audio device has changed!");
/*     */                   
/*     */                   this.devicePoolState.compareAndSet(DeviceCheckState.ONGOING, DeviceCheckState.CHANGE_DETECTED);
/*     */                 } 
/*     */               } else if (!this.library.getCurrentDeviceName().equals(currentDevice) && this.library.getAvailableSoundDevices().contains(currentDevice)) {
/*     */                 LOGGER.info("Preferred audio device has become available!");
/*     */                 
/*     */                 this.devicePoolState.compareAndSet(DeviceCheckState.ONGOING, DeviceCheckState.CHANGE_DETECTED);
/*     */               } 
/*     */               
/*     */               this.devicePoolState.compareAndSet(DeviceCheckState.ONGOING, DeviceCheckState.NO_CHANGE);
/*     */             });
/*     */       } 
/*     */     } 
/* 229 */     return this.devicePoolState.compareAndSet(DeviceCheckState.CHANGE_DETECTED, DeviceCheckState.NO_CHANGE);
/*     */   }
/*     */   
/*     */   public void tick(boolean paused) {
/* 233 */     if (shouldChangeDevice()) {
/* 234 */       reload();
/*     */     }
/* 236 */     if (!paused) {
/* 237 */       tickInGameSound();
/*     */     } else {
/* 239 */       tickMusicWhenPaused();
/*     */     } 
/* 241 */     this.channelAccess.scheduleTick();
/*     */   }
/*     */   
/*     */   private void tickInGameSound() {
/* 245 */     this.tickCount++;
/*     */     
/* 247 */     this.queuedTickableSounds.stream().filter(SoundInstance::canPlaySound).forEach(this::play);
/* 248 */     this.queuedTickableSounds.clear();
/*     */ 
/*     */     
/* 251 */     for (Iterator<TickableSoundInstance> iterator1 = this.tickingSounds.iterator(); iterator1.hasNext(); ) { TickableSoundInstance instance = iterator1.next();
/* 252 */       if (!instance.canPlaySound()) {
/* 253 */         stop((SoundInstance)instance);
/*     */       }
/* 255 */       instance.tick();
/*     */       
/* 257 */       if (instance.isStopped()) {
/* 258 */         stop((SoundInstance)instance); continue;
/*     */       } 
/* 260 */       float volume = calculateVolume((SoundInstance)instance);
/* 261 */       float pitch = calculatePitch((SoundInstance)instance);
/* 262 */       Vec3 position = new Vec3(instance.getX(), instance.getY(), instance.getZ());
/* 263 */       ChannelAccess.ChannelHandle handle = this.instanceToChannel.get(instance);
/* 264 */       if (handle != null) {
/* 265 */         handle.execute(channel -> {
/*     */               channel.setVolume(volume);
/*     */               
/*     */               channel.setPitch(pitch);
/*     */               
/*     */               channel.setSelfPosition(position);
/*     */             });
/*     */       } }
/*     */ 
/*     */     
/* 275 */     Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator = this.instanceToChannel.entrySet().iterator();
/* 276 */     while (iterator.hasNext()) {
/* 277 */       Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry = iterator.next();
/*     */       
/* 279 */       ChannelAccess.ChannelHandle handle = entry.getValue();
/* 280 */       SoundInstance instance = entry.getKey();
/*     */       
/* 282 */       if (handle.isStopped()) {
/* 283 */         int minDeleteTime = (Integer)this.soundDeleteTime.get(instance);
/* 284 */         if (minDeleteTime <= this.tickCount) {
/* 285 */           if (shouldLoopManually(instance)) {
/* 286 */             this.queuedSounds.put(instance, this.tickCount + instance.getDelay());
/*     */           }
/* 288 */           iterator.remove();
/* 289 */           LOGGER.debug(MARKER, "Removed channel {} because it's not playing anymore", handle);
/* 290 */           this.soundDeleteTime.remove(instance);
/*     */           
/*     */           try {
/* 293 */             this.instanceBySource.remove(instance.getSource(), instance);
/* 294 */           } catch (RuntimeException runtimeException) {}
/*     */ 
/*     */ 
/*     */           
/* 298 */           if (instance instanceof TickableSoundInstance) {
/* 299 */             this.tickingSounds.remove(instance);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     Iterator<Map.Entry<SoundInstance, Integer>> queueIterator = this.queuedSounds.entrySet().iterator();
/* 306 */     while (queueIterator.hasNext()) {
/* 307 */       Map.Entry<SoundInstance, Integer> next = queueIterator.next();
/*     */       
/* 309 */       if (this.tickCount >= (Integer)next.getValue()) {
/* 310 */         SoundInstance instance = next.getKey();
/*     */ 
/*     */         
/* 313 */         if (instance instanceof TickableSoundInstance) {
/* 314 */           ((TickableSoundInstance)instance).tick();
/*     */         }
/*     */         
/* 317 */         play(instance);
/* 318 */         queueIterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void tickMusicWhenPaused() {
/* 325 */     Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator = this.instanceToChannel.entrySet().iterator();
/* 326 */     while (iterator.hasNext()) {
/* 327 */       Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry = iterator.next();
/* 328 */       ChannelAccess.ChannelHandle handle = entry.getValue();
/* 329 */       SoundInstance instance = entry.getKey();
/* 330 */       if (instance.getSource() == SoundSource.MUSIC && handle.isStopped()) {
/* 331 */         iterator.remove();
/* 332 */         LOGGER.debug(MARKER, "Removed channel {} because it's not playing anymore", handle);
/* 333 */         this.soundDeleteTime.remove(instance);
/* 334 */         this.instanceBySource.remove(instance.getSource(), instance);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean requiresManualLooping(SoundInstance instance) {
/* 340 */     return (instance.getDelay() > 0);
/*     */   }
/*     */   
/*     */   private static boolean shouldLoopManually(SoundInstance instance) {
/* 344 */     return (instance.isLooping() && requiresManualLooping(instance));
/*     */   }
/*     */   
/*     */   private static boolean shouldLoopAutomatically(SoundInstance instance) {
/* 348 */     return (instance.isLooping() && !requiresManualLooping(instance));
/*     */   }
/*     */   
/*     */   public boolean isActive(SoundInstance instance) {
/* 352 */     if (!this.loaded) {
/* 353 */       return false;
/*     */     }
/*     */     
/* 356 */     if (this.soundDeleteTime.containsKey(instance) && (Integer)this.soundDeleteTime.get(instance) <= this.tickCount) {
/* 357 */       return true;
/*     */     }
/*     */     
/* 360 */     return this.instanceToChannel.containsKey(instance);
/*     */   }
/*     */   
/*     */   public PlayResult play(SoundInstance instance) {
/* 364 */     if (!this.loaded) {
/* 365 */       return PlayResult.NOT_STARTED;
/*     */     }
/*     */     
/* 368 */     if (!instance.canPlaySound()) {
/* 369 */       return PlayResult.NOT_STARTED;
/*     */     }
/*     */     
/* 372 */     WeighedSoundEvents soundEvent = instance.resolve(this.soundManager);
/* 373 */     Identifier eventLocation = instance.getIdentifier();
/* 374 */     if (soundEvent == null) {
/* 375 */       if (ONLY_WARN_ONCE.add(eventLocation)) {
/* 376 */         LOGGER.warn(MARKER, "Unable to play unknown soundEvent: {}", eventLocation);
/*     */       }
/* 378 */       if (!SharedConstants.DEBUG_SUBTITLES) {
/* 379 */         return PlayResult.NOT_STARTED;
/*     */       }
/*     */       
/* 382 */       soundEvent = new WeighedSoundEvents(eventLocation, "FOR THE DEBUG!");
/*     */     } 
/*     */     
/* 385 */     Sound sound = instance.getSound();
/*     */     
/* 387 */     if (sound == SoundManager.INTENTIONALLY_EMPTY_SOUND) {
/* 388 */       return PlayResult.NOT_STARTED;
/*     */     }
/*     */     
/* 391 */     if (sound == SoundManager.EMPTY_SOUND) {
/* 392 */       if (ONLY_WARN_ONCE.add(eventLocation)) {
/* 393 */         LOGGER.warn(MARKER, "Unable to play empty soundEvent: {}", eventLocation);
/*     */       }
/* 395 */       return PlayResult.NOT_STARTED;
/*     */     } 
/*     */     
/* 398 */     float instanceVolume = instance.getVolume();
/* 399 */     float attenuationDistance = Math.max(instanceVolume, 1.0F) * sound.getAttenuationDistance();
/*     */     
/* 401 */     SoundSource soundSource = instance.getSource();
/* 402 */     float volume = calculateVolume(instanceVolume, soundSource);
/* 403 */     float pitch = calculatePitch(instance);
/*     */     
/* 405 */     SoundInstance.Attenuation attenuation = instance.getAttenuation();
/* 406 */     boolean isRelative = instance.isRelative();
/*     */     
/* 408 */     if (!this.listeners.isEmpty()) {
/* 409 */       float range = (isRelative || attenuation == SoundInstance.Attenuation.NONE) ? Float.POSITIVE_INFINITY : attenuationDistance;
/* 410 */       for (SoundEventListener listener : this.listeners) {
/* 411 */         listener.onPlaySound(instance, soundEvent, range);
/*     */       }
/*     */     } 
/*     */     
/*     */     boolean startedSilently = false;
/* 416 */     if (volume == 0.0F) {
/* 417 */       if (instance.canStartSilent() || soundSource == SoundSource.MUSIC) {
/* 418 */         startedSilently = true;
/*     */       } else {
/* 420 */         LOGGER.debug(MARKER, "Skipped playing sound {}, volume was zero.", sound.getLocation());
/* 421 */         return PlayResult.NOT_STARTED;
/*     */       } 
/*     */     }
/*     */     
/* 425 */     Vec3 position = new Vec3(instance.getX(), instance.getY(), instance.getZ());
/*     */ 
/*     */     
/* 428 */     boolean isLooping = shouldLoopAutomatically(instance);
/* 429 */     boolean isStreaming = sound.shouldStream();
/*     */     
/* 431 */     CompletableFuture<ChannelAccess.ChannelHandle> handleFuture = this.channelAccess.createHandle(sound.shouldStream() ? Library.Pool.STREAMING : Library.Pool.STATIC);
/* 432 */     ChannelAccess.ChannelHandle handle = handleFuture.join();
/* 433 */     if (handle == null) {
/* 434 */       if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 435 */         LOGGER.warn("Failed to create new sound handle");
/*     */       }
/* 437 */       return PlayResult.NOT_STARTED;
/*     */     } 
/*     */     
/* 440 */     LOGGER.debug(MARKER, "Playing sound {} for event {}", sound.getLocation(), eventLocation);
/*     */     
/* 442 */     this.soundDeleteTime.put(instance, this.tickCount + 20);
/* 443 */     this.instanceToChannel.put(instance, handle);
/* 444 */     this.instanceBySource.put(soundSource, instance);
/*     */     
/* 446 */     handle.execute(channel -> {
/*     */           channel.setPitch(pitch);
/*     */           
/*     */           channel.setVolume(volume);
/*     */           if (attenuation == SoundInstance.Attenuation.LINEAR) {
/*     */             channel.linearAttenuation(attenuationDistance);
/*     */           } else {
/*     */             channel.disableAttenuation();
/*     */           } 
/* 455 */           channel.setLooping((isLooping && !isStreaming));
/*     */           
/*     */           channel.setSelfPosition(position);
/*     */           channel.setRelative(isRelative);
/*     */         });
/* 460 */     if (!isStreaming) {
/* 461 */       this.soundBuffers.getCompleteBuffer(sound.getPath())
/* 462 */         .thenAccept(soundBuffer -> handle.execute(()));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 468 */       this.soundBuffers.getStream(sound.getPath(), isLooping)
/* 469 */         .thenAccept(stream -> handle.execute(()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 476 */     if (instance instanceof TickableSoundInstance) {
/* 477 */       this.tickingSounds.add((TickableSoundInstance)instance);
/*     */     }
/* 479 */     if (startedSilently) {
/* 480 */       return PlayResult.STARTED_SILENTLY;
/*     */     }
/* 482 */     return PlayResult.STARTED;
/*     */   }
/*     */   
/*     */   public void queueTickingSound(TickableSoundInstance tickableSoundInstance) {
/* 486 */     this.queuedTickableSounds.add(tickableSoundInstance);
/*     */   }
/*     */   
/*     */   public void requestPreload(Sound sound) {
/* 490 */     this.preloadQueue.add(sound);
/*     */   }
/*     */   
/*     */   private float calculatePitch(SoundInstance instance) {
/* 494 */     return Mth.clamp(instance.getPitch(), 0.5F, 2.0F);
/*     */   }
/*     */   
/*     */   private float calculateVolume(SoundInstance instance) {
/* 498 */     return calculateVolume(instance.getVolume(), instance.getSource());
/*     */   }
/*     */   
/*     */   private float calculateVolume(float volume, SoundSource source) {
/* 502 */     return Mth.clamp(volume, 0.0F, 1.0F) * Mth.clamp(this.options.getFinalSoundSourceVolume(source), 0.0F, 1.0F) * this.gainBySource.getFloat(source);
/*     */   }
/*     */   
/*     */   public void pauseAllExcept(SoundSource... ignoredSources) {
/* 506 */     if (!this.loaded) {
/*     */       return;
/*     */     }
/*     */     
/* 510 */     for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> instance : this.instanceToChannel.entrySet()) {
/* 511 */       if (!List.<SoundSource>of(ignoredSources).contains(((SoundInstance)instance.getKey()).getSource())) {
/* 512 */         ((ChannelAccess.ChannelHandle)instance.getValue()).execute(Channel::pause);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resume() {
/* 518 */     if (this.loaded) {
/* 519 */       this.channelAccess.executeOnChannels(channels -> channels.forEach(Channel::unpause));
/*     */     }
/*     */   }
/*     */   
/*     */   public void playDelayed(SoundInstance instance, int delay) {
/* 524 */     this.queuedSounds.put(instance, this.tickCount + delay);
/*     */   }
/*     */   
/*     */   public void updateSource(Camera camera) {
/* 528 */     if (!this.loaded || !camera.isInitialized()) {
/*     */       return;
/*     */     }
/*     */     
/* 532 */     ListenerTransform transform = new ListenerTransform(camera.position(), new Vec3(camera.forwardVector()), new Vec3(camera.upVector()));
/* 533 */     this.executor.execute(() -> this.listener.setTransform(transform));
/*     */   }
/*     */   
/*     */   public void stop(Identifier sound, SoundSource source) {
/* 537 */     if (source != null) {
/* 538 */       for (SoundInstance instance : (Iterable<SoundInstance>)this.instanceBySource.get(source)) {
/* 539 */         if (sound == null || instance.getIdentifier().equals(sound)) {
/* 540 */           stop(instance);
/*     */         }
/*     */       }
/*     */     
/* 544 */     } else if (sound == null) {
/* 545 */       stopAll();
/*     */     } else {
/* 547 */       for (SoundInstance instance : this.instanceToChannel.keySet()) {
/* 548 */         if (instance.getIdentifier().equals(sound)) {
/* 549 */           stop(instance);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugString() {
/* 557 */     return this.library.getDebugString();
/*     */   }
/*     */   
/*     */   public List<String> getAvailableSoundDevices() {
/* 561 */     return this.library.getAvailableSoundDevices();
/*     */   }
/*     */   
/*     */   public ListenerTransform getListenerTransform() {
/* 565 */     return this.listener.getTransform();
/*     */   }
/*     */   
/*     */   public enum PlayResult {
/* 569 */     STARTED,
/* 570 */     STARTED_SILENTLY,
/* 571 */     NOT_STARTED;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/SoundEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */