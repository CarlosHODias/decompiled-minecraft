/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SelectItemModelProperties {
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, SelectItemModelProperty.Type<?, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*  9 */   public static final Codec<SelectItemModelProperty.Type<?, ?>> CODEC = ID_MAPPER.codec(Identifier.CODEC);
/*    */   
/*    */   public static void bootstrap() {
/* 12 */     ID_MAPPER.put(Identifier.withDefaultNamespace("custom_model_data"), CustomModelDataProperty.TYPE);
/* 13 */     ID_MAPPER.put(Identifier.withDefaultNamespace("main_hand"), MainHand.TYPE);
/* 14 */     ID_MAPPER.put(Identifier.withDefaultNamespace("charge_type"), Charge.TYPE);
/* 15 */     ID_MAPPER.put(Identifier.withDefaultNamespace("trim_material"), TrimMaterialProperty.TYPE);
/* 16 */     ID_MAPPER.put(Identifier.withDefaultNamespace("block_state"), ItemBlockState.TYPE);
/* 17 */     ID_MAPPER.put(Identifier.withDefaultNamespace("display_context"), DisplayContext.TYPE);
/* 18 */     ID_MAPPER.put(Identifier.withDefaultNamespace("local_time"), LocalTime.TYPE);
/* 19 */     ID_MAPPER.put(Identifier.withDefaultNamespace("context_entity_type"), ContextEntityType.TYPE);
/* 20 */     ID_MAPPER.put(Identifier.withDefaultNamespace("context_dimension"), ContextDimension.TYPE);
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("component"), ComponentContents.castType());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/SelectItemModelProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */