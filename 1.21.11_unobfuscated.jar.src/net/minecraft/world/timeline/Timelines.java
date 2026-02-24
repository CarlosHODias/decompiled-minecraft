/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.EasingType;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.attribute.modifier.BooleanModifier;
/*     */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*     */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ public interface Timelines {
/*  21 */   public static final ResourceKey<Timeline> DAY = key("day");
/*  22 */   public static final ResourceKey<Timeline> MOON = key("moon");
/*  23 */   public static final ResourceKey<Timeline> VILLAGER_SCHEDULE = key("villager_schedule");
/*  24 */   public static final ResourceKey<Timeline> EARLY_GAME = key("early_game");
/*     */   
/*     */   public static final float DAY_SKY_LIGHT_LEVEL = 15.0F;
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_LEVEL = 4.0F;
/*  29 */   public static final int NIGHT_SKY_LIGHT_COLOR = ARGB.colorFromFloat(1.0F, 0.48F, 0.48F, 1.0F);
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_FACTOR = 0.24F;
/*     */   public static final int NIGHT_SKY_COLOR_MULTIPLIER = -16777216;
/*  33 */   public static final int NIGHT_FOG_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.06F, 0.06F, 0.09F);
/*  34 */   public static final int NIGHT_CLOUD_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.1F, 0.1F, 0.15F);
/*     */ 
/*     */   
/*     */   static void bootstrap(BootstrapContext<Timeline> context) {
/*  38 */     EasingType skyAngleEase = EasingType.symmetricCubicBezier(0.362F, 0.241F);
/*     */     
/*  40 */     int nightStart = 12600;
/*  41 */     int nightEnd = 23401;
/*  42 */     int noon = 6000;
/*     */     
/*  44 */     context.register(DAY, Timeline.builder()
/*  45 */         .setPeriodTicks(24000)
/*  46 */         .addTrack(EnvironmentAttributes.SUN_ANGLE, track -> track.setEasing(skyAngleEase).addKeyframe(6000, 360.0F).addKeyframe(6000, 0.0F))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  51 */         .addTrack(EnvironmentAttributes.MOON_ANGLE, track -> track.setEasing(skyAngleEase).addKeyframe(6000, 540.0F).addKeyframe(6000, 180.0F))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  56 */         .addTrack(EnvironmentAttributes.STAR_ANGLE, track -> track.setEasing(skyAngleEase).addKeyframe(6000, 360.0F).addKeyframe(6000, 0.0F))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         .addModifierTrack(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12600, true).addKeyframe(23401, false))
/*     */ 
/*     */ 
/*     */         
/*  65 */         .addModifierTrack(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, track -> track.addKeyframe(133, -1).addKeyframe(11867, -1).addKeyframe(13670, NIGHT_FOG_COLOR_MULTIPLIER).addKeyframe(22330, NIGHT_FOG_COLOR_MULTIPLIER))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         .addModifierTrack(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, track -> track.addKeyframe(133, -1).addKeyframe(11867, -1).addKeyframe(13670, -16777216).addKeyframe(22330, -16777216))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  77 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, track -> track.addKeyframe(730, -1).addKeyframe(11270, -1).addKeyframe(13140, NIGHT_SKY_LIGHT_COLOR).addKeyframe(22860, NIGHT_SKY_LIGHT_COLOR))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  83 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<?, ?>)FloatModifier.MULTIPLY, track -> track.addKeyframe(730, 1.0F).addKeyframe(11270, 1.0F).addKeyframe(13140, 0.24F).addKeyframe(22860, 0.24F))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  89 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<?, ?>)FloatModifier.MULTIPLY, track -> track.addKeyframe(133, 1.0F).addKeyframe(11867, 1.0F).addKeyframe(13670, 0.26666668F).addKeyframe(22330, 0.26666668F))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  95 */         .addTrack(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, track -> track.addKeyframe(71, 1609540403).addKeyframe(310, 703969843).addKeyframe(565, 117167155).addKeyframe(730, 16770355).addKeyframe(11270, 16770355).addKeyframe(11397, 83679283).addKeyframe(11522, 268028723).addKeyframe(11690, 703969843).addKeyframe(11929, 1609540403).addKeyframe(12243, -1310226637).addKeyframe(12358, -857440717).addKeyframe(12512, -371166669).addKeyframe(12613, -153261261).addKeyframe(12732, -19242189).addKeyframe(12841, -19440589).addKeyframe(13035, -321760973).addKeyframe(13252, -1043577037).addKeyframe(13775, 918435635).addKeyframe(13888, 532362547).addKeyframe(14039, 163001139).addKeyframe(14192, 11744051).addKeyframe(21807, 11678515).addKeyframe(21961, 163001139).addKeyframe(22112, 532362547).addKeyframe(22225, 918435635).addKeyframe(22748, -1043577037).addKeyframe(22965, -321760973).addKeyframe(23159, -19440589).addKeyframe(23272, -19242189).addKeyframe(23488, -371166669).addKeyframe(23642, -857440717).addKeyframe(23757, -1310226637))
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
/* 129 */         .addModifierTrack(EnvironmentAttributes.STAR_BRIGHTNESS, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> track.addKeyframe(92, 0.037F).addKeyframe(627, 0.0F).addKeyframe(11373, 0.0F).addKeyframe(11732, 0.016F).addKeyframe(11959, 0.044F).addKeyframe(12399, 0.143F).addKeyframe(12729, 0.258F).addKeyframe(13228, 0.5F).addKeyframe(22772, 0.5F).addKeyframe(23032, 0.364F).addKeyframe(23356, 0.225F).addKeyframe(23758, 0.101F))
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
/* 143 */         .addModifierTrack(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_ARGB, track -> track.addKeyframe(133, -1).addKeyframe(11867, -1).addKeyframe(13670, NIGHT_CLOUD_COLOR_MULTIPLIER).addKeyframe(22330, NIGHT_CLOUD_COLOR_MULTIPLIER))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 149 */         .addTrack(EnvironmentAttributes.EYEBLOSSOM_OPEN, track -> track.addKeyframe(12600, TriState.TRUE).addKeyframe(23401, TriState.FALSE))
/*     */ 
/*     */ 
/*     */         
/* 153 */         .addModifierTrack(EnvironmentAttributes.CREAKING_ACTIVE, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12600, true).addKeyframe(23401, false))
/*     */ 
/*     */ 
/*     */         
/* 157 */         .addModifierTrack(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> track.setEasing(EasingType.CONSTANT).addKeyframe(21062, 1.0F).addKeyframe(21905, 0.002F))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 162 */         .addModifierTrack(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> track.setEasing(EasingType.CONSTANT).addKeyframe(362, 0.0F).addKeyframe(23667, 0.7F))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 167 */         .addModifierTrack(EnvironmentAttributes.BEES_STAY_IN_HIVE, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12542, true).addKeyframe(23460, false))
/*     */ 
/*     */ 
/*     */         
/* 171 */         .addModifierTrack(EnvironmentAttributes.MONSTERS_BURN, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12542, false).addKeyframe(23460, true))
/*     */ 
/*     */ 
/*     */         
/* 175 */         .build());
/*     */ 
/*     */     
/* 178 */     Timeline.Builder moonPhases = Timeline.builder()
/* 179 */       .setPeriodTicks(24000 * MoonPhase.COUNT)
/* 180 */       .addTrack(EnvironmentAttributes.MOON_PHASE, track -> {
/*     */           
/*     */           for (MoonPhase phase : MoonPhase.values()) {
/*     */             track.addKeyframe(phase.startTick(), phase);
/*     */           }
/* 185 */         }).addModifierTrack(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> {
/*     */           track.setEasing(EasingType.CONSTANT);
/*     */           
/*     */           for (MoonPhase phase : MoonPhase.values()) {
/*     */             track.addKeyframe(phase.startTick(), DimensionType.MOON_BRIGHTNESS_PER_PHASE[phase.index()] * 0.5F);
/*     */           }
/*     */         });
/* 192 */     context.register(MOON, moonPhases.build());
/*     */     
/* 194 */     int workStartTime = 2000;
/* 195 */     int totalWorkTime = 7000;
/*     */     
/* 197 */     context.register(VILLAGER_SCHEDULE, Timeline.builder()
/* 198 */         .setPeriodTicks(24000)
/* 199 */         .addTrack(EnvironmentAttributes.VILLAGER_ACTIVITY, track -> track.addKeyframe(10, Activity.IDLE).addKeyframe(2000, Activity.WORK).addKeyframe(9000, Activity.MEET).addKeyframe(11000, Activity.IDLE).addKeyframe(12000, Activity.REST))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 206 */         .addTrack(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY, track -> track.addKeyframe(10, Activity.IDLE).addKeyframe(3000, Activity.PLAY).addKeyframe(6000, Activity.IDLE).addKeyframe(10000, Activity.PLAY).addKeyframe(12000, Activity.REST))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 213 */         .build());
/*     */ 
/*     */     
/* 216 */     context.register(EARLY_GAME, Timeline.builder()
/* 217 */         .addModifierTrack(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, (AttributeModifier<?, ?>)BooleanModifier.AND, track -> track.addKeyframe(0, false).addKeyframe(120000, true))
/*     */ 
/*     */ 
/*     */         
/* 221 */         .build());
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceKey<Timeline> key(String id) {
/* 226 */     return ResourceKey.create(Registries.TIMELINE, Identifier.withDefaultNamespace(id));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/timeline/Timelines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */