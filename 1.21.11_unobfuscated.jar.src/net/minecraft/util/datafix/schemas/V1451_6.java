/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class V1451_6
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public static final String SPECIAL_OBJECTIVE_MARKER = "_special";
/*     */   
/*     */   public V1451_6(int versionKey, Schema parent) {
/*  36 */     super(versionKey, parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/*  41 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*     */     
/*     */     Supplier<TypeTemplate> ITEM_STATS = () -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType()));
/*     */     
/*  45 */     schema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(new Pair[] { Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", ITEM_STATS.get()), Pair.of("minecraft:used", ITEM_STATS.get()), Pair.of("minecraft:broken", ITEM_STATS.get()), Pair.of("minecraft:picked_up", ITEM_STATS.get()), Pair.of("minecraft:dropped", ITEM_STATS.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))) })));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Map<String, Supplier<TypeTemplate>> criterionTypes = createCriterionTypes(schema);
/*  60 */     schema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", (TypeTemplate)DSL.taggedChoiceLazy("type", DSL.string(), criterionTypes), "DisplayName", References.TEXT_COMPONENT.in(schema)), UNPACK_OBJECTIVE_ID, REPACK_OBJECTIVE_ID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Map<String, Supplier<TypeTemplate>> createCriterionTypes(Schema schema) {
/*     */     Supplier<TypeTemplate> itemCriterion = () -> DSL.optionalFields("id", References.ITEM_NAME.in(schema));
/*     */     Supplier<TypeTemplate> blockCriterion = () -> DSL.optionalFields("id", References.BLOCK_NAME.in(schema));
/*     */     Supplier<TypeTemplate> entityCriterion = () -> DSL.optionalFields("id", References.ENTITY_NAME.in(schema));
/*  74 */     Map<String, Supplier<TypeTemplate>> criterionTypes = Maps.newHashMap();
/*  75 */     criterionTypes.put("minecraft:mined", blockCriterion);
/*     */     
/*  77 */     criterionTypes.put("minecraft:crafted", itemCriterion);
/*  78 */     criterionTypes.put("minecraft:used", itemCriterion);
/*  79 */     criterionTypes.put("minecraft:broken", itemCriterion);
/*  80 */     criterionTypes.put("minecraft:picked_up", itemCriterion);
/*  81 */     criterionTypes.put("minecraft:dropped", itemCriterion);
/*     */     
/*  83 */     criterionTypes.put("minecraft:killed", entityCriterion);
/*  84 */     criterionTypes.put("minecraft:killed_by", entityCriterion);
/*     */     
/*  86 */     criterionTypes.put("minecraft:custom", () -> DSL.optionalFields("id", DSL.constType(namespacedString())));
/*     */     
/*  88 */     criterionTypes.put("_special", () -> DSL.optionalFields("id", DSL.constType(DSL.string())));
/*  89 */     return criterionTypes;
/*     */   }
/*     */   
/*  92 */   protected static final Hook.HookFunction UNPACK_OBJECTIVE_ID = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> ops, T value) {
/*  95 */         Dynamic<T> input = new Dynamic(ops, value);
/*     */         
/*  97 */         return (T)((Dynamic)DataFixUtils.orElse(
/*  98 */             input.get("CriteriaName").asString().result()
/*  99 */             .map(name -> {
/*     */                 int colonPos = name.indexOf(':');
/*     */                 
/*     */                 if (colonPos < 0) {
/*     */                   return Pair.of("_special", name);
/*     */                 }
/*     */                 try {
/*     */                   Identifier statType = Identifier.bySeparator(name.substring(0, colonPos), '.'), statId = Identifier.bySeparator(name.substring(colonPos + 1), '.');
/*     */                   return Pair.of(statType.toString(), statId.toString());
/* 108 */                 } catch (Exception e) {
/*     */                   
/*     */                   return Pair.of("_special", name);
/*     */                 } 
/* 112 */               }).map(explodedId -> input.set("CriteriaType", input.createMap((Map)ImmutableMap.of(input.createString("type"), input.createString((String)explodedId.getFirst()), input.createString("id"), input.createString((String)explodedId.getSecond()))))), input))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 121 */           .getValue();
/*     */       }
/*     */     };
/*     */   
/*     */   public static String packNamespacedWithDot(String location) {
/* 126 */     Identifier parsedLoc = Identifier.tryParse(location);
/* 127 */     return (parsedLoc != null) ? (parsedLoc.getNamespace() + "." + parsedLoc.getNamespace()) : location;
/*     */   }
/*     */   
/* 130 */   protected static final Hook.HookFunction REPACK_OBJECTIVE_ID = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> ops, T value) {
/* 133 */         Dynamic<T> input = new Dynamic(ops, value);
/*     */         
/* 135 */         Optional<Dynamic<T>> repackedId = input.get("CriteriaType").get().result().flatMap(type -> {
/*     */               Optional<String> statType = type.get("type").asString().result(), statId = type.get("id").asString().result();
/*     */ 
/*     */               
/*     */               if (statType.isPresent() && statId.isPresent()) {
/*     */                 String unpackedType = statType.get();
/*     */ 
/*     */                 
/*     */                 return unpackedType.equals("_special") ? Optional.of(input.createString(statId.get())) : Optional.of(type.createString(V1451_6.packNamespacedWithDot(unpackedType) + ":" + V1451_6.packNamespacedWithDot(unpackedType)));
/*     */               } 
/*     */               
/*     */               return Optional.empty();
/*     */             });
/*     */         
/* 149 */         return (T)((Dynamic)DataFixUtils.orElse(repackedId.map(id -> input.set("CriteriaName", id).remove("CriteriaType")), input)).getValue();
/*     */       }
/*     */     };
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V1451_6.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */