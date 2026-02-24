/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ConditionalItemModelProperties {
/*    */   public static final MapCodec<ConditionalItemModelProperty> MAP_CODEC;
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ConditionalItemModelProperty>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   static {
/* 10 */     MAP_CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatchMap("property", ConditionalItemModelProperty::type, c -> c);
/*    */   }
/*    */   public static void bootstrap() {
/* 13 */     ID_MAPPER.put(Identifier.withDefaultNamespace("custom_model_data"), CustomModelDataProperty.MAP_CODEC);
/* 14 */     ID_MAPPER.put(Identifier.withDefaultNamespace("using_item"), IsUsingItem.MAP_CODEC);
/* 15 */     ID_MAPPER.put(Identifier.withDefaultNamespace("broken"), Broken.MAP_CODEC);
/* 16 */     ID_MAPPER.put(Identifier.withDefaultNamespace("damaged"), Damaged.MAP_CODEC);
/* 17 */     ID_MAPPER.put(Identifier.withDefaultNamespace("fishing_rod/cast"), FishingRodCast.MAP_CODEC);
/* 18 */     ID_MAPPER.put(Identifier.withDefaultNamespace("has_component"), HasComponent.MAP_CODEC);
/* 19 */     ID_MAPPER.put(Identifier.withDefaultNamespace("bundle/has_selected_item"), BundleHasSelectedItem.MAP_CODEC);
/* 20 */     ID_MAPPER.put(Identifier.withDefaultNamespace("selected"), IsSelected.MAP_CODEC);
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("carried"), IsCarried.MAP_CODEC);
/* 22 */     ID_MAPPER.put(Identifier.withDefaultNamespace("extended_view"), ExtendedView.MAP_CODEC);
/* 23 */     ID_MAPPER.put(Identifier.withDefaultNamespace("keybind_down"), IsKeybindDown.MAP_CODEC);
/* 24 */     ID_MAPPER.put(Identifier.withDefaultNamespace("view_entity"), IsViewEntity.MAP_CODEC);
/* 25 */     ID_MAPPER.put(Identifier.withDefaultNamespace("component"), ComponentMatches.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/conditional/ConditionalItemModelProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */