/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class RangeSelectItemModelProperties {
/*    */   public static final MapCodec<RangeSelectItemModelProperty> MAP_CODEC;
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends RangeSelectItemModelProperty>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   static {
/* 10 */     MAP_CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatchMap("property", RangeSelectItemModelProperty::type, c -> c);
/*    */   }
/*    */   public static void bootstrap() {
/* 13 */     ID_MAPPER.put(Identifier.withDefaultNamespace("custom_model_data"), CustomModelDataProperty.MAP_CODEC);
/* 14 */     ID_MAPPER.put(Identifier.withDefaultNamespace("bundle/fullness"), BundleFullness.MAP_CODEC);
/* 15 */     ID_MAPPER.put(Identifier.withDefaultNamespace("damage"), Damage.MAP_CODEC);
/* 16 */     ID_MAPPER.put(Identifier.withDefaultNamespace("cooldown"), Cooldown.MAP_CODEC);
/* 17 */     ID_MAPPER.put(Identifier.withDefaultNamespace("time"), Time.MAP_CODEC);
/* 18 */     ID_MAPPER.put(Identifier.withDefaultNamespace("compass"), CompassAngle.MAP_CODEC);
/* 19 */     ID_MAPPER.put(Identifier.withDefaultNamespace("crossbow/pull"), CrossbowPull.MAP_CODEC);
/* 20 */     ID_MAPPER.put(Identifier.withDefaultNamespace("use_cycle"), UseCycle.MAP_CODEC);
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("use_duration"), UseDuration.MAP_CODEC);
/* 22 */     ID_MAPPER.put(Identifier.withDefaultNamespace("count"), Count.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/RangeSelectItemModelProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */