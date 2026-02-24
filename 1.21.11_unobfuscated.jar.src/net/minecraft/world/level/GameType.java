/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.player.Abilities;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public enum GameType
/*     */   implements StringRepresentable
/*     */ {
/*  18 */   SURVIVAL(0, "survival"),
/*  19 */   CREATIVE(1, "creative"),
/*  20 */   ADVENTURE(2, "adventure"),
/*  21 */   SPECTATOR(3, "spectator");
/*     */ 
/*     */   
/*  24 */   public static final GameType DEFAULT_MODE = SURVIVAL;
/*     */   
/*  26 */   public static final StringRepresentable.EnumCodec<GameType> CODEC = StringRepresentable.fromEnum(GameType::values);
/*     */   
/*  28 */   private static final IntFunction<GameType> BY_ID = ByIdMap.continuous(GameType::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  29 */   public static final StreamCodec<ByteBuf, GameType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, GameType::getId);
/*     */   
/*     */   @Deprecated
/*  32 */   public static final Codec<GameType> LEGACY_ID_CODEC = Codec.INT.xmap(GameType::byId, GameType::getId);
/*     */   
/*     */   private static final int NOT_SET = -1;
/*     */   
/*     */   private final int id;
/*     */   private final String name;
/*     */   private final Component shortName;
/*     */   private final Component longName;
/*     */   
/*     */   GameType(int id, String name) {
/*  42 */     this.id = id;
/*  43 */     this.name = name;
/*  44 */     this.shortName = (Component)Component.translatable("selectWorld.gameMode." + name);
/*  45 */     this.longName = (Component)Component.translatable("gameMode." + name);
/*     */   }
/*     */   
/*     */   public int getId() {
/*  49 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/*  58 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component getLongDisplayName() {
/*  62 */     return this.longName;
/*     */   }
/*     */   
/*     */   public Component getShortDisplayName() {
/*  66 */     return this.shortName;
/*     */   }
/*     */   
/*     */   public void updatePlayerAbilities(Abilities abilities) {
/*  70 */     if (this == CREATIVE) {
/*  71 */       abilities.mayfly = true;
/*  72 */       abilities.instabuild = true;
/*  73 */       abilities.invulnerable = true;
/*  74 */     } else if (this == SPECTATOR) {
/*  75 */       abilities.mayfly = true;
/*  76 */       abilities.instabuild = false;
/*  77 */       abilities.invulnerable = true;
/*  78 */       abilities.flying = true;
/*     */     } else {
/*  80 */       abilities.mayfly = false;
/*  81 */       abilities.instabuild = false;
/*  82 */       abilities.invulnerable = false;
/*  83 */       abilities.flying = false;
/*     */     } 
/*  85 */     abilities.mayBuild = !isBlockPlacingRestricted();
/*     */   }
/*     */   
/*     */   public boolean isBlockPlacingRestricted() {
/*  89 */     return (this == ADVENTURE || this == SPECTATOR);
/*     */   }
/*     */   
/*     */   public boolean isCreative() {
/*  93 */     return (this == CREATIVE);
/*     */   }
/*     */   
/*     */   public boolean isSurvival() {
/*  97 */     return (this == SURVIVAL || this == ADVENTURE);
/*     */   }
/*     */   
/*     */   public static GameType byId(int id) {
/* 101 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public static GameType byName(String name) {
/* 105 */     return byName(name, SURVIVAL);
/*     */   }
/*     */   
/*     */   @Contract("_,!null->!null;_,null->_")
/*     */   public static GameType byName(String name, GameType defaultMode) {
/* 110 */     GameType result = (GameType)CODEC.byName(name);
/* 111 */     return (result != null) ? result : defaultMode;
/*     */   }
/*     */   
/*     */   public static int getNullableId(GameType gameType) {
/* 115 */     return (gameType != null) ? gameType.id : -1;
/*     */   }
/*     */   
/*     */   public static GameType byNullableId(int id) {
/* 119 */     if (id == -1) {
/* 120 */       return null;
/*     */     }
/* 122 */     return byId(id);
/*     */   }
/*     */   
/*     */   public static boolean isValidId(int id) {
/* 126 */     return Arrays.<GameType>stream(values())
/* 127 */       .anyMatch(gameType -> (gameType.id == id));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/GameType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */