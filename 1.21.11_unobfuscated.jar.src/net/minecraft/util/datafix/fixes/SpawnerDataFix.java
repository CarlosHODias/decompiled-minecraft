/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SpawnerDataFix
/*    */   extends DataFix {
/*    */   public SpawnerDataFix(Schema outputSchema) {
/* 17 */     super(outputSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type<?> oldType = getInputSchema().getType(References.UNTAGGED_SPAWNER);
/* 23 */     Type<?> newType = getOutputSchema().getType(References.UNTAGGED_SPAWNER);
/*    */     
/* 25 */     OpticFinder<?> spawnDataFinder = oldType.findField("SpawnData");
/* 26 */     Type<?> newSpawnDataType = newType.findField("SpawnData").type();
/*    */     
/* 28 */     OpticFinder<?> spawnPotentialsFinder = oldType.findField("SpawnPotentials");
/* 29 */     Type<?> newSpawnPotentialsType = newType.findField("SpawnPotentials").type();
/*    */     
/* 31 */     return fixTypeEverywhereTyped("Fix mob spawner data structure", oldType, newType, spawner -> newSpawnPotentialsType.updateTyped(spawnDataFinder, spawnDataFinder, ()).updateTyped(newSpawnDataType, spawnPotentialsFinder, ()));
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
/*    */   private <T> Typed<T> wrapEntityToSpawnData(Type<T> newType, Typed<?> spawnData) {
/* 44 */     DynamicOps<?> ops = spawnData.getOps();
/*    */     
/* 46 */     return new Typed(newType, ops, Pair.of(spawnData.getValue(), new Dynamic(ops)));
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> Typed<T> wrapSpawnPotentialsToWeightedEntries(Type<T> newType, Typed<?> spawnPotentials) {
/* 51 */     DynamicOps<?> ops = spawnPotentials.getOps();
/* 52 */     List<?> entries = (List)spawnPotentials.getValue();
/* 53 */     List<?> wrappedEntries = entries.stream().map(o -> {
/*    */           Pair<Object, Dynamic<?>> entry = (Pair<Object, Dynamic<?>>)o;
/*    */           
/*    */           int weight = ((Number)((Dynamic)entry.getSecond()).get("Weight").asNumber().result().orElse(1)).intValue();
/*    */           Dynamic<?> newEntryRemainder = new Dynamic(ops);
/*    */           newEntryRemainder = newEntryRemainder.set("weight", newEntryRemainder.createInt(weight));
/*    */           Dynamic<?> newInnerRemainder = ((Dynamic)entry.getSecond()).remove("Weight").remove("Entity");
/*    */           return Pair.of(Pair.of(entry.getFirst(), newInnerRemainder), newEntryRemainder);
/* 61 */         }).toList();
/* 62 */     return new Typed(newType, ops, wrappedEntries);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/SpawnerDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */