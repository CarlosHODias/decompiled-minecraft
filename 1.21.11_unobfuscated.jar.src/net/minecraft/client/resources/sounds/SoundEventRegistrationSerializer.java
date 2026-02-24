/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.valueproviders.ConstantFloat;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.SampledFloat;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class SoundEventRegistrationSerializer implements JsonDeserializer<SoundEventRegistration> {
/* 21 */   private static final FloatProvider DEFAULT_FLOAT = (FloatProvider)ConstantFloat.of(1.0F);
/*    */ 
/*    */   
/*    */   public SoundEventRegistration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 25 */     JsonObject object = GsonHelper.convertToJsonObject(json, "entry");
/*    */     
/* 27 */     boolean replace = GsonHelper.getAsBoolean(object, "replace", false);
/* 28 */     String subtitle = GsonHelper.getAsString(object, "subtitle", null);
/* 29 */     List<Sound> sounds = getSounds(object);
/*    */     
/* 31 */     return new SoundEventRegistration(sounds, replace, subtitle);
/*    */   }
/*    */   
/*    */   private List<Sound> getSounds(JsonObject object) {
/* 35 */     List<Sound> result = Lists.newArrayList();
/*    */     
/* 37 */     if (object.has("sounds")) {
/* 38 */       JsonArray array = GsonHelper.getAsJsonArray(object, "sounds");
/* 39 */       for (int i = 0; i < array.size(); i++) {
/* 40 */         JsonElement element = array.get(i);
/*    */         
/* 42 */         if (GsonHelper.isStringValue(element)) {
/* 43 */           Identifier name = Identifier.parse(GsonHelper.convertToString(element, "sound"));
/* 44 */           result.add(new Sound(name, (SampledFloat)DEFAULT_FLOAT, (SampledFloat)DEFAULT_FLOAT, 1, Sound.Type.FILE, false, false, 16));
/*    */         } else {
/* 46 */           result.add(getSound(GsonHelper.convertToJsonObject(element, "sound")));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 51 */     return result;
/*    */   }
/*    */   
/*    */   private Sound getSound(JsonObject object) {
/* 55 */     Identifier name = Identifier.parse(GsonHelper.getAsString(object, "name"));
/*    */     
/* 57 */     Sound.Type type = getType(object, Sound.Type.FILE);
/*    */     
/* 59 */     float volume = GsonHelper.getAsFloat(object, "volume", 1.0F);
/* 60 */     Validate.isTrue((volume > 0.0F), "Invalid volume", new Object[0]);
/*    */     
/* 62 */     float pitch = GsonHelper.getAsFloat(object, "pitch", 1.0F);
/* 63 */     Validate.isTrue((pitch > 0.0F), "Invalid pitch", new Object[0]);
/*    */     
/* 65 */     int weight = GsonHelper.getAsInt(object, "weight", 1);
/* 66 */     Validate.isTrue((weight > 0), "Invalid weight", new Object[0]);
/*    */     
/* 68 */     boolean preload = GsonHelper.getAsBoolean(object, "preload", false);
/*    */     
/* 70 */     boolean stream = GsonHelper.getAsBoolean(object, "stream", false);
/*    */     
/* 72 */     int attenuationDistance = GsonHelper.getAsInt(object, "attenuation_distance", 16);
/*    */     
/* 74 */     return new Sound(name, (SampledFloat)ConstantFloat.of(volume), (SampledFloat)ConstantFloat.of(pitch), weight, type, stream, preload, attenuationDistance);
/*    */   }
/*    */   
/*    */   private Sound.Type getType(JsonObject sound, Sound.Type fallback) {
/* 78 */     Sound.Type type = fallback;
/* 79 */     if (sound.has("type")) {
/* 80 */       type = Sound.Type.getByName(GsonHelper.getAsString(sound, "type"));
/* 81 */       Objects.requireNonNull(type, "Invalid type");
/*    */     } 
/* 83 */     return type;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/SoundEventRegistrationSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */