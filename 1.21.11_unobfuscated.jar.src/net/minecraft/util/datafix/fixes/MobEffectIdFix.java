/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class MobEffectIdFix extends DataFix {
/*     */   static {
/*  24 */     ID_MAP = (Int2ObjectMap<String>)Util.make(new Int2ObjectOpenHashMap(), m -> {
/*     */           m.put(1, "minecraft:speed");
/*     */           m.put(2, "minecraft:slowness");
/*     */           m.put(3, "minecraft:haste");
/*     */           m.put(4, "minecraft:mining_fatigue");
/*     */           m.put(5, "minecraft:strength");
/*     */           m.put(6, "minecraft:instant_health");
/*     */           m.put(7, "minecraft:instant_damage");
/*     */           m.put(8, "minecraft:jump_boost");
/*     */           m.put(9, "minecraft:nausea");
/*     */           m.put(10, "minecraft:regeneration");
/*     */           m.put(11, "minecraft:resistance");
/*     */           m.put(12, "minecraft:fire_resistance");
/*     */           m.put(13, "minecraft:water_breathing");
/*     */           m.put(14, "minecraft:invisibility");
/*     */           m.put(15, "minecraft:blindness");
/*     */           m.put(16, "minecraft:night_vision");
/*     */           m.put(17, "minecraft:hunger");
/*     */           m.put(18, "minecraft:weakness");
/*     */           m.put(19, "minecraft:poison");
/*     */           m.put(20, "minecraft:wither");
/*     */           m.put(21, "minecraft:health_boost");
/*     */           m.put(22, "minecraft:absorption");
/*     */           m.put(23, "minecraft:saturation");
/*     */           m.put(24, "minecraft:glowing");
/*     */           m.put(25, "minecraft:levitation");
/*     */           m.put(26, "minecraft:luck");
/*     */           m.put(27, "minecraft:unluck");
/*     */           m.put(28, "minecraft:slow_falling");
/*     */           m.put(29, "minecraft:conduit_power");
/*     */           m.put(30, "minecraft:dolphins_grace");
/*     */           m.put(31, "minecraft:bad_omen");
/*     */           m.put(32, "minecraft:hero_of_the_village");
/*     */           m.put(33, "minecraft:darkness");
/*     */         });
/*     */   }
/*  60 */   private static final Set<String> MOB_EFFECT_INSTANCE_CARRIER_ITEMS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*     */ 
/*     */   
/*     */   private static final Int2ObjectMap<String> ID_MAP;
/*     */ 
/*     */ 
/*     */   
/*     */   public MobEffectIdFix(Schema outputSchema) {
/*  68 */     super(outputSchema, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Optional<Dynamic<T>> getAndConvertMobEffectId(Dynamic<T> obj, String fieldName) {
/*  77 */     Objects.requireNonNull(obj); return obj.get(fieldName).asNumber().result().map(id -> (String)ID_MAP.get(id.intValue())).map(obj::createString);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectIdField(Dynamic<T> input, String oldFieldName, Dynamic<T> output, String newFieldName) {
/*  81 */     Optional<Dynamic<T>> mappedId = getAndConvertMobEffectId(input, oldFieldName);
/*  82 */     return output.replaceField(oldFieldName, newFieldName, mappedId);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectIdField(Dynamic<T> input, String oldFieldName, String newFieldName) {
/*  86 */     return updateMobEffectIdField(input, oldFieldName, input, newFieldName);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectInstance(Dynamic<T> input) {
/*  90 */     input = updateMobEffectIdField(input, "Id", "id");
/*  91 */     input = input.renameField("Ambient", "ambient");
/*  92 */     input = input.renameField("Amplifier", "amplifier");
/*  93 */     input = input.renameField("Duration", "duration");
/*  94 */     input = input.renameField("ShowParticles", "show_particles");
/*  95 */     input = input.renameField("ShowIcon", "show_icon");
/*     */     
/*  97 */     Optional<Dynamic<T>> hiddenEffect = input.get("HiddenEffect").result().map(MobEffectIdFix::updateMobEffectInstance);
/*  98 */     return input.replaceField("HiddenEffect", "hidden_effect", hiddenEffect);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectInstanceList(Dynamic<T> input, String oldField, String newField) {
/* 102 */     Optional<Dynamic<T>> newValue = input.get(oldField).asStreamOpt().result().map(effects -> input.createList(effects.map(MobEffectIdFix::updateMobEffectInstance)));
/* 103 */     return input.replaceField(oldField, newField, newValue);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateSuspiciousStewEntry(Dynamic<T> input, Dynamic<T> output) {
/* 107 */     output = updateMobEffectIdField(input, "EffectId", output, "id");
/*     */     
/* 109 */     Optional<Dynamic<T>> duration = input.get("EffectDuration").result();
/* 110 */     return output.replaceField("EffectDuration", "duration", duration);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateSuspiciousStewEntry(Dynamic<T> input) {
/* 114 */     return updateSuspiciousStewEntry(input, input);
/*     */   }
/*     */   
/*     */   private Typed<?> updateNamedChoice(Typed<?> input, DSL.TypeReference typeReference, String name, Function<Dynamic<?>, Dynamic<?>> function) {
/* 118 */     Type<?> oldType = getInputSchema().getChoiceType(typeReference, name);
/* 119 */     Type<?> newType = getOutputSchema().getChoiceType(typeReference, name);
/* 120 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, typedTag -> typedTag.update(DSL.remainderFinder(), function));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule blockEntityFixer() {
/* 124 */     Type<?> blockEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 125 */     return fixTypeEverywhereTyped("BlockEntityMobEffectIdFix", blockEntityType, input -> updateNamedChoice(input, References.BLOCK_ENTITY, "minecraft:beacon", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixMooshroomTag(Dynamic<T> entityTag) {
/* 135 */     Dynamic<T> initialEntry = entityTag.emptyMap();
/* 136 */     Dynamic<T> entry = updateSuspiciousStewEntry(entityTag, initialEntry);
/*     */     
/* 138 */     if (!entry.equals(initialEntry)) {
/* 139 */       entityTag = entityTag.set("stew_effects", entityTag.createList(Stream.of(entry)));
/*     */     }
/* 141 */     return entityTag.remove("EffectId").remove("EffectDuration");
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixArrowTag(Dynamic<T> data) {
/* 145 */     return updateMobEffectInstanceList(data, "CustomPotionEffects", "custom_potion_effects");
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixAreaEffectCloudTag(Dynamic<T> data) {
/* 149 */     return updateMobEffectInstanceList(data, "Effects", "effects");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateLivingEntityTag(Dynamic<?> data) {
/* 154 */     return updateMobEffectInstanceList(data, "ActiveEffects", "active_effects");
/*     */   }
/*     */   
/*     */   private TypeRewriteRule entityFixer() {
/* 158 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/* 159 */     return fixTypeEverywhereTyped("EntityMobEffectIdFix", entityType, input -> {
/*     */           input = updateNamedChoice(input, References.ENTITY, "minecraft:mooshroom", MobEffectIdFix::fixMooshroomTag);
/*     */           input = updateNamedChoice(input, References.ENTITY, "minecraft:arrow", MobEffectIdFix::fixArrowTag);
/*     */           input = updateNamedChoice(input, References.ENTITY, "minecraft:area_effect_cloud", MobEffectIdFix::fixAreaEffectCloudTag);
/*     */           return input.update(DSL.remainderFinder(), MobEffectIdFix::updateLivingEntityTag);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeRewriteRule playerFixer() {
/* 169 */     Type<?> playerType = getInputSchema().getType(References.PLAYER);
/* 170 */     return fixTypeEverywhereTyped("PlayerMobEffectIdFix", playerType, input -> input.update(DSL.remainderFinder(), MobEffectIdFix::updateLivingEntityTag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixSuspiciousStewTag(Dynamic<T> tag) {
/* 177 */     Optional<Dynamic<T>> effectsList = tag.get("Effects")
/* 178 */       .asStreamOpt().result()
/* 179 */       .map(list -> tag.createList(list.map(MobEffectIdFix::updateSuspiciousStewEntry)));
/*     */     
/* 181 */     return tag.replaceField("Effects", "effects", effectsList);
/*     */   }
/*     */   
/*     */   private TypeRewriteRule itemStackFixer() {
/* 185 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*     */     
/* 187 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 188 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 189 */     return fixTypeEverywhereTyped("ItemStackMobEffectIdFix", itemStackType, input -> {
/*     */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/*     */           if (idOpt.isPresent()) {
/*     */             String id = (String)((Pair)idOpt.get()).getSecond();
/*     */             if (id.equals("minecraft:suspicious_stew")) {
/*     */               return input.updateTyped(tagF, ());
/*     */             }
/*     */             if (MOB_EFFECT_INSTANCE_CARRIER_ITEMS.contains(id)) {
/*     */               return input.updateTyped(tagF, ());
/*     */             }
/*     */           } 
/*     */           return input;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 206 */     return TypeRewriteRule.seq(
/* 207 */         blockEntityFixer(), new TypeRewriteRule[] {
/* 208 */           entityFixer(), 
/* 209 */           playerFixer(), 
/* 210 */           itemStackFixer()
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/MobEffectIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */