/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ReorganizePoi
/*    */   extends DataFix {
/*    */   public ReorganizePoi(Schema outputSchema, boolean changesType) {
/* 21 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type<Pair<String, Dynamic<?>>> poiChunkType = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 28 */     if (!Objects.equals(poiChunkType, getInputSchema().getType(References.POI_CHUNK))) {
/* 29 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 31 */     return fixTypeEverywhere("POI reorganization", poiChunkType, ops -> ());
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> cap(Dynamic<T> input) {
/* 35 */     Map<Dynamic<T>, Dynamic<T>> sections = Maps.newHashMap();
/* 36 */     for (int i = 0; i < 16; i++) {
/* 37 */       String key = String.valueOf(i);
/* 38 */       Optional<Dynamic<T>> section = input.get(key).result();
/* 39 */       if (section.isPresent()) {
/* 40 */         Dynamic<T> sectionRecords = section.get();
/* 41 */         Dynamic<T> newSection = input.createMap((Map)ImmutableMap.of(input.createString("Records"), sectionRecords));
/* 42 */         sections.put(input.createString(Integer.toString(i)), newSection);
/* 43 */         input = input.remove(key);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return input.set("Sections", input.createMap(sections));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ReorganizePoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */