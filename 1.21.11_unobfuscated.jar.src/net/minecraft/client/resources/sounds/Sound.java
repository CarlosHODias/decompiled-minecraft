/*     */ package net.minecraft.client.resources.sounds;
/*     */ 
/*     */ import net.minecraft.client.sounds.SoundEngine;
/*     */ import net.minecraft.client.sounds.Weighted;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.SampledFloat;
/*     */ 
/*     */ public class Sound
/*     */   implements Weighted<Sound> {
/*  12 */   public static final FileToIdConverter SOUND_LISTER = new FileToIdConverter("sounds", ".ogg");
/*     */   
/*     */   private final Identifier location;
/*     */   private final SampledFloat volume;
/*     */   private final SampledFloat pitch;
/*     */   private final int weight;
/*     */   private final Type type;
/*     */   private final boolean stream;
/*     */   private final boolean preload;
/*     */   private final int attenuationDistance;
/*     */   
/*     */   public Sound(Identifier location, SampledFloat volume, SampledFloat pitch, int weight, Type type, boolean stream, boolean preload, int attenuationDistance) {
/*  24 */     this.location = location;
/*  25 */     this.volume = volume;
/*  26 */     this.pitch = pitch;
/*  27 */     this.weight = weight;
/*  28 */     this.type = type;
/*  29 */     this.stream = stream;
/*  30 */     this.preload = preload;
/*  31 */     this.attenuationDistance = attenuationDistance;
/*     */   }
/*     */   
/*     */   public Identifier getLocation() {
/*  35 */     return this.location;
/*     */   }
/*     */   
/*     */   public Identifier getPath() {
/*  39 */     return SOUND_LISTER.idToFile(this.location);
/*     */   }
/*     */   
/*     */   public SampledFloat getVolume() {
/*  43 */     return this.volume;
/*     */   }
/*     */   
/*     */   public SampledFloat getPitch() {
/*  47 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeight() {
/*  52 */     return this.weight;
/*     */   }
/*     */ 
/*     */   
/*     */   public Sound getSound(RandomSource random) {
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void preloadIfRequired(SoundEngine soundEngine) {
/*  62 */     if (this.preload) {
/*  63 */       soundEngine.requestPreload(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  68 */     return this.type;
/*     */   }
/*     */   
/*     */   public boolean shouldStream() {
/*  72 */     return this.stream;
/*     */   }
/*     */   
/*     */   public boolean shouldPreload() {
/*  76 */     return this.preload;
/*     */   }
/*     */   
/*     */   public int getAttenuationDistance() {
/*  80 */     return this.attenuationDistance;
/*     */   }
/*     */   
/*     */   public enum Type {
/*  84 */     FILE("file"),
/*  85 */     SOUND_EVENT("event");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Type(String name) {
/*  90 */       this.name = name;
/*     */     }
/*     */     
/*     */     public static Type getByName(String name) {
/*  94 */       for (Type type : values()) {
/*  95 */         if (type.name.equals(name)) {
/*  96 */           return type;
/*     */         }
/*     */       } 
/*  99 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "Sound[" + String.valueOf(this.location) + "]";
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/Sound.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */