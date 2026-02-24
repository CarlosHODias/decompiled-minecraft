/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class BlockPosFormatAndRenamesFix
/*    */   extends DataFix {
/* 18 */   private static final List<String> PATROLLING_MOBS = List.of("minecraft:witch", "minecraft:ravager", "minecraft:pillager", "minecraft:illusioner", "minecraft:evoker", "minecraft:vindicator");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPosFormatAndRenamesFix(Schema outputSchema) {
/* 28 */     super(outputSchema, true);
/*    */   }
/*    */   
/*    */   private Typed<?> fixFields(Typed<?> typed, Map<String, String> fields) {
/* 32 */     return typed.update(DSL.remainderFinder(), tag -> {
/*    */           for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)fields.entrySet()) {
/*    */             tag = tag.renameAndFixField(entry.getKey(), entry.getValue(), ExtraDataFixUtils::fixBlockPos);
/*    */           }
/*    */           return tag;
/*    */         });
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixMapSavedData(Dynamic<T> data) {
/* 41 */     return 
/* 42 */       data.update("frames", frames -> frames.createList(frames.asStream().map(())))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 48 */       .update("banners", banners -> banners.createList(banners.asStream().map(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 59 */     List<TypeRewriteRule> rules = new ArrayList<>();
/* 60 */     addEntityRules(rules);
/* 61 */     addBlockEntityRules(rules);
/*    */     
/* 63 */     rules.add(writeFixAndRead("BlockPos format for map frames", getInputSchema().getType(References.SAVED_DATA_MAP_DATA), getOutputSchema().getType(References.SAVED_DATA_MAP_DATA), input -> input.update("data", this::fixMapSavedData)));
/*    */ 
/*    */ 
/*    */     
/* 67 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 68 */     rules.add(fixTypeEverywhereTyped("BlockPos format for compass target", itemStackType, ItemStackTagFix.createFixer(itemStackType, "minecraft:compass"::equals, typed -> typed.update(DSL.remainderFinder(), ()))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 73 */     return TypeRewriteRule.seq(rules);
/*    */   }
/*    */   
/*    */   private void addEntityRules(List<TypeRewriteRule> rules) {
/* 77 */     rules.add(createEntityFixer(References.ENTITY, "minecraft:bee", Map.of("HivePos", "hive_pos", "FlowerPos", "flower_pos")));
/*    */ 
/*    */ 
/*    */     
/* 81 */     rules.add(createEntityFixer(References.ENTITY, "minecraft:end_crystal", Map.of("BeamTarget", "beam_target")));
/* 82 */     rules.add(createEntityFixer(References.ENTITY, "minecraft:wandering_trader", Map.of("WanderTarget", "wander_target")));
/* 83 */     for (String patrollingMob : PATROLLING_MOBS) {
/* 84 */       rules.add(createEntityFixer(References.ENTITY, patrollingMob, Map.of("PatrolTarget", "patrol_target")));
/*    */     }
/* 86 */     rules.add(fixTypeEverywhereTyped("BlockPos format in Leash for mobs", getInputSchema().getType(References.ENTITY), input -> input.update(DSL.remainderFinder(), ())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void addBlockEntityRules(List<TypeRewriteRule> rules) {
/* 92 */     rules.add(createEntityFixer(References.BLOCK_ENTITY, "minecraft:beehive", Map.of("FlowerPos", "flower_pos")));
/* 93 */     rules.add(createEntityFixer(References.BLOCK_ENTITY, "minecraft:end_gateway", Map.of("ExitPortal", "exit_portal")));
/*    */   }
/*    */   
/*    */   private TypeRewriteRule createEntityFixer(DSL.TypeReference type, String entityName, Map<String, String> fields) {
/* 97 */     String name = "BlockPos format in " + String.valueOf(fields.keySet()) + " for " + entityName + " (" + type.typeName() + ")";
/* 98 */     OpticFinder<?> entityF = DSL.namedChoice(entityName, getInputSchema().getChoiceType(type, entityName));
/* 99 */     return fixTypeEverywhereTyped(name, getInputSchema().getType(type), input -> fields.updateTyped(entityF, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockPosFormatAndRenamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */