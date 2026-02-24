/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*    */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*    */ import net.minecraft.world.attribute.modifier.FloatWithAlpha;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.timeline.Timelines;
/*    */ 
/*    */ public class WeatherAttributes
/*    */ {
/* 15 */   public static final EnvironmentAttributeMap RAIN = EnvironmentAttributeMap.builder()
/* 16 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.6F, 0.75F))
/* 17 */     .<Integer, Integer>modify(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_RGB, ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.6F))
/* 18 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.5F))
/* 19 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.3125F))
/* 20 */     .<Integer, Integer>modify(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.ALPHA_BLEND, ARGB.color(0.3125F, Timelines.NIGHT_SKY_LIGHT_COLOR))
/* 21 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.3125F))
/* 22 */     .<Float>set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.0F)
/* 23 */     .<Integer, Integer>modify(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_ARGB, ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.6F))
/* 24 */     .<Boolean>set(EnvironmentAttributes.BEES_STAY_IN_HIVE, true)
/* 25 */     .build();
/*    */   
/* 27 */   public static final EnvironmentAttributeMap THUNDER = EnvironmentAttributeMap.builder()
/* 28 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.94F))
/* 29 */     .<Integer, Integer>modify(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_RGB, ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.3F))
/* 30 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.095F, 0.94F))
/* 31 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.52734375F))
/* 32 */     .<Integer, Integer>modify(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.ALPHA_BLEND, ARGB.color(0.52734375F, Timelines.NIGHT_SKY_LIGHT_COLOR))
/* 33 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.52734375F))
/* 34 */     .<Float>set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.0F)
/* 35 */     .<Integer, Integer>modify(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_ARGB, ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.3F))
/* 36 */     .<Boolean>set(EnvironmentAttributes.BEES_STAY_IN_HIVE, true)
/* 37 */     .build();
/*    */   
/* 39 */   private static final Set<EnvironmentAttribute<?>> WEATHER_ATTRIBUTES = (Set<EnvironmentAttribute<?>>)Sets.union(RAIN.keySet(), THUNDER.keySet());
/*    */   
/*    */   public static void addBuiltinLayers(EnvironmentAttributeSystem.Builder system, WeatherAccess weatherAccess) {
/* 42 */     for (EnvironmentAttribute<?> attribute : WEATHER_ATTRIBUTES) {
/* 43 */       addLayer(system, weatherAccess, attribute);
/*    */     }
/*    */   }
/*    */   
/*    */   private static <Value> void addLayer(EnvironmentAttributeSystem.Builder system, WeatherAccess weatherAccess, EnvironmentAttribute<Value> attribute) {
/* 48 */     EnvironmentAttributeMap.Entry<Value, ?> rainEntry = RAIN.get(attribute);
/* 49 */     EnvironmentAttributeMap.Entry<Value, ?> thunderEntry = THUNDER.get(attribute);
/* 50 */     system.addTimeBasedLayer(attribute, (result, cacheTickId) -> {
/*    */           float thunderLevel = weatherAccess.thunderLevel(), rainLevel = weatherAccess.rainLevel() - thunderLevel;
/*    */           if (rainEntry != null && rainLevel > 0.0F) {
/*    */             Value rainValue = (Value)rainEntry.applyModifier(result);
/*    */             result = attribute.type().stateChangeLerp().apply(rainLevel, result, rainValue);
/*    */           } 
/*    */           if (thunderEntry != null && thunderLevel > 0.0F) {
/*    */             Value thunderValue = (Value)thunderEntry.applyModifier(result);
/*    */             result = attribute.type().stateChangeLerp().apply(thunderLevel, result, thunderValue);
/*    */           } 
/*    */           return result;
/*    */         });
/*    */   }
/*    */   
/*    */   public static interface WeatherAccess
/*    */   {
/*    */     static WeatherAccess from(final Level level) {
/* 67 */       return new WeatherAccess()
/*    */         {
/*    */           public float rainLevel() {
/* 70 */             return level.getRainLevel(1.0F);
/*    */           }
/*    */           
/*    */           public float thunderLevel()
/*    */           {
/* 75 */             return level.getThunderLevel(1.0F); } }; } float rainLevel(); float thunderLevel(); } class null implements WeatherAccess { public float thunderLevel() { return level.getThunderLevel(1.0F); }
/*    */ 
/*    */     
/*    */     public float rainLevel() {
/*    */       return level.getRainLevel(1.0F);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/WeatherAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */