/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class MapBannerBlockPosFormatFix extends com.mojang.datafixers.DataFix {
/*    */   public MapBannerBlockPosFormatFix(Schema outputSchema) {
/* 14 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type<?> type = getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
/* 20 */     OpticFinder<?> dataF = type.findField("data");
/* 21 */     OpticFinder<?> bannersF = dataF.type().findField("banners");
/* 22 */     OpticFinder<?> bannerF = DSL.typeFinder(((List.ListType)bannersF.type()).getElement());
/* 23 */     return fixTypeEverywhereTyped("MapBannerBlockPosFormatFix", type, input -> input.updateTyped(dataF, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/MapBannerBlockPosFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */