/*     */ package net.minecraft.world.scores.criteria;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public class ObjectiveCriteria {
/*  18 */   private static final Map<String, ObjectiveCriteria> CUSTOM_CRITERIA = Maps.newHashMap();
/*  19 */   private static final Map<String, ObjectiveCriteria> CRITERIA_CACHE = Maps.newHashMap();
/*     */   static {
/*  21 */     CODEC = Codec.STRING.comapFlatMap(name -> (DataResult)byName(name).<DataResult>map(DataResult::success).orElse(DataResult.error(())), ObjectiveCriteria::getName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<ObjectiveCriteria> CODEC;
/*     */   
/*  28 */   public static final ObjectiveCriteria DUMMY = registerCustom("dummy");
/*  29 */   public static final ObjectiveCriteria TRIGGER = registerCustom("trigger");
/*  30 */   public static final ObjectiveCriteria DEATH_COUNT = registerCustom("deathCount");
/*  31 */   public static final ObjectiveCriteria KILL_COUNT_PLAYERS = registerCustom("playerKillCount");
/*  32 */   public static final ObjectiveCriteria KILL_COUNT_ALL = registerCustom("totalKillCount");
/*  33 */   public static final ObjectiveCriteria HEALTH = registerCustom("health", true, RenderType.HEARTS);
/*  34 */   public static final ObjectiveCriteria FOOD = registerCustom("food", true, RenderType.INTEGER);
/*  35 */   public static final ObjectiveCriteria AIR = registerCustom("air", true, RenderType.INTEGER);
/*  36 */   public static final ObjectiveCriteria ARMOR = registerCustom("armor", true, RenderType.INTEGER);
/*  37 */   public static final ObjectiveCriteria EXPERIENCE = registerCustom("xp", true, RenderType.INTEGER);
/*  38 */   public static final ObjectiveCriteria LEVEL = registerCustom("level", true, RenderType.INTEGER);
/*  39 */   public static final ObjectiveCriteria[] TEAM_KILL = new ObjectiveCriteria[] { 
/*  40 */       registerCustom("teamkill." + ChatFormatting.BLACK.getName()), registerCustom("teamkill." + ChatFormatting.DARK_BLUE.getName()), 
/*  41 */       registerCustom("teamkill." + ChatFormatting.DARK_GREEN.getName()), registerCustom("teamkill." + ChatFormatting.DARK_AQUA.getName()), 
/*  42 */       registerCustom("teamkill." + ChatFormatting.DARK_RED.getName()), registerCustom("teamkill." + ChatFormatting.DARK_PURPLE.getName()), 
/*  43 */       registerCustom("teamkill." + ChatFormatting.GOLD.getName()), registerCustom("teamkill." + ChatFormatting.GRAY.getName()), 
/*  44 */       registerCustom("teamkill." + ChatFormatting.DARK_GRAY.getName()), registerCustom("teamkill." + ChatFormatting.BLUE.getName()), 
/*  45 */       registerCustom("teamkill." + ChatFormatting.GREEN.getName()), registerCustom("teamkill." + ChatFormatting.AQUA.getName()), 
/*  46 */       registerCustom("teamkill." + ChatFormatting.RED.getName()), registerCustom("teamkill." + ChatFormatting.LIGHT_PURPLE.getName()), 
/*  47 */       registerCustom("teamkill." + ChatFormatting.YELLOW.getName()), registerCustom("teamkill." + ChatFormatting.WHITE.getName()) };
/*     */   
/*  49 */   public static final ObjectiveCriteria[] KILLED_BY_TEAM = new ObjectiveCriteria[] { 
/*  50 */       registerCustom("killedByTeam." + ChatFormatting.BLACK.getName()), registerCustom("killedByTeam." + ChatFormatting.DARK_BLUE.getName()), 
/*  51 */       registerCustom("killedByTeam." + ChatFormatting.DARK_GREEN.getName()), registerCustom("killedByTeam." + ChatFormatting.DARK_AQUA.getName()), 
/*  52 */       registerCustom("killedByTeam." + ChatFormatting.DARK_RED.getName()), registerCustom("killedByTeam." + ChatFormatting.DARK_PURPLE.getName()), 
/*  53 */       registerCustom("killedByTeam." + ChatFormatting.GOLD.getName()), registerCustom("killedByTeam." + ChatFormatting.GRAY.getName()), 
/*  54 */       registerCustom("killedByTeam." + ChatFormatting.DARK_GRAY.getName()), registerCustom("killedByTeam." + ChatFormatting.BLUE.getName()), 
/*  55 */       registerCustom("killedByTeam." + ChatFormatting.GREEN.getName()), registerCustom("killedByTeam." + ChatFormatting.AQUA.getName()), 
/*  56 */       registerCustom("killedByTeam." + ChatFormatting.RED.getName()), registerCustom("killedByTeam." + ChatFormatting.LIGHT_PURPLE.getName()), 
/*  57 */       registerCustom("killedByTeam." + ChatFormatting.YELLOW.getName()), registerCustom("killedByTeam." + ChatFormatting.WHITE.getName()) };
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean readOnly;
/*     */   private final RenderType renderType;
/*     */   
/*     */   private static ObjectiveCriteria registerCustom(String name, boolean readOnly, RenderType renderType) {
/*  65 */     ObjectiveCriteria result = new ObjectiveCriteria(name, readOnly, renderType);
/*  66 */     CUSTOM_CRITERIA.put(name, result);
/*  67 */     return result;
/*     */   }
/*     */   
/*     */   private static ObjectiveCriteria registerCustom(String name) {
/*  71 */     return registerCustom(name, false, RenderType.INTEGER);
/*     */   }
/*     */   
/*     */   protected ObjectiveCriteria(String name) {
/*  75 */     this(name, false, RenderType.INTEGER);
/*     */   }
/*     */   
/*     */   protected ObjectiveCriteria(String name, boolean readOnly, RenderType renderType) {
/*  79 */     this.name = name;
/*  80 */     this.readOnly = readOnly;
/*  81 */     this.renderType = renderType;
/*  82 */     CRITERIA_CACHE.put(name, this);
/*     */   }
/*     */   
/*     */   public static Set<String> getCustomCriteriaNames() {
/*  86 */     return (Set<String>)ImmutableSet.copyOf(CUSTOM_CRITERIA.keySet());
/*     */   }
/*     */   
/*     */   public static Optional<ObjectiveCriteria> byName(String name) {
/*  90 */     ObjectiveCriteria value = CRITERIA_CACHE.get(name);
/*  91 */     if (value != null) {
/*  92 */       return Optional.of(value);
/*     */     }
/*  94 */     int colonPos = name.indexOf(':');
/*  95 */     if (colonPos < 0) {
/*  96 */       return Optional.empty();
/*     */     }
/*  98 */     return BuiltInRegistries.STAT_TYPE.getOptional(Identifier.bySeparator(name.substring(0, colonPos), '.'))
/*  99 */       .flatMap(statType -> getStat(statType, Identifier.bySeparator(name.substring(colonPos + 1), '.')));
/*     */   }
/*     */   
/*     */   private static <T> Optional<ObjectiveCriteria> getStat(StatType<T> statType, Identifier key) {
/* 103 */     Objects.requireNonNull(statType); return statType.getRegistry().getOptional(key).map(statType::get);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 107 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() {
/* 111 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public RenderType getDefaultRenderType() {
/* 115 */     return this.renderType;
/*     */   }
/*     */   
/*     */   public enum RenderType implements StringRepresentable {
/* 119 */     INTEGER("integer"),
/* 120 */     HEARTS("hearts");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RenderType(String id) {
/* 126 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 130 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 135 */       return this.id;
/*     */     }
/*     */     
/* 138 */     public static final StringRepresentable.EnumCodec<RenderType> CODEC = StringRepresentable.fromEnum(RenderType::values); private final String id;
/*     */     
/*     */     public static RenderType byId(String key) {
/* 141 */       return (RenderType)CODEC.byName(key, INTEGER);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/scores/criteria/ObjectiveCriteria.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */