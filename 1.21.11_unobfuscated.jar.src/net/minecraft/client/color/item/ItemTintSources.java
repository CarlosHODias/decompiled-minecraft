/*    */ package net.minecraft.client.color.item;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ItemTintSources {
/*    */   public static final Codec<ItemTintSource> CODEC;
/*  9 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemTintSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   static {
/* 11 */     CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(ItemTintSource::type, c -> c);
/*    */   }
/*    */   public static void bootstrap() {
/* 14 */     ID_MAPPER.put(Identifier.withDefaultNamespace("custom_model_data"), CustomModelDataSource.MAP_CODEC);
/* 15 */     ID_MAPPER.put(Identifier.withDefaultNamespace("constant"), Constant.MAP_CODEC);
/* 16 */     ID_MAPPER.put(Identifier.withDefaultNamespace("dye"), Dye.MAP_CODEC);
/* 17 */     ID_MAPPER.put(Identifier.withDefaultNamespace("grass"), GrassColorSource.MAP_CODEC);
/* 18 */     ID_MAPPER.put(Identifier.withDefaultNamespace("firework"), Firework.MAP_CODEC);
/* 19 */     ID_MAPPER.put(Identifier.withDefaultNamespace("potion"), Potion.MAP_CODEC);
/* 20 */     ID_MAPPER.put(Identifier.withDefaultNamespace("map_color"), MapColor.MAP_CODEC);
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("team"), TeamColor.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/ItemTintSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */