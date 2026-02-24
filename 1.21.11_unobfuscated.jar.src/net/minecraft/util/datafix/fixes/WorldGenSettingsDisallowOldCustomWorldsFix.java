/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.NbtFormatException;
/*    */ 
/*    */ public class WorldGenSettingsDisallowOldCustomWorldsFix extends com.mojang.datafixers.DataFix {
/*    */   public WorldGenSettingsDisallowOldCustomWorldsFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected com.mojang.datafixers.TypeRewriteRule makeRule() {
/* 17 */     Type<?> worldGenSettingsType = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 18 */     OpticFinder<?> dimensionsFinder = worldGenSettingsType.findField("dimensions");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("WorldGenSettingsDisallowOldCustomWorldsFix_" + getOutputSchema().getVersionKey(), worldGenSettingsType, input -> input.updateTyped(dimensionsFinder, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WorldGenSettingsDisallowOldCustomWorldsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */