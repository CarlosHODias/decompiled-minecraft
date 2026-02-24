/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public abstract class BossEvent
/*     */ {
/*     */   private final UUID id;
/*     */   protected Component name;
/*     */   protected float progress;
/*     */   protected BossBarColor color;
/*     */   protected BossBarOverlay overlay;
/*     */   protected boolean darkenScreen;
/*     */   protected boolean playBossMusic;
/*     */   protected boolean createWorldFog;
/*     */   
/*     */   public BossEvent(UUID id, Component name, BossBarColor color, BossBarOverlay overlay) {
/*  21 */     this.id = id;
/*  22 */     this.name = name;
/*  23 */     this.color = color;
/*  24 */     this.overlay = overlay;
/*  25 */     this.progress = 1.0F;
/*     */   }
/*     */   
/*     */   public UUID getId() {
/*  29 */     return this.id;
/*     */   }
/*     */   
/*     */   public Component getName() {
/*  33 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(Component name) {
/*  37 */     this.name = name;
/*     */   }
/*     */   
/*     */   public float getProgress() {
/*  41 */     return this.progress;
/*     */   }
/*     */   
/*     */   public void setProgress(float progress) {
/*  45 */     this.progress = progress;
/*     */   }
/*     */   
/*     */   public BossBarColor getColor() {
/*  49 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(BossBarColor color) {
/*  53 */     this.color = color;
/*     */   }
/*     */   
/*     */   public BossBarOverlay getOverlay() {
/*  57 */     return this.overlay;
/*     */   }
/*     */   
/*     */   public void setOverlay(BossBarOverlay overlay) {
/*  61 */     this.overlay = overlay;
/*     */   }
/*     */   
/*     */   public boolean shouldDarkenScreen() {
/*  65 */     return this.darkenScreen;
/*     */   }
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean darkenScreen) {
/*  69 */     this.darkenScreen = darkenScreen;
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldPlayBossMusic() {
/*  74 */     return this.playBossMusic;
/*     */   }
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean playBossMusic) {
/*  78 */     this.playBossMusic = playBossMusic;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean createWorldFog) {
/*  83 */     this.createWorldFog = createWorldFog;
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldCreateWorldFog() {
/*  88 */     return this.createWorldFog;
/*     */   }
/*     */   
/*     */   public enum BossBarColor implements StringRepresentable {
/*  92 */     PINK("pink", ChatFormatting.RED),
/*  93 */     BLUE("blue", ChatFormatting.BLUE),
/*  94 */     RED("red", ChatFormatting.DARK_RED),
/*  95 */     GREEN("green", ChatFormatting.GREEN),
/*  96 */     YELLOW("yellow", ChatFormatting.YELLOW),
/*  97 */     PURPLE("purple", ChatFormatting.DARK_BLUE),
/*  98 */     WHITE("white", ChatFormatting.WHITE);
/*     */ 
/*     */     
/* 101 */     public static final Codec<BossBarColor> CODEC = (Codec<BossBarColor>)StringRepresentable.fromEnum(BossBarColor::values);
/*     */     
/*     */     private final String name;
/*     */     private final ChatFormatting formatting;
/*     */     
/*     */     BossBarColor(String name, ChatFormatting formatting) {
/* 107 */       this.name = name;
/* 108 */       this.formatting = formatting;
/*     */     }
/*     */     
/*     */     public ChatFormatting getFormatting() {
/* 112 */       return this.formatting;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 116 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 121 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BossBarOverlay implements StringRepresentable {
/* 126 */     PROGRESS("progress"),
/* 127 */     NOTCHED_6("notched_6"),
/* 128 */     NOTCHED_10("notched_10"),
/* 129 */     NOTCHED_12("notched_12"),
/* 130 */     NOTCHED_20("notched_20");
/*     */ 
/*     */     
/* 133 */     public static final Codec<BossBarOverlay> CODEC = (Codec<BossBarOverlay>)StringRepresentable.fromEnum(BossBarOverlay::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     BossBarOverlay(String name) {
/* 138 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 142 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 147 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/BossEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */