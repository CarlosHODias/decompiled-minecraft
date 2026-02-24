/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.mojang.blaze3d.audio.ListenerTransform;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.client.resources.sounds.SoundEventRegistration;
/*     */ import net.minecraft.client.resources.sounds.SoundEventRegistrationSerializer;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.client.resources.sounds.TickableSoundInstance;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.MultipliedFloats;
/*     */ import net.minecraft.util.valueproviders.SampledFloat;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SoundManager
/*     */   extends SimplePreparableReloadListener<SoundManager.Preparations> {
/*  42 */   public static final Identifier EMPTY_SOUND_LOCATION = Identifier.withDefaultNamespace("empty");
/*  43 */   public static final Sound EMPTY_SOUND = new Sound(EMPTY_SOUND_LOCATION, (SampledFloat)ConstantFloat.of(1.0F), (SampledFloat)ConstantFloat.of(1.0F), 1, Sound.Type.FILE, false, false, 16);
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final Identifier INTENTIONALLY_EMPTY_SOUND_LOCATION = Identifier.withDefaultNamespace("intentionally_empty");
/*  48 */   public static final WeighedSoundEvents INTENTIONALLY_EMPTY_SOUND_EVENT = new WeighedSoundEvents(INTENTIONALLY_EMPTY_SOUND_LOCATION, null);
/*  49 */   public static final Sound INTENTIONALLY_EMPTY_SOUND = new Sound(INTENTIONALLY_EMPTY_SOUND_LOCATION, (SampledFloat)ConstantFloat.of(1.0F), (SampledFloat)ConstantFloat.of(1.0F), 1, Sound.Type.FILE, false, false, 16);
/*  50 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String SOUNDS_PATH = "sounds.json";
/*  52 */   private static final Gson GSON = new GsonBuilder()
/*  53 */     .registerTypeAdapter(SoundEventRegistration.class, new SoundEventRegistrationSerializer())
/*  54 */     .create();
/*     */   
/*  56 */   private static final TypeToken<Map<String, SoundEventRegistration>> SOUND_EVENT_REGISTRATION_TYPE = new TypeToken<Map<String, SoundEventRegistration>>() {  }
/*  57 */   ; private final Map<Identifier, WeighedSoundEvents> registry = Maps.newHashMap();
/*     */   private final SoundEngine soundEngine;
/*  59 */   private final Map<Identifier, Resource> soundCache = new HashMap<>();
/*     */   
/*     */   public SoundManager(Options options) {
/*  62 */     this.soundEngine = new SoundEngine(this, options, ResourceProvider.fromMap(this.soundCache));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Preparations prepare(ResourceManager manager, ProfilerFiller profiler) {
/*  67 */     Preparations preparations = new Preparations();
/*     */     
/*  69 */     Zone ignored = profiler.zone("list"); 
/*  70 */     try { preparations.listResources(manager);
/*  71 */       if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*     */         try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  73 */      for (String namespace : (Iterable<String>)manager.getNamespaces()) { 
/*  74 */       try { Zone zone = profiler.zone(namespace); 
/*  75 */         try { List<Resource> resources = manager.getResourceStack(Identifier.fromNamespaceAndPath(namespace, "sounds.json"));
/*  76 */           for (Resource resource : resources) {
/*  77 */             profiler.push(resource.sourcePackId()); 
/*  78 */             try { Reader reader = resource.openAsReader(); 
/*  79 */               try { profiler.push("parse");
/*  80 */                 Map<String, SoundEventRegistration> map = (Map<String, SoundEventRegistration>)GsonHelper.fromJson(GSON, reader, SOUND_EVENT_REGISTRATION_TYPE);
/*  81 */                 profiler.popPush("register");
/*  82 */                 for (Map.Entry<String, SoundEventRegistration> entry : map.entrySet()) {
/*  83 */                   preparations.handleRegistration(Identifier.fromNamespaceAndPath(namespace, entry.getKey()), entry.getValue());
/*     */                 }
/*  85 */                 profiler.pop();
/*  86 */                 if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (RuntimeException e)
/*  87 */             { LOGGER.warn("Invalid {} in resourcepack: '{}'", new Object[] { "sounds.json", resource.sourcePackId(), e }); }
/*     */             
/*  89 */             profiler.pop();
/*     */           } 
/*  91 */           if (zone != null) zone.close();  } catch (Throwable throwable) { if (zone != null) try { zone.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {} }
/*     */ 
/*     */ 
/*     */     
/*  95 */     return preparations;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void apply(Preparations preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 100 */     preparations.apply(this.registry, this.soundCache, this.soundEngine);
/*     */ 
/*     */     
/* 103 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 104 */       for (Identifier location : this.registry.keySet()) {
/* 105 */         WeighedSoundEvents event = this.registry.get(location);
/*     */         
/* 107 */         if (!ComponentUtils.isTranslationResolvable(event.getSubtitle()) && BuiltInRegistries.SOUND_EVENT.containsKey(location)) {
/* 108 */           LOGGER.error("Missing subtitle {} for sound event: {}", event.getSubtitle(), location);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 114 */     if (LOGGER.isDebugEnabled()) {
/* 115 */       for (Identifier location : this.registry.keySet()) {
/* 116 */         if (!BuiltInRegistries.SOUND_EVENT.containsKey(location)) {
/* 117 */           LOGGER.debug("Not having sound event for: {}", location);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 122 */     this.soundEngine.reload();
/*     */   }
/*     */   
/*     */   public List<String> getAvailableSoundDevices() {
/* 126 */     return this.soundEngine.getAvailableSoundDevices();
/*     */   }
/*     */   
/*     */   public ListenerTransform getListenerTransform() {
/* 130 */     return this.soundEngine.getListenerTransform();
/*     */   }
/*     */   
/*     */   protected static class Preparations {
/* 134 */     private final Map<Identifier, WeighedSoundEvents> registry = Maps.newHashMap();
/* 135 */     private Map<Identifier, Resource> soundCache = Map.of();
/*     */     
/*     */     private void listResources(ResourceManager resourceManager) {
/* 138 */       this.soundCache = Sound.SOUND_LISTER.listMatchingResources(resourceManager);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleRegistration(Identifier eventLocation, SoundEventRegistration soundEventRegistration) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield registry : Ljava/util/Map;
/*     */       //   4: aload_1
/*     */       //   5: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   10: checkcast net/minecraft/client/sounds/WeighedSoundEvents
/*     */       //   13: astore_3
/*     */       //   14: aload_3
/*     */       //   15: ifnonnull -> 22
/*     */       //   18: iconst_1
/*     */       //   19: goto -> 23
/*     */       //   22: iconst_0
/*     */       //   23: istore #4
/*     */       //   25: iload #4
/*     */       //   27: ifne -> 37
/*     */       //   30: aload_2
/*     */       //   31: invokevirtual isReplace : ()Z
/*     */       //   34: ifeq -> 78
/*     */       //   37: iload #4
/*     */       //   39: ifne -> 53
/*     */       //   42: getstatic net/minecraft/client/sounds/SoundManager.LOGGER : Lorg/slf4j/Logger;
/*     */       //   45: ldc 'Replaced sound event location {}'
/*     */       //   47: aload_1
/*     */       //   48: invokeinterface debug : (Ljava/lang/String;Ljava/lang/Object;)V
/*     */       //   53: new net/minecraft/client/sounds/WeighedSoundEvents
/*     */       //   56: dup
/*     */       //   57: aload_1
/*     */       //   58: aload_2
/*     */       //   59: invokevirtual getSubtitle : ()Ljava/lang/String;
/*     */       //   62: invokespecial <init> : (Lnet/minecraft/resources/Identifier;Ljava/lang/String;)V
/*     */       //   65: astore_3
/*     */       //   66: aload_0
/*     */       //   67: getfield registry : Ljava/util/Map;
/*     */       //   70: aload_1
/*     */       //   71: aload_3
/*     */       //   72: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   77: pop
/*     */       //   78: aload_0
/*     */       //   79: getfield soundCache : Ljava/util/Map;
/*     */       //   82: invokestatic fromMap : (Ljava/util/Map;)Lnet/minecraft/server/packs/resources/ResourceProvider;
/*     */       //   85: astore #5
/*     */       //   87: aload_2
/*     */       //   88: invokevirtual getSounds : ()Ljava/util/List;
/*     */       //   91: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   96: astore #6
/*     */       //   98: aload #6
/*     */       //   100: invokeinterface hasNext : ()Z
/*     */       //   105: ifeq -> 232
/*     */       //   108: aload #6
/*     */       //   110: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   115: checkcast net/minecraft/client/resources/sounds/Sound
/*     */       //   118: astore #7
/*     */       //   120: aload #7
/*     */       //   122: invokevirtual getLocation : ()Lnet/minecraft/resources/Identifier;
/*     */       //   125: astore #8
/*     */       //   127: getstatic net/minecraft/client/sounds/SoundManager$2.$SwitchMap$net$minecraft$client$resources$sounds$Sound$Type : [I
/*     */       //   130: aload #7
/*     */       //   132: invokevirtual getType : ()Lnet/minecraft/client/resources/sounds/Sound$Type;
/*     */       //   135: invokevirtual ordinal : ()I
/*     */       //   138: iaload
/*     */       //   139: lookupswitch default -> 202, 1 -> 164, 2 -> 185
/*     */       //   164: aload #7
/*     */       //   166: aload_1
/*     */       //   167: aload #5
/*     */       //   169: invokestatic validateSoundResource : (Lnet/minecraft/client/resources/sounds/Sound;Lnet/minecraft/resources/Identifier;Lnet/minecraft/server/packs/resources/ResourceProvider;)Z
/*     */       //   172: ifne -> 178
/*     */       //   175: goto -> 98
/*     */       //   178: aload #7
/*     */       //   180: astore #9
/*     */       //   182: goto -> 223
/*     */       //   185: new net/minecraft/client/sounds/SoundManager$Preparations$1
/*     */       //   188: dup
/*     */       //   189: aload_0
/*     */       //   190: aload #8
/*     */       //   192: aload #7
/*     */       //   194: invokespecial <init> : (Lnet/minecraft/client/sounds/SoundManager$Preparations;Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/resources/sounds/Sound;)V
/*     */       //   197: astore #9
/*     */       //   199: goto -> 223
/*     */       //   202: new java/lang/IllegalStateException
/*     */       //   205: dup
/*     */       //   206: aload #7
/*     */       //   208: invokevirtual getType : ()Lnet/minecraft/client/resources/sounds/Sound$Type;
/*     */       //   211: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */       //   214: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */       //   219: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   222: athrow
/*     */       //   223: aload_3
/*     */       //   224: aload #9
/*     */       //   226: invokevirtual addSound : (Lnet/minecraft/client/sounds/Weighted;)V
/*     */       //   229: goto -> 98
/*     */       //   232: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       //   #143	-> 14
/*     */       //   #144	-> 25
/*     */       //   #145	-> 37
/*     */       //   #146	-> 42
/*     */       //   #148	-> 53
/*     */       //   #149	-> 66
/*     */       //   #152	-> 78
/*     */       //   #154	-> 87
/*     */       //   #155	-> 120
/*     */       //   #158	-> 127
/*     */       //   #160	-> 164
/*     */       //   #161	-> 175
/*     */       //   #164	-> 178
/*     */       //   #165	-> 182
/*     */       //   #167	-> 185
/*     */       //   #204	-> 199
/*     */       //   #206	-> 202
/*     */       //   #209	-> 223
/*     */       //   #210	-> 229
/*     */       //   #211	-> 232
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   182	3	9	weighted	Lnet/minecraft/client/sounds/Weighted;
/*     */       //   199	3	9	weighted	Lnet/minecraft/client/sounds/Weighted;
/*     */       //   127	102	8	soundLocation	Lnet/minecraft/resources/Identifier;
/*     */       //   223	6	9	weighted	Lnet/minecraft/client/sounds/Weighted;
/*     */       //   120	109	7	sound	Lnet/minecraft/client/resources/sounds/Sound;
/*     */       //   0	233	0	this	Lnet/minecraft/client/sounds/SoundManager$Preparations;
/*     */       //   0	233	1	eventLocation	Lnet/minecraft/resources/Identifier;
/*     */       //   0	233	2	soundEventRegistration	Lnet/minecraft/client/resources/sounds/SoundEventRegistration;
/*     */       //   14	219	3	registration	Lnet/minecraft/client/sounds/WeighedSoundEvents;
/*     */       //   25	208	4	missesRegistration	Z
/*     */       //   87	146	5	cachedProvider	Lnet/minecraft/server/packs/resources/ResourceProvider;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   182	3	9	weighted	Lnet/minecraft/client/sounds/Weighted<Lnet/minecraft/client/resources/sounds/Sound;>;
/*     */       //   199	3	9	weighted	Lnet/minecraft/client/sounds/Weighted<Lnet/minecraft/client/resources/sounds/Sound;>;
/*     */       //   223	6	9	weighted	Lnet/minecraft/client/sounds/Weighted<Lnet/minecraft/client/resources/sounds/Sound;>;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void apply(Map<Identifier, WeighedSoundEvents> registry, Map<Identifier, Resource> soundCache, SoundEngine engine) {
/* 214 */       registry.clear();
/* 215 */       soundCache.clear();
/*     */       
/* 217 */       soundCache.putAll(this.soundCache);
/*     */       
/* 219 */       for (Map.Entry<Identifier, WeighedSoundEvents> entry : this.registry.entrySet()) {
/* 220 */         registry.put(entry.getKey(), entry.getValue());
/* 221 */         ((WeighedSoundEvents)entry.getValue()).preloadIfRequired(engine);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean validateSoundResource(Sound sound, Identifier eventLocation, ResourceProvider resourceProvider) {
/* 227 */     Identifier soundPath = sound.getPath();
/* 228 */     if (resourceProvider.getResource(soundPath).isEmpty()) {
/* 229 */       LOGGER.warn("File {} does not exist, cannot add it to event {}", soundPath, eventLocation);
/* 230 */       return false;
/*     */     } 
/* 232 */     return true;
/*     */   }
/*     */   class null implements Weighted<Sound> {
/*     */     public int getWeight() { WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation); return (registration == null) ? 0 : registration.getWeight(); }
/* 236 */     public Sound getSound(RandomSource random) { WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation); if (registration == null) return SoundManager.EMPTY_SOUND;  Sound wrappedSound = registration.getSound(random); return new Sound(wrappedSound.getLocation(), (SampledFloat)new MultipliedFloats(new SampledFloat[] { wrappedSound.getVolume(), sound.getVolume() }, ), (SampledFloat)new MultipliedFloats(new SampledFloat[] { wrappedSound.getPitch(), sound.getPitch() }, ), sound.getWeight(), Sound.Type.FILE, (wrappedSound.shouldStream() || sound.shouldStream()), wrappedSound.shouldPreload(), wrappedSound.getAttenuationDistance()); } public void preloadIfRequired(SoundEngine soundEngine) { WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation); if (registration == null) return;  registration.preloadIfRequired(soundEngine); } } public WeighedSoundEvents getSoundEvent(Identifier location) { return this.registry.get(location); }
/*     */ 
/*     */   
/*     */   public Collection<Identifier> getAvailableSounds() {
/* 240 */     return this.registry.keySet();
/*     */   }
/*     */   
/*     */   public void queueTickingSound(TickableSoundInstance instance) {
/* 244 */     this.soundEngine.queueTickingSound(instance);
/*     */   }
/*     */   
/*     */   public SoundEngine.PlayResult play(SoundInstance instance) {
/* 248 */     return this.soundEngine.play(instance);
/*     */   }
/*     */   
/*     */   public void playDelayed(SoundInstance instance, int delay) {
/* 252 */     this.soundEngine.playDelayed(instance, delay);
/*     */   }
/*     */   
/*     */   public void updateSource(Camera camera) {
/* 256 */     this.soundEngine.updateSource(camera);
/*     */   }
/*     */   
/*     */   public void pauseAllExcept(SoundSource... ignoredSources) {
/* 260 */     this.soundEngine.pauseAllExcept(ignoredSources);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 264 */     this.soundEngine.stopAll();
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 268 */     this.soundEngine.destroy();
/*     */   }
/*     */   
/*     */   public void emergencyShutdown() {
/* 272 */     this.soundEngine.emergencyShutdown();
/*     */   }
/*     */   
/*     */   public void tick(boolean paused) {
/* 276 */     this.soundEngine.tick(paused);
/*     */   }
/*     */   
/*     */   public void resume() {
/* 280 */     this.soundEngine.resume();
/*     */   }
/*     */   
/*     */   public void refreshCategoryVolume(SoundSource category) {
/* 284 */     this.soundEngine.refreshCategoryVolume(category);
/*     */   }
/*     */   
/*     */   public void stop(SoundInstance soundInstance) {
/* 288 */     this.soundEngine.stop(soundInstance);
/*     */   }
/*     */   
/*     */   public void updateCategoryVolume(SoundSource source, float gain) {
/* 292 */     this.soundEngine.updateCategoryVolume(source, gain);
/*     */   }
/*     */   
/*     */   public boolean isActive(SoundInstance instance) {
/* 296 */     return this.soundEngine.isActive(instance);
/*     */   }
/*     */   
/*     */   public void addListener(SoundEventListener listener) {
/* 300 */     this.soundEngine.addEventListener(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(SoundEventListener listener) {
/* 304 */     this.soundEngine.removeEventListener(listener);
/*     */   }
/*     */   
/*     */   public void stop(Identifier sound, SoundSource source) {
/* 308 */     this.soundEngine.stop(sound, source);
/*     */   }
/*     */   
/*     */   public String getDebugString() {
/* 312 */     return this.soundEngine.getDebugString();
/*     */   }
/*     */   
/*     */   public void reload() {
/* 316 */     this.soundEngine.reload();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/SoundManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */