/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.Util;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class WorldGenSettingsHeightAndBiomeFix extends DataFix {
/*    */   private static final String NAME = "WorldGenSettingsHeightAndBiomeFix";
/*    */   
/*    */   public WorldGenSettingsHeightAndBiomeFix(Schema outputSchema) {
/* 22 */     super(outputSchema, true);
/*    */   }
/*    */   public static final String WAS_PREVIOUSLY_INCREASED_KEY = "has_increased_height_already";
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> worldGenSettingsType = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 28 */     OpticFinder<?> dimensionsFinder = worldGenSettingsType.findField("dimensions");
/*    */     
/* 30 */     Type<?> worldGenSettingsTypeNew = getOutputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 31 */     Type<?> dimensionsType = worldGenSettingsTypeNew.findFieldType("dimensions");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("WorldGenSettingsHeightAndBiomeFix", worldGenSettingsType, worldGenSettingsTypeNew, input -> {
/*    */           OptionalDynamic<?> wasIncreasedOpt = ((Dynamic)input.get(DSL.remainderFinder())).get("has_increased_height_already");
/*    */           boolean wasExpSnap = wasIncreasedOpt.result().isEmpty(), wasPreviouslyIncreased = wasIncreasedOpt.asBoolean(true);
/*    */           return input.update(DSL.remainderFinder(), ()).updateTyped(dimensionsFinder, dimensionsType, ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updateLayers(Dynamic<?> layers) {
/* 83 */     Dynamic<?> airLayer = layers.createMap((Map)ImmutableMap.of(
/* 84 */           layers.createString("height"), 
/* 85 */           layers.createInt(64), 
/* 86 */           layers.createString("block"), 
/* 87 */           layers.createString("minecraft:air")));
/*    */     
/* 89 */     return layers.createList(Stream.concat(Stream.of(airLayer), layers.asStream()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WorldGenSettingsHeightAndBiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */